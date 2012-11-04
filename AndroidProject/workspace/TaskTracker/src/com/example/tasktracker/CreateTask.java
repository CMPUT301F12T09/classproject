package com.example.tasktracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class CreateTask extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        
        Button save = (Button) findViewById(R.id.button_create_save);
        Button cancel = (Button) findViewById(R.id.button_create_cancel);
        
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                confirmTask(v);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                cancelTask(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_task, menu);
        return true;
    }
    
    public void confirmTask(View view)
    {
    	//Prompt confirmation
    	finish();
    }
    
    public void cancelTask(View view)
    {
    	finish();
    }
}
