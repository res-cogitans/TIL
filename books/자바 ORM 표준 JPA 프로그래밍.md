# 자바 ORM 표준 JPA 프로그래밍

# 01장 JPA 소개

- JPA를 사용하지 않을 경우
  - 반복적인 쿼리 작성으로 인해 생산성이 떨어진다.
  - 객체지향적 설계를 할 수록 DB 저장은 불편해진다.
    - 결과적으로 더 많은 쿼리를 작성하게 되며
    - 객체 모델링을 포기하고 데이터 중심 모델로 돌아가게 된다.

- ORM; Object-Relational Mapping 프레임워크
  - 객체와 RDB 사이의 괴리를 해결해주는 역할
  - JPA: 자바 표준 ORM 기술

- JPA 도입 시
  - 단순 쿼리문 작성할 필요 없음 -> 생산성 증가
  - 객체 중심 개발 가능 -> 생산성, 유지보수 향상
  - RDBMS 변경에 유연하게 대응 가능(DB 방언)



## 1.1 SQL을 직접 다룰 때 발생하는 문제점

- 자바 애플리케이션은 JDBC API를 사용하여 SQL을 DB에 전달하여 DB를 사용한다.



### 1.1.1 JDBC를 사용한 개발의 예시

```java
public class Member {
    
    private String memberId;
    private String name;
}
```

```java
public class MemberDAO {
    
    public Member find(String memberId){...}
    public void save(Member member) {...}
}
```

- MemberDAO의 개발 과정

  - `find()` 메서드 개발

    1. 회원 조회용 SQL 작성

    ```sql
    SELECT MEMBER_ID, NAME FROM MEMBER M WHERE MEMBER_ID = ?
    ```

    2. JDBC API를 이용하여 SQL 실행

       ```java
       Resultset re = stmt.excuteQuery(sql);
       ```

    3. 조회 결과를 `Member` 객체로 매핑

    ```java
    String memberId = rs.getString("MEMBER_ID");
    String name = rs.getString("NAME");
    
    Member member = new Member();
    member.setMemberId(memberId);
    member.setName(name);
    ...
    ```

  - `save()` 메서드 개발
    1. 회원 등록용 SQL 작성
    2. 객체 필드를 꺼내서 등록 SQL에 전달
    3. JDBC API로 SQL 실행

- **문제의 원인: 객체 모델과 DB 모델의 괴리**

  - 동일한 기능을 수행하는 `List`의 경우 `add()` 메서드 한 번으로 객체를 저장할 수 있다.

  - 하지만 객체 모델은 DB 모델과 다르기 때문에
    - 간단하게 객체를 직접 저장할 수 없고
    - 개발자가 SQL과 JDBC API로 직접 변환을 수행해야 한다.
    - **무의미한 반복!**



### 1.1.2 SQL 의존적인 개발

- 회원 객체에 대해 연락처 저장 요구사항이 발생했다고 생각해 보자.

  - 회원 등록과 관련하여

    - 회원 테이블에 TEL 컬럼 추가

    - 회원 객체에 tel 필드 추가

    - INSERT SQL 수정

      - 회원 객체의 연락처 값을 전달

        ```java
        pstmt.setString(3, member.getTel());
        ```

  - 회원 조회와 관련하여 추가 작업 필요
    - 조회용 SQL 수정
    - 조회 결과를 Member 객체에 매핑
  - 회원 수정과 관련하여 추가 작업 필요
    - 마찬가지로 반복적인 작업 필요

- **지나치게 많은 부분을 수정해야**

  - 정작 자바 `Collection`처럼 객체 단위 관리가 가능했다면 이런 일은 없었다.

- 연관된 객체
  - 추가 요구사항: 회원이 특정 한 팀에 필수로 소속되어야 한다
  - 기존의 회원 조회 SQL을 그대로 사용해야 하기 때문에
    회원을 함께 조회해야 하는 `findWithTeam()`메서드를 사용해야 팀 정보까지 얻어올 수 있다.
  - 실수하기 좋다.
  - 연관관계가 증가할수록 위와 같은 메서드는 증가한다.



#### 애플리케이션에서 SQL을 직접 다룰 때의 문제점

- SQL에 모든 것을 의존하는 상황이다.
- DAO에 SQL을 은닉해도 어떤 SQL이 실행되는가를 확인하기 위해서 재확인해야 한다.
- 물리적으로는 SQL과 JDBC API가 데이터 접근 계층에 은닉되었지만
  논리적으로는 엔티티와 강한 의존관계를 갖는다.
  - 엔티티를 신뢰할 수 없는 상황이다.
  - 진정한 의미의 계층 분할이 어렵다.
  - SQL 의존적 개발을 피할 수 없다.



### 1.1.3 JPA와 문제 해결

- JPA식으로 기존 SQL 의존적 개발의 문제점들을 어떻게 해결하는지 보자.

- CRUD의 예

  ```java
  jpa.persist(member);								//저장
  ...
  String memberId = "helloId";
  Member member1 = jpa.find(Member.class, memberId);	//조회
  ...
  Member member2 = fpa.find(Member.class, memberId);
  member.setName("변경된 이름");						//수정
  ...
  Member member3 = jpa.find(Member.class, memberId);
  Team team = member.getTeam();						//연관된 객체 조회
  ```

  JPA는 연관된 객체를 사용하는 시점에 적절한 SELECT SQL을 실행시키기 때문에
  위와 같이 객체를 객체처럼 다룰 수 있게 된다.



## 1.2 패러다임의 불일치

- 애플리케이션의 복잡성은 점차 증가한다. 증가하는 복잡성을 제어하지 못하면 유지보수가 어려워진다.
  - 객체지향 프로그래밍의 도구들: 추상화, 캡슐화, 정보은닉, 상속, 다형성 등
    복잡성을 제어하기위한 수단이다.
  - 도메인 모델을 객체로 모델링하면 객체지향 언어의 장점을 활용할 수 있지만,
    이 데이터를 **저장할 때 문제가 생긴다.**

- 필드의 저장은 문제가 없지만,
  **부모 객체를 상속받았거나, 다른 객체를 참조하고 있다면 저장이 어렵다.**

  - 자바가 제공하는 저장 기능
    - 직렬화: 객체를 파일로 저장하는 기능
    - 역직렬화: 저장된 파일을 객체로 복구하는 기능
    - 문제: **직렬화된 객체는 검색이 어렵다!**

  - RBD에 저장하기

    - 현실적인 방안이지만 난점이 많다.
      - 데이터 중심으로 구조화되어 있다.
      - 집합적인 사고를 요구한다.
      - 객체지향의 개념(추상화, 상속, 다형성 등)이 없음

    - 객체와 RDB는 지향적이 다르기에 기능과 표현 방법이 다름
      **객체와 관계형 데이터베이스(RDB)의 패러다임 불일치 문제**
      -> 객체 구조를 테이블 구조에 저장하는 데는 한계가 있다.

- 패러다임 불일치를 해소하기 위해
  - 중간에서 개발자가 작업해야 한다.
  - **문제: 지나치게 많은 시간과 코드를 소모한다!**



### 1.2.1 상속

- 객체에는 상속의 개념이 있지만 테이블에는 상속 개념이 없다.

  - 그나마 슈퍼타입-서브타입 관계를 이용하여 상속과 유사한 형태로 설계가 가능하기는 하다. (DTYPE)

- 예시: `Item`과 이를 `extends`한 `Album` 객체

  - 저장의 경우 2개의 SQL을 별도로 보내야 한다.

    ```SQL
    INSERT INTO ITEM ...
    INSERT INTO ALBUM ...
    ```

  - 조회의 경우  각 테이블을 조인해서 조회하고 그 결과로 `Album` 객체를 생성해야 한다.



#### JPA와 상속

- 개발자는 자바 컬렉션에 객체를 저장하듯이 JPA를 사용하면 된다.

  - 저장의 경우

    ```java
    jpa.persist(album);
    ```

    이렇게 되면 JPA가 필요한 SQL을 실행하여 각 테이블에 정보를 저장해 준다.

  - 조회의 경우

    ```java
    String albumId = "id100";
    Album album = jpa.find(Album.class, albumId)
    ```

    JPA가 테이블을 조인하여 필요 데이터를 조회, 결과를 반환한다.



### 1.2.2 연관관계

- 객체는 참조를 사용하여 다른 객체와 연관관계를 갖고, 연관된 객체를 조회한다.
- 테이블은 외래 키를 사용하여 다른 테이블과 연관관계를 갖고, 조인을 사용해서 연관된 테이블을 조회한다.

- 객체는 참조 방향으로만 조회가 가능하지만, 테이블은 외래 키 하나로 반대 반향 조인도 가능하다.



#### 객체를 테이블에 맞추어 모델링

- 만일 테이블 방식으로 객체를 모델링한다면
  `Member`는 자신이 소속된 팀의 PK인 `teamId`필드를 가지고 있어야 한다. 하지만 객체인 `Member`는 팀의 참조인 `Team team`을 가지고 있는 방식을 통해야 조회가 가능하다.
- 좋은 객체 모델링도, 객체지향적 특성도 무너지게 된다.



#### 객체지향 모델링

- 반면 연관된 `Team`의 참조 `team`을 보관한다면
  객체를 테이블에 저장하거나 조회하기 어려워진다.

- 결국 개발자의 중관 변환 역할이 필요해진다.
  - 저장 시에는 `team` 참조 필드를 `TEAM_ID` 외래 키로 변환해야하며
  - 조회 시에는 `TEAM_ID` 외래 키 값을 `team` 객체 참조로 변환해줘야 한다.



#### JPA와 연관관계

```java
member.setTeam(team);	//회원과 팀 연관관계 설정
jpa.persist(member);	//회와과 연관관계 함께 저장
```

- 외래 키와 참조 사이의 변환을JPA가 처리해주기에 편하게 사용할 수 있으며
  객체지향적 특성을 유지하면서도 테이블에 데이터 저장, 조회를 편리하게 할 수 있다.



### 1.2.3 객체 그래프 탐색

- 연관관계를 따라 객체 그래프를 탐색하는 예시

  ```java
  member.getOrder().getOrderItem...
  ```

  객체의 경우는 위와 같은 탐색이 가능하지만,

  - 만일 `Member`를 조회할 때 `order`쪽에 대해 조인하지 않았다면(다른 쪽의 연관관계인 `Team`만을 조회했다면)

    ```sql
    SELECT M.*, T.*
    FROM MEMBER M
    JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID
    ```

    위와 같은 SQL을 실행하여 `Member`에 대한 조회를 수행한 경우라면 `member.getTeam()`은 값을 정상적으로 가져오지만,
    `member.getOrder()`는 데이터를 갖지 않기에 탐색도 불가능하고, `null`을 리턴할 뿐이다.

- SQL을 직접 다룰 경우 처음 실행하는 SQL에 따라 객체 그래프르 어디까지 탐색할 수 있는지 정해진다.
  개발자 입장에서는 객체 그래프를 함부로 탐색할 수 없게 된다. SQL을 직접 확인하지 않는 이상 어디까지 탐색이 가능한지 모른다.
  -> 엔티티가 SQL에 논리적으로 종속되어 발생하는 문제



#### JPA와 객체 그래프 탐색

- JPA 사용 시에는 다음과 같은 객체 그래프 탐색이 자유롭다.

  ```java
  member.getOrder().getOrderItem()...
  ```

  JPA는 연관된 객체를 사용하는 시점에 적절한 SELECT SQL을 실행하기 때문에 위와 같은 탐색이 가능하다.
  -> 연관된 객체를 신뢰하고 자유롭게 조회할 수 있다.

  - 연관된 객체의 조회를 미루는 이 기능을 **지연 로딩**이라고 하며 JPA는 지연 로딩을 투명(transparent)하게 처리한다.
    즉, `getOrder()`등 탐새과 관련한 메서드의 구현에 JPA에 관련된 어떠한 코드도 직접 사용할 필요가 없다.



### 1.2.4 비교

- 데이터베이스는 PK를 기준으로 각 로우(row)를 비교한다.
- 객체는 동일성 비교와 동등성 비교가 있다.

- 이로 인해서 동일 PK를 기반으로 회원 객체를 두 번 조회하면,
  두 회원 객체는 동일성 비교에서 `false`가 리턴된다.
  - DB 측면에서는 같지만, 객체 측면에서는 동일성을 충족하지 못하기 때문이다.



#### JPA와 비교

- JPA의 경우 동일 트랜잭션에서 같은 객체가 조회됨을 보장함으로써 위의 문제를 해결할 수 있다.



### 1.2.5 정리

- 객체 모델과 RDB 모델의 패러디임 차이가 크고, 이를 극복하기 위해선 개발자가 너무 많은 시간과 코드를 소비한다.
- 객체지향적 설계는 오히려 위의 패러다임 불일치 문제를 더 강화시킨다. 이로 인해 개발자의 노력이 더 필요해지며,
  - 결국에는 데이터 중심 설계 모델로 변하게 된다.
- 패러다임의 불일치 문제의 해소를 위한 기술이 JPA다.



## 1.3 JPA란 무엇인가?

- Java Persistence API, 자바 진영의 ORM 기술 표준
- ORM; Object Relational Mapping
  - 객체와 관계형 데이터베이스를 매핑
  - 개발자 대신 패러다임 불일치 문제를 해결
  - 개발자는 ORM 프레임워크에 엔티티를 어떤 방식으로 매핑할 지만 알려주면 됨

- 하이버네이트(hibernate)
  - JPA의 대표적인 ORM 프레임워크
  - 간단한 CRUD부터 대부분의 패러다임 불일치 문제까지 해결해주는 성숙한 ORM 프레임워크



### 1.3.1 JPA 소개

- 역사
  - 과거 자바의 기술 표준: 엔터프라이즈 자바 빈즈(EJB)
    - 엔티티 빈: EJB의 ORM 기술
    - 복잡하고 기술 성숙도가 떨어지며 자바 엔터프라이즈(J2EE) 애플리케이션 서버에서만 동작
  - 오픈소스 ORM 프레임워크인 하이버네이트 등장
    - 가볍고 실용적이며 기술 성숙도가 높고 J2EE에 의존적이지 않아 많은 개발자가 사용
    - EJB 3.0에서 하이버네이트 기반으로 새로운 ORM 표준 등장
      -> **JPA**

- JPA 구현체
  - Hibernate
  - EclipseLink
  - DataNucleus



### 1.3.2 왜 JPA를 사용해야 하는가?

- 생산성
  - SQL을 작성하고 JDBC API를 만드는 반복적 작업을 JPA가 대신해줌
- 유지보수
  - SQL을 직접 다루지 않기에 변경 시에 수정해야 하는 SQL, JDBC 변경 작업이 최소화되었음
  - 패러다임 불일치 문제 해결로 인해 객체지향적 언어의 장점을 이용한 도메인 모델 설계가 가능함
    -> 유지보수에 용이
- 패러다임의 불일치 해결
- 성능

- 데이터 접근 추상화와 벤더 독립성
  - 특정 DB에 종속되지 않은 개발이 가능

- 표준



## Q & A: ORM에 대한 궁금증과 오해

- ORM 프레임워크를 사용한다고 해도 SQL와 DB에 대한 이해가 필요하다.
  - 데이터는 결국 DB에 저장되며
  - 객체와 테이블을 잘 매핑하기 위해서는 양 측 모두에 대한 이해가 중요하다.

- JPA를 잘 이해하고 사용하면 성능 문제가 발생하지 않는다. 오히려 더 좋은 성능을 낼 수도 있다.
- JPA는 복잡한 쿼리보다는 실시간 처리용 쿼리에 더 최적화 되어 있다.
  복잡한 쿼리는 SQL을 직접 작성하는 것이 더 쉬울 수 있다.
  따라서 JPA의 네이티브 SQL 기능을 쓰거나, 마이바티스나 스프링의 Jdbc template등 SQL 매퍼 프레임워크를 혼용하는 것도 좋다.
- JPA와 SQL 매퍼의 차이
  - 마이바티스 등의 SQL 매퍼는 매핑을 대신 처리해주긴 하지만 SQL을 직접 작성해야 한다는 점에서 SQL 의존적이라는 차이점이 있다.

- 하이버네이트는 매우 널리 쓰이고 있는 신뢰할만한 프레임워크다.

- 현재는 마이바티스를 많이 사용하지만 점점 JPA 비중이 높아지고 있으며, 외국의 경우에는 이미 압도적이다.
- JPA는 객체지향과 RDB 양측 모두에 대한 기초를 요하기에 학습 곡선이 높다.





# 02장 JPA 시작

## 2.3 라이브러리와 프로젝트 구조

- JPA 구현체로 하이버네이트를 사용하기 위한 핵심 라이브러리
  - `hibernate-core`: 하이버네이트 라이브러리
  - `hibernate-entitymanager`: 하이버네이트가 JPA 구현체로 동작하도록 JPA 표준을 구현한 라이브러리
  - `hibernate-japa-x.x-api`: JPA x.x 표준 API를 모아둔 라이브러리

- maven의 핵심 라이브러리
  - `hibernate-entitymanager`
    - JPA 표준과 하이버네이트를 포함하는 라이브러리
    - 다음 중요 라이브러리도 함께 받게 됨
      - `-hibernate-core.jar`
      - `-hibernate-jpa-x.x-api.jar`



## 2.4 객체 매핑 시작

```java
@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "NAME")
    private String username;

    private Integer age;
}
```

- 객체 생성 및 거기에 맞는 SQL문 실행하여 테이블 생성한다.

- JPA는 아래와 같은 매핑 애노테이션을 분석해서 어떤 객체가 어떤 테이블과 유관한지 알아냄

  - `@Entity`
    - 이 클래스를 테이블과 매핑한다는 것을 JPA에게 알려줌
    - `@Entity`가 사용된 클래스를 엔티티 클래스라 함
  - `@Table`
    - 엔티티 클래스에 매핑할 테이블 정보를 알려줌
    - `name`속성을 생략할 경우 엔티티 이름을 테이블 이름으로 매핑한다.
  - `@Id`
    - 필드를 PK로 매핑
    - `@Id`가 사용된 필드를 식별자 필드라고 함

  - `@Column`
    - 필드를 컬럼에 매핑
  - 매핑 정보가 없는 필드
    - 매핑 어노테이션이 생략되었을 경우 필드명을 사용하여 컬럼명으로 매핑
    - 만일 데이터베이스가 대소문자를 구별한다면 `@Column(name="AGE")`처럼 명시적으로 매핑해야 한다.



## 2.5 `Persistence.xml` 설정

- JPA는 `persistence.xml`을 사용해서 설정 정보를 관리
  - `META-INF/persistence.xml` 클래스 패스에 있을 경우 자동으로  JPA가 인식

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence" version="2.1">
    <persistence-unit name="jpabook">
        <properties>
            
            <!--필수 속성-->
            <property name="javax.persistence.jdbc.driver"
                      value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.user" value="sa"/>
            <property name="javax.persistence.jdbc.password" value=""/>
            <property name="javax.persistence.jdbc.url"
                      value="jdbc:h2:tcp://localhost/~/test"/>
            <property name="hibernate.dialect"
                      value="org.hibernate.dialect.H2Dialect"/>
            
            <!-- 옵션 -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true"/>
            <property name="hibernate.id.new_generator_mappings" value="true"/>
            
        </properties>
    </persistence-unit>
</persistence>
```

- `<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence" version="2.1">`
  - XML 네임스페이스, 사용할 버전 지정
- `    <persistence-unit name="jpabook">`
  - 영속성 유닛(persistence unit)의 고유명 등록
- `properties`
  - JPA 표준 속성
    - `javax.persistence.jdbc.driver`: JDBC 드라이버
    - `javax.persistence.jdbc.user`, `javax.persistence.jdbc.password`: DB 접속 아이디와 패스워드
    - `javax.persistence.jdbc.url`: DB 접속 URL
  - 하이버네이트 속성
    - `hibernate.dialect`: **데이터베이스 방언(dialect)** 설정

- `javax.persistence`로 시작하는 속성은 JPA 표준 속성
- `hibernate`로 시작하는 속성은 하이버네이트 구현체에 종속되는 속성



### 데이터베이스 방언

- JPA는 DB 종속적이지 않은 표준 기술
  - DB 교체가 간단히 가능
  - **문제: 각 DB는 SQL 문법과 함수에 있어 조금씩 다르다.**
    - 데이터 타입: MySQL의 `VARCHAR` vs 오라클의 `VARCHAR2`
    - 다른 함수명: 문자열을 자르는 함수의 SQL 표준은 `SUBSTRING()` vs 오라클은 `SUBSTR()`
    - 페이징 처리: `MySQL의 LIMIT` vs 오라클의 `ROWNUM`
  - JPA에서는 위와 같은 SQL 표준을 지키지 않거나 특정 DB에만 고유한 기능을 **방언(Dialect)**라 한다.
  - 특정 방언에 종속적일 경우 DB 종류 고체가 어려움
    이런 문제를 해결하기 위해 데이터베이스 방언 클래스를 제공

- JPA는 `Dialect`를 사용하는데
  - 이 `Dialect`의 구현은 `MySQLDialect`일 수도, `OracleDialect`일 수도, `H2Dialect`일 수도 있다.
  - DB 의존적 방언은 `Dialect`가 처리하면 된다. (DIP를 지킨 코드의 다형성 활용과 같다.)
  - DB 방언을 처리하는 방식은 JPA에 표준화되어있지 않다.
- 하이버네이트가 제공하는 DB 방언
  - H2: `org.hibernate.dialect.H2Dialect`
  - 오라클: `org.hibernate.dialect.Oracle10gDialect`
  - MySQL: `org.hibernate.dialect.MySQL5InnoDBDialect`
  - 하이버네이트는 45개 이상의 DB 방언을 지원한다.



## 2.6 애플리케이션 개발

- JPA 어플리케이션 시작 코드

  ```java
  public class JpaMain {
  
  	public static void main(String[] args) {
  
  		//엔티티 매니저 팩토리 생성
  		EntityManagerFactory emf =
  				Persistence.createEntityManagerFactory("jpabook");
  		//엔티티 매니저 생성
  		EntityManager em = emf.createEntityManager();
  		//트랜잭션 획득
  		EntityTransaction tx = em.getTransaction();
  
  		try {
  
  			tx.begin();		//트랜잭션 시작
  			logic(em);		//비즈니스 로직 실행
  			tx.commit();	//트랜잭션 커밋
  
  		} catch (Exception e) {
  			tx.rollback();	//트랜잭션 롤백
  		} finally {
  			em.close();		//엔티티 매니저 종료
  		}
  		emf.close();		//엔티티 매니저 팩토리 종료
  	}
  
  	private static void logic(EntityManager em) {
  		//구체적인 비즈니스 로직이 들어가는 부분
  	}
  
  }
  ```

- 코드 분석: 크게 세 부분이다.
  - 엔티티 매니저 설정
  - 트랜잭션 관리
  - 비즈니스 로직



### 2.6.1 엔티티 매니저 설정

**엔티티 매니저의 생성과정**

- 엔티티 매니저 팩토리 생성
  - `persistence.xml`의 설정 정보를 조회
  - `Persistence`글래스가 입력한 영속성 유닛의 이름(여기서는 `jpabook`)을 찾아서 엔티티 매니저 팩토리 생성
  - 이 과정에서 JPA 동작을 위한 기반 객체 생성하고,
    구현체에 따라 DB 커넥션 풀을 생성하기도 한다.
    -> **생성 비용이 매우 크다!**
    ->**엔티티 매니저 팩토리는 애플리케이션 전체에서 단 한번만 생성하고 공유해서 사용해야 한다!!!**
- 엔티티 매니저 생성
  - 엔티티 매니저 팩토리가 엔티티 매니저를 생성
  - **엔티티 매니저**
    - **JPA 기능 대부분은 엔티티 매니저가 제공**(예시: CRUD) 
    - 엔티티 매니저는 **내부에 DB 커넥션을 유지**하면서 DB와 통신
      -> 사용하는 개발자 입장에서는 일종의 가상 데이터베이스
    - 엔티티 매니저는 DB 커넥션과 밀집한 관계가 있기 떄문에
      **쓰레드간에 공유하거나 재사용해서는 안 된다!!!**
- 종료
  - 사용이 끝난 엔티티 매니저, 엔티티 매니저 팩토리는 반드시 종료해야 한다.
    - `em.close()`, `emf.close()`



### 2.6.2 트랜잭션 관리

- **JPA는 항상 트랜잭션 내에서 데이터를 변경해야 한다.**
  - 그렇지 않을 경우 **예외 발생!**
  - 트랜잭션은 엔티티 매니저에서 트랜잭션 API를 받아와야 한다. (`em.getTransaction()`)
  - 비즈니스 로직이 정상 작동할 경우 트랜잭션을 커밋하고, 예외가 발생하면 롤백한다.



### 2.6.3 비즈니스 로직

```java
    private static void logic(EntityManager em) {
        Member member = new Member();
        member.setUsername("tester");
        member.setAge(25);

        //등록
        em.persist(member);

        //수정
        member.setAge(40);

        //단건 조회
        Member foundMember = em.find(Member.class, member.getId());
        System.out.println("foundMember.getId() = " + foundMember.getId());
        System.out.println("foundMember.getUsername() = " + foundMember.getUsername());
        System.out.println("foundMember.getAge() = " + foundMember.getAge());

		//목록 조회
        List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
		System.out.println("members.size() = " + members.size());

		//삭제
		em.remove(member);
    }
```

- 실행해 보면, CRUD작업이 엔티티 매니저 `em`에 의해 수행됨을 볼 수 있다.
- 등록
  - `em.persist()`메서드로 저장할 엔티티를 넘겨준다.
  - JPA가 대응하는 쿼리문을 생성하여 DB에 전달한다.

- 수정
  - `member.setAge()`만으로 수정이 반영된다.
  - 어떤 엔티티가 수정되었는지 추적하는 기능을 JPA가 가지고 있기 때문이다.
- 삭제
  - `em.remove()`
- 단건조회
  - `em.find()`



### 2.6.4 JPQL

```java
        List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
```

- 단건조회의 경우 `em.find()`메서드로 해결했지만 검색 쿼리의 경우는 다르다.
- JPA는 테이블이 아닌 엔티티 객체를 대상으로 검색해야 한다. (엔티티 중심 개발이기에)
- 애플리케이션이 필요한 엔티티 정보만 가져와 엔티티 객체로 받고, 검색해야 한다.
  - 이를 위해 JPQL; Java Persistence Query Language 쿼리 언어를 사용하면 된다.
  - JPQL은 SQL을 추상화한 객체지향 쿼리 언어다.

- JPQL과 SQL의 차이점
  - JPQL은 엔티티 객체(클래스와 객체)를 대상으로 쿼리한다.
  - SQL은 DB 테이블을 대상으로 쿼리한다.

- **위의 JPQL문에서 `Member`는 엔티티 객체지, 테이블이 아니다. JPQL은 DB 테이블을 전혀 알지 못한다.** 



### 2.7 정리

- 간단한 CRUD 예제만을 살펴봤는데도 JPA가 반복적인 JDBC API와 SQL 작성을 대신 처리해준 덕분에
  코드량이 줄어들고 생산성이 증가하였다.
- 생산성 외에 JPA가 제공하는 핵심 기능에 대해 살펴볼 것이다.

