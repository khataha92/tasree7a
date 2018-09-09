package com.tasree7a.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.annotation.Nullable;

import it.sephiroth.android.library.exif2.ExifInterface;
import it.sephiroth.android.library.exif2.ExifTag;

public class BitmapUtil {
    private static final String PHOTOS_EXTENSION = ".jpeg";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File putBitmapInDiskCache(Context context, Bitmap avatar) {
        // Create a path pointing to the system-recommended cache dir for the app, with sub-dir named
        // thumbnails
//        File cacheDir = new File(context.getCacheDir(), "captured_images");
        // Create a path in that dir for a file, named by the default hash of the url
        String fName = String.valueOf(System.currentTimeMillis())
                + PHOTOS_EXTENSION;

        try {
            File cacheFile = File.createTempFile(fName, null, context.getCacheDir());
            // Create a file at the file path, and open it for writing obtaining the output stream
            cacheFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(cacheFile);
            // Write the bitmap to the output stream (and thus the file) in PNG format (lossless compression)
            avatar.compress(Bitmap.CompressFormat.PNG, 100, fos);
            // Flush and close the output stream
            fos.flush();
            fos.close();
            return cacheFile;
        } catch (Exception e) {
            // Log anything that might go wrong with IO to file
            return null;
        }
    }

    @Nullable
    public static Bitmap decodeSampledBitmapFromUri(Activity activity, Uri imageFileUri, int reqWidth, int reqHeight) {
        InputStream is = openInputStreamForResource(imageFileUri, activity);
        if (is == null) {
            return null;
        }
        // First decode with options.inJustDecodeBounds=true to calculate the options.outWidth and options.outHeight
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        is = openInputStreamForResource(imageFileUri, activity);
        if (is == null) {
            return null;
        }
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap scaledBitmap = BitmapFactory.decodeStream(is, null, options);
        return fixBitmapExifProps(scaledBitmap, imageFileUri, activity);
    }


    @Nullable
    private static InputStream openInputStreamForResource(Uri resourceUri, Activity context) {
        try {
            return context.getContentResolver().openInputStream(resourceUri);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
//            FirebaseCrash.report(ex);
            return null;
        }
    }

    private static Bitmap fixBitmapExifProps(Bitmap scaledBitmap, Uri imageUri, Activity context) {
        InputStream is = openInputStreamForResource(imageUri, context);
        if (is == null) {
            return scaledBitmap;
        }
        ExifInterface exif = new ExifInterface();
        try {
            exif.readExif(is, ExifInterface.Options.OPTION_ALL);
        } catch (Exception e) {
            e.printStackTrace();
            return scaledBitmap;
        }
        ExifTag tag = exif.getTag(ExifInterface.TAG_ORIENTATION);
        int orientation = (tag == null ? -1 : tag.getValueAsInt(-1));
        if (orientation == 8 || orientation == 3 || orientation == 6) {
            return rotateViaMatrix(scaledBitmap, orientation);
        }
        return scaledBitmap;
    }

    private static Bitmap rotateViaMatrix(Bitmap original, int orientation) {
        Matrix matrix = new Matrix();
        matrix.setRotate(degreesForRotation(orientation));
        return (Bitmap.createBitmap(original, 0, 0, original.getWidth(),
                original.getHeight(), matrix, true));
    }

    private static int degreesForRotation(int orientation) {
        int result;
        switch (orientation) {
            case 8:
                result = 270;
                break;
            case 3:
                result = 180;
                break;
            default:
                result = 90;
        }
        return (result);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
