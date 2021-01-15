package com.example.kuba.keepitfoody;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * This class is used to create and manage local SQLite database.
 * It handles saving data received from server to database as well as fetching data
 * when called from specified context.
 */
public class FoodyDatabaseHelper extends SQLiteOpenHelper {

    private static FoodyDatabaseHelper instance;
    private SQLiteDatabase database;

    // FOODY DATABASE
    public static final String DATABASE_NAME = "keepitfoody.db";
    public static final int DATABASE_VERSION = 1;

    //TABLE USER
    public static final String USER_TABLE_NAME = "USER";
    public static final String USER_COLUMN_ID = "ID";
    public static final String USER_COLUMN_ID_USER = "ID_USER";
    public static final String USER_COLUMN_EMAIL = "EMAIL";
    public static final String USER_COLUMN_NAME = "NAME";
    public static final String USER_COLUMN_SEX = "SEX";
    public static final String USER_COLUMN_PASSWORD = "PASSWORD";
    public static final String USER_COLUMN_TOKEN = "TOKEN";
    public static final String USER_COLUMN_BMR = "BMR";
    public static final String USER_COLUMN_WATER = "WATER";
    public static final String USER_COLUMN_DATE_OF_BIRTH = "DATE_OF_BIRTH";
    public static final String USER_COLUMN_WEIGHT = "WEIGHT";
    public static final String USER_COLUMN_HEIGHT = "HEIGHT";
    public static final String USER_COLUMN_PICTURE = "PICTURE";

    // TABLE INGREDIENTS
    public static final String INGREDIENTS_TABLE_NAME = "INGREDIENTS";
    public static final String INGREDIENTS_COLUMN_ID = "ID";
    public static final String INGREDIENTS_COLUMN_NAME = "NAME";
    public static final String INGREDIENTS_COLUMN_CATEGORY = "CATEGORY";
    public static final String INGREDIENTS_COLUMN_ENERGY = "ENERGY";
    public static final String INGREDIENTS_COLUMN_PROTEIN = "PROTEIN";
    public static final String INGREDIENTS_COLUMN_CARBOHYDRATES = "CARBOHYDRATES";
    public static final String INGREDIENTS_COLUMN_FIBRE = "FIBRE";
    public static final String INGREDIENTS_COLUMN_FATS = "FATS";
    public static final String INGREDIENTS_COLUMN_SALT = "SALT";
    public static final String INGREDIENTS_COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String INGREDIENTS_COLUMN_GLUTEN = "GLUTEN";
    public static final String INGREDIENTS_COLUMN_LACTOSE = "LACTOSE";

    // TABLE RECIPES
    public static final String RECIPES_TABLE_NAME = "RECIPES";
    public static final String RECIPES_COLUMN_ID = "ID";
    public static final String RECIPES_COLUMN_NAME = "NAME";
    public static final String RECIPES_COLUMN_DESCRIPTION = "CATEGORY";
    public static final String RECIPES_COLUMN_PHOTO = "PHOTO";
    public static final String RECIPES_COLUMN_PREPARATION_TIME = "PREPARATION_TIME";
    public static final String RECIPES_COLUMN_DIFFICULTY = "DIFFICULTY";
    public static final String RECIPES_COLUMN_AUTHOR_ID = "AUTHOR_ID";
    public static final String RECIPES_COLUMN_AUTHOR_PHOTO = "AUTHOR_PHOTO";
    public static final String RECIPES_COLUMN_AUTHOR_FIRST_NAME = "AUTHOR_FIRST_NAME";
    public static final String RECIPES_COLUMN_INGREDIENTS = "INGREDIENTS";

    // TABLE MEALS
    public static final String MEALS_TABLE_NAME = "MEALS";
    public static final String MEALS_COLUMN_ID = "ID";
    public static final String MEALS_COLUMN_NAME = "NAME";
    public static final String MEALS_COLUMN_DATE = "DATE";
    public static final String MEALS_COLUMN_TIME = "TIME";
    public static final String MEALS_COLUMN_RECIPE_ID = "RECIPE_ID";
    public static final String MEALS_COLUMN_INGREDIENTS = "INGREDIENTS";
    public static final String MEALS_COLUMN_STATUS = "STATUS";

    // TABLE SPECIALISTS
    public static final String SPECIALISTS_TABLE_NAME = "SPECIALISTS";
    public static final String SPECIALISTS_COLUMN_ID = "ID";
    public static final String SPECIALISTS_COLUMN_EMAIL = "EMAIL";
    public static final String SPECIALISTS_COLUMN_FIRST_NAME = "FIRST_NAME";
    public static final String SPECIALISTS_COLUMN_LAST_NAME = "LAST_NAME";
    public static final String SPECIALISTS_COLUMN_PROFESSION = "PROFESSION";
    public static final String SPECIALISTS_COLUMN_SPECIALIZATION = "SPECIALIZATION";
    public static final String SPECIALISTS_COLUMN_PICTURE = "PICTURE";


    public static synchronized FoodyDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new FoodyDatabaseHelper(context.getApplicationContext());
        }

        return instance;
    }

    public FoodyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE_NAME + " (" +
                USER_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                USER_COLUMN_ID_USER + " INTEGER, " +
                USER_COLUMN_EMAIL + " TEXT NOT NULL, " +
                USER_COLUMN_NAME + " TEXT, " +
                USER_COLUMN_SEX + " BOOLEAN, " +
                USER_COLUMN_PASSWORD + " TEXT, " +
                USER_COLUMN_BMR + " INTEGER, " +
                USER_COLUMN_WATER + " DOUBLE, " +
                USER_COLUMN_DATE_OF_BIRTH + " TEXT, " +
                USER_COLUMN_WEIGHT + " DOUBLE, " +
                USER_COLUMN_HEIGHT + " INTEGER, " +
                USER_COLUMN_PICTURE + " TEXT, " +
                USER_COLUMN_TOKEN + " TEXT " +
                ");";

        final String SQL_CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + INGREDIENTS_TABLE_NAME + " (" +
                INGREDIENTS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                INGREDIENTS_COLUMN_NAME + " TEXT NOT NULL, " +
                INGREDIENTS_COLUMN_CATEGORY + " TEXT, " +
                INGREDIENTS_COLUMN_ENERGY + " DOUBLE, " +
                INGREDIENTS_COLUMN_PROTEIN + " DOUBLE, " +
                INGREDIENTS_COLUMN_FATS + " DOUBLE, " +
                INGREDIENTS_COLUMN_CARBOHYDRATES + " DOUBLE, " +
                INGREDIENTS_COLUMN_FIBRE + " DOUBLE, " +
                INGREDIENTS_COLUMN_SALT + " DOUBLE, " +
                INGREDIENTS_COLUMN_DESCRIPTION + " TEXT, " +
                INGREDIENTS_COLUMN_GLUTEN + " BOOLEAN, " +
                INGREDIENTS_COLUMN_LACTOSE + " BOOLEAN" +
                ");";

        final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " + RECIPES_TABLE_NAME + " (" +
                RECIPES_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                RECIPES_COLUMN_NAME + " TEXT NOT NULL, " +
                RECIPES_COLUMN_DESCRIPTION + " TEXT, " +
                RECIPES_COLUMN_PHOTO + " TEXT, " +
                RECIPES_COLUMN_PREPARATION_TIME + " INTEGER, " +
                RECIPES_COLUMN_DIFFICULTY + " INTEGER, " +
                RECIPES_COLUMN_AUTHOR_ID + " INTEGER, " +
                RECIPES_COLUMN_AUTHOR_PHOTO + " TEXT, " +
                RECIPES_COLUMN_AUTHOR_FIRST_NAME + " TEXT, " +
                RECIPES_COLUMN_INGREDIENTS + " TEXT " +
                ");";

        final String SQL_CREATE_MEALS_TABLE = "CREATE TABLE " + MEALS_TABLE_NAME + " (" +
                MEALS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MEALS_COLUMN_NAME + " TEXT NOT NULL, " +
                MEALS_COLUMN_DATE + " TEXT NOT NULL, " +
                MEALS_COLUMN_TIME + " TEXT NOT NULL, " +
                MEALS_COLUMN_INGREDIENTS + " TEXT NOT NULL, " +
                MEALS_COLUMN_STATUS + " BOOLEAN NOT NULL, " +
                MEALS_COLUMN_RECIPE_ID + " INTEGER " +
                ");";

        final String SQL_CREATE_SPECIALISTS_TABLE = "CREATE TABLE " + SPECIALISTS_TABLE_NAME + " (" +
                SPECIALISTS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                SPECIALISTS_COLUMN_EMAIL + " TEXT NOT NULL," +
                SPECIALISTS_COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                SPECIALISTS_COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                SPECIALISTS_COLUMN_PROFESSION + " TEXT NOT NULL, " +
                SPECIALISTS_COLUMN_SPECIALIZATION + " TEXT NOT NULL, " +
                SPECIALISTS_COLUMN_PICTURE + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
        db.execSQL(SQL_CREATE_RECIPES_TABLE);
        db.execSQL(SQL_CREATE_MEALS_TABLE);
        db.execSQL(SQL_CREATE_SPECIALISTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + INGREDIENTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RECIPES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MEALS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SPECIALISTS_TABLE_NAME);
        onCreate(db);
    }

    public Cursor getUser() {
        return database.query(
                USER_TABLE_NAME,
                null,
                USER_COLUMN_ID + "=\"" + 1 + "\"",
                null,
                null,
                null,
                null);
    }

    public void updateProfilePicture(String picture) {
        ContentValues cv = new ContentValues();
        cv.put(FoodyDatabaseHelper.USER_COLUMN_PICTURE, picture);
        database.update(FoodyDatabaseHelper.USER_TABLE_NAME, cv, "id = " + 1, null);
    }

    public Cursor getRecipeIngredientsNames(final int RECIPE_ID) {
        String [] columns = { RECIPES_COLUMN_INGREDIENTS };

        return database.query(
                RECIPES_TABLE_NAME,
                columns,
                INGREDIENTS_COLUMN_ID + "=\"" + RECIPE_ID + "\"",
                null,
                null,
                null,
                 null);
    }

    public Cursor getIngredientByID(final int ID) {
        return database.query(
                INGREDIENTS_TABLE_NAME,
                null,
                INGREDIENTS_COLUMN_ID + "=\"" + ID + "\"",
                null,
                null,
                null,
                null);
    }

    public Cursor getAllIngredients() {
        return database.query(
                INGREDIENTS_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                INGREDIENTS_COLUMN_NAME + " ASC");
    }

    public Cursor getAllRecipes() {
        return database.query(
                RECIPES_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                RECIPES_COLUMN_NAME + " ASC");
    }

    public Cursor getRecipeById(int recipeId) {
        return database.query(
                RECIPES_TABLE_NAME,
                null,
                RECIPES_COLUMN_ID + "=\"" + recipeId + "\"",
                null,
                null,
                null,
                null);
    }

    public Cursor getAllMeals() {
        return database.query(
                MEALS_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MEALS_COLUMN_DATE + " ASC");
    }

    public Cursor getMealsFromDate(String date) {
        return database.query(
                MEALS_TABLE_NAME,
                null,
                MEALS_COLUMN_DATE + " LIKE '" + date + "%'",
                null,
                null,
                null,
                MEALS_COLUMN_TIME + " ASC");
    }

    public Cursor getAllDays() {
        String[] columns = { MEALS_COLUMN_DATE };
        return database.query(
                MEALS_TABLE_NAME,
                columns,
                null,
                null,
                MEALS_COLUMN_DATE,
                null,
                null);
    }

    public boolean mealDelete(final int ID) {
        return database.delete(
                MEALS_TABLE_NAME,
                MEALS_COLUMN_ID + "=" + ID,
                null) > 0;
    }

    public void mealChangeStatus(final int mealId, boolean status) {
        ContentValues cv = new ContentValues();
        cv.put(FoodyDatabaseHelper.MEALS_COLUMN_STATUS, status);
        database.update(FoodyDatabaseHelper.MEALS_TABLE_NAME, cv, "id = " + mealId, null);
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }


    public Cursor getAllSpecialists() {
        return database.query(
                SPECIALISTS_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                SPECIALISTS_COLUMN_LAST_NAME + " ASC");
    }

    public Cursor getSpecialistById(int specialistId) {
        return database.query(
                SPECIALISTS_TABLE_NAME,
                null,
                SPECIALISTS_COLUMN_ID + "=\"" + specialistId + "\"",
                null,
                null,
                null,
                SPECIALISTS_COLUMN_LAST_NAME + " ASC");
    }

    /**
     * Fetch specialist from local database.
     * @param specialistId
     * @return An object of type Specialist
     */
    public Specialist fetchSpecialist(int specialistId) {
        Specialist specialist = new Specialist();
        Cursor cursor = getSpecialistById(specialistId);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int ID = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_ID));
        String email = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_EMAIL));
        String firstName = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_LAST_NAME));
        String profession = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_PROFESSION));
        String specialization = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_SPECIALIZATION));
        String picture = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_PICTURE));

        specialist.setID(ID);
        specialist.setEmail(email);
        specialist.setFirstName(firstName);
        specialist.setLastName(lastName);
        specialist.setProfession(profession);
        specialist.setSpecialization(specialization);
        specialist.setPicture(picture);
        return  specialist;
    }

    /**
     * Fetch all specialists from local database.
     * @return A collection of type Specialist
     */
    public ArrayList<Specialist> fetchSpecialists() {
        ArrayList<Specialist> specialists = new ArrayList<>();
        Specialist specialist;
        Cursor cursor = getAllSpecialists();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int ID = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_ID));
            String email = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_EMAIL));
            String firstName = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_LAST_NAME));
            String profession = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_PROFESSION));
            String specialization = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_SPECIALIZATION));
            String picture = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.SPECIALISTS_COLUMN_PICTURE));

            specialist = new Specialist();
            specialist.setID(ID);
            specialist.setEmail(email);
            specialist.setFirstName(firstName);
            specialist.setLastName(lastName);
            specialist.setProfession(profession);
            specialist.setSpecialization(specialization);
            specialist.setPicture(picture);
            specialists.add(specialist);
        }

        return specialists;
    }

    /**
     * Fetch recipes from local database by given date and map
     * to Meal class object.
     * @return A collection of type Recipe
     */
    public ArrayList<Recipe> fetchRecipes() {
        Recipe recipe;
        ArrayList<Recipe> recipes = new ArrayList<>();
        Cursor cursor = getAllRecipes();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int ID = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_DESCRIPTION));
            String image = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_PHOTO));
            int preparationTime = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_PREPARATION_TIME));
            int difficulty = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_DIFFICULTY));
            int AUTHOR_ID = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_AUTHOR_ID));
            String authorImage = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_AUTHOR_PHOTO));
            String authorFirstName = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_AUTHOR_FIRST_NAME));

            recipe = new Recipe();
            recipe.setID(ID);
            recipe.setName(name);
            recipe.setDescription(description);
            recipe.setImage(image);
            recipe.setPreparationTime(preparationTime);
            recipe.setDifficulty(difficulty);
            recipe.setAUTHOR_ID(AUTHOR_ID);
            recipe.setAuthorImage(authorImage);
            recipe.setAuthorFirstName(authorFirstName);
            recipes.add(recipe);
        }

        return recipes;
    }

    /**
     * Fetch recipe from local database by id.
     * @param recipeId
     * @return An object of type Recipe
     */
    public Recipe fetchRecipe(int recipeId) {
        Cursor cursor = getRecipeById(recipeId);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int ID = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_NAME));
        String description = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_DESCRIPTION));
        String image = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_PHOTO));
        int preparationTime = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_PREPARATION_TIME));
        int difficulty = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_DIFFICULTY));
        int AUTHOR_ID = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_AUTHOR_ID));
        String authorImage = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_AUTHOR_PHOTO));
        String authorFirstName = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_AUTHOR_FIRST_NAME));

        Recipe recipe = new Recipe();
        recipe.setID(ID);
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setImage(image);
        recipe.setPreparationTime(preparationTime);
        recipe.setDifficulty(difficulty);
        recipe.setAUTHOR_ID(AUTHOR_ID);
        recipe.setAuthorImage(authorImage);
        recipe.setAuthorFirstName(authorFirstName);

        return recipe;
    }

    /**
     * Fetch all ingredients assigned to recipe where id is given in parameter.
     * @param recipeId
     * @return A collection of type Ingredient
     */
    public ArrayList<Ingredient> fetchRecipeIngredients(int recipeId) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient;

        Cursor cursor = getRecipeIngredientsNames(recipeId);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String recipeIngredients = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.RECIPES_COLUMN_INGREDIENTS));

        String [] buffer = new String[0];
        if (recipeIngredients != null) {
            buffer = recipeIngredients.split(":");
        }

        for (String string : buffer) {
            if (recipeIngredients.isEmpty()) break;

            int id_ingredient = Integer.valueOf(string.split(",")[0]);
            int quantity = Integer.valueOf(string.split(",")[1]);

            cursor = getIngredientByID(id_ingredient);

            if (cursor != null) {
                cursor.moveToFirst();
            }

            int ID = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_NAME));
            String category = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_CATEGORY));

            int icon;
            if (category.contains("Mięso")) {
                icon = R.drawable.ic_meat;
            } else if (category.contains("Warzywa")) {
                icon = R.drawable.ic_vegetables;
            } else if (category.contains("Owoce")) {
                icon = R.drawable.ic_fruits;
            } else if (category.contains("Nabiał")) {
                icon = R.drawable.ic_dairy;
            } else if (category.contains("Cukier")) {
                icon = R.drawable.ic_snacks;
            } else if (category.contains("Produkty zbożowe")) {
                icon = R.drawable.ic_grain;
            } else if (category.contains("Tłuszcze")) {
                icon = R.drawable.ic_fats;
            } else if (category.contains("Bakalie i nasiona")) {
                icon = R.drawable.ic_nuts;
            } else if (category.contains("Ryby")) {
                icon = R.drawable.ic_fish;
            } else if (category.contains("Napoje")) {
                icon = R.drawable.ic_beverages;
            } else {
                category = "Pozostałe";
                icon = R.drawable.ic_recipe;
            }

            double energy = cursor.getDouble(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_ENERGY));
            double protein = cursor.getDouble(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_PROTEIN));
            double fats = cursor.getDouble(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_FATS));
            double carbohydrates = cursor.getDouble(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_CARBOHYDRATES));
            double fibre = cursor.getDouble(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_FIBRE));
            double salt = cursor.getDouble(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_SALT));
            String description = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_DESCRIPTION));

            int tmp = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_GLUTEN));
            boolean gluten;
            if (tmp == 0) {
                gluten = false;
            } else {
                gluten = true;
            }

            tmp = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_LACTOSE));
            boolean lactose;
            if (tmp == 0) {
                lactose = false;
            } else {
                lactose = true;
            }

            ingredient = new Ingredient();
            ingredient.setID(ID);
            ingredient.setName(name);
            ingredient.setCategory(category);
            ingredient.setIcon(icon);
            ingredient.setEnergy(energy);
            ingredient.setProtein(protein);
            ingredient.setFats(fats);
            ingredient.setCarbohydrates(carbohydrates);
            ingredient.setFibre(fibre);
            ingredient.setSalt(salt);
            ingredient.setDescription(description);
            ingredient.setGluten(gluten);
            ingredient.setLactose(lactose);
            ingredient.setQuantity(quantity);
            ingredients.add(ingredient);
        }

        return ingredients;
    }

    /**
     * Fetch currently logged in user's data from database.
     * @return An object of type User
     */
    public User fetchUser() {
        User user = new User();
        Cursor cursor = getUser();

        if (cursor != null) {
            cursor.moveToFirst();
        }

        user.setID(cursor.getInt(cursor.getColumnIndex(USER_COLUMN_ID_USER)));
        user.setName(cursor.getString(cursor.getColumnIndex(USER_COLUMN_NAME)));
        int tmp = cursor.getInt(cursor.getColumnIndex(USER_COLUMN_SEX));
        user.setSex(tmp != 0);
        user.setEmail(cursor.getString(cursor.getColumnIndex(USER_COLUMN_EMAIL)));
        user.setBasalMetabolicRate(cursor.getInt(cursor.getColumnIndex(USER_COLUMN_BMR)));
        user.setWater(cursor.getDouble(cursor.getColumnIndex(USER_COLUMN_WATER)));
        user.setDateOfBirth(cursor.getString(cursor.getColumnIndex(USER_COLUMN_DATE_OF_BIRTH)));
        user.setWeight(cursor.getDouble(cursor.getColumnIndex(USER_COLUMN_WEIGHT)));
        user.setHeight(cursor.getInt(cursor.getColumnIndex(USER_COLUMN_HEIGHT)));
        user.setProfilePicture(cursor.getString(cursor.getColumnIndex(USER_COLUMN_PICTURE)));
        return user;
    }

    /**
     * Fetch all ingredients from database.
     * @return A collection of type Ingredient
     */
    public ArrayList<Ingredient> fetchIngredients() {
        Ingredient ingredient;
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Cursor cursor = getAllIngredients();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int ID = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_NAME));
            String category = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_CATEGORY));

            int icon;
            if (category.contains("Mięso")) {
                icon = R.drawable.ic_meat;
            } else if (category.contains("Warzywa")) {
                icon = R.drawable.ic_vegetables;
            } else if (category.contains("Owoce")) {
                icon = R.drawable.ic_fruits;
            } else if (category.contains("Nabiał")) {
                icon = R.drawable.ic_dairy;
            } else if (category.contains("Cukier")) {
                icon = R.drawable.ic_snacks;
            } else if (category.contains("Produkty zbożowe")) {
                icon = R.drawable.ic_grain;
            } else if (category.contains("Tłuszcze")) {
                icon = R.drawable.ic_fats;
            } else if (category.contains("Bakalie i nasiona")) {
                icon = R.drawable.ic_nuts;
            } else if (category.contains("Ryby")) {
                icon = R.drawable.ic_fish;
            } else if (category.contains("Napoje")) {
                icon = R.drawable.ic_beverages;
            } else {
                category = "Pozostałe";
                icon = R.drawable.ic_recipe;
            }

            double energy = cursor.getDouble(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_ENERGY));
            double protein = cursor.getDouble(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_PROTEIN));
            double fats = cursor.getDouble(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_FATS));
            double carbohydrates = cursor.getDouble(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_CARBOHYDRATES));
            double fibre = cursor.getDouble(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_FIBRE));
            double salt = cursor.getDouble(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_SALT));
            String description = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_DESCRIPTION));

            int tmp = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_GLUTEN));
            boolean gluten;
            if (tmp == 0) {
                gluten = false;
            } else {
                gluten = true;
            }

            tmp = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_LACTOSE));
            boolean lactose;
            if (tmp == 0) {
                lactose = false;
            } else {
                lactose = true;
            }

            ingredient = new Ingredient();
            ingredient.setID(ID);
            ingredient.setName(name);
            ingredient.setCategory(category);
            ingredient.setIcon(icon);
            ingredient.setEnergy(energy);
            ingredient.setProtein(protein);
            ingredient.setFats(fats);
            ingredient.setCarbohydrates(carbohydrates);
            ingredient.setFibre(fibre);
            ingredient.setSalt(salt);
            ingredient.setDescription(description);
            ingredient.setGluten(gluten);
            ingredient.setLactose(lactose);
            ingredients.add(ingredient);
        }

        return ingredients;
    }

    /**
     * Fetch meals from local database by given date and map
     * to Meal class object.
     * @param mealDate
     * @return A collection of type Meal
     */
    public ArrayList<Meal> fetchMeals(String mealDate) {
        Meal meal;
        Ingredient ingredient;
        ArrayList<Meal> meals = new ArrayList<>();
        ArrayList<Ingredient> ingredients;

        Cursor cursor = getMealsFromDate(mealDate);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ingredients = new ArrayList<>();
            int ID = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.MEALS_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.MEALS_COLUMN_NAME));
            int RECIPE_ID = cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.MEALS_COLUMN_RECIPE_ID));
            String date = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.MEALS_COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.MEALS_COLUMN_TIME));
            //String dateTime = date + " " + time;

            boolean consumed;
            if (cursor.getInt(cursor.getColumnIndex(FoodyDatabaseHelper.MEALS_COLUMN_STATUS)) == 0) consumed = false;
            else consumed = true;

            if (RECIPE_ID <= 0) {
                String formattedIngredients = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.MEALS_COLUMN_INGREDIENTS));
                String [] buffers = formattedIngredients.split(":");

                if (!formattedIngredients.equals(""))
                    for (String buffer : buffers) {

                        int id = Integer.valueOf(buffer.split(",")[0]);
                        int quantity = Integer.valueOf(buffer.split(",")[1]);

                        Cursor ingredientCursor = getIngredientByID(id);

                        if (ingredientCursor != null) {
                            ingredientCursor.moveToFirst();
                        }

                        String ingredientName = ingredientCursor.getString(ingredientCursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_NAME));
                        String category = ingredientCursor.getString(ingredientCursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_CATEGORY));

                        int icon;
                        if (category.contains("Mięso")) {
                            icon = R.drawable.ic_meat;
                        } else if (category.contains("Warzywa")) {
                            icon = R.drawable.ic_vegetables;
                        } else if (category.contains("Owoce")) {
                            icon = R.drawable.ic_fruits;
                        } else if (category.contains("Nabiał")) {
                            icon = R.drawable.ic_dairy;
                        } else if (category.contains("Cukier")) {
                            icon = R.drawable.ic_snacks;
                        } else if (category.contains("Produkty zbożowe")) {
                            icon = R.drawable.ic_grain;
                        } else if (category.contains("Tłuszcze")) {
                            icon = R.drawable.ic_fats;
                        } else if (category.contains("Bakalie i nasiona")) {
                            icon = R.drawable.ic_nuts;
                        } else if (category.contains("Ryby")) {
                            icon = R.drawable.ic_fish;
                        } else if (category.contains("Napoje")) {
                            icon = R.drawable.ic_beverages;
                        } else {
                            category = "Pozostałe";
                            icon = R.drawable.ic_recipe;
                        }

                        double energy = ingredientCursor.getDouble(ingredientCursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_ENERGY));
                        double protein = ingredientCursor.getDouble(ingredientCursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_PROTEIN));
                        double fats = ingredientCursor.getDouble(ingredientCursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_FATS));
                        double carbohydrates = ingredientCursor.getDouble(ingredientCursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_CARBOHYDRATES));
                        double fibre = ingredientCursor.getDouble(ingredientCursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_FIBRE));
                        double salt = ingredientCursor.getDouble(ingredientCursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_SALT));
                        String description = ingredientCursor.getString(ingredientCursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_DESCRIPTION));

                        int tmp = ingredientCursor.getInt(ingredientCursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_GLUTEN));
                        boolean gluten;
                        if (tmp == 0) {
                            gluten = false;
                        } else {
                            gluten = true;
                        }

                        tmp = ingredientCursor.getInt(ingredientCursor.getColumnIndex(FoodyDatabaseHelper.INGREDIENTS_COLUMN_LACTOSE));
                        boolean lactose;
                        if (tmp == 0) {
                            lactose = false;
                        } else {
                            lactose = true;
                        }

                        ingredient = new Ingredient();
                        ingredient.setID(id);
                        ingredient.setName(ingredientName);
                        ingredient.setCategory(category);
                        ingredient.setIcon(icon);
                        ingredient.setEnergy(energy);
                        ingredient.setProtein(protein);
                        ingredient.setFats(fats);
                        ingredient.setCarbohydrates(carbohydrates);
                        ingredient.setFibre(fibre);
                        ingredient.setSalt(salt);
                        ingredient.setDescription(description);
                        ingredient.setGluten(gluten);
                        ingredient.setLactose(lactose);
                        ingredient.setQuantity(quantity);
                        ingredients.add(ingredient);
                    }
            } else {
                ingredients = fetchRecipeIngredients(RECIPE_ID);
            }

            int icon;
            if (name.equals("Śniadanie")) {
                icon = R.drawable.ic_vector_meal_view;
            } else if (name.equals("Drugie śniadanie")) {
                icon = R.drawable.ic_vector_brunch;
            } else if (name.equals("Lunch")) {
                icon = R.drawable. ic_vector_lunch;
            } else if (name.equals("Obiad")) {
                icon = R.drawable.ic_vector_dinner;
            } else if (name.equals("Podwieczorek")) {
                icon = R.drawable.ic_vector_tea;
            } else if (name.equals("Kolacja")) {
                icon = R.drawable.ic_vector_supper;
            } else if (name.equals("Przekąska")) {
                icon = R.drawable.ic_vector_snack;
            } else {
                icon = R.drawable.ic_recipe;
            }

            meal = new Meal();
            meal.setID(ID);
            meal.setName(name);
            meal.setDate(date);
            meal.setTime(time);
            meal.setIcon(icon);
            meal.setStatus(consumed);
            meal.setRECIPE_ID(RECIPE_ID);
            meal.setIngredients(ingredients);
            meals.add(meal);
        }

        return meals;
    }

}
