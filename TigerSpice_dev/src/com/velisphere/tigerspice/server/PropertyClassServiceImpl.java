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

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.LogService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.users.UserService;
import com.velisphere.tigerspice.shared.LogData;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.UserData;

@SuppressWarnings("serial")
public class PropertyClassServiceImpl extends RemoteServiceServlet implements
		PropertyClassService {
	
	


	public PropertyClassData getPropertyClassForPropertyClassID(String propertyClassID) 

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
	
		
		PropertyClassData propertyClassData = new PropertyClassData();
		try {

			final ClientResponse findPropertyClass = voltCon.montanaClient
					.callProcedure("UI_SelectPropertyClassForPropertyClassID", propertyClassID);

			final VoltTable findPropertyClassResults[] = findPropertyClass.getResults();

			VoltTable result = findPropertyClassResults[0];
			// check if any rows have been returned

			

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					propertyClassData.propertyClassId = result.getString("PROPERTYCLASSID");
					propertyClassData.propertyClassName = result.getString("PROPERTYCLASSNAME");
					propertyClassData.propertyClassDatatype = result.getString("PROPERTYCLASSDATATYPE");
					propertyClassData.propertyClassUnit = result.getString("PROPERTYCLASSUNIT");
				}
			}

			// System.out.println(allUsers);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			voltCon.closeDatabase();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return propertyClassData;
	}

	

	

}
