package com.velisphere.montana.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.voltdb.*;
import org.voltdb.client.*;

public class PreLoadPart {

	 public static void main(String[] args) throws Exception {

	        /*
	         * Instantiate a client and connect to the database.
	         */
	        org.voltdb.client.Client preLoader;
	        preLoader = ClientFactory.createClient();
	     // public static String volt_ip = "16.1.1.149"; // for local db
	    	// public static String volt_ip = "ec2-54-200-208-195.us-west-2.compute.amazonaws.com"; // for aws db
	        
	         preLoader.createConnection("16.1.1.110");
	        //preLoader.createConnection("54.200.77.57");
	        
	         Connection conn;
	         
	         
	         try
		        {
		        Class.forName("com.vertica.jdbc.Driver");
		        } catch (ClassNotFoundException e)
		           {
		           System.err.println("Could not find the JDBC driver class.\n");
		           e.printStackTrace();
		           return;
		           }
		      try
		         {
		         conn = DriverManager.getConnection
		            (
		            "jdbc:vertica://16.1.1.113:5433/VelisphereMart", "vertica", "1Suplies!"
		            );
		         
		         conn.setAutoCommit(true);
		 		System.out.println(" [OK] Connected to Vertica on address: "
		 				+ "16.1.1.113");
		         
		         } catch (SQLException e)
		            {
		            System.err.println("Could not connect to the database.\n");
		            e.printStackTrace();
		            return;
		            }
			 
			 
			 
		         
		        /*
		         * Load the database.
		         */
		      
		      
	         
	         
	        
	        /*
	         * Load the database.
	         */
	        
	        String userID = "617e43e0-00ad-4804-9618-5ff20d8e4931";
	        	        
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1011", "E1", userID);
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1022", "E2", userID);
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1023", "E3", userID);
	        preLoader.callProcedure("ENDPOINT_USER_LINK.insert", "1024", "E4", userID);
	        
	        System.out.println("Endpoints and Users linked");
	        
	        
	        preLoader.callProcedure("SPHERE_USER_LINK.insert", "1002", "1000", userID);
	        preLoader.callProcedure("SPHERE_USER_LINK.insert", "1003", "1001", userID);
	                
	        
	        
	        System.out.println("Spheres and Users linked");
	        
	        
	        
	        Statement myInsert = conn.createStatement();
		      
		    myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT_USER_LINK VALUES ('1021', 'E1', '"+userID+"')");
		    myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT_USER_LINK VALUES ('1022', 'E2', '"+userID+"')");
		    myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT_USER_LINK VALUES ('1023', 'E3', '"+userID+"')");
		    myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT_USER_LINK VALUES ('1024', 'E4', '"+userID+"')");
	        
		    myInsert.executeBatch();
		    
		    myInsert.close();
		    conn.close();
	        
	        System.out.println("Endpoints and Users linked");
	        
	        
	        System.out.println("Done!");
	        
	        /*
	         * Retrieve the message.
	         */
	
	 }
	
}
