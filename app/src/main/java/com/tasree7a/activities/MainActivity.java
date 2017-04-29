package com.tasree7a.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.R;
import com.crashlytics.android.Crashlytics;
import com.tasree7a.ThisApplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import io.fabric.sdk.android.Fabric;

import static com.facebook.FacebookSdk.sdkInitialize;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    CallbackManager callbackManager;

    CustomButton login ;

    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

        sdkInitialize(getApplicationContext());

        AppEventsLogger.activateApp(this);

        ThisApplication.setCurrentActivity(this);

        setContentView(R.layout.activity_main);

        login = (CustomButton) findViewById(R.id.login_with_fb);

        login.setOnClickListener(this);

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("status","success");
            }

            @Override
            public void onCancel() {
                Log.d("status","cancel");
            }

            @Override
            public void onError(FacebookException error) {

                Log.d("status","error");

                error.printStackTrace();

            }

        });
        
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.login_with_fb:

                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

                break;
        }

    }
}
