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
package com.velisphere.blender.amqp;

import java.io.IOException;
import java.net.UnknownHostException;

import org.voltdb.client.ClientFactory;

public class VoltConnector {
	public org.voltdb.client.Client montanaClient;
	
		public void openDatabase() throws UnknownHostException, IOException {
			/*
			 * Instantiate a client and connect to the database.
			 */
	
			// System.out.println("Creating factory on ip " + ServerParameters.volt_ip);
			montanaClient = ClientFactory.createClient();
			montanaClient.createConnection(ServerParameters.volt_ip);
			/*
			System.out.println(" [IN] Connected to VoltDB on address: "
			+ ServerParameters.volt_ip);
			*/
		}
		
		public void closeDatabase() throws UnknownHostException, IOException, InterruptedException {
			/*
			 * Instantiate a client and connect to the database.
			 */
			montanaClient.close();			
		}
	
}

