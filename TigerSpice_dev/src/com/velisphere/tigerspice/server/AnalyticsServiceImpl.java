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
				logItem.addPropertyValuePair(myResult.getString(1), Double.parseDouble(myResult.getString(2)));
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


}
