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

import com.velisphere.toucan.amqp.VoltConnector;

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

			voltCon.montanaClient.callProcedure(
					"UNPROVISIONED_ENDPOINT.insert", uEPID, identifier,
					endpointClassID, new Timestamp(System.currentTimeMillis()));
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

		return Response.ok(uEPID).build();
		// return Response.noContent().build();

	}

}