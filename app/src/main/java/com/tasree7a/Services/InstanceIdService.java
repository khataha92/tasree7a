package com.tasree7a.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by khalid on 1/28/18.
 */

public class InstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = InstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Refreshed token: " + refreshedToken);

    }
}
