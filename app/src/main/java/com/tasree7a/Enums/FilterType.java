package com.tasree7a.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 7/5/17.
 */

public enum FilterType {

    FAVORITE(1),AVAILABLE(2),ALL(3);

    private int value;

    FilterType(Integer val){

        value = val;
    }

    public int getValue() {
        return value;
    }

    static Map<Integer, FilterType> map = new HashMap<>();

    static {

        for (FilterType type : FilterType.values()) {

            map.put(type.value, type);

        }
    }

    public static FilterType valueOf(Integer i){

        return map.get(i);
    }
}
