/*
 * MealPricerBaseHelper
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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.loge.mealpricer.MealPricerDbSchema.IngredientTable;
import com.loge.mealpricer.MealPricerDbSchema.MealTable;
import com.loge.mealpricer.MealPricerDbSchema.ProductTable;

/**
 * Database Helper, Table creation
 * <p/>
 * The main task of this class is to startup the
 * SQLite database engine, and if needed, create
 * new tables. It extends the SQLiteOpenHelper with
 * application specifc values such as db name, verion
 * and the table structure.
 */
class MealPricerBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "mealPricerBase.db";

    /**
     * Constructor
     * <p/>
     * @param context callers context
     */
    public MealPricerBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }


    /**
     * onCreate override
     * <p/>
     * If the table don't exist yet, they are
     * created in this method.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + ProductTable.NAME + "(" +
                ProductTable.Cols.PRODUCT_ID +
                " string primary key, " +
                ProductTable.Cols.NAME + ", " +
                ProductTable.Cols.PRICE + ", " +
                ProductTable.Cols.WEIGHT + ", " +
                ProductTable.Cols.VOLUME +
                ")"
        );

        db.execSQL("create table " + MealTable.NAME + "(" +
                MealTable.Cols.MEAL_ID +
                " string primary key, " +
                MealTable.Cols.NAME + ", " +
                MealTable.Cols.PORTION +
                ")"
        );

        db.execSQL("create table " + IngredientTable.NAME + "(" +
                IngredientTable.Cols.MEAL_ID + ", " +
                IngredientTable.Cols.PRODUCT_ID + ", " +
                IngredientTable.Cols.MEASURE_TYPE + ", " +
                IngredientTable.Cols.AMOUNT +
                ")"
        );

    }

    /**
     * onUpgrade override
     * <p/>
     * Currently no override for upgrade is defined.
     * @param db not used
     * @param oldVersion not used
     * @param newVersion not used
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
