package com.example.kuba.keepitfoody;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Custom ArrayAdapter of type IngredientCategory.
 */
public class IngredientCategoryAdapter extends ArrayAdapter<IngredientCategory> {

    public IngredientCategoryAdapter(@NonNull Context context, ArrayList<IngredientCategory> categories) {
        super(context, 0, categories);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_ingredient_category, parent, false);
        }

        TextView textName = convertView.findViewById(R.id.name);
        ImageView icon = convertView.findViewById(R.id.icon);
        IngredientCategory category = getItem(position);

        if(category != null) {
            textName.setText(category.getName());
            icon.setImageDrawable(getContext().getResources().getDrawable((category.getIcon())));
        }

        return convertView;
    }
}
