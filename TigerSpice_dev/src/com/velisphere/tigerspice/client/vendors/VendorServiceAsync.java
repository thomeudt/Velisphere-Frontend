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
package com.velisphere.tigerspice.client.vendors;

import java.util.LinkedList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.VendorData;

public interface VendorServiceAsync {
	void getAllVendorDetails(AsyncCallback<LinkedList<VendorData>> callback);
	void getVendorForVendorID(String VendorID, AsyncCallback<VendorData> callback);
	void addVendor(String vendorName, String vendorImageURL, AsyncCallback<String> callback);
	void updateVendor(String vendorID, String vendorName, String vendorImageURL, AsyncCallback<String> callback);
	
}
