package com.tasree7a.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.UpdateSalonImagesRequestModel;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.observables.GallaryItemsChangedObservable;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

/**
 * Created by SamiKhleaf on 10/23/17.
 */

public class AddGalleryFragment extends BaseFragment {

    private final int CAMERA_REQUEST = 1888;
    private final int GALLERY_REQUEST = 1889;

    private ImageView selectedImage;

    private CustomButton saveBtn;
    private SalonModel salonModel = null;

    public SalonModel getSalonModel() {
        return salonModel;
    }

    public void setSalonModel(SalonModel salonModel) {
        this.salonModel = salonModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_gallery_item, container, false);

        selectedImage = rootView.findViewById(R.id.add_img);
        saveBtn = rootView.findViewById(R.id.apply);
        rootView.findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

        initSaveButton();
        initSelectedImage();
        return rootView;
    }


    private void initSelectedImage() {

        selectedImage.setOnClickListener(v -> {
            //TODO: TEMP -> create option menu
            AlertDialog alertDialog = new AlertDialog.Builder(ThisApplication.getCurrentActivity()).create();

            alertDialog.setTitle("Choose");

            alertDialog.setMessage("choose your picture");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Camera",
                    (dialog, which) -> {

                        // Creates an Intent to pick a photo
                        Intent takePicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
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

    }


    private void initSaveButton() {

        saveBtn.setOnClickListener(v -> {

            UpdateSalonImagesRequestModel model = new UpdateSalonImagesRequestModel();

            model.setBase64Image(base64Image);

            model.setSalonId(UserDefaultUtil.getCurrentUser().getSalonId());

            model.setOperation("ADD");

            UIUtils.showSweetLoadingDialog();

            updateSalonImages(model);

        });
    }


    private void updateSalonImages(UpdateSalonImagesRequestModel model) {

        RetrofitManager.getInstance().updateSalonImages(model, (isSuccess, result) -> {

            if (isSuccess) {

                RetrofitManager.getInstance().getSalonDetails(salonModel.getId(), (isSuccess1, result1) -> salonModel = (SalonModel) result1);

                if (!showG) {

                    FragmentManager.popCurrentVisibleFragment();

                } else {

                    FragmentManager.showFragmentGallery(salonModel, (ArrayList<ImageModel>) salonModel.getGallery(), null);
                }

                UIUtils.hideSweetLoadingDialog();

                if (callback != null) callback.onResult(true, result);

                GallaryItemsChangedObservable.sharedInstance().setGallaryChanged(new ArrayList<ImageModel>() {

                });

            }
        });
    }


    String base64Image;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Uri uri = data.getData();
            InputStream imageStream = null;
            imageStream = getActivity().getContentResolver().openInputStream(uri);
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            yourSelectedImage.compress(Bitmap.CompressFormat.JPEG,50,stream);

            byte[] byteArray = stream.toByteArray();
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

//            InputStream imageStream = null;
//            Uri uri = data.getData();
            imageStream = getActivity().getContentResolver().openInputStream(uri);
//            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            base64Image = encodeTobase64(yourSelectedImage);
            selectedImage.setImageBitmap(compressedBitmap);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    public File getBitmapFile(Uri data) {

        Cursor cursor = ThisApplication.getCurrentActivity().getApplicationContext()
                .getContentResolver()
                .query(data,
                        new String[]{android.provider.MediaStore.Images.ImageColumns.DATA},
                        null,
                        null,
                        null);

        cursor.moveToFirst();

        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

        String selectedImagePath = cursor.getString(idx);

        cursor.close();

        return new File(selectedImagePath);
    }

//
//    //
//    public String encodeTobase64(Bitmap mImage) {
//
//        Bitmap immagex = mImage;
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//
//        byte[] b = baos.toByteArray();
//
//        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
//
//        return imageEncoded;
//    }


    AbstractCallback callback;


    public void setCallback(AbstractCallback callback) {

        this.callback = callback;
    }


    boolean showG = false;


    public void showGallaryFragment(boolean b) {

        showG = b;

    }
}
