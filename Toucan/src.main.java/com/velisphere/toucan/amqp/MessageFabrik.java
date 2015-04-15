package com.velisphere.toucan.amqp;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageFabrik {

	
	String jsonString;
	
	public MessageFabrik(Object object)
	{
	
		ObjectMapper mapper = new ObjectMapper();
		
		System.out.println("Intake: " + object.toString());
	 
		jsonString = new String();
		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		System.out.println("JSON generiert: " + jsonString);
				
	}

	
	
	public String extractProperty(String jsonInput, String propertyID) throws JsonProcessingException, IOException 
	{

		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser jp = factory.createParser(jsonInput);
		String foundValue = new String();

		while (jp.nextToken() != JsonToken.END_OBJECT) {

			String fieldname = jp.getCurrentName();
			if (propertyID.equals(fieldname)) {
				jp.nextToken();
				foundValue = jp.getText(); 
			}
		}

		jp.close();		 

		return foundValue;  
	}



	public String getJsonString() {
		return jsonString;
	}

		
	


}

