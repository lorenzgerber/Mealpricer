/*
 * MealRecyclerViewAdapter
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

import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loge.mealpricer.MealListFragment.OnListFragmentInteractionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * MealRecyclerView adapter for the MealList fragment
 * <p/>
 * This recycler view provides the main information of the mealpricer app.
 * It shows all meals in the database with the respective price. On initial startup,
 * the portion values for which the ingredients are indicated are presented. Choosing
 * by the gui spinner a different portion only calculates a new value presented in the
 * gui but does not persist it in the database. However, for UI consistency, the values
 * are persisted in a fragment argument during, for example orientation change.
 */
public class MealRecyclerViewAdapter extends RecyclerView.Adapter<MealRecyclerViewAdapter.ViewHolder> {

    private List<Meal> mMeals;
    private ArrayList<Integer> mPortionSelection;
    private final OnListFragmentInteractionListener mListener;
    private static final String[] mPortionsString = {"1", "2", "4"};
    private static final Integer[] mPortionsInteger = {1, 2, 4};

    /**
     * Default constructor assigns data arguments to class variables
     * @param meals list of meals to be shown
     * @param portionSelection ArrayList of integers representing the portion selector gui state
     * @param listener item click interaction listener
     */
    public MealRecyclerViewAdapter(List<Meal> meals, ArrayList<Integer> portionSelection, OnListFragmentInteractionListener listener) {
        mMeals = meals;
        mPortionSelection = portionSelection;
        mListener = listener;
    }

    /**
     * onCreateViewHolder override
     * <p/>
     * Used to inflated the individual recycler view item layout
     * @param parent recycler view
     * @param viewType not used
     * @return view of an individual recycler view item
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_meal_list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * onBindViewHolder override
     * <p/>
     * This method configures the recycler view item widgets and loads them with data.
     * Further, the needed listeners are attached to the widgets. Here, a listener
     * for the spinner widget to choose meal portion calculation and the whole
     * item interaction listener for entering single meal edit activity are used.
     * @param holder viewHolder
     * @param position position of recycler view item
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mMeals.get(position);
        holder.mNameView.setText(mMeals.get(position).getName());
        holder.mPriceView.setText(String.valueOf(mMeals.get(position).getPrice()));


        ArrayAdapter<String> aa = new ArrayAdapter<>(holder.mSpinner.getContext(), android.R.layout.simple_spinner_item, mPortionsString);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mSpinner.setAdapter(aa);
        int mPortion = mPortionSelection.get(position);

        switch(mPortion) {
            case 1:
                holder.mSpinner.setSelection(0);
                break;
            case 2:
                holder.mSpinner.setSelection(1);
                break;
            case 4:
                holder.mSpinner.setSelection(2);
                break;
            default:
                holder.mSpinner.setSelection(0);
                break;
        }

        CustomSpinnerListener spinnerListener = new CustomSpinnerListener();
        spinnerListener.updatePosition(position);
        spinnerListener.setPriceView(holder.mPriceView);

        holder.mSpinner.setOnItemSelectedListener(spinnerListener);

        holder.mImageButton.setOnClickListener(new DeleteOnClickListener(holder));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * Custom onClick Listener
     * <p/>
     * OnClick listener that will delete a meal detail list  entry.
     * It takes the view holder as argument to act upon. This class
     * is specific to the MealRecyclerViewAdapter class.
     */
    private class DeleteOnClickListener implements View.OnClickListener{

        ViewHolder mHolder;

        /**
         * Constructor
         * <p/>
         * Default constructor for custom onClickListener
         * @param holder ViewHolder that contains the entry to be deleted
         */
        public DeleteOnClickListener(ViewHolder holder){
            mHolder = holder;
        }

        /**
         * onClickListener Implementation
         * <p/>
         * Specific method to be used within MealRecyclerViewAdapter class.
         * The OnClickListener implements the functionality to delete a
         * meal entry.
         * @param view
         */
        @Override
        public void onClick(View view) {
            MealPricer.get(mHolder.mImageButton.getContext()).deleteMeal(mHolder.mItem);

            int mPosition = mHolder.getAdapterPosition();
            File mPhotoFile = MealPricer.get(mHolder.mImageButton.getContext()).getPhotoFile(mMeals.get(mPosition));
            if(mPhotoFile != null || mPhotoFile.exists()){
                mPhotoFile.delete();
            }

            mMeals.remove(mPosition);
            mPortionSelection.remove(mPosition);
            notifyItemRemoved(mPosition);
            notifyItemRangeChanged(mPosition, mMeals.size());
        }
    }

    /**
     * getItemCount override
     * <p/>
     * Provides the current length of the main data object, the meal list.
     * @return integer number of meals.
     */
    @Override
    public int getItemCount() {
        return mMeals.size();
    }

    /**
     * Setting and refreshing the data containers
     * <p/>
     * This method is used to refresh the data. There
     * is no sanity check that the length of both
     * data items match.
     * @param meals list of meals
     * @param portions list of portions
     */
    public void setMealsPortions(List<Meal> meals, ArrayList<Integer> portions){
        mMeals = meals;
        mPortionSelection = portions;
    }

    /**
     * ViewHolder class
     * <p/>
     * Fetches and assigns all widgets to the view holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mPriceView;
        public final AppCompatSpinner mSpinner;
        public final ImageButton mImageButton;
        public Meal mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.meal_name);
            mPriceView = view.findViewById(R.id.meal_price);
            mSpinner = view.findViewById(R.id.portion_spinner);
            mImageButton = view.findViewById(R.id.delete_meal_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }

    /**
     * Meal Portion Spinner Listener Class
     * <p/>
     * Provides methods to provide the current RecyclerView item number
     * and a reference to the Price TextView so that it can be updated
     * when the spinner position is changed. Further the class also
     * updates the data structure which is used to persist the spinner
     * positions over orientation change.
     */
    private class CustomSpinnerListener implements AdapterView.OnItemSelectedListener {

        private int mPosition;
        private TextView mPriceView;

        /**
         * provide recycler view item number to which to attach the listener
         * @param position integer recycler view position
         */
        private void updatePosition(int position){
            mPosition = position;
        }

        /**
         * reference to the price TextView
         * @param priceView TextView widget
         */
        private void setPriceView(TextView priceView){
            mPriceView = priceView;
        }

        /**
         * onItemSelected override
         * <p/>
         * When a new spinner value is selected, a new price is calculated for the
         * according portion. Further, the data structure mPortionSelection, is updated
         * to keep the gui setting persistent during orientation changes.
         * @param parent parent of spinner
         * @param view Spinner view
         * @param position spinner position
         * @param id spinner value
         */
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            float mResult = (float) mMeals.get(mPosition).getPrice() / ((float) mMeals.get(mPosition).getPortion() / (float) mPortionsInteger[position]);
            mPriceView.setText(String.valueOf((int) mResult));
            mPortionSelection.set(mPosition, mPortionsInteger[position]);
        }

        /**
         * not implemented
         * @param parent not used
         */
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
