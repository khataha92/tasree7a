package com.tasree7a.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.enums.Sizes;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.models.salonbooking.SalonService;
import com.tasree7a.observables.ServicesTotalChangeObservable;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.viewholders.SalonServiceViewHolder;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mac on 8/27/17.
 */

public class SalonServicesAdapter extends RecyclerView.Adapter<SalonServiceViewHolder> {

    List<SalonService> services = new ArrayList<>();

    List<SalonService> selectedServices = new ArrayList<>();

    public SalonServicesAdapter(List<SalonService> salonServices){

        this.services = salonServices;

    }

    @Override
    public SalonServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = ThisApplication.getCurrentActivity().getLayoutInflater().inflate(R.layout.view_salon_service, null);

        return new SalonServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SalonServiceViewHolder holder, final int position) {

        CircleImageView imageView = (CircleImageView) holder.itemView.findViewById(R.id.image);

        UIUtils.loadUrlIntoImageView(services.get(position).getImageUrl(),imageView, Sizes.MEDIUM);

        TextView name = (TextView) holder.itemView.findViewById(R.id.service_name);

        name.setText(services.get(position).getName());

        TextView price = (TextView) holder.itemView.findViewById(R.id.price);

        price.setText("$" + services.get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(holder.itemView.findViewById(R.id.green_circle).getVisibility() == View.GONE) {

                    holder.itemView.findViewById(R.id.green_circle).setVisibility(View.VISIBLE);

                    selectedServices.add(services.get(position));

                } else{

                    holder.itemView.findViewById(R.id.green_circle).setVisibility(View.GONE);

                    selectedServices.remove(services.get(position));
                }

                ReservationSessionManager.getInstance().setSelectedServices(selectedServices);

                double total = 0;

                for(int i = 0 ; i < selectedServices.size() ; i++){

                    total += selectedServices.get(i).getPrice();

                }

                ServicesTotalChangeObservable.sharedInstance().notifyServicesTotalChanged(total);

            }
        });

        boolean isGreenCircleVisible = selectedServices.indexOf(services.get(position)) != -1;

        holder.itemView.findViewById(R.id.green_circle).setVisibility(isGreenCircleVisible ? View.VISIBLE: View.GONE);

    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}
