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
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tasree7a.R;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.UpdateProductRequestModel;
import com.tasree7a.utils.ImagePickerHelper;
import com.tasree7a.utils.PermissionsUtil;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddSalonProductActivity extends AppCompatActivity {

    public static final String SALON_ID = AddSalonProductActivity.class.getName() + "SALON_ID";

    public static final int REQUEST_CODE = 6456;
    private static final int GALLERY_REQUEST = 6334;
    private static final int CAMERA_REQUEST = 1888;

    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 9347;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 3458;

    private String mSalonId;
    private String mImagePath;
    private File mSelectedFile;

    private ImageView mSelectedImageDisplay;
    private EditText mProductNameEditText;
    private EditText mProductPriceEditText;
    private LinearLayout mLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salon_product);

        if (getIntent() != null) {
            mSalonId = getIntent().getStringExtra(SALON_ID);
        }

        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        Uri selectedUri = null;
        if (data != null) {
            selectedUri = data.getData();
        }

        switch (requestCode) {
            case CAMERA_REQUEST:
                Bitmap selectedBitmap = ImagePickerHelper.handleCameraResult(this, mImagePath);
                if (selectedBitmap != null) {
                    mSelectedFile = new File(mImagePath);
                    mSelectedImageDisplay.setImageBitmap(selectedBitmap);
                    mSelectedImageDisplay.setRotation(90);
                } else {
                    Toasty.error(this, getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
                break;

            case GALLERY_REQUEST:
                try {
                    Bitmap mSelectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedUri);
                    mSelectedImageDisplay.setImageBitmap(mSelectedBitmap);
                    mSelectedFile = new File(getRealPathFromUri(selectedUri));
                } catch (IOException e) {
                    e.printStackTrace();
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
        mSelectedImageDisplay = findViewById(R.id.add_img);
        mProductNameEditText = findViewById(R.id.product_name);
        mProductPriceEditText = findViewById(R.id.product_price);


        findViewById(R.id.add_product_image).setOnClickListener(v -> openSelectImagePromptDialog());
        findViewById(R.id.apply).setOnClickListener(v -> requestAddSalonProduct());
        findViewById(R.id.back).setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
        findViewById(R.id.cancel).setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }

    private void openSelectImagePromptDialog() {
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

    private void requestAddSalonProduct() {
        if (!TextUtils.isEmpty(mProductPriceEditText.getText())
                && !TextUtils.isEmpty(mProductNameEditText.getText())
                && mSelectedFile != null) {

            mLoading.setVisibility(View.VISIBLE);
            RetrofitManager
                    .getInstance()
                    .updateSalonProducts(buildRequestDataModel(),
                            mSelectedFile,
                            (isSuccess, result) -> {
                                if (isSuccess) {
                                    mLoading.setVisibility(View.GONE);
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                }
                            });
        } else {
            Toast.makeText(this, R.string.error_fill_fields, Toast.LENGTH_LONG).show();
        }
    }

    private UpdateProductRequestModel buildRequestDataModel() {
        return new UpdateProductRequestModel()
                .setOperation("ADD")
                .setProductDescription("TODO!!!")
                .setProductName(mProductNameEditText.getText().toString())
                .setProductPrice(mProductPriceEditText.getText().toString())
                .setSalonId(mSalonId);
    }
}
