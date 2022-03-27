package examples.v5AbstaractFactory;

public class Client {

    private AnimalFactory factory;

    public Client() {
        this.factory = Config.readConfig();
    }

    public void callAnimals() {
        Bear bear = factory.createBear();
        Cat cat = factory.createCat();

        bear.growl();
        cat.scratch();
    }
}
