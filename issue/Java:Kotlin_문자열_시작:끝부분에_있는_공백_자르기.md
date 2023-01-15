# Java / Kotlin 문자열 시작 / 끝부분에 있는 공백 자르기

- Java의 경우
    - `String.trim()` 메서드
    - `" "`(space)만 잘라냄, unicode에 있는 특문 공백의 경우 잘라내지 못함
    - `String.strip()` 메서드
        - unicode 특문 공백도 잘라냄
- Kotlin의 경우
    - `String.strip()`이 `deprecated`되어 있음:
        현재 코틀린 컴파일러에서는 지원하지 않는 메서드이기 때문
    - `String.trim()` 메서드를 사용해도 유니코드 특수문자 공백까지 잘라내는 것을 확인할 수 있었음
        -> 코틀린에서는 그냥 `String.trim()` 쓰자.