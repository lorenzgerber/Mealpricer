/*
 * ProductRecyclerViewAdapter
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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loge.mealpricer.ProductListFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * RecyclerViewAdapter for Products
 * <p/>
 * Used in ProductListFragment to present all available
 * products as a list. An OnListFragmentInteractionListener
 * enables navigation to the edit view of an individual
 * product.
 */
public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

    private List<Product> mProducts;
    private final OnListFragmentInteractionListener mListener;

    /**
     * Default Constructor
     * <p/>
     * Assigns arguments to the class variables.
     * @param products List of Products
     * @param listener recyler view item listener
     */
    public ProductRecyclerViewAdapter(List<Product> products, OnListFragmentInteractionListener listener) {
        mProducts = products;
        mListener = listener;
    }

    /**
     * onCreateViewHolder override
     * <p/>
     * This method assigns the layout for individual recycler view rows
     * @param parent recycler view
     * @param viewType not used
     * @return view of one recycler view column
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_product_list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * onBindViewHolder override
     * <p/>
     * Sets up the widgets, loads the data to them and attaches listener.
     * @param holder ViewHolder
     * @param position sequence number of ViewHolder in recycler view
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mProducts.get(position);
        holder.mNameView.setText(mProducts.get(position).getName());
        holder.mPriceView.setText(String.valueOf(mProducts.get(position).getPrice()));
        holder.mWeightView.setText(String.valueOf(mProducts.get(position).getWeight()));
        holder.mVolumeView.setText(String.valueOf(mProducts.get(position).getVolume()));

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

    /**
     * getItemCount override
     * <p/>
     * Gets size of Product List
     * @return length of list
     */
    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    /**
     * Method to set/refresh the data
     * <p/>
     * @param products List of products
     */
    public void setProducts(List<Product> products){
        mProducts = products;
    }

    /**
     * Product ViewHolder
     * <p/>
     * This class represents the row of a recycler view.
     * It consists of four TextView widgets.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mPriceView;
        public final TextView mWeightView;
        public final TextView mVolumeView;
        public Product mItem;

        /**
         * Default constructor
         * <p/>
         * Attaches the widgets to the provided view.
         * @param view ViewHolder of Product list recycler view
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.product_name);
            mPriceView = view.findViewById(R.id.product_price);
            mWeightView = view.findViewById(R.id.product_weight);
            mVolumeView = view.findViewById(R.id.product_volume);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
