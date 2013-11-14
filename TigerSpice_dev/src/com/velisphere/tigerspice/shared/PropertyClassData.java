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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PropertyClassData implements IsSerializable, Comparable <PropertyData>
{
	public String propertyClassId;
	public String propertyClassName;
	public String propertyClassDatatype;
	public String propertyClassUnit;
	
	@Override
	public int compareTo(PropertyData arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String getId(){
		return propertyClassId;
	}
	
	public String getName(){
		return propertyClassName;
	}
	
	public String getDatatype(){
		return propertyClassDatatype;
	}
	
	public String getUnit(){
		return propertyClassUnit;
	}
	
		
}
