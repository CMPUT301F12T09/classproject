package com.example.tasktracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class CreateTask extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_task, menu);
        return true;
    }
    
    public void confirmTask(View view)
    {
    	//Prompt confirmation
    	finish();
    }
    
    public void cancelTask(View view)
    {
    	finish();
    }
}
