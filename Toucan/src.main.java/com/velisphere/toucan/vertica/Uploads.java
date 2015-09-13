package com.velisphere.toucan.vertica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.velisphere.toucan.amqp.ServerParameters;

public class Uploads {

	// THIS IS LOGGER-LIKE AND NEEDS TO BE STORED IN VERTICA, NOT IN
	// VOLTDB!!!!!!!!!!!!!!!

	public String uploadFile(String uploadID, String uploadName,
			String fileType, String originalFileName, String endpointID,
			String timestamp) {

		Connection conn = null;

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
					+ ServerParameters.vertica_ip);

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();
		}

		try {
			Statement myInsert = conn.createStatement();

			myInsert.addBatch("INSERT INTO VLOGGER.FILEUPLOAD VALUES ('"
					+ uploadID
					+ "', '"
					+ uploadName
					+ "', '"
					+ originalFileName
					+ "', '"
					+ fileType
					+ "', '" + endpointID + "', '" + timestamp + "')");

			myInsert.executeBatch();
			myInsert.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "OK";
	}

	
	public String[] downloadFile(String uploadID, String endpointID) {

		Connection conn = null;

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
					+ ServerParameters.vertica_ip);

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();
		}

		try {
			Statement mySelect = conn.createStatement();

			ResultSet myResult = mySelect.executeQuery("SELECT NAME, ORIG_NAME FROM VLOGGER.FILEUPLOAD WHERE FILEUPLOADID = '"
					+ uploadID
					+ "' AND ENDPOINTID = '"
					+ endpointID + "'");
			
			myResult.next();
			String fileName = myResult.getString(1);
			String origName = myResult.getString(2);
			
			mySelect.close();
			
			String[] nameArray = new String[2];
			nameArray[0] = fileName;
			nameArray[1] = origName;
			
			return nameArray;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

		
	}

	
}
