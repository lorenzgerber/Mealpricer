package com.loge.mealpricer;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.loge.mealpricer.MealPricerDbSchema.ProductTable;

import java.util.UUID;

/**
 * Created by loge on 2017-08-17.
 */

public class ProductCursorWrapper extends CursorWrapper {
    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

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
