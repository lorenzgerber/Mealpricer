/*
 * MeasureType
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
 * Enum for ingredient gui configuration
 * <p/>
 * Depending on the available data from the product object
 * a ingredient supports various ways of how the ingredient
 * chooser view should be configured. It depends basically
 * whether there is none, only weight, only volume or
 * both weight and volume data available. The differentation
 * between BOTH_WEIGHT and BOTH_VOLUME further determines
 * which one is used in the ingredient when the product
 * provides values for both.
 */
public enum MeasureType {

    NONE,
    ONLY_WEIGHT,
    ONLY_VOLUME,
    BOTH_WEIGHT,
    BOTH_VOLUME
}
