package com.example.kuba.keepitfoody;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * This class is used to update user data to server.
 */
public class UserUpdateBackground {

    private User user;
    private Context context;

    // Constructor
    public UserUpdateBackground(Context context, User user) {
        this.user = user;
        this.context = context;

        createRequest();
    }

    public void parseJSON() {
        String URL = "https://192.168.134.62/api/user/update.php";
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        JSONObject jsonRequest = createRequest();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                jsonRequest,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("Message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new GetBmrFromApi(context, user.getID()).parseJSON();
                },
                Throwable::printStackTrace);
        queue.add(request);
    }

    /**
     * Map User type object to JSON request object.
     * @return JSONObject
     * @throws JSONException
     */
    private JSONObject createRequest() {
        JSONObject object = new JSONObject();

        try {
            object.put("name", user.getName());
            object.put("date_birth", user.getDateOfBirth());
            object.put("email", user.getEmail());
            object.put("height", user.getHeight());
            object.put("weight", user.getWeight());

            if (user.isSex()) { // Male
                object.put("sex", "M");
            } else { // Female
                object.put("sex", "K");
            }

            object.put("lifestyle", user.getActivityFactor());
            object.put("allergens", user.getAllergens());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

}
