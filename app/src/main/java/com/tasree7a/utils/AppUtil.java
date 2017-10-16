package com.tasree7a.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;

import com.facebook.AccessToken;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tasree7a.Enums.Language;
import com.tasree7a.ThisApplication;

import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static com.tasree7a.utils.PermissionCode.MY_PERMISSIONS_REQUEST_LOCATION;

/**
 * Created by mac on 6/14/17.
 */

public class AppUtil {

    public static int checkPermissionStatus(String permission) {

        return ActivityCompat.checkSelfPermission(ThisApplication.getCurrentActivity(), permission);

    }

    public static void restartApp(){

        Intent i =  ThisApplication.getCurrentActivity().getApplicationContext().getPackageManager()
                .getLaunchIntentForPackage( ThisApplication.getCurrentActivity().getApplicationContext().getPackageName() );

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        ThisApplication.getCurrentActivity().getApplicationContext().startActivity(i);

    }

    public static void checkAppLanguage(){

        Resources res =  ThisApplication.getCurrentActivity().getApplicationContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();

        android.content.res.Configuration conf = res.getConfiguration();

        Language lang = UserDefaultUtil.getAppLanguage();

        conf.locale = new Locale(lang == null ? Language.EN.toString() : lang.toString());

        res.updateConfiguration(conf, dm);

    }

    public static boolean isLoggedIn() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        return accessToken != null || UserDefaultUtil.getCurrentUser() != null;
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

    public static boolean isLocationServiceEnabled(){

        LocationManager lm = (LocationManager)ThisApplication.getCurrentActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {

            return false;

        } else{

            return true;

        }

    }

    public static boolean checkPlayServices() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        return !(ThisApplication.getCurrentActivity() == null || apiAvailability == null) && apiAvailability.isGooglePlayServicesAvailable(ThisApplication.getCurrentActivity()) == ConnectionResult.SUCCESS;

    }

}
