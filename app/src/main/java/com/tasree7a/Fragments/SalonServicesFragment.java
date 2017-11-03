package com.tasree7a.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tasree7a.Adapters.SalonServicesAdapter;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.ReservationSessionManager;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.SalonBooking.SalonService;
import com.tasree7a.Models.SalonBooking.SalonServicesResponse;
import com.tasree7a.R;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.UIUtils;

import java.util.List;

/**
 * Created by SamiKhleaf on 10/24/17.
 *
 */

public class SalonServicesFragment extends BaseFragment {

    TextView addServices;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.salon_services_fragment, container, false);

        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();

            }
        });

        addServices = (TextView) rootView.findViewById(R.id.add_delete);

        addServices.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.showAddNewSalonServiceFragment();
            }
        });

        UIUtils.showLoadingView(rootView, this);

        final RecyclerView salonService = (RecyclerView) rootView.findViewById(R.id.services_list);

        salonService.setLayoutManager(new GridLayoutManager(getContext(), 2));

        RetrofitManager.getInstance().getSalonServices(ReservationSessionManager.getInstance().getSalonModel().getId(), new AbstractCallback() {

            @Override
            public void onResult(boolean isSuccess, Object result) {

                if (isSuccess) {

                    List<SalonService> salonServices = ((SalonServicesResponse) result).getServices();

                    if (salonServices == null || salonServices.size() == 0) {

                        FragmentManager.showAddNewSalonServiceFragment();

                    } else {

                        salonService.setAdapter(new SalonServicesAdapter(salonServices));

                    }

                    UIUtils.hideLoadingView(rootView, SalonServicesFragment.this);

                }

            }
        });

        return rootView;
    }
}
