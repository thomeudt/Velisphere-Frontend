package com.velisphere.blender.webservices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.velisphere.blender.ConfigHandler;
import com.velisphere.blender.amqp.ServerParameters;
import com.velisphere.blender.amqp.VoltConnector;
import com.velisphere.blender.dataObjects.EndpointData;
import com.velisphere.blender.dataObjects.PropertyData;
import com.velisphere.blender.xmlRootElements.EndpointElement;
import com.velisphere.blender.xmlRootElements.EndpointElements;
import com.velisphere.blender.xmlRootElements.PropertyElement;
import com.velisphere.blender.xmlRootElements.PropertyElements;



@Path("/properties")
public class Properties {
		
	@GET
	@Path("/get/sensorsforendpoint/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public PropertyElements getSensorsForEndpoint(@PathParam("param") String endpointID) {
		

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();
		
		System.out.println(" [IN] Get All Sensors for Endpoint Called");

		PropertyElements propertyElements = new PropertyElements();
		propertyElements.setPropertyData(new LinkedList<PropertyData>());
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
			
			VoltTable[] results;
			
			results = voltCon.montanaClient.callProcedure(
					"UI_SelectSensePropertiesForEndpoint", endpointID).getResults();
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
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
				
				propertyElements.getPropertyData().add((propertyData));
				
				
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

		
		
		//endpointsForUser.setEndpointNames(endpointNames);
		
		return propertyElements;
	}


		
		@GET
		@Path("/get/sensorsstateforendpoint/{param}")
		@Produces({ MediaType.APPLICATION_JSON })
		public PropertyElements getSensorsStatesForEndpoint(@PathParam("param") String endpointID) {
			

			ConfigHandler conf = new ConfigHandler();
			conf.loadParamChangesAsXML();
			
			System.out.println(" [IN] Get All Sensors for Endpoint Called");

			PropertyElements propertyElements = new PropertyElements();
			propertyElements.setPropertyData(new LinkedList<PropertyData>());
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
				
				VoltTable[] results;
				
				results = voltCon.montanaClient.callProcedure(
						"UI_SelectSensePropertiesForEndpoint", endpointID).getResults();
				
				VoltTable result = results[0];
				// check if any rows have been returned

				while (result.advanceRow()) {
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
					

					// add property class dependent data
					
					VoltTable[] propertyClassResults;

					
					propertyClassResults = voltCon.montanaClient.callProcedure(
							"UI_SelectPropertyClassForPropertyClassID", propertyData.getPropertyclassId()).getResults();
					
					VoltTable propertyClassResult = propertyClassResults[0];
					// check if any rows have been returned

					while (propertyClassResult.advanceRow()) {
							// extract the value in column checkid
						
						propertyData.unitOfMeasure = propertyClassResult.getString("PROPERTYCLASSUNIT");
												
					}
					
					// add current state data
					
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

						Statement mySelect = conn.createStatement();

						ResultSet myResult = mySelect
								.executeQuery("SELECT propertyentry, time_stamp FROM vlogger.endpointpropertylog "
										+ "WHERE vlogger.endpointpropertylog.endpointid = '"
										+ endpointID 
										+ "' AND vlogger.endpointpropertylog.propertyid = '"
										+ propertyData.propertyId
										+ "' ORDER BY time_stamp DESC LIMIT 1");
						
						while (myResult.next()) {
							
							propertyData.currentValue = myResult.getString(1);
							propertyData.lastUpdate = myResult.getString(2);
							
							// System.out.println("Retrieved: " + logItem.getValue());
						}

						mySelect.close();

					} catch (SQLException e) {
						System.err.println("Could not connect to the database.\n");
						e.printStackTrace();

					}
							
					
					propertyElements.getPropertyData().add((propertyData));
					
					
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

			
			
			//endpointsForUser.setEndpointNames(endpointNames);
			
			return propertyElements;
		}

	
	
	@GET
	@Path("/get/actorsforendpoint/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public PropertyElements getActorsForEndpoint(@PathParam("param") String endpointID) {
		

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();
		
		System.out.println(" [IN] Get All Actors for Endpoint Called");

		PropertyElements propertyElements = new PropertyElements();
		propertyElements.setPropertyData(new LinkedList<PropertyData>());
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
			
			VoltTable[] results;
			
			results = voltCon.montanaClient.callProcedure(
					"UI_SelectActPropertiesForEndpoint", endpointID).getResults();
		
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
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
				
				propertyElements.getPropertyData().add((propertyData));
				
				
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

		
		
		//endpointsForUser.setEndpointNames(endpointNames);
		
		return propertyElements;
	}
	
	@GET
	@Path("/get/actorsstateforendpoint/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public PropertyElements getActorsStatesForEndpoint(@PathParam("param") String endpointID) {
		

		ConfigHandler conf = new ConfigHandler();
		conf.loadParamChangesAsXML();
		
		System.out.println(" [IN] Get All Sensors for Endpoint Called");

		PropertyElements propertyElements = new PropertyElements();
		propertyElements.setPropertyData(new LinkedList<PropertyData>());
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
			
			VoltTable[] results;
			
			results = voltCon.montanaClient.callProcedure(
					"UI_SelectActPropertiesForEndpoint", endpointID).getResults();
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
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
				

				// add property class dependent data
				
				VoltTable[] propertyClassResults;

				
				propertyClassResults = voltCon.montanaClient.callProcedure(
						"UI_SelectPropertyClassForPropertyClassID", propertyData.getPropertyclassId()).getResults();
				
				VoltTable propertyClassResult = propertyClassResults[0];
				// check if any rows have been returned

				while (propertyClassResult.advanceRow()) {
						// extract the value in column checkid
					
					propertyData.unitOfMeasure = propertyClassResult.getString("PROPERTYCLASSUNIT");
											
				}
				
				// add current state data
				
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

					Statement mySelect = conn.createStatement();

					ResultSet myResult = mySelect
							.executeQuery("SELECT payload, time_stamp FROM vlogger.actionexecutedlog "
									+ "WHERE vlogger.actionexecutedlog.actorid = '"
									+ endpointID 
									+ "' AND vlogger.actionexecutedlog.propertyid = '"
									+ propertyData.propertyId
									+ "' ORDER BY time_stamp DESC LIMIT 1");
					
					while (myResult.next()) {
						
						propertyData.setCurrentValue(myResult.getString(1));
						propertyData.lastUpdate = myResult.getString(2);
						
						// System.out.println("Retrieved: " + logItem.getValue());
					}

					mySelect.close();
					
					conn = DriverManager.getConnection("jdbc:vertica://"
							+ ServerParameters.vertica_ip + ":5433/VelisphereMart",
							"vertica", "1Suplies!");

					conn.setAutoCommit(true);
					System.out.println(" [OK] Connected to Vertica on address: "
							+ "16.1.1.113");

					mySelect = conn.createStatement();

					myResult = mySelect
							.executeQuery("SELECT actionid FROM vlogger.actionexecutedlog "
									+ "WHERE vlogger.actionexecutedlog.actorid = '"
									+ endpointID 
									+ "' AND vlogger.actionexecutedlog.propertyid = '"
									+ propertyData.propertyId
									+ "' ORDER BY time_stamp DESC LIMIT 1");
					
					while (myResult.next()) {
						
						propertyData.setProcessedByAction(myResult.getString(1));
						
						// System.out.println("Retrieved: " + logItem.getValue());
					}

					mySelect.close();
					
					conn = DriverManager.getConnection("jdbc:vertica://"
							+ ServerParameters.vertica_ip + ":5433/VelisphereMart",
							"vertica", "1Suplies!");

					conn.setAutoCommit(true);
					System.out.println(" [OK] Connected to Vertica on address: "
							+ "16.1.1.113");

					mySelect = conn.createStatement();

					myResult = mySelect
							.executeQuery("SELECT actionid, sensorid, payload, time_stamp FROM vlogger.actionexecutedlog "
									+ "WHERE vlogger.actionexecutedlog.actorid = '"
									+ endpointID 
									+ "' AND vlogger.actionexecutedlog.propertyid = '"
									+ propertyData.propertyId
									+ "' ORDER BY time_stamp DESC LIMIT 1");
					
					while (myResult.next()) {
						
						propertyData.setTriggeredBySensor(myResult.getString(1));
						
						// System.out.println("Retrieved: " + logItem.getValue());
					}

					mySelect.close();

					
					
					
					
					
				} catch (SQLException e) {
					System.err.println("Could not connect to the database.\n");
					e.printStackTrace();

				}
						
				
				propertyElements.getPropertyData().add((propertyData));
				
				
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

		
		
		//endpointsForUser.setEndpointNames(endpointNames);
		
		return propertyElements;
	}



	
}


