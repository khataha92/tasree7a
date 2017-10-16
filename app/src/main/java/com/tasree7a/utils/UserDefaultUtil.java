package com.tasree7a.utils;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tasree7a.Enums.Language;
import com.tasree7a.Enums.UserDefaultKeys;
import com.tasree7a.Enums.UserFavoriteAction;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.Login.LoginModel;
import com.tasree7a.Models.Login.User;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.Models.SearchHistory.SearchHistoryItem;
import com.tasree7a.interfaces.AbstractCallback;

import java.util.ArrayList;
import java.util.List;

import static com.tasree7a.ThisApplication.getCurrentActivity;
import static com.tasree7a.utils.AppUtil.restartApp;

/**
 * Created by KhalidTaha on 4/20/17.
 */

public class UserDefaultUtil {

    private static String cachedUserLanguage = null;

    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getCurrentActivity());

    public static boolean isAppLanguageArabic() {

        return Language.AR == getAppLanguage();

    }


    public static void init() {

        preferences = PreferenceManager.getDefaultSharedPreferences(getCurrentActivity());
    }


    public static void logout() {

        preferences.edit().clear().commit();
    }

    public static void saveLogedUser(LoginModel user){

        if (user != null){

            setIsFBUser(user.isFacebookLogin());

            setStringValue(UserDefaultKeys.LOGIN_USER_MODEL.toString(), new Gson().toJson(user));

        }

    }

    public static boolean isBusinessUser(){

        try {

            return new Gson().fromJson(getStringValue(UserDefaultKeys.CURRENT_USER.toString()), User.class).isBusiness();

        } catch (Exception e){

            return false;
        }
    }

    private static void setIsFBUser(boolean isFBUser){

        setStringValue(UserDefaultKeys.IS_FB.toString(), isFBUser + "");

    }

    public static boolean isFBUser(){

        return getStringValue(UserDefaultKeys.IS_FB.toString()).equalsIgnoreCase("true");

    }


    public static List<SearchHistoryItem> getSearchHistory() {

        String searchHistory = getStringValue(UserDefaultKeys.SEARCH_HISTORY.getValue());

        if (Strings.isNullOrEmpty(searchHistory)) {

            return new ArrayList<>();

        }

        List<SearchHistoryItem> searchHistoryItems = new Gson().fromJson(searchHistory, new TypeToken<ArrayList<SearchHistoryItem>>() {

        }.getType());

        return searchHistoryItems;

    }


    public static void removeSalonFromFavorite(SalonModel salonModel) {

        for (int i = 0; i < favoriteSalons.size(); i++) {

            if (favoriteSalons.get(i).getId().equalsIgnoreCase(salonModel.getId())) {

                favoriteSalons.remove(i);

                i--;
            }
        }

        RetrofitManager.getInstance().changeSalonToUserFavorite(salonModel.getId(), getCurrentUser().getUserId(), UserFavoriteAction.DELETE.value, new AbstractCallback() {

            @Override
            public void onResult(boolean isSuccess, Object result) {

                setStringValue(UserDefaultKeys.FAVORITE_SALONS.getValue(), new Gson().toJson(favoriteSalons));

            }
        });

    }

    static List<SalonModel> favoriteSalons;

    public static void addSalonToFavorite(SalonModel salonModel) {

        favoriteSalons = getFavoriteSalons();

        for (int i = 0; i < favoriteSalons.size(); i++) {

            if (favoriteSalons.get(i).getId().equalsIgnoreCase(salonModel.getId())) {

                return;

            }

        }

        favoriteSalons.add(salonModel);

        RetrofitManager.getInstance().changeSalonToUserFavorite(salonModel.getId(), getCurrentUser().getUserId(), UserFavoriteAction.ADD.value, new AbstractCallback() {

            @Override
            public void onResult(boolean isSuccess, Object result) {

                setStringValue(UserDefaultKeys.FAVORITE_SALONS.getValue(), new Gson().toJson(favoriteSalons));

            }
        });
    }

    public static void saveFavoriteSalons(List<SalonModel> salonModels) {

        List<SalonModel> favoriteSalons = new ArrayList<>();

        favoriteSalons.clear();

        favoriteSalons.addAll(salonModels);

        setStringValue(UserDefaultKeys.FAVORITE_SALONS.getValue(), new Gson().toJson(favoriteSalons));

    }



    public static List<SalonModel> getFavoriteSalons() {

        String favoriteList = getStringValue(UserDefaultKeys.FAVORITE_SALONS.getValue());

        if (Strings.isNullOrEmpty(favoriteList)) {

            return new ArrayList<>();

        }

        List<SalonModel> favorites = new Gson().fromJson(favoriteList, new TypeToken<ArrayList<SalonModel>>() {

        }.getType());

        return favorites;

    }


    public static boolean isSalonFavorite(SalonModel salonModel) {

        List<SalonModel> favorites = getFavoriteSalons();

        for (int i = 0; i < favorites.size(); i++) {

            if (salonModel.getId().equalsIgnoreCase(favorites.get(i).getId())) {

                return true;
            }
        }

        return false;
    }


    public static Language getUserLanguage() {

        if (!Strings.isNullOrEmpty(cachedUserLanguage)) {

            return Language.valueOf(cachedUserLanguage.toUpperCase());

        }

        String deviceLanguage = getStringValue(UserDefaultKeys.DEVICE_LANGUAGE.toString());

        if(deviceLanguage != null && deviceLanguage.trim().length() == 0) {

            deviceLanguage = "en";
            
        }

        if (deviceLanguage != null) {

            cachedUserLanguage = deviceLanguage;

            return Language.valueOf(deviceLanguage.toUpperCase());

        }

        deviceLanguage = getDeviceLanguage();

        cachedUserLanguage = deviceLanguage;

        return Language.valueOf(deviceLanguage.toUpperCase());

    }


    public static String getDeviceLanguage() {

        Resources res = getCurrentActivity().getResources();
        // Change locale settings in the app.

        android.content.res.Configuration conf = res.getConfiguration();

        String lang = conf.locale.getLanguage();

        if ("ar".equalsIgnoreCase(lang) || "en".equalsIgnoreCase(lang)) {

            return lang;

        } else {

            return "en";
        }

    }


    public static Language getAppLanguage() {

        String lang = getStringValue(UserDefaultKeys.LANGUAGE_LOCALE.toString());

        return Strings.isNullOrEmpty(lang) ? Language.EN : Language.valueOf(lang);

    }


    public static void setAppLanguage(Language lang) {

        setStringValue(UserDefaultKeys.LANGUAGE_LOCALE.toString(), lang.toString());

        restartApp();
    }


    private static void setStringValue(String key, String val) {

        if (key == null || preferences == null) {

            return;

        }

        preferences.edit().putString(key, val).commit();

    }


    private static String getStringValue(final String key) {

        if (key == null || preferences == null) {

            return null;

        }

        return preferences.getString(key, "");

    }

    public static User getCurrentUser(){

        return new Gson().fromJson(getStringValue(UserDefaultKeys.CURRENT_USER.getValue()), User.class);

    }

    public static void saveUser(User user) {

        setStringValue(UserDefaultKeys.CURRENT_USER.getValue(), new Gson().toJson(user));

    }
}
