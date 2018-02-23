package com.tasree7a.activities;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.Observables.PermissionGrantedObservable;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.AppUtil;
import com.tasree7a.utils.PermissionCode;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import static com.tasree7a.AvailableTimesHelper.prepareAvailableTimes;


/**
 * Created by mac on 5/4/17.
 */

public class HomeActivity extends FragmentActivity {

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        ThisApplication.setCurrentActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppUtil.checkAppLanguage();
        prepareAvailableTimes(0);
        boolean isBusiness = UserDefaultUtil.isBusinessUser();
        if (!isBusiness) {
            FragmentManager.showHomeFragment();
            findViewById(R.id.loading).setVisibility(View.GONE);
            UIUtils.hideSweetLoadingDialog();
        } else {
            findViewById(R.id.loading).setVisibility(View.VISIBLE);
            String salonID = UserDefaultUtil.getCurrentUser().getId();
            if (salonID != null
                && salonID.equalsIgnoreCase("-1")) {
                findViewById(R.id.loading).setVisibility(View.GONE);
                FragmentManager.showSalonInfoFragment(true);
            } else {
                requestSalonDetails();
            }
        }
    }

    private void requestSalonDetails() {
        RetrofitManager.getInstance().getSalonDetails(UserDefaultUtil.getCurrentUser().getSalonId(), new AbstractCallback() {
            @Override
            public void onResult (boolean isSuccess, Object result) {
                findViewById(R.id.loading).setVisibility(View.GONE);
                if (isSuccess
                        && result != null) {
                    SalonModel salonModel = (SalonModel) result;
                    salonModel.setBusiness(true);
                    FragmentManager.showSalonDetailsFragment(salonModel, true);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
    protected void onResume () {
        super.onResume();
        ThisApplication.setCurrentActivity(this);
    }

    @Override
    public void onBackPressed () {
        if (FragmentManager.getCurrentVisibleFragment() != null) {
            FragmentManager.getCurrentVisibleFragment().onBackPressed();
        }
    }
}
