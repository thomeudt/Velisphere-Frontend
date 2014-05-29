package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EndpointLogData implements IsSerializable {

	String propertyID;
	String value;
	String timeStamp;
	
	public void setPropertyID(String propertyID){
		this.propertyID = propertyID;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public void setTimeStamp(String timeStamp){
		this.timeStamp = timeStamp;
	}
	
	public String getPropertyID(){
		return this.propertyID;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public String getTimeStamp(){
		return this.timeStamp;
	}
		
	
}
