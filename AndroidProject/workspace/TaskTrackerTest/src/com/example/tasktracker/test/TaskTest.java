package com.example.tasktracker.test;


import model.*;

import junit.framework.TestCase;


public class TaskTest extends TestCase{

	
	public void testTaskStringStringStringBooleanBooleanBooleanBoolean(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
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
		Task task =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		task.addSubmission(f);
		assertTrue(task.getSubmissions().get(0).getUserDeviceId().equals("12345678"));
	}
	
	public void testRemoveSubmission(){

		Task task =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		task.addSubmission(f);
		task.removeSubmission(0);
		assertFalse(task.getSubmissions().contains(f));
	}

	public void testGetUserDeviceId(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
		assertTrue(t.getUserDeviceId().equals("12345678"));
	}
	
	public void testSetUserDeviceId(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
		t.setUserDeviceId("987654321");
		assertTrue(t.getUserDeviceId().equals("987654321"));
	}
	
	public void testGetTaskName(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
		assertTrue(t.getTaskName().equals("Test Name"));
	}
	
	public void testSetTaskName(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
		t.setTaskName("New Name");
		assertTrue(t.getTaskName().equals("New Name"));
	}
	
	public void testGetTaskDescription(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
		assertTrue(t.getTaskDescription().equals("Test Description"));
	}
	
	public void testSetTaskDescription(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
		t.setTaskDescription("New Description");
		assertTrue(t.getTaskDescription().equals("New Description"));
	}
	
	public void testGetEmail(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
		assertTrue(t.getEmail().equals("email@test.com"));
	}
	
	public void testSetEmail(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
		t.setEmail("different@test.com");
		assertTrue(t.getEmail().equals("different@test.com"));
	}
	
	public void testGetWantPhoto(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, true, false, false, "email@test.com");
		assertTrue(t.getWantPhoto() == true);
	}
	
	public void testSetWantPhoto(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, false, "email@test.com");
		t.setWantPhoto(true);
		assertTrue(t.getWantPhoto() == true);
	}
	
	public void testGetWantText(){
		Task t =  new Task("12345678", "Test Name", "Test Description", true, false, false, false, "email@test.com");
		assertTrue(t.getWantText() == true);
	}
	
	public void testSetWantText(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, false, "email@test.com");
		t.setWantText(true);
		assertTrue(t.getWantText() == true);
	}
	
	public void testGetWantAudio(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, true, false, "email@test.com");
		assertTrue(t.getWantAudio() == true);
	}
	
	public void testSetWantAudio(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, false, "email@test.com");
		t.setWantAudio(true);
		assertTrue(t.getWantAudio() == true);
	}
	
	public void testGetIsOpen(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, false, "email@test.com");
		assertTrue(t.getIsOpen() == true);
	}
	
	public void testSetIsOpen(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, false, "email@test.com");
		t.setIsOpen(false);
		assertTrue(t.getIsOpen() == false);
	}
	
	public void testGetIsPublic(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, true, "email@test.com");
		assertTrue(t.getIsPublic() == true);
	}
	
	public void testSetIsPublic(){
		Task t =  new Task("12345678", "Test Name", "Test Description", false, false, false, false, "email@test.com");
		t.setIsPublic(true);
		assertTrue(t.getIsPublic() == true);
	}
	
	public void testGetSubmissions(){
		Task task =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
		assertTrue(task.getSubmissions().isEmpty());
	}
	
	public void testAddFulfillment(){
		Task task =  new Task("12345678", "Test Name", "Test Description", true, true, true, true, "email@test.com");
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		task.addFulfillment(f);
		assertTrue(task.getSubmissions().contains(f));
	}
	
}
