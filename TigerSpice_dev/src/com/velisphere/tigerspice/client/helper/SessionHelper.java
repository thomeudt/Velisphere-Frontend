package com.velisphere.tigerspice.client.helper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.shared.UserData;

// This class contains helper methods for session handling, like retrieving user data based on current session id

public class SessionHelper {

	private static String userID;
	private static String userName; 
	
	
	public static String getCurrentUserID()
	{
		
		    LoginService.Util.getInstance().loginFromSessionServer(new AsyncCallback<UserData>()
		    {
		        @Override
		        public void onFailure(Throwable caught)
		        {
	
		        }
		 
		        @Override
		        public void onSuccess(UserData result)
		        {
		            if (result == null)
		            {
		            	 userID = null;
		            	
		            } else
		            {
		                if (result.getLoggedIn())
		                {
		                	userName = result.userName;
		                	userID = result.userID;
		                		                   
		                } else
		                {
		                	userID = null;
		                }
		            }
		        }
		 
		    });
			
		
		return userID;
				
	}

	public static String getCurrentUserName()
	{
		return userName;
	}
	
	
}
