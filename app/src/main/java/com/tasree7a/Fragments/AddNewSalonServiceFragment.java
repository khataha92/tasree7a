package com.tasree7a.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tasree7a.DummyConstants;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.AddNewServiceRequestModel;
import com.tasree7a.observables.ServicesChangedObservable;
import com.tasree7a.utils.PermissionsUtil;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

/**
 * Created by SamiKhleaf on 10/24/17.
 */

public class AddNewSalonServiceFragment extends BaseFragment {

    private static final int MY_PERMISSIONS_REQUEST= 987;

    ImageView selectedImage;

    CustomButton saveBtn;

    EditText serviceType;

    EditText price;

    EditText duration;

    private Bitmap selectedBitmap;
    private Uri selectedUri;
    private final int CAMERA_REQUEST = 1888;
    private final int GALLERY_REQUEST = 1889;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_add_new_service, container, false);

        rootView.findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

        selectedImage = rootView.findViewById(R.id.add_img);

        serviceType = rootView.findViewById(R.id.service_type);

        price = rootView.findViewById(R.id.price);

        duration = rootView.findViewById(R.id.duration_time);

        saveBtn = rootView.findViewById(R.id.apply);

        saveBtn.setOnClickListener(v -> {

//            if (PermissionsUtil.isPermessionGranted(this))
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                final AddNewServiceRequestModel model = new AddNewServiceRequestModel();
                model.setSalonId(UserDefaultUtil.getCurrentUser().getSalonId());
                model.setServiceName(serviceType.getText().toString());
                model.setServicePrice(price.getText().toString());

//            OutputStream outputStream = new FileOutputStream(output);
//            File file = selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                File file = new File(getRealPathFromUri(Objects.requireNonNull(getContext()), selectedUri));

                RetrofitManager.getInstance().addSalonService(model, file, (isSuccess, result) -> {

                    if (isSuccess) {
                        //TODO: Show Services fragment and don't go back to this

                        FragmentManager.popCurrentVisibleFragment();
                        ServicesChangedObservable.sharedInstance().setServicesChanged();
                    }
                });
            }
        });


        selectedImage.setOnClickListener(v -> {

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


        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    final AddNewServiceRequestModel model = new AddNewServiceRequestModel();
                    model.setSalonId(UserDefaultUtil.getCurrentUser().getSalonId());
                    model.setServiceName(serviceType.getText().toString());
                    model.setServicePrice(price.getText().toString());

//            OutputStream outputStream = new FileOutputStream(output);
//            File file = selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    File file = new File(getRealPathFromUri(Objects.requireNonNull(getContext()), selectedUri));

                    RetrofitManager.getInstance().addSalonService(model, file, (isSuccess, result) -> {

                        if (isSuccess) {
                            //TODO: Show Services fragment and don't go back to this

                            FragmentManager.popCurrentVisibleFragment();
                            ServicesChangedObservable.sharedInstance().setServicesChanged();
                        }
                    });
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    String base64Image = DummyConstants.mDummyImageBase64;



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Bitmap yourSelectedImage = null;

        if (!(requestCode == Activity.RESULT_CANCELED)) {

            if (data != null) {

                selectedUri = data.getData();

                Uri pickedImage = data.getData();
//                 Let's read picked image path using content resolver
                String[] filePath = {MediaStore.Images.Media.DATA};
                String imagePath = "";
                try {
                    if (getActivity() != null && pickedImage != null) {
                        Cursor cursor = getActivity().getContentResolver().query(pickedImage, filePath, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                            cursor.close();
                        }

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        selectedBitmap = BitmapFactory.decodeFile(imagePath, options);
                        selectedImage.setImageBitmap(selectedBitmap);
                    }
                } catch (Exception ignore) {
                }
//



//                    if (requestCode == CAMERA_REQUEST) {
//
//                        yourSelectedImage = (Bitmap) data.getExtras().get("data");
//
//                        selectedImage.setImageBitmap(yourSelectedImage);
//
//                        base64Image = encodeTobase64(yourSelectedImage);
//
//                    } else {
//
//                    final Uri imageUri = data.getData();
//
//                    InputStream imageStream = null;
//
//                    try {
//
//                        imageStream = ThisApplication.getCurrentActivity().getContentResolver().openInputStream(imageUri);
//
//                    } catch (FileNotFoundException e) {
//
//                        e.printStackTrace();
//
//                    }
//
//                     yourSelectedImage = BitmapFactory.decodeStream(imageStream);
//
//                    selectedImage.setImageBitmap(yourSelectedImage);
//
//                    base64Image = encodeTobase64(yourSelectedImage);
//                }
            }
        }
    }


    public String encodeTobase64(Bitmap image) {

        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
}
