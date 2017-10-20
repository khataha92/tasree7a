package com.tasree7a.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.Login.User;
import com.tasree7a.Models.Signup.SignupResponseModel;
import com.tasree7a.R;
import com.tasree7a.utils.FragmentArg;

import java.util.Currency;

/**
 * Created by SamiKhleaf on 10/20/17.
 */

public class SalonInformationFragment extends BaseFragment {

    private RelativeLayout changeImageView; //id: change_image

    private EditText salonName; //id: salon_name

    private EditText ownerNamer; //id: owner_name

    private EditText email; //id: email

    private EditText currency; //id: currency

    private EditText address; //id: address

    private EditText mobile; //id: mobile

    private CustomButton saveBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.view_salon_info, container, false);

        changeImageView = (RelativeLayout) rootView.findViewById(R.id.change_image);

        salonName = (EditText) rootView.findViewById(R.id.salon_name);

        ownerNamer = (EditText) rootView.findViewById(R.id.owner_name);

        email = (EditText) rootView.findViewById(R.id.email);

        currency = (EditText) rootView.findViewById(R.id.currency);

        address = (EditText) rootView.findViewById(R.id.address);

        mobile = (EditText) rootView.findViewById(R.id.mobile);

        saveBtn = (CustomButton) rootView.findViewById(R.id.save);

        Bundle argumentsBundle = getArguments();

        if (argumentsBundle != null) {

            User user = ((SignupResponseModel) argumentsBundle.getSerializable(FragmentArg.SALON_INFO)).getUserDetails().getUser();

            salonName.setText(user.getFirstName() + " " + user.getLastName());

            ownerNamer.setText(user.getUserId());

            email.setText(user.getEmail());

            //TODO: Remove those: written for testing purposes

            currency.setText("ILS");

            address.setText("Ramallah");

            mobile.setText("0595086491");

        }


        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                RetrofitManager.getInstance().addNewSalon(salonName.getText().toString(),
                        ownerNamer.getText().toString(),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "");

            }

        });
        return rootView;
    }
}
