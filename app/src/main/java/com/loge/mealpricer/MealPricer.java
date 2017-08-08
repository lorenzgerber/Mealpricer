package com.loge.mealpricer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    Meal getMeal(UUID mealId){
        Meal mMeal = new Meal();
        return mMeal;
    }

    List<Meal> getMeals(){
        ArrayList<Meal> meals = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            meals.add(new Meal());
        }
        return meals;
    }


    Product getProduct(UUID productId){

        // for the moment make a dummy product
        Product mProduct = new Product();


        return mProduct;
    }

    List<Product> getProducts(){
        ArrayList<Product> products = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            products.add(new Product());
        }
        return products;
    }







}
