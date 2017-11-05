package com.tasree7a.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
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
import android.widget.ImageView;

import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.Gallery.ImageModel;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.Models.UpdateSalonImagesRequestModel;
import com.tasree7a.Observables.GallaryItemsChangedObservable;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

/**
 * Created by SamiKhleaf on 10/23/17.
 */

public class AddGalleryFragment extends BaseFragment {

    ImageView selectedImage;

    CustomButton saveBtn;

    private final int CAMERA_REQUEST = 1888;

    private final int GALLERY_REQUEST = 1889;

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

        selectedImage = (ImageView) rootView.findViewById(R.id.add_img);

        saveBtn = (CustomButton) rootView.findViewById(R.id.apply);

        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                com.tasree7a.Managers.FragmentManager.popCurrentVisibleFragment();
            }
        });

        initSaveButton();

        initSelectedImage();

        return rootView;
    }


    private void initSelectedImage() {

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

    }


    private void initSaveButton() {

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                UpdateSalonImagesRequestModel model = new UpdateSalonImagesRequestModel();

                model.setBase64Image(base64Image);

                model.setSalonId(UserDefaultUtil.getCurrentUser().getSalongId());

                model.setOperation("ADD");

                UIUtils.showSweetLoadingDialog();

                updateSalonImages(model);

            }
        });
    }


    private void updateSalonImages(UpdateSalonImagesRequestModel model) {

        RetrofitManager.getInstance().updateSalonImages(model, new AbstractCallback() {

            @Override
            public void onResult(boolean isSuccess, Object result) {

                if (isSuccess) {

                    RetrofitManager.getInstance().getSalonDetails(salonModel.getId(), new AbstractCallback() {

                        @Override
                        public void onResult(boolean isSuccess, Object result) {

                            salonModel = (SalonModel) result;
                        }
                    });

                    if (!showG) {

                        com.tasree7a.Managers.FragmentManager.popCurrentVisibleFragment();

                    } else {

                        com.tasree7a.Managers.FragmentManager.showFragmentGallery(salonModel, (ArrayList<ImageModel>) salonModel.getGallery(), null);
                    }

                    UIUtils.hideSweetLoadingDialog();

                    if (callback != null) callback.onResult(true, result);

                    GallaryItemsChangedObservable.sharedInstance().setGallaryChanged(new ArrayList<ImageModel>() {

                    });

                }
            }
        });
    }


    String base64Image;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Bitmap yourSelectedImage = null;

        if (!(requestCode == Activity.RESULT_CANCELED)) {

            if (data != null) {

                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

                    yourSelectedImage = (Bitmap) data.getExtras().get("data");

                    selectedImage.setImageBitmap(yourSelectedImage);

                    base64Image = encodeTobase64(yourSelectedImage);

                } else {

                    File file = getBitmapFile(data);

                    try {

                        Bitmap compressedImageFile = new Compressor(ThisApplication.getCurrentActivity().getApplicationContext())
                                .setQuality(75)
                                .setMaxWidth(640)
                                .setMaxHeight(480)
                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                .compressToBitmap(file);


                        selectedImage.setImageBitmap(compressedImageFile);

                        base64Image = encodeTobase64(compressedImageFile);

                    } catch (IOException e) {
                        Log.e("EXCEPTION", "Error: ", e);
                    }


                }
            }
        }
    }

    public File getBitmapFile(Intent data) {

        Uri selectedImage = data.getData();

        Cursor cursor = ThisApplication.getCurrentActivity().getApplicationContext()
                .getContentResolver()
                .query(selectedImage,
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
    public String encodeTobase64(Bitmap image) {

        Bitmap immagex = image;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();

        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }


    AbstractCallback callback;


    public void setCallback(AbstractCallback callback) {

        this.callback = callback;
    }


    boolean showG = false;


    public void showGallaryFragment(boolean b) {

        showG = b;

    }
}
