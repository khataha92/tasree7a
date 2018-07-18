package com.tasree7a.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.customcomponent.CircularCheckBox;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.customcomponent.CustomTimePicker;
import com.tasree7a.customcomponent.SalonStaffContainer;
import com.tasree7a.enums.Gender;
import com.tasree7a.fragments.BaseFragment;
import com.tasree7a.interfaces.AddBarberClickListener;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.AddNewBarberRequestModel;
import com.tasree7a.models.login.User;
import com.tasree7a.models.salondetails.AddNewSalonResponseModel;
import com.tasree7a.models.salondetails.SalonInformationRequestModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.utils.PermissionsUtil;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * pre-fill data -> if from settings {all data}.
 * pre-fill some data if from registration {name, salonName, pass, email}
 */
public class SalonInformationActivity extends AppCompatActivity implements AddBarberClickListener {

    public static final String SALON_NAME = SalonInformationActivity.class.getName() + "SALON_NAME";
    public static final String SALON_OWNER_NAME = SalonInformationActivity.class.getName() + "SALON_OWNER_NAME";
    public static final String SALON_EMAIL = SalonInformationActivity.class.getName() + "SALON_EMAIL";

    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 435;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 347;
//    public static final int REQUEST_CODE = 4545;

    private final int CAMERA_REQUEST = 1888;
    private final int GALLERY_REQUEST = 1889;

    private String mFinalTime = null;

    private ImageView mSelectImage;
    private ImageView mBack;
    private EditText mSalonName;
    private EditText mSalonOwnerNAme;
    private EditText mSalonEmail;
    private EditText mSalonPhoneNumber;

    private AppCompatCheckBox mMaleSalonType;
    private AppCompatCheckBox mFemaleSalonType;

    private TextView mFromTime;
    private TextView mToTime;

    private SalonStaffContainer mSalonStaffContainer;
    private CircularCheckBox[] workingDays = new CircularCheckBox[7];

    private CustomButton mCancelSalonInformationBtn;

    private int[] mWorkingDaysIDs = new int[]{
            R.id.sat,
            R.id.sun,
            R.id.mon,
            R.id.tue,
            R.id.wed,
            R.id.thu,
            R.id.fri
    };

    public static void launch(BaseFragment fragment, String salonName, String salonEmail, String salonOwnerName) {
        Intent intent = new Intent(fragment.getContext(), SalonInformationActivity.class);
        intent.putExtra(SalonInformationActivity.SALON_NAME, salonName);
        intent.putExtra(SalonInformationActivity.SALON_EMAIL, salonEmail);
        intent.putExtra(SalonInformationActivity.SALON_OWNER_NAME, salonOwnerName);
        fragment.startActivity(intent);
    }

    public static void launch(FragmentActivity fragmentActivity, String salonName, String salonEmail, String salonOwnerName) {
        Intent intent = new Intent(fragmentActivity, SalonInformationActivity.class);
        intent.putExtra(SalonInformationActivity.SALON_NAME, salonName);
        intent.putExtra(SalonInformationActivity.SALON_EMAIL, salonEmail);
        intent.putExtra(SalonInformationActivity.SALON_OWNER_NAME, salonOwnerName);
        fragmentActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_information);

        initViews();

        Intent intent = getIntent();
        if (intent != null
                && intent.hasExtra(SALON_NAME)
                && intent.hasExtra(SALON_OWNER_NAME)
                && intent.hasExtra(SALON_EMAIL)) {
            //pre-fill registration data
            mSalonName.setText(intent.getStringExtra(SALON_NAME));
            mSalonOwnerNAme.setText(intent.getStringExtra(SALON_OWNER_NAME));
            mSalonEmail.setText(intent.getStringExtra(SALON_EMAIL));
            mCancelSalonInformationBtn.setVisibility(View.GONE);
            mBack.setVisibility(View.GONE);
        } else {
            loadSalonInformation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED) return;
        Uri selectedUri = data.getData();

        switch (requestCode) {
            case AddBarberActivity.REQUEST_CODE:
                mSalonStaffContainer.addNewBarber((AddNewBarberRequestModel) data.getSerializableExtra(AddBarberActivity.BARBER_MODEL));
                break;

            case CAMERA_REQUEST:
                Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                mSelectImage.setImageBitmap(photo);
                break;

            case GALLERY_REQUEST:
                try {
                    Bitmap mSelectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedUri);
                    mSelectImage.setImageBitmap(mSelectedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onAddNewBarberClicked() {
        startActivityForResult(new Intent(this, AddBarberActivity.class), AddBarberActivity.REQUEST_CODE);
    }

    //TODO: E-Mail does not exist in SalonModel
    private void loadSalonInformation() {
        SalonModel salonModel = UserDefaultUtil.getCurrentSalonUser();
        if (salonModel != null) {
            mSalonName.setText(salonModel.getName());
            mSalonOwnerNAme.setText(salonModel.getOwnerName());
            mSalonPhoneNumber.setText(salonModel.getOwnerMobileNumber());
            mMaleSalonType.setChecked(salonModel.getSalonType() == Gender.MALE);
            mFemaleSalonType.setChecked(salonModel.getSalonType() == Gender.FEMALE);
            mSalonStaffContainer.preFillBarbers(salonModel.getSalonBarbers());
        }
    }

    private void initViews() {
        mBack = findViewById(R.id.back);
        mSelectImage = findViewById(R.id.image);
        mSelectImage.setOnClickListener(v -> openImageSelectionDialog());
        mSalonName = findViewById(R.id.salon_name);
        mSalonOwnerNAme = findViewById(R.id.owner_name);
        mSalonEmail = findViewById(R.id.email);
        mSalonPhoneNumber = findViewById(R.id.mobile);
        mMaleSalonType = findViewById(R.id.male);
        mFemaleSalonType = findViewById(R.id.female);
        mSalonStaffContainer = findViewById(R.id.salon_staff_container);
        CustomButton mSaveSalonInformationBtn = findViewById(R.id.save);
        mCancelSalonInformationBtn = findViewById(R.id.cancel);

        mSalonStaffContainer.setOnAddNewBarberClicked(this);
        mSaveSalonInformationBtn
                .setOnClickListener(v -> {

                    if (mSalonStaffContainer.getBarbers() != null && mSalonStaffContainer.getBarbers().size() != 0) {

                        RetrofitManager.getInstance().addNewSalon(BuildRequestData(),
                                (isSuccess, result) -> {

                                    if (isSuccess) {
                                        User user = UserDefaultUtil.getCurrentUser();
                                        user.setSalonId(((AddNewSalonResponseModel) result).getDetails().getSalonId());
                                        UserDefaultUtil.saveUser(user);

                                        startActivity(new Intent(this, HomeActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Toast.makeText(ThisApplication.getCurrentActivity().getApplicationContext(),
                                R.string.error_add_barbers,
                                Toast.LENGTH_SHORT).show();
                    }
                });

        initWorkingDays();
        initFromTime();
        initToTome();
    }

    private SalonInformationRequestModel BuildRequestData() {
        return new SalonInformationRequestModel()
                .setCityID("23")
                .setOwnerMobile(mSalonPhoneNumber.getText().toString())
                .setOwnerName(mSalonOwnerNAme.getText().toString())
//                .setSalonLat(String.format(Locale.ENGLISH, "%f", curLoc == null ? 1122 : curLoc.getLatitude()))
//                .setSalonLat(String.format(Locale.ENGLISH, "%f", curLoc == null ? 1122 : curLoc.getLongitude()))
                .setSalonLat("1122")
                .setSalonLong("1122")
                .setSalonName(mSalonName.getText().toString())
                .setSalonType(mMaleSalonType.isChecked() ? "1" : "2")
                .setUserID(UserDefaultUtil.getCurrentUser().getId());
    }

    private void initToTome() {
        mToTime = findViewById(R.id.to_hours);
        mToTime.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.date_dialog);
            dialog.findViewById(R.id.done).setOnClickListener(null);
            dialog.findViewById(R.id.done).setAlpha(0.5f);
            dialog.findViewById(R.id.done).setEnabled(false);

            ((CustomTimePicker) dialog.findViewById(R.id.timePicker)).setOnTimeChangedListener((view, hourOfDay, minute) -> {

                dialog.findViewById(R.id.done).setOnClickListener(mFinalTime == null
                        ? null
                        : (View.OnClickListener) v1 -> {
                    SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                    SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    try {
                        Date date = parseFormat.parse(mFinalTime);
                        mToTime.setText(displayFormat.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                });

                dialog.findViewById(R.id.done).setAlpha(mFinalTime == null
                        ? 0.5f
                        : 1f);

                dialog.findViewById(R.id.done).setEnabled(mFinalTime != null);
                mFinalTime = hourOfDay + ":" + minute;
            });
            dialog.show();
        });
    }

    private void initFromTime() {
        mFromTime = findViewById(R.id.from_hours);
        mFromTime.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.date_dialog);
            dialog.findViewById(R.id.done).setOnClickListener(null);
            dialog.findViewById(R.id.done).setAlpha(0.5f);
            dialog.findViewById(R.id.done).setEnabled(false);

            ((CustomTimePicker) dialog.findViewById(R.id.timePicker)).setOnTimeChangedListener((view, hourOfDay, minute) -> {
                dialog.findViewById(R.id.done).setOnClickListener(mFinalTime == null
                        ? null
                        : (View.OnClickListener) v12 -> {
                    SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                    SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    try {
                        Date date = parseFormat.parse(mFinalTime);
                        mFromTime.setText(displayFormat.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                });

                dialog.findViewById(R.id.done).setAlpha(mFinalTime == null
                        ? 0.5f
                        : 1f);

                dialog.findViewById(R.id.done).setEnabled(mFinalTime != null);

                mFinalTime = hourOfDay + ":" + minute;
            });
            dialog.show();
        });
    }

    private void initWorkingDays() {
        for (int i = 0; i < mWorkingDaysIDs.length; i++) {
            workingDays[i] = findViewById(mWorkingDaysIDs[i]);
            final int finalI = i;
            workingDays[i].setOnClickListener(v -> {
                if (workingDays[finalI].isChecked()) {
                    workingDays[finalI].uncheck();
                } else {
                    workingDays[finalI].check();
                }
            });
        }
    }

    private void openImageSelectionDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.choose_image));
        alertDialog.setMessage(getString(R.string.choose_yours));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.camera),
                (dialog, which) -> openCameraDialog());

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.gallery),
                (dialog, which) -> {
                    if (PermissionsUtil.isPermessionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        openGallerySelectionIntent();
                    } else {
                        PermissionsUtil.grantPermession(this, Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_REQUEST_CODE);
                    }
                });

        alertDialog.show();
    }

    private void openCameraDialog() {
        if (PermissionsUtil.isPermessionGranted(this, Manifest.permission.CAMERA)) {
            Log.d("", "");
//            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            mFileUri = CameraUtils.getOutputMediaFileUri(this);
//            if (takePicture.resolveActivity(getPackageManager()) != null) {

//                try {
//                    mSelectedFile = createImageFile();
//                } catch (IOException ignore) {
//                     Error occurred while creating the File
//                }

//                 Continue only if the File was successfully created
//                if (mSelectedFile != null) {
//                    Uri photoURI = FileProvider.getUriForFile(this,
//                            "com.example.android.fileprovider",
//                            mSelectedFile);
//
//                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                    startActivityForResult(takePicture, CAMERA_REQUEST);
//                }
        } else {
            PermissionsUtil.grantPermession(this, Manifest.permission.CAMERA, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }
//    }

    private void openGallerySelectionIntent() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, GALLERY_REQUEST);
    }
}
