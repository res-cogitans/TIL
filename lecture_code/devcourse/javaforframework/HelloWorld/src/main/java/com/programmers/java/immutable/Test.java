package com.programmers.java.immutable;

public class Test {

    public static void main(String[] args) {

        ValueClass instance1 = ValueClass.getInstance(50);
        ValueClass instance2 = ValueClass.getInstance(50);
        ValueClass instance3 = ValueClass.getInstance(100);

        System.out.println("instance1 = " + instance1);
        System.out.println("instance2 = " + instance2);
        System.out.println("instance3 = " + instance3);

        System.out.println("instance1 == instance2:  "
                + (instance1 == instance2));
        System.out.println("instance2 == instance3:  "
                + (instance2 == instance3));
    }
}
