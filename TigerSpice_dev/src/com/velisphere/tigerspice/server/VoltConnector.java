package com.velisphere.tigerspice.server;

import java.io.IOException;
import java.net.UnknownHostException;

import org.voltdb.client.ClientFactory;

public class VoltConnector {
	public static org.voltdb.client.Client montanaClient;
	
		public static void openDatabase() throws UnknownHostException, IOException {
			/*
			 * Instantiate a client and connect to the database.
			 */
	
			montanaClient = ClientFactory.createClient();
			montanaClient.createConnection(ServerParameters.volt_ip);
			System.out.println(" [IN] Connected to VoltDB on address: "
			+ ServerParameters.volt_ip);
		}
		
		public static void closeDatabase() throws UnknownHostException, IOException, InterruptedException {
			/*
			 * Instantiate a client and connect to the database.
			 */
	
			montanaClient.close();
			
		}
	
}

