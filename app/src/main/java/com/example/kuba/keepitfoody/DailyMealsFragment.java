package com.example.kuba.keepitfoody;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Fragment responsible for displaying basal metabolic rate and listing meals from given day.
 */
public class DailyMealsFragment extends Fragment
        implements MenuItem.OnActionExpandListener {

    // REQUEST CODES
    private static final int REQUEST_MEAL = 100;

    private ArrayList<Meal> meals;
    private RecyclerView recyclerView;
    private MealAdapter adapter;

    private User user;

    private double basalMetabolicRate = 0.0;
    private double consumedEnergy = 0.0;
    private double plannedEnergy = 0.0;

    private String date;

    // Layout
    private TextView textViewBmr;
    private TextView textViewWater;
    private TextView textViewDate;
    private TextView textViewIfNoMealsMessage;
    private ProgressBar plannedEnergyBar;
    private ProgressBar consumedEnergyBar;
    private TextView textViewPercentage;
    private OnDailyMealsFragmentListener listener;

    public static DailyMealsFragment newInstance(User user, String date) {
        DailyMealsFragment fragment = new DailyMealsFragment();
        Bundle args = new Bundle();
        args.putParcelable(MenuActivity.USER, user);
        args.putString(MenuActivity.DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            //this.user = getArguments().getParcelable(MenuActivity.USER);
            this.date = getArguments().getString(MenuActivity.DATE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_meals, container, false);

        FoodyDatabaseHelper helper = FoodyDatabaseHelper.getInstance(getContext());
        user = helper.fetchUser();
        meals = helper.fetchMeals(date);

        basalMetabolicRate = user.getBasalMetabolicRate();
        NestedScrollView nestedScrollView = view.findViewById(R.id.nestedScrollView);
        nestedScrollView.requestFocus();

        textViewWater = view.findViewById(R.id.textViewWater);
        textViewBmr = view.findViewById(R.id.textViewBmr);
        textViewDate = view.findViewById(R.id.textViewDate);
        plannedEnergyBar = view.findViewById(R.id.progressBarPlannedEnergy);
        consumedEnergyBar = view.findViewById(R.id.progressBarConsumedEnergy);
        textViewPercentage = view.findViewById(R.id.textViewPercentage);
        textViewIfNoMealsMessage = view.findViewById(R.id.textViewIfNoMealsMessage);

        if (meals.isEmpty()) {
            textViewIfNoMealsMessage.setVisibility(View.VISIBLE);
        }

        textViewDate.setText(formatDate(date));

        // Subtract energy from all meals combined
        calculatePlannedNutrition();
        calculateConsumedNutrition();

        textViewWater.setText(String.valueOf(user.getWater()));

        // Animate progress bar
        animatePlannedBasalMetabolicRate(plannedEnergyBar);
        animateConsumedBasalMetabolicRate(consumedEnergyBar);

        adapter = new MealAdapter(getContext(), meals);
        adapter.setOnItemClickListener(new MealAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Meal meal = meals.get(position);
                Intent intent;

                if (meal.getRECIPE_ID() > 0) {
                    intent = new Intent(getContext(), RecipeDetailsActivity.class);
                    intent.putExtra(MenuActivity.RECIPE, meal.getRECIPE_ID() + "");
                    getActivity().startActivity(intent);
                } else {
                    intent = new Intent(getContext(), MealDetailsActivity.class);
                    intent.putExtra(MenuActivity.MEAL, meal);
                    getActivity().startActivity(intent);
                }

            }

            @Override
            public void onDeleteClick(int position) {
                Meal meal = meals.get(position);

                try {
                    new MealDeleteBackground(getContext(), user.getID(), meal.getID()).sendRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                helper.mealDelete(meal.getID());

                removeItem(position);

            }

            @Override
            public void onStatusChanged(int position, boolean status) {
                Meal meal = meals.get(position);
                meal.setStatus(status);

                if (meal.isStatus()) {
                    Toast.makeText(getContext(), "Posiłek zjedzony.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Posiłek zaplanowany.", Toast.LENGTH_SHORT).show();
                }

                helper.mealChangeStatus(meal.getID(), meal.isStatus());

                try {
                    new MealChangeStatus(getContext(), meal.getID(), meal.isStatus()).sendRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                calculateConsumedNutrition();
                verifyConsumedEnergy();
                animateConsumedBasalMetabolicRate(consumedEnergyBar);
            }
        });
        recyclerView = view.findViewById(R.id.recyclerViewMeals);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(25));
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);

        // Date
        LinearLayout layoutDate = view.findViewById(R.id.layoutDate);
        layoutDate.setOnClickListener(v -> openCalendar(MenuActivity.FLAG_CALCULATOR));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.meal_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.planAction:
                Intent intent = new Intent(getContext(), CreateMealActivity.class);
                intent.putExtra(MenuActivity.USER, user);
                getActivity().startActivityForResult(intent, REQUEST_MEAL);
                break;
            case R.id.detailsAction:

                if (meals.isEmpty()) {
                    Toast.makeText(getContext(), "Zaplanuj conajmniej jeden posiłek, aby zobaczyć szczegóły.", Toast.LENGTH_LONG).show();
                    break;
                }

                intent = new Intent(getContext(), NutritionDetailsActivity.class);
                intent.putExtra(MenuActivity.DATE, date);
                startActivity(intent);
                break;
            case R.id.shoppingListAction:

                if (meals.isEmpty()) {
                    Toast.makeText(getContext(), "Zaplanuj conajmniej jeden posiłek, aby wygenerować listę zakupów.", Toast.LENGTH_LONG).show();
                    break;
                }

                intent = new Intent(getContext(), ShoppingListActivity.class);
                intent.putExtra(MenuActivity.DATE, date);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    /**
     * Formats date in raw string.
     *
     * @param date
     * @return Formatted date.
     */
    public static String formatDate(String date) {
        String formattedDate = "";
        String[] buffer = date.split("-");
        int year = Integer.valueOf(buffer[0]);
        int month = Integer.valueOf(buffer[1]);
        int day = Integer.valueOf(buffer[2]);


        long dateToMillis = new DateTime(
                year,
                month,
                day,
                0,
                0,
                DateTimeZone.UTC).getMillis();
        Date millisToDate = new Date(dateToMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(millisToDate);

        // Day of week
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                formattedDate += "Niedziela, ";
                break;
            case 2:
                formattedDate += "Poniedziałek, ";
                break;
            case 3:
                formattedDate += "Wtorek, ";
                break;
            case 4:
                formattedDate += "Środa, ";
                break;
            case 5:
                formattedDate += "Czwartek, ";
                break;
            case 6:
                formattedDate += "Piątek, ";
                break;
            case 7:
                formattedDate += "Sobota, ";
                break;
        }

        // Year
        formattedDate += day + " ";

        // Month
        switch (month) {
            case 1:
                formattedDate += "Sty";
                break;
            case 2:
                formattedDate += "Lut";
                break;
            case 3:
                formattedDate += "Mar";
                break;
            case 4:
                formattedDate += "Kwi";
                break;
            case 5:
                formattedDate += "Maj";
                break;
            case 6:
                formattedDate += "Cze";
                break;
            case 7:
                formattedDate += "Lip";
                break;
            case 8:
                formattedDate += "Sie";
                break;
            case 9:
                formattedDate += "Wrz";
                break;
            case 10:
                formattedDate += "Paź";
                break;
            case 11:
                formattedDate += "Lis";
                break;
            case 12:
                formattedDate += "Gru";
                break;
        }

        // Day
        formattedDate += " " + year;

        return formattedDate;
    }

    /**
     * Animates TextView.
     *
     * @param initialValue
     * @param finalValue
     * @param textview
     */
    public static void animateTextView(int initialValue, int finalValue, final TextView  textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(valueAnimator1 -> textview.setText(valueAnimator1.getAnimatedValue().toString()));
        valueAnimator.start();
    }

    /**
     * Animate PlannedBasalMetabolicRateProgressBar.
     *
     * @param progressBar
     */
    private void animatePlannedBasalMetabolicRate(ProgressBar progressBar) {
        // Animate basal metabolic rate field
        int animateTo = (int) (basalMetabolicRate - plannedEnergy);
        animateTextView(0, animateTo, textViewBmr);
        verifyPlannedEnergy();

        // Animate percentage field
        animateTo = (int) ((100 * plannedEnergy) / basalMetabolicRate);
        animateTextView(0, animateTo, textViewPercentage);

        // Animate progress bar
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, 0, (float) plannedEnergy * 100);
        anim.setDuration(800);
        progressBar.setMax((int) basalMetabolicRate * 100);
        progressBar.startAnimation(anim);
    }

    /**
     * Animate ConsumedBasalMetabolicRateProgressBar
     * @param progressBar
     */
    private void animateConsumedBasalMetabolicRate(ProgressBar progressBar) {
        // Animate basal metabolic rate field
        verifyConsumedEnergy();

        // Animate percentage field
        int animateTo = (int) ((100 * consumedEnergy) / basalMetabolicRate);
        animateTextView(0, animateTo, textViewPercentage);

        // Animate progress bar
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, 0, (float) consumedEnergy * 100);
        anim.setDuration(800);
        progressBar.setMax((int) basalMetabolicRate * 100);
        progressBar.startAnimation(anim);
    }

    /**
     * Verify if energy from planned meals doesn't exceed basal metabolic rate.
     */
    private void verifyPlannedEnergy() {
        if (plannedEnergy > basalMetabolicRate) {
            int tmp = (int)(plannedEnergy - basalMetabolicRate);
            Toast.makeText(getContext(), "Zaplanowane posiłki przekraczają dzienne zapotrzebowanie kaloryczne o " + tmp + " kcal.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Verify if energy from consumed meals doesn't exceed basal metabolic rate.
     */
    private void verifyConsumedEnergy() {
        int animateTo = (int)(basalMetabolicRate - consumedEnergy);
        if (animateTo < 0) {
            animateTo *= (-1);
            Toast.makeText(getContext(), "Skonsumowane posiłki przekraczają dzienne zapotrzebowanie kaloryczne o " + animateTo + " kcal.", Toast.LENGTH_LONG).show();
            textViewBmr.setTextColor(Color.RED);
            animateTextView(0, animateTo, textViewBmr);
        } else {
            textViewBmr.setTextColor(this.getActivity().getResources().getColor(R.color.colorPrimary));
            animateTextView(0, animateTo, textViewBmr);
        }
    }

    /**
     * Remove an item from collection at specified position.
     * @param position
     */
    private void removeItem(int position) {
        meals.remove(position);

        if (meals.isEmpty()) {
            textViewIfNoMealsMessage.setVisibility(View.VISIBLE);
        }

        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();
        calculatePlannedNutrition();
        calculateConsumedNutrition();
        animatePlannedBasalMetabolicRate(plannedEnergyBar);
        animateConsumedBasalMetabolicRate(consumedEnergyBar);
    }

    /**
     * Sum up energy from planned meals.
     */
    private void calculatePlannedNutrition() {
        if (meals != null) {
            plannedEnergy = 0;

            for (Meal meal : meals) {
                plannedEnergy += meal.getEnergy();
            }
        }
    }

    /**
     * Sum up energy from consumed meals.
     */
    private void calculateConsumedNutrition() {
        if (meals != null) {
            consumedEnergy = 0;

            for (Meal meal : meals) {
                if (meal.isStatus()) {
                    consumedEnergy += meal.getEnergy();
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DailyMealsFragment.OnDailyMealsFragmentListener) {
            listener = (DailyMealsFragment.OnDailyMealsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnDailyMealsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /**
     * This interface must be implemented in main activity
     * in order to receive and send back USER type object.
     */
    public interface OnDailyMealsFragmentListener {
        void onDateClick(String flag);
    }

    /**
     * Opens calendar with given flag.
     * @param flag
     */
    public void openCalendar(String flag) {
        if (listener != null) {
            listener.onDateClick(flag);
        }
    }

}
