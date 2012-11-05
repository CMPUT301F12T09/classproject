package com.example.tasktracker.test;


import java.util.ArrayList;

import com.example.tasktracker.AudioFile;
import com.example.tasktracker.Fulfillment;
import com.example.tasktracker.ImageFile;
import com.example.tasktracker.Task;

import junit.framework.TestCase;


public class TaskTest extends TestCase{

	
	public void testTaskStringStringStringBooleanBooleanBooleanBoolean(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true);
		assertTrue(t.getUserDeviceId().equals("12345678"));
		assertTrue(t.getTaskName().equals("Test Name"));
		assertTrue(t.getTaskDescription().equals("Test Description"));
		assertTrue(t.getWantPhoto() == true);
		assertTrue(t.getWantText() == true);
		assertTrue(t.getWantAudio() == true);
		assertTrue(t.getIsOpen() == true);
		assertTrue(t.getIsPublic() == true);
	}
	
	public void testAddSubmission(){
		Task task =  new Task("12345678", "Test Name", "Test Description", true, true, true, true);
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		task.addSubmission("12345678", "Some Text", images, audio);
		assertTrue(task.getSubmissions().get(0).getUserDeviceId().equals("12345678"));
	}
	
	public void testRemoveSubmission(){

		Task task =  new Task("12345678", "Test Name", "Test Description", true, true, true, true);
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		task.addSubmission("12345678", "Some Text", images, audio);
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		task.removeSubmission(0);
		assertFalse(task.getSubmissions().contains(f));
	}

	public void testGetUserDeviceId(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true);
		assertTrue(t.getUserDeviceId().equals("12345678"));
	}
	
	public void testSetUserDeviceId(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true);
		t.setUserDeviceId("987654321");
		assertTrue(t.getUserDeviceId().equals("987654321"));
	}
	
	public void testGetTaskName(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true);
		assertTrue(t.getTaskName().equals("Test Name"));
	}
	
	public void testSetTaskName(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true);
		t.setTaskName("New Name");
		assertTrue(t.getTaskName().equals("New Name"));
	}
	
	public void testGetTaskDescription(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true);
		assertTrue(t.getTaskDescription().equals("Test Description"));
	}
	
	public void testSetTaskDescription(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true);
		t.setTaskDescription("New Description");
		assertTrue(t.getTaskDescription().equals("New Description"));
	}
	
	public void testGetWantPhoto(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, true, false, false);
		assertTrue(t.getWantPhoto() == true);
	}
	
	public void testSetWantPhoto(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, false);
		t.setWantPhoto(true);
		assertTrue(t.getWantPhoto() == true);
	}
	
	public void testGetWantText(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, false, false, false);
		assertTrue(t.getWantText() == true);
	}
	
	public void testSetWantText(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, false);
		t.setWantText(true);
		assertTrue(t.getWantText() == true);
	}
	
	public void testGetWantAudio(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, true, false);
		assertTrue(t.getWantAudio() == true);
	}
	
	public void testSetWantAudio(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, false);
		t.setWantAudio(true);
		assertTrue(t.getWantAudio() == true);
	}
	
	public void testGetIsOpen(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, false);
		assertTrue(t.getIsOpen() == true);
	}
	
	public void testSetIsOpen(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, false);
		t.setIsOpen(false);
		assertTrue(t.getIsOpen() == false);
	}
	
	public void testGetIsPublic(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, true);
		assertTrue(t.getIsPublic() == true);
	}
	
	public void testSetIsPublic(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, false);
		t.setIsPublic(true);
		assertTrue(t.getIsPublic() == true);
	}
	
	public void testGetSubmissions(){
		Task task =  new Task("12345678", "Test Name", "Test Description", true, true, true, true);
		assertTrue(task.getSubmissions().isEmpty());
	}
	
	public void testAddFulfillment(){
		Task task =  new Task("12345678", "Test Name", "Test Description", true, true, true, true);
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		task.addFulfillment(f);
		assertTrue(task.getSubmissions().contains(f));
	}
	
}
