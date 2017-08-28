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


import java.util.ArrayList;
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

    private static final String ARG_PORTION_SELECTION = "com.loge.mealpricer.portion_selection";

    private OnListFragmentInteractionListener mListener;
    private RecyclerView mMealRecyclerView;
    private MealRecyclerViewAdapter mAdapter;
    private List<Meal> mMeals;
    private ArrayList<Integer> mPortionSelection;

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
     * onCreate Override
     * <p/>
     * Method that loads eventual stored bundle state arguments
     * when the fragment is created. Afterwards, all other data
     * is loaded using the reload method.
     * @param savedInstanceState bundle potentially loaded with arguments
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPortionSelection = new ArrayList<>();

        if (savedInstanceState != null) {
            mPortionSelection = savedInstanceState.getIntegerArrayList(ARG_PORTION_SELECTION);
        }

        reloadData();

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
     * Method that updates the UI
     *
     * This function either instantiates a new ViewAdpater and loads it
     * with data or if the adapter only exists, loads fresh data into it.
     */
    private void updateUI() {


        if(mAdapter == null){
            mAdapter = new MealRecyclerViewAdapter(mMeals,mPortionSelection, mListener);
            mMealRecyclerView.setAdapter(mAdapter);
        } else {
            reloadData();
            mAdapter.setMealsPortions(mMeals, mPortionSelection);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Reloads the meals and if needed the state of portion selection
     * <p/>
     * This method is used to obtain recent data. This is either onCreate,
     * on Resume or when coming back from orientation change
     */
    private void reloadData(){
        MealPricer mealPricer = MealPricer.get(getActivity());
        mMeals = mealPricer.getMeals();

        if(mPortionSelection.isEmpty() || mPortionSelection.size() != mMeals.size()){
            mPortionSelection = new ArrayList<>();
            for(Meal meal:mMeals) {
                meal.setPrice(mealPricer.calcPriceMeal(meal.getMealId()));
                mPortionSelection.add(meal.getPortion());
            }
        } else {
            for(Meal meal:mMeals) {
                meal.setPrice(mealPricer.calcPriceMeal(meal.getMealId()));
            }
        }
    }

    /**
     * onSavedInstanceState override
     * <p/>
     * Used to store the UI state of portion selection. This state is
     * only stored over orientation change, not when visiting another
     * activity because the portion stored with the respective ingredients
     * is considered the base state.
     * @param outState Bundle state to be saved
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(ARG_PORTION_SELECTION, mPortionSelection);
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
     * onResume override
     * <p/>
     * This override is used for when the user returns to this
     * fragment/activity by pressing the back button in the prior
     * activity. In this case data and UI has to be updated to
     * reflect eventual changes made in the prior activity.
     */
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
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
