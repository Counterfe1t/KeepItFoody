package com.example.kuba.keepitfoody;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is to get all ingredients from server
 * via GET request and save it to local database.
 */
public class GetIngredientsFromAPI {

    private Context context;
    private SQLiteDatabase database;

    public GetIngredientsFromAPI(Context context, SQLiteDatabase database) {
        this.context = context;
        this.database = database;
    }

    public void parseJSON() {
        String URL = "https://192.168.134.62/api/ingredient/read.php";
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ingredient = jsonArray.getJSONObject(i);

                            int ID = ingredient.getInt("ID");
                            String name = ingredient.getString("name");
                            String category = ingredient.getString("category");
                            double energy = ingredient.getDouble("energy");
                            double protein = ingredient.getDouble("protein");
                            double fats = ingredient.getDouble("fats");
                            double carbohydrates = ingredient.getDouble("carbohydrates");
                            double fibre = ingredient.getDouble("fibre");
                            double salt = ingredient.getDouble("salt");
                            String description = ingredient.getString("description");
                            int gluten = ingredient.getInt("gluten");
                            int lactose = ingredient.getInt("lactose");

                            ContentValues cv = new ContentValues();
                            cv.put(FoodyDatabaseHelper.INGREDIENTS_COLUMN_ID, ID);
                            cv.put(FoodyDatabaseHelper.INGREDIENTS_COLUMN_NAME, name);
                            cv.put(FoodyDatabaseHelper.INGREDIENTS_COLUMN_CATEGORY, category);
                            cv.put(FoodyDatabaseHelper.INGREDIENTS_COLUMN_ENERGY, energy);
                            cv.put(FoodyDatabaseHelper.INGREDIENTS_COLUMN_PROTEIN, protein);
                            cv.put(FoodyDatabaseHelper.INGREDIENTS_COLUMN_FATS, fats);
                            cv.put(FoodyDatabaseHelper.INGREDIENTS_COLUMN_CARBOHYDRATES, carbohydrates);
                            cv.put(FoodyDatabaseHelper.INGREDIENTS_COLUMN_FIBRE, fibre);
                            cv.put(FoodyDatabaseHelper.INGREDIENTS_COLUMN_SALT, salt);
                            cv.put(FoodyDatabaseHelper.INGREDIENTS_COLUMN_DESCRIPTION, description);
                            cv.put(FoodyDatabaseHelper.INGREDIENTS_COLUMN_GLUTEN, gluten);
                            cv.put(FoodyDatabaseHelper.INGREDIENTS_COLUMN_LACTOSE, lactose);
                            database.insert(FoodyDatabaseHelper.INGREDIENTS_TABLE_NAME, null, cv);
                        }

                    } catch (JSONException e) {
                        Log.e("ERROR", "parseJSON: ", e);
                    }
                },
                error -> Log.e("ERROR", "parseJSON: ", error));

        queue.add(request);
    }

}

