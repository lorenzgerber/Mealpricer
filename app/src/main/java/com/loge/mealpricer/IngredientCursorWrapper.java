package com.loge.mealpricer;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.loge.mealpricer.MealPricerDbSchema.IngredientTable;

import java.util.UUID;

/**
 * Created by loge on 2017-08-22.
 */

public class IngredientCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public IngredientCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Ingredient getIngredient() {
        String uuidMealString = getString(getColumnIndex(IngredientTable.Cols.MEAL_ID));
        String uuidProductString = getString(getColumnIndex(IngredientTable.Cols.PRODUCT_ID));
        int measureType = getInt(getColumnIndex(IngredientTable.Cols.MEASURE_TYPE));
        int amount = getInt(getColumnIndex(IngredientTable.Cols.AMOUNT));

        Ingredient ingredient = new Ingredient(UUID.fromString(uuidMealString), UUID.fromString(uuidProductString));
        ingredient.setMeasureType(measureType);
        ingredient.setAmount(amount);

        return ingredient;
    }

}
