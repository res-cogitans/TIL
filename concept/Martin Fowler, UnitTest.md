# [UnitTest](https://martinfowler.com/bliki/UnitTest.html)

5 May 2014 (https://martinfowler.com/)

[Martin Fowler](https://martinfowler.com/)

[TEST CATEGORIES](https://martinfowler.com/tags/test categories.html)

[EXTREME PROGRAMMING](https://martinfowler.com/tags/extreme programming.html)

Unit testing is often talked about in software development, and is a term that I've been familiar with during my whole time writing programs. Like most software development terminology, however, it's very ill-defined, and I see confusion can often occur when people think that it's more tightly defined than it actually is. [[1\]](https://martinfowler.com/bliki/UnitTest.html#footnote-history)

단위 테스트는 소프트웨어 개발에 있어서 종종 논의되며, 제가 지금까지 프로그램들을 작성해온 내내 친숙했던 개념이다. 하지만, 다른 소프트웨어 개발 전문용어들처럼 이 용어는 매우 잘못 정의되어 있다. 나는 이 개념을 이 개념이 실제 그런 것보다 더 빡빡하게 정의되어있다고 혼동하는 것을 많이 볼 수 있었다.

![img](https://martinfowler.com/bliki/images/unitTest/sketch.png)



Although I'd done plenty of unit testing before, my definitive exposure was when I started working with Kent Beck and used the [Xunit](https://martinfowler.com/bliki/Xunit.html) family of unit testing tools. (Indeed I sometimes think a good term for this style of testing might be "xunit testing.") Unit testing also became a signature activity of [ExtremeProgramming](https://martinfowler.com/bliki/ExtremeProgramming.html) (XP), and led quickly to [TestDrivenDevelopment](https://martinfowler.com/bliki/TestDrivenDevelopment.html).

비록 내가 많은 단위 테스트를 했었지만, 내가 켄트 백과 함께 일 하기 시작하고 Xunit 계통의 단위 테스트 도구들을 사용하게 되었을 때가, 내가 결정적으로 단위 테스트에 노출되었을 때다. (사실 나는 가끔씩 이런 식의 테스트 방식을 "xunit 테스트"라는 용어로 부르는 것이 아닌가 하고 생각한다.) 단위 테스트는 또한 익스트림 프로그래밍(XP)의 상징적인 활동이 되었으며, 테스트 주도 개발로 빠르게 이끌었다. 

There were definitional concerns about XP's use of unit testing right from the early days. I have a distinct memory of a discussion on a usenet discussion group where us XPers were berated by a testing expert for misusing the term "unit test." We asked him for his definition and he replied with something like "in the first morning of my training course I cover 24 different definitions of unit test."

올바르게 단위 테스트를 하고자 하는 XP의 주요 관심사는 초기부터 있어 왔다. 유스넷에서 우리 XPer들이 "단위 테스트"라는 용어를 잘못 사용한다는 것에 대해 어떤 테스트 전문가에게 비난받았던, 유스넷에서의 토론이 명확하게 기억 난다. 우리는 그에게 그는 단위 테스트를 어떻게 정의하는가를 물었고, 그는 "내 교육과정의 첫날에, 나는 단위테스트에 대한 서로 다른 24가지 정의를 다룬다"는 식의 답변을 했었다.

Despite the variations, there are some common elements. Firstly there is a notion that unit tests are low-level, focusing on a small part of the software system. Secondly unit tests are usually written these days by the programmers themselves using their regular tools - the only difference being the use of some sort of unit testing framework [[2\]](https://martinfowler.com/bliki/UnitTest.html#footnote-dev-write). Thirdly unit tests are expected to be significantly faster than other kinds of tests.

차이에도 불구하고 거기에는 어떤 공통점이 있다. 첫째로 단위 테스트는 소프트웨어 시스템의 작은 부분에 집중하는, low-level이라는 생각이 있다. 둘째로 단위 테스트는 보통 프로그래머들 자신이 그들의 일반적인 도구를 사용하여 작성된다.  유일한 차이는 어떤 종류의 단위 테스트 프레임워크를 사용하느냐다. 셋째로 단위 텐스트는 다른 종류의 테스트들보다는 확연히 빨라야 한다.

So there's some common elements, but there are also differences. One difference is what people consider to be a *unit*. Object-oriented design tends to treat a class as the unit, procedural or functional approaches might consider a single function as a unit. But really it's a situational thing - the team decides what makes sense to be a unit for the purposes of their understanding of the system and its testing. Although I start with the notion of the unit being a class, I often take a bunch of closely related classes and treat them as a single unit. Rarely I might take a subset of methods in a class as a unit. However you define it doesn't really matter.

자, 몇몇 공통점들이 있지만, 차이점들도 있다. 그 중 하나는 무엇을 *단위(Unit)*이라고 여기느냐다. 객체 지향 디자인은 한 클래스를 단위로 다루려는 성향이 있고, 절차적 혹은 함수적 접근은 하나의 함수를 단위로 여길 것이다. 하지만 이는 정말로 상황에 따른 것이다. 무엇이 단위인것이 이치에 맞는가는 전체 시스템과 그 테스트에 대한 이해라는 목적에 걸맞는지에 따라 팀에서 결정하는 것이다. 비록 내가 단위를 하나의 클래스라고 보는 생각에서 출발하지만, 나는 종종 서로 연관된 여러 클래스들을 묶어서 그것들을 단일한 단위로 다룬다. 가끔씩 나는 클래스의 subset of methods를 하나의 단위로 다룬다. 하지만 당신이 그걸 어떻게 정의하느냐는 정말로 중요치 않다.



## Solitary or Sociable?

동떨어진 것인가, 사회적인 것인가

A more important distinction is whether the unit you're testing should be sociable or solitary [[3\]](https://martinfowler.com/bliki/UnitTest.html#footnote-solitary). Imagine you're testing an order class's price method. The price method needs to invoke some functions on the product and customer classes. If you like your unit tests to be solitary, you don't want to use the real product or customer classes here, because a fault in the customer class would cause the order class's tests to fail. Instead you use [TestDoubles](https://martinfowler.com/bliki/TestDouble.html) for the collaborators.

당신이 테스트하는 단위가 사회적이어야 하는지 혹은 동떨어져야 하는지가 더 중요한 구별이다. 주문 클래스의 가격 메서드를 테스트 한다고 생각해보라. 가격 메서드는 상품과 소비자 클래스들의 몇몇 함수들을 호출할 필요가 있다. 만약 단위 테스트가 동떨어지기를 원한다면 진짜 상품이나 소비자 클래스를 여기서 사용하고 싶지는 않을 것이다. 왜냐면 소비자 클래스의 문제가 주문 클래스의 테스트를 실패하게 만들 테니까. 대신에 당신은 TestDoubles를 협력자 대신에 사용할 것이다.

![img](https://martinfowler.com/bliki/images/unitTest/isolate.png)



But not all unit testers use solitary unit tests. Indeed when xunit testing began in the 90's we made no attempt to go solitary unless communicating with the collaborators was awkward (such as a remote credit card verification system). We didn't find it difficult to track down the actual fault, even if it caused neighboring tests to fail. So we felt allowing our tests to be sociable didn't lead to problems in practice.

하지만 모든 단위 테스트를 하는 사람들이 동떨어진 단위 테스트를 하는 것은 아니다. xunit 테스트가 90년대에 시작되었을 때 우리는 협력자들의 소통이 어색하지 않는 한 (원격 신용 카드 검증 시스템 같은) 동떨어지게 하려고 시도하지 않았다. 우리는 심지어 그것이 관련 테스트를 실패하게 했어도, 실제 문제 지점을 추적하는 데 어려움을 겪지 않았다. 그래서 우리는 우리의 테스트들이 사회적일 수 있게 허용하는 것이 실제 문제로 이끌지 않을 것이라고 느꼈다.

Indeed using sociable unit tests was one of the reasons we were criticized for our use of the term "unit testing". I think that the term "unit testing" is appropriate because these tests are tests of the behavior of a single unit. We write the tests assuming everything other than that unit is working correctly.

사회적인 단위 테스트를 사용한 것은 우리가 "단위 테스트"라는 용어를 사용한 것에 대해 비평한 이유 중 하나였다. "단위 테스트"라는 용어는 이러한 테스트들이 하나의 단위의 행동에 대한 테스트이기 때문에 그렇다고 나는 생각했다. 그 단위가 올바르게 작동하는 것이라는 것 의외에 어떤 것도 상정하지 않고 우리는 테스트를 작성했다.

As xunit testing became more popular in the 2000's the notion of solitary tests came back, at least for some people. We saw the rise of Mock Objects and frameworks to support mocking. Two schools of xunit testing developed, which [I call the classic and mockist styles](https://martinfowler.com/articles/mocksArentStubs.html). One of the differences between the two styles is that mockists insist upon solitary unit tests, while classicists prefer sociable tests. Today I know and respect xunit testers of both styles (personally I've stayed with classic style).

2000년대에 xunit 테스트가 더 유명해지면서, 동떨어진 테스트의 개념이 되돌아왔다, 최소한 몇몇 사람들에게는 말이다. 우리는 mocking을 돕는 Mock 객체들과 프레임워크들의 부흥을 보았다. xunit 테스트의 두 학파가 발전했는데, 내가 고전적인 방식, 그리고 mock 방식이라 부르는 것이다. 그 두 방식의 차이 중 하나는 mokist는 동떨어진 단위 테스트를 주장하며 반면에 고전파는 사회적 테스트를 선호한다는 것이다. 오늘날 나는 양 쪽 스타일의 xunit 테스터들 모두를 알고 또 존중한다. (개인적으로 나는 고전 스타일에 남아 있다.)

Even a classic tester like myself uses test doubles when there's an awkward collaboration. They are invaluable to remove [non-determinism when talking to remote services](https://martinfowler.com/articles/nonDeterminism.html#RemoteServices). Indeed some classicist xunit testers also argue that any collaboration with external resources, such as a database or filesystem, should use doubles. Partly this is due to non-determinism risk, partly due to speed. While I think this is a useful guideline, I don't treat using doubles for external resources as an absolute rule. If talking to the resource is stable and fast enough for you then there's no reason not to do it in your unit tests.

나와 같은 고전 테스터들은 어색한 협력이 있을 때 test doubles를 사용한다. 

## Speed

The common properties of unit tests — small scope, done by the programmer herself, and fast — mean that they can be run very frequently when programming. Indeed this is one of the key characteristics of [SelfTestingCode](https://martinfowler.com/bliki/SelfTestingCode.html). In this situation programmers run unit tests after any change to the code. I may run unit tests several times a minute, any time I have code that's worth compiling. I do this because should I accidentally break something, I want to know right away. If I've introduced the defect with my last change it's much easier for me to spot the bug because I don't have far to look.

When you run unit tests so frequently, you may not run all the unit tests. Usually you only need to run those tests that are operating over the part of the code you're currently working on. As usual, you trade off the depth of testing with how long it takes to run the test suite. I'll call this suite the **compile suite**, since it's what I run whenever I think of compiling - even in an interpreted language like Ruby.

If you are using Continuous Integration you should run a test suite as part of it. It's common for this suite, which I call the **commit suite**, to include all the unit tests. It may also include a few [BroadStackTests](https://martinfowler.com/bliki/BroadStackTest.html). As a programmer you should run this commit suite several times a day, certainly before any shared commit to version control, but also at any other time you have the opportunity - when you take a break, or have to go to a meeting. The faster the commit suite is, the more often you can run it. [[4\]](https://martinfowler.com/bliki/UnitTest.html#footnote-pipeline)

Different people have different standards for the speed of unit tests and of their test suites. [David Heinemeier Hansson](http://david.heinemeierhansson.com/2014/slow-database-test-fallacy.html) is happy with a compile suite that takes a few seconds and a commit suite that takes a few minutes. [Gary Bernhardt](https://www.destroyallsoftware.com/blog/2014/tdd-straw-men-and-rhetoric) finds that unbearably slow, insisting on a compile suite of around 300ms and [Dan Bodart](http://dan.bodar.com/2012/02/28/crazy-fast-build-times-or-when-10-seconds-starts-to-make-you-nervous/) doesn't want his commit suite to be more than ten seconds

I don't think there's an absolute answer here. Personally I don't notice a difference between a compile suite that's sub-second or a few seconds. I like Kent Beck's rule of thumb that the commit suite should run in no more than ten minutes. But the real point is that your test suites should run fast enough that you're not discouraged from running them frequently enough. And frequently enough is so that when they detect a bug there's a sufficiently small amount of work to look through that you can find it quickly.

## Notes

**1:** I wrote a little about the historical roots of the name in the entry for [IntegrationTest](https://martinfowler.com/bliki/IntegrationTest.html)

**2:** I say "these days" because this is certainly something that has changed due to XP. In the turn-of-the-century debates, XPers were strongly criticized for this as the common view was that programmers should never test their own code. Some shops had specialized unit testers whose entire job would be to write unit tests for code written earlier by developers. The reasons for this included: people having a conceptual blindness to testing their own code, programmers not being good testers, and it was good to have a adversarial relationship between developers and testers. The XPer view was that programmers could learn to be effective testers, at least at the unit level, and that if you involved a separate group the feedback loop that tests gave you would be hopelessly slow. Xunit played an essential role here, it was designed specifically to minimize the friction for programmers writing tests.

**3:** Jay Fields [came up with the terms](https://leanpub.com/wewut) "solitary" and "sociable"

**4:** If you have tests that are useful, but take longer than you want the commit suite to run, then you should build a [DeploymentPipeline](https://martinfowler.com/bliki/DeploymentPipeline.html) and put the slower tests in a later stage of the pipeline.

## Revisions

Updated on Oct 24 2014 to include a mention of Field's sociable/solitary vocabulary.

Updated on March 9 2017 to make the solitary/sociable terms the primary way of describing the distinction and removing the use of the term "collaborator isolation" (due to confusion with isolating test fixture changes from each other).