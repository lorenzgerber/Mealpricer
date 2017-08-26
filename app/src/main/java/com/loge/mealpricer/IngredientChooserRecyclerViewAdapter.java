package com.loge.mealpricer;

import android.support.design.widget.TextInputLayout;
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

import static android.view.View.INVISIBLE;
import static com.loge.mealpricer.Ingredient.MEASURE_TYPE_BOTH_VOLUME;
import static com.loge.mealpricer.Ingredient.MEASURE_TYPE_BOTH_WEIGHT;
import static com.loge.mealpricer.Ingredient.MEASURE_TYPE_NONE;
import static com.loge.mealpricer.Ingredient.MEASURE_TYPE_ONLY_VOLUME;
import static com.loge.mealpricer.Ingredient.MEASURE_TYPE_ONLY_WEIGHT;
import static com.loge.mealpricer.MeasureType.BOTH_VOLUME;
import static com.loge.mealpricer.MeasureType.BOTH_WEIGHT;
import static com.loge.mealpricer.MeasureType.NONE;
import static com.loge.mealpricer.MeasureType.ONLY_VOLUME;
import static com.loge.mealpricer.MeasureType.ONLY_WEIGHT;

public class IngredientChooserRecyclerViewAdapter extends RecyclerView.Adapter<IngredientChooserRecyclerViewAdapter.ViewHolder> {

    private List<Product> mProducts;
    private List<Ingredient> mIngredients;
    private final OnListFragmentInteractionListener mListener;

    public IngredientChooserRecyclerViewAdapter(List<Product> items, List<Ingredient> ingredients, OnListFragmentInteractionListener listener) {
        mProducts = items;
        mIngredients = ingredients;
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
        holder.mItem = mProducts.get(position);
        holder.mNameView.setText(mProducts.get(position).getName());



        int mAmount = mIngredients.get(position).getAmount();

        if (mIngredients.get(position).getMeasureType() == NONE){
            holder.mWeightView.setEnabled(false);
            holder.mVolumeView.setEnabled(false);
            holder.mSelectIngredient.setEnabled(true);
        } else if (mIngredients.get(position).getMeasureType() == ONLY_WEIGHT){
            if(mAmount == 0){
                holder.mWeightView.setText("");
            } else {
                holder.mWeightView.setText(String.valueOf(mAmount));
            }
            holder.mTextInputLayoutVolume.setVisibility(INVISIBLE);

        } else if (mIngredients.get(position).getMeasureType() == ONLY_VOLUME){
            if(mAmount == 0){
                holder.mWeightView.setText("");
            } else {
                holder.mVolumeView.setText(String.valueOf(mAmount));
            }
            holder.mTextInputLayoutWeight.setVisibility(INVISIBLE);

        } else if (mIngredients.get(position).getMeasureType() == BOTH_WEIGHT){
            if(mAmount == 0){
                holder.mWeightView.setText("");
            } else {
                holder.mWeightView.setText(String.valueOf(mAmount));
            }

        } else if (mIngredients.get(position).getMeasureType() == BOTH_VOLUME){
            if(mAmount == 0){
                holder.mWeightView.setText("");
            } else {
                holder.mVolumeView.setText(String.valueOf(mAmount));
            }

        }

        if (mIngredients.get(position).getSelected()){
            holder.mSelectIngredient.setChecked(true);
        } else {
            holder.mSelectIngredient.setChecked(false);
        }

        WeightEditTextListener weightListener = new WeightEditTextListener();
        weightListener.updatePosition(position);
        weightListener.setCheckBox(holder.mSelectIngredient);
        holder.mWeightView.addTextChangedListener(weightListener);

        VolumeEditTextListener volumeListener = new VolumeEditTextListener();
        volumeListener.updatePosition(position);
        volumeListener.setCheckBox(holder.mSelectIngredient);
        holder.mVolumeView.addTextChangedListener(volumeListener);

        SelectedCheckBoxListener selectListener = new SelectedCheckBoxListener();
        selectListener.updatePosition(position);
        selectListener.setWeightControl(holder.mWeightView);
        selectListener.setVolumeControl(holder.mVolumeView);
        holder.mSelectIngredient.setOnCheckedChangeListener(selectListener);



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public void setIngredientsProducts(List<Ingredient> ingredients, List<Product> products){
        mIngredients = ingredients;
        mProducts = products;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mWeightView;
        public final TextInputLayout mTextInputLayoutWeight;
        public final TextView mVolumeView;
        public final TextInputLayout mTextInputLayoutVolume;
        public final CheckBox mSelectIngredient;
        public Product mItem;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.product_name);

            mSelectIngredient = view.findViewById(R.id.select_ingredient);

            mWeightView = view.findViewById(R.id.product_weight);
            mTextInputLayoutWeight = view.findViewById(R.id.textInputLayout_weight);
            mVolumeView = view.findViewById(R.id.product_volume);
            mTextInputLayoutVolume = view.findViewById(R.id.textInputLayout_volume);

            mWeightView.setOnFocusChangeListener(new View.OnFocusChangeListener(){

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        if(mWeightView.getText().toString().equals("0")){
                            mWeightView.setText("");
                        }
                        if(mVolumeView.isEnabled()){
                            mVolumeView.setText("");
                        }
                    }
                }
            });

            mVolumeView.setOnFocusChangeListener(new View.OnFocusChangeListener(){

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        if(mVolumeView.getText().toString().equals("0")){
                            mVolumeView.setText("");
                        }

                        if(mWeightView.isEnabled()){
                            mWeightView.setText("");
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
        private TextView mWeight;
        private TextView mVolume;

        public void updatePosition(int position){this.mPosition = position; }

        public void setWeightControl(TextView weight){
            mWeight = weight;
        }

        public void setVolumeControl(TextView volume){
            mVolume = volume;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (buttonView.isChecked()){
                mIngredients.get(mPosition).setSelected(true);
            } else {
                mIngredients.get(mPosition).setSelected(false);
                mWeight.setText("");
                mVolume.setText("");
            }
        }
    }


    private class WeightEditTextListener implements TextWatcher {
        private int mPosition;
        private CheckBox mSelected;


        public void updatePosition(int position) {
            this.mPosition = position;
        }

        public void setCheckBox(CheckBox selector){
            this.mSelected = selector;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            if(charSequence.length()!=0){
                mIngredients.get(mPosition).setAmount(Integer.parseInt(String.valueOf(charSequence)));
                mIngredients.get(mPosition).setSelected(true);
                mSelected.setChecked(true);
            } else {
                mIngredients.get(mPosition).setAmount(0);
            }

            if(mIngredients.get(mPosition).getMeasureType() == BOTH_VOLUME){
                mIngredients.get(mPosition).setMeasureType(BOTH_WEIGHT);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    private class VolumeEditTextListener implements TextWatcher {
        private int mPosition;
        private CheckBox mSelected;

        public void updatePosition(int position) {
            this.mPosition = position;
        }

        public void setCheckBox(CheckBox selector){
            this.mSelected = selector;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            if(charSequence.length()!=0){
                mIngredients.get(mPosition).setAmount(Integer.parseInt(String.valueOf(charSequence)));
                mIngredients.get(mPosition).setSelected(true);
                mSelected.setChecked(true);
            } else {
                mIngredients.get(mPosition).setAmount(0);
            }

            if(mIngredients.get(mPosition).getMeasureType() == BOTH_WEIGHT) {
                mIngredients.get(mPosition).setMeasureType(BOTH_VOLUME);
            }



        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

}
