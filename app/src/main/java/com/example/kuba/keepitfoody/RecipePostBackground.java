package com.example.kuba.keepitfoody;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This is used to create request out of Recipe type
 * object and send it to server via POST method.
 */
public class RecipePostBackground {

    private Context context;
    private Recipe recipe;
    private User user;

    public RecipePostBackground(Context context, Recipe recipe) {
        this.context = context;
        this.recipe = recipe;

        FoodyDatabaseHelper helper = FoodyDatabaseHelper.getInstance(context);
        this.user = helper.fetchUser();
    }

    public void sendRequest() throws JSONException {
        String URL = "https://192.168.134.62/api/recipe/add.php";
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        JSONObject jsonRequest = createRequest();

        Log.e("JSON", jsonRequest.toString(), null);

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
     * Map Recipe type object to JSON request object.
     * @return JSONObject
     * @throws JSONException
     */
    private JSONObject createRequest() throws JSONException {
        ArrayList<Ingredient> ingredients = recipe.getIngredients();
        JSONObject object = new JSONObject();
        object.put("id_user", user.getID());
        object.put("recipe_name", recipe.getName());
        object.put("photo", recipe.getImage());
        object.put("preparing_time", recipe.getPreparationTime());
        object.put("difficulty", recipe.getDifficulty());
        object.put("prepare", recipe.getDescription());

        JSONObject ingredientsObject = new JSONObject();
        JSONObject quantityObject = new JSONObject();
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            ingredientsObject.put(String.valueOf(i + 1), ingredients.get(i).getID());
            quantityObject.put(String.valueOf(i + 1), ingredients.get(i).getQuantity());
        }
        object.put("id_ingredient", ingredientsObject);
        object.put("quantity", quantityObject);

        return object;
    }

}
