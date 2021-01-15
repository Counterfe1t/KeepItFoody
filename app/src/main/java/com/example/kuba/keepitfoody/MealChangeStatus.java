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
 * This class is used to change status of given meal.
 */
public class MealChangeStatus {

    private Context context;
    private int id;
    private boolean status;

    public MealChangeStatus(Context context, int id, boolean status) {
        this.context = context;
        this.id = id;
        this.status = status;
    }

    public void sendRequest() throws JSONException {
        String URL = "https://192.168.134.62/api/user/status.php";
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        JSONObject jsonRequest = createRequest(id, status);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                jsonRequest,
                response -> {

                },
                Throwable::printStackTrace);
        queue.add(request);
    }

    private JSONObject createRequest(int id, boolean status) throws JSONException {
        JSONObject object = new JSONObject();

        object.put("id_meal", id);
        if (status) object.put("status", 1);
        else object.put("status", 0);

        return object;
    }
}
