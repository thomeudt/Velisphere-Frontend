package com.velisphere.blender.webservices;

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
import com.velisphere.blender.ConfigHandler;
import com.velisphere.blender.ConfigMap;
import com.velisphere.blender.dataObjects.ServiceData;



@Path("/config")
public class Service {
		
	@GET
	@Path("/get/general/{serviceid}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getEndpointDetails(@PathParam("serviceid") String serviceID) {
		

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();
		
		System.out.println(" [IN] Get General Called");

			
		return ConfigMap.getUrl(serviceID);
	}
	
	
		
	
	
	
}


