/*
 * IngredientRecyclerViewAdapter
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

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loge.mealpricer.IngredientListFragment.OnListFragmentInteractionListener;

import java.util.List;

import static com.loge.mealpricer.MeasureType.BOTH_WEIGHT;
import static com.loge.mealpricer.MeasureType.ONLY_WEIGHT;

/**
 * RecyclerViewAdapter for the MealList fragment
 *
 * This view is currently the main representation of the object of interest:
 * the meal with price information for all included ingredients. Currently
 * there is no interaction set on the default implemented recycler item listener.
 * It was not removed because in the next version, click on individual items
 * shall open a dedicated ingredient edit view which is not implemented yet.
 */
public class IngredientRecyclerViewAdapter
        extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder> {

    private  List<Ingredient> mIngredients;
    private  List<Product> mProducts;
    private final List<Integer> mPrices;
    private final OnListFragmentInteractionListener mListener;

    /**
     * Constructor for View Adapter
     *
     * The main constructor assigns the data to the internal variables. There
     * is currently no sanity check for the arguments implemented (i.e. same number
     * of list memebers).
     * @param ingredients list of ingredients for the meal shown
     * @param products list of products corresponding to the ingredients
     * @param prices list of integers with the calculated prices for each ingredident
     * @param listener currently unnused listener for interaction with individual
     *                 recycler view elements.
     */
    public IngredientRecyclerViewAdapter(List<Ingredient> ingredients,
                                         List<Product> products,
                                         List<Integer> prices,
                                         OnListFragmentInteractionListener listener) {
        mIngredients = ingredients;
        mProducts = products;
        mPrices = prices;
        mListener = listener;
    }

    /**
     * onCreateViewHolder override
     *
     * This method inflates the layout of each individual data row
     * in the recycler view
     * @param parent parent view, the recycler view
     * @param viewType not used
     * @return viewholder to be shown on the recycler view
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient_list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * onBindViewHolder override
     *
     * This method contains the logic for setting correct units in the gui.
     * @param holder ViewHolder where the item shall be bound to.
     * @param position integer number of the current item on the recycler view
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIngredientItem = mIngredients.get(position);

        holder.mNameView.setText(mProducts.get(position).getName());
        holder.mAmountView.setText(String.valueOf(mIngredients.get(position).getAmount()));
        MeasureType mType = mIngredients.get(position).getMeasureType();
        if (mType == ONLY_WEIGHT || mType == BOTH_WEIGHT){
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

    /**
     * getItemCount override
     *
     * The item count is based on the number of Ingredients
     * provided by the fragment on instantiation.
     * @return integer number of ingredients
     */
    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    /**
     * set or renew the data on which the view is based
     *
     * This method expects a consistent set of ingredients and products.
     * Currently there is no sanity check for this as it is only called
     * from one spot in the corresponding fragment.
     * @param ingredients list of ingredients to be shown
     * @param products list of products that the ingredient is based on
     */
    public void setIngredientsProducts(List<Ingredient> ingredients, List<Product> products){
        mIngredients = ingredients;
        mProducts = products;
    }

    /**
     * View holder Class, instantiates the view with all widgets
     *
     * The ViewHolder of the IngredientRecyclerViewAdapter has
     * currently only the function of setting up the widgets into
     * the view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mAmountView;
        public final TextView mTypeView;
        public final TextView mValueView;
        public Ingredient mIngredientItem;


        /**
         * Constructor for IngredientRecyclerViewAdapter ViewHolder
         *
         * Getting and setting up the widgets for an individual
         * datarow on the recycler view.
         * @param view The recycler view
         */
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
