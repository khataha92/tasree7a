package com.tasree7a.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

/**
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    private AbstractCallback permissionsCallback;

    private String customTag = "";

    public View rootView;

    private boolean isDestroyed = false;

    private Runnable onActivityResult = null;

    // This flag indicates if fragmentIsVisible() is called once from onResume
    // In general case: fragmentIsVisible() is called in two senarios
    //      1) when this fragemnt first created, thus must be called in onResume()
    //      2) when back to this fragment from another fragment (popCurrentFragment made this fragment visible)
    // so, in case if the first senario we need to call fragmentIsVisible() once, to avoid calling it in normal onResume operations
    // and to make sure the first call will be after onCreateView()
    private boolean isCalledFromOnResume = true;


    public void fragmentIsVisible() {

    }

    /**
     * THis will be called whenever another fragment become in front if this, is hidden by another fragment
     */
    public void fragmentIsHidden() {


    }


    public boolean onBackPressed() {

        if (FragmentManager.getCurrentVisibleFragment() instanceof HomeFragment){

            ThisApplication.getCurrentActivity().finishAffinity();

        } else {

            FragmentManager.popCurrentVisibleFragment();

        }

        return true;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // register this fragment for EventBus
        try {

            EventBus.getDefault().register(this);

        } catch (Throwable t) {

            // No subscribe methods in this fragment
        }


        UIUtils.forceHideKeyboard();

        UIUtils.hideSoftKeyboard();
    }


    @Override
    public void onResume() {
        super.onResume();

        if (isCalledFromOnResume) {

            fragmentIsVisible();

            isCalledFromOnResume = false;

        }

    }


    @Override
    public void onPause() {

        super.onPause();

    }

    @Override
    public void onDestroy() {

        // Weird null pointer happens on some devices inside onDestroy() android implementation
        try {
            super.onDestroy();
        } catch (Throwable t) {
        }


        // clear top bar once because onDestroy will be called again after the new fragment
        // initialization which will make it's (the second one) custom bar without listeners
        if (!isDestroyed) {

            // unregister this fragment for EventBus
            try {

                EventBus.getDefault().unregister(this);

            } catch (Throwable t) {
            }

        }

        isDestroyed = true;

    }

    @Override
    public void onAttach(android.app.Activity activity) {

        super.onAttach(activity);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onStart() {

        super.onStart();

    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onDetach() {

        super.onDetach();

        onActivityResult = null;

    }



    public String getCustomTag(){
        return getClass().getSimpleName();
    }

    public void setCustomTag(String customTag){
        this.customTag = customTag;
    }

    public AbstractCallback getPermissionsCallback() {
        return permissionsCallback;
    }

    public void setPermissionsCallback(AbstractCallback permissionsCallback) {
        this.permissionsCallback = permissionsCallback;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (onActivityResult != null) {

            onActivityResult.run();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//
//            case Constants.PERMISSION_REQUEST_CODE: {
//
//                if (grantResults.length == 0
//                        || grantResults[0] !=
//                        PackageManager.PERMISSION_GRANTED) {
//
//                    Log.i(TAG, "Permission "+ Arrays.toString(permissions)+" has been denied by user");
//
//                    if (permissionsCallback != null) permissionsCallback.onResult(false, null);
//
//                } else {
//
//                    Log.i(TAG, "Permission "+ Arrays.toString(permissions)+" has been granted by user");
//
//                    if (permissionsCallback != null) permissionsCallback.onResult(true, null);
//
//                }
//
//                return;
//            }
//
//        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }


    public Runnable getOnActivityResult() {
        return onActivityResult;
    }

    public void setOnActivityResult(Runnable onActivityResult) {
        this.onActivityResult = onActivityResult;
    }

    public void onTransitionEnd(){

    }
}
