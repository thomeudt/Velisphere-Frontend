/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
package com.velisphere.tigerspice.server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.UUID;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;





import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.vendors.VendorService;
import com.velisphere.tigerspice.shared.VendorData;

@SuppressWarnings("serial")
public class VendorServiceImpl extends RemoteServiceServlet implements
		VendorService {

	
		
	
	public LinkedList<VendorData> getAllVendorDetails()

	{
		VoltConnector voltCon = new VoltConnector();

		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		LinkedList<VendorData> allVendors = new LinkedList<VendorData>();
		try {

			final ClientResponse findAllUsers = voltCon.montanaClient
					.callProcedure("UI_SelectAllVendors");

			final VoltTable findAllUsersResults[] = findAllUsers.getResults();

			VoltTable result = findAllUsersResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					System.out.println("GETTING...");
					
					// extract the value in column checkid
					VendorData vendorData = new VendorData();
					vendorData.vendorName = result.getString("VENDORNAME");
					vendorData.vendorID = result.getString("VENDORID");
										
					
					vendorData.vendorImageURL = getServletContext().getContextPath()+"/tigerspiceDownloads?privateURL="+result.getString("VENDORIMAGEURL")
							+ "&outboundFileName=VENDOR_image&persist=1";
					
					
					
					allVendors.add(vendorData);

				}
			}

			// System.out.println(allEndPointClasses);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return allVendors;
	}


	public VendorData getVendorForVendorID(String vendorID)

	{
		VoltConnector voltCon = new VoltConnector();

		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		VendorData vendor = new VendorData();
		try {

			final ClientResponse findVendor = voltCon.montanaClient
					.callProcedure("UI_SelectVendorForVendorID", vendorID);

			final VoltTable findVendorResults[] = findVendor.getResults();

			VoltTable result = findVendorResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					
					vendor.vendorName = result.getString("VENDORNAME");
					vendor.vendorID = result.getString("VENDORID");
					vendor.vendorPath = result.getString("VENDORMAGEURL");
					vendor.vendorImageURL = getServletContext().getContextPath()+"/tigerspiceDownloads?privateURL="+result.getString("VENDORIMAGEURL")
							+ "&outboundFileName=VENDOR_image&persist=1";


				}
			}

			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vendor;
	}

	
	public String addVendor(String vendorName, String vendorImageURL)

	{

			
			// first add to VoltDB

			VoltConnector voltCon = new VoltConnector();
			String vendorID = UUID.randomUUID().toString();

			try {
				voltCon.openDatabase();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {

				voltCon.montanaClient.callProcedure("VENDOR.insert", vendorID,
						vendorName, vendorImageURL);
			} catch (NoConnectionsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ProcCallException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				voltCon.closeDatabase();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			

		return "OK";

	}


	public String updateVendor(String vendorID, String vendorName, String vendorImageURL)

	{

			
			// first update to VoltDB

			VoltConnector voltCon = new VoltConnector();
			

			try {
				voltCon.openDatabase();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {

				voltCon.montanaClient.callProcedure("UI_UpdateVendor", vendorID,
						vendorName, vendorImageURL);
			} catch (NoConnectionsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ProcCallException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				voltCon.closeDatabase();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			


		return "OK";

	}

	





}
