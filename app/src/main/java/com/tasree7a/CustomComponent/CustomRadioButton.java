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

import com.tasree7a.Enums.CustomOrientation;
import com.tasree7a.Enums.FilterType;
import com.tasree7a.Enums.SortType;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.Checkable;
import com.tasree7a.interfaces.SingleCheckableGroup;

/**
 * Created by mac on 6/13/17.
 */

public class CustomRadioButton extends LinearLayout implements Checkable {

    TextView text;

    RadioButton radio;

    SortType sortType;

    FilterType filterType;

    String itemId;

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

    @Override
    public void setGravity(int gravity){

        ((LinearLayout)getChildAt(0)).setGravity(gravity);
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

                    case R.styleable.CustomRadioButton_SortType:

                        setSortType(SortType.valueOf(a.getInteger(attr,1)));

                        break;

                    case R.styleable.CustomRadioButton_FilterType:

                        setFilterType(FilterType.valueOf(a.getInteger(attr,3)));

                        break;

                    case R.styleable.CustomRadioButton_CustomOrientation:

                        CustomOrientation orientation = CustomOrientation.valueOf(a.getInteger(attr,2));

                        setCustomOrientation(orientation);

                        break;


                }
            }

            a.recycle();

        }

    }

    public void setCustomOrientation(CustomOrientation orientation){

        LinearLayout layout = (LinearLayout) getChildAt(0);

        switch (orientation){

            case VERTICAL:

                layout.setOrientation(VERTICAL);

                break;

            case HORIZONTAL:

                layout.setOrientation(HORIZONTAL);

                View v1 = layout.getChildAt(0);

                layout.removeView(v1);

                layout.addView(v1);

                break;

        }

    }

    @Override
    public void check() {

        radio.setChecked(true);

    }

    public boolean isChecked(){

        return radio.isChecked();
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public SortType getSortType() {
        return sortType;
    }

    @Override
    public void uncheck() {

        radio.setChecked(false);

    }

    public void setLabel(String label){

        text.setText(label);

    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }
}
