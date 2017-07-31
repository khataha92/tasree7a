package com.tasree7a.CustomComponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tasree7a.Enums.FilterType;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.Checkable;

/**
 * Created by mac on 6/13/17.
 */

public class CustomCheckbox extends LinearLayout implements Checkable {

    TextView text;

    RadioButton radio;

    FilterType filterType;

    public CustomCheckbox(Context context) {
        super(context);

        init(null);
    }

    public CustomCheckbox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CustomCheckbox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs){

        LayoutInflater.from(getContext()).inflate(R.layout.custom_radio_button,this);

        text = (TextView) findViewById(R.id.text);

        radio = (RadioButton) findViewById(R.id.radio);

        radio.setClickable(false);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                radio.setChecked(!radio.isChecked());

            }
        });

        if (attrs != null) {

            TypedArray a = ThisApplication.getCurrentActivity().obtainStyledAttributes(attrs, R.styleable.CustomRadioButton);

            for (int i = 0; i < a.getIndexCount(); ++i) {

                int attr = a.getIndex(i);

                switch (attr) {

                    case R.styleable.CustomRadioButton_text:

                        String string = a.getString(attr);

                        text.setText(string);

                        break;

                    case R.styleable.CustomRadioButton_FilterType:

                        setFilterType(FilterType.valueOf(a.getInteger(attr,3)));

                        break;


                }
            }

            a.recycle();

        }

    }

    @Override
    public void check() {

        radio.setChecked(true);

    }

    @Override
    public void uncheck() {

        radio.setChecked(false);

    }

    public boolean isChecked(){

        return radio.isChecked();
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public FilterType getFilterType() {
        return filterType;
    }
}
