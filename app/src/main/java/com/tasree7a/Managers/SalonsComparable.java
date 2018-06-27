package com.tasree7a.managers;

import com.tasree7a.enums.SortType;
import com.tasree7a.models.salondetails.SalonModel;

import java.util.Comparator;

/**
 * Created by mac on 7/4/17.
 */

public class SalonsComparable implements Comparator<SalonModel> {

    private SortType sortType;

    public SalonsComparable(SortType sortType){

        this.sortType = sortType;
    }

    @Override
    public int compare(SalonModel lhs, SalonModel rhs) {

        switch (sortType){

            case RATING:

                if(lhs.getRating() < rhs.getRating()){

                    return 1;

                } else if(lhs.getRating() > rhs.getRating()){

                    return -1;

                }

                 return 0;

            case DISTANCE:

                if(lhs.getDistance() > rhs.getDistance()){

                    return 1;

                } else if(lhs.getDistance() < rhs.getDistance()){

                    return -1;

                }

                return 0;

            case PRICE:

//                if(lhs.get > rhs.getRating()){
//
//                    return 1;
//
//                } else if(lhs.getRating() < rhs.getRating()){
//
//                    return -1;
//
//                }

                return 0;
        }

        return 0;
    }
}
