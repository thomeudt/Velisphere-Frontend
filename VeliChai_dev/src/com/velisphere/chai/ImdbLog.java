package com.velisphere.chai;

import org.voltdb.client.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;

public class ImdbLog {

	public static org.voltdb.client.Client montanaClient;

	public static void openDatabase() throws UnknownHostException, IOException{
		/*
		 * Instantiate a client and connect to the database.
		 */

		ImdbLog.montanaClient = ClientFactory.createClient();
		ImdbLog.montanaClient.createConnection("16.1.1.149");
	}

	public static void writeLog(String exchangeName, String message, String queueName, String routingKey) throws Exception {

		/*
		 * Write log to database.
		 */

		UUID identifier = UUID.randomUUID();
		String identifierString = identifier.toString();

		// TODO: change to a meaningful log table!

		ImdbLog.montanaClient.callProcedure("Insert", "1", "1", "1", "1", identifierString);

	}

}
