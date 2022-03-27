package examples.v2StaticCreationMethod;

public class Cat {

    public void sleep() {
        System.out.println("Zzz");
    }

    public static Cat createCat(String type) {
        if (type.equalsIgnoreCase("Korean")) {
            return new KoreanShortHair();
        }
        if (type.equalsIgnoreCase("russian")) {
            return new RussianBlue();
        }
        throw new IllegalArgumentException("유효하지 않은 고양이 타입입니다.");
    }
}
