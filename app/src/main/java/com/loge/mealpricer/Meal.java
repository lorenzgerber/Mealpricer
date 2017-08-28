/*
 * Meal
 *
 * Mealpricer project, an Android app to calculate
 * the fractional cost of a meal from gross product prices.
 * Coursework 5DV155 Development of mobile applications
 * at Umea University, Summer Course 2017
 *
 * Lorenz Gerber
 *
 * Version 0.1
 *
 * Licensed under GPLv3
 */
package com.loge.mealpricer;

import java.util.UUID;

/**
 * Model class
 *
 * Represents the model of a meal. The portion
 * indicates for how many persons the ingredients that
 * link to this meal are specified. The model class
 * is slightly different from the DB table for meals.
 * The reason is that prices are not actually stored in
 * the DB but always calculated on the fly from ingredients.
 */
public class Meal {

    private final UUID mMealId;
    private String mName;
    private int mPrice;
    private int mPortion;

    /**
     * Default Constructor of the model class Meal
     * <p/>
     * On instantiation, a uuid is set for the new meal.
     */
    public Meal(){
        mMealId = UUID.randomUUID();
        mName = "";
        mPrice = 0;
        mPortion = 1;
    }

    /**
     * Constructor that takes a given uuid
     * <p/>
     * This constructor is used in the DAO to
     * construct run time meal instances from
     * db entries.
     * @param mealId uuid usually loaded from the db
     */
    public Meal(UUID mealId){
        mMealId = mealId;
    }

    /**
     * Getter for MealId property
     * <p/>
     * @return unique identifier of the meal
     */
    public UUID getMealId() {
        return mMealId;
    }

    /**
     * Getter for Name property
     * <p/>
     * @return name of the meal as string
     */
    public String getName() {
        return mName;
    }

    /**
     * Setter for Name property
     * @param name String that determines the name of the meal
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Getter for Price of the Meal
     * <p/>
     * The meal price is calculated based on the ingredients
     * and products. Each stored ingredient relates by a
     * mealId to exactly one meal. Price is for simplicity always
     * an integer.
     * @return returns the price of the Meal as integer
     */
    public int getPrice() {
        return mPrice;
    }

    /**
     * Setter for price property
     * <p/>
     * The price is implemented as integer value.
     * @param price value of the current meal.
     */
    public void setPrice(int price) {
        mPrice = price;
    }

    /**
     * Getter for portion property
     * <p/>
     * The portion property determines for how many portions
     * the ingredients shall be indicated to this meal.
     * @return integer value, currently 1, 2 and 4 is used,
     * defined trough the spinner list in the GUI
     */
    public int getPortion() {
        return mPortion;
    }

    /**
     * Setter for the portion property
     * <p/>
     * The portion property shows for how many
     * portions the ingredients shall be indicated.
     * @param portion integer value 1, 2, 4, currently only set
     *                through the GUI.
     */
    public void setPortion(int portion) {
        mPortion = portion;
    }

    /**
     * Getter of the photo filename
     * <p/>
     * This method constructs the filename of a eventual jpg
     * of the meal.
     * @return hypothetical filename string for the meal photo file
     */
    public String getPhotoFilename() {
        return "IMG_" + getMealId().toString() + ".jpg";
    }




}
