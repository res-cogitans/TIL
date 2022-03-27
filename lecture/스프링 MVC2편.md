# 스프링 MVC 2편 - 백엔드 웹 개발 활용 기술

## 타임리프 - 기본 기능

### 소개

- 유용한 링크
  - 공식 사이트: https://www.thymeleaf.org/
  - 공식 메뉴얼 - 기본 기능: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
  - 공식 메뉴얼 - 스프링 통합: https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html

#### 특징

- 서버사이드 HTML 렌더링(SSR)
- 네츄럴 템플릿(natural templates)
  - 순수 HTML을 최대한 유지
  - 파일 직접 접근 시에도 내용 확인 가능
  - 타 뷰 템플릿(JSP 등)들은 브라우저에서 직접 파일을 열 경우 정상적인 HTML파일 형태로 확인 불가

- 스프링 통합 지원

#### 기본 기능

- 사용 선언 `<html xmlns:th="http://www.thymeleaf.org">`
- 기본 표현식
  - 간단한 표현: 
    - 변수 표현식: ${...} 
    - 선택 변수 표현식: *{...} 
    - 메시지 표현식: #{...} 
    - 링크 URL 표현식: @{...} 
    - 조각 표현식: ~{...} 
  - 리터럴 
    - 텍스트: 'one text', 'Another one!',… 
    - 숫자: 0, 34, 3.0, 12.3,… ◦ 불린: true, false
    - 널: null 
    - 리터럴 토큰: one, sometext, main,…
  - 문자 연산: 
    - 문자 합치기: +
    - 리터럴 대체: |The name is ${name}|
  - 산술 연산: 
    - Binary operators: +, -, *, /, %
    - Minus sign (unary operator): - 
  - • 불린 연산: 
    - Binary operators: and, or 
    - Boolean negation (unary operator): !, not 
  - 비교와 동등: 
    - 비교: >, <, >=, <= (gt, lt, ge, le) 
    - 동등 연산: ==, != (eq, ne) 
  - 조건 연산: 
    - If-then: (if) ? (then) ◦ If-then-else: (if) ? (then) : (else) 
    - Default: (value) ?: (defaultvalue) 
  - 특별한 토큰: ◦ No-Operation: _



### 텍스트 - text, utext

- text
  - `th:text`, `[[...]]`
  - 이스케이프(escape)
    - HTML 문서는 `<`, `>`같은 특수 문자를 기반으로 정의되기 때문에, 뷰 템플릿으로 HTML을 생성할 때는 출력 데이터에 특수문자가 있는지를 주의해서 살펴야 한다.
  - HTML 엔티티
    - `<`, `>`을 태그가 아니라 문자로 표현하기 위해 변경한다(이스케이프). 
    - 변경된 `&lt;``&gt;`를 HTML 엔티티라 한다.
    - 타임리프의 `th:text`, `[[...]]`는 이스케이프를 기본 지원한다.

- utext
  - 이스케이프 기능을 사용하지 않는 방법
  - `th:utext`, `[(...)]`
- 기본적으로 escape를 사용해야 HTML이 정상 렌더링 되지 않는 문제를 피할 수 있다. 특수한 경우가 아니라면 unescape는 사용하지 말자.



### 변수 - SpringEL

- 타임리프에서 변수를 사용할 때는 변수 표현식을 사용
- 변수 표현식:`${...}`
- 다양한 접근법

- - Object

  - - ${user.username} = userA
    - ${user['username']} = userA
    - ${user.getUsername()} = userA

- - List

  - - ${users[0].username} = userA
    - ${users[0]['username']} = userA
    - ${users[0].getUsername()} = userA

- - Map

  - - ${userMap['userA'].username} = userA
    - ${userMap['userA']['username']} = userA
    - ${userMap['userA'].getUsername()} = userA

- 지역 변수

  ```html
  <div th:with="first=${users[0]}">
    <p>처음 사람의 이름은 <span th:text="${first.username}"></span></p>
  </div>
  ```

  선언한 태그 안에서만 사용 가능하다는 점에 주의하라!



### 기본 객체들

- 타임리프가 제공하는 기본 객체들:
  - ${#request}
  - ${#response}
  - ${#session}
  - ${#servletContext}
  - ${#locale}
- 그런데 #request 는 HttpServletRequest 객체가 그대로 제공되기 때문에 데이터를 조회하려면 `request.getParameter("data") `처럼 불편하게 접근해야 한다.
-  이런 점을 해결하기 위해 편의 객체도 제공한다. 
  - HTTP 요청 파라미터 접근: `param`
    -  예) `${param.paramData}`
  - HTTP 세션 접근: `session` 
    - 예) `${session.sessionData} `
    - 세션은 유저가 웹 브라우저를 종료할 때까지는 데이터를 보존한다.
  - 스프링 빈 접근: `@`
    -  예)` ${@helloBean.hello('Spring!')}`
- 출력 결과
  - 식 기본 객체 (Expression Basic Objects)
    - request = org.apache.catalina.connector.RequestFacade@56f4464
    - response = org.apache.catalina.connector.ResponseFacade@4aec6e59
    - session = org.apache.catalina.session.StandardSessionFacade@7cbfb90c
    - servletContext = org.apache.catalina.core.ApplicationContextFacade@359323a6
    - locale = ko_KR
  - 편의 객체
    - Request Parameter = HelloParam
    - session = Hello, Session!
    - spring bean = HelloSpring!



### 유틸리티 객체와 날짜

#### 타임리프 유틸리티 객체들

- 목록

  - #message : 메시지, 국제화 처리 

  - #uris : URI 이스케이프 지원 

  - #dates : java.util.Date 서식 지원 

  - #calendars : java.util.Calendar 서식 지원 

  - #temporals : 자바8 날짜 서식 지원 

  - #numbers : 숫자 서식 지원 #strings : 문자 관련 편의 기능 

  - #objects : 객체 관련 기능 제공 #bools : boolean 관련 기능 제공 

  - #arrays : 배열 관련 기능 제공 

  - #lists , #sets , #maps : 컬렉션 관련 기능 제공 

  - #ids : 아이디 처리 관련 기능 제공, 뒤에서 설명

- 링크
  - 타임리프 유틸리티 객체 https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#expression-utilityobjects 
  - 유틸리티 객체 예시 https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#appendix-b-expressionutility-objects

- 자바8 날짜

  - 타임리프에서 자바8 날짜인 LocalDate , LocalDateTime , Instant 를 사용하려면 추가 라이브러리가 필요하다.

    스프링 부트 타임리프를 사용하면 해당 라이브러리가 자동으로 추가되고 통합된다.

  - 타임리프 자바8 날짜 지원 라이브러리: `thymeleaf-extras-java8time`

  - 자바8 날짜용 유틸리티 객체 #temporals 사용 예시

    ```html
    <span th:text="${#temporals.format(localDateTime, 'yyyy-MM-dd HH:mm:ss')}"></span>
    ```



### URL 링크

- 단순한 URL
  - @{/hello}
  - /hello
- 쿼리 파라미터
  - @{/hello(param1=${param1}, param2=${param2})}
  - /hello?param1=data1&param2=data
  -  () 에 있는 부분은 쿼리 파라미터로 처리된다.
- 경로 변수 @{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})}
  - /hello/data1/data2
  - URL 경로상에 변수가 있으면 () 부분은 경로 변수로 처리된다.
- 경로 변수 + 쿼리 파라미터
  - @{/hello/{param1}(param1=${param1}, param2=${param2})}
  - /hello/data1?param2=data2
  - 경로 변수와 쿼리 파라미터를 함께 사용할 수 있다.

- 상대경로, 절대경로, 프로토콜 기준을 표현할 수 도 있다.
  - /hello : 절대 경로
  - hello : 상대 경로
  - 참고: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#link-urls



### 리터럴

- 타임리프의 리터럴
  - 문자: 'hello'
  - 숫자: 10
  - 불린: true , false
  - null: null
- 타임리프에서 문자 리터럴은 항상 ' (작은 따옴표)로 감싸야 한다. 
- 공백 없이 쭉 이어진다면 하나의 의미있는 토큰으로 인지해서 다음과 같이 작은 따옴표를 생략할 수 있다. 
  -  A-Z , a-z , 0-9 , [] , . , - , _
- 오류
  - `<span th:text="hello world!"></span>`
  - 문자 리터럴은 원칙상 ' 로 감싸야 한다. 중간에 공백이 있어서 하나의 의미있는 토큰으로도 인식되지 않는다.
  - 수정
    - `<span th:text="'hello world!'"></span>`
    - 이렇게 ' 로 감싸면 정상 동작한다.

- 리터럴 대체(Literal substitutions)
  - `<span th:text="|hello ${data}|">`



### 연산

- 비교연산: HTML 엔티티를 사용해야 하는 부분을 주의
- No-Operation: _ 인 경우 마치 타임리프가 실행되지 않는 것 처럼 동작한다. 이것을 잘 사용하면 HTML 의 내용 그대로 활용할 수 있다.



### 속성 값 설정

- 속성 설정
  - th:
  - `*`속성을 지정하면 타임리프는 기존 속성을 th:* 로 지정한 속성으로 대체한다. 기존 속성이 없다면 새로 만든다.
- 속성 추가
  - th:attrappend : 속성 값의 뒤에 값을 추가한다.
  - th:attrprepend : 속성 값의 앞에 값을 추가한다.
  - th:classappend : class 속성에 자연스럽게 추가한다.
- checked 처리
  - 타임리프의 th:checked 는 값이 false 인 경우 checked 속성 자체를 제거한다.



### 반복

- th:each

- 반복

  `<tr th:each="user : ${users}">`

  - 오른쪽 컬렉션의 값을 하나씩 꺼내서 왼쪽 변수에 담고, 태그 반복 실행
  - Iterable, Enumeration 구현한 것들 모두 사용 가능, Map의 경우 Map.Entry가 변수에 담김

- 반복 상태 유지
  - `<tr th:each="user, userStat : ${users}">`
  - 두 번째 파라미터는 반복의 상태를 보여준다.
  - 두 번째 파라미터는 생략 가능하며, 생략시에는 지정 변수명 + Stat이 된다.
  - 기능
    - index : 0부터 시작하는 값
    - count : 1부터 시작하는 값
    - size : 전체 사이즈
    - even , odd : 홀수, 짝수 여부( boolean )
    - first , last :처음, 마지막 여부( boolean )
    - current : 현재 객체



### 조건부 평가

- if, unless(if의 반대)
  - 조건을 충족하지 않으면 태그 자체가 사라지게 된다.
- switch ~ case
  - *은 default



### 주석

1. 표준 HTML 주석

2. 타임리프 파서 주석

   - 렌더링시 제거된다.

3. 타임리프 프로토타입 주석

   - 서버사이드에서 타임리프 렌더링을 거칠 경우 정상 렌더링된다.

   

### 블록

- `th:block`: 타임리프의 유일한 자체 태그 (HTML 태그 아님!)
- 타임리프는 태그 내 속성으로 사용되는데, 이로 인해서 생기는 문제를 해결해줌
- 예를 들어, `th:each` 반복 한 바퀴당 두 가지 출력을 하고 싶은 경우, 블록 단위로 묶는 것이 간단하게 문제를 해결해 준다.



### 자바스크립트 인라인

- 인라인 사용 시 문법적인 불편함을 해소해 준다. (텍스트 렌더링)
  - `var username = [[${user.username}]];`의 username부분에 ""를 붙여 준다. (문자)
  - 또한 자바스크립트에서 문제가 될 수 있는 문자가 포함되어 있을 경우 이스케이프 처리해 준다.
  - 인라인 사용 안 하고 `<script>` 태그 이용할 경우, 일일히 자료형에 맞게 ""을 붙이거나, 빼거나 하면서 조절해줘야 했다.

- 자바스크립트 네추럴 템플릿
  - `var username2 = /*[[${user.username}]]*/ "test username";`
  - 변수 할당 부분에 주석 /* */ 을 붙여주면, 렌더링 되지 않았을 때의 값("test username") 기본값으로 할당하면서, 렌더링 될 경우 그 값이 지워지는 방식으로 사용 가능하다.
  - 실제 HTML 파일 실행해도 기본 값 넣은 형태로 볼 수 있다.

- 객체
  - var user = [[${user}]];
  - 객체를 Json으로 마셜링 해서 사용 가능

- 자바스크립트 인라인 each

  ```javascript
  <!-- 자바스크립트 인라인 each -->
  <script th:inline="javascript">
  
      [# th:each="user, stat : ${users}"]
      var user[[${stat.count}]] = [[${user}]];
      [/]
      
  </script>
  ```




### 템플릿 조각

- 상, 하단 영역, 좌측 카테고리 등 공통 영역 재사용을 위한 기능: 템플릿 조각, 레이아웃

- `template/fragment/footer :: copy` : `template/fragment/footer.html` 템플릿에 있는 `th:fragment="copy`" 라는 부분을 템플릿 조각으로 가져와서 사용한다는 의미이다.
  - 마치 메서드를 호출해 사용하듯이, 특정 태그를 반복적으로 호출해 사용할 수 있다.

- **th:insert** 를 사용하면 현재 태그( div ) 내부에 추가한다.
- **th:replace**는 현재 태그를 교체한다.
  - 단순한 경우에는 '~' 없이 간단하게 사용 가능하다. (단순 표현식)

- 파라미터 사용

  - 파라미터를 사용해서 동적으로 렌더링이 가능하다.

    ```html
    <div th:replace="~{template/fragment/footer :: copyParam ('데이터1', '데이터2')}"></
    div>
    ```

  - 다음의 copyParam을 호출한 것이다:

    ```html
    <footer th:fragment="copyParam (param1, param2)">
     <p>파라미터 자리 입니다.</p>
     <p th:text="${param1}"></p>
     <p th:text="${param2}"></p>
    </footer>
    ```

    

### 템플릿 레이아웃

- 템플릿 조각을 레이아웃에 넘겨 사용하는 방식

- `common_header(~{::title},~{::link})` 이 부분이 핵심이다.
  - `::title` 은 현재 페이지의 title 태그들을 전달한다.
  - `::link` 는 현재 페이지의 link 태그들을 전달한다.

- 전달한 title/link가 교체되었고, 공통 부분이 유지되었다.



#### 템플릿 레이아웃 확장

- 위의 템플릿 레이아웃을 단순히 `<head>`에만이 아니라 `<html>` 전체에 거쳐 적용하는 것이 가능하다.

- 레이아웃을 이용하여 페이지 전체 스타일을 설정한다:

  - 템플릿 조각을 이용하여 컨텐츠 부분을 인자처럼 전달한다.

    `th:fragment="layout (title, content)`에 `th:replace="~{template/layoutExtend/layoutFile :: layout(~{::title},~{::section})}"`을 전달하는 방식

- `layoutExtendMain.html` 는 현재 페이지인데,  자체를 `th:replace` 를 사용해서 변경하는 것을 확인할 수 있다. 결국 `layoutFile.html` 에 필요한 내용을 전달하면서  자체를 `layoutFile.html` 로 변경한다



## 타임리프 - 스프링 통합과 폼

### 타임리프 스프링 통합

- 기본 메뉴얼: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html

- 스프링 통합 메뉴얼: https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html
- 설정 방법
  - 타임리프 템플릿 엔진을 스프링 빈에 등록하고, 타임리프용 뷰 리졸버를 스프링 빈으로 등록하는 방법
    - https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#the-springstandarddialect
    - https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#views-and-viewresolvers
  - 스프링 부트의 경우, build.gradle에 `implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'`추가로 충분하다.

- 타임리프 설정 변경

  - https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties.templating

    에서 thymeleaf 검색



### 입력 폼 처리

- th:object : 커맨드 객체를 지정한다.
- *{...} 
  - 선택 변수 식이라 한다.
  - th:object 에서 선택한 객체에 접근한다.
- th:field: HTML 태그의 id , name , value 속성을 자동으로 처리해준다.

- 특히 이는 검증에서 더 유용!



### 체크박스

#### 체크박스 - 단일

- 체크박스의 한계 -> 체크가 안 되어 있으면 서버로 데이터 자체를 보내지 않음

- `이를 application.properties`의 `logging.level.org.apache.coyote.http11=debug`로 확인해보면

  - 체크박스에 체크했을 경우

    `itemName=itemWithOpenOption&price=15000&quantity=20&open=on]`라고 open 값이 전달되지만

  - 체크하지 않았을 경우,

    `itemName=noOpenItem&price=1000&quantity=50]`처럼 open 값 자체가 전달되지 않는다.

  - 로그로 item.open 여부를 찾아 볼 경우 null이 나온다.

    - 다만, Boolean이 아니라 boolean 사용할 경우 false 나온다(기본값): 헷갈리지 말 것

      `2022-01-14 04:20:33.911  INFO 23480 --- [io-8080-exec-10] h.i.web.form.FormItemController          : item.open=false`

    - boolean을 사용한다고 해도 문제가 되는 점은
      - 만일 체크박스를 해제하고 변경사항을 서버로 보낸다고 하더라도 전달이 안 된다는 점이다.

- 이를 해결하기 위한 방법: 히든 필드

  ```html
  <input type="hidden" name="_open" value="on"/>
  ```

  - _open 값이 넘어가게 한다. _open 값이 넘어갔을 경우 스프링 MVC가 open의 값이 체크되지 않았음을 인식한다.
  - null이 아니라 false로 인식하는 것을 확인할 수 있음 -> 문제 해결

https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/view.html 의 checkbox 부분

https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/tags/form/CheckboxTag.html



#### 타임리프의 체크박스

- `<input type="checkbox" id="open" th:field="*{open}" class="form-check-input">`

  - 이 상태로 html 렌더링 해보면, 히든 필드를 알아서 추가해 줬다.

  - 소스코드를 보면 아래와 같이 checked가 들어가있다: 원래는 이 html에 checked를 개발자가 붙여줘야 한다.

    `<input type="checkbox" id="open" class="form-check-input" disabled name="open" value="true" checked="checked">`

- 그런데, editForm을 같은 방식으로 편집하고 테스트 해보면, 변경사항이 반영되지 않음을 알 수 있다:
  - 이런 상황에는 컨트롤러, 서비스, repo 단까지 차근차근 내려가면서 무엇이 반영되지 않아서 문제인가를 살펴야 한다.
  - 이 상황에는 repo의 update에 open 속성 변경이 반영되지 않아서이니 이를 수정해준다.



#### 체크박스 - 멀티

- 지역 값을 저장해준다.

  - ```java
    Map<String, String> regions = new LinkedHashMap<>();
    regions.put("SEOUL", "서울");
    regions.put("BUSAN", "부산");
    regions.put("JEJU", "제주");
    model.addAttribute("regions", regions);
    ```

  - `LinkedHashMap` 사용한 이유: 순서 보장을 위함

  - 하지만 위 코드를 등록, 조회 등 여러 곳에 중복해서 사용하게 된다.

  - 중복을 줄이기 위해

    ```java
        @ModelAttribute("region")
        public Map<String, String> regions() {
            Map<String, String> regions = new LinkedHashMap<>();
            regions.put("SEOUL", "서울");
            regions.put("BUSAN", "부산");
            regions.put("JEJU", "제주");
            return regions;
        }
    
    ```

    - 이 경우 이 컨트롤러가 호출될 때마다 위를 Model에 담는다.
    - 이는 지금까지 사용했던 `ModelAttribute`와는 다른 기능임에 주의하라.
      - `@ModelAttribute`는 컨트롤러에 있는 별도에 메서드에 적용하여 위와 같이 사용할 수 있다!
      - 반복적으로 사용해야 하는 데이터가 있을 때 중복을 줄이기 위한 방법이다.

- id문제

  ```html
  th:for="${#ids.prev('regions')}" 
  ```

  - 멀티 체크박스는 같은 이름의 여러 체크박스를 만들 수 있다.
  - 그런데 문제는 이렇게 반복해서 HTML 태그를 생성할 때, 생성된 HTML 태그 속성에서 name 은 같아도 되지만, id 는 모두 달라야 한다.
  - 따라서 타임리프는 체크박스를 each 루프 안에서 반복해서 만들 때 임의로 1 , 2 , 3 숫자를 뒤에 붙여준다.

- addItem 말고 item.html에도 적용할 때는
  - `<input type="checkbox" th:field="${item.regions}" th:value="${region.key}" class="form-check-input">` 로 변경한다.
  - item.html의 경우 th:object 안 썼기 때문



### 라디오버튼

```html
        <!-- radio button -->
        <div>
            <div>상품 종류</div>
            <div th:each="type : ${itemTypes}" class="form-check form-check-inline">
                <input type="radio" th:field="*{itemType}" th:value="${type.name()}" class="form-check-input">
                <label th:for="${#ids.prev('itemType')}" th:text="${type.description}" class="form-check-label">
                    BOOK
                </label>
            </div>
        </div>
```



- 체크박스와 달리 라디오버튼은 히든 필드를 갖지 않는다.
  - 라디오 버튼의 특성상 한 번 체크한 이후 변경한다 하더라도 특정 값이 들어가게 될 테니까 그렇다.



#### 타임리프의 ENUM 직접 접근

```html
            <div th:each="type : ${T(hello.itemservice.domain.item.ItemType).values()}" class="form-check form-check-inline">
```

위와 같은 방식으로 변경도 가능하다.

- 이 방식을 사용할 경우 모델에 담지 않아도 되지만,

  타임리프에서 절대경로까지 모두 접근해야 하기 때문에 매우 너저분하다.



### 셀렉트 박스

```html
        <!-- SELECT -->
        <div>
            <div>배송 방식</div>
            <select th:field="*{deliveryCode}" class="form-select">
                <option value="">==배송 방식 선택==</option>
                <option th:each="deliveryCode : ${deliveryCodes}" th:value="${deliveryCode.code}"
                        th:text="${deliveryCode.displayName}">FAST</option>
            </select>
        </div>

```



## 메시지, 국제화

- 사례: "상품명" -> "상품 이름"으로 변경하라는 요구사항이 주어질 경우?
  - HTML 파일에 하드코딩된 "상품명" 메시지가 하드코딩되어 있기 때문에 일일히 변경해줘야 한다.
  - -> 다양한 메시지를 한 곳에서 관리하도록 하는 **메세지**  관리 기능이 필요

- **국제화**
  - 메시지 파일(`messages.properties`)을 국가 별로 관리하는 것
  - `messages_en`과 `messages_ko` 두 버전을 관리하는 방식
  - HTTP의 `accept-language` 헤더를 이용하거나, 사용자의 직접 설정으로 국가 정보를 인식, 쿠키 등을 이용하여 처리한다.
  - 스프링은 메시지, 국제화 기능을 제공하며, 타임리프는 스프링의 메시지, 국제화 기능을 통합 제공한다.



### 스프링 메시지 소스 설정

- `MessageSource`를 스프링 빈으로 등록하여 사용

- `MessageSource`는 인터페이스이기에 구현체인 `ResourceBundleMessageSource`를 빈에 등록하라. (가장 기본적인 구현체)

- 등록 예시

  - 직접 등록

    ```java
    	@Bean
    	public MessageSource messageSource() {
    		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    		messageSource.setBasenames("messages", "errors");
    		messageSource.setDefaultEncoding("utf-8");
    		return messageSource;
    	}
    ```

  - 스프링부트 사용

    - 스프링부트를 사용할 경우 `MessageSource` 자동등록됨
    - 메시지 소스 설정 방법
      - `application.properties`에서 `spring.messages.basename=messages,config.i18n.messages`
      - 단 기본값은  `spring.messages.basename=messages`이다.
        - 국제화 적용시 `messages_en.properties , messages_ko.properties , messages.properties`이 자동적으로 등록됨

    - 구체적인 설정 변경의 경우 스프링부트 메뉴얼의 application.properties 관련 내용을 찾아 보면 된다.



### 스프링 메시지 소스 사용

- `messageSource.getMessage(code, args, locale)`
- `messageSource.getMessage(code, args, defaultMessage, locale)`

- 지정한 메시지 코드가 없을 경우 `NoSuchMessageException`이 발생

  ```java
      @Test
      void notFoundMessageCode() {
          assertThatThrownBy( () -> ms.getMessage("no_code", null, null))
                  .isInstanceOf(NoSuchMessageException.class);
      }
  ```

- 위의 예외를 예방하기 위해 기본 메시지를 줄 수 있다.

  ```java
          String result = ms.getMessage("no_code", null, "기본 메시지", null);
  ```

- 인자를 사용하는 경우

  ```java
          String result = ms.getMessage("hello.name", new Object[]{"spring"}, null);
  ```

- 로케일 설정을 이용한 국제화

  ```java
          assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
          assertThat(ms.getMessage("hello", null, Locale.FRANCE)).isEqualTo("안녕");	// 프랑스어 국제화 메시지 소스가 없기 때문에 기본값
          assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
  ```



### 웹 어플리케이션에 적용하기

#### 메시지 적용

- 타임리프 메시지 적용
  - `#{...}`
  - 예시: `#{label.item}`

- 파라미터 용례
  - `hello.name=안녕 {0}`
  - `<p th:text="#{hello.name(${item.itemName})}"></p>`



#### 국제화 적용

- 크롬 설정에서 언어 변경을 통해 테스트 가능 (accept-language 변경)

- Locale에 따라 스프링에서 메시지 선택됨 (ms.getMessage 참고)

- Locale 선택방식에는 `LocaleResolver` 인터페이스가 관여
  - 스프링 부트는 기본적으로 Accept-Language를 활용하는 `AcceptHeaderLocaleResolver`를 사용한다.
  - 쿠키나 세션 기반으로 Locale 선택방식을 바꾸고 싶다면 이를 손대면 된다.



## 검증 - Validation

- 잘못된 입력을 막아야 한다.
- 잘못된 입력이 들어왔을 때 에러페이지로 바로 넘어가지 않고 적절히 처리해야 한다.
- **컨트롤러의 중요한 역할 중 하나는 HTTP 요청이 정상인지 검증하는 것이다.**

- **클라이언트 검증과 서버 검증**
  - 클라이언트 검증(주로 자바스크립트)은 조작 가능하기에 보안에 취약
  - 서버만으로 검증하면, 즉각적인 고객 사용성이 부족
  - 둘을 적절히 섞어서 사용하며, 최종적으로 서버 검증을 필수적으로 한다.
  - API 방식일 경우 API 스펙을 잘 정의하여, 검증 오류를 API 응답 결과에 잘 넘겨줘야 한다.



### V1: 검증 직접 처리

- 구현 방식 정리
  - 검증 오류가 발생할 경우 입력 폼을 다시 보여준다.
  - 검증 오류에 대해 고객에게 안내하여 재입력할 수 있게 한다.
  - 검증 오류가 발생해도 이미 입력한 데이터가 유지된다.

- Map에 어떤 검증에서 어떤 오류가 발생했는지 담아둔다.

  ```java
          Map<String, String> errors = new HashMap<>();
  
          // 검증 로직
          if (!StringUtils.hasText(item.getItemName())) {
              errors.put("itemName", "상품 이름은 필수입니다.");
          }
  ```

- 뷰에서 위를 사용하여 필드별/혹은 글로벌 에러를 출력한다.

  ```html
          <div>
              <label for="itemName" th:text="#{label.item.itemName}">상품명</label>
              <input type="text" id="itemName" th:field="*{itemName}"
                     th:class="${errors?.containsKey('itemName')} ? 'form-control field-error' : 'form-control'"
                     class="form-control" placeholder="이름을 입력하세요">
              <div class="field-error" th:if="${errors?.containsKey('itemName')}" th:text="${errors['itemName']}">
                  상품명 오류
              </div>
          </div>
  ```

  - 에러 메시지를 더 명확히 드러내기 위해서 해당 필드에 에러가 존재할 경우 `field-error`와 같은 에러 전용 클래스로 분류, CSS를 별도 적용한다(구체적인 CSS는 이 정리에서는 생략하였다).
  - **Safe Navigation Operator**
    - Spring EL 문법
    - errors가 null인 경우를 감안
      - 만일 null일 경우 `.containsKey` 호출시 NPE
    - `errors?.`는 null일 경우 NPE 발생 안 시키고 그냥 null을 반환하게 한다.

- 필드 오류 처리

  - 방법1-`th:class`

    ```html
                <input type="text" id="itemName" th:field="*{itemName}"
                       th:class="${errors?.containsKey('itemName')} ? 'form-control field-error' : 'form-control'"
                       class="form-control" placeholder="이름을 입력하세요">
    ```

    `th:class`에 ? 연산자를 사용하여 오류용 강조 클래스로 설정할 것인지, 일반적인 폼 클래스로 설정할 것인지를 결정

  - 방법2-`th:classappend`

    ```html
    <input type="text" th:classappend="${errors?.containsKey('itemName')} ? 'field-error' :  _"
           class="form-control">
    ```

    `th:append`이용하여 위와 같이 보다 간소하게 표현할 수 있음

    값이 없을 경우 '_' 연산자를 이용해서 아무 것도 하지 않는다.

#### 문제점

- 뷰 템플릿에 중복이 너무 많음
- 타입 오류 처리가 필요함
  - Integer에 Long이나 문자가 들어오면 오류 발생
  - 스프링 MVC 컨트롤러에 진입하기도 전에 예외가 발생하기에 컨트롤러 호출도 없이 400에러가 발생하여 고객에게 오류 페이지를 띄운다.
- 위의 이유로 타입 오류 발생해도 입력한 것이 남지 않는다.
  - 컨트롤러가 호출된다 하더라도 타입 불일치로 인해 값을 보관하지 못한다
  - 이로 인해 고객이 오류가 발생한 지점을 파악하지 못 한다.



### V2: 스프링이 제공하는 방식

#### BindingResult

- BindingResult로 V1의 errors 대체
  - `bindingResult.addError(new FieldError())`
  - 필드 에러의 경우 `FieldError`, 글로벌 에러의 경우 `ObjectError`

- `@ModelAttribute`와 `BindingResult`의 순서

  ```java
  public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
      ...
  }
  ```

  - **`bindingResult`는 검증할 대상 바로 다음에 와야 한다.**

- FieldError
  - `extends ObjectError`
  - `FieldError(String objectName, String field, String defaultMessage)`
    - `objectName`: `@ModelAttribute` 이름: 오류가 발생한 객체 이름
    - `field`: 오류가 발생한 필드 이름
    - `defaultMessage`: 오류 기본 메시지



##### 타임리프 스프링 검증 오류 통합 기능

- 타임리프는 스프링의 `BindingResult`를 이용, 검증 오류를 간편하게 표현하는 기능 제공
- `#fields`: `BindingResult`가 제공하는 검증 오류에 접근 가능
- `th:errors`: 해당 필드에 오류가 있는 경우에 태그를 출력
- `th:errorclass`: `th:field`에서 지정한 필드에 오류가 있으면 `class` 정보를 추가한다.



#### BindingResult: 세부사항

- 스프링이 제공하는 검증 오류를 보관하는 객체
- `BindingResult` 있을 경우, `@ModelAttribute`에 데이터 바인딩 시 오류 발생해도 컨트롤러가 호출된다.
  - 기존 직접 구현 방식의 경우 타입 오류가 발생하는 경우 컨트롤러로 넘어가지 않고 400 오류가 발생했음

- **`BindingResult`에 검증 오류를 적용하는 3가지 방법**
  1. 바인딩 실패 시에 스프링이 `FieldError` 생성하여 `BindingResult`에 넣어주는 방식
  2. 개발자가 직접 넣음
  3. `Validator` 사용

- `BindingResult`는 모델에 자동 포함된다.

- `BindingResult`는 `Errors`를 상속
  - `BindingResult`는 `Errors`보다 많은 기능 제공
  - 둘 다 인터페이스로, 실제로는 `BeanPropertyBindingResult`가 구현



### FieldError, ObjectError

- 현 방식에서는 잘못된 입력 값이 날아가버리는데, 이를 수정해본다.

  ```java
  bindingResult.addError(new FieldError(
          "item", "itemName", item.getItemName(),
          false, null, null,  "상품 이름은 필수입니다."));
  ```

- FieldError 생성자

  ```java 
  public FieldError(String objectName, String field, String defaultMessage)
  public FieldError(String objectName, String field, @Nullable Object rejectedValue,
                    boolean bindingFailure, @nullable String[] codes, 
                    @Nullable Object[] arguments, @Nullable String defaultMessage)
  ```

  - `rejectedValue`: 사용자가 입력한, 거절된 값
  - `bindingFailure`: 타입 오류 같은 바인딩 실패인지, 검증 실패인지를 구별
  - `codes`: 메시지 코드
  - `arguments`: 메시지에서 사용하는 인자

- 입력 데이터가 컨트롤러에 `@ModelAttribute`에 바인딩되는 시점에 오류 발생시 입력 값을 보관하기 위한 수단이다. (Integer 필드에 String 값이 들어오는 등) 
  - 컨트롤러(의 `@ModelAttribute`)에 값을 넘기는 중에 문제 발생시 `FieldError`가 생성

- 타임리프의 `th:field`의 경우
  - 정상 상황에는 모델 객체의 값을 사용하지만
  - 오류가 발생하면 `FieldError`에서 보관한 값을 사용하여 값을 출력한다.

- 스프링의 바인딩 오류 처리
  - 타입 오류로 바인딩에 실패할 경우
  - `FieldError` 생성, 값을 넣어두고 이 오류를 `BindingResult`에 담아서 컨트롤러를 호출한다.
  - 따라서 타입 오류 시에도 400 에러 페이지로 넘어가지 않고 오류 메시지에 대한 설명을 띄울 수 있음.



### 오류 코드와 메시지 처리

- 오류 이름도 일관성을 갖는 것이 좋음 -> 메시지

- 기존의 messages로도 오류 코드에 메시지를 적용할 수는 있으나, 오류 코드 메시지와 구별을 위하여 별도의 `errors.properties` 파일을 만드는 것이 권장된다.

  - 기본 설정 파일인 `messages.properties`외에 추가적으로 `errors.properties`를 포함시키기 위해서 `application.properties`에 다음을 추가한다:

    `spring.messages.basename=messages,errors`

  - `errors.properties`

    ```java
    required.item.itemName=상품 이름은 필수입니다.
    range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
    max.item.quantity=수량은 최대 {0} 까지 허용합니다.
    totalPriceMin=가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}원
    ```

- 오류 생성자의 `codes`에 메시지를 `arguments`에 인자를 넘기는 방식이다.

  - `codes`는 `String[]`이며 `arguments`는 `Object[]`임에 유의하라:

    ```java
    new FieldError("item", "price", item.getPrice(),
                    false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
    ```

#### ObjectError 사용하지 않는 방식

- `FieldError` 및 `ObjectError` 생성자는 너무 많은 인자를 요구하기에 다루기 까탈스러움
- 컨트롤러의 `BindingResult`의 경우 검증 대상이 되는 객체인 `target` 바로 다음에 옴.
  - 즉, `BindingResult`는 본인이 검증해야 할 객체(`target`)을 알고 있음

##### `rejectValue()`, `reject()` (`BindingResult`의 메서드)

- 파라미터 없는 코드 갖는 필드

  ```java
              bindingResult.rejectValue("itemName", "required");
  ```

- 파라미터 있는 코드 갖는 필드

  ```java
  bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
  ```

- 파라미터 있는 글로벌

  ```java
  bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
  ```

- `rejectValue()`

  ```java
  void rejectValue(@Nullable String field, String errorCode,
                  @Nullable Object[] errorArgs, @Nullable String defaultMessage);
  ```

  - `field`: 오류 필드명
  - `errorCode`: 오류 코드(메시지에 등록한 코드가 아닌, messageResolver를 위한 코드)
  - `errorArgs`: 오류 메시지에서 `{0}`를 치환하기 위한 값
  - `defaultMessage`



#### 오류 코드의 설계/단계

- 두 `errors.properties` 를 보라:

  1. ```java
     required.item.itemName=상품 이름은 필수입니다.
     range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
     max.item.quantity=수량은 최대 {0} 까지 허용합니다.
     ```

  2. ```java
     required=필수 값 입니다.
     range=범위는 {0} ~ {1} 까지 허용합니다.
     max=최대 {0} 까지 허용합니다.
     ```

- 1과 같은 구체적인 메시지는 명확하게 정보를 전달하는 반면, 2에 비해 범용성이 떨어진다.

- **기본적으로는 2와 같이 범용성을 갖는 방식을 사용하지만, 구체적인 설명이 필요한 경우에는 1과 같이 더 세밀하게 작성하는 것이 좋다.**

  **즉 구체성에 따라 메시지 단계를 나눠 사용함이 좋다!**

- 예를 들어 `required.item.itemName`이라는 가장 구체적인 코드를 먼저 찾고, 그게 없을 경우에 `required`라는 덜 구체적인 메시지를 찾는 식으로 단계별 적용이 가능하게 개발한다면 변경 및 단계별 적용에 용이할 것이다: `MessageCodesResolver`



#### MessageCodesResolver

- 검증 오류 코드로 메시지 코드들을 생성한다.

- `MessageCodesResolver`는 인터페이스, `DefaultMessageCodesResolver`가 그 구현체

- 예시 코드

  ```java
  public class MessageCodesResolverTest {
  
      MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();
  
      @Test
      void messageCodesResolverObject() {
          String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
          for (String messageCode : messageCodes) {
              System.out.println("messageCode = " + messageCode);
          }
          assertThat(messageCodes).containsExactly("required.item", "required");
      }
  
      @Test
      void messageCodesResolverField() {
          String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
          for (String messageCode : messageCodes) {
              System.out.println("messageCode = " + messageCode);
          }
          assertThat(messageCodes).containsExactly(
                  "required.item.itemName",
                  "required.itemName",
                  "required.java.lang.String",
                  "required"
          );
      }
  }
  
  ```

  - `rejectValue` 메서드 등은 내부적으로  `MessageCodesResolver` 사용하여 위와 같은 `String[]` (우선순위 순서: 구체적인 것부터) 정렬된 메시지 코드 배열을 얻는 것이다.

  

##### `DefaultMessageCodesResolver`의 기본 메시지 생성 규칙

- **`ObjectError`**: 규칙(예시) 
  1. `code.objectName (required.item)`
  2. `code (required)`

- **`FieldError`**: 규칙(예시)
  1. `code.objectName.field (typeMismatch.user.age)`
  2. `code.field (typeMismatch.age)`
  3. `code.fieldType (typeMismatch.int)`
  4. `code (typeMismatch)`



##### 동작 방식

- `rejectValue()`나 `reject()`는 `MessageCodesResolver`를 내부적으로 사용하여 메시지 코드를 생성

- `MessageCodesResolver`를 통해 생성된 우선 순위대로 오류 코드들을 보관

  (`FieldError`와 `ObjectError`의 생성자를 보면 `codes`는 `String[]`타입, 즉 여러 값을 받는다.)



#### ValidationUtils

- 아래 두 코드는 동일한 기능을 수행한다:

  - ```java
    if (!StringUtils.hasText(item.getItemName())) {
        bindingResult.rejectValue("itemName", "required");
    }
    ```

  - ```java
    ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
    ```

- 간단한 공백 등을 검사할 때 유용하게 사용 가능: 자주 쓰이는 if문 등을 미리 정의해두었음.



#### 바인딩 오류

- 검증 오류 코드의 두 종류
  1. 개발자가 직접 설정한 오류 코드: `rejectValue()`를 직접 호출
  2. 스프링이 직접 검증 오류에 추가한 경우(주로 타입 불일치)

- 잘못된 타입의 값을 폼에 입력해보면:

  - ```
    Field error in object 'item' on field 'price': rejected value [qqq]; 
    codes [typeMismatch.item.price,typeMismatch.price,typeMismatch.java.lang.Integer,typeMismatch]; 
    arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [item.price,price]; arguments [];
    default message [price]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'java.lang.Integer' for property 'price'; nested exception is java.lang.NumberFormatException: For input string: "qqq"]
    ```

  - `codes`

    - `typeMismatch.item.price`
    - `typeMismatch.price`
    - `typeMismatch.java.lang.Integer`
    - `typeMismatch`

  - 위의 codes에 해당하는 메시지가 없기 때문에 `defaultMessage`가 나온 것
  - 따라서 `errors.properties`에 해당 코드에 맞는 메시지를 추가하면된다.

- 의문점: 현재 입력 값을 메시지에 어떻게 넣을 것인지?



### Validator 분리

- 지금까지 사용했던 컨트롤러 코드를 보자:

  ````java
      @PostMapping("/add")
      public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
  
          log.info("objectName={}", bindingResult.getObjectName());
          log.info("target={}", bindingResult.getTarget());
  
          // 검증 로직
          ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
  /*
          if (!StringUtils.hasText(item.getItemName())) {
              bindingResult.rejectValue("itemName", "required");
          }
  */
  
          if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >1000000) {
              bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
          }
          if (item.getQuantity() == null || item.getQuantity() >= 9999) {
              bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
          }
  
          // 특정 필드가 아닌 복합 룰 검증
          if (item.getPrice() != null && item.getQuantity() != null) {
              int resultPrice = item.getPrice() * item.getQuantity();
              if (resultPrice < 10000) {
                  bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
              }
          }
  
          // 검증에 실패하면 다시 입력 폼으로
          if (bindingResult.hasErrors()) { // 가독성을 위해 이중 부정을 없앴다.
              log.info("errors = {} ", bindingResult);
              return "validation/v2/addForm";
          }
  
          // 성공 로직
          Item savedItem = itemRepository.save(item);
          redirectAttributes.addAttribute("itemId", savedItem.getId());
          redirectAttributes.addAttribute("status", true);
          return "redirect:/validation/v2/items/{itemId}";
      }
  ````

  - 짧은 성공 로직에 비해 매우 긴 검증 로직을 가지고 있다.

  - 컨트롤러가 너무 많은 일을 하고 있다.
  - 검증 작업을 담당하는 `Validator`의 필요성 : 컨트롤러의 역할을 분리하며, 코드 가독성 높임

- `Validator` 사용하여 분리하면

  - `ItemValidator`

    ```java
    @Component
    public class ItemValidator implements Validator {
    
        @Override
        public boolean supports(Class<?> clazz) {
            return Item.class.isAssignableFrom(clazz);
        }
    
        @Override
        public void validate(Object target, Errors errors) {
            Item item = (Item) target;
    
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName", "required");
    
            if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >1000000) {
                errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
            }
            if (item.getQuantity() == null || item.getQuantity() >= 9999) {
                errors.rejectValue("quantity", "max", new Object[]{9999}, null);
            }
    
            // 특정 필드가 아닌 복합 룰 검증
            if (item.getPrice() != null && item.getQuantity() != null) {
                int resultPrice = item.getPrice() * item.getQuantity();
                if (resultPrice < 10000) {
                    errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
                }
            }
        }
    }
    ```

  - 컨트롤러

    ```java
    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    
        if(itemValidator.supports(item.getClass())) {
            itemValidator.validate(item, bindingResult);
        }
    
        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) { // 가독성을 위해 이중 부정을 없앴다.
            log.info("errors = {} ", bindingResult);
            return "validation/v2/addForm";
        }
    
        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    ```

  - 하지만, 현 시점에서 `ItemValidator`가 굳이 `Validator` 인터페이스를 구현할 필요도 없고, 컴포넌트로 등록해서 사용할 필요도 없다. 

- isAssinableFrom 추가
  - == 사용하는 것보다 나음
- clazz 추가



#### 스프링 Validator

- `org.springframework.validation.Validator`

- **`WebDataBinder`**

  - 스프링의 파라미터 바인딩

  - 검증 기능도 가능: `WebDataBinder` 사용하여 `Validator`사용가능

  - 예시 코드

    ```java
    public class ValidationItemControllerV2 {
    
        private final ItemRepository itemRepository;
        private final ItemValidator itemValidator;
    
        @InitBinder
        public void init(WebDataBinder dataBinder) {
            dataBinder.addValidators(itemValidator);
        }
        
        ...
    }
    ```

    - 해당 컨트롤러 호출 될 때마다 새로 `WebDataBinder` 생성됨

  - ```java
    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    ```

  - `@Validated` 어노테이션만으로 해결.

    - 만약 검증기가 여럿일 경우 `Validator`의 `supports()` 메서드로 구별한다.

- 글로벌 설정

  ```java
  @SpringBootApplication
  public class ItemServiceApplication implements WebMvcConfigurer {
  	public static void main(String[] args) {
  		SpringApplication.run(ItemServiceApplication.class, args);
  	}
   	@Override
   	public Validator getValidator() {
   		return new ItemValidator();
   	}
  }
  ```



## 검증 - Bean Validation

- 필드 검증 로직은 정형화되어 있다:
  - 빈 값인가
  - 특정 값 영역에 들어와 있는가

- 이런 공통적인 검증 로직을 일일히 작성하는 것은 반복적이며 번거롭다!
  - 표준화된 검증 로직: **Bean Validaion**



### Bean Validaion: V3

- 구현체가 아니라 기술 표준: Bean Validation 2.0(JSR-380)

- 일반적으로 사용하는 구현체는 Hibernate Validator (단 Hibernate ORM과는 무관)



##### 스프링과 무관하게 사용하는 순수 Bean Validation

- 라이브러리 추가

  ```java
  dependencies {
  	...
     implementation 'org.springframework.boot:spring-boot-starter-web'
  }
  ```

- 표준 기술인지, `hibernate.validator` 구현체인지

  ```java
  import org.hibernate.validator.constraints.Range;
  
  import javax.validation.constraints.NotBlank;
  import javax.validation.constraints.NotNull;
  ```

  - `NotBlank`와 `NotNull`과 달리 `Range`와 같은 것들은 `hibernate.validator` 구현체임에 유의
  - 단 실무에서 대부분 `hibernate.validator` 사용한다.

- `Item` 엔티티에 적용한 모습

  ```java
  @Data
  public class Item {
  
      private Long id;
  
      @NotBlank(message = "공백X")
      private String itemName;
  
      @NotNull
      @Range(min = 1000, max = 1000000)
      private Integer price;
  
      @NotNull
      @Max(9999)
      private Integer quantity;
  ```

- 용례

  ```java
  void beanValidation() {
      ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
      Validator validator = validatorFactory.getValidator();
  
      Item item = new Item();
      item.setItemName(" "); // 공백
      item.setPrice(0);
      item.setQuantity(10000);
  
      Set<ConstraintViolation<Item>> violations = validator.validate(item);
      for (ConstraintViolation<Item> violation : violations) {
          System.out.println("violation = " + violation);
          System.out.println("violation.getMessage() = " + violation.getMessage());
      }
  }
  ```

  - 단, 위와 같이 validator 직접 만드는 일은 스프링 통합 사용시에는 없다.

  - 출력 결과

    ```java
    violation = ConstraintViolationImpl{interpolatedMessage='1000에서 1000000 사이여야 합니다', propertyPath=price, rootBeanClass=class hello.itemservice.domain.item.Item, messageTemplate='{org.hibernate.validator.constraints.Range.message}'}
    violation.getMessage() = 1000에서 1000000 사이여야 합니다
    violation = ConstraintViolationImpl{interpolatedMessage='9999 이하여야 합니다', propertyPath=quantity, rootBeanClass=class hello.itemservice.domain.item.Item, messageTemplate='{javax.validation.constraints.Max.message}'}
    violation.getMessage() = 9999 이하여야 합니다
    violation = ConstraintViolationImpl{interpolatedMessage='공백일 수 없습니다', propertyPath=itemName, rootBeanClass=class hello.itemservice.domain.item.Item, messageTemplate='{javax.validation.constraints.NotBlank.message}'}
    violation.getMessage() = 공백X
    ```



### 스프링 통합 사용

- 스프링은 빈 검증기를 통합 지원하고 있다.
  - 컨트롤러에서 `validator` 사용 안 하게 하더라도, `BeanValidation` 동작함을 볼 수 있다.
  - 라이브러리에 `spring-boot-starter-validation` 넣으면 스프링 부트가 자동적으로 Bean Validator 인식, 통합함
  - `LocalValidatorFactoryBean`을 글로벌 `Validator`로 등록
    - **주의: 직접 글로벌 `Validator` 적용할 경우 스프링 부트가 글로벌 `Validator` 등록하지 않음 -> 애노테이션 기반 검증 작동 안 함!**
  - `Validator`는 어노테이션을 바탕으로 검증
  - 검증 오류 발생 시, `FieldError`, `ObjectError` 생성하여 `BindingResult`에 담음 

- `@Validated`는 스프링, `@Valid`는 자바 표준

  - 단 `@Valid`는 의존관계 추가 필요

    - `implementation 'org.springframework.boot:spring-boot-starter-validation'`

    - 단, `@Validated`는 groups 기능을 포함

- 검증 순서
  - `@ModelAttribute` 각 필드에 타입 변환 시도
    - 성공할 경우 진행
    - 실패할 경우 `typeMismatch`로 `FieldError` 추가
  - `Validator` 적용

- 바인딩 성공한 필드만 Bean Validation 적용된다.

  

### Bean Validation - 에러 코드

- 기본 제공 오류 메시지

  ```java
  Field error in object 'item' on field 'itemName': rejected value [];
  codes [NotBlank.item.itemName,NotBlank.itemName,NotBlank.java.lang.String,NotBlank];
  arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [item.itemName,itemName];
  arguments []; default message [itemName]];
  default message [공백일 수 없습니다] 
  ```

  - `@NotBlank`의 경우: `NotBlank` 오류 코드 기반으로 `MessageCodesResolver`를 통해 메시지 코드가 순서대로 생성됨
    - `NotBlank.item.itemName
    - `NotBlank.itemName`
    - `NotBlank.java.lang.String`
    - `NotBlank`

- 위의 내용을 메시지에 등록하기만 하면 기본 제공 오류 메시지를 바꿀 수 있다.

  - `errors.properties`

    ```java
    ...
    # Bean Validation 추가
    NotBlank={0} 공백X
    Range={0}, {2} ~{1} 허용
    Max={0}, 최대 {1}
    ```

  - 메시지가 다음과 같이 변경된다. ({0}은 해당 필드명)

    ```
    itemName 공백X
    price, 1,000 ~1,000,000 허용
    quantity, 최대 9,999
    ```

  - `NotBlank.item.itemName=상품 이름을 적어주세요.`을 추가한다면

    에러 메시지는 `상품 이름을 적어주세요.`로 출력된다: 단계별 적용도 가능

- `BeanValidation`메시지 찾는 순서

  1. 생성된 메시지 코드 순서대로
  2. 애노테이션의 `message` 속성
  3. 라이브러리가 제공하는 기본 값




### Bean Validation - 오브젝트 오류

- 특정 필드가 아닌 오류를 처리하는 방식(`ObjectError`): `@ScriptAssert`

  ```java
  ...
  @ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총합이 10,000 원 넘게 입력해 주세요.")
  public class Item {
  ...
  }
  ```

- 문제점

  - 실 사용시에는 제약이 많고 복잡하다.
  - 특히 당 객체의 범위를 넘어서는 검증의 경우 대응이 어렵다.
  - 대안: 오브젝트 오류 관련 부분만 (기존 방식으로) 자바 코드로 작성하는 것을 권장!



### Bean Validation  - 한계

- 위의 예제에서 동일한 `Item` 객체에 대해 등록 / 수정 검증 요구사항이 서로 다르다고 생각해보자. 
- 애노테이션 기반인 Bean Validation의 경우 두 요구사항이 충돌할 경우 해결이 불가능하다.
  - 가령 수정시에만 Id값이 널이 아니게끔 한다면 등록 자체가 불가능해질 것이며(등록한 후에야 id 값이 생기기에)
  - 등록 시와 수정 시에 수량 범위 값이 다를 경우에도 동일 애노테이션으로 다루기 때문에 둘 중 하나만 선택 가능하다.

- **왜 id 값을 검증하는가?**
  - 수정 시 id 값이 항상 들어있게 로직이 구성되어 있지만,
  - 악의적인 HTTP 요청이 올 수 있기 때문에 항시 검증이 필요하다.

- Bean Validation 충돌을 해결하기 위한 두 가지 방법
  1. Bean Validation의 groups 기능을 사용
  2. `Item` 객체를 직접 사용하지 않고 `ItemSaveForm`, `ItemUpdateForm` 등 폼 전송을 위한 별도의 모델 객체를 만들어서 사용



### Bean Validation - groups

- 예시

  - `Item`에서

    ```java
    @Data
    public class Item {
        ...
    	@NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    	@Max(value = 9999, groups = SaveCheck.class)
    	private Integer quantity;
        ...
    }
    ```

  - 컨트롤러에서

    ```java
        @PostMapping("/add")
        public String addItemV2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ...
        }
    ...
        @PostMapping("/{itemId}/edit")
        public String editV3(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item,
                             BindingResult bindingResult) {
    	...
    	}
    ```

    - `@Validated(SaveCheck.class)`와 같은 식으로 적용

- 별개로, 999999999 같은 Integer 범위를 넘어간 경우에는 어떻게 구별할 것인지?
  - 문자를 입력한 경우와 같은 말이 나오는 것은 이상

- **`@Validated`에만 groups 적용 가능, `@Valid`에는 적용 불가 !**

- groups 기능은 실무에서는 잘 사용되지 않는다.
  - 코드가 난잡해지며
  - 폼 객체를 분리하는 것이 낫기 때문이다.



### Form 전송 객체 분리: V4

- 실무에서는 DTO 사용하여 현 작업과 관련한 정보만 전달함이 좋다.
  - 도메인 객체를 그대로 사용한다면
    - 흐름: HTML Form -> Item -> Controller -> Repository
    - 장점: 단순한 개발
    - 단점: 간단한 경우에만 사용 가능, 수정시 중복 발생한다면 groups 사용해야
  - DTO 사용
    - HTML Form -> ItemSaveForm -> Controller -> Item 생성 -> Repository
    - 장점: 폼 데이터가 복잡해도 맞춤 폼 객체를 사용하여 전달 가능, 검증 중복 없음
    - 단점: 폼 데이터를 기반으로 컨트롤러에서 `Item` 객체를 생성하는 변환 과정이 추가됨
- DTO 명명 관례
  - `ItemSave`, `ItemSaveForm`, `ItemSaveRequest`, `ItemSaveDto` 등 의미 있게 짓되, **일관성이 중요!**

- 뷰 템플릿이 비슷하다고 해도 왠만해선 분리함이 좋다.
  - 어설프게 합칠 경우 분기로 인해 유지보수에 장애가 발생
  - 이런 어설픈 분기가 보이는 것이 분리의 신호다.

- DTO Form 파일들은 web에 만들었다. 뷰와 관련된 부분들이기 때문이다.

- 예제

  ```java
  @Data
  public class ItemSaveForm {
  
      @NotBlank
      private String itemName;
  
      @NotNull
      @Range(min = 1000, max = 1000000)
      private Integer price;
  
      @NotNull
      @Max(9999)
      private Integer quantity;
  }
  ```

  - 그리고 컨트롤러에서,

  ```java
  @PostMapping("/add")
  public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
  
      if (form.getPrice() != null && form.getQuantity() != null) {
          int resultPrice = form.getPrice() * form.getQuantity();
          if (resultPrice < 10000) {
              bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
          }
      }
  
      // 검증에 실패하면 다시 입력 폼으로
      if (bindingResult.hasErrors()) { // 가독성을 위해 이중 부정을 없앴다.
          log.info("errors = {} ", bindingResult);
          return "validation/v4/addForm";
      }
  
      Item item = new Item();
      item.setItemName(form.getItemName());
      item.setPrice(form.getPrice());
      item.setQuantity(form.getQuantity());
  
      // 성공 로직
      Item savedItem = itemRepository.save(item);
      redirectAttributes.addAttribute("itemId", savedItem.getId());
      redirectAttributes.addAttribute("status", true);
      return "redirect:/validation/v4/items/{itemId}";
  }
  ```

  - 주의할 점
    - `ItemSaveForm` 매개변수의 `@ModelAttribute` 애노테이션에 name을 item으로 설정해준다. 기존 뷰 템플릿과의 호환을 위함
      - 적지 않을 경우 모델에 `itemSaveForm`으로 들어가기 때문
    - 검증 과정은 form을 이용해서 진행하며
    - Repo에 save 하기 전에 `Item` 객체로 변환하여 넣어준다. 



### Bean Validation - HTTP 메시지 컨버터

- 참고
  - `@ModelAttribute`: HTTP 요청 파라미터(URL 쿼리 스트링, POST Form)를 다룰 때 사용
  - `@RequestBody`: HTTP Body의 데이터를 객체로 변환할 때 사용

- API에 관해 고려해봐야 할 3가지 경우

  - 성공 요청
  - 실패 요청: JSON을 객체로 생성하는 것 자체가 실패
  - 검증 오류 요청: JSON을 객체로 생성하였지만, 검증 실패

- 요청 자체가 실패한 경우: 컨트롤러 호출도 안 되는 경우

  ```java
  @Slf4j
  @RestController
  @RequestMapping("/validation/api/items")
  public class ValidationItemApiController {
  
      @PostMapping("/add")
      public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {
  
          log.info("API 컨트롤러 호출");
  
          if (bindingResult.hasErrors()) {
              log.info("검증 오류 발생 errors={}", bindingResult);
              return bindingResult.getAllErrors();
          }
  
          log.info("성공 로직 실행");
          return form;
      }
  }
  ```

  - 정상 요청시에는 의도대로 컨트롤러 호출이 로그에 잘 남음

  - 그런데 잘못된 요청 보낼 시에는 컨트롤러 호출 자체가 안 일어나고 Exception만 발생

    ```java
    2022-02-28 23:11:19.773  WARN 27424 --- [nio-8080-exec-5] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.http.converter.HttpMessageNotReadableException:
    JSON parse error:                                                                                                         Unrecognized token 'a': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false'); nested exception is com.fasterxml.jackson.core.JsonParseException:                                                               Unrecognized token 'a': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')
     at [Source: (PushbackInputStream); line: 1, column: 50]]
    ```

  - API는 JSON을 객체(ItemSaveForm)로 변환한 이후에야 검증이 가능한데 그걸 하지 못했기 때문 -> 컨트롤러 조차 호출하지 못하게 되는 것

- 검증 오류의 경우

  ```
  [
      {
          "codes": [
              "Max.itemSaveForm.quantity",
              "Max.quantity",
              "Max.java.lang.Integer",
              "Max"
          ],
          "arguments": [
              {
                  "codes": [
                      "itemSaveForm.quantity",
                      "quantity"
                  ],
                  "arguments": null,
                  "defaultMessage": "quantity",
                  "code": "quantity"
              },
              9999
          ],
          "defaultMessage": "9999 이하여야 합니다",
          "objectName": "itemSaveForm",
          "field": "quantity",
          "rejectedValue": 10500,
          "bindingFailure": false,
          "code": "Max"
      }
  ]
  ```

  

  - 응답으로 위와 같이 오고, 다음과 같이 로그가 찍힌다.

  ```json
  2022-02-28 23:24:09.919  INFO 27424 --- [nio-8080-exec-8] h.i.w.v.ValidationItemApiController      : 검증 오류 발생 errors=org.springframework.validation.BeanPropertyBindingResult: 1 errors
  Field error in object 'itemSaveForm' on field 'quantity': rejected value [10500]; codes [Max.itemSaveForm.quantity,Max.quantity,Max.java.lang.Integer,Max]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [itemSaveForm.quantity,quantity]; arguments []; default message [quantity],9999]; default message [9999 이하여야 합니다]
  ```

  - `return bindingResult.getAllErrors()`가 `ObjectError`와 `FieldError`를 반환하였고
  - 스프링이 이를 JSON을 변환, 클라이언트에 전달한 상황이다.
  - 실무에서는 오류 객체를 그대로 반환하지 말고, 필요한 데이터만 뽑아 별개로 API 스펙을 정의, 객체를 만들어 반환하자.

- `@ModelAttribute` vs `@RequestBody`
  - `ModelAttribute`는 필드 단위로 적용되기에, 특정 필드에 타입 오류가 발생해도 다른 필드는 정상 처리된다.
  - `RequestBody`는 객체 전체 단위로 적용된다. 이 경우에는 객체 생성 자체가 성공하지 않으면 예외 발생한다. (컨트롤러 호출도, 검증도 이루어지지 않는다.)



## 로그인 처리1 - 쿠키, 세션

### 프로젝트 설정

- 웹과 도메인을 분리하였음
  - 웹에 변동이 있더라도 도메인에는 변화가 없도록 해야
  - 웹이 도메인을 보되, 반대는 없게 해야
  - 의존관계, 패키지 설정에 있어서 유의



### 로그인 기능 개발 

- 코드

  ```java
  @Service
  @RequiredArgsConstructor
  public class LoginService {
  
      private final MemberRepository memberRepository;
  
      /**
       * @return null일 경우 로그인 실패
       */
      public Member login(String loginId, String password) {
  //        Optional<Member> foundMemberOptional = memberRepository.findByLoginId(loginId);
  //        Member member = foundMemberOptional.get();  // 실제로는 get 말고 다른 것 쓰는 편이 나음
  //        if (member.getPassword().equals(password)) {
  //            return member;
  //        } else {
  //            return null;
  //        }
  
          return memberRepository.findByLoginId(loginId)
                  .filter(m -> m.getPassword().equals(password))
                  .orElse(null);
      }
  }
  ```



### 로그인 처리 - 쿠키 사용

- 로그인 상태 유지하기
  - 쿼리 파라미터를 계속 유지하면서 보내는 것은 어렵고 번거롭기에
  - 쿠키를 사용한다:
    - 로그인에 성공하면 HTTP 응답에 쿠키를 담아서 브라우저에 전달
    - 이후
      - welcome 페이지 처럼 로그인 정보 사용하는 페이지 접근 시 쿠키 사용
      - 모든 요청에 쿠키 정보 포함

- 쿠키
  - 영속 쿠키: 만료 날짜 입력 시 해당 날짜까지 유지
  - 세션 쿠키: 만료 날짜 생략시 브라우저 종료할 때 까지만
    - 서버 세션과는 무관하게, 쿠키 종류로서 세션 쿠키임

- 코드

  ```java
      @PostMapping
      public String login(@Valid @ModelAttribute LoginForm form,
                          BindingResult bindingResult, HttpServletResponse response) {
          if (bindingResult.hasErrors()) {
              return "login/loginForm";
          }
  
          Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
  
          if (loginMember == null) {
              bindingResult.reject("loginFail", "아이디 또는 비밀번호가 틀렸습니다.");
              return "login/loginForm";
          }
  
          // 로그인 성공 처리
  
          // 쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
  
          Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
          response.addCookie(idCookie);
  
          return "redirect:/";
      }
  ```

- 쿠키가 적용되게 홈 화면을 변경하면:

  ```java
      @GetMapping("/")
      public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
  
          if (memberId == null) {
              return "home";
          }
  
          // 로그인
          Member loginMember = memberRepository.findById(memberId);
          if (loginMember == null) {
              return "home";
          }
  
          model.addAttribute("member", loginMember);
          return "loginHome";
      }
  ```



#### 로그아웃

```java
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
```

- MaxAge를 0으로 설정하는 방식으로 없앤다.

- **하지만 위와 같이 쿠키만으로 로그인 기능을 구현하는 것에는 당연히 보안 문제가 있다!**



### 쿠키와 보안 문제

- 쿠키 사용으로 인해 발생하는 보안 문제

  - 쿠키 값은 임의로 변경할 수 있다.

    - 클라이언트가 쿠키를 강제로 변경하면 다른 사용자가 된다.
    - 브라우저 개발자모드 -> Application -> Cookie 변경으로 확인 가능

    - 쿠키에 보관된 정보는 훔쳐갈 수 있다.
      - 쿠키 정보는 웹 브라우저에 보관된다 -> 로컬 PC에서 훔칠 수 있다.
      - 네트워크 요청마다 클라이언트에서 서버로 전달된다 -> 네트워크 전송 구간에서 훔칠 수 있다.

  - 해커가 쿠키를 훔쳐갈 경우 그 값을 계속 사용할 수 있다: 지속적으로 악의적인 요청을 시도 가능

-  대안

  - 쿠키에 중요한 값을 노출하지 않아야함: 사용자별로 임의의 토큰을 노출
    - 서버에서 토큰과 사용자 id를 매핑해서 인식
    - 서버에서 토큰을 관리
  - 토큰은 예상 불가능해야 함
  - 토큰이 탈취되도 사용될 수 없게 해야함
    - 만료시간을 짧게 유지
    - 해킹이 의심되는 경우 서버에서 해당 토큰을 강제로 제거



### 로그인 처리 - 세션

- 세션 저장소
  - key: sessionId,(SSID: Service Set IDentifier) value: Member 객체
  - 기존 쿠키 방식 보안 문제 해결
    - 쿠키 값 변조 -> 예측 불가능한 복잡한 세션 id 사용
    - 클라이언트 해킹으로 인한 쿠키 정보 탈취 -> 세션 id에는 중요한 정보가 없음
    - 쿠키 탈취 후 사용 -> 세션 만료시간을 짧게 사용, 해킹 의심 시 서버에서 해당 세션을 강제로 제거



#### 세션 직접 개발해 보기

- 코드

  - `SessionManager`

    ```java
    public class SessionManager {
    
        public static final String SESSION_COOKIE_NAME = "mySessionId";
        private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
    
        /**
         * 세션 생성
         */
        public void createSession(Object value, HttpServletResponse response) {
    
            // 세션 id를 생성하고, 값을 세션에 저장
            String sessionId = UUID.randomUUID().toString();
            sessionStore.put(sessionId, value);
    
            // 쿠키 생성
            Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
            response.addCookie(mySessionCookie);
        }
    
        /**
         * 세션 조회
         */
        public Object getSession(HttpServletRequest request) {
            Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
            if (sessionCookie == null) {
                return null;
            }
            return sessionStore.get(sessionCookie.getValue());
        }
    
        /**
         * 세션 만료
         */
        public void expire(HttpServletRequest request) {
            Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
            if (sessionCookie != null) {
                sessionStore.remove(sessionCookie.getValue());
            }
        }
    
        public Cookie findCookie(HttpServletRequest request, String cookieName) {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return null;
            }
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(cookieName))
                    .findAny()
                    .orElse((null));
        }
    }
    ```

  - `LoginController`

    ```java
        @PostMapping("/login")
        public String loginV2(@Valid @ModelAttribute LoginForm form,
                            BindingResult bindingResult, HttpServletResponse response) {
            if (bindingResult.hasErrors()) {
                return "login/loginForm";
            }
    
            Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
    
            if (loginMember == null) {
                bindingResult.reject("loginFail", "아이디 또는 비밀번호가 틀렸습니다.");
                return "login/loginForm";
            }
    
            // 로그인 성공 처리
    
            // 세션 관리자를 통해 세션 생성, 회원 데이터 보관
    
            sessionManager.createSession(loginMember, response);
    
            return "redirect:/";
        }
    
        @PostMapping("/logout")
        public String logoutV2(HttpServletRequest request) {
            sessionManager.expire(request);
            return "redirect:/";
        }
    
    ```

  - `HomeController`

    ```java
    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
    
        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);
    
        // 로그인
        if (member == null) {
            return "home";
        }
    
        model.addAttribute("member", member);
        return "loginHome";
    }
    ```

- 기존 방식처럼 쿠키를 사용하지만 서버에서 데이터를 유지하게 했으며, UUID 사용

- 세션 매니저를 통해 무작위 UUID인 세션 id를 얻어오는 방식

  - 이제 Request Headers를 살펴보면

    `Set-Cookie: mySessionId=8076b391-55f0-4b40-8099-08ae9f95e006`

  - 쿠키를 훔쳐 본다고 해도 의미 있는 정보를 유추할 수 없다.

- 위와 같이 직접 만들지 않더라도 서블릿이 세션을 지원한다.

  - 서블릿 세션의 경우 추가적으로 일정 시간 세션 사용하지 않을 시 자동삭제함



### HTTP 서블릿 세션

- **`HttpSession`**
  - 서블릿이 제공하는 방식

- 상수 보관용 클래스

  ```java
  public abstract class SessionConst {
      public static final String LOGIN_MEMBER = "loginMember";
  }
  ```

  - 실제로 클래스를 생성하지는 않을 것이기에 추상 클래스로 만들었음
  - 인터페이스로 만드는 방식도 가능

- `LoginController`

  ```java
      @PostMapping("/login")
      public String loginV3(@Valid @ModelAttribute LoginForm form,
                            BindingResult bindingResult, HttpServletRequest request) {
          ...
          // 로그인 성공 처리
          // 세션이 있으면 있는 세션을 반환, 없다면 신규 세션을 생성
          HttpSession session = request.getSession();
  
          // 세션에 로그인 회원 정보 보관
          session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
  
          return "redirect:/";
      }
  ```

  - 서블릿 세션을 사용하기 위해서는 서블릿 request가 필요하기에 request를 인자로 받음

  - **세션 생성**

    - `request.getSession(true)`: true가 defualt

    - 세션이 있을 경우 기존 세션을 반환한다.

    - 세션이 없을 경우

      - create 인자가 true: 새로운 세션을 생성, 반환한다.

      - create 인자가 false: 새로운 새션을 생성하지 않고, `null` 반환한다.

  - 세션에 로그인 회원 정보 보관

    - `session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember)`
    - `request.setAttribute()`와 유사함, 한 세션에 여러 값을 저장 가능

- 로그아웃

  ```java
      @PostMapping("/logout")
      public String logoutV3(HttpServletRequest request) {
          HttpSession session = request.getSession(false);
          if (session != null) {
              session.invalidate();
          }
          return "redirect:/";
      }
  ```

  - `getSession(false)`인 까닭은, true일 경우 현재 세션이 없다 하더라도 세션을 새로 생성해 버리기 때문임
  - `session.invalidate()`로 세션 삭제

- `HomeController`

  ```java
      @GetMapping("/")
      public String homeLoginV3(HttpServletRequest request, Model model) {
  
          HttpSession session = request.getSession(false);
          if (session == null) {
              return "home";
          }
  
          Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
  
          // 세션에 회원 데이터가 없으면 home
          if (loginMember == null) {
              return "home";
          }
  
          // 세션이 유지되면 로그인으로 이동
          model.addAttribute("member", loginMember);
          return "loginHome";
      }
  ```

  - `request.getSession(false)` 사용한 이유
    - true로 하면 로그인 하지 않고 홈 화면을 요청하는 경우에도 무조건 세션을 생성하게 되기 때문이다.
    - 메모리 소모가 있기 때문에 꼭 필요할 때만 세션을 만들자. 
    
    

#### SessionAttribute

```java
@GetMapping("/")
public String homeLoginV3Spring(
        @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

    // 세션에 회원 데이터가 없으면 home
    if (loginMember == null) {
        return "home";
    }

    // 세션이 유지되면 로그인으로 이동
    model.addAttribute("member", loginMember);
    return "loginHome";
}
```

- 스프링이 제공하는 `@SessionAttribute` 애노테이션을 이용하여 위와 같이 코드를 간소화할 수 있다.

- 단 이 방식으로는 세션을 새로 생성하지는 않기 때문에
- 세션을 생성할 경우에는 기존 방식을 사용하면 된다.



#### TrackingModes

- 첫 로그인을 시도할 경우
  - `http://localhost:8080/;jsessionid=1D2812249FB0BF8C42942FA66506B948`
  - url에 `jsessionid`가 붙는다.

- 쿠키를 지원하지 않는 브라우저의 경우를 위해서 제공되는 기능이다.
  - 처음에는 서버 입장에서는 해당 브라우저가 쿠키를 지원하는지 모르기 때문이다.
  - 단 이 기능을 사용할 경우 계속 이 정보를 모든 링크에 전달해야 한다.
  - Thymeleaf 등 템플릿 엔진을 사용할 경우 위를 url에 자동 전달하게 돕기도 하지만
  - 이 방식 자체가 매우 번거롭다.

- `application.properties`에 다음을 추가하여 위 기능을 끄도록 하자:

  `server.servlet.session.tracking-modes=cookie`



#### 세션 정보

- 세션 정보 출력용 컨트롤러

  ```java
  @Slf4j
  @RestController
  public class SessionInfoController {
  
      @GetMapping("/session-info")
      public String sessionInfo(HttpServletRequest request) {
          HttpSession session = request.getSession(false);
  
          if (session == null) {
              return "세션이 없습니다";
          }
  
          // 세션 데이터 출력
          session.getAttributeNames().asIterator()
                  .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));
  
          log.info("sessionId={}", session.getId());
          log.info("session.getMaxInactiveInterval()={}", session.getMaxInactiveInterval());
          log.info("creationTime={}", new Date(session.getCreationTime()));
          log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
          log.info("isNew={}", session.isNew());
  
          return "세션 출력";
      }
  }
  ```

- 아래와 같은 값이 출력된다:

  ```
  2022-03-11 23:35:52.924  INFO 8112 --- [nio-8080-exec-8] h.l.web.session.SessionInfoController    : 
  session name=loginMember, value=Member(id=1, loginId=test, name=테스터, password=1q2w!)
  sessionId=5704D91BE7B71C6FE8353B467C2D4C05
  session.getMaxInactiveInterval()=1800
  creationTime=Fri Mar 11 23:35:47 KST 2022
  lastAccessedTime=Fri Mar 11 23:35:47 KST 2022
  isNew=false
  ```

- 설명

  - sessionId : 세션Id, JSESSIONID 의 값이다.
    - 예) 34B14F008AA3527C9F8ED620EFD7A4E1
  - maxInactiveInterval : 세션의 유효 시간,
    - 예) 1800초, (30분)
  - creationTime : 세션 생성일시
  - lastAccessedTime : 세션과 연결된 사용자가 최근에 서버에 접근한 시간, 클라이언트에서 서버로 sessionId ( JSESSIONID )를 요청한 경우에 갱신된다.
  -  isNew : 새로 생성된 세션인지, 아니면 이미 과거에 만들어졌고, 클라이언트에서 서버로 sessionId ( JSESSIONID )를 요청해서 조회된 세션인지 여부

- Date 형태로 오지 않고 long으로 오는 값들의 경우 형변환 필요하다.



#### 세션 타임아웃

- 문제 상황

  - 로그아웃 해야 세션이 삭제되는데, 대부분의 사용자는 로그아웃 하지 않고, 브라우저를 종료함.

  - HTTP는 비연결성(ConectionLess)이기에 서버는 사용자의 브라우저 종료 여부를 알 수 없음.
  - 서버 입장에선 세션을 언제 삭제해야 하는지 판단할 수 없음.

- 남아 있는 세션을 무한정 보관할 경우
  - 세션과 관련한 쿠키 정보가 탈취되었을 때의 보안 문제
  - 세션은 기본적으로 메모리에 생성됨 -> 자원 소모, 메모리 누수

- **세션의 종료 시점**
  - 30분 정도씩 시간이 흐를 때마다 종료시키는 방식
  - 하지만 해당 시간마다 로그인 해야 하는 것은 불편
  - 대안: **마지막 요청 시간을 기준으로 30분 정도를 유지해주는 방식, `HttpSession`도 이 방식을 사용**

- **세션 타임아웃 설정**

  - 스프링 부트의 글로벌 설정 방식
    - `application.properties`에서 `server.servlet.session.timeout=1800`
    - 기본 값은 `1800(30min)`\
    - 글로벌 설정의 경우 분 단위로 설정해야: 60(최소값), 120, ... , 1800, ...

  - 특정 세션 단위로 시간을 설정하는 방식
    - `session.getMaxInactiveInterval(1200)`
    - 보안상 혹은 다른 이유로 특정 세션에만 다른 시간을 적용하고자 한다면 위를 사용

- **세션 타임아웃 발생**
  - 세션 타임아웃 시간은 해당 세션과 관련된 `JSESSIONID`를 전달하는 HTTP 요청이 있을 경우 다시 갱신됨
  - `session.getLastAccessedTime()`: 최근 세션 접속 시간
  - 해당 시간 이후로 timeout 시간 지날 경우 WAS가 내부에서 해당 세션을 제거함 



#### 세션 주의사항

- 세션에는 최소한의 데이터만 보관하라.
  - 메모리 사용량이 급격하게 늘어나서 장애로 직결될 위험이 있다!

- 세션 유지 시간을 긴 경우 메모리 사용량이 누적될 수 있기에 적절히 조절이 필요.
  - 기본 시간이 30분이라는 데에 기반하여 조절



## 로그인 처리2 - 필터, 인터셉터

### 서블릿 필터

- 필요성

  - 로그인이 필요한 페이지들을 처리하려면

    일일히 컨트롤러에 동일한 로그인 체크 매커니즘을 반복적으로 호출하게 됨

    - 이는 번거롭고 누락 가능성이 높을 뿐만 아니라
    - 수정이 매우 어렵다.

  - 이와 같은 **공통 관심사(cross-cutting concern)**에 대한 처리가 필요

    - 스프링 AOP로 해결 가능하다.

    - **하지만 웹과 관련된 공통 관심사의 경우 서블릿 필터, 스프링 인터셉터를 사용하는 것이 나음**

      - 웹 관련 공통 관심사를 처리할 때는 HTTP 헤더, url 정보 등이 필요한데

        서블릿 필터, 스프링 인터셉터는 `HttpServletRequest` 등을 제공하기에 웹 관련된 공통 관심사를 처리함에 더 유용하다.

        (AOP의 경우 위와 같은 기능을 제공하지 않는다. )



#### 서블릿 필터 설명

- **필터의 흐름**

  ```mermaid
  graph LR
  A[HTTP 요청] --> B[WAS] --> C[필터] --> D[서블릿] --> E[컨트롤러]
  ```

- 필터는 특정 url 패턴에 적용할 수 있다.

  - /* 의 경우 모든 요청에 필터 적용
  - 서블릿 url 패턴을 찾아보면 구체적으로 이 url 패턴에 대해 알아볼 수 있음

- 스프링 사용 시 서블릿 필터의 '서블릿'은 디스패처 서블릿이라 생각하면 된다.

- **필터의 제한기능**

  ```mermaid
  graph LR
  A[HTTP 요청] --> B[WAS] --> C[필터] --> D[서블릿] --> E[컨트롤러]
  ```

  - 정상 상황에는 위와 같이 처리되는 반면

  ```mermaid
  graph LR
  A[HTTP 요청] --> B[WAS] --> C[필터]
  ```

  - **비정상 상황**에서는 위와 같이 필터 단계에서 **서블릿을 호출하지 않고** 끝낼 수도 있다.

    -> 로그인, 권한 여부 등을 체크하기에 유용

- **필터 체인**

  ```mermaid
  graph LR
  A[HTTP 요청] --> B[WAS] --> C[필터1:로그 남기기] --> F[필터2: 로그인 여부 체크] --> G[필터3: ...]--> D[서블릿] --> E[컨트롤러]
  ```

  - 위와 같이 필터를 연쇄하여 사용할 수도 있다.

- **필터 인터페이스**
  - 필터 인터페이스를 구현, 등록 시 서블릿 컨테이너가 필터를 **싱글톤** 객체로 생성, 관리
  - `init()`: 필터 초기화 메서드, 서블릿 컨테이너가 생성될 때 호출됨
  - `doFilter()`: 고객의 요청이 올 때마다 요청되는 메서드, 필터의 로직을 구현하면 됨
  - `destroy()`: 필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출됨



#### 요청 로그

- 코드

  ```java
  @Slf4j
  public class LogFilter implements Filter {
      @Override
      public void init(FilterConfig filterConfig) throws ServletException {
          log.info("log filter init");
      }
  
      @Override
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
          log.info("log filter doFilter");
  
          HttpServletRequest httpRequest = (HttpServletRequest) request;
          String requestURI = httpRequest.getRequestURI();
  
          String uuid = UUID.randomUUID().toString();
  
          try {
              log.info("Request [{}][{}]", uuid, requestURI);
              chain.doFilter(request, response);
          } catch (Exception e) {
              throw e;
          } finally {
              log.info("");
              log.info("Response [{}][{}]", uuid, requestURI);
          }
      }
  
      @Override
      public void destroy() {
          log.info("log filter destroy");
          Filter.super.destroy();
      }
  }
  ```

  - `javax.servlet`의 `Filter`를 구현해주자.

  - `doFilter()`는 `ServletRequest/Response`를 사용하는데,

     더 많은 기능을 제공하는 `HttpServletRequest/Response`로 다운캐스팅 하였다.

    - `ServletRequest/Response`는 Http 요청이 아닌 경우까지 고려된 인터페이스임. Http 사용할 것이기에 다운캐스팅 한 것임.

  - **`try` 블록 안에서 `chain.doFilter()`를 호출해준 데에 주목하라!**
    - 다음 연쇄 필터를 호출 시도해 줘야 한다.
    - 연쇄 필터를 호출하지 않으면 결과적으로 서블릿 호출도 이뤄지지 않으며, 컨트롤러 호출도 일어나지 않음!
  - **로그를 남길 때 동일 식별자(UUID)를 남기게 해야 추적에 용이함. logback mdc를 참고하라.**

- 필터 등록 - 스프링 부트 사용

  ```java
  @Configuration
  public class WebConfig {
  
      @Bean
      public FilterRegistrationBean logFilter() {
          FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
          filterFilterRegistrationBean.setFilter(new LogFilter());
          filterFilterRegistrationBean.setOrder(1);
          filterFilterRegistrationBean.addUrlPatterns("/*");
  
          return filterFilterRegistrationBean;
      }
  }
  ```

  - 스프링 부트가 WAS를 갖고 띄우기 때문에 WAS 띄울 때 필터를 같이 띄움
  - `setFilter(new LogFilter())`로 위의 필터를 적용
  - `setOrder()`로 필터 연쇄 순서 설정
  - `addUrlPatterns()`로 필터 적용 url을 설정



#### 인증 체크

- 로그인 여부 검사 필터 개발

  ```java
  @Slf4j
  public class LoginCheckFilter implements Filter {
  
      private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};
  
      @Override
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
  
          HttpServletRequest httpRequest = (HttpServletRequest) request;
          String requestURI = httpRequest.getRequestURI();
  
          HttpServletResponse httpResponse = (HttpServletResponse) response;
  
          try {
              log.info("인증 체크 필터 시작{}", requestURI);
  
              if (isLoginCheckPath(requestURI)) {
                  log.info("인증 체크 로직 실행 {}", request);
                  HttpSession session = httpRequest.getSession(false);
                  if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) ==null) {
  
                      log.info("미인증 사용자 요청 {}", requestURI);
                      // 로그인으로 redirect
                      httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                      return;
                  }
              }
  
              chain.doFilter(request, response);
          } catch (Exception e){
              throw e; // 예외 로깅 가능하지만, 톰캣까지 예외를 보내주어야 한다.
          } finally {
              log.info("인증 체크 필터 종료 {}", requestURI);
          }
      }
  
      /**
       * 화이트리스트의 경우 인증 체크 X
       */
      private boolean isLoginCheckPath(String requestURI) {
          return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
      }
  }
  ```

  - `init()`이나 `destroy()`는 구현할 필요가 없다.
    - 인터페이스의 메서드라 할지라도 `default`가 붙은 것들은(`public default void init()`) 구현 하지 않아도 됨

  - 검증을 위한 whitelist

  - 

- 필터 적용을 위하여 `WebConfig`에 빈 등록한다.

  ```java
      @Bean
      public FilterRegistrationBean loginCheckFilter() {
          FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
          filterFilterRegistrationBean.setFilter(new LoginCheckFilter());
          filterFilterRegistrationBean.setOrder(2);
          filterFilterRegistrationBean.addUrlPatterns("/*");
  
          return filterFilterRegistrationBean;
      }
  ```

  - 여기서 url 패턴을 전체로 적용한 것은, 추후 페이지가 추가 되더라도 화이트 리스트를 제외한 전체에 대해 권한 체크를 수행하고 싶어서이기 때문이다.

    - 적용 범위에 따라서 둘 중 어디에 제약을 적용할 것인가에 대해 생각해보자.

    - url 패턴을 넓게 잡아서 필터를 한 번 더 호출한 것으로 인한 성능 저하는 신경쓰지 않아도 될 정도다.

      메서드 1회 호출이 큰 영향을 주지는 않는다.

      **주로 데이터베이스 쿼리나 외부 네트워크에서 성능을 깎아먹기에 오히려 그 부분에 유의해야 한다!**

- **`redirectURL`**

  - `httpResponse.sendRedirect("/login?redirectURL=" + requestURI);`

    로그인 이후에는 자동으로 시도했던 페이지로 리다이렉트 하게끔 만듦

  - 하지만 위의 코드를 실행해 봐도 로그인 이후에 리다이렉트가 일어나지 않는다. 로그인 컨트롤러를 손봐야 한다.

    ```java
        @PostMapping("/login")
        public String loginV4(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                              @RequestParam(defaultValue = "/") String redirectURL,
                              HttpServletRequest request) {
            ...
                        return "redirect:" + redirectURL;
        }
    ```

    - `@RequestParam`으로 쿼리 파라미터 받아와서, 리다이렉트 url 변경해준다.
    - 변경 이후에는 로그인 시 목적 페이지로 정상적으로 리다이렉트 되는 것을 볼 수 있다.

- 정리

  - 서블릿 필터로 공통 관심사를 해결했기에,

    추후 관련 변경사항이 생기더라도 필터만 변경하면 된다.

    - 변경에 용이하며
    - SRP를 지키는 방식임

- 참고: `chain.doFilter(request, response)`의 경우 다음 호출 시에 `request`와 `response`를 다른 객체로 바꿔 넘길 수 있음



### 스프링 인터셉터

#### 스프링 인터셉터 설명

- 서블릿 필터와 마찬가지로 웹과 관련된 공통 관심사를 해결하는 기술

  - 서블릿 필터: 서블릿 제공 기술
  - 스프링 인터셉터: 스프링 MVC제공 기술
  - 두 기술은 적용 순서, 범위, 사용법이 다름

- 스프링 MVC에 특화된 필터 기능

  - 스프링 MVC를 사용하며
  - 필터를 사용할 특별한 이유가 없다면 사용 권장

- **흐름**

  ```mermaid
  graph LR
  A[HTTP 요청] --> B[WAS] --> C[필터] --> D[서블릿] --> E[스프링 인터셉터] --> F[컨트롤러]
  ```

  - 스프링 인터셉터는 스프링 MVC 기능이기에 디스패처 서블릿 이후 등장
  - url 패턴 설정이 서블릿 필터와 다르며, 더 정밀
  - 필터와 마찬가지로 스프링 인터셉터는 부적절한 요청이 발생할 경우 컨트롤러를 호출하지 않을 수 있음
  - 스프링 인터셉터도 서블릿 필터처럼 체인 기능 사용

- **`HandlerInterceptor` 인터페이스**

  - 서블릿 필터는 `doFilter()`만 있었지만, 스프링 인터셉터의 경우 세분화되어 있음:

  ```java
  public interface HandlerInterceptor {
      
  	default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
  			throws Exception {
  		return true;
      }
      
  	default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
  			@Nullable ModelAndView modelAndView) throws Exception {
  	}
      
  	default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
  			@Nullable Exception ex) throws Exception {
  	}
  
  }
  ```

  - `preHandle()`: 컨트롤러 호출 전
    - 리턴 값이 `true`면 다음으로 진행
  - `postHandle()`: 컨트롤러 호출 이후(정확히는 핸들러 어댑터 호출 이후)
  - `afterCompletion()`: 요청 완료 이후(뷰 렌더링 이후)



##### 스프링 인터셉터 호출 흐름

1. 클라이언트--HTTP 요청-->`DispatcherServlet`
2. `DispatcherServlet-->preHandle` 호출(인터셉터)
3. `DispatcherServlet`-->`handle(handler)` 핸들러 어댑터 호출
4. 핸들러 어댑터- -> 핸들러(컨트롤러) 호출

5. -핸들러 어댑터->`DispatcherServlet`로 `ModelAndView` 반환

6. `DispatcherServlet`-->`postHandle`호출(인터셉터)
7. `DispatcherServlet` --> `render(model)` `View` 호출

8. `DispatcherServlet`-->`afterCompletion` 호출(인터셉터)
9. `View` --> HTML 응답



##### 예외 상황의 경우

- 컨트롤러에서 Exception이 발생한다면?
  5. 핸들러 어댑터 -->`DispatcherServlet`로 예외 전달
  5. `DispatcherServlet`-->`afterCompletion` 호출(인터셉터)
  6. `DispatcherServlet` --> `WAS`로 예외 전달
  
- **주의!**
  - **위 상황에서 `postHandle`은 호출되지 않는다!**
  - **하지만, `afterCompletion`은 호출된다!**
    - 이 경우 파라미터 `Exception ex`에 예외를 받아서, 로그 출력 가능
    - 정상 호출 시에는  `Exception ex`는 `null`
  - **`postHandle`은 예외시 호출 X**
  - **`afterCompletion`은 예외시에도 호출 O**



#### 요청 로그

- `LogInterceptor`

  ```java
  @Slf4j
  public class LogInterceptor implements HandlerInterceptor {
  
      public static final String LOG_ID = "logId";
  
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
  
          String requestURI = request.getRequestURI();
          String uuid = UUID.randomUUID().toString();
  
          request.setAttribute(LOG_ID, uuid);
  
          // @RequestMapping: HandlerMethod
          // 정적 리소스: ResourceHttpRequestHandler
          if (handler instanceof HandlerMethod) {
              HandlerMethod hm = (HandlerMethod) handler; // 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
          }
  
          log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
          return true;
      }
  
      @Override
      public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
          log.info("postHandle [{}]", modelAndView);
      }
  
      @Override
      public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
          String requestURI = request.getRequestURI();
          String uuid = (String) request.getAttribute(LOG_ID);
          log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
          if (ex != null) {
              log.error("afterCompletion error!!", ex);
          }
      }
  }
  ```

  - `request`에 변수를 담아서 전달

    - 서블릿 필터와는 달리, 스프링 인터셉터의 `preHandle`, `postHandle`, `afterCompletion`은 호출 시점이 분리되어 있음
    - 값을 전달할 방법이 필요함
      - 필드 값을 사용해서는 안 된다!! 로그 인터셉터도 싱글톤처럼 관리되기에 stateless해야 한다.
    - 때문에 `request`에 값을 담아서 전달하였다.

  - `preHandle`이 `true`로 리턴되야 다음 인터셉터, 컨트롤러가 호출 될 수 있다.

  - `HandlerMethod`

    - 메서드 파라미터로 넘어오는 `Object handler`

      - `Object`인 것은 여러 가지 핸들러를 사용하기 위해 설정된 것

        타입에 따라 다르게 형 변환 등의 처리가 필요하다.

      - `@Controller`, `RequestMapping`을 이용한 경우 `ResourceHttpRequestHandler`가 넘어온다.

  - 오류 로그일 경우 {} 안 넣어도 오류 정보가 뜬다. 인자로 담기만 해도 된다.

- `WebConfig`

  ```java
  @Configuration
  public class WebConfig implements WebMvcConfigurer {
  
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          registry.addInterceptor(new LogInterceptor())
                  .order(1)
                  .addPathPatterns("/**")
                  .excludePathPatterns("/css/**", "/*.ico", "/error");
      }
      ...
  }
  ```

  - 필터에 비해서 정밀하게 url 패턴을 설정 가능

- **스프링의 url 경로 관련: [PathPattern](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/util/pattern/PathPattern.html)**



#### 인증 체크

- `LoginCheckInterCeptor`

  ```java
  @Slf4j
  public class LoginCheckInterceptor implements HandlerInterceptor {
  
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
  
          String requestURI = request.getRequestURI();
  
          log.info("인증 체크 인터셉터 실행{}", requestURI);
  
          HttpSession session = request.getSession();
  
          if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
              log.info("미인증 사용자 요청");
              // 로그인으로 redirect
              response.sendRedirect("/login?redirectURL=" + requestURI);
  
              return false;
          }
  
          return true;
      }
  }
  ```

  - 서블릿 필터에 비해 코드가 간결해졌음에 주목하라.

  - 로그인 인증 여부는 컨트롤러 호출 이전에만 확인하면 되기 때문에

    `preHandle`만 구현하였다.

    - 인터페이스라고 해도 `default` 메서드들은 구현 안 해도 되니까(Java8)

- `WebConfig`

  ```java
      public void addInterceptors(InterceptorRegistry registry) {
          ...
  
          registry.addInterceptor(new LoginCheckInterceptor())
                  .order(2)
                  .addPathPatterns("/**")
                  .excludePathPatterns("/", "/members/add", "/login", "/logout",
                          "/css/**", "/*.ico", "error");
      }
  ```

  - 인터셉터 적용 경로 설정이 간단



### ArgumentResolver 활용

공통 작업이 필요할 때 컨트롤러를 편리하게 사용 가능!



- `HomeController`

  ```java
  //    @GetMapping("/") 기존 버전
      public String homeLoginV3Spring(
              @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
      ...
      }
  
      @GetMapping("/")
      public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) {
      ...
      }
  ```

  - 매개변수가 깔끔하게 정리된다.

- `Login` 애노테이션

  ```java
  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Login {
  }
  ```

  - 파라미터에만 적용했고,
  - 리플렉션 등을 활용할 수 있게 런타임까지 애노테이션 정보가 남아 있음, 보통 런타임 사용

- `LoginMemberArgumentResolver`

  ```java
  @Slf4j
  public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
  
      @Override
      public boolean supportsParameter(MethodParameter parameter) {
          log.info("supportsParameter 실행");
  
          boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
          boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
          return hasLoginAnnotation && hasMemberType;
      }
  
      @Override
      public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
  
          log.info("resolveArgument 실행");
  
          HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
          HttpSession session = request.getSession(false);
  
          if (session == null) {
              return null;
          }
  
          return session.getAttribute(SessionConst.LOGIN_MEMBER);
      }
  }
  ```

  - `supportsParameter()`: 해당 애노테이션(`@Login`이 있으면서 지원하는 타입(`Member`)일 경우 이 `ArgumentResolver`사용
  - `resolveArgument()`
    - 컨트롤러 호출 이전에 호출되어 필요한 파라미터 정보를 생성
    - 이후 스프링 MVC가 컨트롤러 메서드 호출할 때 반환값을 인자로 전달

- `WebConfig`

  ```java
      @Override
      public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
          resolvers.add(new LoginMemberArgumentResolver());
      }
  ```

  

## 예외 처리와 오류 페이지

### 서블릿 예외 처리

#### 개념

- 서블릿이 예외 처리를 지원하는 방식
  - `Exception`: 예외가 발생해서 WAS 서블릿 컨테이너까지 날아갈 때
  - `response.sendError(Http 상태 코드, 오류 메시지)`



##### Exception

- **자바 직접 실행**

  - 자바의 메인 메서드를 실행하는 경우 `main` 쓰레드가 실행된다.

    예외 발생 메서드가 계속해서 던져져서, `main` 메서드를 넘어서 던져지면

  - 예외 정보를 남기고 쓰레드가 종료됨

- **웹 어플리케이션**

  - 요청별로 쓰레드가 할당, 서블릿 컨테이너에서 실행

  - 애플리케이션 바깥으로 예외가 던져질 경우

    ```mermaid
    graph LR
    A[컨트롤러:예외 발생]-->B[인터셉터]-->C[서블릿]-->D[필터]-->E[WAS]
    ```

    - WAS는 어떻게 예외를 어떻게 처리할까?

      - `server.error.whitelabel.enabled=false`을 `application.properties`에 더해서,

        스프링의 기본 에러 페이지를 닫고 테스트 해본다.

      ```java
      @Slf4j
      @Controller
      public class ServletExController {
      
          @GetMapping("/error-ex")
          public void errorEx() {
              throw new RuntimeException("테스트 예외 발생!");
          }
      }
      ```

      - 톰캣이 기본으로 제공하는 `HTTP Status 500 – Internal Server Error`화면이 나옴
        - 서버 내부에서 해결 불가한 오류 500
      - 매핑이 되어있지 않은 url `http://localhost:8080/no-page`을 입력시에는 톰캣이 제공하는 404에러 확인 가능



##### response.sendError(Http 상태 코드, 오류 메시지)

- `HttpServletResponse`의 메서드
- 예외를 일으키지는 않지만, 서블릿 컨테이너에게 오류 발생을 전달 가능

- http 상태 코드와 오류 메시지 넣을 수 있음

- `ServletExController`

  ```java
      @GetMapping("/error-404")
      public void error404(HttpServletResponse response) throws IOException {
          response.sendError(SC_NOT_FOUND, "404 오류!");
      }
  
      @GetMapping("/error-500")
      public void error500(HttpServletResponse response) throws IOException {
          response.sendError(500, "500 오류!");
      }
  ```

  - 메서드 특성상 IOException 던져야 함: 왜?

- 호출 흐름

  ```mermaid
  graph LR
  A[컨트롤러:sendError]-->B[인터셉터]-->C[서블릿]-->D[필터]-->E[WAS: sendError 호출 기록 확인]
  ```

  - `sendError`는 `response`에 오류 상태를 저장한다.
  - 서블릿 컨테이너는 클라이언트 응답 전에 `sendError`가 호출되었는가를 확인한다.
  - 호출 되었을 경우, 오류 코드에 맞춰 오류 페이지를 제공한다.



- Exception의 경우 무조건 500에러!
- sendError의 경우 HTTP 상태 코드, 오류 메시지 지정 가능.



#### 오류 화면 제공

- 고객 친화적 오류화면 제공
- 서블릿은 `Exception`이나 `sendError`상황에서 오류 처리 기능을 제공한다.

- 제공 방식

  - 과거: `web.xml`

    ```xml
    <web-app>
     <error-page>
     <error-code>404</error-code>
     <location>/error-page/404.html</location>
     </error-page>
     <error-page>
     <error-code>500</error-code>
     <location>/error-page/500.html</location>
     </error-page>
     <error-page>
     <exception-type>java.lang.RuntimeException</exception-type>
     <location>/error-page/500.html</location>
     </error-page>
    </web-app>
    ```

  - 현재는 스프링 부트가 제공하는 기능을 사용하여 서블릿 오류 페이지를 등록

    (스프링 부트를 통해 서블릿 컨테이너를 실행하기 때문)

- `WebServerCustomizer`

  ```java
  @Component
  public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
      @Override
      public void customize(ConfigurableWebServerFactory factory) {
  
          ErrorPage errorPage404 = new ErrorPage(NOT_FOUND, "/error-page/404");
          ErrorPage errorPage500 = new ErrorPage(INTERNAL_SERVER_ERROR, "/error-page/500");
  
          ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");
  
          factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
      }
  }
  ```

  - 에러 페이지를 등록했음
  - `RuntimeException`로 등록할 경우 하위 자식 클래스들의 경우까지 연결

-  컨트롤러와 뷰를 구현하면, 제대로 오류 페이지가 작동하는 것을 볼 수 있다.



#### 오류 페이지 작동원리

##### 요청 흐름

- 위의 예제에서,

- `Exception` 발생 시에는

  ```mermaid
  graph LR
  A[컨트롤러:예외 발생]-->B[인터셉터]-->C[서블릿]-->D[필터]-->E[WAS]
  ```

  - 순서로 호출된다. 여기서 다시 해당 오류 페이지로 `/error-page/500`이 지정되어 있음을 확인하고,

    ```mermaid
    graph LR
    A[WAS: 에러페이지 요청]-->B[필터]-->C[서블릿]-->D[인터셉터]-->E[컨트롤러]-->F[view]
    ```

  - 다시 위와 같이 요청 흐름이 이루어진다.

    - Http요청이 다시 온 것과 유사하지만, 실제로는 요청이 다시 온 것은 아니며 내부 동작이다.
    - **클라이언트 요청은 1회 이루어졌지만 컨트롤러는 2회 호출되었다.**
    - **클라이언트(웹 브라우저)는 서버 내부의 위와 같은 동작에 대해 모른다. 위는 모두 서버 내에서 일어난 일이다.**



##### 오류 정보 추가

- WAS는 오류 페이지를 단순 요청만 하는 것이 아니라 오류 정보를 `request`의 `attribute`에 추가하여 넘긴다.

  오류 페이지에서 이 정보를 사용할 수도 있다.

- `RequestDispatcher`에 오류 정보 상수들이 보관되어 있다.

  ```java
  public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
  public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
  public static final String ERROR_MESSAGE = "javax.servlet.error.message";
  public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
  public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
  public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";
  ```

  - 한 번 출력해보면

    | ERROR PAGE 404                       | ERROR PAGE 500                       |
    | ------------------------------------ | ------------------------------------ |
    | ERROR_EXCEPTION:null                 | ERROR_EXCEPTION:null                 |
    | ERROR_EXCEPTION_TYPE:null            | ERROR_EXCEPTION_TYPE:null            |
    | ERROR_MESSAGE:404 오류!              | ERROR_MESSAGE:500 오류!              |
    | ERROR_REQUEST_URI:/error-404         | ERROR_REQUEST_URI:/error-500         |
    | ERROR_SERVLET_NAME:dispatcherServlet | ERROR_SERVLET_NAME:dispatcherServlet |
    | ERROR_STATUS_CODE:404                | ERROR_STATUS_CODE:500                |
    | dispatcherType=ERROR                 | dispatcherType=ERROR                 |



#### 필터

- 예외 발생 시 오류 페이지 출력을 위해 WAS가 2번째 호출을 시도한다.

  하지만, 이 경우 이미 클라이언트부터 온 요청 흐름에서 필터/인터셉터의 검증을 이미 완료한 상황이다.

  비효율적인 호출을 막기 위해서 클라이언트에서 온 요청인지, 서버 내부의 요청인지를 구별할 수 있어야 한다.

  -> **`DispatcherType`의 필요**



##### DispatcherType

- 서블릿 스펙은 요청이 고객에 의한 것인지, 서버에 의한 것인지를 알 수 있는 `DispatherType`을 제공한다.
  - `dispatherType=REQUEST`: 고객의 요청
  - `dispatcherType=ERROR`: 서버 오류 페이지용 요청

| DispatcherType | 설명                                                         |
| -------------- | ------------------------------------------------------------ |
| FOWARD         | 서블릿에서 다른 서블릿이나 JSP를 호출할 때(`RequestDispatcher.foward(request, response)`) |
| INCLUDE        | 서블릿에서 다른 서블릿이나 JSP의 결과를 포함할 때(`RequestDispatcher.include(request, response)`) |
| REQUEST        | 클라이언트 요청                                              |
| ASYNC          | 서블릿 비동기 호출                                           |
| ERROR          | 서버에서 에러 호출                                           |



##### 필터와 DispatcherType

- `WebConfig`에서

  ```java
      @Bean
      public FilterRegistrationBean logFilter() {
          FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
          filterRegistrationBean.setFilter(new LogFilter());
          filterRegistrationBean.setOrder(1);
          filterRegistrationBean.addUrlPatterns("/*");
          filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
          return filterRegistrationBean;
      }
  ```

  - `filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR)`: `REQUEST`와 `ERROR` 모두를 넣었기에

    클라이언트의 일반 요청 외에 에러 페이지에서도 필터가 호출된다.

    참고로, default는 `REQUEST`만 지정된다.

  - 필요에 따라 달리 지정하면 된다. 오류 메시지 전용 필터를 사용하거나, 클라이언트 요청에만 필터를 지정하거나 하는 식으로

  

#### 인터셉터

- 필터의 경우와 같이, 오류 처리로 인한 서버 내부의 호출이 이루어졌을 때 인터셉터가 요청 검증을 할 필요가 없다.

  다음과 같은 변

- `WebConfig`

  ```java
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          registry.addInterceptor(new LogInterceptor())
                  .order(1)
                  .addPathPatterns("/**")
                  .excludePathPatterns("/css/**", "*.ico", "/error", "/error-page/**");   // 오류 페이지 경로를 추가
      }
  ```

  - 인터셉터의 경우 `DispatcherType`을 지원하지 않는다,

    대신에, `.excludePathPatterns`에 오류 페이지 경로를 추가하는 방식으로 문제를 해결할 수 있다.



### 스프링 부트 - 오류 페이지

- 기존에 예외 처리 페이지를 만들기 위해 사용한 방식
  - `WebServerCustomizer`
  - 예외 종류에 따라 `ErrorPage` 추가
  - 예외 처리용 컨트롤러 `ErrorPageController`

- 스프링 부트가 기본 제공하는 사항:

  - `ErrorPage` 자동 등록, `/error` 경로로 기본 오류 페이지를 설정함
    - `new ErrorPage("/error")`, 상태코드와 예외를 설정하지 않을 경우 기본 오류 페이지로 사용됨
    - 서블릿 밖으로 예외가 발생하거나, `sendError` 호출시 모든 오류는 `error`를 호출하게 됨
  - `BasicErrorController` 스프링 컨트롤러를 자동으로 등록
    - `ErrorPage`에서 등록한 `/error`를 매핑하여 처리하는 컨트롤러

  - **`ErrorMvcAutoConfiguration`**: 오류 페이지를 자동 등록하는 역할

- 개발자는 뷰 템플릿 경로에 오류 페이지만 넣어두면 된다.



#### 뷰 선택 우선순위

`BasicErrorController`의 처리순서다:

1. 뷰 템플릿

   - `resources/templates/error/500.html`
   - `resources/templates/error/5xx.html`

2. 정적 리소스(`static`, `public`)

   - `resources/static/error/404.html`

   - `resources/static/error/4xx.html`

3. 적용 대상이 없을 때 뷰 이름(`error`)
   - `resources/templates/error.html`

- 구체적인 것이 더 우선순위를 가짐
- 뷰 템플릿이 정적 리소스보다 우선순위가 높음



#### `BasicErrorController`가 제공하는 기본 정보들

- `BasicErrorController`가 모델에 담아 뷰에 전달하는 정보들로, 뷰 템플릿은 이 값을 이용하여 출력할 수 있다.

- 오류 페이지에 다음을 추가해보자:

  ```html
              <li th:text="|timestamp: ${timestamp}|"></li>
              <li th:text="|path: ${path}|"></li>
              <li th:text="|status: ${status}|"></li>
              <li th:text="|message: ${message}|"></li>
              <li th:text="|error: ${error}|"></li>
              <li th:text="|exception: ${exception}|"></li>
              <li th:text="|errors: ${errors}|"></li>
              <li th:text="|trace: ${trace}|"></li>
  ```

  - 다음과 같이 출력된다:

    ```
    오류 정보
    timestamp: Wed Mar 23 13:35:56 KST 2022
    path: /error-ex
    status: 500
    message: null
    error: Internal Server Error
    exception: null
    errors: null
    trace: null
    ```

- **오류 관련 내부 정보들을 고객에게 노출하는 것은 좋지 않다.**

  - 고객이 해당 정보를 읽어도 혼란만 더해지며

  - 보안상 문제가 된다.

  - `BasicErrorController`가 다음 오류 정보를 `model`에 포함할지 선택 가능하다: `application.properties`에서

    ```
    server.error.include-exception=false : exception 포함 여부( true , false )
    server.error.include-message=never : message 포함 여부
    server.error.include-stacktrace=never : trace 포함 여부
    server.error.include-binding-errors=on_param : errors 포함 여부
    ```

    - `on param`은 파라미터가 있을 경우 해당 정보를 노출한다: 운영 서버에서 사용하지 말자.

      (`localhost:8080/error-ex/?message=`)

- 사용자에게는 간단한 오류 메세지와 오류 화면을 제공하고

  **오류는 서버에 로그로 남겨서 로그로 확인해라.**



#### 기타 참고사항

- **스프링 부트 오류 관련 옵션**
  - `server.error.whitelabel.enabled=true`: 오류 처리 화면을 못 찾으면 스프링 whitelabel 오류 페이지 적용
  
  - `server.error.path=/error`: 오류 페이지 경로, 스프링이 자동 등록하는 서블릿 글로벌 오류 페이지 경로와 `BasicErrorController` 오류 컨트롤러 경로에 함께 사용된다.
  
- **확장 포인트**
  - 에러 공통 처리 컨트롤러의 기능을 변경하고 싶다면,
    - `ErrorController` 인터페이스를 상속받거나
    - `BasicErrorController`를 상속받아서 기능을 추가



## API 예외 처리

- **주의점**

  - html 페이지의 경우 오류 페이지로 대부분의 예외 처리가 가능하지만

  - API의 경우 각 오류 상황에 맞는 오류 의답 스펙을 정하고 JSON으로 데이터를 내려주어야 한다.

- `ApiController`

  ```java
  @Slf4j
  @RestController
  public class ApiExceptionController {
  
      @GetMapping("/api/members/{id}")
      public MemberDto getMember(@PathVariable("id") String id) {
  
          if (id.equals("ex")) {
              throw new RuntimeException("잘못된 사용자");
          }
  
          return new MemberDto(id, "hello " + id);
      }
  
      @Data
      @AllArgsConstructor
      static class MemberDto {
          private String memberId;
          private String name;
      }
  }
  ```

  `RestController`를 만들고, 의도적으로 `Exception` 발생시킬 수 있는 상황을 만든다.

- 이 경우 `Exception` 발생하면 json 응답이 아니라 기존의 에러페이지(html)을 반환한다. 이를 수정해보자:

- `ErrorPageCotroller`

  ```java
  @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> errorPage500Api(
          HttpServletRequest request, HttpServletResponse response) {
  
      log.info("API errorPage 500");
  
      HashMap<String, Object> result = new HashMap<>();// 실제로는 객체 사용이 더 좋다.
      Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
      result.put("status", request.getAttribute(ERROR_STATUS_CODE));
      result.put("message", ex.getMessage());
  
      Integer statusCode = (Integer)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
  
      return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
  }
  ```

  - `RequestMapping`에 `produce`값으로 `MediaType.APPLICATION_JSON_VALUE`를 넣어서
  -  `Accept` 헤더가 json을 요구할 경우 오류 정보를 json의 형태로 전달해줄 수 있게 하자.



### 스프링부트 기본 오류 처리

- API의 경우에도 스프링부트가 제공하는 `BasicErrorController` 오류 제공 메커니즘을 적용할 수 있다.

  다음과 같은 json 데이터가 반환된다:

  ```json
  {
      "timestamp": "2022-03-24T04:58:41.672+00:00",
      "status": 500,
      "error": "Internal Server Error",
      "path": "/api/members/ex"
  }
  ```

  - 한편, `Accept` 헤더를 json이 아닌, `text/html`로 변경한 요청의 응답을 받아보면, html 페이지가 반환된다.

    `produce` 값을 변경하는 것을 바탕으로 구현되어 있다. (`BasicErrorController`)

    - 기본적으로는 `error` 메서드를 이용하여 경로를 처리하고, html 요청이 올 경우`errorHtml()`메서드로 처리하는 것

  - json 응답의 경우에도 `application.properties`의 변경을 통해 오류 정보를 추가 전달이 가능하다.

- **Html 페이지 / API 오류 처리 **

  - `BasicErrorController`: API 오류 처리에도 사용 가능하지만(`BasicErrorController`확장을 통해), 기본적으로 **html 페이지 처리일 때 더 적합**

  - **`@ExceptionHandler`: API 오류 처리시에는, 이걸 사용하자.**

    - API의 경우는 각 컨트롤러, 예외마다 다른 응답 결과를 출력해야 할 수도 있기 때문에

      html 오류 페이지처럼 일관되게 처리하기는 힘들기 때문이다.



### HandlerExceptionResolver

- **목표**
  - 발생하는 예외에 따라 상태코드를 다르게 처리한다.
    - 지금까지는 예외가 서블릿을 넘어 WAS까지 전달됐을 경우 Http 상태코드가 500으로 처리된다.
  - 오류 메시지, 형식 등을 API마다 다르게 처리하고 싶다.



#### 상태코드 변환

- 스펙에 정의된 잘못된 입력값을 처리한다고 생각해보자.

  해당 경우에는 `IllegalArgumentException`이 던져지고, 400 에러코드를 출력해야 한다.

- 컨트롤러에 잘못된 인자를 받아서 예외를 던지는 경우를 우선 만들어보자.

  ```java
  @Slf4j
  @RestController
  public class ApiExceptionController {
      @GetMapping("/api/members/{id}")
      public MemberDto getMember(@PathVariable("id") String id) {
          ...
          if (id.equals("bad")) {
              throw new IllegalArgumentException("잘못된 입력값");
          }
  }
  ```

  postman으로 응답 결과를 받아보면:

  ```json
  {
      "timestamp": "2022-03-25T12:10:53.640+00:00",
      "status": 500,
      "error": "Internal Server Error",
      "path": "/api/members/bad"
  }
  ```

  상태코드는 500이다. WAS의 입장에서는 서버 내부에서 예외가 터진 것이기 때문이다.



#### `HandlerExceptionResolver`

- 핸들러 밖으로 예외가 던져진 경우 예외를 해결하고, 동작을 정의할 수 있는 스프링 MVC의 기술.  `ExceptionResolver`

- 인터페이스 코드:

  ```java
  public interface HandlerExceptionResolver {
  	@Nullable
  	ModelAndView resolveException(
  			HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex);
  
  }
  ```



#### 처리 흐름

- **`ExcptionResolver` 적용 이전의 예외 처리 흐름**
  1. 클라이언트--HTTP 요청-->`DispatcherServlet`
  2. `DispatcherServlet-->preHandle` 호출(인터셉터)
  3. `DispatcherServlet`-->`handle(handler)` 핸들러 어댑터 호출
  4. 핸들러 어댑터- -> 핸들러(컨트롤러) 호출, **예외 발생**
  5. 핸들러 어댑터 -->`DispatcherServlet`로 예외 전달
  5. `DispatcherServlet`-->`afterCompletion` 호출(인터셉터)
  6. `DispatcherServlet` --> `WAS`로 예외 전달


- **`ExceptionResolver` 적용 이후**

  6. `DispatcherServlet` --> `ExceptionResolver`로 예외 해결 시도

  7. 오류 처리에 따라 달라짐

     - `ModelAndView`에 값이 있다면: `DispatcherServlet` --> `render(model)` `View` 호출

     - 텅 빈 ModelAndView가 반환되면 뷰 렌더링 생략: 8로 바로 이동

     - `null`반환시 다음 `ExceptionResolver`실행, 처리 가능한 `ExceptionResolver` 없을 경우 예외처리가 안 됨

       **`ExcptionResolver` 적용 이전의 예외 처리 흐름**과 동일

  8. `DispatcherServlet` -->`afterCompletion` 호출(인터셉터)

  9. `DispatcherServlet` --> WAS로 정상 응답

  10. WAS는 응답을 보고 `sendError` 호출을 확인하고 오류페이지를 찾음

- `MyHandlerExceptionResolver` 구현

  ```java
  @Slf4j
  public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
      @Override
      public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
          try {
              if (ex instanceof IllegalArgumentException) {
                  log.info("IllegalArgumentException resolver to 400");
                  response.sendError(SC_BAD_REQUEST, ex.getMessage());
                  return new ModelAndView();
              }
          } catch (IOException e) {
              log.error("resolver ex", e);
          }
  
          return null;
      }
  }
  ```

  등록: `WebConfig`

  ```java
      @Override
      public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
          resolvers.add(new MyHandlerExceptionResolver());
      }
  ```

  - **`configureHandlerExceptionResolvers(..)` 메서드를 오버라이드 하면 스프링이 기본 사용하는 `ExceptionResolver`가 제거되니 주의하라!**

  응답은 다음과 같다:

  ```json
  {
      "timestamp": "2022-03-25T13:21:49.019+00:00",
      "status": 400,
      "error": "Bad Request",
      "path": "/api/members/bad"
  }
  ```

  400 상태코드로 제대로 변경된 것을 볼 수 있다.

  - `ModelAndView`가 `null`로 반환되면 오류를 처리하지 못한 것으로 판단한다.
  - `ModelAndView`가 정상적으로 반환되면 흐름이 다시 정상적으로 돌아온다.
    - **주의! `ExceptioResolver`로 예외를 해결해도 `postHandle()`은 호출되지 않는다! **
  - `Exception`을 `sendError()`처럼 해결하는 방식



#### 반환값에 따른 동작방식

반환값에 따른 `DispatcherServlet`의 동작방식이다:

- **빈 `ModelAndView`**: 뷰를 렌더링하지 않고, 정상 흐름으로 서블릿이 리턴

- **`ModelAndView지정`**: 뷰를 렌더링

- **`null`**: 예외 처리가 가능할 때 까지 다음 `ExceptionResolver`를 실행,

  처리 가능한 `ExceptionResolver`없을 경우에는 `ExceptionResolver`사용 안 할때처럼 동작



#### 활용

- **예외 상태 코드 변환**: 변환 후 서블릿에서 상태 코드에 따른 오류를 처리하도록 위임한다.
- **뷰 템플릿 처리**: `ModelAndView`에 값을 채워서 새로운 오류 화면 렌더링
- **API 응답 처리**: 응답 바디에 직접 데이터를 넣어주는것도 가능. JSON으로 응답할 경우 API 응답 처리 가능



### HandlerExceptionResolver - API 예외 처리

- 기존 방식대로 예외가 발생할 때마다 WAS까지 예외를 던지고,

  오류 페이지 정보를 가지고 `/error`를 호출하는 것은 지나치게 복잡함

- `ExceptionResolver`를 활용하여 문제 해결 가능!

- 예제

  - `UserException` 생성(테스트를 위해생성자만 받아옴)

  - `UserHandlerExceptionResolver` 구현

    ```java
    @Slf4j
    public class UserHandlerExceptionResolver implements HandlerExceptionResolver {
    
        private final ObjectMapper objectMapper = new ObjectMapper();
    
        @Override
        public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
            try {
    
                if (ex instanceof UserException) {
                    log.info("UserException resolver to 400");
                    String acceptHeader = request.getHeader("accept");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    
                    if ("application/json".equals(acceptHeader)) {
                        Map<String, Object> errorResult = new HashMap<>();
                        errorResult.put("ex", ex.getClass());
                        errorResult.put("message", ex.getMessage());
    
                        String result = objectMapper.writeValueAsString(errorResult);
    
                        response.setContentType("application/json");
                        response.setCharacterEncoding("utf-8");
                        response.getWriter().write(result);
                        return new ModelAndView();
                    } else {
                        return new ModelAndView("error/500");
                    }
                }
    
            } catch (IOException e) {
                log.error("resolver ex", e);
            }
            return null;
        }
    }
    ```

    - `UserException`인 경우 400 응답코드로 변환

      - 헤더의 `Accept`값이 `json`이면
        - 오류 정보를 json으로 변환하고(`ObjectMapper`)
        - `response`에 담아 빈 `ModelAndView()`를 리턴한다.
      - `Accept`가 json이 아니면(html)
        - `new ModelAndView("error/500")`리턴하여 오류 페이지를 호출한다.

    - **응답 결과**

      - `UserException` 발생시 (`/user-ex`요청)

        - `Accept` Header 값이

          - `application/json`이면:

            ```json
            {
                "ex": "hello.exception.exception.UserException",
                "message": "사용자 오류"
            }
            ```

            json 정상 전달

          - `text/html`이면: `templates/error`에 등록한 html 500 에러페이지 출력

- **`ExceptionResolver`를 이용하여 예외를 처리**

  - **서블릿 컨테이너까지 예외 전달되지 않음**,

    **스프링 MVC에서 예외처리 끝**

  - **WAS 입장에서는 정상 처리가된 것**

- 서블릿 컨테이너까지 예외를 전달하지 않기에 예외처리가 매우 간단해졌음

  하지만, `ExceptionResolver` 구현이 복잡함

  -> 스프링이 제공하는 기술 사용하면 됨!



### 스프링 ExceptionResolver

- 스프링이 제공하는 `ExceptionResolver`
  - `HandlerExceptionResolver`에 다음 순서로 등록
    1. `ExceptionHandlerExceptionResolver` -> 높은 우선순위
    2. `ResponseStatusExceptionResolver`
    3. `DefaultHandlerExceptionResolver` -> 낮은 우선순위

- **`ExcpetionHandlerExceptionResolver`**
  - `@ExceptionHandler`를 처리, API 예외 대부분을 처리
- **`ResponseStatusExceptionResolver`**
  - HTTP 상태 코드 지정
  - `@ResponseStatus(code = HttpStatus.NOT_FOUND)`

- **`DefaultHandlerExceptionResolver`**
  - 스프링 내부 기본 예외 처리



#### `ResponseStatusExceptionResolver`

- 예외에 따라 상태코드를 지정

- 대상
  - `@ResponseStatus`
  - `ResponseStatusException` 예외

- 사용

  ```java
  @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
  public class BadRequestException extends RuntimeException {
  }
  ```

  API 통신시에는 다음을 출력:

  ```json
  {
      "timestamp": "2022-03-27T21:40:17.747+00:00",
      "status": 400,
      "error": "Bad Request",
      "path": "/api/response-status-ex1"
  }
  ```

  - 상태코드와 `reason`을 뽑아내서, `sendError`하고, `ModelAndView`반환
    - `HandlerExceptionResolver`와 같음!

- `reason`은 메시지 소스에서 찾는 기능도 제공함

  ```java
  @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
  ```

  그리고 메시지에 내용만 추가해주면 적용된다,



#### `ResponseStatusException`

- 필요성

  - 개발자가 직접 변경할 수 없는 예외에도 적용할 수 있어야.
  - 애노테이션은 동적 변경이 어려움

- 사용

  ```java
      @GetMapping("/api/response-status-ex2")
      public String responseStatusEx2() {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
      }
  ```

  - `ResponseStatusException`을 던졌지만, `IllegalArgumentException`을 처리한다.

- `ResponseStatusException`은 `RuntimeException`을 확장하고 있다.



#### `ExceptionHandlerExceptionResolver`

- **`DefaultHandlerExceptionResolver`**

  - 스프링 내부 예외를 해결

  - `responser.sendError()` 이용해서 해결하는 방식

- 스프링의 적용예

  ```java
      @GetMapping("/api/default-handler-ex")
      public String defaultException(@RequestParam Integer data) {
          return "ok";
      }
  ```

  파라미터의 `TypeMismatch` 발생시, 응답은 다음과 같다:

  ```json
  {
      "timestamp": "2022-03-27T22:03:13.083+00:00",
      "status": 400,
      "error": "Bad Request",
      "path": "/api/default-handler-ex"
  }
  ```

  400으로 변환된 것을 볼 수 있다(기본적으로는 500임).

  -> `DefaultHandlerExceptionResolver`의 기능

  
