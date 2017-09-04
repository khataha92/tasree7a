package com.tasree7a.Enums;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Khalid on 8/1/17.
 * Types of CalendarDayViewHolder used in CalendarMonthAdapter to represent a day cell
 * Contains methods to customize a cell with specific dates
 */

public enum CalendarDayViewType {

    // Empty cell without a day, blank
    EMPTY_DAY(0),

    // Normal day typical cell
    NORMAL_DAY(1),

    // Cell within selection
    IN_SELECTION_DAY(2),

    // Cell within marker dates interval
    IN_MARKERS_INTERVAL_DAY(3);



    private int value;

    private static Map<Integer, CalendarDayViewType> map = new HashMap<>();

    static {
        for (CalendarDayViewType type : CalendarDayViewType.values()) {
            map.put(type.value, type);
        }
    }

    CalendarDayViewType(int i) {

        value = i;

    }

    public int getValue() {
        return value;
    }

    public static CalendarDayViewType valueOf(int value) {
        return map.get(value);
    }


}
