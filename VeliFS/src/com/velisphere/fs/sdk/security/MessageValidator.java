package com.velisphere.fs.sdk.security;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.velisphere.fs.sdk.config.ConfigData;


public class MessageValidator {

	
	public static boolean validateHmac(String receivedHMAC, String payload, String endpointID) 
	{
		

		String secret = ConfigData.secret;
		
		System.out.println("Secret in DB: " + secret );
		
		String calculatedHmac = HashTool.getHmacSha1(payload, secret);
		
		System.out.println("Calculated HMAC: " + calculatedHmac + " <> Received HMAC: " + receivedHMAC);
		
		boolean validationOK = false;
		
		if (calculatedHmac.equals(receivedHMAC)) validationOK = true;
		
		return validationOK;

	}
	
	
	
	
}
