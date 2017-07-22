package com.tasree7a.Enums;

/**
 * Created by KhalidTaha on 4/20/17.
 */

public enum UserDefaultKeys {

    DEVICE_LANGUAGE ("DEVICE_LANGUAGE"),SEARCH_HISTORY("SEARCH_HISTORY"),FAVORITE_SALONS("FAVORITE_SALONS"), LANGUAGE_LOCALE("LANGUAGE_LOCALE");

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
