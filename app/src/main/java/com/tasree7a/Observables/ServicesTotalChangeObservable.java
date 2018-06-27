package com.tasree7a.observables;

import java.util.Observable;


/**
 * Created by mohammad on 5/3/15.
 * This class will handle the change to the check in date and notify observers for this action
 */
public class ServicesTotalChangeObservable extends Observable {

    private static volatile ServicesTotalChangeObservable instance = null;

    private ServicesTotalChangeObservable() {
    }

    public static synchronized ServicesTotalChangeObservable sharedInstance() {

        if (instance == null) {

            instance = new ServicesTotalChangeObservable();

        }

        return instance;
    }

    /**
     * Set has changed to true to be able to notify observers
     */
    public void notifyServicesTotalChanged(double total) {

        setChanged();

        notifyObservers(total);

    }
}
