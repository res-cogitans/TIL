package com.programmers.java.functional.talker;

import com.programmers.java.functional.dictionary.EnglishGreeting;

class TooMuchTalker implements Talker {

    @Override
    public void talk(Object x) {
        System.out.println(x);
    }
}
