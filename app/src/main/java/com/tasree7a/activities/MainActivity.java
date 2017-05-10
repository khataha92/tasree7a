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
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.R;
import com.crashlytics.android.Crashlytics;
import com.tasree7a.ThisApplication;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import io.fabric.sdk.android.Fabric;

import static com.facebook.FacebookSdk.sdkInitialize;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    CallbackManager callbackManager;

    CustomButton login ;

    LoginButton loginButton;

    TextView signup;

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

        setContentView(R.layout.activity_main);

        signup = (TextView) findViewById(R.id.sign_up);

        signup.setOnClickListener(this);

        login = (CustomButton) findViewById(R.id.login_with_fb);

        login.setOnClickListener(this);

        loginButton = (LoginButton) findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {

                                    String email = object.getString("email");
                                    String birthday = object.getString("birthday");

                                    startActivity(new Intent(MainActivity.this,HomeActivity.class));

                                    finish();

                                } catch (Exception e){

                                    e.printStackTrace();

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields",
                        "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
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

                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email","user_birthday"));

                break;

            case R.id.sign_up:

                startActivity(new Intent(this,SignupActivity.class));

                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        ThisApplication.setCurrentActivity(this);
    }
}
