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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class manages the creation and removal of sql tables for tasks,
 * fulfilments, photos, and audio. This helper is accessed by the 
 * DatabaseManager and handles the sql commands. 
 * 
 * @author glancop
 *
 */

public class DatabaseHelper extends SQLiteOpenHelper{
			
	private static final String DATABASE = "tasktraker.db";
	private static final int VERSION = 2;
	
	private static final String CREATE_TASKS = "CREATE TABLE tasks( task_id INTEGER PRIMARY KEY AUTOINCREMENT,"
							+ " service_id TEXT, service_type TEXT,"
							+ " task_name TEXT, task_description TEXT, want_text INTEGER,"
							+ " want_photo INTEGER, want_audio INTEGER, is_public INTEGER,"
							+ " is_open INTEGER, user_device_id TEXT,"
							+ " belongs_to_id TEXT, body TEXT, user_email TEXT);";
	
	private static final String CREATE_FULFILLMENTS = "CREATE TABLE fulfillments("
							+ " fulfill_id INTEGER PRIMARY KEY AUTOINCREMENT, "
							+ " service_id TEXT, service_type TEXT, user_device_id TEXT,"
							+ " text_response TEXT, date_added TEXT, parent_task INTEGER, belongs_to_id TEXT, body TEXT,"
							+ " FOREIGN KEY(parent_task) REFERENCES tasks(task_id));";
	
	private static final String CREATE_PHOTOS = "CREATE TABLE photos (photo_id INTEGER PRIMARY"
							+ " KEY AUTOINCREMENT, service_id TEXT, service_type TEXT,"
							+ " photo BLOB, parent_task INTEGER, parent_fulfill INTEGER, belongs_to_id TEXT, body TEXT,"
							+ " FOREIGN KEY(parent_task) REFERENCES tasks(task_id),"
							+ " FOREIGN KEY(parent_fulfill) REFERENCES fulfillments(fulfill_id));";
	
	private static final String CREATE_AUDIO = "CREATE TABLE audio (audio_id INTEGER PRIMARY"
							+ " KEY AUTOINCREMENT, service_id TEXT, service_type TEXT,"
							+ " audio BLOB, parent_task INTEGER, parent_fulfill INTEGER, belongs_to_id TEXT, body TEXT,"
							+ " FOREIGN KEY(parent_task) REFERENCES tasks(task_id),"
							+ " FOREIGN KEY(parent_fulfill) REFERENCES fulfillments(fulfill_id));";
	
	public DatabaseHelper(Context context){
		super(context, DATABASE, null, VERSION);
	}
	
	/**
     * When implemented, this method will initialise the sql tables
     * using the above create table statements.
     * @param db
     */
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(CREATE_TASKS);
		db.execSQL(CREATE_FULFILLMENTS);
		db.execSQL(CREATE_PHOTOS);
		db.execSQL(CREATE_AUDIO);
	}
	
	/**
     * When implemented, this method will remove the sql tables 
     * if they exist.
     * @param db
     * @param oldV
     * @param newV
     */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
		db.execSQL("DROP TABLE IF EXISTS tasks");
		db.execSQL("DROP TABLE IF EXISTS fulfillments");
		db.execSQL("DROP TABLE IF EXISTS photos");
		db.execSQL("DROP TABLE IF EXISTS audio");
		onCreate(db);
	}
}
