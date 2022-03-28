package com.programmers.java.functional;

@FunctionalInterface
interface MyMap {
    void map();     // static/default 메서드가 있어도 추상 메서드가 하나라면 함수형 인터페이스

    default void sayHello() {
        System.out.println("Hello, World!");
    }

    static void sayBye() {
        System.out.println("Good Bye!");
    }
}