package examples.v2StaticCreationMethod;

public class God {

    private static final God GOD = new God();

    private God() {
        System.out.println("아브라함이 있기 전에 내가 있었다.");
    }

    public static God pray() {
        return GOD;
    }
}
