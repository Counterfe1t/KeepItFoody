package com.example.kuba.keepitfoody;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

/**
 * This class is used to download all data from remote database in the background thread.
 * After successful request save downloaded data to local SQLite database.
 */
public class SyncDatabase extends AsyncTask<String, Void, String> {

    private Context context;
    private FoodyDatabaseHelper helper;
    private User user;

    public SyncDatabase(Context context, User user) {
        this.context = context;
        this.helper = FoodyDatabaseHelper.getInstance(context);
        this.user = user;
    }

    @Override
    protected String doInBackground(String... strings) {

        SQLiteDatabase database = helper.getDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FoodyDatabaseHelper.USER_COLUMN_ID, 1);
        cv.put(FoodyDatabaseHelper.USER_COLUMN_ID_USER, user.getID());
        cv.put(FoodyDatabaseHelper.USER_COLUMN_EMAIL, user.getEmail());
        cv.put(FoodyDatabaseHelper.USER_COLUMN_NAME, user.getName());
        cv.put(FoodyDatabaseHelper.USER_COLUMN_PASSWORD, user.getPassword());
        boolean sex = user.isSex();
        cv.put(FoodyDatabaseHelper.USER_COLUMN_SEX, sex);
        cv.put(FoodyDatabaseHelper.USER_COLUMN_DATE_OF_BIRTH, user.getDateOfBirth());
        cv.put(FoodyDatabaseHelper.USER_COLUMN_WEIGHT, user.getWeight());
        cv.put(FoodyDatabaseHelper.USER_COLUMN_HEIGHT, user.getHeight());
        cv.put(FoodyDatabaseHelper.USER_COLUMN_TOKEN, user.getToken());
        cv.put(FoodyDatabaseHelper.USER_COLUMN_PICTURE, user.getProfilePicture());
        database.insert(FoodyDatabaseHelper.USER_TABLE_NAME, null, cv);

        new GetMealsFromAPI(context, helper.getDatabase(), user.getID()).parseJSON();
        new GetIngredientsFromAPI(context, helper.getDatabase()).parseJSON();
        new GetRecipesFromAPI(context, helper.getDatabase()).parseJSON();
        new GetSpecialistsFromAPI(context, helper.getDatabase()).parseJSON();
        new GetBmrFromApi(context, user.getID()).parseJSON();

        return null;
    }
}
