
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

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.net.Uri;
import android.provider.MediaStore;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * This class handles the taking of photos on-the-fly for task
 * fulfillment submissions.  The user may take as many photos as they
 * want in sequence, and after each one that the user deems satisfactory
 * they may tap "Use Photo" to add it to the list of submitted photos
 * for the current Task Fulfillment.  The user may submit as many
 * pictures as they want, and only needs to tap "Done" when they have 
 * finished. 
 * @author zturchan
 *
 */
//Some camera code taken from http://mobile.tutsplus.com/tutorials/android/android-sdk-quick-tip-launching-the-camera/
public class PhotoTaker extends Activity //implements SurfaceHolder.Callback
{
    private Bitmap thumbnail;
    private Task curTask;
    private final static int CAMERA_PIC_REQUEST = 4444;
    private ArrayList<ImageFile> photos;
    @Override
    /**
     * Create all UI elements and connect the appropriate listeners.
     * Also get the current set of photos so that we will add to them
     * for the current fulfillment.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_taker);
        
        Bundle data = getIntent().getExtras();
        //curTask =(Task) data.getParcelable("task");

        curTask = (Task) getIntent().getSerializableExtra("task");
        photos = (ArrayList<ImageFile>) getIntent().getSerializableExtra("images");

        Button usePhoto = (Button) findViewById(R.id.button_photo_use);
        Button takePhoto = (Button) findViewById(R.id.button_photo_take);
        Button done = (Button) findViewById(R.id.photo_done);
        
        done.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                donePhoto(v);
            }
        });
        
        takePhoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                setBogoPic();
            }
        });
        
        usePhoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                usePhoto(v);
            }
        });
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_photo_taker, menu);
        return true;
    }
    /**
     * Call the android photo app so that the user can take a photo. 
     * Result will be returned as a Bitmap.
     * @param view
     */
    public void takePhoto (View view){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        }
        catch(Exception e){
            
        }
    }
    /**
     * When the requested activity (camera capture) returns, we store it 
     * as a bitmap and set the imageview so the user can see the picture
     * they have just taken and choose whetehr or not to use it. 
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        try{
        if (requestCode == CAMERA_PIC_REQUEST) {  
            thumbnail = (Bitmap) data.getExtras().get("data");  
            ImageView image = (ImageView) findViewById(R.id.takePhoto);  
            image.setImageBitmap(thumbnail); 
        }  }
        catch (Exception e){
            
        }
    }
    /**
     * Add the current photo to the list that will 
     * eventually be passed to the new fulfillment
     * Also display a confirmation message
     * @param view
     */
    public void usePhoto(View view){
        //photos.add(new ImageFile(thumbnail));
        FulfillTask.fulfillment.addImage(new ImageFile(thumbnail));
    	Toast toast = Toast.makeText(this, "Image saved", 5);
        toast.show();
    }
    
    /**
     * When the user has taken enough photos, return and package 
     * in the task and the image list.
     * @param view
     */
    public void donePhoto(View view)
    {
        Intent returnIntent = new Intent(PhotoTaker.this,FulfillTask.class);
        returnIntent.putExtra("task",curTask);
        //returnIntent.putExtra("images", photos);
        setResult(RESULT_OK,returnIntent);  
        finish();
    }
    
    private Bitmap ourBMP;
	protected void setBogoPic() {
		ImageButton button = (ImageButton) findViewById(R.id.takePhoto);
		ourBMP = PictureGenerator.generateBitmap(400,400);
		button.setImageBitmap(ourBMP);
	}
}
