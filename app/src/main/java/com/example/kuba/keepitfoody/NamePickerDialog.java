package com.example.kuba.keepitfoody;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import java.util.ArrayList;

/**
 * This DialogFragment is used to set name to Meal type object.
 */
public class NamePickerDialog extends DialogFragment {

    private String name;
    private int icon;
    private Spinner spinner;
    private NamePickerDialogListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_name_picker, container, false);
        setCancelable(false);

        ArrayList<MealType> mealTypes = getMealTypes();
        MealTypeAdapter adapter = new MealTypeAdapter(getContext(), mealTypes);
        spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);


        ImageView confirmButton = view.findViewById(R.id.confirm);
        confirmButton.setOnClickListener(v -> {
            MealType item = (MealType) spinner.getSelectedItem();
            name = item.getName();
            icon = item.getIcon();
            onConfirmClicked(name, icon);
            this.dismiss();
        });
        ImageView dismissButton = view.findViewById(R.id.dismiss);
        dismissButton.setOnClickListener(v -> onConfirmClicked("cancel", icon));

        return view;
    }

    private void onConfirmClicked(String name, int icon) {
        if (listener != null) {
            listener.applyMealName(name, icon);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (NamePickerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement MealNameDialogListener");
        }

    }

    /**
     * Public interface used to pass data from NamePickerDialog
     * to CreateMealActivity.
     */
    public interface NamePickerDialogListener {
        void applyMealName(String name, int icon);
    }

    /**
     * Load all meal types and return as a list of objects.
     * @return A collection of type MealType
     */
    private ArrayList<MealType> getMealTypes() {
        ArrayList<MealType> mealTypes = new ArrayList<>();
        mealTypes.add(new MealType("Śniadanie", R.drawable.ic_vector_meal_view));
        mealTypes.add(new MealType("Drugie śniadanie", R.drawable.ic_vector_brunch));
        mealTypes.add(new MealType("Lunch", R.drawable.ic_vector_lunch));
        mealTypes.add(new MealType("Obiad", R.drawable.ic_vector_dinner));
        mealTypes.add(new MealType("Podwieczorek", R.drawable.ic_vector_tea));
        mealTypes.add(new MealType("Kolacja", R.drawable.ic_vector_supper));
        mealTypes.add(new MealType("Przekąska", R.drawable.ic_vector_snack));
        return mealTypes;
    }

}
