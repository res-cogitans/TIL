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
