package com.example.tasktracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
/**
 * This class will act as the activity where the user can record audio
 * on the fly for task submissions.
 * 
 * We are currently unsure as to how this will be implemented and it
 * will be delivered in part 4
 * @author zturchan
 *
 */
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
