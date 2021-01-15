package com.example.kuba.keepitfoody;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import java.util.ArrayList;

/**
 * Activity used to create a meal from scratch or select a recipe
 * and then send it to server.
 */
public class CreateMealActivity extends AppCompatActivity implements
    BasketFragment.OnBasketFragmentInteractionListener,
    SelectRecipeFragment.OnSelectRecipeFragmentInteractionListener,
    NamePickerDialog.NamePickerDialogListener,
    SetQuantityDialog.SetQuantityDialogListener {

    private Meal meal;
    private ArrayList<Ingredient> ingredients;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal);

        //Intent intent = getIntent();
        //user = intent.getParcelableExtra(MenuActivity.USER);
        meal = new Meal();
        meal.setDate(MenuActivity.getDate());
        meal.setTime(MenuActivity.getTime());
        FoodyDatabaseHelper helper = FoodyDatabaseHelper.getInstance(this);
        ingredients = helper.fetchIngredients();

        viewPager = findViewById(R.id.container);
        FragmentManager fm = getSupportFragmentManager();
        sectionsPagerAdapter = new SectionsPagerAdapter(fm);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        openDialog();
    }

    /**
     * A class that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = SelectIngredientFragment.newInstance();
                    break;
                case 1:
                    fragment = SelectRecipeFragment.newInstance();
                    break;
                case 2:
                    fragment = BasketFragment.newInstance(meal);
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Składniki";
                case 1:
                    return "Przepisy";
                case 2:
                    return "Posiłek";
            }

            return null;
        }
    }

    /**
     * Open NamePickerDialog to set name for new meal.
     */
    public void openDialog() {
        NamePickerDialog dialog = new NamePickerDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * This method allows communication
     * between BasketFragment and CreateMealActivity
     * through public interface.
     */
    @Override
    public void confirmMeal(Meal meal) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MenuActivity.MEAL, meal);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * This method allows communication
     * between SelectRecipeFragment and CreateMealActivity
     * through public interface.
     */
    @Override
    public void onConfirmClick(int recipeId, String date, String time) {
        meal.setRECIPE_ID(recipeId);
        meal.setDate(date);
        meal.setTime(time);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(MenuActivity.MEAL, meal);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * This method allows communication
     * between NamePickerDialog and CreateMealActivity
     * through public interface.
     */
    @Override
    public void applyMealName(String name, int icon) {
        if (name.equals("cancel")) {
            this.finish();
        } else {
            meal.setName(name);
            meal.setIcon(icon);
        }
    }

    /**
     * Add ingredient to meal. Merge if ingredient already exists.
     * @param quantity Ingredient's weight
     * @param ID Ingredient's id
     */
    @Override
    public void applyIngredientQuantity(int quantity, int ID) {
        Ingredient ingredient = new Ingredient();

        for (Ingredient item : ingredients) {
            if (item.getID() == ID) {
                ingredient = item;
                break;
            }
        }

        // Sprawdzam czy dany skladnik zostal juz dodany, jesli tak to sumuje wage (g)
        ArrayList<Ingredient> mealIngredients = meal.getIngredients();
        for (Ingredient item : mealIngredients) {
            if(item.getID() == ID) {
                quantity += item.getQuantity();
                mealIngredients.remove(item);
                meal.setIngredients(mealIngredients);
                break;
            }
        }

        ingredient.setQuantity(quantity);
        meal.addIngredient(ingredient);
    }
}
