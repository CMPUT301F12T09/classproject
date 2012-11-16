
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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.provider.Settings.Secure;

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
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private ArrayList<Fulfillment> fulfillments = new ArrayList<Fulfillment>();
	private ArrayList<ImageFile> images = new ArrayList<ImageFile>();
	private ArrayList<AudioFile> audios = new ArrayList<AudioFile>();
	
	private static ServiceManager instance = null;
	
	private HttpClient httpclient = new DefaultHttpClient();
	private Gson gson = new Gson();
	private HttpPost httpPost = new HttpPost("http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T09/");
	
	private String userId; 
	
	protected ServiceManager(String path)
	{
	}
	
	/**
	 * Request an instance of the service manager singleton of the specified type: 1 for the current service
	 * @param type
	 */
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
	
	/**
	 * Used to send out a newly created object. Should not be used to update existing objects.
	 * Must include the type ("TASK" "FULLFILMENT" "IMAGE" "AUDIO")
	 * @param toSend
	 * @param type
	 */
	public void requestSaveOut(final SavableToService toSend, final String type)
	{	
		new AsyncTask<Void, Void, Void>()
		{
    		@Override
		    protected Void doInBackground(Void... params)
		    {
    			System.out.println("Sending new");
    			toSend.saveToString();
				try
		    	{
					List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("action", "post"));
					nameValuePairs.add(new BasicNameValuePair("description", type));
					nameValuePairs.add(new BasicNameValuePair("summary", type));
					nameValuePairs.add(new BasicNameValuePair("content", gson.toJson(toSend)));
					
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
				    
				    System.out.println("ADDING ID " + tempNew.id);
		    	}
		    	catch(ClientProtocolException e)
				{
					System.out.println("ERROR-Protocol");
					System.out.println(e);
				}
				catch(IOException e)
				{
					System.out.println("ERROR-IO");
					System.out.println(e);
				}
				catch(Exception e)
				{
					System.out.println("ERROR-General");
					System.out.println(e);
				}
    			
    			return null;
		    }
    		
    		 @Override
 		    protected void onPostExecute(Void result)
    		{
    		}
 		}.execute();
	}
	
	/**
	 * Save a task to the webservice
	 * Will also savout out all of it's attached fulfillments
	 * If the task already has a service id it will update instead of add
	 * Does not save out the task if you are not the author
	 * Still saves out the fulfillments if you are not the author
	 * @param toSend
	 */
	public void saveToService(final Task toSend)
	{
		toSend.saveToString();
		try
		{
			if(toSend.id==null)
			{		 
				System.out.println("Sending new");
				try
		    	{
					//It's fine to send out private tasks if you are the author, but don't send any task if you aren't
					if(toSend.getUserDeviceId() == userId)
					{
						List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("action", "post"));
						nameValuePairs.add(new BasicNameValuePair("description", "TASK"));
						nameValuePairs.add(new BasicNameValuePair("summary", "TASK"));
						nameValuePairs.add(new BasicNameValuePair("content", gson.toJson(toSend)));
						
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
					    
					    System.out.println("ADDING ID " + tempNew.id);
					}
					
				    ArrayList<Fulfillment> tempFulfillments = toSend.getSubmissions();
				    for(int i = 0; i < tempFulfillments.size(); i++)
				    {
						System.out.println(i);
				    	tempFulfillments.get(i).belongsTo = toSend.id;
				    	saveToService(tempFulfillments.get(i));
				    }
		    	}
		    	catch(ClientProtocolException e)
				{
					System.out.println("ERROR-Protocol");
					System.out.println(e);
				}
				catch(IOException e)
				{
					System.out.println("ERROR-IO");
					System.out.println(e);
				}
				catch(Exception e)
				{
					System.out.println("ERROR-General");
					System.out.println(e);
				}
			}
			else
			{		
				System.out.println("Attempting to update");
				try
		    	{							
					List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("action", "update"));
					nameValuePairs.add(new BasicNameValuePair("id", toSend.id));
					nameValuePairs.add(new BasicNameValuePair("description", "TASK"));
					nameValuePairs.add(new BasicNameValuePair("summary", "TASK"));
					nameValuePairs.add(new BasicNameValuePair("content", gson.toJson(toSend)));
					
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
				    else
				    {
				    	System.out.println("Update not found");
				    }
				    entity.consumeContent();
				    
					ArrayList<Fulfillment> tempFulfillments = toSend.getSubmissions();
				    for(int i = 0; i < tempFulfillments.size(); i++)
				    {
				    	tempFulfillments.get(i).belongsTo = toSend.id;
				    	saveToService(tempFulfillments.get(i));
				    }
		    	}
		    	catch(ClientProtocolException e)
				{
					System.out.println("ERROR-Protocol");
					System.out.println(e);
				}
				catch(IOException e)
				{
					System.out.println("ERROR-IO");
					System.out.println(e);
				}
				catch(Exception e)
				{
					System.out.println("ERROR-General");
					System.out.println(e);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("ERROR-General");
			e.printStackTrace();
		}
	}
	
	/**
	 * Save a fulfillment to the webservice
	 * It will also save out its attached images and audio
	 * Does nothing if the fulfillment already has a service id 
	 * @param toSend
	 */
	public void saveToService(final Fulfillment toSend)
	{
		toSend.saveToString();

		if(toSend.id.equals(null))
		{		    
			try
	    	{				
				List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("action", "post"));
				nameValuePairs.add(new BasicNameValuePair("description", "FULFILLMENT"));
				nameValuePairs.add(new BasicNameValuePair("summary", "FULFILLMENT"));
				nameValuePairs.add(new BasicNameValuePair("content", gson.toJson(toSend)));
				
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
	    	catch(ClientProtocolException e)
			{
				System.out.println("ERROR-Protocol");
				System.out.println(e);
			}
			catch(IOException e)
			{
				System.out.println("ERROR-IO");
				System.out.println(e);
			}
			catch(Exception e)
			{
				System.out.println("ERROR-General");
				System.out.println(e);
			}
		}
		else
		{		
			//We don't need to update fulfillments yet
		}
	}
	
	/**
	 * Save an image to the service
	 * Does nothing if it already has a service id
	 * @param toSend
	 */
	public void saveToService(final ImageFile toSend)
	{
		toSend.saveToString();

		if(toSend.id==null)
		{		    
			try
	    	{
				List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("action", "post"));
				nameValuePairs.add(new BasicNameValuePair("description", "IMAGE"));
				nameValuePairs.add(new BasicNameValuePair("summary", "IMAGE"));
				nameValuePairs.add(new BasicNameValuePair("content", gson.toJson(toSend)));
				
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
	    	catch(ClientProtocolException e)
			{
				System.out.println("ERROR-Protocol");
				System.out.println(e);
			}
			catch(IOException e)
			{
				System.out.println("ERROR-IO");
				System.out.println(e);
			}
			catch(Exception e)
			{
				System.out.println("ERROR-General");
				System.out.println(e);
			}
		}
		else
		{		
			//We don't need to update fulfillments yet
		}
	}
	
	/**
	 * Save an audio file to the webservice
	 * Does nothing if it already has a service id
	 * @param toSend
	 */
	public void saveToService(AudioFile toSend)
	{
		try
		{
			if(toSend.id==null)
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
	 * remove a task from the webservice
	 * must have a service id
	 * will also remove the attached fulfillments
	 * @param toRemove
	 */
	public void removeFromService(Task toRemove)
	{
		if(toRemove.id==null)
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
	 * must have a service id
	 * will also remove the attached images and audio
	 * @param toRemove
	 */
	public void removeFromService(Fulfillment toRemove)
	{
		if(toRemove.id==null)
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
	 * must have a service id
	 * @param toRemove
	 */
	public void removeFromService(ImageFile toRemove)
	{
		if(toRemove.id==null)
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
	 * must have a service id
	 * @param toRemove
	 */
	public void removeFromService(AudioFile toRemove)
	{
		if(toRemove.id==null)
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
	 * Get a specified task from the service, construct it and return it
	 * @param id
	 */
	public Task retreiveTaskFromService(String id)
	{	
		Task responseTask = null;
		try
		{
			responseTask = new Task();
			List <BasicNameValuePair> nvps = new ArrayList <BasicNameValuePair>();
			nvps.add(new BasicNameValuePair("action", "get"));
			nvps.add(new BasicNameValuePair("id", id));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
		    
		    String status = response.getStatusLine().toString();
		    HttpEntity entity = response.getEntity();
		    
		    System.out.println(status);
		    String jsonStringVersion = "";
		    
		    if (entity != null) {
		        InputStream is = entity.getContent();
		        jsonStringVersion = convertStreamToString(is);
		        Type taskType = Task.class;     
		        responseTask = gson.fromJson(jsonStringVersion, taskType);
		    }
		    entity.consumeContent();
		    
		    String temp = jsonStringVersion.substring(1, jsonStringVersion.length()-2);
		    temp = temp.substring(temp.indexOf("{")+1, temp.indexOf("}"));
		    String delims = "[:,]+";
		    String[] tokens = temp.split(delims);
		    
		    for(int i = 0; i < tokens.length; i++)
		    {	    	
		    	if(i == 1)
		    	{
		    		String data = tokens[i].substring(1, tokens[i].length()-1);
		    		responseTask = Task.buildFromString(data);
		    	}
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return responseTask;
	}
	
	/**
	 * Get a specified task from the service, construct it and return it
	 * @param id
	 */
	public Fulfillment retreiveFulfillmentFromService(String id)
	{	
		Fulfillment responseTask = null;
		try
		{
			responseTask = new Fulfillment();
			List <BasicNameValuePair> nvps = new ArrayList <BasicNameValuePair>();
			nvps.add(new BasicNameValuePair("action", "get"));
			nvps.add(new BasicNameValuePair("id", id));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
		    
		    String status = response.getStatusLine().toString();
		    HttpEntity entity = response.getEntity();
		    
		    System.out.println(status);
		    
		    String jsonStringVersion = "";
		    
		    if (entity != null) {
		        InputStream is = entity.getContent();
		        jsonStringVersion = convertStreamToString(is);
		        Type taskType = Fulfillment.class;     
		        responseTask = gson.fromJson(jsonStringVersion, taskType);
		    }
		    entity.consumeContent();
		    
		    String temp = jsonStringVersion.substring(1, jsonStringVersion.length()-2);
		    temp = temp.substring(temp.indexOf("{")+1, temp.indexOf("}"));
		    String delims = "[:,]+";
		    String[] tokens = temp.split(delims);
		    
		    for(int i = 0; i < tokens.length; i++)
		    {	    	
		    	if(i == 1)
		    	{
		    		String data = tokens[i].substring(1, tokens[i].length()-1);
		    		responseTask = Fulfillment.buildFromString(data);
		    	}
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return responseTask;
	}
	
	/**
	 * Get a specified image file from the service, construct it and return it
	 * @param id
	 */
	public ImageFile retreiveImageFromService(String id)
	{	
		ImageFile responseTask = null;
		try
		{
			responseTask = new ImageFile();
			List <BasicNameValuePair> nvps = new ArrayList <BasicNameValuePair>();
			nvps.add(new BasicNameValuePair("action", "get"));
			nvps.add(new BasicNameValuePair("id", id));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
		    
		    String status = response.getStatusLine().toString();
		    HttpEntity entity = response.getEntity();
		    
		    System.out.println(status);
		    
		    String jsonStringVersion = "";
		    if (entity != null) {
		        InputStream is = entity.getContent();
		        jsonStringVersion = convertStreamToString(is);
		        Type taskType = ImageFile.class;     
		        responseTask = gson.fromJson(jsonStringVersion, taskType);
		    }
		    entity.consumeContent();
		    
		    String temp = jsonStringVersion.substring(1, jsonStringVersion.length()-2);
		    temp = temp.substring(temp.indexOf("{")+1, temp.indexOf("}"));
		    String delims = "[:,]+";
		    String[] tokens = temp.split(delims);
		    
		    for(int i = 0; i < tokens.length; i++)
		    {	    	
		    	if(i == 1)
		    	{
		    		String data = tokens[i].substring(1, tokens[i].length()-1);
		    		responseTask = ImageFile.buildFromString(data);
		    	}
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return responseTask;
	}
	
	/**
	 * Get a specified audio file from the service, construct it and return it
	 * @param id
	 */
	public AudioFile retreiveAudioFromService(String id)
	{	
		AudioFile responseTask = null;
		try
		{
			responseTask = new AudioFile();
			List <BasicNameValuePair> nvps = new ArrayList <BasicNameValuePair>();
			nvps.add(new BasicNameValuePair("action", "get"));
			nvps.add(new BasicNameValuePair("id", id));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
		    
		    String status = response.getStatusLine().toString();
		    HttpEntity entity = response.getEntity();
		    
		    System.out.println(status);
		    
		    String jsonStringVersion = "";
		    if (entity != null) {
		        InputStream is = entity.getContent();
		        jsonStringVersion = convertStreamToString(is);
		        Type taskType = AudioFile.class;     
		        responseTask = gson.fromJson(jsonStringVersion, taskType);
		    }
		    entity.consumeContent();
		    
		    String temp = jsonStringVersion.substring(1, jsonStringVersion.length()-2);
		    temp = temp.substring(temp.indexOf("{")+1, temp.indexOf("}"));
		    String delims = "[:,]+";
		    String[] tokens = temp.split(delims);
		    
		    for(int i = 0; i < tokens.length; i++)
		    {	    	
		    	if(i == 1)
		    	{
		    		String data = tokens[i].substring(1, tokens[i].length()-1);
		    		responseTask = AudioFile.buildFromString(data);
		    	}
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return responseTask;
	}
	
	/**
	 * Clears out the storage arrays used to construct the ownership relations
	 */
	private void clearTempStorage()
	{
		tasks.clear();
		fulfillments.clear();
		audios.clear();
		images.clear();
	}
	
	/**
	 * Save out all of the data inside the give task manger
	 * @param requester
	 */
	private void saveOutData(final TaskManager requester)
	{
		ArrayList<Task> currentTasks = requester.getTaskList();
		System.out.println(currentTasks.size());
		for(int i = 0; i < currentTasks.size(); i++)
		{
			saveToService(currentTasks.get(i));
		}
	}
	
	/**
	 * Pull the contents of the service in string form and return it
	 */
	private String getServiceContents()
	{
		String jsonStringVersion = new String();
		
		try
		{
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
		    
		    entity.consumeContent();
		}
		catch (Exception e){
	        System.out.println("Error pulling data");
	        e.printStackTrace();
		}
		
		return jsonStringVersion;
	}
	
	/**
	 * Parse the given string for different objects and build them
	 * @param jsonStringVersion
	 */
	private void decodeObjects(String jsonStringVersion)
	{
		String temp = jsonStringVersion.substring(1, jsonStringVersion.length()-2);
	    String delims = "[{}]+";
	    String[] tokens = temp.split(delims);
	    
	    for(int i = 0; i < tokens.length; i++)
	    {
	    	String type = "";
	    	String id = "";
	    	
	    	if(tokens[i].equals(","))
	    	{
	    		continue;
	    	}
	    	
	    	String delims2 = "[:,]+";
		    String[] tokens2 = tokens[i].split(delims2);
		    
		    for(int j = 0; j < tokens2.length; j++)
		    {		    		    	
		    	if(j == 1)
		    	{
		    		type = tokens2[j].substring(1, tokens2[j].length()-1);
		    	}
		    	else if(j == 3)
		    	{
		    		id = tokens2[j].substring(1, tokens2[j].length()-1);
		    	}
		    }
		    
		    if(type.equals("TASK"))
		    {
		    	Task tempTask = retreiveTaskFromService(id);
		    	tempTask.id = id;
		    	tasks.add(tempTask);
		    }
		    else if(type.equals("FULFILLMENT"))
		    {
		    	Fulfillment tempFulfillment = retreiveFulfillmentFromService(id);
		    	tempFulfillment.id = id;
		    	fulfillments.add(tempFulfillment);
		    }
		    else if(type.equals("AUDIO"))
		    {
		    	AudioFile tempAudio = retreiveAudioFromService(id);
		    	tempAudio.id = id;
		    	audios.add(tempAudio);
		    }
		    else if(type.equals("IMAGE"))
		    {
		    	ImageFile tempImage = retreiveImageFromService(id);
		    	tempImage.id = id;
		    	images.add(tempImage);
		    }
	    }
	}
	
	/**
	 * Go through the storage arrays and attach the lower level objects to the higher level objects that match their ownedBy id
	 */
	private void createOwnershipStructure()
	{
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
	}
	
	/**
	 * Save out the data from the task manager
	 * Pull in the data from the service
	 * Build all of the objects in the service
	 * Construct the relationships
	 * Clear the task managers data
	 * Give the task manager the new list
	 * @param requester
	 */
	public void requestUpdate(final TaskManager requester)
	{
		userId = requester.getUserId();
		clearTempStorage();
		
		String status;
		HttpResponse response;

		try
		{    		
    		//Save all current data out to the service
    		System.out.println("StartingSave");
    		saveOutData(requester);
    		System.out.println("FinishingSave");
    		
    		//gets all data from webservice and sends tokens to be decoded into tasks, fulfillments, images and audio
			String jsonStringVersion = getServiceContents();
		    decodeObjects(jsonStringVersion);
		    
		    //joins the objects to their owners
		    createOwnershipStructure();
    		
    		//Clear out the task list of the requester and populate it with the new tasks
    		requester.clearTasks();
    		for(int i = 0; i < tasks.size(); i++)
    		{
    			requester.addTask(tasks.get(i));
    		}
    	}
		catch(Exception e)
		{
			System.out.println("ERROR-General");
			System.out.println(e);
		}
	}
	
	/**
	 * Take a stream of data and construct a readable string from it
	 * @param is
	 */
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
