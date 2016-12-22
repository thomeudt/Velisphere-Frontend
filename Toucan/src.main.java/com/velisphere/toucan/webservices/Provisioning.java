package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velisphere.toucan.amqp.VoltConnector;
import com.velisphere.toucan.dataObjects.SecretData;
import com.velisphere.toucan.security.HashTool;

@Path("/provisioning")
public class Provisioning {

	@PUT
	@Path("/put/endpoint/{identifier}")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response postPlainTextMessage(
			@PathParam("identifier") String identifier, String endpointClassID)
			throws Exception {
			
		
		// add to unprovisioned endpoints list

		VoltConnector voltCon = new VoltConnector();
		String uEPID = UUID.randomUUID().toString();
		String secretKey = HashTool.generateKey();

		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			System.out.println("UEPID: " + uEPID);
			System.out.println("Identifier: " + identifier);
			System.out.println("EPC: " + endpointClassID);
						
			
			System.out.println("Secret Key: " + secretKey);
			System.out.println("Signature (HMAC): " + HashTool.getHmacSha1(uEPID, secretKey));

			voltCon.montanaClient.callProcedure(
					"UNPROVISIONED_ENDPOINT.insert", uEPID, identifier,
					endpointClassID, new Timestamp(System.currentTimeMillis()),secretKey);
		} catch (NoConnectionsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ProcCallException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		SecretData secretData = new SecretData();
		secretData.setEpid(uEPID);
		secretData.setSecret(secretKey);
		
		ObjectMapper mapper = new ObjectMapper();

		String jsonString = mapper.writeValueAsString(secretData);
		
		
		return Response.ok(jsonString).build();
		// return Response.noContent().build();

	}

}