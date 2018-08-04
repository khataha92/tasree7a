package com.tasree7a.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.R;
import com.tasree7a.activities.SalonInformationActivity;
import com.tasree7a.customcomponent.CustomSwitch;
import com.tasree7a.fragments.BaseFragment;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import static android.view.View.GONE;
import static com.tasree7a.utils.UserDefaultUtil.isAppLanguageArabic;

/**
 * Created by kodeX95 on 7/20/17.
 */

public class SettingsFragment extends BaseFragment {

    private CustomSwitch langSwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        rootView.findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

        initLanguageSwitch();
        initPassView();
        initInfoView();
        return rootView;
    }


    private void initInfoView() {
        if (!UserDefaultUtil.isBusinessUser())
            rootView.findViewById(R.id.info_cont).setVisibility(GONE);
        else {
            rootView.findViewById(R.id.change_info_btn).setOnClickListener(v -> SalonInformationActivity.launch(this, true));
        }
    }

    private void initPassView() {
        if (UserDefaultUtil.isFBUser()) {
            rootView.findViewById(R.id.pass_container).setVisibility(GONE);
            return;
        }
        rootView.findViewById(R.id.change_pass_btn).setOnClickListener(v -> FragmentManager.showChangePasswordFragment(getActivity()));
    }

    private void initLanguageSwitch() {
        langSwitch = rootView.findViewById(R.id.lang_switch);
        langSwitch.setChecked(isAppLanguageArabic());

        //   local    isChecked
        //    ar   ->   true
        //    en   ->   false
        langSwitch.setAction(() -> UIUtils.showConfirmLanguageChangeDialog(getContext(), langSwitch));
    }
}
