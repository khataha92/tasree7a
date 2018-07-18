package com.tasree7a.activities;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.observables.PermissionGrantedObservable;
import com.tasree7a.utils.AppUtil;
import com.tasree7a.utils.PermissionCode;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.Objects;

import static com.tasree7a.AvailableTimesHelper.prepareAvailableTimes;


/**
 * Created by mac on 5/4/17.
 */

public class HomeActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThisApplication.setCurrentActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppUtil.checkAppLanguage();
        //noinspection ResultOfMethodCallIgnored
        prepareAvailableTimes(0);
        boolean isBusiness = UserDefaultUtil.isBusinessUser();
        if (!isBusiness) {
            FragmentManager.showHomeFragment();
            UIUtils.hideSweetLoadingDialog();
        } else {
            FragmentManager.showSalonDetailsFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionCode.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        if (FragmentManager.getCurrentVisibleFragment() != null) {
            Objects.requireNonNull(FragmentManager.getCurrentVisibleFragment()).onBackPressed();
        }
    }
}
