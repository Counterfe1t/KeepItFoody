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

import java.util.ArrayList;

/**
 * This is used to create request out of Meal type
 * object and send it to server via POST method.
 */
public class MealPostBackground {

    private Context context;
    private final int USER_ID;
    private Meal meal;

    public MealPostBackground(Context context, Meal meal, final int USER_ID) {
        this.context = context;
        this.meal = meal;
        this.USER_ID = USER_ID;
    }

    public void sendRequest() throws JSONException {
        String URL = "https://192.168.134.62/api/user/add-meal.php";
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

    /**
     * Map Meal type object to JSON request object.
     * @return JSONObject
     * @throws JSONException
     */
    private JSONObject createRequest() throws JSONException {
        ArrayList<Ingredient> ingredients = meal.getIngredients();
        JSONObject object = new JSONObject();
        object.put("who", USER_ID);
        object.put("meal_name", meal.getName());
        object.put("date", meal.getDate() + " " + meal.getTime());

        if (meal.isStatus()) object.put("status", 1);
        else object.put("status", 0);

        JSONObject ingredientsObject = new JSONObject();
        JSONObject quantityObject = new JSONObject();

        if (meal.getRECIPE_ID() > 0) {
            object.put("recipe", meal.getRECIPE_ID());
        } else {
            for (int i = 0; i < meal.getIngredients().size(); i++) {
                ingredientsObject.put(String.valueOf(i + 1), ingredients.get(i).getID());
                quantityObject.put(String.valueOf(i + 1), ingredients.get(i).getQuantity());
            }
            object.put("id_ingredient", ingredientsObject);
            object.put("quantity", quantityObject);
        }

        return object;
    }
}
