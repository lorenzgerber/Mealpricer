package com.loge.mealpricer;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import static com.loge.mealpricer.MeasureType.BOTH_VOLUME;
import static com.loge.mealpricer.MeasureType.BOTH_WEIGHT;

/**
 * Helper Class to Collect TextWatchers
 * <p/>
 * This class serves as a container for all
 * TextWatcher inner classes for the IngredientChooserRecyclerViewAdapter.
 */
public class IngredientChooserListenersAndTextWatchers {

    /**
     * Custom TextListener Class for the WeightEdit TextView Widget
     * <p/>
     * This class contains the functionality that will write user
     * input to the underlying data structures, interpreting also
     * the current configuration of the GUI.
     */
    public static class WeightEditTextListener implements TextWatcher {
        private int mPosition;
        private CheckBox mSelected;
        private boolean mOverride;
        private List<Ingredient> mIngredients;

        public WeightEditTextListener(List<Ingredient> ingredients){
            mIngredients = ingredients;
        }

        /**
         * Setting the current ViewHolder position
         * <p/>
         * @param position integer current ViewHolder position
         */
        public void updatePosition(int position) {
            this.mPosition = position;
        }

        /**
         * Setting override for onTextChange
         * <p/>
         * This method is used to override the listener
         * during binding the ViewHolder
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
    public static class VolumeEditTextListener implements TextWatcher {
        private int mPosition;
        private CheckBox mSelected;
        private boolean mOverride;
        private List<Ingredient> mIngredients;

        public VolumeEditTextListener(List<Ingredient> ingredients){
            mIngredients = ingredients;
        }

        /**
         * Setting the current ViewHolder position
         * <p/>
         * @param position integer current ViewHolder position
         */
        public void updatePosition(int position) {
            this.mPosition = position;
        }

        /**
         * Setting override for onTextChange
         * <p/>
         * This method is used to override the listener
         * during binding the ViewHolder
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

    /**
     * Custom Listener Class for the ingredient selection checkbox
     * <p/>
     * The checkbox decides whether an ingredient shall be persistently
     * stored in the database. This listener is used to remove text in
     * the TextViews when the checkbox is unchecked. To use it, before
     * setting, the current ViewHolder position and references to the
     * respective TextViews has to be provided with the given methods.
     */
    public static class SelectedCheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        private int mPosition;
        private TextView mWeight;
        private TextView mVolume;
        private boolean mOverride;
        private List<Ingredient> mIngredients;

        public SelectedCheckBoxListener(List<Ingredient> ingredients){
            mIngredients = ingredients;
        }

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
         * during binding the ViewHolder
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
}
