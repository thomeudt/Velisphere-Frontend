/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
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


import java.util.HashSet;






import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.UserData;

@RemoteServiceRelativePath("voltEndpoint")
public interface EndpointService extends RemoteService {
	Vector<EndpointData> getAllEndpointDetails();
	Vector<EndpointData> getEndpointsForSphere(String sphereID);
	String addEndpointToSphere(String endpointID, String sphereID);
	String removeEndpointFromSphere(String endpointID, String sphereID);
	Vector<EndpointData> getEndpointsForUser(String userID);
	EndpointData getEndpointForEndpointID(String endpointID);
	String updateEndpointNameForEndpointID(String endpointID, String endpointName);
}


