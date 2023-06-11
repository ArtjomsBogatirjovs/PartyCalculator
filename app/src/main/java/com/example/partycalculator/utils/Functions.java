package com.example.partycalculator.utils;

public abstract class Functions {
    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return ((String) value).isEmpty();
        }
        return value.toString().isEmpty();
    }
}
