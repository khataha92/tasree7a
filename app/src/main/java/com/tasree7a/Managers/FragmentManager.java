package com.tasree7a.managers;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.HomeActivity;
import com.tasree7a.fragments.AddGalleryFragment;
import com.tasree7a.fragments.AddProductFragment;
import com.tasree7a.fragments.BaseFragment;
import com.tasree7a.fragments.BookNowFragment;
import com.tasree7a.fragments.BookScheduleFragment;
import com.tasree7a.fragments.CalenderFragment;
import com.tasree7a.fragments.ChangePasswordFragment;
import com.tasree7a.fragments.FragmentBookingList;
import com.tasree7a.fragments.FragmentFilter;
import com.tasree7a.fragments.FragmentMapView;
import com.tasree7a.fragments.FullScreenGalleryActivity;
import com.tasree7a.fragments.HomeFragment;
import com.tasree7a.fragments.ProfileFragment;
import com.tasree7a.fragments.SalonDetailsFragment;
import com.tasree7a.fragments.SettingsFragment;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.models.salonbooking.SalonService;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.models.salondetails.SalonProduct;
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

    public static BaseFragment getCurrentVisibleFragment() {
        int index = currentFragments.size() - 1;
        if (index < 0) {
            return null;
        }
        return currentFragments.get(index);
    }

    public static void showBookScheduleFragment(Context context, List<SalonService> mSalonServices) {
        BookScheduleFragment fragment = new BookScheduleFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BookScheduleFragment.SALON_SERVICES, (ArrayList<? extends Parcelable>) mSalonServices);
        fragment.setArguments(args);
        replaceFragment(context, fragment);
    }

    public static void showBookNowFragment(Context context) {
        BookNowFragment fragment = new BookNowFragment();
        replaceFragment(context, fragment);
    }

    public static void showHomeFragment(Context context) {
        HomeFragment homeFragment = new HomeFragment();
        replaceFragment(context, homeFragment);
    }

    public static void showCalendarFragment(Context context, AbstractCallback callback) {
        CalenderFragment calenderFragment = new CalenderFragment();
        calenderFragment.setCallback(callback);
        replaceFragment(context, calenderFragment);
    }

//    public static void showGalleryFullScreenFragment(Context context, List<ImageModel> images, int position) {
//        FullScreenGalleryActivity fragment = new FullScreenGalleryActivity();
//        fragment.setPosition(position);
//        fragment.setImageModelList(images);
//        replaceFragment(context, fragment);
//    }

    public static void showSettingsFragment(Context context) {
        SettingsFragment settingsFragment = new SettingsFragment();
        replaceFragment(context, settingsFragment);
    }

    public static void showChangePasswordFragment(Context context) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        replaceFragment(context, fragment);
    }

    public static void showProfileFragment(Context context) {
        ProfileFragment fragment = new ProfileFragment();
        replaceFragment(context, fragment);
    }

    //    public static void showFeedBackFragment() {
//        FragmentFeedBack fragment = new FragmentFeedBack();
//        replaceFragment(fragment, true);
//    }
    public static void showFragmentBookingList(Context context) {
        FragmentBookingList bookingList = new FragmentBookingList();
        replaceFragment(context, bookingList);
    }

    public static void showFragmentGallery(SalonModel salon, ArrayList<ImageModel> imageModels, ArrayList<SalonProduct> salonProducts) {
//        FragmentGallery fragmentGallery = new FragmentGallery();
//        fragmentGallery.setSalon(salon);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(FragmentArg.IMAGE_LIST, imageModels);
//
//        if (salonProducts != null) {
//            bundle.putSerializable(FragmentArg.PRODUCTS_LIST, salonProducts);
//        }
//
//        fragmentGallery.setArguments(bundle);
//        replaceFragment(null, fragmentGallery);
    }

    public static void showSalonDetailsFragment(Context context) {
        showSalonDetailsFragment(context, null);
    }

    public static void showSalonDetailsFragment(Context context, SalonModel salonModel) {
        SalonDetailsFragment salonDetailsFragment = new SalonDetailsFragment();
        if (salonModel != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(SalonDetailsFragment.SALON_MODEL, salonModel);
            salonDetailsFragment.setArguments(bundle);
        }
        replaceFragment(context, salonDetailsFragment);
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

    private static void replaceFragment(Context context, final BaseFragment newFragment) {
        // The current fragment will become invisible
        int indexOfCurrentFragment = currentFragments.size() - 1;
        BaseFragment currentFragment = null;
        if (indexOfCurrentFragment >= 0) {
            currentFragment = currentFragments.get(indexOfCurrentFragment);
        }

        currentFragments.add(newFragment);
        FragmentTransaction tr = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
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

    public static void popBeforeCurrentVisibleFragment(Context context) {
        int index = currentFragments.size() - 2;

        if (index < 0) {
            return;
        }

        // TODO It works fine, but the reference to the fragment still exists in the activity's backstack
        try {
            BaseFragment toPopFragment = currentFragments.remove(index);
            destroyFragmentProcess(toPopFragment);
            android.support.v4.app.FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
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

    public static void showMapViewFragment(Context context, List<SalonModel> salonModels) {
        FragmentMapView fragment = new FragmentMapView();
        fragment.setSalonModelList(salonModels);
        replaceFragment(context, fragment);
    }

    public static void showFilterFragment(Context context) {
        FragmentFilter fragment = new FragmentFilter();
        replaceFragment(context, fragment);
    }


    public static void showAddGalleryItemFragment(Context context, SalonModel salon, AbstractCallback callback) {
        AddGalleryFragment fragment = new AddGalleryFragment();
        fragment.showGallaryFragment(true);
        fragment.setSalonModel(salon);
        fragment.setCallback(callback);
        replaceFragment(context, fragment);
    }

    public static void showAddProductFragment(Context context, AbstractCallback callback) {
        AddProductFragment fragment = new AddProductFragment();
        fragment.setCallback(callback);
        replaceFragment(context, fragment);
    }
}
