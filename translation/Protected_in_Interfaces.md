#  Protected in Interfaces

- https://stackoverflow.com/questions/5376970/protected-in-interfaces

Why are all methods in an `interface` definition implicitly `public`? Why does it not allow a `protected` method?



Because an interface is supposed to mean "what you can see from outside the class". It would not make sense to add non-public methods.

인터페이스는 "클래스 바깥에서 무엇을 볼 수 있는지"를 의미하도록 의도되었기 때문이다. public 하지 않은 메서드를 추가하는 것은 무의미할 것이다.



Although the often quoted reason is that "interfaces define public APIs", I think that is an over-simplification. (And it "smells" of circular logic too.)

비록 "인터페이스는 public API들을 정의한다"는 것이 종종 인용된 이유긴 하다. 하지만 내 생각에 그건 과도한 단순화(over-simplification)가 아닐까 싶다. (그리고 순환 논리의 "냄새가 나기도" 한다.)

It would not be meaningless to have interfaces that have a mixture of access modifiers; e.g. partly public and partly restricted to other classes in the same package as the interface. In fact, in some cases this could be down-right useful, IMO.

부분적으로는 public이고, 부분적으로는 동일 패키지의 다른 클래스들에 제약되어 있는 식으로 하는, 접근 제한자를 혼합해서 가지고 있는 인터페이스를 갖는 것이 무의미하지는 않을 것이다. 사실 몇몇 경우에 이 방식은 솔직한 말로(down-right) 유용하다고 나는 생각한다.

Actually, I think that the part of *reasoning* behind making members of an interface implicitly public is that *it makes the Java language simpler*:

사실, 나는 인터페이스의 멤버들을 public하게끔 만드는 것에 숨어 있는 생각은, 이런 방식으로 자바 언어를 더 단순하게 만든다는 것이 아닐까 싶다.

- Implicitly public interface members are simpler for programmers to deal with. How many times have you seen code (classes) where the method access modifiers were chosen seemingly at random? A lot of "ordinary" programmers have difficulty understanding how best to manage Java abstraction boundaries<sup>[1](#footnote_1)</sup>. Adding public/protected/package-private to interfaces makes it even harder for them.
- 인터페이스 멤버들을 public으로 강제하는 것은 프로그래머들이 다루기에 더 쉽다. 메서드 접근 제한자가 보기에 무작위하게 선택된 코드(클래스들)을 얼마나 많이 봤었나? 수 많은 "평범한" 프로그래머들은 자바의 추상 영역을 어떻게 가장 잘 다룰 수 있는가를 이해하는 데 있어서 어려움을 겪는다.<sup>[1 번역](#footnote_trans1)</sup> public/protected/package-private를 인터페이스에 더하는 것은 그들에게 더 어려울 것이다.
- Implicitly public interface members simplify the language specification ... and hence the task for Java compiler writers, and the folks who implement the Reflection APIs.
- 인터페이스 멤버들을 public하게 강제하는 것은 이 언어의 명세(specification)를... 그리고 자바 컴파일러 라이터를, 그리고 리플렉션 API를 구현하려는 사람들을 단순하게 만든다.

The line of thinking that the "interfaces define public APIs" is arguably a consequence (or characteristic) of the simplifying language design decision ... not the other way around. But in reality, the two lines of thought probably developed in parallel in the minds of the Java designers.

"인터페이스는 public API들을 정의한다"는 식의 사고는 언어를 단순화하려는 디자인 결정의 결과라고 주장할 수 있겠다... 다른 방식이 아니라 말이다. 하지만 실제로는 자바 디자이너들의 머리 속에서 두 가지 방식의 사유가 평행적으로 발달되어 왔다.

At any rate, the official response to the RFE in [JDK-8179193](https://bugs.openjdk.java.net/browse/JDK-8179193) makes it clear that the Java design team decided<sup>[2](#footnote_2)</sup> that allowing `protected` on interfaces adds complexity for little real benefit. Kudos to @skomisa for [finding the evidence](https://stackoverflow.com/a/59171193/139985).

어쨌든, JDK-8179193에서 RFE에게 공식으로 답한 것을 보면, 자바 디자인 팀이 `protected`를 인터페이스에 추가할 수 있도록 허용하는 것은 아주 적은 이익을 얻기 위해 복잡성을 더하는 것이라고 결정했다고<sup>[2 번역](#footnote_trans2)</sup> 하는 점이 명확해진다. Kudos가 @skomisa에게 finding the evidence에서 답한 것을 보라.

The evidence in the RFE settles the issue. That is the official reason why that has not been added.

RFE에서의 단서는 이 문제, 왜 그것[접근제한자]가 더해지지 않았는가에 대한 공식적인 근거를 해명한다.



<a name="footnote_1">1</a>  Of course, top-gun programmers have no difficulty with these things, and may welcome a richer palette of access control features. But, what happens when their code is handed over to someone else to maintain?

<a name="footnote_trans1">1 번역</a>  물론, 최상급(top-gun) 프로그래머들은 이런 것들을 어려워하지 않고, 접근 제한자가 더 풍부해지는 것을 반길 것이다. 하지만 그들이 짠 코드가 다른 이들이 유지보수하게끔 맡겨진다면 어떤 일이 일어나겠나?

<a name="footnote_2">2</a>  You may disagree with their decision or their stated reasoning but that is moot.

<a name="footnote_trans2">2 번역</a>  물론 당신은 그들의 결정에 동의하지 않을 수도 있고, 그들이 주장한 생각이 논쟁적이라고 볼 수도 있다.



# [Protected methods in interfaces: share across packages](https://bugs.openjdk.java.net/browse/JDK-8179193)

A DESCRIPTION OF THE REQUEST :

요청에 대한 설명:

Since modifiers are a bit limited in Java, a way to share methods across packages is restricted to public methods. Sometimes it is dangerous to make a method public, but it needs to be because of the lack of proper modifiers. My solution overcomes this limitation.

자바에서 제어자(modifiers)들은 약간 한정되어 있기에, 패키지를 가로질러서 메서드들을 공유하는 것은 public 메서드들로만 제약되어 있다. 메서드를 public으로 생성하는 것은 가끔씩 위험하지만, 적절한 제어자가 없기 때문에 그런 방식으로 만들게 된다. 이 제약에 대한 내 해결책이다.

The java language specification doesn't currently allow the protected modifier for interface methods. We can take advantage of this fact and use protected for interface methods for this new feature.

자바 언어 명세에서는 protected 제어자를 인터페이스 메서드들에는 현재 허용하지 않고 있다. 우린 이 점에서 착안하여, protected 인터페이스 메서드들을 사용하여 이득을 볼 수 있다.

If an interface method is marked protected and the interface is implemented by a class in another package, the method would not need to be public, but could also be private or at least package protected. The method is visible, what ever the class declares it to be and additionally visible in the source package of the interface (and sub packages?).

만약 인터페이스 메서드가 protected이며, 그 인터페이스가 다른 패키지의 클래스에서 구현된다면, 메서드는 public 하지 않고, private하거나 최소한 package protected일 수 있다. 그 클래스에서 이 메서드를 어떻게 선언하건 간에 이 메서드는 보일것이며, 추가적으로 인터페이스의 패키지에서도 (그리고 서브 패키지들에서도?) 볼 수 있다.

This way we could share certain methods across well known packages.

이 방식으로 우리는 특정 메서드들을 잘 알고 있는 패키지들을 가로질러 공유할 수 있다.

JUSTIFICATION :

설명:

There needs to be a way to make methods visible to other packages other than making it public.

public으로 만들지 않고서도 다른 패키지에서 메서드들을 볼 수 있게 만들 방법이 필요하다.

Public methods, that are not meant to be used outside the API, should not pollute the API. The suggestion is a clean way to overcome this limitation.

API 바깥에서 사용되도록 의도하지 않은 public 메서드들은 API를 오염시켜선 안 된다. 이 제안은 이 제약을 넘어설 깨끗한 방안이다.



Comments

답변

This proposal attempts to solve a problem in a way that adds complexity and special cases for little actual gain. A typical way to solve this problem is to have a private class that implements a public interface. The implementation methods are public, but are within a private class, so they remain private.

이 제안은 복잡성과 특수 케이스들을 더해서 아주 조금의 실질적 이득을 얻는 방식으로 문제를 해결하고자 시도한다. 이 문제를 해결하는 전형적인 방식은 public 인터페이스를 구현한 private 클래스를 만드는 것이다. 이 구현 메서드들은 public이지만 private 클래스 안에 있기 때문에, private를 유지한다.

An alternative available starting in Java 9 is to make classes and methods public, but within a module that has a qualified-export to specific "friend" modules instead of being exported to the general public.

자바 9에서부터 가능한 대안은 클래스와 메서드들을 public하게 만들되, general public하게 export되지 않고, 특정 "friend"모듈들에만 qualified export하는 모듈 속에 만들면 된다. 

Closing as Won't Fix.

수정하지 않는 것으로 종결됨.