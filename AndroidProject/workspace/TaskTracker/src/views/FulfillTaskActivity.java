
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

import java.io.FileNotFoundException;
import java.util.ArrayList;

import model.AudioFile;
import model.Fulfillment;
import model.ImageFile;
import model.Task;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private final static int SELECT_IMAGE = 2;
    private final static int AUDIO_REQUEST = 3;
    private TextView nameView, descView, responseView, scopeView;
  //  private ArrayList<ImageFile> photos;
    private ArrayList<AudioFile> audio;
    private int index;
    private Task curTask;
    public static Fulfillment fulfillment;
    private EditText textResponse;
    private String android_id;
    /**
     * When the activity is created, initialise appropriate UI components
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
        //curTask = (Task) getIntent().getSerializableExtra("task");
        //photos = (ArrayList<ImageFile>) getIntent().getSerializableExtra("images");
        index = data.getInt("index");
        curTask = TaskManager.getInstance(1, this).getViewableTaskList().get(index);
        
        if(curTask.getIsOpen() == false)
        {
        	Toast toast = Toast.makeText(this, "This task is closed for submissions", 5);
            toast.show();
        	finish();
        }
        
        //Initialise a fulfillment with default values
        android_id = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);
        fulfillment = new Fulfillment(android_id,""/*,new ArrayList<ImageFile>(),new ArrayList<AudioFile>()*/);
        //	public Fulfillment(String ownerId, String text, ArrayList<ImageFile> images, ArrayList<AudioFile> audio){
        
        nameView = (TextView) findViewById(R.id.text_fulfill_name);
        nameView.setText("Task Name: "+curTask.getTaskName());
        
        descView = (TextView) findViewById(R.id.text_fulfill_taskdesc);
        descView.setText("Description: "+curTask.getTaskDescription());
             
        responseView = (TextView) findViewById(R.id.text_fulfill_desired);
        responseView.setText("Desired Responses: "+curTask.buildResponses());
        
        scopeView = (TextView) findViewById(R.id.text_fulfill_scope);
        scopeView.setText("Task Scope: "+buildScope(curTask));
        
        textResponse = (EditText)  findViewById(R.id.text_edit_textresponse);
        
        
        Button takePhoto = (Button) findViewById(R.id.button_fulfill_takePhoto);
        //Button photoMem = (Button) findViewById(R.id.button_fulfill_photoMem);
        Button recordAudio = (Button) findViewById(R.id.button_fulfill_recordAudio);
        //Button audioMem = (Button) findViewById(R.id.button_fulfill_audioMem);
        Button cancel = (Button) findViewById(R.id.button_fulfill_cancel);
        Button save = (Button) findViewById(R.id.button_fulfill_save);
        
        audio = new ArrayList<AudioFile>();
 //     photos = new ArrayList<ImageFile>();
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
    	setResult(RESULT_OK);
    	Intent intent = new Intent(FulfillTaskActivity.this, PhotoTakerActivity.class);
        //intent.putExtra("task",curTask);
    	intent.putExtra("task",index);
        //intent.putExtra("images", photos);
    	startActivityForResult(intent,PIC_REQUEST); 
    }
    /**
     * When implemented, when the user clicks on the "Record Audio" button
     * they will see a new activity which will let them record using the
     * android device's microphone. 
     * @param view
     */
    public void recordAudio(View view)
    {
    	setResult(RESULT_OK);
    	Intent intent = new Intent(FulfillTaskActivity.this, AudioRecorderActivity.class);
    	intent.putExtra("task",index);
    	startActivityForResult(intent, AUDIO_REQUEST);
    }
    /**
     * When implemented, will allow the user to select a photo from the phone's
     * storage for use in a fulfillment
     * @param view
     */
    public void findPhoto(View view)
    {
    	setResult(RESULT_OK);
    	
    	startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), SELECT_IMAGE);

    }
    /**
     * When implemented, will allow the user to select an audio file
     * from the phone's storage for use in a fulfillment
     * @param view
     */
    public void findAudio(View view)
    {
    	setResult(RESULT_OK);
    	Intent intent = new Intent(FulfillTaskActivity.this, MemoryCheckActivity.class);
    	//set extra for type
    	startActivity(intent); //change to require return
    	//get returned audio
    	//if not null add to fulfillment
    }
    
    public void cancelFulfill(View view)
    {
    	setResult(RESULT_OK);
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
    	setResult(RESULT_OK);
    	//update the fulfillment with the current text from the text box
    	fulfillment.setTextInput(textResponse.getText().toString().trim());
    	//Ask for confirmation
    	//Send fulfillment to task manager
        TaskManager manage = TaskManager.getInstance(1, this);
        fulfillment.belongsTo = manage.getViewableTaskList().get(index).id;
        
        Toast toast;
        if(manage.getViewableTaskList().get(index).getWantText() && fulfillment.getTextInput().trim().equals("")){
    		toast = Toast.makeText(this, "This task requires a text repsonse", 5);
            toast.show();
            return;
        }
        if(manage.getViewableTaskList().get(index).getWantAudio() && fulfillment.getAudioFiles().size() == 0){
    		toast = Toast.makeText(this, "This task requires an audio repsonse", 5);
            toast.show();
            return;
        }
        if(manage.getViewableTaskList().get(index).getWantPhoto() && fulfillment.getImageFiles().size() == 0){
    		toast = Toast.makeText(this, "This task requires an image repsonse", 5);
            toast.show();
            return;
        }

        manage.addSubmission(index, fulfillment);

       	finish();
    }
    //With the new dynamically built fulfillment, this code might be unnecessary
    //because he task is not updated by the photo taker and the photos
    //are added to this class's static fulfillment

    /**
     * This method builds a string of the privacy setting of the given task t.
     * @param t
     * @return
     */
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
