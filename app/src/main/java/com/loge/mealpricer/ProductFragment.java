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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductFragment extends Fragment {

    public static final String ARG_PRODUCT_ID = "product_id";
    private static final int REQUEST_PHOTO = 2;


    private Product mProduct;
    private File mPhotoFile;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private EditText mNameField;
    private EditText mWeightField;
    private EditText mVolumeField;
    private EditText mPriceField;


    public static ProductFragment newInstance(UUID productId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT_ID, productId);

        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID productId = (UUID) getArguments().getSerializable(ARG_PRODUCT_ID);
        mProduct = MealPricer.get(getActivity()).getProduct(productId);
        mPhotoFile = MealPricer.get(getActivity()).getPhotoFile(mProduct);
    }

    @Override
    public void onPause() {
        super.onPause();

        MealPricer.get(getActivity()).updateProduct(mProduct);
    }


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




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);

        mNameField = (EditText) v.findViewById(R.id.product_name_entry);
        mNameField.setText(mProduct.getName());
        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int before, int count) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int cout){
                mProduct.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Intentionally left blank
            }

        });

        mWeightField = (EditText) v.findViewById(R.id.product_weight_entry);
        mWeightField.setText(String.valueOf(mProduct.getWeight()));
        mWeightField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int before, int count) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count){
                if (s.length() != 0){
                    mProduct.setWeight(Integer.parseInt(s.toString()));
                } else {
                    mProduct.setWeight(0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Intentionally left blank
            }

        });

        mVolumeField = (EditText) v.findViewById(R.id.product_volume_entry);
        mVolumeField.setText(String.valueOf(mProduct.getVolume()));
        mVolumeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int before, int count) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int cout){
                if (s.length() != 0){
                    mProduct.setVolume(Integer.parseInt(s.toString()));
                } else {
                    mProduct.setVolume(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Intentionally left blank
            }

        });

        mPriceField = (EditText) v.findViewById(R.id.product_price_entry);
        mPriceField.setText(String.valueOf(mProduct.getPrice()));
        mPriceField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int before, int count) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int cout){

                if(s.length() != 0){
                    mProduct.setPrice(Integer.parseInt(s.toString()));
                } else {
                    mProduct.setPrice(0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Intentionally left blank
            }

        });

        PackageManager packageManager = getActivity().getPackageManager();

        mPhotoButton = (ImageButton) v.findViewById(R.id.product_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.loge.mealpricer.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);


                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });


        mPhotoView = (ImageView) v.findViewById(R.id.product_image);
        updatePhotoView();

        return v;
    }



    private void updatePhotoView() {
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

}
