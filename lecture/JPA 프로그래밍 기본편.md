﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿# 자바 ORM 표준 JPA 프로그래밍 - 기본편(김영한)
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

#### 연관관계 매핑 시 고려사항 3가지

- **다중성**: 헷갈릴 때는 반대 방향을 생각해 본다. (앞의 것이 연관관계의 주인이라 가정하고 설명한다.)
  - 다대일 `@ManyToOne`
  - 일대다 `@OneToMany`
  - 일대일 `@OneToOne`
  - 다대다 `@ManyToMany`
- **단방향, 양방향**
  - 테이블
    - 외래 키 하나로 양쪽 조인 가능
    - 실제로는 방향 개념은 없음
  - 객체
    - 참조용 필드가 있는 쪽으로만 참조 가능
    - 한쪽만 참조하면 단방향
    - 양쪽이 서로 참조하면 양방향
      - 이 경우에도 실제로 양방향 참조는 없다. 참조의 관점에서는 단방향이 두 개 있는 것이다. 
- **연관관계의 주인**
  - 테이블에선 외래 키 하나로 두 테이블이 연관관계를 맺음
  - 객체 양방향 관계는 A->B, B->A 처럼 참조가 2군데
  - 객체 양방향 관계는 참조가 두 군데 있기 때문에 둘 중 테이블의 외래 키를 관리할 곳을 지정해야 한다.
  - 연관관계의 주인: 외래 키를 관리하는 참조
  - 주인의 반대편: 외래 키에 영향을 주지 않음, 단순 조회만 가능



### 다대일[N:1]

- JPA에서 가장 많이 사용하는 관계이며, 중요
- 다대일 단방향
  - 가장 많이 사용하는 관계
  - 반대는 일대다

- 다대일 양방향
  - 단방향에서 반대 방향 참조를 추가한 것
    - `@OneToMany(mappedBy = "")`
  - DB상의 차이는 없다.



### 일대다[1:N]

- 일이 연관관계의 주인인 경우
- 일대다 단방향(권장하지 않는 모델)
  - 객체 입장에서는 생각해볼 수 있는 모델이지만,
  - DB 테이블 입장에서는 정작 다(MEMBER 쪽)에 FK가 있어야 한다.
  - 때문에 Team쪽이 연관관계의 주인이라 할지라도 주인의 반대쪽에 FK 업데이트 쿼리가 나가야 한다. -> 쿼리가 더 나갈 수 밖에 없는 구조
  - 성능상의 문제는 미미하지만, 이런 혼재된 쿼리로 인해서 혼란이 발생하기 쉽다는 큰 문제가 있다.
  - `@JoinColumn`을 꼭 사용해야 한다. 그렇지 않으면 조인 테이블 방식을 사용함(중간에 테이블을 하나 추가함: ex. TEAM_MEMBER)
  - 정리: 단점
    - 엔티티가 관리하는 외래 키가 다른 테이블에 있음
    - 연관관계 관리를 위해 추가로 UPDATE SQL 실행
    - **일대다 단방향 매핑보다는 다대일 양방향 매핑을 사용하라!**
- 일대다 양방향
  - 공식 스펙은 아님
  - `@JoinColumn(name="TEAM_ID", insertable=false, updatable=false)` - 읽기 전용 매핑
  - 연관관계 주인의 반대편에 읽기 전용 필드를 사용해서 양방향처럼 사용하는 방법이다.



### 일대일[1:1]

- 일대일 관계는 대칭적
- 외래 키 자유롭게 선택 가능
- 외래 키에 데이터베이스 유니크(UNI) 제약조건 추가되는 편이 1:1관게 표현에 좋음
- 주 테이블에 외래키 있는 경우
  - 일대일 단방향
    - 다대일 단방향 매핑과 유사(어노테이션만 차이가 있음)
  - 일대일 양방향
    - `@@OneToOne(mappedBy ="")`을 반대편 적용
- 대상(주인이 아닌) 테이블에 외래키 있는 경우
  - 단방향
    - 불가능하다.
    - 지원 스펙도 없고 방법도 없다.
  - 양방향
    - 가능은 하지만 억지스러움

- 어느 쪽에 FK를 놓을 것인가?
  - 만일 Member와 Locker가 1:1관계라고 가정해 보자.
  - 이후 비즈니스 변경으로 인해서 Member가 다수의 Locker를 사용해도 되게 바뀐다면 Locker가 FK 관리하는 것이 좋다.
    - 테이블 확장은 보통 어려운 일이지만 이 경우 유니크 제약조건을 빼기만 하면 다대일 관계로 전환이 가능해지기 때문이다.
  - 반면 반대의 경우라면 Member에 FK를 두는 것이 좋다.
    - 또한 Member가 일반적으로 조회가 더 자주되기 때문에 성능상으로는 Member에 FK를 두는 것이 이득이 될 확률이 높다.
- 정리
  - 주  테이블(자주 엑세스하는)에 외래키
    - 주 객체가 대상 객체의 참조를 가지는 것처럼 주 테이블에 외래키를 두고 대상 테이블을 찾음
    - 객체지향 개발자가 선호
    - JPA 매핑이 편리
    - 장점: 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 확인 가능
    - 단점: 답이 없으면 외래키에 null 허용
  - 대상 테이블에 외래키
    - 대상 테이블에 외래키가 존재
    - 전통적인 데이터베이스 개발자 선호
    - 장점: 주 테이블과 대상 테이블을 일대일에서 일대다 관계로 변경할 때 테이블 구조 유지
    - 단점: 프록시 기능의 한계로 **지연 로딩으로 설정해도 항상 즉시 로딩되게 된다.**



### 다대다[N:M]

- 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없음
- 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야함
- 객체는 컬렉션을 사용해서 객체 2개로 다대다 관계 가능

- `@ManyToMany` 사용
- `@JoinTable`로 연결 테이블 지정

- 다대다 매핑도 단방향, 양방향 가능
- 한계 
  - 편리해보이지만 **실무에서 사용해서는 안 된다!**
  - 연결 테이블이 단순히 연결만 하고 끝나지 않음
  - 연결 테이블에는 매핑 정보(ID)만 들어올 수 있지만, 추가적인 정보를 넣을 수는 없다. (주문시간, 수량 등)
- 극복
  - 연결 테이블용 엔티티 추가(연결 테이블을 엔티티로 승격)
    - `@ManyToMany` -> `@OneToMany`, `@ManyToOne`
    - 양 쪽의 FK를 PK로 사용하기 보다는 새로 의미 없는 값을 Generate 하여 PK로 사용하는 것이 더 낫다.
      - 더 유연하여 확장 및 변경에 용이하며
      - 제약조건을 추가하는 것으로 같은 효과를 볼 수도 있기 때문이다.





## 고급 매핑

### 상속관계 매핑

- 관계형 데이터베이스에는 상속관계가 없다.
- 슈퍼타입 서브타입 관계라는 모델링 기법이 객체 상속과 유사
- 상속관계 매핑: 객체 상속 구조와 DB의 슈퍼타입 서브타입 관계를 매핑하는 것

- 슈퍼타입 서브타입 논리 모델을 물리 모델로 구현하는 방법
  - 조인 전략: 각각 테이블로 변환
  - 단일 테이블 전략: 통합 테이블로 변환
  - 구현 클래스마다 테이블 전략: 서브타입 테이블로 변환

- 주요 어노테이션
  - `@Inheritance(strategy=InheritanceType.XXX)`
    - JOINED: 조인 전략
    - SINGLE_TABLE: 단일 테이블 전략
    - TABLE_PER_CLASS: 구현 클래스마다 테이블 전략
  - `@DiscriminatorColumn(name=“DTYPE”)`
    - 구별을 위한 컬럼
    - 조인 전략에서는 사용하지 않아도 되지만 사용 권장
    - 단일 테이블 전략에서는 필수적으로 필요하기에 자동적으로 생성
  - `@DiscriminatorValue(“XXX”)`
    - 기본은 엔티티명, DTYPE상 입력되는 값을 설정 가능

- #### 조인 전략

  - 장점
    - 테이블 정규화
    - 외래 키 참조 무결성 제약조건 활용가능
    - 저장공간 효율화
  - 단점
    - 조회시 조인을 많이 사용, 성능 저하
    - 조회 쿼리가 복잡함
    - 데이터 저장시 INSERT SQL 2번 호출

- #### 단일 테이블 전략

  - 장점
    - 조인이 필요 없으므로 일반적으로 조회 성능이 빠름
    - 조회 쿼리가 단순함
  - 단점
    - 자식 엔티티가 매핑한 컬럼은 모두 nullable
    - 단일 테이블에 모든 것을 저장하기에 테이블이 커질 수 있으며, 상황에 따라 오히려 성능 저하 가능

- #### 구현 클래스마다 테이블 전략

  - ORM 전문가와 DB 전문가 모두가 추천하지 않는 전략
  - 장점
    - 서브타입을 명확하게 구분해서 처리할 때 효과적
    - not null 제약조건 사용 가능
  - 단점
    - 여러 자식 테이블을 함께 조회할 때 성능이 느림(UNION SQL)
    - 자식 테이블을 통합해서 쿼리하기 어려움
    - 변경이 어려움

- 정리

  - 구현 클래스마다 테이블 전략은 고려하지 말 것
  - 기본적으로는 조인 전략(특히 비즈니스적 중요성이 높거나 복잡한 경우)
  - 단순한 경우에는 단일 테이블 전략



### Mapped Superclass - 매핑 정보 상속

- 공통 매핑 정보가 필요할 때 사용(id, name)

  - 동일한 속성을 가지는 경우 반복적 작업을 줄일 수 있음(상속과는 무관!)

  - 수정일자 등 공통적으로 사용되는 사항 등에 사용

- 적용되는 엔티티들이 BaseEntity를 상속하게 하고, BaseEntity에는 `@MappedSuperclass`어노테이션을 붙인다.

- 특징

  - 상속관게 매핑이 아니다!

  - 엔티티가 아니며, 따라서 테이블과 매핑되는 것도 아니다!

  - 자식 클래스에 매핑 정보만 제공한다.

  - 조회 및 검색 불가(em.find(BaseEntity) 불가)

  - 추상 클래스 권장: 직접 생성해서 사용할 일이 없기 때문

  - 등록일, 수정일, 등록자, 수정자 등에서 유용

  - `@Entity` 붙은 클래스는 `@Entity` 혹은 `@MappedSuperclass` 붙은 클래스만 상속 가능하다.

    



## 프록시와 연관관계 관리

### 프록시

- 기초
  - `em.find()`와 `em.getReference()`
    - `em.find()`: DB 통해 실제 엔티티 객체를 조회
    
    - `em.getReference()`: DB조회를 미루는 가짜(프록시) 엔티티 객체를 조회(DB에 쿼리 안 나감)
    
    - 구체적으로 살펴보면,
    
      ```java
      	em.persist(member);
      
      	em.flush();
      	em.clear();
      
      	Member findMember = em.find(Member.class, member.getId());
      ```
    
      이 경우 jpa는 **MEMBER 테이블과 연결된 TEAM 테이블을 조인**해서 가지고 온다.
    
      
    
      반면 다음은
    
      ```java
      	Member findMember = em.getReference(Member.class, member.getId());
      ```
    
      `em.find()`대신 **`em.getReference()`**사용하니, 
    
      **SELECT 쿼리 자체가 나가지 않았다.**
    
      
    
      여기서 다음을 추가한다면
    
      ```java
      	System.out.println("findMember.id = " + findMember.getId());
      	System.out.println("findMember.name = " + findMember.getName());
      ```
    
      `em.find()` 사용할 때처럼 **조인 쿼리가 나간다.**
    
      이는 위 코드의 첫 행을 실행하는 시점이 아니라, 두 번째 행을 실행하는 시점에서 발생한다.
    
      id값의 경우 `getReference(Member.class, member.getId());`시에

- 세부사항
  - `em.getReference()`로 호출한 프록시 객체는 원본 엔티티 객체와는 다른 클래스이다. (JPA에서 생성)
  - 실제 클래스를 상속 받아서 만들어진다.
  - 프록시 객체는 실제 객체의 참조(target)를 보관
  - 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메서드 호출

-  프록시 객체의 초기화

  ```java
  Member member = em.getReference(Member.class, "id1");
  member.getName();
  ```

  1. `getName()`호출
  2. 초기화 요청: 멤버 프록시에 있는 Member target에 값이 없을 경우 JPA가 영속성 컨텍스트에 요청
  3. DB조회
  4. 실제 Entity 생성
  5. target의 `getName()`이용해서 값을 가져옴

- 프록시의 특징
  - 프록시 객체는 처음 사용할 때 한 번만 초기화
  - 프록시 객체를 초기화할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아님, 초기화되면 프록시 객체를 통해서 실제 엔티티에 접근 가능
  - 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의해야함(==비교가 실패함. 대신 instance of 사용해야 함.)
    - JPA에서 타입 비교할 때는 그냥 instance of 이용하자.
  - 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 `em.getReference()`를 호출해도 실제 엔티티 반환
    - JPA의 동일성 보장으로 인해서 instance == reference는 항진(reference가 instance.getId()로 조회한 것)
    - 이런 특성으로 인하여 프록시 조회를 한 이후에 find로 조회를 하더라도 동일성은 보장, 이 경우 프록시의 타입으로 둘 다 맞춰진다.
  - 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생 (하이버네이트는 **`org.hibernate.LazyInitializationException` **예외를 터트림)
    - `em.detach()`, `em.clear()`, `em.close()` 모두 그렇다.

- 프록시 확인
  - 프록시 인스턴스의 초기화 여부 확인
    - `PersistenceUnitUtil.isLoaded(Object entity)`
  - 프록시 클래스 확인 방법
    - `entity.getClass().getName() `출력(..javasist.. or HibernateProxy…) 
  - 프록시 강제 초기화
    - `org.hibernate.Hibernate.initialize(entity);`
    - 참고: JPA 표준은 강제 초기화 없음
    - 강제 호출: `member.getName()`

>
>

### 즉시 로딩과 지연 로딩

#### 지연 로딩

- `@ManyToOne(fetch = FetchType.LAZY)`
- 매핑된 타 테이블은 프록시로 조회한다.
  - Member에 연결된 Team은 프록시 조회
- 실제 매핑된 테이블의 값을 사용하는 시점에 초기화(DB조회)
  - 가령, `team.getName()`

- 만일 연결된 테이블을 자주 같이 조회한다면?
  - 즉시로딩 EAGER를 사용하라.



#### 즉시 로딩

- `FetchType.EAGER`
- JPA구현체는 가능한 처음부터 둘을 JOIN해서 쿼리 한 번에 조회



#### 프록시와 즉시 로딩 주의사항!

- **가급적 지연 로딩만 사용하라! 실무에선 더욱!**

- 즉시 로딩을 적용시 예상하지 못한 쿼리 발생

  - 테이블의 수가 많아지면 매우 치명적이다!

- **즉시 로딩은 JPQL에서 N+1 문제를 일으킨다.**

  ```java
              List<Member> members = em.createQuery("select m from Member m", Member.class)
                      .getResultList();
  ```

  - 이 경우 JPQL이 select * From Member; 과 같은 SQL로 번역되어 실행될 것이다.
  - EAGER이기에 Member의 Team을 구해와야 하기 때문에 조회한 수 만큼 다음과 같은 쿼리가 나갈 것이다:
    - select * from Team where TEAM_ID = xxx;
    - 첫 번째 Member를 가지고 오면 해당하는 Team 쿼리를 날림
    - 최초쿼리 1 + N개의 쿼리가 나가게 됨: N+1문제

  - 해결
    - 기본적으로 모두 지연로딩을 사용하고, 추가적으로
    - fetch join
      - `"select m from Member m join fetch m.team"`
    - batch size

- 기본값

  - `@ManyToOne`, `@OneToOne`은 즉시 로딩
    - **LAZY로 수정하라!**
  - `@OneToMany`, `@ManyToMany`는 기본이 지연 로딩

- 활용
  - 모든 연관관계에 지연 로딩을 사용!
  - 실무에서는 즉시 로딩은 절대 안 된다!
  - JPQL fetch 조인이나 엔티티 그래프 기능을 사용
  - 즉시 로딩은 상상하지 못 한 쿼리가 나감



### 영속성 전이(CASCADE)와 고아 객체

#### 영속성 전이: CASCADE

- 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들고 싶을 경우 사용

  - 예: 부모 엔티티를  저장할 때 자식 엔티티도 같이 저장

    ```java
    @Entity
    public class Parent {
        ...
        @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
        private List<Child> children = new ArrayList<>();
        ...
    }
    ```

    - 이 경우 `em.persist(parent)`만으로도 `em.persist(child1)` `em.persist(child2)`까지 실행된다.

- 주의사항
  - 연속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
  - 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐
  - 완전히 종속적일 때, 단일 소유자일 때 사용
    - 특히 ALL의 경우 생명주기가 완전히 동일함을 의미한다.



#### 고아 객체

- 고아 객체 제거: 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
- `orphanRemoval = true`
- 주의
  - 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 보고 삭제하는 기능
  - **참조하는 곳이 하나일 때 사용해야 함!**
  - 특정 엔티티가 개인 소유할 때 사용
  - `@OneToOne`, `@OneToMany`만 가능
  - 부모를 제거할 시 자식도 함께 사라진다는 점은 `CascadeType.REMOVE`와 똑같이 이해된다.



#### 영속성 전이 + 고아 객체, 생명 주기

- `CascadeType.ALL + orphanRemoval = true`
- 스스로 생명주기를 관리하는 엔티티는 `em.persist()`로 영속화, `em.remove()`로 제거
- 두 옵션을 모두 활성화할 경우 부모 엔티티로 자식의 생명주기를 관리할 수 있다.
- 도메인 주도 설계(DDD)의 Aggregate Root 개념을 구현할 때 유용





## 값 타입

#### JPA의 데이터 타입 분류

- ##### 엔티티 타입

  - `@Entity`로 정의하는 객체
  - 데이터가 변해도 식별자로 지속해서 추적 가능

- ##### 값 타입

  - int, Integer, String처럼 단순히 값으로 사용하는 자바 기본 타입이나 객체
  - 식별자가 없고 값만 있으므로 변경시 추적 불가
  - 값 타입을 소유한 엔티티에 생명주기를 의존함



#### 값 타입 분류

- 기본값 타입
  - 자바 기본 타입(int, double)
  - 래퍼 클래스(Integer, Long)
  - String
- 임베디드 타입(embedded type, 복합 값 타입)
- 컬렉션 값 타입(collection value type)



### 기본값 타입

- 생명주기를 엔티티에 의존한다.
- 값 타입은 공유하면 안 된다.
  - 회원 이름을 변경시 다른 회원의 이름도 함께 변경되어선 안 된다.
- 자바의 기본 타입(primitive type)은 절대 공유되지 않는다.
  - 기본 타입은 항상 값을 복사함
    - `int a, b에 대하여 a = b;`는 값을 복사하는 것이지, 공유가 아니다. (공유라면 b값을 변경하면 a값이 변경된다.)
  - 래퍼 클래스나 String 같은 특수한 케이스는 공유 가능한 객체지만 변경 불가
    - 클래스는 참조값을 저장하기 때문에 Integer b에 대하여 Integer a = b;는 값의 공유가 발생한다.
    - 하지만 Integer를 변경할 방법이 없다 -> side effect는 없다.



### 임베디드 타입(복합 값 타입)

- 새로운 값 타입을 직접 정의할 수 있음

  - 예) 거주 도시, 도로명, 우편번호를 합쳐서 주소로 만드는 것

- JPA에서는 임베디드 타입이라고 함

- 주로 기본 값 타입을 모아서 만들어지기에 복합 값 타입이라고도 함

- 값 타입으로 취급됨(엔티티 아님, 추적 안 됨!)

- 임베디드 타입의 값이 null이면 매핑한 컬럼 값은 모두 null

- 사용

  - `@Embeddable`: 값 타입을 정의하는 곳에 표시

  - `@Embedded`: 값 타입을 사용하는 곳에 표시

  - 기본 생성자 필수

    ```java
    @Entity
    public class Member {
        ...
        @Embedded
        private Period period;
        ...
    }
    ```

    ```java
    @Embeddable
    public class Period {
    
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        
        public boolean isWork() {
            ...
        }
    
        public Period() {
        }
    }
    ```

- 장점
  - 재사용
  - 높은 응집도
  - `Period.isWork()`처럼 해당 값 타입만 사용하는 의미 있는 메서드를 만들 수 있음
  - 값 타입이기에 그것을 소유한 엔티티에 생명주기를 의존함
- 임베디드 타입과 테이블 매핑
  - 테이블은 임베디드 타입을 도입해도 변화가 없지만(임베디드 타입은 엔티티의 값일 뿐이니까)
  - 객체의 경우, 상태, 행위를 포함하기에 이런 값 타입의 사용으로 인한 이점이 있다. (객체지향적 설계)
    - 객체와 테이블을 아주 세밀하게(find-grained) 매핑하는 것이 가능
  - 잘 설계한 ORM 어플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많음



#### 임베디드 타입과 연관관계

- 임베디드 타입은 그 필드에 엔티티를 둘 수 있음(FK 이용)

- ##### `@AttributeOverride`: 속성 재정의

  - 한 엔티티에서 같은 값 타입을 사용할 경우, 컬럼명이 중복됨

  - `@AttributesOverrides`, `@AttributeOverride`를 사용해서 컬럼명, 속성을 재정의한다.

    ```java
        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name="city",
                column = @Column(name = "WORK_CITY")),
                @AttributeOverride(name="street",
                column = @Column(name = "WORK_STREET")),
                @AttributeOverride(name="zipcode",
                column = @Column(name = "WORK_ZIPCODE"))
        })
        private Address workAddress;
    ```



### 값 타입과 불변 객체

> 값 타입은 복잡한 객체 세상을 조금이라도 단순화하려고 만든 개념이다. 따라서 값 타입은 단순하고 안전하게 다룰 수 있어야 한다.



- 값 타입 공유 참조

  - 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험함
  - **부작용(side effect) 발생**

  ```java
  {
      ...
      em.persist(address);
      
      member1.setHomeAddress(address);
  	em.persist(member1);
  	
  	member2.setHomeAddresss(address);
  	em.persist(member2);
  	
      ...
      member2.getHomeAddress().setCity("newCity");
      ...
  }
  ```

  - 위와 같은 상황이라면 member1, member2 모두 address를 자신의 HomeAddress로 같이 가지고 있기 때문에
    member2의 필드에 있는 address 값을 변경하면 member1의 값도 같이 변경되어, update 쿼리가 나가게 된다.

- 값 타입 복사
  - 값 타입의 실제 인스턴스인 값을 공유하는 것은 위험
  - 대신, **값(인스턴스)을 복사해서 사용하라!**
    - 위의 예시에서는 둘 다 address를 사용하는 것이 아니라, member2의 경우에는 address의 값을 복사한 address2를 사용하는 식으로 사용해야 한다.

- 객체 타입의 한계

  - 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있다.
  - 문제는 임베디드 타입처럼 **직접 정의한 값 타입은 자바의 기본 타입이 아니라 객체 타입**이다.
  - 기본형(primitive type)과 참조형 자료형의 차이
    - 자바 기본 타입에 값을 대입하면 값을 복사한다.
    - **객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없다.**
  - **객체의 공유 참조는 피할 수 없다.**
  - **불변 객체를 사용하라!**

  

#### 불변 객체

- 객체 타입을 수정할 수 없게 만들면 부작용을 원천 차단
- **값 타입은 불변 객체(immutable object)로 설계해야함**
- **불변 객체: 생성 시점 이후 절대 값을 변경할 수 없는 객체**
- 생성자로만 값을 설정하고 수정자(setter)를 만들지 않거나, private으로 만들면 됨
- 참고: Integer, String은 자바가 제공하는 대표적인 불변 객체



> 불변이라는 작은 제약으로 부작용이라는 큰 재앙을 막을 수 있다.



- 불변 객체를 사용할 때 수정은
  - 값 타입의 새로운 인스턴스를 만들고 그것으로 기존 값 타입 전체를 대체한다.
    - member의 address -> 수정할 값을 수정한 newAddress로 set



### 값 타입의 비교

- 인스턴스가 달라도 그 안에 값이 같으면 같은 것으로 봐야 한다. (a==b가 true 리턴해야)

  ```java
  int a = 10;
  int b = 10;
  ```

  ```java
  Address a = new Address("서울특별시");
  Address b = new Address("서울특별시");
  ```

- **동일성(identity) 비교**: 인스턴스의 참조 값을 비교 == 연산자 사용
- **동등성(equivalence) 비교** 인스턴스의 값을 비교, `equals()` 사용
- 값 타입은 `a.equals(b)`를 사용해서 동등성 비교를 해야 함
- 값 타입의 `equals()` 메서드를 적절하게 재정의 (주로 모든 필드 사용)



### 값 타입 컬렉션

- 값 타입을 하나 이상 저장할 때, 컬렉션에 넣어서 사용

- `@ElementCollection`, `@CollectionTable`사용

- RDB는 기본적으로 컬렉션을 담는 것이 아니라 개별 값을 담기 때문에 컬렉션을 저장하기 위한 별도의 테이블이 필요

  ```java
  public class Member {
      @ElementCollection
      @CollectionTable(name = "FAVORITE_FOOD", joinColumns =
              @JoinColumn(name = "MEMBER_ID")
      )
      @Column(name = "FOOD_NAME")
      private Set<String> favoriteFoods = new HashSet<>();
  
      @ElementCollection
      @CollectionTable(name = "ADDRESS", joinColumns =
              @JoinColumn(name = "MEMBER_ID")
      )
      private List<Address> addressHistory = new ArrayList<>();
  }
  
  ```

- 값 타입 컬렉션도 fetch 기본값은 지연 로딩

- 값 타입 컬렉션은 생명주기를 엔티티에 의존

  - 참고: 값 타입 컬렉션은 영속성 전이(cascade) + 고아 객체 제거 기능을 필수로 가진다고 볼 수 있다.

- 값 타입 컬렉션 변경 시

  ```java
  findMember.getAddressHistory().remove(new Address("oldCity", "street", "zipcode"));
  findMember.getAddressHistory().add(new Address("newCity", "street", "zipcode"));
  ```

  - 불변 타입으로 사용해야 하기에 위와 같이 한다.
  - 그런데 **remove 작업 시 기존 입력된 것과의 동일성을 판단해서 지울 것이기 때문에 `equals()` 등 메서드를 적절히 오버라이딩 하는것이 중요하다!**

- 값 타입 컬렉션의 제약사항
  - 값 타입은 엔티티와 다르게 식별자 개념이 없다.
  - 값은 변경하면 추적이 어렵다.
  - **값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.**
  - **값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본 키를 구성해야함: null 입력X, 중복 저장X**

- 값 타입 컬렉션의 대안

  - 실무에서는 상황에 따라 **값 타입 컬렉션 대신에 일대다 관계를 고려한다.**

  - 일대다 관계를 위한 엔티티를 만들고, 여기에서 값 타입을 사용
  - 영속성 전이 + 고아 객체 제거를 사용하여 값 타입 컬렉션처럼 사용한다.
  - Ex) Address Entity
    - 영속성 전이, 고아객체 제거 사용
    - Id
    - Address를 래핑

- 정리

  - 엔티티 타입의 특징
    - 식별자 존재
    - 생명 주기 관리
    - 공유 가능
  - 값 타입의 특징
    - 식별자 없음
    - 생명 주기를 엔티티에 의존
    - 공유하지 않는 것이 안전
    - 불변 객체로 만드는 것이 안전

  - 값 타입은 정말 값 타입이라 판단될 때만 사용
  - 엔티티와 값 타입을 혼동해서 엔티티를 값 타입으로 만들면 안 됨.
  - 식별자가 필요하고, 지속해서 값을 추적, 변경해야 한다면 그것은 값 타입이 아닌 엔티티이다.



####  `equals()`와 `hashCode()` 오버라이딩에 관하여

- 값 타입은 불변 객체로 사용해야 하기에, 각각 개별 인스턴스로 만들어야 한다.

- 그런데 `remove()`등 사용할 때, 기존 인스턴스와의 동일성을 판단하기 위해서 `equals()`와 `hashCode()`를 재정의해야 한다.

- getter/setter 처럼 generate 단축키(Alt + Ins)를 이용해 간단히 생성 가능하다.

- 주의할 점은 아래의 옵션을 InteliJ에서 체크함이 좋다.

  

generate equals() and hashCode()

- [ ] Accept subclasses as parameter to equals() method

While generally incompliant to Object.equals() specification accepting subclasses might be nessesary for generated method to work correctly with frameworks, which generate Proxy subclasses like Hibernate.

생성된 메서드가 하이버네이트처럼 프록시 서브클래스들을 만드는 프레임워크들과 호환시키고 싶으시다면,
일반적인 Object.equals() 규격과 다르게 서브클래스들을 포함시키는 것이 필요할 수 있습니다.

- [x] Use getters during code generation
- 해당 클래스에 getter 만들지 않으면 당연히 위 옵션을 체크하더라도 getter 메서드 사용하지 않고 구현된다.

- 왜 getter를 사용해야 하는가?
  - getter를 생성, private 하게 만들면 값 타입을 불변 객체로 만들 수 있기 때문이다.





## 객체지향 쿼리 언어1 - 기본 문법

### JPA의 다양한 쿼리 방식

- **JPQL**: 표준 문법
- JPA Criteria: 자바 코드로 짜고, 그것을 generate 해주는 방식(QueryDSL도 마찬가지)
- **QueryDSL**
- 네이티브 SQL: 특정 DB에 종속적인 쿼리를 날려야 할 때
- JDBC API 직접 사용, MyBatis, SpringJdbcTemplate 함께 사용



#### JPQL

- JPQL의 필요성

  - 가장 단순한 조회 방법
    - `em.find()`
    - 객체 그래프 탐색(`a.getB().getC()`)
    - 그러나 검색은 어떻게?
  - JPA를 사용하면 엔티티 객체를 중심으로 개발
    - 검색을 할 때도 **테이블이 아닌 엔티티 객체를 대상으로 검색**
    - 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
    - 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요

- 특징

  - 테이블이 아닌 객체를 대상으로 검색하는 객체지향 쿼리
    - SQL은 데이터베이스 테이블을 대상으로 쿼리
  - JPA는 SQL을 추상화한 JPQL이라는 객체지향 쿼리 언어 제공
    - 특정 특정 데이터베이스 SQL에 의존하지 않는다.
  - SQL과 유사한 문법
    - SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원
  - 예시

  ```java
              ...
              List<Member> resultList = em.createQuery(
                      "select m From Member m where m.username like '%kim%'",
                      Member.class
              ).getResultList();
              ...
  ```



#### Criteria

- JPQL은 사실 단순 String이기에 동적 쿼리를 만들기 어렵다.

  반면, criteria는 동적 쿼리를 만들기 용이하다.

- JPA 공식 기능으로 JPQL 빌더 역할을 수행한다.

- 문자가 아닌 자바 코드이기에 오타 등을 잡아내기 좋다.

- 단점

  - SQL답지 못함
  - 직관성이 떨어져서 유지보수가 힘듦

- QueryDSL 사용을 권장

- 예시

  ```java
  //Criteria 사용 준비
  CriteriaBuilder cb = em.getCriteriaBuilder();
  CriteriaQuery<Member> query = cb.createQuery(Member.class);
  
  //루트 클래스 (조회를 시작할 클래스)
  Root<Member> m = query.from(Member.class);
  
  //쿼리 생성 CriteriaQuery<Member> cq =
  query.select(m).where(cb.equal(m.get("username"), “kim”));
  List<Member> resultList = em.createQuery(cq).getResultList();
  ```

  

#### QueryDSL

- 특징

  - 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
    - 컴파일 시점에 문법 오류를 찾을 수 있음
  - JPQL 빌더 역할
  - 동적 쿼리 작성 편리함
  - 실무 사용 권장
  - 예시

  ```java
          JPAFactoryQuery query = new JPAQueryFactory(em);
          QMember m = QMember.member;
          
          List<Member> list =
                  query.selectFrom(m)
                          .where(m.age.gt(18))
                          .orderBy(m.name.desc())
                          .fetch();
  ```



#### 네이티브 SQL

- SQL을 직접 사용하게끔 JPA가 제공하는 기능
- JPQL로 해결할 수 없는 특정 데이터베이스에 의존적인 기능
  - 오라클의 CONNECT BY 처럼 특정 DB만 사용하는 SQL 힌트

- 예시

  ```java
  String sql =
   “SELECT ID, AGE, TEAM_ID, NAME FROM MEMBER WHERE NAME = ‘kim’";
  List<Member> resultList =
   em.createNativeQuery(sql, Member.class).getResultList();
  ```

  

#### 기타

- JPA를 사용하면서 JDBC 커넥션을 직접 사용하거나 스프링 JdbcTemplate, MyBatis 등을 함께 사용할 수 있다.
- 단 영속성 컨텍스트를 적절한 시점에 강제로 플러시할 필요가 있다.
  - 예) JPA를 우회해서 SQL을 실행하기 직전에 영속성 컨텍스트를 수동 플러시



### JPQL(Java Persistence Query Language)

#### 기본 문법과 기능

```SQL
select_문 :: =
 select_절
 from_절
 [where_절]
 [groupby_절]
 [having_절]
 [orderby_절]
 
update_문 :: = update_절 [where_절]
delete_문 :: = delete_절 [where_절]
```

- 기본 문법
  - 엔티티와 속성은 대소문자를 구분한다. (Member, age)
  - JPQL 키워드는 대소문자를 구분하지 않는다. (SELECT, From, where)
  - **엔티티 이름을 사용함, 테이블 이름 아님!**
  - **별칭은 필수** (as는 생략 가능)

- 집합과 정렬

  ```SQL
  select
   COUNT(m), //회원수
   SUM(m.age), //나이 합
   AVG(m.age), //평균 나이
   MAX(m.age), //최대 나이
   MIN(m.age) //최소 나이
  from Member m
  ```

  - GROUP BY, HAVING, ORDER BY도 마찬가지


##### TypeQuery/Query

- TypeQuery: 반환 타입이 명확할 때 사용
- Query: 반환 타입이 명확하지 않을 때 사용

##### 결과 조회 API

- `query.getResultList()`
  - 결과가 **하나 이상**일 때, 리스트 반환
  - 결과가 없으면 빈 리스트 반환 (Exception 발생 안 함)
- `query.getSingleResult()`
  - 결과가 **정확히 하나**, 단일 객체 반환
  - 결과가 없을 경우 `javax.persistence.NoResultException`
  - 결과가 둘 이상일 경우 `javax.persistence.NonUniqueResultException`
  - Spring Data JPA에서는, 위와 달리 이를 추상화하여, 결과가 없을 경우 null이나 Optional(신버전)로 반환한다.

##### 파라미터 바인딩 - 이름 기준, 위치 기준

- 이름 기준

```java
em.createQuery("select m from Member m where m.username = :username", Member.class);
        boundParameterQuery.setParameter("username", "member2");      
```

- 위치 기준

```java
em.createQuery(
                "select m from Member m where m.username=?1", Member.class)
                .setParameter(1, "member1");
```

위치 기준은 사용을 권장하지 않는다. 순서가 밀릴 수 있기 때문이다.



#### 프로젝션

- SELECT절에 조회할 대상을 지정하는 것
- 프로젝션 대상: 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자 등 기본 데이터 타입)
  - RDB는 스칼라 타입만 선택 가능

- 예시

  - 엔티티 프로젝션
    - `SELECT m FROM Member m`
    - `SELECT m.team FROM Member m`

  - 임베디드 타입 프로젝션: `SELECT m.address FROM Member m`
  - 스칼라 타입 프로젝션: `SELECT m.username, m.age FROM Member m`

- DISTINCT로 중복 제거(`SELECT DISTINCT`)



##### 프로젝션의 종류

- **엔티티 프로젝션**
  - **SELECT절의 대상(아래에서 result의 Member)들은 모두 영속성 컨텍스트에 의해 관리되는 대상이다.**

  ```java
          List<Member> result = em.createQuery("SELECT m FROM Member m", Member.class)
                  .getResultList();
  
          Member foundMember = result.get(1);
          foundMember.setAge(50);
  ```

  때문에 foundMember의 변경에 대해서 UPDATE 쿼리가 나간다.

  - **묵시적 조인과 명시적 조인**

    - 조인이 필요한 경우, 실제 쿼리에서는 조인이 나간다. (**묵시적 조인**)

    ```java
            List<Team> teamList = em.createQuery("SELECT  m.team FROM Member m", Team.class).getResultList();
    ```

    조인은 성능 문제와 연관이 깊고, 튜닝할 경우도 많기에 다음을 권장한다: (**명시적 조인**)

    ```java
            List<Team> teamList = em.createQuery("SELECT t FROM Member m join m.team t", Team.class).getResultList();
    
    ```

    이렇게 되야 코드를 보고 조인 쿼리가 나간다는 것을 분명히 예상할 수 있다.

- **임베디드 타입 프로젝션**

```java
        List<Address> result = em.createQuery("select o.address from Order o", Address.class).getResultList();
```

임베디드 타입 프로젝션의 경우 값 타입의 특성상 특정 엔티티에 소속해있을수밖에 없기에 `o.address`식으로 엔티티 소속을 표현해줘야 한다.

- **스칼라 타입 프로젝션**

```java
        List result = em.createQuery("SELECT DISTINCT m.username, m.age FROM Member m").getResultList();
```



##### 여러 값을 조회하는 방법

1. Query 타입으로 조회

   ```java
       public static void multipleValueProjection(EntityManager em) {
           List result = em.createQuery("SELECT DISTINCT m.username, m.age FROM Member m").getResultList();
   
           Object o1 = result.get(0);   // username
           Object o2 = result.get(1);   // age
       }
   ```

   

2. Object[] 타입으로 조회

   ```java
            List<Object[]> result = em.createQuery("SELECT DISTINCT m.username, m.age FROM Member m").getResultList();
   ```

3. new 명령어로 조회

   ```java
   public class MemberDTO {
       
       private String username;
       private Integer age;
   }
   ```

   를 사용하여

   ```java
           List<MemberDTO> resultList = em.createQuery(
                   "SELECT new cogitans.jpa_jpql.MemberDTO(m.username, m.age) FROM Member m", MemberDTO.class)
                   .getResultList();
   ```

   형태로 조회 가능하다. (생성자 사용과 유사)

   - 단순 값을 DTO로 바로 조회
   - 패키지명을 포함한 전체 클래스명 입력
   - 순서와 타입이 일치하는 생성자가 필요



#### 페이징 API

- JPA가 페이징을 추상화한 두 API
  - `setFirstResult(int startPositon)`: 조회 시작 위치(0부터 시작)
  - `setMaxResults(int maxResult)`: 조회할 데이터 수

- 예시

  ```java
          List<Member> result = em.createQuery("SELECT m FROM Member m ORDER BY m.age DESC", Member.class)
                  .setFirstResult(0)
                  .setMaxResults(15)
                  .getResultList();
  ```

- 방언에 따라 서로 다른 전략



#### 조인

- SQL조인과 실행 자체는 동일하지만, 객체 스타일이다.
- 내부 조인: `SELECT m FROM Member m [INNER] JOIN m.team t`
- 외부 조인: `SELECT m FROM Member m LEFT [OUTER] JOIN m.team t`
- 세타 조인: `select count(m) from Member m, Team t where m.username = t.name`
  - 연관관계가 없는 것을 비교하고 싶을 때

- ON 절

  - ON절을 활용한 조인(JPA 2.1부터 지원)

    1. 조인 대상 필터링

       - 예시) 회원과 팀을 조인하면서 팀 이름이 A인 팀만 조인

         - JPQL

           ```SQL
           SELECT m FROM Member m LEFT JOIN m.team t ON t.name ='A'
           ```

         - SQL

           ```sql
           SELECT m.*, t.* FROM
           Member m LEFT JOIN Team t ON m.TEAM_ID=t.id AND t.name='A'
           ```

           

    2. 연관관계 없는 엔티티 외부 조인(하이버네이트 5.1부터)

       - 예시) 회원의 이름과 팀의 이름이 같은 대상 외부 조인

         - JPQL

           ```SQL
           SELECT m FROM
           Member m LEFT JOIN Team t ON m.username = t.name
           ```

         - SQL

           ```sql
           SELECT m.*, t.* FROM
           Member m LEFT JOIN Team t ON m.username = t.name
           ```



#### 서브쿼리

- 예시

  - 나이가 평균보다 많은 회원

    ```SQL
    SELECT m FROM Member m
    WHERE m.age > (SELECT AVG(m2.age) FROM Member m2)
    ```

    메인쿼리와 서브쿼리의 대상이 따로, 이렇게 해야 성능이 나음

  - 한 건이라도 주문한 고객

    ```sQL
    SELECT m FROM Member m
    WHERE (SELECT COUNT(o) FROM Order o WHERE m = o.member) > 0
    ```

    서브쿼리에서 다른 엔티티를 끌어 들여옴, 성능상 불리함

##### 서브쿼리 지원 함수

- `[NOT] EXISTS (subquery)`: 서브쿼리에 결과가 존재하면 참
  - `{ALL|ANY|SOME} (subquery)`
  - `ALL` 모두 만족시 / `ANY = SOME`: 조건 중 하나를 만족

- `[NOT] IN (subquery)`: 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참

- JPA 서브쿼리 한계
  - JPA는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능
  - SELECT 절도 가능 (하이버네이트에서만 지원)
  - **FROM 절의 서브쿼리는 현재 JPQL에서 불가능**
    - **조인으로 풀 수 있으면 풀어서 해결**



#### JPQL 타입 표현

- 문자: 'HELLO', 'She''s'

- 숫자: 10L(Long), 10D(Double), 10F(Float)

- Boolean: TRUE, FALSE

- `cogitans.jpa_jpql.domain.MemberType.ADMIN` **(패키지명을 포함해야!)**

  - 단 파라미터 바인딩을 이용해서 좀 더 단순하게 사용 가능:

    ```java
            String query = "SELECT m.username, 'HELLO', true FROM Member m" +
                    "WHERE m.type = :userType";
    
            List<Object[]> result = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
    ```

- 엔티티 타입: TYPE(m) = Member (상속 관계에서 사용)

  ```SQL
          em.createQuery("SELECT i FROM Item i WHERE TYPE(i) = Book", Item.class);
  ```



#### JPQL 기타

- SQL과 문법이 같은 식
  - EXIST, IN
  - AND, OR, NOT
  - =, >, >=, <=, <>
  - BETWEEN, LIKE, IS NULL



#### 조건식 - CASE 식

- 기본 CASE 식

  ```sql
  SELECT
  	CASE WHEN m.age <= 10 then '학생요금'
  		 WHEN m.age >= 60 then '경로요금'
           ELSE '일반요금'
      END
  FROM Member m
  ```

- 단순 CASE 식

  ```sql
  SELECT
  	CASE t.name
  		WHEN '팀A' then '인센티브110%'
  		WHEN '팀B' then '인센티브120%'
  		ELSE '인센티브105%'
  	END
  FROM Team t
  ```

- COALESCE: 하나씩 조회해서 null이 아니면 반환

  - 예시: 사용자 이름이 없으면 이름 없는 회원을 반환

    ```SQL
    SELECT COALESCE(m.username, '이름 없는 회원' AS username FROM Member m
    ```

- NULLIF: 두 값이 같으면 null 반환, 다르면 첫 번째 값 반환

  - 예시; 사용자 이름이 '관리자'면 null을 반환하고 나머지는 본인의 이름을 반환

    ```sql
    SELECT NULLIF(m.username, '관리자') AS username FROM Member m
    ```



#### JPQL 기본함수

- JPQL 표준 함수: DB에 무관하게 그냥 사용하면 된다.
  - CONCAT

    ```sql
            String query = "SELECT 'a' || 'b' FROM Member m";
            String query = "SELECT CONCAT('a', 'b') FROM Member m";
    ```

    `s = ab` 출력

    '||'을 사용하는 방식은 하이버네이트에서만,

    CONCAT은 표준 함수

  - SUBSTRING

  - TRIM

  - LOWER, UPPER

  - LENGTH

  - LOCATE

    ```sql
    SELECT LOCATE('de', 'abcdefg') FROM Member m
    ```

  - ABS, SQRT, MOD

  - SIZE, INDEX(JPA 용도)

    - SIZE

      ```sql
      SELECT SIZE(t.members) FROM Member m
      ```

      컬렉션의 크기

    - INDEX

- 사용자 정의 함수

  - 하이버네이트는 사용 전 방언에 추가해야 한다.

    - 사용하는 DB 방언을 상속받고, 사용자 정의 함수를 등록한다.

      ```java
      public class CustomH2Dialect extends H2Dialect {
      
          public CustomH2Dialect() {
              registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
          }
      ```

    - `persistence.xml`에 위를 등록한다

    - 다음과 같이 사용한다.

      ```sql
      SELECT FUNCTION('group_concat', m.username) FROM Member m
      ```

      출력결과: `s = `member0,member1,member2,member3,member4`

    - 하이버네이트의 경우 다음과 같이 표현 가능하다.

      ```sql
      SELECT group_concat(m.username) FROM Member m
      ```

      

