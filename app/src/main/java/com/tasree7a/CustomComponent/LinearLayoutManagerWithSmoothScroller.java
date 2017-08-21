package com.tasree7a.CustomComponent;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.tasree7a.utils.UIUtils;

public class LinearLayoutManagerWithSmoothScroller extends LinearLayoutManager {

    private static final String TAG = LinearLayoutManagerWithSmoothScroller.class.getSimpleName();

    Context context;

    private int dpToMakeVisible = 0;

    private float millisecondsPerInch = 100f;

    public void setDpToMakeVisible(int dpToMakeVisible) {

        this.dpToMakeVisible = dpToMakeVisible;
    }


    public LinearLayoutManagerWithSmoothScroller(Context context) {
        super(context, VERTICAL, false);

        this.context = context;
    }

    public LinearLayoutManagerWithSmoothScroller(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(context)
        {

            @Override
            protected int getVerticalSnapPreference() {
                return SNAP_TO_START;
            }

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition)
            {
                return LinearLayoutManagerWithSmoothScroller.this
                        .computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics)
            {
                return millisecondsPerInch/displayMetrics.densityDpi;
            }

            @Override
            protected void onStop() {
                super.onStop();

            }

            @Override
            public int calculateDyToMakeVisible(View view, int snapPreference) {
                final RecyclerView.LayoutManager layoutManager = getLayoutManager();
                if (layoutManager == null || !layoutManager.canScrollVertically()) {
                    return 0;
                }
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                        view.getLayoutParams();
                final int top = layoutManager.getDecoratedTop(view) - params.topMargin- UIUtils.dpToPx(dpToMakeVisible);
                final int bottom = layoutManager.getDecoratedBottom(view) + params.bottomMargin;
                final int start = layoutManager.getPaddingTop();
                final int end = layoutManager.getHeight() - layoutManager.getPaddingBottom();
                return calculateDtToFit(top, bottom, start, end, snapPreference);
            }
        };

        smoothScroller.setTargetPosition(position);

        startSmoothScroll(smoothScroller);

    }

    public void setMillisecondsPerInch(float millisecondsPerInch) {
        this.millisecondsPerInch = millisecondsPerInch;
    }
}
