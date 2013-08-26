package com.velisphere.chai;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.*;





public class MessagePack {

	 public static String extractProperty(String jsonInput, String propertyID) throws JsonProcessingException, IOException 
	   {
	      // JSONParser parser=new JSONParser();
	      // Object obj = parser.parse(jsonInput);
	      // HashMap messagePack = (HashMap)obj;
	      	 
		 JsonParser jp = ChaiWorker.factory.createParser(jsonInput);

		 
//		  Map<String, String> messagePack = ChaiWorker.mapper.readValue(jsonInput, Map.class);

		 String foundValue = new String();
		 
		 while (jp.nextToken() != JsonToken.END_OBJECT) {
			 
				String fieldname = jp.getCurrentName();
				if (propertyID.equals(fieldname)) {
				  jp.nextToken();
				  foundValue = jp.getText(); // display mkyong
		 
				}
		 
				}
		 
			  
			  jp.close();		 
		  
		  
	      return foundValue;  
	      	      
	   }
}
