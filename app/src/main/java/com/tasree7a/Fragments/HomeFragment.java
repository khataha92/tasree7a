package com.tasree7a.Fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.tasree7a.Adapters.PopularSallonsAdapter;
import com.tasree7a.CustomComponent.CustomSwitch;
import com.tasree7a.CustomComponent.CustomTopBar;
import com.tasree7a.Enums.FilterType;
import com.tasree7a.Enums.ResponseCode;
import com.tasree7a.Managers.FilterAndSortManager;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Managers.SalonsComparable;
import com.tasree7a.Managers.SessionManager;
import com.tasree7a.Models.PopularSalons.PopularSalonsResponseModel;
import com.tasree7a.Models.PopularSalons.SalonModel;
import com.tasree7a.Observables.FavoriteChangeObservable;
import com.tasree7a.Observables.FilterAndSortObservable;
import com.tasree7a.Observables.PermissionGrantedObservable;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.MainActivity;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.interfaces.OnSearchBarStateChange;
import com.tasree7a.utils.AppUtil;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class HomeFragment extends BaseFragment implements Observer {

    RecyclerView popularSallons;

    CustomTopBar topBar;

    DrawerLayout nvDrawer;

    NavigationView nvView ;

    ImageView closeDrawer;

    View transparentView;

    View loadingView;

    List<SalonModel> filteredSalons;

    CustomSwitch langSwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        FilterAndSortObservable.getInstance().addObserver(this);

        PermissionGrantedObservable.getInstance().addObserver(this);

        FavoriteChangeObservable.sharedInstance().addObserver(this);

        topBar = (CustomTopBar) rootView.findViewById(R.id.top_bar);

        nvDrawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);

        nvView = (NavigationView) rootView.findViewById(R.id.nvView);

        closeDrawer = (ImageView) nvView.getHeaderView(0).findViewById(R.id.close_menu);

        transparentView = rootView.findViewById(R.id.transparent_view);

        int width = (int)(getResources().getDisplayMetrics().widthPixels/1.5);

        loadingView = rootView.findViewById(R.id.loading);


        initLangButton();

        nvView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        int itemId = menuItem.getItemId();

                        switch (itemId){

                            case R.id.map_view:

                                FragmentManager.showMapViewFragment(SessionManager.getInstance().getSalons());

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

                                getActivity().finish();

                                break;

                            case R.id.settings:

                                FragmentManager.showSettingsFragment();

                                break;

                        }

                        nvDrawer.closeDrawers();

                        return true;
                    }
                });

        showLoadingView();

        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) nvView.getLayoutParams();

        params.width = width;

        nvView.setLayoutParams(params);

        topBar.setOnFirstIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nvDrawer.openDrawer(nvView);

            }
        });

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

                String searchText = s.toString();

                List<SalonModel> salonModels = new ArrayList<>();

                for(int i = 0 ; i < filteredSalons.size() ; i++){

                    if(filteredSalons.get(i).getName().contains(searchText)){

                        salonModels.add(filteredSalons.get(i));

                    }
                }

                ((PopularSallonsAdapter)popularSallons.getAdapter()).setSalonModels(salonModels);

                popularSallons.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        closeDrawer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                nvDrawer.closeDrawers();

            }
        });

        popularSallons = (RecyclerView) rootView.findViewById(R.id.popular_sallons);

        popularSallons.setLayoutManager(new LinearLayoutManager(ThisApplication.getCurrentActivity()));

        return rootView;

    }


    private void initLangButton() {

        langSwitch = (CustomSwitch) nvView.getHeaderView(0).findViewById(R.id.switch_item);

        langSwitch.setChecked(UserDefaultUtil.isAppLanguageArabic());

        langSwitch.setAction(new Runnable() {

            @Override
            public void run() {

                UIUtils.showConfirmLanguageChangeDialog(langSwitch);

            }
        });

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Location location = AppUtil.getCurrentLocation();

        getSalons(location);

    }

    private void getSalons(Location location){

        if(location == null){

            return;
        }

        RetrofitManager.getInstance().getNearestSalons(location.getLatitude(), location.getLongitude(), new AbstractCallback() {

            @Override
            public void onResult(boolean isSuccess, Object result) {

                if(isSuccess){

                    hideLoadingView();

                    PopularSalonsResponseModel model = (PopularSalonsResponseModel) result;

                    if(model.getResponseCode() == ResponseCode.SUCCESS){

                        List<SalonModel> salons = model.getSalons();

                        filteredSalons = salons;

                        SessionManager.getInstance().setSalons(salons);

                        PopularSallonsAdapter adapter = new PopularSallonsAdapter();

                        adapter.setSalonModels(salons);

                        popularSallons.setAdapter(adapter);

                    } else{

                        // TODO: 7/4/17 to show message
                    }


                }

            }
        });

    }

    @Override
    public void update(Observable o, Object arg) {

        if(o instanceof FavoriteChangeObservable) {

            popularSallons.getAdapter().notifyDataSetChanged();

        } else if(o instanceof PermissionGrantedObservable){

            Location location = AppUtil.getCurrentLocation();

            getSalons(location);

        } else if(o instanceof FilterAndSortObservable){

            filteredSalons = new ArrayList<>();

            List<FilterType> filterTypes = FilterAndSortManager.getInstance().getFilters();

            List<SalonModel> allSalons = SessionManager.getInstance().getSalons();

            for(int i = 0; i < allSalons.size() ; i++){

                boolean shouldContain = true;

                for(int j = 0 ; j < filterTypes.size() ;j ++){

                    if(!allSalons.get(i).filterValue(filterTypes.get(j))){

                        shouldContain = false;

                        break;
                    }
                }

                if(shouldContain){

                    filteredSalons.add(allSalons.get(i));

                }
            }

            Collections.sort(filteredSalons,new SalonsComparable(FilterAndSortManager.getInstance().getSortType()));

            ((PopularSallonsAdapter)popularSallons.getAdapter()).setSalonModels(filteredSalons);

            popularSallons.getAdapter().notifyDataSetChanged();
        }
    }

    private void showLoadingView(){

        if(loadingView != null){

            loadingView.setVisibility(View.VISIBLE);

        }
    }

    private void hideLoadingView(){

        if(loadingView != null){

            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        FilterAndSortObservable.getInstance().deleteObserver(this);

        PermissionGrantedObservable.getInstance().deleteObserver(this);

        FavoriteChangeObservable.sharedInstance().deleteObserver(this);
    }
}
