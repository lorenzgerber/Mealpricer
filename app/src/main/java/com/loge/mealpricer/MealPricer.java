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

    public Product newProduct(){
        Product mProduct = new Product();
        return mProduct;
    }

    public Meal newMeal(){
        Meal mMeal = new Meal();
        return mMeal;
    }



    public void addProudct(Product p) {
        ContentValues values = getContentValues(p);
        mDatabase.insert(ProductTable.NAME, null, values);
    }


    public void addMeal(Meal m){
        ContentValues values = getContentValues(m);
        mDatabase.insert(MealTable.NAME, null, values);
    }

    public void addIngredient(Ingredient i){
        ContentValues values = getContentValues(i);
        mDatabase.insert(IngredientTable.NAME, null, values);
    }


    public void deleteMeal(Meal m){
        String uuidMealIdString = m.getMealId().toString();
        deleteIngredients(getIngredients(m.getMealId()));

        mDatabase.delete(MealTable.NAME,
                MealTable.Cols.MEAL_ID + " = ?",
                new String[]{uuidMealIdString});

    }



    public void deleteIngredient(Ingredient i){

        String uuidMealIdString = i.getMealId().toString();
        String uuidProductIdString = i.getProductId().toString();

        mDatabase.delete(IngredientTable.NAME,
                IngredientTable.Cols.MEAL_ID + " = ? AND " +
                IngredientTable.Cols.PRODUCT_ID + " = ?",
                new String[]{uuidMealIdString, uuidProductIdString});
    }

    public void deleteIngredients(List<Ingredient> is){
        for (Ingredient ingredient:is){
            deleteIngredient(ingredient);
        }
    }




    public void updateProduct(Product product){
        String uuidString = product.getProductId().toString();
        ContentValues values = getContentValues(product);

        mDatabase.update(ProductTable.NAME, values, ProductTable.Cols.PRODUCT_ID + " = ?",
                new String[] { uuidString});
    }


    public void updateMeal(Meal meal){
        String uuidString = meal.getMealId().toString();
        ContentValues values = getContentValues(meal);

        mDatabase.update(MealTable.NAME, values, MealTable.Cols.MEAL_ID + " = ?",
                new String[] { uuidString });
    }

    public void updateIngredient(Ingredient ingredient){
        String uuidMealString = ingredient.getMealId().toString();
        String uuidProductString = ingredient.getProductId().toString();
        ContentValues values = getContentValues(ingredient);

        mDatabase.update(IngredientTable.NAME, values,
                IngredientTable.Cols.MEAL_ID +
                        " = ? AND " + IngredientTable.Cols.PRODUCT_ID + " = ?",
                new String[] { uuidMealString, uuidProductString});
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


    List<Meal> getMeals(){
        List<Meal> meals = new ArrayList<>();
        MealCursorWrapper cursor = queryMeals(null ,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                meals.add(cursor.getMeal());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return meals;
    }

    List<Ingredient> getIngredients(UUID mealId){
        List<Ingredient> ingredients = new ArrayList<>();
        IngredientCursorWrapper cursor = queryIngredients(IngredientTable.Cols.MEAL_ID + " = ?",
                new String[] {mealId.toString()});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                ingredients.add(cursor.getIngredient());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return ingredients;
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




    public Meal getMeal(UUID mealId){
        MealCursorWrapper cursor = queryMeals(
                MealTable.Cols.MEAL_ID + " = ?",
                new String[] { mealId.toString() }
        );

        try{
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getMeal();
        } finally {
            cursor.close();
        }
    }

    public Ingredient getIngredient(UUID mealId, UUID productId){
        IngredientCursorWrapper cursor = queryIngredients(
                IngredientTable.Cols.MEAL_ID + " = ? AND " +
                        IngredientTable.Cols.PRODUCT_ID + " = ?",
                new String[] {mealId.toString(), productId.toString() }
        );

        try{
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getIngredient();
        } finally {
            cursor.close();
        }

    }


    public File getPhotoFile(Product product){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, product.getPhotoFilename());
    }

    public File getPhotoFile(Meal meal){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, meal.getPhotoFilename());
    }


    public int calcPriceIngredient(UUID mealId, UUID productId){
        Ingredient mIngredient = getIngredient(mealId, productId);
        Product mProduct;
        if (mIngredient!=null){
            mProduct = getProduct(productId);

            if(mIngredient.getMeasureType() == 1 || mIngredient.getMeasureType() == 3){
                if(mProduct.getPrice() == 0 || (mProduct.getWeight() == 0)){
                    return 0;
                }
                float mResult = (float) mProduct.getPrice() / (float) mProduct.getWeight() * (float) mIngredient.getAmount();
                return (int) mResult;

            } else if (mIngredient.getMeasureType() == 2 || mIngredient.getMeasureType() == 4){
                if(mProduct.getPrice() == 0 || (mProduct.getVolume() == 0)){
                    return 0;
                }
                float mResult = (float) mProduct.getPrice() / (float) mProduct.getVolume() * (float) mIngredient.getAmount();
                return (int) mResult;

            } else {
                return 0;
            }
        }
        return 0;
    }

    public int calcPriceMeal(UUID mealId){

        List<Ingredient> ingredients;
        ingredients = getIngredients(mealId);
        int totalValue = 0;

        for(Ingredient ingredient: ingredients){
            totalValue += calcPriceIngredient(ingredient.getMealId(), ingredient.getProductId());
        }

        return totalValue;
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

    private static ContentValues getContentValues(Meal meal){
        ContentValues  values = new ContentValues();
        values.put(MealTable.Cols.MEAL_ID, meal.getMealId().toString());
        values.put(MealTable.Cols.NAME, meal.getName());
        values.put(MealTable.Cols.PORTION, meal.getPortion());

        return values;
    }

    private static ContentValues getContentValues(Ingredient ingredient){
        ContentValues values = new ContentValues();
        values.put(IngredientTable.Cols.MEAL_ID, ingredient.getMealId().toString());
        values.put(IngredientTable.Cols.PRODUCT_ID, ingredient.getProductId().toString());
        values.put(IngredientTable.Cols.MEASURE_TYPE, ingredient.getMeasureType());
        values.put(IngredientTable.Cols.AMOUNT, ingredient.getAmount());

        return values;
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

    private MealCursorWrapper queryMeals(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                MealTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new MealCursorWrapper(cursor);
    }

    private IngredientCursorWrapper queryIngredients(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                IngredientTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new IngredientCursorWrapper(cursor);
    }









}
