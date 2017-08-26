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

public class MealDetailActivity extends AppCompatActivity
        implements IngredientListFragment.OnListFragmentInteractionListener {


    static final String EXTRA_MEAL_ID = "com.loge.mealpricer.meal_id";
    private static final int REQUEST_PHOTO = 2;

    private Meal mMeal;
    private UUID mealId;
    private File mPhotoFile;
    private ImageView mImageView;


    public static Intent newIntent(Context packageContext, UUID mealId){
        Intent intent = new Intent(packageContext, MealDetailActivity.class);
        intent.putExtra(EXTRA_MEAL_ID, mealId);
        return intent;
    }

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
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), MealDetailActivity.this);
            mImageView.setBackground(new BitmapDrawable(MealDetailActivity.this.getResources(), bitmap));
        }


        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        FloatingActionButton fab_photo = findViewById(R.id.fab_take_photo);

        PackageManager packageManager = this.getPackageManager();
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        fab_photo.setEnabled(canTakePhoto);

        fab_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = FileProvider.getUriForFile(MealDetailActivity.this,
                        "com.loge.mealpricer.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);


                List<ResolveInfo> cameraActivities = MealDetailActivity.this
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    MealDetailActivity.this.grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);






                Snackbar.make(view, "Take Photo for Meal", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    @Override
    public void onPause(){
        super.onPause();

        MealPricer.get(this)
                .updateMeal(mMeal);
    }

    @Override
    public void onListFragmentInteraction() {
    }

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
                Bitmap bitmap = PictureUtils.getScaledBitmap(
                        mPhotoFile.getPath(), MealDetailActivity.this);
                mImageView.setBackground(new BitmapDrawable(getResources(), bitmap));
            }
        }

    }

}
