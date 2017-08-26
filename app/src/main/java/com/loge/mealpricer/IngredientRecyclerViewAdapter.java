package com.loge.mealpricer;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loge.mealpricer.IngredientListFragment.OnListFragmentInteractionListener;

import java.util.List;

public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder> {

    private  List<Ingredient> mIngredients;
    private  List<Product> mProducts;
    private final List<Integer> mPrices;
    private final OnListFragmentInteractionListener mListener;

    public IngredientRecyclerViewAdapter(List<Ingredient> ingredients, List<Product> products, List<Integer> prices, OnListFragmentInteractionListener listener) {
        mIngredients = ingredients;
        mProducts = products;
        mPrices = prices;
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
        //holder.mProductItem = mProducts.get(position);

        holder.mNameView.setText(mProducts.get(position).getName());
        holder.mAmountView.setText(String.valueOf(mIngredients.get(position).getAmount()));
        int mType = mIngredients.get(position).getMeasureType();
        if (mType == 1 || mType == 3){
            holder.mTypeView.setText(R.string.gram_unit);
        } else {
            holder.mTypeView.setText(R.string.milli_liter_unit);
        }


        holder.mValueView.getResources().getString(R.string.price_sek, mPrices.get(position));
        Resources r = holder.mValueView.getResources();
        String mPrice = r.getString(R.string.price_sek, mPrices.get(position));
        holder.mValueView.setText(mPrice);



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public void setIngredientsProducts(List<Ingredient> ingredients, List<Product> products){
        mIngredients = ingredients;
        mProducts = products;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mAmountView;
        public final TextView mTypeView;
        public final TextView mValueView;
        public Ingredient mIngredientItem;
        //public Product mProductItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.ingredient_name);
            mAmountView = view.findViewById(R.id.ingredient_amount);
            mTypeView = view.findViewById(R.id.ingredient_amount_type);
            mValueView = view.findViewById(R.id.ingredient_value);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }


}
