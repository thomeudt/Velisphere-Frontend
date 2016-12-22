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

public class EndpointData implements IsSerializable
{
	public String endpointId;
	public String endpointName;
	public String endpointclassId;
	public String endpointProvDate;
	public String endpointState;
	
	
	public String getEndpointState() {
		return endpointState;
	}

	public void setEndpointState(String endpointState) {
		this.endpointState = endpointState;
	}

	public String getId(){
		return endpointId;
	}
	
	public String getName(){
		return endpointName;
	}
	
	public String getEpcId(){
		return endpointclassId;
	}
	
	public String getEndpointProvDate(){
		return endpointProvDate;
	}
	
	public void setId(String endpointID){
		this.endpointId = endpointID;
	}
	
	public void setName(String endpointName){
		this.endpointName = endpointName;
	}
	
	public void  setEpcId(String endpointclassId){
		this.endpointclassId = endpointclassId;
	}
	
	public void  setEndpointProvDate(String endpointProvDate){
		this.endpointProvDate = endpointProvDate;
	}
}
