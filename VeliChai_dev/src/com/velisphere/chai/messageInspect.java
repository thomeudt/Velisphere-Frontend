package com.velisphere.chai;

import org.json.JSONException;
import org.json.JSONObject;

public class messageInspect {

	/*
	 * This contains all the possible inspection actions
	 * 
	 */
	
	
	public static final void inspectAMQP(String messageBody) throws Exception
	{
		// System.out.println(" [IN] "+ messageBody);
		
		// MessageAdm.provideLdapMobiles("ute");
		// this was the old ldap test
		
		// now follows the chat example, meaning: just unpack the incoming messagePack and forward to destination queue as plain text
		
		
		MessagePack mp = new MessagePack();
		String DestQueueName = mp.extractProperty(messageBody, "0");
		String ChatMessage = mp.extractProperty(messageBody, "1");
		

		// First, we discard all messages where the controller queue is addressed as the target queue in the messagepack
		
		if (DestQueueName.equals(ServerParameters.controllerQueueName)){
			System.out.println("False send to controller queue!!!");
		}
		else
		{		
			Send.main(ChatMessage, DestQueueName);
		}
		
		
		
		
		
	}
	
	
	
	
}
