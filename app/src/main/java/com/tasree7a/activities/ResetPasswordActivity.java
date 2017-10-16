package com.tasree7a.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.R;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.StringUtil;
import com.tasree7a.utils.UIUtils;

/**
 * Created by mac on 9/30/17.
 */

public class ResetPasswordActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);

        init();
    }

    private void init() {

        final EditText email = (EditText) findViewById(R.id.email);

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailAddress = email.getText().toString();

                if(!StringUtil.isValidEmail(emailAddress) || Strings.isNullOrEmpty(emailAddress)) {

                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.INVALID_EMAIL), Toast.LENGTH_SHORT).show();

                    return;

                }

                RetrofitManager.getInstance().resetPassword(emailAddress, new AbstractCallback() {
                    @Override
                    public void onResult(boolean isSuccess, Object result) {

                    }
                });

            }
        });
    }
}
