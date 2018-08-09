package com.tasree7a.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.adapters.ViewPagerAdapter;
import com.tasree7a.enums.Language;
import com.tasree7a.enums.LoginType;
import com.tasree7a.fragments.BaseLoginFragment;
import com.tasree7a.models.login.User;
import com.tasree7a.models.signup.SignupModel;
import com.tasree7a.utils.AppUtil;
import com.tasree7a.utils.UserDefaultUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

import static com.facebook.FacebookSdk.sdkInitialize;
import static com.tasree7a.ThisApplication.callbackManager;

public class MainActivity extends FragmentActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    TabLayout tabs;
    ViewPager tabsPager;


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key_Hash", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        printKeyHash(this);
        initializations();
        setContentView(R.layout.activity_main);
        initTabsView();
        AppUtil.checkAppLanguage(this);

        if ((UserDefaultUtil.getAppLanguage() == Language.AR) && UserDefaultUtil.getUserLanguage(this) == Language.AR)
            ThisApplication.getCurrentActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        else
            ThisApplication.getCurrentActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
    }

    private void initializations() {
        Fabric.with(this, new Crashlytics());

        sdkInitialize(getApplicationContext());

        AppEventsLogger.activateApp(this);

        ThisApplication.setCurrentActivity(this);

        if (AppUtil.isLoggedIn()) {
            startHomeActivity();
        }
    }

    private void startHomeActivity() {
        SignupModel regUser = UserDefaultUtil.getRegisteringUser();
        User user = UserDefaultUtil.getCurrentUser();
        String salonID = user.getSalonId();
        if (UserDefaultUtil.isBusinessUser()
                && (salonID == null
                || salonID.equalsIgnoreCase("-1"))) {
            SalonInformationActivity.launch(this,
                    String.format(Locale.ENGLISH, "%s %s", regUser.getFirstName(), regUser.getLastName()),
                    regUser.getEmail(),
                    regUser.getUsername(),
                    false);
            finish();
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
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

        tabs = findViewById(R.id.tabs);

        tabsPager = findViewById(R.id.viewpager);

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

            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }
}
