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
		

		final ClientResponse response = Imdb.montanaClient.callProcedure("FindMatchingChecksEqual", endpointID, propertyID, checkValue, operator, expired);

		if (response.getStatus() != ClientResponse.SUCCESS){
            System.err.println(response.getStatusString());
            System.exit(-1);
        }
		
		
		final VoltTable results[] = response.getResults();
        if (results.length == 0) {
            System.out.printf("Not valid match found!\n");
        }
		
        System.out.println("Length: " + results.length);
        
        for (int i = 0; i < results.length; i++)
        {
        	VoltTableRow row = results[i].fetchRow(i);
    		// System.out.println("OUTPUT: " + row.getString("CHECKID"));
        }
        
	
	
	
	}

	


}



