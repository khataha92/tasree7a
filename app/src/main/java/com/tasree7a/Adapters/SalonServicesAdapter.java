package com.tasree7a.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tasree7a.Enums.Sizes;
import com.tasree7a.Models.SalonBooking.SalonService;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.ViewHolders.SalonServiceViewHolder;
import com.tasree7a.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mac on 8/27/17.
 */

public class SalonServicesAdapter extends RecyclerView.Adapter<SalonServiceViewHolder> {

    List<SalonService> services = new ArrayList<>();

    public SalonServicesAdapter(List<SalonService> salonServices){

        this.services = salonServices;

    }

    @Override
    public SalonServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = ThisApplication.getCurrentActivity().getLayoutInflater().inflate(R.layout.view_salon_service, null);

        return new SalonServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SalonServiceViewHolder holder, int position) {

        CircleImageView imageView = (CircleImageView) holder.itemView.findViewById(R.id.image);

        UIUtils.loadUrlIntoImageView(services.get(position).getImageUrl(),imageView, Sizes.MEDIUM);

        TextView name = (TextView) holder.itemView.findViewById(R.id.service_name);

        name.setText(services.get(position).getName());

        TextView price = (TextView) holder.itemView.findViewById(R.id.price);

        price.setText("$" + services.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}
