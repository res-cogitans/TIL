# Spring Security

# Spring Security Quick-start

## 웹 어플리케이션의 주요 보안 위협 요소

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



## SpringBoot MVC 프로젝트에 Spring Security 적용해보기

### 환경 설정

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



## 기본 로그인 계정 설정 추가

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



## Thymeleaf 확장

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



# Spring Security Architecture

## 1일차 미션

### 로그인 계정 추가해보기

#### 계정 추가

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

        - 다시 `AbstractUserDetailsAuthenticationProvider`에서 `this.preAuthenticationChecks.check(user);` 수행

          - 내부 클래스인 `DefaultPreAuthenticationChecks`에서 다음 수행

            ```java
                    public void check(UserDetails user) {
                        if (!user.isAccountNonLocked()) {
                            AbstractUserDetailsAuthenticationProvider.this.logger.debug("Failed to authenticate since user account is locked");
                            throw new LockedException(AbstractUserDetailsAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
                        } else if (!user.isEnabled()) {
                            AbstractUserDetailsAuthenticationProvider.this.logger.debug("Failed to authenticate since user account is disabled");
                            throw new DisabledException(AbstractUserDetailsAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
                        } else if (!user.isAccountNonExpired()) {
                            AbstractUserDetailsAuthenticationProvider.this.logger.debug("Failed to authenticate since user account has expired");
                            throw new AccountExpiredException(AbstractUserDetailsAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
                        }
                    }
            ```

            - 계정이 Locked, Disabled, Expired 되어있는지 체크

        - 그 다음으로 `this.additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken)authentication);` 수행

          ```java
              protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
                  if (authentication.getCredentials() == null) {
                      this.logger.debug("Failed to authenticate since no credentials provided");
                      throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
                  } else {
                      String presentedPassword = authentication.getCredentials().toString();
                      if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                          this.logger.debug("Failed to authenticate since password does not match stored value");
                          throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
                      }
                  }
              }
          ```

          - `this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())`: 비밀번호 체크

            - 가져온 회원 정보인 `userdetails`, 입력한 로그인 정보 `authentication`이 일치함에도 이 부분에서 문제 발생함

          - `passwordEncoder`를 찾아보면: `DelegatingPasswordEncoder`

            ```java
                public boolean matches(CharSequence rawPassword, String prefixEncodedPassword) {
                    if (rawPassword == null && prefixEncodedPassword == null) {
                        return true;
                    } else {
                        String id = this.extractId(prefixEncodedPassword);
                        PasswordEncoder delegate = (PasswordEncoder)this.idToPasswordEncoder.get(id);
                        if (delegate == null) {
                            return this.defaultPasswordEncoderForMatches.matches(rawPassword, prefixEncodedPassword);
            ```

            - `null`값 체크에 의해 `return this.defaultPasswordEncoderForMatches.matches(rawPassword, prefixEncodedPassword);`

              - `DelegatePasswordEncoder.UnmappedIdPasswordEncoder`의 `maches` 호출

                ```java
                        public boolean matches(CharSequence rawPassword, String prefixEncodedPassword) {
                            String id = DelegatingPasswordEncoder.this.extractId(prefixEncodedPassword);
                            throw new IllegalArgumentException("There is no PasswordEncoder mapped for the id \"" + id + "\"");
                        }
                ```

                - 여기서 예외 발생 -> 500에러
                - `UnmappedIdPasswordEncoder`: 예외 발생용임, 기능적으로는 무의미
                - **`PasswordEncoder`** 관련 설정이 필요


#### PasswordEncoder

- 주의 사항

  - Spring Security 5에서는 `DelegatingPasswordEncoder`가 기본 `PasswordEncoder`로 사용됨

  - `DelegatingPasswordEncoder`는 패스워드 해시 알고리즘별로 `PasswordEncoder`를 제공

    - **해시 알고리즘에 따른 `PasswordEncoder`를 선택하기 위해 패스워드 앞에 `prefix`를 추가해야 함**

      - 디폴트 알고리즘은 `bcrypt`

      - 예시

        ```
        {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
        {noop}password
        {pbkdf2}5d923b44a6d129f3ddf3e3c8d29412723dcbde72445e8ef6bf3b508fbf17fa4ed4d6b99ca763d8dc
        {sha256}97cde38028ad898ebc02e690819fa220e88c62e0699403e94fff291cfffaf8410849f27605abcbc0
        ```

      - `PasswordEncoderFactories`

        ```java
        public static PasswordEncoder createDelegatingPasswordEncoder() {
        	String encodingId = "bcrypt";
        	Map<String, PasswordEncoder> encoders = new HashMap<>();
        	encoders.put(encodingId, new BCryptPasswordEncoder());
        	encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
        	encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
        	encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
        	encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
        	encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        	encoders.put("scrypt", new SCryptPasswordEncoder());
        	encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
        	encoders.put("SHA-256",
        			new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
        	encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
        	encoders.put("argon2", new Argon2PasswordEncoder());
        	return new DelegatingPasswordEncoder(encodingId, encoders);
        }
        ```

    - `UserDetailsPasswordService`의 구현체(일반적으로는 `InMemoryUserDetailsManager`) 사용시

      - 최초 로그인 1회 성공시 `{noop}` 타입에서 `bcrypt` 타입으로 `PasswordEncoder`가 변경됨

- `password` 값에 `prefix` 추가하면: `.withUser("user").password("{noop}user123").roles("USER")` 정상적으로 로그인 되는 것을 볼 수 있음



### 로그아웃, Cookie 기반 자동 로그인(Remember-Me) 기능 설정하기

#### 로그아웃

- `WebSecurityConfigure`에 설정 추가함

  ```java
  @Configuration
  @EnableWebSecurity
  public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {
  	...
      protected void configure(HttpSecurity http) throws Exception {
          http
              ...
              .logout()
                  .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                  .logoutSuccessUrl("/")
                  .invalidateHttpSession(true)
                  .clearAuthentication(true)
      }
  ```

  - 로그아웃 처리 path `"/logout"`(`LogoutFilter` 생성자에서 설정해주는 기본값임)
  - 로그아웃 성공 후 리다이렉션 path `"/"`
  - `.invalidateHttpSession(true)`, `.clearAuthentication(true)` 설정은 사실 생략해도 무방함
    - `SecurityContextLogoutHandler`에 기본값 `true`로 설정되어 있기 때문

- `LogoutFilter`: 로그아웃 기능을 담당

  ```java
  public class LogoutFilter extends GenericFilterBean {
  ...
      public LogoutFilter(LogoutSuccessHandler logoutSuccessHandler, LogoutHandler... handlers) {
  ...
      public void setLogoutRequestMatcher(RequestMatcher logoutRequestMatcher) {
  ```

  - `logoutSuccessHandler`: 로그아웃 성공 후 리다이렉션 path를 가지고 있음

  - `SecurityContextLogoutHandler`

    ```java
    public class SecurityContextLogoutHandler implements LogoutHandler {
    ...
    	@Override
    	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    		Assert.notNull(request, "HttpServletRequest required");
    		if (this.invalidateHttpSession) {
    			HttpSession session = request.getSession(false);
    			if (session != null) {
    				session.invalidate();
    				if (this.logger.isDebugEnabled()) {
    					this.logger.debug(LogMessage.format("Invalidated session %s", session.getId()));
    				}
    			}
    		}
    		SecurityContext context = SecurityContextHolder.getContext();
    		SecurityContextHolder.clearContext();
    		if (this.clearAuthentication) {
    			context.setAuthentication(null);
    		}
    	}
    ```

    - 설정에 따라 `session.invalidate();`, `context.setAuthentication(null);` 수행

  - `setLogoutRequestMatcher`: 설정의 `.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))`이 반영되는 부분

    - 이것도 기본값임



#### Cookie 기반의 자동 로그인 설정하기: Remember Me 기능

- `WebSecurityConfigure`에 설정 추가함

  ```java
  @Configuration
  @EnableWebSecurity
  public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {
  	...
      protected void configure(HttpSecurity http) throws Exception {
          http
              ...
              .rememberMe()
                  .rememberMeParameter("remember-me")
                  .tokenValiditySeconds(300);
      }
  ```

  - 로그인 화면에 가면 체크박스 추가됨: `<input type="checkbox" name="remember-me">`
    - `name` 값은 `.rememberMeParameter()`의 설정과 일치

- `Cookie` 기반의 `RememberMe` 관련 클래스들

  - `AbstractAuthenticationProcessingFilter`

    ```java
    public abstract class AbstractAuthenticationProcessingFilter extends GenericFilterBean
    		implements ApplicationEventPublisherAware, MessageSourceAware {
    ...
        private RememberMeServices rememberMeServices = new NullRememberMeServices();
    
    ...
    	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
    			Authentication authResult) throws IOException, ServletException {
    		...
    		this.rememberMeServices.loginSuccess(request, response, authResult);
    		...
    	}
    ```

  - `AbstractRememberMeServices`

    ```java
    public abstract class AbstractRememberMeServices
    		implements RememberMeServices, InitializingBean, LogoutHandler, MessageSourceAware {
        ...
    	@Override
    	public final void loginSuccess(HttpServletRequest request, HttpServletResponse response,
    			Authentication successfulAuthentication) {
    		if (!rememberMeRequested(request, this.parameter)) {
    			this.logger.debug("Remember-me login not requested.");
    			return;
    		}
    		onLoginSuccess(request, response, successfulAuthentication);
    	}
    ```

  - `TokenBasedRememberMeServices`: 실제 토큰 생성하는 부분

    ```java
    public class TokenBasedRememberMeServices extends AbstractRememberMeServices {
        ...
    	@Override
    	public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
    			Authentication successfulAuthentication) {
    		...
    		int tokenLifetime = calculateLoginLifetime(request, successfulAuthentication);
    		long expiryTime = System.currentTimeMillis();
    		// SEC-949
    		expiryTime += 1000L * ((tokenLifetime < 0) ? TWO_WEEKS_S : tokenLifetime);
    		String signatureValue = makeTokenSignature(expiryTime, username, password);
    		setCookie(new String[] { username, Long.toString(expiryTime), signatureValue }, tokenLifetime, request,
    				response);
    		...
    	}
    ```

  - `RememberMeAuthenticationFilter`: 자동 로그인 담당

    ```java
    public class RememberMeAuthenticationFilter extends GenericFilterBean implements ApplicationEventPublisherAware {
        ...
    	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    			throws IOException, ServletException {
    		...
    		Authentication rememberMeAuth = this.rememberMeServices.autoLogin(request, response);
            ...
    ```

    - 브라우저가 종료된 이후 다시 접근할 때, `rememberMe` 설정 여부를 확인, 자동 로그인



## Spring Security Architecture

- 스프링 시큐리티의 거시적 구조

  - 웹 요청을 가로챈 후 사용자를 인증하고, 인증된 사용자가 적절한 권한을 가지고 있는지 확인함

  - `Filter`: 웹 요청을 가로챔
  - `AuthenticationManager`: 사용자 인증 관련 처리
  - `AccessDecisionManager`: 사용자가 해당 리소스에 접근할 수 있는 권한 있는지 확인

![img](https://iyboklee.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fec0fd34a-8d5a-49eb-9a8a-4277d83232aa%2F2_1.png?table=block&id=50c470e5-d94d-4207-8e0f-8cf38435e3ed&spaceId=e1875985-78ea-4757-91b0-baa29d833ec6&width=2000&userId=&cache=v2)



### FilterChainProxy (Spring Security 필터 체인)

- 스프링 시큐리티의 실제적인 구현은 서블릿 필터를 통해 이뤄짐

  - 서블릿 필터는 요청의 전처리, 후처리, 요청 자체를 리다이렉트하기도 함

- `FilterChainProxy` 세부 내용의 설정

  - `WebSecurityConfigurAdapter`의 구현체에서 설정

  - 예제에서는 `WebSecurityConfigure` 클래스

    ```java
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            ...
    ```

  - 일반적으로 `@EnableWebSecurity` 함께 사용함

- 웹 요청은 필터 체인을 차례로 통과

  - 요청은 모든 필터를 통과하지만, 모든 필터가 동작하지는 않음
  - 동작 필요가 없을 경우 다음 필터로 요청을 바로 넘기기도 함
  - 일반적으로 `springSecurityFilterChain` 이름으로 빈 등록됨

- 웹 요청이 어떻게 `FilterChainProxy`로 전달되는가?

  - `DelegatingFilterProxy`
    - `SecurityFilterAutoConfiguration`에서 자동으로 등록됨
    - 요청을 Target Filter Bean에 위임하는 역할
    - 기본적으로 `SpringSecurityFilterChain`에게 전달

- 구조

  ```mermaid
  flowchart TD
  A[Client] <--> B
  	subgraph FilterChain
  	direction TB
  	B[Filter] <--> C[DelegatingFilterProxy]
  	C <--> D[Filter2]
  	D <--> E[Servlet]
  	end
  	C --> F
  	
  	subgraph SecurityFilterChain
  	direction TB
  	F[Security Filter0] <--> G[Security Filter1] <--> H[Security FilterN]
  	end
  ```


- 주요 Security Filter: **필터 체인 내에서의 상대적인 순서에 따라 정렬**

  | 이름                                    | 설명                                                         |
  | --------------------------------------- | ------------------------------------------------------------ |
  | ChannelProcessingFilter                 | 웹 요청이 어떤 프로토콜로 (http 또는 https) 전달되어야 하는지 처리 |
  | SecurityContextPersistenceFilter        | SecurityContextRepository를 통해 SecurityContext를 Load/Save 처리 |
  | LogoutFilter                            | 로그아웃 URL로 요청을 감시하여 매칭되는 요청이 있으면 해당 사용자를 로그아웃 시킴 |
  | UsernamePasswordAuthenticationFilter    | ID/비밀번호 기반 Form 인증 요청 URL(기본값: /login) 을 감시하여 사용자를 인증함 |
  | DefaultLoginPageGeneratingFilter        | 로그인을 수행하는데 필요한 HTML을 생성함                     |
  | RequestCacheAwareFilter                 | 로그인 성공 이후 인증 요청에 의해 가로채어진 사용자의 원래 요청으로 이동하기 위해 사용됨 |
  | SecurityContextHolderAwareRequestFilter | 서블릿 3 API 지원을 위해 HttpServletRequest를 HttpServletRequestWrapper 하위 클래스로 감쌈 |
  | RememberMeAuthenticationFilter          | 요청의 일부로 remeber-me 쿠키 제공 여부를 확인하고, 쿠키가 있으면 사용자 인증을 시도함 |
  | AnonymousAuthenticationFilter           | 해당  인증 필터에 도달할때까지 사용자가 아직 인증되지 않았다면, 익명 사용자로 처리하도록 함 |
  | ExceptionTranslationFilter              | 요청을 처리하는 도중 발생할 수 있는 예외에 대한 라우팅과 위임을 처리함 |
  | FilterSecurityInterceptor               | 접근 권한 확인을 위해 요청을 AccessDecisionManager로 위임    |

  - [공식 문서](https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-security-filters)



## RequestCacheAwareFilter

- 인증 요청에 의해 가로채어진 원래 요청으로 이동하는 기능
  - 캐시된 요청이 있다면 캐시된 요청을 처리, 없다면 현재 요청을 처리
  - 예시: 익명 사용자가 권한을 필요로 하는 리소스에 접근 시도할 경우
    - `AccessDecisionManager`에서 예외 발생
    - `ExceptionTranslationFilter`가 접근 거부 예외를 처리
    - 익명 사용자일 경우 해당 리소스로의 접근을 캐시 처리하고, 로그인 페이지로 이동
    - 로그인이 성공하면 `RequestCacheAwareFilter`가 원래 요청 리소스로 리다이렉트
- `FilterSecurityInterceptor`
  - 인가 처리를 하는 `AccessDecisionManager`와 유관한 필터
    - 접근 거부 예외가 발생하는 지점: `AccessDeniedException`
    - `ExceptionTranslationFilter`가 이 예외를 catch (`handleSecurityException()`)
      - 여기서 catch 하는 `SecurityException`은 구체적으로 `AuthenticationException`과 `AccessDeniedException`으로 나뉨



## ChannelProcessingFilter

- 요청이 어떤 프로토콜로 전달되어야 하는지 처리
- 전송 레이어 보안을 위해 SSL 인증서 생성, 애플리케이션에 적용(HTTPS)
- HTTPS
  - HTTP의 암호화 버전
  - 데이터 암호화를 위해 SSL(Secure Sockets Layer) 사용
  - SSL
    - Netscape가 개발
    - SSL 3.0부터 명칭이 TLS로 변경됨(다만 SSL이라 많이 부름)
  - SSL 암호화를 위해 SSL 인증서 필요
    - 서버는 SSL 인증서를 클라이언트에 전달
    - 클라이언트는 전달받은 SSL 인증서를 검증, 신뢰할 수 있는 서버인지 확인
    - 신뢰할 수 있는 경우 SSL 인증서 공개키를 이용해 실제 암호화에 사용될 암호화키를 암호화하여 서버에 전달
      - 실제 암복호화는 대칭키 방식 사용
      - 대칭키 공유를 위해 RSA 암호화 사용
- SSL 인증서 생성
  - 본래는 인증 기관에서 발급
  - keytool 이용하여 임의로 SSL 인증서 생성 가능
    - 로컬 테스트 용도로 사용 가능
    - Java 설치 경로 bin 디렉토리 내에 있음



### Keytool 이용한 SSL 인증서 생성

- keystore 만들기
  - `keytool -genkey -alias [keystore 별칭] -keyalg RSA -storetype PKCS12 -keystore [keystore 파일]`
- keystore에서 인증서 추출하기
  - ``keytool -export -alias [keystore 별칭] -keystore [keystore 파일] -rfc -file [인증서 파일]`
- trust-store 만들기
  - `keytool -import -alias [trust keystore 별칭] -file [인증서 파일] -keystore [trust keystore 파일]`



### SSL 인증서 적용

- keytool 이용해서 생성한 `keystore` 파일과 `truststore` 파일을 프로젝트 resource 디렉토리로 복사한다.

- `application.yml` 설정 변경

  ```yaml
  server:
    port: 443
    ssl:
      enabled: true
      key-alias: prgrms_keystore
      key-store: classpath:prgrms_keystore.p12
      key-store-password: prgrms123
      key-password: prgrms123
      trust-store: classpath:prgrms_truststore.p12
      trust-store-password: prgrms123
  ```

  - HTTPS니까 포트번호를 443으로 변경

- `WebSecurityConfigure`

  ```java
      @Override
      protected void configure(HttpSecurity http) throws Exception {
          http
              ...
                  .and()
              .requiresChannel()
                  .anyRequest().requiresSecure()
          ;
      }
  ```

  - HTTPS 채널을 통해 처리해야 하는 요청을 정의 가능
  - `FilterInvocationSecurityMetadataSource` 클래스에 HTTPS 프로토콜로 처리해야 URL 정보가 담김
  - 실제적인 처리를 `ChannelDecisionManager` 클래스로 위임함

- 실제 서비스에서는 WAS에 직접 인증서를 넣지 않음

  - 일반적으로 앞단에 L4 / L7 로드밸런서를 두고, 로드밸런서에서 SSL 인증서를 처리함
    = SSL 오버로딩
  - WAS 서버가 비즈니스 로직 처리에만 리소스를 집중할 수 있게 됨

