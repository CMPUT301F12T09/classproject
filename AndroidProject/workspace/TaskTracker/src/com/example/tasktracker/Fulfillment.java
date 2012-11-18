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

import java.io.*;
import java.security.acl.Owner;
import java.util.*;

/**
 * This class acts as an object blueprint for a submission of
 * requirements to the task author.
 * <ul compact>
 * <li>Photo</li>
 * <li>Text</li>
 * <li>Audio</li>
 * <li>UserdeviceId, a unique id for each android device 
 * designed to track who is the author of a task </ul>
 * This class is SaveableToService which means it can be encoded/decoded
 * via String to the webservice.  
 * @author zturchan
 *
 */


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
	public Fulfillment(String ownerId, String text, ArrayList<ImageFile> images, ArrayList<AudioFile> audio){
		this.userDeviceId = ownerId;
		this.textInput = text;
		this.dateAdded = new Date();
		this.imageFiles = images;
		this.audioFiles = audio;
	}
	
	public Fulfillment()
	{
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
	
	/**
	 * Encodes the submission as a string for storage on webservice
	 */
	@Override
	public String toString() { 
		String contents = "";
		int pictures = imageFiles.size();
		int sounds = audioFiles.size();
		if (this.textInput != null){
			contents += "Text";
		}
		if (pictures > 0){
			contents += String.format("%d Photo", pictures);
		}
		if (sounds > 0){
			contents += String.format("%d Audio", sounds);
		}
		
		String task = String.format("User Id: %s\nDate: %d %d %d\nContents: %s \n",
									  this.userDeviceId, this.dateAdded.getMonth(), this.dateAdded.getDate(), this.dateAdded.getYear(), contents);
        return task;
	}
	
	/**
	 * Saves the fulfillment as a string for future loading
	 */
	//Called by the service manager to get the string to be sent to the service
	public String saveToString()
	{
		//Format of (service given id) (service given id of owner) (type of object) (body string)
		String ret;
		body = String.format("%s %s %d %d %d %d %d %d %d", userDeviceId, textInput, dateAdded.getDate(), dateAdded.getHours(), dateAdded.getMinutes(), dateAdded.getMonth(), dateAdded.getSeconds(), dateAdded.getTime(), dateAdded.getYear());
		
		ret = String.format("%s %s %s %s", id, belongsTo, "FULFILLMENT", body);
		return ret;
	}
	/**
	 * Recreates a fulfillment from a string in storage
	 * @param data
	 * @return
	 */
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
		
		//System.out.printf("%s %s %s %s %s %s %s\n", tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7], tokens[8]);
		
		ret.dateAdded.setDate(Integer.decode(tokens[2]));
		ret.dateAdded.setHours(Integer.decode(tokens[3]));
		ret.dateAdded.setMinutes(Integer.decode(tokens[4]));
		ret.dateAdded.setMonth(Integer.decode(tokens[5]));
		ret.dateAdded.setSeconds(Integer.decode(tokens[6]));
		ret.dateAdded.setTime(Integer.decode(tokens[7]));
		//ret.dateAdded.setYear(Integer.decode(tokens[8]));
		
		return ret;
	}
}
