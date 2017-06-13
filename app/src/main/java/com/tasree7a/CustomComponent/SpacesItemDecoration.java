package com.tasree7a.CustomComponent;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mac on 6/14/17.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;

    boolean includeEdge = true;

    public SpacesItemDecoration(int space) {
        this.spacing = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getLayoutManager() instanceof GridLayoutManager) {

            GridLayoutManager layoutManager = (GridLayoutManager)parent.getLayoutManager();

            int spanCount = layoutManager.getSpanCount();

            int position = parent.getChildAdapterPosition(view); // item position

            int column = position % spanCount; // item column

            if (includeEdge) {

                outRect.left = spacing - column * spacing / spanCount;

                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {

                    outRect.top = spacing;

                }

                outRect.bottom = spacing;

            } else {

                outRect.left = column * spacing / spanCount;

                outRect.right = spacing - (column + 1) * spacing / spanCount;

                if (position >= spanCount) {

                    outRect.top = spacing;

                }
            }

        }

    }
}

