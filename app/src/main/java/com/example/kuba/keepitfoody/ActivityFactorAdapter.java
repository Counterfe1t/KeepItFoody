package com.example.kuba.keepitfoody;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Custom ArrayAdapter of type ActivityFactor
 */
public class ActivityFactorAdapter extends ArrayAdapter<ActivityFactor> {

    public ActivityFactorAdapter(@NonNull Context context, ArrayList<ActivityFactor> activityFactors) {
        super(context, 0, activityFactors);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_activity_factor, parent, false);
        }

        TextView textName = convertView.findViewById(R.id.name);
        ActivityFactor item = getItem(position);

        if(item != null) {
            textName.setText(item.getName());
        }

        return convertView;
    }
}
