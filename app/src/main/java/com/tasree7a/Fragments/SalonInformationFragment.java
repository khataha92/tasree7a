package com.tasree7a.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Base64;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.tasree7a.CustomComponent.CircularCheckBox;
import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.CustomComponent.CustomTimePicker;
import com.tasree7a.CustomComponent.SalonStaffContainer;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.AddNewBarberRequestModel;
import com.tasree7a.Models.AddNewStaffMemberDataModel;
import com.tasree7a.Models.Login.User;
import com.tasree7a.Models.SalonDetails.AddNewSalonResponseModel;
import com.tasree7a.Models.SalonDetails.SalonInformationRequestModel;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.AppUtil;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

    private RelativeLayout changeImageView; //id: change_image

    private EditText salonName; //id: salon_name

    private EditText ownerNamer; //id: owner_name

    private EditText email; //id: email

    private EditText currency; //id: currency

    private EditText mobile; //id: mobile

    private CustomButton saveBtn;

    private AppCompatCheckBox male, female;

    private String base64Image = "";

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

    private TextView fromTime, toTime;

    String finalTime = null;

    private final int CAMERA_REQUEST = 1888;

    private final int GALLERY_REQUEST = 1889;

    private boolean shouldPopFragment = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.view_salon_info, container, false);

        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();
            }
        });

        rootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();
            }
        });

        male = (AppCompatCheckBox) rootView.findViewById(R.id.male);

        female = (AppCompatCheckBox) rootView.findViewById(R.id.female);

        fromTime = (TextView) rootView.findViewById(R.id.from_hours);

//        staffContainer = (SalonStaffContainer) rootView.findViewById(R.id.salon_staff_container);

        fromTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ThisApplication.getCurrentActivity());

                dialog.setContentView(R.layout.date_dialog);

                dialog.findViewById(R.id.done).setOnClickListener(null);

                dialog.findViewById(R.id.done).setAlpha(0.5f);

                dialog.findViewById(R.id.done).setEnabled(false);


                ((CustomTimePicker) dialog.findViewById(R.id.timePicker)).setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                        if (finalTime == null) {

                            dialog.findViewById(R.id.done).setOnClickListener(null);

                            dialog.findViewById(R.id.done).setAlpha(0.5f);

                            dialog.findViewById(R.id.done).setEnabled(false);

                        } else {

                            dialog.findViewById(R.id.done).setAlpha(1f);

                            dialog.findViewById(R.id.done).setEnabled(true);

                            dialog.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm");

                                    SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");

                                    try {
                                        Date date = parseFormat.parse(finalTime);

                                        fromTime.setText(displayFormat.format(date));

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    dialog.dismiss();
                                }
                            });
                        }


                        finalTime = hourOfDay + ":" + minute;
                    }
                });

                dialog.show();
            }
        });

        toTime = (TextView) rootView.findViewById(R.id.to_hours);

        toTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ThisApplication.getCurrentActivity());

                dialog.setContentView(R.layout.date_dialog);

                dialog.findViewById(R.id.done).setOnClickListener(null);

                dialog.findViewById(R.id.done).setAlpha(0.5f);

                dialog.findViewById(R.id.done).setEnabled(false);


                ((CustomTimePicker) dialog.findViewById(R.id.timePicker)).setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                        if (finalTime == null) {

                            dialog.findViewById(R.id.done).setOnClickListener(null);

                            dialog.findViewById(R.id.done).setAlpha(0.5f);

                            dialog.findViewById(R.id.done).setEnabled(false);

                        } else {

                            dialog.findViewById(R.id.done).setAlpha(1f);

                            dialog.findViewById(R.id.done).setEnabled(true);

                            dialog.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm");

                                    SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");

                                    try {
                                        Date date = parseFormat.parse(finalTime);

                                        toTime.setText(displayFormat.format(date));

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    dialog.dismiss();
                                }
                            });
                        }


                        finalTime = hourOfDay + ":" + minute;
                    }
                });

                dialog.show();
            }
        });


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
                                startActivityForResult(takePicture, CAMERA_REQUEST);//zero can be replaced with any action code

                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Gallery",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, GALLERY_REQUEST);//one can be replaced with any action code

                            }
                        });

                alertDialog.show();

            }
        });

        salonName = (EditText) rootView.findViewById(R.id.salon_name);

        ownerNamer = (EditText) rootView.findViewById(R.id.owner_name);

        email = (EditText) rootView.findViewById(R.id.email);

        mobile = (EditText) rootView.findViewById(R.id.mobile);

        saveBtn = (CustomButton) rootView.findViewById(R.id.save);

//        SalonModel user = UserDefaultUtil.getCurrentSalonUser();

//        salonName.setText(user.getName());

//        ownerNamer.setText(user.getOwnerName());

//        email.setText(UserDefaultUtil.getCurrentUser().getEmail());

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

//        mobile.setText(user.getOwnerMobileNumber());

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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

                    return;
                }

                salonInformationRequestModel = new SalonInformationRequestModel();
                salonInformationRequestModel.setCityID("23");
                salonInformationRequestModel.setOwnerMobile(mobile.getText().toString());
                salonInformationRequestModel.setOwnerName(ownerNamer.getText().toString());
                salonInformationRequestModel.setSalonBase64Image(base64Image);
                salonInformationRequestModel.setSalonLat(AppUtil.getCurrentLocation().getLatitude() + "");
                salonInformationRequestModel.setSalonLong(AppUtil.getCurrentLocation().getLongitude() + "");
                salonInformationRequestModel.setUserID(UserDefaultUtil.getCurrentUser().getId());
                salonInformationRequestModel.setSalonName(salonName.getText().toString());
                salonInformationRequestModel.setSalonType(male.isChecked() ? "0" : "1");

                UIUtils.showLoadingView(rootView, FragmentManager.getCurrentVisibleFragment());
//
//                if (staffContainer.getBarbers() != null || staffContainer.getBarbers().size() == 0) {
//
//                    RetrofitManager.getInstance().addNewSalon(salonInformationRequestModel, new AbstractCallback() {
//
//                        @Override
//                        public void onResult(boolean isSuccess, Object result) {
//
//                            final AddNewSalonResponseModel responseModel = (AddNewSalonResponseModel) ((Response) result).body();
//
//                            RetrofitManager.getInstance().getSalonDetails(responseModel.getDetails().getSalonId(), new AbstractCallback() {
//
//                                @Override
//                                public void onResult(boolean isSuccess, Object result) {
//
//                                    SalonModel model = (SalonModel) result;
//
//                                    model.setBusiness(true);
//
//                                    AddNewBarberRequestModel barberModel;
//
//                                    User user = UserDefaultUtil.getCurrentUser();
//
//                                    user.setSalonId(model.getId());
//
//                                    UserDefaultUtil.saveUser(user);
//
//                                    for (AddNewStaffMemberDataModel staffMemberDataModel : staffContainer.getBarbers()) {
//
//                                        barberModel = new AddNewBarberRequestModel();
//
//                                        barberModel.setSalonId(responseModel.getDetails().getSalonId());
//
//                                        barberModel.setLastName(staffMemberDataModel.getStaffName().split(" ")[1]);
//
//                                        barberModel.setFirstName(staffMemberDataModel.getStaffName().split(" ")[0]);
//
//                                        barberModel.setEmail(staffMemberDataModel.getStaffEmail());
//
//                                        barberModel.setPass(staffMemberDataModel.getStaffPass());
//
//                                        barberModel.setCreatedAt("1");
//
//                                        barberModel.setStartTime("12");
//
//                                        barberModel.setEndTime("15");
//
//                                        barberModel.setUpdatedAt("16");
//
//                                        barberModel.setUserName("username" + staffMemberDataModel.getStaffName() + barberModel.getSalonId());
//
//                                        RetrofitManager.getInstance().addNewBarber(barberModel, new AbstractCallback() {
//
//                                            @Override
//                                            public void onResult(boolean isSuccess, Object result) {
//
//                                            }
//                                        });
//
//                                        barberModel = null;
//                                    }
//
//                                    UIUtils.hideLoadingView(rootView, FragmentManager.getCurrentVisibleFragment());
//
//                                    if (shouldPopFragment) {
//
//                                        FragmentManager.showSalonDetailsFragment(model);
//
//                                        FragmentManager.popBeforeCurrentVisibleFragment();
//
//                                    } else {
//
//                                        FragmentManager.popCurrentVisibleFragment();
//
//                                    }
//
//                                }
//                            });
//
//                        }
//                    });
//
//                } else {
//
//                    Toast.makeText(ThisApplication.getCurrentActivity().getApplicationContext(),
//                            "You have to Add 1 Barber at least",
//                            Toast.LENGTH_SHORT).show();
//
//                }
            }

        });

        return rootView;
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
