package com.tasree7a.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tasree7a.Adapters.PopularSallonsAdapter;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;

public class HomeFragment extends BaseFragment {

    RecyclerView popularSallons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

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
