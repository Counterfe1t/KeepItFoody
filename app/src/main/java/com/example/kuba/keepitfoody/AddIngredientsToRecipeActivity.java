package com.example.kuba.keepitfoody;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Activity used to add ingredients while creating a recipe.
 */
public class AddIngredientsToRecipeActivity extends AppCompatActivity implements SetQuantityDialog.SetQuantityDialogListener, SearchView.OnQueryTextListener {

    private ArrayList<Ingredient> ingredients;
    private ArrayList<Ingredient> ingredientsDisplay;
    private ArrayList<Ingredient> newIngredients;
    private RecyclerView recyclerView;
    private IngredientAdapter adapter;

    private FoodyDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Wybierz składniki");
        setContentView(R.layout.activity_add_ingredients_to_recipe);

        helper = FoodyDatabaseHelper.getInstance(this);
        ingredients = helper.fetchIngredients();
        ingredientsDisplay = ingredients;
        newIngredients = new ArrayList<>();

        adapter = new IngredientAdapter(this, ingredientsDisplay, 4);
        adapter.setOnItemClickListener(position -> {
            Ingredient ingredient = ingredientsDisplay.get(position);
            openDialog(ingredient.getID());
        });

        recyclerView = findViewById(R.id.recyclerViewIngredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(25));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_recipe_add_ingredients_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchAction);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Szukaj");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirmAction:

                Intent resultIntent = new Intent();
                resultIntent.putParcelableArrayListExtra(MenuActivity.INGREDIENT, newIngredients);
                setResult(RESULT_OK, resultIntent);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        ingredientsDisplay = new ArrayList<>();

        if (query == null || query.length() == 0) {
            ingredientsDisplay.addAll(ingredients);
        }
        else {
            query = query.toLowerCase().trim();

            for (Ingredient ingredient : ingredients) {
                if (ingredient.getName().toLowerCase().contains(query)) {
                    ingredientsDisplay.add(ingredient);
                }
            }

        }

        adapter = new IngredientAdapter(this, ingredientsDisplay, 4);
        adapter.setOnItemClickListener(position -> {
            Ingredient ingredient = ingredientsDisplay.get(position);
            openDialog(ingredient.getID());
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    /**
     * Opens SetQuantityDialog to set weight to ingredient by id
     * @param ingredientId
     */
    public void openDialog(int ingredientId) {
        SetQuantityDialog dialog = SetQuantityDialog.newInstance(ingredientId);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    /**
     * After setting quantity add ingredient to the list.
     * Merge if ingredient already exists.
     * @param quantity
     * @param ingredientId
     */
    @Override
    public void applyIngredientQuantity(int quantity, int ingredientId) {
        Ingredient ingredient = null;
        for (Ingredient item : newIngredients) {
            if(item.getID() == ingredientId) {
                ingredient = item;
                ingredient.setQuantity(quantity + item.getQuantity());
                newIngredients.remove(item);
                break;
            }
        }

        if (ingredient == null) {
            for (Ingredient item : ingredients) {
                if (item.getID() == ingredientId) {
                    ingredient = item;
                    break;
                }
            }
            ingredient.setQuantity(quantity);
        }

        Toast.makeText(this, "Dodano składnik.", Toast.LENGTH_SHORT).show();
        newIngredients.add(ingredient);
    }

}
