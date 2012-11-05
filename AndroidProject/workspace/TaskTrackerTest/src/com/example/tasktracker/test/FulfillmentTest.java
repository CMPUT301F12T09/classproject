package com.example.tasktracker.test;

import java.util.ArrayList;
import java.util.Date;

import com.example.tasktracker.AudioFile;
import com.example.tasktracker.Fulfillment;
import com.example.tasktracker.ImageFile;


import junit.framework.TestCase;


public class FulfillmentTest extends TestCase{
	
	public void testFulfillment(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		assertTrue(f.getUserDeviceId().equals("12345678"));
		assertTrue(f.getTextInput().equals("Some Text"));
		assertTrue(f.getImageFiles() == images);
		assertTrue(f.getAudioFiles() == audio);
	}
	
	public void testGetUserDeviceId(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		assertTrue(f.getUserDeviceId().equals("12345678"));
	}
	
	public void testSetUserDeviceId(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		f.setUserDeviceId("987654321");
		assertTrue(f.getUserDeviceId().equals("987654321"));
	}
	
	public void testGetTextInput(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		assertTrue(f.getTextInput().equals("Some Text"));
	}
	
	public void testSetTextInput(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		f.setTextInput("Different Text");
		assertTrue(f.getTextInput().equals("Different Text"));
	}
	
	@SuppressWarnings("deprecation")
	public void testGetDateAdded(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		Date storedDate = f.getDateAdded();
		Date today = new Date();
		assertTrue(storedDate.getDate() == today.getDate());
		assertTrue(storedDate.getMonth() == today.getMonth());
		assertTrue(storedDate.getYear() == today.getYear());
	}
	
	public void testSetDateAdded(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		Date newDate = new Date();
		f.setDateAdded(newDate);
		assertTrue(newDate == f.getDateAdded());
	}
	
	public void testGetImageFiles(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		assertTrue(f.getImageFiles() == images);
	}
	
	public void testSetImageFiles(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		ImageFile i = new ImageFile();
		ArrayList<ImageFile> newImages = new ArrayList<ImageFile>();
		newImages.add(i);
		f.setImageFiles(newImages);
		assertTrue(f.getImageFiles() == newImages);
	}
	
	public void testGetAudioFiles(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		assertTrue(f.getAudioFiles() == audio);
	}
	
	public void testSetAudioFiles(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		AudioFile a = new AudioFile();
		ArrayList<AudioFile> newAudio = new ArrayList<AudioFile>();
		newAudio.add(a);
		f.setAudioFiles(newAudio);
		assertTrue(f.getAudioFiles() == newAudio);
	}
	
	public void testAddImage(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		ImageFile i = new ImageFile();
		f.addImage(i);
		assertTrue(f.getImageFiles().contains(i));
	}
	
	public void testAddAudio(){
		ArrayList<ImageFile> images = new ArrayList<ImageFile>();
		ArrayList<AudioFile> audio = new ArrayList<AudioFile>();
		Fulfillment f = new Fulfillment("12345678", "Some Text", images, audio);
		AudioFile a = new AudioFile();
		f.addAudio(a);
		assertTrue(f.getAudioFiles().contains(a));
	}
	
}
