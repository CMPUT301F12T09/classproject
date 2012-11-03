package com.example.tasktracker;

import java.io.*;
import java.util.*;

import android.content.Context;


public class DatabaseManager{
	
	
	private static final String FILE_NAME = "tasks.sav";
		
	private Context context;
	
	private static DatabaseManager instance = null;
	
	protected DatabaseManager(Context context){
		this.context = context; 
	}
	
	public static DatabaseManager getInstance(int type, Context context)
	{
		DatabaseManager ret = null;
		if( type == 1)//change if we need to accommodate multiple servers
		{
			if(instance == null)
			{
				instance = new DatabaseManager(context);
			}
			
			ret = instance;
		}
		
		return ret;
	}

	public void saveTasks(ArrayList<Task> tasks){
		 try {  
	            FileOutputStream fOut = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
	        	ObjectOutputStream oOut = new ObjectOutputStream(fOut);    
	            oOut.writeObject(tasks);
	            oOut.flush();
	            oOut.close();  
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	}
	

	@SuppressWarnings("unchecked")
	public ArrayList<Task> loadTasks(){
	    ArrayList<Task> loadedTasks = new ArrayList<Task>();
   	try {  
       	FileInputStream fIn = context.openFileInput(FILE_NAME);
           ObjectInputStream oIn = new ObjectInputStream(fIn);  
           loadedTasks = (ArrayList<Task>) oIn.readObject();  
           oIn.close(); 
       } 
        catch (IOException e) {  
           e.printStackTrace();  
       } catch (ClassNotFoundException e) {  
           e.printStackTrace();  
       }
       return loadedTasks;
	}
}
