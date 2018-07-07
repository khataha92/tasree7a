package com.tasree7a.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tasree7a.R;
import com.tasree7a.interfaces.SalonServiceSelectionListener;
import com.tasree7a.models.salonbooking.SalonService;
import com.tasree7a.viewholders.SalonServiceViewHolder;

import java.util.List;

public class SalonServicesAdapter extends RecyclerView.Adapter<SalonServiceViewHolder> {

    private List<SalonService> mServices;
    private SalonServiceSelectionListener mSalonServiceSelectionListener;
    public SalonServicesAdapter(List<SalonService> salonServices, SalonServiceSelectionListener salonServiceSelectionListener) {
        mServices = salonServices;
        mSalonServiceSelectionListener = salonServiceSelectionListener;
    }

    @NonNull
    @Override
    public SalonServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalonServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_salon_service, parent, false), mSalonServiceSelectionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final SalonServiceViewHolder holder, final int position) {
        holder.bind(mServices.get(position));
    }

    @Override
    public int getItemCount() {
        return mServices.size();
    }
}
