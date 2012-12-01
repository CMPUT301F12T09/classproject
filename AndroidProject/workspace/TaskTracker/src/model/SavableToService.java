
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

import java.io.Serializable;

/**
 * This class ensures the functionality to be saved to the webservice.
 * Can be encoded and decoded to/from a String.
 * 
 * @author glancop
 *
 */
public class SavableToService
{
	
	private static final long serialVersionUID = 1L;
	public SavableToService()
	{
	}
	
	public String saveToString()
	{
		return "";
	}
	public String toString()
	{
		return type + ": " + id;
	}
	public String getId(){
		return id;
	}
	public void setId(String inId){
		id = inId;
	}
	public String getType(){
		return type;
	}
	public void setType(String inType){
		type = inType;
	}
	public long getDbId()
	{
		return -1;
	}
		
	public String id;
	public String belongsTo;
	public String type;
	public String body;
}
