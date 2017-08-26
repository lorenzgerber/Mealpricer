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

public class IngredientChooserActivity extends AppCompatActivity
        implements IngredientChooserFragment.OnListFragmentInteractionListener {

    private UUID mealId;

    public static Intent newIntent(Context packageContext, UUID mealId){
        Intent intent = new Intent(packageContext, IngredientChooserActivity.class);
        intent.putExtra(EXTRA_MEAL_ID, mealId);
        return intent;
    }

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

    @Override
    public void onListFragmentInteraction() {

    }

    @Override
    public Intent getSupportParentActivityIntent() {
        final Intent intent = new Intent(this, MealDetailActivity.class);
        intent.putExtra(EXTRA_MEAL_ID, mealId);

        return intent;
    }


}
