package com.tasree7a.observables;

import java.util.Observable;

/**
 * Created by mac on 6/20/17.
 */

public class PermissionGrantedObservable extends Observable {

    private static PermissionGrantedObservable instance = null;

    private PermissionGrantedObservable(){

    }

    public static PermissionGrantedObservable getInstance(){

        if(instance == null){

            instance = new PermissionGrantedObservable();

        }

        return instance;
    }

    public void notifyPermissionGranted(String permission){

        setChanged();

        notifyObservers(permission);

    }

}
