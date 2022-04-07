package com.programmers.java.immutable;

import java.util.HashMap;
import java.util.Map;

public class ValueClass {

    private static Map<Integer, ValueClass> instancePool = new HashMap<>();

    private int value;

    private ValueClass(int value) {
        this.value = value;
    }

    public static ValueClass getInstance(int value) {
        if (instancePool.containsKey(value)) {
            return instancePool.get(value);
        }
        else {
            ValueClass valueClass = new ValueClass(value);
            instancePool.put(value, valueClass);
            return valueClass;
        }
    }
}