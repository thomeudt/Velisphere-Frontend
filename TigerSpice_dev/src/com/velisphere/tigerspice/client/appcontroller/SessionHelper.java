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
package com.velisphere.tigerspice.client.appcontroller;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.client.Login;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.shared.UserData;

// This class contains helper methods for session handling, like retrieving user data based on current session id

public class SessionHelper {

	private static String userID;
	private static String userName;

	public static void validateCurrentSession() {
		String sessionID = Cookies.getCookie("sid");
		System.out.println("[IN] Session validation started. Current Session ID (sid cookie): " + sessionID);
		

		if (sessionID == null) {
			
			System.out.println("[IN] Session cookie not found. Redirect to login page.");
			userID = null;
			Login loginScreen = new Login();
			loginScreen.onModuleLoad();

		} else {

			LoginService.Util.getInstance().loginFromSessionServer(
					new AsyncCallback<UserData>() {
						@Override
						public void onFailure(Throwable caught) {
							System.out.println("[IN] Session validation request to server failed. Redirect to login page. Error: " + caught);
							Login loginScreen = new Login();
							loginScreen.onModuleLoad();
						}

						@Override
						public void onSuccess(UserData result) {
							if (result == null) {
								System.out.println("[IN] Server declined session cookie, invalid or expired. Redirect to login page.");
								userID = null;
								Login loginScreen = new Login();
								loginScreen.onModuleLoad();
																

							} else {
								if (result.getLoggedIn()) {
									userName = result.userName;
									userID = result.userID;
									System.out.println("[IN] Session validation ok.");
									if (userID != null) EventUtils.RESETTABLE_EVENT_BUS.fireEvent(new SessionVerifiedEvent());
									

								} else {
									System.out.println("[IN] Server declined session cookie, invalid or expired. Redirect to login page.");
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
		SessionHelper.validateCurrentSession();
		return userName;
	}

	public static String getCurrentUserID() {
		SessionHelper.validateCurrentSession();
		return userID;
	}

}
