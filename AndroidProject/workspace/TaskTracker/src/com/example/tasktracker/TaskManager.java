package com.example.tasktracker;

import java.util.ArrayList;
import android.provider.Settings.Secure;
import android.content.Context;


public class TaskManager{
		
	private ArrayList<Task> TaskList;
	
	private String userId; 
	
	private static TaskManager instance = null;
	
	private DatabaseManager dbManager = null;
	
	private ServiceManager sManager = null;
	
	protected TaskManager(Context context){
		this.dbManager = DatabaseManager.getInstance(1, context);
		this.TaskList = dbManager.loadTasks();
		this.sManager = ServiceManager.getInstance(1);
		this.userId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID); 
		
	}
	
	public static TaskManager getInstance(int type, Context context)
	{
		TaskManager ret = null;
		if(type == 1)//change if we need to accommodate multiple servers
		{
			if(instance == null)
			{
				instance = new TaskManager(context);
			}
			
			ret = instance;
		}
		
		return ret;
	}
	//update name of task at index
	public void updateName(int index, String name){
		TaskList.get(index).setTaskName(name);
		
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	//update description of task at index
	public void updateDesc(int index, String desc){
		TaskList.get(index).setTaskDescription(desc);
		
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	
	public void updateRequirements(int index, boolean text, boolean photo, boolean audio){
		TaskList.get(index).setWantText(text);
		TaskList.get(index).setWantPhoto(photo);
		TaskList.get(index).setWantAudio(audio);
		
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	
	public void updateOpen(int index, boolean open){
		TaskList.get(index).setIsOpen(open);
		
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	
	public void updatePublic(int index, boolean pub){
		TaskList.get(index).setIsPublic(pub);
		
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	
	public void createTask(String name, String desc, boolean wantText, boolean wantPhoto, boolean wantAudio, boolean isPublic){
		Task newTask = new Task(userId, name, desc, wantText, wantPhoto, wantAudio, isPublic);
		TaskList.add(newTask);
		
		//If public update via service manager
		if(isPublic){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	
	public void deleteTask(int index){
		//find task to delete so we can check the public flag
		Task toRemove = TaskList.get(index);
		//remove task
		TaskList.remove(toRemove);
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	
	public void addSubmission(int index, String text, ArrayList<ImageFile> images, ArrayList<AudioFile> audio){
		TaskList.get(index).addSubmission(userId, text, images, audio);
		
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	
	public void removeSubmission(int taskIndex, int subIndex){
		TaskList.get(taskIndex).removeSubmission(subIndex);
		//If public update via service manager
		if(TaskList.get(taskIndex).getIsPublic()){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	
	public ArrayList<Task> getTaskList(){
		//get a fresh copy of the TaskList
		this.TaskList = dbManager.loadTasks();
		
		//TODO fetch public tasks via service manager
		
		return TaskList;
	}	
	
	public void addTask(Task toAdd)
	{
		TaskList.add(toAdd);
	}
}
