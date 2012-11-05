package com.example.tasktracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


//Some camera code taken from http://mobile.tutsplus.com/tutorials/android/android-sdk-quick-tip-launching-the-camera/
public class PhotoTaker extends Activity //implements SurfaceHolder.Callback
{
    private final static int CAMERA_PIC_REQUEST = 4444;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_taker);
        
        Button usePhoto = (Button) findViewById(R.id.button_photo_use);
        Button takePhoto = (Button) findViewById(R.id.button_photo_take);
        Button cancel = (Button) findViewById(R.id.photo_cancel);
        
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                cancelPhoto(v);
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
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");  
            ImageView image = (ImageView) findViewById(R.id.takePhoto);  
            image.setImageBitmap(thumbnail); 
        }  }
        catch (Exception e){
            
        }
    }
    public void usePhoto(View view){
        
    }
    
    public void cancelPhoto(View view)
    {
    	finish();
    }
}
