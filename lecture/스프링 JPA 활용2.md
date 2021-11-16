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
  - update는 변경성 메서드기에 추가적으로 쿼리하지는 않는다.
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

  ```java
      @Bean
      Hibernate5Module hibernate5Module() {
          Hibernate5Module hibernate5Module = new Hibernate5Module();
  //        hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
          return hibernate5Module;
      }
  ```

  - 정상적으로 프록시가 초기화된 엔티티들만 반환하게 한다.
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

```java
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();// 새로이 문제되는 컬렉션 조회 부분!
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }
```

- 강제초기화 한 이유: Hibernate5Module의 기본 설정상 프록시 엔티티의 데이터는 뿌리지 않음.
  - 일부러 초기화하여 데이터 뿌리게 하였음.
  - 별개로, 양방향 연관관계의 경우 `@JsonIgnore`잊지 말자.

- 엔티티를 직접 노출하기에 지양해야 하는 방식.



### 주문 조회 V2: 엔티티를 DTO로 변환

```java
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
    }

    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
```

- DTO에 @Data 붙일지, @Getter만 붙여줄지 고민해보자.

- orderItems 필드는 null로 조회되었다. (지연 로딩)
  - 이를 해결하는 가장 무식한 방법: V1처럼 컬렉션의 각 원소들마다 stream.forEach 등 이용하여 강제 초기화해주면 된다.

- DTO로 반환하라는 지침의 명확한 의미는
  - 단순히 엔티티를 DTO로 Wrapping 하라는 뜻이 아니다!
  - DTO 안에 엔티티도 없어야 맞다.
  - DTO에 단순히 Wrapping만 하는 정도로는 엔티티 외부 노출을 막을 수 없다.
  - 완전히 엔티티에 대한 의존을 끊어야 한다!

- 이 방법은 너무 많은 쿼리를 날린다 -> 성능 문제!



### 주문 조회 V3: 엔티티를 DTO로 변환 

#### 페치 조인 최적화

```java
public List<Order> findAllWithItem() {
    return em.createQuery(
            "SELECT o FROM Order o" +
                    " JOIN FETCH o.member m" +
                    " JOIN FETCH o.delivery d" +
                    " JOIN FETCH o.orderItems oi" +
                    " JOIN FETCH oi.item i", Order.class )
            .getResultList();
}
```

- ORDER가 2개가 아니라 4개가 나오게 된다.
- 얻은 값은 중복 PK를 가지게 된다. (즉 컬렉션이 동일 객체를 2개 갖게 된다.)

```java
public List<Order> findAllWithItem() {
    return em.createQuery(
            "SELECT DISTINCT o FROM Order o" +
                    " JOIN FETCH o.member m" +
                    " JOIN FETCH o.delivery d" +
                    " JOIN FETCH o.orderItems oi" +
                    " JOIN FETCH oi.item i", Order.class )
            .getResultList();
}
```

- DISTINCT를 붙인다 해도 DB에서 가져온 결과 자체는 같다.
- 하지만 DISTINCT가 있을 경우 JPA가 자체적으로 (루트 엔티티의) 중복 값을 제거하고 담아 준다.

- V3의 경우 패치 조인 사용으로 바꾸었기 때문에, 결과적으로 쿼리가 단 한번만 나간다.

- 하지만 컬렉션 패치 조인의 경우 페이징이 불가능함에 유의하라!
  - 페이징 설정을 한다고 하더라도 실제 쿼리에서는 LIMIT나 OFFSET같은 페이징 관련 쿼리가 나가지 않는다.
  - 하이버네이트의 경우 메모리에서 페이징 처리를 하게 되는데, 데이터 양이 많다면 당연히 문제가 발생한다.
  - ?:N 관계의 경우 데이터 양이 N쪽에 맞게 늘어나게 되어 적절한 페이징이 불가하기에 페이징 쿼리를 보낼 수 없으며, 그 상황에서 어떻게든 페이징을 하기 위해서 하이버네이트의 경우 이런 방법을 택하는 것이다.

- 컬렉션 페치 조인은 단 하나만 사용 가능하다! 둘 이상에 사용하지 말 것, 데이터 정합성을 깨서 조회할 확률이 높다.



#### 페이징과 한계 돌파

- 컬렉션 엔티티 조회 + 페이징을 위한 방법(V3.1)

  1. 먼저 ToOne 관계를 모두 페치 조회
  2. 컬렉션은 지연 로딩으로 조회

  3. 지연 로딩 성능 최적화를 위해 `hibernate.default_batch_fetch_size`, `@BatchSize`를 적용한다.

   - `hibernate.default_batch_fetch_size`: 글로벌 설정

   - `@BatchSize`: 개별 최적화

   - 컬렉션이나 프록시 객체를 한꺼번에 설정한 size만큼 IN 쿼리로 조회한다.

- hibernate.default_batch_fetch_size를 설정하자, 필요한 정보를 IN 쿼리를 이용하여 한 번에 가져온다.

- - 1 + N + N번의 쿼리가 1 + 1 + 1번의 쿼리로 줄어드는 것!
  
- V3의 경우 쿼리가 한 번에 나가지만 중복된 데이터까지 모두 DB에서 애플리케이션으로 나가게 된다.
  - V3.1의 경우 데이터 자체가 중복 없게 최적화되어 전달된다. (V3보다 쿼리 호출 수는 증가하지만 DB 데이터 전송량은 감소)
  - 데이터 수가 많은 경우 V3.1의 방식이 나을 확률이 높다.
  - V3와 달리 페이징도 가능하다.

- hibernate.default_batch_fetch_size
  - 100~1000개 사이가 좋다.
  - 최소제한은 없지만 DB에 따라 1000으로 IN 파라미터를 제한하기도 한다.
  - 사이즈가 클 수록 DB에 순간 부하가 크게 증가할 수 있다.
  - 메모리의 경우 WAS 입장에서 100개든 1000개든 최종적으로 로딩해야 하는 양 자체는 동일하다.
  - DB나 애플리케이션이나 최대한 견딜 수 있는 부하를 고려하여 사용한다. 높게 설정할 수 있을 수록 좋다.



### 주문조회 V4: JPA에서 DTO 직접 조회

```java
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();

        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "SELECT new cogitans.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " FROM OrderItem oi" +
                        " JOIN oi.item i" +
                        " WHERE oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "SELECT new cogitans.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " FROM Order o" +
                        " JOIN o.member m" +
                        " JOIN o.delivery d", OrderQueryDto.class)
                .getResultList();
    }
}
```

```java
@Data
public class OrderQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
```

```java
@Data
public class OrderItemQueryDto {

    @JsonIgnore
    private Long orderId;

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
```

- 쿼리: 루트(최초 쿼리) 1번 + 컬렉션 N번 실행: N+1 문제
- ToOne 관계들을 먼저 조회하고, ToMany 관계는 각각 별개로 처리한다.
  - ToOne의 경우 조인 최적화가 쉽기에 한 번에 조회
  - ToMany의 경우 최적화가 어려우므로 `findOrderItems()`같은 별도의 메서드로 조회
    - orderItem 입장에서 order는 ToOne 관계이므로 조회해도 문제 없다.



### 주문조회 V5: JPA에서 DTO 직접 조회 - 컬렉션 조회 최적화

```java
public List<OrderQueryDto> findAllByDto_optimization() {
    List<OrderQueryDto> result = findOrders();

    Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

    result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

    return result;
}

private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
    List<OrderItemQueryDto> orderItems = em.createQuery(
            "SELECT new cogitans.jpashop.repository.order.query" +
                    ".OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                    " FROM OrderItem oi" +
                    " JOIN oi.item i" +
                    " WHERE oi.order.id IN :orderIds", OrderItemQueryDto.class)
            .setParameter("orderIds", orderIds)
            .getResultList();

    Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
            .collect(Collectors.groupingBy(OrderItemQueryDto -> OrderItemQueryDto.getOrderId()));
    return orderItemMap;
}

private List<Long> toOrderIds(List<OrderQueryDto> result) {
    List<Long> orderIds = result.stream()
            .map(o -> o.getOrderId())
            .collect(Collectors.toList());
    return orderIds;
}
```

- Query: 루트 1번, 컬렉션 1번
- ToOne 관계들을 먼저 조회하고(V4와 동일), 여기서 얻은 식별자 orderId로 ToMany 관계인 OrderItem을 한꺼번에 조회
- Map을 사용해서 매칭 성능 향상: O(1)

- 페치 조인을 이용하는 방식에 비해 코드 양은 늘지만 SELECT되는 절의 수가 줄어든다.



### 주문조회 V6: JPA에서 DTO로 직접 조회, 플랫 데이터 최적화

```java
public List<OrderFlatDto> findAllByDto_flat() {
    return em.createQuery(
            "SELECT new" +
                    " cogitans.jpashop.repository.order.query.OrderFlatDto" +
                    "(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)"+
                    " FROM Order o" +
                    " JOIN o.member m" +
                    " JOIN o.delivery d" +
                    " JOIN o.orderItems oi" +
                    " JOIN oi.item i", OrderFlatDto.class)
            .getResultList();
}
```

```java
@Data
public class OrderFlatDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
```

- 한 번의 쿼리로 조회하는 방식이다.
- 모조리 몰아넣기 위해서 별개의 DTO를 만들었다.
- 하지만 Json 전달 결과는 이 시점에서는 중복이 발생한다.

```java
@GetMapping("/api/v6/orders")
public List<OrderQueryDto> ordersV6() {
    List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
    return flats.stream()
            .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
                            o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                    mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                            o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
            )).entrySet().stream()
            .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                    e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                    e.getKey().getAddress(), e.getValue()))
            .collect(toList());
}
```

또한 `@EqualsAndHashCode(of = "orderId")`로 OrderQueryDto를 묶어줘야 한다.

- 단점

  - 쿼리는 한 번이지만 조인으로 인해서 DB에서 애플리케이션에 전달하는 데이터에 중복 데이터가 추가되므로 상황에 따라 V5보다 더 느릴 수도 있다.

  - 애플리케이션에서 추가 작업이 크다.

  - 페이징 불가능하다.



### API 개발 고급 정리

**권장 순서**

1. 엔티티 조회 방식으로 우선 접근
   1. 페치 조인으로 쿼리 수를 최적화
   2. 컬렉션 최적화
      1. 페이징 필요 -> `hibernate.default_batch_fetch_size`, `@BatchSize`로 최적화
      2. 페이징이 불필요 -> 페치 조인 사용
2. 엔티티 조회 방식으로 해결이 안 되면 DTO 조회 방식 사용
3. DTO 조회 방식으로 해결이 안 되면 NativeSQL or 스프링 JdbcTemplate

- 참고: 엔티티 조회 방식은 페치 조인이나 `hibernate.default_batch_fetch_size`, `@BatchSize` 같이 코드를 거의 수정하지 않고, 옵션만 약간 변경하여 다양한 성능 최적화를 시도할 수 있다.

  - 반면에 DTO를 직접 조회하는 방식은 성능을 최적화 하거나 그 방식을 변경할 때 많은 코드 변경이 필요하다.

  - 또 DTO 조회를 해야 할 정도의 트래픽 상태의 경우 캐시 조회 등의 방식을 고려하는 편이 낫다.
  - 엔티티는 캐시하지 말 것! 반드시 DTO를 캐시할 것!

