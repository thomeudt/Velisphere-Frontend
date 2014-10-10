/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.velisphere.chai.dataObjects.ActionObject;

public class ActionManipulationEngine {

	
	
	public static LinkedList<ActionObject> executeActionItems(Entry<String, String> actionHash, HashMap<String, String> inboundMessageMap) throws Exception
	{
				
		LinkedList<ActionObject> executedActions = new LinkedList<ActionObject>();
		
		final ClientResponse findActionDetails = BusinessLogicEngine.montanaClient
				.callProcedure("AME_DetailsForAction", actionHash.getValue(), actionHash.getKey());
		
		final VoltTable findActionDetailsResults[] = findActionDetails
				.getResults();
		
		VoltTable actionDetails = findActionDetailsResults[0];
		// check if any rows have been returned
		
		HashMap<String,String> outboundMessageMap = new HashMap<String, String>();
		while (actionDetails.advanceRow()) 
			{
				//System.out.println(actionDetails.toFormattedString());
				//System.out.println("Actiondetails: " + actionDetails);
				String targetEPID = new String();
				String payload = new String();
				if (actionDetails.getString("TGTEPIDFROMINBOUNDPROP").isEmpty() == false){
					targetEPID = inboundMessageMap.get(actionDetails.getString("TGTEPIDFROMINBOUNDPROP"));
				} else
				{
					targetEPID = actionDetails.getString("TARGETENDPOINTID");
				}
				
				
				if (actionDetails.getString("INBOUNDPROPERTYID").isEmpty() == false){
					payload = inboundMessageMap.get(actionDetails.getString("INBOUNDPROPERTYID"));
				}
				else
					if (actionDetails.getString("CURRENTSTATEPROPERTYID").isEmpty() == false){
						//TODO This is still to be implemented in version 1.0!
					}
					else
						payload = actionDetails.getString("CUSTOMPAYLOAD");
				
				outboundMessageMap.put(actionDetails.getString("OUTBOUNDPROPERTYID"), payload);
								
				// execute action via AMQP
				
				Send.sendHashTable(outboundMessageMap, targetEPID, inboundMessageMap.get("EPID"));
				
				// return ActionObject
				
				ActionObject executedAction = new ActionObject(actionDetails.getString("ACTIONID"), inboundMessageMap.get("EPID"),targetEPID, payload, actionDetails.getString("OUTBOUNDPROPERTYID"));
				executedActions.add(executedAction);

			}
				
		return executedActions;
		
	}


}
