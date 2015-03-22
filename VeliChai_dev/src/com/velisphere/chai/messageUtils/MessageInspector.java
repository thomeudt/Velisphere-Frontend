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
package com.velisphere.chai.messageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.velisphere.chai.VelisphereMart;
import com.velisphere.chai.dataObjects.ActionObject;
import com.velisphere.chai.dataObjects.BLEResultObject;
import com.velisphere.chai.engines.ActionManipulationEngine;
import com.velisphere.chai.engines.BusinessLogicEngine;
import com.velisphere.chai.engines.ServiceEngine;

public class MessageInspector implements Runnable {

	/*
	 * This contains all the possible inspection actions
	 */

	// Declare class in method

	String messageBody = new String();

	MessageInspector(String s) {
		messageBody = s;
	}

	public void run() {
		HashMap<String, String> forEvaluation = new HashMap<String, String>();
		
		System.out.println(" [IN] Message JSON:"+ messageBody);
		
		
		try {
			forEvaluation = MessageFactory.extractKeyPropertyPairs(messageBody);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(" [IN] Message Type:"+ forEvaluation.get("TYPE"));
		
		if (forEvaluation.get("TYPE").equals("REG"))
		{
			try {
				regularMessage(forEvaluation);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
		if (forEvaluation.get("TYPE").equals("CTL"))
		{
			try {
				controlMessage(forEvaluation);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
	return;
	}
	
	
	private void regularMessage(HashMap<String, String> forEvaluation) throws Exception
	{
		String transactionID = UUID.randomUUID().toString();

		String EPID = forEvaluation.get("EPID");
	
		HashMap<String, String> triggeredActions = new HashMap<String, String>();

		Iterator<Map.Entry<String, String>> it = forEvaluation
				.entrySet().iterator();
					
		while (it.hasNext()) {

			Map.Entry<String, String> e = (Map.Entry<String, String>) it
					.next();
			
			LinkedList<ActionObject> executedActions = new LinkedList<ActionObject>();
			
			System.out.println(" [IN] Regular Message Received");
			System.out.println(" [IN] Key: " + e.getKey() + " / Value: " + e.getValue());
						
			if (e.getKey() != "EPID" && e.getKey() != null
					&& e.getKey() != "SECTOK"
					&& e.getKey() != "TIMESTAMP"
					&& e.getKey() != "TYPE"
					&& e.getKey() != "STATE")
			{
				    BLEResultObject bleResult = BusinessLogicEngine.runChecks(EPID, e.getKey(), e.getValue(), (byte) 0);
				    triggeredActions.putAll(bleResult.getTriggerActions());
				    
				    
				    
				    for (Iterator<Entry<String, String>> aIT = triggeredActions.entrySet().iterator(); aIT
							.hasNext();) {
						executedActions.addAll(ActionManipulationEngine.executeActionItems(aIT.next(), forEvaluation));
						
						aIT.remove();
					}
				    //LoggerEngine.writeEndpointLog(EPID, e.getKey(), e.getValue());
				    VelisphereMart.insertTransactionLog(transactionID, EPID, e.getKey(), e.getValue(), executedActions, bleResult.getChecks(), bleResult.getCheckpaths() );		    
					// BusinessLogicEngine.runChecks(EPID, e.getKey(), e.getValue(), (byte) 0);
			}
		
		}
	}


	private void controlMessage(HashMap<String, String> forEvaluation) throws Exception
	{
		String EPID = forEvaluation.get("EPID");
		Iterator<Map.Entry<String, String>> it = forEvaluation
				.entrySet().iterator();
			
		while (it.hasNext()) {

			Map.Entry<String, String> e = (Map.Entry<String, String>) it
					.next();
			
			System.out.println(" [IN] Control Message Received");
			System.out.println(" [IN] Key: " + e.getKey() + " / Value: " + e.getValue());
			
			if (e.getKey() == "setState")
			{
				System.out.println(" [IN] State Update received from " + EPID);
				ServiceEngine.setEndpointState(EPID, e.getValue());
				
			}
		}
	}
			
		
	
}