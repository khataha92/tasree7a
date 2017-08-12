package com.tasree7a.Observables;

import android.location.Location;

import com.tasree7a.Models.PopularSalons.SalonModel;

import java.util.Observable;


/**
 * Created by mohammad on 5/3/15.
 * This class will handle the change to the check in and check out dates and notify observers for this action
 */
public class LocationChangedObservable extends Observable {

    private static volatile LocationChangedObservable instance = null;

    private LocationChangedObservable() {
    }

    public static synchronized LocationChangedObservable sharedInstance() {

        if (instance == null) {

            instance = new LocationChangedObservable();

        }

        return instance;
    }

    /**
     * Set has changed to true to be able to notify observers
     */
    public void setLocationChanged(Location location) {

        setChanged();

        notifyObservers(location);

    }
}
