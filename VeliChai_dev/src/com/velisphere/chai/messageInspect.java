package com.velisphere.chai;

import org.json.JSONException;
import org.json.JSONObject;

public class messageInspect {

	/*
	 * This contains all the possible inspection actions
	 * 
	 */
	
	
	public void inspectAMQP(String messageBody) throws Exception
	{
		System.out.println("Inspected: "+ messageBody);
		
		MessageAdm.provideLdapMobiles("ute");
		
		
	}
	
	
	
	
}
