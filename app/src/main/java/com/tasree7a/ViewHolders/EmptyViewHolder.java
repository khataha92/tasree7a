package com.tasree7a.viewholders;

import android.view.View;

/**
 * Created by Khalid Taha on 3/20/16.
 * EmptyViewHolder
 */
public class EmptyViewHolder extends BaseCardViewHolder {


    public EmptyViewHolder(View itemView) {

        super(itemView, null);

    }

    @Override
    public void onViewAttachedToWindow() {
        super.onViewAttachedToWindow();

        // Set height of the empty view from card value if exists
        if (cardModel != null && cardModel.getCardValue() != null && cardModel.getCardValue() instanceof Integer) {

            itemView.getLayoutParams().height = (int) cardModel.getCardValue();

        }

    }
}
