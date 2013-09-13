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

/**
 * TODO
 * Here we need to
 * - lookup rules in every evaluation step - rules can be attached to a check or any multicheck * 
 */

import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.client.*;

import com.amd.aparapi.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;



public class BusinessLogicEngine {

	public static org.voltdb.client.Client montanaClient;

	public static void openDatabase() throws UnknownHostException, IOException {
		/*
		 * Instantiate a client and connect to the database.
		 */

		BusinessLogicEngine.montanaClient = ClientFactory.createClient();
		BusinessLogicEngine.montanaClient.createConnection(ServerParameters.volt_ip);
		// System.out.println(" [IN] Connected to VoltDB on address: "
		// + ServerParameters.volt_ip);
	}

	public static void writeLog(String exchangeName, String message,
			String queueName, String routingKey) throws Exception {

		/*
		 * Write log to database.
		 */

		UUID identifier = UUID.randomUUID();
		String identifierString = identifier.toString();

		// TODO: change to a meaningful log table!

		BusinessLogicEngine.montanaClient.callProcedure("Insert", "1", message, queueName,
				"1", identifierString);

	}

	public static List<String> lookupRulesForCheckID(String checkID)
			throws NoConnectionsException, IOException, ProcCallException {

		List<String> ruleIDs = new ArrayList<String>();

		final ClientResponse findRulesForCheckIDResponse = Imdb.montanaClient
				.callProcedure("FindRulesForCheckID", checkID);
		if (findRulesForCheckIDResponse.getStatus() != ClientResponse.SUCCESS) {
			System.err.println(findRulesForCheckIDResponse.getStatusString());
		}
		final VoltTable findRulesForCheckIDResults[] = findRulesForCheckIDResponse
				.getResults();
		if (findRulesForCheckIDResults.length == 0) {
			// System.out.printf("Not valid match found!\n");
		}

		VoltTable result = findRulesForCheckIDResults[0]; 
			// check if any rows have been returned
			while (result.advanceRow()) {
					{

						
						// extract the value in column checkid
						ruleIDs.add(result.getString("RULEID"));
						//System.out.println(result.getString("RULEID"));
					}
				}
	
		return ruleIDs;
	}


	
	public static List<String> runChecks(String endpointID, String propertyID,
			String checkValue, String operator, byte expired) throws Exception {

		List<String> triggerRules = new ArrayList<String>();
		List<String> ruleIDs = new ArrayList<String>();		

		// find checks that match the incoming expression
		
		final ClientResponse bleChecksForExpression = Imdb.montanaClient.callProcedure(
				"BLE_ChecksForExpression", endpointID, propertyID, checkValue,
				operator, expired);

		if (bleChecksForExpression.getStatus() != ClientResponse.SUCCESS) {
			System.err.println(bleChecksForExpression.getStatusString());

		}


		final VoltTable vtChecksForExpression[] = bleChecksForExpression.getResults();
		
		List<String> validCheckIDs = new ArrayList<String>();

		VoltTable vtChecksForExpressionT = vtChecksForExpression[0];

		while (vtChecksForExpressionT.advanceRow()) {

			validCheckIDs.add(vtChecksForExpressionT.getString("CHECKID"));
			ruleIDs = lookupRulesForCheckID(vtChecksForExpressionT.getString("CHECKID"));
		}
		
		
		
		// lookup relevant checkpaths that contain the check
		
		HashMap<String,String> checkPathMembers = new HashMap<String,String>();

		Iterator<String> itVCI = validCheckIDs.iterator();

		while (itVCI.hasNext())
		{
			String sTR = itVCI.next();
						
			final ClientResponse bleCheckPathForChecks = Imdb.montanaClient.callProcedure(
					"BLE_CheckPathForChecks", sTR);

			if (bleChecksForExpression.getStatus() != ClientResponse.SUCCESS) {
				System.err.println(bleChecksForExpression.getStatusString());

			}

			final VoltTable vtCheckPathForChecks[] = bleCheckPathForChecks.getResults();
												
			VoltTable vtCheckPathForChecksT = vtCheckPathForChecks[0];

			 while (vtCheckPathForChecksT.advanceRow()) {

				String key = vtCheckPathForChecksT.getString("CHECKPATHID");
				String value = vtCheckPathForChecksT.getString("CHECKID");
				System.out.println(vtCheckPathForChecksT.getString("CHECKPATHID"));
				checkPathMembers.put(key, value);	
				
				 
				// System.out.println(vtCheckPathForChecksT.getString("CHECKPATHID") + " --- " + vtCheckPathForChecksT.getString("CHECKID"));
			
			}
			

		}
		
		
		
		// lookup MultiChecks contained in the checkpaths


		HashMap<String,String> checkPathMultiCheckMembers = new HashMap<String,String>();
		Iterator<String> itCPM = checkPathMembers.keySet().iterator();		
		if (checkPathMembers.isEmpty() == false){
		

		while (itCPM.hasNext())
		{
			String sTR = itCPM.next();
			final ClientResponse bleCheckPathForMultiChecks = Imdb.montanaClient.callProcedure(
					"BLE_CheckPathForMultiChecks", sTR);

			if (bleChecksForExpression.getStatus() != ClientResponse.SUCCESS) {
				System.err.println(bleChecksForExpression.getStatusString());

			}

			final VoltTable vtCheckPathForMultiChecks[] = bleCheckPathForMultiChecks.getResults();



			VoltTable vtCheckPathForMultiChecksT = vtCheckPathForMultiChecks[0];

			 while (vtCheckPathForMultiChecksT.advanceRow()) {

				checkPathMultiCheckMembers.put(vtCheckPathForMultiChecksT.getString("CHECKPATHID"), vtCheckPathForMultiChecksT.getString("MULTICHECKID"));
				//System.out.println(checkPathMultiCheckMembers.get(vtCheckPathForMultiChecksT.getString("CHECKPATHID")));
				
			}
					 
		}
		}
		
		System.out.println(checkPathMultiCheckMembers);
		
		// evaluate first level multichecks (connected to checks) in relevant checkpaths
		
		HashMap<String,String> relevantFirstLevelMultiChecks = new HashMap<String,String>();
				
		Iterator<String> itCPMCM = checkPathMultiCheckMembers.keySet().iterator();
		while (itCPMCM.hasNext())
		{
						
			String sTR = itCPMCM.next();
			
			final ClientResponse bleCheckPathForMultiChecks = Imdb.montanaClient.callProcedure(
					"BLE_CheckPathForMultiChecks", sTR);

			if (bleChecksForExpression.getStatus() != ClientResponse.SUCCESS) {
				System.err.println(bleChecksForExpression.getStatusString());

			}

			final VoltTable vtCheckPathForMultiChecks[] = bleCheckPathForMultiChecks.getResults();



			VoltTable vtCheckPathForMultiChecksT = vtCheckPathForMultiChecks[0];

			 while (vtCheckPathForMultiChecksT.advanceRow()) {

				 relevantFirstLevelMultiChecks.put(vtCheckPathForMultiChecksT.getString("CHECKPATHID"), vtCheckPathForMultiChecksT.getString("MULTICHECKID"));
				 // System.out.println(validFirstLevelMultiChecks.get(vtCheckPathForMultiChecksT.getString("CHECKPATHID")));
			}

		}


		HashMap<String,String> trueFirstLevelMultiChecks = new HashMap<String,String>();
		
		Iterator<String> itVFLMC = relevantFirstLevelMultiChecks.values().iterator();
		while (itVFLMC.hasNext())
		{
			String sTR = itVFLMC.next();
			
			//System.out.println(sTR);
			final ClientResponse IsMultiCheckTrue = Imdb.montanaClient.callProcedure(
					"BLE_IsMultiCheckTrue", sTR);

			if (IsMultiCheckTrue.getStatus() != ClientResponse.SUCCESS) {
				System.err.println(bleChecksForExpression.getStatusString());

			}

			final VoltTable vtIsMultiCheckTrue[] = IsMultiCheckTrue.getResults();

			if (vtIsMultiCheckTrue.length != 0) {

			VoltTable vtCheckPathForMultiChecksT = vtIsMultiCheckTrue[0];

			 while (vtCheckPathForMultiChecksT.advanceRow()) {

				 trueFirstLevelMultiChecks.put(vtCheckPathForMultiChecksT.getString("CHECKPATHID"), vtCheckPathForMultiChecksT.getString("MULTICHECKID"));
								
			}
			}

		}

		
		
		// evaluate all other multichecks (connected to multichecks) in relevant checkpaths
		
		
		
		/*
		 * Run the lowest level check engine
		 */

		// Initialize list of rules to be triggered after evaluation

		/*
		 * Step 1: Query all checks matching the data in terms of endPointID,
		 * propertyID, CheckValue etc.
		 *

		// System.out.println("------------------NEW IMDB CHECK---------------------------");
		// System.out.println("Endpoint:    " + endpointID);
		// System.out.println("Property:    " + propertyID);
		// System.out.println("Checkvalue   " + checkValue);
		// System.out.println("-----------------------------------------------------------");

		HashMap<String, List<String>> validChecksContainer = improvedFindChecks(
				endpointID, propertyID, checkValue, operator, expired);

		List<String> validCheckIDs = validChecksContainer.get("validCheckIDs");
		List<String> triggerRules = new ArrayList<String>();
		triggerRules.addAll(validChecksContainer.get("ruleIDs"));

		/*
		 * Step 2: Update all check entries in VoltDB with the new state "1" for
		 * true and find all Multichecks linked to the Checks that were just
		 * evaluated, return a List Also, reset the state of the multi check to
		 * false as a basis for the next round of checks
		 *

		List<String> validMultiCheckIDs = new ArrayList<String>();
		
		if(validCheckIDs.isEmpty() == false)
		{
			validMultiCheckIDs = markChecksTrueAndGetParentMultiChecks(validCheckIDs);
		}

		/*
		 * Step 3: Evaluate if these Multichecks are true and update multicheck
		 * state accordingly
		 

		if (validMultiCheckIDs.isEmpty() == false)
			triggerRules.addAll(evaluateMultiChecks(validMultiCheckIDs));

		/*
         * Step 4: Find all MultiChecks that are parents to the MultiChecks that
         * were just evaluated, return a List Also, reset the state of the
         * MultiCheck to false as a basis for the next round of checks
         *

        
		List<String> validCycleMultiCheckIDs = new ArrayList<String>();
		
		if(validMultiCheckIDs.isEmpty() == false)
		{
			validCycleMultiCheckIDs = getMultiCheckParents(validMultiCheckIDs);
		}

        /*
         * Step 5: Evaluate if these Multichecks are true and update multicheck
         * state accordingly Run this in a loop until no further parent
         * multichecks can be found
         *

        evaluateCycleMultiChecks(validCycleMultiCheckIDs);
               
                
        while (validCycleMultiCheckIDs.isEmpty() == false) {
                triggerRules
                                .addAll(evaluateCycleMultiChecks(validCycleMultiCheckIDs));
                validCycleMultiCheckIDs = getMultiCheckParents(validCycleMultiCheckIDs);
        }
        */
        return triggerRules;

	}
}