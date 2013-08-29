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
