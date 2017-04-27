package com.tasree7a;

import android.app.Activity;
import android.app.Application;

import com.tasree7a.utils.UserDefaultUtil;

/**
 * Created by KhalidTaha on 4/20/17.
 */

public class ThisApplication extends Application {

    private static Activity currentActivity;

    public static void setCurrentActivity(Activity currentActivity) {
        ThisApplication.currentActivity = currentActivity;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        UserDefaultUtil.init();
    }
}
