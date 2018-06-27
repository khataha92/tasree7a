package com.tasree7a.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.adapters.SalonServicesAdapter;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.salonbooking.SalonService;
import com.tasree7a.models.salonbooking.SalonServicesResponse;
import com.tasree7a.observables.ServicesChangedObservable;
import com.tasree7a.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by SamiKhleaf on 10/24/17.
 */

public class SalonServicesFragment extends BaseFragment implements Observer {

    TextView addServices;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ServicesChangedObservable.sharedInstance().addObserver(this);
    }

    RecyclerView salonService;

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

        salonService = (RecyclerView) rootView.findViewById(R.id.services_list);

        salonService.setLayoutManager(new GridLayoutManager(getContext(), 2));

        requestSalonServices();

        return rootView;
    }

    List<SalonService> services = new ArrayList<>();

    private void requestSalonServices() {

        RetrofitManager.getInstance().getSalonServices(ReservationSessionManager.getInstance().getSalonModel().getId(), new AbstractCallback() {

            @Override
            public void onResult(boolean isSuccess, Object result) {

                if (isSuccess) {

                    List<SalonService> salonServices = ((SalonServicesResponse) result).getServices();

                    if (salonServices == null || salonServices.size() == 0) {

                        FragmentManager.showAddNewSalonServiceFragment();

                    } else {
                        services.clear();
                        services = salonServices;
                        initList();
                    }

                    UIUtils.hideLoadingView(rootView, SalonServicesFragment.this);

                }

            }
        });
    }

    private void initList() {
        SalonServicesAdapter adapter = new SalonServicesAdapter(services);
        adapter.notifyDataSetChanged();
        salonService.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ServicesChangedObservable.sharedInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ServicesChangedObservable) {
            requestSalonServices();
            salonService.getAdapter().notifyDataSetChanged();
        }
    }
}
