package com.loge.mealpricer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductFragment extends Fragment {

    public static final String ARG_PRODUCT_ID = "product_id";


    private Product mProduct;

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
    }

    @Override
    public void onPause() {
        super.onPause();

        MealPricer.get(getActivity()).updateProduct(mProduct);
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
                    CharSequence s, int start, int before, int cout){
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


        return v;
    }




}
