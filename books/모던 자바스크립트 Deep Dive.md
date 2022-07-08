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



# 03장 자바스크립트 개발 환경과 실행 방법

## 3.1 자바스크립트 실행 환경

- 자바스크립트 실행 환경이면 **ECMAScript는 공통적으로 호환되지만, 다른 부분에서 차이가 있음**

- 자바스크립트 엔진을 내장한 환경들
  - 브라우저
    - 웹 페이지를 브라우저 화면에 렌더링하기 위함
    - DOM API 등 Client-side API들을 제공
      - DOM, BOM, Canvas, XMLHttpRequest, fetch, requestAnimationFrame, SVG, Web Storage, Web Component, Web Worker, ...
      - DOM API: HTML 요소를 파싱, 객체화한 것
  - Node.js
    - 브라우저 외부에서 자바스크립트 실행 환경을 제공
    - 파일 시스템 등 Node.js Host API들을 제공
      - 단 브라우저의 경우 Web API인 FileReader 객체를 이용하여 읽기는 가능
    - 기본적으로 DOM API를 제공하지 않지만 웹 크롤링을 위해 DOM 라이브러리를 이용하기도 함



## 3.2 웹 브라우저

- 구글 크롬 브라우저
  - ECMAScript 사양 준수
  - 가장 높은 시장 점유율
  - V8 자바스크립트 엔진: Node.js에서도 사용



### 3.2.1 개발자 도구

- 크롬에서 제공하는 DevTools

  - F12 or Ctrl + Shift + I

  - 주요 기능

    | 패널        | 설명                                                         |
    | ----------- | ------------------------------------------------------------ |
    | Elements    | 로딩된 웹 페이지의 DOM과 CSS를 편집, 렌더링된 뷰를 확인 가능<br />편집된 내용이 저장되지는 않음 |
    | Console     | 로딩된 웹 페이지의 에러 확인<br />console.log의 실행 결과 확인 |
    | Sources     | 로딩된 웹 페이지의 자바스크립트 코드 디버깅                  |
    | Network     | 로딩된 웹 페이지에 관한 네트워크 요청 정보와 성능 확인       |
    | Application | 웹 스토리지, 세션, 쿠키 확인, 실행                           |



### 3.2.2 콘솔

- REPL; Read Eval Print Loop: 입력 수행 출력 반복 환경으로도 사용 가능
  - 직접 자바스크립트 코드를 입력해 결과를 확인 가능



## 3.3 Node.js

- 프로젝트 규모가 커질 경우 프레임워크, 라이브러리, 툴들을 도입하기 위해
  Node.js + npm이 필요

- NPM; Node Package Manager
  - 자바스크립트 패키지 매니저
  - 패키지 저장소 + 관리를 위한 CLI 환경 제공

- Node.js REPL
  - `node`를 터미널에 입력하여 실행 가능
  - `node 파일명` 형태로 `.js` 파일 실행 가능



## 3.4 비주얼 스튜디오 코드

- 확장 플러그인
  - Code Runner
    - Ctrl + Alt + N으로 파일 실행 가능
  - Live Server
    - 수정사항을 바로 브라우저에 반영해주는 플러그인
    - 가상 서버를 가동



# 04장 변수

- 자바스크립트는 개발자의 직접적인 메모리 제어를 허용하지 않음
- 변수
  - 하나의 값을 저장하기 위해 확보한 메모리 공간
  - 메모리 공간을 식별하기 위해 붙인 이름

- 변수명은 식별자임
  - 식별자는 값 자체가 아니라 메모리 주소를 기억하고 있음
  - 변수, 함수, 클래스의 이름은 모두 식별자



## 4.3 변수 선언

- 변수 선언 방식
  - var
  - let(ES6 이후 도입)
  - const(ES6 이후 도입)
- var
  - 블록 레벨 스코프를 지원하지 않고, 함수 레벨 스코프를 지원
  - var는 deprecated 되진 않았지만, 권장되지는 않음

- 자바스크립트 엔진의 변수 선언

  - 변수명을 등록하고, 메모리 공간을 확보

  - **값이 할당되지 않은 메모리 공간은 비어 있지 않고,**
    **`undefined`가 암묵적 할당됨!!!**

  - 변수 선언의 단계

    - 선언 단계: 변수명을 등록, 엔진에 변수의 존재를 알림

    - 초기화 단계: 메모리 공간 확보, `undefined` 할당

  - `var` 사용한 변수 선언은 **선언과 초기화가 동시에 진행됨!**
  - `ReferenceError`: 선언하지 않은 식별자에 접근하려 하면 발생



## 4.4 변수 선언의 실행 시점과 변수 호이스팅

```javascript
console.log(score);		// undefined 출력됨

var score;				// 변수 선언
```

- **변수 선언보다 변수 참고가 먼저 일어나지만 `ReferenceError`가 발생하지 않음**
- **변수 선언은 런타임이 아니라 그 이전에 실행되기 때문임**
- 자바스크립트 엔진의 실행 순서
  - 소스코드 실행 준비
    - 소스코드 평가 과정: 모든 선언문을 먼저 실행
      - `var`, `let`, `function*`, `class`로 선언하는 모든 식별자들이 해당
  - 선언문을 제외한 소스코드를 한 줄씩 순차적으로 실행

- **변수 호이스팅(variable hoisting)**
  - 인터프리터가 변수 및 함수의 메모리 공간을 선언 전에 미리 할당하는 것
  - 선언들을 함수 유효 범위의 최상단에 선언함

- 주의사항
  - `var` 변수 및 함수의 선언만 해당, **할당은 해당되지 않음!**
  - **`let`, `const`, 함수표현식의 경우 호이스팅 발생하지 않음!**



## 4.5 값의 할당

- 자바스크립트의 경우 선언과 할당을 하나의 statement로 단축 표현해도
  선언과 할당을 2개의 statement로 나누어 실행한다.

  - 즉, 다음 둘의 실행은 동일하다.

    ```javascript
    var score;
    score = 80;
    ```

    ```javascript
    var score = 80;
    ```

- **주의! 실행 시점**

  - 변수 선언: 런타임 이전에 실행(`undefined`로 초기화)
  - 값의 할당: 런타임 실행

- 변수에 값을 할당할 때는 `undefined`가 할당된 메모리 공간을 지우고 그 자리에 할당하는 것이 아님
- **새로운 메모리 공간을 확보, 새 값을 할당함!**



## 4.6 값의 재할당

- `var`로 선언한 변수는 값을 재할당할 수 있음
  - 엄밀히 따지면 `var` 변수는 `undefined`로 초기화되기에 
    처음으로 값을 할당하는 것도 재할당임
  - 재할당할 경우 기존 메모리 공간을 건드리지 않고, 새 메모리 공간 확보하여 값 할당
  - 재할당으로 인해 필요 없어진 값은 GC에 의해 메모리에서 자동 해제됨
- `const` 키워드를 사용하면 재할당 불가: 상수 표현 가능
  - `const`는 상수 이외의 목적으로도 사용 가능



## 4.7 식별자 네이밍 규칙

- camelCase
  - 변수명, 함수명에 사용
- PascalCase
  - 생성자 함수, 클래스명
