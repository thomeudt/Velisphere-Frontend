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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

public class MessageInspect implements Runnable {

	/*
	 * This contains all the possible inspection actions
	 * 
	 */

	// Declare class in method

	String messageBody = new String();
	MessageInspect(String s) { messageBody = s; }

	
	@Override
	public void run() {

		/*
		 * This is the old chat example
		
		
		String ChatMessage = null;
		String DestQueueName = null;
		try {
			ChatMessage = MessagePack.extractProperty(messageBody, "1");
			DestQueueName = MessagePack.extractProperty(messageBody, "0");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		*/

		// First, we discard all messages where the controller queue is addressed as the target queue in the messagepack

		String DestQueueName = null;
		
		try {
			DestQueueName = MessagePack.extractProperty(messageBody, "0");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if (DestQueueName.equals(ServerParameters.controllerQueueName)){
			System.out.println("False send to controller queue!!!");
		}
		else
		{		
		
			HashMap<String, String> forEvaluation = new HashMap<String, String>();
			try {
				forEvaluation = MessagePack.extractKeyPropertyPair(messageBody);

				String EPID = forEvaluation.get("EPID");
			
				Imdb.resetChecksForEndpoint(EPID);
				
				//System.out.println("**************************** IMDB CHECK INITIATED ************************************");
				
				List<String> triggeredRules = new ArrayList<String>();
				
				for ( Map.Entry<String, String> e : forEvaluation.entrySet() )
				{
					// System.out.println( e.getKey() + "="+ e.getValue() );
					if(e.getKey() != "EPID" && e.getKey() != null && e.getKey() != "SECTOK" && e.getKey() != "TIMESTAMP" && e.getKey() != "TYPE" )
					{
						// System.out.println("EPID: " + EPID);
						// System.out.println("KEY:  " + e.getKey());
						// System.out.println("VALUE:" + e.getValue());
						triggeredRules.addAll(Imdb.runChecks(EPID, e.getKey(), e.getValue(), "=", (byte) 0));
					}
				}
				
				for(int i=0; i < triggeredRules.size(); i++)
				{
				System.out.println("Rule found: " + triggeredRules.get(i));
				Send.main(triggeredRules.get(i), "adam");
				}
				
				
				



			
			
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
		}
		return;

	}

}