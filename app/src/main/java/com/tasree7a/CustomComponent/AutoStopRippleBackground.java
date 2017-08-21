package com.tasree7a.CustomComponent;

import android.content.Context;
import android.util.AttributeSet;

import com.skyfishjy.library.RippleBackground;

/**
 * Created by Khalid Taha on 10/16/16.
 */

public class AutoStopRippleBackground extends RippleBackground {

    private int animationExpirationMS = 1000;

    public AutoStopRippleBackground(Context context) {
        super(context);
    }

    public AutoStopRippleBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoStopRippleBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void startRippleAnimation() {
        super.startRippleAnimation();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRippleAnimation();
            }
        }, animationExpirationMS);
    }


    public void setAnimationExpirationMS(int animationExpirationMS) {
        this.animationExpirationMS = animationExpirationMS;
    }
}
