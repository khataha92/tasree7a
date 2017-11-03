package com.tasree7a.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tasree7a.Enums.Sizes;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.ReservationSessionManager;
import com.tasree7a.Models.Login.User;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

/**
 * Created by SamiKhleaf on 7/31/17.
 */

public class ProfileFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

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

        ImageView image = (ImageView) rootView.findViewById(R.id.profpic);

        if (UserDefaultUtil.isFBUser()) {

            UIUtils.loadUrlIntoImageView(ReservationSessionManager.getInstance().getFbImage(), image, Sizes.LARGE);

        } else {

            UIUtils.loadUrlIntoImageView(UserDefaultUtil.getCurrentUser().getImageUrl(), image, Sizes.LARGE);

        }


        rootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();
            }

        });
        rootView.findViewById(R.id.profile_image_cont).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO: TEMP -> create option menu
                AlertDialog alertDialog = new AlertDialog.Builder(ThisApplication.getCurrentActivity()).create();
                alertDialog.setTitle("Choose");
                alertDialog.setMessage("choose your picture");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Camera",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);//zero can be replaced with any action code

                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Gallery",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                            }
                        });

                alertDialog.show();
            }
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
