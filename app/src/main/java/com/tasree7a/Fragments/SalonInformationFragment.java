package com.tasree7a.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
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
import android.widget.TextView;
import android.widget.Toast;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.customcomponent.CircularCheckBox;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.customcomponent.CustomTimePicker;
import com.tasree7a.customcomponent.SalonStaffContainer;
import com.tasree7a.enums.Gender;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.AddNewBarberRequestModel;
import com.tasree7a.models.AddNewStaffMemberDataModel;
import com.tasree7a.models.login.User;
import com.tasree7a.models.salondetails.AddNewSalonResponseModel;
import com.tasree7a.models.salondetails.SalonInformationRequestModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.utils.AppUtil;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Response;

/**
 * Created by SamiKhleaf on 10/20/17.
 */

public class SalonInformationFragment extends BaseFragment {

    private final int CAMERA_REQUEST = 1888;
    private final int GALLERY_REQUEST = 1889;

    private RelativeLayout changeImageView; //id: change_image
    private EditText salonName; //id: salon_name
    private EditText ownerNamer; //id: owner_name
    private EditText email; //id: email
    private EditText currency; //id: currency
    private EditText mobile; //id: mobile
    private CustomButton saveBtn;
    private AppCompatCheckBox male, female;
    private TextView fromTime, toTime;

    private String finalTime = null;
    private String base64Image = "";
    private boolean shouldPopFragment = true;

    private SalonInformationRequestModel salonInformationRequestModel = null;
    private SalonStaffContainer staffContainer;
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

        initViews();

        rootView.findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());
        rootView.findViewById(R.id.cancel).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

        fromTime.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(ThisApplication.getCurrentActivity());

            dialog.setContentView(R.layout.date_dialog);

            dialog.findViewById(R.id.done).setOnClickListener(null);

            dialog.findViewById(R.id.done).setAlpha(0.5f);

            dialog.findViewById(R.id.done).setEnabled(false);

            ((CustomTimePicker) dialog.findViewById(R.id.timePicker)).setOnTimeChangedListener((view, hourOfDay, minute) -> {

                if (finalTime == null) {

                    dialog.findViewById(R.id.done).setOnClickListener(null);

                    dialog.findViewById(R.id.done).setAlpha(0.5f);

                    dialog.findViewById(R.id.done).setEnabled(false);

                } else {

                    dialog.findViewById(R.id.done).setAlpha(1f);

                    dialog.findViewById(R.id.done).setEnabled(true);

                    dialog.findViewById(R.id.done).setOnClickListener(v1 -> {

                        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm");

                        SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");

                        try {
                            Date date = parseFormat.parse(finalTime);

                            fromTime.setText(displayFormat.format(date));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        dialog.dismiss();
                    });
                }


                finalTime = hourOfDay + ":" + minute;
            });

            dialog.show();
        });

        toTime = rootView.findViewById(R.id.to_hours);

        toTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ThisApplication.getCurrentActivity());

                dialog.setContentView(R.layout.date_dialog);

                dialog.findViewById(R.id.done).setOnClickListener(null);

                dialog.findViewById(R.id.done).setAlpha(0.5f);

                dialog.findViewById(R.id.done).setEnabled(false);


                ((CustomTimePicker) dialog.findViewById(R.id.timePicker)).setOnTimeChangedListener((view, hourOfDay, minute) -> {

                    if (finalTime == null) {

                        dialog.findViewById(R.id.done).setOnClickListener(null);

                        dialog.findViewById(R.id.done).setAlpha(0.5f);

                        dialog.findViewById(R.id.done).setEnabled(false);

                    } else {

                        dialog.findViewById(R.id.done).setAlpha(1f);

                        dialog.findViewById(R.id.done).setEnabled(true);

                        dialog.findViewById(R.id.done).setOnClickListener(v12 -> {

                            SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm");

                            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");

                            try {
                                Date date = parseFormat.parse(finalTime);

                                toTime.setText(displayFormat.format(date));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            dialog.dismiss();
                        });
                    }


                    finalTime = hourOfDay + ":" + minute;
                });

                dialog.show();
            }
        });


        changeImageView = rootView.findViewById(R.id.change_image);

        changeImageView.setOnClickListener(v -> {

            //TODO: TEMP -> create option menu
            AlertDialog alertDialog = new AlertDialog.Builder(ThisApplication.getCurrentActivity()).create();
            alertDialog.setTitle("Choose");
            alertDialog.setMessage("choose your picture");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Camera",
                    (dialog, which) -> {

                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, CAMERA_REQUEST);//zero can be replaced with any action code

                    });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Gallery",
                    (dialog, which) -> {

                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, GALLERY_REQUEST);//one can be replaced with any action code

                    });

            alertDialog.show();

        });

        salonName = rootView.findViewById(R.id.salon_name);

        ownerNamer = rootView.findViewById(R.id.owner_name);

        email = rootView.findViewById(R.id.email);

        mobile = rootView.findViewById(R.id.mobile);

        saveBtn = rootView.findViewById(R.id.save);

//        SalonModel user = UserDefaultUtil.getCurrentSalonUser();

//        salonName.setText(user.getName());

//        ownerNamer.setText(user.getOwnerName());

//        email.setText(UserDefaultUtil.getCurrentUser().getEmail());

        for (int i = 0; i < workingDaysIDs.length; i++) {

            workingDays[i] = rootView.findViewById(workingDaysIDs[i]);

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

//        mobile.setText(user.getOwnerMobileNumber());

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                executeRequests();
            }

        });

        checkLocationPermission();

        prefillData();

        return rootView;
    }

    private void prefillData() {
        SalonModel salonModel = UserDefaultUtil.getCurrentSalonUser();
        if (salonModel != null) {
            salonName.setText(salonModel.getName());
            ownerNamer.setText(salonModel.getOwnerName());
//            email.setText(salonModel.getEmail());
            mobile.setText(salonModel.getOwnerMobileNumber());
            if (salonModel.getSalonType() == Gender.MALE) {
                male.setChecked(true);
                female.setChecked(false);
            } else {
                female.setChecked(true);
                male.setChecked(false);
            }
            staffContainer.prefillBarbers(salonModel.getSalonBarbers());
        }
    }

    private void executeRequests() {
        prepareAddSalonRequestData();
        if (staffContainer.getBarbers() != null && staffContainer.getBarbers().size() != 0) {
            UIUtils.showLoadingView(rootView, FragmentManager.getCurrentVisibleFragment());
            RetrofitManager.getInstance().addNewSalon(salonInformationRequestModel, (isSuccess, result) -> {
                final AddNewSalonResponseModel responseModel = (AddNewSalonResponseModel) ((Response) result).body();
                addSalonBarbers(responseModel);
            });
        } else {
            Toast.makeText(ThisApplication.getCurrentActivity().getApplicationContext(),
                    "You have to Add 1 Barber at least",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLocationPermission() {
        //TODO: Add request permission,  https://stackoverflow.com/questions/44646668/streaming-upload-of-a-base64-image-using-retrofit
        if (ContextCompat.checkSelfPermission(ThisApplication.getCurrentActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ThisApplication.getCurrentActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ThisApplication.getCurrentActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1111);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (ContextCompat.checkSelfPermission(ThisApplication.getCurrentActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(ThisApplication.getCurrentActivity().getApplicationContext(), "Should Access Location", Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareAddSalonRequestData() {
        salonInformationRequestModel = new SalonInformationRequestModel();
        salonInformationRequestModel.setCityID("23");
        salonInformationRequestModel.setOwnerMobile(mobile.getText().toString());
        salonInformationRequestModel.setOwnerName(ownerNamer.getText().toString());
        salonInformationRequestModel.setSalonBase64Image(base64Image);

        Location curLoc = AppUtil.getCurrentLocation();
        if (curLoc != null) {
            salonInformationRequestModel.setSalonLat(curLoc.getLatitude() + "");
            salonInformationRequestModel.setSalonLong(curLoc.getLongitude() + "");
        } else {
            salonInformationRequestModel.setSalonLat("1122");
            salonInformationRequestModel.setSalonLong("1122");
        }
        salonInformationRequestModel.setUserID(UserDefaultUtil.getCurrentUser().getId());
        salonInformationRequestModel.setSalonName(salonName.getText().toString());
        salonInformationRequestModel.setSalonType(male.isChecked() ? "1" : "2");
    }

    private void addSalonBarbers(final AddNewSalonResponseModel responseModel) {
        RetrofitManager.getInstance().getSalonDetails(responseModel.getDetails().getSalonId(), (isSuccess, result) -> {
            SalonModel model = (SalonModel) result;
            model.setBusiness(true);
            User user = UserDefaultUtil.getCurrentUser();
            user.setSalonId(model.getId());
            UserDefaultUtil.saveUser(user);

            for (AddNewStaffMemberDataModel staffMemberDataModel : staffContainer.getBarbers()) {
                RetrofitManager.getInstance().addNewBarber(getRequestDataModel(staffMemberDataModel, responseModel), (isSuccess1, result1) -> {
                });
            }

            UIUtils.hideLoadingView(rootView, FragmentManager.getCurrentVisibleFragment());
            if (shouldPopFragment) {
                FragmentManager.showSalonDetailsFragment(model);
                FragmentManager.popBeforeCurrentVisibleFragment();
            } else {
                FragmentManager.popCurrentVisibleFragment();
            }
        });
    }

    private AddNewBarberRequestModel getRequestDataModel(AddNewStaffMemberDataModel staffMemberDataModel, AddNewSalonResponseModel responseModel) {
        AddNewBarberRequestModel barberModel;
        barberModel = new AddNewBarberRequestModel();
        barberModel.setSalonId(UserDefaultUtil.getCurrentUser().getId());
        try {
            barberModel.setFirstName(staffMemberDataModel.getStaffName().split(" ")[0]);
            barberModel.setLastName(staffMemberDataModel.getStaffName().split(" ")[1]);
        } catch (Exception e) {
            Log.e("Exception occuered", e.getMessage());
        }
        barberModel.setEmail(staffMemberDataModel.getStaffEmail());
        barberModel.setPass(staffMemberDataModel.getStaffPass());
        barberModel.setCreatedAt("1");
        barberModel.setStartTime("12");
        barberModel.setEndTime("15");
        barberModel.setUpdatedAt("16");
        barberModel.setUserName("username" + staffMemberDataModel.getStaffName() + barberModel.getSalonId());
        return barberModel;
    }

    private void initViews() {
        male = rootView.findViewById(R.id.male);
        female = rootView.findViewById(R.id.female);
        fromTime = rootView.findViewById(R.id.from_hours);
        staffContainer = rootView.findViewById(R.id.salon_staff_container);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != Activity.RESULT_CANCELED) {

            Bitmap selectedImage = null;

            if (data != null) {
                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

                    selectedImage = (Bitmap) data.getExtras().get("data");

                } else {

                    final Uri imageUri = data.getData();

                    final InputStream imageStream;

                    try {

                        imageStream = ThisApplication.getCurrentActivity().getApplicationContext().getContentResolver().openInputStream(imageUri);

                        selectedImage = BitmapFactory.decodeStream(imageStream);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                byte[] byteArray = byteArrayOutputStream.toByteArray();

                base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

                ((ImageView) rootView.findViewById(R.id.image)).setImageBitmap(selectedImage);
            }
        }
    }


    @Override
    public boolean onBackPressed() {

        return false;
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


    public boolean isShouldPopFragment() {

        return shouldPopFragment;
    }


    public void setShouldPopFragment(boolean shouldPopFragment) {

        this.shouldPopFragment = shouldPopFragment;
    }
}
