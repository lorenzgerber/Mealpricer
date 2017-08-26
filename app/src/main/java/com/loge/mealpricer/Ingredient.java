package com.loge.mealpricer;

import java.util.UUID;

import static com.loge.mealpricer.MeasureType.NONE;

public class Ingredient {

    private final UUID mMealId;
    private final UUID mProductId;
    private MeasureType mMeasureType;
    private int mAmount;
    private boolean mSelected;


    public Ingredient(UUID mealId, UUID productId){
        mMealId = mealId;
        mProductId = productId;
        mMeasureType = NONE;
        mAmount = 0;
        mSelected = false;

    }

    public UUID getMealId() { return mMealId; }

    public UUID getProductId() {
        return mProductId;
    }

    public MeasureType getMeasureType() {
        return mMeasureType;
    }

    public void setMeasureType(MeasureType measureType) {
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
