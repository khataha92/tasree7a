package com.tasree7a.CustomComponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyfishjy.library.RippleBackground;
import com.tasree7a.Models.Barber;
import com.tasree7a.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khalid Taha on 10/16/16.
 */

public class SalonStaffContainer extends LinearLayout {

    List<Barber> barbers = new ArrayList<>();

    public SalonStaffContainer(Context context) {
        super(context);

        init();
    }

    public SalonStaffContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SalonStaffContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        TextView add = new TextView(getContext());

        add.setText(getContext().getString(R.string.PLUS));

        add.setTextColor(getContext().getResources().getColor(R.color.APP_GREEN));

        add.setGravity(Gravity.CENTER);

        add.setTextSize(55);

        add.setPadding(0,20 * -1,0,0);

        add.setBackgroundResource(R.drawable.dotted_border);

        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,180);

        add.setLayoutParams(params);

        addView(add);
    }

    public void addBarber(Barber barber) {

        barbers.add(barber);

        render();
    }

    private void render() {

        for (int i = 0; i < getChildCount() -1; i++) {

            removeView(getChildAt(i));

        }


    }

}
