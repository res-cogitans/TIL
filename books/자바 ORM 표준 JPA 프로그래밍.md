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

