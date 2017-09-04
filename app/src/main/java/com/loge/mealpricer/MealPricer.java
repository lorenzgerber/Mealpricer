/*
 * MealPricer
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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.loge.mealpricer.MealPricerDbSchema.*;
import static com.loge.mealpricer.MeasureType.BOTH_VOLUME;
import static com.loge.mealpricer.MeasureType.BOTH_WEIGHT;
import static com.loge.mealpricer.MeasureType.ONLY_VOLUME;
import static com.loge.mealpricer.MeasureType.ONLY_WEIGHT;

/**
 * Main Data IO class
 * <p/>
 * This class provides application level access to the persistent data stored
 * in the database. It serves as the layer between Database, model objects
 * and user application calls. To provide simple application wide data access,
 * this class is implemented as a singleton according to the example of the
 * course book (chap 14, Android Programming: The Big Nerd Ranch, 3:e, 2017).
 */
public class MealPricer {
    private static MealPricer sMealPricer;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;

    /**
     * public Singleton constructor
     * <p/>
     * According to android code analyzer, singletons
     * are not completely safe in android. In the future
     * this implementation should be improved.
     * @param context callers context
     * @return a MealPricer instance
     */
    public static MealPricer get(Context context){
        if (sMealPricer == null){
            sMealPricer = new MealPricer(context);
        }
        return sMealPricer;
    }

    /**
     * Private constructor
     * <p/>
     * The default constructor is only used internally
     * @param context callers context
     */
    private MealPricer(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new MealPricerBaseHelper(mContext).getWritableDatabase();

    }

    /**
     * Instantiates a new product object instance
     * <p/>
     * Uses the zero argument constructor which will
     * create a UUID on instantiation.
     * @return product instance
     */
    public Product newProduct(){
        return new Product();
    }

    /**
     * Instantiates a new Meal object instance
     * <p/>
     * Uses the zero argument constructor which will
     * create an UUID on instantiation.
     * @return new instance of Meal
     */
    public Meal newMeal(){
        return new Meal();
    }

    /**
     * Write product to the database
     * <p/>
     * Takes a product object and writes it to the
     * database.
     * @param p product object
     */
    public void addProduct(Product p) {
        ContentValues values = getContentValues(p);
        mDatabase.insert(ProductTable.NAME, null, values);
    }

    /**
     * Write meal to the database
     * <p/>
     * Takes a meal object and writes it to the database.
     * @param m meal object to be added to the database
     */
    public void addMeal(Meal m){
        ContentValues values = getContentValues(m);
        mDatabase.insert(MealTable.NAME, null, values);
    }

    /**
     * Write Ingredient to the database
     * <p/>
     * Takes an ingredient object and writes it to the database.
     * @param i ingredient object to be added to the database
     */
    public void addIngredient(Ingredient i){
        ContentValues values = getContentValues(i);
        mDatabase.insert(IngredientTable.NAME, null, values);
    }


    /**
     * Delete meal from database
     * <p/>
     * This method will also delete all ingredients
     * linked through their mealId uuid to the meal object.
     * @param m meal object to be deleted from the database
     */
    public void deleteMeal(Meal m){
        String uuidMealIdString = m.getMealId().toString();
        deleteIngredients(getIngredients(m.getMealId()));

        mDatabase.delete(MealTable.NAME,
                MealTable.Cols.MEAL_ID + " = ?",
                new String[]{uuidMealIdString});

    }


    /**
     * Delete ingredient from database
     * <p/>
     * This method deletes the corresponding ingredient
     * in the database when productId and mealId match.
     * @param i ingredient object to be deleted
     */
    public void deleteIngredient(Ingredient i){

        String uuidMealIdString = i.getMealId().toString();
        String uuidProductIdString = i.getProductId().toString();

        mDatabase.delete(IngredientTable.NAME,
                IngredientTable.Cols.MEAL_ID + " = ? AND " +
                IngredientTable.Cols.PRODUCT_ID + " = ?",
                new String[]{uuidMealIdString, uuidProductIdString});
    }

    /**
     * Deletes a list of ingredients
     * <p/>
     * Convenience method to delete a whole list of ingredients.
     * @param is list of ingredient objects
     */
    private void deleteIngredients(List<Ingredient> is){
        for (Ingredient ingredient:is){
            deleteIngredient(ingredient);
        }
    }

    /**
     * Update a database product entry
     * <p/>
     * Method looks for existence of productId from
     * provided product object in db and updates if found.
     * @param product Product object to be used for updating corresponding database entry
     */
    public void updateProduct(Product product){
        String uuidString = product.getProductId().toString();
        ContentValues values = getContentValues(product);

        mDatabase.update(ProductTable.NAME, values, ProductTable.Cols.PRODUCT_ID + " = ?",
                new String[] { uuidString});
    }

    /**
     * Update a database meal entry
     * <p/>
     * Method looks for database entry with corresponding
     * mealId to the one provided in meal object. If found,
     * the database is updated with the provided object data.
     * Currently unused method as name updates of the meal are
     * not implemented yet.
     * @param meal meal object to be used for updating corresponding database entry
     */
    public void updateMeal(Meal meal){
        String uuidString = meal.getMealId().toString();
        ContentValues values = getContentValues(meal);

        mDatabase.update(MealTable.NAME, values, MealTable.Cols.MEAL_ID + " = ?",
                new String[] { uuidString });
    }

    /**
     * Update a database ingredient entry
     * <p/>
     * Method looks for database entry with corresponding
     * mealId and productId to these provided in the ingredient
     * object. If they match, the database entry is updated with
     * the provided object data.
     * @param ingredient object instance
     */
    public void updateIngredient(Ingredient ingredient){
        String uuidMealString = ingredient.getMealId().toString();
        String uuidProductString = ingredient.getProductId().toString();
        ContentValues values = getContentValues(ingredient);

        mDatabase.update(IngredientTable.NAME, values,
                IngredientTable.Cols.MEAL_ID +
                        " = ? AND " + IngredientTable.Cols.PRODUCT_ID + " = ?",
                new String[] { uuidMealString, uuidProductString});

    }

    /**
     * Database get method for products
     * <p/>
     * Method returns a product list corresponding
     * the whole product table in the database.
     * @return List of product objects
     */
    List<Product> getProducts(){
        List<Product> products = new ArrayList<>();

        try (ProductCursorWrapper cursor = queryProducts(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                products.add(cursor.getProduct());
                cursor.moveToNext();
            }
        }

        return products;
    }


    /**
     * Database get method for meals
     * <p/>
     * Method returns a meal list corresponding
     * the whole meal table in the database.
     * @return List of meal objects.
     */
    List<Meal> getMeals(){
        List<Meal> meals = new ArrayList<>();

        try (MealCursorWrapper cursor = queryMeals(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                meals.add(cursor.getMeal());
                cursor.moveToNext();
            }
        }

        return meals;
    }

    /**
     * Database get method for ingredients
     * <p/>
     * The method is used to obtain all ingredients
     * persistently associated to a specific meal.
     * @param mealId uuid of the meal for which the ingredients shall be returned
     * @return list of ingredient objects
     */
    List<Ingredient> getIngredients(UUID mealId){
        List<Ingredient> ingredients = new ArrayList<>();

        try (IngredientCursorWrapper cursor = queryIngredients(IngredientTable.Cols.MEAL_ID + " = ?",
                new String[]{mealId.toString()})) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ingredients.add(cursor.getIngredient());
                cursor.moveToNext();
            }
        }

        return ingredients;
    }

    /**
     * Database get method for a single product
     * <p/>
     * Method queries for the database entry with the
     * provided productId. As the productId is also the
     * table key, maximum one product can be found.
     * @param productId uuid of the product to fetch from the database
     * @return product object with provided uuid. If not found, the method
     * returns null.
     */
    public Product getProduct(UUID productId){

        try (ProductCursorWrapper cursor = queryProducts(
                ProductTable.Cols.PRODUCT_ID + " = ?",
                new String[]{
                        productId.toString()
                }
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getProduct();
        }
    }


    /**
     * Database get method for a single meal
     * <p/>
     * Method queries for the database entry with the
     * provided mealId. As the mealId is also the table key
     * maximum one meal can be found.
     * @param mealId uuid of the meal to fetch from the database
     * @return meal object with provided uuid. If not found, the
     * method returns null.
     */
    public Meal getMeal(UUID mealId){

        try (MealCursorWrapper cursor = queryMeals(
                MealTable.Cols.MEAL_ID + " = ?",
                new String[]{mealId.toString()}
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getMeal();
        }
    }

    /**
     * Database get method for a single ingredient
     * <p/>
     * Method queries the database with the provided
     * mealId and productId. As each mealId and productId
     * are themselves table keys, combinations of these are also
     * unique in the ingredient table.
     * @param mealId uuid of the meal to which the ingredient is associated
     * @param productId uuid of the product which the ingredient represents
     * @return ingredient object with corresponding uuid's or null
     * if not exists in database
     */
    public Ingredient getIngredient(UUID mealId, UUID productId){

        try (IngredientCursorWrapper cursor = queryIngredients(
                IngredientTable.Cols.MEAL_ID + " = ? AND " +
                        IngredientTable.Cols.PRODUCT_ID + " = ?",
                new String[]{mealId.toString(), productId.toString()}
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getIngredient();
        }

    }

    /**
     * Method to obtain a file handle for product photo file
     * <p/>
     * The method returns a file handle to the jpg image of
     * the photo if it exists, else null.
     * @param product object instance
     * @return file handle or null
     */
    public File getPhotoFile(Product product){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, product.getPhotoFilename());
    }

    /**
     * Method to obtain a file handle for meal photo file
     * <p/>
     * The method returns a file handle to the jpg image of
     * the photo if it exists, else null.
     * @param meal object instance
     * @return file handle or null
     */
    public File getPhotoFile(Meal meal){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, meal.getPhotoFilename());
    }


    /**
     * Calculation of fractional ingredient price
     * <p/>
     * This method calculates the fractional cost of an ingredient by using the
     * amount value of ingredient with the price indications of the corresponding
     * product entry. The ingredient is identified by it's mealId and productId.
     * For the calculation, all values are casted to floats and finally casted back
     * to an integer.
     * @param mealId uuid of meal to which ingredient is associated with
     * @param productId uuid of product that ingredient represents
     * @return integer fractional price of the ingredient
     */
    public int calcPriceIngredient(UUID mealId, UUID productId){
        Ingredient mIngredient = getIngredient(mealId, productId);
        Product mProduct;
        if (mIngredient!=null){
            mProduct = getProduct(productId);

            if(mIngredient.getMeasureType() == ONLY_WEIGHT || mIngredient.getMeasureType() == BOTH_WEIGHT){
                if(mProduct.getPrice() == 0 || (mProduct.getWeight() == 0)){
                    return 0;
                }
                float mResult = (float) mProduct.getPrice() / (float) mProduct.getWeight() * (float) mIngredient.getAmount();
                return (int) mResult;

            } else if (mIngredient.getMeasureType() == ONLY_VOLUME || mIngredient.getMeasureType() == BOTH_VOLUME){
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

    /**
     * Calculate cost of a meal by summing all fractional ingredient costs
     * <p/>
     * Convenience method that iterates through all ingredients associated
     * with a meal and summing up the fractional costs.
     * @param mealId uuid of the meal for which the price shall be calculated
     * @return integer price of the meal calculated from fractional ingredient costs
     */
    public int calcPriceMeal(UUID mealId){

        List<Ingredient> ingredients;
        ingredients = getIngredients(mealId);
        int totalValue = 0;

        for(Ingredient ingredient: ingredients){
            totalValue += calcPriceIngredient(ingredient.getMealId(), ingredient.getProductId());
        }

        return totalValue;
    }

    /**
     * DB helper method for product table
     * <p/>
     * Method takes a product object and assigns the values
     * to a contentValues object used by the database
     * access methods.
     * @param product object instance
     * @return contentValues object
     */
    private static ContentValues getContentValues(Product product){
        ContentValues values = new ContentValues();
        values.put(ProductTable.Cols.PRODUCT_ID, product.getProductId().toString());
        values.put(ProductTable.Cols.NAME, product.getName());
        values.put(ProductTable.Cols.PRICE, product.getPrice());
        values.put(ProductTable.Cols.WEIGHT, product.getWeight());
        values.put(ProductTable.Cols.VOLUME, product.getVolume());

        return values;
    }

    /**
     * DB helper method for meal table
     * <p/>
     * Method takes a meal object and assigns the values
     * to a contentValues object used by the database
     * access methods.
     * @param meal object instance
     * @return contentValues object
     */
    private static ContentValues getContentValues(Meal meal){
        ContentValues  values = new ContentValues();
        values.put(MealTable.Cols.MEAL_ID, meal.getMealId().toString());
        values.put(MealTable.Cols.NAME, meal.getName());
        values.put(MealTable.Cols.PORTION, meal.getPortion());

        return values;
    }

    /**
     * DB helper method for ingredient table
     * <p/>
     * Method takes an ingredient object and assigns the values
     * to a contentValues object used by the database
     * access methods.
     * @param ingredient object instance
     * @return contentValues object
     */
    private static ContentValues getContentValues(Ingredient ingredient){
        ContentValues values = new ContentValues();
        values.put(IngredientTable.Cols.MEAL_ID, ingredient.getMealId().toString());
        values.put(IngredientTable.Cols.PRODUCT_ID, ingredient.getProductId().toString());
        values.put(IngredientTable.Cols.MEASURE_TYPE, ingredient.getMeasureType().ordinal());
        values.put(IngredientTable.Cols.AMOUNT, ingredient.getAmount());

        return values;
    }

    /**
     * DB helper Class for product queries
     * <p/>
     * Method wraps a cursor object to a product specific
     * form that then can be handled by the DAO methods
     * such as getProduct. For internal use.
     * @param whereClause string sql where clause
     * @param whereArgs string array where clause arguments
     * @return ProductCursorWrapper instance
     */
    private ProductCursorWrapper queryProducts(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ProductTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                ProductTable.Cols.NAME
        );
        return new ProductCursorWrapper(cursor);
    }

    /**
     * DB helper Class for meal queries
     * <p/>
     * Method wraps a cursor object to a meal specific
     * form that then can be handled by the DAO methods
     * such as getMeal. For internal use.
     * @param whereClause string sql where clause
     * @param whereArgs string array where clause arguments
     * @return MealCursorWrapper instance
     */
    private MealCursorWrapper queryMeals(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                MealTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                MealTable.Cols.NAME
        );
        return new MealCursorWrapper(cursor);
    }

    /**
     * DB helper Class for ingredient queries
     * <p/>
     * Method wraps a cursor object to a ingredient specific
     * form that then can be handled by the DAO methods
     * such as getIngredient. For internal use.
     * @param whereClause string sql where clause
     * @param whereArgs string array where clause arguments
     * @return IngredientCursorWrapper instance
     */
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
