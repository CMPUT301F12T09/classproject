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


//Some camera code taken from http://mobile.tutsplus.com/tutorials/android/android-sdk-quick-tip-launching-the-camera/
public class PhotoTaker extends Activity //implements SurfaceHolder.Callback
{
    private Bitmap thumbnail;
    private Task curTask;
    private final static int CAMERA_PIC_REQUEST = 4444;
    private ArrayList<ImageFile> photos;
    @Override
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
                takePhoto(v);
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
        
    public void takePhoto (View view){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        }
        catch(Exception e){
            
        }
    }
    
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
    public void usePhoto(View view){
        photos.add(new ImageFile(thumbnail));
        Toast toast = Toast.makeText(this, "Image saved", 5);
        toast.show();
    }
    
    public void donePhoto(View view)
    {
        Intent returnIntent = new Intent(PhotoTaker.this,FulfillTask.class);
        returnIntent.putExtra("task",curTask);
        returnIntent.putExtra("images", photos);
        setResult(RESULT_OK,returnIntent);  
        finish();
    }
}
