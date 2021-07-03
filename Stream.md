# Stream<T>

# Package java.util.stream

Classes to support functional-style operations on streams of elements, such as map-reduce transformations on collections.

### Stream<T>

A sequence of elements supporting sequential and parallel aggregate operations.

순차적(sequential)이며 병행적인 aggregate operations을 지원하는 원소들의 시퀀스.

## Interface Stream<T>

**Type Parameters:**`T` - the type of the stream elements

매개변수 자료형: T - 스트림 원소들의 자료형

**All Superinterfaces:**[AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html), [BaseStream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/BaseStream.html)<T,[Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)<T>>

상위인터페이스들: [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html), [BaseStream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/BaseStream.html)<T,[Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)<T>>

```java
public interface Stream<T> extends BaseStream<T,Stream<T>>
```

A sequence of elements supporting sequential and parallel aggregate operations. The following example illustrates an aggregate operation using Stream and IntStream:

순차적(sequential)이며 병행적인 aggregate operations을 지원하는 원소들의 시퀀스. 아래의 예는 Stream과 IntStream을 사용한 aggregate operations을 보여준다:

```java
     int sum = widgets.stream()
                      .filter(w -> w.getColor() == RED)
                      .mapToInt(w -> w.getWeight())
                      .sum();
```

In this example, widgets is a Collection<Widget>. We create a stream of Widget objects via Collection.stream(), filter it to produce a stream containing only the red widgets, and then transform it into a stream of int values representing the weight of each red widget. Then this stream is summed to produce a total weight.

이 예시에서 widget은 Collection<Widget>이다. 우리는 Collection.stream()을 통해, 오로지 붉은 widgets만을 갖게끔 filter한 스트림을 만들고, 이를 각 붉은 widget의 무게들을 보여주는 int 값들의 스트림으로 변형시키는  Widget 객체들의 스트림을 만든다. 그리고 이 스트림은 총 무게를 만들어 내게끔 합산된다.

In addition to Stream, which is a stream of object references, there are primitive specializations for IntStream, LongStream, and DoubleStream, all of which are referred to as "streams" and conform to the characteristics and restrictions described here.

객체 references의 스트림인 Stream에 더하여, primitive 특화 IntStream, LongStream, DoubleStream이 있다. 이들 각각은 "스트림들"이라 언급되며 여기서 서술되는 특성들과 제약에 따른다.

To perform a computation, stream operations are composed into a stream pipeline. A stream pipeline consists of a source (which might be an array, a collection, a generator function, an I/O channel, etc), zero or more intermediate operations (which transform a stream into another stream, such as filter(Predicate)), and a terminal operation (which produces a result or side-effect, such as count() or forEach(Consumer)). Streams are lazy; computation on the source data is only performed when the terminal operation is initiated, and source elements are consumed only as needed.

계산(computation)을 수행하기 위해서, 스트림 연산들은 스트림 파이프라인으로 구성된다. 한 스트림 파이프라인은 소스(배열일수도, 콜렉션일수도, generator function일수도 I/O채널 등일수도 있다) , 0개 이상의 중간 연산들(스트림을 다른 스트림으로 변형시키는 것으로, 필터(술어Predicate)와 같은 것),  terminal operation(결과 혹은 부작용(side-effect)을 일으키는 것, count() or forEach(Consumer)와 같은 것들)으로 이루어진다. 스트림은 게으르다; 소스 데이터에 대한 연산은 오로지 terminal operation이 시작되었을 때만 수행되며, 소스 원소들은 오로지 필요할 때마다 소모된다.

Collections and streams, while bearing some superficial similarities, have different goals. Collections are primarily concerned with the efficient management of, and access to, their elements. By contrast, streams do not provide a means to directly access or manipulate their elements, and are instead concerned with declaratively describing their source and the computational operations which will be performed in aggregate on that source. However, if the provided stream operations do not offer the desired functionality, the BaseStream.iterator() and BaseStream.spliterator() operations can be used to perform a controlled traversal.

콜렉션과 스트림은, 몇몇 피상적 공통점을 갖고 있음에도, 서로 다른 목표를 가진다. 콜렉션들은 기본적으로 그 원소들에 대한 효율적인 관리 및 접근에 기본적으로 관심이 있다. 반대로 스트림은 그 원소들에 직접적으로 접근하거나 조작하는 수단을 제공하지 않는다. 대신 그 소스와 거기서 총체적으로(aggregate) 수행될 컴퓨터 연산(computational operations)을 명시적으로 서술(declaratively describing)하는 데 관심이 있다. 하지만, 만약 주어진 스트림 연산들이 원하는 기능(desired functionality)을 제공하지 못한다면, BaseStream.iterator()와 BaseStream.spliterator() 연산들이 통제된 순회(controlled traversal)을 수행하는데 사용될 수 있다.

A stream pipeline, like the "widgets" example above, can be viewed as a query on the stream source. Unless the source was explicitly designed for concurrent modification (such as a ConcurrentHashMap), unpredictable or erroneous behavior may result from modifying the stream source while it is being queried.

스트림 파이프라인은, 위의 "widgets" 예시와 같이, 스트림 소스에 대한 쿼리로 보일 수도 있다. 이 소스가 동시 변경에 맞게 명확히 디자인되지 않아서(ConcurrentHashMap 처럼), 예기치못하거나 에러로 인한 결과가 쿼리되는 동안 스트림 소스를 변형하는 데에 발생할 수 있다.

Most stream operations accept parameters that describe user-specified behavior, such as the lambda expression w -> w.getWeight() passed to mapToInt in the example above. To preserve correct behavior, these behavioral parameters:

대부분의 스트림 연산들은 매개변수를 유저가 명시한(user-specified) 행위들, 예시의 mapToInt로 넘겨진 람다식 w→w.gerWeight() 와 같은 것으로 서술한다. 올바른 행위를 보존하기 위해서 이 행위 매개변수들은(behavioral parameters) 다음과 같아야 한다:

- must be [non-interfering](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html#NonInterference) (they do not modify the stream source); and
- 불간섭적(non-interfering)이어야만 한다(스트림 소스를 변형시키지 않는다); 그리고
- in most cases must be [stateless](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html#Statelessness) (their result should not depend on any state that might change during execution of the stream pipeline).
- 대부분의 경우에서 무상태(stateless)여야만 한다(그들의 결과는 스트림 파이프라인의 수행 중에 변화할 수 있는 어떠한 상태에도 의존적이지 않아야 한다).

Such parameters are always instances of a functional interface such as Function, and are often lambda expressions or method references. Unless otherwise specified these parameters must be non-null.

이러한 매개변수들은 항상 Function과 같은 함수형 인터페이스(functional interface)의 인스턴스이다. 그리고 종종 람다식이거나 메소드 레퍼런스(method references)다. 별도로 명시된 바가 없다면 이러한 매개변수들은 null값이 아니어야만 한다.

A stream should be operated on (invoking an intermediate or terminal stream operation) only once. This rules out, for example, "forked" streams, where the same source feeds two or more pipelines, or multiple traversals of the same stream. A stream implementation may throw IllegalStateException if it detects that the stream is being reused. However, since some stream operations may return their receiver rather than a new stream object, it may not be possible to detect reuse in all cases.

스트림은 단 한번만 수행(중간적 혹은 terminal 스트림 연산을 호출)되는 것이 좋다. 예를 들어 이는 "갈라진("forked") 스트림들을 배제시킨다. 이런 스트림들에서는 동일한 소스가 두 개 이상의 파이프라인에 들어가거나(feeds) 수 많은 순회들(traversals)이 동일한 스트림 안에 들어간다. 한 스트림 구현은 만일 그 스트림이 재사용 된 것을 발견한다면 IllegalStateException을 발생시킬 수 있다. 하지만 몇몇 스트림 연산들이 새 스트림 객체보다는 그 연산들의 receiver를 리턴할 수 있기에, 재사용을 모든 경우에서 발견할 수는 없다.

Streams have a BaseStream.close() method and implement AutoCloseable, but nearly all stream instances do not actually need to be closed after use. Generally, only streams whose source is an IO channel (such as those returned by Files.lines(Path, Charset)) will require closing. Most streams are backed by collections, arrays, or generating functions, which require no special resource management. (If a stream does require closing, it can be declared as a resource in a try-with-resources statement.)

스트림들은 BaseStream.close() 메소드를 가지고 있으며, AutoCloseable를 구현하고 있지만, 거의 모든 스트림 인스턴스들은 사용 후에 단을 필요가 실상 없다. 일반적으로 그 소스가 IO채널인 스트림들만이(Files.lines(Path, Charset)으로 리턴되는 것들과 같은) 닫을 필요가 있다. 대부분의 스트림들은 특별히 자원 관리가 불필요한 콜렉션, 배열, 생성함수(generating functions)에  기반하기 때문이다. (만약 한 스트림이 닫을 필요가 없다면 이는 try-with-resources statement로 리소스 안에서 선언될 수 있다.)

Stream pipelines may execute either sequentially or in parallel. This execution mode is a property of the stream. Streams are created with an initial choice of sequential or parallel execution. (For example, Collection.stream() creates a sequential stream, and Collection.parallelStream() creates a parallel one.) This choice of execution mode may be modified by the BaseStream.sequential() or BaseStream.parallel() methods, and may be queried with the BaseStream.isParallel() method.

스트림 파이프라인들은 순차적으로도 병렬적으로도 수행될 수 있다. 이 수행 모드는 스트림의 속성(property)다. 스트림들은 순차적 혹은 병렬적 수행을 최초로 선택되면서 만들어진다. (예를 들어서, Collection.stream()은 순차적 스트림을 만들며, Collection.parallelStream()은 병렬적인 스트림을 만든다.) 이 수행 모드 선택은 BaseStream.sequential()이나 BaseStream.parallel() 메소드로 수정 가능하며, BaseStream.isParallel()메소드로 쿼리될 수 있다.

**Since:**

1.8