/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
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

import java.awt.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;
import java.util.Vector;

import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.rules.CheckPathService;
import com.velisphere.tigerspice.client.rules.MulticheckColumn;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.CheckPathObjectData;

@SuppressWarnings("serial")
public class CheckPathServiceImpl extends RemoteServiceServlet implements
		CheckPathService {

	 
	/**
	 * 
	 */


	
	public String addNewUiObject(CheckPathObjectData uiObject)

	{
				
		ObjectMapper mapper = new ObjectMapper();
		
		
	 
		String jsonCheckpathObject = "E0"; 
		
		try {
	 				 
			// display to console
			jsonCheckpathObject = mapper.writeValueAsString(uiObject);
			System.out.println("JSON generiert: " + jsonCheckpathObject);
	 
		} catch (JsonGenerationException e) {
	 
			e.printStackTrace();
	 
		} catch (JsonMappingException e) {
	 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}
	 

		System.out.println("**************************ROTZE");
		
		

		return jsonCheckpathObject;

	}
	
	
	

	
}
