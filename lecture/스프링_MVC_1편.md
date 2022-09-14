# 스프링 MVC 1편

웹 애플리케이션 이해
-----------

### 웹 서버, 웹 애플리케이션 서버

**웹 서버**

*   HTTP 기반으로 동작
*   정적 리소스 제공, 기타 부가기능
*   정적(파일) HTML, CSS, JS, 이미지, 영상
*   Ex) NGINX, APACHE

**웹 애플리케이션 서버 WAS**

*   HTTP 기반 동작
*   웹 서버 기능(정적 리소스 제공) 포함 +
*   프로그램 코드를 실행해서 어플리케이션 로직 수행
    *   동적 HTML, HTTP API(JSON)
    *   서블릿, JSP, 스프링 MVC
*   Ex) 톰캣, Jetty, Undertow

**차이**

*   정적 리소스 vs + 애플리케이션 로직
*   경계가 사실 모호: 웹 서버도 프로그램 실행 기능을 포함하기도
*   자바의 경우 서블릿 컨테이너 기능을 제공하면 WAS(단, 서블릿 없이 자바코드를 실행하는 서버 프레임워크도 존재)
*   WAS는 애플리케이션 코드를 실행하는데 더 특화

**웹 시스템 구성 - WAS, DB**

*   CLI -> WAS(애플리케이션 로직 / HTML, CSS, JS / 이미지) -> DB
*   WAS, DB만으로 시스템 구성 가능
*   WAS가 너무 많은 역할을 담당, 서버 과부화 우려
*   가장 중요한 애플리케이션 로직이 정적 리소스로 인해 수행이 어려워질 수 있음
*   WAS 장애시 오류 화면도 노출 불가

**웹 시스템 구성 - WEB, WAS, DB**

*   CLI -> Web Server(HTML, CSS, JS / 이미지) -> WAS(애플리케이션 로직) -> DB
*   정적 리소스는 웹 서버가 처리
*   웹 서버는 동적 처리(애플리케이션 로직 등) 필요시 WAS에 요청 위임
*   WAS는 중요한 애플리케이션 로직 처리 전담
*   효율적인 리소스 관리: 정적 리소스 사용이 많다면 Web 서버 증설,  
    애플리케이션 리소스가 많이 사용되면 WAS 증설
*   잘 죽지 않는 웹 서버, 잘 죽는 WAS -> WAS, DB 장애시 웹 서버가 오류 제공 가능
*   실제 사용에서는 CDN 등 추가, 만일 API만 제공 시에는 웹 서버 없어도 되는 등 차이 존재



### 서블릿

**HTML Form 데이터 전송 -> 웹 애플리케이션 서버 직접 구현 방식**

*   서버 TCP/IP 대기, 소켓 연결
*   HTTP 메시지 파싱 … 읽기 작업들
*   로직 실행 후 응답 데이터 생성 TCP/IP 응답 전달, 소켓 종료
*   짧은 비즈니스 로직 실행을 위해 해야 할 일이 너무 많음  
    \-> 서블릿 사용 필요

**서블릿**

*   urlPatterns(/save)의 URL이 호출되면 서블릿 코드가 실행
*   HTTP 요청 정보를 편리하게 사용할 수 있는 HttpServletRequest
*   HTTP 응답 정보를 편리하게 제공할 수 있는 HttpServletResponse
*   웹 브라우저 -> localhost:8080/save -> WAS에서 요청 메시지를 기반으로 request, response 객체 생성 -> 서블릿 컨테이너의 myServlet 실행(request, response) -> response 객체 정보로 HTTP 응답 생성 -> 웹 브라우저
*   **HTTP 요청, 응답 흐름**
    *   HTTP 요청시
        *   WAS는 request, response 객체를 새로 만들어서 서블릿 객체 호출
        *   개발자는 request 객체에서 HTTP 요청 정보를 꺼내 사용
        *   개발자는 response 객체에 HTTP 응답 정보를 입력
        *   WAS는 response 객체에 담긴 내용으로 HTTP 응답 정보 생성

**서블릿 컨테이너**

*   톰캣처럼 서블릿을 지원하는 WAS
*   WAS 내의 서블릿 컨테이너는 서블릿 객체를 생성, 초기화 호출, 종료하는 생명주기 관리
*   서블릿 객체는 **싱글톤**으로 관리
    *   최초 로딩 시점에 서블릿 객체를 미리 만들어두고 재활용
    *   모든 고객 요청은 동일한 서블릿 객체 인스턴스에 접근
    *   **공유 변수 사용 주의!**
    *   서블릿 컨테이너 종료시 같이 종료
*   JSP도 서블릿으로 변환되어서 사용
*   동시 요청을 위한 멀티 쓰레드 처리 지원



### 동시 요청 - 멀티 쓰레드

서블릿 객체는 쓰레드가 호출함

**쓰레드**

*   애플리케이션 코드를 하나하나 순차적으로 실행하는 것
    
*   하나의 코드 라인만 수행
    
*   동시 처리가 필요하면 쓰레드를 추가로 생성
    
*   **단일 요청시**: 요청 -> 연결 -> 쓰레드 할당 -> 서블릿 호출 -> 응답 -> 휴식
    
*   **다중 요청시**: 요청1 -> 연결 -> 쓰레드 할당 -> 요청 처리중(처리 지연)  
    이 때 요청 2 발생시에 연결 -> 쓰레드 대기 -> timeout
    
*   **요청마다 쓰레드 생성**: 요청1이 지연된다 해도 쓰레드2 생성한다면 요청 2 처리(사용 후에는 쓰레드 날림)
    
    *   **장점**:
        *   동시 요청 처리 가능
        *   리소스(CPU, 메모리) 가 허용할 때까지 처리 가능
        *   하나의 쓰레드가 지연되도 나머지는 정상작동
    *   **단점**
        *   쓰레드 생성 비용이 비쌈 -> 응답 속도 감소
        *   쓰레드는 컨텍스트 스위칭 비용이 발생 -> 한정된 CPU로 처리를 옮겨 다닐 때 생기는 비용
        *   쓰레드 생성에 제한이 없음 -> 요청이 너무 많으면 리소스 임계점을 넘어서 서버가 죽을 수 있음

**쓰레드 풀** - 요청마다 쓰레드를 생성하는 방식의 보완

*   **특징**
    *   필요한 쓰레드를 쓰레드 풀에 보관, 관리
    *   쓰레드 최대치를 관리: 톰캣은 최대 200개 기본설정
*   **사용**
    *   요청 시 쓰레드를 쓰레드 풀에서 꺼내 사용, 다 사용후에는 반납
    *   쓰레드 풀에 남은 쓰레드가 없을 경우 거절 혹은 특정 숫자만큼 대기하게 설정 가능
*   **장점**
    *   쓰레드가 미리 생성되어 있기에, 생성 및 종료 비용이 절약되고 응답이 빠름
    *   최대치가 있기에 요청이 너무 많아도 기존 요청은 안전하게 처리 가능
*   **실무 팁**
    *   주요 튜닝 포인트는 최대 쓰레드(max thread) 수
    *   값이 너무 낮을 경우: 서버 리소스는 여유, 클라이언트는 쉽게 응답 지연
    *   값이 너무 높을 경우: 리소스 임계점 초과로 서버 다운 가능
    *   장애 발생시
        *   클라우드의 경우 서버 늘리고, 이후 튜닝
        *   평상시에 충분히 튜닝
    *   적정 숫자:
        *   애플리케이션 로직의 복잡도, CPU, 메모리, IO 리소스 상황에 따라 달라짐
        *   성능 테스트
            *   실서비스와 최대한 유사하게 시도
            *   tool: 아파치, ab, 제이미터, nGrinder

**WAS의 멀티 쓰레드 지원**

*   멀티 쓰레드에 대한 부분은 WAS가 처리
*   개발자가 멀티 쓰레드 관련 코드를 신경쓰지 않아도 됨: 싱글 쓰레드 프로그래밍처럼 개발
*   단, 멀티 쓰레드 환경이기에 싱글톤 객체(서블릿, 스프링 빈)는 주의해서 사용



### HTML, HTTP API, CSR, SSR

**백엔드 개발자의 관심사 3가지**  
**1\. 정적 리소스**: 고정된 HTML, CSS, JS, 이미지, 영상 등을 제공, 주로 웹 브라우저

**2\. 동적 HTML 페이지**

*   동적으로 필요한 HTML파일을 생성해서 전달
*   웹 브라우저 -> 요청 -> WAS -> DB -> 주문정보 조회 -> WAS -> 동적으로 HTML 생성(JSP, thymeleaf) -> 생성된 HTML -> 웹 브라우저

**3\. HTTP API**

*   HTML이 아니라 **데이터(JSON) 전달**: UI 화면이 필요하면 클라이언트가 별도처리
*   HTTP API의 접점
    *   **UI 클라이언트** 접점
        *   앱 클라이언트(안드로이드, 아이폰, PC 앱)
        *   웹 브라우저에저 JS 통한 HTTP API 호출
            *   Ex) 웹 클라이언트: JS로 ajax로 서버 API 호출해 데이터를 전달 받고 HTML 동적 생성 후 화면에 띄움
        *   React, Vue.js 같은 웹 클라이언트
    *   **서버 to 서버**
        *   주문 서버 -> 결제 서버
            *   Ex) 웹/앱 클라이언트의 HTTP API를 통해 주문 서버 WAS에서 주문 정보를 받고 DB에서 조회, 정보를 결제 서버 WAS에 전달
        *   기업간 데이터 통신

**SSR 서버 사이드 렌더링**

*   **최종 HTML을 서버에서 만들어서 웹 브라우저에 전달**
*   주로 정적인 화면에 사용
*   관련: JSP, thymeleaf

**CSR 클라이언트 사이드 렌더링**

*   HTML 결과를 웹 브라우저의 JS로 동적 생성
*   주로 동적인 화면에 사용, 웹 환경을 앱처럼 필요한 부분부분 변경 가능
*   HTML 요청 -> 내용 없고 JS 링크 있는 HTML 응답 -> JS 요청 -> 클라이언트 로직, HTML 렌더링 코드 있는 JS 응답 -> HTTP API가 데이터 요청 -> JSON 응답 -> 자바스크립트로 HTML 결과 렌더링
*   Ex) 구글 지도, Gmail, 구글 캘린더
*   관련: React, Vue.js

**ETC**

*   React, Vue.js를 CSR + SSR 동시 지원하는 웹 프레임워크도 있음
*   SSR 사용해도 JS 사용해서 화면 일부를 동적으로 변경 가능

**백엔드 개발자 입장에서 UI기술**

*   서버사이드 렌더링 기술(**필수**)
    *   JSP, **thymeleaf** 등
    *   화면이 정적, 복잡하지 않을 경우
*   클라이언트 사이드 렌더링 기술
    *   React, Vue.js
    *   복잡하고 동적인 UI 사용
*   선택
    *   백엔드 개발자에게 프론트엔드 학습은 옵션
    *   서버, DB, 인프라 등등을 배워야



### 자바 백엔드 웹 기술 역사

**과거 기술**

*   서블릿(1997): HTML (동적) 생성이 어려움
*   JSP(1999): HTML 생성은 편리하지만, 비즈니스 로직까지 너무 많은 역할 담당
*   서블릿, JSP 포함 MVC 패턴 사용
*   MVC프레임워크 난립(2000초 ~ 2010초)
    *   MVC 패턴 자동화, 웹 편의성 기능 다수 지원
    *   스트럿츠, 웹워크, 스프링 MVC(구버전)
*   애노테이션 기반의 스프링 MVC 등장  
    **현재 기술**
*   스프링 부트 등장
    *   서버 내장
    *   과거: 서버에 WAS 직접 설치, 소스는 War파일 만들어서 WAS에 배포
    *   스프링 부트는 Jar에 WAS 포함 -> 빌드 배포 단순화  
        **최신 기술 - 스프링 웹 기술의 분화**
*   Web Servlet - Spring MVC
*   Web Reactive - Spring WebFlux
    *   특징
        *   비동기 논블로킹 처리
        *   최소 쓰레드로 최대 성능 -> 쓰레드 컨텍스트 스위칭 비용 효율화
        *   함수형 스타일 -> 동시처리 코드 효율화
        *   서블릿 기술 사용 안 함
    *   문제
        *   기술적 난도
        *   RDB 지원 부족
        *   MVC의 쓰레드 모델도 아직 충분히 빠름
        *   실무 사용 적음

**자바 뷰 템플릿 역사**: HTML을 편리하게 동적 생성하는 뷰 기능

*   JSP: 느린 속도, 기능 부족
*   프리마커(Freemarker), 벨로시티(Velocity): 속도 문제 해결, 다양한 기능
*   **타임리프**(Thymeleaf)
    *   네츄럴 템플릿: HTML의 형태를 유지하면서 뷰 템플릿 적용 가능
    *   스프링 MVC와 강력한 기능 통함
    *   가장 낫지만, 성능 측면에서는 프리마커, 벨로시티가 더 나음



서블릿
---

*   톰캣 설치 -> 서블릿 코드를 클래스 파일로 빌드해서 올림 -> 톰캣 서버 실행
*   스프링 부트의 경우 내장 톰캣
*   `@ServletComponentScan`: 서블릿 자동 등록
*   `@WebServlet` 서블릿 애노테이션
    *   name: 서블릿 이름
    *   urlPatterns: URL 매핑
    *   서블릿명과 URL 매핑은 겹치면 안 됨
*   **application properties**에 `logging.level.org.apache.coyote.http11=debug` 개발 용으로 유용한 로그(운영 서버의 경우 성능 저하)



### HttpServletRequest

**HttpServletRequest**

*   HTTP 요청 메시지를 파싱해주며, 그 결과를 객체에 담아 제공
*   부가 기능 제공
    *   **임시 저장소** 기능: 해당 HTTP 요청이 끝날 때까지 유지
        *   저장: `request.setAttribute(name, value)`
        *   조회: `request.getAttribute(name)`
    *   **세션 관리 기능**: `request.getSession(create:true)`
*   request.getLocale(): Accept-Language 최고 순위를 반환



### HTTP 요청 데이터 - 개요

HTTP CLI -> SRV 데이터 전달 방법

1.  GET - 쿼리 파라미터
2.  POST - HTML Form
3.  HTTP message body에 데이터를 직접 담아 요청



### HTTP 요청 데이터 - GET 쿼리 파라미터

*   `URL?queryparam1&queryparam2` 형식
*   `request.getParameter("name");` 형태로 조회
*   `request.getParameterValues("name");`는 복수의 동일 명칭 값이 있을 때 배열로 반환



### HTTP 요청 데이터 POST HTML Form

**특징**

*   content-type: `application/x-www-form-urlencoded`
*   메시지 바디에 쿼리 파라미터와 같은 형식으로 데이터 전달: `username=hello&age=20`
*   서버 입장에서는 GET 쿼리 파라미터 방식과 동일하게 조회
*   단 이 경우에는 `content-type`이 지정



### HTTP 요청 데이터 - API 메시지 바디

*   주로 POST, PUT, PATCH 사용  
    **단순 텍스트**  
    inputStream 이용해서 데이터 읽음 -> 바이트코드  
    바이트 <-> 문자 전환은 인코딩 명시

**JSON**

*   JSON은 객체로 바꿔서 사용
*   ObjectMapper.readValue() 이용해서 파싱(Jackson 라이브러리)
*   HTML Form 데이터의 경우도 Body 통한 전달이라 InputStream으로 읽는 것이 가능하지만 파라미터 조회가 더 좋음.



### HttpServletResponse

**역할**: 응답 메시지 생성

*   응답코드 지정
*   헤더 생성
*   바디 생성
*   편의 기능 제공: Content-Type, 쿠키, Redirect

**응답코드**

*   `response.setStatus(HttpServletResponse.SC_OK)`  
    `response.setStatus(200)`도 가능하지만, 위 처럼 적는 편이 더 나음

    ```java
    private void content(HttpServletResponse response) {  
      //Content-Type: text/plain;charset=utf-8  
     //Content-Length: 2 //response.setHeader("Content-Type", "text/plain;charset=utf-8");  response.setContentType("text/plain");  
      response.setCharacterEncoding("utf-8");  
      //response.setContentLength(2); //(생략시 자동 생성)  
    }
    ```
    
* Content-Type, 문자 인코딩을 `setHeader`이용하여 지정할 수도 있으나, `response.setContentType("text/plain")` 등 이용하여 간단하게 지정하는 것도 가능하다.

*   Content-Length 지정을 생략시 자동 생성된다.

**쿠키 설정**

```java
private void cookie(HttpServletResponse response) {  
    //Set-Cookie: myCookie=good; Max-Age=600;  
 //response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");  Cookie cookie = new Cookie("myCookie", "good");  
  cookie.setMaxAge(600); //600초  
  response.addCookie(cookie);  
}
```


*   Cookie 객체 생성하여 간단하게 설정 가능

**Redirect 설정**

```java
private void redirect(HttpServletResponse response) throws IOException {  
    //Status Code 302  
 //Location: /basic/hello-form.html  
```

```java
 //response.setStatus(HttpServletResponse.SC_FOUND); //302 //response.setHeader("Location", "/basic/hello-form.html");
  response.sendRedirect("/basic/hello-form.html");  
}
```


*   주석처리된 2줄로도 설정 가능, 아래의 한 줄로 간단하게 설정 가능.



**Message Body 설정**

```java
PrintWriter writer = response.getWriter(); // InputStream이용할수도 있다
writer.println("OK"); //(print/println);
```



### HTTP 응답 데이터

응답 메시지의 내용:

1.  단순 텍스트(`writer.println("OK");`)
2.  HTML 응답
3.  HTTP API - Message Body JSON 응답

**단순 텍스트, HTML**

```java
response.setContentType("text/html");  
response.setCharacterEncoding("utf-8");
```


**API JSON**

```java
response.setContentType("application/json");  
response.setCharacterEncoding("utf-8");	// 사실 불필요
String result = objectMapper.writeValueAsString(helloData);  
response.getWriter().write(result);
```


*   application/json은 utf-8 사용하게 정의되어 있음, 추가할 필요 없음.



서블릿, JSP, MVC 패턴
----------------

*   동시성 문제를 고려하여, 실무에서는 HashMap 대신 ConcurrentHashMap, AtomicLong 사용을 고려하라.



### 서블릿을 이용한 개발

* 서블릿으로 직접 HTML 전달 시 일일히 HTML 적어줘야 하기에 실수할 가능성 높음

  ```java
  w.write("<html>\n" +  
          "<head>\n" +  
          " <meta charset=\"UTF-8\">\n" +  
          "</head>\n" +  
          "<body>\n" +  
          "성공\n" +  
          "<ul>\n" +  
          " <li>id="+member.getId()+"</li>\n" +  
          " <li>username="+member.getUsername()+"</li>\n" +  
          " <li>age="+member.getAge()+"</li>\n" +  
          "</ul>\n" +  
          "<a href=\"/index.html\">메인</a>\n" +  
          "</body>\n" +  
          "</html>");
  ```

*   동적인 HTML 생성 가능  
    \-> 하지만 서블릿으로는 HTML 생성이 매우 불편하다.  
    \-> **템플릿 엔진**: 동적인 변경이 필요한 부분만 코드를 적용



### JSP 이용한 개발

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>  
<html>  
<head>  
 <title>Title</title>  
</head>  
<body>  
<form action="/jsp/members/save.jsp" method="post">  
 username: <input type="text" name="username" />  
 age: <input type="text" name="age" />  
 <button type="submit">전송</button>  
</form>  
</body>  
</html>  
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
```


* JSP 방식

  ```jsp
  <%  
  // request, response 사용 가능  
    MemberRepository memberRepository = MemberRepository.getInstance();
   System.out.println("save.jsp");  
   String username = request.getParameter("username");  
   int age = Integer.parseInt(request.getParameter("age"));  
     
   Member member = new Member(username, age);  
   System.out.println("member = " + member);  
   memberRepository.save(member);  
  %>
  ```

  - <% (자바코드) %> 자바 로직 사용 가능

* 표시 없는 경우 위의 서블릿에서 한 것처럼 프린트 한 것과 같다고 보면 됨.

  ```jsp
  <%@ page import="java.util.List" %>  
  <%@ page import="logos.servlet.domain.member.MemberRepository" %>
  <%@= java code %>
  ```

* import 해줘야 함.

* `<%@=` 자바 코드를 출력

*   JSP를 사용해도 아직 부족한 점이 많음:
    *   로직과 뷰가 섞여 있음
    *   JSP가 과다한 역할을 맡고 있음  
        \-> MVC 패턴 등장



### MVC 패턴: 개요

**문제상황**: 위와 같이

*   단일 서블릿이나 JSP만으로 비즈니스 + 뷰 처리 -> 과도한 역할, 유지보수 힘듦
*   **변경의 라이프 사이클: 변경 주기가 다르다면 분리해야 한다!**
*   뷰 템플릿은 화면 렌더링 최적화되어 있음 -> 기능에 맞게 담당하게 해야

**MVC**

*   컨트롤러: HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행, 뷰에 전달할 결과 데이터를 조회해서 모델에 담음
*   모델: 뷰에 출력할 데이터를 담아둠 - 뷰의 역할 분할을 도움
*   뷰: 모델에 담긴 데이터를 이용, 화면을 그림(HTML 생성 등)
*   MVC 패턴1: 컨트롤러가 비즈니스 로직까지 -> 과도한 역할
*   MVC 패턴2: 컨트롤러는 컨트롤러 로직, 서비스, 리포지토리는 비즈니스 로직, 데이터 접근  
    \-> 컨트롤러는 비즈니스 로직이 있는 서비스를 호출



### MVC 패턴 : 적용

*   컨트롤러: 서블릿
*   뷰: JSP
*   모델: HttpServletRequest(request는 데이터 저장소를 가지고 있음: .set/getAttribute
*   액션이 “/save”(절대경로)가 아니라 “save”(상대경로)
*   `dispather.forward()`: 다른 서블릿이나 JSP로 이동하는 기능, 서버 내부에서 다시 호출이 발생
    *   vs redirect: 리다이렉트와는 다르다. 포워드는 서버 내부의 호출이기에 클라이언트가 호출 불가, 리다이렉트는 클라이언트에 응답 -> redirect 경로로 클라이언트의 재요청
*   컨트롤러를 통해서 호출하고 싶은 경우, WEB-INF에 저장
*   `age=<%=((Member)request.getAttribute("member")).getAge()%>`로 값을 얻어올 수 있지만,  
    `id=${member.id}`: JSP 표현식으로 간단하게 표현 가능. ->프로퍼티 접근법
*   `<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>`: jstl 사용을 위함

    <table>  
     <thead>  
     <th>id</th>  
     <th>username</th>  
     <th>age</th>  
     </thead>  
     <tbody>  
     <c:forEach var="item" items="${members}">  
     <tr>  
     <td>${item.id}</td>  
     <td>${item.username}</td>  
     <td>${item.age}</td>  
     </tr>  
     </c:forEach>  
     </tbody>  
    </table>
    
*   jstl을 이용해서 반복 출력을 적용



### MVC 패턴: 한계

*   포워드 중복: RequestDispather 생성하고, foward로 뷰 이동하는 코드가 중복 호출됨
*   ViewPath 중복: ViewPath 앞 뒷부분 중복 발생, 다른 뷰로 변경하려면 코드를 다 바꾸게됨
*   사용하지 않는 코드: response 등 사용하지 않게 되는 코드가 많음, 또한 이러한 코드는 테스트 케이스 작성도 어려움
*   공통 기능 처리가 어려움: 메서드로 뽑아내도, 호출이 필요하며, 안 호출하는 실수 가능  
    \-> **공통 처리**의 문제: 서블릿(컨트롤러) 호출 이전에 공통 기능이 처리해야 한다.  
    \-> **프론트 컨트롤러(Front Controller) 패턴**



MVC 프레임워크 만들기
-------------

### 프론트 컨트롤러 패턴 소개

**도입 이전에는**: 공통로직을 직접적으로 컨트롤러 이전에 넣어줬어야 함  
**도입 후에는**: 공통 로직을 담당하는 프론트 컨트롤러를 통해서 컨트롤러로 접근  
**FrontController 패턴 특징**

*   프론트 컨트롤러 서블릿 하나로 CLI 요청 받음
*   상황에 맞는 컨트롤러를 찾아 호출
*   입구를 하나로!
*   공통 처리 가능
*   프론트 컨트롤러 외 컨트롤러는 서블릿 안 써도 됨!
*   **스프링 웹 MVC와의 연관**
    *   스프링 웹 MVC의 핵심
    *   DispatherServlet이 프론트 컨트롤러 패턴



### 프론트 컨트롤러 도입 - v1

*   서블릿 컨트롤러가 매핑을 통해 개별 컨트롤러들(컨트롤러 인터페이스의 구현)을 호출, 각 컨트롤러들의 오버라이드 된 메서드를 호출하여 공통 코드를 이용해서 각각의 로직 실행 가능.



### View 분리 - v2

*   dispatcher 생성, foward 부분을 MyView로 분리, 컨트롤러의 process 메서드가 MyView 반환, 그 MyView의 render()를 프론트 컨트롤러가 호출
*   MyView를 인터페이스로 만들고 구현하면 JSP 말고 다른 템플릿 엔진 등으로도 쉽게 변경 가능



### Model 추가 - v3

*   서블릿 종속성 제거
    *   불필요한 request, response 전달을 지움 -> Model 객체 이용
*   뷰 이름 중복 제거
    *   개별 컨트롤러는 뷰의 논리 이름만 반환, 물리 이름은 프론트 컨트롤러에서 처리하게함  
        \-> ViewResolver
    *   뷰 위치가 변경되더라도 프론트 컨트롤러만 수정하면 됨 -> 수정 용이
        *   **변경 지점을 하나로 만들어라!**
*   ModelView
    *   Model 역할에 View 이름까지 전달



### 단순하고 실용적인 컨트롤러 - v4

*   구조적 개선이 있었지만, 생산성이 낮다는 점을 개선 -> 계속 모델 뷰를 만드는 수고가 없음
*   프론트 컨트롤러는 모델 생성, 컨트롤러 호출 시 모델 전달
*   컨트롤러는 뷰 논리 이름 그대로 반환



### 유연한 컨트롤러

*   단일 프로젝트에서 다양한 종류의 컨트롤러를 사용하고 싶을 때(서로 다른 인터페이스)

**어댑터 패턴**

*   프론트 컨트롤러가 다양한 방식의 컨트롤러를 어댑터를 통해 사용 가능
*   기존에는 프론트 컨트롤러 -> 핸들러(컨트롤러) 즉각 호출
*   이제는 프론트 컨트롤러 -> 핸들러 매핑 정보 -> **핸들러 어댑터** 목록조회 -> 이를 바탕으로 핸들러 어댑터 호출 -> 핸들러 호출
*   **핸들러**: 컨트롤러보다 더 넓은 의미 -> 컨트롤러 뿐만 아니라 다른 경우에도 사용
*   프론트 컨트롤러 - **핸들러 매핑 정보 찾음 - 목록에서 어댑터 찾음 -> 어댑터 컨트롤러 호출, 모델뷰 반환받음** -> 뷰리졸버 -> 뷰
*   OCP를 지키면서 기능 추가 가능.
*   다수 인터페이스의 컨트롤러를 편리하게 사용 가능하다.



스프링 MVC - 구조 이해
---------------

### 스프링 MVC 전체 구조

**DispatherSerlvet**

*   스프링 MVC의 프론트 컨트롤러
*   부모 클래스에서 HttpServlet 상속
*   스프링 부트는 `DispatcherServlet`을 등록하면서 모든 경로(`urlPatterns="/"`에 대해서 매핑한다.
    *   별도 규정이 있을 경우, 그것이 먼저 동작, 등록
*   요청 흐름
    *   서블릿이 호출되면 `service()`호출, . . . , `doDispatch()`호출
*   `doDispatch()`: 핸들러 찾기, 핸들러 어댑터 찾기 -> 실제 호출 . . .처럼 예제와 같은 방식
*   동작 순서
    1.  핸들러 조회
    2.  핸들러 어댑터 조회
    3.  핸들러 어댑터 실행
    4.  ModelAndView 반환
    5.  viewResolver 호출
    6.  View 반환
    7.  뷰 랜더링



### 핸들러 매핑과 핸들러 어댑터

*   애노테이션 기반 컨트롤러 이전 스타일

    ```java
    @Component("/springmvc/old-controller")  
    public class OldController implements Controller { 
        @Override  
          public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {  
            System.out.println("OldController.handleRequest");  
     return null;  }  
    }
    ```

**스프링 부트가 등록하는 대략적 순서**  

- **HandlerMapping**  
  1. `RequestMappingHandlerMapping`: `@RequestMapping`  
  2. `BeanNameUrlHandlerMApping`: url이름과 일치하는 스프링 빈의 이름  
- **HandlerAdapter**  
  1. `RequestMappingHandlerAdapter`  
  2.  `HttpRequestHandlerAdapter`  
  3.  `SimpleControllerHandlerAdapter`: Controller 인터페이스(`.supports(handler)` 검사)

*   실행 순서
    1.  핸들러 매핑에서 핸들러 조회
    2.  핸들러 어댑터 조회
    3.  핸들러 어댑터 실행
*   가장 많이 사용하는 것: `@RequestMapping`



### 뷰 리졸버

application.properties

```properties
spring.mvc.view.prefix=/WEB-INF/views/  
spring.mvc.view.suffix=.jsp
```

- 스프링 부트가 `InternalResourceViewResolver`를 자동등록.

**스프링 부트가 등록하는 뷰 리졸버 순서**  

1. `BeanNameViewResolver`: 빈 이름으로 뷰 찾아서 반환(엑셀 파일 생성 등)  
2. `InternalResourceViewResolver`: JSP를 처리할 수 있는 뷰 반환



### 스프링 MVC - 시작하기

**`@RequestMapping`**

*   애노테이션 기반의 유연하고 실용적인 컨트롤러 사용 가능
*   요청 정보를 매핑
*   대응하는 핸들러 매핑 및 어댑터:
    *   `RequestMappingHandlerMapping`
    *   `RequestMappingHandlerAdapter`
*   **`@Controller`**
    *   컴포넌트 스캔의 대상
    *   애노테이션 기반 컨트롤러로 인식됨
*   클래스에 `@RequestMapping` 혹은 `@Controller`가 클래스에 적혀 있으면 인식됨
    *   `@RequestMapping` 경우, `@Component`붙어서 스프링 빈에 등록시키자. (`@Bean`으로 직접 등록하거나)
    *   `RequestMappingHandlerMapping`의 `isHander(Class<?> beanType)`메서드는 `Controller.class`||`RequestMapping.class`애노테이션 소유 여부를 검사하기 때문이다.



### 스프링 MVC - 컨트롤러 통합

*   단일 컨트롤러에 `@RequestMapping` 붙은 여러 메서드 사용하여 한 컨트롤러 안에서 통합이 가능해짐.
*   컨트롤러에 `@RequestMapping(공통 url)` + 메서드의 (부분url)로 호출 가능, 공통 url과 동일한 url의 경우 `@RequestMapping`만 작성.



### 스프링 MVC - 실용적인 방식

* ModelView 직접 만들어 반환해서 생기는 불편을 해소

* **실무에서 주로 사용하는 방식**

  ```java
  //    @RequestMapping(name = "/save", method = RequestMethod.POST)  
    @PostMapping("/save")
    public String save(@RequestParam("username") String username,  
    @RequestParam("age") int age,  
    Model model) {  
        Member member = new Member(username, age);  
        memberRepository.save(member);  
  
    model.addAttribute("member", member);  
   return "save-result";  
  }
  ```

* `@RequestParam`: GET 쿼리 파라미터, POST Form 모두 지원

* 반환값을 String으로

*   HTTP메서드를 지정: method= 에서 `@PostMapping`식으로



스프링 MVC - 기본 기능
---------------

*   **War**: 서블릿 컨테이너(WAS, 톰캣 등)을 별도로 설치하고 거기 빌드된 파일을 넣을 경우, JSP를 쓸 경우
*   **Jar**: 내장 서버(톰캣 등) 사용 시
    *   스프링 부트 사용시: `/resources/static/index.html`을 자동적으로 welcome page로 처리



### 로깅 Logging

*   운영 시스템에서는 sout 등의 시스템 콘솔이 아니라, 로깅 라이브러리를 사용하여 로그를 출력
*   **로깅 라이브러리**: 스프링 부트 기본 로깅 라이브러리 `spring-boot-starter-logging`은 기본으로 다음 로깅 라이브러리를 사용:
    *   SLF4J - [http://www.slf4j.org](http://www.slf4j.org)
    *   Logback - [http://logback.qos.ch](http://logback.qos.ch)
    *   **SLF4J**는 여러 로그 라이브러리를 통합한 **인터페이스**, **Logback**은 **구현체**


*   application.properties: 어느 수준까지 로그를 볼 것인지
    *   trace, debug, info, warn, error 순서(기본 설정은 **info**: `logging.level.root.info`)
    *   **서비스 레벨**은 **info**, **개발**에서나 **debug(주로)**, trace 표시(로그의 장점)  

    *   특정 경로에 대한 로깅 레벨 설정
    
        `logging.level.logos.springmvc=trace`

*   **RestController**: 메시지 바디에 그 String 등을 바로 반환(REST API의 REST)
    
    *   `@Controller` 반환값이 String인 경우 뷰의 이름으로 취급됨
    *   `@RestController`는 반환 값으로 뷰를 찾지 않고 HTTP 메시지 바디에 바로 입력
*   **올바른 로그 사용법**
    
    *   `log.trace("data" + data)`: java는 data에 값을 넣고, "data"문자열과 얻은 값 “Spring” **문자열을 연산**한다. 이 과정에서 리소스가 사용되는데, 만일 info level까지 로그를 출력하는 상황임에도, 이런 방식으로 로그를 작성할 경우 trace, debug와 같이 **실제 출력이 발생하지 않는데도 무의미한 리소스 낭비**가 발생한다.
    *   `log.trace("data={}", data)`: 메서드에 파라미터를 넘기기만 함, 무의미한 연산 없음.
*   **로그 사용의 장점**
    
    *   쓰레드 사용/클래스 이름 등의 부가 정보를 볼 수 있음, 출력 형태 조절
    *   상황에 따라 로그 출력 레벨 조절 가능 - 간단한 설정 파일 변경만으로
    *   설정을 통해 콘솔 뿐만 아니라 파일, 네트워크 등 로그를 다른 위치에 남길 수 있으며, 파일 저장 시에는 날짜, 용량 등에 따라 분할도 가능
    *   성능 자체(내부 버퍼링, 멀티 쓰레드 등)



### 요청 매핑

*   **`@RequestMapping`**: 배열\[\]로 제공하므로, 두 개씩 넣을 수도 있다.
    
    *   ex) `@RequestMapping({"hello-basic", "/hello-go"})`
    *   `/hello-basic`과 `/hello-basic/`은 다른 url이지만 스프링은 이 둘을 같은 요청으로 매핑
    *   매핑 메소드가 일치하지 않을 경우 스프링 MVC가 405(Method Not Allowed) 반환
    *   **경로 변수(PathVariable) 사용**
        *   리소스 경로에 식별자를 넣는 스타일
        *   `@PathVariable`로 간편 조회
        *   매핑 주소의 값 명칭 `{userId}`와 `@PathVariable("value")`붙은 패러미터 `String {userId}`가 동일하다면 `@PathVariable`의 괄호를 지워도 작동.
    
    ```java
    @GetMapping("/mapping/{userId}")  
    	public String mappingPath(@PathVariable("userId") String data) {  
        log.info("mappingPath userId={}", data);  
    	return "ok";
    }
    ```

경로 변수는 여러 개 사용도 가능하다:

```java
@GetMapping("/mapping/users/{userId}/orders/{orderId}")
public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
    log.info("mappingPath userId={}, orderId={}", userId, orderId);
     return "ok";
 }
```


특정 **패러미터**가 있어야 한다는 **조건**을 추가할 수도 있다:

```java
@GetMapping(value = "/mapping-param", params = "mode=debug")  
public String mappingParam() {  
    log.info("mappingParam");  
 return "ok";  
}
```


유사하게, 특정 **헤더 조건**을 요구할 수도 있다:

```java
@GetMapping(value = "/mapping-header", headers = "mode=debug")  
public String mappingHeader() {  
    log.info("mappingHeader");  
 return "ok";  
}
```

**미디어 타입 조건**을 요구하는 매핑도 가능하다:

```java
/**  
 * Content-Type 헤더 기반 추가 매핑 Media Type  
 * consumes="application/json"
 * consumes="!application/json"
 * consumes="application/*"
 * consumes="*\/*"
 * MediaType.APPLICATION_JSON_VALUE  <- 추천
*/
@PostMapping(value = "/mapping-consume", consumes = "application/json")  // consumes= MediaType.APPLICATION_JSON_VALUE
public String mappingConsumes() {  
    log.info("mappingConsumes");  
 return "ok";  
}
```


* 만일 타입이 일치하지 않는다면 415 상태코드(Unsupported Media Type)

*   consume은 컨텐츠 소비 시, 반대는 produce: (accept 헤더 기반)

    ```java
    @PostMapping(value = "/mapping-produce", produces = "text/html")  
    public String mappingProduces() {  
        log.info("mappingProduces");  
     return "ok";  
    }
    ```



### 요청 매핑 - API 예시

```java
@RestController  
@RequestMapping("/mapping/users")  
public class MappingClassController {  

    @GetMapping  
  public String user() {  
        return "get users";  
  }  

    @PostMapping  
  public String addUser() {  
        return "post user";  
  }  

    @GetMapping("/{userId}")  
    public String findUser(@PathVariable String userId) {  
        return "get userId=" + userId;  
  }  

    @PatchMapping("/{userId}")  
    public String updateUser(@PathVariable String userId) {  
        return "update userId=" + userId;  
  }  

    @DeleteMapping("/{userId}")  
    public String deleteUser(@PathVariable String userId) {  
        return "delete userId=" + userId;  
  }  

}
```



### HTTP 요청 - 기본, 헤더 조회

```java
@Slf4j  
@RestController  
public class RequestHeaderController {  

    @RequestMapping("/headers")  
    public String headers(HttpServletRequest request,  
  HttpServletResponse response,  
  HttpMethod httpMethod,  
  Locale locale,  
  @RequestHeader MultiValueMap<String, String> headerMap,  
  @RequestHeader("host") String host,  
  @CookieValue(value = "myCookie", required = false) String cookie) {  
        log.info("request={}", request);  
  log.info("response={}", response);  
  log.info("httpMethod={}", httpMethod);  
  log.info("locale={}", locale);  
  log.info("headerMap={}", headerMap);  
  log.info("header host={}", host);  
  log.info("myCookie={}", cookie);  

 return "ok";  
  }  
}
```


*   LocaleResolver: 기본 제공 외의 locale 사용
*   **`MultiValueMap`**: 하나의 키에 여러 값을 받을 수 있는 Map, List로 반환한다.

> 참고 > @Conroller 의 사용 가능한 파라미터 목록은 다음 공식 메뉴얼에서 확인할 수 있다.  
> [https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-annarguments](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-annarguments)  
> 참고  
> @Conroller 의 사용 가능한 응답 값 목록은 다음 공식 메뉴얼에서 확인할 수 있다.  
> [https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-annreturn-types](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-annreturn-types)



### HTTP 요청 파라미터 - 쿼리 파라미터, HTML Form

*   요청의 3가지 방식
    1.  GET-쿼리 파라미터(검색, 필터, 페이징 등)
    2.  POST-HTML Form(회원가입, 상품 주문 등)
    3.  HTTP message body: json, xml, text(HTTP API) -> POST, PUT, PATCH
*   요청 파라미터(request parameter) 조회: 1, 2 해당
*   Jar 사용시 Web-app 루트 사용 불가, static에 리소스

> **Jar에 JSP를 넣기**  
> SpringBoot에서 기본 View는 타임리프  
> 타임리프는 src/main/resources/templates에 만들면 특별한 설정없이 자동으로 View를 찾음  
> JSP는 jar로 묶을 때 src/main/webapp/WEB-INF/jsp에 위치하다보니 jar에 포함되지 않았음  
> 해결 방법은?  
> **src/main/resources/META-INF/resources/WEB-INF/jsp**에 JSP파일을 두면 jar에 포함되고 view도 잘 찾는다.  
> 출처: [https://seongtak-yoon.tistory.com/24](https://seongtak-yoon.tistory.com/24)



### HTTP 요청 파라미터 - @RequestParam

```java
@ResponseBody  
@RequestMapping("/request-param-v2")  
public String requestParamV2(  
        @RequestParam("username") String memberName,  
  @RequestParam("age") int memberAge) {  

    log.info("memberName={}, memberAge={}", memberName, memberAge);  

 return "ok";  
}
```


*   **`@ResponseBody`**
    
    *   컨트롤러 클래스가 `@RestController`가 아니라 `@Controller`인데, 리턴 타입이 String이면 ok라는 뷰를 찾게 된다(뷰 리졸버), 이럴 때 이 매핑만 직접 바디에 스트링을 전달하는 식으로 하고 싶다면 **`@ResponseBody`** 붙이자.
    
*   수정된 버전
    
    ```java
    @ResponseBody  
    @RequestMapping("/request-param-v3")  
    public String requestParamV3(  
            @RequestParam String username,  
      @RequestParam int age) {  
        log.info("memberName={}, memberAge={}", username, age);  
         return "ok";  
    }
    ```
    
* 수정된 버전2

  ```java
  @ResponseBody  
  @RequestMapping("/request-param-v4")  
  public String requestParamV4(String username, int age) {  
      log.info("memberName={}, memberAge={}", username, age);  
      return "ok";  
  }
  ```

* **`@RequestParam`**
  *   파라미터 이름으로 바인딩
  *   `@RequestParam("name")`의 name이 파라미터 변수명과 동일하다면 괄호는 생략 가능하다(수정된 버전)
  *   위의 경우, 패러미터가 String, int, Integer 등 단순 타입일 경우 `@RequestParam` 자체도 생략 가능하다(수정된 버전2).
      *   단 이 경우, `@RequestParam(Required=false)`로 설정된다.

  ````java
  @ResponseBody  
  @RequestMapping("/request-param-required")  
  public String requestParamRequired(  
          @RequestParam(required = true) String username,  
    @RequestParam(required = false) Integer age) {
      log.info("memberName={}, memberAge={}", username, age);  
       return "ok";  
  }
  ````

* **필수 파라미터 여부 - `@RequestParam(required = false)`**
  *   false의 경우 파라미터가 없어도 된다.

  *   `@RequestParam` 생략 시 기본적으로 false다.

  *   **파라미터가 오지 않는(null)의 경우와, 빈 문자를 보내는 경우"" 구별**
      *   `http://localhost:8080/request-param-required` :이 경우 username은 null이기에 bad request며, `MissingServletRequestParameterException`발생하지만,
      *   `http://localhost:8080/request-param-required?username` 혹은 `http://localhost:8080/request-param-required?username=`: 이 경우에는 400 bad request나 예외가 발생하지 않는다. 로그를 보면 `memberName=, memberAge=null`이다.
      *   **null과 빈 문자 구별에 유의하자!**

  *   **int가 아니라 Integer를 사용해야 하는 이유**
    *   위에서 age 파라미터의 자료형을 int로 받을 경우 required=false임에도 `IllegalStateException` 발생한다. 원시 자료형 int는 null값으로 변환(translated) 될 수 없기 때문이다.

    *   > java.lang.IllegalStateException: Optional int parameter ‘age’ is present but cannot be translated into a null value due to being declared as a primitive type. Consider declaring it as object wrapper for the corresponding primitive type.

    *   **해결법**

        *   Integer 같은 Wrapper 클래스 사용
        *   defaultValue 적용

    ```java
    @ResponseBody  
    @RequestMapping("/request-param-default")  
    public String requestParamDefault(  
        @RequestParam(defaultValue = "guest") String username,  
        @RequestParam(defaultValue = "-1") int age) {  
        log.info("memberName={}, memberAge={}", username, age);  
        return "ok";  
    }
    ```

* **defaultValue**

* `http://localhost:8080/request-param-default`(null) 혹은 `http://localhost:8080/request-param-default?username=`(빈 문자)의 경우에도 로그가 `memberName=guest, memberAge=-1`출력되는 것을 볼 수 있다.

  *   이 경우 required가 불필요하다.

      ```java
      @ResponseBody  
      @RequestMapping("/request-param-map")  
        public String requestParamMap(@RequestParam Map<String, Object> paramMap) {  
      
            log.info("memberName={}, memberAge={}", paramMap.get("username"), paramMap.get("age"));  
      
      /*        for (Object key : paramMap.keySet()) {  
                     log.info("{}={}", key, paramMap.get(key));
                     }  
                          paramMap.keySet().iterator().forEachRemaining( (key) ->    
                       log.info("{}={}", key, paramMap.get(key)) );*/  
            return "ok";  
        }  
      }
      ```

* **`@RequestParamMap`**: 파라미터를 맵으로 조회
  *   `Map(key=value)` 형태나 `MultiValueMap(key=[value1, value2 . . .])` 형태도 가능
  *   파라미터가 한 개임이 명확하지 않다면 `MultiValueMap` 사용할 것, 하지만 보통의 경우 한 개

### 

### HTTP 요청 파라미터 - @ModelAttribute

*   lombok `@Data`
    * `@Getter` `@Setter` `@ToString` `@EqualsAndHashCode` `@RequiredArgsConstructor`를 자동적용

      ```java
      @ResponseBody  
      @RequestMapping("/model-attribute-v1")  
      public String ModelAttributeV1(@ModelAttribute HelloData helloData) {  
          log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());  
        log.info("hellodata={}", helloData);  
      
       return "ok";  
      }
      ```
    
*   **`@ModelAttribute`**
    *   실행 순서
        1.  `HelloData`객체 생성
        2.  요청 파라미터 이름을 객체 프로퍼티에서 찾아 setter로 값을 바인딩
    *   `BindException`: 프로퍼티와 자료형이 맞지 않는 파라미터 값으로 바인딩 할 때
    *   `@ModelAttribute` 생략 가능하다.
    
*   **애노테이션 생략 규칙**
    *   `String,`, `int`, `Integer` 등 단순 타입 = `@RequestParam`
    *   그 외 = `@ModelAttribute` (단 argument resolver로 지정해 둔 타입은 제외!)

### HTTP 요청 메시지 - 단순 텍스트

```java
@Slf4j  
@Controller  
public class RequestBodyStringController {  

    @PostMapping("/request-body-string-v1")  
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        ServletInputStream inputStream = request.getInputStream();  
  String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);  

  log.info("messageBody={}", messageBody);  

  response.getWriter().write("ok");  
  }  

    @PostMapping("/request-body-string-v2")  
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {  
      
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);  

  log.info("messageBody={}", messageBody);  

  responseWriter.write("ok");  
  }  

    @PostMapping("/request-body-string-v3")  
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {  
      
        String messageBody = httpEntity.getBody();  

  log.info("messageBody={}", messageBody);  

 return new HttpEntity<>("ok");  
  }  

}
```


*   **HttpEntity**: HTTP header, body 편리하게 조회 가능
    * 메시지 바디 정보를 직접 조회
    
    *   응답에서 사용
        *   메시지 바디 정보 직접 반환
        *   헤더 정보 포함 가능
        *   view 조회 X
        
    * `RequestEntity`, `ResponseEntity("message body", 상태코드)`도 사용 가능
    
      ```java
      @ResponseBody  
      @PostMapping("/request-body-string-v4")  
      public String requestBodyStringV4(@RequestBody String messageBody) throws IOException {  
          log.info("messageBody={}", messageBody);  
      	return "ok";  
      }
      ```
    
*   **`@RequestBody` `@ResponseBody` 사용 방식**
    
    *   헤더 정보가 필요하다면 `@RequestHeader`나 `HttpEntity`사용
    
*   스프링 MVC는 메시지 바디 -> 문자/객체로 변환 전달: **HTTP 메시지 컨버터**



### HTTP 요청 메시지 - JSON

```java
@Slf4j  
@Controller  
public class RequestBodyJsonController {  

    private ObjectMapper objectMapper = new ObjectMapper();  

  @PostMapping("/request-body-json-v1")  
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        ServletInputStream inputStream = request.getInputStream();  
  String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);  

  log.info("messageBody={}", messageBody);  

  HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);  
  log.info("userName={}, age={}", helloData.getUsername(), helloData.getAge());  

  response.getWriter().write("ok");  
  }  

    @ResponseBody  
 @PostMapping("/request-body-json-v2")  
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {  

        log.info("messageBody={}", messageBody);  
  HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);  
  log.info("userName={}, age={}", helloData.getUsername(), helloData.getAge());  

 return "ok";  
  }  

    @ResponseBody  
 @PostMapping("/request-body-json-v3")  
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {  

        log.info("userName={}, age={}", helloData.getUsername(), helloData.getAge());  

 return "ok";  
  }  
}
```


* `HttpEntity`나 `@RequestBody`를 사용시 HTTP 메시지 컨버터가 메시지 바디의 내용을 변환해 준다.

* `@RequestBody` 생략 불가 -> 생략시 `@ModelAttribute`적용되버림

  ```java
  @ResponseBody  
  @PostMapping("/request-body-json-v4")  
  public String requestBodyJsonV4(@RequestBody HttpEntity<HelloData> data) {  
  
      HelloData helloData = data.getBody();  
  
    log.info("userName={}, age={}", helloData.getUsername(), helloData.getAge());  
  
   return "ok";  
  }  
  
  @ResponseBody  
  @PostMapping("/request-body-json-v5")  
  public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) {  
  
      log.info("userName={}, age={}", helloData.getUsername(), helloData.getAge());  
  
   return helloData;  
  }
  ```

* `HttpEntity` 사용 가능(`@RequestBody` 생략)

* 파라미터로 받은 객체를 반환 가능

*   **요청, 응답흐름**
    *   **`@RequestBody` 요청**: JSON요청 -> HTTP 메시지 컨버터 -> 객체
        *   **content-type: application/json 주의!!**
    *   **`@ResponseBody` 응답**: 객체 -> HTTP 메시지 컨버터 -> JSON 응답
        *   **Accept: application/json 주의!!**

### HTTP 응답 - 정적 리소스, 뷰 템플릿

*   서버에서 응답 데이터를 만드는 방법
    
    1.  정적 리소스 - 브라우저에 정적인 html, css, js
    2.  뷰 템플릿: 동적 html
    3.  HTTP 메시지 - HTTP API의 경우, 메시지 바디에 JSON
*   **정적 리소스**
    
    *   스프링 부트가 사용하는 정적 리소스 디렉토리
        *   `/static` `/public` `/resources` `/META_INF/resources`
    *   `src/main/resources` 리소스 보관 장소
    *   변경 없이 해당 파일을 실행
*   **뷰 템플릿**
    
    *   뷰 템플릿을 거쳐 html 생성, 뷰가 응답을 만들어 전달
    *   스프링 부트 기본 경로: `src/main/resources/templates`

*   String 반환하는 경우: 뷰 리졸버가 뷰를 찾고 렌더링(`@ResponseBody` 구별 주의)
*   void 반환하는 경우: `@Controller` 사용 && HTTP 메시지 바디를 처리하는 파라미터 없는 경우(`HttpServletResponse`나 `OutputStream(Writer)` 등이 없는 경우)
    *   요청 url을 참고하여 논리 뷰 이름으로 사용
    *   명시성이 낮고, 조건에 맞지 않는 상황도 종종 있음
*   **Thymeleaf**
    *   `bulid.gradle`에 등록
    *   `application.properties`에서 prefix와 suffix 설정



### HTTP 응답 - HTTP API, 메시지 바디에 직접 입력

```java
@Slf4j  
@Controller  
public class ResponseBodyController {  

    @GetMapping("/response-body-string-v1")  
    public void responseBodyV1(HttpServletResponse response) throws IOException {  
        response.getWriter().write("ok");  
  }  

    @GetMapping("/response-body-string-v2")  
    public ResponseEntity<String> responseBodyV2()  {  
        return new ResponseEntity<>("ok", HttpStatus.OK);  
  }  

    @GetMapping("/response-body-string-v21")  
    public HttpEntity<String> responseBodyV21()  {  
        return new HttpEntity<String>("ok");  
  }  

    @ResponseBody
    @GetMapping("/response-body-string-v3")  
    public String responseBodyV3() throws IOException {  
        return "ok";  
  }  

    @GetMapping("/response-body-json-v1")  
    public ResponseEntity<HelloData> responseBodyJsonV1() {  
        HelloData helloData = new HelloData();  
  helloData.setUsername("hellowKnight");  
  helloData.setAge(36);  

 return new ResponseEntity<HelloData>(helloData, HttpStatus.OK);  
  }  

    @ResponseStatus(HttpStatus.OK)  
    @ResponseBody  
 @GetMapping("/response-body-json-v2")  
    public HelloData responseBodyJsonV2() {  
        HelloData helloData = new HelloData();  
  helloData.setUsername("hellowKnight");  
  helloData.setAge(36);  

 return helloData;  
  }  

}
```


*   `@ResponseBody`사용 시 상태코드 전달은 `@ResponseStatus`를 사용
*   정적, 동적 상태 코드
    *   `@ResponseStatus`사용할 시 애노테이션으로 고정되기에 **정적**인 것만 가능
    *   `HttpEntity`나 `ResponseEntity`를 사용하면 `@ResponseStatus`사용이 강제되는 `@ResponseBody`와 다르게 **동적**인 처리가 가능
*   클래스 레벨에 `@ResponseBody` 사용하여 일괄적용 가능, **`@ResponseBody`+`@Controller` -> `@RestController`**



### HTTP 메시지 컨버터

*   HTTP 메시지 바디에 담긴 데이터를 직접 읽는 경우 용이
*   스프링 MVC가 HTTP 메시지 컨버터를 **적용하는 경우**
    *   `@RequestBody` `@ResponseBody`
    *   `HttpEntity` `RequestEntity` `ResponseEntity`
*   **스프링 부트 기본 메시지 컨버터**
    *   ByteArrayHttpMessageConverter
        *   클래스 타입 `byte[]`
        *   모든 미디어 타입 가능
        *   쓰기 미디어 타입: `application/octet-stream`
    *   StringHttpMessageConverter
        *   클래스타입 `String`
        *   모든 미디어 타입 가능
        *   쓰기 미디어 타입: `text/plain`
    *   MappingJackson2HttpMessageConverter
        *   클래스타입: 객체, `HashMap`
        *   미디어 타입 `application/json` 관련
        *   쓰기 미디어 타입: `application/json` 관련
*   **HTTP 요청 데이터 읽기**
    1.  HTTP 메시지 컨버터가 적용되는 형태의 요청이 옴
    2.  메시지 컨버터가 `canRead()` 호출: 클래스 타입 지원 여부, Content-Type 미디어 타입 지원 여부
    3.  `read()`호출, 객체 생성, 반환
*   **HTTP 응답 데이터 생성**
    1.  컨트롤러에서 지원 형태로 값이 반환
    2.  메시지 컨버터가 `canWrite()` 호출: 클래스 타입 지원 여부, 요청의 Accept 미디어 타입 지원 여부(정확히는 `@RequestMapping`의 produces
    3.  `write()`호출, 응답 메시지 바디에 데이터 생성



### 요청 매핑 핸들러 어댑터 구조

*   **RequestMappingHandlerAdapter**
    *   애노테이션(`@RequestMapping`) 기반 컨트롤러들을 처리하는 핸들러 어댑터
    *   **ArgumentResolver**: `HandlerMethodArgumentResolver`
        *   컨트롤러들의 파라미터들: `HttpServlet Request`, `Model`, `@RequestParam`, `@ModelAttribute`, `InputStream`, `@ReqeustBody`, `HttpEntity` 등을 처리해주는 역할
        *   `RequestMappingHandlerAdapter`가 `ArgumentResolver` 호출하면, 파라미터 값(객체)을 생성하고, 컨트롤러를 호출하고 값을 넘겨준다.
*   **ReturnValueHandler**: `HandlerMethodReturnValueHandler`
    *   응답 값을 반환, 처리
*   **HTTP 메시지 컨버터**: `@ResponseBody`나 `HttpEntity` 등 처리
    *   요청 시, `@ResponseBody`나 `HttpEntity` 등을 처리하는 `ArgumentResolver`가 HTTP 컨버터를 사용하여(read) 객체를 생성
    *   응답 시, `ReturnValueHandler`가 HTTP컨버터를 호출해서(write) 응답 결과를 만듦.
*   `WebMvcConfigurer` 을 상속받아 스프링 빈으로 등록하여,`HandlerMethodArgumentResolver` `HandlerMethodReturnValueHandler` `HttpMessageConverter` 인터페이스 기능 확장 가능



### 스프링 MVC - 웹 페이지 만들기

*   artifact를 item-service라고 작성했는데, 패키지명에는 '-'가 포함되지 않게 주의