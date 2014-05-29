package com.velisphere.tigerspice.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.shared.EndpointLogData;

public class AnalyticsServiceImpl extends RemoteServiceServlet implements
	AnalyticsService {

	@Override
	public LinkedList<EndpointLogData> getEndpointLog(String endpointID,
			String propertyID) {
		
		
		Connection conn;
		LinkedList<EndpointLogData> logData = new LinkedList<EndpointLogData>();
		
		 try
	        {
	        Class.forName("com.vertica.jdbc.Driver");
	        } catch (ClassNotFoundException e)
	           {
	           System.err.println("Could not find the JDBC driver class.\n");
	           e.printStackTrace();
	           
	           }
	      try
	         {
	         conn = DriverManager.getConnection
	            (
	            "jdbc:vertica://"+ServerParameters.vertica_ip+":5433/VelisphereMart", "vertica", "1Suplies!"
	            );
	         
	         conn.setAutoCommit(true);
	 		System.out.println(" [OK] Connected to Vertica on address: "
	 				+ "16.1.1.113");
	 		
	 		Statement mySelect = conn.createStatement();
	 		
	 		ResultSet myResult = mySelect.executeQuery
                    ("SELECT * FROM vlogger.endpointpropertylog WHERE PROPERTYID = '"+propertyID+"' AND ENDPOINTID = '"+endpointID+"' LIMIT 1000");

	 		
	 		
	 		while (myResult.next())
	 		{
	 			EndpointLogData logItem = new EndpointLogData();
	 			logItem.setPropertyID(myResult.getString(3));
	 			logItem.setValue(myResult.getString(4));
	 			logItem.setTimeStamp(myResult.getString(5));
	 			logData.add(logItem);
	 			System.out.println("Retrieved: " + myResult.getString(4));
	 		}
	        
	 		mySelect.close();
	 		
	 		
	 		
	         } catch (SQLException e)
	            {
	            System.err.println("Could not connect to the database.\n");
	            e.printStackTrace();
	           
	            }


			
		
		
		return logData;
	}

}
