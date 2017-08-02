package com.loge.mealpricer;

import java.util.List;
import java.util.UUID;

/**
 * Created by loge on 2017-08-01.
 */

public class Meal {

    private UUID mMealId;
    private String mName;
    private int mPortion;

    private List<Ingredient> Ingredients;


    public Meal(){
        mMealId = UUID.randomUUID();

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

    public int getPortion() {
        return mPortion;
    }

    public void setPortion(int portion) {
        mPortion = portion;
    }

    public List<Ingredient> getIngredients() {
        return Ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        Ingredients = ingredients;
    }




}
