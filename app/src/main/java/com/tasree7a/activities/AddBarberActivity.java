package com.tasree7a.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcsoft.timerangepickerdialog.RangeTimePickerDialog;
import com.tasree7a.R;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.models.AddNewBarberRequestModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.utils.UserDefaultUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AddBarberActivity extends AppCompatActivity implements RangeTimePickerDialog.ISelectedTime {

    public static final int REQUEST_CODE = 4567;
    public static final String SALON_MODEL = AddBarberActivity.class.getName() + "SALON_MODEL";
    public static final String BARBER_MODEL = AddBarberActivity.class.getName() + " BARBER_MODEL";
    public static final String TAG = AddBarberActivity.class.getName() + "_TAG";

    private SalonModel mSalonModel;

    private TextView mFromTime;
    private TextView mToTime;
    private EditText mBarberName;
    private LinearLayout mTimeRangeSelector;

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

    @Override
    public void onSelectedTime(int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
        // Use parameters provided by Dialog
//        Toast.makeText(this, "Start: " + hourStart + ":" + minuteStart + "\nEnd: " + hourEnd + ":" + minuteEnd, Toast.LENGTH_SHORT).show();

        mFromTime.setText(to12HourFormat(String.format(Locale.ENGLISH, "%02d:%02d", hourStart, minuteStart)));
        mToTime.setText(to12HourFormat(String.format(Locale.ENGLISH, "%02d:%02d", hourEnd, minuteEnd)));
    }

    private String to12HourFormat(String time) {

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            return new SimpleDateFormat("K:mm aa", Locale.ENGLISH).format(sdf.parse(time));

        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initViews() {
        mFromTime = findViewById(R.id.from_hours);
        mToTime = findViewById(R.id.to_hours);
        mBarberName = findViewById(R.id.barber_name);
        mTimeRangeSelector = findViewById(R.id.from_to);

        CustomButton mSaveBarberBtn = findViewById(R.id.add_barber);
        CustomButton mCancelAddingBarberBtn = findViewById(R.id.cancel_barber);

        mTimeRangeSelector.setOnClickListener(v -> {
            RangeTimePickerDialog dialog = new RangeTimePickerDialog();
            dialog.newInstance(R.color.CyanWater, R.color.White, R.color.Yellow, R.color.colorPrimary, true);
            dialog.setIs24HourView(false);
            FragmentManager fragmentManager = getFragmentManager();
            dialog.show(fragmentManager, TAG);
        });

        mSaveBarberBtn.setOnClickListener(v -> {
            if (isDataValid()) {
                Intent resultIntent = new Intent();
                AddNewBarberRequestModel barberModel = new AddNewBarberRequestModel();
                barberModel.setCreatedAt(new Date().toString());
                barberModel.setFirstName(mBarberName.getText().toString().split(" ")[0]);
                barberModel.setLastName(mBarberName.getText().toString().split(" ")[1]);
                barberModel.setSalonId((UserDefaultUtil.getCurrentSalonUser().getId()));
                barberModel.setEmail("static@email.com");
                barberModel.setUserName("staticUserName" + new Random().nextInt());
                barberModel.setStartTime(mFromTime.getText().toString());
                barberModel.setEndTime(mToTime.getText().toString());

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
        }
        return true;
    }
}
