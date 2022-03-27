package examples.v4FactoryMethodPattern;

public class KoreanShortHairCreator implements Creator {
    @Override
    public Cat createCat(String type) {
        Cat cat = new KoreanShortHair();
        cat.eyeColor = "blue";
        cat.furColor = "grey";
        cat.furPattern = "mono";
        cat.size = "small";
        return cat;
    }
}
