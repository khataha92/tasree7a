package com.tasree7a.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.CustomComponent.CustomSwitch;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.R;
import com.tasree7a.utils.UIUtils;

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

        rootView.findViewById(R.id.change_pass_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.showChangePasswordFragment();

            }
        });

        //TODO: When font are ready

//        ((TextView)rootView.findViewById(R.id.language_tv)).setTypeface(FontUtil.heavy());
//        ((TextView)rootView.findViewById(R.id.notifications_tv)).setTypeface(FontUtil.heavy());
//        ((TextView)rootView.findViewById(R.id.password_tv)).setTypeface(FontUtil.heavy());
//
//        ((TextView)rootView.findViewById(R.id.notification_book_text)).setTypeface(FontUtil.book());
//        ((TextView)rootView.findViewById(R.id.notification_fav_text)).setTypeface(FontUtil.book());

        initLanguageSwitch();

        return rootView;
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
