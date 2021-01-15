package com.example.kuba.keepitfoody;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Fragment used to display recipes in a collection of items.
 */
public class BrowseRecipesFragment extends Fragment implements SearchView.OnQueryTextListener {

    private ArrayList<Recipe> recipes;
    private ArrayList<Recipe> recipesDisplay;
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        FoodyDatabaseHelper helper = FoodyDatabaseHelper.getInstance(getContext());
        recipes = helper.fetchRecipes();
        this.recipesDisplay = recipes;
        
    }

    /**
     * Creates new instance of BrowseRecipesFragment class
     * with parameters.
     *
     * @return Returns created fragment instance
     */
    public static BrowseRecipesFragment newInstance() {
        BrowseRecipesFragment fragment = new BrowseRecipesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_recipes, container, false);

        adapter = new RecipeAdapter(getContext(), recipes);
        adapter.setOnClickListener(position -> {
            Recipe recipe = recipesDisplay.get(position);
            Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
            intent.putExtra(MenuActivity.RECIPE, recipe.getID() + "");
            getActivity().startActivity(intent);
        });
        recyclerView = view.findViewById(R.id.recyclerViewRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(30));
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recipe_menu, menu);
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
                Intent intent = new Intent(getActivity(), CreateRecipeActivity.class);
                getActivity().startActivityForResult(intent, MenuActivity.REQUEST_RECIPE);
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

        adapter = new RecipeAdapter(getContext(), recipesDisplay);
        recyclerView.setAdapter(adapter);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

}
