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

import java.awt.List;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;
import java.util.Vector;

import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.shared.ActionObject;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.CheckPathData;
import com.velisphere.tigerspice.shared.EndpointData;

@SuppressWarnings("serial")
public class CheckServiceImpl extends RemoteServiceServlet implements
		CheckService {

	/**
	 * 
	 */

	public Vector<CheckData> getChecksForEndpointID(String endpointID)

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

		Vector<CheckData> checksForEndpointID = new Vector<CheckData>();
		try {

			final ClientResponse findCheck = voltCon.montanaClient
					.callProcedure("UI_SelectChecksForEndpoint", endpointID);

			final VoltTable findCheckResults[] = findCheck.getResults();

			VoltTable result = findCheckResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					CheckData check = new CheckData();
					check.checkId = result.getString("CHECKID");
					check.endpointId = result.getString("ENDPOINTID");
					check.checkName = result.getString("NAME");
					check.checkValue = result.getString("CHECKVALUE");

					Byte expired;
					expired = (Byte) result.get("EXPIRED", VoltType.TINYINT);
					check.expired = expired.byteValue();

					check.operator = result.getString("OPERATOR");
					check.propertyId = result.getString("PROPERTYID");

					Byte state;
					state = (Byte) result.get("STATE", VoltType.TINYINT);
					check.state = state.byteValue();

					checksForEndpointID.add(check);

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

		return checksForEndpointID;
	}

	public String addNewCheck(String checkID, String endpointID,
			String propertyID, String checkValue, String operator, String name,
			String checkpathID, LinkedList<ActionObject> actions)

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
			voltCon.montanaClient.callProcedure("CHECK.insert", checkID,
					endpointID, propertyID, checkValue, operator, 0, 0, name,
					checkpathID);
			voltCon.montanaClient.callProcedure("CHECKSTATE.insert", checkID,
					0, checkpathID);
			System.out.println("Adding Actions: " + actions);
			Iterator<ActionObject> it = actions.iterator();
			while (it.hasNext()) {
				ActionObject action = it.next();
				voltCon.montanaClient.callProcedure("ACTION.insert",
						action.actionID, action.actionName, action.endpointID,
						"", 0, checkID, "", checkpathID);
				voltCon.montanaClient.callProcedure(
						"OUTBOUNDPROPERTYACTION.insert", action.actionID,
						action.propertyID, action.propertyIdIndex, "", "",
						action.manualValue, action.actionID, checkpathID);
			}

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
					+ ServerParameters.vertica_ip);

			Statement myInsert = conn.createStatement();

			Iterator<ActionObject> it = actions.iterator();
			while (it.hasNext()) {
				ActionObject action = it.next();
				myInsert.executeUpdate("INSERT INTO VLOGGER.ACTION VALUES ('"
						+ action.actionID + "','" + action.actionName + "','"
						+ action.endpointID + "','','0','" + checkID + "','"
						+ "" + "','" + checkpathID + "')");

			}

			myInsert.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return "OK";

	}

	public String updateCheck(String checkID, String name, String checkValue,
			String operator, String checkpathID,
			LinkedList<ActionObject> actions)

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
			voltCon.montanaClient.callProcedure("UI_UpdateCheck", checkID,
					name, checkValue, operator);
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

		System.out.println("Updating Actions: " + actions);
		Iterator<ActionObject> it = actions.iterator();
		while (it.hasNext()) {
			ActionObject action = it.next();
			try {
				/**
				 * voltCon.montanaClient.callProcedure("ACTION.insert",
				 * action.actionID, action.actionName, action.endpointID, "", 0,
				 * checkID, "", checkpathID);
				 * voltCon.montanaClient.callProcedure
				 * ("OUTBOUNDPROPERTYACTION.insert", action.actionID,
				 * action.propertyID, action.propertyIdIndex, "", "",
				 * action.manualValue, action.actionID, checkpathID);
				 **/
				voltCon.montanaClient.callProcedure(
						"UI_UpsertActionsForCheckID", action.actionID,
						action.actionName, action.endpointID, checkID,
						checkpathID, action.propertyID, action.propertyIdIndex,
						action.manualValue);
				System.out.println("PROPIDINDEX: " + action.propertyIdIndex);

			} catch (IOException | ProcCallException e) {
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
		
		
		// second update in Vertica

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
					+ ServerParameters.vertica_ip);

			Statement myUpdate = conn.createStatement();

			Iterator<ActionObject> itV = actions.iterator();
			while (itV.hasNext()) {
				ActionObject action = itV.next();
				myUpdate.executeUpdate("UPDATE VLOGGER.ACTION SET ACTIONNAME = '" 
						+ action.actionName + "', TARGETENDPOINTID = '"
						+ action.endpointID + "', TGTEPIDFROMINBOUNDPROP = '', EXPIRED = '0' WHERE CHECKID = '" + checkID + "'");

			}

			myUpdate.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}
		
		

		return "OK";

	}

	public String deleteCheck(String checkID)

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
			voltCon.montanaClient.callProcedure("UI_DeleteCheck", checkID);
			voltCon.montanaClient.callProcedure("UI_DeleteCheckState", checkID);
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

	public Vector<CheckData> getChecksForUserID(String userID)

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

		Vector<EndpointData> endPointsforUser = new Vector<EndpointData>();
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
					endPointsforUser.add(endpointData);

				}
			}

			// System.out.println(endPointsforSphere);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Vector<CheckData> checksForEndpointID = new Vector<CheckData>();
		try {

			Iterator<EndpointData> it = endPointsforUser.iterator();

			while (it.hasNext()) {

				final ClientResponse findCheck = voltCon.montanaClient
						.callProcedure("UI_SelectChecksForEndpoint",
								it.next().endpointId);

				final VoltTable findCheckResults[] = findCheck.getResults();

				VoltTable result = findCheckResults[0];
				// check if any rows have been returned

				while (result.advanceRow()) {
					{
						// extract the value in column checkid

						CheckData check = new CheckData();
						check.checkId = result.getString("CHECKID");
						check.endpointId = result.getString("ENDPOINTID");
						check.checkName = result.getString("NAME");
						check.checkValue = result.getString("CHECKVALUE");

						Byte expired;
						expired = (Byte) result
								.get("EXPIRED", VoltType.TINYINT);
						check.expired = expired.byteValue();

						check.operator = result.getString("OPERATOR");
						check.propertyId = result.getString("PROPERTYID");

						Byte state;
						state = (Byte) result.get("STATE", VoltType.TINYINT);
						check.state = state.byteValue();

						checksForEndpointID.add(check);

					}
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

		return checksForEndpointID;
	}

	@Override
	public String getCheckNameForCheckID(String checkID) {

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

		String checkName = new String();

		try {

			final ClientResponse findCheck = voltCon.montanaClient
					.callProcedure("UI_SelectCheckForCheckID", checkID);

			final VoltTable findCheckResults[] = findCheck.getResults();

			VoltTable result = findCheckResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					checkName = result.getString("NAME");

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

		return checkName;

	}

	@Override
	public LinkedList<ActionObject> getActionsForCheckID(String checkID,
			String checkpathID) {

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

		LinkedList<ActionObject> actions = new LinkedList<ActionObject>();

		try {

			final ClientResponse findActions = voltCon.montanaClient
					.callProcedure("UI_SelectActionsForCheckID", checkpathID,
							checkID);

			final VoltTable findActionsResults[] = findActions.getResults();

			VoltTable result = findActionsResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					ActionObject action = new ActionObject();
					action.actionID = result.getString("ACTIONID");
					action.actionName = result.getString("ACTIONNAME");
					action.endpointID = result.getString("TARGETENDPOINTID");
					action.propertyID = result.getString("OUTBOUNDPROPERTYID");
					action.propertyIdIndex = result
							.getString("INBOUNDPROPERTYID");
					action.manualValue = result.getString("CUSTOMPAYLOAD");
					System.out.println("Inboundlentgh = "
							+ result.getString("INBOUNDPROPERTYID").length());
					action.settingSourceIndex = "Manual entry"; // this is the
																// default
					if (result.getString("INBOUNDPROPERTYID").length() > 0)
						action.settingSourceIndex = "Incoming value from sensor device";

					actions.add(action);
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

		return actions;

	}

	public String deleteAllActionsForCheckId(String checkID)

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
					"UI_DeleteAllActionsForCheckID", checkID);
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
