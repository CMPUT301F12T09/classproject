
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

package controllers;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import model.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * This class manages the database of tasks stored locally.  This 
 * manager will be updated from the webservice when the update data button 
 * is clicked on the mainscreen, as well as automatically when new tasks
 * are created.  There is a single instance of this class that can be accessed
 * from the rest of the classes and all interactions with locally stored
 * tasks must go through this manager.
 * @author zturchan
 *
 */
public class DatabaseManager{
			
	private static DatabaseManager instance = null;
	
	private SQLiteDatabase db;
	private DatabaseHelper myHelper;
	
	
	protected DatabaseManager(Context context){
		myHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		db = myHelper.getWritableDatabase();
	}
	
	public void close(){
		myHelper.close();
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

	public void newTask(Task task){
		ContentValues val = new ContentValues();
		val.put("task_name", task.getTaskName());
		val.put("task_description", task.getTaskDescription());
		val.put("want_text", task.getWantText());
		val.put("want_photo", task.getWantPhoto());
		val.put("want_audio", task.getWantAudio());
		val.put("is_public", task.getIsPublic());
		val.put("is_open", task.getIsOpen());
		val.put("user_device_id", task.getUserDeviceId());
		db.insert("tasks", null, val);
		ArrayList<Fulfillment> submissions = task.getSubmissions();
		for(Fulfillment f : submissions){
			
		}
	}
	
	public void addFulfillment(Task task, Fulfillment ful){
		long t_id = task.getDbId();
		ContentValues val = new ContentValues();
		val.put("user_device_id", ful.getUserDeviceId());
		val.put("text_response", ful.getTextInput());
		val.put("date_added", ful.getDateAdded().toString());
		val.put("parent_task", t_id);
		long f_id = db.insert("fulfillments", null, val);
		ArrayList<ImageFile> images = ful.getImageFiles();
		for(ImageFile i : images){
			ContentValues vali = new ContentValues();
			vali.put("parent_task", t_id);
			vali.put("parent_fulfill", f_id);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			i.bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
			byte[] toStore = bos.toByteArray();
			vali.put("photo", toStore);
			
		}
		/* add after audio is implemented
		ArrayList<AudioFile> audio = ful.getAudioFiles();
		for(AudioFile a : audio){
			ContentValues vala = new ContentValues();
			vala.put("parent_task", t_id);
			vala.put("parent_fulfill", f_id);
			//TODO convert audio file to blob
			vala.put("audio", toStore);
		}
		*/
	}
	
	public void removeFulfillment(Fulfillment ful){
		long f_id = ful.getDbId();
		db.delete("fulfillments", "fulfill_id = " + f_id, null);
		db.delete("photos", "parent_fulfill = " + f_id, null);
		db.delete("audio", "parent_fulfill = " + f_id, null);
	}
	
	public void updateTask(Task task){
		long t_id = task.getDbId();
		ContentValues val = new ContentValues();
		val.put("task_name", task.getTaskName());
		val.put("task_description", task.getTaskDescription());
		val.put("want_text", task.getWantText());
		val.put("want_photo", task.getWantPhoto());
		val.put("want_audio", task.getWantAudio());
		val.put("is_public", task.getIsPublic());
		val.put("is_open", task.getIsOpen());
		val.put("user_device_id", task.getUserDeviceId());
		db.update("tasks", val, "task_id = "+ t_id, null);
	}
	
	public void removeTask(Task task){
		long t_id = task.getDbId();
		db.delete("tasks", "task_id = " + t_id, null);
		db.delete("fulfillments", "parent_task = " + t_id, null);
		db.delete("photos", "parent_task = " + t_id, null);
		db.delete("audio", "parent_task = " + t_id, null);
	}
	
	
	public ArrayList<Task> loadTasks(){
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		Cursor cursorTask = db.query("tasks", null, null, null, null, null, null);
		
		cursorTask.moveToFirst();
		while(!cursorTask.isAfterLast()){
			Task task = rebuildTask(cursorTask);
			tasks.add(task);
			cursorTask.moveToNext();
		}
		cursorTask.close();
		
		return tasks;
	}
	
	private Task rebuildTask(Cursor c){
		Task task = new Task();
		long t_id = c.getLong(0);
		task.setDbId(t_id);
		task.setId(c.getString(1));
		task.setType(c.getString(2));
		task.setTaskName(c.getString(3));
		task.setTaskDescription(c.getString(4));
		if(c.getInt(5) == 1){
			task.setWantText(true);
		}else{
			task.setWantText(false);
		}
		if(c.getInt(6) == 1){
			task.setWantPhoto(true);
		}else{
			task.setWantPhoto(false);
		}
		if(c.getInt(7) == 1){
			task.setWantAudio(true);
		}else{
			task.setWantAudio(false);
		}
		if(c.getInt(8) == 1){
			task.setIsPublic(true);
		}else{
			task.setIsPublic(false);
		}
		if(c.getInt(9) == 1){
			task.setIsOpen(true);
		}else{
			task.setIsOpen(false);
		}
		task.setUserDeviceId(c.getString(10));
		
		
		Cursor cursorFulfill  = db.query("fulfillments", null, "parent_task = "+t_id, null, null, null, null);
		cursorFulfill.moveToFirst();
		while(!cursorFulfill.isAfterLast()){
			Fulfillment ful = rebuildFulfillment(cursorFulfill);
			task.addFulfillment(ful);
			cursorFulfill.moveToNext();
		}
		cursorFulfill.close();
		
		return task;
	}
	
	private Fulfillment rebuildFulfillment(Cursor c){
		Fulfillment ful = new Fulfillment();
		long f_id = c.getLong(0);
		ful.setDbId(f_id);
		ful.setId(c.getString(1));
		ful.setType(c.getString(2));
		ful.setUserDeviceId(c.getString(3));
		ful.setTextInput(c.getString(4));
		try{
			Date date = new SimpleDateFormat().parse(c.getString(5));
			ful.setDateAdded(date);
		}catch(Exception e){
			
		}
		ArrayList<ImageFile> newImages = new ArrayList<ImageFile>();
		Cursor cursorPhotos  = db.query("photos", null, "parent_fulfill = "+f_id, null, null, null, null);
		cursorPhotos.moveToFirst();
		while(!cursorPhotos.isAfterLast()){
			ImageFile image = rebuildImage(cursorPhotos);
			newImages.add(image);
			cursorPhotos.moveToNext();
		}
		cursorPhotos.close();
		ful.setImageFiles(newImages);
		
		ArrayList<AudioFile> newAudio= new ArrayList<AudioFile>();
		Cursor cursorAudio  = db.query("audio", null, "parent_fulfill = "+f_id, null, null, null, null);
		cursorAudio.moveToFirst();
		while(!cursorAudio.isAfterLast()){
			AudioFile audio = rebuildAudio(cursorAudio);
			newAudio.add(audio);
			cursorAudio.moveToNext();
		}
		cursorAudio.close();
		ful.setAudioFiles(newAudio);
		
		return ful;
	}
	
	private ImageFile rebuildImage(Cursor c){
		ImageFile image = new ImageFile();
		image.setId(c.getString(1));
		image.setType(c.getString(2));
		ByteArrayInputStream bis = new ByteArrayInputStream(c.getBlob(3));
		image.bitmap = BitmapFactory.decodeStream(bis);
		
		return image;
		
	}
	//TODO implement audio rebuild
	private AudioFile rebuildAudio(Cursor c){
		AudioFile audio = new AudioFile();
		audio.setId(c.getString(1));
		audio.setType(c.getString(2));
		
		return audio;
	}
}
