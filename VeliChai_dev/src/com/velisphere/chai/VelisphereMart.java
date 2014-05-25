package com.velisphere.chai;

import java.sql.*;

public class VelisphereMart {




/* This example connects to Vertica and issues a simple select */



  public static void query()
     {
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
         // So far so good, lets issue a query...
         Statement mySelect = conn.createStatement();
         mySelect.executeUpdate("INSERT INTO VLOGGER.ENDPOINTPROPERTYLOG VALUES ('A', 'B', 'C', 'D', NULL)");
         ResultSet myResult = mySelect.executeQuery
        		   //("INSERT INTO VLOGGER.ENDPOINTPROPERTYLOG VALUES ('A', 'B', 'C', 'D', NULL)");
                   ("SELECT * FROM VLOGGER.ENDPOINTPROPERTYLOG");
        		 

         while (myResult.next())
            {
            System.out.println(myResult.getString(1));
            }
         mySelect.close();
         conn.close();
         } catch (SQLException e)
            {
            System.err.println("Could not connect to the database.\n");
            e.printStackTrace();
            return;
            }
      } // end of main method
} // end of class 