package com.tasree7a.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;

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
