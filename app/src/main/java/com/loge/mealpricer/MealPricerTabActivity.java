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
     * <p/>
     * Here also the tab switching when returning
     * from another activity is handled using the
     * SWTICH_TAB extra.
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
     * MealList recycler view item listener implementation
     * <p/>
     * Starts a MealDetailActivity providing the chosen mealId obtained
     * from the listener as extra.
     * @param meal object instance which will be accessible through listener
     */
    @Override
    public void onListFragmentInteraction(Meal meal){
        Intent intent = MealDetailActivity.newIntent(this, meal.getMealId());
        startActivity(intent);
    }

    /**
     * ProductList recycler view item listener implementation
     * <p/>
     * Starts a ProductActivity providing the chosen productId obtained
     * from the listener as extra.
     * @param product object which will be accessible through recycler view item listener
     */
    @Override
    public void onListFragmentInteraction(Product product) {
        Intent intent = ProductActivity.newIntent(this, product.getProductId());
        startActivity(intent);
    }

    /**
     * Helper method for the new meal AlertDialog
     * <p/>
     * This method is called as positive exit from the enterMealNameDialog.
     * It requests a new meal instance from the database and configures it
     * according to the user entries from the alert dialog.
     */
    private void createNewMeal(){
        Meal mMeal = MealPricer.get(MealPricerTabActivity.this).newMeal();
        mMeal.setName(mMealName);
        mMeal.setPortion(mMealPortion);
        MealPricer.get(MealPricerTabActivity.this).addMeal(mMeal);
        onListFragmentInteraction(mMeal);
    }

    /**
     * Helper method for new product AlertDialog
     * <p/>
     * This method is called as positive exit from the enterProductNameDialog.
     * It requests a new product instance from the database and configures it
     * according to the user entries from the alert dialog.
     */
    private void createNewProduct(){
        Product mProduct = MealPricer.get(MealPricerTabActivity.this).newProduct();
        mProduct.setName(mProductName);
        MealPricer.get(MealPricerTabActivity.this).addProduct(mProduct);
        onListFragmentInteraction(mProduct);
    }

    /**
     * Custom Alert Dialog for new Meal
     * <p/>
     * Dialog asks user for product name and the meal portion. The
     * dialog uses a custom layout to host both an EditText box
     * and a spinner. On succes, a helper method is called that
     * creates the new meal in the database and subsequently
     * starts a new mealDetail activity indicating the new
     * meal id as extra.
     */
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

    /**
     * AlertDialog to obtain product name for new product
     * <p/>
     * Standard AlertDialog that asks the user for the new
     * product name. On success, a helper method is called
     * that creates the new product in the database and
     * subsequently starts a new productDetail activity
     * providing the new productId as extra.
     */
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
