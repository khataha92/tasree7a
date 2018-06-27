package com.tasree7a.enums;

/**
 * Created by KhalidTaha on 4/20/17.
 */

public enum UserDefaultKeys {

    DEVICE_LANGUAGE ("DEVICE_LANGUAGE"),SEARCH_HISTORY("SEARCH_HISTORY"),FAVORITE_SALONS("FAVORITE_SALONS"), LANGUAGE_LOCALE("LANGUAGE_LOCALE"), LOGIN_USER_MODEL("LOGIN_USER_MODEL"), IS_FB("IS_FB"), CURRENT_USER("CURRENT_USER"), IS_REGESTERING("IS_REGESTERING"), SALON_USER_MODEL("SALON_USER_MODEL");

    private String value;

    UserDefaultKeys(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {

        return this.getValue();
    }

}
