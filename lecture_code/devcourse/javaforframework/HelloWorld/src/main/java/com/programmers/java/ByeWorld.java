package com.programmers.java;
class CustomInt {
    int value;
}

public class ByeWorld {

    public static void main(String[] args) {
        // 이것은 주석입니다.

//        여러 줄을 동시에
//                주석 처리해봅시다.

        /*
            여러 줄로 주석 표현하기
         */

        int a = 100;

        ByeWorld bye = new ByeWorld();

        bye.doubled(a);

        CustomInt b = new CustomInt();
        b.value = 100;

        bye.doubled(b);

        System.out.println("a = " + a);
        System.out.println("b.value = " + b.value);

        System.out.println("Good Bye, Cruel World!!!");
    }

    private void doubled(int a) {
        a *= 2;
    }

    private void doubled(CustomInt a) {
        a.value *= 2;
    }
}
