package com.tasree7a.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImagePickerHelper {
    public static String dispatchTakePictureIntent(@NonNull Activity activity, final int requestCode) {
        Uri photoURI = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(activity);
            } catch (IOException ex) {
                // Error occurred while creating the File
//            ...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(activity,
                        "com.tasree7a.utils.Tasree7aFileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, requestCode);
                return photoFile.getAbsolutePath();
            }
        }
        return null;
    }

    @NonNull
    private static File createImageFile(@NonNull Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public static Bitmap handleCameraResult(Activity activity, String imagePath) {
        File file = new File(imagePath);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media
                    .getBitmap(activity.getContentResolver(), Uri.fromFile(file));
            return bitmap;

        } catch (IOException e) {
            Log.e("Tasree7a_Exception", "Error, ", e);
        }
        return bitmap;
    }
}
