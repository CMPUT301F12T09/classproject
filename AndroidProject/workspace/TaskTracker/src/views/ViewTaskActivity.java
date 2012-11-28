
/**
*	Copyright 2012 Zak Turchansky, Evan Fauser, Gordon Lancop, Seth Davis
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
import model.Task;
import com.example.tasktracker.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * This class is an activity that allows a user to view a task
 * before they decide to fulfill it.  If the user wants, they can
 * access the fulfill activity directly from this view instead of having 
 * to go via the main screen.
 * 
 * This class displays task name, description, scope, and requirements to the user
 * 
 * @author zturchan
 *
 */
public class ViewTaskActivity extends Activity {

    private ListView submissions;
    private ArrayAdapter<Fulfillment> adapter;
    private ArrayList<Fulfillment> FulfillmentList;
    private Task task;
	private int index;
    /**
     * Initialize appropriate UI components and connect listeners
     * Also update text on screen with values from the task object 
     * accessed via the Task Manager.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        
        Bundle bundle = getIntent().getExtras();
        task = (Task) bundle.get("Task");
        index = bundle.getInt("index");

        TextView name = (TextView) findViewById(R.id.text_view_name); 
        name.setText("Task Name: " + task.getTaskName());
        TextView description = (TextView) findViewById(R.id.text_view_taskdesc); 
        description.setText("Description: " + task.getTaskDescription());
        TextView wanted = (TextView) findViewById(R.id.text_view_desired);
        String desired = "Desired Responses: ";
        if(task.getWantText()){
        	desired += "Text";
        }
        if(task.getWantPhoto()){
        	desired += " Photo";
        }
        if(task.getWantAudio()){
        	desired += " Audio";
        }
        wanted.setText(desired);
        TextView open = (TextView) findViewById(R.id.text_view_openclose); 
        if(task.getIsOpen()){
        	open.setText("Availability: Open");
        }else{
        	open.setText("Availability: Closed");
        }
        TextView scope = (TextView) findViewById(R.id.text_fulfill_scope);
        if(task.getIsPublic()){
        	scope.setText("Task Scope: Public");
        }else{
        	scope.setText("Task Scope: Private");
        }
        submissions = (ListView) findViewById(R.id.oldLogs);
        FulfillmentList = task.getSubmissions();
    	adapter = new ArrayAdapter<Fulfillment>(this, R.layout.task_display, FulfillmentList);
    	submissions.setAdapter(adapter);
    	
    	submissions.setOnItemClickListener(new OnItemClickListener(){
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    			Intent intent = new Intent(ViewTaskActivity.this, ViewResponseActivity.class);
                intent.putExtra("task",task);
                intent.putExtra("taskindex",index);
                Fulfillment ful = (Fulfillment)submissions.getItemAtPosition(position);
                intent.putExtra("response", ful);
                startActivity(intent);
    			
    		}
    	});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_task, menu);
        return true;
    }
    /**
     * access the fulfill activity as a shortcut
     * @param view
     */
    public void goToFulfill(View view){
    	setResult(RESULT_OK);
    	Intent intent = new Intent(this,FulfillTaskActivity.class);
        intent.putExtra("task",task);
        startActivity(intent);
    }
    /**
     * return to the main menu
     * @param view
     */
    public void returnToMain(View view){
    	setResult(RESULT_OK);
    	finish();
    }
}