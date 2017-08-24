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
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ProductListFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private RecyclerView mProductRecyclerView;
    private DividerItemDecoration mDividerItemDecoration;
    private LinearLayoutManager mLayoutManager;
    private ProductRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductListFragment() {
    }

    public static ProductListFragment newInstance() {
        ProductListFragment fragment = new ProductListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mProductRecyclerView = (RecyclerView) view;
            mLayoutManager = new LinearLayoutManager(context);
            mProductRecyclerView.setLayoutManager(mLayoutManager);

            mDividerItemDecoration = new DividerItemDecoration(
                    mProductRecyclerView.getContext(),
                    mLayoutManager.getOrientation()
            );

            mProductRecyclerView.addItemDecoration(mDividerItemDecoration);

            updateUI();

        }
        return view;
    }


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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Product item);
    }
}
