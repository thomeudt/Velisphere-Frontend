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

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
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
	
	

	public static void resetChecksForEndpoint(String endpointID)
			throws Exception {

		/*
		 * Reset all checks for this endpoint type to false to prepare
		 * for new incoming data
		 */
		
		final ClientResponse resetResponse = BusinessLogicEngine.montanaClient.callProcedure(
				"ResetChecks", endpointID);
		if (resetResponse.getStatus() != ClientResponse.SUCCESS) {
			System.err.println(resetResponse.getStatusString());}

		}

	
	public static HashSet<String> lookupRulesForCheckID(String checkID)
			throws NoConnectionsException, IOException, ProcCallException {

		HashSet<String> ruleIDs = new HashSet<String>();

		final ClientResponse findRulesForCheckIDResponse = BusinessLogicEngine.montanaClient
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
				// System.out.println(result.getString("RULEID"));
			}
		}

		return ruleIDs;
	}

	public static HashSet<String> lookupRulesForMultiCheckID(String multiCheckID)
			throws NoConnectionsException, IOException, ProcCallException {

		HashSet<String> ruleIDs = new HashSet<String>();

		final ClientResponse findRulesForMultiCheckIDResponse = BusinessLogicEngine.montanaClient
				.callProcedure("FindRulesForMultiCheckID", multiCheckID);
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
				ruleIDs.add(result.getString("RULEID"));
				// System.out.println(result.getString("RULEID"));
			}
		}

		return ruleIDs;

	}


	
	
	public static HashSet<String> runChecks(String endpointID,
			String propertyID, String checkValue, byte expired)
			throws Exception {

		
		HashSet<String> triggerRules = new HashSet<String>();

		// find checks that match the incoming expression

		final ClientResponse bleAllChecksForExpression = BusinessLogicEngine.montanaClient
				.callProcedure("BLE_AllChecksForExpression", endpointID,
						propertyID);

		final VoltTable vtAllChecksForExpression[] = bleAllChecksForExpression
				.getResults();

		HashSet<String> allCheckIDs = new HashSet<String>();

		VoltTable vtAllChecksForExpressionT = vtAllChecksForExpression[0];

		while (vtAllChecksForExpressionT.advanceRow()) {

			allCheckIDs.add(vtAllChecksForExpressionT.getString("CHECKID"));

		}

		// find checks that match the incoming expression and mark as true

		HashSet<String> checkPathMembers = new HashSet<String>();
		
		final ClientResponse bleChecksForExpression = BusinessLogicEngine.montanaClient
				.callProcedure("BLE_ChecksForExpression", endpointID,
						propertyID, checkValue, expired);

		final VoltTable vtChecksForExpression[] = bleChecksForExpression
				.getResults();

		//HashSet<String> validCheckIDs = new HashSet<String>();

		VoltTable vtChecksForExpressionT = vtChecksForExpression[0];

		while (vtChecksForExpressionT.advanceRow()) {

			//validCheckIDs.add(vtChecksForExpressionT.getString("CHECKID"));
			checkPathMembers.add(vtChecksForExpressionT.getString("CHECKPATHID"));
			
			// start multichecks here
			
			triggerRules.addAll(lookupRulesForCheckID(vtChecksForExpressionT
					.getString("CHECKID")));
						
		}

		Iterator<String> cpID = checkPathMembers.iterator();

		//System.out.println(checkPathMembers);
		
		while (cpID.hasNext())
		{
		triggerRules.addAll(firstLevelMultiChecks(cpID.next()));
		}
		
		return triggerRules;

	}
	
	
	
	
				
		
	

private static HashSet<String> firstLevelMultiChecks (String checkpathID) throws NoConnectionsException, IOException, ProcCallException
	{
	
	HashSet<String> triggerRules = new HashSet<String>();
	
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
			triggerRules
			.addAll(lookupRulesForMultiCheckID(vtIsMultiCheckTrueT
					.getString("MULTICHECKID")));
					
		}
				
	}
	
	triggerRules.addAll(recursionMultichecks(checkpathID, validFirstLevelMultiChecks));
	
	return triggerRules;
}
	

	private static HashSet<String> recursionMultichecks (String checkpathID, HashSet<String> validMultiChecks) throws NoConnectionsException, IOException, ProcCallException{
	
	HashSet<String> triggerRules = new HashSet<String>();
		
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
				recursionMultichecks(checkpathID, moreHighLevelMultiChecks);

				final VoltTable vtIsCycleMultiCheckTrue[] = bleIsCycleMultiCheckTrue
						.getResults();

				VoltTable vtIsCycleMultiCheckTrueT = vtIsCycleMultiCheckTrue[0];

				while (vtIsCycleMultiCheckTrueT.advanceRow()) {
			
					triggerRules
							.addAll(lookupRulesForMultiCheckID(vtIsCycleMultiCheckTrueT
									.getString("MULTICHECKID")));
				}
			}
			
		}
	
	return triggerRules;
	}

	
}