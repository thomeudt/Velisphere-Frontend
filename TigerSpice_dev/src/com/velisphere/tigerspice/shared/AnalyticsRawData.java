package com.velisphere.tigerspice.shared;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AnalyticsRawData implements IsSerializable {

	HashMap<String, String> propertyValuePairs = new HashMap<String, String>();
	
	String timeStamp;
	
	public void addPropertyValuePair(String propertyID, String value){
		this.propertyValuePairs.put(propertyID, value);
	}
		
	public void setTimeStamp(String timeStamp){
		this.timeStamp = timeStamp;
	}
	
	public Map<String, String> getPropertyValuePairs(){
		return this.propertyValuePairs;
	}
		
	
	public String getTimeStamp(){
		return this.timeStamp;
	}
		
	
}
