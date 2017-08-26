package com.loge.mealpricer;

import java.util.UUID;


public class Product {

    private final UUID mProductId;
    private String mName;
    private int mPrice;
    private int mWeight;
    private int mVolume;

    public Product(){
        mProductId = UUID.randomUUID();
        mName = null;
        mPrice = 0;
        mWeight = 0;
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

    public String getPhotoFilename() {
        return "IMG_" + getProductId().toString() + ".jpg";
    }
}
