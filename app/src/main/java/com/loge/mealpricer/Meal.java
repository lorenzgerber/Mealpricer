package com.loge.mealpricer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by loge on 2017-08-01.
 */

public class Meal {

    private UUID mMealId;
    private String mName;
    private double mPrice;
    private int mPortion;

    private List<Ingredient> mIngredients;


    public Meal(){
        mIngredients = new ArrayList<>();
        mMealId = UUID.randomUUID();
        mName = "Röschti";
        mPrice = 50;
        mPortion = 1;
        Product mProduct1 = new Product();
        Product mProduct2 = new Product();
        Ingredient mIngredient1 = new Ingredient(mProduct1);
        Ingredient mIngredient2 = new Ingredient(mProduct2);
        mIngredient1.setAmount(300);
        mIngredient2.setAmount(100);
        mIngredient1.setMeasureType(0);
        mIngredient2.setMeasureType(0);

        mIngredients.add(mIngredient1);
        mIngredients.add(mIngredient2);

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

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(Double price) {
        mPrice = price;
    }

    public int getPortion() {
        return mPortion;
    }

    public void setPortion(int portion) {
        mPortion = portion;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void addIngredient(){


    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }




}
