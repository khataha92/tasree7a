package com.tasree7a;

import android.util.Log;

/**
 * Created by samikhleaf on 2/23/18.
 */

public class AvailableTimesHelper {

    public static String prepareAvailableTimes(int timeIndex) {
        String time = "";
        String hour = "";
        String min = "";
        String AmPm = "";
        switch (timeIndex) {
            case 0:
                hour = "12";
                min = "00";
                AmPm = "AM";
                break;
            case 1:
                hour = "12";
                min = "30";
                AmPm = "AM";
                break;
            case 2:
                hour = "01";
                min = "00";
                AmPm = "AM";
                break;
            case 3:
                hour = "01";
                min = "30";
                AmPm = "AM";
                break;
            case 4:
                hour = "02";
                min = "00";
                AmPm = "AM";
                break;
            case 5:
                hour = "02";
                min = "30";
                AmPm = "AM";
                break;
            case 6:
                hour = "03";
                min = "00";
                AmPm = "AM";
                break;
            case 7:
                hour = "03";
                min = "30";
                AmPm = "AM";
                break;
            case 8:
                hour = "04";
                min = "00";
                AmPm = "AM";
                break;
            case 9:
                hour = "04";
                min = "30";
                AmPm = "AM";
                break;
            case 10:
                hour = "05";
                min = "00";
                AmPm = "AM";
                break;
            case 11:
                hour = "05";
                min = "30";
                AmPm = "AM";
                break;
            case 12:
                hour = "06";
                min = "00";
                AmPm = "AM";
                break;
            case 13:
                hour = "06";
                min = "30";
                AmPm = "AM";
                break;
            case 14:
                hour = "07";
                min = "00";
                AmPm = "AM";
                break;
            case 15:
                hour = "07";
                min = "30";
                AmPm = "AM";
                break;
            case 16:
                hour = "08";
                min = "00";
                AmPm = "AM";
                break;
            case 17:
                hour = "08";
                min = "30";
                AmPm = "AM";
                break;
            case 18:
                hour = "09";
                min = "00";
                AmPm = "AM";
                break;
            case 19:
                hour = "09";
                min = "30";
                AmPm = "AM";
                break;
            case 20:
                hour = "10";
                min = "00";
                AmPm = "AM";
                break;
            case 21:
                hour = "10";
                min = "30";
                AmPm = "AM";
                break;
            case 22:
                hour = "11";
                min = "00";
                AmPm = "AM";
                break;
            case 23:
                hour = "11";
                min = "30";
                AmPm = "AM";
                break;
            case 24:
                hour = "12";
                min = "00";
                AmPm = "PM";
                break;
            case 25:
                hour = "12";
                min = "30";
                AmPm = "PM";
                break;
            case 26:
                hour = "01";
                min = "00";
                AmPm = "PM";
                break;
            case 27:
                hour = "01";
                min = "30";
                AmPm = "PM";
                break;
            case 28:
                hour = "02";
                min = "00";
                AmPm = "PM";
                break;
            case 29:
                hour = "02";
                min = "30";
                AmPm = "PM";
                break;
            case 30:
                hour = "03";
                min = "00";
                AmPm = "PM";
                break;
            case 31:
                hour = "03";
                min = "30";
                AmPm = "PM";
                break;
            case 32:
                hour = "04";
                min = "00";
                AmPm = "PM";
                break;
            case 33:
                hour = "04";
                min = "30";
                AmPm = "PM";
                break;
            case 34:
                hour = "05";
                min = "00";
                AmPm = "PM";
                break;
            case 35:
                hour = "05";
                min = "30";
                AmPm = "PM";
                break;
            case 36:
                hour = "06";
                min = "00";
                AmPm = "PM";
                break;
            case 37:
                hour = "06";
                min = "30";
                AmPm = "PM";
                break;
            case 38:
                hour = "07";
                min = "00";
                AmPm = "PM";
                break;
            case 39:
                hour = "07";
                min = "30";
                AmPm = "PM";
                break;
            case 40:
                hour = "08";
                min = "00";
                AmPm = "PM";
                break;
            case 41:
                hour = "08";
                min = "30";
                AmPm = "PM";
                break;
            case 42:
                hour = "09";
                min = "00";
                AmPm = "PM";
                break;
            case 43:
                hour = "09";
                min = "30";
                AmPm = "PM";
                break;
            case 44:
                hour = "10";
                min = "00";
                AmPm = "PM";
                break;
            case 45:
                hour = "10";
                min = "30";
                AmPm = "PM";
                break;
            case 46:
                hour = "11";
                min = "00";
                AmPm = "PM";
                break;
            case 47:
                hour = "11";
                min = "30";
                AmPm = "PM";
                break;
        }

        return hour + ":" + min + " " + AmPm;
    }
}
