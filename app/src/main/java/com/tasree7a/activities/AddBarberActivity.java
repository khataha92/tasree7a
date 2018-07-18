package com.tasree7a.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.tasree7a.R;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.models.AddNewBarberRequestModel;
import com.tasree7a.models.salondetails.SalonModel;

import java.util.Date;

public class AddBarberActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 4567;
    public static final String SALON_MODEL = AddBarberActivity.class.getName() + "SALON_MODEL";
    public static final String BARBER_MODEL = AddBarberActivity.class.getName() + " BARBER_MODEL";

    private SalonModel mSalonModel;

    private EditText mBarberName;
    private EditText mBarberEmail;
    private EditText mBarberPassword;
    private EditText mBarberPasswordConfirmation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salon_barber);

        if (getIntent() != null) {
            mSalonModel = (SalonModel) getIntent().getSerializableExtra(SALON_MODEL);
        } else {
            throw new RuntimeException("Missing parameter;");
        }

        initViews();
    }

    private void initViews() {
        mBarberName = findViewById(R.id.barber_name);
        mBarberEmail = findViewById(R.id.barber_email);
        mBarberPassword = findViewById(R.id.barber_password);
        mBarberPasswordConfirmation = findViewById(R.id.barber_password_confirmation);

        CustomButton mSaveBarberBtn = findViewById(R.id.add_barber);
        CustomButton mCancelAddingBarberBtn = findViewById(R.id.cancel_barber);

        mSaveBarberBtn.setOnClickListener(v -> {
            if (isDataValid()) {
                Intent resultIntent = new Intent();
                AddNewBarberRequestModel barberModel = new AddNewBarberRequestModel();
                barberModel.setCreatedAt(new Date().toString());
                barberModel.setEmail(mBarberEmail.getText().toString());
                barberModel.setFirstName(mBarberName.getText().toString().split(" ")[0]);
                barberModel.setLastName(mBarberName.getText().toString().split(" ")[1]);
                barberModel.setPass(mBarberPassword.getText().toString());
//                barberModel.setSalonId(mSalonModel.getId());
                barberModel.setStartTime("");
                barberModel.setEndTime("");
                barberModel.setUserName(""); //TODO: what is this for;;

                resultIntent.putExtra(BARBER_MODEL, barberModel);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        mCancelAddingBarberBtn.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }

    private boolean isDataValid() {
        if (TextUtils.isEmpty(mBarberName.getText()) || (mBarberName.getText().toString().split(" ").length == 1)) {
            Toast.makeText(this, R.string.error_provide_full_name, Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(mBarberEmail.getText())) {
            Toast.makeText(this, R.string.error_provide_email, Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(mBarberPassword.getText())) {
            Toast.makeText(this, R.string.error_provide_password, Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(mBarberPasswordConfirmation.getText()) || !mBarberPassword.getText().toString().equalsIgnoreCase(mBarberPasswordConfirmation.getText().toString())) {
            Toast.makeText(this, R.string.error_password_not_match, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
