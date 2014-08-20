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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.voltdb.VoltTable;
import org.voltdb.VoltType;
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
import com.velisphere.tigerspice.shared.UnusedFolderPropertyData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.UserData;

public class PropertyServiceImpl extends RemoteServiceServlet implements
		PropertyService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8872989521623692797L;

	public LinkedList<PropertyData> getAllPropertyDetails()

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

		LinkedList<PropertyData> allProperties = new LinkedList<PropertyData>();
		try {

			final ClientResponse findAllProperties = voltCon.montanaClient
					.callProcedure("UI_SelectAllProperties");

			final VoltTable findAllPropertiesResults[] = findAllProperties
					.getResults();

			VoltTable result = findAllPropertiesResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					PropertyData propertyData = new PropertyData();
					propertyData.propertyId = result.getString("PROPERTYID");
					propertyData.propertyName = result
							.getString("PROPERTYNAME");
					propertyData.propertyclassId = result
							.getString("PROPERTYCLASSID");
					propertyData.endpointclassId = result
							.getString("ENDPOINTCLASSID");
					allProperties.add(propertyData);

				}
			}

			

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

	@Override
	public LinkedList<PropertyData> getPropertiesForEndpointClass(
			String endpointClassID) {

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

		LinkedList<PropertyData> propertiesForEndpointClass = new LinkedList<PropertyData>();
		try {

			final ClientResponse findAllProperties = voltCon.montanaClient
					.callProcedure("UI_SelectPropertyDetailsForEndpointClass",
							endpointClassID);

			final VoltTable findAllPropertiesResults[] = findAllProperties
					.getResults();

			VoltTable result = findAllPropertiesResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					PropertyData propertyData = new PropertyData();
					propertyData.propertyId = result.getString("PROPERTYID");
					propertyData.propertyName = result
							.getString("PROPERTYNAME");
					propertyData.propertyclassId = result
							.getString("PROPERTYCLASSID");
					propertyData.endpointclassId = result
							.getString("ENDPOINTCLASSID");
					propertyData.isActor = (byte) result
							.get("ACT", VoltType.TINYINT);
					propertyData.isSensor = (byte) result
							.get("SENSE", VoltType.TINYINT);
					propertyData.isConfigurable = (byte) result
							.get("CONFIGURABLE", VoltType.TINYINT);
					propertyData.status = (byte) result
							.get("STATUS", VoltType.TINYINT);
					propertiesForEndpointClass.add(propertyData);

				}
			}

			//Collections.sort(propertiesForEndpointClass);

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

		return propertiesForEndpointClass;

	}
	
	
	@Override
	public String getValueForEndpointProperty(
			String endpointID, String propertyID) {

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

		String valueForEndpointProperty = new String();
		try {

			final ClientResponse findValueForEndpointProperty = voltCon.montanaClient
					.callProcedure("UI_LastLogEntryForEndpointProperty",
							endpointID, propertyID);

			final VoltTable findValueForEndpointPropertyResults[] = findValueForEndpointProperty
					.getResults();

			VoltTable result = findValueForEndpointPropertyResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					valueForEndpointProperty = result.getString("propertyEntry");

				}
			}

			

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

		return valueForEndpointProperty;

	}

	@Override
	public String getPropertyClass(
			String propertyID) {

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

		String valueForEndpointProperty = new String();
		try {

			final ClientResponse findPropertyClass = voltCon.montanaClient
					.callProcedure("UI_SelectPropertyClassForPropertyID",
							propertyID);

			final VoltTable findPropertyClassResults[] = findPropertyClass
					.getResults();

			VoltTable result = findPropertyClassResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					valueForEndpointProperty = result.getString("propertyClassID");

				}
			}

			

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

		return valueForEndpointProperty;

	}

	
	@Override
	public String getPropertyName(
			String propertyID) {

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

		String valueForEndpointProperty = new String();
		try {

			final ClientResponse findPropertyName = voltCon.montanaClient
					.callProcedure("UI_SelectPropertyNameForPropertyID",
							propertyID);

			final VoltTable findPropertyNameResults[] = findPropertyName
					.getResults();

			VoltTable result = findPropertyNameResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					valueForEndpointProperty = result.getString("propertyName");

				}
			}

			

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

		return valueForEndpointProperty;

	}

	@Override
	public LinkedList<PropertyData> getActorPropertiesForEndpointID(
			String endpointID) {
		
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

		LinkedList<PropertyData> actPropertiesForEndpoint = new LinkedList<PropertyData>();
		try {

			final ClientResponse findAllProperties = voltCon.montanaClient
					.callProcedure("UI_SelectActPropertiesForEndpoint",
							endpointID);

			final VoltTable findAllPropertiesResults[] = findAllProperties
					.getResults();

			VoltTable result = findAllPropertiesResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					PropertyData propertyData = new PropertyData();
					propertyData.propertyId = result.getString("PROPERTYID");
					propertyData.propertyName = result
							.getString("PROPERTYNAME");
					propertyData.propertyclassId = result
							.getString("PROPERTYCLASSID");
					propertyData.endpointclassId = result
							.getString("ENDPOINTCLASSID");
					
					actPropertiesForEndpoint.add(propertyData);

				}
			}

			//Collections.sort(propertiesForEndpointClass);

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

		return actPropertiesForEndpoint;
	}

	
	@Override
	public LinkedList<PropertyData> getSensorPropertiesForEndpointID(
			String endpointID) {
		
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

		LinkedList<PropertyData> sensePropertiesForEndpoint = new LinkedList<PropertyData>();
		try {

			final ClientResponse findAllProperties = voltCon.montanaClient
					.callProcedure("UI_SelectSensePropertiesForEndpoint",
							endpointID);

			final VoltTable findAllPropertiesResults[] = findAllProperties
					.getResults();

			VoltTable result = findAllPropertiesResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					PropertyData propertyData = new PropertyData();
					propertyData.propertyId = result.getString("PROPERTYID");
					propertyData.propertyName = result
							.getString("PROPERTYNAME");
					propertyData.propertyclassId = result
							.getString("PROPERTYCLASSID");
					propertyData.endpointclassId = result
							.getString("ENDPOINTCLASSID");
					
					sensePropertiesForEndpoint.add(propertyData);

				}
			}

			//Collections.sort(propertiesForEndpointClass);

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

		return sensePropertiesForEndpoint;
	}


}


