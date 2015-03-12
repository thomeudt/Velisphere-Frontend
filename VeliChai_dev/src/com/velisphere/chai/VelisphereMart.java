package com.velisphere.chai;

import java.sql.*;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TimeZone;
import java.util.UUID;

import com.velisphere.chai.dataObjects.ActionObject;
import com.velisphere.chai.dataObjects.CheckObject;

public class VelisphereMart {


  private static Connection conn;

  /* This example connects to Vertica and issues a simple select */

  public static void connect()
     {
     
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
            "jdbc:vertica://"+ServerParameters.vertica_ip+":5433/VelisphereMart", "vertica", "1Suplies!"
            );
         
         conn.setAutoCommit(true);
 		System.out.println(" [OK] Connected to Vertica on address: "
 				+ ServerParameters.vertica_ip);
         
         } catch (SQLException e)
            {
            System.err.println("Could not connect to the database.\n");
            e.printStackTrace();
            return;
            }
      } 
  
  
  public static void insertTransactionLog(String transactionID, String endpointID, String propertyID, String entry, LinkedList<ActionObject> executedActions,
		  LinkedList<CheckObject> checks, LinkedList<String> checkPaths)
  {
   try
      {
      Statement myInsert = conn.createStatement();
      
      myInsert.addBatch("INSERT INTO VLOGGER.ENDPOINTPROPERTYLOG VALUES ('"+transactionID+"', '"+endpointID+"', '"+propertyID+"', '"+entry+"', STATEMENT_TIMESTAMP())");
      
      Iterator<ActionObject> aIT = executedActions.iterator();
      
      while (aIT.hasNext()){
    	  ActionObject executedAction = aIT.next();
    	  myInsert.addBatch("INSERT INTO VLOGGER.ACTIONEXECUTEDLOG VALUES ('"+transactionID+"', '"+executedAction.getActionID()+"', '"+executedAction.getSensorID()+"', '"+executedAction.getActorID()+"', '"+executedAction.getPayload()+"', STATEMENT_TIMESTAMP(), '"+executedAction.getPropertyID()+"')");  
    	  
      }
      
      Iterator<CheckObject> cIT = checks.iterator();
      
      while (cIT.hasNext()){
    	  CheckObject executedCheck = cIT.next();
    	 
    	  myInsert.addBatch("INSERT INTO VLOGGER.CHECKEXECUTEDLOG VALUES ('"+transactionID+"', '"+executedCheck.getCheckID()+"', '"+executedCheck.getCheckValue()+"', '"+executedCheck.getHit()+"', STATEMENT_TIMESTAMP())");  
      }
      
      Iterator<String> cpIT = checkPaths.iterator();
      
      while (cpIT.hasNext()){
    	  String executedCheckpathID = cpIT.next();
    	 
    	  myInsert.addBatch("INSERT INTO VLOGGER.CHECKPATHEXECUTEDLOG VALUES ('"+transactionID+"', '"+executedCheckpathID+"', STATEMENT_TIMESTAMP())");  
      }
      
      myInsert.executeBatch();
      myInsert.close();
      
      } catch (SQLException e)
         {
         System.err.println("Could not connect to the database.\n");
         e.printStackTrace();
         return;
         }
   } 
  
  public static void ainsertActionExecutionLog(String actionID, String payload, String sensorID, String actorID)
  {
   try
      {
	   
	  UUID entryID = UUID.randomUUID();
	  	   
      Statement myInsert = conn.createStatement();
      
      
      myInsert.executeUpdate("INSERT INTO VLOGGER.ACTIONEXECUTEDLOG VALUES ('"+entryID.toString()+"', '"+actionID+"', '"+sensorID+"', '"+actorID+"', '"+payload+"', STATEMENT_TIMESTAMP())");
      myInsert.close();
      
      } catch (SQLException e)
         {
         System.err.println("Could not connect to the database.\n");
         e.printStackTrace();
         return;
         }
   } 
    
}  