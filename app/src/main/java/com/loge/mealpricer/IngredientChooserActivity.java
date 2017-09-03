/*
 * IngredientChooserActivity
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.UUID;

import static com.loge.mealpricer.MealDetailActivity.EXTRA_MEAL_ID;

/**
 * Base Activity to choose ingredients for a meal
 * <p/>
 * Activity hosts the fragment to choose ingredients for a meal.
 * This activity is started from the MealDetailActivity/MealListFragment
 * and obtains the mealId as an extra.
 */
public class IngredientChooserActivity extends AppCompatActivity{

    private UUID mealId;

    /**
     * static method used to start the activity from within another
     * activity/fragment. It facilitates for passing the mealId as extra.
     * <p/>
     * @param packageContext from caller activity
     * @param mealId uuid of the meal that shall be used in the IngredientChooser activity
     * @return intent to start IngredientChooserActivity
     */
    public static Intent newIntent(Context packageContext, UUID mealId){
        Intent intent = new Intent(packageContext, IngredientChooserActivity.class);
        intent.putExtra(EXTRA_MEAL_ID, mealId);
        return intent;
    }

    /**
     * configures toolbar and starts fragment
     * <p/>
     * This method configures and starts the fragment which contains
     * the main functionality. Some configurations of the toolbar are
     * done here.
     * @param savedInstanceState bundle with extra
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mealId = (UUID) getIntent().getSerializableExtra(EXTRA_MEAL_ID);


        setContentView(R.layout.activity_ingredient_chooser_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.ingredient_chooser_fragment_container);

        if (fragment == null ){

            fragment = IngredientChooserFragment.newInstance(mealId);
            fm.beginTransaction()
                    .add(R.id.ingredient_chooser_fragment_container, fragment)
                    .commit();
        }

    }

    /**
     * method to return an extra to the previous activity
     * <p/>
     * This is used to inform the resuming previous activity from where we return
     * to set the proper state of the tab gui.
     * @return intent with extra
     */
    @Override
    public Intent getSupportParentActivityIntent() {
        final Intent intent = new Intent(this, MealDetailActivity.class);
        intent.putExtra(EXTRA_MEAL_ID, mealId);

        return intent;
    }


}
