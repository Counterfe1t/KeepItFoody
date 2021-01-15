package com.example.kuba.keepitfoody;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * This DialogFragment is used to set weight or quantity to Ingredient type object.
 */
public class SetQuantityDialog extends DialogFragment {

    private int quantity;
    private int ingredientId;
    private EditText editTextQuantity;

    private SetQuantityDialogListener listener;

    public static SetQuantityDialog newInstance(int ID) {
        SetQuantityDialog dialog = new SetQuantityDialog();
        Bundle args = new Bundle();
        args.putInt(MenuActivity.POSITION, ID);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.ingredientId = getArguments().getInt(MenuActivity.POSITION);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_set_quantity, container, false);
        setCancelable(false);


        editTextQuantity = view.findViewById(R.id.quantity);
        editTextQuantity.requestFocus();

        ImageView confirmButton = view.findViewById(R.id.confirm);
        confirmButton.setOnClickListener(v -> {

            if (validateQuantity()) {
                quantity = Integer.valueOf(editTextQuantity.getText().toString().trim());
                onConfirmClicked(quantity);
                this.dismiss();
            }


        });
        ImageView dismissButton = view.findViewById(R.id.dismiss);
        dismissButton.setOnClickListener(v -> {
            this.dismiss();
        });

        return view;
    }

    /**
     * Check if text field is not empty.
     * @return
     */
    private boolean validateQuantity() {
        String input = editTextQuantity.getText().toString().trim();

        try {
            if (input.isEmpty()) {
                editTextQuantity.setError("Pole nie może być puste.");
                return false;
            }

            if (Integer.valueOf(input).equals(0)) {
                editTextQuantity.setError("Waga składnika nie może być równa zero.");
                return false;
            }

        } catch (NumberFormatException e) {
            editTextQuantity.setError("Waga składnika jest za duża.");
            return false;
        }

        return true;
    }

    private void onConfirmClicked(int quantity) {
        if (listener != null) {
            listener.applyIngredientQuantity(quantity, ingredientId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SetQuantityDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement SetQuantityDialogListener");
        }

    }

    /**
     * Public interface used to communicate with SetQuantityDialog instance.
     */
    public interface SetQuantityDialogListener {
        void applyIngredientQuantity(int quantity, int ingredientId);
    }
}
