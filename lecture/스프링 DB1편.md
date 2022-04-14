# 스프링 DB 1편 - 데이터 접근 핵심 원리

# JDBC

## JDBC 이해

### JDBC 등장 이유

- 애플리케이션을 개발할 때 중요한 데이터는 대부분 DB에 저장
- 애플리케이션 서버의 일반적인 사용법
  1. DB와 커넥션 연결
  2. 애플리케이션이 DB로 SQL 전달
  3. DB에서 응답



#### 과거의 경우

- 과거 DB는 아래와 같은 것들이 각각 상이했다.
  - 커넥션 연결
  - SQL 전달
  - 결과 응답 받는 방법
- 문제점
  - DB를 교체할 때마다 서버에 개발된 DB 사용 코드도 함께 변경해야 함
  - 각각의 DB마다 상이한 방식을 학습해야 함



### JDBC 표준 인터페이스

- **JDBC; Java DataBase Connectivity**
  - 위와 같은 문제를 해결하기 위해 등장
  - **DB 기능에 대한 표준 인터페이스**
    - `java.sql.Connection`: 연결
    - `java.sql.Statement`: SQL을 담은 내용
    - `java.sql.ResultSet`: 요청에 대한 응답
  - **JDBC 드라이버**
    - JDBC 인터페이스를 각각의 DB 벤더(회사)에서 각 DB에 알맞게 구현한 라이브러리
    - MySQL JDBC 드라이버, Oracle JDBC 드라이버 , ...
- JDBC의 문제 해결
  - DB 변경 시에 애플리케이션 서버의 DB 코드를 변경할 필요가 없음
  - 개발자는 각 DB의 연결, SQL 전달, 응답 방식을 학습할 필요가 없이, JDBC 인터페이스만 학습해도 된다.
- 한계
  - DB 사이의 자료형, SQL 등이 여전히 상이함.
    - ANSI SQL 표준은 일부만 적용됨
    - ex) 페이징 SQL은 DB마다 상이
    - **DB를 변경할 때 JDBC 코드는 변경할 필요가 없으나, SQL은 DB에 맞춰 변경해야 함!**
  - JPA를 도입할 경우 위의 문제를 일부 해결할 수 있음



## JDBC와 최신 데이터 접근 기술

- JDBC는

  - 오래됨(1997 출시)
  - 사용법이 복잡

- 현재는 JDBC를 직접 사용하지 않고, 편리하게 사용하는 기술들이 제공됨

  - SQL Mapper
  - ORM

- **SQL Mapper**

  - 구조

    ```mermaid
    graph LR
    A[애플리케이션 로직] -->|SQL 전달| B[SQL Mapper<br>-Jdbc Template<br>-MyBatis]-->|SQL 전달| C[JDBC]
    ```

  - 장점

    - SQL 응답 결과를 객체로 변환
    - JDBC의 반복 코드를 제거

  - 단점: 개발자가 SQL을 직접 작성해야 한다.

  - 대표 기술 스프링 Jdbc Template, MyBatis

- **ORM 기술**

  - 구조

    ```mermaid
    graph LR
    A[애플리케이션 로직] -->|객체 전달| B[JPA]
    C[JDBC] --> |구현| B
    C[JPA 구현체<br>-하이버네이트<br>-이클립스 링크] -->|SQL 전달| D[JDBC]
    ```

  - 객체를 RDB와 매핑해주는 기술

  - 반복적 SQL 작성 작업 사라짐

  - DB마다 상이한 SQL을 사용하는 문제 해결

  - 대표 기술: JPA, 하이버네이트, 이클립스 링크

- **최신 DB 접근 기술들도 결국 JDBC를 사용한다.**

  - **따라서 JDBC를 직접 사용하지 않더라도, JDBC의 동작 원리에 대해서 알아야 한다.**
  - **JDBC는 자바 개발자의 필수 기술이다.**



## 데이터베이스 연결

- 커넥션 상수 정의

  ```java
      public static final String URL = "jdbc:h2:tcp://localhost/~/test";
      public static final String USERNAME = "sa";
      public static final String PASSWORD = "";
  ```

- 커넥션 받아오기

  ```java
  @Slf4j
  public class DBConnectionUtil {
  
      public static Connection getConnection() {
          try {
              var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
              log.info("get Connection={}, class={}", connection, connection.getClass());
              return connection;
          } catch (SQLException e) {
              throw new IllegalStateException(e);
          }
      }
  }
  ```

- `Connection`은 인터페이스임
  - `getConnection()`으로 리턴한 `Connection` 구현체는 `org.h2.jdbc.JdbcConnection`이다. (H2 드라이버를 사용하여 테스트하였기 때문)
  - 인터페이스를 사용하기에 DB가 변경되어도 JDBC 코드를 바꾸지 않아도 되는 유연성을 제공



### JDBC DriverManager

- 라이브러리에 등록된 DB 드라이버들을 관리, `Connection`을 획득하는 기능 제공
- 과정
  - `DriverManager.getConnection()`으로 `Connection` 요청
  - `DriverManager`는 라이브러리에 등록된 드라이버 목록을 자동 인식
    - `URL`, `이름`, `비밀번호` 등 정보를 바탕으로
    - 개별 드라이버들이 커넥션을 획득할 수 있는가를 확인
  - 지원되는 드라이버는 관련된 `Connection` 구현체를 클라이언트에 반환



###  JDBC - Create

```java
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("DB error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }
```

- `con.prepareStatement(sql);`: DB에 전달할 SQL과 파라미터로 전달할 데이터들을 준비
  - `Statement`와 달리 `PreparedStatement`는 파라미터 바인딩이 추가됨
    - `PreparedStatement`는 `Statement` 하위 타입
    - **SQL Injection 공격을 예방하기 위해 `PreparedStatement` 사용해야 한다.**
  - 파라미터 바인딩 기능:`setString()`, `setInt()` 등, 전달할 순서와 함께 전달
- `pstmt.executeUpdate()`: `Statement`를 통해 준비된 SQL을 커넥션을 통해 실제 DB로 전달
  - 반환하는 `int`값은 영향 받은 DB row 수임

```java
    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error" , e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error" , e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error" , e);
            }
        }
    }
}
```

- **리소스 정리**
  - 닫아야 하는 리소스의 수가 여럿인데,
    앞 자원을 닫을 때 예외가 터진다면 뒤 자원을 닫지 못한다.
  - 때문에 위와 같이 일일히 설정해 줘야 한다.
  - 반환 순서는 획득 순서의 역순이다.
  - 리소스가 제대로 정리되지 않는다면
     **커넥션이 끊어지지 않고 계속 유지되는 리소스 누수가 발생한다. 이로 인해 커넥션 부족으로 장애가 발생할 수 있다.**



### JDBC - Read

```java
    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt =null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                var member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        } catch (SQLException e) {
            log.error("DB error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }
```

- 데이터를 조회할 때는 `executeUpdate()`가 아니라 **`executeQuery()`**

- `executeQuery()`는 `ResultSet`을 반환

  

#### ResultSet

- 다음과 같은 구조로 이해하자.

  | 위치 | member_id | money |
  | ---- | --------- | ----- |
  | 1    | member1   | 10000 |
  | 2    | member2   | 20000 |

- 커서가 위의 표의 **`위치`**를 지정한다고 생각하자.

  - 최초의 커서는 데이터를 가지고 있지 않기에 `rs.next()`를 최소 1회는 호출해야 데이터를 조회할 수 있다.
  - `rs.next()`를 호출할 때마다 커서는 다음 위치로 이동한다.
  - `rs.next()`의 반환값은 커서의 이동으로 도달한 위치의 데이터 유무를 나타낸다.
  - `rs.getInt()`: 현재 커서가 가리키고 있는 위치를 `int`타입으로 변환한다.

- 단건 데이터를 조회할 경우에는 `if`문을 사용해도 충분하지만, 여러 건의 데이터를 조회할 때는 `while`문을 사용하자.



### JDBC - Update, Delete

- 등록과 유사하게 `executeUpdate()` 사용한다.



# Connection Pool과 DataSource의 이해

## Connection Pool의 이해

- DB 커넥션 획득 과정
  - 고객의 요청
  - 애플리케이션 로직 실행
  - DB 드라이버에 커넥션 조회
  - DB와 TCP/IP 커넥션 연결(3 way handshake 등)
  - 커넥션이 연결되면, ID, PW 등 정보를 DB에 전달
  - DB에서 ID, PW 등으로 내부 인증 완료, DB 세션 형성
  - DB가 커넥션 생성 완료 응답 전송
  - DB 드라이버가 커넥션 객체를 생성, 반환
- 커넥션 획득 과정은 복잡한 과정을 거치며, 시간을 많이 소요한다.
  - DB와 애플리케이션 서버 모두 커넥션 생성을 위한 리소스를 매 호출마다 사용해야 한다.
  - 고객이 애플리케이션을 사용할 때 **SQL 실행 시간 + 커넥션 생성 시간이 소모**
    -> **응답 속도에 영향, 부정적인 UX!**



### 커넥션 풀

- 커넥션을 미리 생성해두고 관리, 위와 같은 문제를 해결함
- 애플리케이션 시작 시점에 커넥션 풀이 커넥션을 미리 확보하여 보관
  - 보관 기본값은 일반적으로 10개
- 커넥션 풀의 커넥션은 이미 DB와 TCP/IP 연결되어 있기에 SQL을 즉각적으로 DB에 전달 가능
- 애플리케이션 로직은 DB 드라이버를 통해 커넥션을 획득하지 않고, **커넥션 풀에 이미 생성된 커넥션을 객체 참조로 사용한다.**
  - 커넥션을 다 사용한 후, **커넥션을 종료하지 않고 커넥션 풀에 반환한다.**
- **정리**
  - 커넥션 풀의 적절한 커넥션 수는 상황에 따라 다르기에 성능 테스트를 통해 정해야 한다.
  - 커넥션 풀은 최대 커넥션 수를 제한할 수 있기에 **DB에 무한정 연결이 생성되는 것을 막아, DB를 보호하기도 한다.**
  - **실무에서는 커넥션 풀을 항상 기본적으로 사용한다.**
  - 커넥션 풀은 단순하기에 직접 구현할 수도 있지만 유용한 오픈소스를 사용하는 것이 낫다.
    - 대표적인 오픈소스: `commons-dbcp2`, `tomcat-jdbc pool`, **`hikariCP`**
    - **`hirakiCp`**
      - 성능, 사용의 편리함, 안정성 모두에 있어 가장 나음
      - 스프링 부트 2.0부터 기본 커넥션 풀로 사용(스프링 부트에서 기본 제공)



## DataSource 이해

- 커넥션을 얻는 방법은 다양하다.
  - 커넥션 풀 획득에 `DBCP2 커넥션 풀`을 사용하다가 `HikariCP 커넥션 풀`을 사용하는 식으로 바꾸려면?
  - 커넥션을 획득하는 애플리케이션 코드를 변경해야 한다.



### DataSource

- 위와 같은 상황에서 유연성을 갖기 위해서 **커넥션 획득 방법을 추상하는 인터페이스**
- `Connection getConnection()` 메서드가 핵심 기능이다.
- **정리**
  - 대부분의 커넥션 풀은 `DataSource` 인터페이스를 구현
  - 따라서 특정 구현체가 아니라 `DataSource`를 사용하게끔 애플리케이션 로직을 작성하면 됨
  - **`DriverManager`는 `DataSource` 인터페이스를 사용하지 않음!**
    - `DriverManager`에서 `DataSource` 기반 커넥션 풀로 변경 시 코드를 고쳐야 하는 문제 발생
    - 이 문제를 해결하기 위해 **스프링이 `DriverManagerDataSource` 인터페이스를 제공**



### 커넥션을 DriverManager로 얻어오는 방식과 DataSource로  얻어오는 방식의  차이점

```java
Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);		    //드라이버 매니저
```

```java
DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);	//데이터 소스
Connection con1 = dataSource.getConnection();
Connection con2 = dataSource.getConnection();
```

- 차이
  - `DriverManager`의 경우 커넥션을 획득할 때마다 파라미터를 계속 전달해야 하지만,
  - `DataSource`의 경우 처음 객체 생성 시에만 파라미터를 넘기고, 그 이후 커넥션을 획득하기 위해서는 `getConnection()`을 호출하기만 하면 됨
- **설정과 사용의 분리**
  - 설정: `URL`, `USERNAME`, `PASSWORD` 입력
  - 사용: 설정과 무관하게 `getConnection()`
  - **유연성에 있어서 큰 차이를 만든다.**



### ConnectionPool

```java
        //커넥션 풀링: HikariProxyConnection(Proxy) -> JdbcConnection(Target)
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(15);
        dataSource.setPoolName("MyConnectionPool");

        useDataSource(dataSource);
        Thread.sleep(1000);
```

- 커넥션 풀 설정: 위와 같은 방식으로 설정해준다.

- 왜 `Thread.sleep()` 사용했는가?

  ```
  16:55:41.827 [MyConnectionPool connection adder] DEBUG com.zaxxer.hikari.pool.HikariPool - MyConnectionPool - Added connection conn2: url=jdbc:h2:tcp://localhost/~/test user=SA
  16:55:41.834 [MyConnectionPool connection adder] DEBUG com.zaxxer.hikari.pool.HikariPool - MyConnectionPool - Added connection conn3: url=jdbc:h2:tcp://localhost/~/test user=SA
  16:55:41.843 [MyConnectionPool connection adder] DEBUG com.zaxxer.hikari.pool.HikariPool - MyConnectionPool - Added connection conn4: url=jdbc:h2:tcp://localhost/~/test user=SA
  ...
  ```

  - **`connection adder` 쓰레드**가 커넥션을 추가하는 데 텀을 줘야 하기 때문이다.
  - 커넥션 풀이 커넥션을 채우는 일은 시간이 오래 걸리기 때문에(외부 접속: TCP/IP) 별도의 쓰레드를 사용하였다.
    - 별도의 쓰레드에서 커넥션 풀을 채우기 때문에 애플리케이션 실행 시간에 영향을 주지 않는다.
  - 만약 커넥션이 생성되기 전에 / 커넥션이 남아 있지 않을 때 커넥션을 요청한다면?
    - **대기한다. (block)**
    - **대기 시간이 일정 이상을 넘어설 경우 예외를 던진다. (구체적인 시간은 설정에 따라 다르다.)**

- 커넥션 획득

  ```
  con1=HikariProxyConnection@917831210 wrapping conn0: url=jdbc:h2:tcp://localhost/~/test user=SA, 
  class=class com.zaxxer.hikari.pool.HikariProxyConnection
  
  con2=HikariProxyConnection@1464555023 wrapping conn1: url=jdbc:h2:tcp://localhost/~/test user=SA, 
  class=class com.zaxxer.hikari.pool.HikariProxyConnection
  ...
  MyConnectionPool - After adding stats (total=15, active=2, idle=13, waiting=0)
  ```

  - 위와 같은 커넥션 이름, 커넥션 클래스를 확인할 수 있다.
  - 두 개의 커넥션을 풀에서 획득했기에 활성 2개임을 확인할 수 있다. (커넥션을 사용 후에 꼭 반환하자!)



### 기존 CRUD를 DataSource 이용하게 변경하기

```java
/**
 * JDBC - DataSource 사용, JdbcUtils 사용
 */
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV1 {

    private final DataSource dataSource;		//의존관계 주입받음
    
    ...
    
    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={} class={}", con, con.getClass());
        return con;
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {

        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }
```

- 외부에서 `DataSource`를 주입받음
  - 자바 표준 인터페이스이기에 변경할 필요 없음
- **`JdbcUtils` 편의 메서드**
  - 커넥션을 좀 더 편하게 닫을 수 있음
- `closeConnection()`: 커넥션 풀을 사용할 경우 커넥션을 닫는 것이 아니라 반환한다. 

