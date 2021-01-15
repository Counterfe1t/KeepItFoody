package com.example.kuba.keepitfoody;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Activity used to perform technical tasks such as database synchronization.
 */
public class SettingsFragment extends Fragment {

    private OnSettingsInteractionListener listener;

    /**
     * This interface must be implemented in main activity
     * in order to receive and send back USER type object.
     */
    public interface OnSettingsInteractionListener {
        void onSettingsInteraction();
    }

    public void syncDatabase() {
        if (listener != null) {
            listener.onSettingsInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsInteractionListener) {
            listener = (OnSettingsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnSettingsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        if(getActivity() != null) {
        }

        Button syncDatabaseButton = view.findViewById(R.id.syncDatabase);
        syncDatabaseButton.setOnClickListener(v -> syncDatabase());

        Button testButton = view.findViewById(R.id.testButton);
        testButton.setVisibility(View.GONE);
        testButton.setOnClickListener(v -> {
            // do something...
        });

        return view;
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
}
