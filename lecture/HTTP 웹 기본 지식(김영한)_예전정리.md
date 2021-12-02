```
---


---

<h1>모든 개발자를 위한 HTTP 웹 기본 지식(김영한)</h1>
<ul>
<li>웹 기술은 HTTP 기반으로 제작된다.</li>
</ul>
<h3 id="인터넷-네트워크">인터넷 네트워크</h3>
<pre><code>           📡인터넷
클라이언트--◆--◆--◆- 서버
			◆--◆
</code></pre>
<ul>
<li>클라이언트-&gt; 인터넷 -&gt; 서버  혹은 그 반대 통신은<br>
여러 노드들을 거쳐 이루어진다.</li>
</ul>
<h3 id="인터넷-프로토콜ip-internet-protocol">인터넷 프로토콜(IP, Internet Protocol)</h3>
<ul>
<li>지정한 IP Address에 데이터를 전달.</li>
<li>데이터 전달의 단위는 패킷(packet).</li>
</ul>
<p><strong>IP패킷 정보</strong></p>
<table border="3">
<tbody><tr><td>출발/목적지 IP 등<table border="3"><tbody><tr><td>전송 데이터</td></tr></tbody></table></td>
</tr></tbody></table>
<p><strong>패킷 전달의 예시와 IP프로토콜의 한계</strong><br>
👉: 200.200.200.2에 <a href="http://www.google.com">www.google.com</a> 정보를 구해오세요!<br>
😙: 패킷(출발/목적지 IP + <a href="http://www.google.com">www.google.com</a>)을 들고 떠남<br>
🤔: (각 노드를 거치며) 200.200.200.2로 가려면 어떻게 해야해요?<br>
😎: 목적지에 도착한 경우 문제가 없다,</p>
<p>하지만,</p>
<p>🤠: 목적지에 왔으니까 그냥 패킷을 전달만 하면 되겠지?<br>
-&gt;  받을 대상이 없어지거나, 서비스 불능인 상황이라도 전송하게 됨<br>
-&gt; <strong>비연결성</strong></p>
<p>😜: 노드 속에서 길을 잃어버렸다~<br>
🤪: 패킷들이 서로 다른 노드를 타고 가다가 다른 순서로 와버렸네?<br>
(전송 데이터 크기가 너무 크면 여러 패킷으로 나누어 전송하기에 순서중요)<br>
-&gt; <strong>비신뢰성</strong></p>
<p>😡: IP에 전세냈나? 다른 프로그램이 통신을 차지하고 있으니 통신이 안 되네.<br>
-&gt; <strong>프로그램 구분 불가</strong></p>
<p>-&gt; 이러한 한계로 인하여 TCP 기술이 필요.</p>
<h2 id="tcpudp">TCP/UDP</h2>
<h3 id="인터넷-프로토콜-스택의-4계층">인터넷 프로토콜 스택의 4계층</h3>
<ul>
<li>어플리케이션 계층: HTTP, FTP</li>
<li>전송 계층: TCP, UDP</li>
<li>인터넷 계층: IP</li>
<li>네트워크 인터페이스 계층: LAN카드 드라이버 등</li>
</ul>
<table border="1" cellspacing="3" cellpadding="1">
<tbody><tr>
<td>어플리케이션</td><td>웹 브라우저, 네트워크 게임, 채팅 프로그램<br>
<table border="1"><tbody><tr><td>SOCKET 라이브러리</td></tr></tbody></table>
</td></tr> 
<tr>
<td>OS<br>Win/Linux 등</td><td><table border="1"><tbody><tr><td>TCP</td><td>UDP</td></tr></tbody></table>
<table border="1"><tbody><tr><td>IP</td></tr></tbody></table></td>
</tr>
<tr>
<td>네트워크 인터페이스</td><td>LAN드라이버, LAN 장비<table border="1"><tbody><tr><td>LAN카드</td></tr></tbody></table>
</td></tr></tbody></table>
<ol>
<li>웹브라우저 등 어플리케이션에서 메시지 생성</li>
<li>SOCKET 라이브러리 통해 HTTP메시지 전달</li>
<li>OS의 TCP에서 TCP정보 생성</li>
<li>IP에서</li>
</ol>
<h2 id="tcp-3way-handshake">TCP 3way Handshake</h2>
<ol>
<li>SYN(CLI → SRV)</li>
<li>SYN+ACK(SRV→CLI)</li>
<li>ACK(CLI→SRV)</li>
</ol>
<ul>
<li>SYN: 접속 요청</li>
<li>ACK: 요청 수락</li>
<li>3의 ACK와 함께 데이터 전송이 가능하다.</li>
<li>이 소켓 연결은 개념적/논리적 연결, 즉 가상 연결이다. (물리적 연결이 아님!)</li>
</ul>
<ol>
<li><strong>데이터 전달이 보증된다.</strong></li>
<li>순서가 보장된다.</li>
</ol>
<h2 id="udp-프로토콜user-datagram-protocol">UDP 프로토콜(User Datagram Protocol)</h2>
<ul>
<li>기능 거의 없음: TCP와 달리 전달 보증, 순서 보증을 하지 않음.</li>
<li>IP 기능 + 포트(PORT) + 체크섬(메시지 검증) + etc
<ul>
<li>포트: 동 IP에 다수 패킷이 올 때 해결</li>
</ul>
</li>
<li>단순하고 빠름 ↔ TCP</li>
<li>표준 TCP의 경우는 그냥 사용하지만, 커스텀이 필요할 때 UDP를 애플리케이션에서 추가 작업함.</li>
<li>HTTP3는 UDP사용</li>
</ul>
<h2 id="포트port">포트(PORT)</h2>
<ul>
<li>동 클라이언트에서 다수의 프로세스 통신 문제 해결</li>
</ul>
<p>Ex) CLI1의 8090포트(게임) - 서버1의 11220포트</p>
<p>CLI1의 21000포트(화상통화) - 서버1의 32202 포트</p>
<p>CLI1의 10010포트(웹 브라우저) - 서버2의 80포트</p>
<ul>
<li>위 사례의 경우 CLI1은 동 IP임에도 각각 다른 프로세스의 여러 패킷이 왔을 때 각각 전달받는 포트가 있기에 문제 없음.</li>
<li>SRV1도 마찬가지.</li>
</ul>
<h3 id="tcp-세그먼트">TCP 세그먼트</h3>
<ul>
<li>출발, 목적지 PORT</li>
<li>TCP/IP 패킷은 수신에서 출발 IP PORT정보, 이것을 발신시에</li>
</ul>
<h3 id="포트">포트</h3>
<ul>
<li>0~65535(16bit, 2byte) 할당 가능</li>
<li>0~1023 포트는 잘 알려진 포트로, 함부로 사용 X.</li>
<li>FTP: 20, 21</li>
<li>TELNET 23</li>
<li>HTTP 80</li>
<li>HTTPS 443</li>
</ul>
<h2 id="dns">DNS</h2>
<ul>
<li>IP 는 길고 변경 가능하기에 대신 사용.</li>
<li>Domain Name System(전화번호부와 유사)</li>
<li>도메인명 → IP</li>
<li>URL</li>
</ul>
<p><code>foo://example.com:8042/over/there?name=ferret#nose</code></p>
<ul>
<li>URN</li>
</ul>
<p>`urn: example: animal: ferret: nose</p>
<ul>
<li>Scheme:
<ul>
<li>
<ul>
<li>foo</li>
</ul>
</li>
<li>-urn</li>
</ul>
</li>
<li>authority: <a href="http://example.com">example.com</a>: 8042</li>
<li>path:
<ul>
<li>over/there</li>
<li>example:animal:ferret:nose</li>
</ul>
</li>
<li>query: ?name=ferret</li>
<li>fragment:#nose</li>
</ul>
<p><strong>URL: Uniform Resource Identifier</strong></p>
<p>Uniform → 리소스를 식별하는 통일된 방식</p>
<p>Resource → URI가 식별 가능한 모든 것</p>
<p>Identifier → 다른 항목과 구분하는 데 필요한 정보</p>
<p><strong>URL과 URN</strong></p>
<ul>
<li>URL: 리소스 있는 위치을 지정(가변)</li>
<li>URN: 리소스에 이름을 지정(불변)
<ul>
<li>ex) ISBN 8960777331</li>
<li>URN이름만으로 실제 리소스를 찾는 방법은 보편화되지 않음. 사용 적음.</li>
</ul>
</li>
</ul>
<p><strong>URL 전체문법</strong></p>
<pre class=" language-html"><code class="prism  language-html">scheme://[userinfo@]host[:port}[/path][?query][#fragment]
&lt;https://www.google.com:443/search?q=hello&amp;hl=ko&gt;

</code></pre>
<ul>
<li>
<p>Scheme: 주로 프로토콜 사용</p>
<ul>
<li>프로토콜: 어떤 방식으로 Resource에 접근하느냐 하는 (CLI-SRV간) 약속, 규칙</li>
</ul>
<p>ex) http, https(HTTP Secure), ftp, …etc</p>
</li>
<li>
<p>Userinfo: 사용 드물다. URL에 사용자 정보를 포함하여 인증</p>
</li>
<li>
<p>Host: 호스트명. 도메인명 혹은 IP주소를 직접 사용 가능하다.</p>
</li>
<li>
<p>Port: 접속포트, 일반적으로 생략. http(80), https(443)등 생략 가능.</p>
</li>
<li>
<p>Path: 리소스 경로, 계층 구조</p>
</li>
<li>
<p>query: key=value 형태로 웹 서버에 제공하는 파라미터</p>
<p>?로 시작하며, &amp;로 추가 가능. `?keyA=valA&amp;keyB=valB)</p>
<p>query parameter, query string(문자열로 넘기기에) 등으로 불림.</p>
</li>
<li>
<p>fragment: html 내부 북마크 등에서 사용하며, 서버로 전송 안 한다. 사용 적다.</p>
</li>
</ul>
<h2 id="웹-브라우저-요청-흐름">웹 브라우저 요청 흐름</h2>
<ol>
<li>웹 브라우저에서 요청(<a href="https://www">https://www</a>…?q…)</li>
<li>DNS조회하여 목적지의 IP, PORT 찾음</li>
<li>HTTP 요청 메시지 작성</li>
</ol>
<p>(세부 추후 입력)</p>
<h2 id="http">HTTP</h2>
<ul>
<li>HyperTextTransferProtocol</li>
<li>거의 모든 형태의 데이터 전송 가능:
<ul>
<li>html, text</li>
<li>json, xml(api)</li>
<li>서버 간 데이터도 대부분 http</li>
<li>이미지, 음성, 영상, 파일</li>
</ul>
</li>
</ul>
<h3 id="http-역사">HTTP 역사</h3>
<ul>
<li>http 0.9(1991) GET메서드만 지원, http 헤더는 없음</li>
<li>http 1.0(1996) 메서드, 헤더 추가</li>
<li>http 1.1(1997) 가장 많이 사용
<ul>
<li>RFC 2068(1997) → RFC2616(1999, 다수 사용) → RFC 7230~7235(2014)</li>
</ul>
</li>
<li>http 2 (2015) 성능 개선</li>
<li>http 3 (진행중) TCP → UDP, 성능개선</li>
</ul>
<h3 id="기반-프로토콜">기반 프로토콜</h3>
<ul>
<li>http 1.1 ~ 2 → TCP</li>
<li>http 3 → UDP</li>
</ul>
<h3 id="특징">특징</h3>
<ul>
<li>클라이언트 서버 구조</li>
<li>무상태 프로토콜 지향(stateless), 비연결성</li>
<li>http메시지</li>
<li>단순함, 확장기능</li>
</ul>
<h3 id="클라이언트-서버-구조">클라이언트 서버 구조</h3>
<ul>
<li>
<p>Request - Response 구조</p>
<ul>
<li>클라이언트: 요청, 응답 대기</li>
<li>서버: 요청결과 만들어 응답</li>
<li>이러한 분리로 인해 CLI→UX/UI에, SRV→비즈니스 로직에 집중 가능</li>
<li>이 요청-응답 구조는 연쇄적으로 구성될 수 있다. 즉 웹 브라우저(요청)에 대해 서버 역할(응답)을 하던 중계 서버는 연결된 서버들에 대해 클라이언트 역할(요청)이기도 하다.</li>
</ul>
</li>
<li>
<p>무상태 프로토콜</p>
<ul>
<li>Stateless: 서버가 클라이언트의 상태를 보존하지 않는다.
<ul>
<li>↔ Stateful: 상태 유지, 항상 같은 서버가 유지한다. 때문에 중간에 서버 장애시 처음부터 다시 작업을 수행해야 한다.</li>
</ul>
</li>
<li>장점: 서버 확장성이 높다: 스케일아웃(수평 확장)이 용이하다.
<ul>
<li>클라이언트 요청이 급증할 경우, 서버를 대거 투입 가능하다. 응답 서버를 쉽게 바꿀 수 있기에 무한한 서버 증설이 가능하다.</li>
</ul>
</li>
<li>단점: 클라이언트가 추가 데이터를 전송해야 한다.</li>
<li>한계: 로그인 등을 구현하기 위해선 상태 유지(Stateful)이 필요하다. 다만 상태 유지는 필요한 경우에만 최소한으로 사용해야 한다.
<ul>
<li>서비스 소개 화면은 무상태로 구현하고, 로그인 등의 기능이 필요한 화면은 로그인 상태를 서버에 유지시키거나, 브라우저 쿠기, 서버 세션 등을 이용하여 상태유지한다.</li>
</ul>
</li>
</ul>
</li>
<li>
<p>비연결성(Conectionless)</p>
<ul>
<li>
<p>연결을 유지하는 모델의 경우 불필요하게 서버 자원을 계속 소모하게 된다.</p>
</li>
<li>
<p>일반적으로 응답은 초 단위 이하의 빠른 속도로 이루어지기에, 사용자가 다수라 할지라도 동시처리하는 실서버 요청은 매우 작다.</p>
</li>
<li>
<p>TCP/IP연결 → 요청 → 응답 → TCP/IP 연결 종료식으로 연결을 끊어준다면 최소한의 자원만 소모한다.</p>
</li>
<li>
<p>http는 기본적으로 연결을 유지하지 않는 모델이다.</p>
</li>
<li>
<p>한계와 극복</p>
<ul>
<li>TCP/IP 연결을 그때그때 새로 맺어야 한다. → 3way handshake 시간 추가</li>
<li>웹 사이트 요청시 html 외 css js, 이미지 등 많은 자원을 다운로드</li>
<li>현재는 http지속 연결 Persistent Connection으로 해결한다. (http 2, 3에서 더 최적화)</li>
<li>c html d→??</li>
<li>특정 시각 이벤트, 접수 오픈 등 동시접속 트래픽이 한 순간 급상승하는 상황에 대비하려면 해당 페이지로 접근하기 이전에 정적 페이지로 첫 페이지를 만들어 놓고 게임, 설문 등 시간 소요 컨텐츠를 이용한 후에 해당 페이지로 접근하게끔 만들어두면 좋다. 한 순간에 몰리는 트래픽이 줄어들기 때문이다.</li>
</ul>
</li>
</ul>
</li>
</ul>
<h2 id="http-메시지">HTTP 메시지</h2>
<h3 id="http-메시지의-구조">HTTP 메시지의 구조</h3>
<ul>
<li>start-line 시작 라인</li>
<li>헤더</li>
<li>empty line 공백라인(CRLF)</li>
<li>message body(없을 수도 있다)</li>
</ul>
<p>시작 라인</p>
<ul>
<li>start-line=request-line=status-line</li>
<li>Method SP(공백) request-target SP HTTP-version CRLF(엔터)</li>
<li>요청 메시지
<ul>
<li><strong>HTTP 메서드</strong>: GET, POST, PUT, DELETE등, 서버가 수행해야 할 동작 지정</li>
<li><strong>요청 대상</strong>: absolute-path[?query]</li>
<li>HTTP 버전</li>
</ul>
</li>
<li>응답 메시지
<ul>
<li>status-line: HTTP version SP status-code SP reason-phrase CRLF
<ul>
<li>reason-phrase이유문구: 사람이 이해할 수 있는 설명글 ex) OK</li>
</ul>
</li>
</ul>
</li>
</ul>
<h3 id="http-헤더">HTTP 헤더</h3>
<ul>
<li>header-field = field-name “:” OWS(띄어쓰기 허용) field-vals OWS
<ul>
<li>field-name: 대소문자 구분 없음, value는 구분</li>
</ul>
</li>
<li>용도
<ul>
<li>HTTP전송을 위한 모든 부가정보</li>
<li>메시지 바디 제외, 메타데이터 전체</li>
<li>임의 헤더 추가 가능</li>
</ul>
</li>
<li>메시지 바디: byte로 표현 가능한 모든 데이터(*HTTP스펙)</li>
</ul>
<h3 id="http-메서드">HTTP 메서드</h3>
<ul>
<li>HTTP API 제작을 통한 예시
<ul>
<li>회원 CRUD 관리예시
<ul>
<li>잘못된 예시
<ul>
<li>회원목록조회 /read-member-list</li>
<li>회원 삭제 /delete-member</li>
<li>회원 등록 /create-member</li>
<li>이 경우 URI를 모두 다르게 했음</li>
<li>URI의 리소스 식별이 중요하다!</li>
<li>여기서 핵심은 “회원”, 이를 기반으로 URI계층구조를 사용한다면:</li>
<li>회원 등록, 수정, 삭제 등등이 모두 /members로 중복될 것이다. 이를 구분할 방법이 필요하다.</li>
</ul>
</li>
<li>리소스와 행위의 구분
<ul>
<li>동일 리소스(members)에 대한 접근이기에 URI를 같게 한 것이다.</li>
<li>반면 그 리소스에 대한 행위: 조회, 등록, 수정, 삭제는 메서드를 통해 구별한다.</li>
</ul>
</li>
</ul>
</li>
</ul>
</li>
<li>HTTP 주요 메서드
<ul>
<li>GET: 리소스 조회</li>
<li>POST: 요청 데이터 처리(주로 등록)</li>
<li>PUT: 리소스를 대체, 해당 리소스가 없을 경우 생성</li>
<li>PATCH: 리소스 부분 변경</li>
<li>DELETE: 리소스 삭제</li>
</ul>
</li>
<li>HTTP 기타 메서드
<ul>
<li>HEAD: GET과 기본적으로는 동일하지만, 메시지 부분을 제외하고 상태 줄과 헤더만 반환한다.</li>
<li>OPTIONS: 대상 리소스에 대한 통신 가능 옵션(메서드)을 설명 (주로 CORS에서 사용)</li>
<li>CONNECT:대상 자원으로 식별되는 서버에 대한 터널을 설정</li>
<li>TRACE: 대상 리소스에 대한 경로를 따라 메시지 루프백 테스트를 수행</li>
</ul>
</li>
<li>GET
<ul>
<li>리소스 조회</li>
<li>서버에 전달할 데이터는 query(쿼리 파라미터, 스트림) 통해 전달</li>
<li>메시지 바디로 데이터를 전달하는 것이 가능은 하지만, 지원되지 않는 경우도 있다.</li>
</ul>
</li>
<li>POST
<ul>
<li>
<p>요청 데이터를 처리</p>
</li>
<li>
<p>메시지 바디를 통해 서버로 요청 데이터 전달, 서버는 요청 데이터 처리</p>
</li>
<li>
<p>메시지 바디 통해 들어 온 데이터를 처리하는 모든 기능 수행</p>
</li>
<li>
<p>주로 전달된 데이터로 신규 리소스 등록, 프로세스 처리에 사용</p>
</li>
<li>
<p>POST 용례</p>
<ul>
<li>HTML 양식에 입력된 필드 같은 데이터 블록을 데이터 처리 프로세스에 가공
<ul>
<li>ex) HTML Form에 입력한 정보로 회원가입, 주문</li>
</ul>
</li>
<li>게시판, 뉴스 그룹 등에 메시지 게시
<ul>
<li>ex) 게시판 글 쓰기, 댓글 달기</li>
</ul>
</li>
<li>서버가 아직 식별하지 않은 새 리소스 생성
<ul>
<li>ex)신규 주문 생성</li>
</ul>
</li>
<li>기존 자원에 데이터 추가
<ul>
<li>한 문서 끝에 내용 추가하기</li>
</ul>
</li>
</ul>
</li>
<li>
<p>리소스 URI에 POST 요청이 오면 요청 데이터를 어떻게 처리할 지 리소스마다 따로 정해야 한다.</p>
</li>
<li>
<p>POST를 언제 사용할지</p>
<ol>
<li>새 리소스 생성</li>
<li>요청 데이터 처리: 생성, 변경 수준이 아니라 프로세스 처리시에 POST의 결과로 새 리소스 생성이 안 될수도 있음</li>
<li>다른 메서드로 처리하기 애매할 경우: 조회용 json 전달 등 GET 사용이 힘들 때 등</li>
</ol>
<ul>
<li>리소스 단위 URI 설계가 가상 이상적이지만, 현실적으로 이를 지킬 수 없는 경우 동사형 URI같은 컨트롤 URI 사용을 고려하라.</li>
</ul>
</li>
</ul>
</li>
<li>PUT
<ul>
<li>리소스를 (완전히!) 대체, 리소스가 없을 경우 생성
<ul>
<li>가령 기존에는 property1: abc, property2: 50의 정보를 가지고 있던 리소스에 property2:100을 PUT할 경우 property1 구역 자체가 아예 사라져 버린다.</li>
</ul>
</li>
<li>POST와의 차이: PUT의 경우 클라이언트가 리소스를 식별하여, URI를 지정, 반면 POST는 서버에서 구체적인 위치 지정</li>
</ul>
</li>
<li>PATCH
<ul>
<li>PUT과 달리 property2만 변경하는 식으로 수정이 가능하다.</li>
<li>단 서버에 따라 PATCH를 지원 안 하기도 한다(최근에는 적은 사례). 그 경우 POST사용한다.</li>
</ul>
</li>
<li>DELETE: 리소스 제거</li>
<li>HTTP 메서드의 속성
<ul>
<li>안전Safe
<ul>
<li>호출해도 리소스를 변경하지 않음: 단 이는 해당 리소스만 고려한 것으로, 외부 요인으로 중간에 리소스가 변경되는 것은 고려 안 한다.</li>
<li>ex) GET, HEAD, OPTIONS, TRACE</li>
</ul>
</li>
<li>멱등Idempotent
<ul>
<li>f(f(x))=f(x), 몇 번 호출해도 결과가 똑같음</li>
<li>ex) GET, PUT, DELETE</li>
<li>POST는 멱등이 아니다!</li>
<li>활용-자동 복구 메커니즘: 서버가 정상 응답 못 할때, 클라이언트가 다시 요청 가능</li>
</ul>
</li>
<li>캐시 가능Cacheable
<ul>
<li>응답 결과 리소스를 캐시해서 사용해도 된다, 단 key가 맞아야</li>
<li>ex) GET, HEAD, POST, PATCH</li>
<li>단 POST, PATCH의 경우 본문 내용까지 캐시 키로 고려해야 하며, 어렵다.</li>
<li>실질적으로는 GET, HEAD를 사용하며, 이 경우 Url만 키로 사용하면 된다.</li>
</ul>
</li>
</ul>
</li>
<li>HTTP메서드 활용</li>
</ul>
```