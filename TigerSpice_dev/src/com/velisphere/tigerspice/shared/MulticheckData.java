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

public class MulticheckData implements IsSerializable, Comparable <MulticheckData>
{
	
	public String multiCheckId;
	public String operator;
	public Byte state;
	public Byte expired;
	public String multiCheckName;
	
	
	
	public String getMulticheckId(){
		return multiCheckId;
	}
	
		
	public String getName(){
		return multiCheckName;
	}

		
	public String getOperator(){
		return operator;
	}
	
	
	public Byte getState(){
		return state;
	}
	
	public Byte getExpired(){
		return expired;
	}
	
	
	public void setName(String checkName){
		this.multiCheckName = checkName;
	}

	@Override
	public int compareTo(MulticheckData o) {
		// TODO Auto-generated method stub
		return 0;
	}
		
	
}
