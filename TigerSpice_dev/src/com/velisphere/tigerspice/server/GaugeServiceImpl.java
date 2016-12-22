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
import java.util.LinkedList;
import java.util.UUID;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;
import org.voltdb.types.TimestampType;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.dash.GaugeService;
import com.velisphere.tigerspice.client.helper.VeliConstants;
import com.velisphere.tigerspice.shared.ActionData;
import com.velisphere.tigerspice.shared.DashData;
import com.velisphere.tigerspice.shared.FavoriteData;
import com.velisphere.tigerspice.shared.GaugeData;
import com.velisphere.tigerspice.shared.SerializableLogicContainer;

@SuppressWarnings("serial")
public class GaugeServiceImpl extends RemoteServiceServlet implements
		GaugeService {

	
	@Override
	public String saveDashboard(String userID, String dashName, String dashID, 
			LinkedList<GaugeData> gaugeDatas) {
	
		ObjectMapper mapper = new ObjectMapper();
		String jsonGauges = "";
		 		
		try {
	 				 
			// create JSON data
			jsonGauges = mapper.writeValueAsString(gaugeDatas);
			
			//System.out.println("JSON generiert: " + jsonGauges);
	 
			
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

				
				voltCon.montanaClient.callProcedure("DASHBOARD.upsert", dashID,
						dashName, jsonGauges, userID);
				
			} catch (NoConnectionsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ProcCallException e1) {

			}

			try {
				voltCon.closeDatabase();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (JsonGenerationException e) {
	 
			e.printStackTrace();
	 
		} catch (JsonMappingException e) {
	 
			e.printStackTrace();
	 
		} catch (IOException e) {
	 
			e.printStackTrace();
	 
		}

		
		
		return dashID;
	}
	

	@Override
	public String deleteDashboard(String dashID) {
	
		 		
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

				
				voltCon.montanaClient.callProcedure("DASHBOARD.delete", dashID);
				
			} catch (NoConnectionsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ProcCallException e1){
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
	public String getDashboardName(String dashID) {
	

			String dashName = "";
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

				
				VoltTable[] results = voltCon.montanaClient.callProcedure("@AdHoc",
					       "SELECT NAME FROM DASHBOARD WHERE DASHBOARDID = '"+dashID+"' ORDER BY NAME ASC").getResults();

				VoltTable result = results[0];
				// check if any rows have been returned

				while (result.advanceRow()) {
					{
						dashName = result.getString("NAME");
						
					}
				}

				
			} catch (NoConnectionsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ProcCallException e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		

			
			

			try {
				voltCon.closeDatabase();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		
	
		return dashName;
}

	
	@Override
	public LinkedList<DashData> getAllDashboardsForUser(String userID)

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

		LinkedList<DashData> allDashes = new LinkedList<DashData>();
		try {

			
			 VoltTable[] results = voltCon.montanaClient.callProcedure("@AdHoc",
				       "SELECT DASHBOARDID, NAME, USERID FROM DASHBOARD WHERE USERID = '"+userID+"' ORDER BY NAME ASC").getResults();
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					DashData dash = new DashData();
					dash.setDashboardID(result.getString("DASHBOARDID"));
					dash.setDashboardName(result.getString("NAME"));
					
					// Leave out JSON to improve performance when only reading names to populate tabpane
					
					//dash.setJson(result.getString("JSON"));
					
					dash.setUserID(result.getString("USERID"));
					
					allDashes.add(dash);
					
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

		return allDashes;
	}

	
	@Override
	public LinkedList<GaugeData> getGaugesForDashID(String dashID)

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


		LinkedList<GaugeData> gaugeDatas = new LinkedList<GaugeData>();
		
		DashData dash = new DashData();
		try {

			
			 VoltTable[] results = voltCon.montanaClient.callProcedure("@AdHoc",
				       "SELECT * FROM DASHBOARD WHERE DASHBOARDID = '" + dashID + "' ORDER BY NAME ASC").getResults();
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
					dash.setDashboardID(result.getString("DASHBOARDID"));
					dash.setDashboardName(result.getString("NAME"));
					dash.setJson(result.getString("JSON"));
					dash.setUserID(result.getString("USERID"));
					
				
			}
			ObjectMapper mapper = new ObjectMapper();
			try {
			        
				gaugeDatas = mapper.readValue(dash.getJson(), new TypeReference<LinkedList<GaugeData>>() { });
				
			     } catch (JsonGenerationException e) {
			        System.out.println(e);
			        } catch (JsonMappingException e) {
			       System.out.println(e);
			    } catch (IOException e) {
			    System.out.println(e);
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

		return gaugeDatas;
	}

	
	
}
