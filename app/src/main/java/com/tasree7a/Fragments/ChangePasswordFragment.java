package com.tasree7a.Fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.R;

/**
 * Created by SamiKhleaf on 7/30/17.
 */

public class ChangePasswordFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_change_password, container, false);

        rootView.findViewById(R.id.apply).getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        rootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();

            }
        });

        return rootView;
    }
}