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
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.EndpointData;

@SuppressWarnings("serial")
public class CheckServiceImpl extends RemoteServiceServlet implements
		CheckService {

	 
	/**
	 * 
	 */


	public Vector<CheckData> getChecksForEndpointID(String endpointID)

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

		Vector<CheckData> checksForEndpointID = new Vector<CheckData>();
		try {

			final ClientResponse findCheck= voltCon.montanaClient
					.callProcedure("UI_SelectChecksForEndpoint", endpointID);

			final VoltTable findCheckResults[] = findCheck.getResults();

			VoltTable result = findCheckResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					
					CheckData check = new CheckData();
					check.checkId = result.getString("CHECKID");
					check.endpointId = result.getString("ENDPOINTID");
					check.checkName  = result.getString("NAME");
					check.checkValue = result.getString("CHECKVALUE");
										
					Byte expired;
					expired = (Byte) result.get("EXPIRED", VoltType.TINYINT);
					check.expired = expired.byteValue();
					
					check.operator = result.getString("OPERATOR");
					check.propertyId = result.getString("PROPERTYID");
					
					Byte state;
					state = (Byte) result.get("STATE", VoltType.TINYINT);
					check.state = state.byteValue();
								
					
					checksForEndpointID.add(check);

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

		return checksForEndpointID;
	}

	

	
}
