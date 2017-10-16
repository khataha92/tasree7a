package com.tasree7a.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.tasree7a.Enums.UserDefaultKeys;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.SalonDetails.SalonDetailsResponseModel;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.Observables.PermissionGrantedObservable;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.FragmentArg;
import com.tasree7a.utils.PermissionCode;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

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

        boolean isBusiness = UserDefaultUtil.getCurrentUser().isBusiness();

        if(getIntent().getExtras() != null) {

            isBusiness = getIntent().getExtras().getBoolean(FragmentArg.IS_BUSINESS);

        }

        if(!isBusiness) {

            FragmentManager.showHomeFragment();

            findViewById(R.id.loading).setVisibility(View.GONE);

            UIUtils.hideSweetLoadingDialog();

        } else {

            findViewById(R.id.loading).setVisibility(View.VISIBLE);

            RetrofitManager.getInstance().getSalonDetails(UserDefaultUtil.getCurrentUser().getSalongId(), new AbstractCallback() {
                @Override
                public void onResult(boolean isSuccess, Object result) {

                    findViewById(R.id.loading).setVisibility(View.GONE);

                    if(isSuccess) {

                        SalonModel salonModel = (SalonModel) result;

                        salonModel.setBusiness(true);

                        FragmentManager.showSalonDetailsFragment(salonModel, true);

                    }

                }
            });
        }


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
