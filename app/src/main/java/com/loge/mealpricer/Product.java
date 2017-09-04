/*
 * Product
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

import java.util.UUID;

/**
 * Model Class
 *
 * Represents the model of a product. A product has
 * one price but can have an indication both in
 * volume and weight. Obviously, the correctness of
 * this can not be checked in the model and is up
 * to the user input. Currently, weight and volume
 * are implemented completely independent of each
 * other without any update mechanism even if
 * volume, weight and price are available.
 */

public class Product {

    private final UUID mProductId;
    private String mName;
    private int mPrice;
    private int mWeight;
    private int mVolume;

    /**
     * Default constructor of the model calls product
     * <p/>
     * On instantiation, an uuid is set for the new meal.
     */
    public Product(){
        mProductId = UUID.randomUUID();
        mName = null;
        mPrice = 0;
        mWeight = 0;
    }

    /**
     * Constructor that takes an existing uuid
     * <p/>
     * This constructor is used in the DAO to
     * construct run time product instances from
     * db entries.
     * @param productId uuid, usually loaded from db entries
     */
    public Product(UUID productId){
        mProductId = productId;
    }

    /**
     * Getter for ProductId property
     * <p/>
     * @return unique identifier of the product
     */
    public UUID getProductId() {
        return mProductId;
    }

    /**
     * Getter for Name property
     * <p/>
     * @return name of the product as string
     */
    public String getName() {
        return mName;
    }

    /**
     * Setter for Name property
     * <p/>
     * @param name String that determines the name of the product
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Getter for price property
     * <p/>
     * @return returns integer value of product price
     */
    public int getPrice() {
        return mPrice;
    }

    /**
     * Setter for price property
     * <p/>
     * The price is valid both for volume and weight
     * property. Hence it is up to the user to provide
     * the correct information.
     * @param price of product as integer value
     */
    public void setPrice(int price) {
        mPrice = price;
    }

    /**
     * Getter for weight property
     * <p/>
     * @return returns the products weight as integer
     */
    public int getWeight() {
        return mWeight;
    }

    /**
     * Setter for weight property
     * <p/>
     * Setter for the weight value that relates to the given price.
     * It is up to the user to check for a correct value.
     * @param weight of the product as integer
     */
    public void setWeight(int weight) {
        mWeight = weight;
    }

    /**
     * Getter of the volume property
     * <p/>
     * Volume value that relates to the given price.
     * @return volume of the product as integer
     */
    public int getVolume() {
        return mVolume;
    }

    /**
     * Setter of the volume property
     * <p/>
     * Setter for the volume value that relates to the given price.
     * It is up to the user to check for a correct value.
     * @param volume of the product as integer
     */
    public void setVolume(int volume) {
        mVolume = volume;
    }

    /**
     * Getter of the photo filename
     * <p/>
     * This method constructs filename string for the product photo file. Method will return
     * the name independent whether the file exists or not.
     * @return filename string for the product photo file.
     */
    public String getPhotoFilename() {
        return "IMG_" + getProductId().toString() + ".jpg";
    }
}
