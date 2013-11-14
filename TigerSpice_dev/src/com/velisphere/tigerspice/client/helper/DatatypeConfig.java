package com.velisphere.tigerspice.client.helper;

import java.util.HashSet;

public class DatatypeConfig {

	HashSet<String> textOperators = new HashSet<String>();
	
	public DatatypeConfig() {
		textOperators.add("=");
		textOperators.add("!=");
		
	}
	
	public HashSet<String> getTextOperators() {
		return this.textOperators;
	}
	
}
