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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
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
					propertyData.propertyclassName = result
							.getString("PROPERTYCLASSNAME");
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

		Connection conn;
		String valueForEndpointProperty = new String();

		try {
			Class.forName("com.vertica.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Could not find the JDBC driver class.\n");
			e.printStackTrace();

		}
		try {
			conn = DriverManager.getConnection("jdbc:vertica://"
					+ ServerParameters.vertica_ip + ":5433/VelisphereMart",
					"vertica", "1Suplies!");

			conn.setAutoCommit(true);
			System.out.println(" [OK] Connected to Vertica on address: "
					+ "16.1.1.113");

			Statement mySelect = conn.createStatement();

			ResultSet myResult = mySelect
					.executeQuery("SELECT propertyentry FROM vlogger.endpointpropertylog "
							+ "WHERE vlogger.endpointpropertylog.endpointid = '"
							+ endpointID 
							+ "' AND vlogger.endpointpropertylog.propertyid = '"
							+ propertyID
							+ "' ORDER BY time_stamp DESC LIMIT 1");

			while (myResult.next()) {
				
				valueForEndpointProperty = myResult.getString(1);
				
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
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
					propertyData.isActor = (byte) result
							.get("ACT", VoltType.TINYINT);
					propertyData.isSensor = (byte) result
							.get("SENSE", VoltType.TINYINT);
					propertyData.isConfigurable = (byte) result
							.get("CONFIGURABLE", VoltType.TINYINT);
					propertyData.status = (byte) result
							.get("STATUS", VoltType.TINYINT);
					
					
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
					propertyData.isActor = (byte) result
							.get("ACT", VoltType.TINYINT);
					propertyData.isSensor = (byte) result
							.get("SENSE", VoltType.TINYINT);
					propertyData.isConfigurable = (byte) result
							.get("CONFIGURABLE", VoltType.TINYINT);
					propertyData.status = (byte) result
							.get("STATUS", VoltType.TINYINT);
				
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

	
	public String addProperty(String propertyName, String propertyClassID, String epcID, boolean act, boolean sense, boolean status, boolean configurable)

	{

			
			// first add to VoltDB

			VoltConnector voltCon = new VoltConnector();
			String propertyID = UUID.randomUUID().toString();

			try {
				voltCon.openDatabase();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
			int actInt = act? 1 : 0;
			int senseInt = sense? 1 : 0;
			int statusInt = status? 1 : 0;
			int configurableInt = configurable? 1 : 0;
					
			try {

				voltCon.montanaClient.callProcedure("PROPERTY.insert", propertyID, propertyName, propertyClassID, epcID, actInt, senseInt, statusInt, configurableInt);
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

			// second add to Vertica

			Connection conn;

			try {
				Class.forName("com.vertica.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.err.println("Could not find the JDBC driver class.\n");
				e.printStackTrace();

			}
			try {
				conn = DriverManager.getConnection("jdbc:vertica://"
						+ ServerParameters.vertica_ip + ":5433/VelisphereMart",
						"vertica", "1Suplies!");

				conn.setAutoCommit(true);
				System.out.println(" [OK] Connected to Vertica on address: "
						+ "16.1.1.113");

				Statement myInsert = conn.createStatement();
				myInsert.executeUpdate("INSERT INTO VLOGGER.PROPERTY VALUES ('"
						+ propertyID + "','" + propertyName + "','" +propertyClassID + "','" +epcID + "','" + actInt + "','" +senseInt + "','" +statusInt + "','" + configurableInt + "')");

				myInsert.close();

			} catch (SQLException e) {
				System.err.println("Could not connect to the database.\n");
				e.printStackTrace();

			}


		return "OK";

	}

	@Override
	public PropertyData getPropertyDetailsForPropertyID(String propertyID) {
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

		PropertyData propertyData = new PropertyData();
		try {

			final ClientResponse findAllProperties = voltCon.montanaClient
					.callProcedure("UI_SelectPropertyDetailsForPropertyID",
							propertyID);

			final VoltTable findAllPropertiesResults[] = findAllProperties
					.getResults();

			VoltTable result = findAllPropertiesResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					
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

		return propertyData;
	}
	
	@Override
	public LinkedList<PropertyData> getConfiguratorPropertiesForEndpointID(
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

		LinkedList<PropertyData> configPropertiesForEndpoint = new LinkedList<PropertyData>();
		try {

			final ClientResponse findAllProperties = voltCon.montanaClient
					.callProcedure("UI_SelectConfigPropertiesForEndpoint",
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
					propertyData.isActor = (byte) result
							.get("ACT", VoltType.TINYINT);
					propertyData.isSensor = (byte) result
							.get("SENSE", VoltType.TINYINT);
					propertyData.isConfigurable = (byte) result
							.get("CONFIGURABLE", VoltType.TINYINT);
					propertyData.status = (byte) result
							.get("STATUS", VoltType.TINYINT);
					
					
					configPropertiesForEndpoint.add(propertyData);

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

		return configPropertiesForEndpoint;
	}



}


