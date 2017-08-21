package com.loge.mealpricer;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;



public class MealPricerTabActivity extends AppCompatActivity
        implements ProductListFragment.OnListFragmentInteractionListener,
        MealListFragment.OnListFragmentInteractionListener {

    private static final String SWITCH_TAB = "switch_tab";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private int currentItemId;

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
                    Snackbar.make(view, "Create a new Meal DB entry  ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Meal mMeal = MealPricer.get(MealPricerTabActivity.this).newMeal();
                    MealPricer.get(MealPricerTabActivity.this).addMeal(mMeal);
                    onListFragmentInteraction(mMeal);
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
        //Intent intent = new Intent(MealPricerTabActivity.this, MealDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Product item) {
        Intent intent = ProductActivity.newIntent(this, item.getProductId());
        startActivity(intent);
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
                return ProductListFragment.newInstance(1);
            }
            return MealListFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
