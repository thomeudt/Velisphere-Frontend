package com.velisphere.chai;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;

/*
 * This class contains all methods needed to extract values from a MessagePack or to build a new MessagePack
 */


public class MessagePack {

	public static String extractProperty(String jsonInput, String propertyID) throws JsonProcessingException, IOException 
	{

		JsonParser jp = ChaiWorker.factory.createParser(jsonInput);
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
}
