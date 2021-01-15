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
 * This is used to create request out of Ingredient type
 * object and send it to server via POST method.
 */
public class IngredientPostBackground {

    private Context context;
    private Ingredient ingredient;

    public IngredientPostBackground(Context context, Ingredient ingredient) {
        this.context = context;
        this.ingredient = ingredient;

        try {
            createRequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest() throws JSONException {
        String URL = "https://192.168.134.62/api/ingredient/add.php";
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
     * Map Ingredient type object to JSON request object.
     * @return JSONObject
     * @throws JSONException
     */
    private JSONObject createRequest() throws JSONException {
        JSONObject object = new JSONObject();

        object.put("name", ingredient.getName());
        object.put("category", ingredient.getCategory());
        object.put("energy", ingredient.getEnergy());
        object.put("protein", ingredient.getProtein());
        object.put("carbohydrates", ingredient.getCarbohydrates());
        object.put("fats", ingredient.getFats());
        object.put("fibre", ingredient.getFibre());
        object.put("salt", ingredient.getSalt());
        object.put("description", ingredient.getDescription());

        if (ingredient.isGluten()) {
            object.put("gluten", 1);
        } else {
            object.put("gluten", 0);
        }
        if (ingredient.isLactose()) {
            object.put("lactose", 1);
        } else {
            object.put("lactose", 0);
        }

        return object;
    }

}
