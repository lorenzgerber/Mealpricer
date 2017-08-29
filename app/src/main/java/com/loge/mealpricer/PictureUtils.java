/*
 * PictureUtils
 *
 * Mealpricer project, an Android app to calculate
 * the fractional cost of a meal from gross product prices.
 * Coursework 5DV155 Development of mobile applications
 * at Umea University, Summer Course 2017
 *
 * Lorenz Gerber
 *
 * Version 0.1
 *
 * Licensed under GPLv3
 */
package com.loge.mealpricer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;


/**
 * Helper Class for scaling images
 * <p/>
 * This code is mostly taken from the course book
 * (Android Programming: The Big Nerd Ranch, 3:e, 2017) and is currently
 * in a very raw form. In the future, this class has to be extended to provide
 * also image cropping of the meal image four-ways: depending both on the aspect-ratio
 * of the source image, and on the orientation of the screen.
 */
class PictureUtils {

    /**
     * method that uses display size to guess an initial scaling value
     * <p/>
     * This method can be used for convenience when no the exact scaling
     * parameters are not know yet.
     * @param path file handle to image
     * @param activity caller's activity
     * @return scaled bitmap
     */
    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }


    /**
     * Method that scales an image to given arguments
     * <p/>
     * The method takes an image file handle and scales the provided image
     * down according to the provided width and height arguments.
     * @param path file handle to image
     * @param destWidth destination width in pixel
     * @param destHeight destination height in pixel
     * @return scaled image
     */
    private static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if(srcHeight > destHeight || srcWidth > destWidth){
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / srcWidth;

            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(path, options);
    }
}
