package com.example.tasktracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class MemoryCheck extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_check);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_memory_check, menu);
        return true;
    }
    
    public void cancelmemoryCheck(View view)
    {
    	finish();
    }
}
