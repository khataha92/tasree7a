package com.tasree7a.utils;

import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tasree7a.Enums.FontsType;
import com.tasree7a.ThisApplication;

import java.lang.reflect.Field;
import java.util.HashMap;


/**
 * Created by mohammad on 4/19/15.
 * This is the util that is responsible fot fonts
 */
public class FontUtil {

    private static final String TAG = FontUtil.class.getSimpleName();

    private static HashMap<FontsType,Typeface> arabicFonts = new HashMap<>();

    private static HashMap<FontsType,Typeface> englishFonts = new HashMap<>();

    /**
     * Get font from font type
     * @return Typeface
     */

    public static Typeface book(){

        return getFont(FontsType.BOOK, null);

    }

    public static Typeface medium(){

        return getFont(FontsType.MEDIUM, null);

    }

    public static Typeface black(){

        return getFont(FontsType.BLACK, null);
    }

    public static Typeface heavy(){

        return getFont(FontsType.HEAVY, null);

    }


    /**
     * Get font from name
     */

    public static void resetFonts(){

        arabicFonts.clear();

        englishFonts.clear();

    }

    public static Typeface getFont(FontsType fontType) {

        return getFont(fontType, null);

    }


    public static Typeface getFont(FontsType fontType, String language) {

        boolean isArabic = false;

        if (language == null && UserDefaultUtil.deviceLanguageIsArabic()) {

            isArabic = true;

        } else if (language != null && language.equalsIgnoreCase("ar")) {

            isArabic = true;

        }

        Typeface typeface = isArabic? arabicFonts.get(fontType) : englishFonts.get(fontType);

        if(typeface == null){

            if (isArabic) {

                typeface = getArabicFont(fontType);

                arabicFonts.put(fontType, typeface);

            } else {

                typeface = getEnglishFont(fontType);

                englishFonts.put(fontType, typeface);

            }

        }

        return typeface;

    }

    /**
     * Get english font
     *
     * @param fontsType Font type
     * @return TypeFace
     */
    private static Typeface getEnglishFont(FontsType fontsType) {

        if (fontsType == FontsType.BOOK) {

            return Typeface.createFromAsset(ThisApplication.getCurrentActivity().getAssets(), "fonts/Avenir/AvenirLTStd-Book.otf");

        }

        if (fontsType == FontsType.HEAVY) {

            return Typeface.createFromAsset(ThisApplication.getCurrentActivity().getAssets(), "fonts/Avenir/AvenirLTStd-Heavy.otf");

        }

        if (fontsType == FontsType.MEDIUM) {

            return Typeface.createFromAsset(ThisApplication.getCurrentActivity().getAssets(), "fonts/Avenir/AvenirLTStd-Medium.otf");

        }


        if (fontsType == FontsType.BLACK) {

            return Typeface.createFromAsset(ThisApplication.getCurrentActivity().getAssets(), "fonts/Avenir/AvenirLTStd-Black.otf");

        }

        return Typeface.createFromAsset(ThisApplication.getCurrentActivity().getAssets(), "fonts/Avenir/AvenirLTStd-Roman.otf");

    }

    /**
     * Get arabic font
     *
     * @param fontsType Font type
     * @return TypeFace
     */
    private static Typeface getArabicFont(FontsType fontsType) {

        if (fontsType == FontsType.BOOK) {

            return Typeface.createFromAsset(ThisApplication.getCurrentActivity().getAssets(), "fonts/Frutiger/FrutigerLTArabic-45Light.ttf");

        }

        if (fontsType == FontsType.BLACK) {

            return Typeface.createFromAsset(ThisApplication.getCurrentActivity().getAssets(), "fonts/Frutiger/FrutigerLTArabic-75Black.ttf");

        }


        if (fontsType == FontsType.MEDIUM) {

            return Typeface.createFromAsset(ThisApplication.getCurrentActivity().getAssets(),  "fonts/Frutiger/FrutigerLTArabic-55Roman.ttf");

        }


        if (fontsType == FontsType.HEAVY) {

            return Typeface.createFromAsset(ThisApplication.getCurrentActivity().getAssets(),  "fonts/Frutiger/FrutigerLTArabic-65Bold.ttf");

        }


        return Typeface.createFromAsset(ThisApplication.getCurrentActivity().getAssets(),  "fonts/Frutiger/FrutigerLTArabic-65Bold.ttf");


    }



    /**
     * Set Font for all elements in TextInputLayput View
     * @param mFont FontType to use
     * @param textInputLayout TextInputLayout object
     */
    public static void setHintTypeface(FontsType mFont, TextInputLayout textInputLayout){

        if (textInputLayout == null || mFont == null) return;

        try {

            textInputLayout.setTypeface(getFont(mFont));

            Field mErrorField = textInputLayout.getClass().getDeclaredField("mErrorView");

            mErrorField.setAccessible(true);

            ((TextView)mErrorField.get(textInputLayout)).setTypeface(getFont(mFont));


        } catch (Exception ignored) {

            Log.e(TAG, "error",ignored);

        }
    }


    public static void setTypefaceForAllTextViews(ViewGroup rootView, Typeface typeface) {

        if (rootView == null) return;

        for (int i = rootView.getChildCount() - 1; i >= 0; i--) {

            final View child = rootView.getChildAt(i);

            if (child instanceof ViewGroup) {

                setTypefaceForAllTextViews((ViewGroup) child, typeface);

            } else if (child != null && child instanceof TextView) {

                ((TextView) child).setTypeface(typeface);
            }
        }

    }


    public static String arabicToDecimal(String number) {

        char[] chars = new char[number.length()];

        for(int i=0;i<number.length();i++) {

            char ch = number.charAt(i);

            if (ch >= 0x0660 && ch <= 0x0669)
            {
                ch -= 0x0660 - '0';
            }
            else if (ch >= 0x06f0 && ch <= 0x06F9)
            {

                ch -= 0x06f0 - '0';
            }

            chars[i] = ch;
        }

        return new String(chars);
    }

    public static Typeface getFontFromTextLanguage(String str, FontsType fontType) {

        return getFont(fontType, StringUtil.isStringArabic(str)? "ar" : "en");

    }


    public static void appendFontForLanguage(TextView textView, String str, FontsType fontType) {

        str = str == null? "" : str;

        if (textView == null) return;

        textView.setText(str);

        if (str.isEmpty()) {

            textView.setTypeface(FontUtil.getFont(fontType, UserDefaultUtil.getDeviceLanguage()));

        } else if (StringUtil.isStringArabic(str)) {

            textView.setTypeface(FontUtil.getFont(fontType, "ar"));

        } else {

            textView.setTypeface(FontUtil.getFont(fontType, "en"));

        }

    }

    public static FontsType getFontTypeFromTypeFace(Typeface typefaceToGet) {

        FontsType[] fontsTypes = FontsType.values();

        // check arabic fonts
        for (FontsType fontsType : fontsTypes) {

            if (getFont(fontsType, "ar").equals(typefaceToGet)) {

                return fontsType;
            }

        }

        // check english fonts
        for (FontsType fontsType : fontsTypes) {

            if (getFont(fontsType, "en").equals(typefaceToGet)) {

                return fontsType;
            }

        }

        return FontsType.BOOK;

    }

}
