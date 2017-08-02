package com.loge.mealpricer;

/**
 * Created by loge on 2017-08-01.
 */

public class Ingredient {

    private Product mProduct;
    private int mMeasureType;
    private int mAmount;

    public Ingredient(){

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
