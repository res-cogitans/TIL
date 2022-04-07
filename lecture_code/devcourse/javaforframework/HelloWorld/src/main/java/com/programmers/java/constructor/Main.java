package com.programmers.java.constructor;

public class Main {

    public static void main(String[] args) {
        Son son = new Son();
        System.out.println("=================================");
        Father father = new Father();
        System.out.println("=================================");
        Father sonDeclaredFather = new Son();
        System.out.println("=================================");
    }
}
