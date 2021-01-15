package com.example.kuba.keepitfoody;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

/**
 * This class is used to get basal metabolic rate from server
 * via GET request and save it to local database.
 */
public class GetBmrFromApi {

    private Context context;
    private SQLiteDatabase database;
    private int userId;

    public GetBmrFromApi(Context context, int userId) {
        this.context = context;
        this.userId = userId;
        this.database = FoodyDatabaseHelper.getInstance(context).getDatabase();
    }

    public void parseJSON() {
        String URL = "https://192.168.134.62/api/user/bmr.php?who=" + userId;
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    try {
                        int bmr = response.getInt("bmr");
                        double water = response.getDouble("water");

                        ContentValues cv = new ContentValues();
                        cv.put(FoodyDatabaseHelper.USER_COLUMN_BMR, bmr);
                        cv.put(FoodyDatabaseHelper.USER_COLUMN_WATER, water);
                        database.update(FoodyDatabaseHelper.USER_TABLE_NAME, cv, "id = 1", null);

                    } catch (JSONException e) {
                        Log.e("ERROR", "parseJSON: ", e);
                    }
                },
                error -> Log.e("ERROR", "parseJSON: ", error));

        queue.add(request);
    }

}
