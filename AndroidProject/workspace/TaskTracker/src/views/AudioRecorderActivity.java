
/**
*    Copyright 2012 Zak Turchansky, Evan Fauser, Gordon Lancop, Seth Davis
*	
*	Licensed under the Apache License, Version 2.0 (the "License");
*	you may not use this file except in compliance with the License.
*	You may obtain a copy of the License at
*	
*	http://www.apache.org/licenses/LICENSE-2.0
*	
*	Unless required by applicable law or agreed to in writing, software
*	distributed under the License is distributed on an "AS IS" BASIS,
*	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*	See the License for the specific language governing permissions and
*	limitations under the License.
**/

package views;

import java.util.ArrayList;

import model.AudioFile;
import model.ImageFile;
import model.Task;

import com.example.tasktracker.R;
import com.example.tasktracker.R.layout;
import com.example.tasktracker.R.menu;

import controllers.ByteArrayOutputStream;
import controllers.TaskManager;

import android.media.AudioRecord;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
/**
 * This class will act as the activity where the user can record audio
 * on the fly for task submissions.
 * 
 * We are currently unsure as to how this will be implemented and it
 * will be delivered in part 4
 * @author zturchan
 *
 */
public class AudioRecorderActivity extends Activity {

	private Uri audioUri;
    private Task curTask;
    private final static int SOUND_REC_REQUEST = 5555;
    private ArrayList<AudioFile> audio;
    private Button useAudio;
	/**
	 * Create all UI elements and connect the appropriate listeners.
     * Also get the current set of audio files so that we will add 
     * to them for the current fulfillment.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);
        
        Bundle data = getIntent().getExtras();

        useAudio = (Button) findViewById(R.id.button_audio_use);
        useAudio.setEnabled(false);
        
        int index = data.getInt("index");
        curTask = TaskManager.getInstance(1, this).getViewableTaskList().get(index);
        audio = (ArrayList<AudioFile>) getIntent().getSerializableExtra("audio");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_audio_recorder, menu);
        return true;
    }
    /**
     * Call the android audio recorder so that the user can record an audio file. 
     * Result will be returned as a Bitmap.
     * @param view
     */
    public void recordAudio(View view){
    	Intent audioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        try{
            startActivityForResult(audioIntent, SOUND_REC_REQUEST);
            useAudio.setEnabled(true);
        }
        catch(Exception e){
            
        }
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        try{
        	if (requestCode == SOUND_REC_REQUEST) {  
        		audioUri = data.getData();  
        	}  
        }catch (Exception e){
            
        }
    }
    
    public void useAudio(View view){
    	
    	InputStream inputS = openInputStream(audioUri);
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	
    	
    	byte[] buffer = new byte[1024];

    	int length = 0;
    	while ((length = inputS.read(buffer)) != -1) {
    		bos.write(buffer, 0, length);
    	}
		byte[] toStore = bos.toByteArray();
        FulfillTaskActivity.fulfillment.addAudio(new AudioFile(toStore));
    	Toast toast = Toast.makeText(this, "Audio saved: " + audioUri.getPath(), 5);
        toast.show();
    }
    
    public void doneAudio(View view){
        Intent returnIntent = new Intent(AudioRecorderActivity.this, FulfillTaskActivity.class);
        setResult(RESULT_OK, returnIntent);  
        finish();
    }
    
}
