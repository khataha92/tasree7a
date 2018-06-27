package com.tasree7a.observables;

import java.util.Observable;


/**
 * This class will handle the change to the check in date and notify observers for this action
 */
public class MenuIconClickedObservable extends Observable {

    private static volatile MenuIconClickedObservable instance = null;

    private MenuIconClickedObservable() {
    }

    public static synchronized MenuIconClickedObservable sharedInstance() {

        if (instance == null) {

            instance = new MenuIconClickedObservable();

        }

        return instance;
    }

    /**
     * Set has changed to true to be able to notify observers
     */
    public void menuIconClicked() {

        setChanged();

        notifyObservers();

    }
}
