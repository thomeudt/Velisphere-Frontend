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
import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.BCrypt;
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.UserData;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService
{
   
 
    @Override
    public UserData loginServer(String name, String password)
    {
                //validate username and password
                 
                //store the user/session id
    	
    	UserData user = new UserData();

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

			final ClientResponse findUser = voltCon.montanaClient
					.callProcedure("UI_FindUserForEmail", name);

			final VoltTable findUserResults[] = findUser.getResults();

			VoltTable result = findUserResults[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					user.userEmail = result.getString("USEREMAIL");
					user.userID = result.getString("USERID");
					user.userName = result.getString("USERNAME");
					user.userPassword = result.getString("USERPWHASH");
					UUID uSID = UUID.randomUUID();
					user.sessionID = uSID.toString();  
					
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

		boolean pwValid = BCrypt.checkpw(password, user.userPassword);
		    	
    	if (pwValid == true)
    		{
    		storeUserInSession(user);
    		user.setLoggedIn(true);
    		}
 
        return user;
    }
 
    @Override
    public UserData loginFromSessionServer()
    {
        return getUserAlreadyFromSession();
    }
 
    @Override
    public void logout()
    {
        deleteUserFromSession();
    }
 
    @Override
    public boolean changePassword(String name, String newPassword)
    {
		return false;
        // change password logic
    }
 
    private UserData getUserAlreadyFromSession()
    {
        UserData user = null;
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession();
        Object userObj = session.getAttribute("user");
             
        if (userObj != null && userObj instanceof UserData)
        {
            user = (UserData) userObj;
        }
        return user;
    }
 
    private void storeUserInSession(UserData user)
    {
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("user", user);
    }
 
    private void deleteUserFromSession()
    {
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession();
        session.removeAttribute("user");
    }
    
   
 
}
