package examples.v5AbstaractFactory;

public class Config {

    public static AnimalFactory readConfig() {
        return new KoreanAnimalFactory();
    }
}
