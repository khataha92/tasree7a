package com.tasree7a.Models;

import android.support.annotation.FloatRange;
import android.view.ViewGroup;

/**
 * Created by muja on 6/19/17.
 * For customizing loading views
 */

public class LoadingViewModel<LP extends ViewGroup.LayoutParams> {

    private String upperText;

    private String lowerText;

    private LP layoutParams;

    private float alpha = 1.0f;


    public String getUpperText() {

        return upperText;
    }


    public LoadingViewModel setUpperText(String upperText) {

        this.upperText = upperText;

        return this;

    }


    public String getLowerText() {

        return lowerText;
    }


    public LoadingViewModel<LP> setLowerText(String lowerText) {

        this.lowerText = lowerText;

        return this;
    }


    public LP getLayoutParams() {

        return layoutParams;
    }


    public LoadingViewModel setLayoutParams(LP layoutParams) {

        this.layoutParams = layoutParams;

        return this;
    }


    public float getAlpha() {

        return alpha;
    }


    public LoadingViewModel setAlpha(@FloatRange(from=0.0, to=1.0) float alpha) {

        this.alpha = alpha;

        return this;
    }





}