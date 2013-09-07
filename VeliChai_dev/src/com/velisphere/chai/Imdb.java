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

import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.client.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Imdb {

	public static org.voltdb.client.Client montanaClient;

	public static void openDatabase() throws UnknownHostException, IOException{
		/*
		 * Instantiate a client and connect to the database.
		 */

		Imdb.montanaClient = ClientFactory.createClient();
		Imdb.montanaClient.createConnection(ServerParameters.volt_ip);
		System.out.println(" [IN] Connected to VoltDB on address: " + ServerParameters.volt_ip);
	}

	public static void writeLog(String exchangeName, String message, String queueName, String routingKey) throws Exception {

		/*
		 * Write log to database.
		 */

		UUID identifier = UUID.randomUUID();
		String identifierString = identifier.toString();

		// TODO: change to a meaningful log table!

		Imdb.montanaClient.callProcedure("Insert", "1", message, queueName, "1", identifierString);

	}


	public static void resetChecksForEndpoint(String endpointID) throws Exception {


		/*
		 * Step 1: Reset all checks for this endpoint type to false to prepare for new incoming data
		 * 
		 */

		final ClientResponse resetResponse = Imdb.montanaClient.callProcedure("ResetChecks", endpointID);
		if (resetResponse.getStatus() != ClientResponse.SUCCESS){
			System.err.println(resetResponse.getStatusString());

		}

	}	


	public static void runChecks(String endpointID, String propertyID, String checkValue, String operator, byte expired) throws Exception {

		/*
		 * Run the lowest level check engine
		 */



		/*
		 * Step 2: Query all checks matching the data in terms of endPointID, propertyID, CheckValue etc.
		 * 
		 */

		final ClientResponse selectResponse = Imdb.montanaClient.callProcedure("FindMatchingChecksEqual", endpointID, propertyID, checkValue, operator, expired);

		// Check if there was a proper resonse from Volt

		if (selectResponse.getStatus() != ClientResponse.SUCCESS){
			System.err.println(selectResponse.getStatusString());

		}

		// if no matching response was found, send a console message. needs to be removed

		final VoltTable results[] = selectResponse.getResults();
		if (results.length == 0) {
			System.out.printf("Not valid match found!\n");
		}

		List<String> validCheckIDs = new ArrayList<String>();

		// now all checkids that met the search criteria get filled into an array list for later use


		for (VoltTable result : results) 
		{
			// check if any rows have been returned 
			if (result.getRowCount() > 0)
				for (int i = 0; i < result.getRowCount(); i++)
				{
					{

						// get the row
						VoltTableRow row = result.fetchRow(i);
						// extract the value in column checkid
						validCheckIDs.add(row.getString("CHECKID"));
						System.out.println("CHECKID FOUND: " + row.getString("CHECKID"));
					}
				}
		}


		/*
		 * Step 3: Update all check entries in VoltDB with the new state "1" for true
		 * and find all Multichecks linked to the Checks that were just evaluated, return a List
		 * Also, reset the state of the multi check to false as a basis for the next round of checks
		 * 
		 */


		List<String> validMultiCheckIDs = new ArrayList<String>();

		for (String checkID : validCheckIDs)
		{
			final ClientResponse updateResponse = Imdb.montanaClient.callProcedure("UpdateChecks", 1, checkID);
			if (updateResponse.getStatus() != ClientResponse.SUCCESS){
				System.err.println(updateResponse.getStatusString());
			}
			final ClientResponse findMulticheckResponse = Imdb.montanaClient.callProcedure("FindAllMultichecksForCheck", checkID);
			if (findMulticheckResponse.getStatus() != ClientResponse.SUCCESS){
				System.err.println(findMulticheckResponse.getStatusString());
			}
			final VoltTable findMulticheckResults[] = findMulticheckResponse.getResults();
			if (findMulticheckResults.length == 0) {
				System.out.printf("Not valid match found!\n");
			}

			for (VoltTable findMulticheckResult : findMulticheckResults) 
			{
				// check if any rows have been returned 
				if (findMulticheckResult.getRowCount() > 0)
				{
					for (int i = 0; i < findMulticheckResult.getRowCount(); i++)
					{
						// get the row
						VoltTableRow row = findMulticheckResult.fetchRow(i);
						// extract the value in column checkid
						validMultiCheckIDs.add(row.getString("MULTICHECKID"));
						System.out.println("MULTICHECK FOUND: " + row.getString("MULTICHECKID"));
						// reset the multicheck state
						Imdb.montanaClient.callProcedure("UpdateMultiChecks", 0, row.getString("MULTICHECKID"));
					}
				}
			}

		}


		/*
		 * Step 5: Evaluate if these Multichecks are true and update multicheck state accordingly
		 */


		for (String multicheckID : validMultiCheckIDs)
		{

			// to this list we add all the check states that are part of the multicheck. if all entries are true, a logical AND is met, if any entry is true, a logical OR is met
			List<Boolean> checkStates = new ArrayList<Boolean>(); 

			// Query the Multichecks

			final ClientResponse findCheckForMulticheckResponse = Imdb.montanaClient.callProcedure("FindChecksForMultiCheckID", multicheckID);

			if (findCheckForMulticheckResponse.getStatus() != ClientResponse.SUCCESS){
				System.err.println(findCheckForMulticheckResponse.getStatusString());
			}

			final VoltTable findCheckForMulticheckResults[] = findCheckForMulticheckResponse.getResults();
			if (findCheckForMulticheckResults.length == 0) {
				System.out.printf("Not valid match found!\n");
			}

			// Query all the checks that define the Multichecks

			List<String> checkIDsMatchingMultiCheck = new ArrayList<String>();

			for (VoltTable findCheckForMulticheckResult : findCheckForMulticheckResults) 
			{
				// check if any rows have been returned 
				if (findCheckForMulticheckResult.getRowCount() > 0)
				{
					for (int i = 0; i < findCheckForMulticheckResult.getRowCount(); i++)
					{
						// get the row
						VoltTableRow row = findCheckForMulticheckResult.fetchRow(i);
						// extract the value in column checkid
						checkIDsMatchingMultiCheck.add(row.getString("CHECKID"));
						System.out.println("ATTCHED CHECKS FOUND: " + row.getString("CHECKID"));

						// evaluate each check linked to the multicheck whether it is true or not

						final ClientResponse findCheckStateResponse = Imdb.montanaClient.callProcedure("FindChecksForCheckID", row.getString("CHECKID"));
						final VoltTable findCheckStateResults[] = findCheckStateResponse.getResults();


						for (VoltTable findCheckStateResult : findCheckStateResults) 
						{
							// check if any rows have been returned 
							if (findCheckStateResult.getRowCount() > 0)
							{
								for (int j = 0; j < findCheckStateResult.getRowCount(); j++)
								{
									VoltTableRow checkRow = findCheckStateResult.fetchRow(j);
									if (checkRow.getLong("STATE") == 1) {
										System.out.println("STATE: TRUE");
										checkStates.add(true);
									}
									else
									{
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
				final ClientResponse findMultiCheckOperatorResponse = Imdb.montanaClient.callProcedure("FindMultiChecksForMultiCheckID", multicheckID);
				final VoltTable findMultiCheckOperatorResults[] = findMultiCheckOperatorResponse.getResults();
				VoltTableRow multiCheckOperatorRow = findMultiCheckOperatorResults[0].fetchRow(0);
				String multiCheckOperator = multiCheckOperatorRow.getString("OPERATOR");
				
				if(checkStates.contains(true) && multiCheckOperator.equals("OR")){
					multiCheckState = true;
					Imdb.montanaClient.callProcedure("UpdateMultiChecks", 1, multicheckID);
					System.out.println("Multicheck Eval OR Result: " + multiCheckState);
				}
				
				if((checkStates.contains(true) == true && checkStates.contains(false) == false) && multiCheckOperator.equals("AND")){
					multiCheckState = true;
					Imdb.montanaClient.callProcedure("UpdateMultiChecks", 1, multicheckID);
					System.out.println("Multicheck Eval AND Result for MultiCheck " + multicheckID + ": " + multiCheckState);
				}
				
			}				
		}



		/*
		 * Step 6: Find all Multichecks linked to the Multichecks that were just evaluated, return a List
		 * 			
		 */

		/*
		 * Step 7a: If List is empty, all multichecks have been iterated, no additional action, return 
		 */

		/*
		 * Step 7b: If List is not empty, evaluate if these Multichecks are true and update multicheck state accordingly
		 * 			Also, find all rules related to the multichecks that are true and trigger their action
		 * 			
		 */

	}


	public static void runCheckParentMulticheck(String checkID)
	{
		// Evaluate first in line Multicheck
	}

	public static void runMultiCheckParentMulticheck(String checkID)
	{
		// Evaluate subsequent Multichecks
	}






}



