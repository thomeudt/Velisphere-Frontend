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
import java.util.LinkedList;
import java.util.UUID;
import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.spheres.SphereService;
import com.velisphere.tigerspice.shared.SphereData;

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
					.callProcedure("UI_SelectSpheresForUser", userID);

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
	
	public String addSphere(String userID, String sphereName)

	{

			
			// first add to VoltDB

			VoltConnector voltCon = new VoltConnector();
			String sphereID = UUID.randomUUID().toString();
			String linkID = UUID.randomUUID().toString();

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
				voltCon.montanaClient.callProcedure("SPHERE_USER_LINK.insert", linkID, sphereID,
						userID);
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
						+ sphereID + "','" + sphereName + "','0')");

				myInsert.close();

			} catch (SQLException e) {
				System.err.println("Could not connect to the database.\n");
				e.printStackTrace();

			}


		return "OK";

	}
	
	@Override
	public SphereData getSphereForSphereID(String sphereID) {

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

		SphereData sphereData = new SphereData();
		try {

			final ClientResponse findAllSpheres = voltCon.montanaClient
					.callProcedure("UI_SelectSphereForSphereID", sphereID);

			final VoltTable findAllSpheresResults[] = findAllSpheres.getResults();

			VoltTable result = findAllSpheresResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					sphereData.setName(result.getString("SPHERENAME"));
					sphereData.setIsPublic((int) result.getLong("PUBLIC"));
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
		
		return sphereData;

	
	}

	@Override
	public String updatePublicStateForSphereID(String sphereID, int publicState)

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
			voltCon.montanaClient.callProcedure("UI_UpdatePublicStateForSphereID",
					sphereID, publicState);
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
	public LinkedList<SphereData> getPublicSpheresForUserID(String userID) {

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
					.callProcedure("UI_SelectPublicSpheresForUser", userID);

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



	
}
