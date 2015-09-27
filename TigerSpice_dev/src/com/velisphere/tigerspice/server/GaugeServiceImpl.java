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

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.UUID;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;
import org.voltdb.types.TimestampType;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.dash.GaugeService;
import com.velisphere.tigerspice.client.helper.VeliConstants;
import com.velisphere.tigerspice.shared.ActionData;
import com.velisphere.tigerspice.shared.GaugeData;

@SuppressWarnings("serial")
public class GaugeServiceImpl extends RemoteServiceServlet implements
		GaugeService {

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

	@Override
	public String saveDashboard(String gaugeName,
			LinkedList<GaugeData> gaugeDatas) {
	
		ObjectMapper mapper = new ObjectMapper();
		String gaugeID = UUID.randomUUID().toString();
		String jsonGauges = "";
		 		
		try {
	 				 
			// display to console
			jsonGauges = mapper.writeValueAsString(gaugeDatas);
			
			System.out.println("JSON generiert: " + jsonGauges);
	 
			
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

				voltCon.montanaClient.callProcedure("DASHBOARD.insert", gaugeID,
						gaugeName, jsonGauges);
			} catch (NoConnectionsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ProcCallException e1) {

			}

			try {
				voltCon.closeDatabase();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (JsonGenerationException e) {
	 
			e.printStackTrace();
	 
		} catch (JsonMappingException e) {
	 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}

		
		
		return gaugeID;
	}
	
}
