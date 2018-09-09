package com.tasree7a.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.tasree7a.BuildConfig;
import com.tasree7a.R;
import com.tasree7a.activities.MainActivity;
import com.tasree7a.adapters.PopularSalonsAdapter;
import com.tasree7a.customcomponent.CustomSwitch;
import com.tasree7a.customcomponent.CustomTopBar;
import com.tasree7a.enums.FilterType;
import com.tasree7a.enums.Gender;
import com.tasree7a.enums.Language;
import com.tasree7a.enums.ResponseCode;
import com.tasree7a.interfaces.OnSearchBarStateChange;
import com.tasree7a.interfaces.ScrollListener;
import com.tasree7a.managers.FilterAndSortManager;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.managers.SalonsComparable;
import com.tasree7a.managers.SessionManager;
import com.tasree7a.models.favoritemodels.FavoriteDetailsModel;
import com.tasree7a.models.favoritemodels.FavoriteResponseModel;
import com.tasree7a.models.popularsalons.PopularSalonsResponseModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.observables.FavoriteChangeObservable;
import com.tasree7a.observables.FilterAndSortObservable;
import com.tasree7a.observables.LocationChangedObservable;
import com.tasree7a.observables.PermissionGrantedObservable;
import com.tasree7a.utils.AppUtil;
import com.tasree7a.utils.DefaultDividerItemDecoration;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import timber.log.Timber;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class HomeFragment extends BaseFragment implements Observer, ScrollListener {
    private static final String TAG = HomeFragment.class.getSimpleName();

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;


    private int pageIndex = 0;

    private RecyclerView popularSalons;
    private CustomTopBar topBar;
    private DrawerLayout nvDrawer;
    private NavigationView nvView;
    private ImageView closeDrawer;
    private View transparentView;
    private View loadingView;

    private List<SalonModel> filteredSalons = new ArrayList<>();
    private List<SalonModel> salons = new ArrayList<>();

    private CustomSwitch langSwitch;
    private View navHeader;

    static Location currentLocation;
    private PopularSalonsAdapter mAdapter;
    private boolean shouldRequestData = true;


    public static MyLocationListener listener = new MyLocationListener();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        addObservables();

        getUserFavouriteSalons();

        initUserLocation();

        initViews();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Remove location updates to save battery.
        stopLocationUpdates();
    }

    @Override
    public void onReachedEnd() {
        pageIndex += 1;
        getSalons();
    }

    @SuppressWarnings("ConstantConditions")
    private void requestPermissions() {
        Log.i(TAG, "Requesting permission");
        // Request permission. It's possible this can be auto answered if device policy
        // sets the permission in a given state or the user denied the permission
        // previously and checked "Never ask again".
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId, View.OnClickListener listener) {
        try {
            Snackbar.make(
                    rootView.findViewById(android.R.id.content),
                    getString(mainTextStringId),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(actionStringId), listener).show();
        } catch (Exception ignored) {
        }
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        //noinspection ConstantConditions
        int permissionState = checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(getActivity(), task -> mRequestingLocationUpdates = false);
    }

    @SuppressWarnings("ConstantConditions")
    private void initUserLocation() {
        mRequestingLocationUpdates = false;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mSettingsClient = LocationServices.getSettingsClient(getContext());

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    @SuppressWarnings("ConstantConditions")
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), locationSettingsResponse -> {
                    Log.i(TAG, "All location settings are satisfied.");

                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());

//                        updateUI();
                })
                .addOnFailureListener(getActivity(), e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Timber.i("Location settings are not satisfied. Attempting to upgrade location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Timber.i("PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Timber.e(errorMessage);
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                            mRequestingLocationUpdates = false;
                    }

//                    updateUI();
                });
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                if (salons == null || salons.isEmpty())
                    getSalonsWithLocation(mCurrentLocation);
            }
        };
    }

    private void initViews() {
        topBar = rootView.findViewById(R.id.top_bar);
        topBar.setActivityContext(getActivity());
        initTopBar();

        nvDrawer = rootView.findViewById(R.id.drawer_layout);
        nvView = rootView.findViewById(R.id.nvView);
        navHeader = nvView.getHeaderView(0);
        closeDrawer = nvView.getHeaderView(0).findViewById(R.id.close_menu);
        initSideMenu();

        transparentView = rootView.findViewById(R.id.transparent_view);
        loadingView = rootView.findViewById(R.id.loading);
        showLoadingView();

        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) nvView.getLayoutParams();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels / 1.5);
        nvView.setLayoutParams(params);

        initPopularSalonsRecyclerView();
    }

    private void initPopularSalonsRecyclerView() {
        popularSalons = rootView.findViewById(R.id.popular_sallons);
        popularSalons.setLayoutManager(new LinearLayoutManager(getContext()));
        popularSalons.addItemDecoration(new DefaultDividerItemDecoration(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.list_item_divider)));
        mAdapter = new PopularSalonsAdapter(getActivity(), salons, this);
        popularSalons.setAdapter(mAdapter);
    }

    private void addObservables() {
        FilterAndSortObservable.getInstance().addObserver(this);
        PermissionGrantedObservable.getInstance().addObserver(this);
        FavoriteChangeObservable.sharedInstance().addObserver(this);
        LocationChangedObservable.sharedInstance().addObserver(this);
    }

    private void initTopBar() {
        topBar.setOnFirstIconClickListener(v -> nvDrawer.openDrawer(nvView));
        topBar.setOnSearchBarStateChange(new OnSearchBarStateChange() {
            @Override
            public void onSearchOpen() {
                transparentView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchClose() {
                transparentView.setVisibility(View.GONE);
            }
        });

        topBar.setOnSearchTextChanged(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().toLowerCase();
                List<SalonModel> salonModels = new ArrayList<>();
                if (filteredSalons == null) {
                    return;
                }
                for (int i = 0; i < filteredSalons.size(); i++) {
                    if (filteredSalons.get(i).getName().toLowerCase().contains(searchText)) {
                        salonModels.add(filteredSalons.get(i));
                    }
                }

                ((PopularSalonsAdapter) popularSalons.getAdapter()).setSalonModels(salonModels);
                popularSalons.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {
                transparentView.setVisibility(View.GONE);
            }
        });
    }

    private void initSideMenu() {
        initProfileImage();
        initLangButton();
        initMenuItems();
        initCloseButton();
    }

    private void initCloseButton() {
        closeDrawer.setOnClickListener(v -> nvDrawer.closeDrawers());
    }

    private void initProfileImage() {
        navHeader.findViewById(R.id.profile_image).setOnClickListener(v -> {
            if (!UserDefaultUtil.isFBUser() || UserDefaultUtil.isBusinessUser())
                FragmentManager.showProfileFragment(getActivity());
        });
    }

    private void initMenuItems() {
        nvView.getMenu().getItem(4).setVisible(UserDefaultUtil.isBusinessUser());
        nvView.setNavigationItemSelectedListener(
                menuItem -> {
                    int itemId = menuItem.getItemId();
                    switch (itemId) {
                        case R.id.bookings:
                            FragmentManager.showFragmentBookingList(getActivity());
                            break;
                        case R.id.map_view:
                            FragmentManager.showMapViewFragment(getActivity(), SessionManager.getInstance().getSalons());
                            break;
                        case R.id.favorites:
                            FilterAndSortManager.getInstance().getFilters().add(FilterType.FAVORITE);
                            FilterAndSortObservable.getInstance().notifyFilterChanged();
                            break;
                        case R.id.sallons:
                            FragmentManager.showHomeFragment(getActivity());
                            break;
                        case R.id.logout:
                            UserDefaultUtil.logout();
                            UserDefaultUtil.saveUserToken("");
                            AccessToken.setCurrentAccessToken(null);
                            startActivity(new Intent(getContext(), MainActivity.class));
                            Objects.requireNonNull(getActivity()).finish();
                            break;
                        case R.id.settings:
                            FragmentManager.showSettingsFragment(getActivity());
                            break;
                    }

                    nvDrawer.closeDrawers();
                    return true;
                });
    }

    private void initLangButton() {
        langSwitch = navHeader.findViewById(R.id.switch_item);
        langSwitch.setChecked(UserDefaultUtil.isAppLanguageArabic());
        langSwitch.setAction(() -> UIUtils.showConfirmLanguageChangeDialog(getContext(), langSwitch));
        if (UserDefaultUtil.getAppLanguage() == Language.AR) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }

    private void getSalons() {
        Location location = AppUtil.getCurrentLocation();
        getSalonsWithLocation(location);
    }

    private void getSalonsWithLocation(Location location) {
        if (location == null || filteredSalons == null || !shouldRequestData) {
            return;
        }

        RetrofitManager.getInstance().getNearestSalons(UserDefaultUtil.getCurrentUser().getId(), location.getLatitude(), location.getLongitude(), pageIndex, (isSuccess, result) -> {
            if (isSuccess) {
                hideLoadingView();
                PopularSalonsResponseModel model = (PopularSalonsResponseModel) result;
                if (model.getResponseCode() == ResponseCode.SUCCESS) {
                    if (model.getSalons() == null || model.getSalons().isEmpty()) {
                        mAdapter.setHasMore(false);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        salons.addAll(model.getSalons());
                        filteredSalons.addAll(salons);
                        SessionManager.getInstance().setSalons(salons);
                        mAdapter.setHasMore(true);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
            rootView.findViewById(R.id.loading).setVisibility(View.GONE);
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof FavoriteChangeObservable) {
            popularSalons.getAdapter().notifyDataSetChanged();
        } else if (o instanceof FilterAndSortObservable) {
            pageIndex = 0;
            filteredSalons = new ArrayList<>();
            List<FilterType> filterTypes = FilterAndSortManager.getInstance().getFilters();
            if (filterTypes.contains(FilterType.FAVORITE)) {
                filteredSalons = UserDefaultUtil.getFavoriteSalons();
                popularSalons.getAdapter().notifyDataSetChanged();
            } else {
                shouldRequestData = false;
                boolean isMale = FilterAndSortManager.getInstance().getSalonType() == Gender.MALE;
                List<SalonModel> allSalons = SessionManager.getInstance().getSalons();
                for (int i = 0; i < allSalons.size(); i++) {
                    boolean shouldContain = true;
                    for (int j = 0; j < filterTypes.size(); j++) {
                        if (!allSalons.get(i).filterValue(filterTypes.get(j))) {
                            shouldContain = false;
                            break;
                        }
                    }

                    if (shouldContain
                            && ((isMale && allSalons.get(i).getSalonType() == Gender.MALE)
                            || (!isMale && allSalons.get(i).getSalonType() == Gender.FEMALE))) {
                        filteredSalons.add(allSalons.get(i));
                    }
                }
            }

            Collections.sort(filteredSalons, new SalonsComparable(FilterAndSortManager.getInstance().getSortType()));
            ((PopularSalonsAdapter) popularSalons.getAdapter()).setSalonModels(filteredSalons);
            popularSalons.getAdapter().notifyDataSetChanged();
        }
    }

    private void showLoadingView() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingView() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeObservables();
    }

    private void removeObservables() {
        FilterAndSortObservable.getInstance().deleteObserver(this);
        PermissionGrantedObservable.getInstance().deleteObserver(this);
        FavoriteChangeObservable.sharedInstance().deleteObserver(this);
        LocationChangedObservable.sharedInstance().deleteObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        currentLocation = null;
    }

    public void getUserFavouriteSalons() {
        if (!UserDefaultUtil.isBusinessUser()) {
            RetrofitManager.getInstance().getUserFavoriteSalons(UserDefaultUtil.getCurrentUser().getId(), (isSuccess, result) -> {
                if (isSuccess && result != null && ((FavoriteResponseModel) result).getDetails() != null) {
                    List<SalonModel> salonModels = new ArrayList<>();
                    Iterator it = ((FavoriteResponseModel) result).getDetails().iterator();
                    UserDefaultUtil.saveFavoriteSalons(salonModels);

                    while (it.hasNext()) {
                        salonModels.add(((FavoriteDetailsModel) it).getSalonModel());
                    }
                }
            });
        }
    }

    private static class MyLocationListener implements LocationListener, Serializable {
        @Override
        public void onLocationChanged(Location location) {
            if (currentLocation == null) {
                currentLocation = location;
                LocationChangedObservable.sharedInstance().setLocationChanged(currentLocation);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
