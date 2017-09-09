package com.loge.mealpricer;

import android.text.Editable;
import android.text.TextWatcher;


/**
 * Helper Class to Collect TextWatchers
 * <p/>
 * This class serves as a container for all
 * TextWatcher inner classes for the product fragment UI.
 */
class ProductFragmentTextWatchers {


    /**
     * TextListener for Product Name TextView Widget in Product Fragment
     * <p/>
     * This class contains the functionality that will write user
     * input to the underlying data structures
     */
    public static class NameTextWatcher implements TextWatcher {

        final Product mProduct;

        public NameTextWatcher(Product product){
            mProduct = product;
        }

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
    }

    /**
     * TextListener for Product Weight TextView Widget in Product Fragment
     * <p/>
     * This class contains the functionality that will write user
     * input to the underlying data structures
     */
    public static class WeightTextWatcher implements TextWatcher{

        final Product mProduct;

        public WeightTextWatcher(Product product){
            mProduct = product;
        }


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
    }

    /**
     * TextListener for Product Volume TextView Widget in Product Fragment
     * <p/>
     * This class contains the functionality that will write user
     * input to the underlying data structures
     */
    public static class VolumeTextWatcher implements TextWatcher{

        final Product mProduct;

        public VolumeTextWatcher(Product product){
            mProduct = product;
        }

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
    }

    /**
     * TextListener for Product Price TextView Widget in Product Fragment
     * <p/>
     * This class contains the functionality that will write user
     * input to the underlying data structures
     */
    public static class PriceTextWatcher implements TextWatcher{

        final Product mProduct;

        public PriceTextWatcher(Product product){
            mProduct = product;
        }

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
    }
}
