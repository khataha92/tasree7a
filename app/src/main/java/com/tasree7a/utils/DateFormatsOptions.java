package com.tasree7a.utils;

/**
 * Created by mohammad on 5/25/15.
 * This is the class that contains date formates
 */
public enum DateFormatsOptions {

    DEFAULT_FORMAT("yyyy-MM-dd"),

    API_FORMAT("MM/dd/yyyy"),

    WEBSITE_FORMAT("dd-MM-yyyy"),

    EVENT_FORMAT("yyyy-dd-MM"),

    YEAR_MONTH_DAY("yyyy-MM-dd"),

    CHECK_OUT_FORMAT("E, d MMM, yyyy"),

    DAY_MONTH_DAY("E MMM d"),

    MONTH_DAY_YEAR("MMM d , yyyy"),

    MONTH_DAY_YEAR_2("MMM d, yyyy"),

    MONTH_DAY("MMM dd"),

    DAY_MONTH_YEAR("d MMM , yyyy"),

    CALENDER_ALERT_LTR("MMM yyyy"),

    CALENDER_ALERT_RTL("yyyy MMMM"),

    MONTH_DAY_DOT("d MMM"),

    MONTH_DAY_DOT_RTL("MMM d"),

    MONTH_DAY_YEAY_NO_COMM_RTL("d MMM yyyy"),

    MONTH_DAY_YEAY_NO_COMM("MMM d yyyy");


    DateFormatsOptions(String val){

        this.value = val;

    }

    public String getValue(){

        return value;

    }

    private String value;


    public static DateFormatsOptions getMonthDotDay(){

        return MONTH_DAY_DOT_RTL;
    }

    public static DateFormatsOptions getMonthDayYearWithComma(){

        return MONTH_DAY_YEAR_2;// MONTH_DAY_YEAR_2: MONTH_DAY_YEAR_2);
    }

    public static DateFormatsOptions getYearMonthDay(){

        return MONTH_DAY_YEAY_NO_COMM;
    }


}
