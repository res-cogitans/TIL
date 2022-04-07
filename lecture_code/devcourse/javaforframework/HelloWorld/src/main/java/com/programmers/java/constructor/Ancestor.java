package com.programmers.java.constructor;

public abstract class Ancestor implements Family {

    @Override
    public void run() {

    }

    Ancestor() {
        System.out.println("추상 조상님");
    }
}
