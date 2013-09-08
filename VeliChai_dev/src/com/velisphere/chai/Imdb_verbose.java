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
 * - put various steps into proper methods
 * - cycle last two steps as long as they continues to yield results (more parent multichecks found)
 * - lookup rules in every evaluation step - rules can be attached to a check or any multicheck * 
 */

import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.client.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Imdb_verbose {

	public static org.voltdb.client.Client montanaClient;

	public static void openDatabase() throws UnknownHostException, IOException {
		/*
		 * Instantiate a client and connect to the database.
		 */

		Imdb_verbose.montanaClient = ClientFactory.createClient();
		Imdb_verbose.montanaClient.createConnection(ServerParameters.volt_ip);
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

		Imdb_verbose.montanaClient.callProcedure("Insert", "1", message, queueName,
				"1", identifierString);

	}

	public static void resetChecksForEndpoint(String endpointID)
			throws Exception {

		/*
		 * Step 1: Reset all checks for this endpoint type to false to prepare
		 * for new incoming data
		 */

		final ClientResponse resetResponse = Imdb_verbose.montanaClient.callProcedure(
				"ResetChecks", endpointID);
		if (resetResponse.getStatus() != ClientResponse.SUCCESS) {
			System.err.println(resetResponse.getStatusString());

		}

	}

	static List<String> findChecks(String endpointID, String propertyID,
			String checkValue, String operator, byte expired)
			throws NoConnectionsException, IOException, ProcCallException {

		/*
		 * Query all checks matching the data in terms of endPointID,
		 * propertyID, CheckValue etc.
		 */

		final ClientResponse selectResponse = Imdb_verbose.montanaClient.callProcedure(
				"FindMatchingChecksEqual", endpointID, propertyID, checkValue,
				operator, expired);

		// Check if there was a proper resonse from Volt

		if (selectResponse.getStatus() != ClientResponse.SUCCESS) {
			System.err.println(selectResponse.getStatusString());

		}

		// if no matching response was found, send a console message. needs to
		// be removed

		final VoltTable results[] = selectResponse.getResults();
		if (results.length == 0) {
			System.out.printf("Not valid match found!\n");
		}

		List<String> validCheckIDs = new ArrayList<String>();

		// now all checkids that met the search criteria get filled into an
		// array list for later use

		for (VoltTable result : results) {
			// check if any rows have been returned
			if (result.getRowCount() > 0)
				for (int i = 0; i < result.getRowCount(); i++) {
					{

						// get the row
						VoltTableRow row = result.fetchRow(i);
						// extract the value in column checkid
						validCheckIDs.add(row.getString("CHECKID"));
						System.out.println("*** VALID CHECK FOUND: "
								+ row.getString("CHECKID"));
					}
				}
		}

		return validCheckIDs;

		// ADD CHECK IF CHECK IS REFERENCED IN A RULE --> TRIGGER ACTION

	}

	static List<String> markChecksTrueAndGetParentMultiChecks(
			List<String> validCheckIDs) throws NoConnectionsException,
			IOException, ProcCallException {

		/*
		 * Update all check entries in VoltDB with the new state "1" for true
		 * and find all Multichecks linked to the Checks that were just
		 * evaluated, return a List Also, reset the state of the multi check to
		 * false as a basis for the next round of checks
		 */

		List<String> validMultiCheckIDs = new ArrayList<String>();

		for (String checkID : validCheckIDs) {
			final ClientResponse updateResponse = Imdb_verbose.montanaClient
					.callProcedure("UpdateChecks", 1, checkID);
			if (updateResponse.getStatus() != ClientResponse.SUCCESS) {
				System.err.println(updateResponse.getStatusString());
			}
			final ClientResponse findMulticheckResponse = Imdb_verbose.montanaClient
					.callProcedure("FindAllMultichecksForCheck", checkID);
			if (findMulticheckResponse.getStatus() != ClientResponse.SUCCESS) {
				System.err.println(findMulticheckResponse.getStatusString());
			}
			final VoltTable findMulticheckResults[] = findMulticheckResponse
					.getResults();
			if (findMulticheckResults.length == 0) {
				System.out.printf("Not valid match found!\n");
			}

			for (VoltTable findMulticheckResult : findMulticheckResults) {
				// check if any rows have been returned
				if (findMulticheckResult.getRowCount() > 0) {
					for (int i = 0; i < findMulticheckResult.getRowCount(); i++) {
						// get the row
						VoltTableRow row = findMulticheckResult.fetchRow(i);
						// extract the value in column checkid
						validMultiCheckIDs.add(row.getString("MULTICHECKID"));
						System.out.println("MULTICHECK FOUND: "
								+ row.getString("MULTICHECKID"));
						// reset the multicheck state
						Imdb_verbose.montanaClient.callProcedure("UpdateMultiChecks",
								0, row.getString("MULTICHECKID"));
					}
				}
			}

		}
		return validMultiCheckIDs;

	}

	static void evaluateMultiChecks(List<String> validMultiCheckIDs)
			throws NoConnectionsException, IOException, ProcCallException {

		/*
		 * Evaluate if these Multichecks are true and update multicheck state
		 * accordingly
		 */

		for (String multicheckID : validMultiCheckIDs) {

			// to this list we add all the check states that are part of the
			// multicheck. if all entries are true, a logical AND is met, if any
			// entry is true, a logical OR is met
			List<Boolean> checkStates = new ArrayList<Boolean>();

			// Query the Multichecks

			final ClientResponse findCheckForMulticheckResponse = Imdb_verbose.montanaClient
					.callProcedure("FindChecksForMultiCheckID", multicheckID);

			if (findCheckForMulticheckResponse.getStatus() != ClientResponse.SUCCESS) {
				System.err.println(findCheckForMulticheckResponse
						.getStatusString());
			}

			final VoltTable findCheckForMulticheckResults[] = findCheckForMulticheckResponse
					.getResults();
			if (findCheckForMulticheckResults.length == 0) {
				System.out.printf("Not valid match found!\n");
			}

			// Query all the checks that define the Multichecks

			List<String> checkIDsMatchingMultiCheck = new ArrayList<String>();

			for (VoltTable findCheckForMulticheckResult : findCheckForMulticheckResults) {
				// check if any rows have been returned
				if (findCheckForMulticheckResult.getRowCount() > 0) {
					for (int i = 0; i < findCheckForMulticheckResult
							.getRowCount(); i++) {
						// get the row
						VoltTableRow row = findCheckForMulticheckResult
								.fetchRow(i);
						// extract the value in column checkid
						checkIDsMatchingMultiCheck
								.add(row.getString("CHECKID"));
						System.out.println("ATTCHED CHECKS FOUND: "
								+ row.getString("CHECKID"));

						// evaluate each check linked to the multicheck whether
						// it is true or not

						final ClientResponse findCheckStateResponse = Imdb_verbose.montanaClient
								.callProcedure("FindChecksForCheckID",
										row.getString("CHECKID"));
						final VoltTable findCheckStateResults[] = findCheckStateResponse
								.getResults();

						for (VoltTable findCheckStateResult : findCheckStateResults) {
							// check if any rows have been returned
							if (findCheckStateResult.getRowCount() > 0) {
								for (int j = 0; j < findCheckStateResult
										.getRowCount(); j++) {
									VoltTableRow checkRow = findCheckStateResult
											.fetchRow(j);
									if (checkRow.getLong("STATE") == 1) {
										System.out.println("STATE: TRUE");
										checkStates.add(true);
									} else {
										System.out.println("STATE: FALSE");
										checkStates.add(false);
									}
								}
							}
						}
					}
				}

				// here we do the evaluation based on the operator

				boolean multiCheckState = false;

				// first, we look up the operator
				final ClientResponse findMultiCheckOperatorResponse = Imdb_verbose.montanaClient
						.callProcedure("FindMultiChecksForMultiCheckID",
								multicheckID);
				final VoltTable findMultiCheckOperatorResults[] = findMultiCheckOperatorResponse
						.getResults();
				VoltTableRow multiCheckOperatorRow = findMultiCheckOperatorResults[0]
						.fetchRow(0);
				String multiCheckOperator = multiCheckOperatorRow
						.getString("OPERATOR");

				if (checkStates.contains(true)
						&& multiCheckOperator.equals("OR")) {
					multiCheckState = true;
					Imdb_verbose.montanaClient.callProcedure("UpdateMultiChecks", 1,
							multicheckID);
					System.out.println("*** Multicheck Eval OR Result: "
							+ multiCheckState);
				}

				if ((checkStates.contains(true) == true && checkStates
						.contains(false) == false)
						&& multiCheckOperator.equals("AND")) {
					multiCheckState = true;
					Imdb_verbose.montanaClient.callProcedure("UpdateMultiChecks", 1,
							multicheckID);
					System.out
							.println("*** Multicheck Eval AND Result for MultiCheck "
									+ multicheckID + ": " + multiCheckState);
				}

			}
		}

		// ADD CHECK IF MULTICHECK IS REFERENCED IN A RULE --> TRIGGER ACTION

	}

	static List<String> getMultiCheckParents(List<String> validMultiCheckIDs)
			throws NoConnectionsException, IOException, ProcCallException {
		/*
		 * Find all MultiChecks that are parents to the MultiChecks that were
		 * just evaluated, return a List Also, reset the state of the MultiCheck
		 * to false as a basis for the next round of checks
		 */

		List<String> validCycleMultiCheckIDs = new ArrayList<String>();

		for (String multiCheckID : validMultiCheckIDs) {
			System.out.println("Cycling for Multicheck: " + multiCheckID);

			final ClientResponse findMulticheckResponse = Imdb_verbose.montanaClient
					.callProcedure("FindParentMultiChecksForMultiCheckID",
							multiCheckID);
			if (findMulticheckResponse.getStatus() != ClientResponse.SUCCESS) {
				System.err.println(findMulticheckResponse.getStatusString());
			}
			final VoltTable findMulticheckResults[] = findMulticheckResponse
					.getResults();
			if (findMulticheckResults.length == 0) {
				System.out.printf("Not valid match found!\n");
			}

			for (VoltTable findMulticheckResult : findMulticheckResults) {
				// check if any rows have been returned
				if (findMulticheckResult.getRowCount() > 0) {
					for (int i = 0; i < findMulticheckResult.getRowCount(); i++) {
						// get the row
						VoltTableRow row = findMulticheckResult.fetchRow(i);
						// extract the value in column checkid
						validCycleMultiCheckIDs.add(row
								.getString("MULTICHECKLID"));
						System.out.println("CYCLICAL MULTICHECK FOUND: "
								+ row.getString("MULTICHECKLID"));
						// reset the multicheck state
						Imdb_verbose.montanaClient.callProcedure("UpdateMultiChecks",
								0, row.getString("MULTICHECKLID"));
					}
				}
			}

		}
		return validCycleMultiCheckIDs;
	}

	static void evaluateCycleMultiChecks(List<String> validCycleMultiCheckIDs)
			throws NoConnectionsException, IOException, ProcCallException {

		/*
		 * Evaluate if these Multichecks are true and update multicheck state
		 * accordingly
		 */

		for (String multicheckID : validCycleMultiCheckIDs) {

			// to this list we add all the check states that are part of the
			// multicheck. if all entries are true, a logical AND is met, if any
			// entry is true, a logical OR is met
			List<Boolean> multiCheckStates = new ArrayList<Boolean>();

			final ClientResponse findMultiCheckForMulticheckResponse = Imdb_verbose.montanaClient
					.callProcedure("FindLinkedMultiChecksForMultiCheckID",
							multicheckID);

			if (findMultiCheckForMulticheckResponse.getStatus() != ClientResponse.SUCCESS) {
				System.err.println(findMultiCheckForMulticheckResponse
						.getStatusString());
			}

			final VoltTable findMultiCheckForMulticheckResults[] = findMultiCheckForMulticheckResponse
					.getResults();
			if (findMultiCheckForMulticheckResults.length == 0) {
				System.out.printf("Not valid match found!\n");
			}

			// Query all the Multichecks that define the Multichecks

			List<String> multiCheckIDsMatchingMultiCheck = new ArrayList<String>();

			for (VoltTable findMultiCheckForMulticheckResult : findMultiCheckForMulticheckResults) {
				// check if any rows have been returned
				if (findMultiCheckForMulticheckResult.getRowCount() > 0) {
					for (int i = 0; i < findMultiCheckForMulticheckResult
							.getRowCount(); i++) {
						// get the row
						VoltTableRow row = findMultiCheckForMulticheckResult
								.fetchRow(i);
						// extract the value in column checkid
						multiCheckIDsMatchingMultiCheck.add(row
								.getString("MULTICHECKRID"));
						System.out.println("ATTCHED MULTICHECKS FOUND: "
								+ row.getString("MULTICHECKRID"));

						// evaluate each check linked to the multicheck whether
						// it is true or not

						final ClientResponse findMultiCheckStateResponse = Imdb_verbose.montanaClient
								.callProcedure(
										"FindMultiChecksForMultiCheckID",
										row.getString("MULTICHECKRID"));
						final VoltTable findMultiCheckStateResults[] = findMultiCheckStateResponse
								.getResults();

						for (VoltTable findMultiCheckStateResult : findMultiCheckStateResults) {
							// check if any rows have been returned
							if (findMultiCheckStateResult.getRowCount() > 0) {
								for (int j = 0; j < findMultiCheckStateResult
										.getRowCount(); j++) {
									VoltTableRow checkRow = findMultiCheckStateResult
											.fetchRow(j);
									if (checkRow.getLong("STATE") == 1) {
										System.out
												.println("MULTICHECKSTATE: TRUE");
										multiCheckStates.add(true);
									} else {
										System.out
												.println("MULTICHECKSTATE: FALSE");
										multiCheckStates.add(false);
									}
								}
							}
						}
					}
				}

				// here we do the evaluation based on the operator

				boolean multiCheckState = false;

				// first, we look up the operator
				final ClientResponse findMultiCheckOperatorResponse = Imdb_verbose.montanaClient
						.callProcedure("FindMultiChecksForMultiCheckID",
								multicheckID);
				final VoltTable findMultiCheckOperatorResults[] = findMultiCheckOperatorResponse
						.getResults();
				VoltTableRow multiCheckOperatorRow = findMultiCheckOperatorResults[0]
						.fetchRow(0);
				String multiCheckOperator = multiCheckOperatorRow
						.getString("OPERATOR");

				if (multiCheckStates.contains(true)
						&& multiCheckOperator.equals("OR")) {
					multiCheckState = true;
					Imdb_verbose.montanaClient.callProcedure("UpdateMultiChecks", 1,
							multicheckID);
					System.out.println("*** Cyclical Multicheck Eval OR Result: "
							+ multiCheckState);
				}

				if ((multiCheckStates.contains(true) == true && multiCheckStates
						.contains(false) == false)
						&& multiCheckOperator.equals("AND")) {
					multiCheckState = true;
					Imdb_verbose.montanaClient.callProcedure("UpdateMultiChecks", 1,
							multicheckID);
					System.out
							.println("*** Cyclical Multicheck Eval AND Result for MultiCheck "
									+ multicheckID + ": " + multiCheckState);
				}

			}
		}
	}

	public static void runChecks(String endpointID, String propertyID,
			String checkValue, String operator, byte expired) throws Exception {

		/*
		 * Run the lowest level check engine
		 */

		/*
		 * Step 1: Query all checks matching the data in terms of endPointID,
		 * propertyID, CheckValue etc.
		 */

		System.out.println("------------------NEW IMDB CHECK---------------------------");
		System.out.println("Endpoint:    " + endpointID);
		System.out.println("Property:    " + propertyID);
		System.out.println("Checkvalue   " + checkValue);
		System.out.println("-----------------------------------------------------------");
		
		List<String> validCheckIDs = findChecks(endpointID, propertyID,
				checkValue, operator, expired);

		/*
		 * Step 2: Update all check entries in VoltDB with the new state "1" for
		 * true and find all Multichecks linked to the Checks that were just
		 * evaluated, return a List Also, reset the state of the multi check to
		 * false as a basis for the next round of checks
		 */

		List<String> validMultiCheckIDs = markChecksTrueAndGetParentMultiChecks(validCheckIDs);

		/*
		 * Step 3: Evaluate if these Multichecks are true and update multicheck
		 * state accordingly
		 */

		if (validMultiCheckIDs.isEmpty() == false)
			evaluateMultiChecks(validMultiCheckIDs);

		/*
		 * Step 4: Find all MultiChecks that are parents to the MultiChecks that
		 * were just evaluated, return a List Also, reset the state of the
		 * MultiCheck to false as a basis for the next round of checks
		 */

		List<String> validCycleMultiCheckIDs = getMultiCheckParents(validMultiCheckIDs);

		/*
		 * Step 5: Evaluate if these Multichecks are true and update multicheck
		 * state accordingly Run this in a loop until no further parent
		 * multichecks can be found
		 */

		evaluateCycleMultiChecks(validCycleMultiCheckIDs);

		while (validCycleMultiCheckIDs.isEmpty() == false) {
			evaluateCycleMultiChecks(validCycleMultiCheckIDs);
			validCycleMultiCheckIDs = getMultiCheckParents(validCycleMultiCheckIDs);
		}

	}

	public static void runCheckParentMulticheck(String checkID) {
		// Evaluate first in line Multicheck
	}

	public static void runMultiCheckParentMulticheck(String checkID) {
		// Evaluate subsequent Multichecks
	}

}
