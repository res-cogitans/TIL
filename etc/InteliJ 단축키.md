﻿﻿﻿# InteliJ 단축키
- Alt + Enter = Show context actions / static import
- Ctrl Ctrl = run anything
- Ctrl + Shift + Enter = 구문완성: 세미콜론, 반복, 조건문 등
- Ctrl + Shift + Alt + T = 리팩토링
- Shift + F6 = Rename
- Ctrl + Alt + V = 변수 추출
- Ctrl + Shift + T = 테스트 작성, 테스트와 테스트 대상 이동
- Ctrl + Alt + N = Inline Variable
- iter tab = for문 자동완성
- Ctrl + D = 복사
- Ctrl + O = 탐색(라이브러리 밖까지)
- sout -> System.out.println
- psvm -> public static void main(String[] args)
- Shift + Ctrl + Alt + L: 정리
- Ctrl + Alt + P: 변수를 파라미터로 보냄
- Ctrl + E: 최근 사용 검색
- Ctrl + Ctrl + ↓: 멀티 포커스 (Clone Caret Below)
- Ctrl + R = 바꾸기
  - (폴더를 강조한 후에) Ctrl + Shift + R = 폴더 단위로 바꾸기
- Ctrl + P = 파라미터 확인




## 설정(Settings)
- Editor/Live template에서 축약어 규정 가능: psvm sout 같은 것들
- JPA의 모든 데이터 변경 등 로직등은 트랜젝션 안에서 실행되어야: `@Transactional`
- 조회만 하는 곳에는 `@Transactional(readOnly = true)` 성능 향상 가능
- 다른 쓰레드에서 동시 입력된다면 동일 name 가입이 가능해진다. 최후의 제약조건으로 DB차원에서 Unique 하게 설정하라.
- 스프링 데이터 JPA 사용시 EntityManager를
	- `@Autowired`, 필드나 생성자 주입하거나, 롬복의 `RequiredArgsConstructor`(final) 로 주입할 수도 있음.

