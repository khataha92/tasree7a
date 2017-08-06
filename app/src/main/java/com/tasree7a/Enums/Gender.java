package com.tasree7a.Enums;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 7/31/17.
 */

public enum Gender {

    @SerializedName("Male")
    MALE(4),
    @SerializedName("Female")
    FEMALE(5);

    int val;

    Gender(int val){

        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
