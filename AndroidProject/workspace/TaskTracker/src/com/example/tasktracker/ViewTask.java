package com.example.tasktracker;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * This class is an activity that allows a user to view a task
 * before they decide to fulfill it.  If the user wants, they can
 * access the fulfill activity directly from this view instead of having 
 * to go via the main screen.
 * 
 * This class displays task name, description, scope, and requirements to the user
 * 
 * @author zturchan
 *
 */
public class ViewTask extends Activity {

    private ListView submissions;
    private ArrayAdapter<Fulfillment> adapter;
    private ArrayList<Fulfillment> FulfillmentList;
    private Task task;
	
    /**
     * Initialize appropriate UI components and connect listeners
     * Also update text on screen with values from the task object 
     * accessed via the Task Manager.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        
        Button fulfillTask = (Button) findViewById(R.id.button_view_fulfill);
        Button mainMenu = (Button) findViewById(R.id.button_view_cancel);
        
        fulfillTask.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                goToFulfill(v);
            }
        });
        
        mainMenu.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                returnToMain(v);
            }
        });
        
        Bundle bundle = getIntent().getExtras();
        task = (Task) bundle.get("Task");
        TextView name = (TextView) findViewById(R.id.text_view_name); 
        name.setText("Task Name: " + task.getTaskName());
        TextView description = (TextView) findViewById(R.id.text_view_taskdesc); 
        description.setText("Description: " + task.getTaskDescription());
        TextView wanted = (TextView) findViewById(R.id.text_view_desired);
        String desired = "Desired Responses: ";
        if(task.getWantText()){
        	desired += "Text";
        }
        if(task.getWantPhoto()){
        	desired += " Photo";
        }
        if(task.getWantAudio()){
        	desired += " Audio";
        }
        wanted.setText(desired);
        TextView open = (TextView) findViewById(R.id.text_view_openclose); 
        if(task.getIsOpen()){
        	open.setText("Availability: Open");
        }else{
        	open.setText("Availability: Closed");
        }
        TextView scope = (TextView) findViewById(R.id.text_fulfill_scope);
        if(task.getIsPublic()){
        	scope.setText("Task Scope: Public");
        }else{
        	scope.setText("Task Scope: Private");
        }
        submissions = (ListView) findViewById(R.id.oldLogs);
        FulfillmentList = task.getSubmissions();
    	adapter = new ArrayAdapter<Fulfillment>(this, R.layout.task_display, FulfillmentList);
    	submissions.setAdapter(adapter);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_task, menu);
        return true;
    }
    /**
     * access the fulfill activity as a shortcut
     * @param view
     */
    public void goToFulfill(View view){
    	Intent intent = new Intent(this,FulfillTask.class);
        intent.putExtra("task",task);
        startActivity(intent);
    }
    /**
     * return to the main menu
     * @param view
     */
    public void returnToMain(View view){
    	finish();
    }
}