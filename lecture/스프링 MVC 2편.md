# 스프링 MVC 2편 - 백엔드 웹 개발 활용 기술

## 타임리프 - 기본 기능

### 소개

- 유용한 링크
  - 공식 사이트: https://www.thymeleaf.org/
  - 공식 메뉴얼 - 기본 기능: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
  - 공식 메뉴얼 - 스프링 통합: https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html

#### 특징

- EHf서버사이드 HTML 렌더링(SSR)
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



#### 검증 직접 처리

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
