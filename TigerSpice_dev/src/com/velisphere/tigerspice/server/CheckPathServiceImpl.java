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
import com.velisphere.tigerspice.shared.CheckPathObjectTree;
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

	
	public String createJsonCheckpath(CheckPathObjectTree uiObject)

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
	 
		return jsonCheckpathObject;

	}
	
	
	public String addNewMulticheck(String checkId, String operator, String multicheckName)

	{
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
			voltCon.montanaClient.callProcedure("MULTICHECK.insert",
					checkId, operator, 0, 0, multicheckName);
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

		return "OK";

	}

	
	public String addNewMulticheckCheckLink(String multicheckId, String checkId)

	{
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
			voltCon.montanaClient.callProcedure("MULTICHECK_CHECK_LINK.insert",
					UUID.randomUUID().toString(), multicheckId,  checkId);
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

		return "OK";

	}


	public String addNewMulticheckMulticheckLink(String multicheckLId, String multicheckRId)

	{
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
			voltCon.montanaClient.callProcedure("MULTICHECK_MULTICHECK_LINK.insert",
					UUID.randomUUID().toString(), multicheckLId,  multicheckRId);
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

		return "OK";

	}


	public String addNewCheckpath(String checkpathName)

	{
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

		String checkPathId = UUID.randomUUID().toString();
		
		try {
			voltCon.montanaClient.callProcedure("CHECKPATH.insert",
					checkPathId, checkpathName, null);
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

		return checkPathId;

	}
	
	public String updateCheckpath(String checkpathID, String uiObject)

	{
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
			voltCon.montanaClient.callProcedure("UI_UpdateCheckpath",
					checkpathID, uiObject);
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

		return "OK";

	}

	
	public CheckPathObjectTree getUiObjectJSONForCheckpathID(String checkpathID)

	{
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

		String uiObjectJSON = new String();
		CheckPathObjectTree uiObject = new CheckPathObjectTree();
		try {

			final ClientResponse findCheckpath= voltCon.montanaClient
					.callProcedure("UI_SelectCheckpathForCheckpathID", checkpathID);

			final VoltTable findCheckpathResults[] = findCheckpath.getResults();

			VoltTable result = findCheckpathResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					
					uiObjectJSON = result.getString("UIOBJECT");
					
				}
			}

			ObjectMapper mapper = new ObjectMapper();
			try {
			        
				uiObject = mapper.readValue(uiObjectJSON, CheckPathObjectTree.class);
			        System.out.println(uiObject);
			    } catch (JsonGenerationException e) {
			        System.out.println(e);
			        } catch (JsonMappingException e) {
			       System.out.println(e);
			    } catch (IOException e) {
			    System.out.println(e);
			    } 
			
			
			// System.out.println(endPointsforSphere);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return uiObject;
	}

	

	
}
