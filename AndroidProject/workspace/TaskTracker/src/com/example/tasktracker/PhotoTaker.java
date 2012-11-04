package com.example.tasktracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

//Some camera code taken from http://stackoverflow.com/questions/5991319/capture-image-from-camera-and-display-in-activity

public class PhotoTaker extends Activity {
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_taker);
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_photo_taker, menu);
        return true;
    }
    
    public void takePhoto(View view)
    {
    	//take photo and ask for confirmation
    	finish();
    }
    
    public void cancelPhoto(View view)
    {
    	finish();
    }
}
