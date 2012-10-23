package com.example.tasktracker;

import java.util.ArrayList;


public class TaskManager
{
	private ArrayList<Task> TaskList;
	
	private static TaskManager instance = null;
	
	protected TaskManager()
	{
	}
	
	public static TaskManager getInstance(int type)
	{
		TaskManager ret = null;
		if( type == 1)//change if we need to acomodate multiple servers
		{
			if(instance == null)
			{
				instance = new TaskManager();
			}
			
			ret = instance;
		}
		
		return ret;
	}
	
	public void updateName()
	{
	}
	
	public void updateDesc()
	{
	}
	
	public void updateRequirements()
	{
	}
	
	public void updateOpen()
	{
	}
	
	public void updatePublic()
	{
	}
	
	public void cancel() // ?
	{
	}
	
	public void createTask()
	{
	}
	
	public void deleteTask()
	{
	}
}
