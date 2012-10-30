package com.example.tasktracker;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;

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
