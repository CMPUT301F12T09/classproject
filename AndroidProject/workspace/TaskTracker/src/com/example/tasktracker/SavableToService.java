package com.example.tasktracker;

import java.io.Serializable;


public class SavableToService implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	public SavableToService()
	{
	}
	
	public String saveToString()
	{
		return "";
	}
	
	public String id;
	public String belongsTo;
	public String type;
	public String body;
}
