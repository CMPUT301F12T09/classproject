package com.example.tasktracker;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainScreen extends Activity {

    private ListView tasks;
    private ArrayAdapter<Task> adapter;
    private ArrayList<Task> TaskList;
    private TaskManager tManager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        Button createTask = (Button) findViewById(R.id.createTask);
        Button updateData = (Button) findViewById(R.id.updateData);
        tasks = (ListView) findViewById(R.id.tasks);
        tManager = TaskManager.getInstance(1, this);
        
        registerForContextMenu(tasks);
        
        
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
        super.onCreateContextMen                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       u(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_screen, menu);
    }
    
    //Context menu listener code from http://developer.android.com/guide/topics/ui/menus.html
    //and http://mobile.dzone.com/news/context-menu-android-tutorial
    @Override
    public boolean onContextItemSelected(MenuItem item){
        Intent intent;
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.menu_fulfill:
                //probably want to bundle the task and pass it off
                intent = new Intent(this,FulfillTask.class);
                intent.putExtra("task",TaskList.get((int)info.id));
                intent.putExtra("index",(int)info.id);
                startActivity(intent);
                //This will toast the task name of the selected task, that way
                //we know if we can access tasks in this manner and therefore do comparisons
                //of who did what task, and populate fields for editing tasks etc
                //Access selected task with TaskList.get((int)info.id)
                //Toast.makeText(this, "selected task "+TaskList.get((int)info.id).getTaskName(),Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_public:
                //Make task public
                return true;
            case R.id.menu_edit:
                intent = new Intent(this,EditTask.class);
                startActivity(intent);
                return true;
            case R.id.menu_view:
            	intent = new Intent(this,ViewTask.class);
                intent.putExtra("Task", TaskList.get((int)info.id));
                startActivity(intent);
                return true;
            case R.id.menu_close:
                //close the task
                return true;
            default:
                return super.onContextItemSelected(item);
                
        }
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
    
    public void updateData(View view){
    	TaskList = tManager.getTaskList();
    	adapter = new ArrayAdapter<Task>(this, R.layout.task_display, TaskList);
    	tasks.setAdapter(adapter);
    	
    }
      
    public void goToCreateTask(View view){
        Intent intent = new Intent(MainScreen.this, CreateTask.class);
        startActivity(intent);
    }

}
