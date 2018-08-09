package com.tasree7a.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.HomeActivity;
import com.tasree7a.activities.ResetPasswordActivity;
import com.tasree7a.activities.SignupActivity;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.enums.LoginType;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.login.LoginModel;
import com.tasree7a.models.login.LoginResponseModel;
import com.tasree7a.models.login.User;
import com.tasree7a.models.signup.SignupResponseModel;
import com.tasree7a.utils.FragmentArg;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.tasree7a.ThisApplication.callbackManager;

public class BaseLoginFragment extends com.tasree7a.fragments.BaseFragment implements View.OnClickListener {

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
        signup = rootView.findViewById(R.id.sign_up);
        rootView.findViewById(R.id.forgot_password).setOnClickListener(v -> {
            Intent intent = new Intent(ThisApplication.getCurrentActivity(), ResetPasswordActivity.class);
            startActivity(intent);
        });

        signup.setOnClickListener(this);
        rootView.findViewById(R.id.new_user).setOnClickListener(this);
        login = rootView.findViewById(R.id.login_with_fb);
        login.setOnClickListener(this);
        normalLogin = rootView.findViewById(R.id.normal_login_button);
        normalLogin.setOnClickListener(this);
        loginButton = rootView.findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        if (isBusiness) {
            login.setVisibility(View.GONE);
            rootView.findViewById(R.id.or_container).setVisibility(View.GONE);
            rootView.findViewById(R.id.container).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                isBusiness = false;

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            Log.v("LoginActivity", response.toString());

                            // Application code
                            try {
                                com.tasree7a.models.signup.SignupModel signupModel = new com.tasree7a.models.signup.SignupModel();
                                String name = object.getString("name");
                                String email = object.getString("email");
                                String fbId = object.getString("id");
                                String fbImage = "https://graph.facebook.com/" + fbId + "/picture?type=large";
                                ReservationSessionManager.getInstance().setFbImage(fbImage);
                                String[] names = name.split(" ");
                                String firstName = names[0];
                                String lastName = "";

                                if (names.length > 1) {
                                    lastName = names[names.length - 1];
                                }

                                signupModel.setFirstName(firstName);
                                signupModel.setLastName(lastName);
                                signupModel.setFbLogin(true);
                                signupModel.setFbId(fbId);
                                signupModel.setEmail(email);
                                signupModel.setUsername("");
                                RetrofitManager.getInstance().register(signupModel, (isSuccess, result) -> {
                                    if (isSuccess) {
                                        SignupResponseModel model = (SignupResponseModel) result;
                                        User user = model.getUserDetails().getUser();
                                        user.setFacebook(true);
                                        UserDefaultUtil.saveUser(user);
                                        startHomeActivity();
                                    } else {
                                        Toast.makeText(getContext(), "Error in login, please try again later", Toast.LENGTH_LONG).show();
                                    }
                                    login.hideLoader();
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error in login, please try again later", Toast.LENGTH_LONG).show();
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
//                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
                login.showLoader();
                loginButton.callOnClick();
                break;

            case R.id.new_user:
            case R.id.sign_up:
                startActivity(new Intent(ThisApplication.getCurrentActivity(), SignupActivity.class));
                break;

            case R.id.normal_login_button:
                EditText email = rootView.findViewById(R.id.input_email);
                EditText password = rootView.findViewById(R.id.input_password);
                String emailStr = email.getText().toString();
                String pass = password.getText().toString();
                if (!emailStr.isEmpty() && !pass.isEmpty()) {

                    model = new LoginModel();
                    model.setUsername(emailStr);
                    model.setPassword(pass);
                    model.setFacebookLogin(false);
                    model.setBusiness(isBusiness);
                    UIUtils.showSweetLoadingDialog();

                    normalLogin.showLoader();

                    RetrofitManager.getInstance().login(model, (isSuccess, result) -> {
                        UIUtils.hideSweetLoadingDialog();
                        if (isSuccess) {
                            LoginResponseModel responseModel = (LoginResponseModel) result;
                            if (responseModel.getResponseCode().equalsIgnoreCase("200")) {
                                User user = responseModel.getUser();
                                user.setBusiness(isBusiness ? 1 : 0);
                                user.setSalonId(responseModel.getSalonId());
                                UserDefaultUtil.saveUser(user);
                                startHomeActivity();
                            } else {
                                showLoginError();
                            }
                        } else {
                            showLoginError();
                        }

                        normalLogin.hideLoader();
                    });
                }
                break;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void showLoginError() {
        Toasty.error(getContext(), getString(R.string.login_error), Toast.LENGTH_LONG).show();
    }

    private void startHomeActivity() {
        if (!isBusiness) {
            startActivity(new Intent(ThisApplication.getCurrentActivity(), HomeActivity.class));
        } else {
            //TODO: Start Salon Details later
            Intent intent = new Intent(ThisApplication.getCurrentActivity(), HomeActivity.class);
            intent.putExtra(FragmentArg.IS_BUSINESS, isBusiness);
            startActivity(intent);
        }

        if (ThisApplication.getCurrentActivity() != null)
            ThisApplication.getCurrentActivity().finishAffinity();
    }
}