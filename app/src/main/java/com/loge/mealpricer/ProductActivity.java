/*
 * ProductActivity
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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.UUID;

/**
 * Activity that hosts the fragment for editing products
 * <p/>
 * This activity hosts the MealDetailFragment which contains
 * builds a form like view to edit products. Further, this
 * activity handles an extra which is sent back to the calling
 * activity on ending the activity to inform the calling
 * activity (MealPricerTabActivity) which tab view to
 * show (ProductListFragment).
 *
 */
public class ProductActivity extends AppCompatActivity {

    private static final String EXTRA_PRODUCT_ID =
            "com.loge.mealpricer.product_id";
    private static final String SWITCH_TAB = "switch_tab";
    private static final int TAB_SECOND = 1;

    /**
     * Method to load an extra on ProductActivity intent
     * <p/>
     * This method is called from an activity that wants to start
     * ProductActivity and provides by an extra which product to
     * edit.
     * @param packageContext caller activity context
     * @param productId string of productId uuid
     * @return intent loaded with extra
     */
    public static Intent newIntent(Context packageContext, UUID productId){
        Intent intent = new Intent(packageContext, ProductActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    /**
     * onCreate override
     * <p/>
     * Method creates the material design toolbar and
     * floating action button. Further it loads a
     * MealDetail fragment with the productId as args
     * and starts the fragment.
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null ){

            UUID productId = (UUID) getIntent()
                    .getSerializableExtra(EXTRA_PRODUCT_ID);
            fragment = ProductFragment.newInstance(productId);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    /**
     * Method to load the callers activity intent with an extra
     * <p/>
     * This method is used to send back information to the caller about
     * which Activity/Fragment we're coming from to open the correct
     * tab in the view pager.
     * @return intent loaded with extra
     */
    @Override
    public Intent getSupportParentActivityIntent() { // getParentActivityIntent() if you are not using the Support Library
        final Bundle bundle = new Bundle();
        final Intent intent = new Intent(this, MealPricerTabActivity.class);

        bundle.putInt(SWITCH_TAB, TAB_SECOND); // Both constants are defined in your code
        intent.putExtras(bundle);

        return intent;
    }

}
