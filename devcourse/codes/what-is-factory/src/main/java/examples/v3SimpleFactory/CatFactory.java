package examples.v3SimpleFactory;

import examples.v4FactoryMethodPattern.Cat;

public class CatFactory {

    public static Cat createCat(String type) {
        Cat cat = null;
        cry();
        if (type.equalsIgnoreCase("Korean")) {
            cat = new KoreanShortHair();
            cat.eyeColor = "yellow";
            cat.furColor = "white and black";
            cat.furPattern = "cow";
            cat.size = "large";
            return cat;
        }
        if (type.equalsIgnoreCase("russian")) {
            cat = new RussianBlue();
            cat.eyeColor = "blue";
            cat.furColor = "grey";
            cat.furPattern = "mono";
            cat.size = "small";
            return cat;
        }
        throw new IllegalArgumentException("유효하지 않은 고양이 타입입니다.");
    }

    private static void cry() {
        //모든 고양이는 태어날 때 "야~~~옹!" 하고 웁니다.
        System.out.println("야~~~옹!");
    }
}
