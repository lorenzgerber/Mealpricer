package com.loge.mealpricer;

/**
 * Created by loge on 2017-08-07.
 */

public class MealPricerDbSchema {
    public static final class ProductTable {
        public static final String NAME = "product";

        public static final class Cols {
            public static final String PRODUCT_ID = "id";
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String PRICE = "price";
            public static final String WEIGHT = "weight";
            public static final String VOLUME = "volume";
        }
    }

    public static final class MealTable {
        public static final String NAME = "meal";

        public static final class Cols {
            public static final String MEAL_ID = "id";
            public static final String NAME = "id";
        }
    }

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
