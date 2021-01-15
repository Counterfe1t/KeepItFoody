package com.example.kuba.keepitfoody;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used update user image to server.
 */
public class UserUpdatePictureBackground {

    private Context context;
    private User user;
    private String encodedImage;

    public UserUpdatePictureBackground(Context context, User user, String encodedImage) {
        this.context = context;
        this.user = user;
        this.encodedImage = encodedImage;
    }

    public void parseJSON() {
        String URL = "https://192.168.134.62/api/user/photo.php";
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        JSONObject req = createRequest();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                req,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("Message"), Toast.LENGTH_SHORT).show();
                        String picture = response.getString("path");
                        Log.e("ERR", "path: " + picture, null);
                        FoodyDatabaseHelper.getInstance(context).updateProfilePicture(picture);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace());

        queue.add(request);
    }

    /**
     * Create JSONObject request.
     * @return JSONObject request
     */
    private JSONObject createRequest() {
        JSONObject request = new JSONObject();

        try {
            request.put("id", user.getID());
            request.put("picture", encodedImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return request;
    }
}
