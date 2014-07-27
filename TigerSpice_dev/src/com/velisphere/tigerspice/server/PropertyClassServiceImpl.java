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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.analytics.LogService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.users.UserService;
import com.velisphere.tigerspice.shared.EPCData;
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
	
	
	public String addPropertyClass(String propertyClassName, String propertyClassDataType, String propertyClassUnit)

	{

			
			// first add to VoltDB

			VoltConnector voltCon = new VoltConnector();
			String propertyClassID = UUID.randomUUID().toString();

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

				voltCon.montanaClient.callProcedure("PROPERTYCLASS.insert", propertyClassID,
						propertyClassName, propertyClassDataType,propertyClassUnit);
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
				myInsert.executeUpdate("INSERT INTO VLOGGER.PROPERTYCLASS VALUES ('"
						+ propertyClassID + "','" + propertyClassName + "','" + propertyClassDataType + "','" + propertyClassUnit + "') ");

				myInsert.close();

			} catch (SQLException e) {
				System.err.println("Could not connect to the database.\n");
				e.printStackTrace();

			}


		return "OK";

	}
	
	
	public LinkedList<PropertyClassData> getAllPropertyClassDetails()

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

		LinkedList<PropertyClassData> allPropertyClasses = new LinkedList<PropertyClassData>();
		try {

			final ClientResponse findAllPropertyClasses = voltCon.montanaClient
					.callProcedure("UI_SelectAllPropertyClasses");

			final VoltTable findAllPropertyClassesResults[] = findAllPropertyClasses.getResults();

			VoltTable result = findAllPropertyClassesResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					System.out.println("GETTING...");
					
					// extract the value in column checkid
					PropertyClassData pcData = new PropertyClassData();
					pcData.propertyClassName = result.getString("PROPERTYCLASSNAME");
					pcData.propertyClassId = result.getString("PROPERTYCLASSID");
					pcData.propertyClassDatatype = result.getString("PROPERTYCLASSDATATYPE");
					pcData.propertyClassUnit = result.getString("PROPERTYCLASSUNIT");
					
					
					allPropertyClasses.add(pcData);

				}
			}

			// System.out.println(allEndPointClasses);

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
		
		return allPropertyClasses;
	}
	
	
	public String updatePropertyClass(String pcID, String pcName, String pcDataType, String pcUnit)

	{

			
			// first update to VoltDB

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

				voltCon.montanaClient.callProcedure("UI_UpdatePropertyClass", pcID,
						pcName, pcDataType, pcUnit);
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

			// second update to Vertica

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
				myInsert.executeUpdate(""
						+ "UPDATE VLOGGER.PROPERTYCLASS SET PROPERTYCLASSNAME = '"+pcName+"', PROPERTYCLASSUNIT = '"+pcUnit+"',"
								+ "PROPERTYCLASSDATATYPE = '"+pcDataType+"' WHERE PROPERTYCLASSID = '"+pcID+"'");
						
				myInsert.close();

			} catch (SQLException e) {
				System.err.println("Could not connect to the database.\n");
				e.printStackTrace();

			}


		return "OK";

	}




	

	

}
