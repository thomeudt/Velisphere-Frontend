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

import java.awt.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
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
import com.velisphere.tigerspice.client.actions.ActionService;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.logic.CheckPathService;

import com.velisphere.tigerspice.shared.ActionData;
import com.velisphere.tigerspice.shared.CheckDataUNUSED;
import com.velisphere.tigerspice.shared.CheckPathDataUNUSED;
import com.velisphere.tigerspice.shared.CheckPathObjectTreeUNUSED;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.CheckPathObjectDataUNUSED;
import com.velisphere.tigerspice.shared.SphereData;

@SuppressWarnings("serial")
public class ActionServiceImpl extends RemoteServiceServlet implements
		ActionService {

	public LinkedList<ActionData> getActionsForCheckpathID(String checkpathID){
		
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

		LinkedList<ActionData> actionsForCheckpathID = new LinkedList<ActionData>();
		try {

			final ClientResponse findAction = voltCon.montanaClient
					.callProcedure("UI_SelectActionsForCheckpathID", checkpathID);

			final VoltTable findActionResult[] = findAction.getResults();

			VoltTable result = findActionResult[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					ActionData action = new ActionData();
					action.setActionID(result.getString("ACTIONID"));
					action.setTargetEndpointID(result.getString("TARGETENDPOINTID"));
					actionsForCheckpathID.add(action);
				}
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

		
		
		return actionsForCheckpathID;
		
	}
	
}
