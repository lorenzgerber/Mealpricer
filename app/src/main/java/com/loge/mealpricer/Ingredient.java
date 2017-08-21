package com.loge.mealpricer;

/**
 * Created by loge on 2017-08-01.
 */

public class Ingredient {

    public static final int MEASURE_TYPE_WEIGHT = 0;
    public static final int MEASURE_TYPE_VOLUME = 1;

    private Product mProduct;
    private int mMeasureType;
    private int mAmount;

    public Ingredient(Product product){
        mProduct = product;
        mMeasureType = MEASURE_TYPE_WEIGHT;
        mAmount = 0;

    }

    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product product) {
        mProduct = product;
    }

    public int getMeasureType() {
        return mMeasureType;
    }

    public void setMeasureType(int measureType) {
        mMeasureType = measureType;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }
}
