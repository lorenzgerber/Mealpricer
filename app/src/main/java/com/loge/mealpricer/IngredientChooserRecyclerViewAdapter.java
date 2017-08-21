package com.loge.mealpricer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.loge.mealpricer.IngredientChooserFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

import static com.loge.mealpricer.Ingredient.MEASURE_TYPE_WEIGHT;

public class IngredientChooserRecyclerViewAdapter extends RecyclerView.Adapter<IngredientChooserRecyclerViewAdapter.ViewHolder> {

    private List<Product> mProducts;
    private List<Ingredient> mIngredients;
    private boolean[] mSelected;
    private final OnListFragmentInteractionListener mListener;

    public IngredientChooserRecyclerViewAdapter(List<Product> items, OnListFragmentInteractionListener listener) {
        mProducts = items;
        mIngredients = new ArrayList<>();

        mListener = listener;
        for (Product product:mProducts){
            mIngredients.add(new Ingredient(product));
        }

        mSelected = new boolean[mProducts.size()];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient_chooser_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mProducts.get(position);
        holder.mNameView.setText(mProducts.get(position).getName());
        if (mIngredients.get(position).getMeasureType()== MEASURE_TYPE_WEIGHT ){
            holder.mWeightView.setText(String.valueOf(mIngredients.get(position).getAmount()));
            holder.mVolumeView.setText(R.string.not_used);
        } else {
            holder.mVolumeView.setText(String.valueOf(mIngredients.get(position).getAmount()));
            holder.mWeightView.setText(R.string.not_used);
        }

        if (mSelected[position]){
            holder.mSelectIngredient.setChecked(true);
        } else {
            holder.mSelectIngredient.setChecked(false);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public void setProducts(List<Product> products){
        mProducts = products;
        mIngredients = new ArrayList<>();
        for (Product product:mProducts){
            mIngredients.add(new Ingredient(product));
        }

        mSelected = new boolean[mProducts.size()];

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mWeightView;
        public final TextView mVolumeView;
        public final CheckBox mSelectIngredient;
        public Product mItem;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.product_name);
            mWeightView = (TextView) view.findViewById(R.id.product_weight);
            mVolumeView = (TextView) view.findViewById(R.id.product_volume);
            mSelectIngredient = (CheckBox) view.findViewById(R.id.select_ingredient);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
