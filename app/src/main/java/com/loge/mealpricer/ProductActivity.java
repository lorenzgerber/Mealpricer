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

import java.util.UUID;

public class ProductActivity extends AppCompatActivity {

    static final String EXTRA_PRODUCT_ID =
            "com.loge.mealpricer.product_id";
    private static final String SWITCH_TAB = "switch_tab";
    private static final int TAB_SECOND = 1;

    public static Intent newIntent(Context packageContext, UUID productId){
        Intent intent = new Intent(packageContext, ProductActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

    @Override
    public Intent getSupportParentActivityIntent() { // getParentActivityIntent() if you are not using the Support Library
        final Bundle bundle = new Bundle();
        final Intent intent = new Intent(this, MealPricerTabActivity.class);

        bundle.putInt(SWITCH_TAB, TAB_SECOND); // Both constants are defined in your code
        intent.putExtras(bundle);

        return intent;
    }



}
