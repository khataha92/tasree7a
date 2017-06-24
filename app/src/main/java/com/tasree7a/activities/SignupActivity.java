package com.tasree7a.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.Signup.SignupModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.StringUtil;
import com.tasree7a.utils.UIUtils;

/**
 * Created by mac on 5/3/17.
 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText fullName;

    EditText username;

    EditText inputEmail;

    EditText inputPassword;

    CustomButton register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        RelativeLayout login = (RelativeLayout) findViewById(R.id.login_container);

        login.setOnClickListener(this);

        fullName = (EditText) findViewById(R.id.input_full_name);

        username = (EditText) findViewById(R.id.input_username);

        inputEmail = (EditText) findViewById(R.id.input_email);

        inputPassword = (EditText) findViewById(R.id.input_password);

        register = (CustomButton) findViewById(R.id.register);

        register.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        ThisApplication.setCurrentActivity(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.login_container:

                finish();

                break;

            case R.id.register:

                signup();

                break;
        }
    }

    private void signup(){

        try {

            String firstName = fullName.getText().toString().substring(0, fullName.getText().toString().indexOf(' '));

            String lastName = fullName.getText().toString().substring(fullName.getText().toString().indexOf(' ') + 1);

            String username = this.username.getText().toString();

            String password = inputPassword.getText().toString();

            String email = inputEmail.getText().toString();

            if(!firstName.isEmpty() && !lastName.isEmpty() && !username.isEmpty() && !password.isEmpty()){

                SignupModel model = new SignupModel();

                model.setUsername(username);

                model.setPassword(password);

                model.setEmail(email);

                model.setFbLogin(false);

                model.setFirstName(firstName);

                model.setLastName(lastName);

                model.setFbLogin(false);

                UIUtils.showSweetLoadingDialog();

                RetrofitManager.getInstance().register(model, new AbstractCallback() {

                    @Override
                    public void onResult(boolean isSuccess, Object result) {

                        UIUtils.hideSweetLoadingDialog();

                        if(isSuccess){

                            startActivity(new Intent(SignupActivity.this,HomeActivity.class));

                            finish();

                        } else{


                        }

                    }
                });


            } else{

                Toast.makeText(this, getString(R.string.SIGN_UP_ERROR_MISSING_INPUT), Toast.LENGTH_SHORT).show();
            }

        } catch (IndexOutOfBoundsException e){

            Toast.makeText(getApplicationContext(),getString(R.string.ERROR_FIRST_NAME_LAST_NAME),Toast.LENGTH_LONG).show();
        }

    }
}
