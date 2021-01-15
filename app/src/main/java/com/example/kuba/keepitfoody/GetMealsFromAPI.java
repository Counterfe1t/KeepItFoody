package com.example.kuba.keepitfoody;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to get all currently logged in user's
 * meals from server via GET request and save it to local database.
 */
public class GetMealsFromAPI {

    private Context context;
    private SQLiteDatabase database;
    private int USER_ID;

    public GetMealsFromAPI(Context context, SQLiteDatabase database, int USER_ID) {
        this.context = context;
        this.database = database;
        this.USER_ID = USER_ID;
    }

    public void parseJSON() {
        String URL = "https://192.168.134.62/api/user/read-meals.php?who=" + USER_ID;
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject meal = jsonArray.getJSONObject(i);

                            int id = meal.getInt("id_meal");

                            int id_recipe;
                            if (meal.isNull("id_recipe")) id_recipe = 0;
                            else id_recipe = meal.getInt("id_recipe");

                            String name = meal.getString("meal_name");
                            String datetime = meal.getString("date");

                            boolean consumed;
                            if (meal.getInt("status") == 0) consumed = false;
                            else consumed = true;

                            String[] buffer = datetime.split(" ");
                            String date = buffer[0].trim();
                            String time = buffer[1].trim().substring(0,5);

                            String INGREDIENT_URL = "https://192.168.134.62/api/user/read-meals.php?fk_ing=" + id;
                            JsonObjectRequest ingredientRequest = new JsonObjectRequest(
                                    Request.Method.GET,
                                    INGREDIENT_URL,
                                    null,
                                    mealResponse -> {
                                        String value = "";

                                        try {
                                            JSONArray ingredientJsonArray = mealResponse.getJSONArray("data");

                                            for (int j = 0; j < ingredientJsonArray.length(); j++) {
                                                JSONObject ingredient = ingredientJsonArray.getJSONObject(j);

                                                int id_ingredient = ingredient.getInt("id_ingredient");
                                                int quantity = ingredient.getInt("quantity");
                                                //int unit = ingredient.getInt("id_unit");

                                                //value += id_ingredient + "," + quantity + "," + unit + ":";
                                                value += id_ingredient + "," + quantity + ":";
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        ContentValues cv = new ContentValues();
                                        cv.put(FoodyDatabaseHelper.MEALS_COLUMN_ID, id);
                                        cv.put(FoodyDatabaseHelper.MEALS_COLUMN_RECIPE_ID, id_recipe);
                                        cv.put(FoodyDatabaseHelper.MEALS_COLUMN_NAME, name);
                                        cv.put(FoodyDatabaseHelper.MEALS_COLUMN_DATE, date);
                                        cv.put(FoodyDatabaseHelper.MEALS_COLUMN_TIME, time);
                                        cv.put(FoodyDatabaseHelper.MEALS_COLUMN_STATUS, consumed);
                                        cv.put(FoodyDatabaseHelper.MEALS_COLUMN_INGREDIENTS, value);
                                        database.insert(FoodyDatabaseHelper.MEALS_TABLE_NAME, null, cv);
                                    },
                                    error -> error.printStackTrace());

                            queue.add(ingredientRequest);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace());

        queue.add(request);
    }

}