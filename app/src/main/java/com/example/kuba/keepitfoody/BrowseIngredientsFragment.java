package com.example.kuba.keepitfoody;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
 * Fragment used to display ingredients and nutrition details.
 */
public class BrowseIngredientsFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private ArrayList<Ingredient> ingredients;
    private ArrayList<Ingredient> ingredientsDisplay;
    private RecyclerView recyclerView;
    private IngredientAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        FoodyDatabaseHelper helper = FoodyDatabaseHelper.getInstance(getContext());
        ingredients = helper.fetchIngredients();

        this.ingredientsDisplay = ingredients;
    }

    /**
     * Creates new instance of BrowseIngredientsFragment class
     * with parameters.
     *
     * @return Returns created fragment instance
     */
    public static BrowseIngredientsFragment newInstance() {
        BrowseIngredientsFragment fragment = new BrowseIngredientsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_ingredients, container, false);

        adapter = new IngredientAdapter(getContext(), ingredients, 1);
        adapter.setOnItemClickListener(position -> {
            IngredientDialog dialog = IngredientDialog.newInstance(ingredientsDisplay.get(position));
            dialog.show(getFragmentManager(), MenuActivity.INGREDIENT);
        });
        recyclerView = view.findViewById(R.id.recyclerViewIngredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(25));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.ingredient_menu, menu);
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
                Intent intent = new Intent(getActivity(), CreateIngredientActivity.class);
                getActivity().startActivityForResult(intent, MenuActivity.REQUEST_INGREDIENT);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        ingredientsDisplay = new ArrayList<>();

        if (query == null || query.length() == 0) {
            ingredientsDisplay.addAll(ingredients);
        }
        else {
            query = query.toLowerCase().trim();

            for (Ingredient ingredient : ingredients) {
                if (ingredient.getName().toLowerCase().contains(query)) {
                    ingredientsDisplay.add(ingredient);
                }
            }

        }

        adapter = new IngredientAdapter(getContext(), ingredientsDisplay, 1);
        adapter.setOnItemClickListener(position -> {
            IngredientDialog dialog = IngredientDialog.newInstance(ingredientsDisplay.get(position));
            dialog.show(getFragmentManager(), MenuActivity.INGREDIENT);
        });
        recyclerView.setAdapter(adapter);

        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

}