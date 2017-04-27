package com.tasree7a.utils;

import android.graphics.Color;
import android.util.TypedValue;
import com.tasree7a.ThisApplication;

/**
 * Created by KhalidTaha on 4/20/17.
 */

public class UIUtils {

    public static int dpToPx(float dp) {

        float resultPix = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ThisApplication.getCurrentActivity().getResources().getDisplayMetrics());

        return (int)resultPix;
    }

    public static int getColor(int colorResId){

        return ThisApplication.getCurrentActivity().getResources().getColor(colorResId);

    }

    public static int changeAlpha(int color, float alpha) {

        int aChannel = (int)(alpha * 255);

        return Color.argb(aChannel, Color.red(color), Color.green(color), Color.blue(color));
    }
}
