package com.tasree7a.utils;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.google.common.base.Strings;
import com.tasree7a.Enums.UserDefaultKeys;
import com.tasree7a.ThisApplication;

//import br.com.objectos.core.lang.Strings;

/**
 * Created by KhalidTaha on 4/20/17.
 */

public class UserDefaultUtil {

    private static String cachedUserLanguage = null;

    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ThisApplication.getCurrentActivity());

    public static boolean deviceLanguageIsArabic() {

        return "ar".equalsIgnoreCase(getUserLanguage());

    }

    public static void init(){

        preferences = PreferenceManager.getDefaultSharedPreferences(ThisApplication.getCurrentActivity());
    }

    public static String getUserLanguage() {

        if (!Strings.isNullOrEmpty(cachedUserLanguage)) {

            return cachedUserLanguage;

        }

        String deviceLanguage = getStringValue(UserDefaultKeys.DEVICE_LANGUAGE.toString());

        if (deviceLanguage != null) {

            cachedUserLanguage = deviceLanguage;

            return deviceLanguage;

        }

        deviceLanguage = getDeviceLanguage();

        cachedUserLanguage = deviceLanguage;

        return deviceLanguage;

    }

    public static String getDeviceLanguage() {

        Resources res = ThisApplication.getCurrentActivity().getResources();
        // Change locale settings in the app.

        android.content.res.Configuration conf = res.getConfiguration();

        String lang = conf.locale.getLanguage();

        if ("ar".equalsIgnoreCase(lang) || "en".equalsIgnoreCase(lang)) {

            return lang;

        } else {

            return "en";
        }

    }

    private static String getStringValue(final String key) {

        if (key == null || preferences == null) {

            return null;

        }

        return preferences.getString(key, null);

    }
}
