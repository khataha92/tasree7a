package com.tasree7a.Observables;

import com.tasree7a.Models.SalonDetails.SalonModel;

import java.util.Observable;


/**
 * Created by mohammad on 5/3/15.
 * This class will handle the change to the check in date and notify observers for this action
 */
public class FavoriteChangeObservable extends Observable {

    private static volatile FavoriteChangeObservable instance = null;

    private FavoriteChangeObservable() {
    }

    public static synchronized FavoriteChangeObservable sharedInstance() {

        if (instance == null) {

            instance = new FavoriteChangeObservable();

        }

        return instance;
    }

    /**
     * Set has changed to true to be able to notify observers
     */
    public void setFavoriteChanged(SalonModel salonModel) {

        setChanged();

        notifyObservers(salonModel);

    }
}
