package com.tasree7a.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tasree7a.CustomComponent.CustomRatingBar;
import com.tasree7a.CustomComponent.RoundedCornersImageView;
import com.tasree7a.R;

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

        sallonName = (TextView) itemView.findViewById(R.id.sallon_name);

        city = (TextView) itemView.findViewById(R.id.salon_city);

        ratingBar = (CustomRatingBar) itemView.findViewById(R.id.salon_rating);

        imageView = (RoundedCornersImageView) itemView.findViewById(R.id.sallon_image);

        favorite = (ImageView) itemView.findViewById(R.id.is_popular);

    }


}
