package com.tasree7a.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.facebook.AccessToken;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.tasree7a.utils.PermissionCode.MY_PERMISSIONS_REQUEST_LOCATION;

/**
 * Created by mac on 6/14/17.
 */

public class AppUtil {

    public static int checkPermissionStatus(String permission) {

        return ActivityCompat.checkSelfPermission(ThisApplication.getCurrentActivity(), permission);

    }

    public static boolean isLoggedIn() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        return accessToken != null;
    }

    public static Location getCurrentLocation() {

        if (checkPermissionStatus(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ThisApplication.getCurrentActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return null;

        }

        LocationManager lm = (LocationManager) ThisApplication.getCurrentActivity().getSystemService(LOCATION_SERVICE);

        Location location = null;

        try {

            List<String> providers = lm.getProviders(true);

            for (String provider : providers) {

                Location l = lm.getLastKnownLocation(provider);

                if (l == null) {

                    continue;

                }

                if (location == null || l.getAccuracy() < location.getAccuracy()) {

                    location = l;
                }
            }

        } catch (SecurityException e){

            e.printStackTrace();

        } finally {

            return location;

        }
    }
}
