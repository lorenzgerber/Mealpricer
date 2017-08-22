package com.loge.mealpricer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loge.mealpricer.IngredientListFragment.OnListFragmentInteractionListener;
import com.loge.mealpricer.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder> {

    private final List<Ingredient> mIngredients;
    private final List<Product> mProducts;
    private final OnListFragmentInteractionListener mListener;

    public IngredientRecyclerViewAdapter(List<Ingredient> ingredients, List<Product> products, OnListFragmentInteractionListener listener) {
        mIngredients = ingredients;
        mProducts = products;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIngredientItem = mIngredients.get(position);
        holder.mProductItem = mProducts.get(position);

        holder.mNameView.setText(mProducts.get(position).getName());
        holder.mAmountView.setText(String.valueOf(mIngredients.get(position).getAmount()));
        int mType = mIngredients.get(position).getMeasureType();
        if (mType == 1 || mType == 3){
            holder.mTypeView.setText(R.string.gram_unit);
        } else {
            holder.mTypeView.setText(R.string.milli_liter_unit);
        }

        float mAmount = (float) mIngredients.get(position).getAmount();
        float mPrice = (float) mProducts.get(position).getPrice();

        float mPortion;
        if(mType == 1 || mType == 3){
            mPortion = (float) mProducts.get(position).getWeight();
        } else {
            mPortion = (float) mProducts.get(position).getVolume();
        }

        float result = mPrice/mPortion*mAmount;

        holder.mValueView.setText(String.valueOf((int) result) + " SEK");



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mIngredientItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mAmountView;
        public final TextView mTypeView;
        public final TextView mValueView;
        public Ingredient mIngredientItem;
        public Product mProductItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.ingredient_name);
            mAmountView = (TextView) view.findViewById(R.id.ingredient_amount);
            mTypeView = (TextView) view.findViewById(R.id.ingredient_amount_type);
            mValueView = (TextView) view.findViewById(R.id.ingredient_value);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }


}
