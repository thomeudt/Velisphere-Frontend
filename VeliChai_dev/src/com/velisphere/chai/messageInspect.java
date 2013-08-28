package com.velisphere.chai;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class messageInspect implements Runnable {

	/*
	 * This contains all the possible inspection actions
	 * 
	 */

	// Declare class in method

	String messageBody = new String();
	messageInspect(String s) { messageBody = s; }

	
	@Override
	public void run() {

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
