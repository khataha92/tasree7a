package com.tasree7a.Enums;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tasree7a.Adapters.CardsRecyclerAdapter;
import com.tasree7a.Models.BaseCardModel;

import java.util.ArrayList;


/**
 * Created by mac on 5/9/16.
 */
public interface CardFactory {

    int getFactoryId();

    ArrayList<BaseCardModel> getCardModels();

    RecyclerView getRecyclerView();

    CardsRecyclerAdapter getCardsAdapter();

    View getRootView();
}
