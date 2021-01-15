package com.example.kuba.keepitfoody;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Activity used to create new recipe and send it to server.
 */
public class CreateRecipeActivity extends AppCompatActivity {

    private String imageBase64 = "";

    private ImageView imageViewRecipe;
    private TextInputEditText editName;
    private TextInputEditText editDescription;
    private TextInputEditText editPreparationTime;
    private TextView textViewDifficulty;
    private TextView textViewAddIngredients;
    private SeekBar seekBar;

    private RecyclerView recyclerView;
    private IngredientAdapter adapter;
    private ArrayList<Ingredient> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        imageViewRecipe = findViewById(R.id.imageViewRecipe);
        imageViewRecipe.setOnClickListener(v -> {
            Toast.makeText(this, "Otwieram gelerię...", Toast.LENGTH_SHORT).show();
            openGallery();
        });

        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        editPreparationTime = findViewById(R.id.editPreparationTime);

        textViewDifficulty = findViewById(R.id.textViewDifficulty);
        textViewDifficulty.setText("Bardzo łatwy");
        seekBar = findViewById(R.id.difficulty);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                switch (seekBar.getProgress() + 1) {
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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        textViewAddIngredients = findViewById(R.id.textViewAddIngredients);
        textViewAddIngredients.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddIngredientsToRecipeActivity.class);
            startActivityForResult(intent, MenuActivity.REQUEST_INGREDIENT);
        });

        ingredients = new ArrayList<>();
        adapter = new IngredientAdapter(this, ingredients, 3);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirmAction:

                if (!imageBase64.isEmpty()) {
                    Recipe newRecipe = createRecipe();
                    if (newRecipe != null) {
                        newRecipe.setImage(imageBase64);
                        Intent resultIntent = new Intent();
                        //resultIntent.putExtra(MenuActivity.RECIPE, newRecipe);
                        setResult(RESULT_OK, resultIntent);

                        try {
                            new RecipePostBackground(this, newRecipe).sendRequest();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        finish();
                    }

                } else {
                    Toast.makeText(this, "Zdjęcie jest wymagane!", Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Create Recipe type object.
     * @return
     */
    private Recipe createRecipe() {
        Recipe recipe = new Recipe();

        if (editName.getText().toString().isEmpty()) {
            editName.setError("To pole nie może być puste!");
            return null;
        } else {
            recipe.setName(editName.getText().toString().trim());
        }

        if (editDescription.getText().toString().isEmpty()) {
            editDescription.setError("To pole nie może być puste!");
            return null;
        } else {
            recipe.setDescription(editDescription.getText().toString().trim());
        }

        if (editPreparationTime.getText().toString().isEmpty()) {
            editPreparationTime.setError("To pole nie może być puste!");
            return null;
        } else {
            recipe.setPreparationTime(Integer.valueOf(editPreparationTime.getText().toString().trim()));
        }

        recipe.setDifficulty(seekBar.getProgress() + 1);

        if (!ingredients.isEmpty()) {
            recipe.setIngredients(ingredients);
        } else {
            Toast.makeText(this, "Dodaj co najmniej jeden składnik!", Toast.LENGTH_SHORT).show();
            return null;
        }

        return recipe;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == MenuActivity.REQUEST_IMAGE) {

            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                imageViewRecipe.setImageURI(imageUri);

                //Encoding picture into Base64 String
                imageViewRecipe.buildDrawingCache();
                Bitmap bitmap = imageViewRecipe.getDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encodedPicture = Base64.encodeToString(byteArray, Base64.DEFAULT);
                imageBase64 = encodedPicture.replace("\n", "");

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Anulowano.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == MenuActivity.REQUEST_INGREDIENT) {

            if (resultCode == RESULT_OK) {

                ArrayList<Ingredient> newIngredients = new ArrayList<>();

                if (data != null) {
                    newIngredients = data.getParcelableArrayListExtra(MenuActivity.INGREDIENT);
                }

                for (Ingredient ingredient : newIngredients) {
                    mergeIfMember(ingredient);
                }

                adapter = new IngredientAdapter(this, ingredients, 3);
                adapter.setOnItemClickListener(position -> {
                    IngredientDialog dialog = IngredientDialog.newInstance(ingredients.get(position));
                    dialog.show(getSupportFragmentManager(), MenuActivity.INGREDIENT);
                });
                recyclerView.setAdapter(adapter);


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Anulowano.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    /**
     * If recipe already contains ingredient merge new ingredient
     * with the existing one.
     * @param newIngredient
     */
    private void mergeIfMember(Ingredient newIngredient) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getID() == newIngredient.getID()) {
                ingredients.remove(ingredient);
                newIngredient.setQuantity(newIngredient.getQuantity() + ingredient.getQuantity());
                break;
            }
        }
        ingredients.add(newIngredient);
    }


    /**
     * Open gallery to add image to recipe.
     */
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, MenuActivity.REQUEST_IMAGE);
    }
}