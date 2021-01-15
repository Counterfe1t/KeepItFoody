package com.example.kuba.keepitfoody;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Activity used to display basic information and a collection
 * of Ingredient type objects for specified meal.
 */
public class MealDetailsActivity extends AppCompatActivity {

    private Meal meal;
    private IngredientAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        Intent intent = getIntent();
        setMeal(intent.getParcelableExtra(MenuActivity.MEAL));
        ArrayList<Ingredient> ingredients = meal.getIngredients();

        adapter = new IngredientAdapter(this, ingredients, 2);
        adapter.setOnItemClickListener(position -> {
            IngredientDialog dialog = IngredientDialog.newInstance(ingredients.get(position));
            dialog.show(getSupportFragmentManager(), MenuActivity.INGREDIENT);
        });
        recyclerView = findViewById(R.id.recyclerViewIngredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(25));
        recyclerView.setAdapter(adapter);

        TextView textViewName = findViewById(R.id.mealName);
        TextView textViewDateTime = findViewById(R.id.mealTime);
        ImageView imageViewIcon = findViewById(R.id.mealIcon);

        textViewName.setText(meal.getName());
        textViewDateTime.setText(meal.getTime());
        imageViewIcon.setImageDrawable(this.getResources().getDrawable((meal.getIcon())));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }


}
