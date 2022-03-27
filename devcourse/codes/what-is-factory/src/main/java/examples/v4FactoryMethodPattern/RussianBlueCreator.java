package examples.v4FactoryMethodPattern;

public class RussianBlueCreator implements Creator {
    @Override
    public Cat createCat(String type) {
        Cat cat = new RussianBlue();
        cat.eyeColor = "blue";
        cat.furColor = "grey";
        cat.furPattern = "mono";
        cat.size = "small";
        return cat;
    }
}
