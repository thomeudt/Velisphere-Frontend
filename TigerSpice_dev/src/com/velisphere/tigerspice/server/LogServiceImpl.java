/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.LogService;
import com.velisphere.tigerspice.client.users.UserService;
import com.velisphere.tigerspice.shared.LogData;
import com.velisphere.tigerspice.shared.UserData;

public class LogServiceImpl extends RemoteServiceServlet implements
		LogService {
	
	 /** The Constant serialVersionUID. */
	  private static final long serialVersionUID = -8892989521623692797L;

	public HashSet<LogData> getAllLogEntries() 

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
	
		
		HashSet<LogData> allLogEntries = new HashSet<LogData>();
		try {

			final ClientResponse findAllLogEntries = voltCon.montanaClient
					.callProcedure("SelectAllLogEntries");

			final VoltTable findAllUsersResults[] = findAllLogEntries.getResults();

			VoltTable result = findAllUsersResults[0];
			// check if any rows have been returned

			

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					LogData logData = new LogData();
					logData.exchangeName = result.getString("EXCHANGENAME");
					logData.queueName = result.getString("QUEUENAME");
					logData.message = result.getString("MESSAGE");
					logData.identifier = result.getString("IDENTIFIER");
					logData.routingKey = result.getString("ROUTINGKEY");
					allLogEntries.add(logData);
				}
			}

			// System.out.println(allUsers);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			voltCon.closeDatabase();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allLogEntries;
	}


	

}
