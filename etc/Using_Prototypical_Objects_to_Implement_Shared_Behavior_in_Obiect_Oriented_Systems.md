# Using Prototypical Objects to Implement Shared Behavior in Object Oriented Systems
**Henry Lieberman**



## Abstract

A traditional philosophical controversy between representing general concepts as abstract *sets* or *classes* and representing concepts as concrete *prototypes* is reflected in a controversy between two mechanisms for sharing behavior between objects in object oriented programming languages.

*Inheritance* splits the object world into *classes*, which encode behavior shared among a group of *instances*, which represent individual members of these sets.

The class/instance distinction is not needed if the alternative of using *prototypes* is adopted.

A prototype represents the default behavior for a concept, and new objects can re-use part of the knowledge stored in the prototype by saying how the new object differs from the prototype.

The prototype approach seems to hold some advantages for representing default knowledge, and incrementally and dynamically modifying concepts.
*Delegation* is the mechanism for implementing this in object oriented languages.

After checking its idiosyncratic behavior, an object can forward a message to prototypes to invoke more general knowledge.

Because class objects must be created before their instances can be used, and behavior can only be associated with classes, inheritance fixes the communication patterns between objects at instance creation time.

Because any object can be used as a prototype, and any messages can be forwarded at any time, delegation is the more flexible and general of the two techniques.

- 집합론과 원형 이론 사이의 철학적 논쟁은, OOP에 있어 행위를 공유하는 방식(mechanism)에도 반영됨
  - 집합론은 상속을 기반으로 세계를 클래스로 나눈다. 클래스는 인스턴스 사이에 공통된 행위를 공통 코드로 만든다.
    - 클래스는 인스턴스보다 먼저 있어야 하며, 행위란 클래스와 연관된 것이기에 객체 간의 소통 방식을 인스턴스 생성 시점에 바꾼다.
  - 원형 이론에 기반하면, 원형은 그 개념의 기본적인 행동을 보여준다. 다른 객체들은 그 원형에서 달라지는 부분을 규정함으로써, 나머지 부분을 재사용할 수 있다.
    - 이 방식은 점진적 변화하는 개념 표현에 있어서 유용성을 갖는다.
    - OOP에서는 위임(delegation)이 이것을 구현하는 방식이다.
    - 원형과 다른 부분을 사용한 후에 원형에 나머지 공통 부분을 호출하게끔 전달한다(fowarding).
    - 모든 객체는 원형일 수 있으며 언제건 전달이 가능하기에 위임은 (상속보다) 더 유연하다.
  - 영국인 러셀이 보편자에 기대고, 대륙의 비트겐슈타인이 유명론적인 주장을 한다는 것은 재미있는 포인트(단순화할 순 없긴 하지만)



## 1. Sets vs. prototypes: a philosophical dilemma with practical consequences 
How do people represent knowledge about generalizations they make from experience with concrete situations? 

Philosophers concerned with the theory of knowledge have debated this question, but as we shall see, the issue is not without practical consequences for the task of representing knowledge in object oriented systems. 

Because much of object oriented programming involves constructing representations of objects in the real world, our mechanisms for storing and using real world knowledge get reflected in mechanisms for dealing with objects in computer languages.

We'll examine how the traditional controversy between representing concepts as sets versus representing concepts as prototypes gives rise to two mechanisms, *inheritance* and *delegation*, for sharing behavior between related objects in object oriented languages. 



When a person has experience in a particular situation, say concerning a particular elephant named Clyde, facts about Clyde can often prove useful when encountering another elephant, say one named Fred. 

If we have mental representations of a concept for Clyde, and a concept for Fred, the question then becomes:

How do the representations of Clyde and Fred share knowledge?

How can we answer questions, such as Fred's color, number of legs. size. etc. by reference to what we already know about Clyde? 

In the absence of any mechanism for sharing knowledge between related concepts, we'd have to repeat all the knowledge about Clyde in a representation of Fred.



There are two points of view we can consider adopting.

The first is based on the idea of abstract sets. 

From learning about Clyde, we can construct a concept of the *set [or class] of elephants*, which abstracts out what we believe is true about all individual animals sufficiently similar to Clyde to be called elephants.

The description of the set can enumerate all the "essential" properties of elephants.

We can view Clyde as a member or instance of this class.

In an object oriented system, the set approach involves creating an object to represent the set of elephants, and establishing a link representing the membership relation between the object representing Clyde and the set object.

Since the description of the set represents what is true about all its members, we can answer questions about Clyde by referring to the description of the set.

Establishing the same kind of membership link between Fred and the set of elephants enables Fred and Clyde to share some of the same knowledge.

If Fred and Clyde share some additional properties, such as that of being Indian elephants, that are not shared by some other elephants, these can be embodied in a *subclass* object, which shares all the properties of the elephant set, adjoining the additional properties relevant to India.



But there's an alternative point of view.

We can consider Clyde to represent the concept of a prototypical elephant.

If I ask you to "think of an elephant", no doubt the mental image of some particular elephant will pop to mind, complete with the characteristics of gray color, trunk, etc.

If Clyde was the elephant most familiar to you, the prototypical elephant might be an image of Clyde himself.

If I ask you a question such as "How many legs does an elephant have?", a way to answer the question is to assume that the answer is the same as how many legs Clyde has, unless there's a good reason to think otherwise.

The concept of Fred can have a connection marking its prototype as Clyde, as a mechanism for sharing information between the two weighty pachyderms.

The description of Fred can store any information that is unique to Fred himself.

If I ask "How many legs does Fred have?', you assume the answer is the same for Fred as for Clyde, in the absence of any contrary evidence.

If you then learn that Fred is a three-legged elephant, that knowledge is stored with Fred and is always searched before reference to the prototype is made.



## 2. Prototypes have advantages for incremental learning of concepts

Thought the concept of a set has proven fruitful in mathematics, the prototype approach in some ways corresponds more closely to the way people seem to acquire knowledge from concrete situations. 

The difficulty with sets stems from their abstractness; people seem to be a lot better at dealing with specific examples first, then generalizing from them than they are at absorbing general abstract principles fast, and later applying them in particular cases.

Prototype systems allow creating individual concepts first, then generalizing them by saying what aspects of the concept are allowed to vary.

Set-oriented systems require creating the abstract description of the set first, before individual instances can be installed as members. 



In mathematics, sets are defined either by enumerating their members, or by describing the unifying principles that identify membership in the set.

We can neither enumerate all the elephants, nor are we good at making definitive lists of the essential properties of an elephant.

Yet the major impetus for creating new concepts always seems to be experience with examples.

If Clyde is our only experience with elephants, our concept of an elephant can really be no different than the concept of Clyde.

After meeting other elephants, the analogies we make between concepts like Fred and Clyde serve to pick out the important characteristics of elephants.



Prototypes seem to be better at expressing knowledge about defaults.

If we assert grayness as one of the identifying characteristics of membership in the set of elephants, we can't say that there are exceptional white elephants without risking contradiction.

Yet it is easy to say that Fred, the white elephant, is just like Clyde, except that he is white.

As Wittgenstein observed, it is difficult to say, in advance, exactly what characteristics are essential for a concept.

It seems that as new examples arise, people can always make new analogies to previous concepts that preserve some aspects of the "defaults" for that concept and ignore others. 

- 객체의 보편자를 규정하는 것보다, 개별 객체의 개체차, 종차를 규정해나가는 방식이 점진적 개념 구성에 있어서 편리하다.



## 3. Inheritance implements sets, delegation implements prototypes 
Having set the stage with our philosophical discussion of the issues of concept representation, we turn now to how these issues affect the more mundane details of implementation of object oriented programming systems.



Implementing the set-theoretic approach to sharing knowledge in object oriented systems is traditionally done by a mechanism called *inheritance*, first pioneered by the language Simula, later adopted by Smalltalk, flavors and Loops, among others.

An object called a class encodes common behavior for a set of objects.

A class also has a description of what characteristics are allowed to vary among members of the set.

Classes have the power to generate instance objects, which represent members of a set.

All instances of a class share the same behavior, but can maintain unique values for a set of state variables predeclared by the class.

To represent Clyde, you create a description for the class elephant, with an instance variable for the elephant's name, values of which can be used to distinguish Clyde and Fred.

A class can give rise to *subclasses*, which add additional variables and behavior to the class.



Implementing the prototype approach to sharing knowledge in object oriented systems is an alternative mechanism called *delegation*, appearing in the actor languages, and several Lisp-based object oriented systems, such as Director [Kalm 79], T [Rees 85], Orbit [Steels 82], and others.

Delegation removes the distinction between classes and instances.

Any object can serve as a prototype.

To create an object that shares knowledge with a prototype, you construct an extension object, which has a list containing its prototypes, which my be *shared* with other objects, and personal behavior idiosyncratic to the object itself.

When an extension object receives a message, it first attempts to respond to the message using the behavior stored its personal part.

If the object's personal characteristics are not relevant for answering the message, the object forwards the message on to the prototypes to see if one can respond to the message.

This process of forwarding is called *delegating* the massage.

Fred the elephant would be an extension object that stored behavior unique to Fred in its personal part, and referenced the prototype Clyde in its shared part. 



## 4. Tools for representing behavior and internal state are the building blocks of object oriented systems 
Each object oriented system must provide some linguistic mechanisms for defining the behavior of objects.

The philosophy of object oriented programming is to use the object representation to encode both the procedures and data of conventional languages.

Rather than define the procedural behavior or the data content of an object all at once, it is convenient to break both aspects of an object into a set of parts that can be accessed or modified individually by name.



An object's internal state consists of variables or acquaintances, which can be accessed in most object oriented systems by sending the object a message consisting of the variable's name.

- 여기서 acquaintances는 객체 참조처럼 받아들이면 될 듯

An object's procedure for responding to messages [in actors, we say its *script*] can be composed of a set of procedures called *methods*, each of which is specialized for handling only a certain subset of the messages the object receives, identified by name.

Breaking up an object's state into named variables means that different portions of the state can be modified incrementally, without affecting the others.

- 객체의 상태를 서로 다른 이름을 가진 변수들로 쪼갰기에 상태의 일부분은 다른 상태와 별개로 점진적으로 변화할 수 있음.

Breaking up an object's behavior into named methods means that different portions of the behavior can be modified incrementally, without affecting the others.

The language must then provide ways of combining groups of methods and variables to form objects, and some means of allowing an object to share behavior [implemented as methods and variables] residing in previously defined objects.

We will call these composite objects *extensions*.

These building blocks are represented in the illustration "Tools for sharing knowledge", with "icons" to be used in further discussion.

![tools_for_sharing_knowledge](..\img\tools_for_sharing_knowledge.png)



Many object oriented languages supply primitive linguistic mechanisms for creating objects with methods, variables and extensions.

An alternative approach, which is advocated in the actor formalism, is to define methods, variables and extensions as objects in their own right, with their behavior determined by a message passing protocol among them.

- 객체들 사이의 메시지 전달 프로토콜을 기반으로 정의하는 방식: actor formalism의 방식

Obviously, an object representing a method cannot itself have methods, otherwise infinite recursion would result.

- 메서드를 나타내는 객체는 메서드를 가질 수 없음: 무환 순환 문제

Using simple objects primitive to the system, a variable is defined to be an object that remembers a name and a value, and responds to access and modification messages.

- 기본 객체(자바에서는 기본 자료형)

A method responds only to those messages for which it is designed, rejecting others.

Extension objects use delegation to forward messages from one part of the object to another to locate the appropriate response. 



Everyone who is already convinced of the utility of object oriented programming shouldn't have much trouble discerning the advantages of using object oriented programming in the implementation of the knowledge sharing mechanisms.

Foremost among them is the ability to define other kinds of objects which implement alternatives to the standard versions.

- 객체의 변형을 구현하는 능력이 핵심

Instead of an ordinary variable, one might like to have "active" variables that take action when changed, "read-only" variables, maybe even "write-only" variables, each of which could be defined as a different type of variable object.

- 다양한 변수
  - 변화가 발생했을 때 작동하는 active 변수
  - 읽기 전용 변수
  - 쓰기 전용 변수

Alternative kinds of method objects can use differing strategies to combine behavior from contributing components, replacing the so-called "method combination" feature of the flavors system, and making "multiple inheritance" easier.

Different kinds of extension objects can make different efficiency tradeoffs on the issue of copying versus sharing.



The mechanisms for sharing knowledge in object oriented languages have now grown so complicated that it is impossible to reach universal consensus on the best mechanism.

Using object oriented programming itself to implement the basic building blocks of state and behavior is the best approach for allowing experimentation and coexistence among competing formalisms. 



## 5. A Logo example illustrates the differences between delegation and inheritance 
An example from the domain of Logo turtle graphics will illustrate how the choice between delegation and inheritance effects the control and data structures in an object oriented system.

The delegation approach is illustrated in the figure titled "Sharing Knowledge with Delegation".

The first thing we would like to do is create an object representing a pen, which remembers a location on the screen, and can be moved to a different location, drawing lines between the old and new locations.

![sharing_knowledge_with_delegation](..\img\sharing_knowledge_with_delegation.png)



We start out by creating a prototypical pen object, which has a specific location on the screen x=200, y=50, and behavior to respond to the draw message.

When we would like to create a new pen object, we need only describe what's different about the new pen from the first one, in this case the x variable. Since the y is the same and behavior for the draw message is the same, these need not be replaced.



The `draw` method will have to use the value of the x variable, and it's important that the correct value of x is used. 
When the draw method is delegated from the new pen to the old pen, even though the draw method of the original pen is invoked, it should be the x of the new pen that is used.

- (100, 200)의 펜은 `draw`에 있어서 변화가 없기 때문에 (50, 100) 펜의 `draw`를 호출한다.
  - 하지만 이 경우 (50, 100) 펜의 `draw`가 호출됨에도 불구하고, `draw` 메서드에서 사용되는 x값은 새 펜(100)의 것이어야 한다.



To insure this, whenever a message is delegated, it must also pass along the object that originally received the message.
This is called the SELF variable in Simula, Smalltalk and flavors, although I find the term "self" a little misleading, since a method originally defined for one kind of object often winds up sending to a "self' of a different kind.

In actor terminology, this object is called the client, since the object being delegated to can be thought of as performing a service for the original object.

- 프로토타입에게 위임하는 방식에서
  - 호출되는 프로토타입 객체는 호출한 (변형) 객체에 대해서 서비스를 수행한다는 면에서
  - 호출한 (변형) 객체는 클라이언트라 할 수 있음

When a pen delegates a draw message to a prototypical pen, it is saying "I don't know how to handle the draw message.

I'd like you answer it for me if you can, but if you have any further questions, like what is the value of my x variable, or need anything done, you should come back to me and ask."

If the message is delegated further, all questions about the values of variables or requests to reply to messages are all referred to the object that delegated the message in the first place. 



Suppose now we'd like to create a `turtle` at the same location as the original pen, using the original pen as a prototype.

How is a `turtle` different from a pen?

A turtle shares some of the behavior of a pen, but has additional state, namely it's `heading`.

Remembering a heading is essential in implementing the additional behavior of being able to respond to `forward` and `back` messages by relying on the behavior of the response to the draw message.

We may choose either to provide a new behavior for the turtle's draw operation, or rely on the draw operation provided by the original pen. 

- `heading`을 상태로 갖는 거북이의 `draw`를 새 행동으로 만들 것인지? 아니면 기존의 `draw` 연산에 기댈 것인지?



Let's look at the same example with the inheritance approach to sharing knowledge as found in Simula and Smalltalk, instead of delegation.

This is illustrated in the figure titled "*Sharing knowledge with inheritance*".

![sharing_knowledge_with_inheritance](..\img\sharing_knowledge_with_inheritance.png)

With inheritance, it is necessary to create objects representing classes.

To make a pen, it is first necessary to make a pen class object, which specifics both the behavior and the names of variables. 



Individual pens are created by supplying values for all the instance variables of the pen class, creating an instance object.

Values for all the variables must be specified, even if they do not have unique values in the instance.

No new behavior may be attached to an individual pen.

- 클래스-상속 기반으로 문제를 해결할 경우 개별 객체에 행동이 붙을 수는 없음

Extending behavior is accomplished by a different operation, that of creating a new subclass.

- 행동을 확장하는 것은 서브클래스를 만드는 방식으로 이루어짐

The step which goes from a instance to behavior stored in its class is performed by a "hard-wired" lookup loop in systems like Simula and Smalltalk, not by message passing, as in the delegation approach. 

- 위임 방식: 메세지 전달을 이용해서 새로운 행동을 수행
- 상속 방식: "hard-wired" lockup loop를 이용해서 새 행동을 수행



To extend pens with new behavior, we must first create a new class object.

Here a `turtle class` adds a new variable heading along with new behavior for the forward message.

Notice that the variables from the pen class, x and y, were copied down into the turtle class.

An individual turtle instance must supply values for all the variables of its class, superclass, and so on.

- 자손 클래스는 조상 클래스의 변수를 모두 가지고 있음

This copying leads to larger instance objects for classes further and further down the inheritance hierarchy.

- 상속 계층도 아래로 갈수록 점점 비대한 인스턴스 객체를 만들게 될 것

The lookup of methods, performed by a primitive, unchangeable routine instead of message passing, starts a search for methods in the class of an object, and proceeds up the subclass-to-superclass chain.



How does a method inherited from the pen class to the turtle class access a method implemented in the turtle class?

Since inheritance systems  usually do  not  use  message passing to communicate from subclass to superclass, they can't pass the turtle object along in the message, as we would in delegation.

- 상속 방식 사용시 상위 클래스에 정의된 메서드를 사용하는 경우 메시지에 하위 클래스 객체 자체를 넘길 수 없음



Instead, most use variable binding to bind a  special variable `self`  to the object that originally receives a  message.

We shall see later on that this leads to trouble.



In addition, inheritance systems also allow the "shortcut" of 
binding all the variables of an  instance so that  they can  be 
referenced  dix~fly  by  code  running  in  methods  as  free 
variables.  While this is sometimes more efficient, it short- 
circuits  the  message  passing  mechanism,  defeating  the 
independence of internal representation which is the hallmark 
of object oriented programming.  Since variable references 
use  different  linguistic  syntax  than  message  sends,  ff  we 
wanted to change the coordinate representation from x  and y 
to  polar coordinates using  rho  and  theta,  we'd  have  to 
change  all  the  referencing  methods.  Sticking  to  message 
passing to access x  and y  means that even if the coordinates 
were changed to polar, we could still provide methods that 
compute the rectangular coordinates from the polar, and the 
change would be transparent. 