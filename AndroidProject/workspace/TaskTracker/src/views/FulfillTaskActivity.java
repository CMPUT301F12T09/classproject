
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
import model.Fulfillment;
import model.Task;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tasktracker.R;

import controllers.TaskManager;
/**
 * This class is an activity which allows the user to perform the actions
 *  requested by a task author.  
 * <ul compact>
 * <li>Text Response</li>
 * <li>Photo (from memory or on the fly)</li>
 * <li>Audio (from memory or on the fly)</li>
 * </ul>
 * @author zturchan
 *
 */
public class FulfillTaskActivity extends Activity {
    
    private final static int PIC_REQUEST = 1;
    private TextView nameView, descView, responseView, scopeView;
  //  private ArrayList<ImageFile> photos;
    private ArrayList<AudioFile> audio;
    private int index;
    private Task curTask;
    public static Fulfillment fulfillment;
    private EditText textResponse;
    private String android_id;
    /**
     * When the activity is created, initialize appropriate UI components
     * and connect the appropriate listeners.  We also set the text of the 
     * information TextViews to reflect that information entered by the 
     * author to describe the task (name, description, responses, scope)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulfill_task);
        
        Bundle data = getIntent().getExtras();
        //curTask =(Task) data.getParcelable("task");
        curTask = (Task) getIntent().getSerializableExtra("task");
        //photos = (ArrayList<ImageFile>) getIntent().getSerializableExtra("images");
        index = data.getInt("index");
        
        //Initialize a fulfillment with default values
        android_id = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);
        fulfillment = new Fulfillment(android_id,""/*,new ArrayList<ImageFile>(),new ArrayList<AudioFile>()*/);
        //	public Fulfillment(String ownerId, String text, ArrayList<ImageFile> images, ArrayList<AudioFile> audio){
        
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
 //     photos = new ArrayList<ImageFile>();
        
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
    
    /**
     * When the user clicks on the "Take Photo" Button, initiate the photo
     * taker activity and pass it the task we're working on and the arrayList
     * of photos we are working with
     * @param view
     */
    public void takePhoto(View view)
    {
    	Intent intent = new Intent(FulfillTaskActivity.this, PhotoTakerActivity.class);
        intent.putExtra("task",curTask);
        //intent.putExtra("images", photos);
    	startActivityForResult(intent,1); 
    }
    /**
     * When implemented, when the user clicks on the "Record Audio" button
     * they will see a new activity which will let them record using the
     * android device's microphone. 
     * @param view
     */
    public void recordAudio(View view)
    {
    	Intent intent = new Intent(FulfillTaskActivity.this, AudioRecorderActivity.class);
    	startActivity(intent); //change to require return
    	//get returned audio
    	//if not null add to fulfillment
    }
    /**
     * When implemented, will allow the user to select a photo from the phone's
     * storage for use in a fulfillment
     * @param view
     */
    public void findPhoto(View view)
    {
    	Intent intent = new Intent(FulfillTaskActivity.this, MemoryCheckActivity.class);
    	//set extra for type
    	startActivity(intent); //change to require return
    	//get returned photo
    	//if not null add to fulfillment
    }
    /**
     * When implemented, will allow the user to select an audio file
     * from the phone's storage for use in a fulfillment
     * @param view
     */
    public void findAudio(View view)
    {
    	Intent intent = new Intent(FulfillTaskActivity.this, MemoryCheckActivity.class);
    	//set extra for type
    	startActivity(intent); //change to require return
    	//get returned audio
    	//if not null add to fulfillment
    }
    
    public void cancelFulfill(View view)
    {
    	finish();
    }
    
    /**
     * When the user has fulfilled the requirements of the task (perhaps more)
     * the system will check to see that the submission is acceptable.  The
     * validity check is to be implemented, but assuming the fulfillment is
     * valid this method will create a new fulfillment object and attach it 
     * to the correct task via the taskmanager
     * @param view
     */
    public void saveFulfill(View view)
    {
    	//update the fulfillment with the current text from the text box
    	fulfillment.setTextInput(textResponse.getText().toString().trim());
    	//Ask for confirmation
    	//Send fulfillment to task manager
        TaskManager manage = TaskManager.getInstance(1, this);
        manage.addSubmission(index, fulfillment);
       	finish();
    }
    //With the new dynamically built fulfillment, this code might be unnecessary
    //because he task is not updated by the photo taker and the photos
    //are added to this class's static fulfillment
    /**
     * This method is called when the activity we called with a result request 
     * returns to us.  Currently it only checks for picture request, but 
     * will also check for audio requests when that is implemented.
     * The result is simply updating the array of files to hold whatever 
     * new ones we got from the activity.  
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PIC_REQUEST) {
            if(resultCode == RESULT_OK){
                curTask=(Task)data.getSerializableExtra("task");
                //photos=(ArrayList<ImageFile>)data.getSerializableExtra("images");
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