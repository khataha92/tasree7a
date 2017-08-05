package com.tasree7a.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Observables.PermissionGrantedObservable;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.utils.PermissionCode;
import com.tasree7a.utils.UIUtils;

import static com.tasree7a.utils.UIUtils.hideSweetLoadingDialog;

/**
 * Created by mac on 5/4/17.
 */

public class HomeActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThisApplication.setCurrentActivity(this);

        setContentView(R.layout.activity_home);

        FragmentManager.showHomeFragment();

        UIUtils.hideSweetLoadingDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case PermissionCode.MY_PERMISSIONS_REQUEST_LOCATION:

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    PermissionGrantedObservable.getInstance().notifyPermissionGranted(permissions[0]);

                }

                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ThisApplication.setCurrentActivity(this);
    }

    @Override
    public void onBackPressed() {

        if(FragmentManager.getCurrentVisibleFragment() != null){

            FragmentManager.getCurrentVisibleFragment().onBackPressed();

        }
    }
}
