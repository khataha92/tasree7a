package com.tasree7a.CustomComponent;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import com.tasree7a.utils.UIUtils;

/**
 * Created by mohammadnabulsi on 11/10/15.
 * CustomTypefaceSpan
 */

public class CustomTypefaceSpan extends TypefaceSpan {
    private final Typeface newType;
    private int color = -1;
    private Float fontSize;

    public CustomTypefaceSpan(String family, Typeface type) {
        super(family);
        newType = type;
        color = -1;
    }

    public CustomTypefaceSpan(Typeface type) {
        super("");
        newType = type;
        color = -1;
    }

    public CustomTypefaceSpan(Typeface type, int intColor) {
        super("");
        newType = type;
        color  =  intColor;
    }

    /**
     * Set font size in sp
     * @param fontSize float
     * @return CustomTypefaceSpan
     */
    public CustomTypefaceSpan setFontSize(float fontSize) {

        this.fontSize = fontSize;

        return this;

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType, color, fontSize);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType, color, fontSize);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf, int color, Float fontSize) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        if(color != -1){

            paint.setColor(color);

        }

        paint.setTypeface(tf);

        if (fontSize != null) {

            paint.setTextSize(UIUtils.dpToPx(fontSize.intValue()));

        }
    }
}