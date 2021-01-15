package com.example.kuba.keepitfoody;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Activity used to generate and display shopping list for specified day.
 */
public class ShoppingListActivity extends AppCompatActivity {

    private ArrayList<Ingredient> shoppingList;
    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;

    private FoodyDatabaseHelper helper;
    private String date;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.getParent().requestChildFocus(nestedScrollView, nestedScrollView);

        Intent intent = getIntent();
        date = intent.getStringExtra(MenuActivity.DATE);
        helper = FoodyDatabaseHelper.getInstance(this);
        shoppingList = prepareShoppingList(helper.fetchMeals(date));

        TextView textViewDate = findViewById(R.id.textViewDate);
        textViewDate.setText(DailyMealsFragment.formatDate(date));

        adapter = new ShoppingListAdapter(this, shoppingList);
        recyclerView = findViewById(R.id.recyclerViewShoppingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(25));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Prepare shopping list.
     * @param meals
     * @return A collction of Ingredient type object.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Ingredient> prepareShoppingList(ArrayList<Meal> meals) {
        ArrayList<Ingredient> list = new ArrayList<>();

        for (Meal meal : meals) {
            for (Ingredient ingredient : meal.getIngredients()) {
                if (!doesItemExist(list, ingredient)) {
                    list.add(ingredient);
                } else {
                    list = mergeItems(list, ingredient);
                }
            }
        }

        list.sort((item1, item2) -> item1.getName().compareToIgnoreCase(item2.getName()));

        return list;
    }

    private boolean doesItemExist(ArrayList<Ingredient> list, Ingredient item) {
        for (Ingredient i : list) {
            if (i.getID() == item.getID()) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Ingredient> mergeItems(ArrayList<Ingredient> list, Ingredient item) {
        ArrayList<Ingredient> toRemove = new ArrayList<>();
        ArrayList<Ingredient> toAdd = new ArrayList<>();

        for (Ingredient i : list) {
            if (i.getID() == item.getID()) {
                item.setQuantity(item.getQuantity() + i.getQuantity());
                toRemove.add(i);
                toAdd.add(item);
            }
        }

        list.removeAll(toRemove);
        list.addAll(toAdd);

        return list;
    }

}
