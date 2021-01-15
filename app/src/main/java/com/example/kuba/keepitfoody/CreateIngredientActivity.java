package com.example.kuba.keepitfoody;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Activity used to create new ingredient and send it to server for validation.
 */
public class CreateIngredientActivity extends AppCompatActivity {

    private EditText editIngredientName;
    private EditText editEnergy;
    private EditText editProtein;
    private EditText editFat;
    private EditText editCarbohydrates;
    private EditText editFibre;
    private EditText editSalt;
    private CheckBox checkBoxGluten;
    private CheckBox checkBoxLactose;
    private Spinner spinner;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ingredient);

        editIngredientName = findViewById(R.id.editIngredientName);
        editEnergy = findViewById(R.id.editEnergy);
        editProtein = findViewById(R.id.editProtein);
        editFat = findViewById(R.id.editFat);
        editCarbohydrates = findViewById(R.id.editCarbohydrates);
        editFibre = findViewById(R.id.editFibre);
        editSalt = findViewById(R.id.editSalt);
        checkBoxGluten = findViewById(R.id.gluten);
        checkBoxLactose = findViewById(R.id.lactose);

        ArrayList<IngredientCategory> categories = getIngredientCategories();
        IngredientCategoryAdapter adapter = new IngredientCategoryAdapter(this, categories);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_ingredient_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirmAction:

                if (validate()) {
                    Ingredient newIngredient = createIngredient();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MenuActivity.INGREDIENT, newIngredient);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<IngredientCategory> getIngredientCategories() {
        ArrayList<IngredientCategory> categories = new ArrayList<>();
        categories.add(new IngredientCategory("Mięso i przetwory mięsne", R.drawable.ic_meat));
        categories.add(new IngredientCategory("Warzywa i przetwory warzywne", R.drawable.ic_vegetables));
        categories.add(new IngredientCategory("Owoce i przetwory owocowe", R.drawable.ic_fruits));
        categories.add(new IngredientCategory("Nabiał i jaja", R.drawable.ic_dairy));
        categories.add(new IngredientCategory("Produkty zbożowe", R.drawable.ic_grain));
        categories.add(new IngredientCategory("Cukier i przetwory cukiernicze", R.drawable.ic_snacks));
        categories.add(new IngredientCategory("Tłuszcze", R.drawable.ic_fats));
        categories.add(new IngredientCategory("Ryby i przetwory rybne", R.drawable.ic_fish));
        categories.add(new IngredientCategory("Napoje", R.drawable.ic_beverages));
        categories.add(new IngredientCategory("Bakalie i nasiona", R.drawable.ic_nuts));
        categories.add(new IngredientCategory("Pozostałe", R.drawable.ic_recipe));
        categories.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        return categories;
    }

    /**
     * Create object of type Ingredient from entered values.
     * @return An object of type Ingredient
     */
    private Ingredient createIngredient() {
        Ingredient ingredient = new Ingredient();

        String name = editIngredientName.getText().toString().trim();
        IngredientCategory selectedItem = (IngredientCategory) spinner.getSelectedItem();
        String category = selectedItem.getName();
        int icon = selectedItem.getIcon();
        double energy = Double.valueOf(editEnergy.getText().toString().trim());
        double protein = Double.valueOf(editProtein.getText().toString().trim());
        double fats = Double.valueOf(editFat.getText().toString().trim());
        double carbohydrates = Double.valueOf(editCarbohydrates.getText().toString().trim());
        double fibre = Double.valueOf(editFibre.getText().toString().trim());
        double salt = Double.valueOf(editSalt.getText().toString().trim());

        ingredient.setName(name);
        ingredient.setCategory(category);
        ingredient.setIcon(icon);
        ingredient.setEnergy(energy);
        ingredient.setProtein(protein);
        ingredient.setFats(fats);
        ingredient.setCarbohydrates(carbohydrates);
        ingredient.setFibre(fibre);
        ingredient.setSalt(salt);
        ingredient.setDescription("");

        if (checkBoxGluten.isChecked()) {
            ingredient.setGluten(true);
        } else {
            ingredient.setGluten(false);
        }

        if (checkBoxLactose.isChecked()) {
            ingredient.setLactose(true);
        } else {
            ingredient.setLactose(false);
        }

        return ingredient;
    }

    /**
     * Validate all necessary text fields.
     * @return
     */
    private boolean validate() {
        if (editEnergy.getText().toString().isEmpty()) {
            editEnergy.setError("Pole nie może być puste!");
            return false;
        }

        if (editProtein.getText().toString().isEmpty()) {
            editProtein.setError("Pole nie może być puste!");
            return false;
        }

        if (editFat.getText().toString().isEmpty()) {
            editFat.setError("Pole nie może być puste!");
            return false;
        }

        if (editCarbohydrates.getText().toString().isEmpty()) {
            editCarbohydrates.setError("Pole nie może być puste!");
            return false;
        }

        if (editFibre.getText().toString().isEmpty()) {
            editFibre.setError("Pole nie może być puste!");
            return false;
        }

        if (editSalt.getText().toString().isEmpty()) {
            editSalt.setError("Pole nie może być puste!");
            return false;
        }

        return true;
    }
}
