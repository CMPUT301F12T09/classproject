package com.example.tasktracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ViewTask extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_task, menu);
        return true;
    }
    
    public void goToFulfill(View view)
    {
    	Intent intent = new Intent(this, FulfillTask.class);
    	startActivity(intent);
    }
    
    public void returnToMain(View view)
    {
    	finish();
    }
}
