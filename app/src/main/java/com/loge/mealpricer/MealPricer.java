package com.loge.mealpricer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loge on 2017-08-07.
 */

public class MealPricer {
    private static MealPricer sMealPricer;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static MealPricer get(Context context){
        if (sMealPricer == null){
            sMealPricer = new MealPricer(context);
        }
        return sMealPricer;
    }

    private MealPricer(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new MealPricerBaseHelper(mContext)
                .getWritableDatabase();

    }

    List<Meal> getMeals(){
        ArrayList<Meal> meals = new ArrayList<>();
        return meals;
    }

    List<Product> getProducts(){
        ArrayList<Product> products = new ArrayList<>();
        return products;
    }

}
