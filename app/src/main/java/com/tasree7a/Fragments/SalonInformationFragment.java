package com.tasree7a.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

    private EditText address; //id: address

    private EditText mobile; //id: mobile

    private CustomButton saveBtn;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.view_salon_info, container, false);

        fromTime = (TextView) rootView.findViewById(R.id.from_hours);

        staffContainer = (SalonStaffContainer) rootView.findViewById(R.id.salon_staff_container);

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

                salonInformationRequestModel.setCityID("23");
                salonInformationRequestModel.setOwnerMobile(mobile.getText().toString());
                salonInformationRequestModel.setOwnerName(ownerNamer.getText().toString());
                salonInformationRequestModel.setSalonBase64Image(base64Image);
                salonInformationRequestModel.setSalonLat(AppUtil.getCurrentLocation().getLatitude() + "");
                salonInformationRequestModel.setSalonLong(AppUtil.getCurrentLocation().getLongitude() + "");
                salonInformationRequestModel.setUserID(UserDefaultUtil.getCurrentUser().getId());
                salonInformationRequestModel.setSalonName(salonName.getText().toString());
                salonInformationRequestModel.setSalonType("1");

                UIUtils.showLoadingView(rootView, FragmentManager.getCurrentVisibleFragment());

                RetrofitManager.getInstance().addNewSalon(salonInformationRequestModel, new AbstractCallback() {

                    @Override
                    public void onResult(boolean isSuccess, Object result) {

                        final AddNewSalonResponseModel responseModel = (AddNewSalonResponseModel) ((Response) result).body();

                        RetrofitManager.getInstance().getSalonDetails(responseModel.getDetails().getSalonId(), new AbstractCallback() {

                            @Override
                            public void onResult(boolean isSuccess, Object result) {

                                SalonModel model = (SalonModel) result;

                                model.setBusiness(true);

                                AddNewBarberRequestModel barberModel;

                                User user = UserDefaultUtil.getCurrentUser();

                                user.setSalongId(model.getId());

                                UserDefaultUtil.saveUser(user);

                                for (AddNewStaffMemberDataModel staffMemberDataModel : staffContainer.getBarbers()) {

                                    barberModel = new AddNewBarberRequestModel();

                                    barberModel.setSalonId(responseModel.getDetails().getSalonId());

                                    barberModel.setLastName(staffMemberDataModel.getStaffName().split(" ")[1]);

                                    barberModel.setFirstName(staffMemberDataModel.getStaffName().split(" ")[0]);

                                    barberModel.setEmail(staffMemberDataModel.getStaffEmail());

                                    barberModel.setPass(staffMemberDataModel.getStaffPass());

                                    barberModel.setCreatedAt("1");

                                    barberModel.setStartTime("12");

                                    barberModel.setEndTime("15");

                                    barberModel.setUpdatedAt("16");

                                    barberModel.setUserName("username" + staffMemberDataModel.getStaffName() + barberModel.getSalonId());

                                    RetrofitManager.getInstance().addNewBarber(barberModel, new AbstractCallback() {

                                        @Override
                                        public void onResult(boolean isSuccess, Object result) {

                                        }
                                    });

                                    barberModel = null;
                                }

                                UIUtils.hideLoadingView(rootView, FragmentManager.getCurrentVisibleFragment());

                                FragmentManager.showSalonDetailsFragment(model);

                                FragmentManager.popBeforeCurrentVisibleFragment();


                            }
                        });

                    }
                });

            }

        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

//        super.onActivityResult(requestCode, resultCode, data);

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
