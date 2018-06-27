
package com.tasree7a.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 7/4/17.
 */

public enum SortType {

    DISTANCE(2),RATING(1),PRICE(3);


    private int value;

    SortType(int val){

        value = val;
    }

    public int getValue() {
        return value;
    }

    private static Map<Integer, SortType> map = new HashMap<>();

    static {

        for (SortType type : SortType.values()) {

            map.put(type.value, type);

        }
    }

    public static SortType valueOf(Integer value) {

        return map.get(value);

    }
}
