package com.tasree7a.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tasree7a.R;
import com.crashlytics.android.Crashlytics;
import com.tasree7a.ThisApplication;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

        ThisApplication.setCurrentActivity(this);

        setContentView(R.layout.activity_main);

//        Log.d("status",""+ (1/0));
    }
}
