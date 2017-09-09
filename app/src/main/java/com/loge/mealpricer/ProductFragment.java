/*
 * ProductFragment
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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;


import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Fragment that host the product editing functionality
 * <p/>
 * This fragment is hosted in the ProductActivity from
 * which it obtains a bundle argument determining which
 * product that shall be opened for editing. The fragment
 * uses a simple fixed view with several EditText widgets
 * and additional functionality to take and show the photo
 * of the product.
 */
public class ProductFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "product_id";
    private static final int REQUEST_PHOTO = 2;


    private Product mProduct;
    private File mPhotoFile;
    private ImageView mPhotoView;


    /**
     * Method to load the fragment with the productId as bundle argument
     * <p/>
     * This method is called from the hosting activity to provide the
     * information which productId that shall be loaded for edit.
     * @param productId string uuid of the product to be edited
     * @return fragment loaded with bundle args
     */
    public static ProductFragment newInstance(UUID productId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT_ID, productId);

        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * onCreate override
     * <p/>
     * Method used to (re)load the data on create.
     * @param savedInstanceState bundle with extra
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID productId = (UUID) getArguments().getSerializable(ARG_PRODUCT_ID);
        mProduct = MealPricer.get(getActivity()).getProduct(productId);
        mPhotoFile = MealPricer.get(getActivity()).getPhotoFile(mProduct);
    }

    /**
     * onPause override
     * <p/>
     * Calls the activity for store volatile data to the database.
     * This method is called for example on orientation change.
     */
    @Override
    public void onPause() {
        super.onPause();
        MealPricer.get(getActivity()).updateProduct(mProduct);
    }

    /**
     * Taking care of returning photo capture intent
     * <p/>
     * This method catches the returning photo capture intent, tries
     * to fetch the photo and call the updatePhotoView Method.
     * @param requestCode specific request int
     * @param resultCode returning activity result code
     * @param data not used here
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.loge.mealpricer.fileprovider", mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }

    }

    /**
     * onCreateView override of ProductFragment
     * <p/>
     * Method that fetches and inflates the layout. Further it
     * attaches and configures widgets. This includes setting the
     * TextChange listeners for TextEdit fields and the onClick
     * listener for the photo ImageButton.
     * @param inflater inflater instance
     * @param container container where to attach the view
     * @param savedInstanceState bundle
     * @return view with product edit gui
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);

        EditText nameField = v.findViewById(R.id.product_name_entry);
        nameField.setText(mProduct.getName());
        nameField.addTextChangedListener(new ProductFragmentTextWatchers
                .NameTextWatcher(mProduct));

        EditText weightField = v.findViewById(R.id.product_weight_entry);
        weightField.setText(String.valueOf(mProduct.getWeight()));
        weightField.addTextChangedListener(new ProductFragmentTextWatchers
                .WeightTextWatcher(mProduct));

        EditText volumeField = v.findViewById(R.id.product_volume_entry);
        volumeField.setText(String.valueOf(mProduct.getVolume()));
        volumeField.addTextChangedListener(new ProductFragmentTextWatchers
                .VolumeTextWatcher(mProduct));

        EditText priceField = v.findViewById(R.id.product_price_entry);
        priceField.setText(String.valueOf(mProduct.getPrice()));
        priceField.addTextChangedListener(new ProductFragmentTextWatchers
                .PriceTextWatcher(mProduct));

        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;


        mPhotoView = v.findViewById(R.id.product_image);
        mPhotoView.setClickable(canTakePhoto);
        mPhotoView.setOnClickListener(new PhotoImageViewOnClickListener(captureImage));

        updatePhotoView();

        return v;
    }

    /**
     * Custom onClick Listener
     * <p/>
     * OnClick listener that will start an implicit intent provided as argument to
     * the listener.
     */
    private class PhotoImageViewOnClickListener implements View.OnClickListener{

        Intent mCaptureImage;

        /**
         * Conststructor
         * <p/>
         * Default constructor takes implicit intent to take a photo.
         * @param captureImage implicit intent to take photo
         */
        public PhotoImageViewOnClickListener(Intent captureImage){
            mCaptureImage = captureImage;
        }

        /**
         * OnClickListener Implementation
         * <p/>
         * Starts implicit intent to take a product photo when the user
         * presses the ImageView.
         * @param view view object to which to attach the onClickListener
         */
        @Override
        public void onClick(View view) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.loge.mealpricer.fileprovider", mPhotoFile);
            mCaptureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);


            List<ResolveInfo> cameraActivities = getActivity()
                    .getPackageManager().queryIntentActivities(mCaptureImage,
                            PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo activity : cameraActivities) {
                getActivity().grantUriPermission(activity.activityInfo.packageName,
                        uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            startActivityForResult(mCaptureImage, REQUEST_PHOTO);
        }
    }

    private void updatePhotoView() {
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(getResources().getDrawable(R.drawable.ic_take_photo));
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

}
