package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velisphere.toucan.ConfigHandler;
import com.velisphere.toucan.amqp.VoltConnector;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.SphereData;
import com.velisphere.toucan.security.HashTool;
import com.velisphere.toucan.volt.PasswordChecker;
import com.velisphere.toucan.volt.UserData;
import com.velisphere.toucan.xmlRootElements.EndpointElements;
import com.velisphere.toucan.xmlRootElements.SphereElements;



@Path("/sphere")
public class Sphere {
		
	@GET
	@Path("/get/endpoints/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public EndpointElements getEndpointsForSphere(@PathParam("param") String sphereID) {
		

		
		// AMQP handling from here

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();
		
		System.out.println(" [IN] Get Endpoints Called");

		EndpointElements endpointsForUser = new EndpointElements();
		
		LinkedList<EndpointData> endpoints = new LinkedList<EndpointData>();
		
		
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
					"UI_SelectEndpointsForSphere", sphereID).getResults();
			
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
		endpointsForUser.setEndpointData(endpoints);
		//endpointsForUser.setEndpointNames(endpointNames);
		
		return endpointsForUser;
	}
	
	
	

}