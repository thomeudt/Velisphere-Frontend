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
package com.velisphere.tigerspice.client.helper;

import com.google.gwt.user.client.ui.Anchor;

public class DynamicAnchor extends Anchor {

	String stringQueryFirst;
	String stringQuerySecond;
	String stringQueryThird;
	Integer intQueryFirst;
	Integer intQuerySecond;
	Integer intQueryThird;
	
	public DynamicAnchor(String text, boolean asHtml, String stringQueryFirst)
	{
		super(text, asHtml); 
		this.stringQueryFirst = stringQueryFirst;
		
	}
	
	public String getStringQueryFirst(){
		return stringQueryFirst;
	}
}
