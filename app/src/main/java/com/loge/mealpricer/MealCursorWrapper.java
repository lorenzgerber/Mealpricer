/*
 * MealCursorWrapper
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

import com.loge.mealpricer.MealPricerDbSchema.MealTable;

import java.util.UUID;

/**
 * DAO Abstraction for DB access of the Meal Table
 *
 * This class provides DAO abstraction for the Meal Table.
 * It parses the data retrieved from the DB into an Meal
 * object.
 */
class MealCursorWrapper extends CursorWrapper{


    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public MealCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * Meal DAO getter
     * @return Meal object
     */
    public Meal getMeal(){
        String uuidString = getString(getColumnIndex(MealTable.Cols.MEAL_ID));
        String name = getString(getColumnIndex(MealTable.Cols.NAME));
        int portion = getInt(getColumnIndex(MealTable.Cols.PORTION));

        Meal meal = new Meal(UUID.fromString(uuidString));
        meal.setName(name);
        meal.setPortion(portion);

        return meal;
    }

}
