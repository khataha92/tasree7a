package com.tasree7a.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.R;

/**
 * Created by SamiKhleaf on 7/28/17.
 */

public class BusinessRegistrationFragment extends Fragment {

    public BusinessRegistrationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_business_registration, container, false);
    }

}
