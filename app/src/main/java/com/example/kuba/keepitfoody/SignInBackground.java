package com.example.kuba.keepitfoody;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to handle login events in the background thread.
 */
public class SignInBackground {

    private Context context;
    private String email;
    private String password;

    public SignInBackground(Context context, String email, String password) {
        this.context = context;
        this.email = email;
        this.password = password;
    }

    public void sendRequest() throws JSONException {
        String URL = "https://192.168.134.62/api/user/login.php";
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

                        if (message.contains("Zalogowano")) {
                            User user = new User();
                            user.setEmail(email);
                            user.setPassword(password);
                            user.setToken(response.getString("token"));
                            user.setID(response.getInt("id"));
                            user.setName(response.getString("name"));
                            user.setDateOfBirth(response.getString("date_birth"));
                            user.setHeight(response.getDouble("height"));
                            user.setWeight(response.getDouble("weight"));
                            user.setActivityFactor(response.getDouble("lifestyle"));
                            user.setProfilePicture(response.getString("picture"));
                            if (response.getString("sex").contains("M")) {
                                user.setSex(true); // Male
                            } else {
                                user.setSex(false); // Female
                            }

                            ContentValues cv = new ContentValues();
                            cv.put(FoodyDatabaseHelper.USER_COLUMN_ID_USER, user.getID());
                            cv.put(FoodyDatabaseHelper.USER_COLUMN_EMAIL, email);
                            cv.put(FoodyDatabaseHelper.USER_COLUMN_PASSWORD, password);
                            cv.put(FoodyDatabaseHelper.USER_COLUMN_TOKEN, user.getToken());

                            SQLiteDatabase database = FoodyDatabaseHelper.getInstance(context).getDatabase();
                            database.insert(FoodyDatabaseHelper.USER_TABLE_NAME, null, cv);

                            Intent intent = new Intent(context, SplashScreenActivity.class);
                            intent.putExtra(MenuActivity.USER, user);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace);
        queue.add(request);
    }

    private JSONObject createRequest() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("email", email);
        object.put("pass", password);
        return object;
    }

}
