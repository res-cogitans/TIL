package com.programmers.java.functional;

import com.programmers.java.functional.dictionary.Dictionary;
import com.programmers.java.functional.dictionary.mapper.IntegerToStringMapper;
import com.programmers.java.functional.dictionary.LambdaDictionary;
import com.programmers.java.functional.dictionary.mapper.Mapper;
import com.programmers.java.functional.dictionary.mapper.StringToDoubleMapper;
import com.programmers.java.functional.talker.LambdaTalker;
import com.programmers.java.functional.talker.Talker;

public class Main {

    static void saySomething(Class<? extends Talker> aClass) {
        System.out.println(aClass);
    }

    static void functionalInterfaceExecution() {
        new Talker() {
            @Override
            public void talk(Object x) {
                System.out.println(this.getClass() + " said: " + x.toString());
                System.out.println(this.getClass() + " said: " + x.toString());
                System.out.println(this.getClass() + " said: " + x.toString());
            }
        }.talk(new Dictionary() {
            @Override
            public String supply() {
                return "Hello, World! From " + this.getClass();
            }
        }.supply());

        new Dictionary() {
            @Override
            public String supply() {
                return "Hello, World!";
            }
        }.supply();

//        new Talker() {
//            @Override
//            public void talk() {
//                saySomething(this.getClass());
//            }
//        }.talk();
//
//        new Talker() {
//            @Override
//            public void talk() {
//                saySomething(this.getClass());
//            }
//        }.talk();
//
    }

    public static void main(String[] args) {

        LambdaDictionary lambdaDictionary =
                (language) -> "An " + language + " speaker say the hello in this way: Hello!";
        LambdaTalker lambdaTalker =
                (greeting) -> System.out.println(greeting + " I am talker");
        lambdaTalker.talk(lambdaDictionary.supply("English"));

//        Mapper mapper1 = (s) -> s.length();

        Mapper mapper1 = Integer::valueOf;
        IntegerToStringMapper mapper2 = Double::valueOf;
        StringToDoubleMapper mapper3 =
                (x) -> mapper2.map(mapper1.map("12"));

        LambdaTalker lambdaTalker2 =
                (x) -> lambdaDictionary.supply("English");
    }
}
