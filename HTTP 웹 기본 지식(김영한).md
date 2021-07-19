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
<li>DNS서버</li>
</ul>

