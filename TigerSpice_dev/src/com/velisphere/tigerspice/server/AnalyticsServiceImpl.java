package com.velisphere.tigerspice.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.UUID;

import javax.servlet.ServletContext;

import au.com.bytecode.opencsv.CSVWriter;

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
                    ("SELECT * FROM vlogger.endpointpropertylog WHERE PROPERTYID = '"+propertyID+"' AND ENDPOINTID = '"+endpointID+"' ORDER BY TIME_STAMP");

	 		
	 		
	 		while (myResult.next())
	 		{
	 			EndpointLogData logItem = new EndpointLogData();
	 			logItem.setPropertyID(myResult.getString(3));
	 			logItem.setValue(Double.parseDouble(myResult.getString(4)));
	 			logItem.setTimeStamp(myResult.getString(5));
	 			logData.add(logItem);
	 			//System.out.println("Retrieved: " + logItem.getValue());
	 		}
	        
	 		mySelect.close();
	 		
	 		
	 		
	         } catch (SQLException e)
	            {
	            System.err.println("Could not connect to the database.\n");
	            e.printStackTrace();
	           
	            }


			
		
		
		return logData;
	}
	
	@Override
	public String getEndpointLogAsFile(String endpointID,
			String propertyID) throws IOException {
		
		
		Connection conn;
		LinkedList<EndpointLogData> logData = new LinkedList<EndpointLogData>();
		
		ServletContext app = this.getServletContext();
    	
    	File tmpDir = (File) app.getAttribute("javax.servlet.context.tempdir");
		File tempFile = new File(tmpDir.getAbsolutePath(), UUID.randomUUID().toString()+".csv");
		
 		
		
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
                    ("SELECT * FROM vlogger.endpointpropertylog WHERE PROPERTYID = '"+propertyID+"' AND ENDPOINTID = '"+endpointID+"' ORDER BY TIME_STAMP");

	 		
	 		
	 		
	 		CSVWriter writer = new CSVWriter(new FileWriter(tempFile));
	 	   
	 	     
	 		
	 		
	 		while (myResult.next())
	 		{
	 			writer.writeAll(myResult, true);
	 			//System.out.println("Retrieved: " + logItem.getValue());
	 		}
	 	
	 		 writer.flush();
		 		writer.close();
		
	 		mySelect.close();
	 		
	 		
	 		
	         } catch (SQLException e)
	            {
	            System.err.println("Could not connect to the database.\n");
	            e.printStackTrace();
	           
	            }

		System.out.println("[IN] Creating temporary file at " + tempFile.getAbsolutePath());
		return tempFile.getAbsolutePath();
	}


}
