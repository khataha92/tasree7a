package com.tasree7a.utils;

import android.graphics.Paint;
import android.text.TextUtils;

import com.twitter.Regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mohammad on 5/17/15.
 * This is the class that will contain strings func. utils
 */
public class StringUtil {

    public static List<String> splitWordsIntoStringsThatFit(String source, float maxWidthPx, Paint paint) {
        ArrayList<String> result = new ArrayList<>();

        ArrayList<String> currentLine = new ArrayList<>();

        String[] sources = source.split("\\s");
        for(String chunk : sources) {
            if(paint.measureText(chunk) < maxWidthPx) {
                processFitChunk(maxWidthPx, paint, result, currentLine, chunk);
            } else {
                //the chunk is too big, split it.
                List<String> splitChunk = splitIntoStringsThatFit(chunk, maxWidthPx, paint);
                for(String chunkChunk : splitChunk) {
                    processFitChunk(maxWidthPx, paint, result, currentLine, chunkChunk);
                }
            }
        }

        if(! currentLine.isEmpty()) {
            result.add(TextUtils.join(" ", currentLine));
        }
        return result;
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    private static List<String> splitIntoStringsThatFit(String source, float maxWidthPx, Paint paint) {
        if(TextUtils.isEmpty(source) || paint.measureText(source) <= maxWidthPx) {
            return Arrays.asList(source);
        }

        ArrayList<String> result = new ArrayList<>();
        int start = 0;
        for(int i = 1; i <= source.length(); i++) {
            String substr = source.substring(start, i);
            if(paint.measureText(substr) >= maxWidthPx) {
                //this one doesn't fit, take the previous one which fits
                String fits = source.substring(start, i - 1);
                result.add(fits);
                start = i - 1;
            }
            if (i == source.length()) {
                String fits = source.substring(start, i);
                result.add(fits);
            }
        }

        return result;
    }

    /**
     * Processes the chunk which does not exceed maxWidth.
     */
    private static void processFitChunk(float maxWidth, Paint paint, ArrayList<String> result, ArrayList<String> currentLine, String chunk) {
        currentLine.add(chunk);
        String currentLineStr = TextUtils.join(" ", currentLine);
        if (paint.measureText(currentLineStr) >= maxWidth) {
            //remove chunk
            currentLine.remove(currentLine.size() - 1);
            result.add(TextUtils.join(" ", currentLine));
            currentLine.clear();
            //ok because chunk fits
            currentLine.add(chunk);
        }
    }


    public static boolean isStringEnglish(String input) {

        if (input == null) {

            return true;

        }

        final Pattern RTL_CHARACTERS =
                Pattern.compile("[\u0600-\u06FF\u0750-\u077F\u0590-\u05FF\uFE70-\uFEFF]");

        Matcher matcher = RTL_CHARACTERS.matcher(input);

        return !matcher.find();

    }

    /**
     * Check if the specified string is arabic
     *
     * @param input The string to test
     * @return If the string is arabic
     */
    public static boolean isStringArabic(String input) {

        if (input == null) {

            return true;

        }

        return Regex.RTL_CHARACTERS.matcher(input).find();

    }

    public static String setImageSize(String imageUrl, String size) {

        return imageUrl.replace("[size]", size);

    }


    public static String commaSeparatedString(List<Object> list) {

        String str = "";

        if (list != null && list.size() > 0) {

            for (int i=0; i<list.size(); i++) {

                str += list.get(i).toString();

                if (i<list.size()-1) {

                    str += ",";

                }

            }

        }

        return str;

    }


    /**
     * Add a character after every n characters, padding character
     * @param character char to be inserted
     * @param string String to be insterted into
     * @param num int every character
     * @return padded String
     */
    public static String insertCharacterEvery(char character, String string, int num) {

        if (string.length() <= num) {

            return string;
        }

        string = string.replaceAll(String.valueOf(character), "");

        StringBuilder retVal = new StringBuilder(string);

        for (int i = 0; i < string.length() / num; i++) {

            retVal.insert(((i + 1) * num) + i, character);

        }

        return retVal.toString().trim();

    }


    /**
     * If a string contains one of these giving strings
     * @param string String, main stirng witch will contains one of the following strings
     * @param strings String[], strings to check whatever one of them is contained in the previous string or not
     * @return boolean one of them is contained
     */
    public static boolean stringContainsOneOfThese(String string, String[] strings) {

        for (String str : strings) {

            if (string.contains(str)) {

                return true;
            }

        }

        return false;

    }


    public static String valueToString(Object val){

        String string = "";

        if(val instanceof List || val instanceof ArrayList){

            List<String> strings = (List<String>) val;

            for(int i = 0 ; i < strings.size() ; i++){

                string += strings.get(i) + "\n";
            }

        } else if(val instanceof String) {

            return val.toString();

        }

        return string;
    }

}
