## 초기화 블록Initialization Block

```java
class BlockData {
    static int classVar = 1;    // 클래스 명시적 초기화
    int instanceVar = 2;        // 인스턴스 명시적 초기화
    static {
        classVar = 3;           // 클래스 초기화 블록
        System.out.println("클래스 초기화 블록");
    }
    {
        instanceVar = 4;         // 인스턴스 초기화 블록
        System.out.println("인스턴스 초기화 블록");
    }

    public BlockData() {
        classVar = 5;           // 생성자 초기화
        instanceVar = 6;
        System.out.println("생성자 실행");
    }
}

public class InitializationBlock {

    public static void main(String[] args) {
        System.out.println("---------------------------------------");

        System.out.println("BlockData.classVar = " + BlockData.classVar);

        BlockData blockData = new BlockData();

        System.out.println("인스턴스 생성");

        System.out.println("BlockData.classVar = " + BlockData.classVar);
        System.out.println("blockData.instanceVar = " + blockData.instanceVar);

        System.out.println("---------------------------------------");


    }
}

```

- 초기화 블록의 경우 **생성자보다 먼저 호출된다.**

  - 인스턴스 초기화 블록은 `static`이 붙은 초기화 블록이며,

    클래스를 처음 사용할 때 단 한 번만 호출된다.

- 실행 순서는

  - 클래스 초기화 블록 -> 인스턴스 초기화 블록 -> 생성자 초기화 순서다.

  - 클래스 초기화 블록은 처음 한 번만 호출된다.



### 잡담: 스프링의 경우도 적용된다

- (당연한 얘기지만) 스프링도 생성자 호출하기에 컴포넌트 스캔 등으로 빈 등록을 한다고 해도 생성자 호출이 이루어지기 전에 블록이 호출된다.
  - 별개로, 어차피 스프링 빈은 싱글턴이기 때문에 초기화 블록을 쓰나 인스턴스 초기화 블록을 쓰나 마찬가지일 것.
  - 아래는 예제로 사용한 코드다. 애플리케이션 실행 시 멤버서비스 클래스 초기화 블록 실행"이 먼저 나오는 것을 볼 수 있다.

```java
@Component
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    static {
        System.out.println("멤버서비스 클래스 초기화 블록 실행");
    }
    
    public MemberServiceImpl(MemberRepository memberRepository) {
        System.out.println("멤버서비스(컴포넌트 스캔으로 등록) 생성자");
        this.memberRepository = memberRepository;
    }
    ...
}
```



### String 클래스의 COMPACT_STRINGS

```java
    static final boolean COMPACT_STRINGS;

    static {
        COMPACT_STRINGS = true;
    }
```

- `String`클래스는 `COMPACT_STRINGS`도 초기화 블록 방식으로 등록되었다.