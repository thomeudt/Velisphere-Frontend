package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.voltdb.VoltTable;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.velisphere.toucan.ConfigHandler;
import com.velisphere.toucan.amqp.ServerParameters;
import com.velisphere.toucan.amqp.VoltConnector;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.volt.PasswordChecker;
import com.velisphere.toucan.volt.UserData;
import com.velisphere.toucan.xmlRootElements.Endpoints;
import com.velisphere.toucan.xmlRootElements.Todo;



@Path("/authentication")
public class Authenticator {

	
	@PUT
	@Path( "/put/user/{username}" )
	@Consumes( MediaType.TEXT_PLAIN )
	public Response postPlainTextMessage( 
			@PathParam( "username" ) String username,
			String password ) 
					throws Exception {

	PasswordChecker checker = new PasswordChecker();
	
	UserData user = checker.loginServer(username, password);
	
		
	System.out.println("User ID " + user.userID + " validated for User Name: " + username);
		
	return Response.ok(user.userID).build();
	//return Response.noContent().build();

	}
	
	@GET
	@Path("/get/user/name/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getUserName(@PathParam("param") String userID) {
		
		// AMQP handling from here

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();

		String userName = new String();
		
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
					"USER.select", userID).getResults();
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
					// extract the value in column checkid
					userName = result.getString("USERNAME");
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


		return userName;
	}
	
	@GET
	@Path("/get/user/endpoints/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Endpoints getEndpointsForUser(@PathParam("param") String userID) {
		
		// AMQP handling from here

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();
		
		System.out.println(" [IN] Get Endpoints Called");

		Endpoints endpointsForUser = new Endpoints();
		
		LinkedList<EndpointData> endpoints = new LinkedList<EndpointData>();
		LinkedList<String> endpointNames = new LinkedList<String>();
		
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
					"UI_SelectEndpointsForUser", userID).getResults();
			
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
				endpoints.add(endpointData);
				endpointNames.add(endpointData.endpointName);
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

		System.out.println(" [IN] Result from Get Endpoints: " + Arrays.toString(endpoints.toArray()));
		//endpointsForUser.setEndpointData(endpoints);
		endpointsForUser.setEndpointNames(endpointNames);
		
		return endpointsForUser;
	}

	

}