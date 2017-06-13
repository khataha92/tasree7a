package com.tasree7a.CustomComponent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tasree7a.R;
import com.tasree7a.utils.UIUtils;

/**
 * Created by mac on 6/13/17.
 */

public class CustomRate extends LinearLayout{

    ImageView[] images = new ImageView[5];

    public CustomRate(Context context) {

        super(context);

        init();

    }

    public CustomRate(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CustomRate(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        setOrientation(HORIZONTAL);

        setGravity(Gravity.CENTER);

        for(int i = 0 ; i < images.length ;i ++){

            images[i] = new ImageView(getContext());

            images[i].setImageResource(R.drawable.ic_star_unchecked);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dpToPx(28),UIUtils.dpToPx(28));

            params.setMargins(0,0,UIUtils.dpToPx(10),0);

            images[i].setLayoutParams(params);

            images[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    int imageResource = R.drawable.ic_star_checked;

                    for(int j = 0 ; j < images.length ; j++){

                        images[j].setImageResource(imageResource);

                        if(images[j] == v){

                            imageResource = R.drawable.ic_star_unchecked;

                        }
                    }

                }
            });

            addView(images[i]);
        }
    }

}
