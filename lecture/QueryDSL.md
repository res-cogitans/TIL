# QueryDSL

- 최신 자바 프로젝트가 사용하는 기술
  - 스프링부트 + 스프링 데이터 JPA
- 위의 조합의 한계: 복잡한 쿼리 / 동적 쿼리 해결 불가
- Query DSL: 위의 문제를 해결
  - 쿼리를 자바 코드로 작성
    - 코드 자동 완성의 도움을 받을 수 있음
    - 일부분을 메서드로 뽑아내서 재사용이 가능
  - 문법 오류를 컴파일 시점에 확인
    - JPQL의 경우 `String` 기반이기에 문법 오류를 자바 컴파일러가 확인할 수 없으며, 런타임에나 확인할 수 있음
  - 동적 쿼리 문제 해결
  - 쉬운 SQL 스타일 문법



# 프로젝트 환경설정

- Spring Initializer에서 QueryDSL 추가 지원하지는 않음
- 