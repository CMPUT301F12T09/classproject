
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
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
/**
 * This class acts as a manager that interfaces with the webservice 
 * for remote storage and sharing of tasks.  It is accessed via a getInstance()
 * method and can save Tasks, Fulfillments, ImageFiles and AudioFiles to 
 * the webservice, as well as delete them.
 * 
 * This class also can update the taskmanager with new and updated
 * tasks from the webservice
 * 
 * @author zturchan
 *
 */
public class ServiceManager
{
	//We need to keep a pool for all objects as we don't know who they belong to until after we build them all.
	private ArrayList<Task> tasks;
	private ArrayList<Fulfillment> fulfillments;
	private ArrayList<ImageFile> images;
	private ArrayList<AudioFile> audios;
	
	private static ServiceManager instance = null;
	
	private HttpClient httpclient = new DefaultHttpClient();
	private Gson gson = new Gson();
	private HttpPost httpPost = new HttpPost("http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T09/");
	
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
				instance = new ServiceManager("http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T09/");
			}
			ret = instance;
		}
		
		return ret;
	}
	
	//====================================================================================================================================================
	// To be called when before deleting an object.
	// Will require a connection to the internet in order to properly delete.
	// Unless we come up with a storage system for pseudo deleted objects.
	//====================================================================================================================================================
	/**
	 * Save a task to the webservice
	 * @param toSend
	 */
	public void saveToService(Task toSend)
	{
		try
		{
			if(toSend.id == "")
			{
				List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("action", "post"));
				nameValuePairs.add(new BasicNameValuePair("belongsTo", toSend.belongsTo));
				nameValuePairs.add(new BasicNameValuePair("type", toSend.type));
				nameValuePairs.add(new BasicNameValuePair("body", toSend.saveToString()));
				
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httpPost);
			    
			    String status = response.getStatusLine().toString();
			    System.out.println(status);
			    
			    //Get the updated object with proper id set
			    
			    SavableToService tempNew = new SavableToService();
			    HttpEntity entity = response.getEntity();
			    
			    if (entity != null) {
			        InputStream is = entity.getContent();
			        String jsonStringVersion = convertStreamToString(is);
			        Type taskType = SavableToService.class;     
			        tempNew = gson.fromJson(jsonStringVersion, taskType);
			    }
			    entity.consumeContent();
			    
			    toSend.id = tempNew.id;
			    toSend.belongsTo = tempNew.id; //Tasks should belong to themselves, or nothing if that seems weird
			    
			    ArrayList<Fulfillment> tempFulfillments = toSend.getSubmissions();
			    for(int i = 0; i < tempFulfillments.size(); i++)
			    {
			    	tempFulfillments.get(i).belongsTo = toSend.id;
			    	saveToService(tempFulfillments.get(i));
			    }
			}
			else
			{
				List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("action", "update"));
				nameValuePairs.add(new BasicNameValuePair("id", toSend.id));
				nameValuePairs.add(new BasicNameValuePair("belongsTo", toSend.belongsTo));
				nameValuePairs.add(new BasicNameValuePair("type", toSend.type));
				nameValuePairs.add(new BasicNameValuePair("body", toSend.saveToString()));
				
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httpPost);
			    
			    String status = response.getStatusLine().toString();
			    System.out.println(status);
			    
			    //Get the updated object with proper id set
			    
			    SavableToService tempNew = new SavableToService();
			    HttpEntity entity = response.getEntity();
			    
			    if (entity != null) {
			        InputStream is = entity.getContent();
			        String jsonStringVersion = convertStreamToString(is);
			        Type taskType = SavableToService.class;     
			        tempNew = gson.fromJson(jsonStringVersion, taskType);
			    }
			    entity.consumeContent();
			    
				ArrayList<Fulfillment> tempFulfillments = toSend.getSubmissions();
			    for(int i = 0; i < tempFulfillments.size(); i++)
			    {
			    	tempFulfillments.get(i).belongsTo = toSend.id;
			    	saveToService(tempFulfillments.get(i));
			    }
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Save a fulfillment to the webservice
	 * @param toSend
	 */
	public void saveToService(Fulfillment toSend)
	{
		try
		{
			if(toSend.id == "")
			{
				List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("action", "post"));
				nameValuePairs.add(new BasicNameValuePair("belongsTo", toSend.belongsTo));
				nameValuePairs.add(new BasicNameValuePair("type", toSend.type));
				nameValuePairs.add(new BasicNameValuePair("body", toSend.saveToString()));
				
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httpPost);
			    
			    String status = response.getStatusLine().toString();
			    System.out.println(status);
			    
			    //Get the updated object with proper id set
			    
			    SavableToService tempNew = new SavableToService();
			    HttpEntity entity = response.getEntity();
			    
			    if (entity != null) {
			        InputStream is = entity.getContent();
			        String jsonStringVersion = convertStreamToString(is);
			        Type taskType = SavableToService.class;     
			        tempNew = gson.fromJson(jsonStringVersion, taskType);
			    }
			    entity.consumeContent();
			    
			    toSend.id = tempNew.id;
			    
			    ArrayList<AudioFile> tempAudio = toSend.getAudioFiles();
			    for(int i = 0; i < tempAudio.size(); i++)
			    {
			    	tempAudio.get(i).belongsTo = toSend.id;
			    	saveToService(tempAudio.get(i));
			    }
			    
			    ArrayList<ImageFile> tempImages = toSend.getImageFiles();
			    for(int i = 0; i < tempImages.size(); i++)
			    {
			    	tempImages.get(i).belongsTo = toSend.id;
			    	saveToService(tempImages.get(i));
			    }
			}
			else
			{
				//This already exists in the service and probably doesn't need to be updated
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Save an image to the service
	 * @param toSend
	 */
	public void saveToService(ImageFile toSend)
	{
		try
		{
			if(toSend.id == "")
			{
				List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("action", "post"));
				nameValuePairs.add(new BasicNameValuePair("belongsTo", toSend.belongsTo));
				nameValuePairs.add(new BasicNameValuePair("type", toSend.type));
				nameValuePairs.add(new BasicNameValuePair("body", toSend.saveToString()));
				
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httpPost);
			    
			    String status = response.getStatusLine().toString();
			    System.out.println(status);
			    
			    //Get the updated object with proper id set
			    
			    SavableToService tempNew = new SavableToService();
			    HttpEntity entity = response.getEntity();
			    
			    if (entity != null) {
			        InputStream is = entity.getContent();
			        String jsonStringVersion = convertStreamToString(is);
			        Type taskType = SavableToService.class;     
			        tempNew = gson.fromJson(jsonStringVersion, taskType);
			    }
			    entity.consumeContent();
			    
			    toSend.id = tempNew.id;
			}
			else
			{
				//This already exists in the service and probably doesn't need to be updated
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Save an audio file to the webservice
	 * @param toSend
	 */
	public void saveToService(AudioFile toSend)
	{
		try
		{
			if(toSend.id == "")
			{
				List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("action", "post"));
				nameValuePairs.add(new BasicNameValuePair("belongsTo", toSend.belongsTo));
				nameValuePairs.add(new BasicNameValuePair("type", toSend.type));
				nameValuePairs.add(new BasicNameValuePair("body", toSend.saveToString()));
				
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httpPost);
			    
			    String status = response.getStatusLine().toString();
			    System.out.println(status);
			    
			    //Get the updated object with proper id set
			    
			    SavableToService tempNew = new SavableToService();
			    HttpEntity entity = response.getEntity();
			    
			    if (entity != null) {
			        InputStream is = entity.getContent();
			        String jsonStringVersion = convertStreamToString(is);
			        Type taskType = SavableToService.class;     
			        tempNew = gson.fromJson(jsonStringVersion, taskType);
			    }
			    entity.consumeContent();
			    
			    toSend.id = tempNew.id;
			}
			else
			{
				//This already exists in the service and probably doesn't need to be updated
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//====================================================================================================================================================
	// To be called when before deleting an object.
	// Will require a connection to the internet in order to properly delete.
	// Unless we come up with a storage system for pseudo deleted objects.
	//====================================================================================================================================================
	/**
	 * remove a task from the webservice
	 * @param toRemove
	 */
	public void removeFromService(Task toRemove)
	{
		if(toRemove.id == "")
		{
			//throw an error and leave
			return;
		}
		
		try
		{	
			ArrayList<Fulfillment> tempFulfillments = toRemove.getSubmissions(); 
			for(int i = 0; i < tempFulfillments.size(); i++)
			{
				removeFromService(tempFulfillments.get(i));
			}
			
			List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("action", "remove"));
			nameValuePairs.add(new BasicNameValuePair("id", toRemove.id));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httpPost);
		    
		    String status = response.getStatusLine().toString();
		    System.out.println(status);
		    
		    HttpEntity entity = response.getEntity();
		    entity.consumeContent();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * remove a fulfillment from the service
	 * @param toRemove
	 */
	public void removeFromService(Fulfillment toRemove)
	{
		if(toRemove.id == "")
		{
			//throw an error and leave
			return;
		}
		
		try
		{
			ArrayList<AudioFile> tempAudio = toRemove.getAudioFiles(); 
			for(int i = 0; i < tempAudio.size(); i++)
			{
				removeFromService(tempAudio.get(i));
			}
			
			ArrayList<ImageFile> tempImages = toRemove.getImageFiles(); 
			for(int i = 0; i < tempImages.size(); i++)
			{
				removeFromService(tempImages.get(i));
			}
			
			List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("action", "remove"));
			nameValuePairs.add(new BasicNameValuePair("id", toRemove.id));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httpPost);
		    
		    String status = response.getStatusLine().toString();
		    System.out.println(status);
		    
		    HttpEntity entity = response.getEntity();
		    entity.consumeContent();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * remove an image from the webservice
	 * @param toRemove
	 */
	public void removeFromService(ImageFile toRemove)
	{
		if(toRemove.id == "")
		{
			//throw an error and leave
			return;
		}
		
		try
		{
			List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("action", "remove"));
			nameValuePairs.add(new BasicNameValuePair("id", toRemove.id));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httpPost);
		    
		    String status = response.getStatusLine().toString();
		    System.out.println(status);
		    
		    HttpEntity entity = response.getEntity();
		    entity.consumeContent();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * remove an audio file from the service
	 * @param toRemove
	 */
	public void removeFromService(AudioFile toRemove)
	{
		if(toRemove.id == "")
		{
			//throw an error and leave
			return;
		}
		
		try
		{
			List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("action", "remove"));
			nameValuePairs.add(new BasicNameValuePair("id", toRemove.id));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httpPost);
		    
		    String status = response.getStatusLine().toString();
		    System.out.println(status);
		    
		    HttpEntity entity = response.getEntity();
		    entity.consumeContent();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Update the taskmanager with new data from the webservice
	 * @param requester
	 */
	public void requestUpdate(TaskManager requester)
	{
		//Save all current data out to the service
		ArrayList<Task> currentTasks = requester.getTaskList();
		for(int i = 0; i < currentTasks.size(); i++)
		{
			saveToService(currentTasks.get(i));
		}
		//gets all data from webservice and sends tokens to be decoded into tasks, fulfillments, images and audio
		try
		{
		        String jsonStringVersion = new String();
		        List <BasicNameValuePair> nvps = new ArrayList <BasicNameValuePair>();
		        nvps.add(new BasicNameValuePair("action", "list"));
		
		        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		        HttpResponse response = httpclient.execute(httpPost);
		
		    String status = response.getStatusLine().toString();
		    HttpEntity entity = response.getEntity();

		    System.out.println(status);
		    
		    if (entity != null) {
		        InputStream is = entity.getContent();
		        jsonStringVersion = convertStreamToString(is);
		    }
		    
		    StringTokenizer st = new StringTokenizer(jsonStringVersion);
		    while(st.hasMoreTokens()){
		        if (st.nextToken() == "TASK"){
		            tasks.add(Task.buildFromString(st.nextToken()));
		        }
		        else if (st.nextToken() == "FULFILLMENT"){
		            fulfillments.add(Fulfillment.buildFromString(st.nextToken()));
		        }
		        else if (st.nextToken() == "AUDIO"){
		            audios.add(AudioFile.buildFromString(st.nextToken()));
		        }
		        else if (st.nextToken() == "IMAGE"){
		            images.add(ImageFile.buildFromString(st.nextToken()));
		        }
		        st.nextToken();
		    }
		    
		    entity.consumeContent();
		}
		catch (Exception e){
		        e.printStackTrace();
		}
		//Add all image files to the fulfillments that own them
		for(int i = 0; i < images.size(); i++)
		{
			ImageFile tempI = images.get(i);
			for(int j = 0; j < fulfillments.size(); j++)
			{
				Fulfillment tempF = fulfillments.get(j);
				if(tempF.id == tempI.belongsTo)
				{
					tempF.addImage(tempI);
					break;
				}
			}
		}
		
		//Add all audio files to the fulfillments that own them
		for(int i = 0; i < audios.size(); i++)
		{
			AudioFile tempA = audios.get(i);
			for(int j = 0; j < fulfillments.size(); j++)
			{
				Fulfillment tempF = fulfillments.get(j);
				if(tempF.id == tempA.belongsTo)
				{
					tempF.addAudio(tempA);
					break;
				}
			}
		}
		
		//Add all fulfillments to the tasks that own them
		for(int i = 0; i < fulfillments.size(); i++)
		{
			Fulfillment tempF = fulfillments.get(i);
			for(int j = 0; j < tasks.size(); j++)
			{
				Task tempT = tasks.get(j);
				if(tempT.id == tempF.belongsTo)
				{
					tempT.addFulfillment(tempF);
					break;
				}
			}
		}
		
		//Clear out the task list of the requester and populate it with the new tasks
		currentTasks.clear();
		for(int i = 0; i < tasks.size(); i++)
		{
			requester.addTask(tasks.get(i));
		}
	}
	
	private  String convertStreamToString(InputStream is) 
	{		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				is.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
