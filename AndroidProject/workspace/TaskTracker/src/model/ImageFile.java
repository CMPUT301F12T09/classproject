
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

package model;

import java.io.File;

import android.graphics.Bitmap;

/**
 * This class acts as a wrapper for an image file that can either be 
 * recorded and submitted on the fly or read in from storage.
 * 
 * <p>This class extends SavableToService which means it has the necessary
 * functionality to be saved (encoded/decoded via String) to the webservice.</p>
 * 
 * @author zturchan
 *
 */
public class ImageFile extends SavableToService 
{
	public File image;
	public Bitmap bitmap;
	
	//Called by the service manager to get the string to be sent to the service
	public String saveToString()
	{
		//Format of (service given id) (service given id of owner) (type of object) (body string)
		String ret;
		//body = String.format("", image);
		
		ret = String.format("%s %s %s %s", id, belongsTo, "IMAGE", body);
		return ret;
	}
	
	public ImageFile(Bitmap bmp){
	    bitmap = bmp;
	}
	public ImageFile(){
	    
	}
	
	//Called by the service manager when building the objects pulled from the server
	public static ImageFile buildFromString(String data)
	{
		//parse the string and get the required data
		//We should get the body string from the saveToString method
		
		/*
		String deliminator = "[ ]+";
		String[] tokens = data.split(deliminator);
		
		String ownerId = tokens[0]; 
		String text = tokens[1];
		*/
		
		ImageFile ret = new ImageFile();
		
		return ret;
	}
	
}