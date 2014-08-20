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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PropertyData implements IsSerializable
{
	public String propertyId;
	public String propertyName;
	public String propertyclassId;
	public String endpointclassId;
	public byte isActor;
	public byte isSensor;
	public byte isConfigurable;
	public byte status;
	
	
	public String getId(){
		return propertyId;
	}
	
	public String getName(){
		return propertyName;
	}
	
	public String getPropertyName(){
		return propertyName;
	}
	
	public String getPropertyId(){
		return propertyName;
	}
	
	public String getPropertyclassId(){
		return propertyclassId;
	}
	
	public String getEpcId(){
		return endpointclassId;
	}
	
	public String getEndpointclassId(){
		return endpointclassId;
	}
	
	public byte getIsConfigurable(){
		return isConfigurable;
	}
	
	public byte getIsActor(){
		return isActor;
	}
	
	public byte getIsSensor(){
		return isSensor;
	}
	
	public byte getStatus(){
		return status;
	}
	 
	
}
