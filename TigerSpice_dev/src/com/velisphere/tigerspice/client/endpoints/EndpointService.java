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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.LogicLinkTargetData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.UnprovisionedEndpointData;
import com.velisphere.tigerspice.shared.UserData;

@RemoteServiceRelativePath("voltEndpoint")
public interface EndpointService extends RemoteService {
	LinkedList<EndpointData> getAllEndpointDetails();
	LinkedList<EndpointData> getEndpointsForSphere(String sphereID);
	LinkedList<EndpointData> getEndpointsForMultipleIDs(LinkedList<String> endpointIDs);
	String addEndpointToSphere(String endpointID, String sphereID);
	String removeEndpointFromSphere(String endpointID, String sphereID);
	LinkedList<EndpointData> getEndpointsForUser(String userID);
	EndpointData getEndpointForEndpointID(String endpointID);
	String updateEndpointNameForEndpointID(String endpointID, String endpointName);
	UnprovisionedEndpointData getUnprovisionedEndpoints(String endpointID, String captchaWord);
	String addNewEndpoint(String endpointID, String endpointName, String endpointclassID, String userID);
	HashMap<String, LinkedList<LogicLinkTargetData>> getLinksForEndpointList(LinkedList<String> endpointID);
	String addNewAlert(String endpointID, String checkpathID, String alertName,
			String userID);
}


