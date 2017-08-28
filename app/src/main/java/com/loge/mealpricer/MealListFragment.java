/*
 * MealListFragment
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


import java.util.List;

/**
 * A fragment that contains a recycler view
 * <p/>
 * This fragment is by default hosted ot the main tabbed activity
 * MeaPricerTabActivity which also implements the onListFragmentInteractionListener
 * interface. The fragment hosts a recycler view that shows a list of all
 * meals
 */
public class MealListFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private RecyclerView mMealRecyclerView;
    private MealRecyclerViewAdapter mAdapter;

    /**
     * Method to create new instance of fragment
     * <p/>
     * This method is usually called from the the hosting activity.
     * @return instance of MealListFragment
     */
    public static MealListFragment newInstance() {
        return new MealListFragment();
    }

    /**
     * onCreate override
     * <p/>
     * This method is used to install the recycler view on startup. It does
     * also some configuration on the recycler view (divider lines etc.)
     * @param inflater inflater instance used to inflate recycler view
     * @param container provides the ViewGroup where the recycler view shall be installed to
     * @param savedInstanceState not used in override
     * @return view to be rendered
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mMealRecyclerView = (RecyclerView) view;
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            mMealRecyclerView.setLayoutManager(layoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                    mMealRecyclerView.getContext(),
                    layoutManager.getOrientation()
            );

            mMealRecyclerView.addItemDecoration(dividerItemDecoration);

            updateUI();
        }
        return view;
    }

    /**
     * method that updates the UI
     *
     * This function gets the data from the database and
     * loads it onto the recycler view adapter.
     */
    private void updateUI() {

        MealPricer mealPricer = MealPricer.get(getActivity());
        List<Meal> meals = mealPricer.getMeals();

        for(Meal meal:meals){
            meal.setPrice(mealPricer.calcPriceMeal(meal.getMealId()));
        }

        if(mAdapter == null){
            mAdapter = new MealRecyclerViewAdapter(meals, mListener);
            mMealRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setMeals(meals);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * onAttach override
     * <p/>
     * This method checks that the calling activity implements the
     * OnListFragmentInteractionListener interface
     * @param context Context of the caller, usually the activity.
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
     * onDetach override
     * <p/>
     * Detaching the custom OnListFragmentInteractionListener
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * interface for Listener
     * <p/>
     * This needs to be impelemented by the activity that hosts this
     * fragment.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Meal item);
    }
}
