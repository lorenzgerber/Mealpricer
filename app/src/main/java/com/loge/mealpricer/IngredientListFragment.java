/*
 * IngredientListFragment
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A fragment that contains a recycler view
 * <p/>
 * Activities containing this fragment MUST implement
 * the {@link OnListFragmentInteractionListener}
 * interface. Currently, this interface is not used. The
 * Recycler view of this fragment presents the ingredients
 * of the meal that is obtained to the fragment as an extra.
 */
public class IngredientListFragment extends Fragment {

    public static final String ARG_MEAL_ID = "meal_id";

    private UUID mMealId;
    private RecyclerView mIngredientListRecyclerView;
    private IngredientRecyclerViewAdapter mAdapter;
    private List<Ingredient> mIngredients;
    private List<Product> mProducts;
    private List<Integer> mPrices;
    private OnListFragmentInteractionListener mListener;

    /**
     * Method to create new instance of fragment, providing the extra data
     * <p/>
     * @param mealId uuid of the meal to be shown in the view
     * @return instance of IngredientList fragment
     */
    public static IngredientListFragment newInstance(UUID mealId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEAL_ID, mealId);

        IngredientListFragment fragment = new IngredientListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * onCreate override to obtain extra data
     * <p/>
     * The mealId string is obtained and converted into an UUID
     * @param savedInstanceState bundle which has to contain the mealId as String
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MealPricer mealPricer = MealPricer.get(getActivity());
        mMealId = (UUID) getArguments().getSerializable(ARG_MEAL_ID);

        mIngredients = new ArrayList<>();
        mProducts = new ArrayList<>();

        mPrices = new ArrayList<>();

        mIngredients = mealPricer.getIngredients(mMealId);
        if(mIngredients != null){
            for(Ingredient ingredient:mIngredients){
                mProducts.add(mealPricer.getProduct(ingredient.getProductId()));
                mPrices.add(mealPricer.calcPriceIngredient(mMealId, ingredient.getProductId()));
            }
        }

    }

    /**
     * onCreateView override
     *
     * Loads and inflates the recycler view
     * @param inflater helper to inflate the layout
     * @param container here the recycler view will be inflated into
     * @param savedInstanceState eventual additional data
     * @return Recycler view, ready to render
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mIngredientListRecyclerView = (RecyclerView) view;
            mIngredientListRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            updateUI();

        }
        return view;
    }

    /**
     * Updates the data of the view
     *
     * Method reloads in sequence the ingredient list and
     * based on ingredients the products from the database.
     * For the prices, it only provides a container, they
     * will be calculated in the ViewHolder.
     */
    private void updateUI(){
        MealPricer mealPricer = MealPricer.get(getActivity());

        mIngredients = mealPricer.getIngredients(mMealId);
        for (Ingredient ingredient: mIngredients){
            mProducts.add(mealPricer.getProduct(ingredient.getProductId()));
        }

        if(mAdapter == null){
            mAdapter = new IngredientRecyclerViewAdapter(mIngredients, mProducts, mPrices, mListener);
            mIngredientListRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setIngredientsProducts(mIngredients, mProducts);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * override onAttach to set listener
     *
     * This listener is currently not used. It is left in place as
     * it will be used in the next version to call an individual
     * ingredient edit activity similar to the product activity.
     * Currently, ingredient edits have to be done in the ingredient
     * chooser activity/fragment.
     * @param context no specific use in override
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    /**
     * onResume override
     * <p/>
     * onResume triggers an update of the UI. This happens for example
     * also on rotation of the device.
     */
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    /**
     * onDetach override
     * <p/>
     * Removal of listeners.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * Not used in the current version.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction();
    }
}
