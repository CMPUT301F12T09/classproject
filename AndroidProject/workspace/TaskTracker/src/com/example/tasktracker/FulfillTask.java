package com.example.tasktracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class FulfillTask extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulfill_task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_fulfill_task, menu);
        return true;
    }
    
    public void takePhoto(View view)
    {
    	Intent intent = new Intent(this, PhotoTaker.class);
    	startActivity(intent); //change to require return
    	//get returned photo
    	//if not null add to fulfillment
    }
    
    public void recordAudio(View view)
    {
    	Intent intent = new Intent(this, AudioRecorder.class);
    	startActivity(intent); //change to require return
    	//get returned audio
    	//if not null add to fulfillment
    }
    
    public void findPhoto(View view)
    {
    	Intent intent = new Intent(this, MemoryCheck.class);
    	//set extra for type
    	startActivity(intent); //change to require return
    	//get returned photo
    	//if not null add to fulfillment
    }
    
    public void findAudio(View view)
    {
    	Intent intent = new Intent(this, MemoryCheck.class);
    	//set extra for type
    	startActivity(intent); //change to require return
    	//get returned audio
    	//if not null add to fulfillment
    }
    
    public void cancelFulfill(View view)
    {
    	finish();
    }
    
    public void saveFulfill(View view)
    {
    	//Ask for confirmation
    	//Send fulfillment to task manager
    	finish();
    }
}
