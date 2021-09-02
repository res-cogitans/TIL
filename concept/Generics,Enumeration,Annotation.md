# Generics, Enumeration, Annotation
- 남궁성, <Java의 정석>, chapter 14

## Generics
- compile-time- type check
- **용어**: `class Box<T> {}`에서
	- Box는 원시 타입
	- T는 타입 변수, 타입 매개변수
	- Generics 타입 호출: T에 타입 지정 
	- 매개변수화된 타입(parameterized type): T에 지정된 타입
- **제한**
	- static 멤버에 타입 변수 T를 사용할 수 없음
	- Generics 배열은 생성(new) 불가
	- 제한 설정
		- `class FruitBox<T extends Fruit & Eatable>` 식으로, T의 타입 제한이 가능하다.
		- &로 조건들 연결 가능하며, 인터페이스의 경우에도 `extends`임에 유의해야 한다.
- 매개변수의 T값만 다른 형태로 오버로딩할 수는 없다. 즉 `method(GenClass<Integer> ins)`와 `method(GenClass<Long> ins)`는 동등하게 취급된다.
	- **와일드카드 ?**를 이용하여 계층관계 클래스들을 포함시킬 수 있다.
		- <? extends T>: T와 자손
		- <? super T>: T와 조상
		- <?>: <? extends Object>와 동일
			- 형변환 가능하게 만드려면 <?>을 사용 <Object>는 X
		- 와일드 카드를 사용할 시에 &는 사용불가
- Generics 메서드
	- `static <T> void sort(List<T> list, Comparator<? super T>)`
	- static 메서드라도 사용가능(static 멤버에 T 사용불가)
	- 호출 시 `<Member>sort(list)`식으로 타입 매개변수를 앞에 붙여줘야함
대입된 타입이 존재할 경우 클래스명.이나 this.를 생략해선 안 된다.
- 컴파일된 클래스 파일에는 Generics 정보가 없다.

## 열거형(enums)
- **타입에 안전한 열거형(typesafe enum)**: C 등의 언어와 차이, 값이 같더라도 타입이 다르면 컴파일 에러 발생
- 상수 값이 변경되어도 관련 소스 컴파일 필요 없음
- 정의: `enum 열거형이름 {상수명1, 상수명2, ...}`
- '=='로 비교 가능, 비교 연산자('<', '>' 등) 사용 불가, 이를 위해선 `compareTo()`사용
- 열겨형의 추상 메서드
	- 열거형에 추상 메서드를 선언 시,  각 열거형 상수가 이를 구현해야 함.
- 열거형 상수는 열거형 클래스의 객체이며, 각각이 static 상수이기에 '=='로 비교 가능하다.

## 어노테이션
- 주석처럼, 타 프로그램에 정보를 제공하면서도 프로그램 자체에는 영향을 미치지 않음
-  **메타 어노테이션**
	- 새로운 어노테이션 정의에 사용
	- 표준 메타 어노테이션
		- `@Target`: 어노테이션이 적용가능한 대상
		- `@Documented`: 어노테이션 정보가 javadoc으로 작성된 문서에 포함됨
		- `@Inherited`: 자손클래스에 상속되는 어노테이션
		- `@Retension`: 어노테이션 유지 범위 지정
		- `@Repeatalbe`: 반복 적용 가능
