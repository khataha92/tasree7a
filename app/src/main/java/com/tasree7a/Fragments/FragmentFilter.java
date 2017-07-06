package com.tasree7a.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.tasree7a.Adapters.CitiesSpinnerAdapter;
import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.CustomComponent.CustomCheckableGroup;
import com.tasree7a.CustomComponent.CustomCheckbox;
import com.tasree7a.CustomComponent.CustomRadioButton;
import com.tasree7a.CustomComponent.CustomRadioGroup;
import com.tasree7a.Enums.FilterType;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.SessionManager;
import com.tasree7a.Models.PopularSalons.CityModel;
import com.tasree7a.Models.PopularSalons.SalonModel;
import com.tasree7a.Observables.FilterAndSortObservable;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.Checkable;
import com.tasree7a.Managers.FilterAndSortManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by mac on 6/5/17.
 */

public class FragmentFilter extends BaseFragment {

    CustomButton applyButton;

    CustomRadioGroup sortTypeGroup = null;

    CustomCheckableGroup filters = null;

    List<CityModel> availableCities = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_filter,null);

        applyButton = (CustomButton) rootView.findViewById(R.id.btn_apply);

        sortTypeGroup = (CustomRadioGroup) rootView.findViewById(R.id.sort_by);

        filters = (CustomCheckableGroup) rootView.findViewById(R.id.filters);

        Spinner citiesSpinner = (Spinner) rootView.findViewById(R.id.cities_spinner);

        List<SalonModel> salonModels = SessionManager.getInstance().getSalons();

        for(int i = 0; i < salonModels.size() ; i++){

            if(!isCityContained(availableCities,salonModels.get(i).getCityModel())){

                availableCities.add(salonModels.get(i).getCityModel());

            }
        }

        CitiesSpinnerAdapter adapter = new CitiesSpinnerAdapter(ThisApplication.getCurrentActivity(),android.R.layout.simple_spinner_dropdown_item,availableCities);

        citiesSpinner.setAdapter(adapter);

        for(int i = 0 ; i < sortTypeGroup.getChildCount() ; i++){

            View view = sortTypeGroup.getChildAt(i);

            if(view instanceof CustomRadioButton){

                CustomRadioButton radioButton = (CustomRadioButton) view;

                radioButton.uncheck();

                if(radioButton.getSortType() == FilterAndSortManager.getInstance().getSortType()){

                    radioButton.check();

                }
            }
        }

        for(int i = 0 ; i < filters.getChildCount() ; i++){

            View view = filters.getChildAt(i);

            if(view instanceof CustomCheckbox){

                CustomCheckbox checkbox = (CustomCheckbox) view;

                checkbox.uncheck();

                if(FilterAndSortManager.getInstance().getFilters().contains(checkbox.getFilterType())){

                    checkbox.check();

                }
            }
        }

        applyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FilterAndSortManager filterAndSortManager = FilterAndSortManager.getInstance();

                filterAndSortManager.setSortType(((CustomRadioButton) sortTypeGroup.getCheckedItem()).getSortType());

                List<FilterType> filterTypes = new ArrayList<>();

                List<Checkable> checkables = filters.getCheckedList();

                for(int i = 0 ; i < checkables.size() ; i++){

                    filterTypes.add(((CustomCheckbox)checkables.get(i)).getFilterType());
                }

                filterAndSortManager.getFilters().clear();

                filterAndSortManager.getFilters().addAll(filterTypes);

                FilterAndSortObservable.getInstance().notifyFilterChanged();

                FragmentManager.popCurrentVisibleFragment();

            }
        });

        ImageView back = (ImageView) rootView.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();

            }
        });

        return rootView;
    }

    private boolean isCityContained(List<CityModel> cities, CityModel cityModel){

        for(int i = 0 ; i < cities.size() ; i++){

            if(cities.get(i).getName().equalsIgnoreCase( cityModel.getName())){

                return true;
            }
        }

        return false;
    }

}
