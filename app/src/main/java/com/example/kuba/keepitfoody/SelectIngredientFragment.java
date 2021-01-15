package com.example.kuba.keepitfoody;

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
 * Fragment used to select Ingredient type object and add it to
 * collection while inside CreateMealActivity.
 */
public class SelectIngredientFragment extends Fragment implements
    SearchView.OnQueryTextListener {

    private ArrayList<Ingredient> ingredients;
    private ArrayList<Ingredient> ingredientsDisplay;
    private RecyclerView recyclerView;
    private IngredientAdapter adapter;

    public SelectIngredientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SelectIngredientFragment.
     */
    public static SelectIngredientFragment newInstance() {
        SelectIngredientFragment fragment = new SelectIngredientFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        FoodyDatabaseHelper helper = FoodyDatabaseHelper.getInstance(getContext());
        this.ingredients = helper.fetchIngredients();

        this.ingredientsDisplay = ingredients;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_ingredient,
                container,
                false);

        adapter = new IngredientAdapter(getContext(), ingredients, 1);
        adapter.setOnItemClickListener(position -> {
            Ingredient ingredient = ingredientsDisplay.get(position);
            int ID = ingredient.getID();
            openDialog(ID);
        });
        recyclerView = view.findViewById(R.id.ingredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(25));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.select_ingredient_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchAction);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Szukaj");
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
            Ingredient ingredient = ingredientsDisplay.get(position);
            int ID = ingredient.getID();
            openDialog(ID);
        });
        recyclerView.setAdapter(adapter);

        return true;
    }


    public void openDialog(int ID) {
        SetQuantityDialog dialog = SetQuantityDialog.newInstance(ID);
        dialog.show(getFragmentManager(), "dialog");
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


}
