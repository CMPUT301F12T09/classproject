
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

package com.example.tasktracker;

import java.io.File;
import java.util.ArrayList;
import android.provider.Settings.Secure;
import android.content.Context;
/**
 * This class manages all the tasks that the system needs to deal with
 * both those stored locally and on the webservice.  This manager is 
 * accessed via a getInstance() method from various points in the program 
 * wherever tasks must be interacted with. 
 * @author zturchan
 *
 */

public class TaskManager{
		
	private ArrayList<Task> TaskList;
	
	private String userId; 
	
	private static TaskManager instance = null;
	
	private DatabaseManager dbManager = null;
	
	private ServiceManager sManager = null;
	
	protected TaskManager(Context context){
		this.TaskList = new ArrayList<Task>();
		this.dbManager = DatabaseManager.getInstance(1, context);
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
	/**update name of task at index
	*/
	public void updateName(int index, String name){
		TaskList.get(index).setTaskName(name);
		
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		    
		    
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	/**update description of task at index*/
	public void updateDesc(int index, String desc){
		TaskList.get(index).setTaskDescription(desc);
		
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	/**
	 * Update the requirements of a task (photo/audio/text)
	 * @param index
	 * @param text
	 * @param photo
	 * @param audio
	 */
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
	/**
	 * Update whether or not the task is still accepting new fulfillments
	 * @param index
	 * @param open
	 */
	public void updateOpen(int index, boolean open){
		TaskList.get(index).setIsOpen(open);
		
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	/**
	 * Update whether or not other users can see the task
	 * @param index
	 * @param pub
	 */
	public void updatePublic(int index, boolean pub){
		TaskList.get(index).setIsPublic(pub);
		
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	/**
	 * Create a new task and add it to the master list.
	 * @param name
	 * @param desc
	 * @param wantText
	 * @param wantPhoto
	 * @param wantAudio
	 * @param isPublic
	 */
	public void createTask(String name, String desc, boolean wantText, boolean wantPhoto, boolean wantAudio, boolean isPublic){
		Task newTask = new Task(userId, name, desc, wantText, wantPhoto, wantAudio, isPublic);
		TaskList.add(newTask);
		
		//If public update via service manager
		if(isPublic){
		        sManager.saveToService(newTask);
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	/**
	 * Delete a task from the master list
	 * @param index
	 */
	public void deleteTask(int index){
		//find task to delete so we can check the public flag
		Task toRemove = TaskList.get(index);
		//remove task
		TaskList.remove(toRemove);
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
		        sManager.removeFromService(toRemove);
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	/**
	 * Add a fulfillment to a given task
	 * @param index
	 * @param text
	 * @param images
	 * @param audio
	 */
	public void addSubmission(int index, String text, ArrayList<ImageFile> images, ArrayList<AudioFile> audio){
		TaskList.get(index).addSubmission(userId, text, images, audio);
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
		        Fulfillment ful = new Fulfillment(userId, text,images, audio);
		        sManager.saveToService(ful);
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
	/**
	 * Remove an unwanted fulfillment from a task
	 * @param taskIndex
	 * @param subIndex
	 */
	public void removeSubmission(int taskIndex, int subIndex){
	        ArrayList<Fulfillment> sub = TaskList.get(taskIndex).getSubmissions();
	        Fulfillment ful = sub.get(subIndex);
	        TaskList.get(taskIndex).removeSubmission(subIndex);
		//If public update via service manager
		if(TaskList.get(taskIndex).getIsPublic()){
		        sManager.removeFromService(ful);
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}
		
	public ArrayList<Task> getTaskList(){
		//get a fresh copy of the TaskList
	        this.TaskList = dbManager.loadTasks();
		
		return TaskList;
	}	
	
	public void addTask(Task toAdd)
	{
		TaskList.add(toAdd);
		dbManager.saveTasks(this.TaskList);
	}
	
	public void clearTasks()
	{
		this.TaskList.clear();
		dbManager.saveTasks(this.TaskList);
	}
}
