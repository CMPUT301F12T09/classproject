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

package model;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This task represents a task that can be created, fulfilled, 
 * edited and viewed.  Tasks are savableToService so they can be stored to the
 * webservice via the ServiceManager
 * @author zturchan
 *
 */


public class Task extends SavableToService implements Serializable//, Parcelable.
{
	
	//default serial for serializable object
	private static final long serialVersionUID = 1L;

	//Name of task
	private String taskName;
	
	//Description of task
	private String taskDescription;
	
	//stores booleans in the form 0b(photo)(text)(audio)(open)(public)000
	private byte flags;
	
	//Device Id of user who created task
	private String userDeviceId;
	
	//email address of task creator if the want email updates
	private String email = "";
	
	//List of fulfillments attached to this task;
	private ArrayList<Fulfillment> submissions;
	
	//id for sql database storage
	private long db_Id;
	
	/**
	 * Parcelable Constructor 
	 * code from techdroid.kbeanie.com/2010/06/parcelable-how-to-do-that-in-android.html 
	 * @param in
	 */
	public Task(Parcel in){
	    readFromParcel(in);
	}

	private void readFromParcel(Parcel in){
	       taskName = in.readString();
	       taskDescription = in.readString();
	       flags = in.readByte();
	       userDeviceId = in.readString();
	       submissions = new ArrayList<Fulfillment>();
	       in.readList(submissions,null);
	}
/*	@Override
	public void writeToParcel(Parcel out, int arg){
	    out.writeString(taskName);
	    out.writeString(taskDescription);
	    out.writeByte(flags);
	    out.writeString(userDeviceId);
	    out.writeList(submissions);
    	}

	@Override
	public int describeContents(){
	    return 0;
	}
*/	
	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>(){
	    public Task createFromParcel(Parcel in){
	        return new Task(in);
	    }
	    public Task[] newArray(int size){
	        return new Task[size];
	    }
	};
	/**
	 * Constructor for a task given all the appropriate info as provided by the user
	 * @param ownerId
	 * @param name
	 * @param desc
	 * @param wantText
	 * @param wantPhoto
	 * @param wantAudio
	 * @param isPublic
	 * @param email
	 */
	//Create task and set fields
	public Task(String ownerId, String name, String desc, boolean wantText, boolean wantPhoto, boolean wantAudio, boolean isPublic, String email){
		this.taskName = name;
		this.taskDescription = desc;
		this.setWantText(wantText);
		this.setWantPhoto(wantPhoto);
		this.setWantAudio(wantAudio);
		this.setIsPublic(isPublic);
		this.setIsOpen(true);
		this.userDeviceId = ownerId;
		this.submissions = new ArrayList<Fulfillment>();
		this.email = email;
	}
	public Task()
	{
		this.submissions = new ArrayList<Fulfillment>();
	}

	/**
	 * Add a new fulfillment from a given user to the task
	 * @param ownerId
	 * @param text
	 * @param images
	 * @param audio
	 */
	//add a new fulfillment to the submissions list
/*	public void addSubmission(String ownerId, String text, ArrayList<ImageFile> images, ArrayList<AudioFile> audio){
		Fulfillment ful = new Fulfillment(ownerId, text,images, audio);
		ful.belongsTo = this.id;
		this.submissions.add(ful);
	}*/
	public void addSubmission(Fulfillment ful){
		this.submissions.add(ful);
	}
	/**
	 * Remove a fulfillment from the list of existing reponses
	 * @param index
	 */
	//remove a fulfillment from the submissions list
	public void removeSubmission(int index){
		this.submissions.remove(index);
	}
	
	//remaining methods are simple getters and setters for data fields
	public String getUserDeviceId(){
		return userDeviceId;
	}
	
	public void setUserDeviceId(String deviceId){
		this.userDeviceId = deviceId;
	}
	
	public String getTaskName(){
		return taskName;
	}
	
	public void setTaskName(String name){
		this.taskName = name;
	}
	
	public String getTaskDescription(){
		return taskDescription;
	}
	
	public void setTaskDescription(String desc){
		this.taskDescription = desc;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	public String getEmail(){
		return email;
	}
	
	public boolean getWantPhoto(){
		if(((flags >>> 7) & 1) == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public void setWantPhoto(boolean photo){
		if (photo){
			this.flags = (byte) (flags | (1 << 7));
		}else{
			this.flags = (byte) (flags & ~(1 << 7));
		}
	}
	
	public boolean getWantText(){
		if(((flags >>> 6) & 1) == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public void setWantText(boolean text){
		if (text){
			this.flags = (byte) (flags | (1 << 6));
		}else{
			this.flags = (byte) (flags & ~(1 << 6));
		}
	}
	
	public boolean getWantAudio(){
		if(((flags >>> 5) & 1) == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public void setWantAudio(boolean audio){
		if (audio){
			this.flags = (byte) (flags | (1 << 5));
		}else{
			this.flags = (byte) (flags & ~(1 << 5));
		}
	}
	
	public boolean getIsOpen(){
		if(((flags >>> 4) & 1) == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public void setIsOpen(boolean open){
		if (open){
			this.flags = (byte) (flags | (1 << 4));
		}else{
			this.flags = (byte) (flags & ~(1 << 4));
		}
	}
	
	public boolean getIsPublic(){
		if(((flags >>> 3) & 1) == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public void setIsPublic(boolean pub){
		if (pub){
			this.flags = (byte) (flags | (1 << 3));
		}else{
			this.flags = (byte) (flags & ~(1 << 3));
		}
	}
	
	public ArrayList<Fulfillment> getSubmissions()
	{
		return submissions;
	}
	
	public void addFulfillment(Fulfillment toAdd)
	{
		submissions.add(toAdd);
	}
	
	public long getDbId(){
		return db_Id;
	}
	
	public void setDbId(long id){
		this.db_Id = id;
	}
	
	/**
	 * Encodes the task itself as a string for storage and loading
	 */
	@Override
	public String toString() {  
		String scope;
		String requirements = "";
/*		if(this.getIsPublic()){
			scope = "Public";
		}else{
			scope = "Private";
		}
		if(this.getWantText()){
			requirements += "Text";
		}
		if(this.getWantPhoto()){
			requirements += " Photo";
		}else
		if(this.getWantAudio()){
			requirements += " Audio";
		}
		
		String task = String.format("Task Name: %s\nDescription: %s\nScope: %s \n" +
									 "Required: %s", this.taskName, this.taskDescription,
										scope, requirements);*/
		//As per Victor's request - only display task name.  
		
		return this.taskName;
        //return task;
	}
	/**
	 * Save the task itself as a String (called by Service Manager)
	 */
	//Called by the service manager to get the string to be sent to the service
	public String saveToString()
	{
		//Format of (service given id) (service given id of owner) (type of object) (body string)
		if(getEmail().equals(""))
		{
			setEmail("NOEMAIL");
		}
		String ret;
		body = String.format("%s+%s+%s+%b+%b+%b+%b+%b+%s", userDeviceId, taskName, taskDescription, getWantText(), getWantPhoto(), getWantAudio(), getIsPublic(), getIsOpen(), getEmail());
		
		ret = String.format("%s+%s+%s+%s", id, id, "TASK", body);
		//System.out.println(ret);
		return ret;
	}
	
	//Called by the service manager when building the objects pulled from the server
	/**
	 * Build a new task from an encoded string (called by service manager)
	 * @param data
	 * @return
	 */
	public static Task buildFromString(String data)
	{
		//parse the string and get the required data
		//We should get the body string from the saveToString method
		
		//System.out.println(data);
		
		String deliminator = "[+]+";
		String[] tokens = data.split(deliminator);
		
		String ownerId = tokens[0]; 
		String name = tokens[1];
		String desc = tokens[2];
		boolean wantText = tokens[3].equals("true");
		boolean wantPhoto = tokens[4].equals("true");
		boolean wantAudio = tokens[5].equals("true");
		boolean isPublic = tokens[6].equals("true");
		boolean isOpen = tokens[7].equals("true");
		String email = tokens[8];
		
		//last argument is empty, should be email, needs to be implemented in save
		Task ret = new Task(ownerId, name, desc, wantText, wantPhoto, wantAudio, isPublic, email);
		ret.setIsOpen(isOpen);
		
		return ret;
	}

	/**
	 * This method builds a string of the requirements for a given task t.
	 * @return
	 */
	public String buildResponses() {
		String ret = "";
		if (getWantText()) {
			ret = ret + "Text ";
		}
		if (getWantPhoto()) {
			ret = ret + "Photo ";
		}
		if (getWantAudio()) {
			ret = ret + "Audio";
		}
		return ret;
	}
}
