package com.velisphere.chai;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;

public class messageInspect implements Runnable {

	/*
	 * This contains all the possible inspection actions
	 * 
	 */
	
	// Declare class in method
	
	String messageBody = new String();
	messageInspect(String s) { messageBody = s; }
	
	
	public static final void ispectAMQP(String messageBody) throws Exception
	{
		// System.out.println(" [IN] "+ messageBody);
		
		// MessageAdm.provideLdapMobiles("ute");
		// this was the old ldap test
		
		// now follows the chat example, meaning: just unpack the incoming messagePack and forward to destination queue as plain text
		
		
		// MessagePack mp = new MessagePack(); changed to static for performance
		
		String DestQueueName = MessagePack.extractProperty(messageBody, "0");
		String ChatMessage = MessagePack.extractProperty(messageBody, "1");
		

		// First, we discard all messages where the controller queue is addressed as the target queue in the messagepack
		
		if (DestQueueName.equals(ServerParameters.controllerQueueName)){
			System.out.println("False send to controller queue!!!");
		}
		else
		{		
			// Send.main(ChatMessage, DestQueueName);
			Thread senderThread;
			
			senderThread = new Thread(new Send(ChatMessage, DestQueueName), "sender");
			senderThread.start();
	    	
			
			
		}
		
		
		
		
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		
		String ChatMessage = null;
		String DestQueueName = null;
		try {
			ChatMessage = MessagePack.extractProperty(messageBody, "1");
			DestQueueName = MessagePack.extractProperty(messageBody, "0");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		// First, we discard all messages where the controller queue is addressed as the target queue in the messagepack
		
		if (DestQueueName.equals(ServerParameters.controllerQueueName)){
			System.out.println("False send to controller queue!!!");
		}
		else
		{		
			try {
				Send.main(ChatMessage, DestQueueName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
		
	}


	
	
	
	
}
