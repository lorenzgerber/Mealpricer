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
import java.util.List;
import java.util.UUID;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "product_id";
    private static final int REQUEST_PHOTO = 2;


    private Product mProduct;
    private File mPhotoFile;
    private ImageView mPhotoView;


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

        EditText nameField = v.findViewById(R.id.product_name_entry);
        nameField.setText(mProduct.getName());
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int before, int count) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count){
                mProduct.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Intentionally left blank
            }

        });

        EditText weightField = v.findViewById(R.id.product_weight_entry);
        weightField.setText(String.valueOf(mProduct.getWeight()));
        weightField.addTextChangedListener(new TextWatcher() {
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

        EditText volumeField = v.findViewById(R.id.product_volume_entry);
        volumeField.setText(String.valueOf(mProduct.getVolume()));
        volumeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int before, int count) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count){
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

        EditText priceField = v.findViewById(R.id.product_price_entry);
        priceField.setText(String.valueOf(mProduct.getPrice()));
        priceField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int before, int count) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count){

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

        ImageButton photoButton = v.findViewById(R.id.product_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        photoButton.setEnabled(canTakePhoto);

        photoButton.setOnClickListener(new View.OnClickListener() {
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


        mPhotoView = v.findViewById(R.id.product_image);
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
