package com.loge.mealpricer;

import java.util.UUID;

/**
 * Created by loge on 2017-08-01.
 */

public class Ingredient {

    public static final int MEASURE_TYPE_NONE = 0;
    public static final int MEASURE_TYPE_ONLY_WEIGHT = 1;
    public static final int MEASURE_TYPE_ONLY_VOLUME = 2;
    public static final int MEASURE_TYPE_BOTH_WEIGHT = 3;
    public static final int MEASURE_TYPE_BOTH_VOLUME = 4;

    private UUID mMealId;
    private UUID mProductId;
    private int mMeasureType;
    private int mAmount;
    private boolean mSelected;


    public Ingredient(UUID mealId, UUID productId){
        mMealId = mealId;
        mProductId = productId;
        mMeasureType = MEASURE_TYPE_NONE;
        mAmount = 0;
        mSelected = false;


    }

    public UUID getMealId() { return mMealId; }

    public void setMealId(UUID mealId){ mMealId = mealId; }

    public UUID getProductId() {
        return mProductId;
    }

    public void setProduct(UUID productId) {
        mProductId = productId;
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

    public boolean getSelected() { return mSelected; }

    public void setSelected(boolean selected ) {mSelected = selected; }

}
