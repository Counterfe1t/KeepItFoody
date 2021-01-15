package com.example.kuba.keepitfoody;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to contact with selected specialist via email.
 */
public class SpecialistContactBackground {

    private Context context;
    private Specialist specialist;
    private User user;
    private FoodyDatabaseHelper helper;

    public SpecialistContactBackground(Context context, int specialistId) {
        this.context = context;
        this.helper = FoodyDatabaseHelper.getInstance(context);
        this.specialist = helper.fetchSpecialist(specialistId);
        this.user = helper.fetchUser();
    }

    public void parseJSON() {
        String URL = "https://192.168.134.62/api/specialist/email-to-specialist.php";
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        JSONObject req = createRequest();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                req,
                response -> {
                    try {
                        String message = response.getString("Message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace());

        queue.add(request);
    }

    private JSONObject createRequest() {
        JSONObject request = new JSONObject();

        try {
            request.put("specialist_email", specialist.getEmail());
            request.put("user_email", user.getEmail());
            request.put("first_name", user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return request;
    }
}
