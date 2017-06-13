package com.tasree7a.CustomComponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tasree7a.Enums.FontsType;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.Checkable;
import com.tasree7a.interfaces.SingleCheckableGroup;
import com.tasree7a.utils.UIUtils;

/**
 * Created by mac on 6/13/17.
 */

public class CustomRadioButton extends LinearLayout implements Checkable {

    TextView text;

    RadioButton radio;


    public CustomRadioButton(Context context) {
        super(context);

        init(null);
    }

    public CustomRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CustomRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

                View view = (View) getParent();

                if(view instanceof SingleCheckableGroup){

                    ((SingleCheckableGroup) view).onItemChecked(CustomRadioButton.this);
                }

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
}
