package com.tasree7a;

import android.app.Application;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

import com.facebook.CallbackManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by KhalidTaha on 4/20/17.
 */

public class ThisApplication extends Application {

    private static ExecutorService nonUIThread;

    private static FragmentActivity currentActivity;

    public static CallbackManager callbackManager;

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
        Mapbox.getInstance(this, "pk.eyJ1Ijoia2hsZWFmc2FtaSIsImEiOiJjamtsamt3NXAxdmlnM3ZvZ2N1bWo4eTY1In0.EnxUjz3HDOrjuNr0qtgUHQ");

    }

    public static ExecutorService getNonUIThread() {

        if (nonUIThread == null) {

            if (Build.VERSION.SDK_INT  >= 21) {

                nonUIThread = new ForkJoinPool(Runtime.getRuntime().availableProcessors(),
                        ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                        null, true);

            } else {

                nonUIThread = Executors.newCachedThreadPool();

            }

        }

        return nonUIThread;
    }
}
