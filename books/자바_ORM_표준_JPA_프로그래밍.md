# 자바 ORM 표준 JPA 프로그래밍

## 목차

[TOC]

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
  Member member2 = jpa.find(Member.class, memberId);
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



## 2.7 정리

- 간단한 CRUD 예제만을 살펴봤는데도 JPA가 반복적인 JDBC API와 SQL 작성을 대신 처리해준 덕분에
  코드량이 줄어들고 생산성이 증가하였다.
- 생산성 외에 JPA가 제공하는 핵심 기능에 대해 살펴볼 것이다.



# 03장 영속성 관리

## 3.1 엔티티 매니저 팩토리와 엔티티 매니저

- 엔티티 매니저 팩토리는 생성 비용이 크기에 하나만 만들자. 엔티티 매니저는 생성 비용이 작다.
- 엔티티 매니저 팩토리는 thread-safe하지만, 엔티티 매니저는 동시성 문제가 발생 가능하다.
- 엔티티 매니저는 DB 커넥션이 필요할 때까지 DB 커넥션 사용을 미룬다.
- 커넥션 풀의 생성
  - J2SE 환경: 하이버네이트 및 JPA 구현체들은 엔티티 매니저 팩토리가 생성될 때 커넥션 풀을 생성한다.
  - J2EE환경(스프링 프레임워크 포함): 해당 컨테이너가 제공하는 데이터 소스 사용



## 3.2 영속성 컨텍스트란?

- **영속성 컨텍스트(persistence context)**
  - 엔티티를 영속화하는 환경
  - 엔티티 매니저로 엔티티를 저장하거나 조회할 때
    엔티티 매니저는 영속성 컨텍스트에 엔티티를 보관, 관리한다.
    - `em.persist()`는 정확히 말해서 엔티티를 영속성 컨텍스트에 저장하는 것이다.
  - 여러 엔티티 매니저가 동일 영속성 컨텍스트에 접근할 수 있다.



## 3.3 엔티티의 생명주기

- **엔티티의 4가지 상태*
  - **비영속(new/transient)**: 영속성 컨텍스트와 전혀 관계가 없는 상태
  - **영속(managed)**: 영속성 컨텍스트에 저장된 상태
    - 영속성 컨텍스트에 의해 관리된다.
    - `em.persist()` 외에도 `em.find()`나 JPQL에 의해 조회된 경우에도 이 상태가 된다.
  - **준영속(detached)**: 영속성 컨텍스트에 저장되었다가 분리된 상태
    - `em.detach()`를 통해 분리된 상태
    - `em.close()`에 의해 영속성 컨텍스트가 닫힌 경우
    - `em.clear()`에 의해 영속성 컨텍스트가 초기화된 경우
  - **삭제(removed)**: 삭제된 상태



## 3.4 영속성 컨텍스트의 특징

- 식별자 값
  - 영속성 컨텍스트는 엔티티를 식별자 값(`@Id`)으로 구분한다. **식별자 값이 있어야 영속 상태일 수 있다.** 아닐 경우 예외가 발생한다.
- 데이터베이스 저장
  - JPA는 일반적으로 트랜잭션을 커밋하는 순간 영속성 컨텍스트를 DB에 반영한다. `flush()`
- 장점
  - 1차 캐시
  - 동일성 보장
  - 트랜잭션을 지원하는 쓰기 지연
  - 변경 감지
  - 지연 로딩



### 3.4.1 엔티티 조회

- **1차 캐시**
  - 영속성 컨텍스트가 내부에 가지고 있는 캐시
  - 영속 상태의 엔티티가 저장되는 곳
  - 식별자 값을 key로, 엔티티 인스턴스를 value로 가진 Map이라 이해하면 간단하다.
  - 식별자 값은 DB PK와 매핑되어 있다.
- 엔티티 조회 절차
  - `em.find()`로 엔티티 조회시에
  - **우선적으로 메모리에 있는 1차 캐시에서 엔티티를 찾고,**
  - 찾지 못할 경우에 DB를 조회한다.
    - DB 조회하여 엔티티를 생성하고
    - 엔티티를 1차 캐시에 저장한 후에
    - 영속 상태의 엔티티를 반환한다.
  - 성능상 이점이 된다.

- 영속 엔티티의 동일성 보장

  - 식별자 값이 같은 엔티티를 조회할 경우 1차 캐시에서 동일한 인스턴스를 반환받기에

    ```java
    Member member1 = em.find(Member.class, 1L);
    Member member2 = em.find(Member.class, 1L);
    ```

    위 상황에서 `member1==member2`는 `true`다.

  - 따라서 **영속성 컨텍스트는 엔티티 동일성을 보장한다.**

- 트랜잭션 격리 수준
  - JPA의 1차 캐시로 인해
    반복 가능한 읽기(REPEATABLE READ) 등급의 트랜잭션 격리 수준을 
    DB가 아닌 애플리케이션 차원에서 제공한다는 장점이 있다.



### 3.4.2 엔티티 등록

- **트랜잭션을 지원하는 쓰기 지연(transactional write-behind)**

  - 엔티티 매니저는 트랜잭션을 커밋하기 전까지 엔티티를 하나씩 DB에 저장하지 않고
    내부 쿼리 저장소에 SQL을 모아둔다.

  - 트랜잭션을 커밋하면 엔티티 매니저는 우선 영속성 컨텍스트를 **플러시**한다.

  - **플러시**

    - 영속성 컨텍스트의 변경 내용을 DB에 동기화하는 작업
    - 등록, 수정 삭제된 엔티티들은 이 때 반영된다.
    - 정확히는, 쓰기 지연 SQL 저장소에 모인 쿼리를 DB에 전송한다.

  - 영속성 컨텍스트의 변경 내용을 DB에 동기화한 후에(즉 플러시 이후에), 실제 DB 트랜잭션을 커밋한다.

  - 따라서, 다음 코드에서

    ```java
    em.persist(memberA);
    em.persist(memberB);
    
    tx.commit();
    ```

    memberA와 memberB는 `tx.commit()` 시점에 플러시가 이루어지면서 **DB에 한 번에 저장**된다.

- **원리**
  - 변경이 발생할 때마다 쿼리를 DB에 보내지 않고 메모리에 모아 두고
    트랜잭션을 커밋할 때 모아둔 쿼리를 DB에 보내고 커밋한다.
  - 변경사항이 발생할 때마다 쿼리를 DB에 보낸다 하더라도, 트랜잭션이 커밋되지 않는다면 결과는 동일하기에,
    위의 방식(JPA의 방식)으로 성능 최적화가 가능하다.



### 3.4.3 엔티티 수정

#### SQL 수정 쿼리의 문제

- 필드의 수가 늘어남에 따라 수정 쿼리의 길이는 길어지며,
  몇몇 필드의 내용을 입력하지 않는 상황을 방지하기 위해 필드 수 조합 만큼의 많은 수정 쿼리를 사용해야 한다.

- 비즈니스 로직을 분석하기 위해 SQL을 확인해야 한다.

  -> SQL 의존적인 비즈니스 로직



#### 변경 감지

```java
Member member = em.find(Member.class, memberId);

member.setUserName("hello");
member.setAge(10);
```

- 따로 `update`하지 않아도 알아서 반영된다!

- 변경 감지(dirty checking)

  - 스냅샷: 영속성 컨텍스트에는 엔티티의 최초 상태가 복사된다.
  - 플러시 시점에 스냅샷과 엔티티를 비교하여 변경된 엔티티를 찾는다.
  - 과정
    - 트랜잭션 커밋 시도
    - 엔티티 매니저 내부에서 플러시 호출
    - 엔티티와 스냅샷을 비교 -> 변경된 엔티티 찾음
    - 변경된 엔티티 있을 경우 수정 쿼리를 SQL 저장소에 보냄
    - SQL 저장소의 SQL을 DB로 보냄
    - DB 트랜잭션 커밋

  - 적용 범위: **영속 상태의 엔티티**
  - UPDATE SQL: 변경 필드뿐만 아니라 전체 필드를 업데이트함
    - 단점: 데이터 전송량이 증가
    - 장점
      - 수정 쿼리가 항상 같음: 애플리케이션 로딩 시점에 수정 쿼리를 미리 생성, 재사용 가능
      - 동일 쿼리를 보내기에 DB가 이미 파싱된 쿼리를 재사용 가능
    - 필드가 너무 크거나(컬럼이 30개 이상), 저장 데이터가 큰 경우: 전략을 변경하자.
      - 하이버네이트의 경우 `@org.hibernate.annotations.DynamicUpdate` 애노테이션 사용시
        수정된 데이터만 사용해서 UPDATE SQL 생성
      - 유사하게, `@DynamicInsert`도 존재: `null` 필드를 제외한 INSERT 쿼리
    - 별개로 이와 같은 최적화 전략이 필요한 상황이면 테이블 설계 자체를 의심해볼 필요가 있다.



### 3.4.4 엔티티 삭제

- 삭제 대상 엔티티 조회 -> 엔티티 삭제의 과정을 거쳐라.
- 즉시 삭제가 아닌 SQL 저장소에 저장되는 원리임
- 삭제된 엔티티는 영속성 컨텍스트에서 제거
- 삭제된 엔티티는 재사용하지 말고 GC 대상이 되게끔 하라.



## 3.5 플러시

- **영속성 컨텍스트의 변경 내용을 DB에 반영**
  - 플러시라고 해서 영속성 컨텍스트를 비우는 것이 아니며, 변경사항을 DB에 동기화한다는 점을 명심
- 과정
  - 변경감지
  - SQL 저장소 쿼리를 DB에 전송
- 방법
  - `em.flush()` 직접 호출
    - 강제 플러시, 거의 사용 안 함.
  - 트랜잭션 커밋 시 자동 호출
    - 플러시가 SQL 저장소 내용을 DB에 전송하는 과정이기에
      트랜잭션 시에는 플러시가 자동으로 먼저 실행되게 됨
  - JPQL 쿼리 실행 시 자동 호출
    - JPQL 실행 결과에 이전까지 작업했던 결과물이 반영되어야 하기 때문이다.



### 3.5.1 플러시 모드 옵션

- `javax.persistence.FlushModeType` 사용하여 `em`에 플러시 모드 적용 가능
  - `FlushModeType.AUTO`: 커밋이나 쿼리 실행시 플러시(기본값)
  - `FlushModeType.COMMIT`: 커밋할 때만 플러시

- `em.setFlushMode(FlushModeType.COMMIT)`



## 3.6 준영속

- 준영속 상태의 엔티티는 영속성 컨텍스트가 제공하는 기능을 사용할 수 없음
- 방법
  - `em.detach(entity)`: 특정 엔티티를 준영속 상태로 전환
  - `em.clear()`
  - `em.close()`



### 3.6.1 엔티티를 준영속 상태로 전환: `detach()`

- `detach(entity)`: 영속성 컨텍스트가 해당 엔티티를 더 관리하지 않음
  - 1차 캐시의 정보들, SQL 저장소의 관련 SQL이 제거됨



### 3.6.2 영속성 컨텍스트 초기화: `clear()`

### 3.6.3 영속성 컨텍스트 종료: `close()`

- 영속성 컨텍스트의 초기화, 종료로 인해 해당 영속성 컨텍스트의 모든 엔티티가 준영속 상태가 됨



### 3.6.4 준영속 상태의 특징

- 거의 비영속 상태에 가까움
  - 영속성 컨텍스트의 기능 제공 안 됨
- 식별자 값을 가짐
  - 한 번 영속 상태였었기에
- 지연 로딩 불가
  - 영속성 컨텍스트의 관리 대상이 아니기에



### 3.6.5 병합: `merge()`

- 준영속 상태의 엔티티를 다시 영속 상태로 전환, 반환



#### 준영속 병합

- 정확히는 기존의 엔티티가 다시 영속상태로 변경되는 것이 아니라,
  새로운 영속 상태의 엔티티가 반환되는 것임.

- 과정

  - `merge()` 실행
  - 파라미터로 넘어온 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회
    - 만일 1차 캐시에 엔티티가 없을 경우 DB에서 엔티티 조회, 1차 캐시에 저장
  - 조회한 영속 엔티티에 파라미터로 넘어온 엔티티 값을 채워 넣음
  - 조회한 영속 엔티티를 반환

- 파라미터로 넘겨준 엔티티는 여전히 준영속 상태임에 주의

  - 반환된, 조회한 영속 엔티티는 별개의 인스턴스다.

  - 따라서

    ```java
    Member mergeMember = em.merge(member);	 //이것보다는
    member = em.merge(member)				//이걸 사용하자
    ```



#### 비영속 병합

- 엔티티 식별자 값으로 영속성 컨텍스트를 조회했을 때 일치하는 엔티티가 없고
  DB에도 일치하는 엔티티가 없다면
- 새로운 엔티티를 생성해서 병합한다.
- 병합은 준영속과 비영속을 신경쓰지 않기에 다음 기능을 수행
  - 준영속 -> 불러서 병합(UPDATE)
  - 비영속 -> 저장(SAVE)



## 3.7 정리

- 엔티티 매니저, 영속성 컨텍스트는 매핑한 엔티티를 실 사용하는 동적인 부분임

  - cf) 엔티티와 테이블을 매핑하는 정적인 부분

  

# 04장 엔티티 매핑

- 매핑 정보 기술법
  - xml
  - **애노테이션(추천): 더 쉽고 직관적**



## 4.1 `@Entity`

- 테이블과 매핑할 클래스에는 반드시 붙여야 한다.
- JPA가 관리하는 엔티티로 등록됨

| 속성 | 기능                                                         | 기본값                  |
| ---- | ------------------------------------------------------------ | ----------------------- |
| name | JPA에서 사용할 엔티티 이름 지정<br />주의: 다른 패키지에 이름이 같은 엔티티가 있을 경우 충돌 발생! | 클래스 명을 그대로 사용 |

- 주의사항
  - 기본 생성자는 필수다: `public` 또는 `protected`
  - `final` 클래스, `enum` , `interface`, `inner` 클래스에는 사용 불가
  - 저장할 필드에 `final`을 사용해선 안 됨



## 4.2 `@Table`

- 엔티티와 매핑할 테이블 지정

| 속성                   | 기능                                                         | 기본값             |
| ---------------------- | ------------------------------------------------------------ | ------------------ |
| name                   | 매핑할 테이블 명                                             | 엔티티 이름을 사용 |
| catalog                | catalog 기능이 있는 DB에서 catalog 매핑                      |                    |
| schema                 | schema 기능이 있는 DB에서 schema 매핑                        |                    |
| uniqueConstraints(DDL) | DDL 생성 시에 유니크 제약조건을 만듦<br />복합 유니크 제약조건도 가능<br />스키마 자동 생성 기능을 사용해서 DDL을 만들때만 사용됨 |                    |



## 4.3 다양한 매핑 사용

```java
@Entity
@Table(name = "MEMBER")
@Getter
@Setter
public class Member {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "NAME")
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;
}
```

```java
public enum RoleType {
    ADMIN, USER
}
```



## 4.4 데이터베이스 스키마 자동 생성

- 클래스의 매핑 정보 + DB 방언을 이용하여 DB 스키마를 JPA가 자동 생성

- 설정: `persistence.xml`

  ```xml
              <property name="hibernate.hibernate.hbm2ddl.auto" value="create"/>
  ```

- 애플리케이션 실행 시점에 DB 테이블을 자동생성함
  - 실행시 기존 테이블을 삭제하고 다시 생성
- 추천
  - 개발자의 수고를 덜어주지만 운영 환경에서 사용할만큼 완벽하지는 않음
  - 개발 환경에서만 쓰거나, 매핑 참고용으로만 사용을 추천
- `hibernate.hbm2ddl.auto` 속성

| 옵션                                       | 설명                                                         |
| ------------------------------------------ | ------------------------------------------------------------ |
| create                                     | 기존 테이블을 삭제하고 새로 생성<br />DROP + CREATE          |
| create-drop                                | create 옵션에 더해, 애플리케이션 종료 시 생성한  DDL 제거<br />DROP + CREATE + DROP |
| update                                     | DB 테이블과 엔티티 매핑정보를 비교, 변경 사항만 수정         |
| validate                                   | DB 테이블과 엔티티 매핑정보를 비교, 차이가 있을 경우 경고를 날리고, 애플리케이션을 실행하지 않음<br />DDL을 수정하지 않는 옵션 |
| 사용하지 않음<br />(구체적인 옵션 값 없음) | 자동 속성 기능을 사용하지 않음<br />속성 자체를 삭제하거나, 유효하지 않은 옵션 값을 주면 적용 |

- 당연히 **운영 환경에서 DDL 수정하는 옵션을 써선 안 된다!** (create, create-drop, update)

- 개발 단계에 따른 추천 전략
  - 초기: create or update
  - 초기화 상태로 테스트 진행하는 개발자 환경 및 CI 서버: create or create-drop
  - 테스트 서버: update or validate
  - 스테이징, 운영 서버: validate or 사용 안 함

- JPA 2.1부터는 스키마 자동 생성 기능을 지원

  ```xml
  <property name="javax.persistence.schema-generation.database.action"
            value="drop-and-create" />
  ```

  - 단 none, create, drop-and-create, drop만 지원하며
  - update, validate는 하이버네이트만 지원한다.

- **이름 매핑 전략**

  - 자바의 관례: CamelCase

  - DB의 관례: under_score

  - 양 측의 관례를 맞추려면 `@Column.name` 속성 등으로 명시해줘야 한다.

    ```java
    @Column(name="role_type")	 //under_score 사용
    String roleType;			//CamelCase 사용
    ```

  - `hibernate.ejb.naming_strategy`: 이름 매핑 전략 변경 가능
    - 하이버네이트의 `org.hibernate.cfg.ImprovedNamingStrategy` 클래스를 이용할 수 있음
      - 테이블 명, 컬럼 명이 명시되지 않은 경우
        카멜케이스(자바) -> 언더스코어 표기법(테이블)로 매핑
  
  - **하이버네이트5부터 `PhisicalNamingStrategy` 사용해야 한다!**



## 4.5 DDL 생성 기능

- DDL에 명명 제약조건을 추가하고 싶다면?

  ```java
  @Table(name = "MEMBER", uniqueConstraints = {
          @UniqueConstraint(
                  name = "NAME_AGE_UNIQUE",
                  columnNames = {"NAME", "AGE"}
          )})
  ...
  @Column(name = "NAME", nullable = false, length = 10)
  private String username;
  ```

  위와 같이 제약조건 추가 가능

- 주의사항

  - **DDL 자동 생성 기능 사용할 때만 적용**
    - DDL 자동 생성 안 쓴다면 무의미

  - **JPA 실행 로직에는 영향을 주지 않음**

- 개발자가 엔티티만 보고도 제약조건을 파악할 수 있다는 장점



## 4.6 기본 키 매핑

- 기본 키를 직접 할당하지 않고, DB 값을 사용하고 싶다면?
  - DB마다 기본 키 생성 방식이 다름!

- JPA의 기본 키 생성 전략
  - 직접 할당: 애플리케이션에서 직접 할당
  - 자동 생성: 대리 키 사용 방식
    - IDENTITY: 기본 키 생성을 DB에 위임
    - SEQUENCE: DB 시퀀스를 이용
    - TABLE: 키 생성 테이블을 사용

- **주의사항**
  - **키 생성 전략을 사용하려면 `persistence.xml`에 `hibernate.id.new_generator_mappings=true`를 반드시 추가해야 함!**
  - 위의 속성 값은 기본적으로 `false`이기 때문(구버전과의 호환성 때문)
    - 상기 옵션이 `true`일 경우 `allocationSize` 속성(키 생성 기능 최적화) 사용 방식이 달라짐



### 4.6.1 기본 키 직접 할당 전략

- `@Id`로 매핑하고, `em.persist()`로 엔티티 저장 전에 기본키를 직접 할당해 주면 된다.
  - 식별자 없이 저장할 경우 예외 발생
    - JPA 표준에 정의되어 있지 않기에, JPA 최상위 예외인 `javax.persistence.PersistenceException` 발생
    - 하이버네이트의 경우 `org.hibernate.id.IdentifierGenerationException` 사용 가능
- `@Id` 적용 가능 자바 타입
  - 자바 기본 자료형
  - 자바 기본 자료형에 대한 Wrapper
  - `String`
  - `java.util.Date`
  - `java.sql.Date`
    - `java.util.Date`를 상속, 시간 및 시간대에 대한 정보는 없음
  - `java.math.BigDecimal`
  - `java.math.BigInteger`



### 4.6.2 IDENTITY 사용 전략

- 기본 키 생성을 DB에 위임하는 전략

  - 주로 MySQL, PostgreSQL, SQL Server, DB2에서 사용

  - ex) MySQL의 AUTO_INCREMENT 기능

    ```SQL
    CREATE TABLE BOARD (
        ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        DATA VARCHAR(255)
    );
    
    INSERT INTO BOARD(DATA) VALUES('A');
    INSERT INTO BOARD(DATA) VALUES('B');
    ```

    - 테이블 결과

      | ID   | DATA |
      | ---- | ---- |
      | 1    | A    |
      | 2    | B    |

      ID 컬럼에 자동으로 값이 입력됨

- **데이터베이스에 값을 저장하고 나서야 기본 키 값을 구할 수 있을 때 사용**

  - **`@GeneratedValue(strategy = GenerationType.IDENTITY)`를 함께 사용하라.**

- **최적화**

  - 해당 객체를 처음 DB에 등록하는 상황에서 JPA가 기본 키 값을 얻어오기 위해 DB를 추가로 조회하게 됨

    - DB에 INSERT한 후에야 PK값을 조회할 수 있다는 점에서 성능 문제가 발생할 수 있지만
      JDBC3의 `Statement.getGeneratedKeys()`이용하여 DB 저장하면서 동시에 생성된 PK 값을 얻어오는 최적화가 가능하다.
      - 하이버네이트의 경우에도 위 방식을 사용하기에 DB 통신을 두 번 하지 않는다.

  - 엔티티를 영속 상태로 만들려면 식별자가 필요 + 엔티티를 DB에 저장해야 PK를 얻는 IDENTITY 전략

    -> **`em.persist()` 호출 즉시 INSERT SQL이 DB에 전달되기에 쓰기 지연이 동작하지 않음!**



### 4.6.3 SEQUENCE 전략

- 시퀀스를 이용하여 PK를 생성하는 전략

  - DB 시퀀스: 유일한 값을 순차적으로 생성하는 DB 오브젝트
  - 시퀀스를 지원하는 오라클, PostgreSQL, DB2, H2 DB에서 사용 가능

- 시퀀스 사용

  - 코드

    - DDL

      ```SQL
      CREATE SEQUENCE BOARD_SEQ START WITH 1 INCREMENT BY 1;
      ```

    - 매핑

      ```java
      @Entity
      @SequenceGenerator (
          name = "BOARD_SEQ_GENERATOR",
          sequenceName = "BOARD_SEQ",	//매핑할 데이터베이스 시퀀스 이름
          initialValue = 1, aloocationSize =1)
      public class Board {
          
          @Id
          @GeneratedValue(strategy = GenrationType.SEQUENCE,
                         generator = "BOARD_SEQ_GENERATOR")
          private Long id;
          ...
      }
      ```

  - 방법

    - 시퀀스 생성
    - 사용할 DB 시퀀스 매핑
    - 키 생성 전략을 `GenrationType.SEQUENCE`로 설정, 시퀀스 생성기를 `generator`로 등록

- 엔티티 사용 코드는 IDENTITY 코드와 같지만, 내부 동작 방식은 다름. 시퀀스 전략은:
  - `em.persist()` 호출 시 DB 시퀀스로 식별자 우선 조회
  - 식별자를 엔티티에 할당, 영속성 컨텍스트에 엔티티 저장
  - 트랜잭션이 커밋되면 DB에 엔티티 저장(쓰기 지연 가능!)

- `@SequenceGenerator` 속성

  | 속성            | 기능                                                         | 기본값             |
  | --------------- | ------------------------------------------------------------ | ------------------ |
  | name            | 식별자 생성기 이름                                           | 필수               |
  | sequenceName    | DB에 등록되어 있는 시퀀스 이름                               | hibernate_sequence |
  | initialValue    | DDL 생성 시에만 사용,<br />시퀀스 DDL 생성 시 처음 시작하는 수를 정함 | 1                  |
  | allocationSize  | 시퀀스 한 번 호출에 증가하는 수(성능 최적화용)               | 50                 |
  | catalog, schema | 데이터베이스 catalog, schema 이름                            |                    |

  - 매핑 DDL

    ```SQL
    CREATE SEQUENCE [sequenceName]
    START WITH [initialValue] INCREMENT BY [allocationSize]
    ```

  - **`SequenceGenerator`는 `@GeneratedValue` 옆에서 사용할 수도 있음**

- `sequenceName` 기본값은 JPA 명세 상으로는 JPA 구현체가 정의함(상단은 하이버네이트 기준)

- 기본 설정의 `allocationSize`가 50이기에 1씩 증가한다고 착각하지 않도록 주의: 아래 참고

- **최적화**

  - DB와 두 번 통신하게 되는 문제점
    - 식별자 조회를 위해 1번
    - 조회한 시퀀스를 PK로 사용하여 DB에 저장하기 위해 1번
  - JPA의 최적화 방법: `allocationSize`
    - 시퀀스를 조회할 때
      - DB에서는 한 번에 `allocationsize`만큼 시퀀스 값이 증가(ex. 1-> 51)
      - 처음 시퀀스를 얻어 온 다음, 다음 `allocationSize` 만큼 증가한 수에 도달할 때까지는 메모리에서 할당
        - 위의 예시로는 1~50까지는 메모리에서 식별자를 할당
        - DB 접근을 줄일 수 있음
    - 시퀀스 값을 선점하기에 여러 JVM이 동시 동작하더라도 PK값이 충돌하지 않음
    - **DB에 직접 접근할 때마다 `allocationSize` 만큼 시퀀스 값이 크게 증가함에 주의**
      - 성능에 덜 신경쓴다면 `allocationSize`를 1로 설정해서 위 상황을 막을 수 있음
    - **`hibernate.id.new_generator_mappings` 속성이 `true`여야 적용된다.**
      - `false`일 경우 하이버네이트의 과거 최적화 방식 이용:
        - 시퀀스 값을 할당받고, `allocationSize`만큼 사용
        - ex) `allocationSize`가 1이고, 시퀀스가 1부터 시작하는 경우
          - DB에서 시퀀스 값 1을 받환받은 경우 1~50 사용
          - DB에서 시퀀스 값 2를 반환받은 경우 51~~100 사용



### 4.6.4 TABLE 전략

- 키 생성 전용 테이블을 생성, 이름과 값으로 사용할 컬럼을 만들어 시퀀스를 흉내냄

  - 테이블 기반이기에 어느 DB에나 적용 가능

- 방법

  - 키 생성 용도로 사용할 테이블 만들기

    - 테이블 DDL

      ```SQL
      CREATE TABLE MY_SEQUENCES (
          SEQUENCE_NAME VARCHAR(255) NOT NULL,
          NEXT_VAL BIGINT,
          PRIMARY KEY (SEQUENCE_NAME)
      )
      ```

      - SEQUENCE_NAME: 시퀀스 이름
      - NEXT_VAL: 시퀀스 값
      - (위의 명칭이 기본 값임)

  - 매핑

    ```java
    @Entity
    @TableGenerator (
        name = "BOARD_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "BOARD_SEQ", allocationSize = 1)
    public class Board {
        
        @Id
        @GeneratedValue(strategy = GenerationType.TABLE,
                       generator = "BOARD_SEQ_GENERATOR")
        private Long id;
        ...
    }
    ```

    - 시퀀스 대신에 테이블을 사용한다는 점을 제외하면 시퀀스 전략과 내부 동작방식이 동일

  - `@TableGenerator` 속성

    | 속성                   | 기능                                           | 기본값              |
    | ---------------------- | ---------------------------------------------- | ------------------- |
    | name                   | 식별자 생성기 이름                             | 필수                |
    | table                  | 키 생성 테이블 명                              | hibernate_sequences |
    | pkColumnName           | 시퀀스 컬럼명                                  | sequence_name       |
    | valueColumnName        | 시퀀스 값 컬럼명                               | next_val            |
    | pkColumnName           | 키로 사용할 값 이름                            | 엔티티 이름         |
    | intialValue            | 초기 값, 마지막으로 생성된 값이 기준           | 0                   |
    | allocationSize         | 시퀀스 한 번 호출에 증가하는 수(성능 최적화용) | 50                  |
    | catalog, schema        | DB catalog, schema 이름                        |                     |
    | uniqueConstraints(DDL) | 유니크 제약 조건 지정 가능                     |                     |

    - JPA 표준 명세상 `table`, `pkColumnName`, `valueColumnName` 기본값은 JPA 구현체가 정의
      (상단의 기본값은 하이버네이트 기준)

    - 매핑할 DDL, 테이블명

      | {pkColumnName} | {valueColumnName} |
      | -------------- | ----------------- |
      | {pkColumnName} | {initialValue}    |

- **최적화**
  - 값을 조회할 때 SELECT, 다음 시퀀스 값으로 증가시킬 때 UPDATE 쿼리 날려야 하니
    시퀀스 전략에 비해 시퀀스 조회 당 DB 통신이 +1이라는 문제점
  - `@TableGenerator.allocationSize` 사용하여 최적화 가능, 방식은 시퀀스 전략과 같음



### 4.6.5 AUTO 전략

- DB 방언에 따라 적절한 전략을 자동으로 선택해줌
  - 오라클은 SEQUENCE, MySQL은 IDENTITY
- `GeneratedValue(strategy  = GenerationType.AUTO)`: 기본값이기에 명시하지 않아도 됨
- DB를 변경해도 코드를 수정할 필요가 없다는 장점
  - 개발 초기 단계, 프로토타입 개발 시 특히 유용
- 시퀀스나 테이블의 경우 각각 시퀀스, 키 생성 테이블을 필요로 하기에, 이 전략들이 선택될 경우 미리 만들어두어야 한다.
  물론, 스키마 자동 생성 기능을 사용시 하이버네이트가 기본 값 기반으로 적절히 처리해준다.



### 4.6.6 기본 키 매핑 정리

- **권장하는 식별자 선택 전략**
  - PK의 3조건
    - NOT NULL
    - UNIQUE
    - 불변
  - 자연 키와 대리 키
    - **자연 키(natural key)**: 비즈니스에 의미 있는 키, ex) 주민번호, 이메일, 전화번호
    - **대리 키(surrogate key)**: (=대체 키) 비즈니스와 무관한 임의 생성 키, ex) 오라클 시퀀스, 키 생성 테이블
  - **자연 키보다는 대리 키를 권장**
    - 현실과 비즈니스 규칙은 생각보다 쉽게 변하기 때문
  - **비즈니스 환경은 언젠가 변함**
    - 주민번호가 PK였는데, 정책 변화로 주민번호 저장이 금지되게 된 케이스
    - 대리 키는 비즈니스와 무관한 임의의 값이라 변동이 매우 드물다.
    - 자연 키의 후보가 되는 컬럼들은 필요에 따라 유니크 인덱스를 설정하여 사용하자.
  - **JPA의 추천: 대리 키**

- **기본 키 값은 당연히 절대 변경하지 말자. JPA가 예외를 발생시키거나, 동작하지 않게 된다.**
  - `setId()` 등을 외부에 노출하지 말라!



## 4.7 필드와 컬럼 매핑: 레퍼런스

- 필드와 컬럼 매핑

  | 매핑 애노테이션 | 설명                           |
  | --------------- | ------------------------------ |
  | @Column         | 컬럼을 매핑한다.               |
  | @Enumerated     | 자바의 enum 타입을 매핑        |
  | @Temporal       | 날짜 타입을 매핑               |
  | @Lob            | BLOB, CLOB 타입을 매핑         |
  | @Transient      | 특정 필드를 DB에 매핑하지 않음 |

- 기타

  | 매핑 어노테이션 | 설명                                |
  | --------------- | ----------------------------------- |
  | @Access         | JPA가 엔티티에 접근하는 방식을 지정 |



### 4.7.1 `@Column`

- 객체 필드를 테이블 컬럼에 매핑

  - `name`, `nullable` 많이 사용
  - `insertable`, `updatable`: DB 값을 읽기 전용으로만 쓰고 싶을 때 사용

- 속성

  | 속성                              | 기능                                                         | 기본값                                                       |
  | --------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
  | name                              | 필드와 매핑할 테이블의 컬럼 이름                             | 객체의 필드 이름                                             |
  | insertable<br />(거의 사용 안 함) | false 지정 시 엔티티 저장 시에도 필드가 DB에 저장되지 않음<br />읽기 전용으로 false 사용 | true                                                         |
  | updatable<br />(거의 사용 안 함)  | false 지정 시 엔티티 수정 시에 필드 수정이 DB에 반영되지 않음<br />읽기 전용으로 false 사용 | true                                                         |
  | table<br />(거의 사용 안 함)      | 하나의 엔티티를 두 개 이상의 테이블에 매핑할 때 사용<br />지정한 필드를 다른 테이블에 매핑 가능 | 현재 클래스가 매핑된 테이블                                  |
  | nullable(DDL)                     | false 설정시 DDL 생성시에 NOT NULL 제약 조건이 붙음          | true                                                         |
  | unique(DDL)                       | 해당 컬럼에 유니크 제약조건을 건다.<br />여러 테이블에 유니크 제약조건을 걸려면 @Table.uniqueConstraints 사용 |                                                              |
  | columnDefinition(DDL)             | 데이터베이스 컬럼 정보를 직접 줄 수 있음                     | 필드의 자바 타입과 방언 정보를 사용,<br /> 적절한 컬럼 타입을 생성함 |
  | length(DDL)                       | 문자 길이 제약조건, String에만 적용                          | 255                                                          |
  | precision, scale(DDL)             | BigDecimal, BigInteger 타입에서 사용<br />precision: 소수점을 포함한 전체 자릿수<br />scale: 소수 자릿수<br />double, float 타입에는 적용되지 않음<br />아주 큰 숫자나 정밀한 소수를 다뤄야 할 때만 사용 | precision = 19,<br />scale =2                                |

- **`@Column` 생략**
  - 대부분 속성 기본값이 적용됨
  - 단, 자바 기본 자료형의 경우 `nullable` 속성이 예외적으로 적용된다.
    - 참조형이 아닌 기본형의 경우 `null` 값 자체를 가질 수 없기에 `nullable` 속성이 `false`가 되야 한다.
    - JPA의 경우 DDL 사용시 기본형에 대해 `not null` 속성을 `false`로 한다.
    - `Integer`, `Long` 등 wrapper 클래스 등은 당연히 `true`다.
    - **`@Column`을 사용할 경우 속성 `nullable`의 기본 값이 `true`이기에**
      **기본 값 사용 시 주의하여 `nullable` 속성을 `false`로 지정하자.**



### 4.7.2 `@Enumerated`

- `enum` 매핑용

- 속성

  | 속성  | 기능                                                         | 기본값           |
  | ----- | ------------------------------------------------------------ | ---------------- |
  | value | - EnumType.ORDINAL: enum 순서를 DB에 저장<br />- EnumType.STRING: enum 이름을 DB에 저장 | EnumType.ORDINAL |

- `EnumType.ORDINAL`
  - 장점: DB에 저장 공간을 적게 차지한다.
  - **단점: 이미 저장된 `enum`의 순서를 변경할 수 없다.**
- `EnumType.STRING`
  - **장점: 저장된 `enum`의 순서가 바뀌거나, `enum`이 추가되어도 안전하다.**
  - 단점: `ORDINAL`에 비해 저장 공간을 더 차지한다.



### 4.7.3 `@Temporal`

- 날짜 타입(`java.util.Date`, `java.util.Calendar`)매핑에 사용

- 속성

  | 속성  | 기능                                                         | 기본값                                 |
  | ----- | ------------------------------------------------------------ | -------------------------------------- |
  | value | - TemporalType.DATE: 날짜, DB의 DATE 타입과 매핑됨(2022-04-12)<br />- TemporalType.TIME: 시간,  DB의 TIME 타입과 매핑됨(22:39:56)<br />- TemporalType.TIMESTAMP: 날짜와 시간, DB의 TIMESTAMP 타입과 매핑(2022-04-12 22:40:12) | DB에 따라 다르지만<br />보통 TIMESTAMP |

- 자바의 Date: 연월일, 시분초
- DB: DATE(날짜), TIME(시간), TIMESTAMP(날짜와 시간)
- DB에 따라 값 생략시 지정되는 값이 다름
  - DATETIME: MySQL
  - TIMESTAMP: H2, 오라클, PostgreSQL



### 4.7.4 `@Lob`

- DB의 BLOB, CLOB과 매핑
  - 매핑 필드가 문자면 CLOB: `String`, `char[]`, `java.sql.CLOB` 
  - 매핑 필드가 숫자면 BLOB: `byte[]`, `java.sql.BLOB`
- 지정 가능한 속성이 없음



### 4.7.5 `@Transient`

- 매핑하지 않는 필드
- DB에 저장도, 조회도 하지 않는다.
- 자바의 키워드와 헷갈리지 말자!(직렬화를 무시하는 필드에 사용)



### 4.7.6 `@Access`

- JPA가 엔티티 데이터에 접근하는 방식

  - `AccessType.FIELD`: 필드 접근 방식, `private` 필드라도 접근 가능

    ```java
    @Id
    private String id;
    ```

    - 위와 같이 `@Id`가 필드에 있을 경우 자동 지정된다.

  - `AccessType.PROPERTY`: 접근자(Getter)로 접근한다.

    ```java
    @Id
    public String getId() {
        return this.id;
    }
    ```

    - 위와 같이 `@Id`가 Getter에 붙어 있을 경우 자동 지정된다.

  - 아래와 같이 두 방식을 함께 사용할 수도 있다:

    ```java
    @Id
    private String id;
    
    @Access(AccessType.PROPERTY)
    public String getName() {
        return this.name;
    }
    ```

    - `@Id`위치로 인해 기본은 필드 접근, `name`에 대해서만 속성 접근 방식을 취하게 된다.



# 05장 연관관계 매핑 기초

- ORM에서 가장 어려운 부분
  - **객체 참조와 FK를 매핑하는 것!**
  - 키워드
    - 방향(Direction): 단방향, 양방향, 테이블의 경우 항상 양방향
    - 다중성(Multiplicity): 1:N, N:1, 1:1, N:M
    - 연관관계의 주인(Owner)



## 5.1 단방향 연관관계

- N:1 관계 기본 이해 예시

  - 회원(N): 팀(1)

  - 회원은 하나의 팀에만 소속

  - 객체 연관관계

    - `Member.team`: `team`을 멤버변수로 가짐으로써(**참조 이용**) 팀 객체와 연관관계를 맺음
    - 회원 -> 팀의 단방향 관계

  - 테이블 연관관계

    - 회원 테이블의 FK TEAM_ID로 연관관계를 맺음(**외래키 이용**)

    - 양방향

      - 회원과 팀을 조인

        ```SQL
        SELECT *
        FROM MEMBER M
        JOIN TEAM T ON M.TEM_ID = T.ID
        ```

      - 팀과 회원을 조인

        ```SQL
        SELECT *
        FROM TEAM T
        JOIN MEMBER M ON T.TEAM_ID  = M.TEAM_ID
        ```

  - **방향: 가장 큰 차이점**

    - 객체 참조의 경우 항상 단방향
    - 테이블의 경우 항상 양방향
    - 객체로 양방향 관계를 표현하려면, 양 쪽 멤버필드에 참조를 보관,
      **하지만 이 방식은 양방향이라기 보단, 단방향 2개가 맞다!**



### 5.1.1  순수한 객체 연관관계, 5.1.2 테이블 연관관계

- **객체 그래프 탐색**: 객체의 경우 참조를 통해 연관관계를 탐색

- **외래키를 통한 조인(테이블)**

  - DDL

    ```SQL
    ALTER TABLE MEMBER ADD CONSTRAINT FK_MEMBER_TEAM
    	FOREIGN KEY (TEAM_ID)
    	REFERENCES TEAM
    ```



### 5.1.3 객체 관계 매핑

- `Member`

  ```java
  @Entity
  @Setter	//연관관계 설정
  public class Member {
  
      @Id
      @Column(name = "ID")
      @GeneratedValue
      private Long id;
  
      @Column(name = "NAME", nullable = false, length = 10)
      private String username;
  
      @ManyToOne
      @JoinColumn(name="TEAM_ID")
      private Team team;
      
      ...
  }
  ```

- `Team`

  ```java
  @Entity
  public class Team {
  
      @Id
      @Column(name = "TEAM_ID")
      private String id;
  
      private String name;
  }
  ```

- `@ManyToOne`
  - 다대일 관계임을 알리는 애노테이션



### 5.1.4 `@JoinColumn`

- FK 매핑에 사용

- 생략 시 FK 탐색 전략

  - 필드명 + "_" + 참조하는 테이블의 PK 컬럼명

- 속성

  | 속성                                                         | 기능                                                      | 기본값                                     |
  | ------------------------------------------------------------ | --------------------------------------------------------- | ------------------------------------------ |
  | name                                                         | 매핑할 외래 키 이름                                       | 필드명 + "_" + 참조하는 테이블의 PK 컬럼명 |
  | referencedColumnName                                         | 외래 키가 참조하는 대상 테이블의 컬럼명                   | 참조하는 테이블의 PK 컬럼명                |
  | foreignKey(DDL)                                              | 외래 키 제약조건을 직접 지정<br />테이블 생성할 때만 적용 |                                            |
  | unique<br />nullable<br />insertable<br />updatable<br />columnDefinition<br />table | Column 속성과 상동                                        |                                            |



### 5.1.5 `@ManyToOne`

- 다대일 관계에서 사용

- 속성

  | 속성         | 기능                                                         | 기본값          |
  | ------------ | ------------------------------------------------------------ | --------------- |
  | optional     | false일 경우 연관된 엔티티가 항상 있어야 한다.               | true            |
  | fetch        | 글로벌 페치 전략 설정                                        | FetchType.EAGER |
  | cascade      | 영속성 전이 기능을 사용                                      |                 |
  | targetEntity | 연관된 엔티티의 타입 정보 설정<br />거의 사용하지 않음<br />컬렉션을 사용해도 제네릭으로 타입 정보를 알 수 있음 |                 |

  ```java
  @OneToMany
  private List<Member> members;			//제네릭으로 타입 정보를 알 수 있음
  
  @OneToMany(targetEntity=Member.class)
  private List members;				   //제네릭이 없어 타입 정보를 알 수 없음
  ```



## 5.2 연관관계 사용

### 5.2.1 저장

- **JPA에서 엔티티를 저장할 때 연관된 모든 엔티티는 영속 상태여야 한다.**



### 5.2.2 조회

- 연관관계가 있는 엔티티를 조회하는 방법

  - 객체 그래프 탐색

    - `member.getTeam()`

  - 객체지향 쿼리(JPQL) 사용

    ```java
    String jpql = "select m from Member m join m.team t where t.name =: teamName";
    
    List<Member> resultList = em.createQuery(jpql, Member.class)
        .setParameter("teamName", "팀1")
        .getResultList();
    ```



### 5.2.3 수정

- `setTeam()`으로 변경, `flush()`발생할 때 반영



### 5.2.4 연관관계 제거

- `setTeam(null)`



### 5.2.5 연관된 엔티티 삭제

- 기존에 있던 연관관계를 먼저 제거하고, 연관된 엔티리르 삭제해야 함
  - 그렇지 않을 경우 FK 제약조건으로 인해 DB 에러 발생



## 5.3 양방향 연관관계

- `List` 이용하야 팀에서도 멤버를 참조하게 변경
- JPA는 `List`, `Collection`, `Set`, `Map` 등 다양한 컬렉션을 지원함



### 5.3.1 양방향 연관관계 매핑

- `Team`

  ```java
      @OneToMany(mappedBy = "team")
      private List<Member> members = new ArrayList<Member>();
  ```

  - `mappedBy` 속성에 반대쪽 매핑의 필드 값을 주면 됨



## 5.4 연관관계의 주인

- 객체의 관점에서는 양방향 연관관계가 없으며, 단방향 연관관계 두 개가 존재하는 상황이다.
- 테이블 기준에서 어떤 관계를 기준으로 외래 키를 관리할지가 필요하다.
- 따라서 `mappedBy` 속성이 필요하며, 두 연관관계 중 기준이 되는 쪽을 설정해야 한다.
  -> **연관관계의 주인(Owner)**: 외래 키 관리자



### 5.4.1 양방향 매핑의 규칙: 연관관계의 주인

- 연관관계의 주인만이 DB 연관관계와 매핑되고, FK를 관리(등록, 수정, 삭제)할 수 있음
- 주인이 아닌 쪽은 읽기만 가능
- 주인인 경우 `mappedBy` 속성을 사용하지 않고, 주인이 아닌 경우 `mappedBy` 속성을 사용하여 연관관계의 주인을 지정한다.
- `Many` 쪽에서 관리할 경우, 자신 테이블의 FK를 관리하지만,
  `One`쪽에서 관리할 경우 (물리적으로) 다른 테이블의 FK를 관리해야 한다.
  - 때문에 `Many`쪽이 관리하는 것이 낫다.



### 5.4.2 연관관계의 주인은 외래 키가 있는 곳

- 연관관계의 주인이 아닌 곳에는 `mappedBy` 속성으로 연관관계의 주인인 엔티티의 FK 필드를 주면 된다.
- `@ManyToOne`의 경우 `Many` 측에게만 쓰는 애노테이션이기 때문에 `mappedBy` 속성을 설정할 수 없다.



## 5.5 양방향 연관관계 저장

- 연관관계의 주인인 `Member.team` 필드를 통해 연관관계를 설정하고 저장한다.



## 5.6 양방향 연관관계의 주의점

- **연관관계의 주인에는 값을 할당하지 않고, 주인이 아닌 곳에만 값을 할당하는 실수**
  - 연관관계의 주인만이 FK 값을 변경할 수 있기에, DB에 정상 저장되지 않는다.



### 5.6.1 순수 객체까지 고려한 양방향 연관관계

- **객체 관점에서는 연관관계의 주인이 아닌 곳에도 값을 할당하는 편이 안전하다.**
  - JPA를 사용하지 않는 경우, 연관관계의 주인이 아닌 쪽에서 연관관계의 주인 쪽으로 조회가 불가능해지기 때문이다.



### 5.6.2 연관관계 편의 메서드

- 양방향 연관관계를 객체 측면에서도 안전하게 설정하려면 연관관계의 주인이 아닌 쪽에도 값을 입력해 줘야 한다.

- 하지만 연관관계를 설정할 때 마다 반대 방향까지 계속 설정해 주는 것은 빠트리는 실수가 일어나기 쉽다.

- 한 쪽의 연관관계 설정 메서드에, 반대 측면에서의 객체 연관관계 까지 설정해주는 코드를 만들자: **연관관계 편의 메서드**

  ```java
      public void setTeam(Team team) {
          if (this.team != null) {
              this.team.getMembers().remove(this);
          }
          this.team = team;
          team.getMembers().add(this);
      }
  ```

  

### 5.6.3 연관관계 편의 메서드 작성 시 주의사항

- **연관관계 변경 메서드에는 기존 연관관계를 제거해줘야 한다.**

  ```java
          if (this.team != null) {
              this.team.getMembers().remove(this);
          }
  ```

  - `team`은 연관관계의 주인이 아니기 때문에 FK 변경에는 지장이 없지만
  - 만일 `team` 쪽에서 `members`를 조회한다면 어떻게 될까?
    - 새로운 영속성 컨텍스트에서 조회할 경우, DB FK 상으로 관계가 끊어져 있기에 기존 연관관계가 조회되지는 않겠지만
    - 기존 영속성 컨텍스트가 살아 있는 상태에서 조회할 경우, 이전 `team`의 `members`에는 여전히 연관관계가 남아 있는 것처럼 해당 `member`가 조회될 것이다.
      -> **이런 위험 때문에 기존 연관관계를 제거해주는 것이 안전하다.**



## 5.7 정리

- 단방향 매핑만으로 테이블 상의 연관관계 매핑은 완료된다.
- 양방향 매핑은 반대 방향 객체 그래프 탐색이 필요할 때만 고려할만하다.
  - 양방향 매핑은 복잡하기 때문에 정말로 필요한지에 대해 고민해볼 필요가 있으며, 정말 필요할 때 양방향을 추가하는 식으로 코드를 짜는 것도 괜찮다.
- 연관관계의 주인
  - "주인"이라고 해서 비즈니스상의 중요성을 떠올려선 안 된다. 이 "주인" 개념은 매핑 상의 개념일 뿐이다.
- 양방향 매핑 시에 무한 루프에 빠지지 않도록 주의해야 한다. 특히 JSON 변환이나 롬복을 사용할 때 자주 발생한다.
- `OneToMany`를 연관관계의 주인으로 설정할 수는 있다.
  - `Team.members`를 연관관계의 주인으로 삼는 것이 그 예다.
  - 하지만 성능과 관리 측면에서 좋지 않다.



# 06장 다양한 연관관계 매핑

- 기본적인 연관관계 매핑(05장)
  - 연관관계 매핑의 고려 사항
    - 다중성
    - 단방향, 양방향
    - 연관관계의 주인
- 여러 연관관계 매핑에 대해 알아보자
  - 다대일: 단방향, 양방향
  - 일대다: 단방향, 양방향
  - 일대일: 주 테이블(단방향, 양방향), 대상테이블(단방향, 양방향)
  - 다대다: 단방향, 양방향



## 6.1 다대일

- FK는 항상 Many 쪽에 있음
- 따라서 연관관계의 주인은 항상 Many



### 6.1.1 다대일 단방향 [N:1]

- 05장에서 다뤘던 `Member` -> `Team` 조회



### 6.1.2 다대일 양방향[N:1, 1:N]

- 05장에서 다뤘던 양방향 조회

  - `Member` -> `Team`에 `Team` -> `Member` 조회 추가

  - FK가 있는 쪽이 연관관계의 주인
  - 양방향 연관관계는 항상 서로를 참조해야 함: 편의 메서드 이용하자.



## 6.2 일대다

- 다대일 관계의 반대
- 하나 이상을 참조할 수 있기에 컬렉션을 사용



### 6.2.1 일대다 단방향[1:N]

- JPA2.0부터 지원함

- FK는 Many 쪽 테이블에 있음

- 때문에 일대다 단방향 매핑의 경우 매핑한 반대쪽 테이블의 FK를 관리하게 된다.

- 예시 코드

  - `Team`

    ```java
    @Entity
    public class Team {
        
        ...
            
        @OneToMany
        @JoinColumn(name = "TEAM_ID")	//MEMBER 테이블의 TEAM_ID (FK)
        private List<Member> members = new ArrayList<Member>();
        
        ...
    }
    ```

  - `Member`

    ```java
    @Entity
    public class Member {
        
        @Id @GeneratedValue
        @Column(name = "MEMBER_ID")
        private Long id;
        
        private String username;
        
        ...
    }
    ```

  - `저장 코드`

    ```java
        private void oneToManySave(EntityManager em) {
            Member member1 = new Member();
            member1.setUsername("testMemberA");
            Member member2 = new Member();
            member2.setUsername("testMemberB");
    
            Team team = new Team();
            team.setName("OneToManyUni");
            team.getMembers().add(member1);
            team.getMembers().add(member2);
    
            em.persist(member1);
            em.persist(member2);
            em.persist(team);	//insert쿼리뿐만 아니라, update쿼리가 2번 나가게 됨
            em.flush()
        }
    ```

- `@JoinColumn`을 명시해야 함

  - 명시하지 않을 경우 조인 테이블(joinTable) 전략을 사용하게 됨
  - 조인 테이블 전략의 경우 테이블이 하나 추가된다는 단점이 있음
  - 기본적으로는 조인 컬럼을 사용함이 낫다.

- 단점

  - 매핑 객체가 관리하는 FK가 다른 테이블에 있음
  - 연관관계의 주인 테이블에 FK가 있다면 처음 해당 테이블을 등록할 때 연관관계를 한 번의 INSERT로 해결할 수 있지만,
    위의 경우 추가로 UPDATE 문 실행해야 함

- **성능도 관리도 부담스럽기에 다대일 양방향 매핑을 쓰자**



### 6.2.2 일대다 양방향[1:N, N:1]

- 실제로는 구현 불가능
  - 관계가 양방향일 경우 반드시 `mappedBy` 속성을 통해 연관관계의 주인의 필드를 명시해줘야 함
  - 그런데 `@ManyToOne` 애노테이션의 속성으로 `mappedBy`를 줄 수 없기 때문에 무조건 Many와 One이 양방향 관계일 경우 무조건 다대일 양방향일수밖에 없음

- 흉내내는 방법: 일대다 단방향 + 다대일 단방향(읽기 전용)
  - 사실은 두 관계이며, 두 관계가 단일한 FK를 관리하기 때문에 문제 발생할 수 있음 -> 읽기 전용으로 관리
  - 일대다 단방향의 문제를 고스란히 가짐

- 예시 코드

  - `Team`: 동일하다!

    ```java
    @Entity
    public class Team {
        
        ...
        
        @OneToMany
        @JoinColumn(name = "TEAM_ID")
        private List<Member> members = new ArrayList<>();
        
        ...
    }
    ```

  - `Member`: 조회 전용 매핑이 추가되었다!

    ```java
    @Entity
    public class Member {
        
        @Id @GeneratedValue
        @Column(name = "MEMBER_ID")
        private Long id;
        
        ...
        @ManyToOne
        @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
        private Team team;
        ...
    }
    ```

    




## 6.3 일대일[1:1]

- 다대일과 달리 어느 쪽이든 FK를 가질 수 있다 -> 어떤 테이블이 FK를 가질지 선택해야 한다.
  - 주 테이블에 FK
    - 주 객체가 대상 객체를 참조하듯이, 주 테이블에 FK를 놓음
    - 객체의 참조는 **참조하는 주체가 참조되는 대상을 필드로 소유**하는 방식인데,
      이 방식의 경우 **주 테이블이 FK를 소유**한다는 점에서 객체지향과 유사하다.
      -> 객체지향 개발자들의 선호
    - 주 테이블만 확인해도 FK를 확인하여, 연관관계를 알 수 있다는 장점
    - 값이 없으면 FK에 `null` 허용
  - 대상 테이블에 FK
    - 전통적으로 DB 개발자들이 선호
    - 테이블 관계를 1:1 -> 1:N으로 변경할 때 테이블 구조를 유지할 수 있다는 장점
  - FK에는 UNIQUE 제약조건 붙여주자.



### 6.3.1 주 테이블에 외래 키

- 객체지향 개발자들이 선호, JPA 매핑이 편리



#### 단방향

- 예시: `Member`와 `Locker`

  - `Member`

    ```java
    @Entity
    public class Member {
    
        @Id @GeneratedValue
        @Column(name = "MEMBER_ID")
        private Long id;
    
        ....
            
        @OneToOne
        @JoinColumn(name = "LOCKER_ID")
        private Locker locker;
        
        ...
    }
    ```

  - `Locker`

    ```java
    @Entity
    public class Locker {
    
        @Id @GeneratedValue
        @Column(name = "LOCKER_ID")
        private Long id;
    
        private String name;
    }
    ```

    - 다대일 관계와 유사



#### 양방향

- 코드

  - `Locker`

    ```java
    @Entity
    public class Locker {
    
        @Id @GeneratedValue
        @Column(name = "LOCKER_ID")
        private Long id;
    
        private String name;
        
        @OneToOne(mapped by = "locker")
        private Member member;
    }
    ```

- 양방향이기에 연관관계의 주인 설정해야 함
  -> FK를 가진 것은 MEMBER 테이블이기에, `Member`의 `Member.locker`가 연관관계의 주인이다.

  - 따라서 `Locker.member`에는 `mappedBy` 설정하여 연관관계의 주인이 아니라고 설정 



### 6.3.2 대상 테이블에 외래 키

#### 단방향

- 1:1 관계에서, 대상 테이블에 외래 키가 있는 단방향 관계는 JPA가 지원하지 않으며, 매핑할 방법도 없음
  - 이 경우
    - 단방향 관계를 반대로 수정하거나
    - 양방향 관계로 만들고 대상 테이블이 연관관계의 주인으로 설정해야 한다.
  - JPA2.0: 1:N 단방향 관계에서 대상 테이블에 FK가 있는 매핑을 허용
  - 1:1 단방향은 여전히 이런 방식 안 됨!



#### 양방향

- 예제: 객체 양방향 조회 + `Locker`에 FK 보관

  - `Member`

    ```java
    @Entity
    public class Member {
    
        @Id @GeneratedValue
        @Column(name = "MEMBER_ID")
        private Long id;
    
        ....
            
        @OneToOne(mapped by = "member")
        private Locker locker;
        
        //team
        //product
        //order
        ...
    }
    ```

  - `Locker`

    ```java
    @Entity
    public class Locker {
    
        @Id @GeneratedValue
        @Column(name = "LOCKER_ID")
        private Long id;
    
        private String name;
        
        @OneToOne
        @JoinColumn(name = "MEMBER_ID")
        private Member member;
    }
    ```

    - 주 엔티티가 아니라 대상 엔티티가 연관관계의 주인, 외래 키 관리

- **프록시 사용시 주의사항**

  - **FK를 직접 관리하지 않는 1:1 관계는 지연 로딩을 사용하더라도 즉시 로딩된다.**
    - `Locker`에 값이 있는가를 `Member`에서 확인할 수는 없다. 때문에 프록시를 생성할 수 없다.
    - 저 자리에 프록시 객체를 넣을지 `null`을 넣을지 모르기 때문!





## 6.4 다대다[N:M]

- RDB는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없음
- 연결 테이블을 이용해서 보통 구현한다.
  - 중간 테이블을 생성, 일대다, 다대일 관계로 풀어낸다.
- 객체 연결의 경우 연결을 위해 추가적인 무언가가 필요하지 않고, 서로 직접적으로 다대다 연관관계를 맺을 수 있다.



### 6.4.1 다대다: 단방향

- 예시: 여러 `Member`와 여러 `Product`의 관계

  - `Member`

    ```java
    @Entity
    public class Member {
    
        @Id @GeneratedValue
        @Column(name = "MEMBER_ID")
        private Long id;
    
        ...
            
        @ManyToMany
        @JoinTable(name = "MEMBER_PRODUCT",
                joinColumns = @JoinColumn(name = "MEMBER_ID"), 
                inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID"))
        private List<Product> products = new ArrayList<>();
        
        ...
    }
    ```

  - `Product`

    ```java
    @Entity
    public class Product {
    
        @Id @Column(name = "PRODUCT_ID")
        private Long id;
    
        private String name;
    }
    ```

- `@ManyToMany`와 `@JoinTable`로 별도의 연결 엔티티를 정의할 필요 없이, 연결 테이블을 바로 매핑하였음.

- `@JoinTable` 속성

  - `name`: 연결 테이블을 지정(MEMBER_PRODUCT)
  - `joinColumns`: 현재 방향(`Member`)와 매핑할 조인 컬럼 정보를 지정(MEMBER_ID)
  - `inverseJoinColumns`: 연관관계의 반대 방향과 매핑할 조인 컬럼 정보를 지정(PRODUCT_ID)

- 조회 시에는 연결 테이블과 `Product` 테이블을 조인한다.



### 6.4.2 다대다: 양방향

- `@ManyToMany`을 양 쪽에 사용하고, 원하는 쪽에 `mappdeBy`로 연관관계의 주인 설정(`mappedBy` 없는 쪽이 연관관계의 주인)

- 예시 코드

  - `Product`

    ```java
    @Entity
    public class Product {
    
        @Id @Column(name = "PRODUCT_ID")
        private Long id;
    
        private String name;
        
        @ManyToMany(mappedBy = "products")
        private List<Member> members;
    }
    ```

  - `Member`

    ```java
        public void addProduct(Product product) {
            products.add(product);
            product.getMembers().add(this);
        }
    ```

    - 편의 메서드를 추가했다.



### 6.4.3 다대다: 매핑의 한계와 극복, 연결 엔티티 사용

- 위의 방법과 같이 `@ManyToMany`만으로 연결 테이블을 자동으로 생성해주는 방식은 (연결 엔티티를 따로 정의하지 않는 경우는)

  - 도메인 모델이 단순해지고 편리하다.
  - 하지만 실무에서 사용하기에는 한계가 있다: **연결 테이블에 추가적인 정보를 담을 수 없기 때문이다!**
    - 위의 `Member`와 `Product`의 경우, 회원이 상품을 주문했을 때
      - 단순히 주문자가 누구인지, 상품이 무엇인지만 표시하는 것으로 그치지 않고, (기존의 연결 테이블의 컬럼들)
      - **주문 수량, 주문 날짜 등의 추가적인 정보를 담은 컬럼이 필요하다!**

- 때문에 이 한계를 극복하기 위해서는 **별도의 연결 엔티티를 생성하고, 그것을 기준으로 일대다, 다대일 관계로 풀어야 한다.**

- 예시 코드: 단방향(`Member` -> `Product`)

  - `MemberProduct`: 연결 테이블

    ```java
    @Entity
    @IdClass(MemberProductId.class)
    public class MemberProduct {
    
        @Id
        @ManyToOne
        @JoinColumn(name = "MEMBER_ID")
        private Member member;
    
        @Id
        @ManyToOne
        @JoinColumn(name = "PRODUCT_ID")
        private Product product;
    
        private int orderAmount;
    }
    ```

  - `MemberProductId`: 연결 테이블이 사용할 복합 기본 키

    ```java
    @EqualsAndHashCode
    public class MemberProductId implements Serializable {
    
        private String member;
        private String product;
    }
    ```

  - `Member`

    ```java
        @OneToMany(mappedBy = "member")
        private List<MemberProduct> memberProducts;
    ```

  - `Product`

    ```java
    @Entity
    public class Product {
    
        @Id @Column(name = "PRODUCT_ID")
        private Long id;
    
        private String name;
    }
    ```

- 관계
  - `Member`는 `MemberProduct`와 양방향 관계
    - `MemberProduct`는 연관관계의 주인(Many 쪽이 연관관계의 주인이니까)
    - `Member`가 `Product`를 조회하기 위해 양방향 관계인 것임
- **연결 테이블**

  - `@Id`: PK 매핑 + `@JoinColumn`: FK 매핑 -> 동시에 PK, FK 매핑
  - `@IdClass`로 복합 기본 키 매핑
- **복합 기본 키**
  - 복합 키를 사용하려면 별도의 식별자 클래스를 만들어야함(`MemberProductId`)
  - 특정 데이터를 식별하는 것이 의미가 없기에 사용함
  - 복합 키 식별자 클래스 특징
    - 별도의 식별자 클래스로 만들어야 함
    - `Serializable`을 구현해야 함
    - `equals`와 `hashCode` 구현해야 함
    - 기본 생성자가 있어야 함
    - 식별자 클래스는 `public` 이어야 함
    - `@IdClass` 대신 `@EmbeddedId` 사용하는 방법도 있음
- 식별 관계(Identifying Relationship)
  - 부모 테이블의 PK를 받아서 자신의 PK + FK로 사용하는 것
- 복합 키를 사용할 경우 매핑을 위해 수행할 작업이 많음
  - 항상 식별자 클래스를 만들어야 함
  - `@IdClass` or `@EmbbdedId` 사용
  - 식별자 클래스의 `equals`, `hashCode`



### 6.4.4 다대다: 새로운 기본 키 사용

- DB가 생성해주는 키 값을 사용하는 방식

- 간편하고,  거의 영구히 사용 가능하며, 비즈니스에 의존하지 않음

- 코드

  ```java
  @Entity
  public class Order {
  
      @Id @GeneratedValue
      @Column(name = "ORDER_ID")
      private Long id;
  
      @ManyToOne
      @JoinColumn(name = "MEMBER_ID")
      private Member member;
  
      @ManyToOne
      @JoinColumn(name = "PRODUCT_ID")
      private Product product;
  
      private int OrderAmount;
  }
  ```

  

### 6.4.5 다대다 연관관계 정리

- 연결 테이블 생성 시 식별자를 어떻게 구성할지 정해야함
  - 식별 관계: 받아온 식별자를 PK + FK로 사용
  - 비식별 관계: 받아온 식별자는 FK로만 사용, 별도의 PK



# 07장 고급 매핑

## 7.1 상속 관계 매핑

- RDB에는 상속 개념은 없지만, 그나마 유사한 Super-Type, Sub-Type 논리 모델이 있다.
  - ORM의 상속관계 매핑은 객체 상속 구조를 슈퍼타입, 서브타입 관계로 매핑하는 것이다.
- **슈퍼타입-서브타입 논리 모델을 물리 모델(테이블)로 구현하는 방법**
  - 각각의 테이블로 변환하기
    - 각각을 모두 테이블로 만들고 조회 시 조인 사용
    - JPA에서는 조인 전략이라 부름
  - 통합 테이블로 변환
    - 하나의 테이블 안에서 관리
    - JPA에서는 단일 테이블 전략이라 부름
  - 서브타입 테이블로 변환
    - 서브 타입마다 하나의 테이블을 만듦
    - JPA에서는 구현 클래스마다 테이블 전략이라 부름



### 7.1.1 조인 전략

- Joined Strategy

- 방식

  - 엔티티 각각에 대해 모두 테이블을 만듦
  - 자식 테이블은 부모 테이블의 PK를 받아서 PK+FK로 사용한다.
  - 타입을 구분하는 컬럼을 추가함
    - 객체는 타입으로 구별 가능하지만, 테이블에는 타입 개념이 없기 때문
    - 구별용 컬럼의 기본 이름은 DTYPE

- 예제 코드

  - `Item`: 부모 클래스

    ```java
    @Entity
    @Inheritance(strategy = InheritanceType.JOINED)
    @DiscriminatorColumn(name = "DTYPE")
    public abstract class Item {
        
        @Id @GeneratedValue
        @Column(name = "ITEM_ID")
        private Long id;
        
        private String name;
        private int price;
    }
    ```

  - `Album`: 자식 클래스

    ```java
    @Entity
    @DiscriminatorValue("A")
    public class Album extends Item {
        
        private String artist;
    }
    ```

  - `@Inheritance(strategy = InheritanceType.JOINED)`: 상속 매핑 시에 사용하는 애노테이션
  - `@DiscriminatorColumn(name = "DTYPE")`: 구분용 컬럼: 저장된 자식 테이블을 구분하는 용도, 기본값은 `DTYPE`
  - `@DiscriminatorValue("A")` 구분 컬럼에 입력할 값
  - PK 컬럼명 변경
    - 위의 경우 부모 테이블의 PK 컬럼명을 그대로 사용한다.
    - 변경하고 싶을 경우 자식 클래스에 `@PrimaryKeyJoinColumn(name = "ALBUM_ID")` 붙이는 식으로 지정 가능

- 장점

  - 테이블이 정규화된다.
  - FK 참조 무결성 제약조건을 활용할 수 있음
  - 저장공간을 효율적으로 사용

- 단점

  - 조회 시 조인이 많이 사용되기에 성능 저하 가능성
  - 조회 쿼리가 복잡함
  - 데이터 저장 시에 INSERT 쿼리가 두 번 실행됨

- 구분 컬럼의 필수 여부

  - JPA 표준 명세는 구분 컬럼을 사용하게 강제
  - 하이버네이트 등 몇몇 구현체에서는 구분 컬럼 없이도 동작



### 7.1.2 단일 테이블 전략

- Inheritance 기본 생성 전략
- Single-Table Strategy
- 방식
  - 단 하나의 테이블만 사용
  - 구분 컬럼으로 어떤 자식 데이터인가를 구별
- 예제 코드
  - 7.1.1의 예제에서 부모 클래스의 애노테이션 전략만 변경하면 동일
  - `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`
- 장점
  - 조인이 필요 없기에 조회 성능이 일반적으로 가장 빠름
  - 조회 쿼리가 단순
- 단점
  - 자식 엔티티가 매핑한 컬럼은 모두 null을 허용해야 한다.
  - 타 전략에 비해 테이블이 비대해지기 쉬움 -> 경우에 따라 조회 성능이 오히려 떨어질 수 있음
- 특징
  - 구분 컬럼을 반드시 사용해줘야 함: `@DiscriminatorColumn` 반드시 설정
  - `@DiscriminatorValue` 지정하지 않을 경우 기본 값으로 엔티티 이름 사용



### 7.1.3 구현 클래스마다 테이블 전략

- Table-per-Concrete-Class Strategy

- 방식

  - 자식 엔티티마다 테이블을 생성
  - 자식 테이블 각각에 모든 컬럼이 있음(즉, 부모 클래스의 필드에 대응하는 컬럼들을 각 테이블이 모두 가지고 있음)

- 예제 코드

  - `Item`

    ```java
    @Entity
    @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
    public abstract class Item {
    
        @Id
        @GeneratedValue
        @Column(name = "ITEM_ID")
        private Long id;
    
        private String name;
        private int price;
    }
    ```

  - `Album`

    ```java
    @Entity
    public class Album extends Item {
    
        private String artist;
    }
    ```

  - 위의 전략들과 달리 구별 컬럼 설정 안 함

- **DB 설계자, ORM 전문가 모두가 일반적으로 추천하지 않는 전략**

- 장점

  - 서브 타입을 구분해서 처리할 때 효과적
  - `NOT NULL` 제약조건 사용 가능

- 단점

  - 여러 자식 테이블을 함께 조회할 때 성능이 느림: UNION 사용해야 함
  - 자식 테이블을 통합해서 쿼리하기 힘듦
  - 변경이 어려움

- 특징

  - 구분 컬럼을 사용하지 않음



## 7.2 `@MappedSuperClass`

- 개념

  - 부모 클래스는 테이블과 매핑하지 않고, 자식 클래스에게 매핑 정보만 제공하고 싶을 때 사용

  - `@Entity`와 달리 실제 테이블과 매핑되지 않음

  - 단순히 매핑 정보를 상속할 목적으로만 사용!

- 예제 코드

  - `BaseEntity`: 공통 매핑 정보를 담음

    ```java
    @MappedSuperclass
    public abstract class BaseEntity {
    
        @Id @GeneratedValue
        private Long id;
        private String name;
    }
    ```

  - `Member`: `BaseEntity`의 매핑 정보 + a

    ```java
    @Entity
    public class Member extends BaseEntity {
    
        private String email;
    }
    ```

  - `Seller`: `BaseEntity`의 매핑 정보를 받으면서 `BaseEntity`의 매핑 정보를 재정의

    ````java
    @Entity
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "SELLER_ID")),
            @AttributeOverride(name = "name", column = @Column(name = "SELLER_NAME"))
    })
    public class Seller extends BaseEntity {
    
        private String shopName;
    }
    ````

    - 단일 컬럼 재정의의 경우 `@AttributeOverride`
    - 복합 컬럼 재정의의 경우 `@AttributeOverrides`

- 특징

  - 테이블과 매핑되지 않음, 매핑 정보 상속만을 위해 사용
    - 엔티티가 아니기에 `em.find()`나 `JPQL`에서 사용 불가
  - 직접 생성할 일이 거의 없기에 추상 클래스로 생성하는 것을 권장
  - **상속 매핑에 적합, 특히 공통적으로 사용하는 속성을 효과적으로 관리할 수 있음**
  - `@Entity`는 `@Entity`이거나 `@MappedSuperClass`로 지정한 클래스만 상속받을 수 있음



## 7.3 복합 키와 식별 관계 매핑

- 유용한 자료: https://techblog.woowahan.com/2595/

### 7.3.1 식별 관계 vs 비식별 관계

- 식별 관계(Identifying Relationship)
  - 부모 테이블의 PK를 받아 자식 테이블의 PK + FK로 사용하는 관계
- 비식별 관계(Non-Identifying Relationship)
  - 부모 테이블의 PK를 받아 자식 테이블의 FK로만 사용하는 관계
  - 필수적(Mandatory) 비식별 관계
    - FK는 NOT NULL, 연관관계를 필수로 맺어야 함
  - 선택적(Optional) 비식별 관계
    - FK는 NULLABLE, 연관관계를 맺을 지는 선택적
- 최근의 추세: **비식별 관계를 주로 사용, 식별 관계는 꼭 필요한 곳에만 사용**



### 7.3.2 복합 키: 비식별 관계 매핑

- **단순히 두 필드에 `@Id` 애노테이션을 붙이는 것으로 복합 PK를 매핑할 수 없다!**
  - JPA는 영속성 컨텍스트에 엔티티를 보관할 때 식별자를 키로 사용
  - `equals()`와 `hashCode()`를 사용하여 식별자를 구별하기에
    **복합 키의 경우 별도의 식별자 클래스를 만들고, 거기에 `equals()`, `hashCode()`를 구현해야 한다!**
- JPA에서 복합키를 구현하는 방법
  - `@IdClass`: 좀더 DB 친화적인 방식
  - `@EmbeddedId`: 좀 더 객체지향적인 방식



#### `@IdClass`

- 예제 코드

  - `Parent`: 복합 키를 PK로 사용하는 엔티티

    ```java
    @Entity
    @IdClass(ParentId.class)
    public class Parent {
    
        @Id @Column(name = "PARENT_ID1")
        private String id1;
        @Id @Column(name = "PARENT_ID2")
        private String id2;
    
        private String name;
    }
    ```

  - `ParentId`: 복합 키 식별자 클래스

    ```java
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public class ParentId implements Serializable {
    
        private String id1;
        private String id2;
    }
    ```

  - `Child`: 비식별 관계 자식 클래스

    ```java
    @Entity
    public class Child {
    
        @Id
        private String id;
    
        @ManyToOne
        @JoinColumns({
                @JoinColumn(name = "PARENT_ID1",
                        referencedColumnName = "PARENT_ID1"),
                @JoinColumn(name = "PARENT_ID2",
                        referencedColumnName = "PARENT_ID2")
        })
        private Parent parent;
    }
    ```

    - FK가 복합 키이기에 매핑 시 여러 컬럼을 매핑해야 한다: `@JoinColumns`로 매핑
    - `@JoinColumn`의 `name` 속성과 `referencedColumnName` 속성 값이 동일할 경우 `referencedColumnName` 생략 가능

- `@IdClass` 사용시 식별자 클래스의 조건

  - 식별자 클래스의 속성명 == 엔티티에서 사용하는 식별자의 속성명
  - `Serializable`을 구현해야 함
  - `equals()`, `hashCode()`구현해야 함
  - 기본 생성자가 있어야 함
  - 식별자 클래스는 `public`이어야 함

- 사용

  - 복합 키를 사용하는 클래스가 해당하는 식별자 필드들을 설정해주기만 하면 된다.

  - 원리

    - 등록: `em.persist()` 호출 시

      - 영속성 컨텍스트에 엔티티를 등록하기 이전에
      - **내부적으로 (`@Id`로 등록된) 식별자 필드들을 사용하여 식별자 클래스를 생성, 영속성 컨텍스트의 키로 사용**
      - 즉 직접적으로 식별자 클래스를 만들어 줄 필요는 없음

    - 조회

      ```java
      ParentId parentId = new ParentId("식별자값1", "식별자값2");
      em.find(Parent.class, parentId);
      ```

      - 식별자 클래스를 생성하여 조회의 키 값으로 사용하면 된다.



#### EmbeddedId

- 예제 코드

  - `Parent`

    ```java
    @Entity
    public class Parent {
    
        @EmbeddedId
        private EmbeddedParentId id;
    
        private String name;
    }
    ```

  - `ParentId`

    ```java
    @Embeddable
    @EqualsAndHashCode
    public class EmbeddedParentId implements Serializable {
    
        @Column(name = "PARENT_ID1")
        private String id1;
        @Column(name = "PARENT_ID2")
        private String id2;
    }
    ```

- `@IdClass`의 경우와 다르게 식별자 클래스에 PK를 직접 매핑한다.
- `@EmbeddedId` 사용시 식별자 클래스의 조건
  - `@Embeddable` 붙여 줘야 함
  - 속성명 일치를 제외한 `@IdClass` 사용 시의 제약조건들
- 사용
  - 저장, 조회 두 경우 모두 식별자 클래스를 직접 생성해서 사용한다.



#### 복합 키: 그 외 특징

- 공통적으로, 복합 키 클래스는 동치성 비교가 가능해야 하기에 `equals()`, `hashCode()` 구현해야 한다.

- `@EmbeddedId`는 보다 더 객체지향적이며 중복이 없다는 장점을 갖지만 경우에 따라 JPQL이 더 길어질 수 있음

  ```java
  em.createQuery("SELECT p.id.id1, p.id.id2 FROM Parent p");	 //@EmbeddedId
  em.createQuery("SELECT p.id1, p.id2 FROM Parent p");		//@IdClass
  ```

- 복합 키에는 `@GenerateValue` 사용 불가, 복합 키 구성하는 컬럼에도 적용 불가



### 7.3.3 복합 키: 식별 관계 매핑

#### `@IdClass`와 식별 관계

- 예제 코드

  - `Parent`

    ```java
    @Entity
    public class Parent {
    
        @Id @Column(name = "PARENT_ID")
        private String id;
        private String name;
    }
    ```

  - `Child`

    ```java
    @Entity
    @IdClass(ChildId.class)
    public class Child {
    
        @Id
        @ManyToOne
        @JoinColumn(name = "PARENT_ID")
        public Parent parent;
    
        @Id
        @Column(name = "CHILD_ID")
        private String childId;
    
        private String name;
    }
    ```

  - `ChildId`

    ```java
    @EqualsAndHashCode
    public class ChildId implements Serializable {
    
        private String parent;
        private String ChildId;
    }
    ```

  - `GrandChild`

    ```java
    @Entity
    @IdClass(GrandChildId.class)
    public class GrandChild {
    
        @Id
        @ManyToOne
        @JoinColumns({
                @JoinColumn(name = "PARENT_ID"),
                @JoinColumn(name = "CHILD_ID")
        })
        private Child child;
    
        @Id
        @Column(name = "GRANDCHILD_ID")
        private String id;
        
        private String name;
    }
    ```

  - `GrandChildId`

    ```java
    @EqualsAndHashCode
    public class GrandChildId implements Serializable {
    
        private ChildId child;
        private String id;
    }
    ```

- PK 매핑과 연관관계 매핑 함께 함(FK)



#### `@EmbeddedId`와 식별관계

- 예제 코드

  - `Parent`

    ```java
    @Entity
    public class Parent {
    
        @Id
        @Column(name = "PARENT_ID")
        private String id;
    
        private String name;
    }
    ```

  - `Child`

    ```java
    @Entity
    public class Child {
    
        @EmbeddedId
        private ChildId id;
    
        @MapsId("parentId")
        @ManyToOne
        @JoinColumn(name = "PARENT_ID")
        private Parent parent;
    
        private String name;
    }
    ```

  - `ChildId`

    ```java
    @Embeddable
    @EqualsAndHashCode
    public class ChildId implements Serializable {
    
        private String parentId;
    
        @Column(name = "CHILD_ID")
        private String id;
    }
    ```

  - `GrandChild`

    ```java
    @Entity
    public class GrandChild {
    
        @EmbeddedId
        private GrandChildId id;
    
        @MapsId("childId")
        @ManyToOne
        @JoinColumns({
                @JoinColumn(name = "PARENT_ID"),
                @JoinColumn(name = "CHILD_ID")
        })
        private EbdChild child;
    
        private String name;
    }
    ```

  - `GrandChildId`

    ```java
    @Embeddable
    @EqualsAndHashCode
    public class GrandChildId implements Serializable {
    
        private ChildId childId;
    
        @Column(name = "GRANDCHILD_ID")
        private String id;
    }
    ```

- `@Id` 대신에 `@MapsId`를 사용함

  - `@MapsId`
    - FK와 매핑한 연관관계를 PK에도 매핑하겠음을 의미
    - 속성 값으로는 `@EmbeddedId`를 사용한 식별자 클래스의 PK 지정



### 7.3.4 비식별 관계로 구현

- 매핑 쉽고 코드도 간편함
- 복합 키가 없기에 복합 키 클래스를 만들 필요가 없음



### 7.3.5 일대일 식별 관계

- 자식 테이블의 PK 값으로 부모 테이블의 PK 값만 사용하면 됨

- 부모 테이블의 PK가 복합 키가 아니라면 자식 테이블의 PK도 복합 키로 구성하지 않아도 됨

- 예제 코드: `Board`와 그 자식 테이블 `BoardDetail`

  - `Board`

    ```java
    @Entity
    public class Board {
    
        @Id @GeneratedValue
        @Column(name = "BOARD_ID")
        private Long id;
    
        private String title;
    
        @OneToOne(mappedBy = "board")
        private BoardDetail boardDetail;
    }
    ```

  - `BoardDetail`

    ```java
    @Entity
    public class BoardDetail {
    
        @Id
        private Long boardId;
    
        @MapsId
        @OneToOne
        @JoinColumn(name = "BOARD_ID")
        private Board board;
    
        private String content;
    }
    ```

  - 사용 코드

    ```java
    Board board = new Board();
    board.setTitle("제목");
    em.persist(board);
    
    BoardDetail boardDetail = new BoardDetail();
    boardDetail.setContent("내용");
    boardDetail.setBoard(board);
    em.persist(boardDetatil)
    ```

- 식별자가 컬럼 하나일 때는 `@MapsId` 속성 값을 비워 두면 됨



### 7.3.6 식별, 비식별 관계의 장단점

- DB 설계 관점에서도, ORM 관점에서도 비식별 관계가 선호된다:

- **DB 설계 관점에서 비식별 관계를 선호**하는 이유

  - 식별 관계의 경우 자식 테이블의 PK 컬럼이 점점 늘어남
    - 부모의 PK가 계속 전달되기 때문
    - 결과적으로 조인할 때 쿼리가 복잡해지면 PK 인덱스가 지나치게 커질 수 있음
  - 식별 관계의 경우 복합 PK를 만들어야 하는 경우가 많음
  - 식별 관계의 경우 비즈니스상 의미 있는 자연 키 컬럼을 조합하여 복합 PK를 만드는 경우가 많음
    - 반면 비식별 관계의 경우 대리 키를 주로 사용함
    - 자연키 사용으로 인해 발생할 수 있는 변경 상황일 때, PK 전파로 인하여 더 변경이 힘들어진다.
  - 식별 관계는 PK 전파되기에 비식별 관계에 비해 테이블 구조가 유연하지 못 함

- **ORM 관점에서 비식별 관계를 선호**하는 이유

  - 일대일 관계가 아닌 경우 식별 관계는 복합 PK를 사용하게 됨
    - 복합 키는 별도의 클래스를 생성해서 사용해야 함 -> 더 많은 노력이 필요
  - 비식별 관계의 경우 주로 PK로 대리 키 사용, JPA는 `@GeneratedValue`처럼 편리한 대리 키 생성 방식 제공

- 식별 관계의 장점

  - PK 인덱스를 사용하기 좋음, PK 전파로 인해 특정 상황에서 조인 없이 하위 테이블에서 검색 가능

  - 부모 아이디가 A인 모든 자식 조회

    ```SQL
    SELECT * FROM CHILD
    WHERE PARENT_ID 'A'
    ```

  - 부모 아이디가 A고 자식 아이디가 B인 자식 조회

    ```SQL
    SELECT * FROM CHILD
    WHERE PARENT_ID = 'A' AND CHILD_ID = 'B'
    ```

- **정리**

  - **가급적 비식별 관계, PK는 `Long` 타입 사용**
  - **선택적 비식별 관계보다는 필수적 비식별 관계를 사용하는 것이 좋음**
    - 선택적 비식별 관계는 NULL을 허용하기에 조인 시에 외부 조인을 사용하게 됨
    - 필수적 비식별 관계는 항상 관계가 있음을 보장하기에 내부 조인만 사용해도 됨



## 7.4 조인 테이블

- DB 테이블의 연관관계를 설정하는 방법
  - 조인 컬럼 사용(FK)
  - 조인 **테이블** 사용
- 조인 테이블 방식
  - 연관관계를 관리하는 조인 테이블이 있기에 연관관계가 맺어지는 테이블에는 관리를 위한 FK 컬럼이 없음
  - 조인 컬럼 사용하는 경우, 선택적 비식별 관계의 경우 FK에 NULL을 허용한다는 문제가 있었지만,
    - 조인 테이블 방식 사용 시, 연관관계를 맺고 싶을 때 그냥 연관관계 관리 테이블에 값을 추가해주면 됨
  - 테이블이 하나 더 추가된다는 단점
    - 관리해야 하는 테이블이 증가하며
    - 연관관계를 맺은 양 쪽 테이블을 조인하려면, 조인 테이블까지 추가로 조인해야 한다.
  - **기본적으로는 조인 컬럼을 사용하고 필요한 경우에만 조인 테이블을 사용하자.**
- 조인 테이블에 대한 기본 내용
  - 객체와 테이블을 매핑할 때
    - 조인 컬럼은 `@JoinColumn`으로 관리, 조인 테이블은 `@JoinTable`로 관리
  - 조인 테이블은 로 다대다 관계를 일대다, 다대일 관계로 풀어내기 위해 사용
    - 일대일, 일대다, 다대일 관계에서도 사용하기도 함
  - 연결 테이블, 링크 테이블이라고도 부름



### 7.4.1 일대일 조인 테이블

- 조인 테이블의 FK 컬럼 각각에 UNIQUE 제약조건을 걸어야 함

- 예제 코드
  - `Parent`

    ```java
    @Entity
    public class Parent {
    
        @Id
        @GeneratedValue
        @Column(name = "PARENT_ID")
        private Long id;
        private String name;
    
        @OneToOne
        @JoinTable(name = "PARENT_CHILD",
                joinColumns = @JoinColumn(name = "PARENT_ID"),
                inverseJoinColumns = @JoinColumn(name = "CHILD_ID"))
        private Child child;
    }
    ```

  - `Child`

    ```java
    @Entity
    public class Child {
    
        @Id
        @GeneratedValue
        @Column(name = "CHILD_ID")
        private Long id;
        private String name;
    }
    ```

    - 양방향 매핑 시 추가 코드

      ```java
      @OneToOne(mappedBy = "child")
      private Parent parent;
      ```

- `@JoinTable`

  - `@JoinColumn` 대신 사용하였음

  - `JoinTable` 속성

    | 속성               | 설명                          |
    | ------------------ | ----------------------------- |
    | name               | 매핑할 조인 테이블 이름       |
    | joinColumns        | 현재 엔티티를 참조하는 FK     |
    | inverseJoinColumns | 반대방향 엔티티를 참조하는 FK |



### 7.4.2 일대다 조인 테이블

- 다(N)와 관련된 컬럼에 UNIQUE 제약 조건을 걸면 된다.

- 예시 코드

  - `Parent`

    ```java
    @Entity
    public class Parent {
    
        @Id
        @GeneratedValue
        @Column(name = "PARENT_ID")
        private Long id;
        private String name;
    
        @OneToMany
        @JoinTable(name = "PARENT_CHILD",
                joinColumns = @JoinColumn(name = "PARENT_ID"),
                inverseJoinColumns = @JoinColumn(name = "CHILD_ID"))
        private List<Child> child = new ArrayList<>();
    }
    ```

  - `Child`

    ```java
    @Entity
    public class Child {
    
        @Id
        @GeneratedValue
        @Column(name = "CHILD_ID")
        private Long id;
        private String name;
    }
    ```



### 7.4.3 다대일 조인 테이블

- 예제 코드

  - `Parent`

    ```java
    @Entity
    public class MTOParent {
    
        @Id
        @GeneratedValue
        @Column(name = "PARENT_ID")
        private Long id;
        private String name;
    
        @OneToMany(mappedBy = "parent")
        private List<MTOChild> child = new ArrayList<>();
    }
    ```

  - `Child`

    ```java
    @Entity
    public class MTOChild {
    
        @Id
        @GeneratedValue
        @Column(name = "CHILD_ID")
        private Long id;
        private String name;
    
        @ManyToOne(optional = false)
        @JoinTable(name = "PARENT_CHILD",
        joinColumns = @JoinColumn(name = "CHILD_ID"),
        inverseJoinColumns = @JoinColumn(name="PARENT_ID"))
        private MTOParent parent;
    }
    ```



### 7.4.4 다대다 조인 테이블

- 조인 테이블의 두 컬럼을 합쳐서 복합 UNIQUE 제약조건을 걸어야 함

- 예시 코드

  - `Parent`

    ```java
    @Entity
    public class Parent {
    
        @Id
        @GeneratedValue
        @Column(name = "PARENT_ID")
        private Long id;
        private String name;
    
        @ManyToMany
        @JoinTable(name = "PARENT_CHILD",
                joinColumns = @JoinColumn(name = "PARENT_ID"),
                inverseJoinColumns = @JoinColumn(name = "CHILD_ID"))
        private List<Child> children = new ArrayList<>();
    }
    ```

  - `Child`

    ```java
    @Entity
    public class Child {
    
        @Id
        @GeneratedValue
        @Column(name = "CHILD_ID")
        private Long id;
        private String name;
    }
    ```

- 조인 테이블에 컬럼을 추가하면 `@JoinTable` 전략 사용 불가



## 7.5 엔티티 하나에 여러 테이블 매핑

- `@SecondaryTable` 사용하여 한 엔티티에 여러 테이블 매핑 가능

- 예제 코드

  ```java
  @Entity
  @Table(name = "BOARD")
  @SecondaryTable(name = "BOARD_DETAIL",
          pkJoinColumns = @PrimaryKeyJoinColumn(name = "BOARD_DETAIL_ID"))
  public class MltBoard {
  
      @Id
      @GeneratedValue
      @Column(name = "BOARD_ID")
      private Long id;
  
      private String title;
  
      @Column(table = "BOARD_DETAIL")
      private String content;
  }
  ```

- `@SecondaryTable`의 속성

  | 속성          | 필드                              |
  | ------------- | --------------------------------- |
  | name          | 매핑할 다른 테이블의 이름         |
  | pkJoinColumns | 매핑할 다른 테이블의 PK 컬럼 속성 |

- `@Column(table = "BOARD_DETAIL")`
  - 해당 필드를 추가 테이블 컬럼에 매핑
  - 지정하지 않을 시 엔티티 기본 테이블에 매핑
- **항상 두 테이블 이상을 조회하기에 최적화가 어려움**



# 08장 프록시와 연관관계 관리

- 프록시
  - 연관된 객체를 처음부터 DB에서 조회하지 않고, 사용 시점으로 조회를 미룸
  - 단 자주 함께 사용하는 객체들의 경우 조인을 사용하여 함께 조회하는 것이 효과적
- 영속성 전이와 고아 객체: 연관된 객체를 함께 저장하거나 삭제할 수 있는 기능



## 8.1 프록시

- **지연 로딩**: 엔티티가 실제 사용될 때까지 DB 조회를 지연
- 지연 로딩을 위해서 실제 엔티티 객체를 대신할 **프록시** 객체가 필요
- **지연 로딩의 구현**
  - JPA 표준 명세상 구체적인 구현 방식은 각 JPA에 위임되어 있음, 이하의 내용은 하이버네이트 기준
  - 지연 로딩 사용을 위해 하이버네이트가 제공하는 방법
    - 프록시
    - 바이트 코드 수정: 복잡한 설정 필요



### 8.1.1 프록시 기초

- `em.find()`: 영속성 컨텍스트에 엔티티가 없으면 DB 조회
- `em.getReference()`: DB 조회하지 않음, 실제 엔티티 객체가 아닌 **DB 접근을 위임한 프록시 객체를 반환**
- 프록시 객체의 구조
  - 실제 객체에 대한 참조(`target`)을 보관
  - 실제 객체와 같은 이름의 메서드를 가지고 있으며, 해당 메서드 호출 시 실제 객체의 메서드를 호출
- 초기화
  - `member.getName()`처럼 실제 사용될 때 DB를 조회해서 엔티티 객체를 생성: **프록시 객체의 초기화**
  - 과정
    - 프록시 객체에 메서드 호출하여 실제 데이터 조회(ex. `member.getName()`)
    - 실제 엔티티가 생성되어 있지 않으면
      - 프록시 객체가 영속성 컨텍스트에 실제 엔티티 생성을 요청(**초기화**)
      - 영속성 컨텍스트가 DB 조회하여 실제 엔티티 객체 생성
      - 프록시 객체는 생성된 실제 엔티티 객체의 참조를 멤버변수 `target`에 보관
    - 프록시 객체가 실제 엔티티 객체의 메서드 호출하여 결과를 반환(ex. `target.getName()`)
- **특징**
  - 프록시 객체는 처음 사용할 때 한 번만 초기화됨
  - 초기화 발생해도 프록시 객체가 실제 엔티티로 바뀌지는 않음
  - 프록시 객체는 원본 엔티티를 상속받은 객체임(타입 체크에 유의)
  - 영속성 컨텍스트에 엔티티가 이미 있을 경우 `getReference()`호출해도 실제 엔티티를 반환
    - 이 경우 DB 조회할 필요가 없기 때문이다.

  - 준영속 상태의 프록시를 초기화하면 문제 발생
    - 초기화는 영속성 컨텍스트의 도움을 받아야 하는데, 준영속 상태면 영속성 컨텍스트의 도움을 받을 수 없기 때문
    - JPA 표준 명세는 지연 로딩(프록시)는 각 구현체에 맡겼기 때문에,
      위와 같은 문제 상황에 어떤 일이 발생할 지에 대해서 표준 명세에 규정되어 있지 않음
      - 하이버네이트의 경우 `LazyInitializationException` 발생




### 8.1.2 프록시와 식별자

- 프록시 객체는 식별자 값을 보관한다.

  - 프록시 조회 시에 파라미터로 전달되는 PK 값을 보관
  - 때문에 프록시 객체에 `getId()` 요청해도 프록시가 초기화되지 않는다.
    - 단 이는 엔티티 접근 방식을 프로퍼티로 설정한 경우에만 적용된다.
      `@Access(AccessType.PROPERTY)`
    - 접근 방식을 필드(`AccessType.FIELD`)로 설정할 경우
      해당 메서드가 PK 값만 조회하는 메서드인지, 다른 필드까지 활용하는 메서드인지 알 수 없기에 프록시 초기화 발생

- 프록시는 연관관계를 설정할 때 유용하게 사용 가능

  ```java
  Member member = em.find(Member.class, "member1");
  Team team = em.getReference(Team.class, "team1");	//쿼리 실행하지 않음
  member.setTeam(team);
  ```

  - 연관관계 설정 시에는 식별자 값만 사용하기에 프록시를 사용하여 DB 접근 회수를 줄일 수 있음
  - 엔티티 접근 방식을 필드로 설정했다고 해도, 연관관계 설정 시에는 프록시 초기화하지 않음



### 8.1.3 프록시 확인

- 프록시 인스턴스의 초기화 여부 확인
  - JPA의 `PersistenceUnitUtil.isLoaded(Object entity)` 메서드 사용
    - 초기화되었거나 프록시 인스턴스가 아닐 경우 `true` 반환
  - 클래스명을 통해 확인
    - 프록시 객체의 경우 클래스명이 해당 엔티티를 상속한 별개의 클래스임
- 프록시 강제 초기화
  - 하이버네이트의 `initialize()` 사용시 프록시 강제 초기화 가능
    - `unProxy()` 메서드도 사용 가능(5.2.10부터 가능)
  - JPA 표준에는 강제 초기화 메서드가 없음
    - 하이버네이트에 종속적이지 않은 방식으로 강제 초기화를 원한다면 프록시의 메서드를 호출하는 방식 이용



## 8.2 즉시 로딩과 지연 로딩

- 프록시 객체는 주로 연관된 엔티티를 지연 로딩할 때 사용됨
- JPA가 제공하는 연관된 엔티티의 조회 시점 선택 방법
  - 즉시 로딩
    - 엔티티를 조회할 때 연관된 엔티티도 함께 조회
    - 설정 방법: `@ManyToOne(fetch = FetchType.EAGER)`
  - 지연 로딩
    - 연관된 엔티티를 실제 사용할 때 조회함
    - 설정 방법: `@ManyToOne(fetch = FetchType.LAZY)`



### 8.2.1 즉시 로딩

- 즉시 로딩 설정 시 연관된 엔티티를 동시에 조회
  - **조회시 JPA의 경우 즉시 로딩을 최적화하기 위해 가능한 한 조인 쿼리를 사용함**
- **NULL 제약조건과 JPA 조인 전략**
  - 외래 키가 NULL을 허용하는 경우 외부 조인(LEFT OUTER JOIN)을 사용함
  - 외래 키가 NOT NULL 제약 조건을 설정하면 외래 키 값의 존재를 보장: 내부 조인을 사용
    - **`@JoinColumn(nullable = false)`로 JPA에게 인식시키면 내부 조인 사용**
    - 외부 조인에 비해 내부 조인은 성능과 최적화에서 유리
    - **`@ManyToOne(optional = false)`로 설정해도 내부 조인을 사용**



### 8.2.2 지연 로딩

- 연관된 객체는 조회되지 않고, 해당 멤버변수에는 프록시 객체가 들어감

### 8.2.3 정리

- 상황에 따라 적절히 사용해야함
  - 즉시 로딩을 과도하게 사용한다면 불필요한 데이터를 조회하느라 자원 낭비, 성능 저하
  - 지연 로딩을 과도하게 사용한다면 연관된 엔티티를 함께 사용하는 일이 많을 경우 비효율적



## 8.3 지연 로딩 사용

### 8.3.1 프록시와 컬렉션 래퍼

- 엔티티에 컬렉션이 있을 경우 하이버네이트는 컬렉션을 하이버네이트의 내장 컬렉션으로 변경
  - 컬렉션을 추적하고 관리하기 위함
- 클래스명을 출력하면: `org.hibernate.collection.internal.PersistentBag`
- 지연 로딩시에 컬렉션은 컬렉션 래퍼가 처리
  - 컬렉션에 대한 프록시 역할 수행
- 컬렉션의 경우 컬렉션의 실제 데이터를 조회할 때 DB 조회하여 초기화
  - 예시) `member.getOrders()`는 초기화를 발생시키지 않지만, `member.getOrders().get(0)`은 발생시킴



### 8.3.2 JPA 기본 페치 전략

- JPA의 기본 설정은 다음과 같음:
  - `@ManyToOne`, `@OneToOne`: 즉시 로딩
  - `OneToMany`, `@ManyToMany` 지연로딩
- 추천방식: **모든 연관관계에 지연 로딩을 사용**, 개발이 많이 진행된 이후 **상황에 따라 꼭 필요한 곳에만 즉시 로딩을 사용하게 최적화**
  - SQL을 직접 사용하는 경우 위와 같은 유연한 최적화가 어려움: 많은 SQL, 애플리케이션 코드 변경이 필요



### 8.3.3 컬렉션에 `FetchType.EAGER` 사용시 주의점

- **컬렉션을 2개 이상 즉시 로딩하는 것은 권장되지 않음**
  - DB 테이블 상으로 일대다 조인임
    - 일대다 조인은 결과 데이터가 다 쪽에 있는 만큼 증가
    - 서로 다른 테이블이 2개 이상일 경우 기하급수적으로 많은 데이터를 조회하게 됨
    - 예를 들어 A테이블을 크기가 N, M인 테이블과 일대다 조인하게 되면 결과는 N * M
  - JPA는 조회 결과를 메모리에서 필터링해서 반환: 지나치게 많은 메모리 소모
- **컬렉션 즉시 로딩은 항상 외부 조인을 사용**
  - 다대일 관계를 조인할 때는 NOT NULL 제약조건이 있다면 내부 조인 사용하지만
  - 일대다 관계를 조인할 때는 내부조인 사용 시 조회되지 않는 데이터가 있을 수도 있기에 **무조건 외부 조인을 사용하게 됨**
    (DB 제약조건으로 해결 불가)
- 설정에 따른 조인 전략
  - `@ManyToONe`, `@OneToOne`
    - `(optional = false)`: 내부 조인
    - `(optional = true)`: 외부 조인
  - `@OneToMany`, `@ManyToMany`
    - `(optional = false)`: 외부 조인
    - `(optional = true)`: 외부 조인



## 8.4 영속성 전이: CASCADE

- 영속성 전이(transitive persistence)
  - 특정 엔티티와 연관된 엔티티의 영속 상태를 동시 변경되게 함
  - JPA는 `CASCADE` 옵션으로 영속성 전이 지원
- **JPA에서 엔티티를 저장할 때 연관된 모든 엔티티는 영속 상태여야 함**
  - 연관된 엔티티에 영속성 전이를 사용하면 일일히 영속 상태로 만들지 않아도 됨
  - 예시: 부모(One)와 자식(Many) 연관관계에서, 부모에 CASCADE 옵션 적용하여, 
    부모만 영속 상태로 만들어도 자식도 영속 상태가 되게 할 수 있음



### 8.4.1 영속성 전이: 저장

- `cascadeType.PERSIST`
- 영속성 전이는 연관관계를 매핑하는 것과는 무관
  - 따라서 영속성 전이를 사용하더라도 연관관계를 추가한 이후 영속상태로 만들어야 함



### 8.4.2 영속성 전이: 삭제

- `cascadeType.REMOVE`
- FK 제약조건을 고려하여 자식을 먼저 삭제한 후에 부모를 삭제
  - 만일 영속성 전이를 설정하지 않고 부모 엔티티만 제거했을 경우, FK 무결성 예외가 발생



### 8.4.3 CASCADE의 종류

- `ALL`
- `PERSIST`
- `MERGE`
- `REMOVE`
- `REFRESH`
- `DETACH`



## 8.5 고아 객체

- 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제하는 기능
- 참고가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 봄
- `orphanRemoval = true`
- 부모 엔티티의 컬렉션에서 자식 엔티티의 참조만 제거하면 자식 엔티티가 자동으로 삭제됨
- 고아 객체 제거 기능은 영속성 컨텍스트를 플러시할 때 적용됨: 플러시 시점에 DELETE SQL 실행됨
- **고아 객체 삭제는 참조하는 곳이 하나일 때만 사용해야 함**
  - 즉, 특정 엔티티가 개인 소유하는 엔티티에만 사용해야 한다.
  - 때문에 `@OneToOne`, `@OneToMany`에만 사용 가능
- `CascadeType.REMOVE`를 설정한 것과 같은 효과를 내는 경우
  - 부모를 제거하면 자식은 고아가 됨 -> 따라서 부모 제거 시 자식은 자연히 제거됨



## 8.6 영속성 전이 + 고아 객체, 생명주기

- `CascadeType.ALL` + `orphanRemoval = true`
- 부모 엔티티를 통해 자식 엔티티의 생명주기를 관리할 수 있음
  - 자식을 저장하려면 부모에 등록만 하면 됨(CASCADE)
  - 자식을 삭제하려면 부모에서 제거하면 된다(orphanRemoval)
- 영속성 전이는 DDD의 Aggregate Root 개념을 구현할 때 사용하면 편리함



# 09장 값 타입

- JPA의 데이터 타입
  - 엔티티 타입
    - `@Entity`로 정의하는 객체
    - 식별자를 통한 주적 가능
  - 값 타입
    - 단순히 값으로 사용하는 자바 기본 타입 + 객체
    - . `int`, `Integer`, `String`
    - 식별자를 통한 추적 불가
- 값 타입의 분류
  - 기본값 타입(basic value type)
    - 자바 기본 타입: `int`, `double`
    - 래퍼 클래스: `Long`, `Double`
    - `String`
  - 임베디드 타입(embedded type): 복합 값 타입
  - 컬렉션 값 타입(collection value type)



## 9.1 기본값 타입

- 식별자 없음
- 생명주기를 엔티티에 의존
- 공유해선 안 됨
  - 자바의 기본 타입은 공유되지 않음
  - 래퍼 클래스나 `String`은 객체지만 자바가 기본 타입처럼 사용할 수 있게 지원함



## 9.2 임베디드 타입(복합 값 타입)

- 새로운 값 타입을 직접 정의해서 사용

- 예시 코드: `Address`

  ```java
  @Embeddable
  public class Address {
      
      @Column(name = "city")	//매핑할 컬럼 정의 가능
      private String city;
      private String street;
      private String zipcode;
  }
  ```

- 장점: 객체지향적

  - 응집도를 높음
  - 재사용성 높음
  - 코드를 더 명확하게 만듦
  - 의미 있는 메서드 생성 가능

- 임베디드 타입의 사용

  - 값 타입을 사용하기 위한 애노테이션: 둘 중 하나 이상은 있어야 함
    - `@Embeddable`: 값 타입의 정의
    - `@Embedded`: 값 타입을 사용함을 표시
  - 기본 생성자가 필수(`protected` 가능)

- 엔티티와 임베디드 타입은 컴포지션(composition) 관계임

  - 하이버네이트는 임베디드 타입을 컴포넌트(components)라 부름
  - 임베디드 타입도 값 타입이기에 엔티티의 생명주기에 의존



### 9.2.1 임베디드 타입과 테이블 매핑

- 임베디드 타입은 그저 값이기 때문에, 임베디드 타입을 쓰건 안 쓰건 간에 매핑하는 테이블은 동일

- 세밀한 매핑을 가능케 함: 잘 설계한 ORM 애플리케이션은 테이블의 수보다 객체의 수가 더 많음!

- UML 표기

  ```mermaid
  classDiagram
  class Member {
  Long id
  String name
  Period workPeriod
  Address homeAddress
  }
  <<entity>> Member
  ```

  - 기본 타입처럼 단순하게 표기하는 것이 편리



### 9.2.2 임베디드 타입과 연관관계

- 임베디드 타입은 값 타입을 포함하거나 엔티티를 참조할 수 있음

  - 엔티티는 공유 가능하기에 참조되며, 값 타임은 소속되며, 공유 불가능하기에 포함

- 예시코드

  - `Member`

    ```java
    @Entity
    public class Member {
        ...
        @Embedded Address address;
        @Embedded PhoneNumber phoneNumber;
        ...
    }
    ```

  - `PhoneNumber`

    ```java
    @Embeddable
    public class PhoneNumber {
        String areaCode;
        @ManyToOne
        PhoneServiceProvider provider;	//엔티티 참조
    }
    ```

  - 관계도

    ```mermaid
    classDiagram
    direction LR
    class Member
    <<Entity>> Member
    Member *-- PhoneNumber
    Member *-- Address
    <<Value>> Address
    <<Value>> PhoneNumber
    PhoneNumber o-- PhoneServiceProvider
    <<Entity>> PhoneServiceProvider
    ```



### 9.2.3 `@AttributeOverride`: 속성 재정의

- 특히, 임베디드 타입을 사용하다가 컬럼명이 중복될 경우에는 필수적으로 사용해야함

- 예제 코드

  ```java
  @Entity
  public class Member {
      @Embedded
      @AttributeOverrides({
          @AttributeOverride(name = "city", column=@Column(name="COMPANY_CITY")),
          ...
      })
      private Address companyAddress
  }
  ```

- **`@AttributeOverrides`는 엔티티에 설정해야 함! 임베디드 타입이 임베디드 타입을 가지고 있더라도 마찬가지!**



### 9.2.4 임베디드 타입과 `null`

- 임베디드 타입이 `null`일 경우 매핑한 컬럼 값은 모두 `null`이 된다.



## 9.3 값 타입과 불변 객체

### 9.3.1 값 타입 공유 참조

- 값 타입을 여러 엔티티에서 공유하면 위험: 특히 임베디드 타입
- 동일 주소를 참조해서 할당했을 경우, 한 엔티티에서 그 값 타입을 변경했을 경우, 다른 엔티티의 값까지 변화: **부작용(side effect)**



### 9.3.2 값 타입의 복사

- 실제 인스턴스인 값을 공유하지 말고, **복사해서 사용하여 문제를 방지**
- **임베디드 타입은 객체 타입이기에 공유 참조를 피할 수 없다.**
- 근본적인 대안: **값 타입의 객체를 불변 타입으로 만들자: 수정이 불가능하게 만들자**



### 9.3.3 불변 객체

- **값 타입은 가능한 불변 객체로 설계하자: 부작용의 원천 차단**
- 가장 간단한 구현법: 생성자로만 값을 설정, mutator 만들지 않기



## 9.4 값 타입의 비교

- 동일성이 아니라 동치성을 비교할 수 있게끔 `equals()` 재정의하자, 또 `equals()` 재정의했으니, `hashCode()`도 재정의하자.



## 9.5 값 타입 컬렉션

- 복수의 값 타입을 보관하고 싶을 경우

  - 컬렉션에 보관하고, `@ElementCollection`, `@CollectionTable`을 사용

- 예제 코드

  - `Member`

    ```java
    @Entity
    public class Member {
        ...
        @ElementCollection
        @CollectionTable(name = "ADDRESS",
                         joinColumns = @JoinColumn(name = "MEMBER_ID"))
        private List<Address> addressHistory = new ArrayList<Address>();
        ...
    }
    ```

  - `Address`: 변화 없음!

- 설명

  - RDB는 컬럼 안에 컬렉션을 포함할 수 없기에, 별도의 테이블을 추가하고, `@CollectionTable`을 이용하여 테이블을 매핑
  - 테이블 매핑정보는 `@AttributeOverride`를 사용해서 재정의 가능
  - `@CollectionTable` 생략할 경우 기본값: `{엔티티 이름}_{컬렉션 속성 이름}`



### 9.5.1 값 타입 컬렉션 사용

- 예제 코드

  ```java
  //임베디드 값 타입
  member.setHomeAddress(new Address("통영", "몽돌해수욕장", "660-123"));
  
  //기본 값 타입 컬렉션
  member.getFavoriteFoods().add("짬뽕");
  
  //임베디드 값 타입 컬렉션
  member.getAddressHistory().add(new Address("서울", "강남", "123-123"));
  
  em.persist(member);
  ```

- 값 타입 컬렉션은 영속성 전이 + 고아 객체 제거 기능을 필수로 갖는다고 볼 수 있음

- 값 타입 컬렉션의 조인 시 페치 전략 기본값은 `LAZY`



### 9.5.2 값 타입 컬렉션의 제약사항

- 엔티티와 달리 값 타입은 추적이 불가능
  - 그냥 값 타입의 경우 엔티티에 소속되어 있기에 엔티티를 통해 찾고, 변경하면 됨
  - 값 타입 컬렉션의 경우 별도의 테이블에 보관되어 있음
    - 따라서 값 타입 컬렉션의 변화를 반영할 DB의 원본 데이터를 찾기가 어려움
    - 때문에 JPA 구현체들은 값 타입 컬렉션에 변경 사항이 발생시
      - 값 타입 컬렉션이 매핑된 테이블의 연관된 모든 데이터를 삭제하고, 현재 값 타입 컬렉션 객체에 있는 모든 값을 DB에 다시 저장
      - ex) `ID = 100`인 회원의 주소값 타입 컬렉션을 변경할 경우
        테이블에서 회원 100과 관련된 데이터를 모두 삭제하고 현재 값을 다시 저장
    - **실무에서는 값 타입 컬렉션이 매핑된 테이블에 데이터가 많을 경우 값 타입 컬렉션 대신에 일대다 관계를 고려하라**
- 값 타입 컬렉션을 매핑하는 테이블의 PK: 모든 컬럼을 묶어서 구성
  - PK 제약조건으로 인해 컬럼에 `null` 입력 불가
  - 같은 값을 중복해서 저장하는 것도 불가
- **그냥 일대다 관계에 영속성 전이 + 고아 객체 제거 사용하는게 낫다!**



# 10장 객체지향 쿼리 언어

- JPA는 복잡한 검색 조건을 사용하여 엔티티를 조회할 수 있는 다양한 쿼리 기술들을 지원
- Criteria, QueryDSL 등은 JPQL 기반이기에 JPQL에 대한 학습이 기본이 되어야 함



## 10.1 객체지향 쿼리 소개

- 필요성
  - JPA의 조회 방식
    - 식별자로 조회: `em.find()`
    - 객체 그래프 탐색
  - 복잡한 검색 방법 필요
    - 모든 엔티티를 메모리에 올려두고 복잡한 검색을 시행하는 것은 현실성이 없음
    - ORM을 사용하기에 테이블을 대상으로 하지 않고, 엔티티 객체를 대상으로 하는 검색의 필요성
- JPQL의 특징
  - 테이블이 아닌 객체를 대상으로 하는 객체지향 쿼리
  - SQL을 추상화하여 특정 DB SQL에 의존하지 않음
- JPA에서 사용할 수 있는 검색 방식
  - JPA가 공식 지원
    - **JPQL(Java Persistence Query Language)**
    - Criteria Query: JPQL을 편하게 작성하도록 도와주는 API, 빌더 클래스 모음
    - Native SQL: JPQL 대신 SQL 사용
  - JPA가 공식 지원하지는 않지만 유용한 기능
    - QueryDSL: Criteria처럼 JPQL을 쉽게 작성하게끔 돕는 빌더 클래스 모음, 비표준 오픈소스 프레임워크
    - JDBC 직접 사용, MyBatis 등 SQL 매퍼 프레임워크 사용: 필요할 경우 직접 사용 가능



### 10.1.1 JPQL 소개

- SQL과 비슷한 문법

- ANSI 표준 SQL이 지원하는 기능을 유사하게 지원

- SQL보다 간결

- 예제 코드

  ```java
  List<Member> resultList = 
      em.createQuery("SELECT m FROM Member m WHERE m.username = 'kim'", Member.class).getResultList();
  ```

  - `Member`: 테이블이 아니라 엔티티명
  - `m.username`: 테이블 컬럼명이 아니라 엔티티 객체의 필드명

- 과정

  - `em.createQuery` 메서드에 실행할 JPQL과 반환할 엔티티의 클래스 타입을 넘겨줌
  - JPA가 JPQL을 SQL로 변환, DB 조회



### 10.1.2 Criteria 쿼리 소개

- JPQL을 작성하는 빌더 클래스

- `String`이 아닌 코드로(여기서는 자바 코드) 작성한다

  - `String`으로 작성된 쿼리는 오타가 있어도 런타임 시점에 발견함
  - Criteria처럼 코드로 JPQL을 작성할 경우 컴파일 시점에 오류 발견

- `String`으로 작성한 JPQL에 비해 갖는 장점

  - 컴파일 시점에 오류 발견
  - IDE를 사용할 경우 코드 자동완성 지원
  - 동적 쿼리를 작성하기 편함

- 지원

  - 하이버네이트를 비롯한 몇몇 ORM 프레임워크의 경우 이미 자신만의 Criteria를 지원해왔으며
  - JPA의 경우 2.0부터 지원

- 사용 코드

  ```java
  //사용 준비
  CriteriaBuilder cb = em.getCriteriaBuilder();
  CriteriaQuery<Member> query = cb.createQuery(Member.class);
  
  //루트 클래스
  Root<Member> m = query.from(Member.class);
  
  //쿼리 생성
  CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
  List<Member> resultList = em.createQuery(cq).getResultList();
  ```

  - 위의 경우 필드 명을 문자(`"username"`)로 작성하였음
  - 저 부분도 문자가 아니라 코드로 작성하고 싶을 경우: 메타 모델(MetaModel) 사용

- 메타 모델 API

  - 자바의 애노테이션 프로세서(annotation processor): 애노테이션을 분석하여 클래스를 생성
  - JPA가 애노테이션 프로세서 기능을 사용하여 `Member`엔티티에서 Criteria 전용 클래스 `Member_`를 생성: 이를 메타모델이라 함
  - 메타 모델 사용시 `m.get(Member_username)` 형태로 사용 가능(문자가 아니라 코드로만 작성)

- **많은 장점을 갖지만 장점을 상쇄할 만큼 지나치게 복잡하고 장황함 -> 가독성, 사용성 모두 떨어짐**



### 10.1.3 QueryDSL 소개

- Criteria처럼 JPQL 빌더 역할을 수행하지만, 훨씬 단순하고 사용하기 좋음

- JPA 비표준 오픈소스 프로젝트

  - JPA, JDO, 몽고 DB, Java Collection, Lucene, Hibernate Search도 거의 동일한 문법으로 지원

- 사용 코드

  ```java
  //준비
  JPAQuery query = new JPAQuery(em);
  QMember member = QMember.member;
  
  //쿼리, 결과조회
  List<Member> members = query.from(member)
      .where(member.username.eq("kim"))
      .list(member);
  ```

- QueryDSL도 애노테이션 프로세서를 이용하여 쿼리 전용 클래스를 만들어야 함: `QMember`는 `Member` 엔티티를 기반으로 생성한 QueryDSL 쿼리 전용 클래스



### 10.1.4 네이티브 SQL 소개

- SQL을 직접 사용

- 벤더 의존적인 기능을 사용해야 할 경우

  - 오라클의 `CONNECT BY`
    - 특정 DB만 지원하는 SQL Hint(하이버네이트는 SQL Hint 지원)
  - 표준화되어있지 않아 JPQL에서 지원하지 않는 기능들
  - 이 외에 JPQL은 지원하지 않는 SQL 지원 기능들

- 특정 DB에 의존적인 SQL을 사용한다는 단점: DB 변경시 수정 필요

- 사용 코드

  ```java
  List<Member> resultList
      = em.createNativeQuery("SELECT ID, AGE, NAME FROM MEMBER WHERE NAME = 'kim'", Member.class)
      .getResultList();
  ```



### 10.1.5 JDBC 직접 사용, SQL 매퍼 프레임워크 사용

- JDBC 커넥션에 직접 접근하고 싶을 경우

  - JPA는 JDBC 커넥션을 획득하는 API를 지공하지 않음
  - JPA 구현체가 제공하는 방법을 사용해야 함

- 하이버네이트 사용 코드

  ```java
  Session session = em.unwrap(Session.class);
  session.dowork(new Work() {
      @Override
      public void execute(Connection connection) throws SQLException {
          //work...
      }
  });
  ```

- JDBC, MyBatis 등을 JPA와 함께 사용할 경우 영속성 컨텍스트를 적절한 시점에 강제로 플러시해줘야 한다.

  - 위와 같은 기술들은 JPA를 우회하여 DB에 접근하는데, 이런 SQL에 대해서는 JPA가 인식하지 못 한다.
  - 영속성 컨텍스트와 DB가 불일치 상태가 되어 데이터 무결성을 훼손할 수 있다.
  - **JPA를 우회하여 SQL을 실행하기 직전에 영속성 컨텍스트를 수동으로 플러시하여 DB와 동기화하자.**
  - 스프링 AOP를 적절히 활용하여, JPA 우회 메서드 호출 때마다 플러시를 호출하게 만든다면 위의 문제 깔끔하게 해결 가능



## 10.2 JPQL

### 10.2.1 기본 문법과 쿼리 API

- SELECT, UPDATE, DELETE 문 사용 가능

  - INSERT 문은 없음

- 기본 문법

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



#### SELECT 문

- 대소문자 구분
  - 엔티티와 속성은 대소문자를 구분함: `Member.username`
  - JPQL 키워드는 대소문자를 구분하지 않음: `SELECT`, `FROM`, `AS`
- 엔티티 이름
  - 클래스 명이 아니라 엔티티 명을 사용(`@Entity(name="XXX")`)
  - 엔티티명을 지정하지 않으면 클래스 명을 기본값으로 사용(추천)
- 별칭은 필수(`AS`는 생략 가능)
  - 하이버네이트의 경우
    - 하이버네이트만의 HQL(Hibernate Query Language)를 제공함
    - 이 경우 `m.username` 형태가 아니라 `username`으로 그냥 사용 가능
  - JPA 표준 명세에서는 별칭(alias) 말고 식별 변수(identification variable)이라고 부름



#### `TypeQuery`, `Query`

- 반환 타입을 명확하게 지정할 수 있을 경우 `TypedQuery` 사용

  ```java
  TypedQuery<Member> query = em.createQuery(JPQL, Member.class);
  ```

- 반환 타입을 명확하게 지정할 수 없을 경우`Query`사용

  ```java
  Query query = em.createQuery(JPQL);
  ```

  - 이 경우 쿼리 결과는 `Object`(조회 대상이 하나일 경우), `Object[]`(조회 대상이 여럿일 경우)를 반환한다.



#### 결과 조회

- `query.getResultList()`: 결과를 컬렉션으로 반환, 결과가 없을 경우 빈 컬렉션을 반환
- `query.getSingleResult()`: 결과가 정확히 하나일 경우 사용
  - 결과가 없을 경우 `NoResultException` 반환
  - 결과가 1개 초과한 경우 `NonUniqueResultException` 반환
  - 위의 예외들은 `javax.persistence`의 예외(JPA 예외), 스프링 사용시 스프링 예외로 번역됨
  - **결과가 정확히 하나가 아닐 경우 예외 발생함에 유의**



### 10.2.2 파라미터 바인딩

- 위치 기준 파라미터 바인딩만 지원하는 JDBC와 달리, 이름 기준 파라미터 바인딩도 지원

- 이름 기준 파라미터(Named Parameters)

  - 파라미터를 이름으로 구분

  - 파라미터 앞에 :를 사용

    ```java
    String JPQL = "... WHERE m.username = :username";
    ...
    query.setParameter("username", usernameParam);
    ```

  - JPQL API는 메서드 체인방식으로 설계되어 있기에 다음이 가능

    ```java
    em.createQuery(JPQL, Member.class)
        .setParameter("username", usernameParam)
        .gerResultList();
    ```

- 위치 기준 파라미터

  ```java
  em.createQuery("... WHERE m.username = ?1", Member.class)
      .setParameter(1, usernameParam)
      .getResultList();
  ```

  - `?1`형태로 ? 다음에 위치 값을 주면 됨
  - 위치 값은 1부터 시작

- 이름 기준 파라미터 바인딩이 더 명확하다.

- **파라미터 바인딩을 사용해야 하는 이유** 

  - 사용하지 않는 경우

    ```java
    "SELECT m FROM Member m WHERE m.username '" + usernameParam + "''"
    ```

  - 위와 같은 경우 SQL 인젝션 공격을 받을 수 있음
  - 성능상으로도 부적합
    - JPA는 파라미터 바인딩 사용할 경우, 파라미터 값이 다르더라도 동일 SQL로 인식
      이 경우 JPQL을 SQL로 파싱한 결과를 재사용 가능
    - DB 또한 내부에서 실행한 SQL을 파싱해서 사용하는데, 같은 쿼리의 경우 파싱 결과 재사용 가능
    - 애플리케이션, DB 모두 파싱 결과 재사용이 가능하기에 성능 향상



### 10.2.3 프로젝션

- 프로젝션(projection)

  - SELECT 절에 조회할 대상을 지정하는 것
  - `SELECT {프로젝션 대상} FROM`으로 대상을 선택

- 프로젝션 대상

  - 엔티티

    ```sql
    SELECT m FROM Member m
    SELECT m.team FROM Member m
    ```

    - 원하는 객체를 바로 조회
    - 컬럼을 일일히 나열하여 조회하는 SQL과는 다름
    - **조회된 엔티티는 영속성 컨텍스트에서 관리됨**

  - 임베디드 타입

    - 엔티티와 유사하게 사용되지만, **조회의 시작점이 될 수 없음**

      ```java
      String query = "SELECT a FROM Address a";
      ```

      - 위와 같은 방식은 불가

      ```java
      String query = "SELECT o.address FROM Order o"
      List<Address> addresses = em.createQuery(query, Address.class).getResultList();
      ```

      - 위와 같이 엔티티를 시작점으로 삼아서 임베디드 타입을 조회할 수 있음

    - 임베디드 타입은 값 타입이기에 영속성 컨텍스트에서 관리되지 않음

  - 스칼라 타입

    - 숫자, 문자, 날짜와 같은 기본 데이터 타입들

      ```java
      List<String> usernames = em.createQuery("SELECT username FROM Member m", String.class)
          .getResultList();
      ```

    - 중복 데이터를 제거하려면 `DISTINCT` 사용: `SELECT DISTINCT username FROM Member m`

    - 통계 쿼리는 주로 스칼라 타입으로 조회함

      ```java
      Double orderAmountAvg = em.createQuery("SELECT AVG(o.oderAmount) FROM Order o", Double.class)
          .GetSingleResult();
      ```

  - 여러 값 조회

    - 프로젝션에 여러 값을 선택할 경우 `TypedQuery` 사용 불가, `Query` 사용해야 함

    - 스칼라 타입의 여러 값 조회 예

      ```java
      Query query = em.createQuery("SELECT m.username, m.age FROM Member m");
      List<Object[]> resultList = query.getResultList();
      
      for (Object[] row, : resultList) {
          String username = (String) row[0];
          Integer age = (Integer) row[1];
      }
      ```

    - 엔티티 타입의 여러 값 조회 예

      ```java
      List<Object[]> resultList =
          	em.createQuery("SELECT o.member, o.product, o.orderAmount FROM Order o")
          	.getResultList();
      
      for (Object[] row : resultList) {
          Member member = (Member) row[0]	//엔티티
      }
      ```

  - NEW 명령어

    - 실무에서는 위와 같이 `Object[]` 사용하지 않고 DTO로 변환하여 사용함

      ```java
      TypedQuery<UserDto> query = em.createQuery("SELECT new jpabook.jpql.UserDto(m.username, m.age) FROM Member m", UserDto.class);
      List<UserDto> resultList = query.getResultList();
      ```

    - 주의사항

      - 패키지 명을 포함한 전체 클래스명을 입력해야 함
      - 순서와 타입이 일치하는 생성자가 필요



### 10.2.4 페이징 API

- 페이징 API용 SQL은 지루하고 반복적이며, DB마다 다른 문법을 사용한다는 문제점이 있음

- JPA는 페이징을 다음 두 API로 추상화하여 위의 문제를 해결함

  - `setFirstResult(int startPosition)`: 조회 시작 위치(0부터 시작)
  - `setMaxResults(int maxResult)`: 조회할 데이터 수

- 예제 코드

  ```java
  em.createQuery(JPQL, Member.class);
      .setFirstResult(10)
      .setMaxResults(20)
  	.getResultList();
  ```

- 페이징 SQL을 더 최적화하고 싶을 경우 네이티브 SQL을 직접 사용해야 함



### 10.2.5 집합과 정렬

#### 집합 함수

| 함수     | 설명                                                         |
| -------- | ------------------------------------------------------------ |
| COUNT    | 결과 수를 구함. 반환 타입: Long                              |
| MAX, MIN | 최대, 최소 값을 구함. 문자, 숫자, 날짜 등에 사용             |
| AVG      | 평균값을 구함. 숫자 타입만 사용 가능. 반환타입: Double       |
| SUM      | 합을 구함. 숫자 타입만 사용 가능. <br />반환 타입: 정수합 Long, 소수합 Double, BigInteger 합 BigInteger, BigDecimal 합: BigDecimal |

- 사용 방식

  ```SQL
  SELECT
  	COUNT(m),
  	SUM(m.age),
  	AVG(m.age),
  	MAX(m.age)),
  	MIN(m.age)
  FROM Member m
  ```

- 사용 시 참고사항

  - `NULL`값은 무시하므로 통계에 잡히지 않음(DISTINCT가 정의되어 있어도 무시됨)

  - 값이 없는데 SUM, AVG, MAX, MIN 사용하면 `NULL` 값이 됨, 단 COUNT의 경우 0이 됨

  - DISTINCT를 집합 함수 안에 사용하여 중복 값을 제거하고 나서 집합을 구할 수 있음

    ```SQL
    SELECT COUNT( DISTINCT m.age ) FROM Member m
    ```

  - DISTINCT를 COUNT에서 사용할 때 임베디드 타입은 지원하지 않음



#### GROUP BY, HAVING

- GROUP BY: 통계 데이터를 구할 때 특정 그룹끼리 묶어줌

- HAVING: GROUP BY와 함께 사용, GROUP BY로 그룹화한 통계 데이터를 기준으로 필터링

- 문법과 예시

  - 문법

    ```SQL
    groupby_절 ::= GROUP BY {단일값 경로 | 별칭} +
    having_절 ::= HAVING 조건식
    ```

- 통계 쿼리, 리포팅 쿼리라 부름

- 애플리케이션으로 여러 줄에 걸쳐 처리해야 하는 코드를 짧게 처리 가능

- 보통 전체 데이터를 기준으로 처리하기에 실시간으로 사용하기에는 부담이 많음

  - 결과가 많을 경우 통계 결과만 저장하는 테이블을 별도로 만들어 두고
    사용자가 적은 시간에 통계 쿼리를 실행하여 결과를 보관하는 것이 좋음



#### 정렬(ORDER BY)

- 문법

  ```SQL
  orderby_절 ::= ORDER BY {상태필드 경로 | 결과 변수 [ASC | DESC]}
  ```



### 10.2.6 JPQL 조인

- SQL조인과 기능은 같지만 문법은 약간 다름



#### 내부 조인

- INNER JOIN 사용, INNER는 생략 가능

- 연관 필드를 사용하여 조인한다는 차이점

  - JPQL의 조인

    ```SQL
    SELECT m
    FROM Member m INNER JOIN m.team t
    WHERE t.name = :teamName
    ```

    - 연관 필드: `m.team`

  - SQL식 조인: 이런 식으로 JPQL을 사용하면 문법 오류 발생

    ```SQL
    FROM Member m JOIN Team t
    ```



#### 외부 조인

- 문법

  ```SQL
  SELECT m
  FROM Member m LEFT [OUTER] JOIN m.team t
  ```

  - OUTER는 생략 가능



#### 컬렉션 조인

- 일대다 관계나 다대다 관계처럼 컬렉션을 사용하는 곳에 조인

- 예시 코드

  ```SQL
  SELECT t, m FROM Team t LEFT JOIN t.members m
  ```

  - 컬렉션 값 연관 필드로 외부 조인함: `t LEFT JOIN t.members m`
  - 컬렉션 조인 시 JOIN 대신에 IN 사용 가능(컬렉션일 때만 사용가능): 과거 방식이기에 사용하지 말자



#### 세타 조인

- WHERE절 이용해서 가능

- 세타 조인은 내부 조인만 지원

- 전혀 관계없는 엔티티도 조인 가능

- 예시 코드

  ```SQL
  SELECT COUNT(m) FROM Member m, Team t
  WHERE m.username = t.name
  ```



#### JOIN ON절(JPA 2.1)

- 조인할 때 ON 절 지원(Since JPA 2.1)

  - 조인 대상을 필터링하고 조인
  - 내부 조인의 ON 절 사용은 WHERE 절 사용과 결과가 동일하기에
    ON 절은 보통 외부 조인에서만 사용

- 예시 코드

  ```SQL
  SELECT m, t FROM Member m
  LEFT JOIN m.team t ON t.name = 'A'
  ```



### 10.2.7 페치 조인

- SQL의 조인 종류는 아님

- JPQL에서 성능 최적화를 위해 지원하는 기능

  - 연관된 엔티티나 컬렉션을 한 번에 조회
  - JOIN FETCH 명령어로 사용

- 문법

  ```SQL
  페치 조인 ::= [LEFT [OUTER] | INNER ] JOIN FETCH 조인경로
  ```



#### 엔티티 페치 조인

- 예시 코드

  ```SQL
  SELECT m
  FROM Member m JOIN FETCH m.team
  ```

  - 연관된 엔티티를 함께 조회
  - 일반적인 JPQL 조인과 달리 별칭을 사용할 수 없음(하이버네이트의 경우 별칭 허용)

- 객체 그래프를 유지하면서 조회

  - 이 경우 지연 로딩을 설정했더라도 프록시가 아닌 실제 엔티티를 가지고 옴
  - 때문에 ROOT 엔티티가 준영속 상태가 되더라도 연관된 엔티티 조회 가능



#### 컬렉션 페치 조인

- 예시 코드

  ```SQL
  SELECT t
  FROM Team t JOIN FETCH t.members
  WHERE t.name = '팀A'
  ```

  - 일대다 조인의 경우 결과 값이 증가할 수 있음



#### 페치 조인과 DISTINCT

- JPQL의 DISTINCT는 SQL에 DISTINCT 추가 + 애플리케이션에서 한 번 더 중복을 제거한다.
- 일대다 컬렉션 페치 조인과 같은 경우 값이 증가할 수 있는데, 이런 경우에도 중복을 제거해줌



#### 페치 조인과 일반 조인의 차이

- **JPQL은 결과를 반환할 때 연관관계를 고려하지 않고, SELECT 절에 지정된 엔티티만 조회함**
  - 때문에 지연 로딩 설정했을 경우 프록시나 초기화되지 않은 컬렉션 래퍼를 반환
  - 즉시 로딩으로 설정했을 경우 즉시 로딩을 위해 쿼리를 한 번 더 실행
- 페치 조인의 경우 연관된 엔티티도 한 번에 조회



#### 페치 조인의 특징과 한계

- SQL 호출 회수를 줄여 성능 최적화 가능
- 글로벌 로딩 전략(`FetchType.LAZY`)보다 우선하여 적용됨
- **글로벌 로딩 전략은 지연로딩으로, 최적화가 필요할 경우 페치 조인을 사용하는 것이 효과적**
- 페치 조인 사용시 지연 로딩이 발생하지 않기에 준영속 상태로도 객체 그래프 탐색이 가능
- 한계
  - 페치 조인 대상에는 별칭을 줄 수 없음(JPA 표준 기준)
    - SELECT, WHERE절, 서브 쿼리에 페치 조인 대상을 사용할 수 없음
  - 단 하이버네이트 등 몇몇 JPA 구현체들은 별칭 지원
    - 별칭을 사용할 경우 연관된 데이터 수가 달라지기에 데이터 무결성이 깨질 수 있음에 주의
    - 2차캐시와 함께 사용할 때, 연관된 데이터 수가 달라진 상태에서 2차 캐시에 저장되면 
      다른 곳에서 조회할 때도 연관된 데이터 수가 달라지는 문제가 발생할 수 있음
  - 둘 이상의 컬렉션을 페치할 수 없음(구현체에 따라 달라짐)
    - 컬렉션 * 컬렉션의 Cartesian 곱이 만들어지기에 결과값이 매우 커짐
    - 하이버네이트의 경우 `MultipleBagFetchException` 발생
  - 컬렉션을 페치 조인하면 페이징 API 사용 불가
    - 컬렉션(일대다)가 아닌 단일 값 연관 필드(일대일, 다대일)들은 페치 조인을 사용해도 페이징 API 사용가능
    - 하이버네이트에서 컬렉션을 페치 조인하고 페이징 API 사용시 경고 발생, 메모리에서 페이징 처리
      - 이 경우 성능 이슈, 메모리 초과 문제 발생 가능하기에 위험
- 정리
  - 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
  - 여러 테이블을 조인해서 엔티티 형태가 아닌 다른 결과를 내야 할 경우 
    여러 테이블에서 필요한 필드만 조회하여 DTO로 반환하는 것이 더 효과적일 수 있음



### 10.2.8 경로 표현식

- 경로 표현식(Path Expression)
  - `m.username`, `m.team` 등 `.` 이용하여 객체 그래프 탐색



#### 용어 정리

- 상태 필드(state field): 단순히 값을 저장하기 위한 필드
- 연관 필드(association field): 연관관계를 위한 필드, 임베디드 타입 포함
  - 단일 값 연관 필드: `@ManyToOne`, `@OneToOne`, 대상이 엔티티
  - 컬렉션 값 연관 필드: `@OneToMany`, `@ManyToMany`, 대상이 컬렉션



#### 경로 표현식과 특징

- 경로에 따른 특징
  - 상태 필드 경로: 경로 탐색의 끝, 더 탐색 불가
  - 단일 값 연관경로: 묵시적으로 내부 조인 발생. 계속 탐색 가능
  - 컬렉션 값 연관 경로: 묵시적으로 내부 조인 발생, 더 탐색 불가, 
    단 FROM 절에서 조인을 통해 별칭을 얻을 경우 별칭으로 탐색 가능
- 묵시적 조인
  - **묵시적 조인은 모두 내부 조인**
  - **외부 조인은 모두 명시적으로 JOIN 키워드 사용해야 함**
- 컬렉션의 경우 `size` 기능을 통해 컬렉션의 크기를 구할 수 있음: 
  `SELECT t.members.size FROM Team t`(COUNT 함수를 사용하는 SQL로 변환됨)



#### 경로 표현식을 사용한 묵시적 조인 시 주의사항

- 항상 내부 조인임
- 컬렉션은 경로 탐색의 끝, 별칭을 얻어서 추가 탐색 가능
- 경로 탐색은 주로 SELECT, WHERE절에서 사용하지만 묵시적 조인으로 인해 SQL의 FROM 절에 영향을 줌
- 묵시적 조인은 조인이 발생하는 상황을 파악하기 힘들기에 성능 분석을 방해할 수 있음: 
  **단순한 경우가 아니면, 성능이 중요하다면 명시적 조인을 사용하자**



### 10.2.9 서브 쿼리

- 서브 쿼리를 지원하지만 제약사항이 있음

  - WHERE, HAVING 절에서만 사용 가능
  - SELECT, FROM 절에서는 사용 불가
    - 하이버네이트의 경우 SELECT 절의 서브 쿼리 허용, FROM 절은 지원 안 함
    - 일부 JPA 구현체의 경우 FROM 절의 서브쿼리도 지원

- 예시 코드

  ```SQL
  SELECT m FROM Member m
  WHERE (SELECT COUNT(o) FROM Order o WHERE m = o.member) > 0
  ```



#### 서브 쿼리 함수

- 서브쿼리는 다음 함수들과 같이 사용 가능
  - `[NOT] EXISTS (subquery)`
  - `{ALL | ANY | SOME} (subquery)`
    - ANY와 SOME은 같은 의미
  - `[NOT] IN (subquery)`
    - 서브 쿼리의 결과 중 하나라도 같은 것이 있으면 참
    - IN은 서브쿼리가 아닌 곳에서도 사용



### 10.2.10 조건식

#### 타입 표현

| 종류        | 설명                                                         | 예제                                                         |
| ----------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 문자        | 작은 따옴표 사이에 표현<br />작은 따옴표를 표현하고 싶다면<br />작은 따옴표 연속 두 개('') 사용 | `HELLO`<br />`She''s`                                        |
| 숫자        | L(Long 타입 지정)<br />D(Double 타입 지정)<br />F(Float 타입 지정) | 10L<br />10D<br />10F                                        |
| 날짜        | DATE {d 'yyyy-mm-dd'}<br />TIME {t 'hh-mm-ss'}<br />DATETIME {ts 'yyyy-mm-dd hh:mm:ss.f'} | {d '2012-03-24'}<br />{t '10-11-11'}<br />{ts '2012-03-24 10-11-11.123'}<br />m.createDate = {d '2012-03-24'} |
| Boolean     | TRUE, FALSE                                                  |                                                              |
| Enum        | 패키지명을 포함한 전체 이름을 사용해야 함                    | jpabook.MemberType.Admin                                     |
| 엔티티 타입 | 엔티티의 타입을 표현, 주로 상속과 관련하여 사용              | TYPE(m) = Member                                             |



#### 연산자 우선 순위

1. 경로 탐색 연산(.)
2. 수학 연산: +, -(단항 연산자), *, /, +, -
3. 비교 연산: =, >, >=, <, <=, <>(다름), [NOT] BETWEEN, [NOT] LIKE, [NOT] IN, 
   IS [NOT] NULL, IS [NOT] EMPTY, [NOT] MEMBER [OF], [NOT] EXISTS
4. 논리 연산: NOT, AND, OR



#### 논리 연산과 비교식

- 논리 연산
  - AND: 둘 다 만족하면 참
  - OR: 둘 중 하나만 만족해도 참
  - NOT: 조건식의 결과 반대
- 비교식: =, >, >=, <, <=, <>



#### Between, IN, Like, NULL 비교

- Between 식: `X [NOT] BETWEEN A AND B`
- IN 식: `X [NOT] IN`
- Like 식: `[NOT] LIKE 패턴값 [ESCAPE 이스케이프 문자]`
  - %: 아무 값들이 입력되어도 됨, 값이 없어도 됨
  - _: 한 글자는 아무 값이 입력되어도 되지만 값이 있어야 함
- NULL 비교식: `{단일값 경로 | 입력 파라미터} IS [NOT] NULL`
  - NULL은 `=`로 비교하면 안 되고 꼭 IS NULL을 사용하여 비교해야 함



#### 컬렉션 식

- **컬렉션에서만 사용 가능한 기능, 컬렉션은 컬렉션 식만 사용 가능**

- 빈 컬렉션 비교 식: `IS [NOT] EMPTY`

- 컬렉션의 멤버 식: `{엔티티나 값} [NOT] MEMBER [OF] {컬렉션 값 연관 경로}`

  - 예제 코드

    ```SQL
    SELECT t FROM Team t
    WHERE :memberParam MEMBER OF t.members
    ```



#### 스칼라 식

- 스칼라: 숫자, 문자, 날짜, case, 엔티티 타입(엔티티의 타입 정보) 등 가장 기본적인 타입

- 수학 식

  - 단항 연산자: +, -
  - 사칙연산: +, -, *, /

- 문자 함수

  | 함수                                                         | 설명                                                         | 예제                            |
  | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------- |
  | CONCAT(문자1, 문자2, ...)                                    | 문자를 합함                                                  | CONCAT('A', 'B') = AB           |
  | SUBSTRING(문자, 위치, [길이])                                | 위치부터 시작해 길이만큼 문자를 구함<br />길이 값이 없으면 나머지 전체 길이를 뜻함 | SUBSTRING('ABCDEF', 2, 3) = BCD |
  | TRIM([[LEADING \| <br />TRAILING] \| BOTH] [트림 문자]<br />FROM] 문자) | LEADING: 왼쪽만<br />TRAILING: 오른쪽만<br />BOTH: 양쪽 다 트림 문자를 제거(기본값)<br />트림 문자의 기본값은 공백(SPACE) | TRIM('ABC ') = 'ABC'            |
  | LOWER(문자)                                                  | 소문자로 변경                                                | LOWER('ABC') = 'abc'            |
  | UPPER(문자)                                                  | 대문자로 변경                                                | UPPER('abc') = 'ABC'            |
  | LENGTH(문자)                                                 | 문자 길이                                                    | LENGTH('ABC') = 3               |
  | LOCATE(찾을 문자, 원본 문자, <br />[검색 시작 위치])         | 검색 위치부터 문자를 검색<br />1부터 시작, 못 찾으면 0 반환  | LOCATE('DE', 'ABCDEFG') = 4     |

  - HQL의 경우 CONCAT 대신 || 사용 가능

- 수학 함수

  | 함수                        | 설명                                                         | 예제                          |
  | --------------------------- | ------------------------------------------------------------ | ----------------------------- |
  | ABS(수학식)                 | 절대값을 구함                                                | ABS(-10) = 10                 |
  | SQRT(수학식)                | 제곱근을 구함                                                | SQRT(4) = 2.0                 |
  | MOD(수학식, 나눌 수)        | 나머지를 구함                                                | MOD(4, 3) = 1                 |
  | SIZE(컬렉션 값 연관 경로식) | 컬렉션의 크기를 구함                                         | SIZE(t.members)               |
  | INDEX(별칭)                 | LIST 타입 컬렉션의 위치값을 구함<br />단 컬렉션이 @OrderColumn을 사용하는 LIST 타입일때만 사용 가능 | t.members m WHERE INDEX(m) >3 |

- 날짜 함수

  - DB의 현재 시간 조회

    - `CURRENT_DATE`: 현재 날짜
    - `CURRENT_TIME`: 현재 시간
    - `CURRENT_TIMESTAMP`: 현재 날짜 시간

  - 예시

    ```sql
    SELECT CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP FROM Team t
    ```

  - 예시: 종료 이벤트 조회

    ```sql
    SELECT e FROM Event e WHERE e.endDate < CURRENT_DATE
    ```

  - 하이버네이트: 년, 월, 일, 시간, 분, 초 값 구하기 기능

    ```
    YEAR, MONTH, DAY, HOUR, MINUTE, SECOND
    ```

    ```SQL
    SELECT YEAR(CURRENT_TIMESTAMP) FROM Member;
    ```

  - 각 날짜 함수는 DB 방언에 등록되어 있음



#### CASE 식

- 특정 조건에 따라 분기

- 종류

  - 기본 CASE

    - 문법

      ```sql
      CASE
      	{WHEN <조건식> THEN <스칼라식>} +
      	ELSE <스칼라식>
      END
      ```

    - 예시

      ```SQL
      SELECT
      	CASE WHEN m.age <= 10 THEN '학생요금'
      		 WHEN m.age >= 60 THEN '경로요금'
      		 ELSE '일반요금'
      	END
      FROM Member m
      ```

  - 심플 CASE

    - 조건식을 사용할 수는 없지만 문법이 단순

    - 문법

      ```sql
      CASE <조건대상>
      	(WHEN <스칼라식1> THEN <스칼라식2>) +
      	ELSE <스칼라식>
      END
      ```

    - 예시

      ```SQL
      SELECT
      CASE t.name
      		WHEN '팀A' THEN '인센티브110%'
      		WHEN '팀B' THEN '인센티브120%'
      		ELSE '인센티브105%'
      	END
      FROM Team t
      ```

  - COALESCE

    - 스칼라식을 차례로 조회하여 `null`이 아니면 반환

    - 문법

      ```sql
      COALESCE (<스칼라식> {, <스칼라식>} +)
      ```

    - 예시: `m.username`이 `null`이면 '이름 없는 회원'을 반환

      ```sql
      SELECT COALESCE(m.username, '이름 없는 회원') FROM Member m
      ```

  - NULLIF

    - 두 값이 같으면 `null`을 반환하고 다르면 첫 번째 값을 반환한다.

    - 집합 함수는 `null`을 포함하지 않기에 일반적으로 집합 함수와 함께 사용

    - 문법

      ```sql
      NULLIF(<스칼라식>, <스칼라식>)
      ```

    - 예시: `m.username`dl '관리자'일 경우 `null`을 반환, 나머지 경우는 본인의 이름을 반환

      ```SQL
      SELECT NULLIF(m.usernmae, '관리자') FROM Member m
      ```



### 10.2.11 다형성 쿼리

- JPQL로 부모 엔티티를 조회하면 그 자식 엔티티도 함께 조회한다

- TYPE

  - 조회 대상을 특정 자식 타입으로 한정할 때 주로 사용

  - 예시

    ```SQL
    SELECT i FROM Item i
    WHERE TYPE(i) IN (Book, Movie)
    ```

- TREAT(JPA 2.1)

  - 자바의 타입 캐스팅과 유사

  - 부모 타입을 특정 자식 타입으로 다룰 때 사용

  - JPA 표준: FROM, WHERE 절에서 사용 가능

    - 하이버네이트의 경우 SELECT 절에서도 사용 가능

  - 예시

    ```SQL
    SELECT i FROM Item i WHERE TREAT(i AS Book).author = 'kim'
    ```



### 10.2.12 사용자 정의 함수 호출(JPA 2.1)

- 문법

  ```SQL
  function_invocation::= FUNCTION(function_name {, function_arg}*)
  ```

- 예시

  ```SQL
  SELECT FUNCTION('group_concat', i.name) FROM Item i
  ```

- 하이버네이트 구현체 사용 시

  - 방언 클래스를 상속해서 구현하고, 사용할 DB 함수를 미리 등록해두어야 함

    - 방언 상속 설정 코드

      ```java
      public class MyH2Dialect extends H2Dialect {
          public MyH2Dialect() {
              registerFunction( "group_concat", new StandardSQLFunction
                              ("group_concat, StandardBasicTypes.STRING"));
          }
      }
      ```

    - 방언 등록 설정(`persistence.xml`)

      ```xml
      <property name="hibernate.dialect" value="hello.MyH2Dialect"/>
      ```

  - 축약 사용 가능

    ```SQL
    SELECT group_concat(i.name) FROM Item i
    ```

    

### 10.2.13 기타 정리

- `enum`은 `=` 비교 연산만 지원

- 임베디드 타입은 비교를 지원하지 않음

- **EMPTY STRING**

  - JPA표준은 `''`을 길이 0인 Empty String으로 정했음
  - DB에 따라 `''`을 `NULL`로 사용하기도 하기에 유의해야

- NULL 정의

  - 조건을 만족하는 데이터가 하나도 없으면 `NULL`

  - `NULL`은 알 수 없는 값(unknown value)이다. `NULL`과 모든 수학적 계산 결과는 `NULL`이 된다.

  - `Null == Null`은 알 수 없는 값이다.

  - `Null is Null`은 참이다.

  - **진리표에서의 NULL**(JPA 표준 명세)

    - AND

      | AND  | T    | F    | U    |
      | ---- | ---- | ---- | ---- |
      | T    | T    | F    | U    |
      | F    | F    | F    | F    |
      | U    | U    | F    | U    |

    - OR

      | OR   | T    | F    | U    |
      | ---- | ---- | ---- | ---- |
      | T    | T    | T    | T    |
      | F    | T    | F    | U    |
      | U    | T    | U    | U    |

    - NOT: NOT(U)는 U



### 10.2.14 엔티티 직접 사용

- PK값

  - JPQL에서 엔티티 객체를 직접 사용할 경우, SQL에서는 PK값을 사용

  - 따라서 다음 두 JPQL은 동일한 SQL로 변환된다:

    ```SQL
    SELECT COUNT(m.id) FROM Member m
    SELECT COUNT(m) FROM Member m
    ```

  - 엔티티를 직접 파라미터로 받는 경우도 마찬가지다.

  - FK값의 경우도 마찬가지다.



### 10.2.15 Named 쿼리: 정적 쿼리

- JPQL 쿼리는 정적 쿼리와 동적 쿼리로 나눌 수 있음
  - 동적 쿼리
    - `em.createQuery(...)`처럼 JPQL을 문자로 직접 완성하여 넘기는 방식
    - 특정 조건에 따라 런타임에 JPQL을 동적으로 구성 가능
  - 정적 쿼리
    - 미리 정의한 쿼리에 이름을 부여하여 필요할 때 사용: Named 쿼리
    - 한 번 정의하면 변경할 수 없음
    - 장점
      - 애플리케이션 로딩 시점에 JPQL 문법을 체크, 파싱: 오류를 빠르게 확인 가능
      - 사용 시점에서 파싱된 결과를 재사용: 성능상의 이점
      - 정적 SQL을 생성하기에 DB의 조회 성능 최적화에도 이점
    - `@NamedQuery` 애노테이션을 이용(자바 코드) OR xml 문서에 작성 가능



#### 애노테이션에 정의

- 명명시 `Member.findByUsername`  형태로 엔티티 이름을 앞에 붙이면
  - 관리하기 편함
  - Named 쿼리 자체가 영속성 유닛 단위로 관리되는 것이기에, 충돌 방지 가능
  - `lockMode`: 쿼리 실행 시 락을 검
  - `hints`: JPA 구현체에게 주는 힌트



#### XML에 정의

- 애노테이션 정의 방식보다 편리
  - 자바로 멀티라인 문자를 다루기 까탈스럽기 때문
  - `&`, `<`, `>`은 XML 예약문자이기 때문에 `&amp`, `&lt;`, `&gt;` 사용하면 됨
    - `<![CDATA[]]>` 사용하는 방식으로 표현도 가능
  - 
    - `META_INF/orm.xml`이 기본 매핑 경로(이 경우 별도의 설정 불필요)
    - 위와 다른 경로에 작성 시 `persistence.xml`에 `<mapping-file>` 설정해주자.



## 10.3 Criteria

- JPQL을 자바 코드로 작성하게 도와주는 빌더 클래스 API
- 문자가 아닌 코드로 (특히 동적) JPQL을 작성 -> 컴파일 시점에 문법 오류를 확인
- 실제 사용하기에는 복잡하고 장황하다는 점이 장점을 덮을 정도로 큼





### Specifications

- DDD에서 유래
- predicate 연산



#### Predicate

- 0차논리: 명제논리

  - P, Q

  - 0항:zero-place predicates

  - 0-arity(함수의 argument or operand)
    $$
    P \rightarrow Q
    $$

- 1차논리: 양화논리

  - P(a) P가 predicate, R(a,b)의 R(individual constant)

  $$
  \exists x(Px\wedge Qx)
  $$

- 2차논리

  - predicate가 predicate에 적용될 수 있음

- 술어(튜플)

  - **원자식** 또는 **원자** 는 단순히 용어 의 튜플에 적용된 술어입니다. *즉, 원자식은 P* a 술어와 *t* *n* *항에 대해 P* ( *t* 1 ,…, *t* *n* ) 형식의 공식입니다 .

- 예시로 살펴보기

  - 미성년자, 노인, 주말 기준으로 할인 적용
    $$
    \forall x \forall y (((Ux \vee Sx) \wedge \neg Wy) \rightarrow Dx)
    $$

  - Predicate인 $Ux, Sx, Wy$의 결합으로 비즈니스 로직을 표현

- 전통적인 방식으로 접근 시 도메인 안에 해당 조건들을 명시

  - `User` 안에 `Ux`, `Sx`에 해당하는 `isUnderage()`나 `isSenior()`등의 메서드를 만드는 식
  - 하지만 비즈니스 로직은 복잡하고, 필연적으로 여러 도메인의 로직들을 함께 사용하게됨

- Specification은 이런 문제를 해결하기 위함

  - 코드

    - `Specification` 인터페이스

      ```java
      public interface Specification<T> {
          boolean isSatisfiedBy(T t);
      }
      ```

    - `isUnderageSpecification`

      ```java
      public class IsUnderageSpecification extends CompositeSpecification<Discount> {
          private static final Integer MAJORITY_AGE = 18;
      
          @Override
          public boolean isSatisfiedBy(Discount person) {
              return discount.getPerson().getAge() < MAJORITY_AGE;
          }
      }
      ```

    - `CompositeSpecification`

      ```java
      public abstract class CompositeSpecification<T> implements Specification<T> {
          public CompositeSpecification<T> or(Specification<T> specification) {
              return new OrSpecification<>(this, specification);
          }
      
          public CompositeSpecification<T> and(Specification<T> specification) {
              return new AndSpecification<>(this, specification);
          }
      
          public CompositeSpecification<T> not() {
              return new NotSpecification<T>(this);
          }
      }
      ```

    - `AndSpecification`

      ```java
      public class AndSpecification<T> extends CompositeSpecification<T> {
      
          private final Specification<T> left;
          private final Specification<T> right;
      
          public AndSpecification(Specification<T> pLeft, Specification<T> pRight) {
              this.left = pLeft;
              this.right = pRight;
          }
      
          public boolean isSatisfiedBy(T t) {
              return left.isSatisfiedBy(t) && right.isSatisfiedBy(t);
          }
      }
      ```

    - `DiscountSpecification`

      ```java
      public class DiscountSpecification extends CompositeSpecification<Person> {
      
          CompositeSpecification<Person> isUnderage
       = new IsUnderageSpecification();
          CompositeSpecification<Person> isSenior
       = new IsSeniorSpecification();
          CompositeSpecification<Person> isWeekend 
      = new IsWeekendSpecification();
      
          @Override
          public boolean isSatisfiedBy(Person person) {
              return isUnderage
                      .and(isWeekend)
                      .or(isSenior)
                      .isSatisfiedBy();
          }
      }
      ```

      - 위의 Specification 들을 결합하여 조건을 간단하게 만들 수 있음

  - 복잡한 조건문을 이해하기 쉬운 각각의 비즈니스 로직으로 쪼갠다!

  - 테스트에 용이

  - 재사용하기 좋음

  - 도메인 로직이 여러 클래스로 분리된다는 단점




### Criteria vs QueryDSL

- Fluent API
  - 메서드 체이닝을 생각하자
- 결국 QueryDSL도 predicate 사용한다.



- 개발자가 올린 비교

- ```java
  CriteriaQuery query = builder.createQuery();
  Root<Person> men = query.from( Person.class );
  Root<Person> women = query.from( Person.class );
  Predicate menRestriction = builder.and(
      builder.equal( men.get( Person_.gender ), Gender.MALE ),
      builder.equal( men.get( Person_.relationshipStatus ),
  RelationshipStatus.SINGLE ));
  Predicate womenRestriction = builder.and(
      builder.equal( women.get( Person_.gender ), Gender.FEMALE ),
      builder.equal( women.get( Person_.relationshipStatus ),
  RelationshipStatus.SINGLE ));
  query.where( builder.and( menRestriction, womenRestriction ) );
  ```

  - 크리테리아
    - 루트
    - Predicate
    - query에 where 적용

  ```java
  JPAQuery query = new JPAQuery(em);
  QPerson men = new QPerson("men");
  QPerson women = new QPerson("women");
  query.from(men, women).where(
        men.gender.eq(Gender.MALE),
        men.relationshipStatus.eq(RelationshipStatus.SINGLE),
        women.gender.eq(Gender.FEMALE),
        women.relationshipStatus.eq(RelationshipStatus.SINGLE));    
  ```

  - Q.. 만들기
  - 바로 쿼리 만들면서
    - 플루언트



### 10.3.1 Criteria 기초

- Criteria API: `javax.persistence.criteria`

- 예시 코드

  ```java
          //JPQL: SELECT m FROM Member m
  
          CriteriaBuilder cb = em.getCriteriaBuilder();  //Criteria 쿼리 빌더
  
          //Criteria 생성, 반환 타입 지정
          CriteriaQuery<Member> cq = cb.createQuery(Member.class);
  
          Root<Member> m = cq.from(Member.class); //FROM 절
          cq.select(m);   //SELECT 절
  
          TypedQuery<Member> query = em.createQuery(cq);
          List<Member> members = query.getResultList();
  ```

- 검색 조건과 정렬

  ```java
  //JPQL
  //SELECT m FROM Member m
  //WHERE m.username='회원1'
  //ORDER BY m.age DESC
  
  CriteriaBuilder cb = em.getCriteriaBuilder();
  
  CriteriaQuery<Member> cq = cb.createQuery(Member.class);
  Root<Member> m = cq.from(Member.class);	//FROM 절 생성
  
  //검색 조건 정의
  Predicate usernameEqual = cb.equal(m.get("username"), "회원1");
  
  //정렬 조건 정의
  Order ageDesc = cb.desc(m.get("age"));
  
  //쿼리 생성
  cq.select(m)
      .where(usernameEqual)	//WHERE 절 생성
      .orderBy(ageDesc);		//ORDER BY 절 생성
  
  List<Member> resultList = em.createQuery(cq).getResultList();
  ```


- 쿼리 루트(Query Root)


  - `Root<Member> m cq.from(Member.class)`: m이 쿼리 루트

  - 조회의 시작점

  - Criteria에서만 사용되는 특수 별칭, 엔티티에만 부여 가능

- 숫자 타입 검색

  ```java
  ...
  //타입 정보 필요
  Predicate ageGt = cb.greaterThan(m.<Integer>get("age"), 10);
  
  cq.select(m);
  cq.where(ageGt);
  cq.orderBy(cb.desc(m.get("age")));
  ```

  - `m.get("age")`로는 타입 정보 추론 불가하기에, 제네릭으로 표기(일반적으로 `String` 등 문자 타입은 지정 불필요)
  - `greaterThan()` 대신 `gt()` 가능



### 10.3.2 Criteria 쿼리 생성

- 반환 타입

  - 지정

    ```java
    CriteriaQuery<Member> cq = cb.createQuery(Member.class);
    ```

  - `Object`

    ```java
    CriteriaQuery<Object> cq = cb.createQuery();
    ```

    - 반환 타입을 지정할 수 없는 경우
    - 반환 타입이 둘 이상인 경우(이 경우 `Object[].class` 이용하여 배열로 반환받는 것이 나음)

  - 튜플

    ```java
    CriteriaQuery<Tuple> cq = cb.creatTupleQuery();
    
    Root<Member> m = cq.from(Member.class);
    
    cq.multiselect(
        m.get("username").alias("username"),
        m.get("age").alias("age")
    );
    
    List<Tuple> resultList = em.createQuery(cq).getResultList();
    
    for(Tuple tuple : resultList) {
        String username = tuple.get("username", String.class);
        Integer age = tuple.get("age", Integer.class);
    }
    ```

    - `Map`과 유사하게 별칭을 이용하여 둘 이상의 반환 타입을 편리하게 조회할 수 있음
    - `cq.multiselect`: 여러 대상을 조회
    - `alias()`: 별칭 지정, 별칭은 필수



### 10.3.3 조회

- DISTINCT: `cq.distinct(true)`

- JPQL의 NEW, `construct()`: 생성자 구문

  ```java
  cq.select(cb.construct(MemberDto.class, m.get("username"), m.get("age")));
  ```

  - 패키지명까지 다 적어야 하는 JPQL과 달리 편리하게 사용 가능(코드 기반이기 때문)



### 10.3.4 집합

- GROUP BY

  ```java
  cq.groupBy(m.get("team").get("name"));
  ```

  - JPQL로는 `group by m.team.name`

- HAVING

  ```java
  cq.groupBy(m.get("team").get("name"))
      .having(cb.gt(minAge, 10));
  ```



### 10.3.5 정렬

- `cb.desc()`, `cb.asc()`



### 10.3.6 조인

```java
Root<Member> m = cq.from(Member.class);
Join<Member, Team> t = m.join("team", JoinType.INNER);

cq.multiSelect(m, t)
    .where(cb.equal(t.get("name"), "팀A"));
```

- 쿼리 루트에서 `join()`메서드 사용하여 조인, 별칭(`t`) 부여
- `JoinType`: `INNER`, `LEFT`, `RIGHT` 지원

- 페치 조인: `m.fetch("team", JoinType.LEFT)`



### 10.3.7 서브 쿼리

- 메인 쿼리와 서브 쿼리가 무관한 경우

  ```java
  Subquery<Member> mainQuery = cb.createQuery(Member.class);
  Subquery<Double> subQuery = mainQuery.subquery(Double.class);
  
  Root<Member> m2 = subQuery.from(Member.class);
  subQuery.select(cb.avg(m2.<Integer>get("age")));
  
  //메인 쿼리
  Root<Member> m = mainQuery.from(Member.class);
  mainQuery.select(m)
      .where(cb.ge(m.<Integer>ger("age"), subQuery));
  ```

- 메인 쿼리와 서브 쿼리가 유관한 경우

  ```java
  Subquery<Member> mainQuery = cb.createQuery(Member.class);
  Root<Member> m = mainQuery.from(Member.class);
  
  Subquery<Team> subQuery = mainQuery.subquery(Team.class);
  Root<Member> subM = subQuery.correlate(m);	//메인 쿼리의 별칭을 가져옴)
  ```

  - `correlate()`: 메인 쿼리의 별칭을 서브 쿼리에서 사용 가능



### 10.3.8 IN 식

```java
cq.select(m)
    .where(cb.in(m.get("username"))
          .value("회원명1")
          .value("회원명2"));
```



### 10.3.9 CASE 식

- `selectCase()`, `when()`, `otherwise()`

  ```java
  cq.multiselect(
      m.get("Username"),
      cb.selectCase()
      	.when(cb.ge(m<Integer>get("age"), 60), 600)
      	.when(cb.le(m<Integer>get("age"), 15), 500)
     		.otherwise (1000)
  );
  ```



### 10.3.10 파라미터 정의

```java
cq.select(m)
    .where(cb.equal(m.get("username"), cb.parameter(String.class, "usernameParam")));

List<Member> resultList = em.createQuery(cq)
    .setParameter("usernameParam", "회원1")
    .getResultList();
```

- 하이버네이트의 경우 파라미터 정의 없이 바로 값을 입력해도, 실제 SQL에서 PreparedStatement에 파라미터 바인딩 사용함



### 10.3.11 네이티브 함수 호출

```java
Expression<Long> function = cb.function("SUM", Long.class,m.get("age"));
cq.select(function);
```

- 하이버네이트 구현체의 경우 방언에 사용자정의 SQL 함수를 등록해야 호출 가능



### 10.3.12 동적 쿼리

- `String` 기반의 JPQL보다 안정적

- 예제 코드

  ```java
  //검색 조건
  Integer age = 10;
  String username = null;
  String teamName = "팀A";
  
  //쿼리 생성
  CriteriaBuilder cb = em.getCriteriaBuilder();
  CriteriaQuery<Member> cq = cb.createQuery(Member.class);
  
  Root<Member> m = cq.from(Member.class);
  Join<Membe, Team> t = m.join("team");
  
  List<Predicate> criteria = new ArrayList<Predicate>();
  
  if (age != null) criteria.add(cb.equal(m.<Integer>get("age"),
                                        cb.parameter(Integer.class, "age")));
  if (username != null) criteria.add(cb.equal(m.get("username"),
                                        cb.parameter(String.class, "username")));
  if (teamName != null) criteria.add(cb.equal(t.get("name"),
                                        cb.parameter(String.class, "teamName")));
  
  cb.where(cb.and(criteria.toArray(new Predicate[0])));
  TypedQuery<Member> query = em.createQuery(cq);
  if (age != null) query.setParameter("age", age);
  if (username != null) query.setParameter("username", username);
  if (teamName != null) query.setParameter("teamName", teamName);
  
  List<Member> resultList = query.gerResultList();
  ```

  

### 10.3.14 Criteria 메타 모델 API

- 완전한 코드 기반으로 만들기 위함

  - `m.get("age")`의 `"age"`는 문자열

- 사용

  - 예시 코드

    ```java
    cq.select(m)
        .where(cb.gt(m.get(Member_.age), 20))
        .orderBy(cb.desc(m.get(Member_.age)));
    ```

  - 메타 모델 클래스: `Member_`

    ```java
    @Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
    @StaticMetamodel(Member.class)
    public abstract class Member_ {
        public static volatile SinglularAttribute<Member.Long> id;
        public static volatile SinglularAttribute<Member.String> username;
        ...
    }
    ```

    - 표준(CANONICAL) 메타 모델 클래스

    - 자동 생성기가 엔티티 클래스를 기반으로 생성해줌

      

