package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.voltdb.VoltTable;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.velisphere.toucan.ConfigHandler;
import com.velisphere.toucan.amqp.VoltConnector;
import com.velisphere.toucan.dataObjects.EndpointClassData;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.xmlRootElements.EndpointClassElement;
import com.velisphere.toucan.xmlRootElements.EndpointElement;
import com.velisphere.toucan.xmlRootElements.EndpointElements;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



@Path("/endpointclass")
public class EndpointClass  {
		
	@Context ServletContext context;
	@GET
	@Path("/get/general/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public EndpointClassElement getEndpointClassDetails(@PathParam("param") String endpointClassID) {
		

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();
		
		System.out.println(" [IN] Get General Called");

		EndpointClassElement endpointClassElement = new EndpointClassElement();
		
		
		
		
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
					"UI_SelectEndpointForEndpointID", endpointClassID).getResults();
			
			VoltTable result = results[0];
			// check if any rows have been returned

						
			while (result.advanceRow()) {
					// extract the value in column checkid
				
				EndpointClassData endpointClassData = new EndpointClassData();
				endpointClassData.endpointclassName = result.getString("ENDPOINTCLASSNAME");
				endpointClassData.endpointclassID = result.getString("ENDPOINTCLASSID");
				endpointClassData.endpointclassPath = result.getString("ENDPOINTCLASSIMAGEURL");
				endpointClassData.vendorID = result.getString("VENDORID");
				endpointClassData.endpointclassImageURL = context.getContextPath()+"/tigerspiceDownloads?privateURL="+result.getString("ENDPOINTCLASSIMAGEURL")
						+ "&outboundFileName=EPC_image&persist=1";
				
				
				endpointClassElement.setEndpointClassData(endpointClassData);
				
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
		
		return endpointClassElement;
	}
	
	
	

}


