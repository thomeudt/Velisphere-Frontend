package com.velisphere.toucan.volt;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.velisphere.toucan.security.HashTool;


public class MessageValidator {

	
	public static boolean validateHmac(String receivedHMAC, String payload, String endpointID) throws NoConnectionsException, IOException, ProcCallException
	{
		

		String secret = null;
		VoltConnector voltCon = new VoltConnector();
		
		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    	
		ClientResponse findSecret = voltCon.montanaClient
				.callProcedure("SEC_SelectSecretForEndpointID", endpointID);

		final VoltTable findSecretResults[] = findSecret.getResults();

		VoltTable result = findSecretResults[0];

		while (result.advanceRow()) {
				
				secret = result
						.getString("SECRET");
				
		}

		//System.out.println("Endpoint ID: " + endpointID );
		//System.out.println("Secret in DB: " + secret );
		
		String calculatedHmac = HashTool.getHmacSha1(payload, secret);
		
		//System.out.println("Calculated HMAC: " + calculatedHmac + " <> Received HMAC: " + receivedHMAC);
		
		boolean validationOK = false;
		
		if (calculatedHmac.equals(receivedHMAC)) validationOK = true;
		
		return validationOK;

	}
	
	
	public static String getSecretFromMontana(String endpointID) throws NoConnectionsException, IOException, ProcCallException
	{
		

		String secret = null;

		VoltConnector voltCon = new VoltConnector();
		
		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		ClientResponse findSecret = voltCon.montanaClient
				.callProcedure("SEC_SelectSecretForEndpointID", endpointID);

		final VoltTable findSecretResults[] = findSecret.getResults();

		VoltTable result = findSecretResults[0];

		while (result.advanceRow()) {
				
				secret = result
						.getString("SECRET");
				
		}

		//System.out.println("Secret in DB: " + secret );
		
				
		return secret;
	
	}

	
}
