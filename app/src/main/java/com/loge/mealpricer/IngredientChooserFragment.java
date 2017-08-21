package com.loge.mealpricer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class IngredientChooserFragment extends Fragment {


    private OnListFragmentInteractionListener mListener;
    private RecyclerView mIngredientChooserRecyclerView;
    private IngredientChooserRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientChooserFragment() {
    }

    public static IngredientChooserFragment newInstance(){
        IngredientChooserFragment fragment = new IngredientChooserFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_chooser_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mIngredientChooserRecyclerView = (RecyclerView) view;
            mIngredientChooserRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            updateUI();

        }
        return view;
    }

    private void updateUI(){
        MealPricer mealPricer = MealPricer.get(getActivity());
        List<Product> products = mealPricer.getProducts();

        if(mAdapter == null){
            mAdapter = new IngredientChooserRecyclerViewAdapter(products, mListener);
            mIngredientChooserRecyclerView.setAdapter(mAdapter);
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Product item);
    }
}
