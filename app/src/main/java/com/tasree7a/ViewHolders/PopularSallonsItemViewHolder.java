package com.tasree7a.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.customcomponent.CustomRatingBar;
import com.tasree7a.customcomponent.RoundedCornersImageView;

/**
 * Created by mac on 5/11/17.
 */

public class PopularSallonsItemViewHolder extends RecyclerView.ViewHolder {

    public TextView sallonName;

    public TextView city;

    public CustomRatingBar ratingBar;

    public RoundedCornersImageView imageView;

    public ImageView favorite ;

    public PopularSallonsItemViewHolder(View itemView) {
        super(itemView);

        sallonName = itemView.findViewById(R.id.sallon_name);

        city = itemView.findViewById(R.id.salon_city);

        ratingBar = itemView.findViewById(R.id.salon_rating);

        imageView = itemView.findViewById(R.id.sallon_image);

        favorite = itemView.findViewById(R.id.is_popular);

    }


}
