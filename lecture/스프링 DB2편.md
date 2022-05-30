# 스프링 DB2편 - 데이터 접근 활용 기술

# 프로젝트 시작하기

## 다양한 데이터 접근기술

- 다양한 데이터 접근 기술
  - JdbcTemplate
  - MyBatis
  - JPA, Hibernate
  - 스프링 데이터 JPA
  - QueryDSL
- SQLMapper
  - JdbcTemplate, MyBatis
  - 개발자는 SQL만 작성, SQL 결과를 객체로 편리하게 매핑
  - JDBC 직접 사용 시 발생하는 여러 중복을 제거 + 여러 편의 기능 제공
- ORM 관련기술
  - JPA, Hibernate, 스프링 데이터 JPA, QueryDSL
  - 기본적인 SQL은 JPA가 대신 작성해줌
  - 객체를 사용하듯이 DB를 사용할 수 있음
  - 



## 프로젝트 살펴보기

- DTO; Data Transfer Object

  - 데이터 전송 객체
  - 기능 없이 데이터 전송 용도로만 사용됨
  - 물론 기능이 아예 없어야 하는 것은 아니며, 객체의 주 목적이 데이터 전송이면 DTO
  - 네이밍 컨벤션의 경우 조직의 합의가 핵심

  - 패키지에서 DTO를 어디에 두어야 할까?
    - `ItemUpdateDto`와 `ItemSearchCond`를 repository 경로에 두었음
    - 궁극적으로 Repository에서 사용하는 DTO이기 때문에 여기에 두었음
    - 만약에 Service에서 마지막으로 사용하는 DTO라면 Service에 두는 것이 낫다.
    - 패키지의 참조 관계도 계층에 따라 두도록 하자.
      - 만약 Repository에서도 사용하는데 Service 패키지에 있다면 순환적 참조가 발생한다.
      - 애매한 상황이면 별도의 패키지를 만들어 사용한다.

- `TestDataInit`

  ```java
  @Slf4j
  @RequiredArgsConstructor
  public class TestDataInit {
  
      private final ItemRepository itemRepository;
  
      /**
       * 확인용 초기 데이터 추가
       */
      @EventListener(ApplicationReadyEvent.class)
      public void initData() {
          log.info("test data init");
          itemRepository.save(new Item("itemA", 10000, 10));
          itemRepository.save(new Item("itemB", 20000, 20));
      }
  
  }
  ```

  - `@EventListener`: 특정 이벤트 발생시 호출해줌
    - `ApplicationReadyEvent.class`
      - 스프링 컨테이너가 완전히 초기화를 끝내고 실행 준비가 되었을 때 발생하는 이벤트
      - `@PostConstruct`를 사용할 경우 AOP 등이 처리되지 않은 시점에 호출될 수 있음
        - `@Transactional` 등과 관련된 AOP가 적용되지 않은 시점에서 호출될 수 있음



- `ItemServiceApplication`

  ```java
  @Import(MemoryConfig.class)
  @SpringBootApplication(scanBasePackages = "hello.itemservice.web")
  public class ItemServiceApplication {
  
  	public static void main(String[] args) {
  		SpringApplication.run(ItemServiceApplication.class, args);
  	}
  
  	@Bean
  	@Profile("local")
  	public TestDataInit testDataInit(ItemRepository itemRepository) {
  		return new TestDataInit(itemRepository);
  	}
  
  }
  ```

  - `@Import`: 설정 파일 등록

    ```java
    @Configuration
    public class MemoryConfig {
    
        @Bean
        public ItemService itemService() {
            return new ItemServiceV1(itemRepository());
        }
    
        @Bean
        public ItemRepository itemRepository() {
            return new MemoryItemRepository();
        }
    
    }
    ```



### 프로필

- 로컬, 운영 환경, 테스트 실행 등 다양한 환경에 따라 다른 설정을 할 때 사용

- `application.properties`

  ```properties
  spring.profiles.active=local
  ```

- 아무런 설정 없이 실행할 경우 `default` 프로필이 실행됨

- test 경로 내의 `test/resources/application.properties`에 별개의 설정을 넣어 두면 테스트용으로 동작

  - 테스트시에는 기본 데이터가 추가되어 있을 경우 테스트에 문제가 생길 수도 있기 때문

- [프로필에 대한 공식 메뉴얼 링크](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)



### 테스트 코드 살펴보기

- `findItems`: 조건 검색 검증

  ```java
      @Test
      void findItems() {
          //given
          Item item1 = new Item("itemA-1", 10000, 10);
          Item item2 = new Item("itemA-2", 20000, 20);
          Item item3 = new Item("itemB-1", 30000, 30);
  
          itemRepository.save(item1);
          itemRepository.save(item2);
          itemRepository.save(item3);
  
          //둘 다 없음 검증
          test(null, null, item1, item2, item3);
          test("", null, item1, item2, item3);
  
          //itemName 검증
          test("itemA", null, item1, item2);
          test("temA", null, item1, item2);
          test("itemB", null, item3);
  
          //maxPrice 검증
          test(null, 10000, item1);
  
          //둘 다 있음 검증
          test("itemA", 10000, item1);
      }
  
      void test(String itemName, Integer maxPrice, Item... items) {
          List<Item> result = itemRepository.findAll(new ItemSearchCond(itemName, maxPrice));
          assertThat(result).containsExactly(items);
      }
  ```

  - `containsExctly`: 순서까지 일치해야 assertion 성공으로 봄

- **구현체가 아니라 인터페이스를 테스트하고 있음**

  - 구현체를 변경하는 것 만으로 간단한 검증이 가능하다.
  - 기본적으로 인터페이스 기반 테스트가 좋다.
  - 물론, 구현을 테스트해야 하는 상황도 나온다.



# 데이터 접근 기술 - 스프링 JdbcTemplate

## 소개 및 설정

- SQL을 직접 사용할 경우 유용

- 장점

  - 설정의 편리함
    - `spring-jdbc` 라이브러리에 포함
    - 스프링으로 JDBC 사용 시 기본으로 사용되는 라이브러리, 별도의 복잡한 설정 불필요
  - 반복 문제 해결
    - 템플릿 콜백 패턴을 사용하여, JDBC 직접 사용시 발생하는 대부분의 반복 작업을 대신 처리함
    - 개발자는 SQL 작성, 파라미터 정의, 응답 값 매핑만 하면 됨
    - 처리해주는 반복 작업들
      - 커넥션 획득
      - `statement` 준비 및 실행
      - 결과 반복하도록 루프 실행
      - 커넥션 종료, `statement`, `resultset` 종료
      - 트랜잭션을 다루기 위한 커넥션 동기화
      - 예외 발생 시 스프링 예외 변환기 실행

- 단점

  - 동적 SQL 해결이 어려움

- 설정: gradle 기준으로

  ```groovy
  	implementation 'org.springframework.boot:spring-boot-starter-jdbc'
  ```

  - 이 외의 별도 설정 과정은 없음



## JdbcTemplate 적용 - 기본

- `JdbcTemplateRepositoryV1`

  ```java
  @Slf4j
  public class JdbcTemplateItemRepositoryV1 implements ItemRepository {
  
      private final JdbcTemplate template;
  
      public JdbcTemplateItemRepositoryV1(DataSource dataSource) {
          this.template = new JdbcTemplate(dataSource);
      }
  
      @Override
      public Item save(Item item) {
          String sql = "insert into item(item_name, price, quantity) values(?, ?, ?)";
          KeyHolder keyHolder = new GeneratedKeyHolder();
          template.update(connection -> {
              //자동 증가 키
              PreparedStatement pstmt = connection.prepareStatement(sql, new String[]{"id"});
              pstmt.setString(1, item.getItemName());
              pstmt.setInt(2, item.getPrice());
              pstmt.setInt(3, item.getQuantity());
              return pstmt;
          }, keyHolder);
  
          long key = keyHolder.getKey().longValue();
          item.setId(key);
          return item;
      }
  
      @Override
      public void update(Long itemId, ItemUpdateDto updateParam) {
          String sql = "update item set item_name=?, price=?, quantity=? where id=?";
          template.update(sql,
                  updateParam.getItemName(),
                  updateParam.getPrice(),
                  updateParam.getQuantity(),
                  itemId);
      }
  
      @Override
      public Optional<Item> findById(Long id) {
          String sql = "select id, item_name, price, quantity from item where id = ?";
          try {
              Item item = template.queryForObject(sql, itemRowMapper(), id);
              return Optional.of(item);
          }
          catch (EmptyResultDataAccessException e) {
              return Optional.empty();
          }
      }
  
      @Override
      public List<Item> findAll(ItemSearchCond cond) {
          String itemName = cond.getItemName();
          Integer maxPrice = cond.getMaxPrice();
  
          String sql = "select id, item_name, price, quantity from item";
          //동적 쿼리
          if (StringUtils.hasText(itemName) || maxPrice != null) {
              sql += " where";
          }
  
          boolean andFlag = false;
          List<Object> param = new ArrayList<>();
          if (StringUtils.hasText(itemName)) {
              sql += " item_name like concat('%',?,'%')";
              param.add(itemName);
              andFlag = true;
          }
  
          if (maxPrice != null) {
              if (andFlag) {
                  sql += " and";
              }
              sql += " price <= ?";
              param.add(maxPrice);
          }
  
          log.info("sql = {}", sql);
  
          return template.query(sql, itemRowMapper(), param.toArray());
      }
  
      private RowMapper<Item> itemRowMapper() {
          return ((rs, rowNum) -> {
              Item item = new Item();
              item.setId(rs.getLong("id"));
              item.setItemName(rs.getString("item_name"));
              item.setPrice(rs.getInt("price"));
              item.setQuantity(rs.getInt("quantity"));
              return item;
          });
      }
  }
  ```

  - 생성자에서 `DataSource`를 주입받고, 생성자 내부에서 `JdbcTemplate` 생성함
    - 관례상 많이 사용하는 방식
  - `save()`
    - DB에서 PK값을 생성하기에, `KeyHolder` 이용하여 다소 복잡하게 PK값을 받아야 함
    - JdbcTemplate이 제공하는 `SimpleJdbcInsert` 기능을 이용하여 더 간단하게 처리 가능
  - `itemRowMapper()`
    - JDBC 직접 사용할 때 `ResultSet` 사용하는 부분
    - JdbcTemplate이 루프를 대신 돌려주고, 개발자는 그 내부 내용을 구현해주기만 하면 된다는 차이



## 동적 쿼리 문제

- `findAll`의 경우 `ItemSearchCond`에 따라 4가지 다른 형태의 SQL이 생성됨을 고려해야 함
  - 검색 조건 없음
  - 상품명으로 검색
  - 최대 가격으로 검색
  - 삼품명 && 최대 가격으로 검색
- 각 조건에 따라 어떤 경우에 `WHERE`을 앞에 넣고, `AND`를 넣는지, 상황에 맞춘 파라미터 생성 등 고려할 사항이 많다.
  - 실무에서는 더 복잡한 쿼리를 작성할 일이 많기 때문에 작성 및 관리가 어렵다.
  - `MyBatis`의 경우 SQL을 직접 사용할 때 동적 쿼리를 더 쉽게 작성할 수 있다는 장점을 갖는다.



## 구성과 실행

- `JdbcTemplateV1Config`

  ```java
  @Configuration
  @RequiredArgsConstructor
  public class JdbcTemplateV1Config {
      
      private final DataSource dataSource;
  
      @Bean
      public ItemService itemService() {
          return new ItemServiceV1(itemRepository());
      }
  
      @Bean
      public ItemRepository itemRepository() {
          return new JdbcTemplateItemRepositoryV1(dataSource);
      }
  }
  ```

- `application.properties`

  ```properties
  spring.profiles.active=local
  spring.datasource.url=jdbc:h2:tcp://localhost/~/db2
  spring.datasource.username=sa
  
  #jdbc sql을 로그로 보고 싶을 경우
  logging.level.org.springframework.jdbc=debug
  ```

  

## 이름 지정 파라미터

- 기존 방식으로는 sql의 ? 순서대로 파라미터 바인딩을 해야 했음

  - 이 방식은 필드 추가, 수정 등이 발생하면서 잘못된 파라미터 바인딩이 발생할 수 있음
  - 특히 실무에서는 파라미터의 수가 매우 많기 때문에 이런 문제가 발생할 확률이 높음
  - DB에 잘못된 데이터가 들어가는 버그의 경우, 코드 수정만으로 끝나는 것이 아니라
    DB 데이터도 복구해야 하기 때문에 버그 해결에 사용되는 리소스가 매우 큼
  - 개발을 할 때는 코드를 줄이는 편리함도 중요하지만, **모호함을 제거해서 코드를 명확히 하는 것이 유지보수 관점에서 더욱 중요하다.**

- `NamedParameterJdbcTemplate`

  - 위의 문제를 해결할 수 있는 기능

- `JdbcTemplateItemRepositoryV2`: 이름 지정 파라미터 도입

  ```java
  @Slf4j
  public class JdbcTemplateItemRepositoryV2 implements ItemRepository {
  
      private final NamedParameterJdbcTemplate template;
  
      public JdbcTemplateItemRepositoryV2(DataSource dataSource) {
          this.template = new NamedParameterJdbcTemplate(dataSource);
      }
  
      @Override
      public Item save(Item item) {
          String sql = "insert into item(item_name, price, quantity) values(:itemName, :price, :quantity)";
  
          SqlParameterSource param = new BeanPropertySqlParameterSource(item);
  
          KeyHolder keyHolder = new GeneratedKeyHolder();
          template.update(sql, param, keyHolder);
  
          long key = keyHolder.getKey().longValue();
          item.setId(key);
          return item;
      }
  
      @Override
      public void update(Long itemId, ItemUpdateDto updateParam) {
          String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";
  
          SqlParameterSource param = new MapSqlParameterSource()
                  .addValue("itemName", updateParam.getItemName())
                  .addValue("price", updateParam.getPrice())
                  .addValue("quantity", updateParam.getQuantity())
                  .addValue("id", itemId);
  
          template.update(sql, param);
      }
  
      @Override
      public Optional<Item> findById(Long id) {
          String sql = "select id, item_name, price, quantity from item where id = :id";
          try {
              Map<String, Long> param = Map.of("id", id);
              Item item = template.queryForObject(sql, param, itemRowMapper());
              return Optional.of(item);
          }
          catch (EmptyResultDataAccessException e) {
              return Optional.empty();
          }
      }
  
      @Override
      public List<Item> findAll(ItemSearchCond cond) {
          String itemName = cond.getItemName();
          Integer maxPrice = cond.getMaxPrice();
  
          SqlParameterSource param = new BeanPropertySqlParameterSource(cond);
  
          String sql = "select id, item_name, price, quantity from item";
          //동적 쿼리
          if (StringUtils.hasText(itemName) || maxPrice != null) {
              sql += " where";
          }
  
          boolean andFlag = false;
          if (StringUtils.hasText(itemName)) {
              sql += " item_name like concat('%',:itemName,'%')";
              andFlag = true;
          }
  
          if (maxPrice != null) {
              if (andFlag) {
                  sql += " and";
              }
              sql += " price <= :maxPrice";
          }
  
          log.info("sql = {}", sql);
  
          return template.query(sql, param, itemRowMapper());
      }
  
      private RowMapper<Item> itemRowMapper() {
          return BeanPropertyRowMapper.newInstance(Item.class);
      }
  }
  
  ```

  - `KeyHolder` 이용 간편해졌음

### 파라미터 전달방식: key-value 형태로 전달함

- `Map`을 직접 전달하는 방식

  - `Map.of("id", id)` 등

- `SqlParameterSource` 인터페이스 사용하는 방식

  - `MapSqlParameterSource` 

    - SQL 타입을 지정할 수 있는 등 SQL 특화 기능들 제공

    - 메서드 체이닝을 통해 편리한 사용도 가능

      ```java
              SqlParameterSource param = new MapSqlParameterSource()
                      .addValue("itemName", updateParam.getItemName())
                      .addValue("price", updateParam.getPrice())
                      .addValue("quantity", updateParam.getQuantity())
                      .addValue("id", itemId);
      ```

  - `BeanPropertySqlParameterSource`
    - 자바빈 프로퍼티 규악을 통해 자동으로 파라미터 객체를 생성함(`getFoo()`)
    - 객체를 통째로 담는것 가능: `BeanPropertySqlParameterSource(item)`
    - 편의성이 가장 좋지만, 사용 불가능한 상황이 분명 있음
      - 예를 들어 `update()`메서드의 `ItemUpdateDto`의 경우 바인딩되어야 하는 id값을 가지고 있지 않음



### BeanPropertyRowMapper

- `BeanPropertyRowMapper.newInstance(Item.class)`
- `ResultSet`의 결과를 자바빈 규약에 맞춰서 변환해줌(`setId()`, `setPrice()`등 ...)
- 별칭
  - 만약 DB 컬럼명(username)과 객체 필드명(nickname)이 일치하지 않는 경우?
  - 이 경우 `setUsername()`형태의 메서드가 없음
  - 별칭 지정하게끔 조회 SQL을 수정하여 해결: `select username as nickname,...`
  - DB 컬럼과 객체 이름이 다른 경우 문제 해결
  - MyBatis에서도 종종 사용되는 기법
- 관례 불일치 해결
  - 자바의 경우 `camelCase` 사용, DB의 경우 주로 `snake_case` 사용
  - `BeanPropertyRowMapper`는 위 불일치 해결을 위해 언더스코어 표기법을 카멜로 자동 변환
  - 컬럼명 `item_name`과 필드명 `itemName`(메서드 `getItemName()`)의 불일치는 따라서 수정하지 않아도 괜찮음



## SimpleJdbcInsert

- INSERT SQL을 직접 작성하지 않아도 되게 하는 기능

- `JdbcTemplateItemRepositoryV3`

  ```java
  public class JdbcTemplateItemRepositoryV3 implements ItemRepository {
  
      private final NamedParameterJdbcTemplate template;
      private final SimpleJdbcInsert jdbcInsert;
  
      public JdbcTemplateItemRepositoryV3(DataSource dataSource) {
          this.template = new NamedParameterJdbcTemplate(dataSource);
          this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                  .withTableName("item")
                  .usingGeneratedKeyColumns("id");
  //                .usingColumns("item_name", "price", "quantity");  //생략 가능!
      }
  
      @Override
      public Item save(Item item) {
          SqlParameterSource param = new BeanPropertySqlParameterSource(item);
          Number key = jdbcInsert.executeAndReturnKey(param);
  
          item.setId(key.longValue());
          return item;
      }
  ```

  - `SimpleJdbcInsert`의 경우 빈 등록이 가능하지만 추천하지는 않음
    - 테이블 이름 등을 설정해주기 때문
  - `usingColumns`: 특정 컬럼만 저장하고 싶을 때 사용, 생략 가능
    - 생성 시점에 DB 테이블의 메타데이터를 조회하기 때문에 생략 가능
  - 어플리케이션 실행 시점에 생성된 INSERT 쿼리 내용을 볼 수 있음



## 그 외 기능

