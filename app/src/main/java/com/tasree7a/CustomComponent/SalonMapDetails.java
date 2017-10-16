package com.tasree7a.CustomComponent;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tasree7a.Constants;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.Observables.FavoriteChangeObservable;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Khalid Taha on 1/19/17.
 * Used to display a hotel details in Map View
 */

public class SalonMapDetails extends LinearLayout implements Observer {

    private Runnable onToggleFavourite;

    private SalonModel currentSalon;

    public SalonMapDetails(Context context) {
        super(context);

        init(context);
    }

    public SalonMapDetails(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public SalonMapDetails(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SalonMapDetails(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context);
    }

    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.map_salon_details, this, true);

        if (isInEditMode()) return;

//        ((TextView)findViewById(R.id.salon_name)).setTypeface(FontUtil.getFont(FontsType.MEDIUM));

//        ((TextView)findViewById(R.id.salon_address)).setTypeface(FontUtil.getFont(FontsType.MEDIUM));

    }

    public void setSalonDetails(final SalonModel salonModel, boolean isNearBy, Object attractionModel, Runnable onToggleFavourite) {

        currentSalon = salonModel;

        setOnToggleFavourite(onToggleFavourite);

        // Set salon info
        ((TextView)findViewById(R.id.salon_name)).setText(salonModel.getName());

        String address = salonModel.getSalonCity();

        ((TextView)findViewById(R.id.salon_address)).setText(address);

        // Set image

        ImageView imageView = (ImageView) findViewById(R.id.salon_image);

        if( salonModel.getImage() != null) {



            imageView.setVisibility(View.VISIBLE);

            Picasso.with(ThisApplication.getCurrentActivity()).load(salonModel.getImage()).into(imageView);

        } else {

            imageView.setVisibility(View.GONE);

        }

        // Set favourite
        final AutoStopRippleBackground favouriteContainer = (AutoStopRippleBackground)findViewById(R.id.favouriteContainer);

        favouriteContainer.setAnimationExpirationMS(1000);

        ImageView favorite = (ImageView) findViewById(R.id.add_to_favorite);//, this, ImageView.class);

        changeStateOfFavourite(salonModel.isFavorite());

        favorite.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                favouriteContainer.stopRippleAnimation();

                if (salonModel.isFavorite()) {

                    UserDefaultUtil.removeSalonFromFavorite(salonModel);

                    FavoriteChangeObservable.sharedInstance().setFavoriteChanged(salonModel);

                } else {

                    favouriteContainer.startRippleAnimation();

                    UserDefaultUtil.addSalonToFavorite(salonModel);

                    FavoriteChangeObservable.sharedInstance().setFavoriteChanged(salonModel);

                }

            }
        });

        // set rating

        CustomRatingBar ratingBar = (CustomRatingBar) findViewById(R.id.salon_rating);

        ratingBar.setRating(salonModel.getRating());

    }


    public void animateSalonDetailsLayout(final boolean showDetails) {

        if (showDetails && this.getVisibility() == View.VISIBLE) {

            return;

        }

        if (!showDetails && this.getVisibility() == View.GONE) {

            return;

        }


        Animation detailsAnimation = AnimationUtils.loadAnimation(ThisApplication.getCurrentActivity(),
                showDetails ? R.anim.slide_up_from_bottom : R.anim.slide_down_from_bottom);

        detailsAnimation.setDuration(Constants.MOVE_LAYOUT_ANIMATION_DURATION);

        detailsAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        detailsAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                SalonMapDetails.this.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                SalonMapDetails.this.setVisibility(showDetails ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        this.clearAnimation();

        this.startAnimation(detailsAnimation);

    }


    public void setOnToggleFavourite(Runnable onToggleFavourite) {
        this.onToggleFavourite = onToggleFavourite;
    }


    private void changeStateOfFavourite(boolean isFavourite) {

        AutoStopRippleBackground favouriteContainer = (AutoStopRippleBackground)findViewById(R.id.favouriteContainer);

        favouriteContainer.setAnimationExpirationMS(1000);

        ImageView favorite = (ImageView) findViewById(R.id.add_to_favorite);

        if(isFavourite){

            favorite.setImageResource(R.drawable.favourites_red_with_border);

        } else {

            favorite.setImageResource(R.drawable.favorite_dark_grey);

        }

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (isInEditMode()) return;

        FavoriteChangeObservable.sharedInstance().deleteObserver(this);

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) return;


        if (UserDefaultUtil.isAppLanguageArabic()) {

            getLayoutParams().height = UIUtils.dpToPx(180);

            findViewById(R.id.main_view).getLayoutParams().height = UIUtils.dpToPx(180);

            requestLayout();

        }


        FavoriteChangeObservable.sharedInstance().deleteObserver(this);

        FavoriteChangeObservable.sharedInstance().addObserver(this);

    }



    @Override
    public void update(Observable o, Object data) {

        if (o instanceof FavoriteChangeObservable) {

            SalonModel hotel = (SalonModel) data;

            if (currentSalon != null && currentSalon.getId().equals(hotel.getId())) {

                changeStateOfFavourite(hotel.isFavorite());

                if (onToggleFavourite != null) onToggleFavourite.run();

            }

        }

    }
}
