package com.velisphere.toucan.webservices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.velisphere.toucan.amqp.ServerParameters;



@Path("/analytics")
public class Analytics {
		
	@GET
	@Path("/get/lastendpointlogtime/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getEndpointsForSphere(@PathParam("param") String endpointID) {
		

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
	
	
	

}


