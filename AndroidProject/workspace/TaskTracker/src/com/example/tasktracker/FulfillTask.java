package com.example.tasktracker;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FulfillTask extends Activity {
    
    private TextView nameView, descView, responseView, scopeView;
    private ArrayList<ImageFile> photos;
    private ArrayList<AudioFile> audio;
    private int index;
    private Task curTask;
    private EditText textResponse;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulfill_task);
        
        Bundle data = getIntent().getExtras();
        //curTask =(Task) data.getParcelable("task");
        curTask = (Task) getIntent().getSerializableExtra("task");
        photos = (ArrayList<ImageFile>) getIntent().getSerializableExtra("images");
        index = data.getInt("index");
        
        nameView = (TextView) findViewById(R.id.text_fulfill_name);
        nameView.setText("Task Name: "+curTask.getTaskName());
        
        descView = (TextView) findViewById(R.id.text_fulfill_taskdesc);
        descView.setText("Description: "+curTask.getTaskDescription());
             
        responseView = (TextView) findViewById(R.id.text_fulfill_desired);
        responseView.setText("Desired Responses: "+buildResponses(curTask));
        
        scopeView = (TextView) findViewById(R.id.text_fulfill_scope);
        scopeView.setText("Task Scope: "+buildScope(curTask));
        
        textResponse = (EditText)  findViewById(R.id.text_edit_textresponse);
        
        
        Button takePhoto = (Button) findViewById(R.id.button_fulfill_takePhoto);
        Button photoMem = (Button) findViewById(R.id.button_fulfill_photoMem);
        Button recordAudio = (Button) findViewById(R.id.button_fulfill_recordAudio);
        Button audioMem = (Button) findViewById(R.id.button_fulfill_audioMem);
        Button cancel = (Button) findViewById(R.id.button_fulfill_cancel);
        Button save = (Button) findViewById(R.id.button_fulfill_save);
        
        audio = new ArrayList<AudioFile>();
        photos = new ArrayList<ImageFile>();
        
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
    	Intent intent = new Intent(FulfillTask.this, PhotoTaker.class);
        intent.putExtra("task",curTask);
        intent.putExtra("images", photos);
    	startActivityForResult(intent,1); 
    	finish();
    }
    
    public void recordAudio(View view)
    {
    	Intent intent = new Intent(FulfillTask.this, AudioRecorder.class);
    	startActivity(intent); //change to require return
    	//get returned audio
    	//if not null add to fulfillment
    }
    
    public void findPhoto(View view)
    {
    	Intent intent = new Intent(FulfillTask.this, MemoryCheck.class);
    	//set extra for type
    	startActivity(intent); //change to require return
    	//get returned photo
    	//if not null add to fulfillment
    }
    
    public void findAudio(View view)
    {
    	Intent intent = new Intent(FulfillTask.this, MemoryCheck.class);
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
        TaskManager manage = TaskManager.getInstance(1, this);
        manage.addSubmission(index, textResponse.getText().toString(),photos,audio);
       	finish();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1) {
            if(resultCode == RESULT_OK){
                curTask=(Task)data.getSerializableExtra("task");
                photos=(ArrayList<ImageFile>)data.getSerializableExtra("images");
            }
        }
    }
    private String buildResponses(Task t){
        String ret = "";
        if (t.getWantText()){
            ret = ret + "Text ";
        }
        if(t.getWantPhoto()){
            ret = ret + "Photo ";
        }
        if(t.getWantAudio()){
            ret = ret + "Audio";
        }
        return ret;
    }
    private String buildScope(Task t){
        String ret ="";
        if(t.getIsPublic()){
            ret = "Public";
        }
        else{
            ret = "Private";
        }
        return ret;
    }
}
