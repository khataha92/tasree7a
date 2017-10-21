package com.tasree7a.CustomComponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tasree7a.Enums.FilterType;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.Checkable;

/**
 * Created by mac on 8/17/17.
 */

public class CircularCheckBox extends LinearLayout implements Checkable {

    boolean isChecked = false;

    TextView textView;

    public CircularCheckBox(Context context) {
        super(context);

        init(null);
    }

    public CircularCheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CircularCheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void  init(AttributeSet attrs){

        setBackgroundResource(R.drawable.circular_image);

        setGravity(Gravity.CENTER);

        textView = new TextView(getContext());

        textView.setGravity(Gravity.CENTER);

        textView.setTextColor(getContext().getResources().getColor(R.color.APP_TEXT_COLOR));

        textView.setText("SAT");

        textView.setTextSize(10);

        addView(textView);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(64,64);

        setLayoutParams(params);

        if (attrs != null) {

            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircularCheckBox);

            for (int i = 0; i < a.getIndexCount(); ++i) {

                int attr = a.getIndex(i);

                switch (attr) {

                    case R.styleable.CircularCheckBox_setChecked:

                        boolean isChecked = a.getBoolean(attr,false);

                        if(isChecked) {

                            check();

                        } else{

                            uncheck();

                        }

                        break;

                    case R.styleable.CircularCheckBox_customCheckboxText:

                        String text = a.getString(attr);

                        textView.setText(text);

                        break;


                }
            }

            a.recycle();

        }

    }


    public boolean isChecked() {

        return isChecked;
    }


    @Override
    public void check() {

        isChecked = true;

        setBackgroundResource(R.drawable.circular_btn);

        textView.setTextColor(getContext().getResources().getColor(R.color.WHITE));

    }

    @Override
    public void uncheck() {

        isChecked = false;

        setBackgroundResource(R.drawable.circular_image);

        textView.setTextColor(getContext().getResources().getColor(R.color.APP_TEXT_COLOR));

    }
}
