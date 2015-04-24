package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.velisphere.toucan.ConfigHandler;
import com.velisphere.toucan.amqp.VoltConnector;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.PropertyData;
import com.velisphere.toucan.xmlRootElements.EndpointElement;
import com.velisphere.toucan.xmlRootElements.EndpointElements;
import com.velisphere.toucan.xmlRootElements.PropertyElement;
import com.velisphere.toucan.xmlRootElements.PropertyElements;



@Path("/properties")
public class Properties {
		
	@GET
	@Path("/get/sensorsforendpoint/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public PropertyElements getSensorsForEndpoint(@PathParam("param") String endpointID) {
		

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();
		
		System.out.println(" [IN] Get All Sensors for Endpoint Called");

		PropertyElements propertyElements = new PropertyElements();
		propertyElements.setPropertyData(new LinkedList<PropertyData>());
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
					"UI_SelectSensePropertiesForEndpoint", endpointID).getResults();
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
					// extract the value in column checkid
				PropertyData propertyData = new PropertyData();
				propertyData.propertyId = result.getString("PROPERTYID");
				propertyData.propertyName = result
						.getString("PROPERTYNAME");
				propertyData.propertyclassId = result
						.getString("PROPERTYCLASSID");
				propertyData.endpointclassId = result
						.getString("ENDPOINTCLASSID");
				propertyData.isActor = (byte) result
						.get("ACT", VoltType.TINYINT);
				propertyData.isSensor = (byte) result
						.get("SENSE", VoltType.TINYINT);
				propertyData.isConfigurable = (byte) result
						.get("CONFIGURABLE", VoltType.TINYINT);
				propertyData.status = (byte) result
						.get("STATUS", VoltType.TINYINT);
				
				propertyElements.getPropertyData().add((propertyData));
				
				
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
		
		return propertyElements;
	}

	
	@GET
	@Path("/get/actorsforendpoint/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public PropertyElements getActorsForEndpoint(@PathParam("param") String endpointID) {
		

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();
		
		System.out.println(" [IN] Get All Actors for Endpoint Called");

		PropertyElements propertyElements = new PropertyElements();
		propertyElements.setPropertyData(new LinkedList<PropertyData>());
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
					"UI_SelectActPropertiesForEndpoint", endpointID).getResults();
		
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
					// extract the value in column checkid
				PropertyData propertyData = new PropertyData();
				propertyData.propertyId = result.getString("PROPERTYID");
				propertyData.propertyName = result
						.getString("PROPERTYNAME");
				propertyData.propertyclassId = result
						.getString("PROPERTYCLASSID");
				propertyData.endpointclassId = result
						.getString("ENDPOINTCLASSID");
				propertyData.isActor = (byte) result
						.get("ACT", VoltType.TINYINT);
				propertyData.isSensor = (byte) result
						.get("SENSE", VoltType.TINYINT);
				propertyData.isConfigurable = (byte) result
						.get("CONFIGURABLE", VoltType.TINYINT);
				propertyData.status = (byte) result
						.get("STATUS", VoltType.TINYINT);
				
				propertyElements.getPropertyData().add((propertyData));
				
				
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
		
		return propertyElements;
	}

	
}


