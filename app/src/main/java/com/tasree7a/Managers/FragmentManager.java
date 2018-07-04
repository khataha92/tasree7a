package com.tasree7a.managers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.HomeActivity;
import com.tasree7a.fragments.AddGalleryFragment;
import com.tasree7a.fragments.AddNewSalonServiceFragment;
import com.tasree7a.fragments.AddProductFragment;
import com.tasree7a.fragments.AddStaffMemberFragment;
import com.tasree7a.fragments.BaseFragment;
import com.tasree7a.fragments.BookNowFragment;
import com.tasree7a.fragments.BookScheduleFragment;
import com.tasree7a.fragments.CalenderFragment;
import com.tasree7a.fragments.ChangePasswordFragment;
import com.tasree7a.fragments.FragmentBookingList;
import com.tasree7a.fragments.FragmentFilter;
import com.tasree7a.fragments.FragmentGallery;
import com.tasree7a.fragments.FragmentMapView;
import com.tasree7a.fragments.FullScreenGalleryFragment;
import com.tasree7a.fragments.HomeFragment;
import com.tasree7a.fragments.ProfileFragment;
import com.tasree7a.fragments.SalonDetailsFragment;
import com.tasree7a.fragments.SalonInformationFragment;
import com.tasree7a.fragments.SalonServicesFragment;
import com.tasree7a.fragments.SettingsFragment;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.models.salondetails.SalonProduct;
import com.tasree7a.utils.FragmentArg;
import com.tasree7a.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by mohammad on 4/22/15.
 * This is the manager that manages transaction between fragments and responsible for
 * replace fragments
 */

public class FragmentManager {
    private static ArrayList<BaseFragment> currentFragments = new ArrayList<>();

    public static BaseFragment getCurrentVisibleFragment() {
        int index = currentFragments.size() - 1;
        if (index < 0) {
            return null;
        }
        return currentFragments.get(index);
    }

    public static void showBookScheduleFragment() {
        BookScheduleFragment fragment = new BookScheduleFragment();
        replaceFragment(fragment);
    }

    public static void showBookNowFragment() {
        BookNowFragment fragment = new BookNowFragment();
        replaceFragment(fragment);
    }

    public static void showHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        replaceFragment(homeFragment);
    }

    public static void showCalendarFragment(AbstractCallback callback) {
        CalenderFragment calenderFragment = new CalenderFragment();
        calenderFragment.setCallback(callback);
        replaceFragment(calenderFragment);
    }

    public static void showGalleryFullScreenFragment(List<ImageModel> images, int position) {
        FullScreenGalleryFragment fragment = new FullScreenGalleryFragment();
        fragment.setPosition(position);
        fragment.setImageModelList(images);
        replaceFragment(fragment);
    }

    public static void showSettingsFragment() {
        SettingsFragment settingsFragment = new SettingsFragment();
        replaceFragment(settingsFragment);
    }

    public static void showChangePasswordFragment() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        replaceFragment(fragment);
    }

    public static void showProfileFragment() {
        ProfileFragment fragment = new ProfileFragment();
        replaceFragment(fragment);
    }

    //    public static void showFeedBackFragment() {
//        FragmentFeedBack fragment = new FragmentFeedBack();
//        replaceFragment(fragment, true);
//    }
    public static void showFragmentBookingList() {
        FragmentBookingList bookingList = new FragmentBookingList();
        replaceFragment(bookingList);
    }

    public static void showFragmentGallery(SalonModel salon, ArrayList<ImageModel> imageModels, ArrayList<SalonProduct> salonProducts) {
        FragmentGallery fragmentGallery = new FragmentGallery();
        fragmentGallery.setSalon(salon);
        Bundle bundle = new Bundle();
        bundle.putSerializable(FragmentArg.IMAGE_LIST, imageModels);

        if (salonProducts != null) {
            bundle.putSerializable(FragmentArg.PRODUCTS_LIST, salonProducts);
        }

        fragmentGallery.setArguments(bundle);
        replaceFragment(fragmentGallery);
    }

    public static void showSalonDetailsFragment(SalonModel salonModel, boolean isFullSalon) {
        SalonDetailsFragment salonDetailsFragment = new SalonDetailsFragment();
        salonDetailsFragment.setDidLoadFullSalon(isFullSalon);
        salonDetailsFragment.setSalonModel(salonModel);
        Bundle args = new Bundle();
        args.putBoolean(FragmentArg.IS_REGESTERING, false);
        salonDetailsFragment.setArguments(args);
        replaceFragment(salonDetailsFragment);
    }

//    private static void showSalonDetailsFragment(SalonModel salonModel, boolean isFullSalon, boolean showInfo) {
//
//    }

    public static void showSalonDetailsFragment(SalonModel salonModel) {
        showSalonDetailsFragment(salonModel, false);
    }

    private static void destroyFragmentProcess(BaseFragment fromFragment) {
        fromFragment.fragmentIsHidden();
        fromFragment.onDestroyView();
        fromFragment.onDestroy();
        fromFragment.onDetach();
    }

    public static void popCurrentVisibleFragment() {
        int index = currentFragments.size() - 1;

        if (index < 0) {
            return;
        }

        final BaseFragment fromFragment = currentFragments.get(index);
        BaseFragment toFragment = index > 0 ? currentFragments.get(index - 1) : null;
        UIUtils.hideSoftKeyboard();

        if (ThisApplication.getCurrentActivity() instanceof HomeActivity) {
            try {
                destroyFragmentProcess(fromFragment);
                android.support.v4.app.FragmentManager manager = ThisApplication.getCurrentActivity().getSupportFragmentManager();
                final FragmentTransaction trans = manager.beginTransaction();
                //trans.setAllowOptimization(true);
                Runnable commitRunnable = () -> {
                    trans.remove(fromFragment);
                    trans.commitNowAllowingStateLoss();
                };

                commitRunnable.run();
                manager.popBackStackImmediate();
                currentFragments.remove(index);

                if (toFragment != null) {
                    toFragment.fragmentIsVisible();
                } else {
                    ThisApplication.getCurrentActivity().finish();
                }
            } catch (Throwable t) {
                Log.e("FragmentManager", "Illegal sate exception in popCurrentVisibleFragment ", t);
            }
        }
    }

    private static void replaceFragment(final BaseFragment newFragment) {
        if (ThisApplication.getCurrentActivity() instanceof HomeActivity && !ThisApplication.getCurrentActivity().isFinishing()) {
            // Hide soft keyboard if it is visible
            InputMethodManager inputManager = (InputMethodManager) ThisApplication.getCurrentActivity().getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            HomeActivity activity = (HomeActivity) ThisApplication.getCurrentActivity();
            View v = activity.getCurrentFocus();

            if (v != null) {
                Objects.requireNonNull(inputManager).hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            // The current fragment will become invisible
            int indexOfCurrentFragment = currentFragments.size() - 1;
            BaseFragment currentFragment = null;
            if (indexOfCurrentFragment >= 0) {
                currentFragment = currentFragments.get(indexOfCurrentFragment);
            }

            currentFragments.add(newFragment);
            FragmentTransaction tr = activity.getSupportFragmentManager().beginTransaction();
            tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            tr.add(R.id.fragment_container, newFragment, newFragment.getCustomTag());
            tr.addToBackStack(newFragment.getCustomTag());

            //tr.setAllowOptimization(true);

            if (currentFragment != null) {
                currentFragment.fragmentIsHidden();
                tr.hide(currentFragment);
            }
            tr.commitAllowingStateLoss();
        }
    }

    public static void popBeforeCurrentVisibleFragment() {
        int index = currentFragments.size() - 2;

        if (index < 0) {
            return;
        }

        // TODO It works fine, but the reference to the fragment still exists in the activity's backstack
        try {
            BaseFragment toPopFragment = currentFragments.remove(index);
            destroyFragmentProcess(toPopFragment);
            android.support.v4.app.FragmentManager manager = ThisApplication.getCurrentActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(toPopFragment);
            trans.commitNowAllowingStateLoss();
        } catch (Throwable t) {
            Log.e("FragmentManager", "Illegal sate exception in popBeforeCurrentVisibleFragment ", t);
        }
    }

    // Gets the first fragment in current fragments list that's matches the same class provided
    public static <BF extends BaseFragment> BF getFragmentFromTheStack(Class<BF> fragmentClass) {
        for (BaseFragment baseFragment : currentFragments) {
            if (fragmentClass.getSimpleName().equals(baseFragment.getClass().getSimpleName())) {
                return fragmentClass.cast(baseFragment);
            }
        }
        return null;
    }

    public static void showMapViewFragment(List<SalonModel> salonModels) {
        FragmentMapView fragment = new FragmentMapView();
        fragment.setSalonModelList(salonModels);
        replaceFragment(fragment);
    }

    public static void showFilterFragment() {
        FragmentFilter fragment = new FragmentFilter();
        replaceFragment(fragment);
    }

    //TODO: Pass Name,Email, for salon from the registration
    public static void showSalonInfoFragment(boolean shouldPopFragment) {
        SalonInformationFragment fragment = new SalonInformationFragment();
        fragment.setShouldPopFragment(shouldPopFragment);
        replaceFragment(fragment);    }

    public static void showAddNewStaffFragment(SalonModel salonModel, AbstractCallback abstractCallback) {
        AddStaffMemberFragment fragment = new AddStaffMemberFragment();
        fragment.setAddStaffCallback(abstractCallback);
        fragment.setSalonModel(salonModel);
        //salonModel != null
        replaceFragment(fragment);
    }

    public static void showAddGalleryItemFragment(SalonModel salon, AbstractCallback callback) {
        AddGalleryFragment fragment = new AddGalleryFragment();
        fragment.showGallaryFragment(true);
        fragment.setSalonModel(salon);
        fragment.setCallback(callback);
        replaceFragment(fragment);
    }

    public static void showAddProductFragment(AbstractCallback callback) {
        AddProductFragment fragment = new AddProductFragment();
        fragment.setCallback(callback);
        replaceFragment(fragment);
    }

    public static void showAddNewSalonServiceFragment() {
        AddNewSalonServiceFragment fragment = new AddNewSalonServiceFragment();
        replaceFragment(fragment);
    }

    public static void showSalonServicesFragment() {
        SalonServicesFragment fragment = new SalonServicesFragment();
        replaceFragment(fragment);
    }
}
