package com.loge.mealpricer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
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

        return null;
    }


    List<Meal> getMeals(){

        return new ArrayList<>();
    }

    public void addMeal(Meal m){

    }

    private static ContentValues getContentValues(Meal meal){
        ContentValues  values = new ContentValues();
        values.put(MealTable.Cols.MEAL_ID, meal.getMealId().toString());
        values.put(MealTable.Cols.NAME, meal.getName());

        return values;
    }


    public Product getProduct(UUID productId){

        ProductCursorWrapper cursor = queryProducts(
                ProductTable.Cols.PRODUCT_ID + " = ?",
                new String[] {
                        productId.toString()
                }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getProduct();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Product product){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, product.getPhotoFilename());
    }

    public void updateProduct(Product product){
        String uuidString = product.getProductId().toString();
        ContentValues values = getContentValues(product);

        mDatabase.update(ProductTable.NAME, values, ProductTable.Cols.PRODUCT_ID + " = ?",
                new String[] { uuidString});
    }

    private static ContentValues getContentValues(Product product){
        ContentValues values = new ContentValues();
        values.put(ProductTable.Cols.PRODUCT_ID, product.getProductId().toString());
        values.put(ProductTable.Cols.NAME, product.getName());
        values.put(ProductTable.Cols.PRICE, product.getPrice());
        values.put(ProductTable.Cols.WEIGHT, product.getWeight());
        values.put(ProductTable.Cols.VOLUME, product.getVolume());

        return values;
    }

    public Product newProduct(){
        Product mProduct = new Product();
        return mProduct;
    }

    public void addProudct(Product p) {
        ContentValues values = getContentValues(p);
        mDatabase.insert(ProductTable.NAME, null, values);
    }


    List<Product> getProducts(){
        List<Product> products = new ArrayList<>();

        ProductCursorWrapper cursor = queryProducts(null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                products.add(cursor.getProduct());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return products;
    }

    private ProductCursorWrapper queryProducts(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ProductTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ProductCursorWrapper(cursor);
    }







}
