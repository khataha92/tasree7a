package com.tasree7a.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tasree7a.Fragments.BaseFragment;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.ThisApplication;

/**
 * Created by KhalidTaha on 4/20/17.
 */

public class UIUtils {

    public static int dpToPx(float dp) {

        float resultPix = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ThisApplication.getCurrentActivity().getResources().getDisplayMetrics());

        return (int)resultPix;
    }

    public static int getColor(int colorResId){

        return ThisApplication.getCurrentActivity().getResources().getColor(colorResId);

    }

    public static int changeAlpha(int color, float alpha) {

        int aChannel = (int)(alpha * 255);

        return Color.argb(aChannel, Color.red(color), Color.green(color), Color.blue(color));
    }

    public static void hideSoftKeyboard(EditText editText) {

        if (ThisApplication.getCurrentActivity() == null) {

            return;

        }

        // Hide soft keyboard if it is visible
        InputMethodManager inputManager = (InputMethodManager) ThisApplication.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        View v = editText != null ? editText : ThisApplication.getCurrentActivity().getCurrentFocus();

        if (v != null) {

            changeSoftKeyboardMode(FragmentManager.getCurrentVisibleFragment(), WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        }

    }

    public static void forceHideKeyboard(EditText editText) {

        Log.d("keyboard", "forceHide!");

        if (ThisApplication.getCurrentActivity() == null) {

            return;

        }

        // Hide soft keyboard if it is visible
        InputMethodManager inputManager = (InputMethodManager) ThisApplication.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        View v = editText != null ? editText : ThisApplication.getCurrentActivity().getCurrentFocus();

        if (v != null) {

            changeSoftKeyboardMode(FragmentManager.getCurrentVisibleFragment(), WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

        }


    }

    public static void forceHideKeyboard() {

        forceHideKeyboard(null);

    }

    public static void showSoftKeyboard(EditText text){

        if(text == null) {

            return;

        }

        InputMethodManager imm = (InputMethodManager) ThisApplication.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        text.requestFocus();
    }

    public static void hideSoftKeyboard() {

        hideSoftKeyboard(null);

    }

    public static void changeSoftKeyboardMode(BaseFragment fragment, int keyboardMode) {

        if (fragment != null
                && !fragment.isDestroyed()
                && fragment.getActivity() != null
                && fragment.getActivity().getWindow() != null) {

            fragment.getActivity().getWindow().setSoftInputMode(keyboardMode);

        }
    }
}
