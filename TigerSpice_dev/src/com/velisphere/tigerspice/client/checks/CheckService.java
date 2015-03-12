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
package com.velisphere.tigerspice.client.checks;

import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.ActionObject;
import com.velisphere.tigerspice.shared.CheckDataUNUSED;
import com.velisphere.tigerspice.shared.EndpointData;


@RemoteServiceRelativePath("voltCheck")
public interface CheckService extends RemoteService {
		LinkedList<CheckDataUNUSED> getChecksForEndpointID(String endpointID);
		String addNewCheck(String checkID, String endpointID, String propertyID, String checkValue, String operator, String name, String checkpathID, LinkedList<ActionObject> actions);
		String updateCheck(String checkID, String name, String checkValue, String operator, String checkpathID, LinkedList<ActionObject> actions);
		String deleteCheck(String checkID);
		Vector<CheckDataUNUSED> getChecksForUserID(String userID);
		String getCheckNameForCheckID(String checkID);
		LinkedList<ActionObject> getActionsForCheckID(String checkID, String checkpathID);
		String deleteAllActionsForCheckId(String checkID);
}
