
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

package controllers;

import java.util.ArrayList;
import java.util.Random;

import model.Fulfillment;
import model.Task;
import model.AudioFile;
import model.ImageFile;
import android.content.Context;
import android.provider.Settings.Secure;

 
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
	private ArrayList<Task> ViewedList;
	
	private static String userId; 
	
	private static TaskManager instance = null;
	
	private DatabaseManager dbManager = null;
	
	private ServiceManager sManager = null;
	
	protected TaskManager(Context context){
		this.TaskList = new ArrayList<Task>();
		this.ViewedList = new ArrayList<Task>();
		this.dbManager = DatabaseManager.getInstance(1, context);
		dbManager.open();
		this.sManager = ServiceManager.getInstance(1);
		this.userId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID); 
		
	}
	/**
	 * Request an instance of the task manager singleton of the specified type:
	 * 1 for the current service
	 * @param type
	 * @param context
	 */
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
	 * @param index
	 * @param name
	*/
	public void updateName(int index, String name){
		TaskList.get(index).setTaskName(name);
		
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		    
		    
		}
		//Either way update local storage;
		dbManager.updateTask(TaskList.get(index));
	}
	/**update description of task at index
	 * @param index
	 * @param desc
	 */
	public void updateDesc(int index, String desc){
		TaskList.get(index).setTaskDescription(desc);
		
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
			//TODO update public storage via service manager;
		}
		//Either way update local storage;
		dbManager.updateTask(TaskList.get(index));
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
		dbManager.updateTask(TaskList.get(index));
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
		dbManager.updateTask(TaskList.get(index));
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
		dbManager.updateTask(TaskList.get(index));
	}
	/**
	 * Create a new task and add it to the master list.
	 * @param name
	 * @param desc
	 * @param wantText
	 * @param wantPhoto
	 * @param wantAudio
	 * @param isPublic
	 * @param email
	 */
	public void createTask(String name, String desc, boolean wantText, boolean wantPhoto, boolean wantAudio, boolean isPublic, String email){
		Task newTask = new Task(userId, name, desc, wantText, wantPhoto, wantAudio, isPublic, email);
		TaskList.add(newTask);
		
		dbManager.newTask(newTask);
		
		sManager.requestSaveOut(newTask, "TASK", this);
	}
	/**
	 * Delete a task from the master list
	 * @param index
	 */
	public void deleteTask(int index){
		//find task to delete so we can check the public flag
		Task toRemove = ViewedList.get(index);
		//remove task
		TaskList.remove(toRemove);
		ViewedList.remove(toRemove);
		
		sManager.removeFromService(toRemove);
		ArrayList<Fulfillment> tempSubs = toRemove.getSubmissions();
		for(int i = 0; i < tempSubs.size(); i++)
		{
			ArrayList<AudioFile> tempAudio = tempSubs.get(i).getAudioFiles();
			for(int j = 0; j < tempAudio.size(); j++)
			{
				sManager.removeFromService(tempAudio.get(j));
			}
			
			ArrayList<ImageFile> tempImages = tempSubs.get(i).getImageFiles();
			for(int j = 0; j < tempImages.size(); j++)
			{
				sManager.removeFromService(tempImages.get(j));
			}
			
			sManager.removeFromService(tempSubs.get(i));
		}
		
		dbManager.removeTask(toRemove);
	}
	/**
	 * Add a fulfillment to a given task
	 * @param index
	 * @param ful
	 */
/*	public void addSubmission(int index, String text, ArrayList<ImageFile> images, ArrayList<AudioFile> audio){
		TaskList.get(index).addSubmission(userId, text, images, audio);
		//If public update via service manager
		if(TaskList.get(index).getIsPublic()){
		        Fulfillment ful = new Fulfillment(userId, text,images, audio);
		        ful.belongsTo = TaskList.get(index).id;
		        sManager.saveToService(ful);
		}
		//Either way update local storage;
		dbManager.saveTasks(TaskList);
	}*/
	public void addSubmission(int index, Fulfillment ful){	
    	this.TaskList = dbManager.loadTasks();
		TaskList.get(index).addSubmission(ful);
    	
		dbManager.addFulfillment(ViewedList.get(index), ful);
		
		//Send email if an address was specified.
		if(!(ViewedList.get(index).getEmail().trim().equals("NOEMAIL"))){

            try {   
                new EmailWrapper().execute(ViewedList.get(index));
            } catch (Exception e) {   
                e.printStackTrace();
            } 
		}
		
		/*
		if(TaskList.get(index).id != null)
		{
			System.out.println("Request");
			sManager.requestSaveOut(ful, "FULLFILMENT", this);
			
			for (int i = 0; i < ful.getImageFiles().size(); i++) {
				ful.getImageFiles().get(i).belongsTo = ful.id;
				sManager.requestSaveOut(ful.getImageFiles().get(i),"IMAGE", this);
				ful.getImageFiles().get(i).setType("Image");
			}
			for (int i = 0; i < ful.getAudioFiles().size(); i++) {
				ful.getAudioFiles().get(i).belongsTo = ful.id;
				sManager.requestSaveOut(ful.getAudioFiles().get(i),"AUDIO", this);
				ful.getImageFiles().get(i).setType("Audio");
			}
		}*/
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
		dbManager.removeFulfillment(ful);
	}
		
	public ArrayList<Task> getTaskList(){
		//get a fresh copy of the TaskList
	        this.TaskList = dbManager.loadTasks();
	        this.ViewedList = this.TaskList;
		
		return TaskList;
	}	
	/**
	 * Returns a list of user tasks and public tasks
	 * @return viewable
	 */
	public ArrayList<Task> getViewableTaskList(){
		this.TaskList = dbManager.loadTasks();
		if(ViewedList.isEmpty())
		{
			this.ViewedList = new ArrayList<Task>();
			for (int i = 0; i < this.TaskList.size(); i++){
				//test
				String tmp2 = this.TaskList.get(i).getUserDeviceId();//.equals(userId);
				
				//Check if we can't view the task i.e. private and not ours
				if (this.TaskList.get(i).getIsPublic() == false && !(this.TaskList.get(i).getUserDeviceId().equals(userId)))
				{
					continue;
				}
				else{
					this.ViewedList.add(this.TaskList.get(i));
				}
			}
		}
		return this.ViewedList;
	}
	
	/**
	 * Returns a list of user tasks and public tasks
	 * @return viewable
	 */
	public ArrayList<Task> refreshViewableTaskList(){
		this.TaskList = dbManager.loadTasks();
		this.ViewedList = new ArrayList<Task>();
		for (int i = 0; i < this.TaskList.size(); i++){
			//test
			String tmp2 = this.TaskList.get(i).getUserDeviceId();//.equals(userId);
			
			//Check if we can't view the task i.e. private and not ours
			if (this.TaskList.get(i).getIsPublic() == false && !(this.TaskList.get(i).getUserDeviceId().equals(userId)))
			{
				continue;
			}
			else{
				this.ViewedList.add(this.TaskList.get(i));
			}
		}
		return this.ViewedList;
	}
	
	/**
	 * Returns a randomised list of tasks
	 * @return ViewedList
	 */
	public ArrayList<Task> getRandomizedList()
	{
		ViewedList.clear();
		Random generator = new Random(System.currentTimeMillis());
		
		for(int i = 0; i < TaskList.size(); i++)
		{
			Task temp = TaskList.get(i);
			
			if(temp.getIsPublic() == false && !(temp.getUserDeviceId().equals(userId)))
			{
				continue;
			}
			
			int index = generator.nextInt() % (ViewedList.size()+1);
			
			System.out.println("**" + index + "**");
			
			if(index < 0)
			{
				index *= -1;
			}
			
			ViewedList.add(index, temp);
		}
		
		return ViewedList;
	}
	
	/**
	 * Returns a alphabeticaly sorted list of tasks
	 * @return ViewedList
	 */
	public ArrayList<Task> getAlphabetizedList()
	{
		ViewedList.clear();
		
		for(int i = 0; i < TaskList.size(); i++)
		{
			Task temp = TaskList.get(i);
			
			if(temp.getIsPublic() == false && !(temp.getUserDeviceId().equals(userId)))
			{
				continue;
			}

			boolean added = false;
			for(int j = 0; j < ViewedList.size(); j++)
			{
				if(temp.getTaskName().compareTo(ViewedList.get(j).getTaskName()) <= 0)
				{
					added = true;
					ViewedList.add(j, temp);
					break;
				}
			}
			if(added == false)
			{
				ViewedList.add(ViewedList.size(), temp);
			}
		}
		
		return ViewedList;
	}
	
	/**
	 * Returns a date sorted list of tasks
	 * @return ViewedList
	 */
	public ArrayList<Task> getDateList()
	{
		ViewedList.clear();
		
		for(int i = 0; i < TaskList.size(); i++)
		{
			Task temp = TaskList.get(i);
			
			if(temp.getIsPublic() == false && !(temp.getUserDeviceId().equals(userId)))
			{
				continue;
			}

			ViewedList.add(ViewedList.size(), temp);
		}
		
		return ViewedList;
	}
	/**
	 * Adds a task to the database and local task list
	 * @param toAdd
	 */
	public void addTask(Task toAdd)
	{
		TaskList.add(toAdd);
		dbManager.newTask(toAdd);
	}
	/**
	 * Clears tasks from the task list and database
	 */
	public void clearTasks()
	{
		this.TaskList.clear();
		dbManager.emptyDatabase();
	}
	/**
	 * Gets user Id
	 * @return userId
	 */
	public String getUserId()
	{
		return userId;
	}
	/**
	 * Refreshes a task
	 * @param toRefresh
	 */
	public void refresh(Task toRefresh)
	{
		dbManager.updateTask(toRefresh);
		for(int i = 0; i < TaskList.size(); i++)
		{
			if(TaskList.get(i).getDbId() == toRefresh.getDbId())
			{
				TaskList.get(i).setId(toRefresh.getId());
				break;
			}
		}
	}
	/**
	 * Refreshes a fulfillment
	 * @param toRefresh
	 */
	public void refresh(Fulfillment toRefresh)
	{
		int breaking = 0;
		dbManager.updateFulfillment(toRefresh);
		for(int i = 0; i < TaskList.size(); i++)
		{
			ArrayList<Fulfillment> temp = TaskList.get(i).getSubmissions();
			for(int j = 0; j < temp.size(); j++)
			{
				if(temp.get(i).getDbId() == toRefresh.getDbId())
				{
					TaskList.get(i).setId(toRefresh.getId());
					breaking = 1;
					break;
				}
			}
		
			if(breaking == 1)
			{
				break;
			}
		}
	}
	public static String getId(){
		return userId;
	}
}
