# enum(Codelatte정리)

### 기존의 방식

```java
public void eat(int foodCategory) {
	if (BREAD == foodCategory) {
		System.out.println("빵")
	}
	. . .
}

//혹은,----------------------------------

public void eat(int foodCategory) {
	if (0 ==foodCategory) {
		System.out.println("빵")
	}
	. . . 
}
```

### 정적상수변수 클래스

- 정적상수변수를 사용할 경우: 심볼릭 상수를 정의하는 클래스

```java
final class FoodCategory {
	public static final int BREAD = 0;
	public static final int CAKE = 1;
	public static final int COFFEE = 2;
	public static final int BEVERAGE = 3;
}

eat(FoodCategory.BREAD);
eat(FoodCategory.CAKE);
```

>> CafeFoodCategory??

- 위와 같은 경우 정수 상수값을 변경해도 영향이 적으며, 유지변수에 용이)
- 그러나 eat(-1) eat(5)와 같이 직접 범위를 벗어난 상수전달가능. (매개변수규격은 정수라서)

### enum 상수 사용

```java
enum FoodCategory {
	BREAD,
	CAKE,
	COFFEE,
	BEVERAGE;
}
```

- 이 경우 선언, 정의가 다름
- eat(enum만 전달가능), enum 안의 상수만 전달가능
- switch문 사용 가능, 코드 가독성 향상.
- IDE의 도움이 용이함



## enum의 특성

### Ordinal()

- ordinal((): 순서에 따라 배열처럼 번호가 부여됨.
맨 앞: BREAD ordinal 0 . . .(순서변경시 ordinal도 변경)

```java
system.out.println(
	FoodCategory.CAKE.ordinal()
);
```

- ordinal값을 기준으로 비교 등 코드 사용시 enum의 장점 퇴색(이것을 사용할 경우 설계 문제를 의심)

### 클래스 정의

```java
enum Gender {
	MALE(10), // MALE("male")  -> new Gender.MALE(10);
	FEMALE(20); //FEMALE("female")

	int value;

private Gender(int value) {
	this.value = sex;

```

- 모든 생성자는 private. 따라서 private 생략 가능.
- 생성자 명시적 선언으로 enum상수를 정할 수 있음.
- 싱글톤 패턴