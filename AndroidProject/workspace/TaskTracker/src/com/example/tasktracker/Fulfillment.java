package com.example.tasktracker;

import java.io.*;
import java.util.*;


public class Fulfillment implements Serializable{
	
	//default serial for serializable object
	private static final long serialVersionUID = 1L;
		
	//Device Id of user who created fulfillment
	private String userDeviceId;
	
	//Text portion of Fulfillment
	private String textInput;
	
	//Date the fulfillment was created
	private Date dateAdded;
	
	//Lists of images and audio files attached to the submission
	private ArrayList<File> imageFiles;
	private ArrayList<File> audioFiles;
	
	//Create new Fulfillment and set fields
	Fulfillment(String ownerId, String text, ArrayList<File> images, ArrayList<File> audio){
		this.userDeviceId = ownerId;
		this.textInput = text;
		this.dateAdded = new Date();
		this.imageFiles = images;
		this.audioFiles = audio;
	}
	
	//remaining methods are simple getters and setters for the data
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
