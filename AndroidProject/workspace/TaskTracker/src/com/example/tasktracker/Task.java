package com.example.tasktracker;

import java.util.ArrayList;


public class Task
{
	private String taskName;
	private String taskDescription;
	private String userName;
	private String userID;
	
	private Byte flags; //stores booleans in the form 0b(photo)(text)(audio)(open)(public)000
	
	private int Identification;
	
	private ArrayList<Fulfillment> submissions;
	
	Task()
	{
	}
}
