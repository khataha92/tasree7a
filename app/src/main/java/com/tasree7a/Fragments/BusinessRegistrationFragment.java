package com.tasree7a.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.SalonInformationActivity;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.login.User;
import com.tasree7a.models.signup.SignupResponseModel;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by SamiKhleaf on 7/28/17.
 */

public class BusinessRegistrationFragment extends BaseFragment implements View.OnClickListener {

    EditText fullName;

    EditText username;

    EditText inputEmail;

    EditText inputPassword;

    EditText ownerNAme;

    CustomButton register;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_business_registration, container, false);
        fullName = rootView.findViewById(R.id.input_full_name);
        username = rootView.findViewById(R.id.input_username);
        inputEmail = rootView.findViewById(R.id.input_email);
        inputPassword = rootView.findViewById(R.id.input_password);
        register = rootView.findViewById(R.id.register);
        register.setOnClickListener(this);

        RelativeLayout login = rootView.findViewById(R.id.login_container);
        login.setOnClickListener(this);
        // Inflate the layout for this fragment
        return rootView;
    }

    private void signup() {
        try {
            register.showLoader();
            String salonName = fullName.getText().toString();
            String username = this.username.getText().toString();
            String password = inputPassword.getText().toString();
            String email = inputEmail.getText().toString();

            if (!salonName.isEmpty() && (!username.isEmpty()) && !password.isEmpty()) {

                com.tasree7a.models.signup.SignupModel model = new com.tasree7a.models.signup.SignupModel();
                model.setUsername(username);
                model.setPassword(password);
                model.setEmail(email);
                model.setFbLogin(false);
                model.setFirstName(salonName.split(" ")[0]);
                model.setLastName(salonName.split(" ")[1]);
                model.setFbLogin(false);
                model.setBuisness(true);

                UIUtils.showSweetLoadingDialog();

                RetrofitManager.getInstance().register(model, (isSuccess, result) -> {
                    UIUtils.hideSweetLoadingDialog();
                    if (isSuccess) {
                        UserDefaultUtil.saveRegesteringUser(model);
                        SignupResponseModel signupResponseModel = (SignupResponseModel) result;
                        User user = signupResponseModel.getUserDetails().getUser();
                        UserDefaultUtil.saveUserToken(user.getUserToken());
                        UserDefaultUtil.saveUser(user);
                        UserDefaultUtil.setIsRegestering(true);
                        SalonInformationActivity.launch(this,
                                salonName,
                                email,
                                username,
                                false);
                        //noinspection ConstantConditions
                        getActivity().finish();
                    }
                });
            } else {
                //noinspection ConstantConditions
                Toasty.error(getContext(), "Missing required Fields", Toast.LENGTH_LONG).show();
            }

            register.hideLoader();
        } catch (Exception e) {
            signUpError();
        }

    }

    @SuppressWarnings("ConstantConditions")
    private void signUpError() {
        Toasty.error(getContext(), getString(R.string.salon_sign_up_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_container:
                ThisApplication.getCurrentActivity().finish();
                break;
            case R.id.register:
                signup();
                break;
        }
    }
}