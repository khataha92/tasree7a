package com.tasree7a.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.Signup.SignupModel;
import com.tasree7a.Models.Signup.SignupResponseModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.HomeActivity;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import static com.facebook.FacebookSdk.getApplicationContext;

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

        fullName = (EditText) rootView.findViewById(R.id.input_full_name);

        username = (EditText) rootView.findViewById(R.id.input_username);

        inputEmail = (EditText) rootView.findViewById(R.id.input_email);

        inputPassword = (EditText) rootView.findViewById(R.id.input_password);

        register = (CustomButton) rootView.findViewById(R.id.register);

        register.setOnClickListener(this);


        RelativeLayout login = (RelativeLayout) rootView.findViewById(R.id.login_container);

        login.setOnClickListener(this);

        // Inflate the layout for this fragment
        return rootView;
    }


    private void signup() {

        try {

            String firstName = fullName.getText().toString().substring(0, fullName.getText().toString().indexOf(' '));

            String lastName = fullName.getText().toString().substring(fullName.getText().toString().indexOf(' ') + 1);

            String username = this.username.getText().toString();

            String password = inputPassword.getText().toString();

            String email = inputEmail.getText().toString();

            if (!firstName.isEmpty() && !lastName.isEmpty() && !username.isEmpty() && !password.isEmpty()) {

                SignupModel model = new SignupModel();

                model.setUsername(username);

                model.setPassword(password);

                model.setEmail(email);

                model.setFbLogin(false);

                model.setFirstName(firstName);

                model.setLastName(lastName);

                model.setFbLogin(false);

                model.setBuisness(false);

                UIUtils.showSweetLoadingDialog();

                RetrofitManager.getInstance().register(model, new AbstractCallback() {

                    @Override
                    public void onResult(boolean isSuccess, Object result) {

                        UIUtils.hideSweetLoadingDialog();

                        if (isSuccess) {

                            SignupResponseModel signupResponseModel = (SignupResponseModel) result;

                            UserDefaultUtil.saveUser(signupResponseModel.getUser());

                            startActivity(new Intent(ThisApplication.getCurrentActivity(), HomeActivity.class));
                            
                            ThisApplication.getCurrentActivity().finish();

                        } else {


                        }

                    }
                });


            } else {

                Toast.makeText(ThisApplication.getCurrentActivity(), getString(R.string.SIGN_UP_ERROR_MISSING_INPUT), Toast.LENGTH_SHORT).show();
            }

        } catch (IndexOutOfBoundsException e) {

            Toast.makeText(getApplicationContext(), getString(R.string.ERROR_FIRST_NAME_LAST_NAME), Toast.LENGTH_LONG).show();
        }

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
