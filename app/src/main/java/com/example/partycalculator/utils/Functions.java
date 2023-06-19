package com.example.partycalculator.utils;

import com.example.partycalculator.entity.Party;
import com.example.partycalculator.entity.PartySingleton;

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

    public static Party getParty() {
        return PartySingleton.getInstance().getParty();
    }

    public static long getPartySysId() {
        return PartySingleton.getInstance().getParty().getSysId();
    }
}
