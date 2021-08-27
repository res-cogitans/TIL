# 제네릭Generic(Codelatte 정리)

- 컴파일 단계에서 자료형을 체크해줌.

```java
class Chicken {

}

class ChickenBox {
	private Chicken[] box = new Chicken[10];
	int cursor = 0;
	
	void putData(Chicken chicken) {
		box[cursor++] = chicken;
	}

	Chicken getChicken() {
		Chicken chicken = box[cursor -1];
		box[cursor- -1] = null;
		cursor--;
		return chicken;
	}
}
```

- 동일한 Box류를 계속 만들어야 한다면 비효율적 코드

```java
class Box {
	private Object[] box = new Object[10];
	int cursor = 0;

	void putData(Object data) {
		box[cursor++] = data;
	}

	Object getData() {
		Object data = box[cursor -1];
		box[cursor -1] = null;
		cursor--;
		return data;
	}
}

// 이후 꺼낼 때는 Chicken chicken1 = chickenBox.getdata();
```

- putData의 매개변수 자료형이 Object이기에 다른 Object가 들어가고, 이후 getData 사용시 형변환 Exception 발생가능.

```java
class Box<T> {
	private Object[] box = new Object[10];
	int cursor = 0;

	void putData(T data) {
		box[cursor++] = data;
	}

	T getData() {
		T data = (T)box[cursor -1];
		box[cursor -1] = null;
		cursor--;
		return data;
	}
}
```

- Formal type parameter T 선언, 사용으로 제네릭 클래스 만듦. 제약
cf) Actual type parameter <Chicken>
- Box<Chicken> chickenBox = new Box<>(); → T를 Chicken클래스로 강제.
- 컴파일 시에 에러를 잡아낼 수 있음.
- <T, E> 형태로 사용도 가능
- T/E 모두→java.lang.Object 이기에 T getData()와 E getData() 동시 사용불가.

### 제네릭 정적 메소드

```java
class Box <T, E> {

    public void <A> static calculate(A data) {
        …
    }

}
----------

class Box <T, E> {

    public void <A extends Chicken> static calculate(A data) {
        …
    }

}
```

### 제네릭 제한자

- extends, super, ?(와일드카드)
- extends/super → 자기 자신도 포함
- ?: Actual type Parameter에서 T처럼 사용가능. <? extends Parent>

———————-

- class 파일에는 Generic 정보가 존재한다
- 컴파일 후에는 Formal Type Parameter를 Object로 서술한다
- Generic 클래스는 컴파일 시점에만 타입 체크를 해주는 클래스로 확인할 수 있다
- Object로 일반화된 클래스나 Generic으로 만든 일반화된 클래스나 작동방식은 다르지 않다

---