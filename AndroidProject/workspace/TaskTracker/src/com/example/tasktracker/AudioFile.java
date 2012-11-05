package com.example.tasktracker;

import java.io.File;


public class AudioFile extends SavableToService
{
	public File audio;
	
	//Called by the service manager to get the string to be sent to the service
	public String saveToString()
	{
		//Format of (service given id) (service given id of owner) (type of object) (body string)
		String ret;
		//body = String.format("", audio);
		
		ret = String.format("%s %s %s %s", id, belongsTo, "AUDIO", body);
		return ret;
	}
	
	//Called by the service manager when building the objects pulled from the server
	public static AudioFile buildFromString(String data)
	{
		//parse the string and get the required data
		//We should get the body string from the saveToString method
		
		/*
		String deliminator = "[ ]+";
		String[] tokens = data.split(deliminator);
		
		String ownerId = tokens[0]; 
		String text = tokens[1];
		*/
		
		AudioFile ret = new AudioFile();
		
		return ret;
	}
}
