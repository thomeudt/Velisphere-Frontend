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
import java.util.Vector;

import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.spheres.SphereService;
import com.velisphere.tigerspice.client.users.UserService;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.SphereData;
import com.velisphere.tigerspice.shared.UserData;

@SuppressWarnings("serial")
public class SphereServiceImpl extends RemoteServiceServlet implements
		SphereService {

			
	
	public LinkedList<SphereData> getAllSpheres()

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

		LinkedList<SphereData> allSpheres = new LinkedList<SphereData>();
		try {

			final ClientResponse findAllSpheres = voltCon.montanaClient
					.callProcedure("UI_SelectAllSpheres");

			final VoltTable findAllSpheresResults[] = findAllSpheres.getResults();

			VoltTable result = findAllSpheresResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					SphereData sphereData = new SphereData();
					sphereData.sphereId = result.getString("SPHEREID");
					sphereData.sphereName = result.getString("SPHERENAME");
					
					
					Byte b;
					b = (Byte) result.get("PUBLIC", VoltType.TINYINT);
					sphereData.sphereIsPublic = b.intValue();
					
					allSpheres.add(sphereData);

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
		
		return allSpheres;
	}

	public String updateSpherenameForSphereID(String sphereID, String sphereName)

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
			voltCon.montanaClient.callProcedure("UI_UpdateSpherenameForSphereID",
					sphereID, sphereName);
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

	@Override
	public LinkedList<SphereData> getAllSpheresForUserID(String userID) {

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

		LinkedList<SphereData> allSpheres = new LinkedList<SphereData>();
		try {

			final ClientResponse findAllSpheres = voltCon.montanaClient
					.callProcedure("UI_SelectAllSpheres");

			final VoltTable findAllSpheresResults[] = findAllSpheres.getResults();

			VoltTable result = findAllSpheresResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					SphereData sphereData = new SphereData();
					sphereData.sphereId = result.getString("SPHEREID");
					sphereData.sphereName = result.getString("SPHERENAME");
					
					
					Byte b;
					b = (Byte) result.get("PUBLIC", VoltType.TINYINT);
					sphereData.sphereIsPublic = b.intValue();
					
					allSpheres.add(sphereData);

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
		
		return allSpheres;

	
	}
	
	public String addSphere(String sphereName)

	{

			
			// first add to VoltDB

			VoltConnector voltCon = new VoltConnector();
			String sphereID = UUID.randomUUID().toString();

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

				voltCon.montanaClient.callProcedure("SPHERE.insert", sphereID,
						sphereName, "0");
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
				myInsert.executeUpdate("INSERT INTO VLOGGER.SPHERE VALUES ('"
						+ sphereID + "','" + sphereName + "','0') ");

				myInsert.close();

			} catch (SQLException e) {
				System.err.println("Could not connect to the database.\n");
				e.printStackTrace();

			}


		return "OK";

	}
	
	@Override
	public String getSphereNameForSphereID(String sphereID) {

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

		String sphereName = new String();
		try {

			final ClientResponse findAllSpheres = voltCon.montanaClient
					.callProcedure("UI_SelectSphereForSphereID", sphereID);

			final VoltTable findAllSpheresResults[] = findAllSpheres.getResults();

			VoltTable result = findAllSpheresResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					sphereName = result.getString("SPHERENAME");
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
		
		return sphereName;

	
	}


	
}
