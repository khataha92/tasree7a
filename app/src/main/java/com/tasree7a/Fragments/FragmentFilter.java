package com.tasree7a.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.adapters.CitiesSpinnerAdapter;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.customcomponent.CustomCheckableGroup;
import com.tasree7a.customcomponent.CustomCheckbox;
import com.tasree7a.customcomponent.CustomRadioButton;
import com.tasree7a.customcomponent.CustomRadioGroup;
import com.tasree7a.customcomponent.CustomSwitch;
import com.tasree7a.enums.FilterType;
import com.tasree7a.enums.Gender;
import com.tasree7a.interfaces.Checkable;
import com.tasree7a.managers.FilterAndSortManager;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.SessionManager;
import com.tasree7a.models.popularsalons.CityModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.observables.FilterAndSortObservable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 6/5/17.
 */

public class FragmentFilter extends BaseFragment {

    CustomButton applyButton;

    CustomSwitch genderFilter;

    CustomRadioGroup sortTypeGroup = null;

    CustomRadioGroup filters = null;

    List<CityModel> availableCities = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_filter, container, false);

        applyButton = rootView.findViewById(R.id.btn_apply);

        sortTypeGroup = rootView.findViewById(R.id.sort_by);

        filters = rootView.findViewById(R.id.filters);

        genderFilter = rootView.findViewById(R.id.male_female);

        genderFilter.setChecked(FilterAndSortManager.getInstance().getSalonType() == Gender.FEMALE);

        genderFilter.setOnClickListener(v -> genderFilter.setChecked(!genderFilter.isChecked()));

        rootView.findViewById(R.id.layout1).setOnClickListener(v -> genderFilter.setChecked(!genderFilter.isChecked()));

        for (int i = 0; i < sortTypeGroup.getChildCount(); i++) {

            View view = sortTypeGroup.getChildAt(i);

            if (view instanceof CustomRadioButton) {

                CustomRadioButton radioButton = (CustomRadioButton) view;

                radioButton.uncheck();

                if (radioButton.getSortType() == FilterAndSortManager.getInstance().getSortType()) {

                    radioButton.check();

                }
            }
        }

        for (int i = 0; i < filters.getChildCount(); i++) {

            View view = filters.getChildAt(i);

            if (view instanceof CustomRadioButton) {

                CustomRadioButton checkbox = (CustomRadioButton) view;

                checkbox.uncheck();

                if (FilterAndSortManager.getInstance().getFilters().contains(checkbox.getFilterType())) {

                    checkbox.check();

                }
            }
        }

        applyButton.setOnClickListener(v -> {

            FilterAndSortManager filterAndSortManager = FilterAndSortManager.getInstance();

            filterAndSortManager.setSortType(((CustomRadioButton) sortTypeGroup.getCheckedItem()).getSortType());

            List<FilterType> filterTypes = new ArrayList<>();

            Checkable checkable = filters.getCheckedItem();

            filterTypes.add(((CustomRadioButton) checkable).getFilterType());

            FilterAndSortManager.getInstance().setSalonType(genderFilter.isChecked() ? Gender.FEMALE : Gender.MALE);

            filterAndSortManager.getFilters().clear();

            filterAndSortManager.getFilters().addAll(filterTypes);

            FilterAndSortObservable.getInstance().notifyFilterChanged();

            FragmentManager.popCurrentVisibleFragment();

        });

        ImageView back = rootView.findViewById(R.id.back);

        back.setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

        return rootView;
    }

    private boolean isCityContained(List<CityModel> cities, CityModel cityModel) {

        for (int i = 0; i < cities.size(); i++) {

            if (cities.get(i).getName().equalsIgnoreCase(cityModel.getName())) {

                return true;
            }
        }

        return false;
    }

}
