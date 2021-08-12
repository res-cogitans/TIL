---


---

<h1 id="스프링-mvc-1편---백엔드-웹-개발-핵심-기술-정리김영한">스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 정리(김영한)</h1>
<h2 id="웹-애플리케이션-이해">웹 애플리케이션 이해</h2>
<h3 id="웹-서버-웹-애플리케이션-서버">웹 서버, 웹 애플리케이션 서버</h3>
<p><strong>웹 서버</strong></p>
<ul>
<li>HTTP 기반으로 동작</li>
<li>정적 리소스 제공, 기타 부가기능</li>
<li>정적(파일) HTML, CSS, JS, 이미지, 영상</li>
<li>Ex) NGINX, APACHE</li>
</ul>
<p><strong>웹 애플리케이션 서버 WAS</strong></p>
<ul>
<li>HTTP 기반 동작</li>
<li>웹 서버 기능(정적 리소스 제공) 포함 +</li>
<li>프로그램 코드를 실행해서 어플리케이션 로직 수행
<ul>
<li>동적 HTML, HTTP API(JSON)</li>
<li>서블릿, JSP, 스프링 MVC</li>
</ul>
</li>
<li>Ex) 톰캣, Jetty, Undertow</li>
</ul>
<p><strong>차이</strong></p>
<ul>
<li>정적 리소스 vs + 애플리케이션 로직</li>
<li>경계가 사실 모호: 웹 서버도 프로그램 실행 기능을 포함하기도</li>
<li>자바의 경우 서블릿 컨테이너 기능을 제공하면 WAS(단, 서블릿 없이 자바코드를 실행하는 서버 프레임워크도 존재)</li>
<li>WAS는 애플리케이션 코드를 실행하는데 더 특화</li>
</ul>
<p><strong>웹 시스템 구성 - WAS, DB</strong></p>
<ul>
<li>CLI -&gt; WAS(애플리케이션 로직 / HTML, CSS, JS / 이미지) -&gt; DB</li>
<li>WAS, DB만으로 시스템 구성 가능</li>
<li>WAS가 너무 많은 역할을 담당, 서버 과부화 우려</li>
<li>가장 중요한 애플리케이션 로직이 정적 리소스로 인해 수행이 어려워질 수 있음</li>
<li>WAS 장애시 오류 화면도 노출 불가</li>
</ul>
<p><strong>웹 시스템 구성 - WEB, WAS, DB</strong></p>
<ul>
<li>CLI -&gt; Web Server(HTML, CSS, JS / 이미지) -&gt; WAS(애플리케이션 로직) -&gt; DB</li>
<li>정적 리소스는 웹 서버가 처리</li>
<li>웹 서버는 동적 처리(애플리케이션 로직 등) 필요시 WAS에 요청 위임</li>
<li>WAS는 중요한 애플리케이션 로직 처리 전담</li>
<li>효율적인 리소스 관리: 정적 리소스 사용이 많다면 Web 서버 증설,<br>
애플리케이션 리소스가 많이 사용되면 WAS 증설</li>
<li>잘 죽지 않는 웹 서버, 잘 죽는 WAS -&gt; WAS, DB 장애시 웹 서버가 오류 제공 가능</li>
<li>실제 사용에서는 CDN 등 추가, 만일 API만 제공 시에는 웹 서버 없어도 되는 등 차이 존재</li>
</ul>
<h3 id="서블릿">서블릿</h3>
<p><strong>HTML Form 데이터 전송 -&gt; 웹 애플리케이션 서버 직접 구현 방식</strong></p>
<ul>
<li>서버 TCP/IP 대기, 소켓 연결</li>
<li>HTTP 메시지 파싱 … 읽기 작업들</li>
<li>로직 실행 후 응답 데이터 생성 TCP/IP 응답 전달, 소켓 종료</li>
<li>짧은 비즈니스 로직 실행을 위해 해야 할 일이 너무 많음<br>
-&gt; 서블릿 사용 필요</li>
</ul>
<p><strong>서블릿</strong></p>
<ul>
<li>urlPatterns(/save)의 URL이 호출되면 서블릿 코드가 실행</li>
<li>HTTP 요청 정보를 편리하게 사용할 수 있는 HttpServletRequest</li>
<li>HTTP 응답 정보를 편리하게 제공할 수 있는 HttpServletResponse</li>
<li>웹 브라우저 -&gt; localhost:8080/save -&gt; WAS에서 요청 메시지를 기반으로 request, response 객체 생성 -&gt; 서블릿 컨테이너의 myServlet 실행(request, response) -&gt; response 객체 정보로 HTTP 응답 생성 -&gt; 웹 브라우저</li>
<li><strong>HTTP 요청, 응답 흐름</strong>
<ul>
<li>HTTP 요청시
<ul>
<li>WAS는 request, response 객체를 새로 만들어서 서블릿 객체 호출</li>
<li>개발자는 request 객체에서 HTTP 요청 정보를 꺼내 사용</li>
<li>개발자는 response 객체에 HTTP 응답 정보를 입력</li>
<li>WAS는 response 객체에 담긴 내용으로 HTTP 응답 정보 생성</li>
</ul>
</li>
</ul>
</li>
</ul>
<p><strong>서블릿 컨테이너</strong></p>
<ul>
<li>톰캣처럼 서블릿을 지원하는 WAS</li>
<li>WAS 내의 서블릿 컨테이너는 서블릿 객체를 생성, 초기화 호출, 종료하는 생명주기 관리</li>
<li>서블릿 객체는 <strong>싱글톤</strong>으로 관리
<ul>
<li>최초 로딩 시점에 서블릿 객체를 미리 만들어두고 재활용</li>
<li>모든 고객 요청은 동일한 서블릿 객체 인스턴스에 접근</li>
<li><strong>공유 변수 사용 주의!</strong></li>
<li>서블릿 컨테이너 종료시 같이 종료</li>
</ul>
</li>
<li>JSP도 서블릿으로 변환되어서 사용</li>
<li>동시 요청을 위한 멀티 쓰레드 처리 지원</li>
</ul>
<h3 id="동시-요청---멀티-쓰레드">동시 요청 - 멀티 쓰레드</h3>
<p>서블릿 객체는 쓰레드가 호출함</p>
<p><strong>쓰레드</strong></p>
<ul>
<li>
<p>애플리케이션 코드를 하나하나 순차적으로 실행하는 것</p>
</li>
<li>
<p>하나의 코드 라인만 수행</p>
</li>
<li>
<p>동시 처리가 필요하면 쓰레드를 추가로 생성</p>
</li>
<li>
<p><strong>단일 요청시</strong>: 요청 -&gt; 연결 -&gt; 쓰레드 할당 -&gt; 서블릿 호출 -&gt; 응답 -&gt; 휴식</p>
</li>
<li>
<p><strong>다중 요청시</strong>: 요청1 -&gt; 연결 -&gt; 쓰레드 할당 -&gt; 요청 처리중(처리 지연)<br>
이 때 요청 2 발생시에 연결 -&gt; 쓰레드 대기 -&gt; timeout</p>
</li>
<li>
<p><strong>요청마다 쓰레드 생성</strong>: 요청1이 지연된다 해도 쓰레드2 생성한다면 요청 2 처리(사용 후에는 쓰레드 날림)</p>
<ul>
<li><strong>장점</strong>:
<ul>
<li>동시 요청 처리 가능</li>
<li>리소스(CPU, 메모리) 가 허용할 때까지 처리 가능</li>
<li>하나의 쓰레드가 지연되도 나머지는 정상작동</li>
</ul>
</li>
<li><strong>단점</strong>
<ul>
<li>쓰레드 생성 비용이 비쌈 -&gt; 응답 속도 감소</li>
<li>쓰레드는 컨텍스트 스위칭 비용이 발생 -&gt; 한정된 CPU로 처리를 옮겨 다닐 때 생기는 비용</li>
<li>쓰레드 생성에 제한이 없음 -&gt; 요청이 너무 많으면 리소스 임계점을 넘어서 서버가 죽을 수 있음</li>
</ul>
</li>
</ul>
</li>
</ul>
<p><strong>쓰레드 풀</strong> - 요청마다 쓰레드를 생성하는 방식의 보완</p>
<ul>
<li><strong>특징</strong>
<ul>
<li>필요한 쓰레드를 쓰레드 풀에 보관, 관리</li>
<li>쓰레드 최대치를 관리: 톰캣은 최대 200개 기본설정</li>
</ul>
</li>
<li><strong>사용</strong>
<ul>
<li>요청 시 쓰레드를 쓰레드 풀에서 꺼내 사용, 다 사용후에는 반납</li>
<li>쓰레드 풀에 남은 쓰레드가 없을 경우 거절 혹은 특정 숫자만큼 대기하게 설정 가능</li>
</ul>
</li>
<li><strong>장점</strong>
<ul>
<li>쓰레드가 미리 생성되어 있기에, 생성 및 종료 비용이 절약되고 응답이 빠름</li>
<li>최대치가 있기에 요청이 너무 많아도 기존 요청은 안전하게 처리 가능</li>
</ul>
</li>
<li><strong>실무 팁</strong>
<ul>
<li>주요 튜닝 포인트는 최대 쓰레드(max thread) 수</li>
<li>값이 너무 낮을 경우: 서버 리소스는 여유, 클라이언트는 쉽게 응답 지연</li>
<li>값이 너무 높을 경우: 리소스 임계점 초과로 서버 다운 가능</li>
<li>장애 발생시
<ul>
<li>클라우드의 경우 서버 늘리고, 이후 튜닝</li>
<li>평상시에 충분히 튜닝</li>
</ul>
</li>
<li>적정 숫자:
<ul>
<li>애플리케이션 로직의 복잡도, CPU, 메모리, IO 리소스 상황에 따라 달라짐</li>
<li>성능 테스트
<ul>
<li>실서비스와 최대한 유사하게 시도</li>
<li>tool: 아파치, ab, 제이미터, nGrinder</li>
</ul>
</li>
</ul>
</li>
</ul>
</li>
</ul>
<p><strong>WAS의 멀티 쓰레드 지원</strong></p>
<ul>
<li>멀티 쓰레드에 대한 부분은 WAS가 처리</li>
<li>개발자가 멀티 쓰레드 관련 코드를 신경쓰지 않아도 됨: 싱글 쓰레드 프로그래밍처럼 개발</li>
<li>단, 멀티 쓰레드 환경이기에 싱글톤 객체(서블릿, 스프링 빈)는 주의해서 사용</li>
</ul>
<h3 id="html-http-api-csr-ssr">HTML, HTTP API, CSR, SSR</h3>
<p><strong>백엔드 개발자의 관심사 3가지</strong><br>
<strong>1. 정적 리소스</strong>: 고정된 HTML, CSS, JS, 이미지, 영상 등을 제공, 주로 웹 브라우저</p>
<p><strong>2. 동적 HTML 페이지</strong></p>
<ul>
<li>동적으로 필요한 HTML파일을 생성해서 전달</li>
<li>웹 브라우저 -&gt; 요청 -&gt; WAS -&gt; DB -&gt; 주문정보 조회 -&gt; WAS -&gt; 동적으로 HTML 생성(JSP, thymeleaf) -&gt; 생성된 HTML -&gt; 웹 브라우저</li>
</ul>
<p><strong>3. HTTP API</strong></p>
<ul>
<li>HTML이 아니라 <strong>데이터(JSON) 전달</strong>: UI 화면이 필요하면 클라이언트가 별도처리</li>
<li>HTTP API의 접점
<ul>
<li><strong>UI 클라이언트</strong> 접점
<ul>
<li>앱 클라이언트(안드로이드, 아이폰, PC 앱)</li>
<li>웹 브라우저에저 JS 통한 HTTP API 호출
<ul>
<li>Ex) 웹 클라이언트: JS로 ajax로 서버 API 호출해 데이터를 전달 받고 HTML 동적 생성 후 화면에 띄움</li>
</ul>
</li>
<li>React, Vue.js 같은 웹 클라이언트</li>
</ul>
</li>
<li><strong>서버 to 서버</strong>
<ul>
<li>주문 서버 -&gt; 결제 서버
<ul>
<li>Ex) 웹/앱 클라이언트의 HTTP API를 통해 주문 서버 WAS에서 주문 정보를 받고 DB에서 조회, 정보를 결제 서버 WAS에 전달</li>
</ul>
</li>
<li>기업간 데이터 통신</li>
</ul>
</li>
</ul>
</li>
</ul>
<p><strong>SSR 서버 사이드 렌더링</strong></p>
<ul>
<li><strong>최종 HTML을 서버에서 만들어서 웹 브라우저에 전달</strong></li>
<li>주로 정적인 화면에 사용</li>
<li>관련: JSP, thymeleaf</li>
</ul>
<p><strong>CSR 클라이언트 사이드 렌더링</strong></p>
<ul>
<li>HTML 결과를 웹 브라우저의 JS로 동적 생성</li>
<li>주로 동적인 화면에 사용, 웹 환경을 앱처럼 필요한 부분부분 변경 가능</li>
<li>HTML 요청 -&gt; 내용 없고 JS 링크 있는 HTML 응답 -&gt; JS 요청 -&gt; 클라이언트 로직, HTML 렌더링 코드 있는 JS 응답 -&gt; HTTP API가 데이터 요청 -&gt; JSON 응답 -&gt; 자바스크립트로 HTML 결과 렌더링</li>
<li>Ex) 구글 지도, Gmail, 구글 캘린더</li>
<li>관련: React, Vue.js</li>
</ul>
<p><strong>ETC</strong></p>
<ul>
<li>React, Vue.js를 CSR + SSR 동시 지원하는 웹 프레임워크도 있음</li>
<li>SSR 사용해도 JS 사용해서 화면 일부를 동적으로 변경 가능</li>
</ul>
<p><strong>백엔드 개발자 입장에서 UI기술</strong></p>
<ul>
<li>서버사이드 렌더링 기술(<strong>필수</strong>)
<ul>
<li>JSP, <strong>thymeleaf</strong> 등</li>
<li>화면이 정적, 복잡하지 않을 경우</li>
</ul>
</li>
<li>클라이언트 사이드 렌더링 기술
<ul>
<li>React, Vue.js</li>
<li>복잡하고 동적인 UI 사용</li>
</ul>
</li>
<li>선택
<ul>
<li>백엔드 개발자에게 프론트엔드 학습은 옵션</li>
<li>서버, DB, 인프라 등등을 배워야</li>
</ul>
</li>
</ul>
<h3 id="자바-백엔드-웹-기술-역사">자바 백엔드 웹 기술 역사</h3>
<p><strong>과거 기술</strong></p>
<ul>
<li>서블릿(1997): HTML (동적) 생성이 어려움</li>
<li>JSP(1999): HTML 생성은 편리하지만, 비즈니스 로직까지 너무 많은 역할 담당</li>
<li>서블릿, JSP 포함 MVC 패턴 사용</li>
<li>MVC프레임워크 난립(2000초 ~ 2010초)
<ul>
<li>MVC 패턴 자동화, 웹 편의성 기능 다수 지원</li>
<li>스트럿츠, 웹워크, 스프링 MVC(구버전)</li>
</ul>
</li>
<li>애노테이션 기반의 스프링 MVC 등장<br>
<strong>현재 기술</strong></li>
<li>스프링 부트 등장
<ul>
<li>서버 내장</li>
<li>과거: 서버에 WAS 직접 설치, 소스는 War파일 만들어서 WAS에 배포</li>
<li>스프링 부트는 Jar에 WAS 포함 -&gt; 빌드 배포 단순화<br>
<strong>최신 기술 - 스프링 웹 기술의 분화</strong></li>
</ul>
</li>
<li>Web Servlet - Spring MVC</li>
<li>Web Reactive - Spring WebFlux
<ul>
<li>특징
<ul>
<li>비동기 논블로킹 처리</li>
<li>최소 쓰레드로 최대 성능 -&gt; 쓰레드 컨텍스트 스위칭 비용 효율화</li>
<li>함수형 스타일 -&gt; 동시처리 코드 효율화</li>
<li>서블릿 기술 사용 안 함</li>
</ul>
</li>
<li>문제
<ul>
<li>기술적 난도</li>
<li>RDB 지원 부족</li>
<li>MVC의 쓰레드 모델도 아직 충분히 빠름</li>
<li>실무 사용 적음</li>
</ul>
</li>
</ul>
</li>
</ul>
<p><strong>자바 뷰 템플릿 역사</strong>: HTML을 편리하게 동적 생성하는 뷰 기능</p>
<ul>
<li>JSP: 느린 속도, 기능 부족</li>
<li>프리마커(Freemarker), 벨로시티(Velocity): 속도 문제 해결, 다양한 기능</li>
<li><strong>타임리프</strong>(Thymeleaf)
<ul>
<li>네츄럴 템플릿: HTML의 형태를 유지하면서 뷰 템플릿 적용 가능</li>
<li>스프링 MVC와 강력한 기능 통함</li>
<li>가장 낫지만, 성능 측면에서는 프리마커, 벨로시티가 더 나음</li>
</ul>
</li>
</ul>
<hr>
<h2 id="서블릿-1">서블릿</h2>
<ul>
<li>톰캣 설치 -&gt; 서블릿 코드를 클래스 파일로 빌드해서 올림 -&gt; 톰캣 서버 실행</li>
<li>스프링 부트의 경우 내장 톰캣</li>
<li><code>@ServletComponentScan</code>: 서블릿 자동 등록</li>
<li><code>@WebServlet</code> 서블릿 애노테이션
<ul>
<li>name: 서블릿 이름</li>
<li>urlPatterns: URL 매핑</li>
<li>서블릿명과 URL 매핑은 겹치면 안 됨</li>
</ul>
</li>
<li><strong>application properties</strong>에 <code>logging.level.org.apache.coyote.http11=debug</code> 개발 용으로 유용한 로그(운영 서버의 경우 성능 저하)</li>
</ul>
<h3 id="httpservletrequest">HttpServletRequest</h3>
<p><strong>HttpServletRequest</strong></p>
<ul>
<li>HTTP 요청 메시지를 파싱해주며, 그 결과를 객체에 담아 제공</li>
<li>부가 기능 제공
<ul>
<li><strong>임시 저장소</strong> 기능: 해당 HTTP 요청이 끝날 때까지 유지
<ul>
<li>저장: <code>request.setAttribute(name, value)</code></li>
<li>조회: <code>request.getAttribute(name)</code></li>
</ul>
</li>
<li><strong>세션 관리 기능</strong>: <code>request.getSession(create:true)</code></li>
</ul>
</li>
<li>request.getLocale(): Accept-Language 최고 순위를 반환</li>
</ul>
<h3 id="http-요청-데이터---개요">HTTP 요청 데이터 - 개요</h3>
<p>HTTP CLI -&gt; SRV 데이터 전달 방법</p>
<ol>
<li>GET - 쿼리 파라미터</li>
<li>POST - HTML Form</li>
<li>HTTP message body에 데이터를 직접 담아 요청</li>
</ol>
<h3 id="http-요청-데이터---get-쿼리-파라미터">HTTP 요청 데이터 - GET 쿼리 파라미터</h3>
<ul>
<li><code>URL?queryparam1&amp;queryparam2</code> 형식</li>
<li><code>request.getParameter("name");</code> 형태로 조회</li>
<li><code>request.getParameterValues("name");</code>는 복수의 동일 명칭 값이 있을 때 배열로 반환</li>
</ul>
<h3 id="http-요청-데이터-post-html-form">HTTP 요청 데이터 POST HTML Form</h3>
<p><strong>특징</strong></p>
<ul>
<li>content-type: <code>application/x-www-form-urlencoded</code></li>
<li>메시지 바디에 쿼리 파라미터와 같은 형식으로 데이터 전달: <code>username=hello&amp;age=20</code></li>
<li>서버 입장에서는 GET 쿼리 파라미터 방식과 동일하게 조회</li>
<li>단 이 경우에는 <code>content-type</code>이 지정</li>
</ul>
<h3 id="http-요청-데이터---api-메시지-바디">HTTP 요청 데이터 - API 메시지 바디</h3>
<ul>
<li>주로 POST, PUT, PATCH 사용<br>
<strong>단순 텍스트</strong><br>
inputStream 이용해서 데이터 읽음 -&gt; 바이트코드<br>
바이트 &lt;-&gt; 문자 전환은 인코딩 명시</li>
</ul>
<p><strong>JSON</strong></p>
<ul>
<li>JSON은 객체로 바꿔서 사용</li>
<li>ObjectMapper.readValue() 이용해서 파싱(Jackson 라이브러리)</li>
<li>HTML Form 데이터의 경우도 Body 통한 전달이라 InputStream으로 읽는 것이 가능하지만 파라미터 조회가 더 좋음.</li>
</ul>
<h3 id="httpservletresponse">HttpServletResponse</h3>
<p><strong>역할</strong>: 응답 메시지 생성</p>
<ul>
<li>응답코드 지정</li>
<li>헤더 생성</li>
<li>바디 생성</li>
<li>편의 기능 제공: Content-Type, 쿠키, Redirect</li>
</ul>
<p><strong>응답코드</strong></p>
<ul>
<li><code>response.setStatus(HttpServletResponse.SC_OK)</code><br>
<code>response.setStatus(200)</code>도 가능하지만, 위 처럼 적는 편이 더 나음</li>
</ul>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">content</span><span class="token punctuation">(</span>HttpServletResponse response<span class="token punctuation">)</span> <span class="token punctuation">{</span>  
  <span class="token comment">//Content-Type: text/plain;charset=utf-8  </span>
 <span class="token comment">//Content-Length: 2 //response.setHeader("Content-Type", "text/plain;charset=utf-8");  response.setContentType("text/plain");  </span>
  response<span class="token punctuation">.</span><span class="token function">setCharacterEncoding</span><span class="token punctuation">(</span><span class="token string">"utf-8"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>  
  <span class="token comment">//response.setContentLength(2); //(생략시 자동 생성)  </span>
<span class="token punctuation">}</span>
</code></pre>
<ul>
<li>Content-Type, 문자 인코딩을 <code>setHeader</code>이용하여 지정할 수도 있으나, <code>response.setContentType("text/plain")</code> 등 이용하여 간단하게 지정하는 것도 가능하다.</li>
<li>Content-Length 지정을 생략시 자동 생성된다.</li>
</ul>
<p><strong>쿠키 설정</strong></p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">cookie</span><span class="token punctuation">(</span>HttpServletResponse response<span class="token punctuation">)</span> <span class="token punctuation">{</span>  
    <span class="token comment">//Set-Cookie: myCookie=good; Max-Age=600;  </span>
 <span class="token comment">//response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");  Cookie cookie = new Cookie("myCookie", "good");  </span>
  cookie<span class="token punctuation">.</span><span class="token function">setMaxAge</span><span class="token punctuation">(</span><span class="token number">600</span><span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">//600초  </span>
  response<span class="token punctuation">.</span><span class="token function">addCookie</span><span class="token punctuation">(</span>cookie<span class="token punctuation">)</span><span class="token punctuation">;</span>  
<span class="token punctuation">}</span>
</code></pre>
<ul>
<li>Cookie 객체 생성하여 간단하게 설정 가능</li>
</ul>
<p><strong>Redirect 설정</strong></p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">redirect</span><span class="token punctuation">(</span>HttpServletResponse response<span class="token punctuation">)</span> <span class="token keyword">throws</span> IOException <span class="token punctuation">{</span>  
    <span class="token comment">//Status Code 302  </span>
 <span class="token comment">//Location: /basic/hello-form.html  </span>
 
 <span class="token comment">//response.setStatus(HttpServletResponse.SC_FOUND); //302 //response.setHeader("Location", "/basic/hello-form.html");</span>
  response<span class="token punctuation">.</span><span class="token function">sendRedirect</span><span class="token punctuation">(</span><span class="token string">"/basic/hello-form.html"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>  
<span class="token punctuation">}</span>
</code></pre>
<ul>
<li>주석처리된 2줄로도 설정 가능, 아래의 한 줄로 간단하게 설정 가능.</li>
</ul>
<p><strong>Message Body 설정</strong></p>
<pre class=" language-java"><code class="prism  language-java">PrintWriter writer <span class="token operator">=</span> response<span class="token punctuation">.</span><span class="token function">getWriter</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// InputStream이용할수도 있다</span>
writer<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token string">"OK"</span><span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">//(print/println);</span>
</code></pre>
<h3 id="http-응답-데이터">HTTP 응답 데이터</h3>
<p>응답 메시지의 내용:</p>
<ol>
<li>단순 텍스트(<code>writer.println("OK");</code>)</li>
<li>HTML 응답</li>
<li>HTTP API - Message Body JSON 응답</li>
</ol>
<p><strong>단순 텍스트, HTML</strong></p>
<pre class=" language-java"><code class="prism  language-java">response<span class="token punctuation">.</span><span class="token function">setContentType</span><span class="token punctuation">(</span><span class="token string">"text/html"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>  
response<span class="token punctuation">.</span><span class="token function">setCharacterEncoding</span><span class="token punctuation">(</span><span class="token string">"utf-8"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
</code></pre>
<p><strong>API JSON</strong></p>
<pre class=" language-java"><code class="prism  language-java">response<span class="token punctuation">.</span><span class="token function">setContentType</span><span class="token punctuation">(</span><span class="token string">"application/json"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>  
response<span class="token punctuation">.</span><span class="token function">setCharacterEncoding</span><span class="token punctuation">(</span><span class="token string">"utf-8"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>	<span class="token comment">// 사실 불필요</span>
String result <span class="token operator">=</span> objectMapper<span class="token punctuation">.</span><span class="token function">writeValueAsString</span><span class="token punctuation">(</span>helloData<span class="token punctuation">)</span><span class="token punctuation">;</span>  
response<span class="token punctuation">.</span><span class="token function">getWriter</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">write</span><span class="token punctuation">(</span>result<span class="token punctuation">)</span><span class="token punctuation">;</span>
</code></pre>
<ul>
<li>application/json은 utf-8 사용하게 정의되어 있음, 추가할 필요 없음.</li>
</ul>
<hr>
<h2 id="서블릿-jsp-mvc-패턴">서블릿, JSP, MVC 패턴</h2>
<ul>
<li>동시성 문제를 고려하여, 실무에서는 HashMap 대신 ConcurrentHashMap, AtomicLong 사용을 고려하라.</li>
</ul>
<h3 id="서블릿을-이용한-개발">서블릿을 이용한 개발</h3>
<ul>
<li>서블릿으로 직접 HTML 전달 시 일일히 HTML 적어줘야 하기에 실수할 가능성 높음</li>
</ul>
<pre class=" language-java"><code class="prism  language-java">w<span class="token punctuation">.</span><span class="token function">write</span><span class="token punctuation">(</span><span class="token string">"&lt;html&gt;\n"</span> <span class="token operator">+</span>  
        <span class="token string">"&lt;head&gt;\n"</span> <span class="token operator">+</span>  
        <span class="token string">" &lt;meta charset=\"UTF-8\"&gt;\n"</span> <span class="token operator">+</span>  
        <span class="token string">"&lt;/head&gt;\n"</span> <span class="token operator">+</span>  
        <span class="token string">"&lt;body&gt;\n"</span> <span class="token operator">+</span>  
        <span class="token string">"성공\n"</span> <span class="token operator">+</span>  
        <span class="token string">"&lt;ul&gt;\n"</span> <span class="token operator">+</span>  
        <span class="token string">" &lt;li&gt;id="</span><span class="token operator">+</span>member<span class="token punctuation">.</span><span class="token function">getId</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token operator">+</span><span class="token string">"&lt;/li&gt;\n"</span> <span class="token operator">+</span>  
        <span class="token string">" &lt;li&gt;username="</span><span class="token operator">+</span>member<span class="token punctuation">.</span><span class="token function">getUsername</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token operator">+</span><span class="token string">"&lt;/li&gt;\n"</span> <span class="token operator">+</span>  
        <span class="token string">" &lt;li&gt;age="</span><span class="token operator">+</span>member<span class="token punctuation">.</span><span class="token function">getAge</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token operator">+</span><span class="token string">"&lt;/li&gt;\n"</span> <span class="token operator">+</span>  
        <span class="token string">"&lt;/ul&gt;\n"</span> <span class="token operator">+</span>  
        <span class="token string">"&lt;a href=\"/index.html\"&gt;메인&lt;/a&gt;\n"</span> <span class="token operator">+</span>  
        <span class="token string">"&lt;/body&gt;\n"</span> <span class="token operator">+</span>  
        <span class="token string">"&lt;/html&gt;"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
</code></pre>
<ul>
<li>동적인 HTML 생성 가능<br>
-&gt; 하지만 서블릿으로는 HTML 생성이 매우 불편하다.<br>
-&gt; <strong>템플릿 엔진</strong>: 동적인 변경이 필요한 부분만 코드를 적용</li>
</ul>
<h3 id="jsp-이용한-개발">JSP 이용한 개발</h3>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>%@</span> <span class="token attr-name">page</span> <span class="token attr-name">contentType</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>text/html;charset=UTF-8<span class="token punctuation">"</span></span> <span class="token attr-name">language</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>java<span class="token punctuation">"</span></span> <span class="token attr-name">%</span><span class="token punctuation">&gt;</span></span>  
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>html</span><span class="token punctuation">&gt;</span></span>  
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>head</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>title</span><span class="token punctuation">&gt;</span></span>Title<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>title</span><span class="token punctuation">&gt;</span></span>  
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>head</span><span class="token punctuation">&gt;</span></span>  
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>body</span><span class="token punctuation">&gt;</span></span>  
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>form</span> <span class="token attr-name">action</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>/jsp/members/save.jsp<span class="token punctuation">"</span></span> <span class="token attr-name">method</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>post<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>  
 username: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>text<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>username<span class="token punctuation">"</span></span> <span class="token punctuation">/&gt;</span></span>  
 age: <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>input</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>text<span class="token punctuation">"</span></span> <span class="token attr-name">name</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>age<span class="token punctuation">"</span></span> <span class="token punctuation">/&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>button</span> <span class="token attr-name">type</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>submit<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>전송<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>button</span><span class="token punctuation">&gt;</span></span>  
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>form</span><span class="token punctuation">&gt;</span></span>  
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>body</span><span class="token punctuation">&gt;</span></span>  
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>html</span><span class="token punctuation">&gt;</span></span>  
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>%@</span> <span class="token attr-name">page</span> <span class="token attr-name">contentType</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>text/html;charset=UTF-8<span class="token punctuation">"</span></span> <span class="token attr-name">language</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>java<span class="token punctuation">"</span></span> <span class="token attr-name">%</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<ul>
<li>JSP 방식</li>
</ul>
<pre class=" language-java"><code class="prism  language-java"><span class="token operator">&lt;</span><span class="token operator">%</span>  
<span class="token comment">// request, response 사용 가능  </span>
  MemberRepository memberRepository <span class="token operator">=</span> MemberRepository<span class="token punctuation">.</span><span class="token function">getInstance</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>  
       
     System<span class="token punctuation">.</span>out<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token string">"save.jsp"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>  
     String username <span class="token operator">=</span> request<span class="token punctuation">.</span><span class="token function">getParameter</span><span class="token punctuation">(</span><span class="token string">"username"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>  
     <span class="token keyword">int</span> age <span class="token operator">=</span> Integer<span class="token punctuation">.</span><span class="token function">parseInt</span><span class="token punctuation">(</span>request<span class="token punctuation">.</span><span class="token function">getParameter</span><span class="token punctuation">(</span><span class="token string">"age"</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>  
       
     Member member <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">Member</span><span class="token punctuation">(</span>username<span class="token punctuation">,</span> age<span class="token punctuation">)</span><span class="token punctuation">;</span>  
     System<span class="token punctuation">.</span>out<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token string">"member = "</span> <span class="token operator">+</span> member<span class="token punctuation">)</span><span class="token punctuation">;</span>  
     memberRepository<span class="token punctuation">.</span><span class="token function">save</span><span class="token punctuation">(</span>member<span class="token punctuation">)</span><span class="token punctuation">;</span>  
<span class="token operator">%</span><span class="token operator">&gt;</span>
</code></pre>
<ul>
<li>&lt;%  (자바코드) %&gt; 자바 로직 사용 가능</li>
<li>표시 없는 경우 위의 서블릿에서 한 것처럼 프린트 한 것과 같다고 보면 됨.</li>
</ul>
<pre class=" language-java"><code class="prism  language-java"><span class="token operator">&lt;</span><span class="token operator">%</span>@ page <span class="token keyword">import</span><span class="token operator">=</span><span class="token string">"java.util.List"</span> <span class="token operator">%</span><span class="token operator">&gt;</span>  
<span class="token operator">&lt;</span><span class="token operator">%</span>@ page <span class="token keyword">import</span><span class="token operator">=</span><span class="token string">"logos.servlet.domain.member.MemberRepository"</span> <span class="token operator">%</span><span class="token operator">&gt;</span>
<span class="token operator">&lt;</span><span class="token operator">%</span>@<span class="token operator">=</span> java code <span class="token operator">%</span><span class="token operator">&gt;</span>
</code></pre>
<ul>
<li>import 해줘야 함.</li>
<li><code>&lt;%@=</code> 자바 코드를 출력</li>
<li>JSP를 사용해도 아직 부족한 점이 많음:
<ul>
<li>로직과 뷰가 섞여 있음</li>
<li>JSP가 과다한 역할을 맡고 있음<br>
-&gt; MVC 패턴 등장</li>
</ul>
</li>
</ul>
<h3 id="mvc-패턴-개요">MVC 패턴: 개요</h3>
<p><strong>문제상황</strong>: 위와 같이</p>
<ul>
<li>단일 서블릿이나 JSP만으로 비즈니스 + 뷰 처리 -&gt; 과도한 역할, 유지보수 힘듦</li>
<li><strong>변경의 라이프 사이클: 변경 주기가 다르다면 분리해야 한다!</strong></li>
<li>뷰 템플릿은 화면 렌더링 최적화되어 있음 -&gt; 기능에 맞게 담당하게 해야</li>
</ul>
<p><strong>MVC</strong></p>
<ul>
<li>컨트롤러: HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행, 뷰에 전달할 결과 데이터를 조회해서 모델에 담음</li>
<li>모델: 뷰에 출력할 데이터를 담아둠 - 뷰의 역할 분할을 도움</li>
<li>뷰: 모델에 담긴 데이터를 이용, 화면을 그림(HTML 생성 등)</li>
<li>MVC 패턴1: 컨트롤러가 비즈니스 로직까지 -&gt; 과도한 역할</li>
<li>MVC 패턴2: 컨트롤러는 컨트롤러 로직, 서비스, 리포지토리는 비즈니스 로직, 데이터 접근<br>
-&gt; 컨트롤러는 비즈니스 로직이 있는 서비스를 호출</li>
</ul>
<h3 id="mvc-패턴--적용">MVC 패턴 : 적용</h3>
<ul>
<li>컨트롤러: 서블릿</li>
<li>뷰: JSP</li>
<li>모델: HttpServletRequest(request는 데이터 저장소를 가지고 있음: .set/getAttribute</li>
<li>액션이 “/save”(절대경로)가 아니라 “save”(상대경로)</li>
<li><code>dispather.forward()</code>: 다른 서블릿이나 JSP로 이동하는 기능, 서버 내부에서 다시 호출이 발생
<ul>
<li>vs redirect: 리다이렉트와는 다르다. 포워드는 서버 내부의 호출이기에 클라이언트가 호출 불가, 리다이렉트는 클라이언트에 응답 -&gt; redirect 경로로 클라이언트의 재요청</li>
</ul>
</li>
<li>컨트롤러를 통해서 호출하고 싶은 경우, WEB-INF에 저장</li>
<li><code>age=&lt;%=((Member)request.getAttribute("member")).getAge()%&gt;</code>로 값을 얻어올 수 있지만,<br>
<code>id=${member.id}</code>: JSP 표현식으로 간단하게 표현 가능. -&gt;프로퍼티 접근법</li>
<li><code>&lt;%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%&gt;</code>: jstl 사용을 위함</li>
</ul>
<pre class=" language-html"><code class="prism  language-html"><span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>table</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>thead</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>th</span><span class="token punctuation">&gt;</span></span>id<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>th</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>th</span><span class="token punctuation">&gt;</span></span>username<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>th</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>th</span><span class="token punctuation">&gt;</span></span>age<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>th</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>thead</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>tbody</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span><span class="token namespace">c:</span>forEach</span> <span class="token attr-name">var</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>item<span class="token punctuation">"</span></span> <span class="token attr-name">items</span><span class="token attr-value"><span class="token punctuation">=</span><span class="token punctuation">"</span>${members}<span class="token punctuation">"</span></span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>tr</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>td</span><span class="token punctuation">&gt;</span></span>${item.id}<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>td</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>td</span><span class="token punctuation">&gt;</span></span>${item.username}<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>td</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>td</span><span class="token punctuation">&gt;</span></span>${item.age}<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>td</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>tr</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span><span class="token namespace">c:</span>forEach</span><span class="token punctuation">&gt;</span></span>  
 <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>tbody</span><span class="token punctuation">&gt;</span></span>  
<span class="token tag"><span class="token tag"><span class="token punctuation">&lt;/</span>table</span><span class="token punctuation">&gt;</span></span>
</code></pre>
<ul>
<li>jstl을 이용해서 반복 출력을 적용</li>
</ul>
<h3 id="mvc-패턴-한계">MVC 패턴: 한계</h3>
<ul>
<li>포워드 중복: RequestDispather 생성하고, foward로 뷰 이동하는 코드가 중복 호출됨</li>
<li>ViewPath 중복: ViewPath 앞 뒷부분 중복 발생, 다른 뷰로 변경하려면 코드를 다 바꾸게됨</li>
<li>사용하지 않는 코드: response 등 사용하지 않게 되는 코드가 많음, 또한 이러한 코드는 테스트 케이스 작성도 어려움</li>
<li>공통 기능 처리가 어려움: 메서드로 뽑아내도, 호출이 필요하며, 안 호출하는 실수 가능<br>
-&gt; <strong>공통 처리</strong>의 문제: 서블릿(컨트롤러) 호출 이전에 공통 기능이 처리해야 한다.<br>
-&gt; <strong>프론트 컨트롤러(Front Controller) 패턴</strong></li>
</ul>
<hr>
<h2 id="mvc-프레임워크-만들기">MVC 프레임워크 만들기</h2>
<h3 id="프론트-컨트롤러-패턴-소개">프론트 컨트롤러 패턴 소개</h3>
<p><strong>도입 이전에는</strong>: 공통로직을 직접적으로 컨트롤러 이전에 넣어줬어야 함<br>
<strong>도입 후에는</strong>: 공통 로직을 담당하는 프론트 컨트롤러를 통해서 컨트롤러로 접근<br>
<strong>FrontController 패턴 특징</strong></p>
<ul>
<li>프론트 컨트롤러 서블릿 하나로 CLI 요청 받음</li>
<li>상황에 맞는 컨트롤러를 찾아 호출</li>
<li>입구를 하나로!</li>
<li>공통 처리 가능</li>
<li>프론트 컨트롤러 외 컨트롤러는 서블릿 안 써도 됨!</li>
<li><strong>스프링 웹 MVC와의 연관</strong>
<ul>
<li>스프링 웹 MVC의 핵심</li>
<li>DispatherServlet이 프론트 컨트롤러 패턴</li>
</ul>
</li>
</ul>
<h3 id="프론트-컨트롤러-도입---v1">프론트 컨트롤러 도입 - v1</h3>
<ul>
<li>서블릿 컨트롤러가 매핑을 통해 개별 컨트롤러들(컨트롤러 인터페이스의 구현)을 호출, 각 컨트롤러들의 오버라이드 된 메서드를 호출하여 공통 코드를 이용해서 각각의 로직 실행 가능.</li>
</ul>
<h3 id="view-분리---v2">View 분리 - v2</h3>
<ul>
<li>dispatcher 생성, foward 부분을 MyView로 분리, 컨트롤러의 process 메서드가 MyView 반환, 그 MyView의 render()를 프론트 컨트롤러가 호출</li>
<li>MyView를 인터페이스로 만들고 구현하면 JSP 말고 다른 템플릿 엔진 등으로도 쉽게 변경 가능</li>
</ul>
<h3 id="model-추가---v3">Model 추가 - v3</h3>
<ul>
<li>서블릿 종속성 제거
<ul>
<li>불필요한 request, response 전달을 지움 -&gt; Model 객체 이용</li>
</ul>
</li>
<li>뷰 이름 중복 제거
<ul>
<li>개별 컨트롤러는 뷰의 논리 이름만 반환, 물리 이름은 프론트 컨트롤러에서 처리하게함<br>
-&gt; ViewResolver</li>
<li>뷰 위치가 변경되더라도 프론트 컨트롤러만 수정하면 됨 -&gt; 수정 용이
<ul>
<li><strong>변경 지점을 하나로 만들어라!</strong></li>
</ul>
</li>
</ul>
</li>
<li>ModelView
<ul>
<li>Model 역할에 View 이름까지 전달</li>
</ul>
</li>
</ul>
<h3 id="단순하고-실용적인-컨트롤러---v4">단순하고 실용적인 컨트롤러 - v4</h3>
<ul>
<li>구조적 개선이 있었지만, 생산성이 낮다는 점을 개선 -&gt; 계속 모델 뷰를 만드는 수고가 없음</li>
<li>프론트 컨트롤러는 모델 생성, 컨트롤러 호출 시 모델 전달</li>
<li>컨트롤러는 뷰 논리 이름 그대로 반환</li>
</ul>
<h3 id="유연한-컨트롤러">유연한 컨트롤러</h3>
<ul>
<li>단일 프로젝트에서 다양한 종류의 컨트롤러를 사용하고 싶을 때(서로 다른 인터페이스)</li>
</ul>
<p><strong>어댑터 패턴</strong></p>
<ul>
<li>프론트 컨트롤러가 다양한 방식의 컨트롤러를 어댑터를 통해 사용 가능</li>
<li>기존에는 프론트 컨트롤러 -&gt; 핸들러(컨트롤러) 즉각 호출</li>
<li>이제는 프론트 컨트롤러 -&gt; 핸들러 매핑 정보 -&gt; <strong>핸들러 어댑터</strong> 목록조회 -&gt; 이를 바탕으로 핸들러 어댑터 호출 -&gt; 핸들러 호출</li>
<li><strong>핸들러</strong>: 컨트롤러보다 더 넓은 의미 -&gt; 컨트롤러 뿐만 아니라 다른 경우에도 사용</li>
<li>프론트 컨트롤러 - <strong>핸들러 매핑 정보 찾음 - 목록에서 어댑터 찾음 -&gt; 어댑터 컨트롤러 호출, 모델뷰 반환받음</strong> -&gt; 뷰리졸버 -&gt; 뷰</li>
<li>OCP를 지키면서 기능 추가 가능.</li>
<li>다수 인터페이스의 컨트롤러를 편리하게 사용 가능하다.</li>
</ul>
<hr>
<h2 id="스프링-mvc---구조-이해">스프링 MVC - 구조 이해</h2>
<h3 id="스프링-mvc-전체-구조">스프링 MVC 전체 구조</h3>
<p><strong>DispatherSerlvet</strong></p>
<ul>
<li>스프링 MVC의 프론트 컨트롤러</li>
<li>부모 클래스에서 HttpServlet 상속</li>
<li>스프링 부트는 <code>DispatcherServlet</code>을 등록하면서 모든 경로(<code>urlPatterns="/"</code>에 대해서 매핑한다.
<ul>
<li>별도 규정이 있을 경우, 그것이 먼저 동작, 등록</li>
</ul>
</li>
<li>요청 흐름
<ul>
<li>서블릿이 호출되면 <code>service()</code>호출, . . . , <code>doDispatch()</code>호출</li>
</ul>
</li>
<li><code>doDispatch()</code>: 핸들러 찾기, 핸들러 어댑터 찾기 -&gt; 실제 호출 . . .처럼 예제와 같은 방식</li>
<li>동작 순서
<ol>
<li>핸들러 조회</li>
<li>핸들러 어댑터 조회</li>
<li>핸들러 어댑터 실행</li>
<li>ModelAndView 반환</li>
<li>viewResolver 호출</li>
<li>View 반환</li>
<li>뷰 랜더링</li>
</ol>
</li>
</ul>
<h3 id="핸들러-매핑과-핸들러-어댑터">핸들러 매핑과 핸들러 어댑터</h3>
<ul>
<li>@기반 컨트롤러 이전 스타일</li>
</ul>
<pre class=" language-java"><code class="prism  language-java">  
<span class="token annotation punctuation">@Component</span><span class="token punctuation">(</span><span class="token string">"/springmvc/old-controller"</span><span class="token punctuation">)</span>  
<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">OldController</span> <span class="token keyword">implements</span> <span class="token class-name">Controller</span> <span class="token punctuation">{</span>  
  
    <span class="token annotation punctuation">@Override</span>  
  <span class="token keyword">public</span> ModelAndView <span class="token function">handleRequest</span><span class="token punctuation">(</span>HttpServletRequest request<span class="token punctuation">,</span> HttpServletResponse response<span class="token punctuation">)</span> <span class="token keyword">throws</span> Exception <span class="token punctuation">{</span>  
        System<span class="token punctuation">.</span>out<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token string">"OldController.handleRequest"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>  
 <span class="token keyword">return</span> null<span class="token punctuation">;</span>  <span class="token punctuation">}</span>  
<span class="token punctuation">}</span>
</code></pre>
<p><strong>스프링 부트가 등록하는 대략적 순서</strong><br>
- <strong>HandlerMapping</strong><br>
0. <code>RequestMappingHandlerMapping</code>: <code>@RequestMapping</code><br>
1. <code>BeanNameUrlHandlerMApping</code>: url이름과 일치하는 스프링 빈의 이름<br>
- <strong>HandlerAdapter</strong><br>
0. <code>RequestMappingHandlerAdapter</code><br>
1. <code>HttpRequestHandlerAdapter</code><br>
2. <code>SimpleControllerHandlerAdapter</code>: Controller 인터페이스(<code>.supports(handler)</code> 검사)</p>
<ul>
<li>실행 순서
<ol>
<li>핸들러 매핑에서 핸들러 조회</li>
<li>핸들러 어댑터 조회</li>
<li>핸들러 어댑터 실행</li>
</ol>
</li>
<li>가장 많이 사용하는 것: <code>@RequestMapping</code></li>
</ul>
<h3 id="뷰-리졸버">뷰 리졸버</h3>
<p>application.properties</p>
<pre class=" language-java"><code class="prism  language-java">spring<span class="token punctuation">.</span>mvc<span class="token punctuation">.</span>view<span class="token punctuation">.</span>prefix<span class="token operator">=</span><span class="token operator">/</span>WEB<span class="token operator">-</span>INF<span class="token operator">/</span>views<span class="token operator">/</span>  
spring<span class="token punctuation">.</span>mvc<span class="token punctuation">.</span>view<span class="token punctuation">.</span>suffix<span class="token operator">=</span><span class="token punctuation">.</span>jsp
</code></pre>
<p>스프링 부트가 <code>InternalResourceViewResolver</code>를 자동등록.</p>
<p><strong>스프링 부트가 등록하는 뷰 리졸버 순서</strong><br>
0. <code>BeanNameViewResolver</code>: 빈 이름으로 뷰 찾아서 반환(엑셀 파일 생성 등)<br>
1. <code>InternalResourceViewResolver</code>: JSP를 처리할 수 있는 뷰 반환</p>
<h3 id="스프링-mvc---시작하기">스프링 MVC - 시작하기</h3>
<p><strong><code>@RequestMapping</code></strong></p>
<ul>
<li>애노테이션 기반의 유연하고 실용적인 컨트롤러 사용 가능</li>
<li>요청 정보를 매핑</li>
<li>대응하는 핸들러 매핑 및 어댑터:
<ul>
<li><code>RequestMappingHandlerMapping</code></li>
<li><code>RequestMappingHandlerAdapter</code></li>
</ul>
</li>
<li><strong><code>@Controller</code></strong>
<ul>
<li>컴포넌트 스캔의 대상</li>
<li>애노테이션 기반 컨트롤러로 인식됨</li>
</ul>
</li>
<li>클래스에 <code>@RequestMapping</code> 혹은 <code>@Controller</code>가 클래스에 적혀 있으면 인식됨
<ul>
<li><code>@RequestMapping</code> 경우, <code>@Component</code>붙어서 스프링 빈에 등록시키자. (<code>@Bean</code>으로 직접 등록하거나)</li>
<li><code>RequestMappingHandlerMapping</code>의 <code>isHander(Class&lt;?&gt; beanType)</code>메서드는 <code>Controller.class</code>||<code>RequestMapping.class</code>애노테이션 소유 여부를 검사하기 때문이다.</li>
</ul>
</li>
</ul>
<h3 id="스프링-mvc---컨트롤러-통합">스프링 MVC - 컨트롤러 통합</h3>
<ul>
<li>단일 컨트롤러에 <code>@RequestMapping</code> 붙은 여러 메서드 사용하여 한 컨트롤러 안에서 통합이 가능해짐.</li>
<li>컨트롤러에 <code>@RequestMapping(공통 url)</code> + 메서드의 (부분url)로 호출 가능, 공통 url과 동일한 url의 경우 <code>@RequestMapping</code>만 작성.</li>
</ul>
<h3 id="스프링-mvc---실용적인-방식">스프링 MVC - 실용적인 방식</h3>
<ul>
<li>ModelView 직접 만들어 반환해서 생기는 불편을 해소</li>
<li><strong>실무에서 주로 사용하는 방식</strong></li>
</ul>
<pre class=" language-java"><code class="prism  language-java"><span class="token comment">//    @RequestMapping(name = "/save", method = RequestMethod.POST)  </span>
  <span class="token annotation punctuation">@PostMapping</span><span class="token punctuation">(</span><span class="token string">"/save"</span><span class="token punctuation">)</span>
  <span class="token keyword">public</span> String <span class="token function">save</span><span class="token punctuation">(</span><span class="token annotation punctuation">@RequestParam</span><span class="token punctuation">(</span><span class="token string">"username"</span><span class="token punctuation">)</span> String username<span class="token punctuation">,</span>  
  <span class="token annotation punctuation">@RequestParam</span><span class="token punctuation">(</span><span class="token string">"age"</span><span class="token punctuation">)</span> <span class="token keyword">int</span> age<span class="token punctuation">,</span>  
  Model model<span class="token punctuation">)</span> <span class="token punctuation">{</span>  
  
    Member member <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">Member</span><span class="token punctuation">(</span>username<span class="token punctuation">,</span> age<span class="token punctuation">)</span><span class="token punctuation">;</span>  
  memberRepository<span class="token punctuation">.</span><span class="token function">save</span><span class="token punctuation">(</span>member<span class="token punctuation">)</span><span class="token punctuation">;</span>  
  
  model<span class="token punctuation">.</span><span class="token function">addAttribute</span><span class="token punctuation">(</span><span class="token string">"member"</span><span class="token punctuation">,</span> member<span class="token punctuation">)</span><span class="token punctuation">;</span>  
 <span class="token keyword">return</span> <span class="token string">"save-result"</span><span class="token punctuation">;</span>  
<span class="token punctuation">}</span>
</code></pre>
<ul>
<li>Model model</li>
<li><code>@RequestParam</code>: GET 쿼리 파라미터, POST Form 모두 지원</li>
<li>반환값을 String으로</li>
<li>HTTP메서드를 지정: method= 에서 <code>@PostMapping</code>식으로</li>
</ul>

