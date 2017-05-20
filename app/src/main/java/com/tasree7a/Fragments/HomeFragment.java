package com.tasree7a.Fragments;

import android.content.Intent;
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
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;

public class HomeFragment extends BaseFragment {

    RecyclerView popularSallons;

    ImageView openMenu;

    DrawerLayout nvDrawer;

    NavigationView nvView ;

    ImageView closeDrawer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        openMenu = (ImageView)rootView.findViewById(R.id.open_menu);

        nvDrawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);

        nvView = (NavigationView) rootView.findViewById(R.id.nvView);

        closeDrawer = (ImageView) nvView.getHeaderView(0).findViewById(R.id.close_menu);

        int width = (int)(getResources().getDisplayMetrics().widthPixels/1.5);

        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) nvView.getLayoutParams();

        params.width = width;

        nvView.setLayoutParams(params);

        openMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nvDrawer.openDrawer(nvView);

            }
        });

        closeDrawer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                nvDrawer.closeDrawers();

            }
        });



        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        popularSallons = (RecyclerView) rootView.findViewById(R.id.popular_sallons);

        popularSallons.setLayoutManager(new LinearLayoutManager(ThisApplication.getCurrentActivity()));

        popularSallons.setAdapter(new PopularSallonsAdapter());

    }
}
