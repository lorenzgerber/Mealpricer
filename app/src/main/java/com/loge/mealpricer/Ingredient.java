/*
 * Ingredient
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

import static com.loge.mealpricer.MeasureType.NONE;

/**
 * Connector class between meal and product.
 * <p/>
 * This class is somewhat hybrid between being a pure model
 * class and containing also View information. The model part,
 * mMealId, mProductId and mAmount is stored also in the database.
 * mSelected is used only during run-time of the object and not
 * stored in the corresponding database table. nMeasure Type is
 * somewhere in between: It keeps both model information (whether
 * to use weight or volume based calculation) but also which
 * price information is available in the product. This information
 * should in fact be inferred from the productId.
 */
public class Ingredient {

    private final UUID mMealId;
    private final UUID mProductId;
    private MeasureType mMeasureType;
    private int mAmount;
    private boolean mSelected;


    /**
     * Constructor
     * <p/>
     * A ingredient instance is used as a connector between a meal and a product
     * hence, on instantiation, by default uuid for meal and product have to
     * be provided. There should only be one unique ingredient per product and meal.
     * @param mealId the id of the meal
     * @param productId the id of the product
     */
    public Ingredient(UUID mealId, UUID productId) {
        mMealId = mealId;
        mProductId = productId;
        mMeasureType = NONE;
        mAmount = 0;
        mSelected = false;

    }

    /**
     * getter for MealId
     * <p/>
     * @return the unique id of the meal that the ingredient relates to
     */
    public UUID getMealId() {
        return mMealId;
    }

    /**
     * getter for ProductId
     * <p/>
     * @return the unique id of the product that the ingredient relates to
     */
    public UUID getProductId() {
        return mProductId;
    }

    /**
     * getter for the measure type
     * <p/>
     * @return enum value of the ingredients' MeasureType
     */
    public MeasureType getMeasureType() {
        return mMeasureType;
    }

    /**
     * setter for the measure type
     * <p/>
     * @param measureType enum value measureType that determines whether to use weight or volume
     *                    based calculation. Further, measure type also specifies whether both
     *                    options are available or not.
     */
    public void setMeasureType(MeasureType measureType) {
        mMeasureType = measureType;
    }

    /**
     * getter amount of the ingredient
     * <p/>
     * @return integer amount of the ingredient, volume or weight, depends on
     * the MeasureType setting
     */
    public int getAmount() {
        return mAmount;
    }

    /**
     * setter for amount of ingredient
     * <p/>
     * @param amount integer value of quantity for the ingredient
     */
    public void setAmount(int amount) {
        mAmount = amount;
    }

    /**
     * getter, boolean value whether ingredient shall be stored in db
     * <p/>
     * @return boolean whether shall be stored in the db
     */
    public boolean getSelected() {
        return mSelected;
    }

    /**
     * setter, boolean value whether ingredient shall be stored in db
     * <p/>
     * @param selected boolean value
     */
    public void setSelected(boolean selected) {
        mSelected = selected;
    }


}
