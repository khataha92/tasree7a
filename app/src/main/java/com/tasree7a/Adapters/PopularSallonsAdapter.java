package com.tasree7a.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.ViewHolders.PopularSallonsItemViewHolder;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.List;

/**
 * Created by mac on 5/11/17.
 */

public class PopularSallonsAdapter extends RecyclerView.Adapter<PopularSallonsItemViewHolder> {

    List<SalonModel> salonModels;

    @Override
    public PopularSallonsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(ThisApplication.getCurrentActivity()).inflate(R.layout.popular_list_item,null);

        return new PopularSallonsItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PopularSallonsItemViewHolder holder, final int position) {

        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.showSalonDetailsFragment(salonModels.get(position));

            }

        });

        holder.favorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(UserDefaultUtil.isSalonFavorite(salonModels.get(position))){

                    holder.favorite.setImageResource( R.drawable.ic_favorite_unchecked);

                    UserDefaultUtil.removeSalonFromFavorite(salonModels.get(position));


                } else{

                    holder.favorite.setImageResource( R.drawable.ic_favorite_checked);

                    UserDefaultUtil.addSalonToFavorite(salonModels.get(position));

                }

            }
        });

        if(UserDefaultUtil.isSalonFavorite(salonModels.get(position))){

            holder.favorite.setImageResource( R.drawable.ic_favorite_checked);


        } else{

            holder.favorite.setImageResource( R.drawable.ic_favorite_unchecked);

        }

        holder.ratingBar.setRating(salonModels.get(position).getRating());

        holder.city.setText(salonModels.get(position).getSalonCity());

        holder.sallonName.setText(salonModels.get(position).getName() + ",");

        Picasso.with(ThisApplication.getCurrentActivity())
                .load(salonModels.get(position).getImage()).into(holder.imageView);


    }

    @Override
    public int getItemCount() {

        return  salonModels == null || salonModels.size() == 0 ? 0 : salonModels.size();

    }

    public List<SalonModel> getSalonModels() {
        return salonModels;
    }

    public void setSalonModels(List<SalonModel> salonModels) {
        this.salonModels = salonModels;
    }
}
