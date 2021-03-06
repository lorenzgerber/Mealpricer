/*
 * IngredientChooserFragment
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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.loge.mealpricer.IngredientListFragment.ARG_MEAL_ID;
import static com.loge.mealpricer.MeasureType.BOTH_WEIGHT;
import static com.loge.mealpricer.MeasureType.NONE;
import static com.loge.mealpricer.MeasureType.ONLY_VOLUME;
import static com.loge.mealpricer.MeasureType.ONLY_WEIGHT;

/**
 * A fragment that contains an interactive recycler view.
 * <p/>
 * The fragment uses a recycler view to present the selection
 * means for choosing ingredients to a meal. This fragment receives on
 * create and resume the mealId.
 */
public class IngredientChooserFragment extends Fragment {


    private RecyclerView mIngredientChooserRecyclerView;
    private IngredientChooserRecyclerViewAdapter mAdapter;
    private List<Ingredient> mIngredients;
    private UUID mMealId;


    /**
     * constructor for fragment
     *
     * Needs to know which mealId we work with
     * to pass it as argument on startup.
     *
     * @param mealId uuid of the meal that shall be used
     * @return fragment initialized with the meal provided as mealId
     */
    public static IngredientChooserFragment newInstance(UUID mealId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEAL_ID, mealId);

        IngredientChooserFragment fragment = new IngredientChooserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * onCreate override to obtain extra
     * <p/>
     * The mealId string is obtained and converted into an UUID
     * @param savedInstanceState bundle which has to contain the mealId as String
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMealId = (UUID) getArguments().getSerializable(ARG_MEAL_ID);
    }

    /**
     * onCreateView override does some gui styling
     * <p/>
     * Besides loading the recycler view layout manager, a divider line
     * between the list entries is initiated here.
     *
     * @param inflater layout inflater
     * @param parent parent ViewGroup, where the recycler view shall be attached to
     * @param savedInstanceState bundle that contains the mealId
     * @return recycler view showing all ingredients
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_chooser_list, parent, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mIngredientChooserRecyclerView = (RecyclerView) view;
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            mIngredientChooserRecyclerView.setLayoutManager(layoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                    mIngredientChooserRecyclerView.getContext(),
                    layoutManager.getOrientation()
            );

            mIngredientChooserRecyclerView.addItemDecoration(dividerItemDecoration);

            updateUI();

        }
        return view;
    }

    /**
     * method to update the recycler view
     * <p/>
     * All products of the db are loaded and iterated over.
     * For each instance from the db, it is checked whether it
     * is already assigned to the current meal. If so, a new
     * ingredient instance is configured accordingly. Else,
     * a new 'clean' ingredient instance is instantiated.
     */
    private void updateUI(){

        MealPricer mealPricer = MealPricer.get(getActivity());
        List<Product> products = mealPricer.getProducts();
        mIngredients = new ArrayList<>();


        for(Product product: products){
            Ingredient ingredient;
            ingredient = mealPricer.getIngredient(mMealId, product.getProductId());
            if ( ingredient != null){
                ingredient.setSelected(true);
                mIngredients.add(ingredient);
            } else {
                ingredient = new Ingredient(mMealId, product.getProductId());
                ingredient.setMeasureType(getMeasureType(product));
                mIngredients.add(ingredient);
            }

        }

        if(mAdapter == null){
            mAdapter = new IngredientChooserRecyclerViewAdapter(products, mIngredients);
            mIngredientChooserRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setIngredientsProducts(mIngredients, products);
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * override for onPause
     * <p/>
     * updates, adds or deletes ingredients
     */
    @Override
    public void onPause(){

        for(Ingredient ingredient:mIngredients){

            if(MealPricer.get(getActivity()).getIngredient(mMealId, ingredient.getProductId()) != null){
                if(ingredient.getSelected()){
                    MealPricer.get(getActivity()).updateIngredient(ingredient);
                } else{
                    MealPricer.get(getActivity()).deleteIngredient(ingredient);
                }
            } else {
                if(ingredient.getSelected()){
                    MealPricer.get(getActivity()).addIngredient(ingredient);
                }
            }
        }

        super.onPause();

    }


    /**
     * Interprets the product for setting correct MeasureType
     * <p/>
     * The method checks which values are available in the product to
     * set a correct MeasureType for the ingredient instance.
     * @param product instance of product
     * @return MeasureType enum value according to the provided product
     */
    private MeasureType getMeasureType(Product product){
        if (product.getWeight() == 0 && product.getVolume() == 0){
            return NONE;
        } else if (product.getWeight() > 0 && product.getVolume() == 0){
            return ONLY_WEIGHT;
        } else if (product.getWeight() > 0 && product.getVolume() > 0){
            return BOTH_WEIGHT;
        }
        return ONLY_VOLUME;
    }

}
