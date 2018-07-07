package com.tasree7a.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.enums.Sizes;
import com.tasree7a.interfaces.SalonServiceSelectionListener;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.models.salonbooking.SalonService;
import com.tasree7a.observables.ServicesTotalChangeObservable;
import com.tasree7a.utils.UIUtils;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mac on 8/27/17.
 */

public class SalonServiceViewHolder extends RecyclerView.ViewHolder {

    private SalonService mSalonService;
    private SalonServiceSelectionListener mSalonServiceSelectionListener;

    public SalonServiceViewHolder(final View itemView, SalonServiceSelectionListener salonServiceSelectionListener) {
        super(itemView);
        mSalonServiceSelectionListener = salonServiceSelectionListener;
        itemView.setOnClickListener(v -> mSalonServiceSelectionListener.onSalonServiceClicked(!mSalonService.isSelected(), getAdapterPosition()));
    }

    public void bind(SalonService salonService) {
        mSalonService = salonService;
        UIUtils.loadUrlIntoImageView(salonService.getImageUrl(), itemView.findViewById(R.id.image), Sizes.MEDIUM);
        ((TextView) itemView.findViewById(R.id.service_name)).setText(salonService.getName());
        ((TextView) itemView.findViewById(R.id.price)).setText(String.format(Locale.ENGLISH, "$%s", salonService.getPrice()));

        itemView.findViewById(R.id.green_circle).setVisibility(mSalonService.isSelected() ? View.VISIBLE : View.GONE);
    }
}
