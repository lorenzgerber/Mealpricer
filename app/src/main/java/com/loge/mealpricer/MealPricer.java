package com.loge.mealpricer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.loge.mealpricer.MealPricerDbSchema.*;

/**
 * Created by loge on 2017-08-07.
 */

public class MealPricer {
    public static MealPricer sMealPricer;

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
        mDatabase = new MealPricerBaseHelper(mContext).getWritableDatabase();

    }

    public Meal getMeal(UUID mealId){
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


    public Product getProduct(UUID productId){

        // for the moment make a dummy product
        Product mProduct = new Product();

        return mProduct;
    }

    public void updateProduct(Product product){
        String uuidString = product.getProductId().toString();
        ContentValues values = getContentValues(product);

        mDatabase.update(ProductTable.NAME, values, ProductTable.Cols.UUID + " = ?",
                new String[] { uuidString});
    }

    private static ContentValues getContentValues(Product product){
        ContentValues values = new ContentValues();
        values.put(ProductTable.Cols.UUID, product.getProductId().toString());
        values.put(ProductTable.Cols.NAME, product.getName());
        values.put(ProductTable.Cols.PRICE, product.getPrice());
        values.put(ProductTable.Cols.WEIGHT, product.getWeight());
        values.put(ProductTable.Cols.VOLUME, product.getVolume());

        return values;
    }

    public void addProudct(Product p) {
        ContentValues values = getContentValues(p);

        mDatabase.insert(ProductTable.NAME, null, values);
    }

    List<Product> getProducts(){
        ArrayList<Product> products = new ArrayList<>();
        for(int i = 0; i < 15; i++){
            products.add(new Product());
        }
        return products;
    }







}
