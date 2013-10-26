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

public class SphereData implements IsSerializable, Comparable <SphereData>
{
	public String sphereId;
	public String sphereName;
	public Integer sphereIsPublic;
	
	@Override
	public int compareTo(SphereData arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String getId(){
		return sphereId;
	}
	
	public String getName(){
		return sphereName;
	}
	
	public Integer getIsPublic(){
		return sphereIsPublic;
	}
}