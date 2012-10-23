package com.example.tasktracker;



public class ServiceManager
{
	private static ServiceManager instance = null;
	private static String URLPath = "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T09";
	
	protected ServiceManager(String path)
	{
	}
	
	public static ServiceManager getInstance(int type)
	{
		ServiceManager ret = null;
		
		if( type == 1)//change if we need to acomodate multiple services
		{
			if(instance == null)
			{
				instance = new ServiceManager(URLPath);
			}
			ret = instance;
		}
		
		return ret;
	}
	
	public void requestUpdate(TaskManager requester)
	{
		//connect to service
		//read in database
		//parse database
		//create tasks/fulfillments
		//link appropriately
		//add tasks to requester
	}
}
