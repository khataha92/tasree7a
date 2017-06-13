package com.tasree7a;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

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
