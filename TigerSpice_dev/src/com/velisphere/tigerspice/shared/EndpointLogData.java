package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EndpointLogData implements IsSerializable {

	String propertyID;
	Double value;
	String timeStamp;
	
	public void setPropertyID(String propertyID){
		this.propertyID = propertyID;
	}
	
	public void setValue(Double value){
		this.value = value;
	}
	
	public void setTimeStamp(String timeStamp){
		this.timeStamp = timeStamp;
	}
	
	public String getPropertyID(){
		return this.propertyID;
	}
	
	public Double getValue(){
		return this.value;
	}
	
	public String getTimeStamp(){
		return this.timeStamp;
	}
		
	
}
