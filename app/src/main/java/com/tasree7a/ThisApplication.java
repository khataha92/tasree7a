package com.tasree7a;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.FragmentActivity;

import com.tasree7a.utils.UserDefaultUtil;

/**
 * Created by KhalidTaha on 4/20/17.
 */

public class ThisApplication extends Application {

    private static FragmentActivity currentActivity;

    public static void setCurrentActivity(FragmentActivity currentActivity) {
        ThisApplication.currentActivity = currentActivity;
    }

    public static FragmentActivity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        UserDefaultUtil.init();
    }
}
