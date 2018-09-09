package com.tasree7a.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tasree7a.R;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.UpdateSalonImagesRequestModel;
import com.tasree7a.utils.ImagePickerHelper;
import com.tasree7a.utils.PermissionsUtil;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddSalonImageActivity extends AppCompatActivity {

    public static final String SALON_ID = AddSalonImageActivity.class.getName() + "SALON_ID";
    public static final String SALON_IMAGE = AddSalonImageActivity.class.getName() + "SALON_IMAGE";

    public static final int REQUEST_CODE = 4567;
    private static final int GALLERY_REQUEST = 3254;
    private static final int CAMERA_REQUEST = 1888;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 3452;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 3458;

    private String mSalonId;
    private CustomButton mSaveImage;
    private CustomButton mCancel;
    private ImageView mBack;
    private ImageView mselectedImage;
    private LinearLayout mLoading;

    private Bitmap mSelectedBitmap;
    private File mSelectedFile;
    private boolean dataValid;
    private String mImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salon_image);

        mSalonId = getIntent().getStringExtra(SALON_ID);

        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        Uri selectedUri = null;
        if (data != null)
            selectedUri = data.getData();

        switch (requestCode) {
            case CAMERA_REQUEST:

                Bitmap selectedBitmap = ImagePickerHelper.handleCameraResult(this, mImagePath);
                if (selectedBitmap != null) {
                    mSelectedFile = new File(mImagePath);
                    mselectedImage.setImageBitmap(selectedBitmap);
                    mselectedImage.setRotation(90);
                } else {
                    Toasty.error(this, getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
                break;

            case GALLERY_REQUEST:
                try {
                    mSelectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedUri);
                    ((ImageView) findViewById(R.id.add_img)).setImageBitmap(mSelectedBitmap);
                    mSelectedFile = new File(getRealPathFromUri(selectedUri));
                } catch (IOException ignore) {
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_REQUEST_CODE:
                openGallerySelectionIntent();
                break;
            case CAMERA_PERMISSION_REQUEST_CODE:
                openCameraDialog();
                break;
        }
    }

    private void initViews() {
        mLoading = findViewById(R.id.loading);
        mselectedImage = findViewById(R.id.add_img);

        findViewById(R.id.apply).setOnClickListener(v -> {
            if (isDataValid()) {
                requestAddSalonGalleryImage();
            } else {
                Toast.makeText(this, "Missing data required", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        findViewById(R.id.back).setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });


        mselectedImage.setOnClickListener(v -> openImageSelectionPromptDialog());
    }

    private void requestAddSalonGalleryImage() {
        mLoading.setVisibility(View.VISIBLE);
        findViewById(R.id.content).setVisibility(View.GONE);

        RetrofitManager
                .getInstance()
                .updateSalonImages(UserDefaultUtil.getCurrentUser().getId(),
                        buildRequestDataMode(),
                        mSelectedFile,
                        (isSuccess, result) -> {
                            mLoading.setVisibility(View.GONE);
                            setResult(Activity.RESULT_OK);
                            finish();
                        });
    }

    private UpdateSalonImagesRequestModel buildRequestDataMode() {
        return new UpdateSalonImagesRequestModel()
                .setSalonId(mSalonId)
                .setOperation("ADD");
    }

    private void openImageSelectionPromptDialog() {
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

    private void openGallerySelectionIntent() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, GALLERY_REQUEST);
    }

    private void openCameraDialog() {
        if (PermissionsUtil.isPermissionGranted(this, Manifest.permission.CAMERA)) {
            mImagePath = ImagePickerHelper.dispatchTakePictureIntent(this, CAMERA_REQUEST);
        } else {
            PermissionsUtil.grantPermission(this, Manifest.permission.CAMERA, CAMERA_PERMISSION_REQUEST_CODE);
        }
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

    public boolean isDataValid() {
        return mSelectedFile != null;
    }
}
