package com.loge.mealpricer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.loge.mealpricer.dummy.DummyContent;

public class MealDetailActivity extends AppCompatActivity
        implements IngredientListFragment.OnListFragmentInteractionListener {

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, MealDetailActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_photo = (FloatingActionButton) findViewById(R.id.fab_take_photo);
        fab_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Take Photo for Meal", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab_add_ingredient = (FloatingActionButton) findViewById(R.id.fab_add_ingredient);
        fab_add_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add New Ingredients", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.ingredient_fragment_container);

        if (fragment == null ){
            fragment = new IngredientListFragment();
            fm.beginTransaction()
                    .add(R.id.ingredient_fragment_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
