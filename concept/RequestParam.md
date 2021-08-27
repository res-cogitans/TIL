org.springframework.web.bind.annotation

## Annotation Type RequestParam

@Target(value=PARAMETER)
 @Retention(value=RUNTIME)
 @Documented
public @interface RequestParam

Annotation which indicates that a method parameter should be bound to a web request parameter.
웹 요청 매개변수여야 하는 메소드 매개변수를 가리키는 어노테이션이다.

Supported for annotated handler methods in Spring MVC and Spring WebFlux as follows:
스프링 MVC와 스프링 웹플럭스에서 어노테이션이 붙은 핸들러 메소드에 지원되며, 다음과 같다:

-   In Spring MVC, "request parameters" map to query parameters, form data, and parts in multipart requests. This is because the Servlet API combines query parameters and form data into a single map called "parameters", and that includes automatic parsing of the request body.
-   In Spring WebFlux, "request parameters" map to query parameters only. To work with all 3, query, form data, and multipart data, you can use data binding to a command object annotated with  [`ModelAttribute`]
- 스프링 MVC에서 "request parameters"은 쿼리 매개변수, 폼 데이터, multipart requests의 부분들로 맵 한다. 때문에  서블릿 API는 쿼리 매개변수와 폼 데이터를 "parameters"라는 단일 맵으로 결합시키며, request body의 자동적인 구문분석(parsing)을 포함한다.
- 스프링 웹플럭스에서, "request parameters"는 쿼리 매개변수에만 맵 한다. 쿼리, 폼 데이터, multipart data 모두에 적용하기 위하여  [ModelAttribute] 어노테이션 붙은 커맨드 객체에 데이터 바인딩을 사용할 수 있다.
If the method parameter type is  [`Map`]  and a request parameter name is specified, then the request parameter value is converted to a  [`Map`]  assuming an appropriate conversion strategy is available.
만일 메소드 매개변수의 자료형이 Map이며 요청 매개변수명이 명시되어 있다면, 적절한 변환 방식(strategy)가 가능하다고 보고(assuming) 요청 매개변수 값은 Map으로 변환된다.
If the method parameter is  [`Map<String, String>`] or  [`MultiValueMap<String, String>`]  and a parameter name is not specified, then the map parameter is populated with all request parameter names and values.
만일 메소드 매개변수가 Map<String, String> 혹은 MultiValueMap<String, String>이며, 매개변수명이 명시되어있지 않다면, 맵 매개변수는 모든 요청 매개변수 명칭들과 값들로 채워진다.

Since:

2.5
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTQxMDE0OTYyOF19
-->