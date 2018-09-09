package com.tasree7a.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.enums.Sizes;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

public class ProfileFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        rootView.findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

        rootView.findViewById(R.id.change_pass_btn).setOnClickListener(v -> FragmentManager.showChangePasswordFragment(getActivity()));

        ImageView image = (ImageView) rootView.findViewById(R.id.profpic);

        if (UserDefaultUtil.isFBUser()) {

            UIUtils.loadUrlIntoImageView(getContext(), ReservationSessionManager.getInstance().getFbImage(), image, Sizes.LARGE);

        } else {

            UIUtils.loadUrlIntoImageView(getContext(), UserDefaultUtil.getCurrentUser().getImageUrl(), image, Sizes.LARGE);

        }


        rootView.findViewById(R.id.cancel).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());
        rootView.findViewById(R.id.profile_image_cont).setOnClickListener(v -> {

            //TODO: TEMP -> create option menu
            AlertDialog alertDialog = new AlertDialog.Builder(ThisApplication.getCurrentActivity()).create();
            alertDialog.setTitle("Choose");
            alertDialog.setMessage("choose your picture");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Camera",
                    (dialog, which) -> {

                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);//zero can be replaced with any action code

                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Gallery",
                    (dialog, which) -> {

                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                    });

            alertDialog.show();
        });

        String name = UserDefaultUtil.getCurrentUser().getFirstName()
                + " "
                + UserDefaultUtil.getCurrentUser().getLastName();

        ((EditText) rootView.findViewById(R.id.input_full_name))
                .setText(name);

        ((EditText) rootView.findViewById(R.id.input_email))
                .setText(UserDefaultUtil.getCurrentUser().getEmail());

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.image_options_menu, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.gallery:

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                return true;

            case R.id.camera:

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);//zero can be replaced with any action code

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
