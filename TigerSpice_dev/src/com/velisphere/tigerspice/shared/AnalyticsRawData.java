package com.velisphere.tigerspice.shared;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AnalyticsRawData implements IsSerializable {

	HashMap<String, Double> propertyValuePairs = new HashMap<String, Double>();
	
	String timeStamp;
	
	public void addPropertyValuePair(String propertyID, Double value){
		this.propertyValuePairs.put(propertyID, value);
	}
		
	public void setTimeStamp(String timeStamp){
		this.timeStamp = timeStamp;
	}
	
	public HashMap<String, Double> getPropertyValuePairs(){
		return this.propertyValuePairs;
	}
		
	
	public String getTimeStamp(){
		return this.timeStamp;
	}
		
	
}
