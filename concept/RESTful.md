# RESTful

##  [MVC와 RESTful API 서비스](https://softwareengineering.stackexchange.com/questions/324730/mvc-and-restful-api-service)

- MVC는 서버 내부 구조화 방식임
  - REST / HTTP와는 무관
- REST는 클라이언트 <-> 서버 간 통신 방식과 유관
  - 서버가 클라이언트에 JSON 개체를 보내건, HTML 템플릿을 보내건 간에 상관 없음



## 그런 REST API로 괜찮은가

### REST라는 개념에 대해서

- REpresentational State Transfer
- a way of providing **interoperability** between computer systems on the Internet.



### REST라는 개념이 등장한 배경

- WEB(1991)
  - 어떻게 인터넷에서 정보를 공유할 것인가?
  - 팀 버너스 리: 정보들을 하이퍼텍스트로 연결한다.
    - 표현방식: HTML
    - 식별자: URI
    - 전송방법: HTTP
- HTTP / 1.0 (1994~1996)
  - Roy T. Fielding: "How do I improve HTTP without breaking the Web?"
  - 기존에 구축된 웹과 어떻게 호환성을 가질 수 있을까?
  - 해결법: HTTP Object Model
  - 4년 후, REST로 발표됨
- REST(1998)
  - Roy T. Fielding, Microsoft Research에서 발표
- REST(2000)
  - 박사 논문으로 발표



### API

- XML-RPC(1998)
  - Microsoft
  - 이후 SOAP으로 변화
- Salesforce API(2000.2)
  - 인터넷에 공개된 거의 최초의 API
  - 지나치게 복잡하여 인기를 끌지 못함
- flickr API(2004.8)
  - REST
    - SOAP에 비해 짧다.
- 사람들이 받은 인상
  - SOAP: 복잡, 규칙 많음, 어려움
  - REST: 단순, 규칙 적음, 쉬움
  - 그 결과로 SOAP의 하락, REST의 인기 급상승
- AWS: 자사 API 사용량의 85%가 REST임을 밝힘(2006)
- Salesforce.com에서 REST API 추가(2010)
- **REST의 승리?**



### "진짜" REST API?

- CMIS(2008)
  - CMS를 위한 표준
  - EMC, IBM, Microsoft등 함께 작업
  - REST 바인딩 지원
  - **Roy T. Fielding: "NO REST in CMIS"**
- Microsoft REST API Guidelines(2016)
  - URI는 `https://{serviceRoot}/{collection}/{id}` 형식이어야 한다,
  - GET, PUT, DELETE, POST, HEAD, PATCH, OPTIONS를 지원해야 한다,
  - API 버저닝은 Major.minor로 하고 uri에 버전 정보를 포함시킨다, ... etc
  - **Roy T. Fielding: "s/REST API/HTTP API": 이건 REST가 아니라 HTTP API다!**
- **REST APIs must be hypertext-driven**
- **"REST API를 위한 최고의 버저닝 전략은 버저닝을 안 하는 것"**



### REST API

- REST 아키텍쳐 스타일을 따르는 API
- REST: 분산 하이퍼미디어 시스템(ex. 웹)을 위한 아키텍처 스타일
- 아키텍처 스타일: 제약조건의 집합
- REST를 구성하는 스타일: hybrid 아키텍처 스타일: 아키텍처 스타일이면서, 아키텍처 스타일의 집합이기도 함
  - client-server
  - stateless
  - cache
  - **uniform interface**
  - layered system
  - code-on-demand(optional)
    - 서버에서 코드를 클라이언트로 보내 실행할 수 있어야 한다: 자바스크립트!
- 대부분은 HTTP만 잘 따라도 지켜진다. 문제가 되는 것은 특히 uniform interface



### Uniform interface 제약조건

- identification of resources: 리소스가 URI로 식별되어야
- manipulcation of resources through representations: 리소스의 조작은 HTTP 메서지 안에 그 표현을 담아서 이루어져야
- **self-descriptive messages**
- **hypermedia as the engine of application state(HATEOAS)**



#### Self-descriptive message

- GET 할 때 HOST표기, Json 보낼 떄 Content-Type 명시 등
- 여기에 추가적으로`json-patch-json`: `json-patch`라는 명세를 통해 이해



#### HATEOAS

- 애플리케이션의 상태는 하이퍼링크를 통해 전이되어야 한다.
- HTML: HATEOAS를 충족
- json: `Link` 헤더를 사용, 다른 링크를 가리켜 주는 헤더



#### Uniform Interface 사용 이유

- 서버와 클라이언트의 독립적 진화를 위함
  - 서버 기능이 변경되어도 클라이언트를 업데이트할 필요가 없음
  - 기존 웹을 깨트리지 않으면서 HTTP를 발전시키려는 REST 제작 의도와 같음



### REST 준수 사례

- 웹
  - 웹 페이지가 변경되었다고 브라우저를 업데이트할 필요가 없음, 반대도 마찬가지
  - HTTP, HTML이 변경되어도 웹은 잘 동작함
  - UI 깨지는 경우는 있어도 최소한 동작은 한다.
- 상호운용성(interoperability)에 대한 집착
  - Referer 오타지만 고치지 않음
  - charset 잘못 지은 이름이지만 고치지 않음
    - 인코딩이 적절
  - HTTP 상태코드 416 포기함(만우절 I'm a teapot)
  - HTTP / 0.9 아직도 지원: 크롬, 파이어폭스



### REST와 웹의 독립적 진화

- HTTP에 지속적 영향
- Host 헤더 추가
- 길이 제한 다루는 방법 명시: 414 URI Too Long
- URI에서 리소스의 정의가 추상적으로 변경: 식별하고자 하는 무언가
- HTTP / 1.1 명세 최신판에서 REST에 대한 언급이 들어감
- 참고) Roy T. Fielding은 HTTP, URI 명세 저자 중 한 사람임



- REST 제약조건을 지켜야 REST API라 부를 수 있음
- 시스템 전체를 통제할 수 있다고 생각하거나, 진화에 관심이 없다면 REST에 대해 따지느라 시간을 낭비하지 마라.



- REST라 부를거면 제대로 구현하고
- 그렇지 않다면 HTTP API라 부르거나 하자



### API에서 REST가 잘 안 지켜지는 까닭

|              | 일반적 웹 페이지 | HTTP API  |
| ------------ | ---------------- | --------- |
| Protool      | HTTP             | HTTP      |
| 커뮤니케이션 | 사람-기계        | 기계-기계 |
| Media Type   | HTML             | JSON      |

- HTML과 JSON의 차이

  |                  | HTML          | JSON               |
  | ---------------- | ------------- | ------------------ |
  | Hyperlink        | 됨(a 태그 등) | 정의되어 있지 않음 |
  | Self-descriptive | 됨(HTML 명세) | 불완전             |

  - 문법 해석은 가능하지만 의미를 해석하려면 별도로 문서(API 문서 등) 필요
  - JSON에서 "id"가 무엇이고, "title"이 무엇이고 등은 알 수가 없다.



### 확장성 지원

- Self-descriptive
  - 확장 가능한 커뮤니케이션
  - 변경이 발생해도 해석이 가능
- HATEOAS
  - 애플리케이션 상태 전이의 late binding
  - 링크는 동적으로 변경될 수 있다. 어떤 상태로 전이가 완료된 이후에 그 다음 전이될 수 있는 상태가 결정된다.



### API Self-descriptive 적용법

- Media type
  - 미디어 타입을 하나 정의
  - 미디어 타입 문서를 작성: "id"가 무엇인지, "title"이 무엇인지
  - IANA에 미디어 타입을 등록: 문서를 명세로 등록
  - 명세에 따라 메시지의 의미를 온전히 해석 가능
- Profile
  - `Link: <https://....>; rel="profile"`
  - 명세 문서를 작성, Link 헤더에 해당 문서를 링크한다.
  - 단점
    - 클라이언트가 Link 헤더(RFC 5988)와 profile(RFC 6906)을 이해해야 한다.
    - Content negotiation을 할 수 없음



### API HATEOAS 적용법

- data를 이용
  - data에 다양한 방법으로 하이퍼링크를 표현
  - 단점: 링크를 표현하는 방법을 직접 정의해야
  - JSON으로 하이퍼링크를 표현하는 방법들을 정의한 명세들을 활용
    - JSON API, HAL, UBER, Siren, Collection + json ...
    - 단, 기존 API를 많이 고쳐야 한다: 침투적
- HTTP 헤더를 이용
  - Link, Location 등의 헤더로 링크를 표현
  - 단점: 정의된 relation만 활용한다면 표현에 한계가 있음



### 생각 포인트

- 하이퍼링크는 반드시 URI이여야 하나? -> NO
  - 상대경로, 절대경로, URI 템플릿 등등 가능
- 미디어타입 등록은 필수인가? -> NO
  - 의도된 청자가 이해할 수만 있으면 된다.
  - 물론 IANA에 등록하는 것이 좋다.



##  [REST API 제대로 알고 사용하기](https://meetup.toast.com/posts/92)

- 로이 필딩(Roy Fielding)

  - HTTP / 웹의 장점을 제대로 사용할 수 있게 제안한 아키텍처: REST

- 구성

  - 자원(resource): URI
  - 행위(verb): HTTP METHOD
  - 표현(Representations)

- 특징

  - Uniform: 유니폼 인터페이스
    - 지정된 리소스에 대한 조작을 통일되고 한정적인 인터페이스로 수행
  - Stateless: 무상태성
    - 상태 정보를 가지지 않음
  - Cacheable: 캐시 가능
    - HTTP 표준을 따르기에 갖는 특징
    - HTTP의 캐싱 기능이 적용 가능함: HTTP 표준인 다음 기술들로 구현 가능:
      - Last-Modified 태그
      - E-Tag
  - Self-descriptiveness: 자체 표현 구조
    - REST API 메시지만 보고도 쉽게 이해할 수 있는 구조
  - 클라이언트 - 서버 구조
    - 서로간의 역할이 명확히 구분
  - 계층형 구조
    - 다중 계층으로 구성 가능
    - 보안, 로드밸런싱, 암호화 계층 추가 가능: 구조상의 유연성
    - 프록시, 게이트웨이 등 네트워크 기반의 중간매체 사용 가능

- 설계 가이드

  - 핵심

    - URI는 정보의 자원을 표현해야 한다.
    - 자원에 대한 행위는 HTTP Method(GET, POST, PUT, DELETE)로 표현한다.

  - HTTP 메서드의 역할

    | HTTP Method | 역할          |
    | ----------- | ------------- |
    | GET         | 리소스를 조회 |
    | POST        | 리소스 생성   |
    | PUT         | 리소스 수정   |
    | DELETE      | 리소스 삭제   |

  - URI 설계 주의사항
    - `/`는 계층 관계를 나타내는 데 사용
    - URI 마지막 문자로 `/` 포함하지 않음
      - URI는 리소스의 unique 식별자이기 때문에 명확해야 한다.
    - `-` 가독성 용으로 사용
    - `_` 사용하지 않음
    - 소문자 사용해라
    - 파일 확장자는 포함하지 않아야 함
      - Accept Header의 역할
  - 리소스 간의 관계 표현법
    - `/orders/{orderid}/items`: 소유 관계를 표현
    - `/orders/{orderid}/rocket-shipping/items`: 서브 리소스를 이용해서 관계명을 명확히 표현
  -  Collection / Document
    - `products/keyboard`: `products`라는 컬렉션, `keyboard`라는 도큐먼트
    - 도큐먼트는 단수, 컬렉션은 복수로 작성한다면 명확
  - 상태 코드
    - 200: 정상
    - 201: 성공적 생성(POST)
    - 400 요청이 부적절
    - 401: 인증되지 않은 상태에서 요청
    - 403: 인증과 무관하게 응답하고 싶지 않은 리소스
      - 400 or 404를 사용하라: 403은 리소스의 존재를 함의
    - 405: 잘못된 메서드 사용
    - 301: 리다이렉트: location header에 변경된 URI 명시해야
    - 500: 서버에 문제



## 원문 자료들

- https://softwareengineering.stackexchange.com/questions/324730/mvc-and-restful-api-service
- https://meetup.toast.com/posts/92



