package com.loge.mealpricer;

import java.util.UUID;


public class Meal {

    private final UUID mMealId;
    private String mName;
    private int mPrice;
    private int mPortion;

    public Meal(){
        mMealId = UUID.randomUUID();
        mName = "";
        mPrice = 0;
        mPortion = 1;
    }

    public Meal(UUID mealId){
        mMealId = mealId;
    }

    public UUID getMealId() {
        return mMealId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public int getPortion() {
        return mPortion;
    }

    public void setPortion(int portion) {
        mPortion = portion;
    }

    public String getPhotoFilename() {
        return "IMG_" + getMealId().toString() + ".jpg";
    }




}
