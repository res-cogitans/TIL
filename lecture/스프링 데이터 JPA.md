# 스프링 데이터 JPA

- 편리하지만, JPA의 너무 많은 부분을 추상화하기에 깊이 있는 내부 동작방식을 이해하기가 어렵다.
- 내부 동작방식에 대한 이해가 없다면 장애 발생시 해결하지 못 한다.



## 프로젝트 환경설정

- `application.yml`

  ```yaml
  spring:
    datasource:
      url: jdbc:h2:tcp://localhost/~/datajpa
      username: sa
      password:
        driver-class-name: org.h2.Driver
      
    jpa:
      hibernate:
        ddl-auto: create
      properties:
        hibernate:
  #        show_sql: true
          format_sql: true
  logging.level:
    org.hibernate.SQL: debug
  #  org.hibernate.type: trace
  ```

  - `  jpa:hibernate: properties: hibernate: show_sql`
    - `logging.level: org.hibernate.SQL: debug`을 이용, 로그로 보면 되기 때문에 사용 안 함
  - `#  org.hibernate.type: trace`: 파라미터까지 보여줌

- 간단한 JPA Repository 만들고 테스트해보기 (Spring Data JPA 아님!)

  - `MemberJpaRepository`

    ```java
    @Repository
    public class MemberJpaRepository {
    
        @PersistenceContext
        private EntityManager em;
    
        public Member save(Member member) {
            em.persist(member);
            return member;
        }
    
        public Member findById(Long id) {
            return em.find(Member.class, id);
        }
    }
    ```

    - 테스트 코드

      ```java
      @SpringBootTest
      @Transactional
      @Rollback(false)
      class MemberJpaRepositoryTest {
      
          @Autowired
          MemberJpaRepository memberRepository;
      
          @Test
          public void testMember() {
              Member member = new Member("memberA");
              Member savedMember = memberRepository.save(member);
      
              Member foundMember = memberRepository.findById(savedMember.getId());
      
              assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
          }
      }
      ```

      - 테스트 클래스에 `@RunWith(SpringRunner.class)`: **JUnit5 + SpringBoot 이후로는 사용할 필요가 없다!**

      - JPA에서는 기본 생성자 `protected`로 만들어 놔도 됨

  - `MemberDataJpaRepository`

    ```java
    public interface MemberDataJpaRepository extends JpaRepository<Member, Long> {
    }
    ```

    - 테스트 코드: 상동

    - 로그를 확인하면 그냥 JPA로 구현한 것과 같게 동작하는 것을 확인할 수 있음

- 학습용 기본 도메인 코드

  - `Member`

    ```java
    @Entity
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString(of = {"id", "username", "age"})
    public class Member {
    
        public Member(String username) {
            this.username = username;
        }
    
        public Member(String username, int age, Team team) {
            this.username = username;
            this.age = age;
            if (team != null) {
                changeTeam(team);
            }
        }
    
        @Id
        @GeneratedValue
        @Column(name= "member_id")
        private Long id;
        private String username;
        private int age;
    
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "team_id")
        private Team team;
    
        public void changeTeam(Team team) {
            if (this.team != null) {
                this.team.getMembers().remove(this);
            }
            this.team = team;
            team.getMembers().add(this);
        }
    }
    ```

  - `Team`

    ```java
    @Entity
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString(of = {"id", "name"})
    public class Team {
    
        @Id
        @GeneratedValue
        @Column(name = "team_id")
        private Long id;
        private String name;
    
        @OneToMany(mappedBy = "team")
        private List<Member> members = new ArrayList<>();
    
        public Team(String name) {
            this.name = name;
        }
    }
    ```



## 공통 인터페이스 기능

### 순수 JPA 기반 리포지터리

- `MemberJpaRepository`

  ```java
  @Repository
  public class MemberJpaRepository {
  
      @PersistenceContext
      private EntityManager em;
  
      public Member save(Member member) {
          em.persist(member);
          return member;
      }
  
      public Optional<Member> findById(Long id) {
          return Optional.ofNullable(em.find(Member.class, id));
      }
  
      public List<Member> findAll() { //JPQL 사용
          return em.createQuery("SELECT m FROM Member m", Member.class)
                  .getResultList();
      }
  
      public void delete(Member member) {
          em.remove(member);
      }
  
      public long count() {
          return em.createQuery("SELECT COUNT(m) FROM Member m", Long.class)
                  .getSingleResult();
      }
  }
  ```

- `TeamJpaRepository`

  ```java
  @Repository
  public class TeamJpaRepository {
  
      @PersistenceContext
      private EntityManager em;
  
      public Team save(Team team) {
          em.persist(team);
          return team;
      }
  
      public Optional<Team> findById(Long id) {
          return Optional.ofNullable(em.find(Team.class, id));
      }
  
      public List<Team> findAll() { //JPQL 사용
          return em.createQuery("SELECT t FROM Team t", Team.class)
                  .getResultList();
      }
  
      public void delete(Team team) {
          em.remove(team);
      }
  
      public long count() {
          return em.createQuery("SELECT COUNT(t) FROM Team t", Long.class)
                  .getSingleResult();
      }
  }
  ```

- 순수 JPA 사용해서 `Repository` 만들 때 코드가 상당히 반복적인 것을 볼 수 있다.

- 수정(update)이 없는 것은 JPA의 DirtyChecking 기능을 사용하면 되기 때문이다.



### 공통 인터페이스 설정

- 원래는 메인 애플리케이션에 다음을 붙여 줘야 했었음

  ````java
  @EnableJpaRepositories(basePackages = "com.example.springdatajpa.repository")
  ````

  - 스프링 부트 사용한다면 세팅이 불필요

- **`extends JpaRepository`한 DataJpa의 Repository들은 어떻게 구현체 없는 인터페이스인데도 동작할 수 있을까?**

  - 클래스명을 찍어보면: `Class com.sun.proxy.$Proxy107`와 같은 형태로 나오는 것을 볼 수 있음
  - **스프링 데이터 JPA가 구현체를 생성, Injection 해준다.**

- `@Repository` 생략 가능하다.

  - `@Repository`는 컴포넌트 스캔을 위한 등록 기능뿐만 아니라,
    JPA 예외를 스프링 예외로 번역해주는 기능도 갖는다.



### 공통 인터페이스 분석

- `JpaRepository`

  ```java
  package org.springframework.data.jpa.repository;
  
  ...
  @NoRepositoryBean
  public interface JpaRepository<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
  
  ```

  - `package org.springframework.data`

    - JPA 말고도 다른 기술 사용하더라도(redis, mongoDB 등...) 사용 가능한 기능
    - `PagingAndSortingRepository`를 보면, 이것도 `data` 경로에 있음을 알 수 있다 -> JPA에만 한정되지 않았음!

  - `PagingAndSortingRepository`

    ```java
    public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {
    ```

    - `CrudRepository`를 `extends`한다

  - `Repository`

    ```java
    public interface Repository<T, ID> {
    ```

    - `CrudRepository`가 `extends`하는 것
    - 최상위 `Marker Interface` (규정된 메서드들 없음)
    - 스프링 빈을 만들 때 클래스 스캔을 하기 쉽게 만들어주는 기능 가짐

  - `spring-data-commons`: 공통 부분에 대한 프로젝트

  - `spring-data-jpa`: 특화된 부분에 대한 프로젝트

- 구조

  - **스프링 데이터 관련**

  ```mermaid
  classDiagram
  class Repository
  <<Interface>> Repository
  Repository <|-- CrudRepository
  
  class CrudRepository
  <<Interface>> CrudRepository
  CrudRepository : save(S) S
  CrudRepository : findOne(ID) T
  CrudRepository : exists(ID) boolean
  CrudRepository : count() long
  CrudRepository : delete(T)
  CrudRepository <|-- PagingAndSortingRepository
  
  PagingAndSortingRepository
  <<Interface>> PagingAndSortingRepository
  PagingAndSortingRepository : findAll(Sort) Iterable~T~
  PagingAndSortingRepository : findAll(Pageable) Page~T~
  ```

  - **스프링 데이터 JPA 관련**

    ```mermaid
    classDiagram
    class PagingAndSortingRepository
    <<Interface>> PagingAndSortingRepository
    PagingAndSortingRepository <|-- JpaRepository
    JpaRepository : findAll() List~T~
    JpaRepository : findAll(Sort) List~T~
    JpaRepository : findAll(Iterable~ID~) List~T~
    JpaRepository : save(Iterable~S~) List~S~
    JpaRepository : flush()
    JpaRepository : saveAndFlush(T) T
    JpaRepository : deleteInBatch(Iterable~T~)
    JpaRepository : deleteAllInBatch()
    JpaRepository : getOne(ID) T
    ```

    - `JpaRepository`: JPA에 특화된 기능 제공

- **주의 사항**

  - 예전에는 `T findOne(ID)`였으나, 현재는 `Optional<T> findById(ID)`로 변경됨

- **제네릭 표기**

  - `T`: 엔티티
  - `ID`: 엔티티의 식별자 타입
  - `S`: 엔티티와 그 자식 타입

- **주요 메서드 설명**

  - `save(S)`: 새로운 엔티티는 저장, **이미 있는 엔티티는 병합**
  - `delete(T)`: em.remove()` 호출
  - `findById(ID)`: `em.find()` 호출
  - `getOne(ID)`: `em.getReference`호출: **프록시로 조회!**
  - `findAll(...)`: 정렬(`Sort`)나 페이징(`Pageable`) 조건을 파라미터로 넣을 수 있음

- 공통 제공 기능외의 특화 기능을 개발하고 싶다면?

  - 커스텀 기능을 사용할 수 있음
  - 또 `findByUsername(String username)` 정도의 기능은 인터페이스에 등록한 것 만으로 자동 구현된다.
    --> **쿼리 메서드 기능**



## 쿼리 메서드 기능

- 쿼리 메서드 기능
  - 메서드 이름으로 쿼리 생성
  - 메서드 이름으로 JPA의 Named Query 호출
  - `@Query` 애노테이션 사용하여 리포지터리 인터페이스에 쿼리 직접 정의



### 메서드 이름으로 쿼리 생성

- 메서드 이름을 분석하여 JPQL 쿼리를 실행함

- 순수 JPA 이용하여 이름, 나이 기반으로 `Member` 찾는 코드

  ```java
      public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
          return em.createQuery("SELECT m FROM Member m WHERE m.username = :username" +
                  " AND m.age > :age")
                  .setParameter("username", username)
                  .setParameter("age", age)
                  .getResultList();
      }
  ```

- `MemberDataJpaRepository`의 동일한 기능을 수행하는 코드

  ```java
      List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
  ```

  - 테스트 코드를 돌려보면, 위와 동일한 기능을 작동하는 것을 볼 수 있다.
  - 메서드명 기반이기에 이름을 정확히 작성해줘야 함

  

 #### 명명 방식

- **Query subject keywords**: 다음 공식 문서를 참조: [링크](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-keywords)

  | Keyword                                                      | Description                                                  |
  | :----------------------------------------------------------- | :----------------------------------------------------------- |
  | `find…By`, `read…By`, `get…By`, `query…By`, `search…By`, `stream…By` | General query method returning typically the repository type, a `Collection` or `Streamable` subtype or a result wrapper such as `Page`, `GeoResults` or any other store-specific result wrapper. Can be used as `findBy…`, `findMyDomainTypeBy…` or in combination with additional keywords. |
  | `exists…By`                                                  | Exists projection, returning typically a `boolean` result.   |
  | `count…By`                                                   | Count projection returning a numeric result.                 |
  | `delete…By`, `remove…By`                                     | Delete query method returning either no result (`void`) or the delete count. |
  | `…First<number>…`, `…Top<number>…`                           | Limit the query results to the first `<number>` of results. This keyword can occur in any place of the subject between `find` (and the other keywords) and `by`. |
  | `…Distinct…`                                                 | Use a distinct query to return only unique results. Consult the store-specific documentation whether that feature is supported. This keyword can occur in any place of the subject between `find` (and the other keywords) and `by`. |

- **Supported keywords inside method names**: 다음 공식 문서를 참조: [링크](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)

  | Keyword                | Sample                                                       | JPQL snippet                                                 |
  | :--------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
  | `Distinct`             | `findDistinctByLastnameAndFirstname`                         | `select distinct … where x.lastname = ?1 and x.firstname = ?2` |
  | `And`                  | `findByLastnameAndFirstname`                                 | `… where x.lastname = ?1 and x.firstname = ?2`               |
  | `Or`                   | `findByLastnameOrFirstname`                                  | `… where x.lastname = ?1 or x.firstname = ?2`                |
  | `Is`, `Equals`         | `findByFirstname`,`findByFirstnameIs`,`findByFirstnameEquals` | `… where x.firstname = ?1`                                   |
  | `Between`              | `findByStartDateBetween`                                     | `… where x.startDate between ?1 and ?2`                      |
  | `LessThan`             | `findByAgeLessThan`                                          | `… where x.age < ?1`                                         |
  | `LessThanEqual`        | `findByAgeLessThanEqual`                                     | `… where x.age <= ?1`                                        |
  | `GreaterThan`          | `findByAgeGreaterThan`                                       | `… where x.age > ?1`                                         |
  | `GreaterThanEqual`     | `findByAgeGreaterThanEqual`                                  | `… where x.age >= ?1`                                        |
  | `After`                | `findByStartDateAfter`                                       | `… where x.startDate > ?1`                                   |
  | `Before`               | `findByStartDateBefore`                                      | `… where x.startDate < ?1`                                   |
  | `IsNull`, `Null`       | `findByAge(Is)Null`                                          | `… where x.age is null`                                      |
  | `IsNotNull`, `NotNull` | `findByAge(Is)NotNull`                                       | `… where x.age not null`                                     |
  | `Like`                 | `findByFirstnameLike`                                        | `… where x.firstname like ?1`                                |
  | `NotLike`              | `findByFirstnameNotLike`                                     | `… where x.firstname not like ?1`                            |
  | `StartingWith`         | `findByFirstnameStartingWith`                                | `… where x.firstname like ?1` (parameter bound with appended `%`) |
  | `EndingWith`           | `findByFirstnameEndingWith`                                  | `… where x.firstname like ?1` (parameter bound with prepended `%`) |
  | `Containing`           | `findByFirstnameContaining`                                  | `… where x.firstname like ?1` (parameter bound wrapped in `%`) |
  | `OrderBy`              | `findByAgeOrderByLastnameDesc`                               | `… where x.age = ?1 order by x.lastname desc`                |
  | `Not`                  | `findByLastnameNot`                                          | `… where x.lastname <> ?1`                                   |
  | `In`                   | `findByAgeIn(Collection<Age> ages)`                          | `… where x.age in ?1`                                        |
  | `NotIn`                | `findByAgeNotIn(Collection<Age> ages)`                       | `… where x.age not in ?1`                                    |
  | `True`                 | `findByActiveTrue()`                                         | `… where x.active = true`                                    |
  | `False`                | `findByActiveFalse()`                                        | `… where x.active = false`                                   |
  | `IgnoreCase`           | `findByFirstnameIgnoreCase`                                  | `… where UPPER(x.firstname) = UPPER(?1)`                     |



#### 정리

- 주의사항
  - 엔티티 필드명이 변경될 경우, 메서드명도 함께 변경해줘야 한다: 아닐 경우 애플리케이션 시작 시점에 오류 발생
    -> 오류를 사전에 잡을 수 있다는 장점
- 문제점
  - 위의 지원 키워드만으로 해결이 안 되는 경우가 일부 존재
  - **메서드명이 지나치게 장황해진다.**



### JPA Named Query

- 실무에서는 사용할 일이 거의 없음

- 사용

  - 해당 엔티티에 `@NamedQuery` 정의(`xml`로도 가능)

    ```java
    @NamedQuery(
            name= "Member.findByUsername",
            query = "SELECT m FROM Member m WHERE m.username = :username"
    )
    public class Member {
    ```

  - JPA Repository에서의 사용

    ```java
        public List<Member> findByUsername(String username) {
            return em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", username)
                    .getResultList();
        }
    ```

  - Data JPA Repository에서의 사용

    ```java
        @Query(name = "Member.findByUsername")
        List<Member> findByUsername(@Param("username") String username);
    ```

    - 위의 `@Query` 애노테이션을 생략해도 정상 작동한다.

      - `엔티티명.메서드명`으로 우선 찾았을 때 있으면 그걸 사용하기 때문

    - 이름을 다르게 할 경우에는 애노테이션이 필요하다.

      ```java
          @Query(name = "Member.findByUsername")
          List<Member> findByNamedQuery(@Param("username") String username);
      ```

- `NamedQuery`가 없을 경우 메서드 이름으로 쿼리를 생성한다. (`NamedQuery`가 우선 순위를 갖는다)

- `@Query` 기능에 밀려 거의 사용되지 않는다.

- 기존 방식(String으로 쿼리 규정하는 JPQL) 비해 갖는 장점

  - **`@NamedQuery`에 규정된 쿼리에 문법적 오류가 있을 경우 애플리케이션 실행 시점에 알 수 있다.**
  - 정적 쿼리라, 미리 파싱해 둘 수 있기 때문이다.
  - 이와 달리 문자열을 사용하는 방식의 경우 미리 파싱하여, 문법 문제를 확인할 수 없다!



### `@Query`: 리포지터리  메서드에 쿼리 정의하기

- 예시 코드

  ```java
      @Query("SELECT m FROM Member m WHERE m.username = username AND m.age = :age")
      List<Member> findUser(@Param("usernme") String username, @Param("age") int age);
  ```

  - JPQL을 인터페이스 메서드에 바로 적어줄 수 있음
  - ***실무에서 많이 사용하는 방식**
    - 메서드 이름으로 쿼리를 생성하는 방식은 메서드명이 너무 장황해지며
    - `NamedQuery`의 경우 도메인에도 코드가 추가된다.

- **`NamedQuery`처럼 애플리케이션 실행 시점에 쿼리 문법 오류를 확인할 수 있다!**



### 쿼리 메서드 - 정리

- 정적 쿼리의 경우

  - 단순한 쿼리의 경우 메서드 이름으로 쿼리 생성

  - 복잡한 쿼리의 경우 `@Query` 이용

- 동적 쿼리의 경우 QueryDSL 사용



#### `@Query`, 값, DTO 조회하기

- 값 조회

  ```java
      @Query("SELECT m.username FROM Member m")
      List<String> findUsernameList();
  ```

- DTO로 조회하기

  ```java
      @Query("SELECT new com.example.springdatajpa.dto.MemberDto(m.id, m.username, t.name)" +
              " FROM Member m JOIN m.team t")
      List<MemberDto> findMemberDto();
  ```

  - JPQL의 new Operation을 이용한다. (JPA의 경우도 마찬가지)
  - QueryDSL 사용시 더 편리하게 처리 가능



### 파라미터 바인딩

- JPQL의 파라미터 바인딩 방식: 이름 기반 파라미터 바인딩을 사용하자

  - 위치 기반: `SELECT m FROM Member m WHERE m.username = ?0`

  - **이름 기반**: `SELECT m FROM Member m WHERE m.username = :name`

- **컬렉션 파라미터 바인딩**

  ```java
      @Query("SELECT m FROM Member m WHERE m.username IN :names")
      List<Member> findByNames(@Param("names") Collection<String> names);
  ```

  - 컬렉션 전달하여 편하게 사용 가능

    ```java
            List<Member> result = memberRepository.findByNames(Arrays.asList("memberA", "memberC"));
    ```



### 반환 타입

- 스프링 데이터 JPA는 다양한 반환 타입 지원

- **Query Return Types**: 다음 공식 문서 참조: [링크](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-return-types)

  | Return type                                                  | Description                                                  |
  | :----------------------------------------------------------- | :----------------------------------------------------------- |
  | `void`                                                       | Denotes no return value.                                     |
  | Primitives                                                   | Java primitives.                                             |
  | Wrapper types                                                | Java wrapper types.                                          |
  | `T`                                                          | A unique entity. Expects the query method to return one result at most. If no result is found, `null` is returned. More than one result triggers an `IncorrectResultSizeDataAccessException`. |
  | `Iterator<T>`                                                | An `Iterator`.                                               |
  | `Collection<T>`                                              | A `Collection`.                                              |
  | `List<T>`                                                    | A `List`.                                                    |
  | `Optional<T>`                                                | A Java 8 or Guava `Optional`. Expects the query method to return one result at most. If no result is found, `Optional.empty()` or `Optional.absent()` is returned. More than one result triggers an `IncorrectResultSizeDataAccessException`. |
  | `Option<T>`                                                  | Either a Scala or Vavr `Option` type. Semantically the same behavior as Java 8’s `Optional`, described earlier. |
  | `Stream<T>`                                                  | A Java 8 `Stream`.                                           |
  | `Streamable<T>`                                              | A convenience extension of `Iterable` that directy exposes methods to stream, map and filter results, concatenate them etc. |
  | Types that implement `Streamable` and take a `Streamable` constructor or factory method argument | Types that expose a constructor or `….of(…)`/`….valueOf(…)` factory method taking a `Streamable` as argument. See [Returning Custom Streamable Wrapper Types](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.collections-and-iterables.streamable-wrapper) for details. |
  | Vavr `Seq`, `List`, `Map`, `Set`                             | Vavr collection types. See [Support for Vavr Collections](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.collections-and-iterables.vavr) for details. |
  | `Future<T>`                                                  | A `Future`. Expects a method to be annotated with `@Async` and requires Spring’s asynchronous method execution capability to be enabled. |
  | `CompletableFuture<T>`                                       | A Java 8 `CompletableFuture`. Expects a method to be annotated with `@Async` and requires Spring’s asynchronous method execution capability to be enabled. |
  | `ListenableFuture`                                           | A `org.springframework.util.concurrent.ListenableFuture`. Expects a method to be annotated with `@Async` and requires Spring’s asynchronous method execution capability to be enabled. |
  | `Slice<T>`                                                   | A sized chunk of data with an indication of whether there is more data available. Requires a `Pageable` method parameter. |
  | `Page<T>`                                                    | A `Slice` with additional information, such as the total number of results. Requires a `Pageable` method parameter. |
  | `GeoResult<T>`                                               | A result entry with additional information, such as the distance to a reference location. |
  | `GeoResults<T>`                                              | A list of `GeoResult<T>` with additional information, such as the average distance to a reference location. |
  | `GeoPage<T>`                                                 | A `Page` with `GeoResult<T>`, such as the average distance to a reference location. |
  | `Mono<T>`                                                    | A Project Reactor `Mono` emitting zero or one element using reactive repositories. Expects the query method to return one result at most. If no result is found, `Mono.empty()` is returned. More than one result triggers an `IncorrectResultSizeDataAccessException`. |
  | `Flux<T>`                                                    | A Project Reactor `Flux` emitting zero, one, or many elements using reactive repositories. Queries returning `Flux` can emit also an infinite number of elements. |
  | `Single<T>`                                                  | A RxJava `Single` emitting a single element using reactive repositories. Expects the query method to return one result at most. If no result is found, `Mono.empty()` is returned. More than one result triggers an `IncorrectResultSizeDataAccessException`. |
  | `Maybe<T>`                                                   | A RxJava `Maybe` emitting zero or one element using reactive repositories. Expects the query method to return one result at most. If no result is found, `Mono.empty()` is returned. More than one result triggers an `IncorrectResultSizeDataAccessException`. |
  | `Flowable<T>`                                                | A RxJava `Flowable` emitting zero, one, or many elements using reactive repositories. Queries returning `Flowable` can emit also an infinite number of elements. |

- 예시 코드

  ```java
      Member findMemberByUsername(String username);
      Optional<Member> findOptionalByUsername(String username);
  ...
  ```

- 주의사항: 여러 건 결과가 조회 가능한데, 컬렉션이 아니라 단건 조회처럼 반환 타입을 받으면 예외 발생함

  - `NonUniqueResultException` 발생, 스프링 사용하기에 `IncorrectResultSizeDataAccessException`으로 번역됨



### 순수 JPA 페이징과 정렬

- 예제 코드

  ```java
      public List<Member> findByPage(int age, int offset, int limit) {
          return em.createQuery("SELECT m FROM Member m WHERE m.age = :age ORDER BY m.username DESC", Member.class)
                  .setParameter("age", age)
                  .setFirstResult(offset)
                  .setMaxResults(limit)
                  .getResultList();
      }
  
      public long totalCount(int age) {
          return em.createQuery("SELECT COUNT(m) FROM Member m WHERE m.age = :age", Long.class)
                  .setParameter("age", age)
                  .getSingleResult();
      }
  ```

  

### 스프링 데이터 JPA 페이징과 정렬

- 스프링 데이터에서는 페이징과 정렬 인터페이스 제공

  - `org.springframework.data.domain.Sort`: 정렬 기능
  - `org.springframework.data.domain.Pageable`: 페이징 기능(내부에 `Sort` 포함)

- 특수 반환 타입

  - `org.springframework.data.domain.Page`: 추가 `COUNT` 쿼리 결과를 포함하는 페이징
  - `org.springframework.data.domain.Slice`: 페이지 번호가 필요 없이 다음 페이지만 확인 가능(내부적으로 `limit + 1` 조회)
  - `List`(자바 컬렉션): 추가 `COUNT` 쿼리 없이 결과만 반환

- 예제 코드

  ```java
  Page<Member> findByAge(int age, Pageable pageable);
  ```

  - 사용

    ```java
        @Test
        void paging() {
            //Given
            memberRepository.save(new Member("member1", 10));
            memberRepository.save(new Member("member2", 10));
            memberRepository.save(new Member("member3", 10));
            memberRepository.save(new Member("member4", 10));
            memberRepository.save(new Member("member5", 10));
            memberRepository.save(new Member("member6", 10));
            memberRepository.save(new Member("member7", 10));
            memberRepository.save(new Member("member8", 10));
    
            int age = 10;
            PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
    
            //When
            Page<Member> page = memberRepository.findByAge(age, pageRequest);
    
            //Then
            List<Member> members = page.getContent();
            long totalCount = page.getTotalElements();
    
            assertThat(members.size()).isEqualTo(3);
            assertThat(totalCount).isEqualTo(8);
            assertThat(page.getNumber()).isEqualTo(0);
            assertThat(page.getTotalPages()).isEqualTo(3);
            assertThat(page.isFirst()).isTrue();
            assertThat(page.hasNext()).isTrue();
        }
    ```

    - 반환 타입이 `Page`라 `COUNT` 쿼리도 알아서 처리해준다.

- `Slice`

  - `TotalCount` 구하지 않음
  - 요청 양보다 `limit`가 1더 크다!
  - `Page`에서 `Slice`로 바꾸기만 하면 페이징 방식을 간단하게 바꿀 수 있음

- `TotalCount`를 구하는 것 자체가 DB의 모든 자료를 조회하기 때문에 성능을 깎아먹는 쿼리가 될 수 있다.

  - `Count`를 할 때는 성능을 위해 조인 하지 않고 구해오는 편이 낫다: 분리가 필요하다.

  - 아래와 같이 카운트 쿼리만 별도로 규정할 수 있다:

    ```java
        @Query(value = "SELECT m FROM Member m LEFT JOIN m.team t",
                countQuery = "SELECT COUNT(m.username) FROM Member m")
        Page<Member> findByAge(int age, Pageable pageable);
    ```

- 주의사항

  - API에서 `Page<Member>`형태로 바로 반환해서는 안 된다. DTO로 변환, 반환해야 한다.
  - `Page<MemberDto> dtoPage = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));`
    - `map()`이용하여 변환하자.



### 벌크성 수정쿼리

- 순수 JPA 코드

  ```java
      public int bulkAgePlus(int age) {
          return em.createQuery("UPDATE Member m SET m.age = m.age +1 WHERE m.age >= :age")
                  .setParameter("age", age)
                  .executeUpdate();
      }
  ```

- Data JPA 코드

  ```java
      @Modifying
      @Query("UPDATE Member m SET m.age = m.age +1 WHERE m.age >= :age")
      int bulkAgePlus(@Param("age") int age);
  ```

  - `@Modifying`: 조회성 쿼리가 아니라 `executeUpdate`형 쿼리임을 밝힘

- 주의사항

  - 벌크 연산은 영속성 컨텍스트를 무시하고 실행됨

  - 이로 인한 문제를 피하기 위해 벌크 연산 이후에는 영속성 컨텍스트의 내용을 날려야함(`em.clear()`)

  - Data JPA의 경우 아래와 같은 설정으로 자동적으로 연산 이후에 `clear()`하게 만들 수 있다.

    ```java
        @Modifying(clearAutomatically = true)
        @Query("UPDATE Member m SET m.age = m.age +1 WHERE m.age >= :age")
        int bulkAgePlus(@Param("age") int age);
    ```

  - **`JdbcTemplate`, `Mybatis`등을 JPA와 같이 사용할 경우도 벌크 연산과 마찬가지로 영속성 컨텍스트에 반영되지 않음에 주의해야 한다!**



### `@EntityGraph`

- 문제 상황 

  - JPA 사용하면 N+1문제를 만날 수 밖에 없음 -> 성능 저하

  - 해결법: 페치 조인

    ```java
        @Query("SELECT m FROM Member m LEFT JOIN FETCH m.team")
        List<Member> findMemberFetchJoin();
    ```

    - DB의 조인은 조회만 하지만 페치 조인은 조회에 더해 데이터까지 가져옴
    - 하지만 페치 조인을 사용하려면 일일히 JPQL을 작성해줘야 한다는 문제점

- `@EntityGraph`

  ```java
      @Override
      @EntityGraph(attributePaths = {"team"})
      List<Member> findAll();
  ```

  - 애노테이션을 붙여주는 것 만으로 해결 가능

  - JPQL 사용하는 경우나, 메서드 이름으로 생성된 쿼리의 경우에도 적용 가능

    ```java
        @EntityGraph(attributePaths = {"team"})
        @Query("SELECT m FROM Member m")
        List<Member> findMemberEntityGraph();
    
        @EntityGraph(attributePaths = {"team"})
        List<Member> findByAge(@Param("age") int age);
    ```

- `NamedEntityGraph`

  ```java
  @NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
  public class Member {
  ```

  ```java
      @EntityGraph("Member.all")
      List<Member> findByAge(@Param("age") int age);
  ```

  - 이런 방식으로도 같은 효과를 볼 수 있음
  - JPA 표준 스펙



### JPA Hint & Lock

#### JPA Hint

- SQL 힌트가 아니라 JPA 구현체에게 제공하는 힌트

- 예시: 조회 전용으로 만들기

  ```java
      @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
      Member findReadOnlyByUsername(String username);
  ```

  - 변경 감지의 대상이 되지 않아 성능 향상을 꾀할 수 있음

- **성능 최적화는 테스트 이후에 그 향상 정도에 따라 판단하여 수행해야 한다.**



#### Lock

- 예시

  ```java
      @Lock(LockModeType.PESSIMISTIC_WRITE)
      List<Member> findLockByUsername(String username);
  ```

- JPA 표준 제공 스펙임

- `SELECT... FOR UPDATE`: 형태로 쿼리가 나감

- 실시간 트래픽이 많은 서비스에서는 걸지 마라.

  - 굳이 걸거라면 `OPTIMISTIC`방식 정도: 버저닝 메커니즘을 이용하는 방식

- 실시간 트래픽보다는 값 일치가 더 중요한 경우라면 거는 것을 생각



## 확장 기능

### 사용자 정의 리포지터리 구현

- 필요

  - 스프링 데이터 JPA가 제공하는 인터페이스를 직접 구현하기에는, 구현해야 할 기능이 너무 많음
  - 다음과 같은 이유 등으로 인터페이스를 직접 구현하고 싶을 때 필요한 기능
    - JPA 직접 사용(`em`)
    - JDBC Template 사용
    - MyBatis 사용
    - DB 커넥션 직접 사용
    - **QueryDSL 사용** 등

- 사용 방법

  - 새 인터페이스 만듦: `CustomMemberRepository`

    ```java
    public interface CustomMemberRepository {
        List<Member> findMemberCustom();
    }
    ```

  - **새 인터페이스의 구현체 생성: `MemberDataJpaRepositoryImpl` (명명 주의!)**

    ```java
    @RequiredArgsConstructor
    public class MemberDataJpaRepositoryImpl implements CustomMemberRepository {
    
        private final EntityManager em;
    
        @Override
        public List<Member> findMemberCustom() {
            return em.createQuery("SELECT m FROM Member m")
                    .getResultList();
        }
    }
    ```

  - 사용할 Data JPA Repsitory가 커스텀 인터페이스도 상속받게 만듦

    ```java
    public interface MemberDataJpaRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
    ```

- **주의사항**

  - **새 인터페이스의 구현체 명은 `우리가 사용할 리포지터리명 + Impl` 형태로 명명해야 한다.**

  - 다른 형태의 `postfix`사용하고 싶다면

    - `xml` 설정

      ```xml
      <repositories base-package="프로젝트경로.repository경로"
                    repository-impl-postfix="impl"/>
      ```

    - `JavaConfig` 설정

      ```java
      @EnableJpaRepositories(basePackages = 
                            "프로젝트경로.repository경로",
                            repositoryImplementationPostfix = "impl")
      ```

- 꼭 커스텀까지 사용해서 하나의 리포지터리에 모든 기능을 몰아 넣을 필요는 없다.

  - 핵심 로직을 담은 리포지터리와 화면에 맞춘 리포지터리를 분리하는 것을 추천한다.



### Auditing

- 실무에서는 모든 테이블 만들 때 등록일, 수정일을 남긴다. 추적이 가능해지기에 운영 난도가 매우 낮아진다.

  - 다음과 같이 일괄적으로 저장해야 하는 항목들을 어떻게 쉽게 저장할 수 있을까?
    - 등록일
    - 수정일(변경 불가능한 경우는 예외)
    - 등록자
      - 보통 로그인한 세션 정보를 기반으로 아이디를 남김
      - 특정 사용자가 아닌 시스템이 데이터 입력을 모두 처리하는 경우에도, 어떤 시스템이 했는가에 대해서 남기기도 함
    - 수정자

- 순수 JPA의 경우

  - 예제 코드

    - `JpaBaseEntity`

      ```java
      @MappedSuperclass
      @Getter
      public abstract class JpaBaseEntity {
      
          @Column(updatable = false)
          private LocalDateTime createdDate;
          private LocalDateTime updatedDate;
      
          @PrePersist
          public void prePersist() {
              LocalDateTime now = LocalDateTime.now();
              this.createdDate = now;
              this.updatedDate = now;
          }
      
          @PreUpdate
          public void preUpdate() {
              this.updatedDate = LocalDateTime.now();
          }
      }
      ```

  - 해당 `JpaBaseEntity`를 상속받기만 하면 등록일, 수정일을 간단하게 기록할 수 있음

  - **JPA 주요 이벤트 애노테이션**

    - `@PrePersist`, `@PostPersist`
    - `@PreUpdate`, `@PostUpdate`

- **스프링 Data JPA 사용시**

  - 설정

    - 앱에 애노테이션 추가: `@EnableJpaAuditing`

      ```java
      @EnableJpaAuditing
      @SpringBootApplication
      public class SpringDataJpaApplication {
      ```

  - `DataJpaBaseEntity`

    ```java
    @EntityListeners(AuditingEntityListener.class)
    @MappedSuperclass
    @Getter
    public class DataJpaBaseEntity {
    
        @CreatedDate
        @Column(updatable = false)
        private LocalDateTime createdDate;
        @LastModifiedDate
        private LocalDateTime lastModifiedDate;
    
        @CreatedBy
        @Column(updatable = false)
        private String createdBy;
    
        @LastModifiedBy
        private String lastModifiedBy;
    }
    ```

  - 등록자, 수정자

    - `auditorProvider()`

    ```java
        @Bean
        public AuditorAware<String> auditorProvider() {
            return () -> Optional.of(UUID.randomUUID().toString());
        }
    ```

    - 실제 사용할 때는
      - 스프링 시큐리티 사용시 `SpringSecurityContext` 등에서 세션정보를 가져와서, ID를 꺼내거나
      - HTTP 세션에서 꺼내는 방식 등으로 userId 등을 등록해 준다.
    - 관련 `BaseEntity`가 호출될 때마다 위의 빈으로 등록된 방식으로 값을 채워준다.

  - `@EnableJpaAuditing(modifyOnCreate = false)`

    - `updated`관련은 생성 시에는 `null`로 들어간다: 비추천

  - 전체 적용

    - `@EntityListeners`를 생략하고 이벤트를 엔티티 전체에 적용하려면 `orm.xml`을 이용해서 등록

- 실무에서의 적용: 추천 방식

  - 변경 시간은 항상 사용 -> `BaseTimeEntity`
  - 변경 주체는 사용할 때도, 안 사용할 때도 있음. `BaseTimeEntity`를 상속한 엔티티를 사용



### Web 확장 - 도메인 클래스 컨버터

- 예제 코드

  ```java
  @RestController
  @RequiredArgsConstructor
  public class MemberController {
  
      private final MemberDataJpaRepository memberRepository;
  
      @PostConstruct
      protected void init() {
          memberRepository.save((new Member("userA", 20)));
      }
  
      @GetMapping("/members/{id}")
      public String findMember(@PathVariable("id") Long memberId) {
          Member member = memberRepository.findById(memberId).get();
          return member.getUsername();
      }
  
      @GetMapping("/members2/{id}")
      public String findMember(@PathVariable("id") Member member) {
          return member.getUsername();
      }
  }
  ```

  - HTTP 요청은 회원 PK 값을 받지만 도메인 클래스 컨버터가 중간에 동작해서 회원 엔티티 객체를 반환
  - 도메인 클래스 컨버터도 리포지터리를 사용해서 엔티티를 찾음

- **주의사항**

  - 도메인 클래스 컨버터를 이용해서 파라미터로 받은 엔티티는 단순 조회용으로만 사용해야 한다.
  - 트랜잭션이 없는 범위에서 엔티티를 조회했기에, 엔티티를 변경해도 DB에 저장되지 않는다.



### Web 확장 - 페이징과 정렬

- 예제 코드

  ```java
      @GetMapping("/members")
      public Page<Member> list(Pageable pageable) {
          return memberRepository.findAll(pageable);
      }
  ```

  - `http://localhost:8080/members?page=0&size=5&sort=id,desc`형태로 요청 가능(파라미터 생략시 기본값)
    - `sort` 조건을 여럿 넣을 수도 있음

- 요청 파라미터

  - `page`: 현재 페이지, **0부터 시작**
  - `size`: 한 페이지에 노출할 데이터 건수
  - `sort` 정렬 조건을 정의(`asc`는 생략 가능)

- 기본값 설정

  -  글로벌 설정: `application.yml`

    ```yaml
    spring:
      data:
        web:
          pageable:
            default-page-size: 10
            max-page-size: 2000
    ```

  - 개별 설정: `@PageableDefault`

    ```java
        @GetMapping("/members")
        public Page<Member> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
            return memberRepository.findAll(pageable);
        }
    ```

- 접두사

  - 페이징 정보가 둘 이상일 경우 접두사로 구분
  - `@Qualifier`에 접두사명 추가
  - ex) `/members?member_page=0&order_page=1`

- `Page`로 반환하면 총 페이지 정보 등이 나오기 때문에, 사용이 용이



#### Page 내용을 DTO로 변환하기

- 예제 코드

  ```java
      @GetMapping("/members")
      public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
          return memberRepository.findAll(pageable).map(MemberDto::from);
      }
  ```

- 엔티티는 DTO를 봐선 안 되지만, DTO가 엔티티를 보는 것은 괜찮다: DTO에 엔티티를 받아서 변환하는 메서드를 넣어도 좋다.

  ```java
  public record MemberDto(Long id, String username, String teamName) {
  
      public MemberDto from(Member member) {
          return new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName());
      }
  }
  ```



#### Page를 1부터 시작하기

- 스프링 Data의 경우 페이지의 시작이 0임

- 1부터 시작하게끔 하고 싶다면?

  - `Page`를 새로이 정의하는 방식

    ```java
    @GetMapping("/members")
    public MyPage<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
        PageRequest request = PageRequest.of(1, 2);
        Page<MemberDto> map = memberRepository.findAll(pageable).map(MemberDto::from);
        MyPage<MemberDto> ...
    }
    ```

    - 원하는 방식으로 규정해서 사용하는 방식

  - `spring.data.web.pageable.one-indexed-parameters`를 `true`로 설정하기

    - **한계: `pageable` 의 `pageNumber` 등의 정보에는 반영되지 않는다!**



## 스프링 데이터 JPA 분석

### 스프링 데이터 JPA 구현체 분석

- `SimpleJpaRepository`

  - 스프링 데이터 JPA가 제공하는 공통 인터페이스의 구현체

    ```java
    @Repository
    @Transactional(readOnly = true)
    public class SimpleJpaRepository<T, ID> implements JpaRepositoryImplementation<T, ID> {
    ```

  - 분석

    - `@Repository`
      - 스프링 빈의 컴포넌트 스캔 대상임
      - 예외가 발생할 경우 스프링 예외로 번역해줌
        - 하부 구현 기술을 JDBC -> JPA로 변경하더라도 기존 비즈니스 로직에 끼치는 영향을 최소화하기 위함
    - `@Transactional(readOnly = true)`
      - 스프링 Data JPA의 모든 기술은 트랜잭션 안에서 작동한다. (JPA의 모든 변경은 트랜잭션 안에서 동작하기에)
        - `@Transactional` 안에서 호출될 경우, 트랜잭션을 이어받고,
        - 아닐 경우 새 트랜잭션을 시작
      - **해당 옵션이 활성화되어 있을 경우 플러시를 생략하기에 성능 향상을 기대할 수 있음**

- **주의 사항: `save()`메서드**

  - 새로운 엔티티의 경우 저장(`persist()`)
  - 새로운 엔티티가 아닐 경우 병합(`merge()`)
    - `merge()`의 경우 DB에 한 번 SELECT 쿼리를 날린다는 것
    - **가능한 한 `merge()`사용해서는 안 된다! 데이터 갱신 용으로 사용하지 말아라!**
  - 그렇다면 어떻게 새 엔티티인지 구별하나?



### 새 엔티티인지 구별하는 기준

- 새 엔티티인지 판단하는 기본 전략

  - 식별자가 객체일 경우: `null`
  - 식별자가 자바 기본 타입일 경우: `0`으로 판단
  - `Persistable` 인터페이스를 구현하여 판단 로직을 변경 가능

- 추천 전략: `CreatedDate`를 이용하는 방식

  ```java
  @Entity
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public class Item extends DataJpaBaseEntity implements Persistable<String> {
  
      @Id
      private String id;
  
      public Item(String id) {
          this.id = id;
      }
  
      @Override
      public boolean isNew() {
          return this.getCreatedDate() == null;
      }
  }
  ```

  

## 나머지 기능들

- 실무에서 많이 사용되지는 않는 기능들



### Specifications(명세)

- "도메인 주도 설계(Domain-Driven Design)"에서 소개된 개념

- 스프링 데이터 JPA는 JPA Criteria를 활용하여 이 개념을 지원

- **술어(predicate)**

  - `true` 혹은 `false`로 평가
  - `WHERE`, `AND`, `OR`, `NOT` 등 연산자 조합하여 다양한 검색조건을 쉽게 생성(컴포지트 패턴)
  - ex) 검색 조건 하나하나
  - 스프링 데이터 JPA에서는 `Specification` 클래스로 정의

- **사용 방법**

  - 해당 리포지터리가 `JpaSpecificationExecutor<T>`를 구현하게끔 만든다.

  - 엔티티에 대한 `Specification`을 만든다:

    ```java
    public class MemberSpec {
    
        public static Specification<Member> teamName(final String teamName) {
            return (root, query, criteriaBuilder) -> {
    
                if (!StringUtils.hasLength(teamName)) {
                    return null;
                }
    
                Join<Member, Team> t = root.join("team", JoinType.INNER);//회원과 조인
                return criteriaBuilder.equal(t.get("name"), teamName);
            };
        }
    
        public static Specification<Member> username(final String username) {
            return (root, query, criteriaBuilder) -> {
    
                if (!StringUtils.hasLength(username)) {
                    return null;
                }
                return criteriaBuilder.equal(root.get("username"), username);
            };
        }
    }
    ```

  - 사용 예시

    ```java
            Specification<Member> spec = MemberSpec.username("member1").and(MemberSpec.teamName("teamA"));
            List<Member> result = memberRepository.findAll(spec);
    ```

- **실무에서는 거의 사용 안 함! -> QueryDSL 사용하라!**



### Query By Example

- 공식 문서: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#query-by-example

- 예시 코드

  ```java
          Member memberExample = new Member("member1", 50, teamA);	//probe
          ExampleMatcher matcher = ExampleMatcher.matching()
                  .withIgnorePaths("age");
          Example<Member> example = Example.of(memberExample, matcher);
  
          List<Member> result = memberRepository.findAll(example);
  ```

- 설명

  - `Probe`: 필드에 데이터가 있는 실제 도메인 객체
  - `ExampleMatcher`: 특정 필드를 일치시키는 상세한 정보 제공, 재사용 가능
  - `Example`: `Probe`와 `ExampleMatcher`로 구성, 쿼리를 생성하는데 사용

- 장점

  - 동적 쿼리를 편리하게 처리
  - 도메인 객체를 그대로 사용
  - DB를 RDB에서 NOSQL로 변경해도 코드 변경이 없게 추상화되어 있음
  - 스프링 데이터 JPA의 `JpaRepository` 인터페이스에 이미 포함됨

- 단점

  - 조인은 가능하지만 내부 조인(INNER JOIN)만 가능, 외부 조인(LEFT JOIN) 불가
  - 중첩 제약조건 안 됨
  - 매칭 조건이 매우 단순함
    - 문자는 `starts/contains;/ends/regex`
    - 다른 속성은 정확한 매칭(`=`)만 지원

- **실무에서 사용하기에는 매칭 조건이 너무 단순하며 LEFT 조인 불가 -> QueryDSL 사용하자!**



### Projections

- 엔티티 대신에 DTO를 편리하게 조회할 때 사용

- 전체 엔티티가 아니라 특정 정보만 조회하고 싶을 때 사용

- 예시 코드

  - `UsernameOnly` 인터페이스 기반 프로젝션

    ```java
    public interface UsernameOnly {
    
        @Value("#{target.username + ' ' + target.age}")	//방법1: Open Projection, SpEL 사용
        String getUsername();
    
    //    String getUsername();	//방법2: Closed Projection
    }
    ```

    - 구현체를 생성하는 것이 아니라 인터페이스를 생성하는 방식
    - 인터페이스 기반의 Closed Projection / Open Projection
      - **Open Projection의 경우 엔티티 데이터를 통째로 가져옴: 최적화는 안 됨** (위의 경우 필요한 `username`만 가져오는 것이 아니라 `Member` 엔티티 데이터 전체를 가지고 온다)
    - 실제 구현체는 스프링 데이터 JPA가 만들어짐: 프록시 객체
    - 원하는 데이터만 가져올 수 있음(Closed)
      - 조회 조건 메서드(username)과 조합하여 사용할 수 있음: + 조회 결과 타입을 어떤 것을 사용할지를 추가적으로 규정 가능

  - `UsernameOnlyDto`: 클래스 기반 프로젝션

    ```java
    public class UsernameOnlyDto {
    
        private final String username;
    
        public UsernameOnlyDto(String username) {
            this.username = username;
        }
    
        public String getUsername() {
            return username;
        }
    }
    ```

    - 생성자와 Getter
      - **생성자의 파라미터명으로 매칭함**

  - `NestedClosedProjections`: 중첩된 케이스

    ```java
    public interface NestedClosedProjections {
    
        String getUsername();
        TeamInfo getTeam();
    
        interface TeamInfo {
            String getName();
        }
    }
    ```

  - 리포지터리 코드

    ```java
        List<UsernameOnly> findProjectionByUsername(@Param("username") String username);
    
        List<UsernameOnlyDto> findProjectionDtoByUsername(@Param("username") String username);
    
        <T> List<T> findProjectionGenericByUsername(@Param("username") String username, Class<T> type);
    ```

    - 동일한 쿼리를 사용하는 경우 제네릭을 이용하여 유연하게 타입을 받을 수도 있다: 동적 프로젝션

      ```java
      List<NestedClosedProjections> resultOfNested = memberRepository.findProjectionGenericByUsername("member1", NestedClosedProjections.class);
      ```

- **주의사항**

  - 프로젝션 대상이 Root 엔티티일 경우 JPQL SELECT 절을 최적화할 수 있음
  - 프로젝션 대상이 Root가 아닐 경우
    - LEFT OUTER JOIN 처리하며
    - 모든 필드를 SELECT하여 엔티티로 조회한 후에 계산

- 정리

  - **프로젝션 대상이 Root 엔티티일 경우 유용**
    - 프로젝션 대상이 Root 엔티티를 넘어가면 최적화 안 됨!
  - 실무의 복잡한 쿼리를 해결하기에는 한계가 있음
  - **단순할 때만 사용하고, 복잡하면 QueryDSL 사용할 것!**



### 네이티브 쿼리

- **가능한 한 사용하지 말자!**

  - **네이티브 SQL을 DTO로 조회할 때는 JdbcTemplate이나 MyBatis 사용을 권장함**

- 예시

  ```java
      @Query(value = "SELECT * FROM member WHERE username = ?", nativeQuery = true)
      Member findByNativeQuery(String username);
  ```

- 특징

  - 페이징 지원
  - 반환 티입
    - `Object[]`
    - `Tuple`
    - `DTO`(스프링 데이터 인터페이스 `Projections` 지원)
  - 제약
    - `Sort` 파라미터를 통한 정렬이 정상 동작하지 않을 수 있음: 직접 처리가 필요
    - JPQL과 달리 애플리케이션 로딩 시점에 문법 확인 불가
    - 동적 쿼리 불가

- **Projections의 활용**

  - 예시 코드

    ```java
        @Query(value = "SELECT m.member_id as id, m.username, t.name as teamName" +
                " FROM member m LEFT JOIN team t",
                countQuery = "SELECT COUNT(*) FROM member",
                nativeQuery = true)
        Page<MemberProjection> findByNativeProjection(Pageable pageable);
    ```

  - **페이징이 가능한 정적 네이티브 쿼리**

- 하이버네이트 활용한 동적 네이티브 쿼리

  - 스프링 JdbcTemplate, MyBatis, jooq 같은 외부 라이브러리 사용
  - `em.createNativeQuery(sql)`

  
