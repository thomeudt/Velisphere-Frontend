package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
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
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.VendorData;
import com.velisphere.toucan.xmlRootElements.EndpointElement;
import com.velisphere.toucan.xmlRootElements.EndpointElements;
import com.velisphere.toucan.xmlRootElements.VendorElement;



@Path("/vendor")
public class Vendor {
		
	@Context ServletContext context;
	@GET
	@Path("/get/general/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public VendorElement getVendorDetails(@PathParam("param") String vendorID) {
		

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();
		
		System.out.println(" [IN] Get General Called");

		VendorElement vendorElement = new VendorElement();
		
		
		
		
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
					"UI_SelectVendorForVendorID", vendorID).getResults();
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
					// extract the value in column checkid
				VendorData vendorData = new VendorData();
				vendorData.vendorName = result.getString("VENDORNAME");
				vendorData.vendorID = result.getString("VENDORID");
				vendorData.vendorPath = result.getString("VENDORIMAGEURL");
				vendorData.vendorImageURL = context.getContextPath()+"/tigerspiceDownloads?privateURL="+result.getString("VENDORIMAGEURL")
						+ "&outboundFileName=VENDOR_image&persist=1";

				vendorElement.setVendorData(vendorData);

				
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
		
		return vendorElement;
	}
	
	
	

}


