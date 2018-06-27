package com.tasree7a.customcomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;

/**
 * Created by mac on 2/3/16.
 * This is for CustomGridCalendar cells, make it square according to Sentiago design
 */
public class CustomSquareRelativeLayout extends PercentRelativeLayout {

    // 0; makes the height equals to the width
    // 1: makes the width equals to the height
    int squareToSide = 0;

    public CustomSquareRelativeLayout(Context context) {
        super(context);

        init(context, null);
    }

    public CustomSquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public CustomSquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        // Set attributes from xml if exists
        if (attrs != null) {

            TypedArray a = ThisApplication.getCurrentActivity().obtainStyledAttributes(attrs, R.styleable.CustomSquareRelativeLayout);

            for (int i = 0; i < a.getIndexCount(); ++i) {

                int attr = a.getIndex(i);

                switch (attr) {

                    case R.styleable.CustomSquareRelativeLayout_baseSide:

                        squareToSide = a.getInteger(attr, 0);

                        break;
                }

            }

            a.recycle();

        }

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int size = squareToSide == 0? widthMeasureSpec : heightMeasureSpec;

        //here will pass the width to height parameter to make it square
        super.onMeasure(size, size);
    }

}
