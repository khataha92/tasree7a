package com.tasree7a.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tasree7a.R;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.utils.UserDefaultUtil;

public class ChangePasswordFragment extends BaseFragment {

    private EditText mOldPassword;
    private EditText mNewPassword;
    private EditText mNewPasswordConfirmation;
    private CustomButton mApply;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_change_password, container, false);

        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        this.rootView.findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());
        this.rootView.findViewById(R.id.cancel).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

        mOldPassword = rootView.findViewById(R.id.input_old_pass);
        mNewPassword = rootView.findViewById(R.id.input_new_pass);
        mNewPasswordConfirmation = rootView.findViewById(R.id.input_confirm_pass);

        mApply = rootView.findViewById(R.id.apply);
        mApply.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        mApply.setOnClickListener(v -> {
            if (isDataValid()) {
//                RetrofitManager.getInstance().pass
            }
        });
    }

    private boolean isDataValid() {
        //TODO: check if the old pass is correct
        return !TextUtils.isEmpty(mOldPassword.getText())
//                && mOldPassword.equals(UserDefaultUtil.getCurrentUser().getp)
                && !TextUtils.isEmpty(mNewPassword.getText())
                && !TextUtils.isEmpty(mNewPasswordConfirmation.getText())
                && mNewPasswordConfirmation.getText().equals(mNewPassword.getText());
    }
}