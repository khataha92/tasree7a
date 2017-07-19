package com.tasree7a.ViewHolders;

import android.view.View;

import com.tasree7a.CustomComponent.SalonLocationCard;
import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.Models.LocationCard.LocationCardModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Khalid on 3/20/16.
 */
public class LocationCardViewHolder extends BaseCardViewHolder {

    SalonLocationCard card;

    //boolean isDataSetup = false;

    public LocationCardViewHolder(View itemView, BaseCardModel model) {

        super(itemView,model);

        initializeView();

    }

    @Override
    public void initializeView() {

        super.initializeView();

        card = (SalonLocationCard) itemView;

        card.setLocationModel((LocationCardModel)cardModel.getCardValue());

        card.setupData();

    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void changePosition(Event event){
//
//        if(event.getEventType() == EventType.HOTEL_DETAILS_SCROLL_CHANGED) {
//
//            int dy = (int) event.getMessage();
//
//            card.changePosition(dy);
//
//        }
//
//    }

    @Override
    public void initializeViewOnUI() {
        super.initializeViewOnUI();


    }

    @Override
    public void onViewDetachedFromWindow() {
        super.onViewDetachedFromWindow();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewAttachedToWindow() {

        super.onViewAttachedToWindow();

//        EventBus.getDefault().register(this);
    }
}
