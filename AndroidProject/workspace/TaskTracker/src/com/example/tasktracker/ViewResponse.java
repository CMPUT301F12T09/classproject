package com.example.tasktracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
/**
 * When implemented, will allow a user to view the existing 
 * responses to a given task
 * @author zturchan
 *
 */
public class ViewResponse extends Activity {
    /**
     * Will initialize UI components and display info about the fulfillment
     * Also will implement appropriate listeners.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_response);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_response, menu);
        return true;
    }
    
    public void cancelViewResponse(View view)
    {
    	finish();
    }
}
