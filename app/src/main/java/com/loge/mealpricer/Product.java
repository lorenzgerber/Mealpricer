package com.loge.mealpricer;

import java.util.UUID;

/**
 * Created by loge on 2017-08-01.
 */

public class Product {

    private UUID mProductId;
    private String mName;
    private int mPrice;
    private int mWeight;
    private int mVolume;

    public Product(){
        mProductId = UUID.randomUUID();
        mName = "Potato";
        mPrice = 20;
        mWeight = 1000;
    }

    public Product(UUID productId){
        mProductId = productId;
    }

    public UUID getProductId() {
        return mProductId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public int getVolume() {
        return mVolume;
    }

    public void setVolume(int volume) {
        mVolume = volume;
    }
}
