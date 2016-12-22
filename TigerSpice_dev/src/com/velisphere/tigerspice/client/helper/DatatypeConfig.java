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
