package com.loge.mealpricer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loge.mealpricer.dummy.DummyContent;
import com.loge.mealpricer.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class IngredientListFragment extends Fragment {

    public static final String ARG_MEAL_ID = "meal_id";

    private UUID mMealId;

    private RecyclerView mIngredientListRecyclerView;
    private IngredientRecyclerViewAdapter mAdapter;
    private List<Ingredient> mIngredients;
    private List<Product> mProducts;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientListFragment() {
    }

    public static IngredientListFragment newInstance(UUID mealId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEAL_ID, mealId);

        IngredientListFragment fragment = new IngredientListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMealId = (UUID) getArguments().getSerializable(ARG_MEAL_ID);

        mIngredients = new ArrayList<>();
        mProducts = new ArrayList<>();

        mIngredients = MealPricer.get(getActivity()).getIngredients(mMealId);
        if(mIngredients != null){
            for(Ingredient ingredient:mIngredients){
                mProducts.add(MealPricer.get(getActivity()).getProduct(ingredient.getProductId()));
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mIngredientListRecyclerView = (RecyclerView) view;
            mIngredientListRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            updateUI();

        }
        return view;
    }


    private void updateUI(){
        MealPricer mealPricer = MealPricer.get(getActivity());

        mIngredients = mealPricer.getIngredients(mMealId);
        for (Ingredient ingredient: mIngredients){
            mProducts.add(mealPricer.getProduct(ingredient.getProductId()));
        }

        if(mAdapter == null){
            mAdapter = new IngredientRecyclerViewAdapter(mIngredients, mProducts, mListener);
            mIngredientListRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setIngredientsProducts(mIngredients, mProducts);
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
    public void onResume(){
        super.onResume();
        updateUI();
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
        void onListFragmentInteraction(Ingredient item);
    }
}
