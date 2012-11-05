package com.example.tasktracker;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;




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
	
	//List of fulfillments attached to this task;
	private ArrayList<Fulfillment> submissions;
	
	//Parcelable Constructor 
	//code from techdroid.kbeanie.com/2010/06/parcelable-how-to-do-that-in-android.html 
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
	
	//Create task and set fields
	Task(String ownerId, String name, String desc, boolean wantText, boolean wantPhoto, boolean wantAudio, boolean isPublic){
		this.taskName = name;
		this.taskDescription = desc;
		this.setWantText(wantText);
		this.setWantPhoto(wantPhoto);
		this.setWantAudio(wantAudio);
		this.setIsPublic(isPublic);
		this.setIsOpen(true);
		this.userDeviceId = ownerId;
		this.submissions = new ArrayList<Fulfillment>();
	}
	
	//add a new fulfillment to the submissions list
	public void addSubmission(String ownerId, String text, ArrayList<ImageFile> images, ArrayList<AudioFile> audio){
		Fulfillment ful = new Fulfillment(ownerId, text,images, audio);
		this.submissions.add(ful);
	}
	
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
	
	public boolean getWantPhoto(){
		if(((flags >>> 7) & 1) == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public void setWantPhoto(boolean text){
		if (text){
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
	
	@Override
	public String toString() {  
		String scope;
		String text;
		String audio;
		String photo;
		if(this.getIsPublic()){
			scope = "public";
		}else{
			scope = "private";
		}
		if(this.getWantText()){
			text = "Text";
		}else{
			text = "";
		}
		if(this.getWantAudio()){
			audio = "Audio";
		}else{
			audio = "";
		}
		if(this.getWantPhoto()){
			photo = "Photo";
		}else{
			photo = "";
		}
		
		String task = String.format("Task Name: %s\nDescription: %s\nScope: %s \n" +
									 "Required: %s %s %s", this.taskName, this.taskDescription,
										scope, text, photo, audio);
        return task;
	}
	
	//Called by the service manager to get the string to be sent to the service
	public String saveToString()
	{
		//Format of (service given id) (service given id of owner) (type of object) (body string)
		String ret;
		body = String.format("%s %s %s %b %b %b %b %b", userDeviceId, taskName, taskDescription, getWantText(), getWantPhoto(), getWantAudio(), getIsPublic(), getIsOpen());
		
		ret = String.format("%s %s %s %s", id, id, "TASK", body);
		return ret;
	}
	
	//Called by the service manager when building the objects pulled from the server
	public static Task buildFromString(String data)
	{
		//parse the string and get the required data
		//We should get the body string from the saveToString method
		
		String deliminator = "[ ]+";
		String[] tokens = data.split(deliminator);
		
		String ownerId = tokens[0]; 
		String name = tokens[1];
		String desc = tokens[2];
		boolean wantText = Boolean.getBoolean(tokens[3]);
		boolean wantPhoto = Boolean.getBoolean(tokens[4]);
		boolean wantAudio = Boolean.getBoolean(tokens[5]);
		boolean isPublic = Boolean.getBoolean(tokens[6]);
		boolean isOpen = Boolean.getBoolean(tokens[7]);
		
		Task ret = new Task(ownerId, name, desc, wantText, wantPhoto, wantAudio, isPublic);
		ret.setIsOpen(isOpen);
		
		return ret;
	}
}
