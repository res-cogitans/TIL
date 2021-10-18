# JavaDocs의 `equals()`

public boolean equals([Object](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/lang/Object.html) obj)

Indicates whether some other object is "equal to" this one.

어떤 다른 객체가 이 객체와 "동등한지(equal to)" 가리킨다.



The `equals` method implements an equivalence relation on non-null object references:

- It is *reflexive*: for any non-null reference value `x`, `x.equals(x)` should return `true`.
- It is *symmetric*: for any non-null reference values `x` and `y`, `x.equals(y)` should return `true` if and only if `y.equals(x)` returns `true`.
- It is *transitive*: for any non-null reference values `x`, `y`, and `z`, if `x.equals(y)` returns `true` and `y.equals(z)` returns `true`, then `x.equals(z)` should return `true`.
- It is *consistent*: for any non-null reference values `x` and `y`, multiple invocations of `x.equals(y)` consistently return `true` or consistently return `false`, provided no information used in `equals` comparisons on the objects is modified.
- For any non-null reference value `x`, `x.equals(null)` should return `false`.

`equals` 메서드는 null이 아닌 객체 참조들의 동등성 관계를 구현한다:

- 이는 *재귀적(reflexive)*이다: null이 아닌 모든 참조값 `x`에 대해서, `x.equals(x)`는 `true`를 반환해야 한다.
- 이는 *대칭적(symmetric)*이다: null이 아닌 모든 참조값 `x`와 `y`에 대해서, `x.equals(y)`는 `true`를 반환하는 것과 `y.equals(x)`가 `true`를 반환하는 것은 필요충분 관계이다.
- 이는 *추이적(transitive)*이다: null이 아닌 모든 참조값 `x`와 `y`, 그리고 `z`에 대해서, 만약 `x.equals(y)`가 `true`를 반환하고, `y.equals(z)`가 `true`를 반환한다면, `x.equals(z)`는 `true`를 반환해야 한다.
- 이는 *일관적(consistent)*이다: null이 아닌 모든 참조값 `x`와 `y`에 대해서, 만약 `equals`비교에 사용되는 객체들의 정보들이 변경되지 않는 한에서, `x.equals(y)`를 여러 번 호출하는 것(multiple invocations)은 일관성 있게 `true`를 반환하거나, 일관성 있게 `false`를 반환해야 한다.
- null이 아닌 참조값 `x`에 대하여 `x.equals(null)`은 `false`를 반환해야 한다.



The `equals` method for class `Object` implements the most discriminating possible equivalence relation on objects; that is, for any non-null reference values `x` and `y`, this method returns `true` if and only if `x` and `y` refer to the same object (`x == y` has the value `true`).

`Object` 클래스의 `equals` 메서드는 객체 사이에서 가능한 한 구별적인 동등성 관계를 구현했다. 그것은, null이 아닌 모든 참조값 `x`와 `y`에 대해서, 이 메서드가 `true`를 반환하는 것은 `x`와 `y`가 동일한(same) 객체를 참조하는 것(`x == y`가 `true` 값을 가질 때)와 필요충분관계이다.

Note that it is generally necessary to override the `hashCode` method whenever this method is overridden, so as to maintain the general contract for the `hashCode` method, which states that equal objects must have equal hash codes.

일반적으로 이 메서드를 오버라이드 했을 때마다 `hashCode`를 오버라이드 하는 것이 일반적으로 필수적임을 주의하라. 동등한(equal) 객체들이 동등한 해시코드를 가져야 한다는 `hashCode` 메서드의 일반규약을 지키기 위함이다.

- Parameters:

  `obj` - the reference object with which to compare.

- Returns:

  `true` if this object is the same as the obj argument; `false` otherwise.

- See Also:

  [`hashCode()`](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/lang/Object.html#hashCode()), [`HashMap`](https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/util/HashMap.html)



-  (`x.equals(y)` iff `y.equals(x)`, 논리학에서 필요충분조건인 두 명제를 동치(equivalence)라고 한다는 점은 흥미롭다.)



# [Secrets of equals() - Part 2](http://www.angelikalanger.com/Articles/JavaSolutions/SecretsOfEquals/Equals-2.html)

How to implement a correct slice comparison in Java



###  Implementing equals() To Allow Mixed-Type Comparison

**Mixed-Type 비교가 가능하게끔 equals()를 구현하기**



All objects in Java are equality-comparable via the `equals()` method because all classes inherit the `equals()` method from class `Object` . As a result, each time a programmer implements a new class, he or she must check whether the inherited version of `equals()` provides the correct semantics for the new class or whether the default implementation must be overridden. Naturally, any user-defined version of `equals()` should be correct. What does "correct" mean? It means meeting the requirements of the so-called equals contract, which is specified in JavaDoc of `Object.equals` .

자바의 모든 객체들은(objects) `equals()`메서드를 통해서 동등성 비교가 가능(equality-comparable)하다. 왜냐면 모든 클래스들은 `Object`클래스로부터 `equals()`메서드를 상속하기 때문이다. 결과적으로 프로그래머가 새 클래스를 구현할 때 마다, 그는 `equals()`의 상속된 버전이 새 클래스의 적절한 의미(론)를 제공하는가, 혹은 기본 구현이 오버라이드 되어야 하는가를 살펴봐야 한다. 자연적으로, 사용자가 정의한 버전의 `equals()`는 옳아야 한다. "옳다는(correct)" 것이 무엇을 의미하는가? 이는 `Object.equals()`의 JavaDoc에 명시되어 있는 소위 equals 일반 규약(contract)이라 불리는 요구사항(requirements)를 만족함을(meeting) 의미한다.



In a previous article we discussed at length how easy it is to inadvertently provide incorrect implementations of `equals()` . The most common mistake you can find in publications and real-world source code is implementations that do not meet the transitivity requirement imposed by the equals contract: if one object is equal to a second, and the second is equal to a third object, then the first and the third object must be equal as well.

이전 글에서 우리는 얼마나 무심코 `equals()`의 잘못된 구현들을 제공할 수 있는가에 대하여 길게 논의했다. 출판물들과 실제 소스 코드들에서 찾을 수 있는 가장 흔한 실수는 equals 일반규약에서 제시하는 추이적 필요조건를 충족시키지 못하는 구현들이다: 만약 한 객체가 두 번째 객체와 동일하다면, 그리고 두 번째 객체가 세 번째 객체와 동일하다면, 첫 번째와 세 번째 객체는 또한 동일해야만(equal) 한다.



The core problem of a non-transitive implementation is that it attempts to allow comparison of objects that belong to the same class hierarchy. All the problems regarding transitivity go away as soon as you disallow mixed-type comparison and only allow comparison of objects of the exact same type. The same-type comparison is easy to implement: you check whether `this` object and the `other` object are of the same type by comparing their `Class` objects which you can obtain via `getClass()` . Here is an example:

추이적이지(transitive) 않은 구현들의 가장 주된 문제는, 이 구현이 동일한 클래스 계층에 속한 객체들의 비교를 시도한다는 것이다. 추이성(transitivity)에 관한 문제 전체는 서로 다른 타입에 대한 비교(mixed-type comparison)를 허용하지 않고 완전히 동일한 자료형의 객체들간의 비교만 허용한다면 사라진다. 동일타입에 대한 비교는 구현하기 쉽다: `this` 객체와 `other` 객체가 동일한 타입인가를 `getClass()`를 통해 얻을 수 있는 그것들의 `class` 객체들을 비교하면 된다. 다음은 그 예시다:



```java
public boolean equals(Object other) {
 ...
 if (this.getClass() != other.getClass())
  return false;
 else
  // these objects can be compared; go and compare their fields
 ...
}
```



This type of implementation of the `equals()` method is easy to get right; it is robust, easy to understand, easy to maintain, and for that reason the recommended way of implementing `equals()` - unless you need something else.

이런 형태의(type of) `equals()`메서드 구현은 맞게 되기(get right) 쉽다; 이는 탄탄하며(robust), 쉽게 이해할 수 있으며, 쉽게 유지할 수 있으며, 이런 이유에서 `equals()`를 구현하는 데 있어서 권장되는 방식인 것이다. - 만약 무언가 다른 것을 필요로 하지 않는다면 말이다.



### The Need for Mixed-Type Comparison

**Mixed-Type Comparison의 필요성**



The idea of comparing objects from the same class hierarchy is not entirely off base. After all, objects from the same class hierarchy (and by class hierarchy we mean all classes derived from a common superclass other than `Object` ) have something in common, namely at least that superclass part. For instance, in a class hierarchy, where `Employee` and `Student` are subclasses of a `Person` , representing roles of a person, it may make sense to compare an `Employee` to a `Student` to see whether they are the same `Person` . With an implementation of `equals()` that only allows same-type comparison an `Employee` and a `Student` would not be comparable. On the other hand, if you have a class hierarchy with a `Fruit` superclass and `Apple` and `Pear` subclasses, then you might find the comparison of an `Apple` and a `Pear` nonsensical.

동일한 클래스 hierarchy에서 객체들을 비교하는 생각이 완전히 틀린 것은(off base) 아니다. 결국, 동일 클래스 hierarchy의 객체들은 (여기서 클래스 hierarchy라는 말은 `Object`가 아닌 공통 슈퍼클래스에서 분리되어 나오는 모든 클래스를 의미한다.) 무언가를 공통적으로 가지고 있는데, 즉 최소한 그 슈퍼클래스의 부분을(part) 가지고 있다. 예를 들어,  한 사람(person)의 역할들을 대표하고 있는 `Employee`와 `Student`가 `Person`의 서브클래스인,  클래스 hierarchy에서, `Employee`를 `Student`와 같은 `person`인가를 비교하는 것은 의미가 통한다(make sense).   동일 타입 비교만을 허용하는 `equals()`의 구현에서는, `Employee`와 `Student`는 비교 불가능하다. 반면에, 만일 `Fruit` 슈퍼클래스와 `Apple`과 `Pear` 서브클래스를 가진 클래스 hierarchy에서는 `Apple`과 `Pear`의 비교는 무의미하다.



Which type of comparability makes sense for a given class hierarchy depends entirely on the semantics of classes. Each type of comparability has side effects, each of which may or may not be desired (see [sidebar](http://www.angelikalanger.com/Articles/JavaSolutions/SecretsOfEquals/Equals-2.html#box) on "` equals()` and its impact on the rest of your program"). The point is: same-type-comparison-only is easy to implement, but it may make sense to allow mixed-type comparison for certain class hierarchies. The question in these cases is: how can it be implemented correctly, so that it meets the requirements of the equals contract? As we demonstrated in a preceding article, providing a correct implementation is a non-trivial task. In this article we will show one way of doing it.

주어진 클래스 hierarchy에서 어떤 형태의 비교 가능성(comparability)가 의미가 통하느냐는, 클래스들의 의미(론)에 전적으로 달려 있다. 비교 가능성의 각 자료형들마다 부작용이(side effects) 있으며, 각각 바랄만하거나, 그렇지 않다. (아래 "`equals()`와 그것이 당신의 프로그램에 미치는 영향"을 보라) 요점은 다음과 같다: 동일 타입 비교만 가능하게 하는 것은 구현하기에는 쉽다. 그러나 특정 클래스 hierarchies에서는 mixed-type comparison을 허용하는 것이 의미가 통한다. 이 케이스들에서 물어볼 것은 다음과 같다: equals 규약의 요구사항을 충족하게끔 어떻게 올바르게 구현될 수 있는가? 이전 글에서 우리가 설명한 바와 같이 올바른 구현을 제공하는 것은 중대한(답이 여럿인, non-trivial) 일이다. 이 글에서 우리는 그런 구현을 하는 한 방식을 볼 것이다.



#### equals() and its impact on the rest of your program

**`equals()`와 그것이 당신의 프로그램에 미치는 영향**

`equals()` is used by various other classes and its semantics, mixed-type comparison or same-type-comparison-only, influences the behavior of other components that you or others use in conjunction with your classes. 복합 타입 비교가 가능한 것이든 단일 타입 비교만 가능하게 한 것이든, 다양한 클래스들과 그 의미(론)들에서 사용되는 `equals()`는 당신의 클래스들과 함께 사용하는 다른 컴포넌트들의 작용(behavior)에 영향을 미친다. 

For instance, `equals()` is closely related to the `HashSet` collection of the JDK, because its implementation relies on the elements' `equals()` methods for its internal organization. 

예를 들어, `equals()`는 JDK의 `hashSet` 컬렉션과 밀접하게 연관이 있다. 왜냐하면 `hashSet`의 구현은 요소들 내부 구조에서의(for internal organization) `equals()`메서드에 의존하기 때문이다.



- *Same-type-comparison-only implementation of* `equals()` . With such an implementation of `equals()` you can store an `Employee("Hanni Hanuta")` and a `Student("Hanni Hanuta")` into the same `HashSet` , but retrieval from the collection will not work as expected. You will not find any of these two contained objects when you ask the `HashSet` whether it contains a `Person("Hanni Hanuta")` , because all three object are unequal to each other. 

- *동일 타입 비교만 가능한* `equals()`*의 구현* . 이러한 `equals()`의 구현에서, 당신은 `Employee("Hanni Hanuta")`과 `Student("Hanni Hanuta")`를 같은 `HashSet`에 저장할 수 있다. 하지만 컬렉션에서 검색(retrieval )은 기대한 대로 작동하지 않을 것이다. `Person("Hanni Hanuta")`를 가지고 있느냐고 `HashSet`에 당신이 묻는다고 해도, 당신은 이 두 객체들 중 무엇도 찾을 수 없을 것이다. 왜냐면 이 세 객체들은 서로 동등하지 않기(unequal) 때문이다.



- *Mixed-type-comparison implementation of* `equals()` . Conversely, with this type of `equals()` implementation you will have problems storing an `Employee("Hanni Hanuta")` *and* a `Student("Hanni Hanuta")` in the same `HashSet` . The `HashSet` will reject the second `add()` operation, because the collection already contains an element that compares equal to the new element.
- *복합 타입 비교가 가능한* `equals()`*의 구현*. 반대로, 이 형태의 `equals()` 구현에서 동일 `HashSet`에 `Employee("Hanni Hanuta")`와 `Student("Hanni Hanuta")`를 저장하는 일에서 문제가 발생할 것이다. `HashSet`은 두 번쨰 `add()` 연산을 거부할 것이다. 왜냐면 이 컬렉션은 이미 새 요소와 동등하게(equal) 비교되는 요소를 이미 가지고 있기 때문이다.



Both effects might be surprising and this is not to say that any of them is incorrect or weird. It's just that the semantics of your `equals()` implementation has an impact on the behavior of other components that rely on the `equals()` method of your classes.

두 효과들은 모두 놀라운 것이며 둘 중 무엇이 잘못되었거나 이상한 것이 아니다. 이는 그저 당신의 `equals()` 구현의 의미(론)이 당신 클래스의 `equals()` 메서드에 의존하는 다른 컴포넌트들의 동작들에 영향을 주는 것일 뿐이다.



### The Semantics of a Mixed-Type Comparison

**복합 타입 비교에 대한 의미(론)**



The most common mistake in implementations of mixed-type comparison is the semantics of the comparison.

복합 타입 비교의 구현들에 대한 가장 일반적인 실수는 비교의 의미(론)이다.



​	Q: Under which circumstances do we consider two objects of different type from the same class hierarchy as equal?

​	A: If they have equal superclass parts.

​	Q: How about the subclass-specific parts?



​	Q: 어떤 상황일 때, 동일 클래스 hierarchy에서 나온 서로 다른 타입의 두 객체들을 동등(equal)하다고 봐야 할까?

​	A: 만약 그것들이 동등한 슈퍼클래스 부분들(parts)을 가지고 있다면 그렇다.

​	Q: 서브클래스에만 있는(subclass-specific) 부분들은 그럼 어떻게 다뤄야 하는가?


The problem is that the intransitive implementations of `equals()` do not care about the subclass-specific parts of the objects that they compare. Here is an example: say, we have a `Point` superclass and a `ColoredPoint` subclass. When we compare `ColoredPoint(0,0,RED)` to `Point(0,0)` , then the typical implementation says: yes, they are equals. If you then go on to compare `Point(0,0)` to a `ColoredPoint(0,0,BLUE)` , then they, too, will compare equal. But when we compare `ColoredPoint(0,0,RED) `and `ColoredPoint(0,0,BLUE)` , then they should compare equal because of the transitivity requirement of the equals contract, but in fact they compare unequal. What went wrong?

`equals()`의 추이적이지 않은 구현들은 그들이 비교하는 객체들의 서브클래스에만 있는 부분들을 신경쓰지 않는다는 점이 문제다. 다음 예시를 보라: `Point`슈퍼클래스와 `ColoredPoint` 서브클래스가 있다. 만약 우리가 `ColoredPoint(0, 0, RED)`를 `Point(0, 0)`과 비교한다면, 전형적인(typical) 구현은 다음과 같이 말할 것이다: 그래, 둘은 동등하다(equals). 만약 당신이 계속해서 `Point(0, 0)`를 `ColoredPoint(0, 0, BLUE)`과 비교한다면, 여기서도 동등하다고 비교할 것이다. 하지만 만약 우리가 `ColoredPoint(0, 0, RED)`와 `ColoredPoint(0, 0, BLUE)`를 비교한다면, 이 둘을 동등하다고 말해야 할 것이다. equals 일반 규약의 추이성 요구사항 때문이다. 그러나 실제로는 그 둘은 동등하지 않다(unequal). 어떻게 잘못된 것인가?



The crux lies in the subclass-specific color field. A `Point` object should only compare equal to a `ColoredPoint` object if the color field contains a default value. Under this rule, the three objects from the example above do not compare equal any longer. `Point` s and `ColoredPoint` s only compare equal when the `ColoredPoint` has a default color, that is, `Point(0,0)` is equal to one and only one `ColoredPoint` with the coordinates `(0,0)` .

난점(crux)은 서브클래스에만 있는 색상 필드에 있다. `Point` 객체는 `ColoredPoint` 객체가 색상 필드에 기본 값을 가지고 있는 경우에만 동등해야 한다. 이 규칙 하에서, 위의 예시의 세 객체들은 더 이상 서로 동등하지 않다. `Point`들과 `ColoredPoint`들은 `ColoredPoint`가 기본 색상을 가지고 있을 때에만 동등할 수 있다. 즉 `Point(0, 0)`은 `ColoredPoint`가 오로지 좌표 `(0, 0)`을 가지고 있는 그 경우에만 동등하다는 것이다.



Transitivity is preserved under this rule. It does not matter what this default color is. The only thing that counts is that for a transitive mixed-type comparison is that all the fields that cannot be compared (which are the subclass-specific fields) have well-defined default values. Hence the rule is:

이 규칙 하에서는 추이성이 보존된다. 이 기본 색상이 무엇인가는 문제되지 않는다. 추이적인 복합 타입 비교에서 가장 중요한 것은 이거다. 비교될 수 없는 모든 필드는(즉 서브클래스에만 있는 필드들은) 잘 정의된 기본 값을 가지고 있어야 한다는 것이다. 따라서 규칙은 다음과 같다:



Two objects from the same class hierarchy compare equal if and only if they have

- equal values for all fields that they have in common (equal superclass part)

  and

- default values for all other fields (default subclass-specific part).

동일 클래스 hierarchy의 두 객체가 동등함은 다음과 필요충분조건이다.

- 공통적으로 가지고 있는 모든 필드들에 대해서 동등한 값들 (동등한 슈퍼클래스 부분)

  그리고

- 모든 다른 필드들에 대한 기본 값들 (기본 상태인 서브클래스에만 있는 부분)



To make things a little more challenging, let us consider a slightly more complex class hierarchy and see whether the rule holds. Say, we have an additional subclass `Point3D` that is derived from class `Point` and that represents 3-dimensional points. Both `ColoredPoint` and `Point3D` belong to the same class hierarchy, class `Point` being the root class of that hierarchy. What does it mean to compare a `ColoredPoint` to a `Point3D` ?

좀 더 복잡한 클래스 hierarchy를 고려해보고, 이 규칙이 유지되는가를(holds) 살펴봄으로써, 보다 상황을 더 까다롭게(challenging) 다뤄보자. 우리는 이제 `Point`클래스에서 분화된 추가적인 서브클래스 `Point3D`를 가지고 있고, 이 클래스는 3차원 지점을 표현하고 있다. `ColoredPoint`와 `Point3D` 둘 다 동일 클래스 hierarchy에 속하며, `Point`클래스는 그 hierarchy의 루트 클래스가 된다. `ColorPoint`를 `Point3D`와 비교하는 것은 무엇을 의미하는가?



Under the rule above it means that a `ColoredPoint` and a `Point3D` compare equal if and only if they have the same `Point` part and default values for their subclass-specific parts, namely the color and the 3 rd coordinate. For instance, if black is the default color and 0 the default coordinate, then `Point(0,0)` and `ColoredPoint(0,0,BLACK)` and `Point3D(0,0,0)` would be equal, but neither `ColoredPoint(0,0,RED)` nor `Point3D(0,0,1)` would be equal to `Point(0,0)` .

위의 규칙에 따르면 `ColorPoint`와 `Point3D`가 동등하다는 것은 둘이 동일한 `Point` 부분들을 가지고 있고, 그들의 서브클래스에만 있는 부분들, 즉 색상과 z축(3번째 좌표값)에서 기본값을 가지고 있는 것과 동치다. 예를 들어서 만약 검정이 기본 색상이며 0이 기본 좌표값이라면, `Point(0, 0)`과 `ColoredPoint(0, 0, BLACK)` 그리고 `Point3D(0, 0, 0)`은 동등할 것이다. 하지만 `ColoredPoint(0, 0, RED)`나 `Point3D(0, 0, 1)`은 `Point(0, 0)`과 동등하지 않다.



After these preliminaries let us implement it.

이러한 예비 작업들(preliminaries)을 따라 구현해보자.



### Implementing the Mixed-Type Comparison

**복합 타입 비교 구현하기**



The functionality that we aim to implement depends on the type of `this` object and the `other` object. There are four cases to distinguish between:

 우리가 구현하고자 목표하는 기능은 `this` 객체와 `other` 객체의 타입에 달려있다. 그것들은 네 가지로 구별된다:



1. `this` and `other` are of the exact same type.
2. All fields can and must be compared.

1. `this`와 `ohter`는 정확히 동일한 타입이다.

   모든 필드들은 비교될 수 있으며, 그래야만 한다.



2. `this` is of a subtype of `other` .

   The `other` object is smaller than `this` object and only the superclass fields can be compared for equality; the subclass-specific fields of `this` object must have default values. 

2. `this`는 `ohter`의 서브타입이다.

   `other` 객체는 `this` 객체보다 작으며, 오직 슈퍼클래스 필드들만 동등성을 위해 비교될 수 있다; `this` 객체의 서브클래스 있는 필드들은 기본값을 가지고 있어야만 한다.



3. `this` is of a supertype of `other`.

   The `other` object is the larger object that has subclass-specific fields that must have default values. This can be implemented easiest if we let the `other` object do the work, which gets us back to case 2  with the roles of `this` and `other` being switched. 

3. `this`는 `other`의 슈퍼타입이다.

   `other` 객체는 기본값을 필수로 갖는 서브클래스 전용 필드들이 있는 더 큰 객체이다. 만약 우리가 `other` 객체가 이 작업(`equals`)을 하게끔 한다면 가장 쉽게 구현될 수 있다. 2번 사례에서 `this`와 `other`의 역할만 반대로 바꾸면 되기 때문이다.



4. `this` and `other` are from different branches of the class hierarchy.

   Both objects have something in common, which must be compared for equality, and they both have subclass-specific fields, which must checked against the default values. This is the tricky part. First, we must identify the superclass that `this` and `other` have in common and second we must check the subclass-specific fields of both objects for default values.

4. `this`와 `other`는 클래스 hierarchy의 서로 다른 branch에서 나온 것이다.

   두 객체들 모두, 동등성 비교를 위해서 비교되어야만 하는 무언가 공통적인 것을 가지고 있다. 그리고 둘 다 기본값을 대조해봐야만 하는, 서브클래스에만 있는 필드들을 가지고 있다. 이 부분이 상당히 곤란한데(tricky), 우선 우리는 반드시 `this`와 `other`가 공통적으로 갖는 슈퍼클래스를 식별해야(identify) 한다. 둘째로 우리는 반드시 양 쪽 모두에서 서브클래스에만 있는 필드들의 기본값을 살펴봐야만 한다.



The principle of the implementation is the usual recursion: every class's implementation of `equals()` takes care of comparing its own fields and delegates to the superclass's implementation of `equals()` for comparison of the inherited fields. Comparing one's own fields comes in two flavors: either the `other` object has fields that `this` object can compare with its own fields, or not. The first case is relevant when `this` object is of the same type or of a subtype of the `other` object (see 1 and 2 in the list above); in this case compare `this` 's fields with `other` 's fields. The second case ("there is nothing to compare on this level because the `other` object is too different") happens in the cases 3 and 4 from the list above; in this case check that `this` 's fields have default values.

구현에 대한 원칙은 일반적인 재귀(recursion)다: 모든 클래스의 `equals()` 구현은 그 자신만의 필드들을 처리하고(takes care of), 슈퍼클래스의 `equals()`구현에게 상속된 필드들에 대한 비교를 위임하는 것이다. 그 자신만의 필드들을 비교하는 것은 두 가지 방식(flavors)으로 나타난다: `this` 객체 자신의 필드로 비교할 수 있는 필드를 `other` 객체가 가지고 있는지, 아닌지이다. 첫 번째 케이스는 만일 `this` 객체가 `other` 객체와 같은 타입이거나, 그것의 서브타입일 때 연관이 있다(위 리스트의 1과 2를 보라); 이 경우에 `this`의 필드들을 `other`의 필드들과 비교하라. 두 번째 케이스("이 단계(level)에서는 `other` 객체가 너무도 다르기 때문에 비교할 것이 없다")는 위 목록의 3, 4번 사례에서 발생한다; 이 경우, `this`의 필드들이 기본값을 가지고 있는지를 살펴보라.



We will encapsulate the actual comparison of a class's own fields into a private helper method that every class in a class hierarchy must provide. Here is the sketch of an implementation of the helper method, which we named `_compareFields()` , for a class with two `int` fields:

우리는 어느 클래스 자신의 필드들에 대한 실제(actual) 비교를 private 헬퍼 메서드 안에 캡슐화할 것이다. 클래스 hierarchy의 모든 클래스들은 이 메서드를 만드시 제공해야만 한다. 두 `int` 필드들을 가지고 있는 한 클래스의 `_compareFields()`라고 이름 붙인 이 헬퍼 메서드의 구현을 간략하게 윤곽을 그려 본다면 다음과 같다:



```java
public class MyClass extends MySuperClass {
  static final int
  field1Default = 0,
  field2Default = 0;
  private int field1 = field1Default;
  private int field2 = field2Default;
  ...
  private boolean _compareFields(Object other) {
    if (other instanceof MyClass) { // at least my type, check fields
    if ( field1 != ((MyClass)other). field1
      || field2 != ((MyClass)other). field2
       )
       return false;
    } else { // higher type, check defaults
       if ( field1 != field1Default
         || field2 != field2Default
          )
          return false;
       }
    return true;
  }
}
```



The `equals()` method performs a couple of preliminary checks, like checking whether the other object is a `null` reference and whether the `other` object belongs to the same class hierarchy as this object. The assumption here is that the topmost superclass of the class hierarchy is a class named RootClass. After the preliminaries `equals()` delegates to another helper method named `_navigateClassHierarchy()` , which controls the class tree traversal and triggers the actual field comparison provided by the other helper method `_compareFields()` .

`equals()` 메서드는 한 두 가지의 사전적인 체크 작업들을 수행한다. 다른 객체가 `null` 참조인지, `other` 객체가 이 객체와 동일한 클래스 hierarchy에 있는지 등인지. 여기서 클래스 hierarchy의 최상단에 있는 클래스는 RootClass라는 이름의 클래스라고 하겠다. 사전작업들을 따라 `equals()`는 `_navigateClassHierarchy()`라는 이름의 다른 헬퍼 메서드에게 작업을 위임한다. 이 헬퍼 메서드는 클래스 트리를 순회를 제어하며, 다른 헬퍼 메서드인 `_compareFields()`가 제공하는 실제 필드 비교 기능을 작동시킬 것이다.



### The Tree Traversal

**트리 순회**



For purpose of illustration let us consider the following class hierarchy:

예시를 위해(For purpose of illustration) 다음과 같은 클래스 hierarchy를 보자:



![img](http://www.angelikalanger.com/Articles/JavaSolutions/SecretsOfEquals/2.Folie1.JPG)



There are four relevant cases to consider, which are listed below. For each of the cases, the diagrams illustrate the location of `thisObject` and `theOtherObject` in the class hierarchy (see box around class name) and the size of each object in terms of the class slices it comprises (see yellow box). For instance, in case 1, `thisObject` is an object of type `MyClass` and contains a `MySuperClass` and a `MyClass` part.

위에 열거한 네 개의 사례들을 고려해 봐야 한다. 각각의 케이스들에 대해서, 다이어그램이 `thisObject`와 `theOtherObject`의 위치를 클래스 hierarchy에서 보여준다. (클래스명 옆의 박스를 보라) 그리고 각 객체를 구성하고 있는 클래스 조각들에 따라 그것의 크기를 보였다.  (노란색 박스를 보라.) 예를 들어, 사례1에서 `thisObject`는 `MyClass` 타입의 객체이며 `MySuperClass`와 `MyClass` 부분들을 가지고 있다. 



```java
MyClass thisObject = new MyClass();
MyClass theOtherObject = null;
```

![img](http://www.angelikalanger.com/Articles/JavaSolutions/SecretsOfEquals/2.Folie2.JPG)



```java
// (1) this and other are of the exact same type 
theOtherObject = new MyClass();
     ... thisObject.equals(theOtherObject) ...	
```



![img](http://www.angelikalanger.com/Articles/JavaSolutions/SecretsOfEquals/2.Folie3.JPG)



```java
// (2) this is of a subtype of other
theOtherObject = new MySuperClass();
     ... thisObject.equals(theOtherObject) ...	
```



![img](http://www.angelikalanger.com/Articles/JavaSolutions/SecretsOfEquals/2.Folie4.JPG)



```java
// (3) this is of a supertype of other
theOtherObject = new MySubClass_1 ();
     ... thisObject.equals(theOtherObject) ...	
```



![img](http://www.angelikalanger.com/Articles/JavaSolutions/SecretsOfEquals/2.Folie5.JPG)



````java
// (4) this and other are from different
// branches of the class hierarchy
thisObject = new MySubClass_1();
theOtherObject = new MySubClass_2();
     ... thisObject.equals(theOtherObject) ...	
````



The algorithm is easy for the cases 1, "` this` and `other` are of the exact same type", and 2, "` this` is of a subtype of `other` " . In these cases we will either compare `this` 's fields to `other` 's fields (1) or check `this` 's field against the default values (2). This is exactly what method `_compareFields()` does. Afterwards we delegate to the superclass for comparison of the inherited fields and start the recursive traversal up the class tree. Here is a first incomplete snippet of the helper method `_navigateClassHierarchy()` :

사례1의 경우 알고리즘은 쉽다. "`this`와 `other`는 정확히 동일한 타입이다", 그리고 2에서는, "`this`는 `other`의 서브타입이다", 이 사례들에서 우리는 `this`의 필드들과 `other`의 필드들을 비교하거나(1) `this`의 필드를 기본값과 대조한다(2). 이는 정확히 `_compareFields()`메서드가 하는 일과 동일하다. 그 후 우리는 상속된 필드들의 비교를 슈퍼클래스에 위임하고, 재귀적 순회를 클래스 트리의 위로 계속한다. 이것이 `_navigateClassHierarchy` 헬퍼 메서드의 첫 번째 부분이다:



```java
public class MyClass extends MySuperClass {
  ...
  protected boolean _navigateClassHierarchy(Object other) {
     ...
     // compare my fields
     if(!_compareFields(other)) return false;
        // pass the buck up
        return super._navigateClassHierarchy(other);
  }
}
```



Case 3, "` this` is of a supertype of `other` " , is similar to case, "` this` is of a subtype of `other` " . If we switch the roles of `this` and other we can use the solution of case 2 which means that we simply call `other` 's `_navigateClassHierarchy()` method and provide the `this` object as an argument. Here is a more elaborate implementation of the tree navigation method:

사례 3은, "`this`는 `other`의 슈퍼타입이다"로, "`this`가 `other`의 서브타입이다"인 케이스 2와 유사하다. 만약 우리가 `this`와 다른 것의 역할을 바꿀 수 있다면, 우리는 케이스 2의 해결책을 사용할 수 있다. 이는 우리가 단순하게 `other`의 `_navigateClassHierarchy()` 메서드를 호출하고 `this` 객체를 인자로 보낼 수 있음을 의미한다. 트리 순회 메서드의 더 정교한 구현을 살펴보자:



```java
public class MyClass extends MySuperClass {
  ...
  protected boolean _navigateClassHierarchy(Object other) {
    if (other instanceof MyClass)
    { // switch roles
      return ((MyClass)other)._navigateClassHierarchy(this);
    }
    else
    { // compare my fields
      if(!_compareFields(other)) return false;
      // pass the buck up
      return super._navigateClassHierarchy(other);
    }
  }
}
```



Note, this implementation is still incomplete, since we haven't addressed case 4, "` this` and `other` are from different branches of the class hierarchy", yet.

아직 우리가 사례 4번을 다루지 않았기에, 이 구현이 여전히 미완성임을 기억하라. 4에서는 `this`와 `other`가 클래스 hierarchy에서 서로 다른 branch에 속한다.



In our example of a case 4 situation, `this` is a `MySubClass_1` object and `other` is a `MySubSubClass` object. Control flow in the tentative implementation of `_navigateClassHierarchy()` shown above will go to the `else` -branch because `other` is not an instance of `MyClass` . The _` compareFields()` method will correctly check whether `this` 's field have default values. Then control flow delegate to the superclass, that is, `MyClass._navigateClassHierarchy()` will be invoked.

4번 상황을 다루는 우리의 예시에서, `this`는 `MySubClass_1` 객체이며, `other`는 `MySubClass` 객체다. (4번 사례에서는) 위에 나온 `_navigateClassHierarchy()`의 잠정적인 구현의 제어 흐름은 `else` branch로 갈 것이다. 왜냐면 `other`는 `MyClass`의 인스턴스가 아니니까. 	_` compareFields()` 메서드는 `this`의 필드가 기본 값을 가지고 있는지를 올바로 점검할 것이다. 그리고 제어 흐름은 슈퍼클래스로 넘어가서, `Myclass._navigateClassHierarchy()`가 호출된다.



This time `other` *is* an instance of `MyClass` , because `MyClass` happens to be superclass that `this` and `other` have in common. Note, we have not yet checked whether the `other` object has default values for its own subclass-specific fields; in order words, we must still traverse the branch of the class tree from which the `other` objects stems. This can be achieved by switching roles and calling `MySubSubClass._navigateClassHierarchy()` . This happens to be exactly what the `then` -branch does anyway. So far, so good.

