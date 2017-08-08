package com.loge.mealpricer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.loge.mealpricer.MealPricerDbSchema.IngredientTable;
import com.loge.mealpricer.MealPricerDbSchema.MealTable;
import com.loge.mealpricer.MealPricerDbSchema.ProductTable;

/**
 * Created by loge on 2017-08-07.
 */

public class MealPricerBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "mealPricerBase.db";

    public MealPricerBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        /*
        db.execSQL("create table " + ProductTable.NAME + "(" +
                ProductTable.Cols.PRODUCT_ID +
                " integer primary key  autoincrement, " +
                ProductTable.Cols.UUID + ", " +
                ProductTable.Cols.NAME + ", " +
                ProductTable.Cols.PRICE + ", " +
                ProductTable.Cols.WEIGHT + ", " +
                ProductTable.Cols.VOLUME +
                ")"
        );

        /*
        db.execSQL("create table " + MealTable.NAME + "(" +
                MealTable.Cols.MEAL_ID +
                " integer primary key autoincrement, " +
                MealTable.Cols.NAME +
                ")"
        );
        */


        db.execSQL("create table " + IngredientTable.NAME + "(" +
                IngredientTable.Cols.MEAL_ID + ", " +
                IngredientTable.Cols.PRODUCT_ID + ", " +
                IngredientTable.Cols.MEASURE_TYPE + ", " +
                IngredientTable.Cols.AMOUNT +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
