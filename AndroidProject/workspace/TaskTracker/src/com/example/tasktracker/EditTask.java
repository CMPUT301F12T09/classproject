package com.example.tasktracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
/**
 * This class is an activity which allows the author of a task to edit
 * relevant information
 * <ul compact>
 * <li>Task Name</li>
 * <li>Task Description</li>
 * <li>Task Requirements: Audio, Photo, Text</li>
 * <li>Task Scope: Public/Private</li>
 * <li>Any submissions submitted to the task may be deleted</li>
 * <li>Delete the entire task</li>
 * </ul>
 * @author zturchan
 *
 */
public class EditTask extends Activity {

    @Override
    /**
     * When implemented, this method will initiate the UI components and 
     * connect appropriate listeners
     */
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
    /**
     * When implemented, will commit changes to the task to local storage
     * and the webservice if public
     * @param view
     */
    public void saveEdit(View view)
    {
    	//Send to task manager
    	finish();
    }
    /**
     * When implemented, will delete the entire task from both local 
     * storage and the webservice if applicable
     * @param view
     */
    public void deleteTask(View view)
    {
    	//Tell task manager to remove
    	finish();
    }
}
