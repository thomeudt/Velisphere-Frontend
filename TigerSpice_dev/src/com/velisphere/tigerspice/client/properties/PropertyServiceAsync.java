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
package com.velisphere.tigerspice.client.properties;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.UnusedFolderPropertyData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.UserData;

public interface PropertyServiceAsync {
	void getAllPropertyDetails(AsyncCallback<Vector<PropertyData>> callback);
	void getPropertiesForEndpointClass(String endpointClassID, AsyncCallback<List<PropertyData>> callback);
	void getValueForEndpointProperty(String endpointID, String propertyID, AsyncCallback<String> callback);
	void getPropertyClass(String propertyID, AsyncCallback<String> callback);
	void getPropertyName(String propertyID, AsyncCallback<String> callback);
	void getActorPropertiesForEndpointID(String endpointID, AsyncCallback<LinkedList<PropertyData>> callback);
	
}
