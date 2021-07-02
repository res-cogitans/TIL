# Collection(Codelatte)

- java.util.pakage
- 유동적 크기의 배열 등을 자료구조 라이브러리

### List<E>

- 순서가 있는 자료구조
- List Interface 구현례: ArrayList, LinkedList, Verctor, Stack

### ArrayList<E>

- 가변배열
- 배열(기본10)이 꽉 차면 새로운 배열을 만들고 기존값을 복사.
- 데이터 삭제시에는 빈칸 땡김(삭제가 빈번한 경우 부적합한 자료구조)
- 빠른 접근 가능하나, 큰 데이터일수록 지연큼

```java
import java.util.ArrayList;

ArrayList<String> texts = new ArrayList<>(30);
//30->최초공간크기

//데이터 삽입
texts.add("딸기");
texts.add("포도");

//논리적 공간 size
int size = texts.size();

//인덱스에 삽입
//set.(index, 값);
texts.set(1,"바나나)

//특정값에 접근
//get(index);
String text = texts.get(1);

//삽입, 접근 모두 논리적인 공간(texts.size())를 넘어설 수는 없음. -> IndexOutOfBoundsException

//삭제
texts.remove(1);
texts.remove("바나나");
//전자가 훨씬 빠름

//논리공간확장
texts.add(null);
```

- ArrayList는 List의 구현이다.

### Linked List<E>

- Node로 연결된 리스트
- 삭제시에는 참조값 변경 삭제.

```java
import java.uitl.LinkedList;

LinkedList<String> texts = new LinkedList<>();

texts.add("딸기");
texts.add("포도");
texts.add("사과");

// 딸기 -> 포도 -> 사과

// 첫 번째 Node의 앞에 삽입
texts.addFirst("시작");

// 시작 -> 딸기 -> 포도 -> 사과

// 마지막 Node의 뒤에 삽입
texts.addLast("마지막");

// 시작 -> 딸기 -> 포도 -> 사과 -> 마지막

// 맨 앞의 값을 삭제
texts.removeFirst();

// 맨 뒤의 값을 삭제
texts.removeLast();
```