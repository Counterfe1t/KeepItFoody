package com.example.kuba.keepitfoody;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Application's main activity. Serves as fragment container and handles
 * navigation withing app. All activities backtrack to this starting point.
 */
public class MenuActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        UserProfileFragment.OnUserProfileInteractionListener,
        SettingsFragment.OnSettingsInteractionListener,
        CalendarViewFragment.OnCalendarViewInteractionListener,
        DailyMealsFragment.OnDailyMealsFragmentListener{

    // Static variables
    public static final String USER = "user";
    public static final String RECIPE = "recipe";
    public static final String INGREDIENT = "ingredients";
    public static final String MEAL = "meal";
    public static final String DATE = "date";
    public static final String IMAGE = "image";
    public static final String POSITION = "position";
    public static final String FLAG = "flag";
    public static final String FLAG_SHOPPING_LIST = "flag_shopping_list";
    public static final String FLAG_CALCULATOR = "flag_calculator";

    // Request codes
    public static final int REQUEST_MEAL = 100;
    public static final int REQUEST_INGREDIENT = 101;
    public static final int REQUEST_RECIPE = 102;
    public static final int REQUEST_IMAGE = 500;

    private DrawerLayout drawer;

    // Fragment management
    private FragmentManager manager;
    private FragmentTransaction transaction;

    // User
    private User user;

    // Layout
    private Toolbar toolbar;
    public NavigationView navigationView;
    private View header;

    private ImageView profileImage;
    private String userImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Setup locale config to polish
        Locale locale = new Locale("pl_PL");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);

        // Get user info
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Kalkulator żywieniowy");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.mainActivityLayout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        // Navigation header
        header = navigationView.getHeaderView(0);

        profileImage = header.findViewById(R.id.imageProfile);
        profileImage.setOnClickListener(v -> {
            openFragment(R.id.nav_profile);
            drawer.closeDrawer(GravityCompat.START);
        });
        if (!user.getProfilePicture().isEmpty() && !user.getProfilePicture().equals("null")) {
            String imageUrl = "https://192.168.134.62/api" + user.getProfilePicture();
            Picasso.with(this).load(imageUrl).fit().centerInside().into(profileImage);
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Fragment fragment = DailyMealsFragment.newInstance(user, getDate());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        navigationView.setCheckedItem(R.id.nav_daily_meals);

        if (!user.isSet()) {
            openFragment(R.id.nav_profile);
            navigationView.setCheckedItem(R.id.nav_profile);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView headerUsername = header.findViewById(R.id.navName);
        headerUsername.setText(user.getName());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        openFragment(item.getItemId());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Overriding android back button.
     */
    @Override
    public void onBackPressed() {
        if (drawer != null) {
            manager = getSupportFragmentManager();
            transaction = manager.beginTransaction();


            if (user.isSet()) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (manager.getBackStackEntryCount() > 0) {
                    // Layout decorations
                    toolbar.setTitle("Kalkulator żywieniowy");

                    manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    transaction.setTransition(R.anim.exit_to_right);
                    transaction.commit();
                } else {
                    super.onBackPressed();
                }
            } else {
                finish();
            }
        }
    }

    /**
     * This method receives id of an item from create_meal_menu drawer
     * and accordingly puts appropriate fragment into main activity
     * frame layout.
     *
     * @param id clicked create_meal_menu drawer item id
     */
    public void openFragment(int id) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Fragment fragment = null;
        Intent intent;

        if (user.isSet()) {
            switch (id) {
                case R.id.nav_daily_meals:
                    toolbar.setTitle("Kalkulator żywieniowy");
                    fragment = DailyMealsFragment.newInstance(user, getDate());
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container, fragment);
                    transaction.commit();
                    break;
                case R.id.nav_calendar:
                    toolbar.setTitle("Kalendarz");
                    fragment = CalendarViewFragment.newInstance(MenuActivity.FLAG_CALCULATOR);
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container, fragment);
                    transaction.commit();
                    break;
                case R.id.nav_recipes:
                    toolbar.setTitle("Przepisy");
                    fragment = BrowseRecipesFragment.newInstance();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container, fragment);
                    transaction.commit();
                    break;
                case R.id.nav_ingredients:
                    toolbar.setTitle("Składniki");
                    fragment = BrowseIngredientsFragment.newInstance();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container, fragment);
                    transaction.commit();
                    break;
                case R.id.nav_shopping_list:
                    toolbar.setTitle("Kalendarz");
                    fragment = CalendarViewFragment.newInstance(MenuActivity.FLAG_SHOPPING_LIST);
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container, fragment);
                    transaction.commit();
                    break;
                case R.id.nav_profile:
                    toolbar.setTitle("Profil użytkownika");
                    fragment = UserProfileFragment.newInstance(user, userImage);
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container, fragment);
                    transaction.commit();
                    break;
                case R.id.nav_settings:
                    toolbar.setTitle("Ustawienia");
                    fragment = SettingsFragment.newInstance();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container, fragment);
                    transaction.commit();
                    break;
                case R.id.nav_specialist:
                    intent = new Intent(this, BrowseSpecialistsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_about:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://aleks-2.mat.umk.pl/pz2018/zesp04/"));
                    startActivity(intent);
                    break;
                case R.id.nav_sign_out:
                    signOut();
                    break;
            }
        } else {
            toolbar.setTitle("Profil użytkownika");
            fragment = UserProfileFragment.newInstance(user, userImage);
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.container, fragment);
            transaction.commit();

            Toast.makeText(this, "Dokończ konfigurowanie profilu użykownika zanim zaczniesz korzystać z aplikacji!", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Sign currently logged in user and opens LoginActivity
     */
    public void signOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Send updated user data to MySQL BACKEND database
    private void userUpdate() {
        new UserUpdateBackground(this, user).parseJSON();
    }

    /**
     * Get today's date in string format
     * @return date in string format
     */
    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        String date = "";
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        date += year + "-";

        if (month < 10) {
            date += "0" + month + "-";
        } else {
            date += month + "-";
        }

        if (day < 10) {
            date += "0" + day;
        } else {
            date += day + "";
        }

        return date;
    }

    /**
     * Get current time in string.
     * @return current time in string format
     */
    public static String getTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String time = "";
        int hours = date.getHours();
        int minutes = date.getMinutes();

        time = hours + ":";
        if (minutes < 10) time += "0" + minutes;
        else time += minutes;

        return time;
    }

    /**
     * This method allows passing data between this activity
     * and every child activity this one may produce.
     *
     * @param requestCode   code necessary to identify the request
     * @param resultCode    code necessary to identify incoming results
     * @param data          extra data from intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Get added meal from AddMealActivity
        if (requestCode == REQUEST_MEAL) {
            if (resultCode == RESULT_OK) {

                Meal meal = data.getParcelableExtra(MEAL);

                try {
                    new MealPostBackground(this, meal, user.getID()).sendRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(this, SplashScreenActivity.class);
                intent.putExtra(MenuActivity.USER, user);
                this.startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
                finish();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Anulowano.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_INGREDIENT) {

            if (resultCode == RESULT_OK) {
                Ingredient ingredient = data.getParcelableExtra(INGREDIENT);
                try {
                    new IngredientPostBackground(this, ingredient).sendRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Anulowano.", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == REQUEST_RECIPE) {

            if (resultCode == RESULT_OK) {

                Intent intent = new Intent(this, SplashScreenActivity.class);
                intent.putExtra(MenuActivity.USER, user);
                this.startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
                finish();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Anulowano.", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == REQUEST_IMAGE) {

            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                profileImage.setImageURI(imageUri);

                //Encoding picture into Base64 String
                profileImage.buildDrawingCache();
                Bitmap bitmap = profileImage.getDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encodedPicture = Base64.encodeToString(byteArray, Base64.DEFAULT);
                String image = encodedPicture.replace("\n", "");
                userImage = imageUri.toString();

                new UserUpdatePictureBackground(this, user, image).parseJSON();

                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                toolbar.setTitle("Profil użytkownika");
                Fragment fragment = UserProfileFragment.newInstance(user, imageUri.toString());
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                transaction.addToBackStack(null);
                transaction.replace(R.id.container, fragment);
                transaction.commit();


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Anulowano.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    /**
     * This method allows passing USER type object
     * between UserProfileFragment and MenuActivity
     * through public interface.
     */
    @Override
    public void onUserProfileInteraction(User user) {
        // Save changes made to user and send them to database
        this.user = user;
        TextView headerUsername = header.findViewById(R.id.navName);
        headerUsername.setText(user.getName());
        userUpdate();
    }

    /**
     * This method allows communication
     * between SettingsFragment and MenuActivity
     * through public interface.
     */
    @Override
    public void onSettingsInteraction() {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.putExtra(MenuActivity.USER, user);
        this.startActivity(intent);
        finish();
    }

    /**
     * Open calendar fragment in normal mode.
     */
    @Override
    public void onCalendarViewInteraction(String date) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        toolbar.setTitle("Kalkulator kalorii");

        Fragment fragment = DailyMealsFragment.newInstance(user, date);
        transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_right)
                .addToBackStack(null)
                .replace(R.id.container, fragment)
                .commit();
    }

    /**
     * Open calendar in shopping list mode.
     */
    @Override
    public void onCalendarViewInteraction2(String date) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Intent intent = new Intent(this, ShoppingListActivity.class);
        intent.putExtra(MenuActivity.DATE, date);
        startActivity(intent);
    }

    /**
     * Open Calendar from within DailyMealsFragment.
     */
    public void openCalendar(String flag) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        toolbar.setTitle("Kalendarz");

        Fragment fragment = CalendarViewFragment.newInstance(flag);
        transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_right)
                .addToBackStack(null)
                .replace(R.id.container, fragment)
                .commit();
    }

    /**
     * This method allows communication
     * between DailyMealsFragment and MenuActivity
     * through public int
     */
    @Override
    public void onDateClick(String flag) {
        openCalendar(flag);
    }
}