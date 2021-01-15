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
 * Custom ArrayAdapter of type ActivityFactor
 */
public class MealTypeAdapter extends ArrayAdapter<MealType> {


    public MealTypeAdapter(@NonNull Context context, ArrayList<MealType> mealTypes) {
        super(context, 0, mealTypes);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_meal_type, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.icon);
        TextView name = convertView.findViewById(R.id.name);
        MealType item = getItem(position);

        if(item != null) {
            icon.setImageDrawable(getContext().getResources().getDrawable((item.getIcon())));
            name.setText(item.getName());
        }

        return convertView;
    }
}
