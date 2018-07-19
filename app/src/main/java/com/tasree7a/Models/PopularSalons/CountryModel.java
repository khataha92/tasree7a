package com.tasree7a.models.popularsalons;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mac on 8/6/17.
 */

public class CountryModel implements Serializable{

    @SerializedName("id")
    String id;

    @SerializedName("country_name")
    String name;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
