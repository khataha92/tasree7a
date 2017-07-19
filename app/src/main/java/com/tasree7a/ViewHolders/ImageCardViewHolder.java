package com.tasree7a.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tasree7a.CustomComponent.CustomRatingBar;
import com.tasree7a.Enums.Sizes;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.Models.PopularSalons.SalonModel;
import com.tasree7a.Observables.FavoriteChangeObservable;
import com.tasree7a.R;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

/**
 * Created by mac on 6/5/17.
 */

public class ImageCardViewHolder extends BaseCardViewHolder {

    public ImageCardViewHolder(View view, BaseCardModel cardModel) {

        super(view, cardModel);

        final SalonModel salonModel = (SalonModel) cardModel.getCardValue();

        View back = itemView.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();

            }

        });

        View addToFavorite = itemView.findViewById(R.id.add_to_favorite);

        addToFavorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(UserDefaultUtil.isSalonFavorite(salonModel)){

                    UserDefaultUtil.removeSalonFromFavorite(salonModel);

                    ((ImageView) v).setImageResource(R.drawable.ic_favorite_unchecked);

                } else{

                    UserDefaultUtil.addSalonToFavorite(salonModel);

                    ((ImageView) v).setImageResource(R.drawable.ic_favorite_checked);

                }

                FavoriteChangeObservable.sharedInstance().setFavoriteChanged(salonModel);

            }
        });

        if(UserDefaultUtil.isSalonFavorite(salonModel)){

            ((ImageView) addToFavorite).setImageResource(R.drawable.ic_favorite_checked);

        } else{

            ((ImageView) addToFavorite).setImageResource(R.drawable.ic_favorite_unchecked);

        }

        CustomRatingBar ratingBar = (CustomRatingBar) itemView.findViewById(R.id.salon_rating);

        ratingBar.setRating(salonModel.getRating());

        TextView salonName = (TextView) itemView.findViewById(R.id.sallon_name);

        salonName.setText(salonModel.getName());

        ImageView salonCover = (ImageView) itemView.findViewById(R.id.salon_image);

        UIUtils.loadUrlIntoImageView(salonModel.getImage(),salonCover, Sizes.MEDIUM);

        View bookNow = itemView.findViewById(R.id.bookNow);

        bookNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO: navigate to book now view

            }

        });

    }
}
