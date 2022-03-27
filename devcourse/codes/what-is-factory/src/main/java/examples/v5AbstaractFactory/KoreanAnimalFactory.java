package examples.v5AbstaractFactory;

public class KoreanAnimalFactory implements AnimalFactory {

    @Override
    public Cat createCat() {
        Cat cat = new KoreanShortHair();
        cat.eyeColor = "blue";
        cat.furColor = "grey";
        cat.furPattern = "mono";
        cat.size = "small";
        return cat;
    }

    @Override
    public Bear createBear() {
        Bear bear = new HalfMoonBear();
        bear.furColor = "black";
        return bear;
    }
}
