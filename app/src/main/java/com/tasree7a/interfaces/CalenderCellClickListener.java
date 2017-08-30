package com.tasree7a.interfaces;

import android.view.View;

import org.joda.time.LocalDate;

/**
 * Created by Khalid Taha on 2/9/16.
 * This is for click events on CustomGridCalendar events
 */
public interface CalenderCellClickListener {
    void onItemClick(LocalDate date, View v);
}
