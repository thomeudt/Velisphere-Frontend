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
 * 
 */

import org.voltdb.VoltTable;
import org.voltdb.client.*;

import com.velisphere.chai.dataObjects.BLEResultObject;
import com.velisphere.chai.dataObjects.CheckObject;
import com.velisphere.chai.dataObjects.MulticheckObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

public class BusinessLogicEngine {
	
	//private static HashSet<String> triggerRules = new HashSet<String>();

	public static org.voltdb.client.Client montanaClient;

	public static void openDatabase() throws UnknownHostException, IOException {
		/*
		 * Instantiate a client and connect to the database.
		 */

		BusinessLogicEngine.montanaClient = ClientFactory.createClient();
		BusinessLogicEngine.montanaClient
				.createConnection(ServerParameters.volt_ip);
		System.out.println(" [OK] Connected to VoltDB on address: "
		+ ServerParameters.volt_ip);
	}

	public static void writeLog(String exchangeName, String message,
			String queueName, String routingKey) throws Exception {

		/*
		 * Write log to database.
		 */

		UUID identifier = UUID.randomUUID();
		String identifierString = identifier.toString();

		// TODO: change to a meaningful log table!

		BusinessLogicEngine.montanaClient.callProcedure("Insert", "1", message,
				queueName, "1", identifierString);

	}
	
	
/**
	public static void resetChecksForEndpoint(String endpointID)
			throws Exception {

		/*
		 * Reset all checks for this endpoint type to false to prepare
		 * for new incoming data
		 *
		
		final ClientResponse resetResponse = BusinessLogicEngine.montanaClient.callProcedure(
				"ResetChecks", endpointID);
		if (resetResponse.getStatus() != ClientResponse.SUCCESS) {
			System.err.println(resetResponse.getStatusString());}

		}
**/
	
	public static HashMap<String, String> lookupActionsForCheckID(String checkID, String checkPathID)
			throws NoConnectionsException, IOException, ProcCallException {

		HashMap<String, String> actionIDs = new HashMap<String, String>();
		
		final ClientResponse findRulesForCheckIDResponse = BusinessLogicEngine.montanaClient
				.callProcedure("BLE_FindActionsForCheckID", checkPathID, checkID);
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
				actionIDs.put(result.getString("ACTIONID"), checkPathID);
				
				
				// System.out.println(result.getString("RULEID"));
			}
		}

		return actionIDs;
	}

	public static HashMap<String, String> lookupActionsForMultiCheckID(String multiCheckID, String checkPathID)
			throws NoConnectionsException, IOException, ProcCallException {

		HashMap<String, String> actionIDs = new HashMap<String, String>();

		final ClientResponse findRulesForMultiCheckIDResponse = BusinessLogicEngine.montanaClient
				.callProcedure("BLE_FindActionsForMultiCheckID", checkPathID, multiCheckID);
		if (findRulesForMultiCheckIDResponse.getStatus() != ClientResponse.SUCCESS) {
			System.err.println(findRulesForMultiCheckIDResponse
					.getStatusString());
		}
		final VoltTable findRulesForMultiCheckIDResults[] = findRulesForMultiCheckIDResponse
				.getResults();
		if (findRulesForMultiCheckIDResults.length == 0) {
			// System.out.printf("Not valid match found!\n");
		}

		VoltTable result = findRulesForMultiCheckIDResults[0];
		// check if any rows have been returned
		while (result.advanceRow()) {
			{
				// extract the value in column checkid
				actionIDs.put(result.getString("ACTIONID"), checkPathID);
				// System.out.println(result.getString("RULEID"));
			}
		}

		return actionIDs;

	}


	
	
	public static BLEResultObject runChecks(String endpointID,
			String propertyID, String checkValue, byte expired)
			throws Exception {

		

		// collect data for later processing and logging
		
		HashMap<String, String> triggerActions = new HashMap<String, String>();
		LinkedList<CheckObject> allChecks = new LinkedList<CheckObject>();
		LinkedList<MulticheckObject> allMultichecks = new LinkedList<MulticheckObject>();


		// find checks that match the incoming expression 

		HashSet<String> checkPathForCheck = new HashSet<String>();
		HashSet<String> trueCheckIDs = new HashSet<String>();
		
		
		final ClientResponse bleChecksForExpression = BusinessLogicEngine.montanaClient
				.callProcedure("BLE_ChecksForExpression", endpointID,
						propertyID, checkValue, expired);

		final VoltTable vtChecksForExpression[] = bleChecksForExpression
				.getResults();

		

		// True checks retrieved first
		VoltTable vtTrueChecksForExpressionT = vtChecksForExpression[0];
		while (vtTrueChecksForExpressionT.advanceRow()) {
			trueCheckIDs.add(vtTrueChecksForExpressionT.getString("CHECKID"));
			ClientResponse bleUpdateCheckState = BusinessLogicEngine.montanaClient
					.callProcedure("BLE_UpdateCheckState", vtTrueChecksForExpressionT.getString("CHECKPATHID"), vtTrueChecksForExpressionT.getString("CHECKID"), 1);
			checkPathForCheck.add(vtTrueChecksForExpressionT.getString("CHECKPATHID"));
			triggerActions.putAll(lookupActionsForCheckID(vtTrueChecksForExpressionT.getString("CHECKID"), vtTrueChecksForExpressionT.getString("CHECKPATHID")));
			CheckObject check = new CheckObject(vtTrueChecksForExpressionT.getString("CHECKID"), checkValue, true);
			allChecks.add(check);
		}
		
		
		
		// Now false checks
		
		
		
		VoltTable vtFalseChecksForExpressionT = vtChecksForExpression[1];
	
		//System.out.println(vtFalseChecksForExpressionT);
		
		while (vtFalseChecksForExpressionT.advanceRow()) {
			
			ClientResponse bleUpdateCheckState = BusinessLogicEngine.montanaClient
					.callProcedure("BLE_UpdateCheckState", vtFalseChecksForExpressionT.getString("CHECKPATHID"), vtFalseChecksForExpressionT.getString("CHECKID"), 0);
			checkPathForCheck.add(vtFalseChecksForExpressionT.getString("CHECKPATHID"));
			CheckObject check = new CheckObject(vtFalseChecksForExpressionT.getString("CHECKID"), checkValue, false);
			allChecks.add(check);
		}
		
		// find rules for all true checks
		
		Iterator<String> cpID = checkPathForCheck.iterator();
		
		
		/**
		 * from here it should be good
		 */
		
		
		//System.out.println("CHECKPATH: " + checkPathForCheck);
		
		while (cpID.hasNext())
		{
		triggerActions.putAll(firstLevelMultiChecks(cpID.next()));
		}
		
		BLEResultObject bleResult = new BLEResultObject(triggerActions, allChecks, null);
		
		return bleResult;

	}
	
	
	
	
				
		
	

private static HashMap<String, String> firstLevelMultiChecks (String checkpathID) throws NoConnectionsException, IOException, ProcCallException
	{
	
	HashMap<String, String> triggerActions = new HashMap<String, String>();
	
	HashSet<String> checkPathMultiCheckMembers = new HashSet<String>();
	
	// find multichecks in checkpath
	
	final ClientResponse bleCheckPathForMultiChecks = BusinessLogicEngine.montanaClient
			.callProcedure("BLE_CheckPathForMultiChecks", checkpathID);

	if (bleCheckPathForMultiChecks.getStatus() != ClientResponse.SUCCESS) {
	System.err
		.println(bleCheckPathForMultiChecks.getStatusString());

	}

	final VoltTable vtCheckPathForMultiChecks[] = bleCheckPathForMultiChecks
			.getResults();

	VoltTable vtCheckPathForMultiChecksT = vtCheckPathForMultiChecks[0];

	while (vtCheckPathForMultiChecksT.advanceRow()) {

		checkPathMultiCheckMembers.add(vtCheckPathForMultiChecksT
				.getString("MULTICHECKID"));
	}

	
	// evaluate the multichecks in checkpath
	
	
	HashSet<String> validFirstLevelMultiChecks = new HashSet<String>();

	//System.out.println("CPMCM " + checkPathMultiCheckMembers);
			

	//System.out.println("MULTICHECKS IN CHECKPATH: " + checkPathMultiCheckMembers);
	
	Iterator<String> itCPMCM = checkPathMultiCheckMembers.iterator();
	while (itCPMCM.hasNext()) {

		String sTR = itCPMCM.next();
		//System.out.println("Looking up multicheck: " + sTR);
		
		final ClientResponse bleIsMultiCheckTrue = BusinessLogicEngine.montanaClient
				.callProcedure("BLE_IsMultiCheckTrue", checkpathID, sTR);

		if (bleIsMultiCheckTrue.getStatus() != ClientResponse.SUCCESS) {
			System.err.println(bleIsMultiCheckTrue.getStatusString());

		}

		final VoltTable vtIsMultiCheckTrue[] = bleIsMultiCheckTrue
				.getResults();

		VoltTable vtIsMultiCheckTrueT = vtIsMultiCheckTrue[0];

		while (vtIsMultiCheckTrueT.advanceRow()) {
			
			validFirstLevelMultiChecks.add(vtIsMultiCheckTrueT
					.getString("MULTICHECKID"));
			triggerActions
			.putAll(lookupActionsForMultiCheckID(vtIsMultiCheckTrueT
					.getString("MULTICHECKID"), checkpathID));
					
		}
				
	}
	
	triggerActions.putAll(recursionMultichecks(checkpathID, validFirstLevelMultiChecks));
	
	return triggerActions;
}
	

	private static HashMap<String, String> recursionMultichecks (String checkpathID, HashSet<String> validMultiChecks) throws NoConnectionsException, IOException, ProcCallException{
	
	HashMap<String, String> triggerActions = new HashMap<String, String>();
		
	HashSet<String> highLevelMultiChecks = new HashSet<String>();
	HashSet<String> moreHighLevelMultiChecks = new HashSet<String>();
		
	highLevelMultiChecks = validMultiChecks;
	
	for (String sTR : highLevelMultiChecks) {

			final ClientResponse bleMultiCheckParentForMultiCheck = BusinessLogicEngine.montanaClient
					.callProcedure("BLE_MultiCheckParentForMultiCheck", checkpathID, sTR);

			final VoltTable vtMultiCheckParentForMultiCheck[] = bleMultiCheckParentForMultiCheck
					.getResults();

			VoltTable vtMultiCheckParentForMultiCheckT = vtMultiCheckParentForMultiCheck[0];

			moreHighLevelMultiChecks.clear();
			while (vtMultiCheckParentForMultiCheckT.advanceRow()) {

				final ClientResponse bleIsCycleMultiCheckTrue = BusinessLogicEngine.montanaClient
						.callProcedure("BLE_IsCycleMultiCheckTrue", checkpathID, 
								vtMultiCheckParentForMultiCheckT
										.getString("MULTICHECKLID"));
				moreHighLevelMultiChecks
						.add(vtMultiCheckParentForMultiCheckT
								.getString("MULTICHECKLID"));
				
				//System.out.println("MORE HIGHLEVEL CHECKS: " + moreHighLevelMultiChecks);
				
				recursionMultichecks(checkpathID, moreHighLevelMultiChecks);

				final VoltTable vtIsCycleMultiCheckTrue[] = bleIsCycleMultiCheckTrue
						.getResults();

				VoltTable vtIsCycleMultiCheckTrueT = vtIsCycleMultiCheckTrue[0];

				while (vtIsCycleMultiCheckTrueT.advanceRow()) {
			
					triggerActions
							.putAll(lookupActionsForMultiCheckID(vtIsCycleMultiCheckTrueT
									.getString("MULTICHECKID"), checkpathID));
				}
			}
			
		}
	
	return triggerActions;
	}


		
}