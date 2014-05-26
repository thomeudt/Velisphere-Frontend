package com.velisphere.chai;

import java.sql.*;
import java.util.Calendar;
import java.util.TimeZone;

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
  
  
  public static void insertLog(String entryID, String endpointID, String propertyID, String entry)
  {
   try
      {
      Statement myInsert = conn.createStatement();
      
      /**
      PreparedStatement pstmt = conn.prepareStatement(
              "INSERT INTO VLOGGER.ENDPOINTPROPERTYLOG (ENTRYID, ENDPOINTID, PROPERTYID, PROPERTYENTRY, TIME_STAMP)" +
              " VALUES(?,?,?,?,?)");
      pstmt.setString(1, entryID);
      pstmt.setString(2, endpointID);
      pstmt.setString(3, propertyID);
      pstmt.setString(4, entry);
      pstmt.setTimestamp(5, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()), Calendar.getInstance(TimeZone.getTimeZone("UTC")));
      
      pstmt.addBatch();
      
      try {
          // Batch is ready, execute it to insert the data
          pstmt.executeBatch();
      } catch (SQLException e) {
          System.out.println("Error message: " + e.getMessage());
          return; // Exit if there was an error
      }

      
      // Commit the transaction to close the COPY command
      conn.commit();
      
      **/
      
      
      myInsert.executeUpdate("INSERT INTO VLOGGER.ENDPOINTPROPERTYLOG VALUES ('"+entryID+"', '"+endpointID+"', '"+propertyID+"', '"+entry+"', STATEMENT_TIMESTAMP())");
      myInsert.close();
      
      } catch (SQLException e)
         {
         System.err.println("Could not connect to the database.\n");
         e.printStackTrace();
         return;
         }
   } 
  
  
  
  
} // end of class 