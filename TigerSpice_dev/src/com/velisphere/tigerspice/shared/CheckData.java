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
package com.velisphere.tigerspice.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CheckData implements IsSerializable, Comparable <CheckData>
{
	
	public String checkId;
	public String endpointId;
	public String propertyId;
	public String checkValue;
	public String operator;
	public Byte state;
	public Byte expired;
	public String checkName;
	
	@Override
	public int compareTo(CheckData arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String getCheckId(){
		return checkId;
	}
	
	public String getEndpointId(){
		return endpointId;
	}
	
	public String getName(){
		return checkName;
	}

	public String getPropertyId(){
		return propertyId;
	}
	
	public String getOperator(){
		return operator;
	}
	
	public String getCheckValue(){
		return checkValue;
	}
	
	public Byte getState(){
		return state;
	}
	
	public Byte getExpired(){
		return expired;
	}
	
	
	
	
	public void setName(String checkName){
		this.checkName = checkName;
	}
		
	
}
