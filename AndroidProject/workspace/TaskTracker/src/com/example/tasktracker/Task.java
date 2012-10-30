package com.example.tasktracker;

import java.io.File;
import java.util.ArrayList;




public class Task
{
	private String taskName;
	private String taskDescription;
	
	private byte flags; //stores booleans in the form 0b(photo)(text)(audio)(open)(public)000
	
	private String userDeviceId;
	
	private ArrayList<Fulfillment> submissions;
	
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
	
	
	public void addSubmission(String ownerId, String text, ArrayList<File> images, ArrayList<File> audio){
		Fulfillment ful = new Fulfillment(ownerId, text,images, audio);
		this.submissions.add(ful);
	}
	
	public void removeSubmission(int index){
		this.submissions.remove(index);
	}
}
