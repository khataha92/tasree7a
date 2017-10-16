package com.tasree7a.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tasree7a.CustomComponent.CustomRatingBar;
import com.tasree7a.Enums.Sizes;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.ReservationSessionManager;
import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.Observables.FavoriteChangeObservable;
import com.tasree7a.Observables.MenuIconClickedObservable;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
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

        View bookNowLbl = itemView.findViewById(R.id.book_now_lbl);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReservationSessionManager.getInstance().setSalonModel(salonModel);

                FragmentManager.showBookNowFragment();

            }
        };



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

        if(salonModel.isBusiness()) {

            addToFavorite.setVisibility(View.GONE);

            bookNowLbl.setVisibility(View.GONE);

            bookNow.setVisibility(View.GONE);

            ImageView menu = (ImageView) back;

            menu.setImageResource(R.drawable.ic_side_menu);

            menu.setColorFilter(ThisApplication.getCurrentActivity().getBaseContext().getResources().getColor(R.color.WHITE));

            menu.setPadding(0,0,0,0);

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MenuIconClickedObservable.sharedInstance().menuIconClicked();

                }
            });

        }

        bookNow.setOnClickListener(listener);

        bookNowLbl.setOnClickListener(listener);

    }
}
