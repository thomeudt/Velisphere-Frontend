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
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.velisphere.toucan.amqp.VoltConnector;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.PropertyClassData;
import com.velisphere.toucan.xmlRootElements.EndpointElement;
import com.velisphere.toucan.xmlRootElements.EndpointElements;
import com.velisphere.toucan.xmlRootElements.PropertyClassElement;



@Path("/propertyclass")
public class PropertyClass {
		
	@GET
	@Path("/get/general/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public PropertyClassElement getPropertyClassDetails(@PathParam("param") String propertyClassID) {
		


		System.out.println(" [IN] Get General Called");

		PropertyClassElement propertyClassElement = new PropertyClassElement();
		
		
		
		
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
					"UI_SelectPropertyClassForPropertyClassID", propertyClassID).getResults();
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
					// extract the value in column checkid
				PropertyClassData propertyClassData = new PropertyClassData();
				propertyClassData.propertyClassId = result.getString("PROPERTYCLASSID");
				propertyClassData.propertyClassName = result.getString("PROPERTYCLASSNAME");
				propertyClassData.propertyClassDatatype = result.getString("PROPERTYCLASSDATATYPE");
				propertyClassData.propertyClassUnit = result.getString("PROPERTYCLASSUNIT");
				
				propertyClassElement.setPropertyClassData(propertyClassData);
				
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
		
		return propertyClassElement;
	}
	
	@GET
	@Path("/get/unitofmeasure/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getUnitOfMeasure(@PathParam("param") String propertyClassID) {
		

	
		
		String unitOfMeasure = new String();
		
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
					"UI_SelectPropertyClassForPropertyClassID", propertyClassID).getResults();
			
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


