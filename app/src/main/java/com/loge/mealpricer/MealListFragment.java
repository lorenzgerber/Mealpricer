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
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MealListFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private RecyclerView mMealRecyclerView;
    private MealRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MealListFragment() {
    }

    public static MealListFragment newInstance() {
        return new MealListFragment();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Meal item);
    }
}
