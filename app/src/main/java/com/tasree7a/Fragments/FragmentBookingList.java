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
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.Models.Bookings.BookingModel;
import com.tasree7a.Models.UserBookingsResponse;
import com.tasree7a.R;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khalid Taha on 8/19/2017.
 */

public class FragmentBookingList extends BaseFragment implements CardFactory {

    RecyclerView bookingList;

    List<BookingModel> bookingModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_salon_bookings, container, false);
//fragment_bookings
        bookingList = (RecyclerView) rootView.findViewById(R.id.bookings_list);

        bookingList.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageView back = (ImageView) rootView.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();

            }
        });

        showLoadingView();

        RetrofitManager.getInstance().getUserBookings(UserDefaultUtil.getCurrentUser().getId(), new AbstractCallback() {
            @Override
            public void onResult(boolean isSuccess, Object result) {

                hideLoadingView();

                if(isSuccess){

                    bookingModels = ((UserBookingsResponse)result).getUserBookings();

                    initBookings();

                }

            }
        });

        return rootView;

    }

    private void showLoadingView(){

        rootView.findViewById(R.id.loading).setVisibility(View.VISIBLE);

    }

    private void hideLoadingView(){

        rootView.findViewById(R.id.loading).setVisibility(View.GONE);

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

        for (int i = 0; i < bookingModels.size(); i++){

            BaseCardModel cardModel = new BaseCardModel();

            cardModel.setCardType(CardType.BOOKING_ITEM);

            cardModel.setCardValue(bookingModels.get(i));

            baseCardModels.add(cardModel);

        }

        return baseCardModels;
    }

    private void initBookings() {

        BaseCardAdapter adapter = new BaseCardAdapter(getCardModels());

        bookingList.setAdapter(adapter);
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
