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
    Boolean changePassword(String id, String oldPassword, String newPassword);
    Boolean closeAccount(String id);
    void logout();
}
