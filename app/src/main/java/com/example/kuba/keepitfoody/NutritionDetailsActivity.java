package com.example.kuba.keepitfoody;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Activity used to provide user with very detailed information
 * about meals' nutrition from specified date.
 */
public class NutritionDetailsActivity extends AppCompatActivity {

    private User user;
    private ArrayList<Meal> meals = new ArrayList<>();
    private FoodyDatabaseHelper helper;

    private double proteinRequirement;
    private double carbohydratesRequirement;
    private double fatsRequirement;

    private int plannedEnergy;
    private double plannedProtein ;
    private double plannedFats;
    private double plannedCarbohydrates;
    private double plannedFibre;
    private double plannedSalt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_details);
        setTitle("Szczegóły");

        Intent intent = getIntent();
        String date = intent.getStringExtra(MenuActivity.DATE);

        helper = FoodyDatabaseHelper.getInstance(this);
        user = helper.fetchUser();
        meals = helper.fetchMeals(date);

        calculatePlannedNutrition();
        calculateNutritionRequirement();

        TextView textViewDate = findViewById(R.id.textViewDate);
        textViewDate.setText(DailyMealsFragment.formatDate(date));

        TextView energy = findViewById(R.id.energy);
        TextView protein = findViewById(R.id.protein);
        TextView fats = findViewById(R.id.fats);
        TextView carbohydrates = findViewById(R.id.carbohydrates);
        TextView fibre = findViewById(R.id.fibre);
        TextView salt = findViewById(R.id.salt);

        energy.setText(String.valueOf(plannedEnergy));
        protein.setText(String.valueOf((int)roundDouble(plannedProtein)));
        fats.setText(String.valueOf((int)roundDouble(plannedFats)));
        carbohydrates.setText(String.valueOf((int)roundDouble(plannedCarbohydrates)));
        fibre.setText(String.valueOf((int)roundDouble(plannedFibre)));
        salt.setText(String.valueOf((int)roundDouble(plannedSalt)));

        TextView textViewEnergy = findViewById(R.id.textViewEnergy);
        textViewEnergy.setText(plannedEnergy + " / " + user.getBasalMetabolicRate() + " kcal");
        TextView textViewEnergyPercentage = findViewById(R.id.textViewEnergyPercentage);
        ProgressBar progressBarEnergy = findViewById(R.id.progressBarEnergy);
        animateProgressBar(progressBarEnergy, textViewEnergyPercentage, plannedEnergy, user.getBasalMetabolicRate());

        if (plannedEnergy > user.getBasalMetabolicRate()) {
            TextView textViewEnergyMessage = findViewById(R.id.textViewEnergyMessage);
            int tmp = plannedEnergy - user.getBasalMetabolicRate();
            textViewEnergyMessage.setText("Przekroczono zapotrzebowanie kaloryczne o " + tmp + " kcal");
            textViewEnergyMessage.setVisibility(View.VISIBLE);
        }

        TextView textViewProtein = findViewById(R.id.textViewProtein);
        textViewProtein.setText((int)plannedProtein + " / " + (int)proteinRequirement + " g");
        TextView textViewProteinPercentage = findViewById(R.id.textViewProteinPercentage);
        ProgressBar progressBarProtein = findViewById(R.id.progressBarProtein);
        animateProgressBar(progressBarProtein, textViewProteinPercentage, plannedProtein, proteinRequirement);

        if (plannedProtein > proteinRequirement) {
            TextView textViewProteinMessage = findViewById(R.id.textViewProteinMessage);
            double tmp = plannedProtein - proteinRequirement;
            textViewProteinMessage.setText("Przekroczono zapotrzebowanie na białko o " + (int)tmp + " g");
            textViewProteinMessage.setVisibility(View.VISIBLE);
        }

        TextView textViewCarbohydrates = findViewById(R.id.textViewCarbohydrates);
        textViewCarbohydrates.setText((int)plannedCarbohydrates + " / " + (int)carbohydratesRequirement + " g");
        TextView textViewCarbohydratesPercentage = findViewById(R.id.textViewCarbohydratesPercentage);
        ProgressBar progressBarCarbohydrates = findViewById(R.id.progressBarCarbohydrates);
        animateProgressBar(progressBarCarbohydrates, textViewCarbohydratesPercentage, plannedCarbohydrates, carbohydratesRequirement);

        if (plannedCarbohydrates > carbohydratesRequirement) {
            TextView textViewCarbohydratesMessage = findViewById(R.id.textViewCarbohydratesMessage);
            double tmp = plannedCarbohydrates - carbohydratesRequirement;
            textViewCarbohydratesMessage.setText("Przekroczono zapotrzebowanie na węglowodany o " + (int)tmp + " g");
            textViewCarbohydratesMessage.setVisibility(View.VISIBLE);
        }

        TextView textViewFats = findViewById(R.id.textViewFats);
        textViewFats.setText((int)plannedFats + " / " + (int)fatsRequirement + " g");
        TextView textViewFatsPercentage = findViewById(R.id.textViewFatsPercentage);
        ProgressBar progressBarFats = findViewById(R.id.progressBarFats);
        animateProgressBar(progressBarFats, textViewFatsPercentage, plannedFats, fatsRequirement);

        if (plannedFats > fatsRequirement) {
            TextView textViewFatsMessage = findViewById(R.id.textViewFatsMessage);
            double tmp = plannedFats - fatsRequirement;
            textViewFatsMessage.setText("Przekroczono zapotrzebowanie na tłuszcze o " + (int)tmp + " g");
            textViewFatsMessage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Method used to animate ProgressBar object.
     * @param progressBar ProgressBar to animate
     * @param textView Corresponding TextView to animate
     * @param animateTo Animate from 0 to this value
     * @param animateMax
     */
    private void animateProgressBar(final ProgressBar progressBar, final TextView textView, double animateTo, double animateMax) {
        // Animate percentage field
        int tmp = (int) ((100 * animateTo) / animateMax);
        if (tmp > 100) {
            DailyMealsFragment.animateTextView(0, 100, textView);
        } else {
            DailyMealsFragment.animateTextView(0, tmp, textView);
        }

        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, 0, (float) animateTo * 100);
        anim.setDuration(800);
        progressBar.setMax((int) animateMax * 100);
        progressBar.startAnimation(anim);
    }

    /**
     * Round double to specified precision and return new value.
     * @param value
     * @return Rounded value
     */
    public static double roundDouble(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(0, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Add up nutrition details from all planned meals.
     */
    private void calculatePlannedNutrition() {
        if (meals != null) {
            plannedEnergy = 0;
            plannedProtein = 0;
            plannedFats = 0;
            plannedCarbohydrates = 0;
            plannedFibre = 0;
            plannedSalt = 0;

            for (Meal meal : meals) {
                plannedEnergy += meal.getEnergy();
                plannedProtein += meal.getProtein();
                plannedFats += meal.getFats();
                plannedCarbohydrates += meal.getCarbohydrates();
                plannedFibre += meal.getFibre();
                plannedSalt += meal.getSalt();
            }

            plannedProtein = roundDouble(plannedProtein);
            plannedFats = roundDouble(plannedFats);
            plannedCarbohydrates = roundDouble(plannedCarbohydrates);
            plannedFibre = roundDouble(plannedFibre);
            plannedSalt = roundDouble(plannedSalt);
        }
    }

    /**
     * Calculate user requirement for protein, fats and carbohydrates.
     */
    private void calculateNutritionRequirement() {
        int basalMetabolicRate = user.getBasalMetabolicRate();
        proteinRequirement = roundDouble((basalMetabolicRate * 0.3) / 4);
        carbohydratesRequirement = roundDouble((basalMetabolicRate * 0.4) / 4);
        fatsRequirement = roundDouble((basalMetabolicRate * 0.3) / 9);
    }
}
