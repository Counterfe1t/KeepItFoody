package com.example.kuba.keepitfoody;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to get all recipes from server
 * via GET request and save it to local database.
 */
public class GetRecipesFromAPI {

    private Context context;
    private SQLiteDatabase database;

    public GetRecipesFromAPI(Context context, SQLiteDatabase database) {
        this.context = context;
        this.database = database;
    }

    public void parseJSON() {
        String URL = "https://192.168.134.62/api/recipe/read.php";
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject recipe = jsonArray.getJSONObject(i);

                            int ID = recipe.getInt("id_recipe");
                            String name = recipe.getString("recipe_name");
                            String description = recipe.getString("prepare");
                            String image = recipe.getString("recipe_photo");
                            int preparationTime = recipe.getInt("preparing_time");
                            int difficulty = recipe.getInt("difficulty");
                            int AUTHOR_ID = recipe.getInt("id_user");
                            String authorImage = recipe.getString("user_photo");
                            String authorFirstName = recipe.getString("first_name");

                            String RECIPE_URL = "https://192.168.134.62/api/recipe/read.php?id_recipe=" + ID;
                            JsonObjectRequest recipeRequest = new JsonObjectRequest(
                                    Request.Method.GET,
                                    RECIPE_URL,
                                    null,
                                    recipeResponse -> {
                                        String value = "";
                                        try {
                                            JSONArray recipeJsonArray = recipeResponse.getJSONArray("data");


                                            for (int j = 0; j < recipeJsonArray.length(); j++) {
                                                JSONObject ingredient = recipeJsonArray.getJSONObject(j);

                                                String id_ingredient = ingredient.getString("id_ingredient");
                                                int quantity = ingredient.getInt("quantity");

                                                value += id_ingredient + "," + quantity + ":";
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        ContentValues cv = new ContentValues();
                                        cv.put(FoodyDatabaseHelper.RECIPES_COLUMN_ID, ID);
                                        cv.put(FoodyDatabaseHelper.RECIPES_COLUMN_NAME, name);
                                        cv.put(FoodyDatabaseHelper.RECIPES_COLUMN_DESCRIPTION, description);
                                        cv.put(FoodyDatabaseHelper.RECIPES_COLUMN_PHOTO, image);
                                        cv.put(FoodyDatabaseHelper.RECIPES_COLUMN_PREPARATION_TIME, preparationTime);
                                        cv.put(FoodyDatabaseHelper.RECIPES_COLUMN_DIFFICULTY, difficulty);
                                        cv.put(FoodyDatabaseHelper.RECIPES_COLUMN_AUTHOR_ID, AUTHOR_ID);
                                        cv.put(FoodyDatabaseHelper.RECIPES_COLUMN_AUTHOR_PHOTO, authorImage);
                                        cv.put(FoodyDatabaseHelper.RECIPES_COLUMN_AUTHOR_FIRST_NAME, authorFirstName);
                                        cv.put(FoodyDatabaseHelper.RECIPES_COLUMN_INGREDIENTS, value);
                                        database.insert(FoodyDatabaseHelper.RECIPES_TABLE_NAME, null, cv);

                                    },
                                    error -> {
                                        error.printStackTrace();
                                        Toast.makeText(context, "Wystąpił błąd podczas pobierania danych.\n" +
                                                "Upewnij się, że masz połączenie z internetem", Toast.LENGTH_SHORT).show();
                                    });

                            queue.add(recipeRequest);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace());

        queue.add(request);
    }

}
