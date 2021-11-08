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
  -  update는 변경성 메서드기에 추가적으로 쿼리하지는 않는다.
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



## API 개발 고급 - 준비

- 등록과 수정은 보통 성능 문제가 발생하지 않는다.

- 주로 문제가 되는 것은 조회다.
