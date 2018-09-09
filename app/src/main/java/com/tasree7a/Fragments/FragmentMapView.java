package com.tasree7a.fragments;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.IconGenerator;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.customcomponent.CustomMapSalonRenderer;
import com.tasree7a.customcomponent.SalonMapDetails;
import com.tasree7a.fragments.BaseFragment;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.utils.AppUtil;
import com.tasree7a.utils.MapsUtils;
import com.tasree7a.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by mac on 7/8/17.
 */

public class FragmentMapView extends BaseFragment {

    private static final String TAG = FragmentMapView.class.getName();

    private boolean isNearBy = false;

    List<SalonModel> salonModelList;

    private AbstractCallback callback;

    private GoogleMap mMap;

    private ClusterManager<SalonModel> mHotelClusterManager;

    private CustomMapSalonRenderer mCustomMapSalonRenderer;

    // Selected hotel from pick attraction screen that exists in the datasouce list and does't exists in the filtered list
    private SalonModel selectedHotelFromPickAttraction;

    private LatLng mapCenter;


    public void setListener(AbstractCallback callback) {

        this.callback = callback;
    }


    private boolean googlePlayAvailable = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Location location = AppUtil.getCurrentLocation();

        mapCenter = new LatLng(location.getLatitude(), location.getLongitude());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {

            rootView = inflater.inflate(R.layout.fragment_map_view, container, false);

            mMap = null;

            mHotelClusterManager = null;

            //mAttractionClusterManager = null;

        } catch (IllegalArgumentException e) {

            Log.e("HotelsMapView", "Error when trying to retrieve hotel map view", e);

            return new View(getActivity());

        } catch (InflateException e) {

            Log.e("HotelsMapView", "Error whn trying to retrieve hotel map view", e);

            return new View(getActivity());

        }

        setUpMapIfNeeded();

        return rootView;
    }


    public void showSalonDetails(boolean showDetails, final SalonModel salonModel) {

        SalonMapDetails hotelDetailsLayout = (SalonMapDetails) rootView.findViewById(R.id.salon_map_details);

        if (showDetails && salonModel != null) {

            hotelDetailsLayout.setSalonDetails(salonModel, true, null, new Runnable() {

                @Override
                public void run() {

                    mCustomMapSalonRenderer.onClusterItemClick(salonModel);
                }
            });

        }

        hotelDetailsLayout.setClickable(true);

        hotelDetailsLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                FragmentManager.showSalonDetailsFragment(salonModel);

            }
        });

        hotelDetailsLayout.animateSalonDetailsLayout(showDetails);

    }


    /**
     * THis will be called whenever the fragment is in the front, is visible
     */
    @Override
    public void fragmentIsVisible() {

        super.fragmentIsVisible();

        if (dateChanged) {

            UIUtils.showSweetLoadingDialog();

            dateChanged = false;

            mCustomMapSalonRenderer.reCalculatedNumberOfNights();

        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


    }


    /**
     * THis will be called whenever another fragment become in front if this, is hidden by another fragment
     */
    @Override
    public void fragmentIsHidden() {

        super.fragmentIsHidden();
    }


    private void onBackIconPressed() {

        FragmentManager.popCurrentVisibleFragment();

    }


    /**
     * * The mapfragment's id must be removed from the FragmentManager
     * *** or else if the same it is passed on the next time then
     * *** app will crash ***
     */
    @Override
    public void onDestroyView() {

        super.onDestroyView();

        MapsUtils.removeFragmentIfAlreadyLoaded(R.id.map);

    }


    /**
     * ** Sets up the mMap if it is possible to do so ****
     */
    public void setUpMapIfNeeded() {

        // Do a null check to confirm that we have not already instantiated the mMap.
        if (mMap == null) {

            MapFragment fragment = ((MapFragment) ThisApplication.getCurrentActivity().getFragmentManager()
                    .findFragmentById(R.id.map));

            if (AppUtil.checkPlayServices() && fragment != null) {

                googlePlayAvailable = true;

                // Try to obtain the mMap from the SupportMapFragment.
                fragment.getMapAsync(new OnMapReadyCallback() {

                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        mMap = googleMap;

                        Location location = AppUtil.getCurrentLocation();

                        IconGenerator iconGenerator = new IconGenerator(ThisApplication.getCurrentActivity());

                        MapsUtils.addSimpleMarkerToMap(location, iconGenerator, mMap);

                        // Check if we were successful in obtaining the mMap.
                        if (mMap != null) {

                            setUpMap();
                        }

                    }
                });

            }

        } else {

            setUpMap();

        }
    }


    /**
     * This is where we can add markers or lines, add listeners or move the
     * camera.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap}
     * is not null.
     */
    private void setUpMap() {

        initializeClusterManagers();

        dataSourceChanged(salonModelList);

        CameraUpdate cameraUpdate;

        // default lat lng is on top of sauid arabia
        LatLng latLng = new LatLng(23.392944, 42.923745);

        int zoom = 12;

        try {

            // try get city lat,lng
            if (mapCenter != null) {

                latLng = mapCenter;

            }

            zoom = 12;

        } catch (Throwable t) {

            Log.e(TAG, "Unable to parse center lat,lng", t);

        }

        // Updates the location and zoom of the MapView
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);


        mMap.moveCamera(cameraUpdate);

        // Add your location marker
        if (isNearBy) {

            Marker yourLocationMarker = mMap.addMarker(new MarkerOptions().anchor(0.5f, 1.0f).title(getString(R.string.YOUR_LOCATION)).position(mapCenter));

            yourLocationMarker.showInfoWindow();

        }

    }


    /**
     * Initialize cluster manager
     */
    private void initializeClusterManagers() {

        if (rootView == null || !isVisible()) {
            return;
        }

        if (mHotelClusterManager == null) {

            mHotelClusterManager = new ClusterManager<>(rootView.getContext(), mMap);

            mCustomMapSalonRenderer = new CustomMapSalonRenderer(mMap, mHotelClusterManager);

            mHotelClusterManager.setRenderer(mCustomMapSalonRenderer);

            mHotelClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<SalonModel>() {

                @Override
                public boolean onClusterClick(Cluster<SalonModel> cluster) {

                    mCustomMapSalonRenderer.onClusterClick(cluster);
                    return false;
                }
            });

            mHotelClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<SalonModel>() {

                @Override
                public boolean onClusterItemClick(SalonModel salonModel) {

                    mCustomMapSalonRenderer.onClusterItemClick(salonModel);
                    return false;
                }
            });

        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {

                mHotelClusterManager.onMarkerClick(marker);


                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                mCustomMapSalonRenderer.collapseSelectedHotelMarker();
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                //TODO Handle your code.

                //Add marker to map for clarity
                mMap.addMarker(new MarkerOptions().position(point).title("My Marker"));
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {

            @Override
            public void onCameraIdle() {

                filterMarkersBasedOnViewPort(null);

            }
        });

    }


    /**
     * Initialize loading view
     */
//    private void initializeLoadingView() {
//
//        TextView loadingText = (TextView) bind(loading_label);
//
//        loadingText.setTypeface(FontUtil.getFont(FontsType.BOOK));
//
//        if (!isNearBy && targetArea != null) {
//
//            loadingText.setText(YamsaferApplication.getContext().getString(R.string.loading_hotel_list) + targetArea.getName());
//
//        } else {
//
//            loadingText.setText(YamsaferApplication.getContext().getString(R.string.loading_hotels_near_by));
//
//        }
//
//        UIUtil.showLoadingView(rootView, this, true, null, null);
//
//    }
    private void openSelectedHotelMarker(final SalonModel hotel) {

        if (hotel == null || mMap == null) return;

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hotel.getPosition(), mMap.getMaxZoomLevel() - 2f), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {

                rootView.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        mCustomMapSalonRenderer.onClusterItemClick(hotel);

                    }
                }, 500);
            }


            @Override
            public void onCancel() {

                rootView.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        mCustomMapSalonRenderer.onClusterItemClick(hotel);
                    }
                }, 500);

            }
        });

    }


    @Override
    public void onLowMemory() {

        super.onLowMemory();

        MapFragment fragment = ((MapFragment) ThisApplication.getCurrentActivity().getFragmentManager()
                .findFragmentById(R.id.map));

        if (fragment != null) {

            fragment.onLowMemory();

        }
    }


    public void dataSourceChanged(final List<SalonModel> list) {

        if (!googlePlayAvailable || list.isEmpty()) return;

        // filter
        filterMarkersBasedOnViewPort(mCustomMapSalonRenderer != null ? new Runnable() {

            @Override
            public void run() {

                mCustomMapSalonRenderer.reSelectSelectedHotel();
            }
        } : null);


    }


    @Override
    public boolean onBackPressed() {

        // set selected attraction
        FragmentMapView.this.onBackIconPressed();

        return false;

    }


    @Override
    public void onDetach() {

        super.onDetach();

        if (mMap != null) {

            mMap.setOnMapClickListener(null);

            mMap.setOnMarkerClickListener(null);

            mMap.setOnCameraChangeListener(null);

            mMap.setBuildingsEnabled(false);

            mMap.setOnCameraIdleListener(null);

            mMap.clear();

        }

        if (mHotelClusterManager != null) {

            mCustomMapSalonRenderer.stop = true;

            mHotelClusterManager.clearItems();

            mHotelClusterManager.setOnClusterClickListener(null);

            mHotelClusterManager.setOnClusterItemClickListener(null);

        }

        if (mCustomMapSalonRenderer != null) {

            mCustomMapSalonRenderer.setOnClusterClickListener(null);

            mCustomMapSalonRenderer.setOnClusterItemClickListener(null);

        }


//        if (mAttractionClusterManager != null) {
//
//            mAttractionClusterManager.clearItems();
//
//            mAttractionClusterManager.setOnClusterClickListener(null);
//
//            mAttractionClusterManager.setOnClusterItemClickListener(null);
//
//        }
//
//        if (mCustomMapAttractionRenderer != null) {
//
//            mCustomMapAttractionRenderer.setOnClusterClickListener(null);
//
//            mCustomMapAttractionRenderer.setOnClusterItemClickListener(null);
//
//        }

    }


    private Subscription filterHotelsSubscription;


    // Filter and put the hotels based in the markers
    // Display only the hotels that is currently within the map window
    public void filterMarkersBasedOnViewPort(final Runnable onFinishClustering) {

        if (mHotelClusterManager == null) return;

        if (filterHotelsSubscription != null && !filterHotelsSubscription.isUnsubscribed()) {

            filterHotelsSubscription.unsubscribe();

        }

        List<SalonModel> tempList = new ArrayList<SalonModel>() {{
            addAll(salonModelList);
            if (selectedHotelFromPickAttraction != null) add(selectedHotelFromPickAttraction);
        }};

        mHotelClusterManager.clearItems();

        filterHotelsSubscription = Observable.from(tempList)
                .observeOn(Schedulers.computation())
                .filter(new Func1<SalonModel, Boolean>() {

                    @Override
                    public Boolean call(SalonModel filterable) {

                        boolean isApply = true;

                        return isApply;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SalonModel>() {

                    @Override
                    public void onCompleted() {

                        mHotelClusterManager.cluster();

                        if (onFinishClustering != null) {

                            new Handler().postDelayed(onFinishClustering, 1000);
                        }
                    }


                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG, "error at filtering hotels ", e);
                    }


                    @Override
                    public void onNext(SalonModel filterable) {

                        mHotelClusterManager.addItem(filterable);
                    }
                });
    }


    boolean dateChanged = false;


    public LatLng getMapCenter() {

        return mapCenter;
    }


    public void setMapCenter(LatLng mapCenter) {

        this.mapCenter = mapCenter;
    }


    public void setSalonModelList(List<SalonModel> salonModelList) {

        this.salonModelList = salonModelList;

    }

}

