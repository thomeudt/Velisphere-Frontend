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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;

public class MessageInspect implements Runnable {

	/*
	 * This contains all the possible inspection actions
	 */

	// Declare class in method

	String messageBody = new String();

	MessageInspect(String s) {
		messageBody = s;
	}

	public void run() {

		/*
		 * This is the old chat example
		 * 
		 * 
		 * String ChatMessage = null; String DestQueueName = null; try {
		 * ChatMessage = MessagePack.extractProperty(messageBody, "1");
		 * DestQueueName = MessagePack.extractProperty(messageBody, "0"); }
		 * catch (JsonProcessingException e) { e.printStackTrace(); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */

		// First, we discard all messages where the controller queue is
		// addressed as the target queue in the messagepack

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

		if (DestQueueName.equals(ServerParameters.controllerQueueName)) {
			System.out.println("False send to controller queue!!!");
		} else {

			HashMap<String, String> forEvaluation = new HashMap<String, String>();
			try {
				forEvaluation = MessagePack.extractKeyPropertyPair(messageBody);

				String EPID = forEvaluation.get("EPID");

				BusinessLogicEngine.resetChecksForEndpoint(EPID);

				// System.out.println("**************************** IMDB CHECK INITIATED ************************************");

				HashSet<String> triggeredRules = new HashSet<String>();

				Iterator<Map.Entry<String, String>> it = forEvaluation
						.entrySet().iterator();

				// for ( Map.Entry<String, String> e : forEvaluation.entrySet()
				// )
				while (it.hasNext()) {
					// System.out.println( e.getKey() + "="+ e.getValue() );

					Map.Entry<String, String> e = (Map.Entry<String, String>) it
							.next();

					if (e.getKey() != "EPID" && e.getKey() != null
							&& e.getKey() != "SECTOK"
							&& e.getKey() != "TIMESTAMP"
							&& e.getKey() != "TYPE") {
							triggeredRules.addAll(BusinessLogicEngine.runChecks(EPID, e.getKey(), e.getValue(), "=", (byte) 0));
							LoggerEngine.writeEndpointLog(EPID, e.getKey(), e.getValue());
							
					}
				}

				HashSet<String> actionItems = new HashSet<String>();
				
				for (Iterator<String> rIT = triggeredRules.iterator(); rIT
						.hasNext();) {
					actionItems.addAll(ActionManipulationEngine.getActionItems( rIT.next()));
					rIT.remove();
				}
				
				// System.out.println("Aktionen: " + actionItems);
				
				for (Iterator<String> aIT = actionItems.iterator(); aIT
						.hasNext();) {
					ActionManipulationEngine.executeActionItems(aIT.next(), forEvaluation);
					
					aIT.remove();
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