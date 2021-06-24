# Padding bit

원출처: [https://stackoverflow.com/questions/58435348/what-is-bit-padding-or-padding-bits-exactly](https://stackoverflow.com/questions/58435348/what-is-bit-padding-or-padding-bits-exactly)

---

The gist of it is they are "wasted" space. I say "wasted" because while having padding bits makes the object bigger, it can make working with the object much easier (which means faster) and the small space waste can generate huge performance gains. In some cases it is essential because the CPU can't handle working with objects of that size.

핵심을 말하자면 패딩 비트는 "낭비된" 공간이라고 할 수 있다. "낭비된"이라고 말한 것은 패딩 비트를 갖는 것은 그 object를 크게 만들었기 때문이다. 이는 그 object를 더 쉽게(즉 더 빠르게) 다룰 수 있게 한다. (패딩비트로 인한) 작은 공간 낭비가 큰 퍼포먼스 이득을 취할 수 있게끔 해준다. 몇몇 경우에는 패딩비트가 근본적으로 필요한 것일 수 있는데, CPU가 다룰 수 없는 크기의 object일 경우가 그렇다.

Lets say you have a struct like (all numbers are just an example, different platforms can have different values):

자, 아래와 같은 구조체를 생각해보자. (아래의 모든 수치는 그저 예시일 뿐으로, 플랫폼에 따라 다른 값을 가질 수 있다.)

```
struct foo
{
    short a; // 16 bits
    char  b; // 8 bits
};

```

and the machine you are working with reads 32 bits of data in a single read operation. Reading a single foo is not a problem since the entire object fits into that 32 bit chunk. What does become a problem is when you have an array. The important thing to remember about arrays is that they are contiguous, there is no space between elements. It's just one object immediately followed by another. So, if you have an array like

당신이 다루고 있는 기계가 단일 연산 당 32비트의 데이터를 읽어들이는 상황이다. foo 하나를 읽어들이는 것은 문제가 아니다. 전체 object가 32비트 청크에 들어맞기 때문이다.  진짜 문제가 되는 경우는 배열이 있는 경우다. 배열에 대해 기억할 점은 배열은 인접해있고(contiguous) 배열 원소 사이에 공간이 없다는 것이다. 한 object가 다른 것에 바로 이어져 있는 것이다. 따라서, 다음과 같은 배열에서

```
foo array[10]{};

```

With this the first `foo` object is in a 32 bit bucket. The next element of the array though will be in the first 32 bit bucket and the second 32 bit bucket. This means that the member `a` is in two separate buckets. Some processors can do this (at a cost) and other processors will just crash if you try to do this. To solve both those problems the compiler will add padding bits to the end of `foo` to pad out it's size. This means foo actually becomes

이 첫번째 foo object(*array[0]으로 추정)*는 32비트 버킷안에 들어간다. 배열의 다음 원소는 첫번째 32비트 버킷과 2번째 32비트 버킷 둘 다에 들어갈 것이다. 이는 멤버변수 a가 두 서로 다른 버킷에 들어가게 된다는 의미다.  *(구조체배열 array의 두 번째 원소 array[1]의 멤버변수 a(16비트)는 array[0]이 24비트를 차지하기 때문에 첫 번째 읽는 단위에 8비트, 두 번째 읽는 단위에 8비트 남는 식으로 잘리게 된다는 의미로 보인다.)* 몇몇 프로세서들은 (어떤 대가를 치루고(at a cost)) 이렇게 할 수 있겠고, 다른 프로세스들은 바로 크래쉬 될 것이다. 이 문제 둘을 다 해결하려고 컴파일러가 패딩 비트를 foo의 끝에 더해, 이 크기를 pad 하는 것이다.  이는 foo를 실제로는 다음과 같이 되게끔 한다.

```
struct foo
{
    short a; // 16 bits
    char  b; // 8 bits
    char  _; // 8 bits of padding
};

```

And now it is easy for the processor to handle `foo` objects by themselves or in an array. It doesn't need to do any extra work and you've only added 8 bits per object. You'd need a lot of objects for that to start to matter on a modern machine.

그리고 이제 프로세서가 foo object들을, 그것들 자체건 간에 그 배열(foo array) 속에서건 간에 다루는 것이 간단해진다. 어떤 추가적 작업이 필요하지도 않으며, 그저 8비트씩 object에 붙였을 뿐이다. 오늘날의 기계에서는 object들에 대해 그 만큼의 많은 패딩 비트들이 필요하겠다. *(32비트 단위 연산을 수행하는 오늘날 CPU에서 그런 패딩비트를 이용한 공간늘리기가 필요하다는 의미로 보임.)*

There is also times where you need padding between members of the type because of unaligned access. Lets say you have

unaligned access로 인하여 그 형(type)의 멤버변수들 사이에 패딩 비트를 필요로 하는 상황들도 있다. 다음을 보라.

```
struct bar
{
    char c; // 8 bits
    int  d; // 32 bits
};

```

Now `bar` is 40 bits wide and `d` more often then not will be stored in two different buckets again. To fix this the compiler adds padding bits between `c` an `d` like

자 bar는 40비트 크기이며 d는 두 다른 버켓에 담기지 않을 것이다. 이를 해결하기 위해서 컴파일러는 c와 d 사이에 패딩 비트를 다음과 같이 추가한다.

```
struct bar
{
    char    c; // 8 bits
    char _[3]; // 24 bits
    int     d; // 32 bits
};

```

and now `d` is guaranteed to go into a single 32 bit bucket.

자 이제 d는 단일한 32비트 버켓 안에 들어가게끔 보장되었다.

- aligned access에 관해 참조:[http://egloos.zum.com/wonchuri/v/2127834](http://egloos.zum.com/wonchuri/v/2127834)