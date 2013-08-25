package com.velisphere.chai;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class MessagePack {

	 public String extractProperty(String jsonInput, String propertyID) throws ParseException 
	   {
	      JSONParser parser=new JSONParser();
	      Object obj = parser.parse(jsonInput);
	      HashMap messagePack = (HashMap)obj;
	      	      
	      return (String) messagePack.get(propertyID);  
	      	      
	   }
}
