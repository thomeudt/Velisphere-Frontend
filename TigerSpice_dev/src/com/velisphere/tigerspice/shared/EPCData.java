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

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Image;

public class EPCData implements IsSerializable
{
	public String endpointclassID;
	public String endpointclassName;
	public String endpointclassPath;
	public String endpointclassImageURL;
	public String vendorID;
		
	public String getName(){
		return endpointclassName;
	}
	
	public String getId(){
		return endpointclassID;
	}

	
	public String getVendorId(){
		return vendorID;
	}

	public String getPath(){
		return endpointclassPath;
	}
	
	public String getImageURL(){
		return endpointclassImageURL;
	}
	
	public void setName(String endpointclassName){
		this.endpointclassName = endpointclassName;
	}
	
	public void setPath(String endpointclassPath){
		this.endpointclassPath = endpointclassPath;
	}
	
	public void setImageURL( String endpointclassImage){
		this.endpointclassImageURL = endpointclassImage;
	}
	
	public void setVendorID( String vendorID){
		this.vendorID = vendorID;
	}
}
