/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
package com.velisphere.tigerspice.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.mindrot.BCrypt;
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ClientStats;
import org.voltdb.client.ClientStatsContext;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.server.ServerParameters;
import com.velisphere.tigerspice.client.helper.HelperService;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.MessageData;
import com.velisphere.tigerspice.shared.MontanaStatsData;
import com.velisphere.tigerspice.shared.UserData;

@SuppressWarnings("serial")
public class HelperServiceImpl extends RemoteServiceServlet implements HelperService
{
   
	static ClientStatsContext montanaStatsContext;
	
	public String autoConfMontana()
	{
		
		Properties props = new Properties();
		InputStream is = null;

		
		// Read settings via BlenderServer
		
		System.out.println("[IN] THIS IS A FRESH COMPILE");
		
		
		System.out.println("[IN] Receiving Configuration from BlenderServer");
		
		

		Client client = ClientBuilder.newClient();

		WebTarget target = client.target( "http://www.connectedthingslab.com:8080/BlenderServer/rest/config/get/general" );
		Response response = target.path("VOLT").request().get();
		
		
		ServerParameters.volt_ip = response.readEntity(String.class);
		System.out.println("[IN] Selected VoltDB: "+ ServerParameters.volt_ip);
		
		target = client.target( "http://www.connectedthingslab.com:8080/BlenderServer/rest/config/get/general" );
		response = target.path("VERTICA").request().get();
		
		ServerParameters.vertica_ip = response.readEntity(String.class);
		System.out.println("[IN] Selected Vertica Database: "+ ServerParameters.vertica_ip);

		
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

		montanaStatsContext = voltCon.montanaClient.createStatsContext();
				
		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return montanaStatsContext.toString();
		
	}
 
    @Override
    public MontanaStatsData getMontanaStats()
    {
                //validate username and password
                 
                //store the user/session id
    	
    	MontanaStatsData stats = new MontanaStatsData();
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
	
	
		
			ClientResponse findCheck;
			try {
				findCheck = voltCon.montanaClient
						.callProcedure("@Statistics",
				                  "IOSTATS",1);
				
				final VoltTable findCheckResults[] = findCheck.getResults();

				VoltTable result = findCheckResults[0];
				// check if any rows have been returned

				result.advanceRow();
					
						// extract the value in column checkid
						
						stats.bytesRead = result.getLong("BYTES_READ");
						stats.bytesWritten = result.getLong("BYTES_WRITTEN");
						stats.connectionHostname = result.getString("CONNECTION_HOSTNAME");
						stats.connectionId = result.getLong("CONNECTION_ID");
						stats.hostId = result.getLong("HOST_ID");
						stats.hostname = result.getString("HOSTNAME");
						stats.messagesRead = result.getLong("MESSAGES_READ");
						stats.messagesWritten = result.getLong("MESSAGES_WRITTEN");
						stats.timestamp = result.getLong("TIMESTAMP");
						stats.IP = ServerParameters.volt_ip;
						
						
				
			} catch (IOException | ProcCallException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
					
				
		

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				 
        return stats;
    }
 
    
    @Override
    public String createMessageJson(MessageData messageData)
    {
    	ObjectMapper mapper = new ObjectMapper();
		
		System.out.println("Intake: " + messageData.toString());
	 
		String json = ""; 
		
		try {
			
			
	 				 
			// display to console
			json = mapper.writeValueAsString(messageData);
			System.out.println("JSON generiert: " + json);
	 
		} catch (JsonGenerationException e) {
	 
			e.printStackTrace();
	 
		} catch (JsonMappingException e) {
	 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}
	 

		return json;
  	
    	
    }
    
     
}
