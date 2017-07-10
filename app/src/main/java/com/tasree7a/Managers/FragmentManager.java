package com.tasree7a.Managers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tasree7a.Fragments.BaseFragment;
import com.tasree7a.Fragments.FragmentFilter;
import com.tasree7a.Fragments.FragmentGallery;
import com.tasree7a.Fragments.FragmentMapView;
import com.tasree7a.Fragments.HomeFragment;
import com.tasree7a.Fragments.SalonDetailsFragment;
import com.tasree7a.Models.Gallery.ImageModel;
import com.tasree7a.Models.PopularSalons.SalonModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.HomeActivity;
import com.tasree7a.utils.FragmentArg;
import com.tasree7a.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohammad on 4/22/15.
 * This is the manager that manages transaction between fragments and responsible for
 * replace fragments
 */

public class FragmentManager  {

    private static ArrayList<BaseFragment> currentFragments = new ArrayList<>();

    public static BaseFragment getCurrentVisibleFragment() {

        int index = currentFragments.size() - 1;

        if (index < 0) {

            return null;

        }

        return currentFragments.get(index);
    }

    public static void showHomeFragment() {

        HomeFragment homeFragment = new HomeFragment();

        replaceFragment(homeFragment, true);

    }

    public static void showFragmentGallery(ArrayList<ImageModel> imageModels){

        FragmentGallery fragmentGallery = new FragmentGallery() ;

        Bundle bundle = new Bundle();

        bundle.putSerializable(FragmentArg.IMAGE_LIST,imageModels);

        fragmentGallery.setArguments(bundle);

        replaceFragment(fragmentGallery,true);
    }

    public static void showSalonDetailsFragment() {

        SalonDetailsFragment homeFragment = new SalonDetailsFragment();

        replaceFragment(homeFragment, true);

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

    public static void showMapViewFragment(List<SalonModel> salonModels){

        FragmentMapView fragment = new FragmentMapView();

        fragment.setSalonModelList(salonModels);

        replaceFragment(fragment,true);
    }

    public static void showFilterFragment() {

        FragmentFilter fragment = new FragmentFilter();

        replaceFragment(fragment, true);
    }


}
