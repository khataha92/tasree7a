package com.tasree7a.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tasree7a.Adapters.BaseCardAdapter;
import com.tasree7a.Adapters.CardsRecyclerAdapter;
import com.tasree7a.CustomComponent.CustomSwitch;
import com.tasree7a.Enums.CardFactory;
import com.tasree7a.Enums.CardType;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.R;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.UIUtils;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khalid Taha on 8/19/2017.
 */

public class FragmentBookingList extends BaseFragment implements CardFactory {

    RecyclerView bookingList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_salon_bookings, container, false);
//fragment_bookings
        bookingList = (RecyclerView) rootView.findViewById(R.id.bookings_list);

        bookingList.setLayoutManager(new LinearLayoutManager(getContext()));

        BaseCardAdapter adapter = new BaseCardAdapter(getCardModels());
        ImageView back = (ImageView) rootView.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();

            }
        });

        bookingList.setAdapter(adapter);

        return rootView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getFactoryId() {
        return 0;
    }

    @Override
    public ArrayList<BaseCardModel> getCardModels() {

        ArrayList<BaseCardModel> baseCardModels = new ArrayList<>();

        for (int i = 0; i < UIUtils.bookingModels.size(); i++){

            BaseCardModel cardModel = new BaseCardModel();

            cardModel.setCardType(CardType.BOOKING_ITEM);

            cardModel.setCardValue(UIUtils.bookingModels.get(i));

            baseCardModels.add(cardModel);

        }

        return baseCardModels;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return null;
    }

    @Override
    public CardsRecyclerAdapter getCardsAdapter() {
        return null;
    }

    @Override
    public View getRootView() {
        return null;
    }
}
