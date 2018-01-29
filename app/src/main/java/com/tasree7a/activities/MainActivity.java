package com.tasree7a.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tasree7a.Adapters.ViewPagerAdapter;
import com.tasree7a.Enums.Language;
import com.tasree7a.Enums.LoginType;
import com.tasree7a.Fragments.BaseLoginFragment;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.utils.AppUtil;
import com.tasree7a.utils.UserDefaultUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

import static com.facebook.FacebookSdk.sdkInitialize;
import static com.tasree7a.ThisApplication.callbackManager;

public class MainActivity extends FragmentActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    TabLayout tabs;

    ViewPager tabsPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        Fabric.with(this, new Crashlytics());

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.tasree7a",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        sdkInitialize(getApplicationContext());

        AppEventsLogger.activateApp(this);

        ThisApplication.setCurrentActivity(this);

        if (AppUtil.isLoggedIn()) {

            startHomeActivity();
        }

        setContentView(R.layout.activity_main);

        initTabsView();

        AppUtil.checkAppLanguage();

        String token = FirebaseInstanceId.getInstance().getToken();

//        Log.d(TAG, token);

        if (UserDefaultUtil.getAppLanguage() == Language.AR && UserDefaultUtil.getUserLanguage() == Language.AR)
            ThisApplication.getCurrentActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        else
            ThisApplication.getCurrentActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

    }


    private void startHomeActivity() {

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);

        startActivity(intent);

        finish();

    }


    @Override
    protected void onResume() {

        super.onResume();

        ThisApplication.setCurrentActivity(this);
    }


    public void setUpViewPager(ViewPager tabsViewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new BaseLoginFragment(LoginType.USER), getApplicationContext().getResources().getString(R.string.CUSTOMER_TAB_TITLE));

        adapter.addFragment(new BaseLoginFragment(LoginType.BUSINESS), getApplicationContext().getResources().getString(R.string.BUSINESS_TAB_TITLE));

        tabsViewPager.setAdapter(adapter);

    }


    private void initTabsView() {


        tabs = (TabLayout) findViewById(R.id.tabs);

        tabsPager = (ViewPager) findViewById(R.id.viewpager);

        setUpViewPager(tabsPager);

        tabs.setupWithViewPager(tabsPager);

        findViewById(R.id.tabs).bringToFront();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabsPager.setCurrentItem(tab.getPosition());

            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }


            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (callbackManager != null) {

            callbackManager.onActivityResult(requestCode,
                    resultCode, data);

        }
    }
}
