package com.tasree7a.fragments;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.MainActivity;
import com.tasree7a.adapters.PopularSalonsAdapter;
import com.tasree7a.customcomponent.CustomSwitch;
import com.tasree7a.customcomponent.CustomTopBar;
import com.tasree7a.enums.FilterType;
import com.tasree7a.enums.Gender;
import com.tasree7a.enums.Language;
import com.tasree7a.enums.ResponseCode;
import com.tasree7a.interfaces.OnSearchBarStateChange;
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
import com.tasree7a.services.LocationService;
import com.tasree7a.utils.AppUtil;
import com.tasree7a.utils.DefaultDividerItemDecoration;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class HomeFragment extends BaseFragment implements Observer {
    private RecyclerView popularSalons;
    private CustomTopBar topBar;
    private DrawerLayout nvDrawer;
    private NavigationView nvView;
    private ImageView closeDrawer;
    private View transparentView;
    private View loadingView;
    private List<SalonModel> filteredSalons;
    private CustomSwitch langSwitch;
    private View navHeader;

    static Location currentLocation;

    public static MyLocationListener listener = new MyLocationListener();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        addObservables();
        topBar = rootView.findViewById(R.id.top_bar);

        //TODO: temp -> till using callbacks in topBar
        topBar.setActivityContext(getActivity());

        nvDrawer = rootView.findViewById(R.id.drawer_layout);
        nvView = rootView.findViewById(R.id.nvView);
        navHeader = nvView.getHeaderView(0);
        closeDrawer = nvView.getHeaderView(0).findViewById(R.id.close_menu);
        transparentView = rootView.findViewById(R.id.transparent_view);
        int width = (int) (getResources().getDisplayMetrics().widthPixels / 1.5);
        loadingView = rootView.findViewById(R.id.loading);
        getUserFavouriteSalons();
        initTopBar();
        initSideMenue();
        showLoadingView();
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) nvView.getLayoutParams();
        params.width = width;
        nvView.setLayoutParams(params);
        popularSalons = rootView.findViewById(R.id.popular_sallons);
        popularSalons.setLayoutManager(new LinearLayoutManager(getContext()));
        popularSalons.addItemDecoration(new DefaultDividerItemDecoration(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.list_item_divider)));

        return rootView;
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

    private void initSideMenue() {
        initProfileImage();
        initLangButton();
        initMenueItems();
        initCloseButton();
    }

    private void initCloseButton() {
        closeDrawer.setOnClickListener(v -> nvDrawer.closeDrawers());
    }

    private void initProfileImage() {
        navHeader.findViewById(R.id.profile_image).setOnClickListener(v -> {
            if (!UserDefaultUtil.isFBUser())
                FragmentManager.showProfileFragment(getActivity());
        });
    }

    private void initMenueItems() {
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
                            FilterAndSortManager.getInstance().reset();
                            FilterAndSortObservable.getInstance().notifyFilterChanged();
                            break;
                        case R.id.logout:
                            UserDefaultUtil.logout();
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

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setMessage(getString(R.string.LOCATION_DISABLED_MESSAGE))

                .setCancelable(false)
                .setPositiveButton(getString(R.string.ENABLE_LOCATION), (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton(getString(R.string.CLOSE), (dialog, id) -> FragmentManager.popCurrentVisibleFragment());
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean isLocationServiceEnabled = AppUtil.isLocationServiceEnabled();
        if (!isLocationServiceEnabled) {
            buildAlertMessageNoGps();
        } else {
            getSallons();
        }

        Intent intent = new Intent(getActivity(), LocationService.class);
        Objects.requireNonNull(getContext()).startService(intent);
    }

    private void getSallons() {
        Location location = AppUtil.getCurrentLocation();
        getSalons(location);
    }

    private void getSalons(Location location) {
        if (location == null || filteredSalons != null) {
            return;
        }

        rootView.findViewById(R.id.loading).setVisibility(View.VISIBLE);

        RetrofitManager.getInstance().getNearestSalons(location.getLatitude(), location.getLongitude(), (isSuccess, result) -> {
            if (isSuccess) {
                hideLoadingView();
                PopularSalonsResponseModel model = (PopularSalonsResponseModel) result;
                if (model.getResponseCode() == ResponseCode.SUCCESS) {
                    List<SalonModel> salons = model.getSalons();
                    filteredSalons = salons;
                    SessionManager.getInstance().setSalons(salons);
                    PopularSalonsAdapter adapter = new PopularSalonsAdapter(getActivity());
                    adapter.setSalonModels(salons);
                    popularSalons.setAdapter(adapter);
                }
            }
            rootView.findViewById(R.id.loading).setVisibility(View.GONE);
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof LocationChangedObservable) {
            getSallons();
        } else if (o instanceof FavoriteChangeObservable) {
            popularSalons.getAdapter().notifyDataSetChanged();
        } else if (o instanceof PermissionGrantedObservable) {
            Location location = AppUtil.getCurrentLocation();
            getSalons(location);
        } else if (o instanceof FilterAndSortObservable) {
            filteredSalons = new ArrayList<>();
            List<FilterType> filterTypes = FilterAndSortManager.getInstance().getFilters();
            if (filterTypes.contains(FilterType.FAVORITE)) {
                filteredSalons = UserDefaultUtil.getFavoriteSalons();
                popularSalons.getAdapter().notifyDataSetChanged();
            } else {
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

                    if (shouldContain && (isMale && allSalons.get(i).getSalonType() == Gender.MALE) || (!isMale && allSalons.get(i).getSalonType() == Gender.FEMALE)) {
                        shouldContain = true;
                    } else {
                        shouldContain = false;
                        break;
                    }
                    if (shouldContain) {
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
                if (isSuccess && result != null) {
                    List<SalonModel> salonModels = new ArrayList<>();
                    for (FavoriteDetailsModel details : ((FavoriteResponseModel) result).getDetails()) {
                        salonModels.add(details.getSalonModel());
                    }
                    UserDefaultUtil.saveFavoriteSalons(salonModels);
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
