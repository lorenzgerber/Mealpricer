/*
 * IngredientCursorWrapper
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

import android.database.Cursor;
import android.database.CursorWrapper;

import com.loge.mealpricer.MealPricerDbSchema.IngredientTable;

import java.util.UUID;

/**
 * DAO Abstraction for DB access of the Ingredient Table
 *
 * This class provides DAO abstraction for the Ingredient Table.
 * It parses the data retrieved from the DB into an Ingredient
 * object.
 */
class IngredientCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public IngredientCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * Ingredient DAO getter
     * @return Ingredient instance
     */
    public Ingredient getIngredient() {
        String uuidMealString = getString(getColumnIndex(IngredientTable.Cols.MEAL_ID));
        String uuidProductString = getString(getColumnIndex(IngredientTable.Cols.PRODUCT_ID));
        MeasureType measureType = MeasureType.values()[getInt(getColumnIndex(IngredientTable.Cols.MEASURE_TYPE))];
        int amount = getInt(getColumnIndex(IngredientTable.Cols.AMOUNT));

        Ingredient ingredient = new Ingredient(UUID.fromString(uuidMealString), UUID.fromString(uuidProductString));
        ingredient.setMeasureType(measureType);
        ingredient.setAmount(amount);

        return ingredient;
    }

}
