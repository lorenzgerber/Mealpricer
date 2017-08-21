package com.loge.mealpricer;

/**
 * Created by loge on 2017-08-01.
 */

public class Ingredient {

    public static final int MEASURE_TYPE_NONE = 0;
    public static final int MEASURE_TYPE_ONLY_WEIGHT = 1;
    public static final int MEASURE_TYPE_ONLY_VOLUME = 2;
    public static final int MEASURE_TYPE_BOTH_WEIGHT = 3;
    public static final int MEASURE_TYPE_BOTH_VOLUME = 4;

    private Product mProduct;
    private int mMeasureType;
    private int mAmount;

    public Ingredient(Product product){
        mProduct = product;
        mMeasureType = MEASURE_TYPE_NONE;
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
