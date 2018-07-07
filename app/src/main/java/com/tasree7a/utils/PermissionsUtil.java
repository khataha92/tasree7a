package com.tasree7a.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.AddNewServiceRequestModel;
import com.tasree7a.observables.ServicesChangedObservable;

import java.io.File;
import java.util.Objects;

public class PermissionsUtil {

    public static boolean isPermessionGranted(Activity context, String permission) {
        return ContextCompat.checkSelfPermission(Objects.requireNonNull(context),
                permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void grantPermession(Activity context, String permission, int permissionRequestCode) {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                permission)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(context,
                    new String[]{permission},
                    permissionRequestCode);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }
}
