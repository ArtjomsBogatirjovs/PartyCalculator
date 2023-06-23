package com.example.partycalculator.filter;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilter implements InputFilter {
    private static StringBuilder s = new StringBuilder();

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder builder = new StringBuilder();

        for (int i = start; i < end; i++) {
            char c = source.charAt(i);
            if (Character.isDigit(c) || (c == '.')) {
                builder.append(c);
            }
        }

        return builder.toString();
    }
}
