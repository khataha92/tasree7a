package com.tasree7a.utils;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.enums.Language;

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


    public static String format(final Date date, final DateFormatsOptions defaultFormat, Locale locale) {

        SimpleDateFormat outFormat = new SimpleDateFormat(defaultFormat.getValue(), locale);

        return FontUtil.arabicToDecimal(outFormat.format(date));

    }

    public static Date formatDate(Context context, final Date date, final DateFormatsOptions defaultFormat, Locale locale) {

        String deviceLanguage = UserDefaultUtil.getUserLanguage(context).name();

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

    public static String format(Context context, final Date date, final DateFormatsOptions defaultFormat) {

        String deviceLanguage = UserDefaultUtil.getUserLanguage(context).name();

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

//        String deviceLanguage = UserDefaultUtil.getUserLanguage().name();
//
//        Locale locale = new Locale(deviceLanguage);
//
//        SimpleDateFormat outFormat = new SimpleDateFormat(defaultFormat.getValue(), locale);
//
//        try {
//
//            return format(outFormat.parse(date), defaultFormat, locale);
//
//        } catch (ParseException e) {
//
//            Log.e("DateUtil", "Error formating date", e);
//
//        }

        return "";
    }

    public static int getHourFromDate(final Date date) {

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance

        calendar.setTime(date);   // assigns calendar to given date

        return calendar.get(Calendar.HOUR_OF_DAY);
    }


    /**
     * Used if and only if the format is unknown or undetermined
     *
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

        Locale locale = StringUtil.isStringArabic(date) ? new Locale("ar") : new Locale("en");

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
     *
     * @return
     */
    public static Date zeroHoursDate(Context context, Date date) {
        return formatDate(context, date, DateFormatsOptions.API_FORMAT, new Locale("en"));
    }

    public static Date parseDate(String date, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format, new Locale(UserDefaultUtil.getAppLanguage().equals(Language.AR) ? "ar" : "en"));
            return formatter.parse(date);
        } catch (ParseException e) {
            Log.d(TAG, "Error:", e);
        }
        return new Date();
    }

    /**
     * Return today's date
     *
     * @param context
     * @return
     */
    public static Date getToday(Context context) {

        return zeroHoursDate(context, new Date());

    }

    public static LocalDate dateToLocalDate(Date date) {

        Locale locale = new Locale("en");

        return LocalDate.parse(new SimpleDateFormat(DateFormatsOptions.DEFAULT_FORMAT.getValue(), locale).format(date));

    }

    public static Date localDateToDate(LocalDate localDate) {

        try {
            Locale locale = new Locale("en");

            return new SimpleDateFormat(DateFormatsOptions.DEFAULT_FORMAT.getValue(), locale).parse(localDate.toString());

        } catch (Throwable t) {
            Log.e(TAG, "error in simple date format", t);
        }

        return null;
    }

    public static LocalDate getMaxLocalDate(ArrayList<LocalDate> list) {

        if (list == null || list.size() <= 0)
            return null;

        LocalDate max = list.get(0);

        for (int i = 1; i < list.size(); i++) {

            if (max == null || (list.get(i) != null && list.get(i).isAfter(max)))
                max = list.get(i);
        }

        return max;
    }

    public static LocalDate getMinLocalDate(ArrayList<LocalDate> list) {

        if (list == null || list.size() <= 0)
            return null;

        LocalDate min = list.get(0);

        for (int i = 1; i < list.size(); i++) {

            if (min == null || (list.get(i) != null && list.get(i).isBefore(min)))
                min = list.get(i);
        }

        return min;
    }


}
