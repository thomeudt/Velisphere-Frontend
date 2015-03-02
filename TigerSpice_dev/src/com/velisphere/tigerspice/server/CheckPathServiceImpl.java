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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.rules.CheckPathService;
import com.velisphere.tigerspice.shared.ActionObject;
import com.velisphere.tigerspice.shared.CheckPathData;
import com.velisphere.tigerspice.shared.CheckPathObjectTree;
import com.velisphere.tigerspice.shared.CheckPathObjectData;
import com.velisphere.tigerspice.shared.LinkedPair;
import com.velisphere.tigerspice.shared.SerializableLogicConnector;
import com.velisphere.tigerspice.shared.SerializableLogicContainer;
import com.velisphere.tigerspice.shared.SerializableLogicLogicCheck;
import com.velisphere.tigerspice.shared.SerializableLogicPhysicalItem;

@SuppressWarnings("serial")
public class CheckPathServiceImpl extends RemoteServiceServlet implements
		CheckPathService {

	 
	/**
	 * 
	 */


	
	public String addNewUiObject(CheckPathObjectData uiObject)

	{
				
		ObjectMapper mapper = new ObjectMapper();
		
		
	 
		String jsonCheckpathObject = "E0"; 
		
		try {
	 				 
			// display to console
			jsonCheckpathObject = mapper.writeValueAsString(uiObject);
			System.out.println("JSON generiert: " + jsonCheckpathObject);
	 
		} catch (JsonGenerationException e) {
	 
			e.printStackTrace();
	 
		} catch (JsonMappingException e) {
	 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}
	 

		System.out.println("**************************ROTZE");
		
		

		return jsonCheckpathObject;

	}


	

	
	public String createJsonCheckpath(CheckPathObjectTree uiObject)

	{
				
		ObjectMapper mapper = new ObjectMapper();
		
		
	 
		String jsonCheckpathObject = "E0"; 
		
		try {
	 				 
			// display to console
			jsonCheckpathObject = mapper.writeValueAsString(uiObject);
			
			System.out.println("JSON generiert: " + jsonCheckpathObject);
	 
		} catch (JsonGenerationException e) {
	 
			e.printStackTrace();
	 
		} catch (JsonMappingException e) {
	 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}
	 
		return jsonCheckpathObject;

	}
	
	
	public String addNewMulticheck(String checkId, String operator, String multicheckName, String checkpathID, LinkedList<ActionObject> actions)

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
			voltCon.montanaClient.callProcedure("MULTICHECK.upsert",
					checkId, operator, 0, 0, multicheckName, checkpathID);
			
			System.out.println("Adding Actions: " + actions);
			Iterator<ActionObject> it = actions.iterator();
			while (it.hasNext()){
				ActionObject action = it.next();
				voltCon.montanaClient.callProcedure("ACTION.upsert",
						action.actionID, action.actionName, action.endpointID, "", 0, "", checkId, checkpathID);
				voltCon.montanaClient.callProcedure("OUTBOUNDPROPERTYACTION.upsert",
						action.actionID, action.propertyID, action.propertyIdIntake, "", "", action.manualValue, action.actionID, checkpathID);
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
								+ action.endpointID + "','','0','" + checkId + "','"
								+ "" + "','" + checkpathID + "')");

					}

					myInsert.close();

				} catch (SQLException e) {
					System.err.println("Could not connect to the database.\n");
					e.printStackTrace();

				}

				return "OK";
	}

	
	public String addNewMulticheckCheckLink(String multicheckId, String checkId, String checkPathId)

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
			voltCon.montanaClient.callProcedure("MULTICHECK_CHECK_LINK.upsert",
					UUID.randomUUID().toString(), multicheckId,  checkId, checkPathId);
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


	public String addNewMulticheckMulticheckLink(String multicheckLId, String multicheckRId, String checkPathId)

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
			voltCon.montanaClient.callProcedure("MULTICHECK_MULTICHECK_LINK.upsert",
					UUID.randomUUID().toString(), multicheckLId,  multicheckRId, checkPathId);
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


	public String addNewCheckpath(String checkpathName, String userID)

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

		String checkPathId = UUID.randomUUID().toString();
		
		try {
			voltCon.montanaClient.callProcedure("CHECKPATH.upsert",
					checkPathId, checkpathName, null, userID);
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

		return checkPathId;

	}
	
	public String addNewCheckpath(String checkpathName, String userID, String checkpathID)

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
			voltCon.montanaClient.callProcedure("CHECKPATH.upsert",
					checkpathID, checkpathName, null, userID);
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
	public String addNewCheckpath(String checkpathName, String userID, String checkpathID, String uiObject)

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
			voltCon.montanaClient.callProcedure("CHECKPATH.upsert",
					checkpathID, checkpathName, uiObject, userID);
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

	
	public String updateCheckpath(String checkpathID,  String uiObject)

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
			voltCon.montanaClient.callProcedure("UI_UpdateCheckpath",
					checkpathID, uiObject);
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

	
	public CheckPathObjectTree getUiObjectJSONForCheckpathID(String checkpathID)

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

		String uiObjectJSON = new String();
		CheckPathObjectTree uiObject = new CheckPathObjectTree();
		try {

			final ClientResponse findCheckpath= voltCon.montanaClient
					.callProcedure("UI_SelectCheckpathForCheckpathID", checkpathID);

			final VoltTable findCheckpathResults[] = findCheckpath.getResults();

			VoltTable result = findCheckpathResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					
					uiObjectJSON = result.getString("UIOBJECT");
					
				}
			}

			ObjectMapper mapper = new ObjectMapper();
			try {
			        
				uiObject = mapper.readValue(uiObjectJSON, CheckPathObjectTree.class);
				
			        System.out.println(uiObject);
			    } catch (JsonGenerationException e) {
			        System.out.println(e);
			        } catch (JsonMappingException e) {
			       System.out.println(e);
			    } catch (IOException e) {
			    System.out.println(e);
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

		return uiObject;
	}


	@Override
	public LinkedHashMap<String, String> getAllCheckpaths(String userID) {
		
		
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

			LinkedHashMap<String, String> allCheckPaths = new LinkedHashMap<String, String>();
			try {

				final ClientResponse findAllCheckpaths = voltCon.montanaClient
						.callProcedure("UI_SelectAllCheckpaths", userID);

				final VoltTable findAllCheckpathsResults[] = findAllCheckpaths.getResults();

				VoltTable result = findAllCheckpathsResults[0];
				// check if any rows have been returned

				while (result.advanceRow()) {
					{
						// extract the value in column checkid
						
						allCheckPaths.put(result.getString("CHECKPATHID"), result.getString("CHECKPATHNAME"));

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
			
			return allCheckPaths;
				
	}


	@Override
	public CheckPathData getCheckpathDetails(String checkpathId) 

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

			CheckPathData checkPath = new CheckPathData();
			
			try {

				final ClientResponse findCheckpath= voltCon.montanaClient
						.callProcedure("UI_SelectCheckpathForCheckpathID", checkpathId);

				final VoltTable findCheckpathResults[] = findCheckpath.getResults();

				VoltTable result = findCheckpathResults[0];
				// check if any rows have been returned

				while (result.advanceRow()) {
					{
						// extract the value in column checkid
						
						checkPath.checkpathId = result.getString("CHECKPATHID");
						checkPath.checkpathName = result.getString("CHECKPATHNAME");
						checkPath.uiObjectJSON = result.getString("UIOBJECT");
						
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

			return checkPath;
	
	}
	
	public String updateCheckpathName(String checkpathID,  String checkpathName)

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
			voltCon.montanaClient.callProcedure("UI_UpdateCheckpathName",
					checkpathID, checkpathName);
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


	public String updateMulticheck(String multicheckID, String multicheckOperator, String multicheckName, String checkpathID, LinkedList<ActionObject> actions)

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
			voltCon.montanaClient.callProcedure("UI_UpdateMulticheck",
					multicheckID, multicheckName, multicheckOperator);
		
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
		while (it.hasNext()){
			ActionObject action = it.next();
			try {
				/**
				voltCon.montanaClient.callProcedure("ACTION.insert",
						action.actionID, action.actionName, action.endpointID, "", 0, checkID, "", checkpathID);
				voltCon.montanaClient.callProcedure("OUTBOUNDPROPERTYACTION.insert",
						action.actionID, action.propertyID, action.propertyIdIndex, "", "", action.manualValue, action.actionID, checkpathID);
						**/
				voltCon.montanaClient.callProcedure("UI_UpsertActionsForMulticheckID",
						action.actionID, action.actionName, action.endpointID, multicheckID, checkpathID, action.propertyID, action.propertyIdIntake,action.manualValue);
			
				
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
						+ action.endpointID + "', TGTEPIDFROMINBOUNDPROP = '', EXPIRED = '0' WHERE CHECKID = '" + multicheckID + "'");

			}

			myUpdate.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}
		
		
		

		return "OK";

	}
	
	public String deleteMulticheckMulticheckLink(String parentMulticheckID)

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
			voltCon.montanaClient.callProcedure("UI_DeleteMulticheckMulticheckLink",
					parentMulticheckID);
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
	
	public String deleteMulticheckCheckLink(String parentMulticheckID)

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
			voltCon.montanaClient.callProcedure("UI_DeleteMulticheckCheckLink",
					parentMulticheckID);
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

	public String deleteMulticheck(String multicheckID)

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
			voltCon.montanaClient.callProcedure("UI_DeleteMulticheck",
					multicheckID);
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
	
	public String addNewCheckpathCheckLink(String checkpathId, String checkId)

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
			voltCon.montanaClient.callProcedure("CHECKPATH_CHECK_LINK.upsert",
					UUID.randomUUID().toString(), checkpathId,  checkId);
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

	public String addNewCheckpathMulticheckLink(String checkpathId, String multicheckId)

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
			voltCon.montanaClient.callProcedure("CHECKPATH_MULTICHECK_LINK.upsert",
					UUID.randomUUID().toString(), checkpathId,  multicheckId);
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
	public LinkedList<ActionObject> getActionsForMulticheckID(String multicheckID, String checkpathID) {

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
					.callProcedure("UI_SelectActionsForMulticheckID", checkpathID, multicheckID);

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
					action.propertyIdIntake = result.getString("INBOUNDPROPERTYID");
					action.manualValue = result.getString("CUSTOMPAYLOAD");	
					System.out.println("Inboundlentgh = " + result.getString("INBOUNDPROPERTYID").length());
					action.settingSourceIndex = "Manual entry"; // this is the default
					if (result.getString("INBOUNDPROPERTYID").length() > 0) action.settingSourceIndex = "Incoming value from sensor device";
					
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

	
	public String deleteAllActionsForMulticheckId(String multicheckID)

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
			voltCon.montanaClient.callProcedure("UI_DeleteAllActionsForMulticheckID",
					multicheckID);
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
	public String createJsonFromPhysical(
			 SerializableLogicPhysicalItem object) {
					
			ObjectMapper mapper = new ObjectMapper();
			
			System.out.println("Intake: " + object.toString());
		 
			String jsonCheckpathObject = "E0"; 
			
			try {
				
				
		 				 
				// display to console
				jsonCheckpathObject = mapper.writeValueAsString(object);
				System.out.println("JSON generiert: " + jsonCheckpathObject);
		 
			} catch (JsonGenerationException e) {
		 
				e.printStackTrace();
		 
			} catch (JsonMappingException e) {
		 
				e.printStackTrace();
		 
			} catch (IOException e) {
		 
				e.printStackTrace();
		 
			}
		 

			return jsonCheckpathObject;

		}
	


	@Override
	public String createJsonFromLogical(
			 SerializableLogicLogicCheck object) {
					
			ObjectMapper mapper = new ObjectMapper();
			
			System.out.println("Intake: " + object.toString());
		 
			String jsonCheckpathObject = "E0"; 
			
			try {
				
				
		 				 
				// display to console
				jsonCheckpathObject = mapper.writeValueAsString(object);
				System.out.println("JSON generiert: " + jsonCheckpathObject);
		 
			} catch (JsonGenerationException e) {
		 
				e.printStackTrace();
		 
			} catch (JsonMappingException e) {
		 
				e.printStackTrace();
		 
			} catch (IOException e) {
		 
				e.printStackTrace();
		 
			}
		 

			return jsonCheckpathObject;

		}





	@Override
	public String createJsonFromConnector(SerializableLogicConnector object) {
		ObjectMapper mapper = new ObjectMapper();
		
		System.out.println("Intake: " + object.toString());
	 
		String jsonCheckpathObject = "E0"; 
		
		try {
			
			
	 				 
			// display to console
			jsonCheckpathObject = mapper.writeValueAsString(object);
			System.out.println("JSON generiert: " + jsonCheckpathObject);
	 
		} catch (JsonGenerationException e) {
	 
			e.printStackTrace();
	 
		} catch (JsonMappingException e) {
	 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}
	 

		return jsonCheckpathObject;

	}
	
	
	@Override
	public String createJsonFromContainer(SerializableLogicContainer object) {
		ObjectMapper mapper = new ObjectMapper();
		
		System.out.println("Intake: " + object.toString());
	 
		String jsonCheckpathObject = ""; 
		
		try {
			
			
	 				 
			// display to console
			jsonCheckpathObject = mapper.writeValueAsString(object);
			System.out.println("JSON generiert: " + jsonCheckpathObject);
	 
		} catch (JsonGenerationException e) {
	 
			e.printStackTrace();
	 
		} catch (JsonMappingException e) {
	 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}
	 

		return jsonCheckpathObject;

	}
	
	@Override
	public SerializableLogicContainer loadJsonToContainer(String checkpathID)

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

		String uiObjectJSON = new String();
		SerializableLogicContainer serializableLogicContainer = new SerializableLogicContainer();
		try {

			final ClientResponse findCheckpath= voltCon.montanaClient
					.callProcedure("UI_SelectCheckpathForCheckpathID", checkpathID);

			final VoltTable findCheckpathResults[] = findCheckpath.getResults();

			VoltTable result = findCheckpathResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					
					uiObjectJSON = result.getString("UIOBJECT");
					
				}
			}

			ObjectMapper mapper = new ObjectMapper();
			try {
			        
				serializableLogicContainer = mapper.readValue(uiObjectJSON, SerializableLogicContainer.class);
				
			        System.out.println(serializableLogicContainer);
			    } catch (JsonGenerationException e) {
			        System.out.println(e);
			        } catch (JsonMappingException e) {
			       System.out.println(e);
			    } catch (IOException e) {
			    System.out.println(e);
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

		return serializableLogicContainer;
	}

	
	
	
	
}
