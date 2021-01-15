package com.example.kuba.keepitfoody;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This dialog fragment is used to display nutrition
 * detail of specified ingredient.
 */
public class IngredientDialog extends DialogFragment {

    private static final String TAG = "ingredients";

    private Ingredient ingredient;

    public static IngredientDialog newInstance(Ingredient ingredient) {
        IngredientDialog dialog = new IngredientDialog();
        Bundle args = new Bundle();
        args.putParcelable(TAG, ingredient);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ingredient = getArguments().getParcelable(TAG);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ingredient_details, container, false);

        TextView nameText = view.findViewById(R.id.name);
        TextView categoryText = view.findViewById(R.id.category);
        TextView energyText = view.findViewById(R.id.energy);
        TextView proteinText = view.findViewById(R.id.protein);
        TextView fatsText = view.findViewById(R.id.fats);
        TextView carbohydratesText = view.findViewById(R.id.carbohydrates);
        TextView fibreText = view.findViewById(R.id.fibre);
        TextView saltText = view.findViewById(R.id.salt);

        nameText.setText(ingredient.getName());
        categoryText.setText(ingredient.getCategory());
        energyText.setText(String.valueOf(BigDecimal.valueOf(ingredient.getEnergy()).setScale(0, RoundingMode.HALF_UP)));
        proteinText.setText(String.valueOf(BigDecimal.valueOf(ingredient.getProtein()).setScale(1, RoundingMode.HALF_UP)));
        fatsText.setText(String.valueOf(BigDecimal.valueOf(ingredient.getFats()).setScale(1, RoundingMode.HALF_UP)));
        carbohydratesText.setText(String.valueOf(BigDecimal.valueOf(ingredient.getCarbohydrates()).setScale(1, RoundingMode.HALF_UP)));
        fibreText.setText(String.valueOf(BigDecimal.valueOf(ingredient.getFibre()).setScale(1, RoundingMode.HALF_UP)));
        saltText.setText(String.valueOf(BigDecimal.valueOf(ingredient.getSalt()).setScale(1, RoundingMode.HALF_UP)));

        ImageView dismissButton = view.findViewById(R.id.dismissButton);
        dismissButton.setOnClickListener(v -> getDialog().dismiss());

        return view;
    }
}
