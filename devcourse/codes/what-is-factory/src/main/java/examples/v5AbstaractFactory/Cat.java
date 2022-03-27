package examples.v5AbstaractFactory;

public abstract class Cat {

    public String eyeColor;
    public String furColor;
    public String furPattern;
    public String size;

    public void scratch() {
        // 각 고양이들이 하는 기본적인 긁기 행동, 재정의할 수 있다.
        System.out.println("그르릉 그르릉");
    }
}
