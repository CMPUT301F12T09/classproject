package com.example.tasktracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

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
    
    public void confirmTask(View view)
    {
        String name = editName.getText().toString();
        String desc = editDesc.getText().toString();
        
        TaskManager manager = TaskManager.getInstance(1, this);
        manager.createTask(name, desc, wantText, wantPhoto, wantAudio, isPublic);
        
    	finish();
    }
    //Checkbox handler taken from http://developer.android.com/guide/topics/ui/controls/checkbox.html
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
    //RadioButton handler taken from http://developer.android.com/guide/topics/ui/controls/radiobutton.html
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
    public void cancelTask(View view)
    {
    	finish();
    }
}
