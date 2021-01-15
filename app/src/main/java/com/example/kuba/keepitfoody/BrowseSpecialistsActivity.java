package com.example.kuba.keepitfoody;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Activity used to display a collection of typ Specialists.
 * You can contact registered specialists via email.
 */
public class BrowseSpecialistsActivity extends AppCompatActivity {

    private ArrayList<Specialist> specialists;
    private RecyclerView recyclerView;
    private SpecialistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_specialists);
        setTitle("Specjaliści");

        specialists = FoodyDatabaseHelper.getInstance(this).fetchSpecialists();

        adapter = new SpecialistAdapter(this, specialists);
        adapter.setOnItemClickListener(position -> {
            Specialist specialist = specialists.get(position);
            new SpecialistContactBackground(this, specialist.getID()).parseJSON();

        });
        recyclerView = findViewById(R.id.recyclerViewSpecialists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(25));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
