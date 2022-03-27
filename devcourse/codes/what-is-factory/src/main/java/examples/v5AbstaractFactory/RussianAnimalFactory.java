package examples.v5AbstaractFactory;

public class RussianAnimalFactory implements AnimalFactory {

    @Override
    public Cat createCat() {
        Cat cat = new RussianBlue();
        cat.eyeColor = "blue";
        cat.furColor = "grey";
        cat.furPattern = "mono";
        cat.size = "small";
        return cat;
    }

    @Override
    public Bear createBear() {
        Bear bear = new BrownBear();
        bear.furColor = "brown";
        return bear;
    }
}
