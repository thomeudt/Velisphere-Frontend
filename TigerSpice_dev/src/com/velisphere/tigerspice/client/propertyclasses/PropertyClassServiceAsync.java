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
package com.velisphere.tigerspice.client.propertyclasses;

import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyClassData;

public interface PropertyClassServiceAsync {
	
	void getPropertyClassForPropertyClassID(String propertyClassID, AsyncCallback<PropertyClassData> callback);
	void addPropertyClass(String propertyClassName, String propertyClassDataType, String propertyClassUnit, AsyncCallback<String> callback);
	void getAllPropertyClassDetails(AsyncCallback<LinkedList<PropertyClassData>> callback);
	void updatePropertyClass(String pcID, String pcName, String pcDataType, String pcUnit, AsyncCallback<String> callback);
}
