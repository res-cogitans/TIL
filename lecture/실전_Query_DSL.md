# Query DSL

[TOC]

## 개발환경 세팅

- `build.kts`

  ```kotlin
  dependencies {
      //QueryDSL 의존성
      implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
      kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
  }
  ```

  - 최근 스프링부트 버전의 경우 `javax.persistence` 대신 `jakarta`로 변경되었기 때문에 위와 같이 `jakarta` 명시해줘야 한다.