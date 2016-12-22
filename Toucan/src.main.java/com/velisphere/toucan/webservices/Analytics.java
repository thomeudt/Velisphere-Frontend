package com.velisphere.toucan.webservices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;

import com.velisphere.toucan.amqp.ServerParameters;
import com.velisphere.toucan.dataObjects.EndpointData;
import com.velisphere.toucan.dataObjects.EndpointPropertyLogData;
import com.velisphere.toucan.volt.VoltConnector;
import com.velisphere.toucan.xmlRootElements.EndpointPropertyLogElements;



@Path("/analytics")
public class Analytics {
		
	@GET
	@Path("/get/lastendpointlogtime/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getLastEndpointLogTime(@PathParam("param") String endpointID) {
		

		Connection conn;
		String timestamp = new String();

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
					.executeQuery("SELECT MAX(time_stamp) FROM vlogger.endpointpropertylog "
							+ "WHERE vlogger.endpointpropertylog.endpointid = '"
							+ endpointID
							+ "'");

			while (myResult.next()) {
				
				timestamp = myResult.getString(1);
				
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return timestamp;
				
	}
	

	
	@GET
	@Path("/get/sensorlogcount/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getSensorLogCount(@PathParam("param") String endpointID) {
		

		Connection conn;
		String rowcount = new String();

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
					.executeQuery("SELECT COUNT(propertyentry) FROM vlogger.endpointpropertylog "
							+ "WHERE vlogger.endpointpropertylog.endpointid = '"
							+ endpointID
							+ "'");

			while (myResult.next()) {
				
				rowcount = myResult.getString(1);
				
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return rowcount;
				
	}

	
	@GET
	@Path("/get/actorlogcount/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getActorLogCount(@PathParam("param") String endpointID) {
		

		Connection conn;
		String rowcount = new String();

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
					.executeQuery("SELECT COUNT(payload) FROM vlogger.actionexecutedlog "
							+ "WHERE vlogger.actionexecutedlog.actorid = '"
							+ endpointID
							+ "'");

			while (myResult.next()) {
				
				rowcount = myResult.getString(1);
				
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return rowcount;
				
	}
	
	@GET
	@Path("/get/sensorcurrentstate/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getSensorCurrentState(@PathParam("param") String endpointID, @QueryParam("propertyid") String propertyID) {
		

		Connection conn;
		String currentState = new String();

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
							+ propertyID
							+ "' ORDER BY time_stamp DESC LIMIT 1");
			
			while (myResult.next()) {
				
				currentState = myResult.getString(1);
				
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return currentState;
				
	}
	
	@GET
	@Path("/get/lastupdate/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getLastUpdate(@PathParam("param") String endpointID, @QueryParam("propertyid") String propertyID) {
		

		Connection conn;
		String timestamp = new String();

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
							+ propertyID
							+ "' ORDER BY time_stamp DESC LIMIT 1");
			
			while (myResult.next()) {
				
				timestamp = myResult.getString(2);
				
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return timestamp;
				
	}

	
	@GET
	@Path("/get/actorlastvalue/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getActorLastValue(@PathParam("param") String endpointID, @QueryParam("propertyid") String propertyID) {
		

		Connection conn;
		String lastValue = new String();

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
							+ propertyID
							+ "' ORDER BY time_stamp DESC LIMIT 1");
			
			while (myResult.next()) {
				
				lastValue = myResult.getString(1);
				
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return lastValue;
				
	}
	
	@GET
	@Path("/get/actortriggeredbysensor/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getActorTriggeredBySensor(@PathParam("param") String endpointID, @QueryParam("propertyid") String propertyID) {
		

		Connection conn;
		String sensor = new String();

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
					.executeQuery("SELECT actionid, sensorid, payload, time_stamp FROM vlogger.actionexecutedlog "
							+ "WHERE vlogger.actionexecutedlog.actorid = '"
							+ endpointID 
							+ "' AND vlogger.actionexecutedlog.propertyid = '"
							+ propertyID
							+ "' ORDER BY time_stamp DESC LIMIT 1");
			
			while (myResult.next()) {
				
				sensor = myResult.getString(1);
				
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

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
					.callProcedure("UI_SelectEndpointForEndpointID", sensor);

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

		return endpointForEndpointID.getName();
								
	}
	
	@GET
	@Path("/get/actorprocessedbyaction/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getActorProcessedByAction(@PathParam("param") String endpointID, @QueryParam("propertyid") String propertyID) {
		

		Connection conn;
		String action = new String();

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
					.executeQuery("SELECT actionid FROM vlogger.actionexecutedlog "
							+ "WHERE vlogger.actionexecutedlog.actorid = '"
							+ endpointID 
							+ "' AND vlogger.actionexecutedlog.propertyid = '"
							+ propertyID
							+ "' ORDER BY time_stamp DESC LIMIT 1");
			
			while (myResult.next()) {
				
				action = myResult.getString(1);
				
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}
		
		
	
		String actionName = new String();

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
					.executeQuery("SELECT actionname FROM vlogger.action "
							+ "WHERE vlogger.action.actionid = '"
							+ action 
							+ "' ORDER BY actionname DESC LIMIT 1");


			while (myResult.next()) {
				
				
				actionName = myResult.getString(1);
								
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return actionName;

	}


	
	
	@GET
	@Path("/get/endpointgeoposition/{param}")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getEndpointGeoPosition(@PathParam("param") String endpointID) {
	
	Connection conn;
	String geoposition = null;

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
				.executeQuery("SELECT vlogger.endpoint.endpointname, vlogger.endpoint.endpointclassid, "
						+ "vlogger.endpoint.endpointid, maxvals.maxtime_stamp, maxvals.propertyclassid, vlogger.endpointpropertylog.propertyentry "
						+ "FROM vlogger.endpointpropertylog "
						+ "JOIN"
						+ "(SELECT max(time_stamp) as maxtime_stamp, property.propertyclassid as propertyclassid FROM vlogger.endpointpropertylog "
						+ "JOIN vlogger.property on vlogger.property.propertyid = vlogger.endpointpropertylog.propertyid "
						+ "JOIN vlogger.propertyclass on vlogger.propertyclass.propertyclassid = vlogger.property.propertyclassid "
						+ "WHERE property.propertyclassid = 'PC_GEO_LATLON' "
						+ "GROUP BY endpointid, property.propertyid, property.propertyclassid) as maxvals "
						+ "ON maxvals.maxtime_stamp = vlogger.endpointpropertylog.time_stamp "
						+ "JOIN vlogger.endpoint ON vlogger.endpoint.endpointid = endpointpropertylog.endpointid "
						+ "WHERE vlogger.endpoint.endpointid = '"+endpointID+"' ORDER BY time_stamp");

		
		
		while (myResult.next()) {
		
			
			geoposition = myResult.getString(6);
			
			
		}

		mySelect.close();

	} catch (SQLException e) {
		System.err.println("Could not connect to the database.\n");
		e.printStackTrace();

	}

	return geoposition;

	}
	
	
	
	@GET
	@Path("/get/endpointpropertylog/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public EndpointPropertyLogElements getEndpointPropertyLog(@PathParam("param") String endpointID, @QueryParam("propertyid") String propertyID) {
		

		Connection conn;
		LinkedList<EndpointPropertyLogData> logData = new LinkedList<EndpointPropertyLogData>();

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
					.executeQuery("SELECT propertyname, propertyentry, time_stamp FROM vlogger.endpointpropertylog "
							+ "JOIN vlogger.property ON vlogger.endpointpropertylog.propertyid = vlogger.property.propertyid WHERE "
							+ "vlogger.endpointpropertylog.PROPERTYID = '"
							+ propertyID
							+ "' AND vlogger.endpointpropertylog.endpointid = '"
							+ endpointID
							+ "' ORDER BY TIME_STAMP");

			while (myResult.next()) {
				EndpointPropertyLogData logItem = new EndpointPropertyLogData();
				logItem.addPropertyValuePair(myResult.getString(1), myResult.getString(2));
				//logItem.addPropertyValuePair("Test", myResult.getString(2));
				logItem.setTimeStamp(myResult.getString(3));
				logData.add(logItem);
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}
		
		EndpointPropertyLogElements logElements = new EndpointPropertyLogElements();
		
		logElements.setPropertyData(logData);

		return logElements;
	
	}


}


