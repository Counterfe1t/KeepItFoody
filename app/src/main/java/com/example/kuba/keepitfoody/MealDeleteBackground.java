package com.example.kuba.keepitfoody;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to delete specific meal.
 */
public class MealDeleteBackground {

    private Context context;
    private final int USER_ID;
    private int MEAL_ID;

    public MealDeleteBackground(Context context, final int USER_ID, final int MEAL_ID) {
        this.context = context;
        this.USER_ID = USER_ID;
        this.MEAL_ID = MEAL_ID;
    }

    public void sendRequest() throws JSONException {
        String URL = "https://192.168.134.62/api/user/delete-meal.php";
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        JSONObject jsonRequest = createRequest();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonRequest,
                response -> {
                    try {
                        String message = response.getString("Message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace);
        queue.add(request);
    }

    private JSONObject createRequest() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("id_meal", MEAL_ID);
        object.put("who", USER_ID);
        return object;
    }

}
