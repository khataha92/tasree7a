package com.tasree7a.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.Adapters.BaseCardAdapter;
import com.tasree7a.Adapters.CardsRecyclerAdapter;
import com.tasree7a.CustomComponent.SalonMapDetails;
import com.tasree7a.Enums.CardFactory;
import com.tasree7a.Enums.CardType;
import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.Models.PopularSalons.SalonModel;
import com.tasree7a.R;

import java.util.ArrayList;

/**
 * Created by mohammad on 5/18/15.
 * This is the fragment that will show languages list and change language
 */
public class SalonDetailsFragment extends BaseFragment implements CardFactory {

    RecyclerView salonDetails;

    BaseCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_salon_details, container, false);

        adapter = new BaseCardAdapter(getCardModels());

        salonDetails = (RecyclerView) rootView.findViewById(R.id.salon_cards);

        salonDetails.setLayoutManager(new LinearLayoutManager(getContext()));

        salonDetails.setAdapter(adapter);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public int getFactoryId() {
        return 0;
    }

    private BaseCardModel getCardModel(CardType type){

        BaseCardModel cardModel = new BaseCardModel();

        cardModel.setCardType(type);

        switch (type){

            case IMAGE_CARD:

                // TODO: 6/5/17

                break;

            case GALARY_CARD:

                // TODO: 6/5/17

                break;
        }

        return cardModel;
    }

    @Override
    public ArrayList<BaseCardModel> getCardModels() {

        ArrayList<BaseCardModel> cardModels = new ArrayList<>();

        cardModels.add(getCardModel(CardType.IMAGE_CARD));

        cardModels.add(getCardModel(CardType.GALARY_CARD));

        cardModels.add(getCardModel(CardType.PRODUCTS_CARD));

        cardModels.add(getCardModel(CardType.SALON_RATE));

        return cardModels;
    }

    @Override
    public RecyclerView getRecyclerView() {

        return salonDetails;

    }

    @Override
    public CardsRecyclerAdapter getCardsAdapter() {

        return null;

    }

    @Override
    public View getRootView() {

        return rootView;

    }
}
