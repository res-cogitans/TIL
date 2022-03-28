package com.programmers.java;

import java.io.PrintStream;

public class LearningNullClass {

    class CustomClass {
        int value;


        void run() {
            System.out.println("instance method");
        }
    }

    public static void main(String[] args) {

        char[] array = null;
        String s = null;
        System.out.println("s = " + s);   // 정상작동
        System.out.println("System.out = " + System.out);

        System.out.println("array = " + array);

//        System.out.println("s.getClass() = " + s.getClass()); // NPE!
//
//        CustomClass customClass = null;
//        customClass.run(); // NPE!
//        System.out.println("customClass = " + customClass); // 정상작동
//
//        System.out.println("customClass.toString() = " + customClass.toString());   // NPE!!
//        System.out.println("customClass.getClass() = " + customClass.getClass());
//        System.out.println("customClass.getClass() + @  = " + customClass.getClass() + "@");
//        System.out.println("customClass = "
//                + customClass.getClass().getName() + "@" + Integer.toHexString(customClass.hashCode()));    // NPE!
        
    }

    static void methodCallFromNull() {
        CustomClass customClass = null;
        customClass.run();
    }
}
