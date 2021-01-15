package com.example.kuba.keepitfoody;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Fragment used to store added ingredients while composing
 * a meal and to confirm newly created meal.
 */
public class BasketFragment extends Fragment {

    private Meal meal;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Ingredient> ingredientsDisplay;
    private IngredientAdapter adapter;
    private RecyclerView recyclerView;

    private OnBasketFragmentInteractionListener listener;

    public BasketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BasketFragment.
     */
    public static BasketFragment newInstance(Meal meal) {
        BasketFragment fragment = new BasketFragment();
        Bundle args = new Bundle();
        args.putParcelable(MenuActivity.MEAL, meal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            this.meal = getArguments().getParcelable(MenuActivity.MEAL);
            this.ingredients = meal.getIngredients();
            this.ingredientsDisplay = ingredients;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket, container, false);

        adapter = new IngredientAdapter(getContext(), ingredients, 2);
        adapter.setOnItemClickListener(position -> {
            IngredientDialog dialog = IngredientDialog.newInstance(ingredientsDisplay.get(position));
            dialog.show(getFragmentManager(), MenuActivity.INGREDIENT);
        });
        recyclerView = view.findViewById(R.id.basket);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(25));
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBasketFragmentInteractionListener) {
            listener = (OnBasketFragmentInteractionListener) context;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.basket_menu, menu);
        /*MenuItem searchItem = menu.findItem(R.id.searchAction);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Szukaj");*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirmAction:
                onConfirmClicked(meal);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method used to communicate with CreateMealActivity
     */
    public interface OnBasketFragmentInteractionListener {
        void confirmMeal(Meal meal);
    }

    /**
     * If the list of ingredients is not empty, set meal's date and add meal to database.
     * @param meal
     */
    public void onConfirmClicked(Meal meal) {

        if (meal.getIngredients().isEmpty()) {
            Toast.makeText(getContext(), "Dodaj co najmniej jeden składnik.", Toast.LENGTH_SHORT).show();
        } else {

            String mealDate = MenuActivity.getDate();
            int year = Integer.valueOf(mealDate.split("-")[0]);
            int month = Integer.valueOf(mealDate.split("-")[1]) - 1;
            int dayOfMonth = Integer.valueOf(mealDate.split("-")[2]);

            String mealTime = MenuActivity.getTime();
            int hour = Integer.valueOf(mealTime.split(":")[0]);
            int minute = Integer.valueOf(mealTime.split(":")[1]);

            DatePickerDialog dialog = new DatePickerDialog(
                    getContext(),
                    (view1, newYear, newMonth, newDay) -> {
                        newMonth++;
                        String date = newYear + "-" + newMonth + "-" + newDay;
                        meal.setDate(date);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                (view12, newHour, newMinute) -> {
                                    String time = "";

                                    if (newHour < 10) time += "0" + newHour;
                                    else time += newHour;

                                    time += ":";

                                    if (newMinute < 10) time += "0" + newMinute;
                                    else time += newMinute;

                                    meal.setTime(time);

                                    if (listener != null) {
                                        listener.confirmMeal(meal);
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
}
