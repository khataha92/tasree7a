package com.tasree7a.fragments;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.UpdateProductRequestModel;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.observables.GallaryItemsChangedObservable;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by SamiKhleaf on 10/23/17.
 */

public class AddProductFragment extends BaseFragment {

    ImageView selectedImage;

    CustomButton saveBtn;

    AbstractCallback callback;

    private final int CAMERA_REQUEST = 1888;

    private final int GALLERY_REQUEST = 1889;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.add_product, container, false);


        selectedImage = rootView.findViewById(R.id.add_img);

        saveBtn = rootView.findViewById(R.id.apply);

        saveBtn.setOnClickListener(v -> {

            UpdateProductRequestModel model = new UpdateProductRequestModel();

            model.setSalonId(UserDefaultUtil.getCurrentUser().getSalonId());
            model.setOperation("ADD");
            model.setBase64Image(base64Image);
            model.setProductName(((EditText) rootView.findViewById(R.id.product_name)).getText().toString());
            model.setProductDescription("description");
            model.setProductPrice(((EditText) rootView.findViewById(R.id.price)).getText().toString());

//            UIUtils.showLoadingView(rootView, FragmentManager.getCurrentVisibleFragment());

            RetrofitManager.getInstance().updateSalonProducts(model, (isSuccess, result) -> {

                if (isSuccess) {

                    if (callback != null)
                        callback.onResult(true, result);

                    GallaryItemsChangedObservable.sharedInstance().setGallaryChanged(new ArrayList<>());

                    UIUtils.hideLoadingView(rootView, FragmentManager.getCurrentVisibleFragment());

                    FragmentManager.popCurrentVisibleFragment();

                }
            });

        });

        rootView.findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

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


    String base64Image;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Bitmap yourSelectedImage;

        if (!(requestCode == Activity.RESULT_CANCELED)) {
            if (data != null) {

                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

                    yourSelectedImage = (Bitmap) data.getExtras().get("data");

                    selectedImage.setImageBitmap(yourSelectedImage);

                    base64Image = encodeTobase64(yourSelectedImage);
                } else {

                    final Uri imageUri = data.getData();

                    InputStream imageStream = null;
                    try {
                        imageStream = ThisApplication.getCurrentActivity().getContentResolver().openInputStream(imageUri);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                    selectedImage.setImageBitmap(yourSelectedImage);

                    base64Image = encodeTobase64(yourSelectedImage);
                }
            }
        }
    }


    public String encodeTobase64(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }


    public void setCallback(AbstractCallback callback) {

        this.callback = callback;
    }
}
