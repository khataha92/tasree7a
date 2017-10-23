package com.tasree7a.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.UpdateProductRequestModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by SamiKhleaf on 10/23/17.
 */

public class AddProductFragment extends BaseFragment {

    ImageView selectedImage;

    CustomButton saveBtn;

    private final int CAMERA_REQUEST = 1888;

    private final int GALLERY_REQUEST = 1889;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.add_product, container, false);


        selectedImage = (ImageView) rootView.findViewById(R.id.add_img);

        saveBtn = (CustomButton) rootView.findViewById(R.id.apply);

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                UpdateProductRequestModel model = new UpdateProductRequestModel();

                model.setSalonId(UserDefaultUtil.getCurrentUser().getSalongId());
                model.setOperation("ADD");
                model.setBase64Image(base64Image);
                model.setProductName("Hi");
                model.setProductDescription("perfecto");
                model.setProductPrice("12.5");

                RetrofitManager.getInstance().updateSalonProducts(model, new AbstractCallback() {

                    @Override
                    public void onResult(boolean isSuccess, Object result) {

                        if (isSuccess) {
                            com.tasree7a.Managers.FragmentManager.popCurrentVisibleFragment();

                        }
                    }
                });

            }
        });


        selectedImage.setOnClickListener(new View.OnClickListener() {

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


        return rootView;
    }


    String base64Image;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        final Uri imageUri = data.getData();

        InputStream imageStream = null;
        try {
            imageStream = ThisApplication.getCurrentActivity().getContentResolver().openInputStream(imageUri);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

        selectedImage.setImageBitmap(yourSelectedImage);

        base64Image = encodeTobase64(yourSelectedImage);

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
