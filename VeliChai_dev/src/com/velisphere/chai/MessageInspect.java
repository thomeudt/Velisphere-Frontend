package com.velisphere.chai;

import org.json.JSONException;
import org.json.JSONObject;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

public class MessageInspect {

	/*
	 * This contains all the possible inspection actions
	 * 
	 */
	
	
	public void inspectAMQP(String messageBody, BasicProperties props, Envelope env) throws Exception
	{
		System.out.println("Inspected: "+ messageBody);
		
		// MessageAdm.provideLdapMobiles("ute");
		// this was the old ldap test
		
		// now follows the chat example, meaning: just unpack the incoming messagePack and forward to destination queue as plain text
		
		MessagePack mp = new MessagePack();
		String DestQueueName = mp.extractProperty(messageBody, "0");
		String ChatMessage = mp.extractProperty(messageBody, "1");
		Send.main(ChatMessage, DestQueueName);
		
		
		
		
		
	}
	
	
	
	
}
