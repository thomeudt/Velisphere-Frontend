package com.velisphere.montana.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.voltdb.*;
import org.voltdb.client.*;
import org.voltdb.types.TimestampType;

public class ShovelExportToMYSQL {

	private static Connection conn;

	public static void main(String[] args) throws Exception {

		/*
		 * Instantiate a client and connect to the database.
		 */

		// VoltDB

		System.out.println("Connecting to VoltDB...");

		org.voltdb.client.Client voltShovel;
		voltShovel = ClientFactory.createClient();

		voltShovel.createConnection("127.0.0.1");

		// MySQL

		connect();

		Statement myInsert = conn.createStatement();

		
		myInsert.addBatch("DELETE FROM ACTION");
		myInsert.addBatch("DELETE FROM ALERT");
		myInsert.addBatch("DELETE FROM CHECKPATH");
		myInsert.addBatch("DELETE FROM CHECKPATH_CHECK_LINK");
		myInsert.addBatch("DELETE FROM CHECKPATH_MULTICHECK_LINK");
		myInsert.addBatch("DELETE FROM CHECKSTATE");
		myInsert.addBatch("DELETE FROM CHECKTABLE");
		myInsert.addBatch("DELETE FROM ENDPOINT");
		myInsert.addBatch("DELETE FROM ENDPOINTCLASS");
		myInsert.addBatch("DELETE FROM ENDPOINTPROPERTYLOG");
		myInsert.addBatch("DELETE FROM FAVORITES");
		myInsert.addBatch("DELETE FROM ITEMCOST");
		myInsert.addBatch("DELETE FROM ENDPOINT_SPHERE_LINK");
		myInsert.addBatch("DELETE FROM ENDPOINT_USER_LINK");
		myInsert.addBatch("DELETE FROM LOGICTEMPLATE");
		myInsert.addBatch("DELETE FROM LOGICTEMPLATE_ENDPOINTCLASS_LINK");
		myInsert.addBatch("DELETE FROM MULTICHECK");
		myInsert.addBatch("DELETE FROM MULTICHECK_CHECK_LINK");
		myInsert.addBatch("DELETE FROM MULTICHECK_MULTICHECK_LINK");
		myInsert.addBatch("DELETE FROM OUTBOUNDPROPERTYACTION");
		myInsert.addBatch("DELETE FROM PLAN");
		myInsert.addBatch("DELETE FROM PROPERTY");
		myInsert.addBatch("DELETE FROM PROPERTYCLASS");
		myInsert.addBatch("DELETE FROM SPHERE");
		myInsert.addBatch("DELETE FROM SPHERE_USER_LINK");
		myInsert.addBatch("DELETE FROM UNPROVISIONED_ENDPOINT");
		myInsert.addBatch("DELETE FROM USER");
		myInsert.addBatch("DELETE FROM VENDOR");

		myInsert.executeBatch();
		
		
		
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM USER;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO USER VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "', '"
							+ result.getString(4) + "', '"
							+ result.getString(5) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM ENDPOINT_USER_LINK;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO ENDPOINT_USER_LINK VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM ENDPOINT_SPHERE_LINK;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO ENDPOINT_SPHERE_LINK VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM SPHERE_USER_LINK;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO SPHERE_USER_LINK VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM SPHERE;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO SPHERE VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getLong(2) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM ENDPOINTCLASS;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO ENDPOINTCLASS VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM ENDPOINT;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO ENDPOINT VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getTimestampAsSqlTimestamp(3) + "', '"
							+ result.getString(4) + "', '"
							+ result.getString(5) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM PROPERTY;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO PROPERTY VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "', '"
							+ result.getLong(4) + "', '"
							+ result.getLong(5) + "', '"
							+ result.getLong(6) + "', '"
							+ result.getLong(7) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM PROPERTYCLASS;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO PROPERTYCLASS VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM CHECK;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO CHECKTABLE VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "', '"
							+ result.getString(4) + "', '"
							
							+ result.getLong(5) + "', '"
							+ result.getLong(6) + "', '"
							+ result.getString(7) + "', '"
							+ result.getString(8) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM CHECKSTATE;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO CHECKSTATE VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getLong(1) + "', '"
							+ result.getString(2) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM MULTICHECK;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO MULTICHECK VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getLong(2) + "', '"
							+ result.getLong(3) + "', '"
							+ result.getString(4) + "', '"
							+ result.getString(5) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM MULTICHECK_CHECK_LINK;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO MULTICHECK_CHECK_LINK VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM MULTICHECK_MULTICHECK_LINK;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO MULTICHECK_MULTICHECK_LINK VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM CHECKPATH;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO CHECKPATH VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM CHECKPATH_CHECK_LINK;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO CHECKPATH_CHECK_LINK VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM CHECKPATH_MULTICHECK_LINK;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO CHECKPATH_MULTICHECK_LINK VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM ACTION;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO ACTION VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "', '"
							+ result.getLong(4) + "', '"
							+ result.getString(5) + "', '"
							+ result.getString(6) + "', '"
							+ result.getString(7) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM OUTBOUNDPROPERTYACTION;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO OUTBOUNDPROPERTYACTION VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "', '"
							+ result.getString(4) + "', '"
							+ result.getString(5) + "', '"
							+ result.getString(6) + "', '"
							+ result.getString(7) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM ITEMCOST;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO ITEMCOST VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getDouble(3) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM PLAN;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO PLAN VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM UNPROVISIONED_ENDPOINT;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO UNPROVISIONED_ENDPOINT VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getTimestampAsSqlTimestamp(3) + "', '"
							+ result.getString(4) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM VENDOR;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO VENDOR VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM ALERT;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO ALERT VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "', '"
							+ result.getString(4) + "', '"
							+ result.getString(5) + "', '"
							+ result.getString(6) + "', '"
							+ result.getString(7) + "', '"
							+ result.getString(8) + "', '"
							+ result.getString(9) + "', '"
							+ result.getString(10) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

					myInsert.executeBatch();

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM LOGICTEMPLATE;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO LOGICTEMPLATE VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM LOGICTEMPLATE_ENDPOINTCLASS_LINK;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO LOGICTEMPLATE_ENDPOINTCLASS_LINK VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {

			System.out.println("Submitting Query to VoltDB...");

			VoltTable[] results = voltShovel.callProcedure("@AdHoc",
					"SELECT * FROM FAVORITES;").getResults();

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid

					System.out.println("Adding batch insert to MySQL...");

					String insertString = "INSERT INTO FAVORITES VALUES ('"
							+ result.getString(0) + "', '"
							+ result.getString(1) + "', '"
							+ result.getString(2) + "', '"
							+ result.getString(3) + "', '"
							+ result.getString(4) + "');";

					System.out.println(insertString);

					myInsert.addBatch(insertString);

				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		
		
		System.out.println("Executing MySQL Batch...");

		myInsert.executeBatch();

		System.out.println("Closing MySQL...");

		myInsert.close();

		System.out.println("Closing VoltDB...");

		voltShovel.close();

		System.out.println("Done!");

		/*
		 * Retrieve the message.
		 */

	}

	/* This example connects to Vertica and issues a simple select */

	public static void connect() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = null;
		} catch (ClassNotFoundException e) {
			System.err.println("Could not find the JDBC driver class.\n");
			e.printStackTrace();
			return;
		}
		try {

			System.out.println("Connecting to mySQL...");

			conn = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/montana_backup", "shovel",
					"lorenz");

			conn.setAutoCommit(true);

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();
			return;
		}
	}

}
