
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

package com.example.tasktracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
/**
 * This class is an activity where the suer will enter relevant information
 * for creating a new class.
 * <ul compact>
 * <li>Task Name</li>
 * <li>Task Description</li>
 * <li>Task Requirements: Audio, Photo, Text</li>
 * <li>Task Scope: Public/Private</li>
 * </ul>
 * 
 * This class contains 2 buttons to exit it.
 * Cancel will ignore any data entered by the user and return to the
 * main menu, while Save/confirm will commit the changes to local storage 
 * (and webservice if the user specifies public)
 * 
 *  * @author zturchan
 *
 */
public class CreateTask extends Activity {

    private EditText editName;
    private EditText editDesc;
    private Button save;
    private Button cancel;
    private CheckBox text;
    private CheckBox photo;
    private CheckBox audio;
    private boolean wantText;
    private boolean wantAudio;
    private boolean wantPhoto;
    private boolean isPublic;
    private RadioButton radioPublic;
    private RadioButton radioPrivate;
    @Override
    /**
     * Initialize all the UI elements for creating a new task and connect
     * the appropriate listeners.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        
        save = (Button) findViewById(R.id.button_create_save);
        cancel = (Button) findViewById(R.id.button_create_cancel);
        editName = (EditText) findViewById(R.id.text_edit_name);
        editDesc = (EditText) findViewById(R.id.text_edit_desc);
        text = (CheckBox) findViewById(R.id.checkbox_text);
        photo = (CheckBox) findViewById(R.id.checkbox_photo);
        audio = (CheckBox) findViewById(R.id.checkbox_audio);
        
        radioPublic = (RadioButton) findViewById(R.id.radio_public);
        radioPrivate = (RadioButton) findViewById(R.id.radio_private);
        //By default all tasks are public
        radioPublic.setChecked(true);
        
        wantText = false;
        wantAudio = false;
        wantPhoto = false;
        isPublic = true;
        
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
    /**
     * When the user is satisfied with their new task, they will click the 
     * Save button and confirm their new task entry.  This method will get the
     * values of the EditText fields and call the instance of the Taskmanager to
     * create a new task with given input.  
     * 
     * When task creation is finished this activity finishes and returns to 
     * the main screen
     * @param view
     */
    public void confirmTask(View view)
    {
        String name = editName.getText().toString();
        String desc = editDesc.getText().toString();
        
        TaskManager manager = TaskManager.getInstance(1, this);
        manager.createTask(name, desc, wantText, wantPhoto, wantAudio, isPublic);
        
    	finish();
    }
    
    /**
     * Checkbox handler taken from 
     * http://developer.android.com/guide/topics/ui/controls/checkbox.html
     * When the user clicks a checkbox to specify requirements for a task,
     * set the appropriate boolean value for photo, audio and text
     * requirements, which will be passed when the task object is 
     * created via the TaskManager
     * @param view
     */
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
    /**
     * RadioButton handler taken from 
     * http://developer.android.com/guide/topics/ui/controls/radiobutton.html
     * This method is called whenever the suer click on the public/private radio 
     * buttons.  Clicking a button will select it and deselect the other if 
     * appropriate, as well as set a boolean variable ot be passed to the new
     * Task object when it is created via the TaskManager.
     * @param view
     */
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
        }
    }
    private void cancelTask(View view)
    {
    	finish();
    }
}
