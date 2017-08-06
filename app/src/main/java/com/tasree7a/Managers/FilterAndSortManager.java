package com.tasree7a.Managers;

import com.tasree7a.Enums.FilterType;
import com.tasree7a.Enums.Gender;
import com.tasree7a.Enums.SortType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 7/5/17.
 */

public class FilterAndSortManager {

    private static FilterAndSortManager instance = null;

    SortType sortType = SortType.DISTANCE;

    Gender salonType = Gender.MALE;

    List<FilterType> filters = new ArrayList<>();

    private FilterAndSortManager(){


    }

    public static FilterAndSortManager getInstance(){

        if(instance == null){

            instance = new FilterAndSortManager();

            instance.getFilters().add(FilterType.ALL);

        }

        return instance;

    }

    public void reset(){

        sortType = SortType.DISTANCE;

        filters.clear();
    }

    public void setSalonType(Gender salonType) {
        this.salonType = salonType;
    }

    public Gender getSalonType() {
        return salonType;
    }

    public List<FilterType> getFilters() {
        return filters;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public SortType getSortType() {
        return sortType;
    }
}
