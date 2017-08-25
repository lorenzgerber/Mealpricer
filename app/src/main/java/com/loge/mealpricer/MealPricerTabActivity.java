package com.loge.mealpricer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class MealPricerTabActivity extends AppCompatActivity
        implements ProductListFragment.OnListFragmentInteractionListener,
        MealListFragment.OnListFragmentInteractionListener {

    private static final String SWITCH_TAB = "switch_tab";
    private static final String[] mPortionsString = {"1", "2", "4"};


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private AppCompatSpinner mSpinner;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    private int currentItemId;

    private String mMealName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_pricer_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItemId = mViewPager.getCurrentItem();
                if(currentItemId == 0){

                    enterMealNameDialog();

                } else if (currentItemId == 1){
                    Snackbar.make(view, "Create a new Product DB entry ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Product mProduct = MealPricer.get(MealPricerTabActivity.this).newProduct();
                    MealPricer.get(MealPricerTabActivity.this).addProudct(mProduct);
                    onListFragmentInteraction(mProduct);
                }

            }
        });

        final Intent intent = getIntent();

        if (intent.hasExtra(SWITCH_TAB)) {
            final int tab = intent.getExtras().getInt(SWITCH_TAB);
            mViewPager.setCurrentItem(tab);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meal_pricer_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

    public void createNewMeal(){
        //Snackbar.make(view, "Create a new Meal DB entry  ", Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show();
        Meal mMeal = MealPricer.get(MealPricerTabActivity.this).newMeal();
        mMeal.setName(mMealName);
        MealPricer.get(MealPricerTabActivity.this).addMeal(mMeal);
        onListFragmentInteraction(mMeal);
    }

    public void enterMealNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MealPricerTabActivity.this);
        builder.setTitle("Enter Name and Portion of Meal");

        LayoutInflater inflater = MealPricerTabActivity.this.getLayoutInflater();

        View mView = inflater.inflate(R.layout.new_meal_dialog, null);




        final EditText input = (EditText) mView.findViewById(R.id.new_meal_name);
        final AppCompatSpinner mSpinner = (AppCompatSpinner) mView.findViewById(R.id.portion_spinner);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mPortionsString);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(aa);
        /*
        int mPortion = mMeals.get(position).getPortion();
        if (mPortion == 1){
            holder.mSpinner.setSelection(0);
        } else if (mPortion == 2){
            holder.mSpinner.setSelection(1);
        } else {
            holder.mSpinner.setSelection(2);
        }*/

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //float mResult = (float) mMeals.get(mPosition).getPrice() / (float) mPortionsInteger[position];
                //holder.mPriceView.setText(String.valueOf((int) mResult));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        builder.setView(mView);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMealName = input.getText().toString();
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
