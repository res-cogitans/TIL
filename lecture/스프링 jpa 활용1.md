
## 프로젝트 환경설정
- 스프링부트 thymeleaf viewName 매핑
	- `resources:templates/` + {ViewName} + `.html`
- springboot devtools
	- `build.gradle`에서 `implementation 'org.springframework.boot:spring-boot-devtools'`
	- Ctrl + Shift + F9로 변경된 리소스만 Recompile해서 변동 반영 가능해진다.

### JPA, DB설정
```java
spring:  
  datasource:  
    url: jdbc:h2:tcp://localhost/~/jpashop;MVCC=TRUE  
    username: sa  
    password:  
    driver-class-name: org.h2.Driver  
  
  jpa:  
    hibernate:  
      ddl-auto: create  
    properties:  
      hibernate:  
        show_sql: true  
        format_sql: true  
          
logging:  
  level:  
    org.hibernate.SQL: debug
```
- `application.yml`의 설정 데이터이다. (`application.properties`와 둘 중 하나만 사용하면 된다.)
- 위와 같은 설정에 대해 배우려면 <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/">스프링 부트 매뉴얼을 참조하라</a>: 
- 
