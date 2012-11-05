package com.example.tasktracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.View;


//Some camera code taken from http://stackoverflow.com/questions/5991319/capture-image-from-camera-and-display-in-activity

public class PhotoTaker extends Activity implements SurfaceHolder.Callback
{

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
        
    public void cancelPhoto(View view)
    {
    	finish();
    }
}
