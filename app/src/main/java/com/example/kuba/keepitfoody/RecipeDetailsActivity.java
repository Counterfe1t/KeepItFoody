package com.example.kuba.keepitfoody;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.operator.IntGenerate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Activity used to display basic information and a collection
 * of Ingredient type objects for specified meal.
 */
public class RecipeDetailsActivity extends AppCompatActivity {

    private int recipeId;
    private Recipe recipe;
    private ArrayList<Ingredient> ingredients;
    private RecyclerView recyclerView;
    private IngredientAdapter adapter;
    private FoodyDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        helper = FoodyDatabaseHelper.getInstance(this);

        // Get recipe from intent extra
        Intent intent = getIntent();
        recipeId = Integer.valueOf(intent.getStringExtra(MenuActivity.RECIPE));
        recipe = helper.fetchRecipe(recipeId);
        recipe.setIngredients(helper.fetchRecipeIngredients(recipe.getID()));
        ingredients = recipe.getIngredients();

        // Setup layout
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        TextView textViewName = findViewById(R.id.recipeName);
        TextView textViewTime = findViewById(R.id.minutes);
        TextView textViewDifficulty = findViewById(R.id.difficulty);
        TextView textViewDescription = findViewById(R.id.description);
        TextView textViewAuthorName = findViewById(R.id.authorName);
        ImageView recipeImage = findViewById(R.id.recipeImage);
        ImageView authorImage = findViewById(R.id.authorImage);

        nestedScrollView.getParent().requestChildFocus(nestedScrollView, nestedScrollView);
        nestedScrollView.fullScroll(View.FOCUS_UP);

        String imageUrl = "https://192.168.134.62/api" + recipe.getImage();
        Picasso.with(this).load(imageUrl).fit().centerInside().into(recipeImage);

        imageUrl = "https://192.168.134.62/api" + recipe.getAuthorImage();
        Picasso.with(this).load(imageUrl).fit().centerInside().into(authorImage);

        textViewName.setText(recipe.getName());
        textViewName.requestFocus();
        textViewAuthorName.setText(recipe.getAuthorFirstName());
        textViewTime.setText(String.valueOf(recipe.getPreparationTime()));
        textViewDifficulty.setText(String.valueOf(recipe.getDifficulty()));
        textViewDescription.setText(recipe.getDescription());

        switch (recipe.getDifficulty()) {
            case 0:
            case 1:
                textViewDifficulty.setText("Bardzo łatwy");
                break;
            case 2:
                textViewDifficulty.setText("Łatwy");
                break;
            case 3:
                textViewDifficulty.setText("Średni");
                break;
            case 4:
                textViewDifficulty.setText("Trudny");
                break;
            default:
                textViewDifficulty.setText("Bardzo trudny");
        }

        adapter = new IngredientAdapter(this, ingredients, 2);
        adapter.setOnItemClickListener(position -> {
            IngredientDialog dialog = IngredientDialog.newInstance(ingredients.get(position));
            dialog.show(getSupportFragmentManager(), MenuActivity.INGREDIENT);
        });
        recyclerView = findViewById(R.id.recyclerViewIngredients);
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
