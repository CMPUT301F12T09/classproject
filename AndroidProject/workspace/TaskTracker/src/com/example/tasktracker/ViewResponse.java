package com.example.tasktracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class ViewResponse extends Activity {

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
