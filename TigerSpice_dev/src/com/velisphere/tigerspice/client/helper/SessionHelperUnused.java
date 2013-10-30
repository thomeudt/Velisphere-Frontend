package com.velisphere.tigerspice.client.helper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.shared.UserData;

// This class contains helper methods for session handling, like retrieving user data based on current session id

public class SessionHelperUnused {

	private String userID;
	
	
	public String getUserID(String sessionID)
	{
		System.out.println("SID:" + sessionID);
		LoginService.Util.getInstance().loginFromSessionServer(new AsyncCallback<UserData>()
	    {	        
			
			@Override
	        public void onFailure(Throwable caught)
	        {
				userID = null;
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
	                	
	                	userID = result.userID;
	                	System.out.println("ROTTE: " + userID);
	                	
	                	         
	                		                   
	                } else
	                {
	                	userID = null;
	                	
	         
	                }
	            }
	        }
	 
	    });
		
		
		System.out.println("ROTT: " + userID);
		return userID;
		
		
	
		
	}

	
	
}
