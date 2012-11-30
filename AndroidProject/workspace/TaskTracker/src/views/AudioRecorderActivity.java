
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

import com.example.tasktracker.R;
import com.example.tasktracker.R.layout;
import com.example.tasktracker.R.menu;

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
public class AudioRecorderActivity extends Activity {

	/**
	 * Create all UI elements and connect the appropriate listeners.
     * Also get the current set of audio files so that we will add 
     * to them for the current fulfillment.
	 */
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
    /**
     * Call the android audio recorder so that the user can record an audio file. 
     * Result will be returned as a Bitmap.
     * @param view
     */
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
