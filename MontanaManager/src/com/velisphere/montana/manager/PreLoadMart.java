package com.velisphere.montana.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.voltdb.*;
import org.voltdb.client.*;



public class PreLoadMart {
	
	private static Connection conn;
	
	 public static void main(String[] args) throws Exception {

		 
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
	      
	      Statement myInsert = conn.createStatement();
	    
	      /**
	      myInsert.addBatch("INSERT INTO VLOGGER.SPHERE VALUES ('1000', 'Home Tübingen', '0')");
	      myInsert.addBatch("INSERT INTO VLOGGER.SPHERE VALUES ('1001', 'My Car', '0')");
	      
	       	       
	        
	        System.out.println("Spheres loaded");
	        
	        myInsert.addBatch("INSERT INTO VLOGGER.USER VALUES ('1001', 'thmeu', 'thorsten@thorsten-meudt.de', '', 'PAYPERUSE')");
	        myInsert.addBatch("INSERT INTO VLOGGER.USER VALUES ('1001', 'utmeu', 'ute_lechner@yahoo.de', '', 'PAYPERUSE')");
	        myInsert.addBatch("INSERT INTO VLOGGER.USER VALUES ('1002', 'lcmeu', 'charlotte@thorsten-meudt.de', '', 'PAYPERUSE')");
	        	        
	        System.out.println("Users loaded");

	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINTCLASS VALUES ('EPC1', 'Blubber Messenger Client Account')");
	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINTCLASS VALUES ('EPC2', 'Generic Universal Sensor / Actor')");
	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINTCLASS VALUES ('EPC3', 'Generic Setter/Getter Application')");

	        	        
	        System.out.println("Endpointclasses loaded");
	        
	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT VALUES ('E1', 'PiPhidget', 'EPC2')");
  	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT VALUES ('E2', 'Blubber Thorsten', 'EPC1')");
  	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT VALUES ('E3', 'Blubber Ute', 'EPC1')");
  	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT VALUES ('E4', 'Reference Home Controller App', 'EPC3')");
	        
	        System.out.println("Endpoints loaded");

	        
	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT_USER_LINK VALUES ('1000', 'E1', '1000')");
	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT_USER_LINK VALUES ('1001', 'E2', '1000')");
	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT_USER_LINK VALUES ('1002', 'E3', '1001')");
  	        	        
	        System.out.println("Endpoints and Spheres linked");
	        
	        
	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT_SPHERE_LINK VALUES ('1000', 'E1', '1000')");
	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT_SPHERE_LINK VALUES ('1001', 'E2', '1000')");
	        myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINT_SPHERE_LINK VALUES ('1002', 'E3', '1001')");
	        	        
	        System.out.println("Endpoints and Users linked");
	        
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTYCLASS VALUES ('PC1', 'Text', 'String', '')");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTYCLASS VALUES ('PC2', 'Generic Digital Port', 'Byte', '')");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTYCLASS VALUES ('PC3', 'Brightness', 'Double', 'lux')");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTYCLASS VALUES ('PC4', 'On Off Switch', 'Byte', '')");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTYCLASS VALUES ('PC5', 'On Off Indicator', 'Byte', '')");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTYCLASS VALUES ('PC6', 'Touch', 'Double', '')");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTYCLASS VALUES ('PC7', 'Rotation', 'Double', '')");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTYCLASS VALUES ('PC8', 'Pressure', 'Double', '')");
	        	        	        	        
	        System.out.println("Property Classes loaded");
	        
	        **/
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR1', 'Switch 1', 'PC4', 'EPC3', 0, 1, 0, 0)");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR2', 'Switch 2', 'PC4', 'EPC3', 0, 1, 0, 0)");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR3', 'Switch 3', 'PC4', 'EPC3', 0, 1, 0, 0)");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR4', 'Switch 4', 'PC4', 'EPC3', 0, 1, 0, 0)");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR5', 'Switch 5', 'PC4', 'EPC3', 0, 1, 0, 0)");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR6', 'To Field', 'PC1', 'EPC1', 0, 1, 0, 0)");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR7', 'Send Content', 'PC1', 'EPC1', 1, 0, 0, 0)");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR8', 'Send Request', 'PC1', 'EPC1', 1, 0, 0, 0)");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR9', 'Receive Content', 'PC1', 'EPC1', 1, 0, 0, 0)");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR10', 'Measured Pressure', 'PC8', 'EPC2', 0, 1, 0, 0)");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR11', 'Measured Rotation', 'PC7', 'EPC2', 0, 1, 0, 0)");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR12', 'Measured Touch', 'PC6', 'EPC2', 0, 1, 0, 0)");
	        myInsert.addBatch("INSERT INTO VLOGGER.PROPERTY VALUES ('PR13', 'Measured Brightness', 'PC3', 'EPC2', 0, 1, 0, 0)");
	              
	        	        	        
	        System.out.println("Properties loaded");
	        /**
	        	        
	        myInsert.addBatch("INSERT INTO VLOGGER.CHECKPATH VALUES ('CP1', 'Dummy 1', '', '')");
	        myInsert.addBatch("INSERT INTO VLOGGER.CHECKPATH VALUES ('CP1', 'Dummy 2', '', '')");
	        myInsert.addBatch("INSERT INTO VLOGGER.CHECKPATH VALUES ('CP1', 'Dummy 3', '', '')");
	        myInsert.addBatch("INSERT INTO VLOGGER.CHECKPATH VALUES ('CP1', 'Dummy 4', '', '')");
	        myInsert.addBatch("INSERT INTO VLOGGER.CHECKPATH VALUES ('CP1', 'Dummy 5', '', '')");
	        myInsert.addBatch("INSERT INTO VLOGGER.CHECKPATH VALUES ('CP1', 'Dummy 6', '', '')");
	        
	        
	        System.out.println("Checkpaths loaded");

	        
	        myInsert.addBatch("INSERT INTO VLOGGER.ITEMCOST VALUES ('ACCOUNT', 'PAYPERUSE', 'Account fee per user account, per month')");
	        myInsert.addBatch("INSERT INTO VLOGGER.ITEMCOST VALUES ('CHECK_HIT', 'PAYPERUSE', 'Cost per Check Hit')");
	        myInsert.addBatch("INSERT INTO VLOGGER.ITEMCOST VALUES ('MULTICHECK_HIT', 'PAYPERUSE', 'Cost per Check Hit')");
	        myInsert.addBatch("INSERT INTO VLOGGER.ITEMCOST VALUES ('ACCOUNT', 'ECO1', 'Account fee per user account, per month')");
	        myInsert.addBatch("INSERT INTO VLOGGER.ITEMCOST VALUES ('CHECK_HIT', 'ECO1', 'Cost per Check Hit')");
	        myInsert.addBatch("INSERT INTO VLOGGER.ITEMCOST VALUES ('MULTICHECK_HIT', 'ECO1', 'Cost per Check Hit')");
	        
	        
	      	        
	        System.out.println("Item Costs loaded");
	        
	        myInsert.addBatch("INSERT INTO VLOGGER.PLAN VALUES ('PAYPERUSE', 'Pay per Use plan with no monthly subscription fee')");
	        myInsert.addBatch("INSERT INTO VLOGGER.PLAN VALUES ('ECO1', 'Light usage plan')");
	        
	        
	      	        
	        System.out.println("Plans loaded");
	      **/
	        myInsert.executeBatch();
	        myInsert.close();
	        conn.close();
	        
	        
	        System.out.println("Batch submitted");
	        
	        System.out.println("Done!");
	        
	        /*
	         * Retrieve the message.
	         */
	
	 }
	
}
