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

import com.google.gwt.user.client.rpc.IsSerializable;

public class UnprovisionedEndpointData implements IsSerializable
{
	public String uepid;
	public String identifier;
	public String endpointclassId;
	public String time_stamp;
	public String endpointclassName;
	public String secondsSinceConnection;

	
	
	public String getUepid(){
		return uepid;
	}
	
	public String getSecondsSinceConnection(){
		return secondsSinceConnection;
	}
	
	
	public String getIdentifier(){
		return identifier;
	}
	
	public String getEpcId(){
		return endpointclassId;
	}
	
	public String getTimestamp(){
		return time_stamp;
	}
	
	public String getEndpointClassName(){
		return endpointclassName;
	}
	
	
	public void setUepid(String uepid){
		this.uepid = uepid;
	}
	
	public void setIdentifier(String identifier){
		this.identifier = identifier;
	}
	
	public void  setEpcId(String endpointclassId){
		this.endpointclassId = endpointclassId;
	}
	
	public void  setEndpointClassName(String endpointclassName){
		this.endpointclassName = endpointclassName;
	}
	
	public void  setTimestamp(String time_stamp){
		this.time_stamp = time_stamp;
	}
	
	public void  setSecondsSinceConnection(String secondsSinceConnection){
		this.secondsSinceConnection = secondsSinceConnection;
	}
}
