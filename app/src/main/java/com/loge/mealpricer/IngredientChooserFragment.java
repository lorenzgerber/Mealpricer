package com.loge.mealpricer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
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
    private List<Product> mProducts;
    private List<Ingredient> mIngredients;
    private boolean[] mSelected;


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

        MealPricer mealPricer = MealPricer.get(getActivity());
        mProducts = mealPricer.getProducts();
        mIngredients = new ArrayList<>();
        mSelected = new boolean[mProducts.size()];




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


        if(mAdapter == null){
            mAdapter = new IngredientChooserRecyclerViewAdapter(mProducts, mIngredients, mSelected, mListener);
            mIngredientChooserRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setProducts(mProducts);
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
