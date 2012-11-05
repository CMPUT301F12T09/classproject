package com.example.tasktracker;

import java.io.*;
import java.util.*;

import android.content.Context;

/**
 * This class manages the database of tasks stored locally.  This 
 * manager will be updated from the webservice when the update data button 
 * is clicked on the mainscreen, as well as automatically when new tasks
 * are created.  There is a single instance of this class that can be accessed
 * from the rest of the classes and all interactions with lcoally stored
 * tasks must go through this manager.
 * @author zturchan
 *
 */
public class DatabaseManager{
	
	
	private static final String FILE_NAME = "tasks.sav";
		
	private Context context;
	
	private static DatabaseManager instance = null;
	
	protected DatabaseManager(Context context){
		this.context = context; 
	}
	/**
	 * Returns to the user the instance of the database manager for use
	 * Can add additional service types if we so wish in the future.
	 * @param type
	 * @param context
	 * @return
	 */
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
	/**
	 * This method will write the list of tasks visible to the user to local
	 * storage for later access.
	 * @param tasks
	 */
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
	
	/**
	 * This method will load the list of tasks that have been previously
	 * stored locally.
	 * @return
	 */
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
