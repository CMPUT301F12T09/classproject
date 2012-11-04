package com.example.tasktracker;

import java.io.*;
import java.security.acl.Owner;
import java.util.*;


public class Fulfillment extends SavableToService implements Serializable{
	
	//default serial for serializable object
	private static final long serialVersionUID = 1L;
		
	//Device Id of user who created fulfillment
	private String userDeviceId;
	
	//Text portion of Fulfillment
	private String textInput;
	
	//Date the fulfillment was created
	private Date dateAdded;
	
	//Lists of images and audio files attached to the submission
	private ArrayList<ImageFile> imageFiles;
	private ArrayList<AudioFile> audioFiles;
	
	//Create new Fulfillment and set fields
	Fulfillment(String ownerId, String text, ArrayList<ImageFile> images, ArrayList<AudioFile> audio){
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
	
	public ArrayList<ImageFile> getImageFiles(){
		return imageFiles;
	}
	
	public void setImageFiles(ArrayList<ImageFile> newImages){
		this.imageFiles = newImages;
	}
	
	public ArrayList<AudioFile> getAudioFiles(){
		return audioFiles;
	}
	
	public void setAudioFiles(ArrayList<AudioFile> newAudio){
		this.audioFiles = newAudio;
	}
	
	public void addImage(ImageFile toAdd)
	{
		imageFiles.add(toAdd);
	}
	
	public void addAudio(AudioFile toAdd)
	{
		audioFiles.add(toAdd);
	}
	
	//Called by the service manager to get the string to be sent to the service
	public String saveToString()
	{
		//Format of (service given id) (service given id of owner) (type of object) (body string)
		String ret;
		body = String.format("%s %s %d %d %d %d %d %d %d", userDeviceId, textInput, dateAdded.getDate(), dateAdded.getHours(), dateAdded.getMinutes(), dateAdded.getMonth(), dateAdded.getSeconds(), dateAdded.getTime(), dateAdded.getYear());
		
		ret = String.format("%s %s %s %s", id, belongsTo, "FULFILLMENT", body);
		return ret;
	}
	
	//Called by the service manager when building the objects pulled from the server
	public static Fulfillment buildFromString(String data)
	{
		//parse the string and get the required data
		//We should get the body string from the saveToString method
		
		String deliminator = "[ ]+";
		String[] tokens = data.split(deliminator);
		
		String ownerId = tokens[0]; 
		String text = tokens[1];
		
		//audio and image files will be added by the service manager later
		Fulfillment ret = new Fulfillment(ownerId, text, null, null);
		ret.dateAdded.setDate(Integer.decode(tokens[2]));
		ret.dateAdded.setHours(Integer.decode(tokens[3]));
		ret.dateAdded.setMinutes(Integer.decode(tokens[4]));
		ret.dateAdded.setMonth(Integer.decode(tokens[5]));
		ret.dateAdded.setSeconds(Integer.decode(tokens[6]));
		ret.dateAdded.setTime(Integer.decode(tokens[7]));
		ret.dateAdded.setYear(Integer.decode(tokens[8]));
		
		return ret;
	}
}
