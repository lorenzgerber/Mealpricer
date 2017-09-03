/*
 * ProductListFragment
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
 * A fragment that uses a recycler view to show all available products.
 * <p/>
 * This fragment is contained in the MealPricerTabActivity as the second tab
 * in the ViewPager.
 */
public class ProductListFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private RecyclerView mProductRecyclerView;
    private ProductRecyclerViewAdapter mAdapter;


    /**
     * Method to create new instance of ProductListFragment
     * <p/>
     * @return ProductListFragment instance
     */
    public static ProductListFragment newInstance() {
        return new ProductListFragment();
    }

    /**
     * onCreateView override
     * <p/>
     * Method that fetches the layout,  inflates it and
     * and installs the recycler view.
     *
     * @param inflater inflater instance
     * @param container to attach the view to
     * @param savedInstanceState bundle
     * @return view with recycler view of products
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mProductRecyclerView = (RecyclerView) view;
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            mProductRecyclerView.setLayoutManager(layoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                    mProductRecyclerView.getContext(),
                    layoutManager.getOrientation()
            );

            mProductRecyclerView.addItemDecoration(dividerItemDecoration);

            updateUI();

        }
        return view;
    }


    /**
     * Method to reload the data for UI
     * <p/>
     * This method reloads the data from the database
     * and notifies the adapter to fire an UI refresh
     */
    private void updateUI() {

        MealPricer mealPricer = MealPricer.get(getActivity());
        List<Product>  products = mealPricer.getProducts();
        if (mAdapter == null){
            mAdapter = new ProductRecyclerViewAdapter(products, mListener);
            mProductRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setProducts(products);
            mAdapter.notifyDataSetChanged();
        }

    }


    /**
     * Safety check on listener interface
     * <p/>
     * This method checks whether the OnListFragmentInteractionListener
     * interface has been implemented by the container activity.
     * @param context callers context
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
     * Method used to reload data and fire
     * UI refresh on orientation change.
     */
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    /**
     * onDetach override
     * <p/>
     * Method to detach the listener
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Interface for OnListFragmentInteracitonListener
     * <p/>
     * This interface needs to be implemented by containing activities.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Product item);
    }
}
