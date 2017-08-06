package com.tasree7a.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.Enums.LoginType;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.Login.LoginModel;
import com.tasree7a.Models.Login.LoginResponseModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.HomeActivity;
import com.tasree7a.activities.MainActivity;
import com.tasree7a.activities.SignupActivity;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import org.json.JSONObject;

import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by SamiKhleaf on 8/4/17.
 */

public class BaseLoginFragment extends BaseFragment implements View.OnClickListener {

    CallbackManager callbackManager;

    CustomButton login;

    LoginButton loginButton;

    CustomButton normalLogin;

    TextView signup;

    LoginModel model;

    boolean isBusiness;


    public BaseLoginFragment() {

    }


    @SuppressLint("ValidFragment")
    public BaseLoginFragment(LoginType type) {

        isBusiness = type == LoginType.BUSINESS;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_base_login, container, false);

        signup = (TextView) rootView.findViewById(R.id.sign_up);

        signup.setOnClickListener(this);

        login = (CustomButton) rootView.findViewById(R.id.login_with_fb);

        login.setOnClickListener(this);

        normalLogin = (CustomButton) rootView.findViewById(R.id.normal_login_button);

        normalLogin.setOnClickListener(this);

        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);

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

                                    model = new LoginModel();

                                    model.setUsername(object.getString("email"));

//                                    String birthday = object.getString("birthday");

                                    model.setFacebookLogin(true);

                                    model.setBusiness(isBusiness);

                                    UserDefaultUtil.saveLogedUser(model);

                                    startHomeActivity();

                                } catch (Exception e) {

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

                Log.d("status", "cancel");
            }


            @Override
            public void onError(FacebookException error) {

                Log.d("status", "error");

                error.printStackTrace();

            }

        });

        return rootView;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_with_fb:

                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));

                break;

            case R.id.sign_up:

                startActivity(new Intent(ThisApplication.getCurrentActivity(), SignupActivity.class));

                break;

            case R.id.normal_login_button:

                EditText email = (EditText) rootView.findViewById(R.id.input_email);

                EditText password = (EditText) rootView.findViewById(R.id.input_password);

                String emailStr = email.getText().toString();

                String pass = password.getText().toString();

                if (!emailStr.isEmpty() && !pass.isEmpty()) {

                    model = new LoginModel();

                    model.setUsername(emailStr);

                    model.setPassword(pass);

                    model.setFacebookLogin(false);

                    model.setBusiness(isBusiness);

                    UserDefaultUtil.saveLogedUser(model);

                    UIUtils.showSweetLoadingDialog();

                    RetrofitManager.getInstance().login(model, new AbstractCallback() {

                        @Override
                        public void onResult(boolean isSuccess, Object result) {

                            UIUtils.hideSweetLoadingDialog();

                            if (isSuccess) {

                                LoginResponseModel responseModel = (LoginResponseModel) result;

                                if (responseModel.getResponseCode().equalsIgnoreCase("200")) {

                                    startHomeActivity();

                                } else {

                                    Toast.makeText(getApplicationContext(), "Error in login ", Toast.LENGTH_LONG).show();

                                }

                            }

                        }
                    });

                } else {


                }

                break;
        }

    }


    private void startHomeActivity() {

        if (!isBusiness) {

            startActivity(new Intent(ThisApplication.getCurrentActivity(), HomeActivity.class));

        } else {

            //TODO: Start Salon Details later
            startActivity(new Intent(ThisApplication.getCurrentActivity(), HomeActivity.class));

        }

        ThisApplication.getCurrentActivity().finishAffinity();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
