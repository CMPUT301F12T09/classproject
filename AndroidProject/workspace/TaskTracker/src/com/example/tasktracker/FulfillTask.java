package com.example.tasktracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FulfillTask extends Activity {
    
    private TextView nameView;
    public Task curTask;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulfill_task);
        
        //Bundle data = getIntent().getExtras();
        //curTask = data.getParcelable("task");
        //nameView.setText("Task Name: "+curTask.getTaskName());
        
        
        
        Button takePhoto = (Button) findViewById(R.id.button_fulfill_takePhoto);
        Button photoMem = (Button) findViewById(R.id.button_fulfill_photoMem);
        Button recordAudio = (Button) findViewById(R.id.button_fulfill_recordAudio);
        Button audioMem = (Button) findViewById(R.id.button_fulfill_audioMem);
        Button cancel = (Button) findViewById(R.id.button_fulfill_cancel);
        Button save = (Button) findViewById(R.id.button_fulfill_save);
        
        takePhoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                takePhoto(v);
            }
        });
        
        photoMem.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                findPhoto(v);
            }
        });
        
        recordAudio.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                recordAudio(v);
            }
        });
        
        audioMem.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                findAudio(v);
            }
        });
        
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                saveFulfill(v);
            }
        });
        
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                cancelFulfill(v);
            }
        });
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
