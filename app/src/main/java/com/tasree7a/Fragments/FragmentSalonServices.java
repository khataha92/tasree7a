package com.tasree7a.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

import com.tasree7a.Adapters.CitiesSpinnerAdapter;
import com.tasree7a.Adapters.SalonServicesAdapter;
import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.CustomComponent.CustomCheckableGroup;
import com.tasree7a.CustomComponent.CustomCheckbox;
import com.tasree7a.CustomComponent.CustomRadioButton;
import com.tasree7a.CustomComponent.CustomRadioGroup;
import com.tasree7a.CustomComponent.CustomSwitch;
import com.tasree7a.Enums.FilterType;
import com.tasree7a.Enums.Gender;
import com.tasree7a.Managers.FilterAndSortManager;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.SessionManager;
import com.tasree7a.Models.PopularSalons.CityModel;
import com.tasree7a.Models.PopularSalons.SalonModel;
import com.tasree7a.Models.SalonBooking.SalonService;
import com.tasree7a.Observables.FilterAndSortObservable;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.Checkable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 6/5/17.
 */

public class FragmentSalonServices extends BaseFragment {

    List<SalonService> salonServices = new ArrayList<SalonService>(){{

        add(new SalonService(){{setName("Hair only"); setPrice(10.5); setImageUrl("http://www.catherineandcosalon.com/pics/service-men.jpg");}});
        add(new SalonService(){{setName("Hair two"); setPrice(12.3); setImageUrl("https://img.grouponcdn.com/needish/98cpiUJF2xkkcu1GaDCJ/g7-700x420/v1/c700x420.jpg");}});
        add(new SalonService(){{setName("Hawajib"); setPrice(2.7); setImageUrl("http://www.sahelieyebrowthreading.com/wp-content/uploads/2016/02/man-service-salon.jpg");}});
        add(new SalonService(){{setName("le7yeh"); setPrice(6.1); setImageUrl("http://endimages.s3.amazonaws.com/legacy/1414718193_Cejasdreamstime.jpg");}});
        add(new SalonService(){{setName("Test"); setPrice(5.34); setImageUrl("http://www.jolie-shopping.com/wp-content/uploads/2016/02/cancela_barber_lo-skin-fade-pompadour-1.jpg");}});
        add(new SalonService(){{setName("Test 2"); setPrice(5); setImageUrl("https://dorar.at/imup2/2014-04/13527627181.jpg");}});
        add(new SalonService(){{setName("Hair only"); setPrice(4); setImageUrl("http://www.jolie-shopping.com/wp-content/uploads/2016/02/cancela_barber_lo-skin-fade-pompadour-1.jpg");}});
        add(new SalonService(){{setName("Hair only"); setPrice(2); setImageUrl("https://i.ytimg.com/vi/W2tK3F6RZPo/hqdefault.jpg");}});
        add(new SalonService(){{setName("Hair and Hawajib"); setPrice(3); setImageUrl("https://i.ytimg.com/vi/bvF6ixC6zoM/maxresdefault.jpg");}});
        add(new SalonService(){{setName("Hir and rejleen"); setPrice(4); setImageUrl("http://ift.tt/2cUdFZF");}});
        add(new SalonService(){{setName("massage"); setPrice(12); setImageUrl("https://i.ytimg.com/vi/65oxK-DgnZs/maxresdefault.jpg");}});
        add(new SalonService(){{setName("test2"); setPrice(8); setImageUrl("https://i.ytimg.com/vi/K77kKEU9tfs/maxresdefault.jpg");}});
        add(new SalonService(){{setName("Hi"); setPrice(6); setImageUrl("http://bayyraq.s3.amazonaws.com/2016/06/hairstyles-3.jpg");}});
        add(new SalonService(){{setName("Hello"); setPrice(6.4); setImageUrl("https://dorar.at/imup2/2014-04/13527627181.jpg");}});
        add(new SalonService(){{setName("How are you"); setPrice(2.5); setImageUrl("https://i.ytimg.com/vi/W2tK3F6RZPo/hqdefault.jpg");}});
        add(new SalonService(){{setName("testing data"); setPrice(21); setImageUrl("https://lh3.googleusercontent.com/ABM6Tejc2Ny3t4VD5W04WcOexkhWFzOi0_VcPEkJN_MItzfjLU5SmmStgaOTIvIHACI=h900");}});
        add(new SalonService(){{setName("nothing"); setPrice(6); setImageUrl("https://s-media-cache-ak0.pinimg.com/originals/8e/7f/7e/8e7f7e9dab33c48ba666878ffbdeeb93.jpg");}});

    }};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_book_select_service,null);

        RecyclerView salonService = (RecyclerView) rootView.findViewById(R.id.services_list);

        salonService.setLayoutManager(new GridLayoutManager(getContext(),2));

        salonService.setAdapter(new SalonServicesAdapter(salonServices));

        return rootView;
    }
}
