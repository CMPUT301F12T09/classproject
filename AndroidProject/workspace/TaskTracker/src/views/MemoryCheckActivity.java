
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
 * This class handles checking for images or audio files in the device's memory,
 * for the ability to attach pre-existing media to a fulfillment.
 * 
 * @author glancop
 *
 */
public class MemoryCheckActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_check);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_memory_check, menu);
        return true;
    }
    
    public void cancelmemoryCheck(View view)
    {
    	finish();
    }
}
