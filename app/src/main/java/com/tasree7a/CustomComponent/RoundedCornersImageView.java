package com.tasree7a.customcomponent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.tasree7a.utils.UIUtils;

/**
 * Created by mac on 5/11/17.
 */

public class RoundedCornersImageView extends AppCompatImageView {
    public RoundedCornersImageView(Context context) {
        super(context);
    }

    public RoundedCornersImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedCornersImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float mRadius = UIUtils.dpToPx(4);

        Path clipPath = new Path();

        float[] radii = {0, 0, 0, 0, 0, 0, 0, 0};
            radii[0] = mRadius;
            radii[1] = mRadius;
            radii[2] = mRadius;
            radii[3] = mRadius;

        clipPath.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), radii, Path.Direction.CW);

        canvas.clipPath(clipPath);

        super.onDraw(canvas);
    }
}
