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


        ArrayAdapter<String> aa = new ArrayAdapter<>(holder.mSpinner.getContext(), android.R.layout.simple_spinner_item, mPortionsString);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mSpinner.setAdapter(aa);
        int mPortion = mMeals.get(position).getPortion();

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
        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MealPricer.get(holder.mImageButton.getContext()).deleteMeal(holder.mItem);

                int mPosition = holder.getAdapterPosition();
                File mPhotoFile = MealPricer.get(holder.mImageButton.getContext()).getPhotoFile(mMeals.get(mPosition));
                if(mPhotoFile != null || mPhotoFile.exists()){
                    mPhotoFile.delete();
                }

                mMeals.remove(mPosition);
                notifyItemRemoved(mPosition);
                notifyItemRangeChanged(mPosition, mMeals.size());
            }
        });


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

    private class CustomSpinnerListener implements AdapterView.OnItemSelectedListener {

        private int mPosition;
        private TextView mPriceView;

        private void updatePosition(int position){
            mPosition = position;
        }

        private void setPriceView(TextView priceView){
            mPriceView = priceView;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            float mResult = (float) mMeals.get(mPosition).getPrice() / ((float) mMeals.get(mPosition).getPortion() / (float) mPortionsInteger[position]);
            mPriceView.setText(String.valueOf((int) mResult));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}
