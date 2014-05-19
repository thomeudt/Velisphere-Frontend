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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.mindrot.BCrypt;
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.users.UserService;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.UserData;

public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {







	/**
	 * 
	 */
	private static final long serialVersionUID = 6705704565729998384L;





	public Vector<UserData> getAllUserDetails()

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

		Vector<UserData> allUsers = new Vector<UserData>();
		try {

			final ClientResponse findAllUsers = voltCon.montanaClient
					.callProcedure("SelectAllUsers");

			final VoltTable findAllUsersResults[] = findAllUsers.getResults();

			VoltTable result = findAllUsersResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					UserData userData = new UserData();
					userData.userEmail = result.getString("USEREMAIL");
					userData.userName = result.getString("USERNAME");
					userData.userID = result.getString("USERID");
					userData.loggedIn = false;
					allUsers.add(userData);

				}
			}

			// System.out.println(allUsers);

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
		
		return allUsers;
	}
	

	public String addNewUser(String userName, String eMail, String password)

	{
		
		ConfigHandler cfh = new ConfigHandler();
		cfh.loadParamChangesAsXML();
		
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
			String pwHash =  BCrypt.hashpw(password, BCrypt.gensalt());
			voltCon.montanaClient.callProcedure("USER.insert", UUID.randomUUID().toString(), userName, eMail, pwHash, "PAYPERUSE");
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
