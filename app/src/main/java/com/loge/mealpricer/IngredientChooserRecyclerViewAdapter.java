package com.loge.mealpricer;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.loge.mealpricer.IngredientChooserFragment.OnListFragmentInteractionListener;


import java.util.List;
import java.util.UUID;

import static com.loge.mealpricer.Ingredient.MEASURE_TYPE_BOTH_VOLUME;
import static com.loge.mealpricer.Ingredient.MEASURE_TYPE_BOTH_WEIGHT;
import static com.loge.mealpricer.Ingredient.MEASURE_TYPE_NONE;
import static com.loge.mealpricer.Ingredient.MEASURE_TYPE_ONLY_VOLUME;
import static com.loge.mealpricer.Ingredient.MEASURE_TYPE_ONLY_WEIGHT;

public class IngredientChooserRecyclerViewAdapter extends RecyclerView.Adapter<IngredientChooserRecyclerViewAdapter.ViewHolder> {

    private List<Product> mProducts;
    private List<Ingredient> mIngredients;
    private UUID mMealId;
    private final OnListFragmentInteractionListener mListener;

    public IngredientChooserRecyclerViewAdapter(UUID mealId, List<Product> items, List<Ingredient> ingredients, OnListFragmentInteractionListener listener) {
        mProducts = items;
        mIngredients = ingredients;
        mMealId = mealId;

        mListener = listener;
        for (Product product:mProducts){
            Ingredient ingredient = new Ingredient(product);
            ingredient.setMeasureType(getMeasureType(product));
            ingredient.setMealId(mMealId);
            mIngredients.add(ingredient);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient_chooser_list_item, parent, false);
        return new ViewHolder(view, new WeightEditTextListener(), new VolumeEditTextListener(), new SelectedCheckBoxListener());
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mProducts.get(position);
        holder.mNameView.setText(mProducts.get(position).getName());
        holder.mWeightEditTextListener.updatePosition(position);
        holder.mVolumeEditTextListener.updatePosition(position);
        holder.mSelectedCheckBoxListener.updatePosition(position);

        if (mIngredients.get(position).getMeasureType() == MEASURE_TYPE_NONE){
            holder.mWeightView.setEnabled(false);
            holder.mVolumeView.setEnabled(false);
            holder.mSelectIngredient.setEnabled(false);
        } else if (mIngredients.get(position).getMeasureType() == MEASURE_TYPE_ONLY_WEIGHT){
            holder.mWeightView.setText(String.valueOf(mIngredients.get(position).getAmount()));
            holder.mVolumeView.setEnabled(false);
        } else if (mIngredients.get(position).getMeasureType() == MEASURE_TYPE_ONLY_VOLUME){
            holder.mVolumeView.setText(String.valueOf(mIngredients.get(position).getAmount()));
            holder.mWeightView.setEnabled(false);
        } else if (mIngredients.get(position).getMeasureType() == MEASURE_TYPE_BOTH_WEIGHT){
            holder.mWeightView.setText(String.valueOf(mIngredients.get(position).getAmount()));
            holder.mVolumeView.setText("0");
        } else if (mIngredients.get(position).getMeasureType() == MEASURE_TYPE_BOTH_VOLUME){
            holder.mWeightView.setText("0");
            holder.mVolumeView.setText(String.valueOf(mIngredients.get(position).getAmount()));
        }


        if (mIngredients.get(position).getSelected()){
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
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mWeightView;
        public final TextView mVolumeView;
        public final CheckBox mSelectIngredient;
        public WeightEditTextListener mWeightEditTextListener;
        public VolumeEditTextListener mVolumeEditTextListener;
        public SelectedCheckBoxListener mSelectedCheckBoxListener;
        public Product mItem;



        public ViewHolder(View view,
                          WeightEditTextListener weightEditTextListener,
                          VolumeEditTextListener volumeEditTextListener,
                          SelectedCheckBoxListener selectedCheckBoxListener) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.product_name);

            mWeightView = (TextView) view.findViewById(R.id.product_weight);
            mWeightEditTextListener = weightEditTextListener;
            mWeightView.addTextChangedListener(mWeightEditTextListener);

            mVolumeView = (TextView) view.findViewById(R.id.product_volume);
            mVolumeEditTextListener = volumeEditTextListener;
            mVolumeView.addTextChangedListener(mVolumeEditTextListener);

            mSelectIngredient = (CheckBox) view.findViewById(R.id.select_ingredient);
            mSelectedCheckBoxListener = selectedCheckBoxListener;
            mSelectIngredient.setOnCheckedChangeListener(mSelectedCheckBoxListener);


            mWeightView.setOnFocusChangeListener(new View.OnFocusChangeListener(){

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        mWeightView.setText("");
                        if(mVolumeView.isEnabled()){
                            mVolumeView.setText("0");
                        }
                    }
                }
            });

            mVolumeView.setOnFocusChangeListener(new View.OnFocusChangeListener(){

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        mVolumeView.setText("");
                        if(mWeightView.isEnabled()){
                            mWeightView.setText("0");
                        }
                    }
                }
            });


        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }

    private class SelectedCheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        private int mPosition;

        public void updatePosition(int position){this.mPosition = position; }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (buttonView.isChecked()){
                mIngredients.get(mPosition).setSelected(true);
            } else {
                mIngredients.get(mPosition).setSelected(false);
            }
        }
    }


    private class WeightEditTextListener implements TextWatcher {
        private int mPosition;

        public void updatePosition(int position) {
            this.mPosition = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            if(charSequence.length()==0){
                mIngredients.get(mPosition).setAmount(0);
            } else {
                mIngredients.get(mPosition).setAmount(Integer.parseInt(String.valueOf(charSequence)));
            }

            if(mIngredients.get(mPosition).getMeasureType() == 4){
                mIngredients.get(mPosition).setMeasureType(3);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    private class VolumeEditTextListener implements TextWatcher {
        private int mPosition;

        public void updatePosition(int position) {
            this.mPosition = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            if(charSequence.length()==0){
                mIngredients.get(mPosition).setAmount(0);
            } else {
                mIngredients.get(mPosition).setAmount(Integer.parseInt(String.valueOf(charSequence)));
            }

            if(mIngredients.get(mPosition).getMeasureType() == 3) {
                mIngredients.get(mPosition).setMeasureType(4);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    private int getMeasureType(Product product){
        if (product.getWeight() == 0 && product.getVolume() == 0){
            return MEASURE_TYPE_NONE;
        } else if (product.getWeight() > 0 && product.getVolume() == 0){
            return MEASURE_TYPE_ONLY_WEIGHT;
        } else if (product.getWeight() > 0 && product.getVolume() > 0){
            return MEASURE_TYPE_BOTH_WEIGHT;
        }
            return MEASURE_TYPE_ONLY_VOLUME;
    }


}
