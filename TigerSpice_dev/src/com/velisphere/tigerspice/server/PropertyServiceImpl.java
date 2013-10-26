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

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.users.UserService;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.UserData;

public class PropertyServiceImpl extends RemoteServiceServlet implements
		PropertyService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8872989521623692797L;

		
	
	public Vector<PropertyData> getAllPropertyDetails()

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

		Vector<PropertyData> allProperties = new Vector<PropertyData>();
		try {

			final ClientResponse findAllProperties = voltCon.montanaClient
					.callProcedure("UI_SelectAllProperties");

			final VoltTable findAllPropertiesResults[] = findAllProperties.getResults();

			VoltTable result = findAllPropertiesResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					PropertyData propertyData = new PropertyData();
					propertyData.propertyId = result.getString("PROPERTYID");
					propertyData.propertyName = result.getString("PROPERTYNAME");
					propertyData.propertyclassId = result.getString("PROPERTYCLASSID");
					propertyData.endpointclassId = result.getString("ENDPOINTCLASSID");
					allProperties.add(propertyData);

				}
			}

			System.out.println(allProperties);

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
		
		return allProperties;
	}













}