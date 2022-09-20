# Collections Framework
- 남궁성, <Java의 정석>, chapter 14
- 구종만, <프로그래밍 대회에서 배우는 알고리즘 문제 풀이 전략>

- Collection 인터페이스는 List, Set와 상속관계지만, Map과는 상속관계가 아니다.

### Map.Entry 인터페이스
- Map 인터페이스의 내부 인터페이스
- key-value를 다루기 위함

## List
- 순서가 유지
- 중복을 허용함

### ArrayList
- **가변 배열의 용량(Capacity)**
	- 가변 배열의 크기 확장을 할 때마다 새로 모든 배열의 내용을 다시 복사한다면, 비효율적이기에 보통 일정 용량을 미리 확보해 둔다.
	- 만일 가변 배열의 최대 크기를 알 수 있다면 용량을 설정하여 메모리 낭비를 막을 수 있다: `ensureCapacity(int minCapacity)` 메서드를 사용하라.
- **요소 삭제 시 주의사항**
	- **높은 인덱스**부터 삭제하라:
		- 앞 요소를 삭제할 경우 자리이동이 발생하기에 연산이 증가한다. 
		- 자리이동의 발생으로 인해 결과 자체가 바뀐다.

### LinkedList
- **배열과 연결 리스트**
	- 배열은 연속적, 빠른 접근, (중간) 삽입 삭제 힘듦
	- 연결 리스트는 불연속적, 느린 접근, (중간) 삽입 삭제 용이
- 데이터와 다음 Node의 주소를 담고 있는 Node 단위
- 단방향 리스트는 이전 노드를 접근하기 힘듦 -> **이중 연결 리스트(doubly linked list)**로 보완: 이전 노드에 대한 주소도 저장
- LinkedList 클래스는 이중 연결 리스트로 구현되어 있다.

### Stack
- LIFO(Last In First Out): 나중에 저장한(push) 것이 먼저 나가는(pop) 구조
- ArrayList로 구현
- 용례) Undo/Redo, 뒤로 가기 / 앞으로 가기, 수식 괄호 검사

### Queue
- FIFO(First In First Out): 선입선출, 먼저 저장(offer)한 것이 먼저 나가는(poll) 구조
- LinkedList로 구현
- 인터페이스로 규정만 해둠: 구현체를 사용할 것
- 용례) 최근 사용 파일 목록, 인쇄 대기 목록, 버퍼
- **PriotyQueue**
	- 우선순위 순서대로 꺼냄(poll)
	- heap자료구조로 배열에 저장(offer)
- **Deque**
	- Double-Ended-Queue
	- 양방향 추가/삭제가 가능하다
	- 구현체로 ArrayDeque, LinkedList 등이 있다.

## Iterator, ListIterator, Enumeration
### Iterator
- **`iterator()`**: Collection에 정의됨, Iterator를 반환. Collection의 각 구현체들에 맞게 Iterator의 구현체를 반환한다.
- **Iteraror의 메서드**: `hasNext()`로 다음 요소 존재를 체크, `next()`로 다음 요소를 꺼냄, `remove()`로 삭제
- **Map의 경우**: `map.keySet()`이나 `map.entrySet()`으로 key나 value를 따로 꺼낸 뒤에 iterator 사용해야 한다.

### Enumeration
- 컬렉션 이전 구버전, 사용 부적합

### ListIterator
- **양방향 Iterator**
- **List의 구현체에만 사용 가능**
- `hasPrevious()`, `previous()`: 이전 조회에 필요
- `nextIndex()` `previousIndex()` index 반환하는 기능
- `add(Object o)`: 새로운 객체 추가 기능
-`set(Object o)`: 읽어 온 요소를 지정된 객체로 변경함
- add, remove, set은 선택적 구현, 구현하지 않을 것이라면 `UnsupportedOpertationException`을 던져 명시해주는 것이 좋다.

### Arrays
- 복사: `copyOf()`, `copyOfRange(arr, n, m)` -> m은 포함 안 함
- 채우기: `fill(arr, value)`
	- `setAll(arr, expression)`  람다식을 이용할 것
- 정렬: `sort()`
- 이진탐색: `binarySearch()` - 정렬된 배열에만 사용
- 비교: `equals()`: 2차원 배열의 경우 주소값을 비교하게 되기에 값이 같아도 false반환함.
- `toString()`: 1차원 배열용, 다차원 배열은 ˋdeepToString()`
- List로 반환: `asList(Object...a)` Arrays.asList로 생성된 List는 크기 변경 불가
	- 변경을 원한다면 ArrayList 등으로 선언하고 new ArrayList(Arrays.asList())식으로 사용
- `parallel~()`: 쓰레드로 처리
- `spliterator()`: 작업을 분할하는 Spliterator반환
- `stream()`: 컬렉션을 스트림으로 변환

## Comparator, Comparable
- 컬렉션 정리에 필요한 메서드를 정의하는 인터페이스

### Comparable
- Integer, String, Date, File등을 오름차순 정렬
- `int compareTo(Object o)`: 같으면 0, 비교하는 값보다 작으면 음수, 크면 양수 리턴
- 기본적인 오름차순 정렬 기준
- String의 경우 유니코든 순 정렬(공백, 숫자, 대, 소문자)

### Comparator
- `int compare(Object o1, Object o2)`, `boolean equals(Object obj)` -> compare만 구현해도 됨. (오버라이딩이 필요할 수도 있기 떄문에 정의한 것)
- 기본 정렬 기준 외의 기준 정의
- `sort(Object[] a, Comparator c)` 식으로 객체가 구현한 `Comparable` 외의 조건 정렬 가능
- String의 경우
	- CASE_INSENSITIVE_ORDER 구현(String의 대소문자 구별 없는 비교) 등
	- * -1을 이용한 역순 정렬 참고

## Set
- 중복 요소를 저장하지 않는 자료구조
- `add(Object o)`나 `addAll(Collection c)` 저장 메서드는 중복 요소가 있을 경우 false 반환

###HashSet
- HashMap 이용하여 만듦
- `retainAll(Collection c)`교집합만 남김
- HashSet은 중복 여부 검사를 대상 요소의 `equals()`와 `hashCode()`로 검사하기에, 클래스의 인스턴스를 비교할 경우에는 참조값을 비교하게 된다.
	- 따라서 해당 인스턴스의 value를 비교하기 위해서는 해당 클래스에서 `equals()`, `hashCode()`메서드를 오버라이딩 해야한다.
	- **`hashCode()` 오버라이딩 조건**
		1. 멱등: 반복 호출이 리턴값을 변하게 하지 않음
		2. `equals`가 true인 두 객체는 `hashCode()` 리턴값도 동일
		3. `equals`가 false인 두 객체의 `hashCode()` 리턴값이 다른 것이 HashTable, HashMap 등의 검색속도 향상에 좋음

### TreeSet
- 이진 검색 트리(binary search tree) 자료 구조 - Red-Black tree
- 정렬, 검색, 범위 검색에 유리
	- 단, 트리 구성으로 인해 LinkedList보다 데이터 삽입이 더 느림
- 부모 노드의 좌측에는 더 작은 값, 우측에는 더 큰 값을 저장
- 객체의 경우 값 비교를 위하여
	1. 객체 자체에 Comparable을 구현하거나
	2. TreeSet에 Comparator를 제공하라

## HashMap, HashTable
- HashTable은 HashMap의 구버전

### HashMap
- key, value -> 둘 다 Object타입
- `getOrDefault(Object key, Object defaultValue)`: 키를 못 찾을 경우 기본값 반환
- 중복 키에 value를 여러 번 대입할 경우 나중 것이 덮어쓴다.
- HashMap의 key나 value값은 null 가능하다, HashTable은 불가능하다.
- HashMap의 value로 HashMap을 저장하여, 하나의 key에 복수의 value 저장하는 것도 가능하다.
- **해싱(hashing)**
	- hash function을 이용, 데이터를 hash table에 저장, 탐색
	- hashCode를 저장하는 배열, 해시코드에 연결된 LinkedList로 구성
		- hashFunction(key)로 hashCode값을 받아서 그것과 연결된 LinkedList 찾고, 거기서 key와 일치하는 value 찾음
	- HashMap의 검색속도는 해시함수가 동일 key에 대해 다른 hashCode를 반환할수록 좋다. LinkedList는 배열에 비해 검색이 느리기 때문이다.
	- **`hashCode()`**
		- Object의 경우 객체의 주소를 이용한다. (객체의 경우 value가 같아도 `equals()`가 false를 반환하는 이유)
		- String의 경우 문자열의 내용으로 해시코드를 만들어낸다.
		- 해시코드가 동일할 경우 HashMap에서 동일 LinkedList에 저장된다. -> `equals()`만 재정의하고, `hashCode()` 재정의하지 않을 경우 `equals()`가 true임에도 서로 다른 해시코드를 반환하고, 따로 저장하게 된다.

### TreeMap
- 이진검색트리 형태로 (key, value) 데이터 저장
- 일반적인 검색에서는 HashMap이 더 나음, 범위검색이나 정렬 기능은 TreeMap

### Properties
- HashTable 이용해 구현
- Object가 아니라 String 형식으로 저장
- 파일 입출력 유용

### Collections
- Arrays처럼 `fill()`, `sort()`, `binarySearch()`등 메서드 포함
- java.utill.Collection(인터페이스), java.utill.Collections(클래스) 구별!
- **동기화 메서드** 
- **읽기 전용 컬렉션** 설정 메서드
- **싱글톤 컬렉션** 만드는 메서드
- **특정 종류의 객체만 저장하게** 만드는 메서드 -> 제네릭 이전 호환용
