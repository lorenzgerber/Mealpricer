/*
 * MealPricerTabActivity
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

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

/**
 * Main Activity of MealPricer
 * <p/>
 * On starting the app, the user ends up in this activity which is implemented
 * with a viewpager to hosts the MealListFragment and the ProductListFragment in
 * material design style tabs. The Activity uses an extra with the ProductListFragment
 * to keep track of which tab to present on returning from either of the fragments.
 * The class further implements AlertDialogs for collecting data from the user to
 * add a new meal or product.
 * <p/>
 * The activity implements the onListFragmentInteractionListeners for both hosted
 * fragments to provide actions on recycler list item clicks.
 */
public class MealPricerTabActivity extends AppCompatActivity
        implements ProductListFragment.OnListFragmentInteractionListener,
        MealListFragment.OnListFragmentInteractionListener {

    private static final String SWITCH_TAB = "switch_tab";
    private static final String[] mPortionsString = {"1", "2", "4"};


    /**
     * The ViewPager that will host the section contents.
     */
    private ViewPager mViewPager;


    private int currentItemId;

    private String mMealName = "";
    private String mProductName = "";
    private int mMealPortion = 1;

    /**
     * onCreate override
     * </p>
     * Here the viewpager for the tabs and the
     * floating action buttons are setup. Further,
     * the behaviour of the toolbar is set.
     * @param savedInstanceState stored state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_pricer_tab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItemId = mViewPager.getCurrentItem();
                if(currentItemId == 0){
                    enterMealNameDialog();
                } else if (currentItemId == 1){
                    enterProductNameDialog();
                }
            }
        });

        final Intent intent = getIntent();

        if (intent.hasExtra(SWITCH_TAB)) {
            final int tab = intent.getExtras().getInt(SWITCH_TAB);
            mViewPager.setCurrentItem(tab);
        }
    }

    /**
     * This is currently a stub
     * as no menu is implemented yet.
     * @param menu widget whet
     * @return boolean when the menu could be installed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meal_pricer_tab, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListFragmentInteraction(Meal item){
        Intent intent = MealDetailActivity.newIntent(this, item.getMealId());
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Product item) {
        Intent intent = ProductActivity.newIntent(this, item.getProductId());
        startActivity(intent);
    }

    private void createNewMeal(){
        Meal mMeal = MealPricer.get(MealPricerTabActivity.this).newMeal();
        mMeal.setName(mMealName);
        mMeal.setPortion(mMealPortion);
        MealPricer.get(MealPricerTabActivity.this).addMeal(mMeal);
        onListFragmentInteraction(mMeal);
    }

    private void createNewProduct(){
        Product mProduct = MealPricer.get(MealPricerTabActivity.this).newProduct();
        mProduct.setName(mProductName);
        MealPricer.get(MealPricerTabActivity.this).addProduct(mProduct);
        onListFragmentInteraction(mProduct);
    }

    private void enterMealNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MealPricerTabActivity.this);
        builder.setTitle("Enter Name and Portion of Meal");

        LayoutInflater inflater = MealPricerTabActivity.this.getLayoutInflater();

        View mView = inflater.inflate(R.layout.new_meal_dialog, null);


        final EditText mInput = mView.findViewById(R.id.new_meal_name);

        final AppCompatSpinner mSpinner = mView.findViewById(R.id.portion_spinner);
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mPortionsString);
        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mArrayAdapter);

        builder.setView(mView);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMealName = mInput.getText().toString();
                mMealPortion = Integer.valueOf(mSpinner.getSelectedItem().toString());
                createNewMeal();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void enterProductNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MealPricerTabActivity.this);
        builder.setTitle("Enter Name of Product");

        final EditText mInput = new EditText(this);
        mInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        builder.setView(mInput);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProductName = mInput.getText().toString();
                createNewProduct();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 1){
                return ProductListFragment.newInstance();
            }
            return MealListFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
