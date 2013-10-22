package com.velisphere.tigerspice.client.users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.UserData;

@RemoteServiceRelativePath("voltLogin")
public interface LoginService extends RemoteService
{
    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util
    {
        private static LoginServiceAsync instance;
 
        public static LoginServiceAsync getInstance()
        {
            if (instance == null)
            {
                instance = GWT.create(LoginService.class);
            }
            return instance;
        }
    }
 
    UserData loginServer(String name, String password);
 
    UserData loginFromSessionServer();
     
    boolean changePassword(String name, String newPassword);
 
    void logout();
}