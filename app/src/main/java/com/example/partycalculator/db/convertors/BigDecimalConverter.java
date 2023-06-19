package com.example.partycalculator.db.convertors;

import androidx.room.TypeConverter;

import java.math.BigDecimal;

public class BigDecimalConverter {
    @TypeConverter
    public BigDecimal fromString(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    @TypeConverter
    public String toString(BigDecimal value) {
        return value == null ? null : value.toString();
    }
}
