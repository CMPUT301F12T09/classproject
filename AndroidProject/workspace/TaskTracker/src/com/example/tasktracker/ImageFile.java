package com.example.tasktracker;

import java.io.File;


public class ImageFile extends SavableToService
{
	public File image;
	
	//Called by the service manager to get the string to be sent to the service
	public String saveToString()
	{
		//Format of (service given id) (service given id of owner) (type of object) (body string)
		String ret;
		//body = String.format("", image);
		
		ret = String.format("%s %s %s %s", id, belongsTo, "FULFILLMENT", body);
		return ret;
	}
	
	//Called by the service manager when building the objects pulled from the server
	public static ImageFile buildFromString(String data)
	{
		//parse the string and get the required data
		//We should get the body string from the saveToString method
		
		/*
		String deliminator = "[ ]+";
		String[] tokens = data.split(deliminator);
		
		String ownerId = tokens[0]; 
		String text = tokens[1];
		*/
		
		ImageFile ret = new ImageFile();
		
		return ret;
	}
}
