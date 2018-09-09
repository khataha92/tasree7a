package com.tasree7a.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.HomeActivity;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.enums.Language;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.ApiError;
import com.tasree7a.models.signup.SignupResponseModel;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import es.dmoral.toasty.Toasty;

/**
 * Created by SamiKhleaf on 7/28/17.
 */

public class CustomerRegistrationFragment extends BaseFragment implements View.OnClickListener {

    EditText fullName;
    EditText username;
    EditText inputEmail;
    EditText inputPassword;
    CustomButton register;

    public CustomerRegistrationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_customer_registration, container, false);
        fullName = rootView.findViewById(R.id.input_full_name);
        username = rootView.findViewById(R.id.input_username);
        inputEmail = rootView.findViewById(R.id.input_email);
        inputPassword = rootView.findViewById(R.id.input_password);
        register = rootView.findViewById(R.id.register);
        register.setOnClickListener(this);

        RelativeLayout login = rootView.findViewById(R.id.login_container);
        login.setOnClickListener(this);
        if (UserDefaultUtil.getAppLanguage() == Language.AR && UserDefaultUtil.getUserLanguage(getContext()) == Language.AR)
            ThisApplication.getCurrentActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        else
            ThisApplication.getCurrentActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void signup() {
        try {
            register.showLoader();
            String firstName = fullName.getText().toString().substring(0, fullName.getText().toString().indexOf(' '));
            String lastName = fullName.getText().toString().substring(fullName.getText().toString().indexOf(' ') + 1);
            String username = this.username.getText().toString();
            String password = inputPassword.getText().toString();
            String email = inputEmail.getText().toString();

            if (!firstName.isEmpty() && !lastName.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                com.tasree7a.models.signup.SignupModel model = new com.tasree7a.models.signup.SignupModel();
                model.setUsername(username);
                model.setPassword(password);
                model.setEmail(email);
                model.setFirstName(firstName);
                model.setLastName(lastName);
                model.setFbLogin(false);
                model.setBuisness(false);
                UIUtils.showSweetLoadingDialog();
                RetrofitManager.getInstance().register(model, (isSuccess, result) -> {
                    UIUtils.hideSweetLoadingDialog();

                    if (isSuccess) {
                        SignupResponseModel signupResponseModel = (SignupResponseModel) result;
                        UserDefaultUtil.saveUser(signupResponseModel.getUserDetails().getUser());
                        UserDefaultUtil.saveUserToken(signupResponseModel.getUserDetails().getUser().getUserToken());
                        startActivity(new Intent(ThisApplication.getCurrentActivity(), HomeActivity.class));
                        ThisApplication.getCurrentActivity().finish();
                    } else {
                        ApiError apiError = (ApiError) result;
                        //noinspection ConstantConditions
                        Toasty.error(getContext(), apiError.getmErrorMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                signUpError();

            }
            register.hideLoader();
        } catch (Exception ignore) {
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void signUpError() {
        Toasty.error(getContext(), getString(R.string.sign_up_error), Toast.LENGTH_LONG).show();
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
