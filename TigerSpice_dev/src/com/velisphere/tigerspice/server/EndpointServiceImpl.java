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
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;
import org.voltdb.types.TimestampType;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.helper.VeliConstants;
import com.velisphere.tigerspice.shared.AlertData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.LogicLinkTargetData;
import com.velisphere.tigerspice.shared.UnprovisionedEndpointData;

public class EndpointServiceImpl extends RemoteServiceServlet implements
		EndpointService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LinkedList<EndpointData> getAllEndpointDetails()

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

		LinkedList<EndpointData> allEndPoints = new LinkedList<EndpointData>();
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
					endpointForEndpointID.endpointState = result
							.getString("ENDPOINTSTATE")
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
	
		// lookup secret
		
		String secret = "";
		
		ClientResponse findEndpoint;
		try {
			findEndpoint = voltCon.montanaClient
					.callProcedure("SEC_SelectSecretForUEPID",
							endpointID);
			
			final VoltTable findEndpointResults[] = findEndpoint
					.getResults();

			VoltTable result = findEndpointResults[0];
			// check if any rows have been returned

			
			
			while (result.advanceRow()) 
				{
					// extract the value in column checkid

					secret = result
							.getString("SECRET");

				}

		} catch (NoConnectionsException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ProcCallException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

				
		
		
		/*
		 * TODO: ADD ERROR HANDLING VERIFY IF DUPLICATE ENTRY!!!!!!!!!!
		 */

		// first add to VoltDB

		String errorTracker = new String("OK");

		
		String linkID = UUID.randomUUID().toString();

	

		try {

			voltCon.montanaClient.callProcedure("ENDPOINT.insert", endpointID,
					endpointName, endpointclassID, new TimestampType(), "UNKNOWN", secret);
			voltCon.montanaClient.callProcedure("ENDPOINT_USER_LINK.insert",
					linkID, endpointID, userID);
		} catch (NoConnectionsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ProcCallException e1) {
			errorTracker = VeliConstants.VOLT_INSERTFAILED;

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
				.target("http://"+ServerParameters.rabbit_ip+":15672/api/users/" + endpointID);
		Response response = target.request().put(
				Entity.json("{\"password\":\"" + endpointID
						+ "\",\"tags\":\"\"}")); // replace endpoint ID with API
													// Key later on

		System.out.println("[IN] RabbitMQ account creation started, result: "
				+ response);

		// allow access to clients virtual host

		target = rabbitClient
				.target("http://"+ServerParameters.rabbit_ip+":15672/api/permissions/hClients/"
						+ endpointID);
		response = target.request().put(
				Entity.json("{\"configure\":\"\",\"write\":\"\",\"read\":\""
						+ endpointID + ".*\"}"));
		System.out
				.println("[IN] RabbitMQ read permission for clients queue requested, result: "
						+ response);

		// allow access to controller virtual host

		target = rabbitClient
				.target("http://"+ServerParameters.rabbit_ip+":15672/api/permissions/hController/"
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
	public HashMap<String, LinkedList<LogicLinkTargetData>> getLinksForEndpointList(
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

		HashMap<String, LinkedList<LogicLinkTargetData>> linkedEndpointsForEndpointID = new HashMap<String, LinkedList<LogicLinkTargetData>>();

		Iterator<String> it = endpointIDs.iterator();

		while (it.hasNext()) {

			final String endpointID = it.next();

			System.out.println("[DB] Looking for links belonging to "
					+ endpointID);

			LinkedList<LogicLinkTargetData> targetsForEndpointID = new LinkedList<LogicLinkTargetData>();

			try {

				final ClientResponse findCheck = voltCon.montanaClient
						.callProcedure("UI_SelectChecksForEndpoint", endpointID);

				final VoltTable findCheckResults[] = findCheck.getResults();

				VoltTable result = findCheckResults[0];
				// check if any rows have been returned

				while (result.advanceRow()) {
					/*
					 * Next we retrieve the query the actions to retrieve the
					 * contained target endpoints
					 */

					// checkpathsForEndpointID.add(result.getString("CHECKPATHID"));

					// LinkedList<String> targetsForCheckpathID = new
					// LinkedList<String>();

					String checkpathID = result.getString("CHECKPATHID");

					System.out.println("[DB] Looking for actions in checkpath "
							+ checkpathID);

					try {

						final ClientResponse findAction = voltCon.montanaClient
								.callProcedure(
										"UI_SelectActionsForCheckpathID",
										checkpathID);

						final VoltTable findActionResult[] = findAction
								.getResults();

						VoltTable resultActionQuery = findActionResult[0];
						// check if any rows have been returned

						while (resultActionQuery.advanceRow()) {

							if (resultActionQuery.getString("TARGETENDPOINTID")
									.length() > 0) {

								LogicLinkTargetData targetData = new LogicLinkTargetData();
								targetData.setCheckpathID(checkpathID);
								targetData.setCheckpathName(resultActionQuery
										.getString("CHECKPATHNAME"));
								targetData
										.setTargetEndpointID(resultActionQuery
												.getString("TARGETENDPOINTID"));
								targetsForEndpointID.add(targetData);
								System.out
										.println("[DB] Found and adding to target list "
												+ targetData.toString());

							} else
								System.out
										.println("[DB] Discarding emptry result: "
												+ resultActionQuery
														.getString("TARGETENDPOINTID"));

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

			if (linkedEndpointsForEndpointID.containsKey(endpointID)) {
				System.out.println("[DB] Appending list of "
						+ targetsForEndpointID.toString() + " to Key "
						+ endpointID);
				System.out.println("[DB] >>>>");
				linkedEndpointsForEndpointID.get(endpointID).addAll(
						targetsForEndpointID);
			} else {
				System.out.println("[DB] Creating full list of "
						+ targetsForEndpointID.toString() + " to Key "
						+ endpointID);
				System.out.println("[DB] >>>>");
				linkedEndpointsForEndpointID.put(endpointID,
						targetsForEndpointID);
			}

		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("[DB] Returning "
				+ linkedEndpointsForEndpointID.toString());
		return linkedEndpointsForEndpointID;
	}

	@Override
	public LinkedList<EndpointData> getEndpointsForMultipleIDs(
			LinkedList<String> endpointIDs) {

		LinkedList<EndpointData> endpointsForMultipleEndpointIDs = new LinkedList<EndpointData>();

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

		Iterator<String> it = endpointIDs.iterator();
		while (it.hasNext()) {

			String endpointID = it.next();

			EndpointData endpointForEndpointID = new EndpointData();

			try {

				final ClientResponse findEndpoint = voltCon.montanaClient
						.callProcedure("UI_SelectEndpointForEndpointID",
								endpointID);

				final VoltTable findEndpointResults[] = findEndpoint
						.getResults();

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
						endpointsForMultipleEndpointIDs
								.add(endpointForEndpointID);

					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return endpointsForMultipleEndpointIDs;

	}
	
	@Override
	public String addNewAlert(AlertData alert)

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
	
		

		//  add to VoltDB

		
		String alertID = UUID.randomUUID().toString();
		alert.setAlertID(alertID);

		try {

			voltCon.montanaClient.callProcedure("ALERT.insert", alert.getAlertID(),
					alert.getUserID(), alert.getEndpointID(), alert.getAlertName(), alert.getProperty(), alert.getOperator(), alert.getThreshold(),
					alert.getType(), alert.getRecipient(), alert.getText(), alert.getCheckpathID());
			
			
		} catch (NoConnectionsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ProcCallException e1) {
			
			System.out
					.println("[ER] ALERT COULD NOT BE ADDED");
			
			e1.printStackTrace();

		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 
		return alertID;

	}
	
	
	@Override
	public LinkedHashMap<String, String> getAllAlertsForEndpoint(String endpointID)

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

		LinkedHashMap<String, String> allAlerts = new LinkedHashMap<String, String>();
		try {

			final ClientResponse findAllUsers = voltCon.montanaClient
					.callProcedure("UI_SelectAllAlertsForEndpoint", endpointID);

			final VoltTable findAllUsersResults[] = findAllUsers.getResults();

			VoltTable result = findAllUsersResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					
					allAlerts.put(result.getString("ALERTID"), result.getString("ALERTNAME"));
					
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

		return allAlerts;
	}
	
	@Override
	public LinkedList<AlertData> getAllAlertsForUser(String userID)

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

		LinkedList<AlertData> allAlerts = new LinkedList<AlertData>();
		try {

			
			 VoltTable[] results = voltCon.montanaClient.callProcedure("@AdHoc",
				       "SELECT * FROM ALERT " +
				       "WHERE USERID='" + userID+"'").getResults();
			
			

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					AlertData alert = new AlertData();
					alert.setAlertID(result.getString("ALERTID"));
					alert.setAlertName(result.getString("ALERTNAME"));
					alert.setCheckpathID(result.getString("CHECKPATHID"));
					alert.setEndpointID(result.getString("ENDPOINTID"));
					alert.setOperator(result.getString("OPERATOR"));
					alert.setProperty(result.getString("PROPERTY"));
					alert.setRecipient(result.getString("RECIPIENT"));
					alert.setText(result.getString("TEXT"));
					alert.setThreshold(result.getString("THRESHOLD"));
					alert.setType(result.getString("TYPE"));
					alert.setUserID(result.getString("USERID"));
					
					
					allAlerts.add(alert);
					
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

		return allAlerts;
	}
	
	
	
	@Override
	public AlertData getAlertDetails(String alertID)

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

		AlertData alert = new AlertData();
		
		try {

			final ClientResponse findAllUsers = voltCon.montanaClient
					.callProcedure("UI_SelectAllAlertDetails", alertID);

			final VoltTable findAllUsersResults[] = findAllUsers.getResults();

			VoltTable result = findAllUsersResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					
					alert.setAlertID(result.getString("ALERTID"));
					alert.setAlertName(result.getString("ALERTNAME"));
					alert.setCheckpathID(result.getString("CHECKPATHID"));
					alert.setEndpointID(result.getString("ENDPOINTID"));
					alert.setOperator(result.getString("OPERATOR"));
					alert.setProperty(result.getString("PROPERTY"));
					alert.setRecipient(result.getString("RECIPIENT"));
					alert.setText(result.getString("TEXT"));
					alert.setType(result.getString("TYPE"));
					alert.setThreshold(result.getString("THRESHOLD"));
					alert.setUserID(result.getString("USERID"));
					
					
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

		return alert;
	}


	
	@Override
	public String deleteAlert(String alertID, String checkpathID)

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
		
		
		// first we delete the alert from the alert table
		
		try {
			voltCon.montanaClient.callProcedure("ALERT.delete",
					alertID);
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
		
		
		// next we delete the related checkpath from the checkpath table
		
				try {
					voltCon.montanaClient.callProcedure("CHECKPATH.delete",
							checkpathID);
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
				
		// now we delete the check that is contained in this checkpath
				
				try {
					voltCon.montanaClient.callProcedure("@AdHoc",
				       "DELETE FROM CHECK " +
				       "WHERE CHECKPATHID='" + checkpathID + "'");
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
				
		// now we delete the action that is contained in this checkpath
				
				try {
					voltCon.montanaClient.callProcedure("@AdHoc",
				       "DELETE FROM ACTION " +
				       "WHERE CHECKPATHID='" + checkpathID + "'");
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

	// now we delete the outbound property action that is contained in this checkpath
				
				try {
					voltCon.montanaClient.callProcedure("@AdHoc",
				       "DELETE FROM OUTBOUNDPROPERTYACTION " +
				       "WHERE CHECKPATHID='" + checkpathID + "'");
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
	

}
