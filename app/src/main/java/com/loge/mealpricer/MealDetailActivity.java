/*
 * MealDetailActivity
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
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;


import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Activity that shows one meal and its ingredients
 * <p/>
 * This activity hosts a fragment with a RecyclerView to
 * show for one meal all it's ingredients and the fractional
 * costs of each ingredient. The activity implements through a
 * Floating Action Button also the functionality to take a photo
 * of the meal which will then be shown in the toolbar. Another
 * Floating Action Button is used to call the ingredient edit
 * activity. The activity receives an extra that determines
 * which meal shall be shown.
 */
public class MealDetailActivity extends AppCompatActivity
        implements IngredientListFragment.OnListFragmentInteractionListener {


    static final String EXTRA_MEAL_ID = "com.loge.mealpricer.meal_id";
    private static final int REQUEST_PHOTO = 2;

    private Meal mMeal;
    private UUID mealId;
    private File mPhotoFile;
    private ImageView mImageView;

    /**
     * Static method used to start the activity from within another
     * activity/fragment. It facilitates for passing the mealId as extra.
     * <p/>
     * @param packageContext from caller activity
     * @param mealId uuid of the meal that shall be used in the MealDetailActivity
     * @return intent to start MealDetailActivity
     */
    public static Intent newIntent(Context packageContext, UUID mealId){
        Intent intent = new Intent(packageContext, MealDetailActivity.class);
        intent.putExtra(EXTRA_MEAL_ID, mealId);
        return intent;
    }

    /**
     * configures the toolbar and starts fragment
     * <p/>
     * This method configures and starts the fragment which contains the
     * main functionality. The material design collapsing toolbar is
     * configured and set from within this method. Further the
     * FAB's for photo intent and starting the edit ingredient activity
     * are also configured here.
     * @param savedInstanceState bundle with extra
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mealId = (UUID) getIntent().getSerializableExtra(EXTRA_MEAL_ID);
        mMeal = MealPricer.get(this).getMeal(mealId);
        mPhotoFile = MealPricer.get(this).getPhotoFile(mMeal);
        setContentView(R.layout.activity_meal_detail);

        mImageView = findViewById(R.id.meal_photo_test);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(mMeal.getName() +
                " - " + mMeal.getPortion() + " Portion(s)");


        if(mPhotoFile == null || !mPhotoFile.exists()){

            mImageView.setBackground(null);
        } else {
            Bitmap bitmap = PictureUtils.getCroppedBitmap(
                    mPhotoFile.getPath(), MealDetailActivity.this);
            mImageView.setBackground(new BitmapDrawable(MealDetailActivity.this.getResources(), bitmap));
        }

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        FloatingActionButton fab_photo = findViewById(R.id.fab_take_photo);

        PackageManager packageManager = this.getPackageManager();
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        fab_photo.setEnabled(canTakePhoto);

        fab_photo.setOnClickListener(new FabPhotoOnClickListener(captureImage));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab_add_ingredient = findViewById(R.id.fab_add_ingredient);
        fab_add_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add New Ingredients", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = IngredientChooserActivity.newIntent(MealDetailActivity.this, mealId);
                startActivity(intent);
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.ingredient_fragment_container);

        if (fragment == null ){

            fragment = IngredientListFragment.newInstance(mealId);
            fm.beginTransaction()
                    .add(R.id.ingredient_fragment_container, fragment)
                    .commit();
        }

    }

    /**
     * Custom onClick Listener
     * <p/>
     * OnClick listener that will start an implicit intent provided as argument to
     * the listener. The listener is specific to the MealDetailActivity FAB photo
     * button.
     */
    private class FabPhotoOnClickListener implements View.OnClickListener {

        final Intent mCaptureImage;

        /**
         * Constructor
         * <p/>
         * Constructor for custom onClick listener
         * @param captureImage Implicit intent to take a photo
         */
        public FabPhotoOnClickListener(Intent captureImage){
            mCaptureImage = captureImage;

        }

        /**
         * onClick implementation
         * <p/>
         * OnClick implementation specific for MealDetailActivity FAB photo
         * button. It will start the provided intent.
         * @param view to which the onClick action is attached to.
         */
        @Override
        public void onClick(View view) {
            Uri uri = FileProvider.getUriForFile(MealDetailActivity.this,
                    "com.loge.mealpricer.fileprovider", mPhotoFile);
            mCaptureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);


            List<ResolveInfo> cameraActivities = MealDetailActivity.this
                    .getPackageManager().queryIntentActivities(mCaptureImage,
                            PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo activity : cameraActivities) {
                MealDetailActivity.this.grantUriPermission(activity.activityInfo.packageName,
                        uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            startActivityForResult(mCaptureImage, REQUEST_PHOTO);

        }
    }

    /**
     * onListFragmentInteraction override
     * <p/>
     * This code is currently not used.
     */
    @Override
    public void onListFragmentInteraction() {
    }

    /**
     * onActivityResult override
     * <p/>
     * Method that kicks in when returning from taking photo intent.
     * It loads the photo and displays it in the toolbar.
     * @param requestCode request code for photo
     * @param resultCode reports success of the returning intent
     * @param data not used in this override
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(this,
                    "com.loge.mealpricer.fileprovider", mPhotoFile);

            this.revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            mPhotoFile = MealPricer.get(MealDetailActivity.this).getPhotoFile(mMeal);
            if(mPhotoFile == null || !mPhotoFile.exists()){
                mImageView.setBackground(null);
            } else {
                Bitmap bitmap = PictureUtils.getCroppedBitmap(
                        mPhotoFile.getPath(), MealDetailActivity.this);
                mImageView.setBackground(new BitmapDrawable(getResources(), bitmap));
            }
        }

    }

}
