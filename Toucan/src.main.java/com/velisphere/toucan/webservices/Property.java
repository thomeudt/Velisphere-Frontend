package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.velisphere.toucan.amqp.VoltConnector;
import com.velisphere.toucan.dataObjects.EndpointClassData;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.PropertyData;
import com.velisphere.toucan.xmlRootElements.EndpointClassElement;
import com.velisphere.toucan.xmlRootElements.EndpointElement;
import com.velisphere.toucan.xmlRootElements.EndpointElements;
import com.velisphere.toucan.xmlRootElements.PropertyElement;



@Path("/property")
public class Property {
		
	@GET
	@Path("/get/general/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public PropertyElement getPropertyDetails(@PathParam("param") String propertyID) {
		

		
		System.out.println(" [IN] Get General Called");

		PropertyElement propertyElement = new PropertyElement();
		
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
					"UI_SelectPropertyDetailsForPropertyID", propertyID).getResults();
			
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
				
				propertyElement.setPropertyData(propertyData);
				
				
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
		
		return propertyElement;
	}
	

	@GET
	@Path("/get/unitofmeasure/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getUnitOfMeasure(@PathParam("param") String propertyID) {
		


		String unitOfMeasure = new String();
		PropertyElement propertyElement = new PropertyElement();
		
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
					"UI_SelectPropertyDetailsForPropertyID", propertyID).getResults();
			
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
				
				propertyElement.setPropertyData(propertyData);
				
				
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


		System.out.println("PROP CLASS ID "+ propertyElement.getPropertyData().getPropertyclassId());
		
		
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
					"UI_SelectPropertyClassForPropertyClassID", propertyElement.getPropertyData().getPropertyclassId()).getResults();
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
					// extract the value in column checkid
				
				unitOfMeasure = result.getString("PROPERTYCLASSUNIT");
				
				
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
		
		return unitOfMeasure;
	}
	
	
	

	

}


