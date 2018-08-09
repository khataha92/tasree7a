package com.tasree7a.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tasree7a.R;
import com.tasree7a.adapters.BaseCardAdapter;
import com.tasree7a.adapters.BookingListAdapter;
import com.tasree7a.enums.BookingStatus;
import com.tasree7a.enums.CardFactory;
import com.tasree7a.enums.CardType;
import com.tasree7a.interfaces.OnBookingStatusChangedListener;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.BaseCardModel;
import com.tasree7a.models.UserBookingsResponse;
import com.tasree7a.models.bookings.BookingModel;
import com.tasree7a.observables.BookingStatusChangedObservable;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class FragmentBookingList extends BaseFragment implements Observer, OnBookingStatusChangedListener {

    private String mUserID;

    private List<BookingModel> mBookingsList = new ArrayList<>();

    private RecyclerView mBookingRecyclerView;
    private BookingListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_salon_bookings, container, false);
        rootView.findViewById(R.id.add_booking).setOnClickListener(v -> FragmentManager.showBookNowFragment(getActivity()));
        mBookingRecyclerView = rootView.findViewById(R.id.bookings_list);
        mBookingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageView back = rootView.findViewById(R.id.back);
        back.setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());
        showLoadingView();
        mUserID = UserDefaultUtil.isBusinessUser() ? UserDefaultUtil.getCurrentSalonUser().getId() : UserDefaultUtil.getCurrentUser().getId();
        requestBookings(mUserID);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        BookingStatusChangedObservable.sharedInstance().addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        BookingStatusChangedObservable.sharedInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof BookingStatusChangedObservable) {
            requestBookings(mUserID);
            mBookingRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onBookingStatusChanged(int position, BookingStatus status) {
        requestBookings(mUserID);
    }

    private void requestBookings(String userID) {
        RetrofitManager.getInstance().getUserBookings(userID, UserDefaultUtil.isBusinessUser() ? "S" : "C", (isSuccess, result) -> {
            hideLoadingView();
            if (isSuccess) {
                mBookingsList.clear();
                mBookingsList = ((UserBookingsResponse) result).getUserBookings();
                initBookings();
            }
        });
    }

    private void initBookings() {
        mAdapter = new BookingListAdapter(mBookingsList, this);
        mBookingRecyclerView.setAdapter(mAdapter);
    }


    private void showLoadingView() {
        rootView.findViewById(R.id.loading).setVisibility(View.VISIBLE);
    }

    private void hideLoadingView() {
        rootView.findViewById(R.id.loading).setVisibility(View.GONE);
    }
}
