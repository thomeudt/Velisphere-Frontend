package com.velisphere.chai.messageUtils;

import java.io.IOException;
import java.util.HashMap;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.velisphere.chai.ChaiWorker;
import com.velisphere.chai.engines.BusinessLogicEngine;
import com.velisphere.chai.security.HashTool;


public class MessageValidator {

	
	public static boolean validateHmac(String receivedHMAC, String payload, String endpointID) throws NoConnectionsException, IOException, ProcCallException
	{
		

		String secret = null;
		ClientResponse findSecret = BusinessLogicEngine.montanaClient
				.callProcedure("SEC_SelectSecretForEndpointID", endpointID);

		final VoltTable findSecretResults[] = findSecret.getResults();

		VoltTable result = findSecretResults[0];

		while (result.advanceRow()) {
				
				secret = result
						.getString("SECRET");
				
		}

		System.out.println("Secret in DB: " + secret );
		
		String calculatedHmac = HashTool.getHmacSha1(payload, secret);
		
		System.out.println("Calculated HMAC: " + calculatedHmac + " <> Received HMAC: " + receivedHMAC);
		
		boolean validationOK = false;
		
		if (calculatedHmac.equals(receivedHMAC)) validationOK = true;
		
		return validationOK;
	
		
	

	}
}
