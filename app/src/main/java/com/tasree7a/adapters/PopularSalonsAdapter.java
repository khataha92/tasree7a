package com.tasree7a.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.utils.UserDefaultUtil;
import com.tasree7a.viewholders.PopularSalonsItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PopularSalonsAdapter extends RecyclerView.Adapter<PopularSalonsItemViewHolder> {

    List<SalonModel> salonModels = new ArrayList<>();
    private Context mContext;

    public PopularSalonsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public PopularSalonsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularSalonsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_list_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final PopularSalonsItemViewHolder holder, int position) {

        holder.imageView.setOnClickListener(v -> FragmentManager.showSalonDetailsFragment(mContext, salonModels.get(position)));

        holder.favorite.setImageResource(UserDefaultUtil.isSalonFavorite(salonModels.get(position))
                ? R.drawable.ic_favorite_checked
                : R.drawable.ic_favorite_unchecked);

        holder.favorite.setOnClickListener(v -> {

            if (UserDefaultUtil.isSalonFavorite(salonModels.get(position))) {

                holder.favorite.setImageResource(R.drawable.ic_favorite_unchecked);

                UserDefaultUtil.removeSalonFromFavorite(salonModels.get(position));

            } else {

                holder.favorite.setImageResource(R.drawable.ic_favorite_checked);

                UserDefaultUtil.addSalonToFavorite(salonModels.get(position));

            }

        });

        if (UserDefaultUtil.isSalonFavorite(salonModels.get(position))) {

            holder.favorite.setImageResource(R.drawable.ic_favorite_checked);


        } else {

            holder.favorite.setImageResource(R.drawable.ic_favorite_unchecked);

        }

        try {
            holder.ratingBar.setRating(salonModels.get(position).getRating());

            holder.city.setText(salonModels.get(position).getSalonCity());

            holder.sallonName.setText(salonModels.get(position).getName() + ",");

            Picasso.with(ThisApplication.getCurrentActivity())
                    .load(salonModels.get(position).getImage()).into(holder.imageView);
        } catch (Exception e) {
            Log.e("CRASH", "CRASH: ", e);
        }

    }


    @Override
    public int getItemCount() {

        return salonModels == null || salonModels.size() == 0 ? 0 : salonModels.size();

    }


    public List<SalonModel> getSalonModels() {

        return salonModels;
    }


    public void setSalonModels(List<SalonModel> salonModels) {

        this.salonModels.clear();

        this.salonModels.addAll(salonModels);

        this.notifyDataSetChanged();
    }
}
