package com.example.kuba.keepitfoody;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to get all specialists from server
 * via GET request and save it to local database.
 */
public class GetSpecialistsFromAPI {

    private Context context;
    private SQLiteDatabase database;

    public GetSpecialistsFromAPI(Context context, SQLiteDatabase database) {
        this.context = context;
        this.database = database;
    }

    public void parseJSON() {
        String URL = "https://192.168.134.62/api/specialist/read.php";
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject specialist = jsonArray.getJSONObject(i);

                            int ID = specialist.getInt("ID");
                            String email = specialist.getString("email");
                            String firstName = specialist.getString("name");
                            String lastName = specialist.getString("surname");
                            String profession = specialist.getString("profession");
                            String specialization = specialist.getString("specialization");
                            String picture = specialist.getString("picture");

                            ContentValues cv = new ContentValues();
                            cv.put(FoodyDatabaseHelper.SPECIALISTS_COLUMN_ID, ID);
                            cv.put(FoodyDatabaseHelper.SPECIALISTS_COLUMN_EMAIL, email);
                            cv.put(FoodyDatabaseHelper.SPECIALISTS_COLUMN_FIRST_NAME, firstName);
                            cv.put(FoodyDatabaseHelper.SPECIALISTS_COLUMN_LAST_NAME, lastName);
                            cv.put(FoodyDatabaseHelper.SPECIALISTS_COLUMN_PROFESSION, profession);
                            cv.put(FoodyDatabaseHelper.SPECIALISTS_COLUMN_SPECIALIZATION, specialization);
                            cv.put(FoodyDatabaseHelper.SPECIALISTS_COLUMN_PICTURE, picture);
                            database.insert(FoodyDatabaseHelper.SPECIALISTS_TABLE_NAME, null, cv);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace());

        queue.add(request);
    }

}
