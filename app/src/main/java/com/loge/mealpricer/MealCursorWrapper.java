package com.loge.mealpricer;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.loge.mealpricer.MealPricerDbSchema.MealTable;

import java.util.UUID;

class MealCursorWrapper extends CursorWrapper{


    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public MealCursorWrapper(Cursor cursor) {
        super(cursor);
    }
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
