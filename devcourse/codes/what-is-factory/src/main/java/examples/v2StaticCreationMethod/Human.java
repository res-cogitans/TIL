package examples.v2StaticCreationMethod;

public class Human {

    private boolean andThenThereWereNone = false;

    public Human bornWithCry() {
        if (andThenThereWereNone) {
            System.out.println("그리고 아무도 없었다.");
            return null;
        }
        cry();
        return new Human();
    }

    public Human bornWithSilence() {
        if (andThenThereWereNone) {
            System.out.println("그리고 아무도 없었다.");
            return null;
        }
        andThenThereWereNone = true;
        return new Human();
    }

    private static void cry() {
        System.out.println("응애!");
    }

}