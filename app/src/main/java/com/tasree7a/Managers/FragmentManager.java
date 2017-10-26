package com.tasree7a.Managers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tasree7a.Fragments.AddGalleryFragment;
import com.tasree7a.Fragments.AddNewSalonServiceFragment;
import com.tasree7a.Fragments.AddProductFragment;
import com.tasree7a.Fragments.AddStaffMemberFragment;
import com.tasree7a.Fragments.BaseFragment;
import com.tasree7a.Fragments.BookNowFragment;
import com.tasree7a.Fragments.BookScheduleFragment;
import com.tasree7a.Fragments.CalenderFragment;
import com.tasree7a.Fragments.ChangePasswordFragment;
import com.tasree7a.Fragments.FragmentBookingList;
import com.tasree7a.Fragments.FragmentFeedBack;
import com.tasree7a.Fragments.FragmentFilter;
import com.tasree7a.Fragments.FragmentGallery;
import com.tasree7a.Fragments.FragmentMapView;
import com.tasree7a.Fragments.FullScreenGalleryFragment;
import com.tasree7a.Fragments.HomeFragment;
import com.tasree7a.Fragments.ProfileFragment;
import com.tasree7a.Fragments.SalonDetailsFragment;
import com.tasree7a.Fragments.SalonInformationFragment;
import com.tasree7a.Fragments.SalonServicesFragment;
import com.tasree7a.Fragments.SettingsFragment;
import com.tasree7a.Models.Gallery.ImageModel;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.Models.SalonDetails.SalonProduct;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.HomeActivity;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.FragmentArg;
import com.tasree7a.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohammad on 4/22/15.
 * This is the manager that manages transaction between fragments and responsible for
 * replace fragments
 */

public class FragmentManager {

    private static ArrayList<BaseFragment> currentFragments = new ArrayList<>();

    public static boolean isLastFragment(){

        return currentFragments.size() == 1;

    }

    public static BaseFragment getCurrentVisibleFragment() {

        int index = currentFragments.size() - 1;

        if (index < 0) {

            return null;

        }

        return currentFragments.get(index);
    }


    public static void showBookScheduleFragment() {

        BookScheduleFragment fragment = new BookScheduleFragment();

        replaceFragment(fragment, true);

    }


    public static void showBookNowFragment() {

        BookNowFragment fragment = new BookNowFragment();

        replaceFragment(fragment, true);
    }


    public static void showHomeFragment() {

        HomeFragment homeFragment = new HomeFragment();

        replaceFragment(homeFragment, true);

    }


    public static void showCalendarFragment(AbstractCallback callback) {

        CalenderFragment calenderFragment = new CalenderFragment();

        calenderFragment.setCallback(callback);

        replaceFragment(calenderFragment, true);
    }


    public static void showGalleryFullScreenFragment(List<ImageModel> images, int position) {

        FullScreenGalleryFragment fragment = new FullScreenGalleryFragment();

        fragment.setPosition(position);

        fragment.setImageModelList(images);

        replaceFragment(fragment, true);

    }


    public static void showSettingsFragment() {

        SettingsFragment settingsFragment = new SettingsFragment();

        replaceFragment(settingsFragment, true);

    }


    public static void showChangePasswordFragment() {

        ChangePasswordFragment fragment = new ChangePasswordFragment();

        replaceFragment(fragment, true);

    }


    public static void showProfileFragment() {

        ProfileFragment fragment = new ProfileFragment();

        replaceFragment(fragment, true);

    }


    public static void showFeedBackFragment() {

        FragmentFeedBack fragment = new FragmentFeedBack();

        replaceFragment(fragment, true);

    }


    public static void showFragmentBookingList() {

        FragmentBookingList bookingList = new FragmentBookingList();

        replaceFragment(bookingList, true);
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

        replaceFragment(fragmentGallery, true);
    }


    public static void showSalonDetailsFragment(SalonModel salonModel, boolean isFullSalon) {

        showSalonDetailsFragment(salonModel, isFullSalon, false);

    }


    public static void showSalonDetailsFragment(SalonModel salonModel, boolean isFullSalon, boolean showInfo) {

        SalonDetailsFragment salonDetailsFragment = new SalonDetailsFragment();

        salonDetailsFragment.setDidLoadFullSalon(isFullSalon);

        salonDetailsFragment.setSalonModel(salonModel);

        Bundle args = new Bundle();

        args.putBoolean(FragmentArg.IS_REGESTERING, showInfo);

        salonDetailsFragment.setArguments(args);

        replaceFragment(salonDetailsFragment, true);

    }


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

                Runnable commitRunnable = new Runnable() {

                    public void run() {

                        trans.remove(fromFragment);

                        trans.commitNowAllowingStateLoss();

                    }

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


    private static void replaceFragment(final BaseFragment newFragment, boolean enableBack) {

        if (ThisApplication.getCurrentActivity() instanceof HomeActivity && !ThisApplication.getCurrentActivity().isFinishing()) {

            // Hide soft keyboard if it is visible
            InputMethodManager inputManager = (InputMethodManager) ThisApplication.getCurrentActivity().getSystemService(Context
                    .INPUT_METHOD_SERVICE);

            HomeActivity activity = (HomeActivity) ThisApplication.getCurrentActivity();

            View v = activity.getCurrentFocus();

            if (v != null) {

                inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

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

            if (enableBack) {

                tr.addToBackStack(newFragment.getCustomTag());

            }

            //tr.setAllowOptimization(true);


            if (currentFragment != null) {

                currentFragment.fragmentIsHidden();

                BaseFragment finalCurrentFragment = currentFragment;

                tr.hide(finalCurrentFragment);

            }

            tr.commitAllowingStateLoss();

        }

    }


    public static void popToRoot() {

        UIUtils.hideSoftKeyboard();

        android.support.v4.app.FragmentManager manager = ThisApplication.getCurrentActivity().getSupportFragmentManager();

        FragmentTransaction trans = manager.beginTransaction();

        for (int i = 1; i < currentFragments.size(); ) {

            currentFragments.get(i).onDestroyView();

            currentFragments.get(i).onDestroy();

            currentFragments.get(i).onDetach();

            trans.remove(currentFragments.get(i));

            trans.commitNowAllowingStateLoss();

            manager.popBackStackImmediate();

            currentFragments.remove(i);

        }

        currentFragments.get(0).fragmentIsVisible();

        //new Handler().postDelayed(() -> {if (currentFragments!= null && !currentFragments.isEmpty())currentFragments.get(0).fragmentIsVisible();}, 200);

    }


    public static boolean fragmentExistsInStack(Class<?> clazz) {

        for (BaseFragment baseFragment : currentFragments) {

            String fragmentTag = baseFragment.getClass().getSimpleName();

            if (fragmentTag.equals(clazz.getSimpleName())) {

                return true;

            }

        }

        return false;

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


    // Gets the last fragment in current fragments list that's matches the same class provided
    public static <BF extends BaseFragment> BF getLastFragmentFromTheStack(Class<BF> fragmentClass) {

        for (int i = currentFragments.size() - 1; i >= 0; i--) {

            BaseFragment baseFragment = currentFragments.get(i);

            if (fragmentClass.getSimpleName().equals(baseFragment.getClass().getSimpleName())) {

                return fragmentClass.cast(baseFragment);

            }

        }

        return null;

    }


    /**
     * Clean all fragments from android.support.v4.app.FragmentManager and current fragments list
     */
    public static void cleanBackStack() {

        try {

            ThisApplication.getCurrentActivity().getSupportFragmentManager().popBackStack(null, android.support.v4.app.FragmentManager
                    .POP_BACK_STACK_INCLUSIVE);

        } catch (Throwable t) {

        }

        currentFragments.clear();


    }


    public static <T extends BaseFragment> List<T> getFragmentsOfType(@NonNull Class<T> fragmentClass) {

        List<T> list = new ArrayList<>();

        for (BaseFragment baseFragment : currentFragments) {

            if (fragmentClass == null || fragmentClass.getSimpleName().equals(baseFragment.getClass().getSimpleName())) {

                list.add(fragmentClass.cast(baseFragment));

            }

        }

        return list;
    }


    public static void showMapViewFragment(List<SalonModel> salonModels) {

        FragmentMapView fragment = new FragmentMapView();

        fragment.setSalonModelList(salonModels);

        replaceFragment(fragment, true);
    }


    public static void showFilterFragment() {

        FragmentFilter fragment = new FragmentFilter();

        replaceFragment(fragment, true);
    }


    public static void showSalonInfoFragment() {

        SalonInformationFragment fragment = new SalonInformationFragment();

        replaceFragment(fragment, true);

    }


    public static void showAddNewStaffFragment(AbstractCallback abstractCallback) {

        showAddNewStaffFragment(null, abstractCallback);

    }

    public static void showAddNewStaffFragment(SalonModel salonModel, AbstractCallback abstractCallback) {

        AddStaffMemberFragment fragment = new AddStaffMemberFragment();

        fragment.setAddStaffCallback(abstractCallback);

        fragment.setSalonModel(salonModel);

        //salonModel != null
        replaceFragment(fragment, true);

    }


    public static void showAddGalleryItemFragment(SalonModel salon ,AbstractCallback callback) {

        AddGalleryFragment fragment = new AddGalleryFragment();

        fragment.showGallaryFragment(true);

        fragment.setSalonModel(salon);

        fragment.setCallback(callback);

        replaceFragment(fragment, true);

    }


    public static void showAddProductFragment(AbstractCallback callback) {

        AddProductFragment fragment = new AddProductFragment();

        fragment.setCallback(callback);

        replaceFragment(fragment, true);
    }


    public static void showAddNewSalonServiceFragment() {

        AddNewSalonServiceFragment fragment = new AddNewSalonServiceFragment();

        replaceFragment(fragment, true);

    }


    public static void showSalonServicesFragment() {

        SalonServicesFragment fragment = new SalonServicesFragment();

        replaceFragment(fragment, true);

    }
}
