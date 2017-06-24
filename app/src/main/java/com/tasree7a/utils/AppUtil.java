package com.tasree7a.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;

import static com.tasree7a.utils.PermissionCode.MY_PERMISSIONS_REQUEST_LOCATION;

/**
 * Created by mac on 6/14/17.
 */

public class AppUtil {

    public static int checkPermissionStatus(String permission) {

        return ActivityCompat.checkSelfPermission(ThisApplication.getCurrentActivity(), permission);

    }

    public static Location getCurrentLocation() {

        if (checkPermissionStatus(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ThisApplication.getCurrentActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return null;

        }

        LocationManager lm = (LocationManager) ThisApplication.getCurrentActivity().getSystemService(Context.LOCATION_SERVICE);

        Location location = null;

        try {

            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        } catch (SecurityException e){

            e.printStackTrace();

        } finally {

            return location;

        }
    }
}
