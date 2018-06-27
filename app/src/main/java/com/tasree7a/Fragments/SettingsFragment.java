package com.tasree7a.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.R;
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

    CustomSwitch langSwitch;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();

            }
        });

        //TODO: When font are ready

        initLanguageSwitch();

        initPassView();

        initInfoView();

        return rootView;
    }


    private void initInfoView() {

        if (!UserDefaultUtil.isBusinessUser())
            rootView.findViewById(R.id.info_cont).setVisibility(GONE);

        else {

            rootView.findViewById(R.id.change_info_btn).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //TODO: Add Existing data
                    FragmentManager.showSalonInfoFragment(false);

                }
            });

        }

    }


    private void initPassView() {

        if (UserDefaultUtil.isFBUser()) {

            rootView.findViewById(R.id.pass_container).setVisibility(GONE);

            return;
        }

        rootView.findViewById(R.id.change_pass_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.showChangePasswordFragment();

            }
        });


    }


    private void initLanguageSwitch() {

        langSwitch = ((CustomSwitch) rootView.findViewById(R.id.lang_switch));

        langSwitch.setChecked(isAppLanguageArabic());

        Log.d("switchh", "is check: " + langSwitch.isChecked());

        //   local    isChecked
        //    ar   ->   true
        //    en   ->   false

        langSwitch.setAction(new Runnable() {

            @Override
            public void run() {

                UIUtils.showConfirmLanguageChangeDialog(langSwitch);

            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }
}
