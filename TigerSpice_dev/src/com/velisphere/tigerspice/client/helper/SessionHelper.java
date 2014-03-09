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
package com.velisphere.tigerspice.client.helper;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.client.Login;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.shared.UserData;

// This class contains helper methods for session handling, like retrieving user data based on current session id

public class SessionHelper {

	private static String userID;
	private static String userName;

	public static void validateCurrentSession() {
		String sessionID = Cookies.getCookie("sid");
		System.out.println("SID: " + sessionID);
		

		if (sessionID == null) {
			userID = null;
			Login loginScreen = new Login();
			loginScreen.onModuleLoad();

		} else {

			LoginService.Util.getInstance().loginFromSessionServer(
					new AsyncCallback<UserData>() {
						@Override
						public void onFailure(Throwable caught) {
							Login loginScreen = new Login();
							loginScreen.onModuleLoad();
						}

						@Override
						public void onSuccess(UserData result) {
							if (result == null) {
								userID = null;
								Login loginScreen = new Login();
								loginScreen.onModuleLoad();
																

							} else {
								if (result.getLoggedIn()) {
									userName = result.userName;
									userID = result.userID;
									
									if (userID != null) EventUtils.EVENT_BUS.fireEvent(new SessionVerifiedEvent());
									

								} else {
									userID = null;
									Login loginScreen = new Login();
									loginScreen.onModuleLoad();
								}
							}
						}

					});
		}


	}

	public static String getCurrentUserName() {
		return userName;
	}

	public static String getCurrentUserID() {
		return userID;
	}

}
