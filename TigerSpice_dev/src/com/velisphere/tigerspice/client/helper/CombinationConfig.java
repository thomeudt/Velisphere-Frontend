package com.velisphere.tigerspice.client.helper;

import java.util.HashSet;

public class CombinationConfig {
	
	// this class defines all logic operators used in combination of checks

	HashSet<String> combinations = new HashSet<String>();
	
	
	public CombinationConfig() {
		combinations.add("AND");
		combinations.add("OR");
	}
	
	public HashSet<String> getCombinations() {
		return this.combinations;
	}
	
}
