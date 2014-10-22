package com.velisphere.tigerspice.server;

import gui.ava.html.image.generator.HtmlImageGenerator;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.commons.codec.binary.Base64;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.TableRowData;

public class AnalyticsServiceImpl extends RemoteServiceServlet implements
		AnalyticsService {

	@Override
	public LinkedList<AnalyticsRawData> getEndpointLog(String endpointID,
			String propertyID) {

		Connection conn;
		LinkedList<AnalyticsRawData> logData = new LinkedList<AnalyticsRawData>();

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
				AnalyticsRawData logItem = new AnalyticsRawData();
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

		return logData;
	}

	@Override
	public String getEndpointLogAsFile(LinkedList<TableRowData> table)
			throws IOException {

		ServletContext app = this.getServletContext();

		File tmpDir = (File) app.getAttribute("javax.servlet.context.tempdir");
		File tempFile = new File(tmpDir.getAbsolutePath(), UUID.randomUUID()
				.toString() + ".csv");

		CSVWriter writer = new CSVWriter(new FileWriter(tempFile));

		Iterator<TableRowData> it = table.iterator();

		while (it.hasNext()) {
			TableRowData row = it.next();
			writer.writeNext(row.getCols());

		}

		writer.flush();
		writer.close();

		System.out.println("[IN] Creating temporary file at "
				+ tempFile.getAbsolutePath());
		return tempFile.getAbsolutePath();
	}
	
	@Override
	public String getEndpointLogChartAsFile(String imageData) {

		ServletContext app = this.getServletContext();
	
		
		File tmpDir = (File) app.getAttribute("javax.servlet.context.tempdir");
		File tempFile = new File(tmpDir.getAbsolutePath(), UUID.randomUUID()
				.toString() + ".png");


		String encodingPrefix = "base64,";
		int contentStartIndex = imageData.indexOf(encodingPrefix) + encodingPrefix.length();
		
		byte[] imageDataArray = Base64.decodeBase64(imageData.substring(contentStartIndex).getBytes());
		System.out.println(String.valueOf(imageDataArray));
		
		
		
		BufferedImage inputImage;
		try {
			inputImage = ImageIO.read(new ByteArrayInputStream(imageDataArray));
			ImageIO.write(inputImage, "png", tempFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		System.out.println("[IN] Creating temporary file at "
				+ tempFile.getAbsolutePath());
		return tempFile.getAbsolutePath();
	}


	@Override
	public LinkedList<AnalyticsRawData> getActionExecutedLog(String endpointID,
			String propertyID) {

		Connection conn;
		LinkedList<AnalyticsRawData> logData = new LinkedList<AnalyticsRawData>();

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
					.executeQuery("SELECT propertyname, actionID, sensorID, payload, time_stamp FROM vlogger.actionexecutedlog "
							+ "JOIN vlogger.property ON vlogger.actionexecutedlog.propertyid = vlogger.property.propertyid WHERE "
							+ "vlogger.actionexecutedlog.PROPERTYID = '"
							+ propertyID
							+ "' AND vlogger.actionexecutedlog.actorid = '"
							+ endpointID
							+ "' ORDER BY TIME_STAMP");

			while (myResult.next()) {
				AnalyticsRawData logItem = new AnalyticsRawData();
				logItem.addPropertyValuePair(myResult.getString(1), myResult.getString(4));
				logItem.addPropertyValuePair("Action ID", myResult.getString(2));
				logItem.addPropertyValuePair("Triggered by Sensor", myResult.getString(3));
				logItem.setTimeStamp(myResult.getString(5));
				logData.add(logItem);
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return logData;
	}

	public String getLastEndpointLogTime(String endpointID) {

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

	public String getEndpointLogCount(String endpointID) {

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


	public String getActionLogCount(String endpointID) {

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


	
	@Override
	public AnalyticsRawData getCurrentSensorState(String endpointID,
			String propertyID) {

		Connection conn;
		AnalyticsRawData logItem = new AnalyticsRawData();

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
				
				logItem.addPropertyValuePair(propertyID, myResult.getString(1));
				logItem.setTimeStamp(myResult.getString(2));
				
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return logItem;
	}
	
	@Override
	public AnalyticsRawData getCurrentActorState(String endpointID,
			String propertyID) {

		Connection conn;
		AnalyticsRawData logItem = new AnalyticsRawData();

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
				
				logItem.addPropertyValuePair(propertyID, myResult.getString(3));
				logItem.setTimeStamp(myResult.getString(4));
				logItem.setSource(myResult.getString(2));
				logItem.setProcessedByID(myResult.getString(1));
				
				// System.out.println("Retrieved: " + logItem.getValue());
			}

			mySelect.close();

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();

		}

		return logItem;
	}
	
	@Override
	public String getActionNameForActionID(String actionID) {

		Connection conn;
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
							+ actionID 
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


	
}
