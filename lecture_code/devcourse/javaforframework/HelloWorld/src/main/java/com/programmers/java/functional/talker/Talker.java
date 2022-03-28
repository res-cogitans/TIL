package com.programmers.java.functional.talker;

@FunctionalInterface
public interface Talker {
    void talk(Object x);     // 추상 메서드가 하나밖에 없는 인터페이스
}