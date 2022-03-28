package com.programmers.java;

public class HelloWorld {

    public static void main(String[] args) {
        String greeting1 = "Hello, World!";
        String greeting2 = "Hello, World!";
        System.out.println("greeting1==greeting2: " + greeting1==greeting2);

        System.out.println(greeting1);

        String s = "";
        for (int i = 0; i < 10; i++) {
            s += i;
        }
        System.out.println("s1 = " + s);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
        System.out.println("sb = " + sb);
    }
}
