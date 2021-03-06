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


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.UserData;

public interface LoginServiceAsync {
	
	void loginServer(String name, String password, AsyncCallback<UserData> callback);
	void loginFromSessionServer (AsyncCallback<UserData> callback);
	void changePassword(String id, String oldPassword, String newPassword, AsyncCallback<Boolean> callback);
	void closeAccount(String id, AsyncCallback<Boolean> callback);
	void logout(AsyncCallback<Void> callback);	
}
