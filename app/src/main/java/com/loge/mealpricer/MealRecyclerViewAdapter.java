package com.loge.mealpricer;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loge.mealpricer.MealListFragment.OnListFragmentInteractionListener;

import java.util.List;


public class MealRecyclerViewAdapter extends RecyclerView.Adapter<MealRecyclerViewAdapter.ViewHolder> {

    private List<Meal> mMeals;
    private final OnListFragmentInteractionListener mListener;
    private static final String[] mPortionsString = {"1", "2", "4"};
    private static final Integer[] mPortionsInteger = {1, 2, 4};

    public MealRecyclerViewAdapter(List<Meal> items, OnListFragmentInteractionListener listener) {
        mMeals = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_meal_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mMeals.get(position);
        holder.mNameView.setText(mMeals.get(position).getName());
        holder.mPriceView.setText(String.valueOf(mMeals.get(position).getPrice()));
        holder.mPortionView.setText(String.valueOf(mMeals.get(position).getPortion()));

        ArrayAdapter<String> aa = new ArrayAdapter<String>(holder.mPortionView.getContext(), android.R.layout.simple_spinner_item, mPortionsString);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mSpinner.setAdapter(aa);
        

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMeals.size();
    }

    public void setMeals(List<Meal> meals){ mMeals = meals; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mPriceView;
        public final TextView mPortionView;
        public final AppCompatSpinner mSpinner;
        public Meal mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.meal_name);
            mPriceView = (TextView) view.findViewById(R.id.meal_price);
            mPortionView = (TextView) view.findViewById(R.id.meal_portion);
            mSpinner = (AppCompatSpinner) view.findViewById(R.id.spinner);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
