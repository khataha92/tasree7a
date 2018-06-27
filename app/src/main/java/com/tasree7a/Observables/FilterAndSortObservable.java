package com.tasree7a.observables;

import java.util.Observable;

/**
 * Created by mac on 6/20/17.
 */

public class FilterAndSortObservable extends Observable {

    private static FilterAndSortObservable instance = null;

    private FilterAndSortObservable(){

    }

    public static FilterAndSortObservable getInstance(){

        if(instance == null){

            instance = new FilterAndSortObservable();

        }

        return instance;
    }

    public void notifyFilterChanged(){

        setChanged();

        notifyObservers();

    }

}
