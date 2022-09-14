## 프로젝트 환경설정
- 스프링부트 thymeleaf viewName 매핑
	- `resources:templates/` + {ViewName} + `.html`
- springboot devtools
	- `build.gradle`에서 `implementation 'org.springframework.boot:spring-boot-devtools'`
	- Ctrl + Shift + F9로 변경된 리소스만 Recompile해서 변동 반영 가능해진다.

### JPA, DB설정
```java
spring:  
  datasource:  
//  url: jdbc:h2:tcp://localhost/~/jpashop;MVCC=TRUE  
    url: jdbc:h2:tcp://localhost/~/jpashop;
    username: sa  
    password:  
    driver-class-name: org.h2.Driver  
  
  jpa:  
    hibernate:  
      ddl-auto: create  
    properties:  
      hibernate:  
        show_sql: true  
        format_sql: true  
          
logging:  
  level:  
    org.hibernate.SQL: debug
```
- `application.yml`의 설정 데이터이다. (`application.properties`와 둘 중 하나만 사용하면 된다.)
- 위와 같은 설정에 대해 배우려면 <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/">스프링 부트 매뉴얼을 참조하라</a>: 

- `@Transactional`이 스프링  테스트에 있다면, 테스트 끝난 후 트랜젝션을 롤백 한다.
	- `@Rollback(false)`로 롤백 막을 수 있다.

```java
@Test  
@Transactional  
@Rollback(false)  
@DisplayName("회원 저장 및 조회")  
void testMember() {  
    //given  
  Member member = new Member();  
  member.setUserName("memberA");  
  
  //when  
  Long savedId = memberRepository.save(member);  
  Member foundMember = memberRepository.find(member.getId());  
  
  //then  
  assertThat(foundMember.getId()).isEqualTo(member.getId());  
  assertThat(foundMember.getUserName()).isEqualTo(member.getUserName());  
  assertThat(foundMember).isEqualTo(member);  
}
```
- 마지막 assertThat이 true인 이유: 같은 영속성 컨텍스트에서 동일 식별자(ID)면 같은 엔티티로 식별하기 때문
- 쿼리 파라미터 로그 남기기: <a href="https://github.com/gavlyukovskiy/spring-boot-data-source-decorator">spring-boot-data-source-decorator</a> 중 P6SPY를 사용하는 경우 `implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6'` 를 `build.gradle`에 추가

- application.yml에서
```java
dependencies {  
  implementation 'org.springframework.boot:spring-boot-starter-data-jpa'  
  implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'  
  implementation 'org.springframework.boot:spring-boot-starter-validation'  
  implementation 'org.springframework.boot:spring-boot-starter-web'  
  implementation 'org.springframework.boot:spring-boot-devtools'  
  
  implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6'
```
- 버전을 표기하지 않는 것은 스프링부트가 맞는 버전을 알아서 찾아주기 때문, 지원하지 않는 경우 명시

## 도메인 분석 설계
### 도메인 모델과 테이블 설계
- 객체 A, B가 서로의 컬렉션(List 등)을 가지고 있을 수는 있지만, 관계형 DB에서는 일반적인 방법으로 이것이 불가능 -> 매핑 테이블로 n:m관계를 1:n, m:1관계로 풀어냄
- 양방향 관계에서 연관관계의 주인은 외래키가 있는 쪽 1;n에서 n이 외래키가 있는 쪽이다.
- 1:1 관계면 외래 키를 어디에 매핑해도 된다.
- 관계형 DB 연관관계의 주인은 외래 키를 누가 관리하느냐는 문제다. 이는 비즈니스상 우위와는 다르다.
- 주인 설정을 이렇게 하는 까닭은 관리하지 않는 외래 키 값이 업데이트 되어 관리와 유지보수가 어렵고, 별도의 업데이트 쿼리를 발생시키는 성능 문제도 있다.

### 엔티티 클래스 개발
- 실무에서는 `Getter`만 붙이고 `Setter`는 필요할 때마다 사용하는 편이 낫다.
- 더 이상적으로는, 둘 다 제공하지 않고 별도의 메서드를 제공하는 편이 낫다.
- `Setter`의 경우 데이터를 건드리기 때문에 엔티티 변경을 추적하기 힘들다. 엔티티 변경 시에는 `Setter` 말고 변경 지점을 명확하게 하는 변경을 위한 비즈니스 메서드를 제공하라.
- 양방향 참조가 발생할 경우 어디를 기준해야 하나? -> 어느 한 쪽을 정해야 한다. 객체의 경우 변경 포인트가 두 군데이지만, FK는 변경을 FK소유 쪽에서만 하면 되니까. (연관관계의 주인)
```java
@OneToMany(mappedBy = "member")  
private List<Order> orders = new ArrayList<>();
```
- order 테이블에 있는 member 필드에 의해서 매핑되었음을 의미

```java
@Enumerated(EnumType.ORDINAL)  
private DeliveryStatus status; // READY< COMP
```
- `EnumType.ORDINAL`: 순서대로 번호 매긴다: READY(0), COMP(1) 그런데 만일 READY, NEW, COMP 식으로 사이에 들어가게 되면, COMP(2) 식으로 번호 변경이 발생해버린다. 따라서
- `EnumType.STRING` 사용한다.
- `@OneToOne` 관계의 경우 어디에 FK를 놓을 지 고민해야 한다. FK를 더 access가 많은 쪽에 놓는 경우
- 정확성이 중요하다면 FK 걸어주는 것이 나음, 상황에 따라 다름
- `ManyToMany` 실무에서 사용은 지양한다.
- 값 타입은 변경 불가능하게 설계하라.
	- `@Setter` 없이 생성자에서만 값을 초기화해 변경 불가능한 클래스를 만들어라. JPA스펙상 Entity나 Embeddable은 기본 생성자를 public이나 protected로 설정하라. 그나마 protected

### 엔티티 설계 주의점
- 가급적 Setter 사용하지 말 것
- **모든 연관관계는 지연로딩으로 설정하라!**
	- 즉석로딩`EAGER` 예측 및 추적이 어려움, JPQL실행시 N+1문제 빈번
		- 가령 order의 member를 EAGER로 하면 JPQL은 다음과 같이 동작한다:
		`JPQL select o From order o; -> SQL select * from order
	- 지연로딩`LAZY`사용할 것.
	- 연관 엔티티를 함께 조회해야 하면 fetch join 또는 엔티티 그래프를 사용
	- `@XToOne` 관계는 기본이 즉시로딩이기에 직접 지연로딩으로 변경해야.
- 초기화 방식: 아래 방식으로 -  **필드에서 초기화하라!**
```java
@OneToMany(mappedBy = "member")  
private List<Order> orders = new ArrayList<>();
```
- NPE가 발생하지 않는다.
- 하이버네이트가 엔티티를 영속화할 때, 이 컬렉션을 하이버네이트가 관리하는 내장 컬렉션으로 변경(ArrayList가 아니라 다른 무언가로)
	- 영속화 이후 수정이 발생하면(set) 하이버네이트가 관리할 수 없는 형태로 변경된다. 처음 객체 생성 이후에 건들지 않으면 하이버네이트 내부 메커니즘에 이상을 주지 않다.
- **테이블 컬럼명 생성 전략**
	- 하이버네이트 기본 구현: 엔티티의 필드명을 그대로 테이블 명으로 사용 `SpringPhysicalNamingStrategy`
	- 스프링 부트를 통한 변경된 설정(엔티티(필드) -> 테이블(컬럼)로 변경)
		1. 카멜 케이스 -> 언더스코어(memberPoint -> member_point)
		2. .(점) -> _(언더스코어)
		3. 대문자 -> 소문자
- cascade
	- persist orderItemA, persist orderItemB . . . 대신에
	- persist order 형태로

```java
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)  
private List<OrderItem> orderItems = new ArrayList<>();
```
- 연관관계 (편의) 메서드
```java
public void setMember(Member member) {  
    this.member = member;  
  member.getOrders().add(this);  
}  
  
public void addOrderItem(OrderItem orderItem) {  
    orderItems.add(orderItem);  
  orderItem.setOrder(this);  
}
```

## 회원 도메인 개발
### 회원 리포지토리 개발
- SQL과 JPQL의 차이
	- SQL: 테이블을 대상으로 쿼리
	- JPQL: 엔티티 객체를 대상으로 쿼리
- 스프링은 `@PersistenceContext`이 있으면 엔티티 매니저를 관리

### 회원 서비스 개발
- em.persist시에 영속성 컨텍스트에 멤버 객체를 올림. ID값이 키(PK Value) -> persist한 시점에서 항상 ID가 있음이 보장 된다.
- 테스트에서 persist가 이루어진다고 해서 바로 DB에 insert가 나가진 않는다. (예외가 있긴 함, @GeneratedValue 전략 사용시에는 더 안 나감)
	- DB 트랜젝션이 커밋 될 때 DB에 쿼리가 나가면서 insert문
	- 스프링에서 @Transactional은 기본적으로 커밋 하지 않고 롤백함
	- @Rollback(false)시 커밋됨
	- 혹은 em.flush 사용 시 insert 후 롤백
- 스프링 부트 사용시 가능한 메모리 DB를 이용한 테스트
	- test 패키지 내에 resources 만들고, application yml을 새로 만든다. (테스트의 경우 test 경로 내에서 우선 탐색)
	- spring의 datasource의url: jdbc:h2:mem:test로 변경한다.
	- http://www.h2database.com/html/cheatSheet.html 참조
```java
spring:  
#  datasource:  
#    url: jdbc:h2:mem:test  
#    username: sa  
#    password:  
#    driver-class-name: org.h2.Driver  
#  
#  jpa:  
#    hibernate:  
#      ddl-auto: create  
#    properties:  
#      hibernate:  
#        show_sql: true  
#        format_sql: true  
  
logging:  
  level:  
    org.hibernate.SQL: debug  
    org.hibernate.type: trace
```
- 이렇게 지우더라도, 스프링부트 기본설정상 메모리 모드로 실행한다.
- 테스트 시의 application.yml을 별도로 설정해주는 편이 좋다.
- create-drop: 시작시 create, 마지막에 drop으로 자원 정리

## 상품 도메인 개발
- 도메인 주도 설계, 객체 지향 설계상
	- 그 데이터를 가지고 있는 곳에 비즈니스 로직이 있는 것이 응집도가 높다.
	- stock증가, 감소 비즈니스 메서드를 만들고, setter를 제공하지 않는 편이 낫다.

## 주문 도메인 개발
- cascade 걸려있다면 별도의 save 없이 걸려있는 원래 엔티티 persist 될 때 같이 persist
- 생성자 막기
	- OrderItem 생성은 createOrderItem으로 사용했다면, 다른 방식으로 OrderItem을 생성하는 것(new)를 막을 필요가 있다.
		- OrderItem에 protected 생성자를 만들어서 어느 정도 제약이 가능하다. 
		- JPA는 protected까지 기본 생성자 만드는 것을 허용하기에 그렇다.
		- `@NoArgsConstructor(access = AccessLevel.PROTECTED)`이용하여 기본 생성자 만들 수 있다.
- 도메인 모델 패턴 vs 트랜젝션 스크립트 패턴
	- 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 도메인 모델 패턴이라고 한다.
	- 반대로 엔티티에는 비즈니스 로직이 없고 서비스 계층에서 대부분의 비즈니스 로직을 다루는 것을 트랜젝션 스크립트 패턴이라고 한다.
	- 일반적으로 sql을 다룰 때는 주로 트랜젝션 스크립트 패턴, 
	- JPA 등 ORM 사용시 주로 도메인 모델 패턴 사용한다.
- 도메인 모델 패턴 사용할 경우, repository 등과 무관하게 엔티티 단위로 테스트 작성할 수 있음 -> 단위테스트 용이

### 주문 검색 기능
- **JPA에서 동적 쿼리 해결**
	- jpql을 String 이어 작성하기
		- 번잡하고 실수하기 쉬움
		- 많은 line
	- Criteria사용하는 방식
		- jpa criteria: JPA표준 방식
		- 직관적으로 어떤 쿼리가 생성되는지 알기 힘듦
	- QueryDsl -> 대안

## 웹 계층 개발
- thymeleaf에서 header footer 등을 include 하는 방식은 중복을 발생시킨다. -><a href="https://www.thymeleaf.org/doc/articles/layouts.html">**Hierarchical-style layouts** 이용하라!</a>
- thymeleaf에서 *{name} 이런식이면, * -> object 통해 접근
```java
public class MemberForm {  
  
    @NotEmpty(message = "회원 이름은 필수입니다.")  
    private String name;
```
그리고
```java
@PostMapping("/members/new")  
public String create(@Valid MemberForm form, BindingResult result) {  
  
    if (result.hasErrors()) {  
        return "members/createMemberForm";  
   }
    ...
}
```
에서 @Valid로 validation 가능
- 에러 발생시 whiteLabel error가 아니라 세련된 방식으로 표현:
	- BindingResult -> 스프링이 에러 내용을 result에 담아주고, 코드를 실행함
```html
    <div class="form-group">  
 <label th:for="name">이름</label>  
 <input type="text" th:field="*{name}" class="form-control"  
  placeholder="이름을 입력하세요"  
  th:class="${#fields.hasErrors('name')}? 'form-control  
fieldError' : 'form-control'">  
 <p th:if="${#fields.hasErrors('name')}"  
  th:errors="*{name}">Incorrect date</p>  
 </div>
```
- {name} hasErrors시, form-control fieldError -> 폼 필드를 빨갛게 만듦
- ` <p th:if="${#fields.hasErrors('name')}" 
  th:errors="*{name}">Incorrect date</p>  ` -> name 필드에 에러가 있을 경우 name필드의 에러 메시지를 뽑아서 띄움
- if (result.hasErrors()) 라도, MemberForm form을 다시 전달하기에 입력한 값을 날리진 않는다.
- 왜 Member가 아니라 MemberForm을 썼나? -> 도메인과 컨트롤러의 Validation은 다를 수도 있고, 코드가 혼잡해짐. 엔티티에 화면 관련 기능이 너무 추가 서로 맞지 않음. 
- 너무 단순한 폼 화면이 아닌 이상 이럴 일은 없음 -> 실제로는 이렇게 한다!
	-> **엔티티는 최대한 순수하게 핵심 비즈니스 로직만!!!**, 유지보수를 위해서!
	-> 화면 관련은: 화면 api는 form 객체나 DTO(데이터 전송 객체) 사용하라!
	-**API 만들 때는 이유 불문, 엔티티를 웹으로 반환하지 마라!!!**, 템플릿 엔진의 경우 선택적 사용이 가능은 하다.
	- 가령 멤버 엔티티에 필드가 하나 추가되어 버리면, 정보가 노출되어 버리거나, api 스펙이 변환되어 버림
- modelSetter로 한번에 set 가능, intelij의 multi line select도 가능(Ctrl + Ctrl)
- 변경시에는
	- merge(병합) 말고 변경 감지를 쓰는 것이 best practice
- 실무에서는 url의 id를 바꿔서 접근하는 취약점이 있음 -> 서버에서 권한을 체크, 업데이트 시 세션 객체 이용 등

### 변경 감지와 병합
- **준영속 엔티티** 
	- 영속성 컨텍스트가 더 이상 관리하지 않는 엔티티
		- Item변경 예제에서, form을 통해 데이터 전달하는데 Book은 id까지 세팅되어있었음. JPA 식별 id도 가지고 있고, 과거에 식별자 기반으로 jpa가 관리도 했었음.
	- 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있음
	- 이것들은 JPA 관리 대상이 아니기에 DB 업데이트 안됨
	- 준영속엔티티를 수정하는 두 가지 방법
		1. 변경 감지
		2. 병합(merge)

- **변경 감지**
```java
public Item updateItem(Long itemId, Book bookParam) {  
    Item foundItem = itemRepository.findOne(itemId);  
  foundItem.setPrice(bookParam.getPrice());  
  foundItem.setName(bookParam.getName());  
  foundItem.setStockQuantity(bookParam.getStockQuantity());  
  return foundItem;
}
```
- 이 경우 foundItem은 findOne(itemId)를 통해 얻은 것(em.find)이기에 영속 상태임. 변경 이후 커밋 되면(transactional), flush 날려서 변경을 감지
	- 트랜젝션 안에서 변경, 커밋됨 -> JPA가 변경을 감지, em.update 등 없었음(Order관련 개발 참조) : 기본 메커니즘
	-> 추천 방식

- **병합**
	- 준영속 상태 엔티티를 영속 상태로 변경할 때 사용
	- 위의 변경 줄들을 JPA로 한 줄에 처리
	- merge로 리턴된 값은 영속성 컨텍스트 관리 대상이지만 파라미터 item은 아니다.
	- **주의사항**
		- 변경 감지시 원하는 속성만 선택 변경이 가능하지만
		- 병합시에는 모든 속성이 변경된다. (모든 필드를 교체)
		- **병합시 값이 없다면 null로 변경할 수도 없는 위험이 있다.**
			- 위의 예시에서는 "책 값은 변경 불가"인데 book.price가 null이라서 merge시 기존의 10,000이 null로 업데이트 되어 버릴수 있음.
			- **떄문에 merge 사용하여 업데이트 하지 말자, 변경 감지를 이용하자: 조회 후 set**
	
- **컨트롤러에서 어설프게 엔티티를 생성하지 말자**
	- 트랜젝션이 있는 서비스 계층에 식별자와 DTO나 파라미터를 전달하는 식으로 하라.
	- 트랜젝션이 서비스 계층에서 영속 상태의 엔티티를 조회, 데이터를 변경하여 변경 감지를 실행하라.
	- setter 쓰지 말고 change 등 메서드 따로 만들어라 -> 추적 용이
- 커맨드성인 것들은 컨트롤러에서는 식별자만 넘기고, 엔티티 안에서 구체적인 핵심 로직을 돌림.
	- 영속 상태에서 엔티티 존재한다는 장점. 만약 패러미터로 객체 넘기면 얘기 다름
