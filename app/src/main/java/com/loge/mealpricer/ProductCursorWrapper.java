/*
 * ProductCursorWrapper
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

import android.database.Cursor;
import android.database.CursorWrapper;

import com.loge.mealpricer.MealPricerDbSchema.ProductTable;

import java.util.UUID;

/**
 * DAO Abstraction for DB access of the Product Table
 *
 * This class provides DAO abstraction for the Product Table.
 * It parses the data retrieved from the DB into an Product
 * object.
 */
class ProductCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * Product DAO getter
     * @return Product object
     */
    public Product getProduct(){
        String uuidString = getString(getColumnIndex(ProductTable.Cols.PRODUCT_ID));
        String name = getString(getColumnIndex(ProductTable.Cols.NAME));
        int weight = getInt(getColumnIndex(ProductTable.Cols.WEIGHT));
        int volume = getInt(getColumnIndex(ProductTable.Cols.VOLUME));
        int price = getInt(getColumnIndex(ProductTable.Cols.PRICE));

        Product product = new Product(UUID.fromString(uuidString));
        product.setName(name);
        product.setWeight(weight);
        product.setVolume(volume);
        product.setPrice(price);

        return product;
    }

}
