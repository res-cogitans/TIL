# 스프링 부트와 JPA 활용2 - API 개발과 성능 최적화

## API 개발 기본

### 회원 등록 API

- 템플릿 엔진 렌더링 컨트롤러와 API 스타일 컨트롤러 패키지를 분리한다.
  - 공통 예외처리시에 관점 차이가 있기 때문이다.

#### V1

```java
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}

```

- `@RestController`: `@Controller` + `@ResponseBody`
- `@RequestBody`로 Member 객체에 맞는 형식으로 데이터를 받을 수 있음
- `@Valid`로 인해서 제약 사항을 체크 가능
  - 예를 들어, Member 엔티티에서 name 필드를 `@NotEmpty`로 설정한다면, name이 null인지 검증한다.
  - 문제점
    - 프레젠테이션 계층을 위한 검증 로직이 엔티티 차원에 있음
      - 다른 계층에서는 위 제약사항이 필요 없을 수도 있음
    - name이 username으로 바꾸면 API 스펙 자체가 변경됨
      - 그런데 엔티티는 변동이 많다
  - 따라서
    - API 스펙을 위한 별도의 DTO가 필요하다.
    - 엔티티를 API 스펙으로 넘기지 말라. 파라미터로 받지도, 외부에 노출하지도 말라.



#### V2

```java
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
```

- V1에 비해서는 CreateMemberRequest라는 추가적인 클래스를 만든다는 단점이 있지만,
- 엔티티와 프레젠테이션 계층을 분리한다.
- API 스펙 변경 문제 없음
  - 엔티티 변경이 API 스펙을 변경시키지 않음
    - 예를들어, name 필드를 username 필드로 변경하더라도,
      기존의 getName() 메서드를 사용하는 부분을 IDE가 잡아주기 때문에 예기치 못한 스펙 불일치로 인한 문제 방지 가능
  - 또한 V1의 경우는 API 스펙 상으로 어떤 데이터를 받는가를 알기 위해서는 추가적으로 엔티티 코드 등을 뒤져봐야 하는데,
    - V2의 경우는 DTO만으로 바로 알 수 있게 된다.
    - Validation의 경우도 DTO에 표기하는 식으로 편하게 볼 수 있다.
      - V1의 경우 엔티티의 제약사항 등이 어떤 계층에는 사용되고, 어떤 계층에는 사용하지 않는 경우, 인식이 매우 어려워진다.

- 다시 한 번, **엔티티를 노출하지 마라!**



### 회원 수정 API

- RESTFUL API에서
  - 수정은 PUT
  - POST는 멱등한 경우

```java
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }
```

```java
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }
```



- 수정의 경우도 별도의 DTO를 사용했다.
  - 등록과 수정의 API 수정은 보통 다르기 때문이다. 수정의 경우 매우 제한적

- 이 경우 DTO를 InnerClass로 만들었다. 어차피 이 안에서만 사용할 것이기 때문이다.

- 커맨드와 쿼리는 분리하는 원칙
  - `return memberService.update(id)`식으로 사용하지 않는 이유!
  -  update는 변경성 메서드기에 추가적으로 쿼리하지는 않는다.
  - find를 별도로 사용해서 쿼리를 짜는 방식을 사용
  - 조회가 지나치게 많이 일어나는 경우가 아닌 이상 이로 인한 트래픽 양은 적기 때문에, 유지보수의 용이성을 위해서 이렇게 짠다!



### 회원 조회 API

- ddl-auto 옵션을 none으로 변경했다. 조회 테스트를 위해서, 한 번 들어간 데이터를 계속 쓸 수 있게 하기 위함이다.

#### V1

```java
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }
```

- 엔티티를 직접 노출했기에, 엔티티에 있는 정보 전체가 노출되게 됨

```java
public class Member {
    ...
	@JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}

```

- 위와 같이 Member를 수정해 준다면 orders는 빼고 데이터를 가져오게 됨.
- 그러나 이런 방식으로도 해결되지 않는 문제점들이 많다: 특히 회원조회와 관련된 API는 매우 다양할 것이며, 요구 데이터의 종류가 매우 많을 것이기 때문에
  - 엔티티에 그 사항을 일일히 반영하는 것은 힘들며
  - 엔티티에 화면(프레젠테이션 계층)에 관련된 로직이 들어가버렸다.
  - 또한 변경이 발생되면 API 스펙이 변경된다는 문제점이 있다.
  - 컬렉션을 직접 반환하면 향후 API 스펙을 변경하기 어렵다.

- 따라서 DTO를 이용하는 편이 낫다.



#### V2

```java
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDTO> collect = findMembers.stream()
                .map(m -> new MemberDTO(m.getName()))
                .collect(Collectors.toList());
        
        return new Result(collect);
    }
```

```java
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO {
        private String name;
    }
```

- 엔티티의 변경이 API 스펙에 영향을 주지 않는다.
- API 스펙과 노출할 데이터(DTO)가 1:1 대응한다. 
- 어떤 데이터를 쓰는지 명확하게 드러난다.
- 유지보수가 용이하다.



## API 개발 고급 - 지연 로딩과 조회 성능 최적화

- 등록과 수정은 보통 성능 문제가 발생하지 않는다.

- 주로 문제가 되는 것은 조회다.



### 간단한 주문 조회 V1: 엔티티를 직접 노출

```java
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }
```

- 양방향 연관관계 사이에서 무한 참조가 발생: StackOverFlow

  - Order에서 Member를 찾고, 그 Member에서 다시 Order를 찾는 반복이 발생한다.

  - 1차적 해결: 양방향 중 한 쪽에는 `@JsonIgnore`어노테이션을 붙인다.

- 그럼에도, Type definition error 발생

  - `[simple type, class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor]`
  - 위는 Order의 member 필드가 LAZY 로딩으로 되어 있기에 생성된 프록시 객체 관련된 것이다.
  - Json으로 Member 타입의 객체를 뽑아내려고 하는데, 실제 타입은 Member의 프록시 객체이기 때문에 문제가 발생한 것이다.

- 해결: Hibernate5Module을 라이브러리에 등록, 빈에 등록해서 사용하면 된다.

  - 하지만 위와 같이 엔티티를 직접 보내는 것 자체를 사용하지 않는 편이 낫다. 엔티티를 직접 API로 보내기 때문에 엔티티 변경이 API 스펙을 변경시키기 때문이다.
  - 특히 Hibernate5Module에서 FORCE_LAZY_LOADING은 더욱 사용하면 안 된다! 쓰지 않는 필드까지 모조리 로딩해버리기 때문이다.
  - 해결: 필요에 따라 프록시를`getName()`등으로 강제 초기화시킨다.

- 실제 API 개발에서는 엔티티를 위와 같이 전체 노출하지 않는다. 따라서 필요한 정보만 보낸다.
  - Hibernate5Module 쓰지 말고, DTO 반환하라!
  - 여전히, 연 로딩을 피하기 위해서 즉시 로딩을 사용해서는 안 된다!
    - 연관관계가 필요 없는 경우에도 데이터를 모조리 조회해서 성능 문제 발생
    - 성능 튜닝이 매우 어려워진다.
    - 성능 최적화가 필요할 경우 페치 조인을 사용하라!



### 간단한 주문 조회V2: 엔티티를 DTO로 반환

```java
    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2() {
        return new Result(
                orderRepository.findAllByString(new OrderSearch())
                        .stream()
                        .map(o -> new SimpleOrderDto(o))
                        .collect(Collectors.toList())
        );
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }
```

- 필요한 정보만 뽑아낸다.
- 하지만 지연 로딩으로 인해 지나치게 많은 쿼리가 날아간다는 단점은 V1과 같다.
  - 처음 쿼리로 ORDER 2개 가지고 옴
  - 각 ORDER마다 회원정보, 배송 조회 쿼리 나감
  - 1(첫 쿼리) + N(주문 수: 2) + N -> 5개의 쿼리가 나감!: N+1 문제
    - N+1은 최악의 상황을 가정한 것(영속성 컨텍스트를 뒤져서 값이 존재하는 경우 N+1보다는 작을 것이기 때문)
    - 하지만 무시할 문제 절대 아니며, 실제로 저런 경우는 적다.
  - EAGER를 쓴다고 해서 이 문제가 해결되는 것도 아니며, EAGER 사용 안 할 이유는 충분히 많음

- 대안: 페치 조인 사용하기



### 간단한 주문 조회  V3: 페치 조인 최적화

```java
public class OrderRepository {
    ...
        public List<Order> findAllWithMemberAndDelivery() {
        return em.createQuery(
                "SELECT o FROM Order o" +
                        " JOIN FETCH o.member m" +
                        " JOIN FETCH o.delivery d", Order.class
        ).getResultList();
    }
}
```

- 조회 방법을 바꾼 것 만으로 쿼리 횟수가 5회 -> 1회로 줄어든다.

- 페치 조인으로인해 order->member, order->delivery 이미 조회된 상태이기에 지연로딩이 발생하지 않는다.



### 간단한 주문 조회 V4: JPA에서 DTO로 바로 조회

```java
package cogitans.jpashop.repository;

...

@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId,
                               String name,
                               LocalDateTime orderDate,
                               OrderStatus orderStatus,
                               Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
```

- 별도의 DTO를 repo 패키지에 만들어 뒀다.

  - 만약에 Controller 클래스에 있는 Dto Inner 클래스를 사용한다면

    OrderRepository가(Repo 계층) Controller 계층을 바라보는 문제 상황이 발생하기 때문이다.

- JPQL문에서는 객체를 바로 인자값으로 넘겨줄 수 없기 때문에 직접 값 하나씩 넘겨줘야 한다. (Address와 같은 값 객체는 가능하다)
  - JPA는 엔티티나 Value Object(`@Embeddable`)만 반환 가능하다.

```java
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "SELECT new cogitans.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.status, d.address)" +
                        " FROM Order o" +
                        " JOIN o.member m" +
                        " JOIN o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
```

- new 명령어를 사용하여 JPQL의 결과를 DTO로 즉시 변환했다.
- 따라서 위와 같은 방식으로 값을 받아서 반환한다.

```java
    @GetMapping("/api/v4/simple-orders")
    public Result ordersV4() {
        return new Result(orderRepository.findOrderDtos());
    }
```

- 이 경우에는 V3의 페치 조인과 달리 엔티티의 모든 데이터를 가지고 오지 않고, 필요한 정보만 SELECT 한다. (일반적인 SQL 사용할 때처럼)

- 정리
  - SELECT할 데이터를 직접 선택하므로 성능 최적화(DB -> 어플리케이션 네트워크 용량, 하지만 크지 않은 차이)
    - 성능 차이는 SELECT의 필드 수보다도 FROM 절이나 JOIN 절 등에서 발생하는 일이 많다.
    - 물론 트래픽이 정말 크다면 고민해봐야 할 문제다.
  - 리포지토리 재사용성이 떨어짐, API 스펙에 맞춘 코드가 리포지토리에 들어가는 난점
    - 리포지토리는 엔티티/엔티티의 객체 그래프를 탐색하는 역할인데, 직접 DTO에 반환하는 경우(V4)는 API 스펙에 맞춘 코드
    - 물리적으로는 계층이 분리되어 있지만, 논리적으로는 그렇지 않다!

- 대안

  - repo 하위에 성능 최적화를 위한 계층(simpleQuery)용 패키지를 따로 만들고

    SimpleQuery용 Repository 만들고, 거기에 `findOrderDtos()` 메서드나

    OrderSimpleQueryDto 클래스를 몰아둔다.

  - 이렇게 하면 Repository를 깨끗하게 유지할 수 있으며, 이 부분의 특수성을 강조할 수 있다.

- **결론: 쿼리 방식 선택 권장 순서**
  1. 엔티티를 DTO로 변환하는 방법을 선택
  2. 필요하다면 페치 조인 사용 -> 대부분의 성능 이슈 해결
  3. 그래도 안 되면 DTO로 직접 조회
  4. 최후의 방법으로 JPA의 네이티브 SQL이나 스프링 JDBC Template을 사용하여 SQL 작성한다.



## API 개발 고급 - 컬렉션 조회 최적화

### 주문 조회 V1: 엔티티 직접 노출

