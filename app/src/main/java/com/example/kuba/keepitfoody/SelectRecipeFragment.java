package com.example.kuba.keepitfoody;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Fragment used to select Recipe type object, convert it to Meal type object
 * and send it to server while inside CreateMealActivity.
 */
public class SelectRecipeFragment extends Fragment implements
        SearchView.OnQueryTextListener {

    private ArrayList<Recipe> recipes;
    private ArrayList<Recipe> recipesDisplay;
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;

    private OnSelectRecipeFragmentInteractionListener listener;

    public SelectRecipeFragment() {
        // Required empty public constructor
    }

    public static SelectRecipeFragment newInstance() {
        SelectRecipeFragment fragment = new SelectRecipeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_recipe, container, false);

        FoodyDatabaseHelper helper = FoodyDatabaseHelper.getInstance(getContext());
        recipes = helper.fetchRecipes();
        recipesDisplay = recipes;

        adapter = new RecipeAdapter(getActivity(), recipes);
        adapter.setOnClickListener(position -> {
            Recipe recipe = recipesDisplay.get(position);
            setDataAndSend(recipe.getID());
        });
        recyclerView = view.findViewById(R.id.recipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(16));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.select_recipe_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchAction);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Szukaj");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addAction:
                /*Intent intent = new Intent(getActivity(), CreateRecipeActivity.class);
                getActivity().startActivityForResult(intent, RECIPE);*/
                Toast.makeText(getActivity(), "ACTION_ADD", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        recipesDisplay = new ArrayList<>();

        if (query == null || query.length() == 0) {
            recipesDisplay.addAll(recipes);
        }  else {
            query = query.toLowerCase().trim();

            for (Recipe recipe : recipes) {
                if (recipe.getName().toLowerCase().contains(query)) {
                    recipesDisplay.add(recipe);
                }
            }
        }

        adapter = new RecipeAdapter(getActivity(), recipesDisplay);
        adapter.setOnClickListener(position -> {
            Recipe recipe = recipesDisplay.get(position);
            setDataAndSend(recipe.getID());
        });
        recyclerView.setAdapter(adapter);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnSelectRecipeFragmentInteractionListener) {
            listener = (OnSelectRecipeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /**
     * Public interface used to send Recipe type object to CreateMealActivity.
     */
    public interface OnSelectRecipeFragmentInteractionListener {
        void onConfirmClick(int recipeId, String date, String time);
    }

    /**
     * After selecting recipe set date and time and send to server.
     * @param recipeId
     */
    private void setDataAndSend(int recipeId) {
        String mealDate = MenuActivity.getDate();
        int year = Integer.valueOf(mealDate.split("-")[0]);
        int month = Integer.valueOf(mealDate.split("-")[1]) - 1;
        int dayOfMonth = Integer.valueOf(mealDate.split("-")[2]);

        String mealTime = MenuActivity.getTime();
        int hour = Integer.valueOf(mealTime.split(":")[0]);
        int minute = Integer.valueOf(mealTime.split(":")[1]);

        android.app.DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                (view1, newYear, newMonth, newDay) -> {
                    newMonth++;
                    String date = newYear + "-" + newMonth + "-" + newDay;

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                            (view12, newHour, newMinute) -> {
                                String time = "";

                                if (newHour < 10) time += "0" + newHour;
                                else time += newHour;

                                time += ":";

                                if (newMinute < 10) time += "0" + newMinute;
                                else time += newMinute;


                                if (listener != null) {
                                    listener.onConfirmClick(recipeId, date, time);
                                }

                            }, hour, minute, true);
                    timePickerDialog.show();
                },
                year,
                month,
                dayOfMonth);
        dialog.show();
    }
}
