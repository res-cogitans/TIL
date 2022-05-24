# Spring Security

## Spring Security Quick-start

### 웹 어플리케이션의 주요 보안 위협 요소

- 인증(Authentication) 절차 미비
  - 인증(Authentication)은 인가(Authorization)와 함께 보안 관련 핵심 개념 중 하나
  - **사용자의 신원을 확인**하는 과정
    - 아이디 / 패스워드 기반 로그인
    - `OAuth2.0` 프로토콜을 통한 Social 인증
  - 일반적인 애플리케이션은 인증 영역과 인증되지 않은 영역(익명 영역)으로 구별 가능
    - 익명 영역: Unauthenticated ("Anonymous") Realm
      - 사용자의 신원과 무관한 기능들
      - 사용자의 민감 정보를 노출하지 않아야 함
      - 시스템의 상태를 변경하거나 데이터를 관리할 수 있는 기능을 제공하지 않아야 함
    - 인증 영역: Authenticated ("Secure") Realm
      - 사용자의 개인정보를 확인하고 수정할 수 있음
      - 개인화된 기능
      - 관리자 기능
- 인가(Authorization) 처리의 미비
  - **적절한 권한이 부여된 사용자들만** 특정 기능 수행 또는 데이터 접근을 허용
  - 주어진 권한을 넘어서는 기능 수행은 데이터 유출 등 보안사고 발생 가능성 높음
- 크리덴셜(Credential) 보안
  - **민감정보 보호**는 최우선 사항임
  - 민감정보를 암호화하지 않고 일반 텍스트로 저장하는 것은 매우 위험
- 전송 레이어 보안
  - **HTTPS 프로토콜을 적용했는지 여부**
  - SSL 보호를 적용해야 함
- 스프링 시큐리티는 어플리케이션 보안 관련 다양한 기능을 제공
  - 자바 애플리케이션의 인증, 인가
  - 스프링 기반 애플리케이션 보안에서 사실상 표준
  - 필요에 따라 커스터마이징 가능
  - 다양한 확장 기능, 자연스러운 통합
  - Spring Session(세션 클러스터 기능 추상화 제공)
  - Spring Security Oauth(Oauth 1a, Oauth2 인증 프로토콜 제공)



### SpringBoot MVC 프로젝트에 Spring Security 적용해보기

#### 환경 설정

- `maven` vs `gradle`

  - 같은 팀, 같은 회사 내부에서조차 사용이 갈리기도 함
  - **양쪽 다 능숙하게 다루어야 함**

- `WebSecurityConfigure`

  - 코드

    ```java
    @Configuration
    @EnableWebSecurity
    public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {
    
        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/assets/**");
        }
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                        .antMatchers("/me").hasAnyRole("USER", "ADMIN")
                        .anyRequest().permitAll()
                        .and()
                    .formLogin()
                        .defaultSuccessUrl("/")
                        .permitAll()
                        .and()
            ;
        }
    }
    ```

    - `@EnableWebSecurity` + `extends WebSecurityConfigurerAdapter`

      - 기본적인 스프링 시큐리티 설정이 자동으로 추가됨
      - 개별 설정을 오버라이드 할 수 있음

    - `WebSecurity` 클래스

      - 필터 체인 관련, 전역 설정을 처리할 수 있는 API 제공
      - `ignoring()`
        - 스프링 시큐리티 필터 체인을 적용하고 싶지 않은 리소스에 대해 설정
        - 일반적으로 정적 리소스(`*.html`, `*.css`, `*.js` 등)을 예외 대상으로 설정함
        - 불필요한 서버 자원 낭비를 방지
        - `FilterChainProxy`
          - 스프링 시큐리티의 진입부
          - 필터 체인을 구성, 실행
          - 필터의 수가 많기에 모든 요청에 대해 필터 체인을 사용하는 것은 비효율적임
          - 정적 리소스 등에 대해서 필터 체인을 실행하는 것은 성능상 손해가 크다. -> `ignoring`의 필요성
    
    - `HttpSecurity` 클래스
    
      - 세부적인 웹 보안기능을 설정할 수 있는 API 제공
    
      - `HttpSecurity` 주요 메서드
    
        | 메서드명                | 설명                                              |
        | ----------------------- | ------------------------------------------------- |
        | **authorizeRequests()** | 공개 리소스 또는 보호받는 리소스에 대한 세부 설정 |
        | **formLogin()**         | 로그인 폼 기능 세부설정                           |
        | **logout()**            | 로그아웃 기능 세부설정                            |
        | **rememberMe()**        | 자동 로그인 기능 세부설정                         |
    
      - 설명
    
        - `authorizeRequests`: 인증 영역, 익명 영역을 리소스에 따라 설정
          - `antMatchers("/me").hasAnyRole("USER", "ADMIN")`: `/Me`는 인증 영역이며, `USER`나 `ADMIN` 권한을 가지고 있어야 한다.
          - `anyRequest().permitAll()`: 그 외의 모든 영역은 익명 영역임, 모두가 호출 가능
        - `formLogin()`: 스프링 시큐리티가 로그인 페이지를 생성해주도록 설정
          - `defaultSuccessUrl("/")`: 로그인 성공 시에 `/`으로 가라.
          - `permitAll()`: 로그인 페이지는 누구나 접근할 수 있어야 함



### 기본 로그인 계정 설정 추가

- 세팅된 애플리케이션을 실행해보면

  - `/me`에 접근하려고 하면 로그인 페이지로 이동한다.
    - `me.html`이 열리는 것이 아니다.
    - me 페이지에 맞는 권한을 갖고 있지 않기에 스프링 시큐리티에서 자동으로 리다이렉트 시킨 것이다.
  - 콘솔창에 있는 암호를 이용하여 로그인 하면
    - 403 Forbidden: 권한 없음
    - 디버그 로그를 살펴보면 `Granted Authorities`가 주어지지 않았음을 확인할 수 있음

- 비밀번호 생성

  - 기본 설정 상태로 사용 시, 애플리케이션 실행할 때마다 새 비밀번호가 생성됨

  - 내부 동작

    ```java
    public class UserDetailsServiceAutoConfiguration {
        ....
        @Bean
        @Lazy
        public InMemoryUserDetailsManager inMemoryUserDetailsManager(SecurityProperties properties, ObjectProvider<PasswordEncoder> passwordEncoder) {
            User user = properties.getUser();
            List<String> roles = user.getRoles();
            return new InMemoryUserDetailsManager(new UserDetails[]{org.springframework.security.core.userdetails.User.withUsername(user.getName()).password(this.getOrDeducePassword(user, (PasswordEncoder)passwordEncoder.getIfAvailable())).roles(StringUtils.toStringArray(roles)).build()});
        }
    
        private String getOrDeducePassword(User user, PasswordEncoder encoder) {
            String password = user.getPassword();
            if (user.isPasswordGenerated()) {
                logger.warn(String.format("%n%nUsing generated security password: %s%n%nThis generated password is for development use only. Your security configuration must be updated before running your application in production.%n", user.getPassword()));
            }
    
            return encoder == null && !PASSWORD_ALGORITHM_PATTERN.matcher(password).matches() ? "{noop}" + password : password;
        }
    }
    ```

    ```java
        public SecurityProperties() {
    	....
    
        public static class User {
            private String name = "user";
            private String password = UUID.randomUUID().toString();
    ```

  - `application.yml` 설정을 통한 변경

    ```java
    spring:
      security:
        user:
          name: user
          password: user123
          roles: USER
    ```

    - 이제 해당 password로 로그인하면, `me` 페이지가 정상적으로 출력되는 것을 볼 수 있다.



### Thymeleaf 확장

- 설정

  - 의존성 추가(`maven` 기준으로 작성)

    ```xml
            <dependency>
                <groupId>org.thymeleaf.extras</groupId>
                <artifactId>thymeleaf-extras-springsecurity5</artifactId>
            </dependency>
    ```

  - 네임스페이스 추가

    ```html
    <html xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
    ```

  - `me.html`  페이지 편집

    ```html
    <!DOCTYPE html>
    <html lang="ko" xmlns:th="http://www.thymeleaf.org"
          xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
    ...
    <span sec:authentication="name"></span> 님 반갑습니다.
    <div sec:authorize="hasRole('ROLE_USER')">
        권한: USER
    </div>
    <div sec:authorize="hasRole('ROLE_ADMIN')">
        권한: ADMIN
    ...
    ```

  - `index.html` 편집

    ```html
    <!DOCTYPE html>
    <html lang="ko" xmlns:th="http://www.thymeleaf.org"
          xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
    ...
    <span sec:authentication="name"></span> 님 반갑습니다.
    ...
    </html>
    ```

    - 출력 결과
      - `index` 페이지: `anonymousUser 님 반갑습니다.`
        - 스프링 시큐리티에서 기본으로 주어지는 authentication 객체의 이름
      - `me`페이지
        - 권한: USER만 출력됨



## Spring Security Architecture

### 1일차 미션

- **로그인 계정을 추가해보기**

  - `applications` 설정 파일을 수정하는 방식으로는 불가

    - 설정 파일의 정보로 유저 하나를 만드는 식으로 구현이 되어 있기 때문임

  - `WebSecurityConfigure`

    ```java
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("user").password("user123").roles("USER")
                    .and()
                    .withUser("admin").password("admin123").roles("ADMIN");
        }
    ```

    - 위와 같이 `User`를 등록해주고 로그인해보면, 에러페이지가 나온다.

      - 403 에러가 아니라 500 에러임에 유의하자!

      - 로그인을 처리하는 클래스: `DaoAuthenticationProvider`에서 예외가 발생했음

        - `DaoAuthenticationProvider`

          ```java
          public class DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
          ...
          ```

        - `AbstractUserDetailsAuthenticationProvider`

          ```java
          public abstract class AbstractUserDetailsAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {
          ...
                  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                  Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, () -> {
                      return this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports", "Only UsernamePasswordAuthenticationToken is supported");
                  });
                  String username = this.determineUsername(authentication);
                  boolean cacheWasUsed = true;
                  UserDetails user = this.userCache.getUserFromCache(username);
                  if (user == null) {
                      cacheWasUsed = false;
          
                      try {
                          user = this.retrieveUser(username, (UsernamePasswordAuthenticationToken)authentication);
          ...
          ```

          - `user = this.retrieveUser(username, (UsernamePasswordAuthenticationToken)authentication);`

            - `User`를 조회해오는 로직(`DaoAuthenticationProvider`의 `retrieveUser`)

              ```java
                  protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
                      this.prepareTimingAttackProtection();
              
                      try {
                          UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
              ```

              - `UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);`

                - `InMemoryUserDetailManager`에서 처리

                - 이 때 `WebSecurityConfigure`에서 만든 설정에 따라 생성된 `InMemoryUserDetailManger`가 사용되는 것

                  ```java
                      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                          UserDetails user = (UserDetails)this.users.get(username.toLowerCase());
                          if (user == null) {
                              throw new UsernameNotFoundException(username);
                          } else {
                              return new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
                          }
                      }
                  ```

                  - 위에서 `User`를 조회해온다.

          - 다시 `AbstractUserDetailsAuthenticationProvider`에서
