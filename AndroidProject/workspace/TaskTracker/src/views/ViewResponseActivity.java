
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

import model.Fulfillment;
import model.ImageFile;
import model.SavableToService;
import model.Task;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tasktracker.R;

import controllers.TaskManager;
/**
 * This class is an activity that allows a user to view a fulfillment
 * selected from ViewTaskActivity. It will display 
 * 
 *This class displays task name, description, scope, and requirements to the user
 * 
 * @author glancop
 *
 */
public class ViewResponseActivity extends Activity {
	private Task curTask;
	private int index;
	private ListView attachments;
	private Fulfillment ful;
	private TextView taskName, taskDesc, textResponse;
	private ArrayList<SavableToService> list;
	private ArrayAdapter<SavableToService> adapter;
    /**
     * Will initialize UI components and display info about the fulfillment
     * Also will implement appropriate listeners.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_response);
        
        Bundle data = getIntent().getExtras();
        //curTask =(Task) data.getParcelable("task");
        //curTask = (Task) getIntent().getSerializableExtra("task");
        //photos = (ArrayList<ImageFile>) getIntent().getSerializableExtra("images");
        index = data.getInt("taskindex");
        curTask = TaskManager.getInstance(1, this).getViewableTaskList().get(index);
        //ful = (Fulfillment) getIntent().getSerializableExtra("response");
        int fulindex = data.getInt("responseindex");
        ful = curTask.getSubmissions().get(fulindex);
        
        
        taskName = (TextView) findViewById(R.id.response_taskname); 
        taskName.setText("Task Name: " + curTask.getTaskName());
        taskDesc = (TextView) findViewById(R.id.response_desc); 
        taskDesc.setText("Description: " + curTask.getTaskDescription());
        textResponse = (TextView) findViewById(R.id.response_textresponse); 
        textResponse.setText("Text Response: " + ful.getTextInput());        
        
        attachments = (ListView) findViewById(R.id.attachments);
        list = new ArrayList<SavableToService>();
        for(SavableToService item: ful.getImageFiles()){
        	list.add(item);
        }
        for(SavableToService item: ful.getAudioFiles()){
        	list.add(item);
        }
    	adapter = new ArrayAdapter<SavableToService>(this, R.layout.task_display, list);
    	attachments.setAdapter(adapter);
    	
    	attachments.setOnItemClickListener(new OnItemClickListener() {
    		   @Override
    		   public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
    			   if(position < ful.getImageFiles().size())
    			   {
    				   System.out.println("===\n" + position + "\n===");
    				   showImage(position);
    			   }
    			   else
    			   {
    				   showAudio(position-ful.getImageFiles().size());
    			   }
    		   }
    	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_response, menu);
        return true;
    }
    /**
     * access the fulfill activity as a shortcut
     * @param view
     */
    public void goToFulfill(View view){
    	setResult(RESULT_OK);
    	Intent intent = new Intent(this,FulfillTaskActivity.class);
        //intent.putExtra("task",curTask);
        intent.putExtra("index",index);

        startActivity(intent);
    }
    /**
     * return to the main menu
     * @param view
     */
    public void cancel(View view){
    	setResult(RESULT_OK);
    	finish();
    }
    
    public void showImage(int imageIndex)
    {
    	final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.image_display);
		dialog.setTitle("Image Viewer");

		ImageButton iButton = (ImageButton) dialog.findViewById(R.id.showImageImage);
		iButton.setImageBitmap(ful.getImageFiles().get(imageIndex).bitmap);
		// if button is clicked, close the custom dialog
		iButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
    }
    
    public void showAudio(int imageIndex)
    {
    	final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.image_display);
		dialog.setTitle("Image Viewer");

		ImageButton iButton = (ImageButton) dialog.findViewById(R.id.showImageImage);
		iButton.setImageBitmap(ful.getImageFiles().get(imageIndex).bitmap);
		// if button is clicked, close the custom dialog
		iButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
    }
}
