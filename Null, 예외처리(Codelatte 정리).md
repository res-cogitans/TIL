# Null, 예외처리(Codelatte 정리)

# Null

- 참조자료형 변수 String

```java
String[] strArray = new String[3];
String text = null;
```

- strArray 배열의 원소 strArray[0], . . . 은 null 값.
null로 선언된 text도 null값.

```java
class Fruit {
	String name = "사과";
}

Public class Main {

	public void main(String[] args) {
		Fruit fruit = null;
		System.out.println(fruit);
	}
}
```

- Null Pointer Exception
참조형 병수 fruit는

### 참조(reference)

- 복제는 파일을 복사, 참조는 심볼릭 링크/바로가기를 만드는 것
심볼릭 링크를 통해 만든 파일에는 원본의 주소값이 담김
원본의 변경 → 심볼릭 링크에도 반영

    매개변수를 다른 객체로 변경하는 것과 매개변수에 담겨 있는 객체에 접근하는 것은 다름.

    ```java
    public class Reference {

        public void ref() {
            RefTestClass refIns1 = new RefTestClass();
            RefTestClass refIns2 = null;
            RefTestClass refIns3 = new RefTestClass();
            refIns3 = refIns2;
            System.out.println("refIns1= " + refIns1);
            System.out.println("refIns2= " + refIns2);
            System.out.println("refIns3= " + refIns3);

            try {
                System.out.println("refIns1.name= " + refIns1.name);
            } catch(NullPointerException nullPointerException) {
                System.out.println("ins1NPE 발생");
            }
            try {
                System.out.println("refIns2.name= " + refIns2.name);
            } catch(NullPointerException nullPointerException) {
                System.out.println("ins2NPE 발생");
            }
            try {
                System.out.println("refIns3.name= " + refIns3.name);
            } catch(NullPointerException nullPointerException) {
                System.out.println("ins3NPE 발생");
            }
        }

    }
    /*refIns1= Reference.RefTestClass@6d311334
    refIns2= null
    refIns3= null
    refIns1.name= Socrates
    ins2NPE 발생
    ins3NPE 발생*/
    ```

    - 
    - 객체배열의 경우,

    ```java
    Class[] class = new Class[10];
    ```

    - 선언 시에 class[0] class[1]등 접근하려고 하면 NPE 발생. 별개로 class[0] = new Class해줘야 각 원소에 할당됨.

    - 메소드/생성자에서 참조 자료형 Type의 인스턴스 type 매개변수로 한다면 null 전달가능
    - null 여부는 ==과 ≠로 알아볼 수 있음.

    ## 예외처리

    Object

    —————————————

    Error                               Exception

    (예외처리불가)                   —————————————

                                    RuntimeException                   OtherException

                                    UncheckedException               CheckedException

    - try {. . . } catch {. . . } finally {. . .} 예외상황 발생시 catch로 점프, finally는 항상 호출함
    - CheckedException: 컴파일을 위해 필수적 Ex) System.out.write  (IOException)
    UncheckedException
    - Exception(특히 Checked 검사용으로)/Runtime~사용시 포괄적 예외처리가 가능함. 다만 세부적 정의하면 좋다.
    - CustomException

    ```java
    public class CustomException extends RuntimeException {
    	public CustomException() {
    		super("커스텀 예외)");
    	}
    }

    . . . 

    if (null==name) {
    	throws new CustomException();
    }
    ```

    - 메소드에 throws CustomException 하는 식으로도 사용 가능.