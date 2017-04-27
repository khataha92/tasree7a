package com.tasree7a.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohammad on 4/19/15.
 */
public enum FontsType {
    BOOK(0), HEAVY(1), BLACK(2), MEDIUM(3), ROMAN(4);

    private int value;

    FontsType(int i) {

        value = i;

    }

    public int getValue() {
        return value;
    }

    private static Map<Integer, FontsType> map = new HashMap<>();

    static {
        for (FontsType type : FontsType.values()) {
            map.put(type.value, type);
        }
    }

    FontsType(Integer i) {

        value = i;

    }

    public static FontsType valueOf(Integer value) {
        return map.get(value);
    }
}

