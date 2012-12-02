
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
	private long db_Id;
	
	public long getDbId()
	{
		return db_Id;
	}
	public void setDbId(long id)
	{
		db_Id = id;
	}
	
	//this is a test overloading of tostring to ensure our bitmap is being passed
	public String toString(){
		if (bitmap != null){
			return "a picture";
		}
		return "a null picture";
	}
	
	public ImageFile(Bitmap bmp){
	    bitmap = bmp;
	}
	public ImageFile(){
	    bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
	}
	
	/**
	 * Called by the service manager to get the string to be sent to the service
	 * @return ret
	 */
	public String saveToString()
	{
		//Format of (service given id) (service given id of owner) (type of object) (body string)
		String ret;
		/*
		StringBuilder builder = new StringBuilder("");
		
		for(int i = 0; i < bitmap.getWidth(); i++)
		{
			for(int j = 0; j < bitmap.getHeight(); j++)
			{
				builder.append("*").append((char)bitmap.getPixel(i, j));
			}
		}
		
		ret = String.format("%s+%s+%s", belongsTo, "IMAGE", builder.toString());*/
		
		ret = String.format("%s+%s+%s", belongsTo, "IMAGE", "-1");
		bitmap = null;
		return ret;
	}
	
	/**
	 * Called by the service manager when building the objects pulled from the server
	 * @param data
	 * @return
	 */
	public static ImageFile buildFromString(String data)
	{
		//parse the string and get the required data
		//We should get the body string from the saveToString method
		System.out.println("GENERATING IMAGE " + data);
		String deliminator = "[+]+";
		String[] tokens = data.split(deliminator);
		
		ImageFile ret = new ImageFile();
		
		ret.belongsTo = tokens[0]; 
		ret.type = tokens[1]; /*
		String[] pixels = tokens[2].split("[*]+");
		int k = 0;
		 
		for(int i = 0; i < 400; i++)
		{
			for(int j = 0; j < 400; j++)
			{
				if(pixels[k].equals(""))
				{
					ret.bitmap.setPixel(i, j, -1);
					k++;
				}
				else
				{
					ret.bitmap.setPixel(i, j, (int)(pixels[k++].charAt(0)));
				}
			}
		}*/
		
		for(int i = 0; i < 400; i++)
		{
			for(int j = 0; j < 400; j++)
			{
				ret.bitmap.setPixel(i, j, Integer.parseInt(tokens[2]));
			}
		}
		
		ret.body = "";
		return ret;
	}
	
}
