package com.tasree7a.Enums;

/**
 * Created by mac on 8/20/17.
 */

public enum CustomOrientation {

    HORIZONTAL(1), VERTICAL(2);

    int value;

    CustomOrientation(int v){

        value = v;

    }

    public static CustomOrientation valueOf(int a){

        if(a == 1){

            return HORIZONTAL;
        }
        else if(a == 2){

            return VERTICAL;
        }

        return VERTICAL;
    }
}
