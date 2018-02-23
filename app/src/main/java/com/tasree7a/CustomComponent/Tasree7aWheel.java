package com.tasree7a.CustomComponent;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.tasree7a.R;
import com.tsongkha.spinnerdatepicker.TwoDigitFormatter;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tasree7aWheel extends LinearLayout {

    private NumberPicker mHoursPicker;
    private NumberPicker mMinuetsPicker;
    private TextView hoursValue;
    private TextView minuetsValue;
    private TextView mAmPm;
    private TextView available;
    boolean AM = true;
    int hour, min;
    private Runnable action;

    public Tasree7aWheel(Context context) {
        super(context);
        initViews(context);
    }

    public Tasree7aWheel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private void initViews(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.availability_wheel, this, true);

        mHoursPicker = itemView.findViewById(R.id.hours);
        mMinuetsPicker = itemView.findViewById(R.id.minutes);
        hoursValue = itemView.findViewById(R.id.hours_value);
        minuetsValue = itemView.findViewById(R.id.minutes_value);
        mAmPm = itemView.findViewById(R.id.am_pm);
        available = itemView.findViewById(R.id.available);
        setNumberPickerTextColor(mHoursPicker, R.color.APP_GREEN);
        setNumberPickerTextColor(mMinuetsPicker, R.color.APP_GREEN);

        mHoursPicker.setMinValue(1);
        mHoursPicker.setMaxValue(12);
        mMinuetsPicker.setMinValue(1);
        mMinuetsPicker.setMaxValue(2);
        mMinuetsPicker.setDisplayedValues(new String[]{"00", "30"});

        mHoursPicker.setFormatter(new TwoDigitFormatter());
        mMinuetsPicker.setFormatter(new TwoDigitFormatter());

        mHoursPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if ((oldVal == 12 && newVal == 1 && AM)
                        || (oldVal == 1 && newVal == 12 && AM)) {
                    mAmPm.setText("PM");
                    AM = !AM;
                } else if ((oldVal == 1 && newVal == 12) ||
                        (oldVal == 12 && newVal == 1)) {
                    mAmPm.setText("AM");
                    AM = !AM;
                }
                hoursValue.setText(String.format("%02d", newVal));
                minuetsValue.setText(String.format("%02d", getMinValue()));
                itemView.findViewById(R.id.selected_time).setVisibility(VISIBLE);
                itemView.findViewById(R.id.select_time).setVisibility(GONE);
                hour = newVal;
                action.run();
            }
        });

        mMinuetsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hoursValue.setText(String.format("%02d", mHoursPicker.getValue()));
                minuetsValue.setText(String.format("%02d", getMinValue(newVal)));
                action.run();
            }
        });

    }

    private int getMinValue() {
        switch (mMinuetsPicker.getValue()) {
            case 2:
                return 30;
            case 1:
            default:
                return 0;

        }
    }

    private int getMinValue(int val) {
        switch (val) {
            case 2:
                return 30;
            case 1:
            default:
                return 0;

        }
    }

    public String getTime() {
//        int time = convertTimeToInt(mHoursPicker.getValue(), mMinuetsPicker.getValue());
//        Calendar c = Calendar.getInstance();
        return String.format("%02d", mHoursPicker.getValue()) + ":" + String.format("%02d", getMinValue()) + " " + mAmPm.getText();//time != 0 ? time : convertTimeToInt(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    private int convertTimeToInt(int hourOfDay, int minute) {
        int temp = 0;
        if (minute >= 30) {
            temp = 1;
        }
        return hourOfDay * 2 + temp;
    }

    public void setAvailable(boolean available) {
        this.available.setTextColor(getResources().getColor(available ? R.color.APP_GREEN : R.color.LIGHT_RED));
        this.available.setText(available ? "Available" : "Not Available");
    }
}