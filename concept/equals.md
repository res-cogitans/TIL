# InteliJ의 `equals()`와 `hashCode()` 오버라이딩에 대한 안내문

generate equals() and hashCode()

- [ ] Accept subclasses as parameter to equals() method

While generally incompliant to Object.equals() specification accepting subclasses might be nessesary for generated method to work correctly with frameworks, which generate Proxy subclasses like Hibernate.

생성된 메서드가 하이버네이트처럼 프록시 서브클래스들을 만드는 프레임워크들과 호환시키고 싶으시다면,
일반적인 Object.equals() 규격과 다르게 서브클래스들을 포함시키는 것이 필요할 수 있습니다.

- [x] Use getters during code generation
- 해당 클래스에 getter 만들지 않으면 당연히 위 옵션을 체크하더라도 getter 메서드 사용하지 않고 구현된다.



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
