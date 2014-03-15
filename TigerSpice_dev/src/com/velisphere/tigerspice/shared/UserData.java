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
package com.velisphere.tigerspice.shared;

import java.util.List;
import java.util.UUID;

import org.mindrot.BCrypt;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.rpc.IsSerializable;

public class UserData implements IsSerializable, Comparable <UserData>
{
	public String userID;
	public String userEmail;
	public String userName;
	public String userPassword;
	public String sessionID;
	public Boolean loggedIn = false;
	
	@Override
	public int compareTo(UserData arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String getEmail(){
		return userEmail;
	}
	
	public String getName(){
		return userName;
	}
	
	public String getId(){
		return userID;
	}
	
	public void setUserPwHash(String password){
		this.userPassword = password;
	}

	public String getSessionId() {
		return sessionID;
	}

	public void setSessionId(String sID) {
		this.sessionID = sID;		
	}

	
	public boolean getLoggedIn() {
	
		return loggedIn;
	}

	public void setLoggedIn(Boolean loggedInStatus) {
		
		this.loggedIn = loggedInStatus;
	}

	
}
