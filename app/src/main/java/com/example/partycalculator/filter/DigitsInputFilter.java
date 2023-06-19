package com.example.partycalculator.filter;

import android.text.InputFilter;
import android.text.Spanned;

public class DigitsInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            char c = source.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}