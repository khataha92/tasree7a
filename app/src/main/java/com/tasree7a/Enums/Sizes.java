
package com.tasree7a.enums;

/**
 * Created by Khalid Taha on 10/4/16.
 * Holds sizes for load images from uri to ImageView
 */

public enum Sizes {
    SMALL("small"), MEDIUM("medium"), LARGE("large");

    private String value;

    Sizes(String str) {

        value = str;

    }

    public String getValue() {
        return value;
    }

    //From String method will return you the Enum for the provided input string
    public static Sizes fromString(String parameterName) {
        if (parameterName != null) {
            for (Sizes objType : Sizes.values()) {
                if (parameterName.equalsIgnoreCase(objType.value)) {
                    return objType;
                }
            }
        }
        return null;
    }


    }
