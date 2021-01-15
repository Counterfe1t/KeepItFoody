package com.example.kuba.keepitfoody;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to handle register events in the background thread.
 */
public class SignUpBackground {

    private Context context;
    private String name;
    private String email;
    private String pass1;
    private String pass2;

    public SignUpBackground(Context context,
                            String name,
                            String email,
                            String pass1,
                            String pass2) {
        this.context = context;
        this.name = name;
        this.email = email;
        this.pass1 = pass1;
        this.pass2 = pass2;
    }

    public void sendRequest() throws JSONException {
        String URL = "https://192.168.134.62/api/user/register.php";
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
        object.put("name", name);
        object.put("email", email);
        object.put("pass", pass1);
        object.put("pass2", pass2);
        Log.e("JSON", object.toString());
        return object;
    }

}
