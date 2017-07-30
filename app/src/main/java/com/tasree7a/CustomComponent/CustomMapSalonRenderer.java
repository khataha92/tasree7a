package com.tasree7a.CustomComponent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.android.ui.SquareTextView;
import com.tasree7a.Fragments.FragmentMapView;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Models.PopularSalons.SalonModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.utils.UIUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Draws profile photos inside markers (using IconGenerator).
 * When there are multiple people in the cluster, draw multiple photos (using ).
 */
public class CustomMapSalonRenderer extends DefaultClusterRenderer<SalonModel> implements ClusterManager.OnClusterItemClickListener<SalonModel>, ClusterManager.OnClusterClickListener<SalonModel> {

    ExecutorService pool = Executors.newFixedThreadPool(1);

    private static final String TAG = CustomMapSalonRenderer.class.getSimpleName();

    private final IconGenerator mHotelIconGenerator = new IconGenerator(ThisApplication.getCurrentActivity().getApplicationContext());

    private final IconGenerator mClusterIconGenerator = new IconGenerator(ThisApplication.getCurrentActivity().getApplicationContext());
    private final int STAR_WIDTH_PX = UIUtils.dpToPx(7.7f);
    private final int STAR_HIEGHT_PX = UIUtils.dpToPx(7.33f);
    public boolean stop = false;
    private Bitmap icon;
    private SalonModel selectedHotel;
    private TextView mHotelSoldTextView;
    private ImageView favouriteIcon;
    private View containerBackground;
    private GoogleMap mMap;

    public CustomMapSalonRenderer(GoogleMap map, ClusterManager<SalonModel> manager) {

        super(ThisApplication.getCurrentActivity().getApplicationContext(), map, manager);

        mMap = map;

        LayoutInflater inflater = (LayoutInflater) ThisApplication.getCurrentActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mClusterIconGenerator.setContentView(makeSquareTextView());

//        mClusterIconGenerator.setBackground(makeClusterBackground());

        View hotelCollapsedMarker = inflater.inflate(R.layout.view_salon_cluster_marker, null);

        containerBackground = hotelCollapsedMarker.findViewById(R.id.container_background);

        mHotelSoldTextView = (TextView)hotelCollapsedMarker.findViewById(R.id.custom_sold_view_cluster_marker);

        mHotelSoldTextView.setTextColor(ThisApplication.getCurrentActivity().getColor(R.color.APP_TEXT_COLOR));

        favouriteIcon =(ImageView) hotelCollapsedMarker.findViewById(R.id.fav_icon);

        mHotelIconGenerator.setContentView(hotelCollapsedMarker);

        containerBackground.setBackground(ContextCompat.getDrawable(ThisApplication.getCurrentActivity(), R.drawable.salon_marker));

        mHotelIconGenerator.setContentPadding(-UIUtils.dpToPx(42), 0, -UIUtils.dpToPx(42), -UIUtils.dpToPx(10));

        mHotelIconGenerator.setBackground(null);

    }

    @Override
    protected void onBeforeClusterItemRendered(SalonModel hotel, MarkerOptions markerOptions) {

        // Draw a single person.
        // Set the info window to show their name.

        if (stop ) return;

        if (selectedHotel != null && selectedHotel.getId().equals(hotel.getId())) {

            selectedHotel = hotel;

            reSelectSelectedHotel();

        } else {

            bindDataToMarker(hotel);

        }


        try {

            icon = mHotelIconGenerator.makeIcon();

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

            markerOptions.anchor(0.5f, 1.0f);

            icon = null;

        } catch (Throwable e) {

            Log.e("CustomMapHotelRenderer", "Error whn trying to render marker image", e);

        }

    }

    @Override
    protected void onBeforeClusterRendered(Cluster<SalonModel> cluster, MarkerOptions markerOptions) {
        // Draw multiple people.
        // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).

        if (stop) return;

        icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

        markerOptions.anchor(0.5f, 1.0f);

        icon = null;


    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }

    @Override
    protected void onClusterItemRendered(SalonModel clusterItem, final Marker marker) {

        //MarkersUtil.fadeOutMarker(marker);

        super.onClusterItemRendered(clusterItem, marker);
    }


    @Override
    public boolean onClusterItemClick(final SalonModel salonModel) {

        if (selectedHotel != null && salonModel.getId().equals(selectedHotel.getId())) {

//            hotel.setIsSkeleton(true);
//
//            FragmentManager.showHotelDetailsFragment(hotel, false);

        } else if (selectedHotel != null) {

            collapseHotelMarker(selectedHotel);

        }

        expandHotelMarker(salonModel);

        showSalonDetails(true, salonModel);

        return false;
    }

    private void expandHotelMarker(SalonModel hotel) {

        if (stop || hotel == null)  return;

        bindDataToMarker(hotel);


        try {

            icon = mHotelIconGenerator.makeIcon();

            getMarker(hotel).setIcon(BitmapDescriptorFactory.fromBitmap(icon));

            //MarkersUtil.fadeOutMarker(getMarker(hotel));

            getMarker(hotel).setZIndex(1000);

            getMarker(hotel).showInfoWindow();

            selectedHotel = hotel;


            icon = null;

        } catch (Throwable e) {

            Log.e("CustomMapHotelRenderer", "Error whn trying to render marker image", e);

        }
    }

    private void collapseHotelMarker(SalonModel hotel) {

        if (stop) return;

        if (hotel != null) {

            bindDataToMarker(hotel);

            try {

                //MarkersUtil.fadeOutMarker(getMarker(hotel));

                icon = mHotelIconGenerator.makeIcon();

                getMarker(hotel).setZIndex(0);

                getMarker(hotel).setIcon(BitmapDescriptorFactory.fromBitmap(icon));

                getMarker(hotel).setAnchor(0.5f, 1.0f);


            } catch (Throwable e) {

                Log.e("CustomMapHotelRenderer", "Error whn trying to render marker image", e);

            }

        }

    }

    @Override
    public boolean onClusterClick(final Cluster<SalonModel> hotelCluster) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (SalonModel hotel : hotelCluster.getItems()) {

            builder.include(hotel.getPosition());
        }

        final LatLngBounds bounds = builder.build();

        final float beforeZoom = mMap.getCameraPosition().zoom;

        if (beforeZoom <= mMap.getMaxZoomLevel()) {

            new Handler().post(new Runnable() {

                @Override
                public void run() {

                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                            bounds, 100), 300,
                            new GoogleMap.CancelableCallback() {
                                @Override
                                public void onFinish() {

                                    if ((int) beforeZoom == (int) mMap.getCameraPosition().zoom) {

                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMap.getCameraPosition().target, beforeZoom + 1), 300, null);

                                    }

                                }

                                @Override
                                public void onCancel() {

                                }
                            });

                }
            });

        }

        return false;
    }

    public void collapseSelectedHotelMarker() {

        if (selectedHotel != null) {

            collapseHotelMarker(selectedHotel);

            showSalonDetails(false, null);

            selectedHotel = null;

        }
    }

    public void reSelectSelectedHotel() {

        if (selectedHotel != null) {

            expandHotelMarker(selectedHotel);

            showSalonDetails(true, selectedHotel);

        }

    }


    @Override
    public Marker getMarker(SalonModel clusterItem) {

        Marker marker = super.getMarker(clusterItem);

        if (marker != null) {

            marker.setAnchor(0.5f, 1.0f);

        }

        return marker;

    }

    private LayerDrawable makeClusterBackground() {

        ShapeDrawable mColoredCircleBackground = new ShapeDrawable(new OvalShape());

        ShapeDrawable outline = new ShapeDrawable(new OvalShape());

        mColoredCircleBackground.getPaint().setColor(getColor(R.color.BLACK));

        outline.getPaint().setColor(getColor(R.color.WHITE));

        int shadowRadius = UIUtils.dpToPx(5);

        outline.getPaint().setShadowLayer(shadowRadius, 0, 0, getColor(R.color.BLACK));

        LayerDrawable background = new LayerDrawable(new Drawable[]{outline, mColoredCircleBackground});

        int strokeWidth = UIUtils.dpToPx(6);

        background.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth);

        background.setLayerInset(0, shadowRadius, shadowRadius, shadowRadius, shadowRadius);

        return background;
    }

    private SquareTextView makeSquareTextView() {

        SquareTextView squareTextView = new SquareTextView(ThisApplication.getCurrentActivity());

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        squareTextView.setLayoutParams(layoutParams);

        squareTextView.setId(com.google.maps.android.R.id.amu_text);

        squareTextView.setTextColor(getColor(R.color.WHITE));

//        squareTextView.setTypeface(FontUtil.getFont(FontsType.BLACK, "en"));

        int twelveDpi = UIUtils.dpToPx(15);

        squareTextView.setPadding(twelveDpi, twelveDpi, twelveDpi, twelveDpi);

        return squareTextView;
    }

    private void bindDataToMarker(SalonModel hotel) {

        mHotelSoldTextView.setVisibility(View.VISIBLE);

        mHotelSoldTextView.setText(hotel.getName());

    }

    private void showSalonDetails(boolean showDetails, SalonModel salonModel) {

        FragmentMapView salonMapView = FragmentManager.getFragmentFromTheStack(FragmentMapView.class);

        if (salonMapView != null) {

            salonMapView.showSalonDetails(showDetails, salonModel);

        }

    }


    public void setSelectedHotel(SalonModel selectedHotel) {
        this.selectedHotel = selectedHotel;
    }

    // recalculate number of nights instead of computing it every time wants to display marker
    public void reCalculatedNumberOfNights() {

        //numberOfNights = (int) ReservationSessionManager.sharedInstance().getNumberOfNights();
    }

}