package examples.v4FactoryMethodPattern;

public interface Creator {

    public default Cat birthOfCat(String type) {
        cry();
        return this.createCat(type);
    }

    Cat createCat(String type);

    private void cry() {
        // 모든 종류의 고양이들이 생성될 때 공통적으로 하는 일
        System.out.println("야옹! 야옹!");
    }

}
