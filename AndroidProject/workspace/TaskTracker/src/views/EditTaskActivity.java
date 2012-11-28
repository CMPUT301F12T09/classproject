
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

import model.Task;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tasktracker.R;

import controllers.TaskManager;
/**
 * This class is an activity which allows the author of a task to edit
 * relevant information
 * <ul compact>
 * <li>Task Name</li>
 * <li>Task Description</li>
 * <li>Task Requirements: Audio, Photo, Text</li>
 * <li>Task Scope: Public/Private</li>
 * <li>Any submissions submitted to the task may be deleted</li>
 * <li>Delete the entire task</li>
 * </ul>
 * @author zturchan
 *
 */
public class EditTaskActivity extends Activity {

	private EditText email, desc, name;
    private CheckBox text;
    private CheckBox photo;
    private CheckBox audio;
    private boolean wantText;
    private boolean wantAudio;
    private boolean wantPhoto;
    private boolean isPublic;
    private boolean isOpen;
    private RadioButton radioPublic;
    private RadioButton radioPrivate;
    private RadioButton radioOpen;
    private RadioButton radioClosed;
	private Task task;
	private int index;
    @Override
    /**
     * When implemented, this method will initiate the UI components and 
     * connect appropriate listeners
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        
        Bundle bundle = getIntent().getExtras();
        task = (Task) bundle.get("Task");
        index = bundle.getInt("index");
        
        email = (EditText) findViewById(R.id.edit_email_edit);
        desc = (EditText) findViewById(R.id.text_edit_desc);
        name = (EditText) findViewById(R.id.text_edit_name);
        email.setText(task.getEmail());
        desc.setText(task.getTaskDescription());
        name.setText(task.getTaskName());
        
        text = (CheckBox) findViewById(R.id.checkbox_text);
        audio = (CheckBox) findViewById(R.id.checkbox_audio);
        photo = (CheckBox) findViewById(R.id.checkbox_photo);
        
        if(task.getWantAudio()){
        	audio.setChecked(true);
        }
        if(task.getWantPhoto()){
        	photo.setChecked(true);
        }
        if(task.getWantText()){
        	text.setChecked(true);
        }
        
        radioPublic = (RadioButton) findViewById(R.id.radio_public);
        radioPrivate = (RadioButton) findViewById(R.id.radio_private);

        if (task.getIsPublic()){
        	radioPublic.setChecked(true);
        	isPublic = true;
        }else{
        	isPublic = false;
        	radioPrivate.setChecked(false);
        }
        radioOpen = (RadioButton) findViewById(R.id.radio_open);
        radioClosed = (RadioButton) findViewById(R.id.radio_close);
        if(task.getIsOpen()){
        	isOpen = true;
        	radioOpen.setChecked(true);
        }else{
        	isOpen = false;
        	radioClosed.setChecked(true);
        }

    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_task, menu);
        return true;
    }
    
    public void cancelEdit(View view)
    {
    	finish();
    }
    /**
     * When implemented, will commit changes to the task to local storage
     * and the webservice if public
     * @param view
     */
    public void saveEdit(View view)
    {
    	//Send to task manager
    	TaskManager manager = TaskManager.getInstance(1, this);
    	manager.updateDesc(index, desc.getText().toString());
    	manager.updateName(index, name.getText().toString());
    	manager.updateRequirements(index, wantText, wantPhoto, wantAudio);
    	manager.updatePublic(index, isPublic);
    	manager.updateOpen(index, isOpen);
    	    	
    	finish();
    }
    /**
     * When implemented, will delete the entire task from both local 
     * storage and the webservice if applicable
     * @param view
     */
    public void deleteTask(View view)
    {
    	//Tell task manager to remove
    	TaskManager manager = TaskManager.getInstance(1, this);
    	manager.deleteTask(index);
    	Toast toast = Toast.makeText(this, "Task Deleted ", 3);
        toast.show();
    	finish();
    }
    public void onCheckBoxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()){
            case R.id.checkbox_text:
                if(checked){
                    wantText = true;
                }else{
                    wantText = false;
                }
                break;
            case R.id.checkbox_photo:
                if(checked){
                    wantPhoto = true;
                }else{
                    wantPhoto = false;
                }
                break;
            case R.id.checkbox_audio:
                if(checked){
                    wantAudio = true;
                }else{
                    wantAudio = false;
                }
        }
    }
    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.radio_public:
                if(checked){
                    isPublic = true;
                }
                break;
            case R.id.radio_private:
                if(checked){
                    isPublic = false;
                }
                break;
            case R.id.radio_open:
            	if(checked){
            		isOpen = true;
            	}
            	break;
            case R.id.radio_close:
            	if(checked){
            		isOpen = false;
            	}
            		
        }
    }
    
    
}
