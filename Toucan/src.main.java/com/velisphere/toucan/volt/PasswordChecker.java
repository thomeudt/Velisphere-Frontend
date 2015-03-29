package com.velisphere.toucan.volt;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;




import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;


public class PasswordChecker {

	 public UserData loginServer(String name, String password)
	    {
	        //validate username and password
	                 
		 	UserData user = new UserData();
	    	VoltConnector voltCon = new VoltConnector();
	    	
	    	// default response is negative
	    	
	    	user.userID = "0000 ERROR - REQUEST REJECTED";

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

				final ClientResponse findUser = voltCon.montanaClient
						.callProcedure("UI_FindUserForEmail", name);

				final VoltTable findUserResults[] = findUser.getResults();

				VoltTable result = findUserResults[0];
				// check if any rows have been returned

				while (result.advanceRow()) {
					{
						// extract the value in column checkid
						
						
						user.userName = result.getString("USERNAME");
						user.userPassword = result.getString("USERPWHASH");
						boolean pwValid = BCrypt.checkpw(password, user.userPassword);
				    	
				    	if (pwValid == true)
				    		{
				    			user.setUserID(result.getString("USERID"));
				    			user.setApiKey(result.getString("APIKEY"));
				    		} 
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

			
	 
	        return user;
	    }
	
}
