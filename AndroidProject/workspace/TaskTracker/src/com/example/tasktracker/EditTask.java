package com.example.tasktracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class EditTask extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_task, menu);
        return true;
    }
    
    public void cancelEdit(View view)
    {
    	finish();
    }
    
    public void saveEdit(View view)
    {
    	//Send to task manager
    	finish();
    }
    
    public void deleteTask(View view)
    {
    	//Tell task manager to remove
    	finish();
    }
}
