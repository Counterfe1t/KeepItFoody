package com.example.kuba.keepitfoody;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

/**
 * Activity used to display loading animation and perform database
 * synchronization in the background thread.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private User user;
    private long ms = 0;
    private long splashTime = 5000;
    private boolean splashActive = true;
    private boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent intent = getIntent();
        user = intent.getParcelableExtra(MenuActivity.USER);

        FoodyDatabaseHelper helper = FoodyDatabaseHelper.getInstance(this);
        helper.onUpgrade(helper.getDatabase(),1, 1);
        new SyncDatabase(this, user).execute();

        Thread thread = new Thread() {
            public void run() {
                try {
                    while (splashActive && ms < splashTime) {
                        if(!paused)
                            ms=ms+100;
                        sleep(100);
                    }
                } catch(Exception e) {}
                finally {
                    Intent intent = new Intent(SplashScreenActivity.this, MenuActivity.class);
                    intent.putExtra(MenuActivity.USER, user);
                    startActivity(intent);
                }
            }
        };
        thread.start();

    }

}
