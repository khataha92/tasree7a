package com.tasree7a.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.mcsoft.timerangepickerdialog.RangeTimePickerDialog;
import com.tasree7a.BuildConfig;
import com.tasree7a.R;
import com.tasree7a.customcomponent.CircularCheckBox;
import com.tasree7a.customcomponent.CustomButton;
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
import com.tasree7a.utils.ImagePickerHelper;
import com.tasree7a.utils.PermissionsUtil;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

/**
 * pre-fill data -> if from settings {all data}.
 * pre-fill some data if from registration {name, salonName, pass, email}
 */
public class SalonInformationActivity extends AppCompatActivity implements AddBarberClickListener, RangeTimePickerDialog.ISelectedTime {

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;


    public static final String TAG = SalonInformationActivity.class.getName() + "_TAG";
    public static final String SALON_NAME = SalonInformationActivity.class.getName() + "SALON_NAME";
    public static final String SALON_OWNER_NAME = SalonInformationActivity.class.getName() + "SALON_OWNER_NAME";
    public static final String SALON_EMAIL = SalonInformationActivity.class.getName() + "SALON_EMAIL";
    public static final String UPDATE = SalonInformationActivity.class.getName() + "UPDATE";

    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 435;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 347;
//    public static final int REQUEST_CODE = 4545;

    private final int CAMERA_REQUEST = 1888;
    private final int GALLERY_REQUEST = 1889;

    private boolean isUpdate;

    private String mFinalTime = null;
    private String mImagePath = null;

    private double mSalonLong;
    private double mSalonLat;

    private File mSelectedFile = null;

    private ImageView mSelectImage;
    private ImageView mBack;
    private EditText mSalonName;
    private EditText mSalonOwnerName;
    private EditText mSalonEmail;
    private EditText mSalonPhoneNumber;
    private LinearLayout mLoading;
    private CustomButton mSaveSalonInformationBtn;

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

    public static void launch(BaseFragment fragment, String salonName, String salonEmail, String salonOwnerName, boolean update) {
        Intent intent = new Intent(fragment.getContext(), SalonInformationActivity.class);
        intent.putExtra(SalonInformationActivity.SALON_NAME, salonName);
        intent.putExtra(SalonInformationActivity.SALON_EMAIL, salonEmail);
        intent.putExtra(SalonInformationActivity.SALON_OWNER_NAME, salonOwnerName);
        intent.putExtra(SalonInformationActivity.UPDATE, update);
        fragment.startActivity(intent);
    }

    public static void launch(BaseFragment fragment, boolean update) {
        fragment.startActivity(new Intent(fragment.getContext(), SalonInformationActivity.class)
                .putExtra(SalonInformationActivity.UPDATE, update));
    }

    public static void launch(FragmentActivity fragmentActivity, String salonName, String salonEmail, String salonOwnerName, boolean update) {
        Intent intent = new Intent(fragmentActivity, SalonInformationActivity.class);
        intent.putExtra(SalonInformationActivity.SALON_NAME, salonName);
        intent.putExtra(SalonInformationActivity.SALON_EMAIL, salonEmail);
        intent.putExtra(SalonInformationActivity.SALON_OWNER_NAME, salonOwnerName);
        intent.putExtra(SalonInformationActivity.UPDATE, update);
        fragmentActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_information);

        initUserLocation();

        initViews();

        Intent intent = getIntent();
        isUpdate = intent.getBooleanExtra(UPDATE, false);

        if (intent.hasExtra(SALON_NAME) && intent.hasExtra(SALON_OWNER_NAME) && intent.hasExtra(SALON_EMAIL)) {
            //pre-fill registration data
            mSalonName.setText(intent.getStringExtra(SALON_NAME));
            mSalonOwnerName.setText(intent.getStringExtra(SALON_OWNER_NAME));
            mSalonEmail.setText(intent.getStringExtra(SALON_EMAIL));
            mCancelSalonInformationBtn.setVisibility(View.GONE);
            mBack.setVisibility(View.GONE);
        } else {
            loadSalonInformation();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }

//        updateUI();
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");

                mSaveSalonInformationBtn.setButtonEnabled(false);
                requestPermissions();

            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
            }
        }

        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Remove location updates to save battery.
        stopLocationUpdates();
    }

    @SuppressWarnings("ConstantConditions")
    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, view -> {
                        // Request permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_PERMISSIONS_REQUEST_CODE);
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        //noinspection ConstantConditions
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        //noinspection ConstantConditions
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, task -> mRequestingLocationUpdates = false);
    }

    @SuppressWarnings("ConstantConditions")
    private void initUserLocation() {
        mRequestingLocationUpdates = false;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        }
    }


    @SuppressLint("MissingPermission")
    @SuppressWarnings("ConstantConditions")
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> {
                    Log.i(TAG, "All location settings are satisfied.");

                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());

//                        updateUI();
                })
                .addOnFailureListener(this, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                            mRequestingLocationUpdates = false;
                    }

//                    updateUI();
                });
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (mCurrentLocation == null) {
                    mCurrentLocation = locationResult.getLastLocation();
                    mSalonLat = mCurrentLocation.getLatitude();
                    mSalonLong = mCurrentLocation.getLongitude();
                    mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                    mSaveSalonInformationBtn.setButtonEnabled(true);
                }

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED) return;
        Uri selectedUri = null;
        if (data != null)
            selectedUri = data.getData();

        switch (requestCode) {
            case AddBarberActivity.REQUEST_CODE:
                //noinspection unchecked
                mSalonStaffContainer.addNewBarber((AddNewBarberRequestModel) data.getSerializableExtra(AddBarberActivity.BARBER_MODEL));
                break;

            case CAMERA_REQUEST:
                Bitmap selectedBitmap = ImagePickerHelper.handleCameraResult(this, mImagePath);
                if (selectedBitmap != null) {
                    mSelectedFile = new File(mImagePath);
                    mSelectImage.setImageBitmap(selectedBitmap);
                    mSelectImage.setRotation(90);
                } else {
                    Toasty.error(this, getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
                break;

            case GALLERY_REQUEST:
                try {
                    Bitmap mSelectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedUri);
                    mSelectImage.setImageBitmap(mSelectedBitmap);
                    mSelectedFile = new File(getRealPathFromUri(selectedUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "image", null);
        return Uri.parse(path);
    }

    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = Objects.requireNonNull(cursor).getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onAddNewBarberClicked() {
        startActivityForResult(new Intent(this, AddBarberActivity.class), AddBarberActivity.REQUEST_CODE);
    }

    @Override
    public void onSelectedTime(int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
        mFromTime.setText(to12HourFormat(String.format(Locale.ENGLISH, "%02d:%02d", hourStart, minuteStart)));
        mToTime.setText(to12HourFormat(String.format(Locale.ENGLISH, "%02d:%02d", hourEnd, minuteEnd)));
    }

    private String to12HourFormat(String time) {

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            return new SimpleDateFormat("K:mm aa", Locale.ENGLISH).format(sdf.parse(time));

        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadSalonInformation() {
        SalonModel salonModel = UserDefaultUtil.getCurrentSalonUser();
        if (salonModel != null) {
            mSalonName.setText(salonModel.getName());
            mSalonOwnerName.setText(salonModel.getOwnerName());
            mSalonPhoneNumber.setText(salonModel.getOwnerMobileNumber());
            mMaleSalonType.setChecked(salonModel.getSalonType() == Gender.MALE);
            mFemaleSalonType.setChecked(salonModel.getSalonType() == Gender.FEMALE);
            UIUtils.loadUrlIntoImageView(this, salonModel.getImage(), mSelectImage, null);

            if (salonModel.getSalonType() == Gender.ALL) {
                mMaleSalonType.setChecked(true);
                mFemaleSalonType.setChecked(true);
            }
//            mSalonStaffContainer.preFillBarbers(salonModel.getSalonBarbers());
        }
    }

    private void initViews() {
        mBack = findViewById(R.id.back);
        mFromTime = findViewById(R.id.from_hours);
        mToTime = findViewById(R.id.to_hours);
        mLoading = findViewById(R.id.loading);
        mSelectImage = findViewById(R.id.image);
        mSelectImage.setOnClickListener(v -> openImageSelectionDialog());
        mSalonName = findViewById(R.id.salon_name);
        mSalonOwnerName = findViewById(R.id.owner_name);
        mSalonEmail = findViewById(R.id.email);
        mSalonPhoneNumber = findViewById(R.id.mobile);
        mMaleSalonType = findViewById(R.id.male);
        mFemaleSalonType = findViewById(R.id.female);
        mSalonStaffContainer = findViewById(R.id.salon_staff_container);
        mSaveSalonInformationBtn = findViewById(R.id.save);
        mCancelSalonInformationBtn = findViewById(R.id.cancel);

        mBack.setOnClickListener(v -> finish());

//        findViewById(R.id.save).setOnClickListener(v -> finish());

        findViewById(R.id.from_to).setOnClickListener(v -> {
            RangeTimePickerDialog dialog = new RangeTimePickerDialog();
            dialog.newInstance(R.color.CyanWater, R.color.White, R.color.Yellow, R.color.colorPrimary, true);
            dialog.setIs24HourView(false);
            FragmentManager fragmentManager = getFragmentManager();
            dialog.show(fragmentManager, TAG);
        });

        mSalonStaffContainer.setOnAddNewBarberClicked(this);
        mSaveSalonInformationBtn
                .setOnClickListener(v -> {

                    if (mSalonStaffContainer.getBarbers() != null && mSalonStaffContainer.getBarbers().size() != 0) {
                        mLoading.setVisibility(View.VISIBLE);

                        if (isUpdate) {
                            RetrofitManager.getInstance().updateSalonDetails(BuildRequestData(true),
                                    mSelectedFile,
                                    (isSuccess, result) -> {
                                        mLoading.setVisibility(View.GONE);
                                        updateSalonBarbers(UserDefaultUtil.getCurrentSalonUser().getId());
                                        startActivity(new Intent(this, HomeActivity.class));
                                        finish();
                                    });
                        } else {

                            if (isDataValid()) {
                                RetrofitManager.getInstance().addNewSalon(BuildRequestData(false), mSelectedFile, (isSuccess, result) -> {
                                    mLoading.setVisibility(View.GONE);
                                    if (isSuccess) {
                                        User user = UserDefaultUtil.getCurrentUser();
                                        user.setSalonId(((AddNewSalonResponseModel) result).getDetails().getSalonId());
                                        UserDefaultUtil.saveUser(user);

                                        updateSalonBarbers(((AddNewSalonResponseModel) result).getDetails().getSalonId());

                                        startActivity(new Intent(this, HomeActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Toasty.error(this, getString(R.string.error_fill_fields), Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(this,
                                R.string.error_add_barbers,
                                Toast.LENGTH_SHORT).show();
                    }

                });

        initWorkingDays();
    }

    private boolean isDataValid() {
        return !TextUtils.isEmpty(mSalonOwnerName.getText().toString())
                && !TextUtils.isEmpty(mSalonPhoneNumber.getText().toString())
                && (mFemaleSalonType.isChecked() || mMaleSalonType.isChecked())
                && !TextUtils.isEmpty(mSalonEmail.getText().toString())
                && mSelectedFile != null;
    }

    private void updateSalonBarbers(String salonId) {
        for (AddNewBarberRequestModel salonBarber : mSalonStaffContainer.getBarbers()) {
            salonBarber.setSalonId(salonId);
            RetrofitManager.getInstance()
                    .addNewBarber(salonBarber,
                            (isSuccess, result) -> {
                                if (!isSuccess) {
                                    Toast.makeText(this, getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                                }
                            });
        }
    }

    private SalonInformationRequestModel BuildRequestData(boolean update) {
        String salonType = "";
        if (mMaleSalonType.isChecked() && mFemaleSalonType.isChecked()) {
            salonType = "3";
        } else if (mMaleSalonType.isChecked()) {
            salonType = "1";
        } else if (mFemaleSalonType.isChecked()) {
            salonType = "2";
        }

        SalonInformationRequestModel model = new SalonInformationRequestModel()
                .setCityID("15")
                .setOwnerMobile(mSalonPhoneNumber.getText().toString())
                .setOwnerName(mSalonOwnerName.getText().toString())
                .setSalonLat(mSalonLat + "")
                .setSalonLong(mSalonLong + "")
                .setSalonName(mSalonName.getText().toString())
                .setSalonType(salonType)
                .setStartAt(mFromTime.getText().toString())
                .setCloseAt(mToTime.getText().toString())
                .setUserEmail(mSalonEmail.getText().toString())
                .setUserID(UserDefaultUtil.getCurrentUser().getId());
        if (update) {
            model.setSalonId(UserDefaultUtil.getCurrentSalonUser().getId());
        } else {
            model.setSalonId(UserDefaultUtil.getCurrentUser().getId());
        }
        return model;
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
                    if (PermissionsUtil.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        openGallerySelectionIntent();
                    } else {
                        PermissionsUtil.grantPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_REQUEST_CODE);
                    }
                });

        alertDialog.show();
    }

    private void openCameraDialog() {
        if (PermissionsUtil.isPermissionGranted(this, Manifest.permission.CAMERA)) {
            mImagePath = ImagePickerHelper.dispatchTakePictureIntent(this, CAMERA_REQUEST);
        } else {
            PermissionsUtil.grantPermission(this, Manifest.permission.CAMERA, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

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
