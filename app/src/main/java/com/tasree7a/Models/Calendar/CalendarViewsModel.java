package com.tasree7a.Models.Calendar;

import com.tasree7a.Enums.CalenderType;

import org.joda.time.LocalDate;

/**
 * Created by Khalid on 8/1/17.
 * Holds initalization data for CalendarMonthAdapter
 */

public class CalendarViewsModel {

    private Object[] days;

    private int monthBeginningCell;

    private LocalDate startDay;

    private LocalDate today;

    private LocalDate checkInDate;

    private LocalDate markerCheckInDate;

    private CalenderType calenderType;


    public Object[] getDays() {

        return days;
    }


    public CalendarViewsModel setDays(Object[] days) {

        this.days = days;

        return this;
    }


    public int getMonthBeginningCell() {

        return monthBeginningCell;
    }


    public CalendarViewsModel setMonthBeginningCell(int monthBeginningCell) {

        this.monthBeginningCell = monthBeginningCell;

        return this;
    }


    public LocalDate getStartDay() {

        return startDay;
    }


    public CalendarViewsModel setStartDay(LocalDate startDay) {

        this.startDay = startDay;

        return this;
    }


    public LocalDate getToday() {

        return today;
    }


    public CalendarViewsModel setToday(LocalDate today) {

        this.today = today;

        return this;
    }


    public LocalDate getCheckInDate() {

        return checkInDate;
    }


    public CalendarViewsModel setCheckInDate(LocalDate checkInDate) {

        this.checkInDate = checkInDate;

        return this;
    }

    public LocalDate getMarkerCheckInDate() {

        return markerCheckInDate;
    }


    public CalendarViewsModel setMarkerCheckInDate(LocalDate markerCheckInDate) {

        this.markerCheckInDate = markerCheckInDate;

        return this;
    }


    public CalenderType getCalenderType() {

        return calenderType;
    }


    public CalendarViewsModel setCalenderType(CalenderType calenderType) {

        this.calenderType = calenderType;

        return this;
    }

}
