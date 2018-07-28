package com.tasree7a.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.adapters.ViewPagerAdapter;
import com.tasree7a.fragments.BusinessRegistrationFragment;
import com.tasree7a.fragments.CustomerRegistrationFragment;
import com.tasree7a.utils.AppUtil;

/**
 * Created by mac on 5/3/17.
 */

public class SignupActivity extends AppCompatActivity {

    TabLayout tabs;

    ViewPager tabsPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        AppUtil.checkAppLanguage(this);

        initTabsView();
    }


    @Override
    protected void onResume() {

        super.onResume();

        ThisApplication.setCurrentActivity(this);
    }


    public void setUpViewPager(ViewPager tabsViewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new CustomerRegistrationFragment(), getApplicationContext().getResources().getString(R.string.CUSTOMER_TAB_TITLE));

        adapter.addFragment(new BusinessRegistrationFragment(), getApplicationContext().getResources().getString(R.string.BUSINESS_TAB_TITLE));

        tabsViewPager.setAdapter(adapter);

    }


    private void initTabsView() {


        tabs = (TabLayout) findViewById(R.id.tabs);

        tabsPager = (ViewPager) findViewById(R.id.viewpager);

        setUpViewPager(tabsPager);

        tabs.setupWithViewPager(tabsPager);

        findViewById(R.id.tabs).bringToFront();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabsPager.setCurrentItem(tab.getPosition());

            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }


            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
