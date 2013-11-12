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

	public static org.voltdb.client.Client montanaClient;

	public static void openDatabase() throws UnknownHostException, IOException {
		/*
		 * Instantiate a client and connect to the database.
		 */

		BusinessLogicEngine.montanaClient = ClientFactory.createClient();
		BusinessLogicEngine.montanaClient
				.createConnection(ServerParameters.volt_ip);
		System.out.println(" [IN] Connected to VoltDB on address: "
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

		final ClientResponse bleChecksForExpression = BusinessLogicEngine.montanaClient
				.callProcedure("BLE_ChecksForExpression", endpointID,
						propertyID, checkValue, expired);

		final VoltTable vtChecksForExpression[] = bleChecksForExpression
				.getResults();

		HashSet<String> validCheckIDs = new HashSet<String>();

		VoltTable vtChecksForExpressionT = vtChecksForExpression[0];

		while (vtChecksForExpressionT.advanceRow()) {

			validCheckIDs.add(vtChecksForExpressionT.getString("CHECKID"));
			triggerRules.addAll(lookupRulesForCheckID(vtChecksForExpressionT
					.getString("CHECKID")));
		}

		// lookup relevant checkpaths that contain the check

		HashSet<String> checkPathMembers = new HashSet<String>();

		Iterator<String> itACI = allCheckIDs.iterator();

		while (itACI.hasNext()) {
			String sTR = itACI.next();

			final ClientResponse bleCheckPathForChecks = BusinessLogicEngine.montanaClient
					.callProcedure("BLE_CheckPathForChecks", sTR);

			final VoltTable vtCheckPathForChecks[] = bleCheckPathForChecks
					.getResults();

			VoltTable vtCheckPathForChecksT = vtCheckPathForChecks[0];

			while (vtCheckPathForChecksT.advanceRow()) {

				String key = vtCheckPathForChecksT.getString("CHECKPATHID");
				// String value = vtCheckPathForChecksT.getString("CHECKID");
				checkPathMembers.add(key);
			}
			
		}
		


		// lookup MultiChecks contained in the checkpaths

		HashSet<String> checkPathMultiCheckMembers = new HashSet<String>();

		Iterator<String> itCPM = checkPathMembers.iterator();

		if (checkPathMembers.isEmpty() == false) {

			while (itCPM.hasNext()) {
				String sTR = itCPM.next();

				final ClientResponse bleCheckPathForMultiChecks = BusinessLogicEngine.montanaClient
						.callProcedure("BLE_CheckPathForMultiChecks", sTR);

				if (bleChecksForExpression.getStatus() != ClientResponse.SUCCESS) {
					System.err
							.println(bleChecksForExpression.getStatusString());

				}

				final VoltTable vtCheckPathForMultiChecks[] = bleCheckPathForMultiChecks
						.getResults();

				VoltTable vtCheckPathForMultiChecksT = vtCheckPathForMultiChecks[0];

				while (vtCheckPathForMultiChecksT.advanceRow()) {

					checkPathMultiCheckMembers.add(vtCheckPathForMultiChecksT
							.getString("MULTICHECKID"));
				}
			}
		}
		
		

		// evaluate first level multichecks (connected to checks) in relevant
		// checkpaths

		HashSet<String> validFirstLevelMultiChecks = new HashSet<String>();

		Iterator<String> itCPMCM = checkPathMultiCheckMembers.iterator();
		while (itCPMCM.hasNext()) {

			String sTR = itCPMCM.next();
			final ClientResponse bleIsMultiCheckTrue = BusinessLogicEngine.montanaClient
					.callProcedure("BLE_IsMultiCheckTrue", sTR);

			if (bleChecksForExpression.getStatus() != ClientResponse.SUCCESS) {
				System.err.println(bleChecksForExpression.getStatusString());

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
		
		

		// evaluate all other multichecks

		HashSet<String> highLevelMultiChecks = new HashSet<String>();
		HashSet<String> moreHighLevelMultiChecks = new HashSet<String>();
		HashSet<String> validHighLevelMultiChecks = new HashSet<String>();

		highLevelMultiChecks = validFirstLevelMultiChecks;

		while (highLevelMultiChecks.isEmpty() == false) {

			for (String sTR : highLevelMultiChecks) {

				final ClientResponse bleMultiCheckParentForMultiCheck = BusinessLogicEngine.montanaClient
						.callProcedure("BLE_MultiCheckParentForMultiCheck", sTR);

				final VoltTable vtMultiCheckParentForMultiCheck[] = bleMultiCheckParentForMultiCheck
						.getResults();

				VoltTable vtMultiCheckParentForMultiCheckT = vtMultiCheckParentForMultiCheck[0];

				moreHighLevelMultiChecks.clear();
				while (vtMultiCheckParentForMultiCheckT.advanceRow()) {

					final ClientResponse bleIsCycleMultiCheckTrue = BusinessLogicEngine.montanaClient
							.callProcedure("BLE_IsCycleMultiCheckTrue",
									vtMultiCheckParentForMultiCheckT
											.getString("MULTICHECKLID"));
					moreHighLevelMultiChecks
							.add(vtMultiCheckParentForMultiCheckT
									.getString("MULTICHECKLID"));

					final VoltTable vtIsCycleMultiCheckTrue[] = bleIsCycleMultiCheckTrue
							.getResults();

					VoltTable vtIsCycleMultiCheckTrueT = vtIsCycleMultiCheckTrue[0];

					while (vtIsCycleMultiCheckTrueT.advanceRow()) {
						validHighLevelMultiChecks.add(vtIsCycleMultiCheckTrueT
								.getString("MULTICHECKID"));
						triggerRules
								.addAll(lookupRulesForMultiCheckID(vtIsCycleMultiCheckTrueT
										.getString("MULTICHECKID")));
					}
				}
				highLevelMultiChecks = moreHighLevelMultiChecks;
			}

		}

		return triggerRules;

	}
}