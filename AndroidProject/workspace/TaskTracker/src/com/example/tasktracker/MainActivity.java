package com.example.tasktracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

/**
 * This is the activity that is called when the app is first
 * launched.  It is a simple splash screen that the user must tap to continue
 * @author zturchan
 *
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    /**
     * Go to the main screen of the app from where you can access most
     * of the app's functionality
     * @param view
     */
    public void continueToMain(View view)
    {
    	Intent intent = new Intent(this, MainScreen.class);
    	startActivity(intent);
    }
}
