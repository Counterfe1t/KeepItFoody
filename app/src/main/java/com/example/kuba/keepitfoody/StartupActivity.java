package com.example.kuba.keepitfoody;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

/**
 * Activity used to check if there already is saved user
 * in the local database. If so log that user automatically.
 * Otherwise start LoginActivity.
 */
public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        try {
            FoodyDatabaseHelper helper = FoodyDatabaseHelper.getInstance(this);
            SQLiteDatabase db = helper.getDatabase();
            Cursor cursor = db.rawQuery(
                    "SELECT count(*) FROM " + FoodyDatabaseHelper.USER_TABLE_NAME,
                    null);

            if (cursor != null) {
                cursor.moveToFirst();
            }

            int count = cursor.getInt(0);
            if (count > 0) {

                cursor = helper.getUser();

                if (cursor != null) {
                    cursor.moveToFirst();
                }

                String email = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.USER_COLUMN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.USER_COLUMN_PASSWORD));

                SignInBackground signInBackground = new SignInBackground(this, email, password);
                signInBackground.sendRequest();

            } else {
                Toast.makeText(this, "Nie ma użytkownika w bazie.", Toast.LENGTH_SHORT).show();

                Thread thread = new Thread() {
                    public void run() {
                        try {
                            for (int i = 0; i < 1000; i += 100) {
                                sleep(100);
                            }
                        } catch(Exception e) {}
                        finally {
                            Intent intent = new Intent(StartupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
                        }
                    }
                };
                thread.start();
            }

            cursor.close();
        } catch (JSONException e) {
            Log.e("ERROR", "onCreate: ", e);
        }

    }
}
