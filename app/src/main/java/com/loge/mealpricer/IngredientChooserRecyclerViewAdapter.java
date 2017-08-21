package com.loge.mealpricer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.loge.mealpricer.IngredientChooserFragment.OnListFragmentInteractionListener;
import com.loge.mealpricer.dummy.DummyContent.DummyItem;

import java.util.List;

public class IngredientChooserRecyclerViewAdapter extends RecyclerView.Adapter<IngredientChooserRecyclerViewAdapter.ViewHolder> {

    private List<Product> mValues;
    private final OnListFragmentInteractionListener mListener;

    public IngredientChooserRecyclerViewAdapter(List<Product> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient_chooser_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());

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
        return mValues.size();
    }

    public void setProducts(List<Product> products){
        mValues = products;
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
