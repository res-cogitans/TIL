# The Principles of OOD

객체지향 디자인의 원칙들

http://www.butunclebob.com/ArticleS.UncleBob.PrinciplesOfOod

------


What is object oriented design? What is it all about? What are it's benefits? What are it's costs? It may seem silly to ask these questions in a day and age when virtually every software developer is using an object oriented language of some kind. Yet the question is important because, it seems to me, that most of us use those languages without knowing why, and without knowing how to get the the most benefit out of them.

무엇이 객체지향 디자인이고, 어떤 이익이 있으며, 그 대가는 무엇인가? 거의(virtually) 모든 소프트웨어 개발자가 어떤 객체지향 언어를 사용하고 있는 시대에 저런 질문을 던지는 것은 어리석어 보일 수 있겠다. 하지만 객체지향 언어를 사용하는 우리들 중 대부분이 왜 객체지향 언어를 사용하는지 그리고 어떻게 해야 객체지향 언어의 이점을 최대한 얻을 수 있는가에 대해서 모르는 것 처럼 내게는 보이기에, 이 질문은 중요하다.

Of all the revolutions that have occurred in our industry, two have been so successful that they have permeated our mentality to the extent that we take them for granted. Structured Programming and Object Oriented Programming. All of our mainstream modern languages are strongly influenced by these two disciplines. Indeed, it has become difficult to write a program that does not have the external appearance of both structured programming and object oriented programming. Our mainstream languages do not have **goto**, and therefore appear to obey the most famous proscription of structured programming. Most of our mainstream languages are class based and do not support functions or variables that are not within a class, therefore they appear to obey the most obvious trappings of object oriented programming.

우리 산업(개발)에서 일어난 모든 혁신들에 대해서 두 가지가 너무도 성공적이라, 우리 머리 속에 너무도 강하게 침투해버려서 우리는 그것들을 당연한 것으로 여긴다. 구조적 프로그래밍(Structured Programming)과 객체지향 프로그래밍(Object Oriented Programming)이 그것이다. 우리가 사용하는 현대의 주류 언어들은 이 둘에 강하게 영향을 받는다. 구조적 프로그래밍과 객체 지향 프로그래밍의 외관을 둘 다 갖지 않는 프로그램을 작성하기가 어려워졌다. 우리의 주류 언어들은 **goto**문을 갖지 않고, 그래서 구조적 프로그래밍의 가장 유명한 규정을 따르는 것처럼 보인다. 우리의 주류 언어 대부분은 클래스 기반이며 클래스 안에 있지 않은 함수나 변수들을 지원하지 않는다. 그렇기에 객체지향 프로그래밍의 가장 명백한 요소(trappings, 부정적인 어감)를 따르는 것처럼 보인다.

Programs written in these languages may look structured and object oriented, but looks can be decieving. All too often today's programmers are unaware of the principles that are the foundation of the disciplines that their languages were derived around. In another blog I'll discuss the principles of structured programming. In this blog I want to talk about the principles of object oriented programming.

이러한 언어들로 작성된 프로그램들은 구조화되어있고, 객체지향적으로 보일지도 모른다. 하지만 그렇게 보인다는 것은 겉 보기에만 그런 것일 수 있다. 오늘날 프로그래머들은 그 언어들이 유래한 원칙들의 기초에 대해서 너무도 종종 무지하다. 다른 블로그에서 나는 구조화된 프로그래밍의 원칙들에 대해서 논의할 것이고, 이 블로그에서는 객체지향 프로그래밍의 원칙들에 대해서 이야기하고자 한다.

In March of 1995, in comp.object, I wrote an [article](http://tinyurl.com/84emx) that was the first glimmer of a set of principles for OOD that I have written about many times since. You'll see them documented in my [PPP](https://www.amazon.com/Software-Development-Principles-Practices-Paperback/dp/B011DBKELY) book, and in many articles on [the objectmentor website](http://www.objectmentor.com/), including a well known [summary](http://www.objectmentor.com/resources/articles/Principles_and_Patterns.pdf).

1995년 3월, comp.object에서 나는 내가 지금까지 여러 번 작성해온, 객체지향 디자인의 원칙들에 대한 글의 초안을 하나 작성했었다. 내 PPP 책에서 그 내용을 볼 수 있고, objectmentor 사이트의 유명한 summary 글을 포함한 많은 글들에서 그 내용을 볼 수 있다.

These principles expose the dependency management aspects of OOD as opposed to the conceptualization and modeling aspects. This is not to say that OO is a poor tool for conceptualization of the problem space, or that it is not a good venue for creating models. Certainly many people get value out of these aspects of OO. The principles, however, focus very tightly on dependency management.

개념화와 모델링이라는 관점들에 반하여, 이 원칙들은 객체지향 디자인의 의존성(dependency) 관리라는 관점들을 드러낸다. 이는 객체지향이 문제 공간에 대한 개념화 도구로서 안 좋다거나, 모델을 생성하기에 좋은 장이 되지 못함을 의미하지 않는다. 분명 많은 사람들은 그런 관점들에 대해서 객체지향의 가치를 찾아낼 것이다. 하지만, 내가 말하려는 원칙들은 의존성 관리에 강하게 집중한다.

Dependency Management is an issue that most of us have faced. Whenever we bring up on our screens a nasty batch of tangled legacy code, we are experiencing the results of poor dependency management. Poor dependency managment leads to code that is hard to change, fragile, and non-reusable. Indeed, I talk about several different *design smells* in the PPP book, all relating to dependency management. On the other hand, when dependencies are well managed, the code remains flexible, robust, and reusable. So dependency management, and therefore these principles, are at the foudation of the *-ilities* that software developers desire.

의존성 관리는 우리들 중 대부분이 맞닥뜨린 문제다. 뒤섞인 레거시 코드들의 더러운 묶음을 화면에 띄울 때마다, 우리는 형편 없는 의존성 관리의 결과를 경험하고 있다. 형편 없는 의존성 관리는 변경하기 어렵고, 허약하고, 재사용 불가능한 코드로 우리를 인도한다. 내가 PPP 책에서 말했던 몇몇의(several) *디자인 냄새*들은 다 의존성 관리에 관한 것이다. 한편 의존성들이 잘 관리되었을 때, 코드는 유연하고, 강건하며, 재사용 가능한 것이 된다. 따라서 의존성 관리 그리고 이 원칙들은 소프트웨어 개발자들이 바라는 *특성(-ilities)*의 기초라 할 수 있다.

The first five principles are principles of *class design*. They are:

첫 다섯 원칙들은 *클래스 디자인*에 관한 것이다. 그것들은:

| 약어    | 명칭                                                         | 설명                                                         |
| ------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **SRP** | The Single Responsibility Principle<br />단일 책임 원칙      | *A class should have one, and only one, reason to change.*<br />*하나의 클래스에는 변화에 대한 단 하나의 이유(reason)만이 있어야 한다.* |
| **OCP** | The Open Closed Principle<br />개방 폐쇄 원칙                | *You should be able to extend a classes behavior, without modifying it.*<br />*클래스의 동작(behavior)을 변경하지 않으면서 확장할 수 있어야 한다.* |
| **LSP** | The Liskov Substitution Principle<br />리스코프 치환 원칙    | *Derived classes must be substitutable for their base classes.*<br />*파생(Derived) 클래스들은 반드시 기반(base) 클래스들을 대체할 수 있어야(substitutable) 한다.* |
| **ISP** | The Interface Segregation Principle<br />인터페이스 분리 원칙 | *Make fine grained interfaces that are client specific.*<br />*인터페이스들을 곱게 갈아서(Make fine grained) 클라이언트가 특정할 수 있게 하라.* |
| **DIP** | The Dependency Inversion Principle<br />의존관계 역전 원칙   | *Depend on abstractions, not on concretions.*<br />*구체화(concretions)가 아니라 추상(abstractions)에 의존하라.* |

The next six principles are about packages. In this context a package is a binary deliverable like a .jar file, or a dll as opposed to a namespace like a java package or a C++ namespace.

다음 여섯 원칙들은 패키지들에 대한 것이다. 이 맥락에서 패키지란 .jar 파일같은 binary deliverable 이거나 자바 패키지나 C++ 네임스페이스 같은 네임스페이스와 구별되는 dll이다.

The first three package principles are about package *cohesion*, they tell us what to put inside packages:

패키지에 대한 첫 다섯 원칙들은 패키지 *응집도*에 관한 것이다. 이 원칙들은 패키지 안에 무엇을 넣을지 알려준다:

| 약어    | 명칭                                                         | 설명                                                         |
| ------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **REP** | The Release Reuse Equivalency Principle<br />릴리즈 재사용 동치성 원칙 | *The granule of reuse is the granule of release.*<br />*한 알(granule)을 재사용하는 것은 한 알의 릴리즈다.* |
| **CCP** | The Common Closure Principle<br />공통 폐쇄 원칙             | *Classes that change together are packaged together.*<br />*함께 변경되는 클래스들은 함께 패키징된다.* |
| **CRP** | The Common Reuse Principle<br />공통 재사용 원칙             | *Classes that are used together are packaged together.*<br />*함께 사용되는 클래스들은 함께 패키지된다.* |


The last three principles are about the couplings between packages, and talk about metrics that evaluate the package structure of a system.

마지막 세 원칙은 패키지들간의 응징도에 대한 것이며, 시스템의 패키지 구조를 평가하는 기준이다.

| 약어    | 명칭                                                       | 설명                                                         |
| ------- | ---------------------------------------------------------- | ------------------------------------------------------------ |
| **ADP** | The Acyclic Dependencies Principle<br />비순환 의존성 원칙 | *The dependency graph of packages must have no cycles.*<br />*패키지의 의존성 그래프에는 순환이 없어야만 한다.* |
| **SDP** | The Stable Dependencies Principle<br />안정적 의존성 원칙  | *Depend in the direction of stability.*<br />*안정적 방향으로 의존하라.* |
| **SAP** | The Stable Abstractions Principle<br />안정적 추상화 원칙  | *Abstractness increases with stability.*<br />*추상성은 안정성과 함께 증가한다.* |


You can find articles describing all these principles in the *Old Articles* section of cleancoder.com.

이 원칙들에 대한 글들은 cleancoder.com의 *Old Article* 섹션에서 볼 수 있다.