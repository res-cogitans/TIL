# 객체지향(Codelatte 정리)

- [codelatte.io](http://codelatte.io) 참조

## 생성자

- 디폴트 생성자와 달리 매개변수 등 필요하지만, 그 객체가 무엇인지를 명시하는 효과.
- 생성자 오버로딩이 가능

- 인스턴스 변수와 정적 메소드는 수명주기가 다르다

- Unnamed 패키지 논리적 경로, 다른 패키지는 모두 import통해야

- 객체 배열의 경우 초기화시에 각 객체는 null로 초기화됨. 따라서 추가적으로 각각 객체생성해줘야함.

## 상속

- 접근제어지시자를 이용하여 상속을 제한할 수 있다.
- 생석자는 상속 불가능하다. 명시적으로 호출해야
- super()부모클래스 생성자.
- 부모클래스에 생성자가 명시되지 않았거나, 기본생성자만 명시되었을 경우는 super()생략가능

## 다형성

- 한 인스턴스가 여러 자료형을 가질 수 있음
- 객체의 형변환은 형변환된 자료형으로서 사용하겠다는 의미로만 존재, 진짜 변환은 아님.

## CRUD 기능

- 대부분이 프로그램이 갖는 Create, Read, Update, Delete의 4기능

## 추상클래스(abstract class)

- 추상메서드: abstract. 규격만 정의함, 내용작성은 상속 후 오버라이딩 해서만 가능(private 불가)
- 생성자 정의는 가능
- 인스턴스 단독 생성은 불가(상속받아야만 사용가능)
- 상속받는 클래스는 추상 메소드 오버라이딩 해야하며 구현체가 존재해야.

## 인터페이스(implements)

- 생성자 선언, 정의불가
- 모든 멤버변수는 public static final
- 메서드의 경우 default/static의 경우 public, else) public abstract (Java8)
(즉 모두 public)
- 상속받을시 반드시 메소드를 오버라이드, 구현체 필수.
- 메소드의 구현을 위한 규약(protocol) - (생성자 선언불가의 이유)
- 상속보다는 class에서 구현

### 추상클래스                             인터페이스

내용 있는 메서드 가능                       public default / public static

생성자 정의가능                                불가능

다양한 접근제어자                            무조건 public

(단 !private)                                   

인스턴스/static변수 선언가능           무조건 public static final

- 인터페이스가 구현을 위한 규약이라면,
- 추상클래스는 확장, 상속의 역할이다.
- ClassB extends ClassA implements InterfaceA, InterfaceB 가능.

## 익명 클래스

- 간단한 추상클래스/인터페이스는 익명클래스로 구현하기도.

```java
public class Main {

    public static void main(String[] args) {
 
        Coffee coffee = new Coffee() {  // Override
            public void make() {
                System.out.println(“Override Make!!”);
            }
            public void serve() {  //New Method
                System.out.println(“Serve”);
            }
        }

public class Coffee {
    public void make() {
	      System.out.println("Make!!");
    }
}
```

### 추상클래스의 구현

- class.method 형태의 호출은 불가함
- 상속받은 abstract 메소드는 모두 오버라이딩 해야함
- Class class = new Class() { ((오버라이드) } 는 Class 객체 생성이 아니라, Class를 상속받는 익명클래스의 객체를 생성함.

### 인터페이스의 구현

-