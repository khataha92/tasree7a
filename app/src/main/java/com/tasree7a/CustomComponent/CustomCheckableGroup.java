package com.tasree7a.customcomponent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.tasree7a.interfaces.Checkable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 6/13/17.
 */

public class CustomCheckableGroup extends LinearLayout  {

    List<Checkable> checkedList = new ArrayList<>();

    public CustomCheckableGroup(Context context) {

        super(context);

        init();

    }

    public CustomCheckableGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CustomCheckableGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setOrientation(HORIZONTAL);
    }

    public List<Checkable> getCheckedList(){
        checkedList.clear();
        for(int i = 0 ; i < getChildCount() ; i++){
            if(getChildAt(i) instanceof CustomCheckbox){
                if(((CustomCheckbox) getChildAt(i)).isChecked()){
                    checkedList.add((Checkable)getChildAt(i));
                }
            }
        }
        return checkedList;
    }
}
