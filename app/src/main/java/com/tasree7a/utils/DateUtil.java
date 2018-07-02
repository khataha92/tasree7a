package com.tasree7a.utils;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;


/**
 * Created by mohammad on 5/5/15.
 * This is the class that will handle date functions
 */

public abstract class DateUtil {

    private static final String TAG = DateUtil.class.getName();

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400

    // Replace the date with "Today" if the date is today, "Tomorrow" if it tomorrow. else format the date
    public static String replaceDateWithTodayOrTomorrow(Date checkDate, DateFormatsOptions format) {

        String dateString = "";

        // not used right now
        LocalDate localDate = DateUtil.dateToLocalDate(checkDate);

        if (DateUtil.dateToLocalDate(DateUtil.getToday()).compareTo(localDate) == 0) {

            dateString = ThisApplication.getCurrentActivity().getString(R.string.TONIGHT);

        } else if (DateUtil.dateToLocalDate(DateUtil.getTomorrow()).compareTo(localDate) == 0) {

            dateString =ThisApplication.getCurrentActivity().getString(R.string.TOMMOROW);

        } else {

            dateString = DateUtil.format(checkDate, format);

        }

        //dateString = DateUtil.format(checkDate, format);

        return dateString;

    }

    public static boolean isToday(Date dt){

        LocalDate localDate = DateUtil.dateToLocalDate(dt);

        return  DateUtil.dateToLocalDate(DateUtil.getToday()).compareTo(localDate) == 0;

    }

    public static int getYearFromDate(Date date){

        return toCalendar(date).get(Calendar.YEAR);

    }

    public static Calendar toCalendar(Date date){

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        return cal;
    }


    public static boolean isTomorrow(Date dt){

        LocalDate localDate = DateUtil.dateToLocalDate(dt);

        return  DateUtil.dateToLocalDate(DateUtil.getTomorrow()).compareTo(localDate) == 0;

    }

    public static void printDifference(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

    }

    /**
     * Get difference in days between two dates
     *
     * @param startDate First date
     * @param endDate   Second date
     * @return Difference in days
     */
    public static long getDayDifference(Date startDate, Date endDate) {

        // using joda-time library to overcome edge cases such as summer time (day-light-saving) and leap seconds, leap yeras
        // http://javarevisited.blogspot.com/2015/07/how-to-find-number-of-days-between-two-dates-in-java.html
        return Days.daysBetween(
                new LocalDate(startDate.getTime()),
                new LocalDate(endDate.getTime())).getDays();

    }

    /**
     * Get day mName from date, three letters
     *
     * @param date The date to get day mName from
     * @param isFull true: returns full day mName, false: returns short day mName
     * @return Day mName
     */
    public static String getDayName(final Date date,boolean isFull) {

        String deviceLanguage = UserDefaultUtil.getUserLanguage().name();

        Locale locale = new Locale(deviceLanguage);

        SimpleDateFormat outFormat = new SimpleDateFormat(isFull? "EEEE" : "EEE", locale);

        return outFormat.format(date);

    }

    public static boolean isFutureDate(Date dt){

        if (dt == null) return false;

        int difference = dt.compareTo(getToday());

        return difference >= 0 ;
    }


    /**
     * Get day number from date
     *
     * @param date The date to get day number from
     * @return Day number
     */
    public static String getDayNumber(final Date date) {

        String deviceLanguage = UserDefaultUtil.getUserLanguage().name();

        Locale locale = new Locale(deviceLanguage);

        SimpleDateFormat outFormat = new SimpleDateFormat("dd", locale);

        return outFormat.format(date);

    }

    /**
     * Get MonthName from date
     *
     * @param date The date to get MonthName from
     * @return MonthName
     */
    public static String getMonthName(final Date date) {

        String deviceLanguage = UserDefaultUtil.getUserLanguage().name();

        Locale locale = new Locale(deviceLanguage);

        SimpleDateFormat outFormat = new SimpleDateFormat("MMMM", locale);

        return outFormat.format(date);

    }

    public static String format(final Date date, final DateFormatsOptions defaultFormat, Locale locale) {

        SimpleDateFormat outFormat = new SimpleDateFormat(defaultFormat.getValue(), locale);

        return FontUtil.arabicToDecimal(outFormat.format(date));

    }

    public static Date formatDate(final Date date, final DateFormatsOptions defaultFormat, Locale locale) {

        String deviceLanguage = UserDefaultUtil.getUserLanguage().name();

        if (locale == null) {

            locale = new Locale(deviceLanguage);

        }

        String formattedDate = format(date, defaultFormat, locale);

        SimpleDateFormat outFormat = new SimpleDateFormat(defaultFormat.getValue(), locale);

        try {

            return new Date(outFormat.parse(formattedDate).getTime());

        } catch (ParseException e) {

            Log.e("DateUtil", "formatDate error formating date", e);
        }

        return date;

    }

    public static String format(final Date date, final DateFormatsOptions defaultFormat) {

        String deviceLanguage = UserDefaultUtil.getUserLanguage().name();

        Locale locale = new Locale(deviceLanguage);
        
        return format(date, defaultFormat, locale);

    }

    public static String format(final Date date, final String defaultFormat, final TimeZone timeZone) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultFormat);

        simpleDateFormat.setTimeZone(timeZone);

        return simpleDateFormat.format(date);
    }

    public static String formatUTC(final Date date, final String defaultFormat) {

        TimeZone timeZone = new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC");

        return format(date, defaultFormat, timeZone);

    }

    public static String format(final String date, final DateFormatsOptions defaultFormat) {

        String deviceLanguage = UserDefaultUtil.getUserLanguage().name();

        Locale locale = new Locale(deviceLanguage);

        SimpleDateFormat outFormat = new SimpleDateFormat(defaultFormat.getValue(), locale);

        try {

            return format(outFormat.parse(date), defaultFormat, locale);

        } catch (ParseException e) {

            Log.e("DateUtil", "Error formating date", e);

        }

        return "";
    }

    public static int getHourFromDate(final Date date) {

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance

        calendar.setTime(date);   // assigns calendar to given date

        return calendar.get(Calendar.HOUR_OF_DAY);
    }


    /**
     * Used if and only if the format is unknown or undetermined
     * @param dateAsString String
     * @return Date
     */
    public static Date stringToDate(String dateAsString) {

        for (DateFormatsOptions option : DateFormatsOptions.values()) {

            Date date = stringToDate(dateAsString, option);

            if (date != null) {

                return date;

            }

        }

        return null;

    }


    public static Date stringToDate(String date, DateFormatsOptions apiFormat) {

        Locale locale = StringUtil.isStringArabic(date)?new Locale("ar") : new Locale("en");

        SimpleDateFormat format = new SimpleDateFormat(apiFormat.getValue(), locale);

        try {

            format.setLenient(false);

            return format.parse(date);

        } catch (Throwable t) {

            Crashlytics.log("currupt_date: " + date + " format: " + apiFormat);

            Log.e("DateUtil", "Error formating date", t);

        }

        return null;
    }

    /**
     * Return a date without the hours and lower compoenents, only year, month, hour
     * @return
     */
    public static Date zeroHoursDate(Date date) {

        return formatDate(date, DateFormatsOptions.API_FORMAT, new Locale("en"));

    }

    /**
     * Return today's date
     * @return
     */
    public static Date getToday() {

        return zeroHoursDate(new Date());

    }

    /**
     * Returns tomorrow's date
     * @return
     */
    public static Date getTomorrow() {

        return getNextDay(getToday());
    }

    /**
     * Return next day's date for the given date
     * @param date
     * @return
     */
    public static Date getNextDay(Date date) {

        Calendar gc = Calendar.getInstance();

        gc.setTimeInMillis(date.getTime());

        gc.add(Calendar.DATE, 1);

        return zeroHoursDate(gc.getTime());

    }


    public static LocalDate dateToLocalDate(Date date) {

        Locale locale = new Locale("en");

        return LocalDate.parse(new SimpleDateFormat(DateFormatsOptions.DEFAULT_FORMAT.getValue(),locale).format(date));

    }

    public static Date localDateToDate(LocalDate localDate){

        try {
            Locale locale = new Locale("en");

            return  new SimpleDateFormat(DateFormatsOptions.DEFAULT_FORMAT.getValue(), locale).parse(localDate.toString());

        } catch (Throwable t) {
           Log.e(TAG, "error in simple date format",t);
        }

        return null;
    }

    public static LocalDate getMaxLocalDate(ArrayList<LocalDate> list){

        if(list == null || list.size() <= 0)
            return null;

        LocalDate max = list.get(0);

        for(int i=1; i<list.size(); i++){

            if(max == null || (list.get(i) != null && list.get(i).isAfter(max)))
                max = list.get(i);
        }

        return max;
    }

    public static LocalDate getMinLocalDate(ArrayList<LocalDate> list){

        if(list == null || list.size() <= 0)
            return null;

        LocalDate min = list.get(0);

        for(int i=1; i<list.size(); i++){

            if(min == null || (list.get(i) != null && list.get(i).isBefore(min)))
                min = list.get(i);
        }

        return min;
    }


}
