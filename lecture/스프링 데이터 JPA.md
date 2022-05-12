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

### 순수 JPA 기반 리포지토리

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



