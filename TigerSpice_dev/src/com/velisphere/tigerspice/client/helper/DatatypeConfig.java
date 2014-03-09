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

public class DatatypeConfig {
	
	// this class contains configuration data for various datatypes used in the velisphere database for property classes
	// It is needed to configure proper logic operations in checks

	HashSet<String> textOperators = new HashSet<String>();
	HashSet<String> numberOperators = new HashSet<String>();
	HashSet<String> booleanOperators = new HashSet<String>();
	
	public DatatypeConfig() {
		textOperators.add("=");
		textOperators.add("!=");
		numberOperators.add("=");
		numberOperators.add("!=");
		numberOperators.add("<");
		numberOperators.add("<=");
		numberOperators.add(">");
		numberOperators.add(">=");
		booleanOperators.add("=");
		booleanOperators.add("!=");
	}
	
	public HashSet<String> getTextOperators() {
		return this.textOperators;
	}
	
	public HashSet<String> getNumberOperators() {
		return this.numberOperators;
	}
	
	public HashSet<String> getBooleanOperators() {
		return this.booleanOperators;
	}
}
