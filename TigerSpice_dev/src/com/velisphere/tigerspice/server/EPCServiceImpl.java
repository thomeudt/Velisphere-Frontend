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

import java.io.File;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import nl.captcha.Captcha;

import org.mindrot.BCrypt;
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;




import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.users.UserService;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.UserData;

@SuppressWarnings("serial")
public class EPCServiceImpl extends RemoteServiceServlet implements
		EPCService {

	
		
	
	public LinkedList<EPCData> getAllEndpointClassDetails()

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

		LinkedList<EPCData> allEndPointClasses = new LinkedList<EPCData>();
		try {

			final ClientResponse findAllUsers = voltCon.montanaClient
					.callProcedure("UI_SelectAllEndpointClasses");

			final VoltTable findAllUsersResults[] = findAllUsers.getResults();

			VoltTable result = findAllUsersResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					System.out.println("GETTING...");
					
					// extract the value in column checkid
					EPCData epcData = new EPCData();
					epcData.endpointclassName = result.getString("ENDPOINTCLASSNAME");
					epcData.endpointclassID = result.getString("ENDPOINTCLASSID");
					String imagePath = result.getString("ENDPOINTCLASSIMAGEURL");
					
					
					
					epcData.endpointclassImageURL = "/tigerspice_dev/tigerspiceDownloads?privateURL="+result.getString("ENDPOINTCLASSIMAGEURL")
							+ "&outboundFileName=EPC_image&persist=1";
					System.out.println("Got " + epcData.endpointclassImageURL);
					
					
					
					allEndPointClasses.add(epcData);

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
		
		return allEndPointClasses;
	}


	public EPCData getEndpointClassForEndpointClassID(String endpointClassID)

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

		EPCData endPointClass = new EPCData();
		try {

			final ClientResponse findEPC = voltCon.montanaClient
					.callProcedure("UI_SelectEndpointClassForEndpointClassID", endpointClassID);

			final VoltTable findEPCResults[] = findEPC.getResults();

			VoltTable result = findEPCResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					
					endPointClass.endpointclassName = result.getString("ENDPOINTCLASSNAME");
					endPointClass.endpointclassID = result.getString("ENDPOINTCLASSID");
					endPointClass.endpointclassPath = result.getString("ENDPOINTCLASSIMAGEURL");

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
		
		return endPointClass;
	}

	
	public String addEndpointClass(String epcName, String epcImageURL)

	{

			
			// first add to VoltDB

			VoltConnector voltCon = new VoltConnector();
			String epcID = UUID.randomUUID().toString();

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

				voltCon.montanaClient.callProcedure("ENDPOINTCLASS.insert", epcID,
						epcName, epcImageURL);
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
				myInsert.executeUpdate("INSERT INTO VLOGGER.ENDPOINTCLASS VALUES ('"
						+ epcID + "','" + epcName + "')");

				myInsert.close();

			} catch (SQLException e) {
				System.err.println("Could not connect to the database.\n");
				e.printStackTrace();

			}


		return "OK";

	}


	public String updateEndpointClass(String epcID, String epcName, String epcImageURL)

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

				voltCon.montanaClient.callProcedure("UI_UpdateEndpointClass", epcID,
						epcName, epcImageURL);
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
						+ "UPDATE VLOGGER.ENDPOINTCLASS SET ENDPOINTCLASSNAME = '"+epcName+"' WHERE ENDPOINTCLASSID = '"+epcID+"'");
						
				myInsert.close();

			} catch (SQLException e) {
				System.err.println("Could not connect to the database.\n");
				e.printStackTrace();

			}


		return "OK";

	}

	





}
