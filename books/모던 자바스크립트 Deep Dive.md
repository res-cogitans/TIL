# 모던 자바스크립트 Deep Dive

[TOC]

# 01장 프로그래밍

- 컴퓨팅적 사고(Computational thinking)
  - 문제 해결을 위해 컴퓨터에게 요구사항을 전달하는 것이 프로그래밍
  - 컴퓨터의 관점에서 문제를 사고(Computational thinking)해야

- 의미(Semantics)와 문법(Syntax)

  - 문법에 맞다고 해도 의미 없을 수 있음

  - 언어의 의미는 문맥에 있는 것이지 문법에 있는 것이 아님

    > Colorless green ideas sleep furiously.
    >
    > 노엄 촘스키의 예시

  - 프로그래밍 언어에서도 위는 통용

    > const number = 'string';

    - 문법적으로는 옳으나, 숫자라는 이름의 변수에 문자가 할당됨: 의미적으로 옳지 않음
      - 프로그래밍에서의 의미: **요구사항의 실현(문제의 해결)**



# 02장 자바스크립트란?

## 2.1 자바스크립트의 탄생

- 1995
  - 넷스케이프 커뮤니케이션즈(Netscape communications)에서 도입
  - 브라우저에서 동작하는 경량 프로그래밍 언어
  - 브렌던 아이크(Brendan Eich) 개발
- 1996
  - 브라우저인 넷스케이프 내비게이터(Netscape Navigator)2에 탑재
  - 모카(Mocha)로 명명(1996.03)
  - 라이브스크립트(LiveScript)로 명명(1996.09)
  - 자바스크립트(JavaScript)로 최종 명명(1996.12)



## 2.2 자바스크립트의 표준화

- 1996.08

  - JScript
    - 자바스크립트의 파생
    - 마이크로소프트가 IE 3.0에 탑재
  - JScript와 자바스크립트 간 표준화가 이루어지지 못함 -> **크로스 브라우징 이슈**

- 1996.11

  - ECMA 인터네셔널에 넷스케이프가 자바스크립트 표준화를 요청

- 1996.07: **ECMA-262**

  - 표준화된 자바스크립트 초판(ECMAScript 1) Specification 완성

  - 상표권으로 인해 ECMAScript로 명명

- 1999: ECMAScript 3(**ES3**)

- 2009: ECMAScript 5(**ES5**): HTML5와 함께 등장

- 2015: ESMAScript 6(**ES6**)
  - let/const 키워드, 화살표 함수, 클래스, 모듈 등 도입



### 버전 별 특징

| 버전 | 출시연도 | 특징                                                         |
| ---- | -------- | ------------------------------------------------------------ |
| ES1  | 1997     | 초판                                                         |
| ES2  | 1998     | ISO/IEC 16262 표준 규격                                      |
| ES3  | 1999     | 정규 표현식, try ... catch                                   |
| ES5  | 2009     | HTML5와 함께 등장<br />JSON, strict mode, 접근자 프로퍼티, 프로퍼티 어트리뷰트 제어,<br />향상된 배열 조작(forEach, map, filter, reduce, some, every) |
| ES6  | 2015     | let / const, 클래스, 화살표 함수, 템플릿 리터럴, 디스트럭처링 할당,<br />스프레드 문법, rest 파라미터, 심벌, 프로미스, Map / Set, 이터러블, for ... of,<br />제너레이터, Proxy, 모듈 import / export |
| ES7  | 2016     | 지수(**)연산자, Array.prototype.includes, String.prototype.includes |
| ES8  | 2017     | async/await, Object 정적 메서드(Object.values, Object.entries, Object.getOwnPropertyDescriptors) |
| ES9  | 2018     | Object rest / spread 프로퍼티, Promise.prototype.finally, async generator,<br />for await ... of |
| ES10 | 2019     | Object.fromEntries, Array.prototype.flat, Array.prototype.flatMap,<br />optional catch binding |
| ES11 | 2020     | String.prototype.matchAll, BigInt.globalThis, Promise.allSettled, null 병합 연산자,<br />옵셔널 체이닝 연산자, for ... in enumeration order |



## 2.3 자바스크립트 성장의 역사

### 2.3.1 Ajax

- **Ajax; Asynchronous JavaScript and XML**
  - 자바스크립트를 이용, 서버와 브라우저가 비동기 방식으로 데이터를 교환할 수 있는 통신
  - 1999년 XMLHttpRequest라는 이름으로 등장
- 필요한 부분만 렌더링할 수 있게 되었음
  - 화면 깜박임 해결
  - 성능 향상



### 2.3.2 jQuery

- **DOM; Document Object Model**을 손쉽게 제어
- 크로스 브라우징 이슈 다소 해결
- 2006년 등장



### 2.3.3 V8 자바스크립트 엔진

- 2008년 등장, 구글 제공
- 빠른 성능
  - 서버 로직을 클라이언트 로직으로 다수 이동시킴



### 2.3.4 Node.js

- 라이언 달(Ryan Dahl)이 2009년 발표
- V8 자바스크립트 엔진으로 빌드된 자바스크립트 런타임 환경(runtime environment)

- 브라우저 이외의 환경에서도 자바스크립트가 동작할 수 있음
  - 기존에는 브라우저의 자바스크립트 엔진에서만 동작
- 다수의 빌트인(built-in) API 제공
- 비동기 I/O 지원, 단일 스레드(single thread) 이벤트 루프 기반 동작
  - 요청 처리 성능이 좋음
  - 따라서 I/O가 빈번한 SPA; Single Page Application에 유용
  - CPU 사용률이 높은 애플리케이션에는 부적합



### 2.3.5 SPA 프레임워크

- 웹 어플리케이션 개발을 위한 프레임워크 등장
- CBD; Component Based Development 방법론에 의거한 SPA가 대중화되면서
  다음 프레임워크들이 등장:
  - Angular
  - React
  - Vue.js
  - Svelte



## 2.4 자바스크립트와 ECMAScript

- ECMAScript
  - 자바스크립트 표준 사양: ECMA-262를 뜻함
- 자바스크립트
  - ECMAScript + 브라우저가 별도 지원하는 클라이언트 사이드 Web API:
    - DOM, BOM, Canvas, XMLHttpRequest, fetch, requestAnimationFrame, SVG, Web storage, Web Component, Web Worker 등
- 클라이언트 사이드 Web API
  - 월드 와이드 웹 콘소시엄(W3C; World Wide Web Consortium)에서 관리
  - https://developer.mozilla.org/ko/ 에서 사양 확인



## 2.5 자바스크립트의 특징

- 웹 브라우저에서 동작하는 프로그래밍 언어
- 영향을 받은 언어들
  - 기본 문법: C, Java
  - 프로토타입: Self
  - 일급 함수: Scheme
- 인터프리터 언어
  - 모던 자바스크립트 엔진들은 처리 속도 개선을 이룸
    - 크롬 V8, 파이어폭스 SpiderMonkey, 사파리 JavaScriptCore, 마이크로소프트 엣지 Chakra
    - 모던 브라우저의 인터프리터들은 일부 소스코드를 컴파일하고 실행하기에 성능 개선
- 멀티 패러다임 프로그래밍 언어
  - 명령헝(imperative)
  - 함수형
  - 프로토타입 기반(prototype-based) 객체지향 프로그래밍