# [Local Variable Type InferenceStyle Guidelines](http://openjdk.java.net/projects/amber/guides/lvti-style-guide)

var를 어떻게 사용하면 좋을지에 대한 이야기입니다.

몇 가지 주의해서 볼 만한 것들을 정리해봤습니다.



## 기본원칙

- 쓰기 쉬운 코드보다 읽기 쉬운 코드가 중요!
- `var`는 명확하게 드러날 때 사용하자!
- IDE가 있어야 가독성이 살아난다면 `var` 쓰지 말자!
- `var voucherService= new VoucherService();`
  - 이건 오히려 `var`를 쓰는 것이 더 가독성이 높음!



## 지침: `var`를 사용할 때는 ...

### 1.정보를 전달하는 변수명을 지어야 함

### 2.지역변수 범위를 최소화하라

```java
var items = new ArrayList<Item>(...);
items.add(MUST_BE_PROCESSED_LAST);
for (var item : items) ...
```

위의 경우 당연히 `MUST_BE_PROCESSED_LAST`는 `for`문의 마지막에 처리됩니다.

```java
var items = new HashSet<Item>(...);
items.add(MUST_BE_PROCESSED_LAST);
for (var item : items) ...
```

하지만 `var`로 선언된 `items`가 `hashSet`으로 변경된다면, `MUST_BE_PROCESSED_LAST`가 `for`문의 마지막에 처리된다는 보장이 없습니다.

특히 심각한 경우는 

```java
var items = new HashSet<Item>(...);

// ... 100 lines of code ...

items.add(MUST_BE_PROCESSED_LAST);
for (var item : items) ...
```

만일 위와 같이 `var` 선언된 변수를 상당히 뒤에서 사용할 경우, 문제 파악 자체가 힘들어진다.

만일 `List<Item>`이라고 명시적으로 선언된 것을 `Set<Item>`으로 변경하는 경우라면 개발자는 `List`와 `Set`의 자료구조 차이로 인한 문제를 고려하겠지만,
`var`로 선언했을 경우에는 놓치기 쉽다.



### 3.초기화가 충분한 정보를 줄 때 쓰자.

```java
ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

var outputStream = new ByteArrayOutputStream();
```

위의 버전보다 아래 버전이 오히려 더 읽기 좋습니다. 생성자가 아니라 정적 팩터리 메서드를 사용한다고 해도 적용될 수 있습니다.



### 4.중첩된 표현식 분리

```java
return strings.stream()
              .collect(groupingBy(s -> s, counting()))
              .entrySet()
              .stream()
              .max(Map.Entry.comparingByValue())
              .map(Map.Entry::getKey);
```

위의 코드는 사실 단일 스트림으로 구성되어 있지 않습니다.
명확하게 스트림별로 구별한다면 다음과 같아집니다.

```java
Map<String, Long> freqMap = strings.stream()
                                   .collect(groupingBy(s -> s, counting()));
Optional<Map.Entry<String, Long>> maxEntryOpt = freqMap.entrySet()
                                                       .stream()
                                                       .max(Map.Entry.comparingByValue());
return maxEntryOpt.map(Map.Entry::getKey);
```

위의 경우 긴 `Optional<Map.Entry<String, Long>>`이 가독성을 상당히 떨어트립니다.

```java
var freqMap = strings.stream()
                     .collect(groupingBy(s -> s, counting()));
var maxEntryOpt = freqMap.entrySet()
                         .stream()
                         .max(Map.Entry.comparingByValue());
return maxEntryOpt.map(Map.Entry::getKey);
```

반면, `var`를 사용한 경우 메서드 체인이 깔끔하게 분리됩니다.



### 5.인터페이스로 선언하는 경우

```java
List<String> list = new ArrayList<>();
```

위와 같이 선언하는 편이 안전하고, 변경에도 더 좋습니다. 그런데 `var`를 사용하면

```java
// Inferred type of list is ArrayList<String>
var list = new ArrayList<String>();
```

바로 `ArrayList`로 선언되어 버립니다. 이 선언 범위가 좁다면 파급 효과를 줄일 수 있다고는 하는데, 개인적으로는 찜찜합니다.



### 6.제네릭을 쓴다면 주의하자

```java
// OK: both declare variables of type PriorityQueue<Item>
PriorityQueue<Item> itemQueue = new PriorityQueue<>();
var itemQueue = new PriorityQueue<Item>();
```

위와 같이 쓴다면 의도대로 `PriorityQueue<Item>`이 나오지만,

```
// DANGEROUS: infers as PriorityQueue<Object>
var itemQueue = new PriorityQueue<>();
```

아래와 같이 `<>`안에 타입 명시를 제대로 안 하면 `PriorityQueue<Object>`가 되 버립니다.



### 7.리터럴과 쓸 때 주의하라

- `boolean`, `char`, `long`(단 `10L`하는 식으로 명확하게 표기), `String`의 경우에는 써도 안전
- `byte`, `short`, `long`은 다음과 같은 경우 몽땅 `int` 취급되어 버린다.

```java
// ORIGINAL
byte flags = 0;
short mask = 0x7fff;
long base = 17;

// DANGEROUS: all infer as int
var flags = 0;
var mask = 0x7fff;
var base = 17;
```

- `float`, `double`의 경우, 

```java
// ORIGINAL
float f = 1.0f;
double d = 2.0;

// GOOD
var f = 1.0f;
var d = 2.0;
```

- `float` 일 때 표기만 맞추면 괜찮습니다. 다만

```java
// ORIGINAL
static final float INITIAL = 3.0f;
...
double temp = INITIAL;

// DANGEROUS: now infers as float
var temp = INITIAL;
```

- 위와 같이 사용한다면 혼란 생깁니다.