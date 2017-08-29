/*
 * MealPricerDbSchema
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

/**
 * Database Scheme
 * <p/>
 * This class defines the database scheme
 * for the application user side with
 * public static variables for global
 * accessability.
 */
class MealPricerDbSchema {

    /**
     * Class defining application wide handles for the product table
     */
    public static final class ProductTable {
        public static final String NAME = "product";

        public static final class Cols {
            public static final String PRODUCT_ID = "id";
            public static final String NAME = "name";
            public static final String PRICE = "price";
            public static final String WEIGHT = "weight";
            public static final String VOLUME = "volume";
        }
    }

    /**
     * Class defining application wide handles for the meal table
     */
    public static final class MealTable {
        public static final String NAME = "meal";

        public static final class Cols {
            public static final String MEAL_ID = "id";
            public static final String NAME = "name";
            public static final String PORTION = "portion";
        }
    }

    /**
     * Class defining application wide handles for the ingredient table
     */
    public static final class IngredientTable {
        public static final String NAME = "ingredient";

        public static final class Cols {
            public static final String MEAL_ID = "meal_id";
            public static final String PRODUCT_ID = "product_id";
            public static final String MEASURE_TYPE = "measure_type";
            public static final String AMOUNT = "amount";
        }
    }
}
