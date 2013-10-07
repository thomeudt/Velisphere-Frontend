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
import com.velisphere.tigerspice.client.UserService;
import com.velisphere.tigerspice.shared.UserData;

public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {
	
	 /** The Constant serialVersionUID. */
	  private static final long serialVersionUID = -8892989521623692797L;

	public HashSet<UserData> getAllUserDetails() 

	{
		HashSet<UserData> allUsers = new HashSet<UserData>();
		try {
			VoltConnector.openDatabase();

			final ClientResponse findAllUsers = VoltConnector.montanaClient
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
					allUsers.add(userData);
				

				}
			}

			System.out.println(allUsers);

			VoltConnector.closeDatabase();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allUsers;
	}

}
