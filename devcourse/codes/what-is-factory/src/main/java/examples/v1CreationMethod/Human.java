package examples.v1CreationMethod;

public class Human {

    public Human bornWithCry() {
        cry();
        return new Human();
    }
    private void cry() {
        System.out.println("응애!");
    }
}