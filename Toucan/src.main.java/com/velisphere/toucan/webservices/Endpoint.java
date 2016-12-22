package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velisphere.toucan.amqp.AMQPServer;

import com.velisphere.toucan.amqp.VoltConnector;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.security.HashTool;
import com.velisphere.toucan.volt.PasswordChecker;
import com.velisphere.toucan.volt.UserData;
import com.velisphere.toucan.xmlRootElements.EndpointElement;
import com.velisphere.toucan.xmlRootElements.EndpointElements;



@Path("/endpoint")
public class Endpoint {
		
	@GET
	@Path("/get/general/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public EndpointElement getEndpointDetails(@PathParam("param") String endpointID) {
		

		
		
		System.out.println(" [IN] Get General Called");

		EndpointElement endpointForUser = new EndpointElement();
		
		
		
		
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

		try {
			
			VoltTable[] results;

			
			results = voltCon.montanaClient.callProcedure(
					"UI_SelectEndpointForEndpointID", endpointID).getResults();
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
					// extract the value in column checkid
				EndpointData endpointData = new EndpointData();
				endpointData.endpointId = result.getString("ENDPOINTID");
				endpointData.endpointName = result
						.getString("ENDPOINTNAME");
				endpointData.endpointclassId = result
						.getString("ENDPOINTCLASSID");
				endpointData.endpointProvDate = result
						.getTimestampAsTimestamp("ENDPOINTPROVDATE")
						.toString();
				endpointData.endpointState = result
						.getString("ENDPOINTSTATE");
				endpointForUser.setEndpointData(endpointData);
				
			}
			
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

		
		
		//endpointsForUser.setEndpointNames(endpointNames);
		
		return endpointForUser;
	}
	
	
		
	
	
	@PUT
	@Path( "/put/isalive" )
	@Consumes( MediaType.TEXT_PLAIN )
	public Response sendIsAlive( 
			String endpointID ) 
					throws Exception {

		
	
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

	try {
		ClientResponse bleUpdateCheckState = voltCon.montanaClient
				.callProcedure("SRV_UpdateEndpointState", endpointID, "UNKNOWN");
	} catch (IOException | ProcCallException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
				
	HashMap<String, String> messageHash = new HashMap<String, String>();
	messageHash.put("getIsAlive", "1");
	
	try {
		AMQPServer.sendHashTable(messageHash, endpointID, "CTL");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	 return Response.ok().build();
	
	}
	
	
	
	@PUT
	@Path( "/put/configmessage/{endpointid}/{propertyid}" )
	@Consumes( MediaType.TEXT_PLAIN )
	public Response sendConfigMessage( 
			@PathParam("endpointid") String endpointID, @PathParam("propertyid") String propertyID, String value ) 
					throws Exception {
					
	HashMap<String, String> messageHash = new HashMap<String, String>();
	messageHash.put(propertyID, value);
	
	try {
		AMQPServer.sendHashTable(messageHash, endpointID, "CTL");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	ObjectMapper mapper = new ObjectMapper();
	String jsonString = mapper.writeValueAsString(messageHash);
		
	return Response.ok(jsonString).build();
	
	}
	

}


