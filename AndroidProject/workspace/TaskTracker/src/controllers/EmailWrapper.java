/*
 *  Copyright 2012: Alexander Y. Kleymenov, Zak Turchansky, Seth Davis, Evan Fauser, Gordon Lancop
 * 
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


/**
 * This class is a wrapper for a task to send email asynchronously. 
 * This class is needed because we can't do network activity on the main GUI thread
 * Original code from http://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a
 */

package controllers;
import model.Task;
import android.os.AsyncTask;


public class EmailWrapper extends AsyncTask<Task, Void, Task> {
	
	 private final String emailFrom = "taskmanagernotifier@gmail.com";
	 private final String password = "cmput301";
	 /**
	  *Create a new gmail sender to send from our gmail account.  
	  *Will send an email whenever a task has been fulfilled to whatever address the user specified. 
	  */
     protected Task doInBackground(Task...tasks) {
    	 GmailSender sender = new GmailSender(emailFrom, password);
         try {
			sender.sendMail("Your Task: "+tasks[0].getTaskName()+" has Been Fulfilled",   
			         "Please Check Task Manager App To View Responses",
			         "taskmanagernotifier@gmail.com",
			         tasks[0].getEmail());
		} catch (Exception e) {
						e.printStackTrace();
		}  
         return tasks[0];
     }

     protected void onProgressUpdate(Integer... progress) {
       //  setProgressPercent(progress[0]);
     }

     protected void onPostExecute(Long result) {
       //  showDialog("Downloaded " + result + " bytes");
     }
 }