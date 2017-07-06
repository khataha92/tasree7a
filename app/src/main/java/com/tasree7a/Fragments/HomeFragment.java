package com.tasree7a.Fragments;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.tasree7a.Adapters.PopularSallonsAdapter;
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
import com.tasree7a.Observables.FilterAndSortObservable;
import com.tasree7a.Observables.PermissionGrantedObservable;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.interfaces.OnSearchBarStateChange;
import com.tasree7a.utils.AppUtil;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        FilterAndSortObservable.getInstance().addObserver(this);

        PermissionGrantedObservable.getInstance().addObserver(this);

        topBar = (CustomTopBar) rootView.findViewById(R.id.top_bar);

        nvDrawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);

        nvView = (NavigationView) rootView.findViewById(R.id.nvView);

        closeDrawer = (ImageView) nvView.getHeaderView(0).findViewById(R.id.close_menu);

        transparentView = rootView.findViewById(R.id.transparent_view);

        int width = (int)(getResources().getDisplayMetrics().widthPixels/1.5);

        loadingView = rootView.findViewById(R.id.loading);

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

        if(o instanceof PermissionGrantedObservable){

            Location location = AppUtil.getCurrentLocation();

            getSalons(location);

        } else if(o instanceof FilterAndSortObservable){

            List<SalonModel> filteredSalons = new ArrayList<>();

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
    }
}
