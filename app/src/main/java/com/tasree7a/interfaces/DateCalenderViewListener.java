package com.tasree7a.interfaces;

import org.joda.time.LocalDate;

/**
 * Created by mohammad on 5/3/15.
 * This is call back for date changes in CalenderFragment
 */
public interface DateCalenderViewListener {

    void onDateChanged(final LocalDate checkInDate);

    void onError(String error);
}
