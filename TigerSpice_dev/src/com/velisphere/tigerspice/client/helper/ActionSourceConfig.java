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

import java.util.HashSet;

public class ActionSourceConfig {
	
	// this class defines all possible data sources for values set in an action

	HashSet<String> sources = new HashSet<String>();
	
	
	public ActionSourceConfig() {
		sources.add("Manual entry");
		sources.add("List of typical entries");
		sources.add("Incoming value from sensor");
	}
	
	public HashSet<String> getSources() {
		return this.sources;
	}
	
}
