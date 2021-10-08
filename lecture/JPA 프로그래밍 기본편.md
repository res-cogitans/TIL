﻿﻿﻿﻿﻿# 자바 ORM 표준 JPA 프로그래밍 - 기본편(김영한)
- JPA - Java Persistence API
- JDBC나 MyBatis, JdbcTemplate보다 진보
- SQL을 직접 작성할 필요가 없기 때문에 생산성 및 유지보수성 상승
- JPA가 실무에서 어려운 이유?
	1. 객체와 테이블을 올바르게 매핑하고 설계하는 방법에 무지
		- 실무에서는 많은 객체와 테이블을 복잡하게 사용하게 되기에 문제
	2. JPA의 내부 동작 방식에 대한 이해 결여
		- 장애 상황에서 대응 못 함





## JPA 소개
### SQL 중심적인 언어의 문제점
- 현재의 개발 패러다임
	-  객체 지향 언어
	- 관계형 DB
	-> 객체를 관계형 DB에 관리 -> SQL !!!
- **SQL 중심적인 개발로 문제 발생**
	- 반복: 자바 객체를 SQL로, SQL을 자바 객체로 바꾸는 등
	- 변동 발생 시마다 객체, SQL 양쪽 반영하는 과정에서 문제 발생하기도
	- **패러다임의 불일치**
		- 개발자가 SQL 매퍼가 됨
		- 객체와 관계형 DB의 차이
			- 상속
				- 테이블의 슈퍼타입 서브타입 관계가 그나마 유사
				- 그러나 이는 매우 복잡
			- 연관관계
				- 객체는 참조를 사용: `member.getTeam()`
				- 테이블은 외래 키를 사용: `JOIN ON M.TEAM_ID = T.TEAM_ID`
				- 객체를 테이블에 맞춰서 모델링, 저장
					- 만일 객체다운 설계를 적용하려고 한다면 오히려 번거로운 작업들을 해야 함 -> 생산성 저하
				- 
				- 객체 그래프 탐색
					- 객체는 자유롭게 객체 그래프를 탐색 할 수 있어야 한다. (`.get...()`)
					- 그러나 처음 실행하는 SQL에 따라 탐색 범위가 결정된다.
					- 엔티티 신뢰 문제
					- 모든 객체를 미리 로딩할 수는 없다. 조회범위를 정하는 get을 만드는 방법 등 ...
			- 데이터 타입
			- 데이터 식별방법 등
				- 비교하기에서 차이가 발생하기도(SQL쿼리를 날리고, 새 객체에 담다보면, 컬렉션에서 동일 key로 value를 꺼냈을 때와는 달리, 서로 다르다고 판정)
				-> SQL을 직접 다루게 되면 계층형 아키텍처의 계층 분할이 어려움
	- 객체를 객체지향적으로 다룰수록 문제 발생!
	-> 객체를 자바 컬렉션에 저장하는 것처럼 DB에 저장하는 방법? -> JPA

### JPA 소개
- 자바 진영의 ORM 표준
	- **ORM**
		- **O**bject-**R**elational **M**apping(객체관계매핑)
		- 객체는 객체대로, RBD는 RBD대로
		- 객체와 RDB를 중간에서 매핑해주는 ORM 프레임워크
	
- JPA는 애플리케이션과 JDBC 사이에서 동작
	- 대신 SQL을 생성하고, JDBC API를 통해 DB와 통신해줌
	- 패러다임 불일치를 해결
	
- 역사
	- EJB - 엔티티 빈(자바 표준)
	- 하이버네이트(오픈 소스): EJB의 문제를 해결한 ORM 프레임워크
	- JPA(자바 표준): 하이버네이트 기반으로 만든 표준
		- JPA는 표준 스펙: 인터페이스 모음
		- 구현체: 하이버네이트, EclipseLink, DataNucleus 
	- 버전
		- JPA 1.0(JSR 220) 2006년: 초기 버전, 복합 키와 연관관계 기능이 부족
		- JPA 2.0(JSR 317) 2009년: 대부분의 ORM 기능을 포함, Jpa Criteria 추가
		- JPA 2.1(JSR 338) 2013년: 스토어드 프로시저 접근, 컨버터(Converter), 엔티티 그래프 기능 추가
	- JPA를 사용해야 할 이유
		- SQL중심적 개발이 아닌 객체 중심적 개발 가능
		- 생산성
			- 컬렉션처럼 단순하게 사용
			- 변경 감지
		- 유지보수
			- 기존에는 필드 변경시 모든 SQL을 수정해야 했음
			- JPA는 필드만 추가하면 SQL은 JPA가 처리
		- **패러다임의 불일치 해결**
			1. 상속
				- 객체 상속 관계를 테이블 슈퍼타입 서브타입
			2. 연관관계
			3. 객체 그래프 탐색
				-> JPA는 객체 그래프를 자유롭게 탐색 가능
			4. 비교하기
				- 동일한 트랜잭션에서 조회한 엔티티는 같음을 보장
		- 성능 최적화
			- JPA라는 중간 계층의 존재로 인해서 오히려 최적화 가능한 부분이 있음
			1. 1차 캐시와 동일성(identity) 보장
				1) 동일 트랜잭션 내에서는 동일 엔티티 반환 -> 조회 성능 약간 향상
				2) DB Isolation Level이 Read Commit이어도 애플리케이션에서 Repeatable Read 보장
			2. 트랜잭션을 지원하는 쓰기 지연(transactional write-behind)
				- INSERT의 경우
					1) 트랜잭션을 커밋할 때까지 INSERT SQL을 모음
					2) JDBC BATCH SQL 기능을 사용해서 한 번에 SQL 전송
						- BATCH 직접 하려고 하면 코드가 상당히 난잡해짐
				- UPDATE의 경우
					1) UPDATE, DELETE로 인한 로우(ROW)락 시간 최소한
					2) 트랜잭션 커밋 시 UPDATE, DELETE SQL 실행하고, 바로 커밋
			3. 지연 로딩(lazy Loading)
				1) 지연 로딩: 객체가 실제 사용될 때 로딩
				2) 즉시 로딩: JOIN SQL로 한번에 연관된 객체까지 미리 조회
		- 데이터 접근 추상화와 벤더 독립성
		- 표준
	
- ORM은 객체와 RDB 둘이 떠받치고 있는 기술



## 시작하기
- 메이븐
	- 의존성관리 등
	- `pom.xml` 설정: 스프링 부트 문서 등 참고하여 맞는 버전으로
- JPA 설정
	- `persistence.xml`
		- `META-INF/persistence.xml`
- **데이터베이스 방언**
- JPA는 특정 데이터베이스에 종속 X
- SQL 표준을 지키지 않는 특정 DB만의 고유 기능
- `<property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>`등이 방언에 대한 지정 부분

### JPA 소개
- **JPA의 구동방식**
	1. Persistence 클래스에서 설정정보(persistence.xml)을 읽음
	2. EntityManagerFactory 클래스 만듦
	3. EntityManagerFactory에서 EntityManager를 생성
- 정석적인 방식
	```java	
	private static void create(String memberName, Long id) {  
	  EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  
	  
	  EntityManager em = emf.createEntityManager();  
	  
	  EntityTransaction tx = em.getTransaction();  
	  tx.begin();  
	  
	 try{  
	        Member member = new Member();  
	        member.setName(memberName);  
	        member.setId(id);  
	  
	  em.persist(member);  
	  
	  tx.commit();  
	  } catch (Exception e) {  
	        tx.rollback();  
	  } finally {  
	        em.close();  
	  }  
	  
	    emf.close();  
	}
	```
	1. emf생성
	2. em 생성
	3. 트랜잭션 시작
	4. 본 코드
	5. 트랜잭션 커밋 / 예외 시에는 롤백
	6. em close
	7. emf close
	-> 스프링 사용 환경에서는 `em.persist();`로 충분
	
- update
	```java
	private static void update(String name, Long id) {  
	  EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  
	  
	  EntityManager em = emf.createEntityManager();  
	  
	  EntityTransaction tx = em.getTransaction();  
	  tx.begin();  
	  
	 try{  
	        Member member = em.find(Member.class, id);  
	        member.setName(name);  
	  
	  tx.commit();  
	  } catch (Exception e) {  
	        tx.rollback();  
	  } finally {  
	        em.close();  
	  }  
	  
	    emf.close();  
	}
	```
	- 왜 `member.setName()`만으로 변경이 가능한가?
	-> JPA 통해 가져온(find) 엔티티는 JPA 관리 하에 있고, 변경 여부를 체크한다. 변경 발견시 Update 쿼리를 날리기 때문에 가능한 것. 

- **주의사항**
	- `EntityManagerFactory`는 하나만 생성해서 애플리케이션 전체에서 공유한다.
		- 웹 서버가 올라오는 시점에 DB당 단 하나만 생성!
	- `EntityManager`는 쓰레드간에 공유하지 않는다. (사용 후 버려야 한다)
		- 고객의 요청이 올 때마다 생성, 사용 후 버림
	- JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
		- RDB에서 데이터 변경은 기본적으로 트랜잭션 안에서 이루어져야 한다.
		

**JPQL 소개**
- JPQL의 필요성
	- 단순한 조회의 경우
		- `em.find();`
		- 객체 그래프 탐색 `(a.getB().getC())`
	- where 등 조건을 붙여서 검색하고 싶다면?
	-> JPQL
	```java
	public static void jpqlFind() {  
	  EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");  
	  
	  EntityManager em = emf.createEntityManager();  
	  
	  EntityTransaction tx = em.getTransaction();  
	  tx.begin();  
	  
	 try{  
	        List<Member> findMembers = em.createQuery("select m from Member as m", Member.class)  
	                .getResultList();  
	  
	 for (Member findMember : findMembers) {  
	            System.out.println("findMember.getName() = " + findMember.getName());  
	  }  
	  
	        tx.commit();  
	  } catch (Exception e) {  
	        tx.rollback();  
	  } finally {  
	        em.close();  
	  }  
	  
	    emf.close();  
	}
	```
	- 취지
		- JPA -> 엔티티 객체 중심으로 개발
		- 그런데 검색의 경우 모든 DB데이터를 객체로 변횐해서 검색하는 것은 불가능하다.
		- 테이블을 검색할 경우 DB에 종속적인 개발이 되어버린다.
		- JPQL은 테이블이 아니라, **엔티티 객체를 대상**으로 검색한다 -> 엔티티 중심의 개발에 도움이 된다.
		-> 객체 지향 쿼리 언어
	- JPA가 제공하는 SQL을 추상화한 객체 지향 쿼리 언어
	- SQL을 추상화했기에 DB 방언을 변경하더라도 JPQL을 수정할 필요는 없음(특정 DB SQL에 종속적이지 않다)
	- SQL과 문법 유사: SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원
	- **JPQL은 엔티티 객체 대상 쿼리**
	- **SQL은 **DB 테이블 대상 쿼리**





## 영속성 관리 - 내부 동작 방식
### 영속성 컨텍스트
- **JPA 핵심 2가지**
	- 객체와 관계형 데이터베이스 매핑(Object Relational Mapping)
		- 정적인 측면
	- 영속성 컨텍스트
		- JPA 내부적 동작에 대한 이해
- **엔티티 매니저 팩토리와 엔티티 매니저**
	- 고객 요청 마다 emf가 em을 생성
	- em은 DB커넥션을 사용하여 DB를 사용
- **영속성 컨텍스트**
	- **엔티티를 영구 저장하는 환경**
	- `em.persist(entity);`
		
		- 엔티티 컨텍스트를 통해 엔티티를 영속화하는 것
		- 엔티티를 DB에 저장하는 것이 아니라 엔티티 컨텍스트에 저장하는 것
	- 영속성 컨텍스트는 논리적인 개념
	- 엔티티 매니저를 통해서 영속성 컨텍스트에 접근
		- J2SE환경: 엔티티 매지너와 영속성 컨텍스트가 1:1
		```mermaid
		graph LR
		A(EntityManager)  ---- 1:1 ---> B[PersistenceContext]
		```
		- J2EE, 스프링 프레임워크와 같은 컨테이너 환경: 엔티티 매니저와 영속성 컨텍스트가 N:1
		```mermaid
		graph LR
		A(EntityManager)  -----> P[PersistenceContext]
		B(EntityManager) ----->P
		C(EntityManager) ----->P
	- **엔티티의 생명주기**
		
		- **비영속**(new/transient)
			- 영속성 컨텍스트와 전혀 관계가 없는 **새로운** 상태
		- **영속**(managed): `em.persist()` `em.find()`
			- 영속성 컨텍스트에 **관리**되는 상태
		- **준영속**(detached): `em.detach`
			- 영속성 컨텍스트에 저장되었다가 **분리된** 상태
		- **삭제**(removed): `em.remove`
		
		```mermaid
		graph LR
		A[비영속: 엔티티 생성]  --em.persist--> B[영속: 컨텍스트에서 관리]
		```
		- 영속상태가 된다고 해서 DB에 쿼리가 날아가는 것이 아님, 트랜잭션을 커밋하는 시점에 컨텍스트 안에 있는 것들이 쿼리가 날라가는 것
- 영속성 컨텍스트의 이점: 중간 계층의 존재로 인한 이점
  - **1차 캐시**
  	- 영속성 컨텍스트 내부에 **1차 캐시** 있음
  	- Map(DB PK, Entity 객체)을 저장하고 있음.
  	- 조회시에
  		- DB를 먼저 뒤지는 것이 아니라 1차 캐시를 우선 뒤진다.
  		- 1차캐시에는 없는 경우, 1차캐시에 없는 경우, DB 조회 후 1차캐시에 저장, 반환한다.
  	- em은 하나의 요청 따라 사용하기에(트랜잭션 끝나고 닫기에) DB상에 이득은 매우 미미하다.
  - **동일성(identity) 보장**
  	- 1차 캐시로 인하여 반복 가능한 읽기(REPEATABLE READ) 등급의 트랜잭션 격리 수준을 DB가 아닌 애플리케이션 차원에서 제공
  	- 동일 식별자(`@Id`) 가진 엔티티는 '==' 연산자에 true 리턴
  - **트랜잭션을 지원하는 쓰기 지연(transactional write-behind)**
  	- INSERT SQL을 일일히 DB로 보내지 않고 커밋 시점에 한번에 DB로 보냄
  	- 영속성 컨텍스 안에는 **쓰기 지연 SQL 저장소**가 있음
  	- persist시에 이를 분석, SQL생성하여 저장소에 저장한다
  	- 트랜잭션을 커밋하는 시점에 flush되면서 쿼리가 날아감
  	- JPA는 내부적으로 동적 객체 생성이 필요, 따라서 기본 생성자가 필요(꼭 public일 필요는 없음)
  - **변경 감지(Dirty Checking)**
    - JPA 커밋 시점에 내부적으로 `flush()`발생
    - 1차 캐시에는 @Id, 엔티티, 스냅샷을 보관: 스냅샷은 컨텍스트 안으로 들어온 상태
    - 엔티티와 스냅샷을 비교: UPDATE 쿼리를 SQL 저장소에 저장,
    - **플러시 **
      - 영속성 컨텍스트의 변경내용을 데이터베이스에 반영
      - 플러시 발생시에 일어나는 일들
        - 변경 감지
        - 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
        - 쓰기 지연 SQL 저장소의 쿼리를 DB에 전송(등록, 수정, 삭제 쿼리)
      
      - 영속성 컨텍스트를 플러시하는 방법
        - `em.flush()`: 직접 호출
          - DB에 쿼리가 바로 나감
          - flush가 일어난다고 1차캐시가 지워지는 것은 아님
        - 트랜잭션 커밋: 플러시 자동 호출
        - JPQL 쿼리 실행: 플러시 자동 호출
          -  JPQL의 대상이 되는 엔티티들을 persist 하는 쿼리들이 날아가지 않은 상태에서 JPQL이 실행되는 일을 방지하기 위함
      - 플러시 모드 옵션(`em.setFlushMode(FlushModeType.COMMIT)`형태로 사용)
        - `FlushModeType.AUTO`: 커밋이나 쿼리를 실행할 때 플러시(기본값)
        - `FlushModeType.COMMIT`: 커밋할 때만 플러시
          - 당장 플러시 할 필요가 없을 때 
      - 플러시 주의사항
        - 영속성 컨텍스트를 비우지 않음
        - **영속성 컨텍스트의 변경내용을 DB에 동기화하는 것**이 플러시!
        - 트랜잭션이라는 작업 단위가 중요 -> **커밋 직전에만 동기화**하면 됨
  - **준영속 상태**
    - 영속 -> 준영속: 영속성 컨텍스트에서 분리(detached)
      - 엔티티는 persist외에도 find로 영속 상태가 아닌 엔티티를 불러올 때 영속 상태가 됨
    - 영속성 컨텍스트가 제공하는 기능을 사용 못함
    - 준영속 상태로 만드는 방법
      - `em.detach(entity)`: 특정 엔티티만 준영속 상태로 전환
      - `em.clear()`: 영속성 컨텍스트를 완전히 초기화
      - `em.close()`: 영속성 컨텍스트를 종료
  - 지연 로딩(Lazy Loading)





## 엔티티 매핑

### 객체와 테이블 매핑

#### 엔티티 매핑 소개

- 객체와 테이블 매핑: `@Entity`, `@Table`
- 필드와 컬럼 매핑: `@Column`
- 기본 키 매핑: `@Id`
- 연관관계 매핑: `@ManyToOne`, `@JoinColumn`

##### 객체와 테이블 매핑

- **`@Entity`**

  - JPA가 관리하는 엔티티
  - **주의사항**
    - 기본 생성자가 필수(public 혹은 protected)
    - final 클래스, Enum, Interface Inner 클래스 사용 불가
    - 저장할 필드에 final 사용 불가

  - 속성: name

    - JPA에서 사용할 엔티티명

    - 기본값은 클래스명

    - ```java
      @Entity(name = "Member")
      public class Member {
      }
      ```

- **`@Table`**

  - **속성**
    - name: 매핑할 테이블명
    - catalog: DB catalog 매핑
    - schema: schema 매핑
    - uniqueConstraints(DDL): DDL 생성시에 유니크 제약 조건 생성



### 데이터베이스 스키마 자동 생성

- JPA는 애플리케이션 로딩 시점에 DDL을 자동 생성
  - 테이블 중심 개발 -> 객체 중심 개발
  - 데이터베이스 방언을 활용, DB에 맞는 DDL
- 개발시에만 사용, 운영에서는 사용 X, 혹은 다듬을 것
- 속성: `hibernate.hbm2ddl.auto`
  - persistence.xml에서 `<property name="hibernate.hbm2ddl.auto" value="create-drop"/>` 형태로 사용한다.
    - create 기존테이블 삭제 후 다시 생성 (DROP + CREATE)
    - create-drop create와 같으나 종료시점에 테이블 DROP
      - 테스트 케이스 등에서 사용
    - update 변경분만 반영(운영DB에는 사용하면 안됨)
      - drop대신 alter
      - 삭제는 불가
    - validate 엔티티와 테이블이 정상 매핑되었는지만 확인
    - none 사용하지 않음
      - 관례상 none이라고 작성

- 주의사항
  - **운영시에는 절대 create, create-drop, update 사용하지 말 것!**
    - 개발 초기 단계는 create 또는 update
    - 테스트 서버는 update 또는 validate
      - 여러 개발자가 사용하는 환경에서 create 사용하면 데이터가 날아가 버리는 문제 발생
    - 스테이징과 운영 서버는 validate 또는 none
    - 권장사항: 운영 서버는 당연하고, 개발 서버에서도 **사용하지 않는 것이 제일 낫다!**
      - 직접 스크립트 짠 것을 로컬에서 시험해 보고 적용하라.
      - alter table이 갖는 위험성이 크다.
      - alter나 drop 자체가 안 되게 분리하는 것도 좋다.
      - 자동 작성된 스크립트를 면밀히 검토해서 넘기는 편이 낫다.
- DDL 생성 기능
  - `@Column(Unique = true, length = 10)`
  - 위 사례에서 unique는 애플리케이션이 아닌 DDL 생성만을 도움: **JPA 실행 로직에는 영향 X**



### 필드와 컬럼 매핑

- @Column 컬럼 매핑

- @Temporal 날짜 타입 매핑

- @Enumerated enum 타입 매핑

- @Lob BLOB, CLOB 매핑

- @Transient 특정 필드를 컬럼에 매핑하지 않음(매핑 무시)

  

#### `@Column` 속성과 기본 값들

- name
  - 필드와 매핑할 테이블의 컬럼 이름
  - 기본값: 객체의 필드 이름
- insertable, updatable
  - 등록, 변경 가능 여부
  - 기본값: TRUE
- nullable(DDL)
  - null 값의 허용 여부를 설정한다. false로 설정하면 DDL 생성 시에 not null 제약조건이 붙는다.
- unique(DDL)
  - @Table의 uniqueConstraints와 같지만 한 컬럼에 간단히 유니크 제 약조건을 걸 때 사용한다.
  - 그러나 이 경우 제약명이 생성된 것(알아보기 힘든 것)이 되기 때문에 적합하지 않다.
  - -> unique 제약은 `@Table` 속성으로 걸자!
- columnDefinition (DDL)
  - 데이터베이스 컬럼 정보를 직접 줄 수 있다. ex) varchar(100) default ‘EMPTY'
  - 기본값: 필드의 자바 타입과 방언 정보를 사용해서 적절한 컬럼 타입
- length(DDL)
  - 문자 길이 제약조건, String 타입에만 사용한다.
  - 기본값: 255
- precision, scale(DDL)
  - BigDecimal 타입에서 사용한다(BigInteger도 사용할 수 있다). precision은 소수점을 포함한 전체 자 릿수를, scale은 소수의 자릿수 다. 참고로 double, float 타입에는 적용되지 않는다. 아주 큰 숫자나 정 밀한 소수를 다루어야 할 때만 사용한다.
  - 기본값: precision=19, scale=2 



#### `@Enumerated`

- EnumType을 매핑할 때 사용

- 속성: value
  - EnumType.ORDINAL: enum 순서를 데이터베이스에 저장
    - 기본값, 하지만 **사용하지 말자!**
      - 나중에 Enum 추가된다면 순서가 바뀌기 때문에 문제!
  - EnumType.STRING: enum 이름을 데이터베이스에 저장



#### `@Temporal`

- 날짜 타입(java.util.Date, java.util.Calendar)을 매핑할 때 사용

- 참고: LocalDate, LocalDateTime을 사용할 때는 생략 가능(최신 하이버네이트 지원) 

- 속성: value
  - TemporalType.DATE: 날짜, 데이터베이스 date 타입과 매핑 (예: 2013–10–11)
  - TemporalType.TIME: 시간, 데이터베이스 time 타입과 매핑 (예: 11:11:11)
  - TemporalType.TIMESTAMP: 날짜와 시간, 데이터베이 스 timestamp 타입과 매핑(예: 2013–10–11 11:11:11) 



#### `@Lob`

- 데이터베이스 BLOB, CLOB 타입과 매핑
- @Lob에는 지정할 수 있는 속성이 없다. 
- 매핑하는 필드 타입이 문자면 CLOB 매핑, 나머지는 BLOB 매핑
  - CLOB: String, char[], java.sql.CLOB
  - BLOB: byte[], java.sql. BLOB



#### `@Transient `

- 필드 매핑X
- 데이터베이스에 저장X, 조회X
- 주로 메모리상에서만 임시로 어떤 값을 보관하고 싶을 때 사용

```java
@Transient private Integer temp; 
```



### 기본 키 매핑

- `@Id`
- `@GeneratedValue`



#### 기본 키 매핑 방법

- 직접 할당: **`@Id`만 사용**
- 자동 생성(**`@GeneratedValue`**)
  - `@GeneratedValue(Strategy = GenerationType.AUTO)` AUTO 사용시 해당 DB 방언에 맞는 방식을 자동 선택, 혹은 AUTO 아닌 특정 타입 선택이 가능하다.
  - **IDENTITY**: 데이터베이스에 위임, MYSQL
  - **SEQUENCE**: 데이터베이스 시퀀스 오브젝트 사용, ORACLE `@SequenceGenerator` 필요
  - **TABLE**: 키 생성용 테이블 사용, 모든 DB에서 사용, `@TableGenerator` 필요
  - **AUTO**: 방언에 따라 자동 지정, 기본값



#### IDENTITY전략

- 기본 키 생성을 데이터베이스에 위임
- 쿼리상 id는 null로 들어갔음
- 주로 MySQL, PostgreSQL, SQL Server, DB2에서 사용 (예: MySQL의 AUTO_ INCREMENT)
- JPA는 보통 트랜잭션 커밋 시점에 INSERT SQL 실행
- AUTO_ INCREMENT는 데이터베이스에 INSERT SQL을 실행 한 이후에 ID 값을 알 수 있음
- IDENTITY 전략은 em.persist() 시점에 즉시 INSERT SQL 실행 하고 DB에서 식별자를 조회
- **주의 사항**
  - IDENTITY전략은 null값이 DB에 INSERT되야 그 값을 알 수 있게 됨.
  - 그런데 영속 상태를 위해서는 PK값이 필요함(1차캐시에 ID값)
  - 떄문에 IDENTITY전략에서는 persist 하는 시점에서 DB에 INSERT 쿼리가 날아간다. (커밋하는 시점이 아니다!)



#### SEQUENCE 전략

- 데이터베이스 시퀀스는 유일한 값을 순서대로 생성하는 특별한 데이터베이스 오브젝트(예: 오라클 시퀀스)

- 오라클, PostgreSQL, DB2, H2 데이터베이스에서 사용

- Id는 Integer말고 Long

  - 과거보다 하드웨어 발전 이루어짐 -> 생각보다 부담 적음
  
  - 10억 정도 한계치에 도달하고 바꾸는 것이 더 힘듦
  
  - 테이블마다 다른 시퀀스를 사용하고 싶다면
  
    ```java
    @Entity
    @SequenceGenerator(
     name = “MEMBER_SEQ_GENERATOR",
     sequenceName = “MEMBER_SEQ", //매핑할 데이터베이스 시퀀스 이름
     initialValue = 1, allocationSize = 1)
    public class Member {
     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE,
     generator = "MEMBER_SEQ_GENERATOR")
     private Long id; 
    ```
  
- **`@SequenceGenerator`** (동시성 이슈 없음) 속성

  - name: 식별자 생성기 이름
    - 기본값: 필수
  - sequenceName: 데이터베이스에 등록되어 있는 시퀀스 이름
    - 기본값: hibernate_sequence
  - initialValue: DDL 생성 시에만 사용됨, 시퀀스 DDL을 생성할 때 처음 1 시작하는 수를 지정한다.
    - 기본값:1
  - allocationSize: 시퀀스 한 번 호출에 증가하는 수(성능 최적화에 사용됨)
    - **데이터베이스 시퀀스 값이 하나씩 증가하도록 설정되어 있으면 이 값 을 반드시 1로 설정해야 한다! **
      - -49 -> 1 -> 51 (SEQ)
      - 1, 2(MEM), 3(MEM) ... 50(MEM)
    - 기본값: 50
  - catalog, schema: 데이터베이스 catalog, schema 이름

#### TABLE 전략

- 키 생성 전용 테이블을 하나 만들어서 데이터베이스 시퀀스를 흉 내내는 전략
- 장점: 모든 데이터베이스에 적용 가능
- 단점: 성능
- **`@TableGenerator`** 속성
  - name: 식별자 생성기 이름
    - 기본값: 필수
  - table 키생성 테이블명
    - 기본값: hibernate_sequences
  - pkColumnName: 시퀀스 컬럼명
    - 기본값: sequence_name 
  - valueColumnName: 시퀀스 값 컬럼명
    - next_val
  - pkColumnValue: 키로 사용할 값 이름
    - 기본값: 엔티티 이름 
  - initialValue: 초기 값, 마지막으로 생성된 값이 기준이다.
    - 기본값: 0
  - allocationSize: 시퀀스 한 번 호출에 증가하는 수(성능 최적화에 사용됨), `@SequenceGenerator`와 유사
    - 기본값: 50
  - catalog, schema: 데이터베이스 catalog, schema 이름
  - uniqueConstraint s(DDL): 유니크 제약 조건을 지정할 수 있다. 



#### 식별자 전략 권장

- PK의 제약 조건: =!null, unique **변하면 안 된다**.

- 자연키(비즈니스상 의미 있는 값)가 아닌 대체키(대리키)를 사용하라

- **권장: Long 자료형, 대체키 + 키 생성전략*



### 실전 예제1 - 요구사항 분석과 기본 매핑

- 컬럼명 / 테이블명 애매할 경우에는 직접 매핑해 준다:

  - ```java
    @Column(name = "MEMBER_ID")
    private Long id;
    ```

  - 특히 Order의 경우, DB에서 ORDER를 예약어로 사용하는 경우가 다수이기에 ORDERS라고 명명하는 것이 관례다:

    - ```java
      @Entity
      @Table(name = "ORDERS")
      public class Order { ... }
      ```

- 제약조건(길이 등)을 설정하는 것은 여러 방법이 있지만, 자바 코드 쪽에 반영하는 것을 추천한다:

  - ```java
    @Column(length = 10)
    private String name;
    ```

  - 이렇게 작성할 경우 개발자가 테이블을 뒤져 보지 않고, 자바 코드만 보는 것 만으로 일목요연하게 파악이 가능하기 때문이다.

  - 같은 이유로 `@Table`이용하여 unique 제약조건 및 `@Index` 규정하는 것을 추천한다.

- 스프링 부트 사용 시 명명규칙은
  - camelCase를 camel_case 형태로 바꿔주는 것이 기본
  - 규칙을 다르게 오버라이딩하는 것도 가능하다.

- RDB에 맞춘 설계로 인해 객체지향적이지 않은 프로그래밍이 강요되는 경우

  - `Order` 엔티티에 필드 `Long memberId`를 규정하는 형태는, 어떤 order에서 주문 member를 찾으려고 할 때,
    1. orderId 이용하여 order 구하고
    2. `order.getMemberId` 이용하여 memberId 구하고
    3. memberId 이용하여 member를 찾는 구조가 되어 버린다

  - 객체지향적인 설계 방식은 order가 `Member member`를 가지고 있고, 이것을 `order.getMember`하는 식으로 꺼내 쓸 수 있는 방식이다.

- 데이터 중심 설계의 문제점

  - 객체 설계를 테이블 설계에 맞춘 방식
  - 테이블의 외래키를 객체에 그대로가져옴
  - 객체 그래프 탐색이 불가능
  - 참조가 없으므로 UML도 잘못되었다.

  - 연관관계 매핑의 필요성





## 연관관계 매핑 기초

- 객체와 RDB 패러다임 차이 주의
- 객체의 참조와 테이블의 외래 키를 매핑
- 주요 용어
  - 방향(Direction): 단방향, 양방향
  - 다중성(Multiplicity): 다대일, 일대다, 일대일, 다대다
  - 연관관계의 주인(Owner): 객체 양방향 연관관계는 관리 주인 필요



#### 연관관계가 필요한 이유

- ORM을 배워야 하는 것은 객체지향 설계를 위함
- 객체를 테이블에 맞춰 데이터 중심으로 모델링하면, 협력 관계를 만들 수 없다.
  - 테이블은 외래 키로 조인을 사용해서 연관된 테이블을 찾는다.
  - 객체는 참조를 사용해서 연관된 객체를 찾는다.
  - 테이블과 객체 사이의 큰 간격!



### 단방향 연관관계

#### 객체 지향 모델링

- Member 객체는 필드로 Team의 id값을 갖는 것이 아닌, Team 타입인 team을 가져야
  - `@ManyToOne` 멤버(다수) 입장에서 팀은 하나이기에
  - `@JoinColumn(name = "TEAM_ID")` 이 관게에서 조인하는 컬럼 명시



### 양방향 연관관계와 연관관계의 주인

#### 양방향 매핑

- 양방향 연관관계는 양쪽으로 참조가 가능한 것(반대 방향으로 객체 그래프를 탐색 가능)
- 단방향 매핑이 좀 더 권장할 만하긴 함
- 양방향 연관관계라도 단방향 연관관계와 테이블 연관관계가 차이가 없다.
  - 조인을 통해서 탐색가능하기에 그렇다.
  - 테이블에서는 FK가 있는것 만으로 양방향 조회가 가능해지는 것이다.
  - 반면 객체의 경우에는 그렇지 않다.

- 가령 위의 예시에서 Team객체가 해당 팀에 소속된 멤버 `List<Member> members`를 필드에 가지고 있다고 한다면

  - ```java
    @Entity
    public Class Team{
        ...
        @OneToMany(mappedBy = "team")
        private List<Member> members new ArrayList<>();
        ...
    }
    ```

    

#### 연관관계의 주인과 mappedBy

- 객체와 테이블 연관관계 차이
  - 객체의 양방향 연관관계는 사실 단방향 연관관계 2개다.(A -> B, B -> A)
  - 테이블의 연관관계에서는 FK로 연결되어 있는 연관관계 하나로 양방향 접근이 가능하다. (양 쪽으로 조인 가능하다.)

- 외래 키 관리 문제
  - 위의 예제에서 MEMBER 테이블의 TEAM_ID(FK)는 어떻게 관리해야 하는가?
    - Member 객체의 team이 변경될 때 업데이트되어야 하는가?
    - Team 객체의 members가 업데이트 되었을 때 업데이트 되어야 하는가?
    - 다시 말해, memberA가 소속한 팀t1에서 t2로 변경하고 싶다면
      - memberA의 team 값을 변경해야 하는지?
      - t1과 t2의 소속 멤버 members 값을 변경해야 하는지?
    - 한편 DB의 입장에서는 위와 무관하게 MEMBER에 저장된 FK값만 업데이트 하면 된다.
    - 연관관계 관리 주체 설정이 필요하다! -> 연관관계의 주인

- 연관관계의 주인(Owner)

  - 양방향 매핑에 해당하는 규칙
  - 객체의 두 관계 중 하나만을 연관관계의 주인으로 지정
    - 주인만이 외래키를 관리(등록, 수정)
    - 주인이 아닌 경우 읽기만 가능
  - mappedBy 속성
    - 주인은 mappedBy 속성 사용하지 않음!
    - 주인이 아닌 경우 mappedBy 속성으로 주인 지정
  - **외래 키가 있는 곳을 주인으로 정해라!**
    - 반대의 경우, Team객체쪽에 변경을 가했는데, Member쪽으로 업데이트 쿼리가 나가는, 혼동되는 상황이 발생한다.
    - 성능 문제: 위 예제에서 Member를 주인으로 매핑한다면, Member쪽에 변경을 할 경우 한방 쿼리가 나가지만, Team쪽을 주인으로 삼는다면, 한 쪽에서 INSERT 쿼리, 한 쪽에선 UPDATE 쿼리 나가는 식이 될 수 있기 때문이다.
    - **`@ManyToOne` 즉, 다대일에서, 다 쪽을 연관관계의 주인으로 삼으면 된다!**
  - 연관관계의 주인 설정은 비즈니스적 중요성과는 무관하다! 이는 ORM상의 문제다. 비즈니스적인 중요성에 따라 연관관계의 주인 설정을 할 필요 없다!

  - 양방향 매핑 시의 주의사항

    - 연관관계의 주인 쪽에 값을 입력하지 않는 실수

      - 연관관계의 주인이 아닌 쪽에만 값을 입력하면 그 쪽은 읽기만 가능하기에 DB에 반영되지 않는다. (null)
      - JPA 관점에서는 연관관계의 주인 쪽에만 값을 입력하면 된다.

    - 하지만 **순수한 객체 관계를 고려하여, 양 쪽 다 값을 넣어야 한다!**

      - 연관관계 주인인 Member 쪽에 team을 입력하고, 반대 쪽 team에는 members 관련 변경사항을 적용하지 않는다면

        - 만일 flush와 clear 작업을 한 후에 team에서 members를 찾는다면 문제가 없다. (DB에 SELECT 쿼리 통해서 찾아 오기 때문에)
        - 하지만 flush와 clear 작업이 없다면, DB에서 값을 찾아 오는 것이 아니라 메모리에 저장된 영속성 컨텍스트의 1차 캐시에서 team 정보를 찾아온다.
        - 이 경우 team의 members 관련 사항은 변경 이전의 상태이기 때문에 변동사항이 반영된 결과가 반환되지 않는다.
      - 테스트 케이스 작성의 경우 JPA 적용 안 할 수도 있기 때문에 양 쪽 다 값을 넣어야 한다.
      - -> **양 쪽 다 값을 넣어야 한다!**
      
    - 연관관계 편의 메서드

      - 위와 같이 양 쪽 모두에 값을 입력하는 것은 실수할 가능성이 있음.

      - 연관관계 편의 메서드를 설정하자

        - 연관관계 주인 쪽의 Setter를 연관관계 편의 메서드로 수정한 예

          ```java
          public void changeTeam(Team team) {
              this.team = team;
              team.getMembers().add(this);
          }
          ```

          - 왜 setTeam이 아니라 changeTeam인가?
            - 단순 세팅만 하는 Setter와 다르며, 실제로는 null 체크 등 다수의 메커니즘이 필요한데,
            - 자바빈 프로퍼티 규약 상의 Setter와 다른 메커니즘을 사용한다는 것을 드러내기 위한 네이밍

        - 양방향 매핑시의 무한루프를 주의하라!

          - 위의 연관관계 편의 메서드를 Team 쪽에서 설정하는 경우

            ```java
            public void addMember(Member member) {
                member.setTeam(this);
                members.add(member);
            }
            ```

          - JPA에서 변경사항은 연관관계의 주인에 반영해야 하지만, 편의 메서드의 경우 어느 쪽에 있어도 되기 때문이다.
          - 다만 위 두 가지 중에서 **하나만** 사용하는 것이 좋다. 무한 루프등의 위험성이 있기 때문이다.
            - toString(), lombok, JSON 생성 라이브러리 등에서 문제가 발생한다.
            - 예를 들어서 Members의 toString에서 team은 Team.toString을 참조할텐데, 거기서도 Member를 참조하게 되는 상황이 발생하기 때문이다.
            - 해결책
              - lombok에서 toString 라이브러리를 가급적 쓰지 말고, 직접 구현해서 사용한다.
              - JSON의 경우, **컨트롤러에서는 엔티티를 절대 반환하지 않는 것**이 좋은 해법이다.
                - 무한 루프의 가능성
                - 이후 엔티티를 변경하게 되면 API 스펙 자체가 변해버리는 사태 발생하기 때문이다.
              - 엔티티는 DTO 이용해서 반환하라!
          - 어느 쪽에서 사용할지는 상황에 따라 다르다.

- 정리
  - 양방향 매핑 정리
    - **단방향 매핑만으로도 이미 연관관계 매핑은 완료**
      - 즉, 단방향 매핑만으로 설계를 끝내라!
    - 양방향 매핑은 반대 방향으로 조회(객체 그래프 탐색) 기능이 추가된 것 뿐
    - JPQL에서 역방향에서 탐색할 일이 많음
    - 단방향 매핑을 잘 하고 양방향은 필요할 때 추가해도 됨(테이블에 영향을 주지 않기 때문이다.)

  - 연관관계의 주인 설정 기준
    - 비즈니스 로직을 기준으로 선택해선 안 됨!
    - 외래 키의 위치를 기준으로 정해야 한다.
    
      

### 실전 예제2 - 연관관계 매핑 시작

- ORDERS 테이블에에 MEMBER_ID(FK)가 있을 때 굳이 MEMBER 테이블에서 ORDER를 참조할 이유가 있을까?
  - 만일 멤버 정보를 기반으로 주문을 검색하고 싶다고 해도 이미 있는 참조를 이용하여 찾을 수 있다.
  - 굳이 양방향 관계를 불필요한데도 사용한 것은 잘못된 설계이다.





## 다양한 연관관계 매핑

### 다대일[N:1]

