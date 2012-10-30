package com.example.tasktracker;

import java.io.File;
import java.util.Date;
import java.util.ArrayList;


public class Fulfillment{
	private String userDeviceId;
	private String textInput;
	
	private Date dateAdded;
	
	private ArrayList<File> imageFiles;
	private ArrayList<File> audioFiles;
	
	Fulfillment(String ownerId, String text, ArrayList<File> images, ArrayList<File> audio){
		this.userDeviceId = ownerId;
		this.textInput = text;
		this.dateAdded = new Date();
		this.imageFiles = images;
		this.audioFiles = audio;
	}
	
	public String getUserDeviceId(){
		return userDeviceId;
	}
	
	public void setUserDeviceId(String deviceId){
		this.userDeviceId = deviceId;
	}
	
	public String getTextInput(){
		return textInput;
	}
	
	public void setTextInput(String text){
		this.textInput = text;
	}
	
	public Date getDateAdded(){
		return dateAdded;
	}
	
	public void setDateAdded(Date newDate){
		this.dateAdded = newDate;
	}
	
	public ArrayList<File> getImageFiles(){
		return imageFiles;
	}
	
	public void setImageFiles(ArrayList<File> newImages){
		this.imageFiles = newImages;
	}
	
	public ArrayList<File> getAudioFiles(){
		return audioFiles;
	}
	
	public void setAudioFiles(ArrayList<File> newAudio){
		this.audioFiles = newAudio;
	}
}
