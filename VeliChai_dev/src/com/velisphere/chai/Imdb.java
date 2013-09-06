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

	public static void runChecks(String endpointID, String propertyID, String checkValue, String operator, byte expired) throws Exception {

		/*
		 * Run the lowest level check engine
		 */


		/*
		 * Step 1: Query all checks matching the data in terms of endPointID, propertyID, CheckValue etc.
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


		for (int i = 0; i < results.length; i++)
		{
			// check if any rows have been returned 
			if (results[i].getRowCount() > 0)
			{

				// get the row
				VoltTableRow row = results[i].fetchRow(i);
				// extract the value in column checkid
				validCheckIDs.add(row.getString("CHECKID"));
				//System.out.println("OUTPUT: " + row.getString("CHECKID"));
			}
		}


		/*
		 * Step 2: Update all check entries in VoltDB with the new state "1" for true
		 * 
		 */

		for (String checkID : validCheckIDs)
		{
			final ClientResponse updateResponse = Imdb.montanaClient.callProcedure("UpdateChecks", 1, checkID);
			if (updateResponse.getStatus() != ClientResponse.SUCCESS){
				System.err.println(selectResponse.getStatusString());

			}
		}


	}




}



