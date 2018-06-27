package com.tasree7a.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.adapters.SalonServicesAdapter;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.salonbooking.SalonService;
import com.tasree7a.models.salonbooking.SalonServicesResponse;
import com.tasree7a.observables.ServicesTotalChangeObservable;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by mac on 9/17/17.
 */

public class BookNowFragment extends BaseFragment implements Observer {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ServicesTotalChangeObservable.sharedInstance().addObserver(this);

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_book_now, null);

        UIUtils.showLoadingView(rootView, this);

        final RecyclerView salonService = rootView.findViewById(R.id.services_list);

        salonService.setLayoutManager(new GridLayoutManager(getContext(), 2));

        RetrofitManager.getInstance().getSalonServices(UserDefaultUtil.isBusinessUser()
                ? UserDefaultUtil.getCurrentSalonUser().getId()
                : ReservationSessionManager.getInstance().getSalonModel().getId(), (isSuccess, result) -> {

                    if (isSuccess) {

                        List<SalonService> salonServices = ((SalonServicesResponse) result).getServices();

                        salonService.setAdapter(new SalonServicesAdapter(salonServices));

                        UIUtils.hideLoadingView(rootView, BookNowFragment.this);

                    }

                });


        //salonService.setAdapter(new SalonServicesAdapter(salonServices));

        View schedule = rootView.findViewById(R.id.schedule);

        schedule.setOnClickListener(v -> {

            if (ReservationSessionManager.getInstance().getSelectedServices().isEmpty()) {

                String message = getString(R.string.ERROR_EMPTY_SERVICE);

                Toast.makeText(ThisApplication.getCurrentActivity(), message, Toast.LENGTH_LONG).show();

                return;

            }

            FragmentManager.showBookScheduleFragment();

        });

        rootView.findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

        rootView.findViewById(R.id.cancel).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

        return rootView;
    }


    @Override
    public void onDetach() {

        super.onDetach();

        ServicesTotalChangeObservable.sharedInstance().deleteObserver(this);

    }


    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof ServicesTotalChangeObservable) {

            double total = (double) arg;

            String strTotal = getString(R.string.TOTAL);

            ((TextView) rootView.findViewById(R.id.total)).setText(strTotal + ": $" + total);

            ReservationSessionManager.getInstance().setTotal(total);

        }

    }
}
