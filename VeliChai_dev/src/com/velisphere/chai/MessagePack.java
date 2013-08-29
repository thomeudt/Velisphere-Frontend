/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
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
