package com.tasree7a.CustomComponent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.tasree7a.interfaces.Checkable;
import com.tasree7a.interfaces.SingleCheckableGroup;

/**
 * Created by mac on 6/13/17.
 */

public class CustomRadioGroup extends LinearLayout implements SingleCheckableGroup {

    public CustomRadioGroup(Context context) {

        super(context);

        init();

    }

    public CustomRadioGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CustomRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        setOrientation(HORIZONTAL);

    }

    @Override
    public void onItemChecked(Checkable checkable) {

        View view = (View)checkable;

        for(int i = 0 ; i < getChildCount() ; i++){

            if(getChildAt(i) == view){

                ((Checkable) getChildAt(i)).check();
            } else{

                ((Checkable) getChildAt(i)).uncheck();
            }
        }
    }

    public Checkable getCheckedItem(){

        for(int i = 0 ; i < getChildCount() ; i++){

            if(getChildAt(i) instanceof CustomRadioButton && ((CustomRadioButton)getChildAt(i)).isChecked()){

                return (CustomRadioButton) getChildAt(i);

            }
        }

        return null;
    }
}
