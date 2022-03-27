package examples.v1CreationMethod;

public class Number {

    private int value;

    public Number(int givenValue) {
        somethingHappen();
        this.value = givenValue;
    }

    public Number next() {
        somethingHappen();
        return new Number(this.value+1);
    }

    public Number previous() {
        somethingHappen();
        return new Number(this.value-1);


    }

    private void somethingHappen() {
        // 잘 모르겠지만 무언가 일어나고 있습니다 ..
    }
}