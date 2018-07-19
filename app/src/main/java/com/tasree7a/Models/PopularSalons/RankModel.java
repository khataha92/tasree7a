package com.tasree7a.models.popularsalons;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by mac on 7/6/17.
 */

public class RankModel implements Serializable{

    @SerializedName("rank")
    double rank;

    @SerializedName("count")
    int count;

    public double getRank() {

        Random random = new Random();

        if(rank == 0){

            rank = (random.nextInt(5) +1) % 5;
        }

        return rank;
    }

    public int getCount() {
        return count;
    }
}
