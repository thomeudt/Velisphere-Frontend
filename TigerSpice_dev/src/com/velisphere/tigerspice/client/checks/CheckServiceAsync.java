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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.ActionObject;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.UserData;

public interface CheckServiceAsync {
	void getChecksForEndpointID(String endpointID, AsyncCallback<Vector<CheckData>> callback);
	void addNewCheck(String checkID, String endpointID, String propertyID, String checkValue, String operator, String name, String checkpathID, LinkedList<ActionObject> actions, AsyncCallback<String> callback );
	void updateCheck(String checkID, String name, String checkValue, String operator, String checkpathID, LinkedList<ActionObject> actions, AsyncCallback<String> callback );
	void deleteCheck(String checkID, AsyncCallback<String> callback );
	void getChecksForUserID(String userID, AsyncCallback<Vector<CheckData>> callback);
	void getCheckNameForCheckID(String checkID, AsyncCallback<String> callback);
	void getActionsForCheckID(String checkID, String checkpathID, AsyncCallback<LinkedList<ActionObject>> callback);
	void deleteAllActionsForCheckId(String checkID, AsyncCallback<String> callback );
}
