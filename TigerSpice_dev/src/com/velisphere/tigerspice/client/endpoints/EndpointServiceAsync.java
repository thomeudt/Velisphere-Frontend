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
package com.velisphere.tigerspice.client.endpoints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.LogicLinkTargetData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.UnprovisionedEndpointData;
import com.velisphere.tigerspice.shared.UserData;

public interface EndpointServiceAsync {
	void getAllEndpointDetails(AsyncCallback<Vector<EndpointData>> callback);
	void getEndpointsForSphere(String sphereID, AsyncCallback<LinkedList<EndpointData>> callback);
	void addEndpointToSphere(String endpointID, String sphereID, AsyncCallback<String> callback);
	void getEndpointsForMultipleIDs(LinkedList<String> endpointIDs, AsyncCallback<LinkedList<EndpointData>> callback);
	void removeEndpointFromSphere(String endpointID, String sphereID, AsyncCallback<String> callback);
	void getEndpointsForUser(String userID, AsyncCallback<LinkedList<EndpointData>> callback);
	void getEndpointForEndpointID(String endpointID, AsyncCallback<EndpointData> callback);
	void updateEndpointNameForEndpointID(String endpointID, String endpointName, AsyncCallback<String> callback);
	void getUnprovisionedEndpoints(String endpointID, String captchaWord, AsyncCallback<UnprovisionedEndpointData> callback);
	void addNewEndpoint(String endpointID, String endpointName, String endpointclassID, String userID, AsyncCallback<String> callback);
	void getLinksForEndpointList(LinkedList<String> endpointID, AsyncCallback<HashMap<String, LinkedList<LogicLinkTargetData>>> callback); 
}
