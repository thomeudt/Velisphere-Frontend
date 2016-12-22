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
package com.velisphere.tigerspice.client.endpointclasses;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.UserData;

public interface EPCServiceAsync {
	void getAllEndpointClassDetails(AsyncCallback<LinkedList<EPCData>> callback);
	void getEndpointClassForEndpointClassID(String endpointClassID, AsyncCallback<EPCData> callback);
	void addEndpointClass(String epcName, String epcImageURL, String vendorID, AsyncCallback<String> callback);
	void updateEndpointClass(String epcID, String epcName, String epcImageURL, String vendorID, AsyncCallback<String> callback);
	
}
