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
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import nl.captcha.Captcha;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.mindrot.BCrypt;
import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;
import org.voltdb.types.TimestampType;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.helper.ErrorCodes;
import com.velisphere.tigerspice.shared.ActionData;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.UnprovisionedEndpointData;

public class EndpointServiceImpl extends RemoteServiceServlet implements
		EndpointService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Vector<EndpointData> getAllEndpointDetails()

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

		Vector<EndpointData> allEndPoints = new Vector<EndpointData>();
		try {

			final ClientResponse findAllUsers = voltCon.montanaClient
					.callProcedure("UI_SelectAllEndpoints");

			final VoltTable findAllUsersResults[] = findAllUsers.getResults();

			VoltTable result = findAllUsersResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					EndpointData endpointData = new EndpointData();
					endpointData.endpointId = result.getString("ENDPOINTID");
					endpointData.endpointName = result
							.getString("ENDPOINTNAME");
					endpointData.endpointclassId = result
							.getString("ENDPOINTCLASSID");
					endpointData.endpointProvDate = result
							.getTimestampAsTimestamp("ENDPOINTPROVDATE")
							.toString();
					allEndPoints.add(endpointData);

				}
			}

			// System.out.println(allEndPoints);

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

		return allEndPoints;
	}

	public LinkedList<EndpointData> getEndpointsForSphere(String sphereID)

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

		LinkedList<EndpointData> endPointsforSphere = new LinkedList<EndpointData>();
		try {

			final ClientResponse findAllEndpoints = voltCon.montanaClient
					.callProcedure("UI_SelectEndpointsForSphere", sphereID);

			final VoltTable findAllEndpointResults[] = findAllEndpoints
					.getResults();

			VoltTable result = findAllEndpointResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					EndpointData endpointData = new EndpointData();
					endpointData.endpointId = result.getString("ENDPOINTID");
					endpointData.endpointName = result
							.getString("ENDPOINTNAME");
					endpointData.endpointclassId = result
							.getString("ENDPOINTCLASSID");
					endpointData.endpointProvDate = result
							.getTimestampAsTimestamp("ENDPOINTPROVDATE")
							.toString();
					endPointsforSphere.add(endpointData);

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

		return endPointsforSphere;

	}

	public String addEndpointToSphere(String endpointID, String sphereID)

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

		String uuid = UUID.randomUUID().toString();

		try {
			voltCon.montanaClient.callProcedure("ENDPOINT_SPHERE_LINK.insert",
					uuid, endpointID, sphereID);
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
			myInsert.executeUpdate("INSERT INTO vlogger.ENDPOINT_SPHERE_LINK VALUES ('"
					+ uuid + "','" + endpointID + "','" + sphereID + "') ");

			myInsert.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return "OK";

	}

	public String removeEndpointFromSphere(String endpointID, String sphereID)

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
			voltCon.montanaClient.callProcedure("UI_DeleteEndpointFromSphere",
					endpointID, sphereID);
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

		// second delete from Vertica

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

			Statement myDelete = conn.createStatement();
			myDelete.executeUpdate("DELETE FROM vlogger.ENDPOINT_SPHERE_LINK WHERE ENDPOINTID = '"
					+ endpointID + "' AND SPHEREID = '" + sphereID + "'");

			myDelete.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return "OK";

	}

	public LinkedList<EndpointData> getEndpointsForUser(String userID)

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

		LinkedList<EndpointData> endPointsforUser = new LinkedList<EndpointData>();
		try {

			final ClientResponse findAllUsers = voltCon.montanaClient
					.callProcedure("UI_SelectEndpointsForUser", userID);

			final VoltTable findAllUsersResults[] = findAllUsers.getResults();

			VoltTable result = findAllUsersResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					EndpointData endpointData = new EndpointData();
					endpointData.endpointId = result.getString("ENDPOINTID");
					endpointData.endpointName = result
							.getString("ENDPOINTNAME");
					endpointData.endpointclassId = result
							.getString("ENDPOINTCLASSID");
					endpointData.endpointProvDate = result
							.getTimestampAsTimestamp("ENDPOINTPROVDATE")
							.toString();
					endPointsforUser.add(endpointData);

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

		return endPointsforUser;
	}

	public EndpointData getEndpointForEndpointID(String endpointID)

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

		EndpointData endpointForEndpointID = new EndpointData();
		try {

			final ClientResponse findEndpoint = voltCon.montanaClient
					.callProcedure("UI_SelectEndpointForEndpointID", endpointID);

			final VoltTable findEndpointResults[] = findEndpoint.getResults();

			VoltTable result = findEndpointResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					endpointForEndpointID.endpointId = result
							.getString("ENDPOINTID");
					endpointForEndpointID.endpointName = result
							.getString("ENDPOINTNAME");
					endpointForEndpointID.endpointclassId = result
							.getString("ENDPOINTCLASSID");
					endpointForEndpointID.endpointProvDate = result
							.getTimestampAsTimestamp("ENDPOINTPROVDATE")
							.toString();

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

		return endpointForEndpointID;
	}

	public String updateEndpointNameForEndpointID(String endpointID,
			String endpointName)

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
			voltCon.montanaClient.callProcedure(
					"UI_UpdateEndpointnameForEndpointID", endpointID,
					endpointName);
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

	public UnprovisionedEndpointData getUnprovisionedEndpoints(
			String identifier, String captchaWord)

	{

		// before all, validate Captcha

		UnprovisionedEndpointData uep = new UnprovisionedEndpointData();

		HttpServletRequest request = getThreadLocalRequest();
		HttpSession session = request.getSession();
		Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
		System.out.println("[IN] Captcha Word " + captchaWord);
		System.out.println("[IN] Captcha on Session " + Captcha.NAME);
		if (captcha.isCorrect(captchaWord)) {

			System.out.println("[IN] Correct Captcha");
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

				final ClientResponse findEndpoint = voltCon.montanaClient
						.callProcedure("UI_SelectUnprovisionedEPforIdentifier",
								identifier);

				final VoltTable findEndpointResults[] = findEndpoint
						.getResults();

				VoltTable result = findEndpointResults[0];
				// check if any rows have been returned

				while (result.advanceRow()) {
					{
						// extract the value in column checkid

						uep.setIdentifier(result.getString("IDENTIFIER"));
						uep.setUepid(result.getString("UEPID"));
						uep.setEpcId(result.getString("ENDPOINTCLASSID"));
						uep.setTimestamp(String.valueOf(result
								.getTimestampAsTimestamp("TIME_STAMP")));
						uep.setEndpointClassName(result
								.getString("ENDPOINTCLASSNAME"));

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

			if (uep.getTimestamp() != null) {
				System.out.println("Time "
						+ (new Date().getTime() - (Timestamp.valueOf(uep
								.getTimestamp()).getTime())));

				if ((new Date().getTime() - (Timestamp.valueOf(uep.time_stamp)
						.getTime())) > 600000000)
					uep = null;
				else
					uep.setSecondsSinceConnection(String.valueOf(((new Date()
							.getTime() - (Timestamp.valueOf(uep.time_stamp)
							.getTime())) / 1000)));
			} else
				uep = null;
		} else
			uep = null;
		return uep;
	}

	public String addNewEndpoint(String endpointID, String endpointName,
			String endpointclassID, String userID)

	{

		/*
		 * TODO: ADD ERROR HANDLING VERIFY IF DUPLICATE ENTRY!!!!!!!!!!
		 */

		// first add to VoltDB

		String errorTracker = new String("OK");

		VoltConnector voltCon = new VoltConnector();
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

			voltCon.montanaClient.callProcedure("ENDPOINT.insert", endpointID,
					endpointName, endpointclassID, new TimestampType());
			voltCon.montanaClient.callProcedure("ENDPOINT_USER_LINK.insert",
					linkID, endpointID, userID);
		} catch (NoConnectionsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ProcCallException e1) {
			errorTracker = ErrorCodes.VOLT_INSERTFAILED;

			System.out
					.println("[ER] ENDPOINT COULD NOT BE PROVISIONED, PROBABLY ALREADY EXISTS");

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
			myInsert.executeUpdate("INSERT INTO VLOGGER.ENDPOINT VALUES ('"
					+ endpointID + "','" + endpointName + "','"
					+ endpointclassID + "')");
			myInsert.executeUpdate("INSERT INTO VLOGGER.ENDPOINT_USER_LINK VALUES ('"
					+ linkID + "','" + endpointID + "','" + userID + "')");
			myInsert.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		// third add to rabbitMQ

		// create rabbitMQ account
		Client rabbitClient = ClientBuilder.newClient();
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(
				"veliadmin2014", "4GfQ2xgIwVsJ9g3wZIQE");
		rabbitClient.register(feature);
		WebTarget target = rabbitClient
				.target("http://16.1.1.107:15672/api/users/" + endpointID);
		Response response = target.request().put(
				Entity.json("{\"password\":\"" + endpointID
						+ "\",\"tags\":\"\"}")); // replace endpoint ID with API
													// Key later on

		System.out.println("[IN] RabbitMQ account creation started, result: "
				+ response);

		// allow access to clients virtual host

		target = rabbitClient
				.target("http://16.1.1.107:15672/api/permissions/hClients/"
						+ endpointID);
		response = target.request().put(
				Entity.json("{\"configure\":\"\",\"write\":\"\",\"read\":\""
						+ endpointID + ".*\"}"));
		System.out
				.println("[IN] RabbitMQ read permission for clients queue requested, result: "
						+ response);

		// allow access to controller virtual host

		target = rabbitClient
				.target("http://16.1.1.107:15672/api/permissions/hController/"
						+ endpointID);
		response = target
				.request()
				.put(Entity
						.json("{\"configure\":\"\",\"write\":\"controller\",\"read\":\"\"}"));
		System.out
				.println("[IN] RabbitMQ write permission for controller queue requested, result: "
						+ response);

		return errorTracker;

	}

	@Override
	public HashMap<String, LinkedList<String>> getLinksForEndpointList(
			LinkedList<String> endpointIDs) {

		/*
		 * Connect to VoltDB
		 */

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

		/*
		 * First of all, we need to retrieve all checkpaths that contain the
		 * check to be inspected
		 * 
		 * We can then retrieve the list of endpoints on which actions are
		 * executed in this checkpath
		 */

		HashMap<String, LinkedList<String>> linkedEndpointsForEndpointID = new HashMap<String, LinkedList<String>>();
	
		Iterator<String> it = endpointIDs.iterator();

		while (it.hasNext()) {
			
			final String endpointID = it.next();
			
			System.out.println("[DB] Looking for links belonging to " + endpointID);
			
			LinkedList<String> targetsForEndpointID = new LinkedList<String>();

			try {

				final ClientResponse findCheck = voltCon.montanaClient
						.callProcedure("UI_SelectChecksForEndpoint", endpointID);

				final VoltTable findCheckResults[] = findCheck.getResults();

				VoltTable result = findCheckResults[0];
				// check if any rows have been returned

				while (result.advanceRow()) 
					{
						/*
						 * Next we retrieve the query the actions to retrieve
						 * the contained target endpoints
						 */

						// checkpathsForEndpointID.add(result.getString("CHECKPATHID"));

						//LinkedList<String> targetsForCheckpathID = new LinkedList<String>();

						System.out.println("[DB] Looking for actions in checkpath " + result.getString("CHECKPATHID"));
					
						try {

							final ClientResponse findAction = voltCon.montanaClient
									.callProcedure(
											"UI_SelectActionsForCheckpathID",
											result.getString("CHECKPATHID"));

							final VoltTable findActionResult[] = findAction
									.getResults();

							VoltTable resultActionQuery = findActionResult[0];
							// check if any rows have been returned

							while (resultActionQuery.advanceRow()) 
								{
								
								if (resultActionQuery.getString("TARGETENDPOINTID").length()>0)
								{
									targetsForEndpointID.add(resultActionQuery
											.getString("TARGETENDPOINTID"));
									System.out.println("[DB] Found and adding to target list " + resultActionQuery.getString("TARGETENDPOINTID"));
	
								} else
									System.out.println("[DB] Discarding emptry result: " + resultActionQuery.getString("TARGETENDPOINTID"));

								
								}
							

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					
					
					
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
		
			if (linkedEndpointsForEndpointID.containsKey(endpointID)){
				System.out.println("[DB] Appending list of " + targetsForEndpointID.toString() + " to Key " + endpointID);
				System.out.println("[DB] >>>>");
				linkedEndpointsForEndpointID.get(endpointID).addAll(targetsForEndpointID);
			}
			else
			{
				System.out.println("[DB] Creating full list of " + targetsForEndpointID.toString() + " to Key " + endpointID);
				System.out.println("[DB] >>>>");
				linkedEndpointsForEndpointID.put(endpointID, targetsForEndpointID);
			}
		
			
		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("[DB] Returning " + linkedEndpointsForEndpointID.toString());
		return linkedEndpointsForEndpointID;
	}

}
