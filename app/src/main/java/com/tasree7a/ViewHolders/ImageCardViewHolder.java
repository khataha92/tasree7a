package com.tasree7a.viewholders;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.SalonServicesActivity;
import com.tasree7a.customcomponent.CustomRatingBar;
import com.tasree7a.enums.Sizes;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.models.BaseCardModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.observables.FavoriteChangeObservable;
import com.tasree7a.observables.MenuIconClickedObservable;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

public class ImageCardViewHolder extends BaseCardViewHolder {

    public ImageCardViewHolder(View view, BaseCardModel cardModel) {
        super(view, cardModel);
        final SalonModel salonModel = (SalonModel) cardModel.getCardValue();

//        if (UserDefaultUtil.isSalonFavorite(salonModel)) {
//            ((ImageView) addToFavorite).setImageResource(R.drawable.ic_favorite_checked);
//        } else {
//            ((ImageView) addToFavorite).setImageResource(R.drawable.ic_favorite_unchecked);
//        }
//
//        CustomRatingBar ratingBar = itemView.findViewById(R.id.salon_rating);
//        ratingBar.setRating(salonModel.getRating());
//        TextView salonName = itemView.findViewById(R.id.sallon_name);
//        salonName.setText(salonModel.getName());
//        ImageView salonCover = itemView.findViewById(R.id.salon_image);
//        UIUtils.loadUrlIntoImageView(itemView.getContext(), salonModel.getImage(), salonCover, Sizes.MEDIUM);
//        View bookNow = itemView.findViewById(R.id.bookNow);
//        if (salonModel.isBusiness()) {
//            addToFavorite.setVisibility(View.GONE);
////            bookNowLbl.setVisibility(View.GONE);
////            bookNow.setVisibility(View.GONE);
//            bookNowLbl.setText(R.string.SERVICES);
//            ImageView menu = (ImageView) back;
//            menu.setImageResource(R.drawable.ic_side_menu);
//            menu.setColorFilter(ThisApplication.getCurrentActivity().getBaseContext().getResources().getColor(R.color.WHITE));
//            menu.setPadding(0, 0, 0, 0);
//            menu.setOnClickListener(v -> MenuIconClickedObservable.sharedInstance().menuIconClicked());
//            View.OnClickListener servicesListener = v -> itemView.getContext().startActivity(new Intent(itemView.getContext(), SalonServicesActivity.class));//FragmentManager.showSalonServicesFragment();
//            bookNow.setOnClickListener(servicesListener);
//            bookNowLbl.setOnClickListener(servicesListener);
//        } else {
//            bookNow.setOnClickListener(listener);
//            bookNowLbl.setOnClickListener(listener);
//        }
    }
}
