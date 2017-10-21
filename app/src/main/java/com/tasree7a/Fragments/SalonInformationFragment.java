package com.tasree7a.Fragments;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tasree7a.CustomComponent.CircularCheckBox;
import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.Login.User;
import com.tasree7a.Models.SalonDetails.SalonInformationRequestModel;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.Models.Signup.SignupResponseModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.utils.AppUtil;
import com.tasree7a.utils.FragmentArg;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * Created by SamiKhleaf on 10/20/17.
 */

public class SalonInformationFragment extends BaseFragment {

    private RelativeLayout changeImageView; //id: change_image

    private EditText salonName; //id: salon_name

    private EditText ownerNamer; //id: owner_name

    private EditText email; //id: email

    private EditText currency; //id: currency

    private EditText address; //id: address

    private EditText mobile; //id: mobile

    private CustomButton saveBtn;

    private String base64Image = "";

    private SalonInformationRequestModel salonInformationRequestModel = null;

    private CircularCheckBox[] workingDays = new CircularCheckBox[7];

    private int[] workingDaysIDs = new int[]{

            R.id.sat,
            R.id.sun,
            R.id.mon,
            R.id.tue,
            R.id.wed,
            R.id.thu,
            R.id.fri

    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.view_salon_info, container, false);

        changeImageView = (RelativeLayout) rootView.findViewById(R.id.change_image);

        changeImageView.setOnClickListener(new View.OnClickListener() {

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

        salonName = (EditText) rootView.findViewById(R.id.salon_name);

        ownerNamer = (EditText) rootView.findViewById(R.id.owner_name);

        email = (EditText) rootView.findViewById(R.id.email);

//        currency = (EditText) rootView.findViewById(R.id.currency);

        address = (EditText) rootView.findViewById(R.id.address);

        mobile = (EditText) rootView.findViewById(R.id.mobile);

        saveBtn = (CustomButton) rootView.findViewById(R.id.save);

        User user = UserDefaultUtil.getCurrentUser();

        salonName.setText(user.getFirstName() + " " + user.getLastName());

        ownerNamer.setText(user.getUserId());

        email.setText(user.getEmail());

        for (int i = 0; i < workingDaysIDs.length; i++) {

            workingDays[i] = (CircularCheckBox) rootView.findViewById(workingDaysIDs[i]);

            final int finalI = i;

            workingDays[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (workingDays[finalI].isChecked()) {

                        workingDays[finalI].uncheck();

                    } else {

                        workingDays[finalI].check();

                    }

                }
            });

        }

        //TODO: Remove those: written for testing purposes

//        currency.setText("ILS");

//        address.setText("Ramallah");
//
        Location location = AppUtil.getCurrentLocation();

        if (location != null) {

            address.setText(location.getLatitude() + ", " + location.getLongitude());

        }

        mobile.setText("0595086491");

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                salonInformationRequestModel = new SalonInformationRequestModel();

                salonInformationRequestModel.setCityID("");
                salonInformationRequestModel.setOwnerMobile(mobile.getText().toString());
                salonInformationRequestModel.setOwnerName(ownerNamer.getText().toString());
                salonInformationRequestModel.setSalonBase64Image(base64Image);
                salonInformationRequestModel.setSalonLat("");
                salonInformationRequestModel.setSalonLong("");
                salonInformationRequestModel.setUserID("");
                salonInformationRequestModel.setSalonName(salonName.getText().toString());
                salonInformationRequestModel.setSalonType("");

                RetrofitManager.getInstance().addNewSalon(salonInformationRequestModel);

            }

        });

//        address.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                FragmentManager.showMapViewFragment(new ArrayList<SalonModel>());
//
//            }
//        });
        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        final Uri imageUri = data.getData();

        final InputStream imageStream;

        try {

            imageStream = ThisApplication.getCurrentActivity().getApplicationContext().getContentResolver().openInputStream(imageUri);

            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            ((ImageView) rootView.findViewById(R.id.image)).setImageBitmap(selectedImage);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Log.d("imagebase64", base64Image);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
