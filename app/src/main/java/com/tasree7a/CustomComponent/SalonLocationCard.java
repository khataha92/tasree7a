package com.tasree7a.CustomComponent;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tasree7a.Models.LocationCard.LocationCardModel;
import com.tasree7a.R;
import com.tasree7a.utils.MapsUtils;
import com.tasree7a.utils.UIUtils;


/**
 * Created by khalid taha
 * HotelLocationCard
 */
public class SalonLocationCard extends FrameLayout {

    private static final String TAG = SalonLocationCard.class.getName();

    int maxTranslation = UIUtils.dpToPx(150);

    ImageView hotelMap;

    private LocationCardModel locationModel;


    public void setLocationModel(LocationCardModel locationModel) {

        this.locationModel = locationModel;
    }


    public SalonLocationCard(Context context) {

        super(context);
    }


    public SalonLocationCard(Context context, AttributeSet attrs) {

        super(context, attrs);
    }


    public SalonLocationCard(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }


    public void changePosition(int dy) {

        float padding = ((float) dy) / 5;

        if (hotelMap.getTranslationY() + padding > maxTranslation) {

            hotelMap.setTranslationY(maxTranslation);

        } else if (hotelMap.getTranslationY() < 0) {

            hotelMap.setTranslationY(0);
        } else {

            hotelMap.setTranslationY(hotelMap.getTranslationY() + padding);

        }


    }

    public void setupData() {

        init();

        findViewById(R.id.view_to_click).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (locationModel.getSalonModel() != null) {

//                FragmentManager.showSalonLocationFragment(locationModel.getSalonModel());

                }

            }
        });


    }


    public void init() {

        hotelMap = (ImageView) findViewById(R.id.salon_map);

        Point size = UIUtils.getScreenSize();

        int height = UIUtils.dpToPx(300);

        int width = size.x;

        hotelMap.setVisibility(View.VISIBLE);

        hotelMap.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String url = MapsUtils.generateStaticMapUrl(locationModel.getLatitude(), locationModel.getLongitude(), width, height);

        UIUtils.loadUrlIntoImageView(url, hotelMap, null);
    }


    @Override
    public void onViewRemoved(View child) {

        super.onViewRemoved(child);

        if (findViewById(R.id.view_to_click) != null) {

            findViewById(R.id.view_to_click).setOnClickListener(null);

        }
    }
}
