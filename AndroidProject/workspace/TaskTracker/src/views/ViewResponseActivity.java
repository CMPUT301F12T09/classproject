
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

import model.Fulfillment;
import model.Task;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tasktracker.R;

public class ViewResponseActivity extends Activity {
	private Task curTask;
	private int index;
	private Fulfillment ful;
	private TextView taskName, taskDesc, textResponse;
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
        curTask = (Task) getIntent().getSerializableExtra("task");
        //photos = (ArrayList<ImageFile>) getIntent().getSerializableExtra("images");
        index = data.getInt("index");
        ful = (Fulfillment) getIntent().getSerializableExtra("response");
       
        Button fulfillTask = (Button) findViewById(R.id.button_view_fulfill);
        Button mainMenu = (Button) findViewById(R.id.button_view_cancel);
        fulfillTask.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                goToFulfill(v);
            }
        });
        
        mainMenu.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                cancel(v);
            }
        });
        
        
        taskName = (TextView) findViewById(R.id.response_taskname); 
        taskName.setText("Task Name: " + curTask.getTaskName());
        taskDesc = (TextView) findViewById(R.id.response_desc); 
        taskDesc.setText("Description: " + curTask.getTaskDescription());
        textResponse = (TextView) findViewById(R.id.response_textresponse); 
        textResponse.setText("Text Response: " + ful.getTextInput());        
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
    	Intent intent = new Intent(this,FulfillTaskActivity.class);
        intent.putExtra("task",curTask);
        startActivity(intent);
    }
    /**
     * return to the main menu
     * @param view
     */
    public void cancel(View view){
    	finish();
    }
}
