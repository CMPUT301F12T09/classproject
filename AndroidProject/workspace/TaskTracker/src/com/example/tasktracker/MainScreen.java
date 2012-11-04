package com.example.tasktracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainScreen extends Activity {

    private ListView tasks;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        Button createTask = (Button) findViewById(R.id.createTask);
        Button updateData = (Button) findViewById(R.id.updateData);
        tasks = (ListView) findViewById(R.id.tasks);
        
        createTask.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                goToCreateTask(v);
            }
        });
        
        updateData.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                updateData(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_screen, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void updateData(View view)
    {
    	//pull server type from settings (if we end up having to support multiple servers)
    }
    
    public void createTask(View view)
    {
    	Intent intent = new Intent(this, CreateTask.class);
    	startActivity(intent);
    }
    
    public void goToCreateTask(View v){
        Intent intent = new Intent(MainScreen.this, CreateTask.class);
        startActivity(intent);
    }

}
