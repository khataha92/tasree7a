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

import com.tasree7a.R;
import com.tsongkha.spinnerdatepicker.TwoDigitFormatter;

import java.lang.reflect.Field;

public class Tasree7aWheel extends LinearLayout {

    private NumberPicker mHoursPicker;
    private NumberPicker mMinuetsPicker;
    private TextView hoursValue;
    private TextView minuetsValue;
    private TextView mAmPm;
    boolean AM = true;
    int hour, min;

    public Tasree7aWheel(Context context) {
        super(context);
        initViews(context);
    }

    public Tasree7aWheel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
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
        View itemView = inflater.inflate(R.layout.availability_wheel, this, true);

        mHoursPicker = itemView.findViewById(R.id.hours);
        mMinuetsPicker = itemView.findViewById(R.id.minutes);
        hoursValue = itemView.findViewById(R.id.hours_value);
        minuetsValue = itemView.findViewById(R.id.minutes_value);
        mAmPm = itemView.findViewById(R.id.am_pm);

        setNumberPickerTextColor(mHoursPicker, R.color.APP_GREEN);
        setNumberPickerTextColor(mMinuetsPicker, R.color.APP_GREEN);

        mHoursPicker.setMinValue(1);
        mHoursPicker.setMaxValue(12);
        mMinuetsPicker.setMinValue(0);
        mMinuetsPicker.setMaxValue(59);

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
                hour = newVal;
            }
        });

        mMinuetsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                oldVal == 59 && newVal == 00 && hour == 12 && AM ? "PM" : "AM"
                if ((oldVal == 59 && newVal == 0 && AM)
                        || (oldVal == 0 && newVal == 59 && AM)) {
                    mAmPm.setText("PM");
                    AM = !AM;
                } else if ((oldVal == 59 && newVal == 0)
                        || (oldVal == 0 && newVal == 59)) {
                    mAmPm.setText("AM");
                    AM = !AM;
                }

                minuetsValue.setText(String.format("%02d", newVal));
            }
        });

    }
}