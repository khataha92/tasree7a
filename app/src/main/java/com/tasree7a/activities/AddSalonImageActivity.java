package com.tasree7a.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.tasree7a.R;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.UpdateSalonImagesRequestModel;
import com.tasree7a.utils.PermissionsUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AddSalonImageActivity extends AppCompatActivity {

    public static final String SALON_ID = AddSalonImageActivity.class.getName() + "SALON_ID";
    public static final String SALON_IMAGE = AddSalonImageActivity.class.getName() + "SALON_IMAGE";

    public static final int REQUEST_CODE = 4567;
    private static final int GALLERY_REQUEST = 3254;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 3452;

    private String mSalonId;
    private CustomButton mSaveImage;
    private CustomButton mCancel;
    private ImageView mBack;

    private Bitmap mSelectedBitmap;
    private File mSelectedFile;

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

        if (resultCode != Activity.RESULT_OK || data == null) return;

        Uri selectedUri = data.getData();

        switch (requestCode) {
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
        }
    }

    private void initViews() {
        findViewById(R.id.apply).setOnClickListener(v -> requestAddSalonGalleryImage());
        findViewById(R.id.cancel).setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
        findViewById(R.id.back).setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
        findViewById(R.id.add_image).setOnClickListener(v -> openImageSelectionPromptDialog());
    }

    private void requestAddSalonGalleryImage() {
        RetrofitManager
                .getInstance()
                .updateSalonImages(buildRequestDataMode(),
                        mSelectedFile,
                        (isSuccess, result) -> {
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
                    if (PermissionsUtil.isPermessionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
}
