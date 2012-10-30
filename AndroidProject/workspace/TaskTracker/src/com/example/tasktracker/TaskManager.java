package com.example.tasktracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import android.provider.Settings.Secure;
import android.content.Context;


public class TaskManager{
	
	private static final String FILE_NAME = "tasks.sav";
	
	private ArrayList<Task> TaskList;
	
	private Context context;
	
	private String userId; 
	
	private static TaskManager instance = null;
	
	protected TaskManager(Context context){
		this.TaskList = new ArrayList<Task>();
		this.context = context;
		this.userId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID); 
	}
	
	public static TaskManager getInstance(int type, Context context)
	{
		TaskManager ret = null;
		if( type == 1)//change if we need to accommodate multiple servers
		{
			if(instance == null)
			{
				instance = new TaskManager(context);
			}
			
			ret = instance;
		}
		
		return ret;
	}
	
	public void updateName(int index, String name){
		TaskList.get(index).setTaskName(name);
		saveTasks();
	}
	
	public void updateDesc(int index, String desc){
		TaskList.get(index).setTaskDescription(desc);
		saveTasks();
	}
	
	public void updateRequirements(int index, boolean text, boolean photo, boolean audio){
		TaskList.get(index).setWantText(text);
		TaskList.get(index).setWantPhoto(photo);
		TaskList.get(index).setWantAudio(audio);
		saveTasks();
	}
	
	public void updateOpen(int index, boolean open){
		TaskList.get(index).setIsOpen(open);
		saveTasks();
	}
	
	public void updatePublic(int index, boolean pub){
		TaskList.get(index).setIsPublic(pub);
		saveTasks();
	}
	
	public void createTask(String name, String desc, boolean wantText, boolean wantPhoto, boolean wantAudio, boolean isPublic){
		Task newTask = new Task(userId, name, desc, wantText, wantPhoto, wantAudio, isPublic);
		TaskList.add(newTask);
		saveTasks();
	}
	
	public void deleteTask(int index){
		TaskList.remove(index);
		saveTasks();
	}
	
	public void addSubmission(int index, String text, ArrayList<File> images, ArrayList<File> audio){
		TaskList.get(index).addSubmission(userId, text, images, audio);
		saveTasks();
	}
	
	public void removeSubmission(int taskIndex, int subIndex){
		TaskList.get(taskIndex).removeSubmission(subIndex);
		saveTasks();
	}
	
	public ArrayList<Task> getTaskList(){
		this.TaskList = loadTasks();
		//Eventually load public tasks from service manager
		return TaskList;
	}
	
	private void saveTasks(){
		 try {  
	            FileOutputStream fOut = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
	        	ObjectOutputStream oOut = new ObjectOutputStream(fOut);    
	            oOut.writeObject(TaskList);
	            oOut.flush();
	            oOut.close();  
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<Task> loadTasks(){
	    ArrayList<Task> loadedTasks = new ArrayList<Task>();
    	try {  
        	FileInputStream fIn = context.openFileInput(FILE_NAME);
            ObjectInputStream oIn = new ObjectInputStream(fIn);  
            loadedTasks = (ArrayList<Task>) oIn.readObject();  
            oIn.close(); 
        } 
         catch (IOException e) {  
            e.printStackTrace();  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        }
        return loadedTasks;
	}
}
