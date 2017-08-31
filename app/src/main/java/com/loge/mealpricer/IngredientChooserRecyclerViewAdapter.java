/*
 * IngredientChooserRecyclerViewAdapter
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


import java.util.List;
import java.util.UUID;

import static android.view.View.INVISIBLE;
import static com.loge.mealpricer.MeasureType.BOTH_VOLUME;
import static com.loge.mealpricer.MeasureType.BOTH_WEIGHT;
import static com.loge.mealpricer.MeasureType.NONE;

/**
 * RecyclerViewAdapter for the ingredient chooser fragment
 * <p/>
 * This class does the heavy lifting regarding GUI logic with user input
 * validation of the ingredient chooser. In this view, the user can choose
 * how much of which ingredient for the active meal. Selection is by
 * TextView's for the values and a checkbox to select an ingredient. This
 * class calls a number of text change listeners to synchronize GUI and
 * data objects.
 *
 */
public class IngredientChooserRecyclerViewAdapter extends RecyclerView.Adapter<IngredientChooserRecyclerViewAdapter.ViewHolder> {

    private List<Product> mProducts;
    private List<Ingredient> mIngredients;

    /**
     * Constructor for View Adapter
     * <p/>
     * The main constructor assigns the data to internal variables.
     * @param products list of products, provided by the fragment
     * @param ingredients list of ingredients, provided by the fragment
     */
    public IngredientChooserRecyclerViewAdapter(List<Product> products, List<Ingredient> ingredients) {
        mProducts = products;
        mIngredients = ingredients;
    }

    /**
     * OnCreateViewHolder override
     * <p/>
     * This method inflates the layout of each individual data row
     * in the recycler view.
     *
     * @param parent The parent view here is the recycler view
     * @param viewType not used here as only one type implemented
     * @return ViewHolder to be shown on the recycler view
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient_chooser_list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * onBindViewHolder override
     * <p/>
     * This method contains the logic interpreting the MeasureType of
     * ingredients into how the GUI/layout should be adapted
     *
     * @param holder ViewHolder where the item shall be bound to
     * @param position the number of the current item on the recycler view
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mWeightEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.mWeightEditTextListener.setOverride(true);
        holder.mVolumeEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.mVolumeEditTextListener.setOverride(true);
        holder.mSelectedCheckBoxListener.updatePosition(holder.getAdapterPosition());
        holder.mSelectedCheckBoxListener.setOverride(true);


        holder.mNameView.setText(mProducts.get(position).getName());
        int mAmount = mIngredients.get(position).getAmount();
        MeasureType mMeasureType = mIngredients.get(position).getMeasureType();

        if(mMeasureType == NONE){
            holder.mWeightView.setEnabled(false);
            holder.mVolumeView.setEnabled(false);
            holder.mSelectIngredient.setEnabled(true);
        }

        switch(mAmount){
            case 0:
                switch(mMeasureType){
                    case ONLY_WEIGHT:
                        holder.mWeightView.setText("");
                        holder.mTextInputLayoutVolume.setVisibility(INVISIBLE);
                        break;
                    case ONLY_VOLUME:
                        holder.mWeightView.setText("");
                        holder.mTextInputLayoutWeight.setVisibility(INVISIBLE);
                        break;
                    case BOTH_WEIGHT:
                        holder.mWeightView.setText("");
                        break;
                    case BOTH_VOLUME:
                        holder.mWeightView.setText("");
                        break;
                    default:
                        break;
                }
                break;
            default:
                switch(mMeasureType){
                    case ONLY_WEIGHT:
                        holder.mWeightView.setText(String.valueOf(mAmount));
                        holder.mTextInputLayoutVolume.setVisibility(INVISIBLE);
                        break;
                    case ONLY_VOLUME:
                        holder.mVolumeView.setText(String.valueOf(mAmount));
                        holder.mTextInputLayoutWeight.setVisibility(INVISIBLE);
                        break;
                    case BOTH_WEIGHT:
                        holder.mWeightView.setText(String.valueOf(mAmount));
                        break;
                    case BOTH_VOLUME:
                        holder.mVolumeView.setText(String.valueOf(mAmount));
                        break;
                    default:
                        break;
                }
                break;
        }


        if (mIngredients.get(position).getSelected()){
            holder.mSelectIngredient.setChecked(true);
        } else {
            holder.mSelectIngredient.setChecked(false);
        }


        holder.mWeightEditTextListener.setOverride(false);
        holder.mVolumeEditTextListener.setOverride(false);
        holder.mSelectedCheckBoxListener.setOverride(false);

    }

    /**
     * getItemCount override
     * <p/>
     * The item count is based on the number of products provided
     * by the fragment on instantiation.
     *
     * @return integer number of products
     */
    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    /**
     * Set or renew the data on which the view is based
     * <p/>
     * This method expects a consistent set of ingredients and products.
     * Currently there is no sanity check for this as it is only called
     * from one spot in the fragment.
     *
     * @param ingredients list of ingredients to be shown
     * @param products list of products that the ingredients are based on
     */
    public void setIngredientsProducts(List<Ingredient> ingredients, List<Product> products){
        mIngredients = ingredients;
        mProducts = products;
    }

    /**
     * ViewHolder that instantiates the view with all widgets
     * <p/>
     * Besides initializing and configuring the widgets into the
     * layout, the view holder also sets and defines callback
     * listeners for onFocus events. These affect only the gui,
     * not the underlying data structures. However, they can
     * fire TextChange listeners defined elsewhere which can
     * cause change to underlying data structures.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView;
        public final TextView mWeightView;
        public final TextInputLayout mTextInputLayoutWeight;
        public final TextView mVolumeView;
        public final TextInputLayout mTextInputLayoutVolume;
        public final CheckBox mSelectIngredient;
        public final WeightEditTextListener mWeightEditTextListener;
        public final VolumeEditTextListener mVolumeEditTextListener;
        public final SelectedCheckBoxListener mSelectedCheckBoxListener;


        /**
         * ViewHolder construct populates the layout with widgets
         * <p/>
         * The only configuration work done on the widgets are
         * two onFocus event listeners for the textView
         * boxes WeightView and VolumeView.
         *
         * @param view
         */
        public ViewHolder(View view) {
            super(view);
            mNameView = view.findViewById(R.id.product_name);

            mSelectIngredient = view.findViewById(R.id.select_ingredient);

            mWeightView = view.findViewById(R.id.product_weight);
            mTextInputLayoutWeight = view.findViewById(R.id.textInputLayout_weight);
            mVolumeView = view.findViewById(R.id.product_volume);
            mTextInputLayoutVolume = view.findViewById(R.id.textInputLayout_volume);

            mWeightEditTextListener = new WeightEditTextListener();
            mWeightEditTextListener.setCheckBox(mSelectIngredient);
            mWeightView.addTextChangedListener(mWeightEditTextListener);

            mVolumeEditTextListener = new VolumeEditTextListener();
            mVolumeEditTextListener.setCheckBox(mSelectIngredient);
            mVolumeView.addTextChangedListener(mVolumeEditTextListener);

            mSelectedCheckBoxListener = new SelectedCheckBoxListener();
            mSelectedCheckBoxListener.setWeightControl(mWeightView);
            mSelectedCheckBoxListener.setVolumeControl(mVolumeView);
            mSelectIngredient.setOnCheckedChangeListener(mSelectedCheckBoxListener);


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

    /**
     * Custom Listener Class for the ingredient selection checkbox
     * <p/>
     * The checkbox decides whether an ingredient shall be persistently
     * stored in the database. This listener is used to remove text in
     * the TextViews when the checkbox is unchecked. To use it, before
     * setting, the current ViewHolder position and references to the
     * respective TextViews has to be provided with the given methods.
     */
    private class SelectedCheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        private int mPosition;
        private TextView mWeight;
        private TextView mVolume;
        private boolean mOverride;

        /**
         * Setting the current ViewHolder position
         * <p/>
         * @param position integer current ViewHolder position
         */
        public void updatePosition(int position){this.mPosition = position; }

        /**
         * Method to provide access to the weight TextView
         * <p/>
         * @param weight reference to the weight TextView widget
         */
        public void setWeightControl(TextView weight){
            mWeight = weight;
        }

        /**
         * Method to provide access to the volume TextView
         * <p/>
         * @param volume reference to the volume TextView widget
         */
        public void setVolumeControl(TextView volume){
            mVolume = volume;
        }

        /**
         * Setting override for onCheckedChanged
         * <p/>
         * This method is used to override the listener
         * during binding the ViewHoler
         * @param override boolean to set override
         */
        public void setOverride(boolean override){
            mOverride = override;
        }

        /**
         * set the connected TextViews to an empty string
         * <p/>
         * Will check for isChecked and set, if false the weight and volume
         * TextView's to empty strings.
         * @param buttonView reference to the selected checkbox
         * @param isChecked current status of the checkbox
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(!mOverride){
                if (buttonView.isChecked()){
                    mIngredients.get(mPosition).setSelected(true);
                } else {
                    mIngredients.get(mPosition).setSelected(false);
                    mWeight.setText("");
                    mVolume.setText("");
                }
            }

        }
    }

    /**
     * Custom TextListener Class for the WeightEdit TextView Widget
     * <p/>
     * This class contains the functionality that will write user
     * input to the underlying data structures, interpreting also
     * the current configuration of the GUI.
     */
    private class WeightEditTextListener implements TextWatcher {
        private int mPosition;
        private CheckBox mSelected;
        private boolean mOverride;

        /**
         * Setting the current ViewHolder position
         * <p/>
         * @param position integer current ViewHolder position
         */
        public void updatePosition(int position) {
            this.mPosition = position;
        }

        /**
         * Setting override for onTextChage
         * <p/>
         * This method is used to override the listener
         * during binding the ViewHoler
         * @param override boolean to set override
         */
        public void setOverride(boolean override){
            mOverride = override;
        }

        /**
         * Method to provide access to selected checkbox
         * <p/>
         * @param selector reference to selector checkbox
         */
        public void setCheckBox(CheckBox selector){
            this.mSelected = selector;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        /**
         * onTextChanged override for WeightEdit TextView widget
         * <p/>
         * Method writes the the user entry to the underlying data structure.
         * It also adjusts the ingredients MeasureType accordingly.
         * @param charSequence The user input
         * @param start not used
         * @param before not used
         * @param count not used
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            if(!mOverride){
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

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // intentionally empty
        }
    }

    /**
     * Custom TextListener Class for the VolumeEdit TextView Widget
     * <p/>
     * This class contains the functionality that will write user
     * input to the underlying data structures, interpreting also
     * the current configuration of the GUI.
     */
    private class VolumeEditTextListener implements TextWatcher {
        private int mPosition;
        private CheckBox mSelected;
        private boolean mOverride;

        /**
         * Setting the current ViewHolder position
         * <p/>
         * @param position integer current ViewHolder position
         */
        public void updatePosition(int position) {
            this.mPosition = position;
        }

        /**
         * Setting override for onTextChage
         * <p/>
         * This method is used to override the listener
         * during binding the ViewHoler
         * @param override boolean to set override
         */
        public void setOverride(boolean override){
            mOverride = override;
        }

        /**
         * Method to provide access to selected checkbox
         * <p/>
         * @param selector reference to selector checkbox
         */
        public void setCheckBox(CheckBox selector){
            this.mSelected = selector;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
        }


        /**
         * onTextChanged override for VolumeEdit TextView widget
         * <p/>
         * Method writes the the user entry to the underlying data structure.
         * It also adjusts the ingredients MeasureType accordingly.
         * @param charSequence The user input
         * @param start not used
         * @param before not used
         * @param count not used
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            if(!mOverride){
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

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // intentionally empty
        }
    }
}
