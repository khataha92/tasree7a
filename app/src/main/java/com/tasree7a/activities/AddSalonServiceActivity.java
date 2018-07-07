package com.tasree7a.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.AddNewServiceRequestModel;
import com.tasree7a.utils.PermissionsUtil;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AddSalonServiceActivity extends AppCompatActivity {

    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 987;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 3458;

    private final int CAMERA_REQUEST = 1888;
    private final int GALLERY_REQUEST = 1889;
    public static final int REQUEST_CODE = 3253;

    private Bitmap mSelectedBitmap;
    private File mSelectedFile;
    private RelativeLayout mAddNewImage;
    private ImageView mSelectedImageDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_service);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThisApplication.setCurrentActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedUri = data.getData();

        switch (requestCode) {
            case CAMERA_REQUEST:
                try {
                    Bitmap selectedBitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    mSelectedImageDisplay.setImageBitmap(selectedBitmap);
                    Uri tempUri = getImageUri(getApplicationContext(), selectedBitmap);
                    mSelectedFile = new File(getRealPathFromUri(tempUri));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case GALLERY_REQUEST:
                try {
                    mSelectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedUri);
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
        mSelectedImageDisplay = findViewById(R.id.add_img);

        findViewById(R.id.add_product_image).setOnClickListener(v1 -> openImageSelectionDialog());
        findViewById(R.id.apply).setOnClickListener(v -> requestAddService());

        findViewById(R.id.cancel).setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        findViewById(R.id.back).setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }

    private void requestAddService() {
        RetrofitManager.getInstance().addSalonService(getServiceModel(), mSelectedFile, (isSuccess, result) -> {
            if (isSuccess) {
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
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
            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, CAMERA_REQUEST);
        } else {
            PermissionsUtil.grantPermession(this, Manifest.permission.CAMERA, CAMERA_PERMISSION_REQUEST_CODE);
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

    public AddNewServiceRequestModel getServiceModel() {
        return new AddNewServiceRequestModel()
                .setServiceName(((TextView) findViewById(R.id.service_type)).getText().toString())
                .setServicePrice(((TextView) findViewById(R.id.price)).getText().toString())
                .setSalonId(UserDefaultUtil.getCurrentUser().getSalonId());
    }
}
