package com.example.tasktracker.test;

import java.util.ArrayList;
import java.util.Date;

import model.*;


import junit.framework.TestCase;


public class FulfillmentTest extends TestCase{
	
	public void testFulfillment(){
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		assertTrue(f.getUserDeviceId().equals("12345678"));
		assertTrue(f.getTextInput().equals("Some Text"));
		
	}
	
	public void testGetUserDeviceId(){
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		assertTrue(f.getUserDeviceId().equals("12345678"));
	}
	
	public void testSetUserDeviceId(){

		Fulfillment f = new Fulfillment("12345678", "Some Text");
		f.setUserDeviceId("987654321");
		assertTrue(f.getUserDeviceId().equals("987654321"));
	}
	
	public void testGetTextInput(){;
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		assertTrue(f.getTextInput().equals("Some Text"));
	}
	
	public void testSetTextInput(){
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		f.setTextInput("Different Text");
		assertTrue(f.getTextInput().equals("Different Text"));
	}
	
	@SuppressWarnings("deprecation")
	public void testGetDateAdded(){
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		Date storedDate = f.getDateAdded();
		Date today = new Date();
		assertTrue(storedDate.getDate() == today.getDate());
		assertTrue(storedDate.getMonth() == today.getMonth());
		assertTrue(storedDate.getYear() == today.getYear());
	}
	
	public void testSetDateAdded(){
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		Date newDate = new Date();
		f.setDateAdded(newDate);
		assertTrue(newDate == f.getDateAdded());
	}
	
	public void testGetImageFiles(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		ArrayList<ImageFile> newImages = new ArrayList<ImageFile>();
		newImages.add(new ImageFile());
		f.setImageFiles(newImages);
		assertTrue(f.getImageFiles() == newImages);
		
	}
	
	public void testSetImageFiles(){
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		ImageFile i = new ImageFile();
		ArrayList<ImageFile> newImages = new ArrayList<ImageFile>();
		newImages.add(i);
		f.setImageFiles(newImages);
		assertTrue(f.getImageFiles() == newImages);
	}
	
	public void testGetAudioFiles(){
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		ArrayList<AudioFile> newAudio = new ArrayList<AudioFile>();
		newAudio.add(new AudioFile());
		f.setAudioFiles(newAudio);
		assertTrue(f.getAudioFiles() == newAudio);
	}
	
	public void testSetAudioFiles(){
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		AudioFile a = new AudioFile();
		ArrayList<AudioFile> newAudio = new ArrayList<AudioFile>();
		newAudio.add(a);
		f.setAudioFiles(newAudio);
		assertTrue(f.getAudioFiles() == newAudio);
	}
	
	public void testAddImage(){
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		ImageFile i = new ImageFile();
		f.addImage(i);
		assertTrue(f.getImageFiles().contains(i));
	}
	
	public void testAddAudio(){
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		AudioFile a = new AudioFile();
		f.addAudio(a);
		assertTrue(f.getAudioFiles().contains(a));
	}
	
	public void testSetDbId(){
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		f.setDbId(987654321);
		assertTrue(f.getDbId() == 987654321);
	}
	
	public void testGetDbId(){
		Fulfillment f = new Fulfillment("12345678", "Some Text");
		f.setDbId(987654321);
		assertTrue(f.getDbId() == 987654321);
	}
}
