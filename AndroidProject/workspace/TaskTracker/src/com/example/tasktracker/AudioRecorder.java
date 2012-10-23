package com.example.tasktracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class AudioRecorder extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_audio_recorder, menu);
        return true;
    }
    
    public void recordAudio(View view)
    {
    	//while down record?
    	//Ask for confirmation
    	finish();
    }
    
    public void cancelAudio(View view)
    {
    	finish();
    }
}
